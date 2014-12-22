define(["codemirror/lib/codemirror", "codemirror/addon/display/fullscreen", "codemirror/addon/selection/active-line",
            "codemirror/addon/mode/loadmode", "codemirror/mode/meta"], function(CodeMirror) {
    var codeMirror = CodeMirror(document.body);
    codeMirror.setOption("lineNumbers", true);
    codeMirror.setOption("fullScreen", true);
    codeMirror.setOption("styleActiveLine", true);
    CodeMirror.modeURL = "codemirror/mode/%N/%N.js";

    //modified from demo/loadmode.html
    function changeMode(val) {
        var mode, spec;
        if (/\//.test(val)) {
          var info = CodeMirror.findModeByMIME(val);
          if (info) {
            mode = info.mode;
            spec = val;
          }
        } else {
          mode = spec = val;
        }
        if (mode) {
          codeMirror.setOption("mode", spec);
          CodeMirror.autoLoadMode(codeMirror, mode);
        }
    }

    function loadCss(theme) {
        var sheets = document.styleSheets;
        var exists = false;
        for (var sheet in sheets) {
          if (sheet && sheet.href && sheet.href.endsWith("/" + theme + ".css")) {
            exists = true;
            break;
          }
        }
        if (!exists) {
          var link = document.createElement("link");
          link.rel = "stylesheet";
          link.type = "text/css";
          link.href = "codemirror/theme/" + theme + ".css";
          document.getElementsByTagName("head")[0].appendChild(link);
        }
    }

    return {
        "codeMirror": codeMirror,
        setTheme: function() {
            var theme = arguments[0];
            if (arguments.length == 1) {
                loadCss(theme);
            } else {
                for (var index = 0; index < arguments.length - 1; index++) {
                    loadCss(arguments[index+1]);
                }
            }
            codeMirror.setOption("theme", theme);
        },

        importJSModules: function(modules, callback) {
            require(modules, function() {
                callback.run();
            });
        },

        setMode: function(modeName) {
            var mimeModes = Object.keys(CodeMirror.mimeModes);
            var modeNames = Object.keys(CodeMirror.modes);
            var allModeNames = mimeModes.concat(modeNames);
            if (allModeNames.indexOf(modeName) >= 0) {
              codeMirror.setOption("mode", modeName);
            } else {
              changeMode(modeName);
            }
        }
    }
});