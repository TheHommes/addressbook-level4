package seedu.address.logic.parser.exceptions;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a parse error encountered by a parser.
 */
public class ParseException extends IllegalValueException {
    private String parsedMessage;
    private String failureMessage;

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor for ParseException object with a string that was failed
     * to be parsed by the parse and further instructions to why the string
     * could not be parsed
     *
     * @param parsedMessage  - the string which contains error
     * @param failureMessage = the respective failure message
     */
    public ParseException(String parsedMessage, String failureMessage) {
        super("Failed to parse " + parsedMessage + ". " + failureMessage);
        this.parsedMessage = parsedMessage;
        this.failureMessage = failureMessage;
    }

    public String getParsedMessage() {
        return this.parsedMessage;
    }

    public String getFailureMessage() {
        return this.failureMessage;
    }
}
