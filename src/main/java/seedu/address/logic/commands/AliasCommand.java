package seedu.address.logic.commands;

import seedu.address.logic.Logic;
import seedu.address.model.alias.AliasToken;
import seedu.address.model.alias.exceptions.DuplicateTokenKeywordException;

/**
 * This command is used to create aliases for other commands and general words
 */

public class AliasCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "alias";

    public static String MESSAGE_USAGE = COMMAND_WORD + ": Creates an alias for a command or for any word: "
            + "Parameters: s/SHORTFORM r/REPRESENTATION\n"
            + "Example: " + COMMAND_WORD
            + " s/codename r/alias";

    public static final String MESSAGE_SUCCESS = "New alias added: %1$s";
    public static final String MESSAGE_DUPLICATE_ALIAS = "Requested alias is already in use";
    public static final String MESSAGE_INVALID_SHORTFORM = "Command name cannot be used as a short-form!";

    private Logic logic;
    private final String shortForm;
    private final String representation;

    /**
     * Creates an AliasCommand to create an alias consisting of the specified parameters below
     *
     * @param shortForm      - the short form desired
     * @param representation - what the short form represents; the long form
     */

    public AliasCommand(String shortForm, String representation) {
        assert shortForm != null;
        assert representation != null;
        this.shortForm = shortForm;
        this.representation = representation;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        assert model != null;

        AliasToken toAdd = new AliasToken(shortForm, representation);

        if (logic.canParseHeader(toAdd.getShortForm())) {
            return new CommandResult(MESSAGE_INVALID_SHORTFORM);
        }

        try {
            model.addAliasToken(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateTokenKeywordException e) {
            return new CommandResult(MESSAGE_DUPLICATE_ALIAS);
        }
    }

    public void setLogic(Logic logic) {
        assert logic != null;
        this.logic = logic;
    }

}
