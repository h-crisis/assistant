/**
 * Created by komori on 2016/08/03.
 */


var mqtt;
var reconnectTimeout = 2000;
var contentIshi = new Array(2);
var contentHokenjo = new Array(2);
var contentEmergency = new Array(2);
function MQTTconnect() {
    if (typeof path == "undefined") {
        path = '/mqtt';
    }
    mqtt = new Paho.MQTT.Client(
        host,
        port,
        path,
        "web_" + parseInt(Math.random() * 100, 10)
    );
    var options = {
        timeout: 3,
        useSSL: useTLS,
        cleanSession: cleansession,
        onSuccess: onConnect,
        onFailure: function (message) {
            $('#status').val("Connection failed: " + message.errorMessage + "Retrying");
            setTimeout(MQTTconnect, reconnectTimeout);
        }
    };
    mqtt.onConnectionLost = onConnectionLost;
    mqtt.onMessageArrived = onMessageArrived;
    if (username != null) {
        options.userName = username;
        options.password = password;
    }
    // console.log("Host="+ host + ", port=" + port + ", path=" + path + " TLS = " + useTLS + " username=" + username + " password=" + password);
    mqtt.connect(options);
}
function onConnect() {
    $('#status').val('Connected to ' + host + ':' + port + path);
    // Connection succeeded; subscribe to our topic
    mqtt.subscribe(topic, {qos: 0});
    $('#topic').val(topic);
}
function onConnectionLost(response) {
    setTimeout(MQTTconnect, reconnectTimeout);
    $('#status').val("connection lost: " + ". Reconnecting");
};
function onMessageArrived(message) {
    var topic = message.destinationName;
    var code = topic.split('/')
    // if(code[2] == "Reload") {
};
$(document).ready(function() {
    MQTTconnect();
});

function handleDownloadEmergency() {
    var bom = new Uint8Array([0xEF, 0xBB, 0xBF]);
    var blob = new Blob([ bom, contentEmergency ], { "type" : "text/csv" });
    if (window.navigator.msSaveBlob) {
        window.navigator.msSaveBlob(blob, "emergency.csv");
        // msSaveOrOpenBlobの場合はファイルを保存せずに開ける
        window.navigator.msSaveOrOpenBlob(blob, "emergency.csv");
    } else {
        document.getElementById("downloadEmergency").href = window.URL.createObjectURL(blob);
    }
}


function handleDownloadIshii() {
    var bom = new Uint8Array([0xEF, 0xBB, 0xBF]);
    var blob = new Blob([ bom, contentIshi ], { "type" : "text/csv" });
    if (window.navigator.msSaveBlob) {
        window.navigator.msSaveBlob(blob, "ishii.csv");
        // msSaveOrOpenBlobの場合はファイルを保存せずに開ける
        window.navigator.msSaveOrOpenBlob(blob, "ishii.csv");
    } else {
        document.getElementById("downloadIshii").href = window.URL.createObjectURL(blob);
    }
}

function handleDownloadHokenjo() {
    var bom = new Uint8Array([0xEF, 0xBB, 0xBF]);
    var blob = new Blob([ bom, contentHokenjo ], { "type" : "text/csv" });
    if (window.navigator.msSaveBlob) {
        window.navigator.msSaveBlob(blob, "hokenjo.csv");
        // msSaveOrOpenBlobの場合はファイルを保存せずに開ける
        window.navigator.msSaveOrOpenBlob(blob, "hokenjo.csv");
    } else {
        document.getElementById("downloadHokenjo").href = window.URL.createObjectURL(blob);
    }
}
