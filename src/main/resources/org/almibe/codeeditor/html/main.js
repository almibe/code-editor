require(["codemirror-4.8/lib/codemirror", "codemirror-4.8/addon/display/fullscreen", "codemirror-4.8/addon/selection/active-line",
    "codemirror-4.8/addon/mode/loadmode", "codemirror-4.8/mode/meta"],
    function(CodeMirror) {
        var codeMirror = CodeMirror(document.body);
        codeMirror.setOption("lineNumbers", true);
        codeMirror.setOption("fullScreen", true);
        codeMirror.setOption("styleActiveLine", true);
        CodeMirror.modeURL = "codemirror-4.8/mode/%N/%N.js";
    }
);