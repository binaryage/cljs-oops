const page = require('webpage').create();
const fs = require('fs');
const system = require('system');

var scriptsToRun = system.args[1];
if (scriptsToRun) {
    scriptsToRun = scriptsToRun.split(",")
}

function printSeparator() {
    console.log("-----------------------------------------------------------------");
}

var continuation;

page.onConsoleMessage = function(message) {
    var m = message.match(/^TESTS DONE \((\d+)\)$/);
    if (m) {
        var failuresCount = parseInt(m[1], 10);
        continuation(failuresCount ? 100 : 0);
    } else {
        console.log(message);
    }
};

var exitCode = 0;

const allScripts = [
    "basic_onone",
    "basic_oadvanced_core",
    "basic_oadvanced_goog"];

const scripts = scriptsToRun || allScripts;

const runScript = function(name, cb) {
    printSeparator();
    const url = "main.html";
    const settings = {headers: {"ScriptToExecute": name}};
    const scriptPath = ".compiled/" + name + "/main.js";
    page.open(url, settings, function(status) {
        if (status != "success") {
            console.log('Failed to open "' + url + '" (' + status + ')');
            cb(1);
        } else {
            console.log("executing build: " + name);

            if (!fs.exists(scriptPath)) {
                console.log("  does not exist => skipping");
                cb(42);
            } else {
                continuation = cb;
                page.injectJs(scriptPath);
            }
        }
    });
};

var currentScriptIndex = -1;

const runNextScript = function() {
    currentScriptIndex++;
    const scriptName = scripts[currentScriptIndex];
    if (!scriptName) {
        printSeparator();
        if (exitCode == 0) {
            console.log("All tests passed.");
        } else {
            console.log("Some tests failed.");
        }
        phantom.exit(exitCode);
    } else {
        runScript(scriptName, function(resultCode) {
            if (!exitCode) {
                exitCode = resultCode;
            }
            runNextScript();
        });
    }
};

runNextScript();
