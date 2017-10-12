package seedu.address.model.alias;

import java.util.Objects;

public class AliasToken {
    private final String shortForm;
    private final String representation;

    public AliasToken(String shortForm, String represenation) {
        assert shortForm != null && !shortForm.matches(".*\\s+.*");
        assert represenation != null && !represenation.isEmpty();

        this.shortForm = shortForm.toLowerCase();
        this.representation = represenation;

    }

    public String getShortForm() {
        return this.shortForm;
    }

    public String getRepresentation() {
        return this.representation;
    }

    @Override
    public String toString() {
        return "[Shortform: " + this.shortForm + ", Representation: " + this.representation + "]";
    }

    @Override
    public boolean equals(Object object) {
        return this == object || (object instanceof AliasToken &&
                this.shortForm.equals(((AliasToken) object).shortForm) &&
                this.representation.equals(((AliasToken) object).representation));
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortForm, representation);
    }
}
