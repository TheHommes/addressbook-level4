package seedu.address.logic;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AliasTokenChangedEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddCommandParser;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.AliasCommandParser;
import seedu.address.logic.parser.DeleteCommandParser;
import seedu.address.logic.parser.EditCommandParser;
import seedu.address.logic.parser.FindCommandParser;
import seedu.address.logic.parser.MasterParser;
import seedu.address.logic.parser.UnaliasCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.alias.AliasToken;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private MasterParser parser;
    private final CommandHistory history;
    private final AddressBookParser addressBookParser;
    private final UndoRedoStack undoRedoStack;

    public LogicManager(Model model) {
        this.model = model;
        this.history = new CommandHistory();
        this.addressBookParser = new AddressBookParser();
        this.undoRedoStack = new UndoRedoStack();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = addressBookParser.parseCommand(commandText);
            command.setData(model, history, undoRedoStack);
            CommandResult result = command.execute();
            undoRedoStack.push(command);
            return result;
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<AliasToken> getAliasTokenList() {
        return parser.getAliasTokenList();
    }

    private void loadAllAliasTokens() {
        List<AliasToken> allTokens = model.getAddressBook().getReadOnlyListOfAliasTokens();
        for (AliasToken token : allTokens) {
            parser.addAliasToken(token);
        }
    }

    private void logParserTokenChange(boolean success, AliasToken changedToken,
                                      String sucesssMsgFormat, String failureMsgFormat) {
        if (success) {
            logger.info(String.format(sucesssMsgFormat, changedToken));
        } else {
            logger.warning(String.format(failureMsgFormat, changedToken));
        }
    }

    @Subscribe
    public void handleAliasTokenChangedEvent(AliasTokenChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(
                event, "Alias token " + event.getAction().toString().toLowerCase()));

        if (event.getAction().equals(AliasTokenChangedEvent.Action.Added)) {
            logParserTokenChange(
                    parser.addAliasToken(event.getToken()),
                    event.getToken(),
                    "Added alias token '%s' to parser",
                    "Failed to add alias token '%s' to parser");
        } else if (event.getAction().equals(AliasTokenChangedEvent.Action.Removed)) {
            logParserTokenChange(
                    parser.removeAliasToken(event.getToken().getShortForm()),
                    event.getToken(),
                    "Removed alias token '%s' from parser",
                    "Failed to remove alias token '%s' from parser");
        }
    }

    private void registerAllDefaultCommandParsers() {
        parser.registerCommandParser(new AddCommandParser());
        parser.registerCommandParser(new DeleteCommandParser());
        parser.registerCommandParser(new FindCommandParser());
        parser.registerCommandParser(new EditCommandParser());
        parser.registerCommandParser(new AliasCommandParser());
        parser.registerCommandParser(new UnaliasCommandParser());
    }

    @Override
    public boolean canParseHeader(String header) {
        return parser.isCommandParserRegistered(header);
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }
}
