package org.almibe.codeeditor;

import javafx.scene.Parent;

import java.util.List;
import java.util.function.Function;

public interface CodeEditor {
    String getContent();
    void setContent(String newContent, boolean markClean);
    boolean isClean();
    void markClean();
    Position getCursorPosition();
    void setCursorPosition(Position position);
    boolean isEditorInitialized();
    void init(Runnable... runAfterLoading);
    Parent getWidget();
    boolean isReadOnly();
    void setReadOnly(boolean readOnly);
    String getMode();
    void setMode(String mode);
    //TODO add includeJSModules back in later
    //void includeJSModules(String[] modules, Runnable runnable);
    String getTheme();
    void setTheme(String theme, String... cssFile);
    void runWhenReady(Runnable runnable);
    void setAutoCompleteFunction(Function<String, List<String>> autoCompleteFunction);
    Function<String, List<String>> getAutoCompleteFunction();

    public interface Position {
        int getLine();
        int getCharacter();
    }
}
