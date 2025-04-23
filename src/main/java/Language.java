import java.util.Objects;

public class Language {
    private final String name;
    private final int rarityScore;

    public Language(String name, int rarityScore) {
        this.name = name;
        this.rarityScore = rarityScore;
    }

    public String getName() {
        return name;
    }

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
