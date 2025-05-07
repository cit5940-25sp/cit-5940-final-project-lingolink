import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a country with its official languages.
 * Allows querying of individual languages and calculation of shared languages with another country.
 */
public class Country {
    private final String name;
    private final Set<Language> languages;

    /**
     * Constructs a Country instance.
     *
     * @param name the official name of the country
     * @param languages the set of official languages spoken in the country
     */
    public Country(String name, Set<Language> languages) {
        this.name = name;
        this.languages = Set.copyOf(languages);
    }


    public String getName() {
        return name;
    }

    /**
     *
     * @return an unmodifiable set of Languages spoken in this country
     */
    public Set<Language> getLanguages() {
        return languages;
    }

    /**
     * Checks if this country speaks the given language.
     *
     * @param language the Language to check for
     * @return true if the country speaks the specified language, false otherwise
     */
    public boolean hasLanguage(Language language) {
        return languages.contains(language);
    }

    /**
     * Finds the intersection of languages spoken by this country and another.
     *
     * @param otherCountry the country to compare with
     * @return a set of Languages common to both countries
     */
    public Set<Language> getSharedLanguages(Country otherCountry) {
        Set<Language> shared = new HashSet<>(languages);
        shared.retainAll(otherCountry.getLanguages());
        return shared;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return name.equalsIgnoreCase(country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
