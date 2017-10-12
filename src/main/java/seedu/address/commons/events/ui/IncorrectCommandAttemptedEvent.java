package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.commands.Command;

public class IncorrectCommandAttemptedEvent extends BaseEvent {

    public IncorrectCommandAttemptedEvent(Command command) {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
