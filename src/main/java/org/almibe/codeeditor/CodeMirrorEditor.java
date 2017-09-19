package org.almibe.codeeditor;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class CodeMirrorEditor implements CodeEditor {
    private final WebView webView;
    private AtomicBoolean isEditorInitialized = new AtomicBoolean(false);
    private final Queue<Runnable> queue = new LinkedBlockingQueue<>();
    private ScheduledExecutorService executor;
    private Function<String, List<String>> autoCompleteFunction = s -> new ArrayList<>();

    public CodeMirrorEditor() {
        webView = new WebView();
    }

    @Override
    public void init(Runnable... runAfterLoading) {
        try {
            final URI editorUri = CodeMirrorEditor.class.getResource("editor.html").toURI();
            queue.addAll(Arrays.asList(runAfterLoading));
            webView.getEngine().load(editorUri.toString());
            webView.getEngine().setOnError(event -> {
                throw new RuntimeException(event.getException());
            });

            executor = Executors.newSingleThreadScheduledExecutor();
            ScheduledFuture future = executor.scheduleWithFixedDelay(new Init(), 0, 100, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public class Init implements Runnable {
        private final String command = "init();";

        @Override
        public void run() {
            Platform.runLater(() -> {
                try {
                    webView.getEngine().executeScript("CodeMirror;");

                    JSObject window = (JSObject) webView.getEngine().executeScript("window");
                    window.setMember("injectedVariables", new AutoCompleteVariables());

                    webView.getEngine().executeScript(this.command);
                    executor.shutdown();
                    executor = null;
                    while(!queue.isEmpty()) {
                        Runnable runnable = queue.remove();
                        Platform.runLater(runnable);
                    }
                    isEditorInitialized.set(true);
                } catch (Exception ex) {
                    //throw new RuntimeException(ex); //usually not needed
                }
            });
        }
    }

    public class AutoCompleteVariables {
        public String match(String word) {
            List<String> resultList = autoCompleteFunction.apply(word);
            StringBuilder jsonResult = new StringBuilder("[");
            Iterator<String> iterator = resultList.iterator();
            while(iterator.hasNext()) {
                String value = iterator.next();
                jsonResult.append(JsString.quote(value));
                if (iterator.hasNext()) {
                    jsonResult.append(",");
                }
            }
            jsonResult.append("]");
            return jsonResult.toString();
        }
    }

    @Override
    public String getContent() {
        return (String) webView.getEngine().executeScript("codeMirror.getValue();");
    }

    @Override
    public void setContent(String newContent, boolean markClean) {
        String escapedContent = JsString.quote(newContent);
        Platform.runLater(() -> {
            webView.getEngine().executeScript("codeMirror.setValue(" + escapedContent + ");");
            if (markClean) { this.markClean(); }
        });
    }

    @Override
    public boolean isClean() {
        return (boolean) webView.getEngine().executeScript("codeMirror.isClean();");
    }

    @Override
    public void markClean() {
        webView.getEngine().executeScript("codeMirror.markClean();");
    }

    @Override
    public Position getCursorPosition() {
        String position = (String) webView.getEngine().executeScript("");
        return null;
    }

    @Override
    public void setCursorPosition(Position position) {
        Platform.runLater(() -> {
            webView.getEngine().executeScript("");
        });
    }

    @Override
    public boolean isEditorInitialized() {
        return isEditorInitialized.get();
    }

    @Override
    public Parent getWidget() {
        return this.webView;
    }

    @Override
    public boolean isReadOnly() {
        return (Boolean) webView.getEngine().executeScript("codeMirror.getOption('readOnly');");
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        Platform.runLater(() -> {
            webView.getEngine().executeScript("codeMirror.setOption('readOnly', " + readOnly + ");");
        });
    }

    @Override
    public String getMode() {
        return (String) webView.getEngine().executeScript("codeMirror.getOption('mode');");
    }

    @Override
    public void setMode(String mode) {
        Platform.runLater(() -> {
            webView.getEngine().executeScript("setMode(\"" + mode + "\");");
        });
    }

//    @Override
//    public void includeJSModules(String[] modules, Runnable runnable) {
//        //TODO test this
//        //fetchCodeEditorObject().call("importJSModules", modules, runnable);
//    }

    @Override
    public String getTheme() {
        return (String) webView.getEngine().executeScript("codeMirror.getOption('theme');");
    }

    @Override
    public void setTheme(String theme, String... cssFile) {
        String argument = "'" + theme + "'";
        if(cssFile != null && cssFile.length > 0) {
            String cssFileArgument = "";
            for(String file : cssFile) {
                cssFileArgument += ", '" + file + "'";
            }
            argument += cssFileArgument;
        }
        final String finalArg = argument;
        Platform.runLater(() -> {
            webView.getEngine().executeScript("setTheme(" + finalArg + ");");
        });
    }

    @Override
    public void runWhenReady(Runnable runnable) {
//        if(isEditorInitialized) {
//            runnable.run();
//        } else {
            queue.add(runnable);
            handleQueue();
//        }
    }

    @Override
    public void setAutoCompleteFunction(Function<String, List<String>> autoCompleteFunction) {
        this.autoCompleteFunction = autoCompleteFunction;
    }

    @Override
    public Function<String, List<String>> getAutoCompleteFunction() {
        return this.autoCompleteFunction;
    }

    private void handleQueue() {
        if(isEditorInitialized.get()) {
            while(!queue.isEmpty()) {
                Runnable runnable = queue.remove();
                Platform.runLater(runnable);
            }
        }
    }
}
