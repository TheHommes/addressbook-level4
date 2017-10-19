package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ONLY_HIDDEN;

/**
 * Lists all persons in the address book to the user.
 */
public class ListHiddenCommand extends Command {

    public static final String COMMAND_WORD = "listhidden";

    public static final String MESSAGE_SUCCESS = "Listed all hidden persons";

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(PREDICATE_SHOW_ONLY_HIDDEN);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
