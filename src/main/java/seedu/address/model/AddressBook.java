package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.model.alias.ReadOnlyAliasToken;
import seedu.address.model.alias.UniqueAliasTokenList;
import seedu.address.model.alias.exceptions.DuplicateTokenKeywordException;
import seedu.address.model.alias.exceptions.TokenKeywordNotFoundException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTagList tags;
    private final UniqueAliasTokenList aliasTokens;

    /**
     * Creates a new AddressBook with all data
     */
    public AddressBook() {
        this.persons = new UniquePersonList();
        this.tags = new UniqueTagList();
        this.aliasTokens = new UniqueAliasTokenList();
    }

    /**
     * Creates an AddressBook using Persons,Tags and AliasTokens in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setPersons(List<? extends ReadOnlyPerson> persons) throws DuplicatePersonException {
        this.persons.setPersons(persons);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    public void setAliasTokens(List<? extends ReadOnlyAliasToken> aliasTokens) throws DuplicateTokenKeywordException {
        this.aliasTokens.setAliasTokens(aliasTokens);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        try {
            setPersons(newData.getPersonList());
        } catch (DuplicatePersonException e) {
            assert false : "AddressBook should not have duplicate persons";
        }

        setTags(new HashSet<>(newData.getTagList()));

        try {
            setAliasTokens(newData.getAliasTokenList());
        } catch (DuplicateTokenKeywordException e) {
            assert false : "AddressBook should not have duplicate aliases";
        }

        syncMasterTagListWith(persons);
    }

    /**
     * Sorts the list.
     */
    public void sortList(String toSort) {
        persons.sort(toSort);
    }

    //// person-level operations

    /**
     * Adds a person to the address book.
     * Also checks the new person's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #tags}.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(ReadOnlyPerson p) throws DuplicatePersonException {
        Person newPerson = new Person(p);
        syncMasterTagListWith(newPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.add(newPerson);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedReadOnlyPerson}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedReadOnlyPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *                                  another existing person in the list.
     * @throws PersonNotFoundException  if {@code target} could not be found in the list.
     * @see #syncMasterTagListWith(Person)
     */
    public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedReadOnlyPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedReadOnlyPerson);

        Person editedPerson = new Person(editedReadOnlyPerson);
        syncMasterTagListWith(editedPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.setPerson(target, editedPerson);
    }

    /**
     * Ensures that every tag in this person:
     * - exists in the master list {@link #tags}
     * - points to a Tag object in the master list
     */
    private void syncMasterTagListWith(Person person) {
        final UniqueTagList personTags = new UniqueTagList(person.getTags());
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        person.setTags(correctTagReferences);
    }

    /**
     * Ensures that every tag in these persons:
     * - exists in the master list {@link #tags}
     * - points to a Tag object in the master list
     *
     * @see #syncMasterTagListWith(Person)
     */
    private void syncMasterTagListWith(UniquePersonList persons) {
        persons.forEach(this::syncMasterTagListWith);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(ReadOnlyPerson key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Hides (@code toHide) from this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if the {@code toHide} is not in this {@code AddressBook}.
     */
    public boolean hidePerson(ReadOnlyPerson toHide) throws PersonNotFoundException {
        if (persons.hide(toHide)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Unhides (@code toUnhide) from this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if the {@code toUnhide} is not in this {@code AddressBook}.
     */
    public boolean unhidePerson(ReadOnlyPerson toUnhide) throws PersonNotFoundException {
        if (persons.unhide(toUnhide)) {

    /**
     * Pins (@code toPin) in this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if the {@code toPin} is not in this {@code AddressBook}.
     */
    public boolean pinPerson(ReadOnlyPerson toPin) throws PersonNotFoundException {
        if (persons.pin(toPin)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Pins (@code toUnpin) from this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if the {@code toUnpin} is not in this {@code AddressBook}.
     */
    public boolean unpinPerson(ReadOnlyPerson toUnpin) throws PersonNotFoundException {
        if (persons.unpin(toUnpin)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //// alias-level operations

    /**
     * Adds an alias token
     *
     * @throws DuplicateTokenKeywordException if another token with same keyword exists.
     */
    public void addAliasToken(ReadOnlyAliasToken toAdd) throws DuplicateTokenKeywordException {
        aliasTokens.add(toAdd);
    }

    /**
     * Removes an alias token
     *
     * @throws TokenKeywordNotFoundException if no such tokens exists.
     */
    public boolean removeAliasToken(ReadOnlyAliasToken toRemove) throws TokenKeywordNotFoundException {
        if (aliasTokens.remove(toRemove)) {
            return true;
        } else {
            throw new TokenKeywordNotFoundException();
        }
    }

    public int getAliasTokenCount() {
        return aliasTokens.size();
    }


    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() + " tags, "
                + aliasTokens.asObservableList().size() + " aliases";
    }

    @Override
    public ObservableList<ReadOnlyPerson> getPersonList() {
        return persons.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public ObservableList<ReadOnlyAliasToken> getAliasTokenList() {
        return aliasTokens.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.persons.equals(((AddressBook) other).persons)
                && this.tags.equalsOrderInsensitive(((AddressBook) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags);
    }
}
