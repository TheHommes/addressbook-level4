package seedu.address.logic.commands;

import seedu.address.logic.parser.Parser;
import seedu.address.model.alias.AliasToken;
import seedu.address.model.alias.exceptions.TokenKeywordNotFoundException;

public class UnaliasCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "unalias";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes an alias for a command or shortform. "
            + "Parameters: s/SHORTFORM\n"
            + "Example: " + COMMAND_WORD
            + " s/codename";

    public static final String MESSAGE_SUCCESS = "Alias removed: %1$s";
    public static final String MESSAGE_UNKNOWN_ALIAS = "This alias is not in use";

    private String shortForm;

    public UnaliasCommand(String shortForm) {
        this.shortForm = shortForm;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        assert model != null;

        AliasToken toRemove = null;
        for (AliasToken token : model.getAddressBook().getReadOnlyListOfAliasTokens()) {
            if (token.getShortForm().equals(this.shortForm)) {
                toRemove = token;
                break;
            }
        }

        try {
            if (toRemove == null) {
                return new CommandResult(MESSAGE_UNKNOWN_ALIAS);
            }

            model.removeAliasToken(toRemove);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toRemove));
        } catch (TokenKeywordNotFoundException e) {
            return new CommandResult(MESSAGE_UNKNOWN_ALIAS);
        }

    }
}
