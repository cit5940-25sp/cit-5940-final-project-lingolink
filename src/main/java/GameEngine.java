import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game engine that implements the game rules and logic.
 * Uses a singleton pattern, manages game difficulty, and coordinates moves.
 */
public class GameEngine {
    // Using Singleton pattern for the game engine
    private static GameEngine instance;

    private final CountryLanguageManager dataService;
    private final List<IGameObserver> observers = new ArrayList<>();

    private GameState gameState;
    private boolean hardMode = false;
    /**
     * Private constructor to initialize the game engine with a data service.
     * Resets the game to a random starting country.
     *
     * @param dataService the CountryLanguageManager providing country and language data
     */
    private GameEngine(CountryLanguageManager dataService) {
        this.dataService = dataService;
        resetGame();
    }

    /**
     * Returns the singleton instance of GameEngine, creating it if necessary.
     *
     * @param dataService the CountryLanguageManager to use on first invocation
     * @return the single shared GameEngine instance
     */
    public static synchronized GameEngine getInstance(CountryLanguageManager dataService) {
        if (instance == null) {
            instance = new GameEngine(dataService);
        }
        return instance;
    }



    /**
     * Resets the game state using the provided Random for country selection.
     *
     * @param random the Random instance used to pick the starting country
     */
    public void resetGame(Random random) {
        List<Country> allCountries = new ArrayList<>(dataService.getAllCountries());
        Country startingCountry = allCountries.get(random.nextInt(allCountries.size()));
        gameState = new GameState(startingCountry);
        notifyObservers();
    }

    /**
     * Resets the game state with a new Random instance.
     * Delegates to resetGame(Random).
     */
    public void resetGame() {
        Random random = new Random();
        resetGame(random);
    }

    /**
     * Enables or disables hard mode, adjusting language usage limits.
     *
     * @param hardMode true to use hard mode limits, false for normal limits
     */
    public void setHardMode(boolean hardMode) {

        this.hardMode = hardMode;
    }

    /**
     * Indicates whether the game is in hard mode.
     *
     * @return true if hard mode is active, false otherwise
     */
    public boolean isHardMode() {
        return hardMode;
    }

    /**
     * Selects a language for the current streak and enforces usage limits.
     * Resets the streak if a new language is chosen.
     *
     * @param language the Language to select
     * @return true if the language was accepted, false if it exceeded its limit
     */
    public boolean setSelectedLanguage(Language language) {

        int languageLimit;
        if (!hardMode){
            languageLimit = 7;
        }   else {
            languageLimit = 4;
        }
        int currentUsage = gameState.getLanguageUsage().getOrDefault(language, 0);

        if (currentUsage >= languageLimit) {
            // Language has reached its limit
            System.out.println("You've already used " + language.getName() + " " +
                    languageLimit + " times. Please pick another language.");
            return false;
        }

        if (gameState.getCurrentLanguage() != language) {
            // begin new streak if user selects a new language
            gameState.setCurrentStreak(0);
        }
        gameState.setCurrentLanguage(language);
        notifyObservers();
        return true;
    }

    /**
     * Attempts to move to a new country by name, validating existence, usage, and language.
     *
     * @param countryName the (case-insensitive) name of the target country
     * @return a MoveResult indicating success or failure and any messages
     */
    public MoveResult moveToCountry(String countryName) {
        Country country = dataService.getCountry(countryName.toLowerCase());

        if (country == null) {
            return new MoveResult(false, "Country not found: " + countryName);
        }

        if (gameState.isCountryUsed(country)) {
            return new MoveResult(false, "Country already used: " + countryName);
        }

        Language currentLang = gameState.getCurrentLanguage();
        if (currentLang == null) {
            return new MoveResult(false, "No language selected. Please choose a language first.");
        }

        if (!country.hasLanguage(currentLang)) {
            return new MoveResult(false, country.getName() + " does not speak " + currentLang.getName());
        }

        return continueStreak(country, currentLang);
    }

    /**
     * Continues the current language streak
     */
    private MoveResult continueStreak(Country country, Language language) {
        int newStreak = gameState.getCurrentStreak() + 1;
        int points = language.getRarityScore() * newStreak;
        GameMove move = new GameMove(country, language, points);

        gameState.incrementLanguageUsage(language);
        gameState.setCurrentCountry(country);
        gameState.setCurrentStreak(newStreak);
        gameState.addPoints(points);
        gameState.addMove(move);

        notifyObservers();
        return new MoveResult(true, "Streak continued with " + language.getName() +
                ". +" + points + " points", move);
    }

    /**
     * Registers an observer to receive updates when the game state changes.
     *
     * @param observer the IGameObserver to notify on state changes
     */
    public void addObserver(IGameObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers of game state changes
     */
    private void notifyObservers() {
        for (IGameObserver observer : observers) {
            observer.onGameStateChanged(gameState);
        }
    }

    /**
     * @return the GameState object representing the session’s current status
     */
    public GameState getGameState() {
        return gameState;
    }
}
