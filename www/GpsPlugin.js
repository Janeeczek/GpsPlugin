
var exec = require('cordova/exec');

var GpsPlugin = (function () {
    function GpsPlugin() {
        var _this = this;
        var successCallback;
        var errorCallback;
        cordova.exec(successCallback, errorCallback, "GpsPlugin", "test", []);
        console.log('GpsPlugin: has been created');
    }
    GpsPlugin.prototype.start = function (successCallback, errorCallback) {
            return cordova.exec(successCallback, errorCallback, "GpsPlugin", "start", []);
        };
        GpsPlugin.prototype.stop = function (successCallback, errorCallback) {
            return cordova.exec(successCallback, errorCallback, "GpsPlugin", "stop", []);
        };
    

    return GpsPlugin;
}());

var NGPS = new GpsPlugin();

module.exports = NGPS;
