package seedu.address.logic.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.UnaliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class UnaliasCommandParser implements Parser<UnaliasCommand> {
    private static final String HEADER = "unalias";
    private static final String FORMAT = HEADER + "SHORTFORM";

    private static final String REGEX_SHORTFORM = "Shortform";

    private static final Pattern REGEX_PATTERN = Pattern.compile(
            HEADER + "\\s+(?<" + REGEX_SHORTFORM + ">[^/]+)",
            Pattern.CASE_INSENSITIVE

    );
    
    @Override
    public String getHeader() {
        return HEADER;
    }

    @Override
    public String getFormat() {
        return FORMAT;
    }

    @Override
    public UnaliasCommand parse(String commandText) throws ParseException {
        Matcher matcher = REGEX_PATTERN.matcher(commandText);
        if (matcher.matches()) {
            String shortForm = parseShortForm(matcher.group(REGEX_SHORTFORM));
            return new UnaliasCommand(shortForm);
        }

        throw new ParseException(commandText, String.format(
                Messages.MESSAGE_INVALID_COMMAND_FORMAT, getFormat()));
    }

    private String parseShortForm(String shortForm) throws ParseException {
        String trimmedShortForm = shortForm.trim();
        if (trimmedShortForm.isEmpty()) {
            throw new ParseException(trimmedShortForm, " SHORTFORM: Cannot be empty");
        }
        return trimmedShortForm;
    }

}
