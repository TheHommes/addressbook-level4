package seedu.address.model.alias;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.alias.exceptions.DuplicateTokenKeywordException;
import seedu.address.model.alias.exceptions.TokenKeywordNotFoundException;

public class AliasTokenList implements Iterable<AliasToken> {
    private final ObservableList<AliasToken> aliasList = FXCollections.observableArrayList();

    public AliasTokenList() {
    }

    /**
     * Constructor to copy given list
     *
     * @param cpList - the list to copy it from
     */
    public AliasTokenList(AliasTokenList cpList) {
        assert cpList != null;
        aliasList.addAll(cpList.aliasList);
    }

    @Override
    public Iterator<AliasToken> iterator() {
        return aliasList.iterator();
    }

    /**
     * @param tokenKeyword - the shortForm used by the alias token
     * @return true if token with required tokenKeyword exists in list, else false
     */
    public boolean contains(String tokenKeyword) {
        for (AliasToken token : aliasList) {
            if (token.getShortForm().equals(tokenKeyword))
                return true;
        }
        return false;
    }

    /**
     * @param token
     * @return true if token exits, else false
     */

    public boolean contains(AliasToken token) {
        return aliasList.contains(token);
    }

    /**
     * Add unique token to list
     *
     * @param token - token to be added
     * @throws DuplicateTokenKeywordException - if thetoken already exists
     */
    public void addAliasToken(AliasToken token) throws DuplicateTokenKeywordException {
        assert token != null;
        if (contains(token.getShortForm()))
            throw new DuplicateTokenKeywordException();
        aliasList.add(token);
    }

    /**
     * Removes a unique token from the list
     *
     * @param token - the token to be removed
     * @throws TokenKeywordNotFoundException - if no such token was found
     */
    public void removeAliasToken(AliasToken token) throws TokenKeywordNotFoundException {
        assert token != null;
        if (!contains(token))
            throw new TokenKeywordNotFoundException();
        aliasList.remove(token);
    }

    /**
     * Replace previous token with a new token while retaining same keyword functionality
     *
     * @param prevToken -  a unique token to be replaced
     * @param newToken  - a unique token to replace prevToken
     * @throws TokenKeywordNotFoundException
     */
    public void replaceAliasToken(AliasToken prevToken, AliasToken newToken) throws TokenKeywordNotFoundException {
        assert prevToken != null && newToken != null;
        assert prevToken.getShortForm().equals(newToken.getShortForm());

        removeAliasToken(prevToken);
        aliasList.add(newToken);
    }

    /**
     * Clears current list and copies all elements from previous list to this.
     *
     * @param prevList - previous list
     */
    public void reset(AliasTokenList prevList) {
        assert prevList != null;
        aliasList.addAll(prevList.aliasList);

    }

    public int getSize() {
        return aliasList.size();
    }

    public List<AliasToken> asReadOnly() {
        return Collections.unmodifiableList(aliasList);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AliasTokenList
                && this.aliasList.equals(((AliasTokenList) other).aliasList));
    }

    @Override
    public int hashCode() {
        return aliasList.hashCode();
    }
}
