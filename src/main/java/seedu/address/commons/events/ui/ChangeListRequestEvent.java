package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

public class ChangeListRequestEvent extends BaseEvent {

    public enum DisplayedList {
        Person,
        Alias
    }

    public final DisplayedList displayedList;

    public ChangeListRequestEvent(DisplayedList displayedList) {
        this.displayedList = displayedList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
