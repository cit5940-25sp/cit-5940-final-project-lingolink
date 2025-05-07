public class GameMove {
    private final Country country;
    private final Language language;
    private final int points;

    /**
    * represents a single move in the game
     */
    public GameMove(Country country, Language language, int points) {
        this.country = country;
        this.language = language;
        this.points = points;
    }

    public Country getCountry() {
        return country;
    }

    public Language getLanguage() {
        return language;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Move: " + country.getName() + " (Language: " +
                (language != null ? language.getName() : "Starting country") +
                ", Points: " + points + ")";
    }
}
