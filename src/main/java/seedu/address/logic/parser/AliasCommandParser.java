package seedu.address.logic.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.AliasCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class AliasCommandParser implements Parser<AliasCommand> {
    private static final String HEADER = "alias";
    private static final String FORMAT = HEADER + " s/SHORTFORM r/REPRESENTATION";

    private static final String REGEX_REPRESENTATION = "Text";
    private static final String REGEX_SHORTFORM = "Shortform";

    private static final Pattern REGEX_PATTERN = Pattern.compile(
            HEADER+"\\s+((?<=\\s)(" +
                    "(r/(?<"+REGEX_REPRESENTATION+">[^/]+)(?!.*\\sr/))|" +
                    "(s/(?<"+REGEX_SHORTFORM+">[^/]+)(?!.*\\ss/))"+
                    ")(\\s|$)){2}",
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
    public boolean shouldPreprocess() {
        return false;
    }

    @Override
    public AliasCommand parse(String commandText) throws ParseException {
        Matcher matcher = REGEX_PATTERN.matcher(commandText);
        if (matcher.matches()) {
            String representation = parseRepresentation(matcher.group(REGEX_REPRESENTATION));
            String shortForm = parseShortForm(matcher.group(REGEX_SHORTFORM));
            return new AliasCommand(shortForm, representation);
        }

        throw new ParseException(commandText, String.format(
                Messages.MESSAGE_INVALID_COMMAND_FORMAT, getFormat()));
    }

    private String parseShortForm(String shortForm) throws ParseException {
        String trimmedShortForm = shortForm.trim();
        if (trimmedShortForm.length() < 2) {
            throw new ParseException(trimmedShortForm, " SHORTFORM: Needs to be more than one character");
        }
        if (trimmedShortForm.matches(".*\\s+.")) {
            throw new ParseException(trimmedShortForm, " SHORTFORM: Needs to be a single word without spaces");
        }

        return trimmedShortForm;
    }

    private String parseRepresentation(String repText) throws ParseException {
        String trimmedRep = repText.trim();

        if (trimmedRep.isEmpty()) {
            throw new ParseException(trimmedRep, " REPRESENTATION: Needs to be at least one character");
        }

        return trimmedRep;
    }
}
