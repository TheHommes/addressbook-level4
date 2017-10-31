package seedu.address.logic.parser.person;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.person.AddCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_BIRTHDAY,
                        PREFIX_TAG);

        //if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_BIRTHDAY, PREFIX_EMAIL)) {
        //throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));}

        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME), "add").get();
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE), "add").get();
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL), "add").get();
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS), "add").get();
            Birthday birthday = ParserUtil.parseBirthday(argMultimap.getValue(PREFIX_BIRTHDAY)).get();
            Remark remark = new Remark(""); // add command does not allow adding remarks straight away
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            boolean isPrivate = false;
            boolean isPinned = false;

            ReadOnlyPerson person = new Person(name, phone, email, address, , birthday, remark, tagList, isPrivate, isPinned);

            return new AddCommand(person);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     *
     * private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
     *   return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());}
     */

    @Override
    public String getCommandWord() {
        return AddCommand.COMMAND_WORD;
    }
}
