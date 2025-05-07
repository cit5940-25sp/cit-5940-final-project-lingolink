import java.util.Objects;

/**
 * Represents a language with its name and rarity score.
 * Used for calculating game points based on language rarity.
 */
public class Language {
    private final String name;
    private final int rarityScore;

    /**
     * Constructs a Language instance.
     *
     * @param name the name of the language
     * @param rarityScore the rarity score for point calculation
     */
    public Language(String name, int rarityScore) {
        this.name = name;
        this.rarityScore = rarityScore;
    }

    /**
     * getter method
     * @return the language name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the rarity score used in scoring calculations
     */
    public int getRarityScore() {
        return rarityScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return name.equalsIgnoreCase(language.name);
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
