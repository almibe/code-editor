package org.almibe.codearea;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public interface CodeArea {
    StringProperty contentProperty();
    BooleanProperty readOnly();
    StringProperty modeProperty();
    StringProperty themeProperty();
}
