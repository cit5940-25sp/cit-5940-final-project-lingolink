import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * Manages the in-memory mappings between countries and languages for the game (stored in our program).
 * Responsible for loading data from CSV, adding languages with rarity scores,
 * and providing lookup access to Country and Language objects.
 */
public class CountryLanguageManager {
    private final Map<String, Country> countries = new HashMap<>();
    private final Map<String, Language> languages = new HashMap<>();

    /**
     * adds a new country with its associated languages.
     *
     * @param name the name of the country to add
     * @param languageNames varargs of language names spoken in the country
     */
    public void addCountry(String name, String... languageNames) {
        Set<Language> countryLanguages = new HashSet<>();
        for (String langName : languageNames) {
            Language lang = languages.get(langName.toLowerCase());
            if (lang != null) {
                countryLanguages.add(lang);
            }
        }
        Country country = new Country(name, countryLanguages);
        countries.put(name.toLowerCase(), country);
    }

    /**
     * Adds a language and its rarity score to the manager.
     *
     * @param name the name of the language
     * @param rarityScore the calculated rarity score used for scoring in the game
     */
    public void addLanguage(String name, int rarityScore) {
        Language language = new Language(name, rarityScore);
        languages.put(name.toLowerCase(), language);
    }

    /**
     * Looks up a Country by name.
     *
     * @param name the name of the country to look up
     * @return the Country object, or null if not found
     */
    public Country getCountry(String name) {
        return countries.get(name.toLowerCase());
    }

    /**
     * Looks up a Language by name.
     *
     * @param name the name of the language to look up
     * @return the Language object, or null if not found
     */
    public Language getLanguage(String name) {
        return languages.get(name.toLowerCase());
    }

    /**
     * Returns all registered countries.
     *
     * @return an unmodifiable collection of all Country objects
     */
    public Collection<Country> getAllCountries() {
        return Collections.unmodifiableCollection(countries.values());
    }

    /**
     * Returns all registered languages.
     *
     * @return an unmodifiable collection of all Language objects
     */
    public Collection<Language> getAllLanguages() {
        return Collections.unmodifiableCollection(languages.values());
    }

    /**
     * Loads country and language data from a CSV file.
     *
     * @param filePath URL pointing to the CSV resource
     * @throws IOException if an I/O error occurs reading the CSV
     * @throws CsvValidationException if the CSV format is invalid
     */
    public void initializeData(URL filePath) throws IOException, CsvValidationException {

        try (InputStream is = filePath.openStream(); InputStreamReader isr = new InputStreamReader(
                is); CSVReader reader = new CSVReader(isr)) {
            String[] headers = reader.readNext();

            int countryIndex = -1;
            int languagesIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase("Country")) {
                    countryIndex = i;
                } else if (headers[i].trim().equalsIgnoreCase("Language")) {
                    languagesIndex = i;
                }
            }

            if (countryIndex == -1 || languagesIndex == -1) {
                throw new IllegalArgumentException(
                        "CSV must contain 'Country' and 'Language' columns");
            }

            // initializing two HashMaps for language frequency and country-language tuples
            Map<String, Integer> langFrequency = new HashMap<>();
            Map<String, Set<String>> countryLangMap = new HashMap<>();

            String[] line;
            while ((line = reader.readNext()) != null) {
                String country = line[countryIndex].trim();
                String[] langs = line[languagesIndex].split("[,;|]"); // covers different separators

                Set<String> uniqueLangs = new HashSet<>();

                for (String rawLang : langs) {
                    String lang = rawLang.trim().toLowerCase();
                    if (!lang.isEmpty()) {
                        uniqueLangs.add(lang);
                        langFrequency.put(lang, langFrequency.getOrDefault(lang, 0) + 1);
                    }
                }
                countryLangMap.put(country, uniqueLangs);
            }

            // Add all languages
            for (Map.Entry<String, Integer> entry : langFrequency.entrySet()) {
                int count = entry.getValue();
                int score = Math.max(1, (int) Math.ceil(10.0 / (count + 1)));
                addLanguage(entry.getKey(), score);
            }

            // Add all countries
            for (Map.Entry<String, Set<String>> entry : countryLangMap.entrySet()) {
                String country = entry.getKey();
                String[] langs = entry.getValue().toArray(new String[0]);
                addCountry(country, langs);
            }
        }
    }
}

