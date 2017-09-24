package seedu.address.ui;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * Controller for an edit page.
 */
public class EditPanel extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(EditPanel.class);
    private static final String ICON = "/images/address_book_32.png";
    private static final String FXML = "EditPanel.fxml";
    private static final String TITLE = "Edit";
    private String index;
    private Index newIndex;
    private Model model;
    private ReadOnlyPerson personToEdit;

    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField tagField;
    @FXML
    private FlowPane tagFlow;

    private final Stage dialogStage;

    public EditPanel(Model model, String index, ReadOnlyPerson personToEdit) {
        super(FXML);
        Scene scene = new Scene(getRoot());
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        FxViewUtil.setStageIcon(dialogStage, ICON);
        this.model = model;
        this.index = index;
        this.personToEdit = personToEdit;
        init(personToEdit);
    }

    /**
     * Initializes the values in text fields.
     *
     * @param personToEdit
     */
    private void init(ReadOnlyPerson personToEdit) {
        nameField.setText(personToEdit.getName().toString());
        phoneField.setText(personToEdit.getPhone().toString());
        emailField.setText(personToEdit.getEmail().toString());
        addressField.setText(personToEdit.getAddress().toString());
        StringBuilder build = new StringBuilder();
        for (Tag tags : personToEdit.getTags()) {
            build.append(tags.toEditString()).append(",");
        }
        String tagList = build.toString();
        personToEdit.getTags().forEach(tag -> tagFlow.getChildren().add(new Label(tag.tagName)));
        tagField.setText(tagList);
    }

    /**
     * Edits a person's values when ok button is clicked.
     *
     * @throws IllegalValueException
     * @throws CommandException
     */
    @FXML
    private void handleOk() throws IllegalValueException, CommandException {
        try {
            EditPersonDescriptor editPerson = getEditPersonDescriptor();
            newIndex = ParserUtil.parseIndex(index);
            EditCommand command = new EditCommand(newIndex, editPerson);
            command.executeButtonUndoableCommand(model);
            dialogStage.close();
        } catch (IllegalValueException e) {
            if (nameField.getText().equals("") || !nameField.getText().matches("[a-zA-z0-9\\s]+")) {
                nameField.setStyle("-fx-border-color: red");
                displayInvalidAlert("name");
            } else {
                nameField.setStyle("-fx-border-color: grey");
            }
            if (phoneField.getText().equals("") || !phoneField.getText().matches("[\\d]{3,}")) {
                phoneField.setStyle("-fx-border-color: red");
                displayInvalidAlert("phone");
            } else {
                phoneField.setStyle("-fx-border-color: grey");
            }

            if (emailField.getText().equals("") || !emailField.getText().matches(".+@{1}.+")) {
                emailField.setStyle("-fx-border-color: red");
                displayInvalidAlert("email");
            } else {
                emailField.setStyle("-fx-border-color: grey");
            }

            if (!addressField.getText().matches(".+")) {
                addressField.setStyle("-fx-border-color: red");
                displayInvalidAlert("address");
            } else {
                addressField.setStyle("-fx-border-color: grey");
            }
            if (!tagField.getText().matches("[\\S.]*")) {
                tagField.setStyle("-fx-border-color: red");
                displayInvalidAlert("tag");
            } else {
                tagField.setStyle("-fx-border-color: grey");
            }

        }
    }

    /**
     * Displays alert for wrong inputs.
     *
     * @param field
     */
    private void displayInvalidAlert(String field) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle(String.format("Wrong %s format", field));
        alert.setHeaderText("Fields are not filled up / invalid input");
        switch (field) {
        case "name":
            alert.setContentText(Name.MESSAGE_NAME_CONSTRAINTS);
            break;
        case "phone":
            alert.setContentText(Phone.MESSAGE_PHONE_CONSTRAINTS);
            break;
        case "email":
            alert.setContentText(Email.MESSAGE_EMAIL_CONSTRAINTS);
            break;
        case "address":
            alert.setContentText(Address.MESSAGE_ADDRESS_CONSTRAINTS);
            break;
        case "tag":
            alert.setContentText(Tag.MESSAGE_EDIT_PANEL_CONSTRAINTS);
            break;
        default:
            break;
        }
        alert.showAndWait();
    }

    private EditPersonDescriptor getEditPersonDescriptor() throws IllegalValueException {
        Name newName = new Name(nameField.getText().trim());
        Phone newPhone = new Phone(phoneField.getText().trim());
        Email newEmail = new Email(emailField.getText().trim());
        Address newAddress = new Address(addressField.getText().trim());
        Set<Tag> tagList = getTags();
        EditPersonDescriptor editPerson = new EditPersonDescriptor();
        editPerson.setName(newName);
        editPerson.setPhone(newPhone);
        editPerson.setEmail(newEmail);
        editPerson.setAddress(newAddress);
        editPerson.setTags(tagList);
        return editPerson;
    }

    private Set<Tag> getTags() throws IllegalValueException {
        if (tagField.getText().isEmpty()) {
            return new HashSet<>();
        }
        String[] stringTags = tagField.getText().trim().split(",");
        Set<Tag> newTagList = new HashSet<>();
        for (String tags : stringTags) {
            newTagList.add(new Tag(tags));
        }
        return newTagList;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Shows the edit panel.
     */
    public void show() {
        logger.fine("Showing edit panel.");
        dialogStage.showAndWait();
    }
}
