package org.almibe.codeeditor;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;

import java.net.URI;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CodeMirrorEditor implements CodeEditor {
    private final WebView webView;
    private boolean isEditorInitialized = false; //TODO make atomic var?
    private final Queue<Runnable> queue = new LinkedBlockingQueue<>();
    private ScheduledExecutorService executor;

    public CodeMirrorEditor() {
        webView = new WebView();
    }

    @Override
    public void init(URI editorUri, Runnable... runAfterLoading) {
        queue.addAll(Arrays.asList(runAfterLoading));
        webView.getEngine().load(editorUri.toString());
        webView.getEngine().setOnError(new EventHandler<WebErrorEvent>() {
            @Override
            public void handle(WebErrorEvent event) {
                throw new RuntimeException(event.getException());
            }
        });

        executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture future = executor.scheduleWithFixedDelay(new Init(), 0, 100, TimeUnit.MILLISECONDS);
    }

    public class Init implements Runnable {
        private final String command = "init();";

        @Override
        public void run() {
            Platform.runLater(() -> {
                try {
                    webView.getEngine().executeScript("CodeMirror;");
                    webView.getEngine().executeScript(this.command);
                    executor.shutdown();
                    executor = null;
                    while(!queue.isEmpty()) {
                        Runnable runnable = queue.remove();
                        Platform.runLater(runnable);
                    }
                    isEditorInitialized = true;
                } catch (Exception ex) {
                    ; //Do nothing
                }
            });
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
        return isEditorInitialized;
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

    private void handleQueue() {
        if(isEditorInitialized) {
            while(!queue.isEmpty()) {
                Runnable runnable = queue.remove();
                Platform.runLater(runnable);
            }
        }
    }
}
