package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.alias.AliasToken;


public class MasterParser {
    private static final Pattern KEYWORD_PATTERN =
            Pattern.compile("([^\\s/]+)([\\s/]+|$)");

    private Map<String, Parser<? extends Command>> commandParsers;
    private Map<String, AliasToken> aliasingTokens;
    private ObservableList<AliasToken> aliasList = FXCollections.observableArrayList();

    public MasterParser() {
        this.commandParsers = new HashMap<String, Parser<? extends Command>>();
        this.aliasingTokens = new HashMap<String, AliasToken>();
    }

    public Command parse(String userInput) {
        String[] pieces = preprocessInitial(userInput.trim());
        if (pieces == null) {
            return new IncorrectCommand(userInput,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        String header = pieces[0];
        String body = pieces[1];
        Parser<? extends Command> parser = selectParser(extractTrueHeader(header));
        if (parser == null) {
            return new IncorrectCommand(header + body,
                    String.format(MESSAGE_UNKNOWN_COMMAND, HelpCommand.MESSAGE_USAGE));
        }

        if (parser.shouldPreprocess()) {
            return makeParserParse(parser, header + preprocessBody(body));
        } else {
            return makeParserParse(parser, header + body);
        }
    }

    private Command makeParserParse(Parser<? extends Command> parser, String preprocessedText) {
        try {
            return parser.parse(preprocessedText);
        } catch (ParseException pe) {
            return new IncorrectCommand(preprocessedText, String.format(pe.getFailureMessage()));
        }
    }

    private Parser<? extends Command> selectParser(String header) {
        return commandParsers.get(header);
    }

    private String[] preprocessInitial(String commandText) {
        Matcher matcher = KEYWORD_PATTERN.matcher(commandText);

        if (matcher.find()) {
            String header = matcher.group(1);
            String spaces = matcher.group(2);

            AliasToken token = aliasingTokens.get(header);
            if (token != null) {
                header = token.getRepresentation();
            }

            String body = commandText.substring(matcher.end());
            return new String[]{header + spaces, body};

        }
        return null;
    }

    private String extractTrueHeader(String preprocessedHeader) {
        Matcher matcher = KEYWORD_PATTERN.matcher(preprocessedHeader);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private String preprocessBody(String bodyText) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher = KEYWORD_PATTERN.matcher(bodyText);

        while (matcher.find()) {
            String keyword = matcher.group(1);
            String spaces = matcher.group(2);

            AliasToken token = aliasingTokens.get(keyword);
            if (token != null) {
                keyword = token.getRepresentation();
            }

            builder.append(keyword);
            builder.append(spaces);
        }

        return builder.toString();
    }

    public boolean registerCommandParser(Parser<? extends Command> commandParser) {
        assert commandParser != null;

        if (commandParsers.containsKey(commandParser.getHeader())) {
            return false;
        }
        if (aliasingTokens.containsKey(commandParser.getHeader())) {
            return false;
        }

        commandParsers.put(commandParser.getHeader(), commandParser);
        return true;
    }

    public boolean isCommandParserRegistered(String header) {
        return commandParsers.containsKey(header);
    }


    public Parser<? extends Command> unregisterCommandParser(String header) {
        return commandParsers.remove(header);
    }

    public boolean addAliasToken(AliasToken token) {
        assert token != null;

        if (aliasingTokens.containsKey(token.getShortForm())) {
            return false;
        }

        if (isCommandParserRegistered(token.getShortForm())) {
            return false;
        }

        aliasList.add(token);
        aliasingTokens.put(token.getShortForm(), token);
        return true;
    }


    public boolean removeAliasToken(String tokenKeyword) {
        assert tokenKeyword != null;

        AliasToken token = aliasingTokens.remove(tokenKeyword);
        if (token != null) {
            return aliasList.remove(token);
        } else {
            return false;
        }
    }

    public boolean doesAliasTokenExist(String tokenKeyword) {
        assert tokenKeyword != null;

        return aliasingTokens.containsKey(tokenKeyword);
    }

    public void clearAllAliasTokens() {
        aliasList.clear();
        aliasingTokens.clear();
    }

    public ObservableList<AliasToken> getAliasTokenList() {
        return aliasList;
    }
}
