package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Represents a Parser that is able to parse user input into a {@code Command} of type {@code T}.
 */
public interface Parser<T extends Command> {

    /**
     * Indicates whether parser would have preprocessed text. The default behaviour is to allow pre-processing.
     * Subclasses will override this method to return false if needed.
     * @return true if pre-processing is requested, else false.
     */
    public default boolean shouldPreprocess() {
        return true;
    }

    /**
     * Gets the header of this command parser.
     * The header is the first token of a command text
     * @return the string command
     */
    public String getHeader();

    /**
     * Gets the format of string required by command parse
     * @return a displayed format needed by user
     */
    public String getFormat();

    /**
     * Parses {@code userInput} into a command and returns it.
     *
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    T parse(String userInput) throws ParseException;
}
