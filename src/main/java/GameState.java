import java.util.*;

/**
 * Represents the current state of a game session.
 * Tracks the current country, selected language, streak length, total score,
 * history of moves, and per-language usage counts.
 */
public class GameState {
    private final List<GameMove> moves = new ArrayList<>();
    private final Set<Country> usedCountries = new HashSet<>();
    private Country currentCountry;
    private Language currentLanguage;
    private int currentStreak;
    private int totalScore;
    private final Map<Language, Integer> languageUsage = new HashMap<>();

    /**
     * Constructor method
     * Initializes a new game session starting from the given country.
     * Adds the starting country as the first move in the history.
     *
     * @param startingCountry the country where the game begins
     */
    public GameState(Country startingCountry) {
        this.currentCountry = startingCountry;
        this.currentLanguage = null;
        this.currentStreak = 0;
        this.totalScore = 0;

        // add starting country to used countries and move history
        usedCountries.add(startingCountry);
        moves.add(new GameMove(startingCountry, null, 0));
    }

    public Country getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(Country currentCountry) {
        this.currentCountry = currentCountry;
        usedCountries.add(currentCountry);
    }

    /**
     * Gets the language the player has selected for the current streak.
     *
     * @return the selected Language, or null if none chosen
     */
    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Sets the language for evaluating subsequent country moves.
     *
     * @param currentLanguage the Language chosen for the streak
     */
    public void setCurrentLanguage(Language currentLanguage) {
        this.currentLanguage = currentLanguage;
    }


    public int getCurrentStreak() {
        return currentStreak;
    }

    /**
     * Sets the current streak count, resetting or adjusting as needed.
     *
     * @param currentStreak the new streak length
     */
    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    /**
     * @return the total points earned so far
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * @param points the number of points to add to the total score
     */
    public void addPoints(int points) {
        this.totalScore += points;
    }

    /**
     * @return an unmodifiable list of GameMove objects
     */
    public List<GameMove> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    /**
     * Records a new move in the session history.
     *
     * @param move the GameMove to add to the history
     */
    public void addMove(GameMove move) {
        moves.add(move);
    }

    /**
     * Checks if the specified country has already been used in this session.
     *
     * @param country the Country to check
     * @return true if the country is in the used-countries set, false otherwise
     */
    public boolean isCountryUsed(Country country) {
        return usedCountries.contains(country);
    }

    /**
     * @return an unmodifiable map from Language to usage count
     */
    public Map<Language, Integer> getLanguageUsage() {
        return Collections.unmodifiableMap(languageUsage);
    }

    /**
     * @param lang the Language whose usage count to increment
     */
    public void incrementLanguageUsage(Language lang) {
        languageUsage.put(lang, languageUsage.getOrDefault(lang, 0) + 1);
    }
}
