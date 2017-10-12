package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.alias.AliasToken;

public class AliasTokenChangedEvent extends BaseEvent {

    public enum Action {
        Added,
        Removed;
    }

    private AliasToken token;
    private Action action;

    public AliasTokenChangedEvent(AliasToken token, Action action) {
        assert token != null;
        assert token != null;

        this.token = token;
        this.action = action;
    }

    @Override
    public String toString() {
        return "Alias token " + action.toString() + ": " + token.toString();
    }

    public AliasToken getToken() {
        return this.token;
    }

    public Action getAction() {
        return this.action;
    }
}
