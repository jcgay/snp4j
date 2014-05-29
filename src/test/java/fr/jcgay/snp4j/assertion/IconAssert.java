package fr.jcgay.snp4j.assertion;

import fr.jcgay.snp4j.Icon;
import org.assertj.core.api.AbstractAssert;

public class IconAssert extends AbstractAssert<IconAssert, Icon> {

    public IconAssert(Icon actual) {
        super(actual, IconAssert.class);
    }

    public IconAssert hasValue(String value) {
        isNotNull();
        if (!actual.getValue().equals(value)) {
            failWithMessage("Expected an icon with representation equals to: <%s>%n but was: <%s>.", value, actual.getValue());
        }
        return this;
    }

    public IconAssert isBase64() {
        isNotNull();
        if (!actual.isBase64()) {
            failWithMessage("Expected an icon represented in base64 but was not.");
        }
        return this;
    }

    public IconAssert isNotBase64() {
        isNotNull();
        if (actual.isBase64()) {
            failWithMessage("Expected an icon not represented in base64 but was.");
        }
        return this;
    }
}
