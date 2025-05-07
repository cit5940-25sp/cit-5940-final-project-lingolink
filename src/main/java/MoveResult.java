import java.util.Collections;
import java.util.Set;

/**
 * Shows the result of an attempted move, and can have different outcomes (hence many constructors)
 * Contains success status, user-facing message, optional next move data,
 * suggested language options when a move fails, and a flag for overused languages.
 */
public class MoveResult {
    private final boolean success;
    private final String message;
    private final Set<Language> languageOptions;
    private final GameMove move;
    private final boolean languageOverused;

    /**
     * Constructs a basic MoveResult with success status and message.
     *
     * @param success whether the move was successful
     * @param message descriptive message or error to display to the user
     */
    public MoveResult(boolean success, String message) {
        this(success, message, false);
    }

    /**
     * Constructs a MoveResult indicating success status, message,
     * and whether the failure was due to exceeding language usage.
     *
     * @param success whether the move was successful
     * @param message descriptive message or error to display to the user
     * @param languageOverused true if the chosen language has been overused
     */
    public MoveResult(boolean success, String message, boolean languageOverused) {
        this(success, message, null, languageOverused);
    }

    /**
     * Constructs a MoveResult including the successful GameMove details.
     *
     * @param success whether the move was successful
     * @param message descriptive message to display on success
     * @param move the GameMove object representing this move
     */
    public MoveResult(boolean success, String message, GameMove move) {
        this(success, message, move, false);
    }

    /**
     * Constructs a comprehensive MoveResult with move details,
     * message, and language overuse flag.
     *
     * @param success whether the move was successful
     * @param message descriptive message to display
     * @param move the GameMove object for this move
     * @param languageOverused true if the chosen language has been overused
     */
    public MoveResult(boolean success, String message, GameMove move, boolean languageOverused) {
        this.success = success;
        this.message = message;
        this.languageOverused = false;
        this.languageOptions = Collections.emptySet();
        this.move = move;
    }

    /**
     * Indicates whether the attempted move succeeded.
     *
     * @return true if the move was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @return user-facing message or error description
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns alternative language options when a move fails.
     *
     * @return a set of suggested Language objects, or empty if none
     */
    public Set<Language> getLanguageOptions() {
        return languageOptions;
    }

    public GameMove getMove() {
        return move;
    }

    public boolean isLanguageOverused() {
        return languageOverused;
    }
}
