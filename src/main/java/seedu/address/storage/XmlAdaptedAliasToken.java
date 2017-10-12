package seedu.address.storage;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.alias.AliasToken;

public class XmlAdaptedAliasToken {

    @XmlElement(required = true)
    private String shortForm;
    @XmlElement(required = true)
    private String representation;

    /**
     * No-arg constructor for JAXB use
     */

    public XmlAdaptedAliasToken() {
    }

    /**
     * Converts a given AliasToken to XmlAdaptedAliasToken for JAXB
     *
     * @param source - this created class will be unaffected by future changes
     */
    public XmlAdaptedAliasToken(AliasToken source) {
        shortForm = source.getShortForm();
        representation = source.getRepresentation();
    }

    /**
     * Converts jaxb AdaptedAliasToken object into AliasToken used in model
     *
     * @throws IllegalValueException
     */
    public AliasToken toModelType() throws IllegalValueException {
        return new AliasToken(shortForm, representation);
    }


}
