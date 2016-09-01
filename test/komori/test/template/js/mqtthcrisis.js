/**
 * Created by manabu on 2016/06/10.
 */
var wsbroker = "broker.titech.ichilab.org";  //mqtt websocket enabled broker
var wsport = 80; // port for above
var client = new Paho.MQTT.Client(wsbroker, wsport,
    "H-CRISIS Assistant Shelter Emergency " + parseInt(Math.random() * 100, 10));

var publishTopic = "";
var publishMessage = "";
var href = window.location.href;
var lat = href.substr(href.indexOf("x="));
lat = lat.substr(2, lat.indexOf(",")-2);
var lon = href.substr(href.indexOf("y="));
lon = lon.substr(2);
var id = href.substr(href.indexOf("ID="));
id = id.substr(3, id.indexOf(",")-3);
id = decodeURI(id);
var name = href.substr(href.indexOf("Name="));
name = name.substr(5);
name = decodeURI(name);

client.onConnectionLost = function (responseObject) {
    console.log("connection lost: " + responseObject.errorMessage);
};
client.onMessageArrived = function (message) {
    console.log(message.destinationName, ' -- ', message.payloadString);
};

var options = {
    timeout: 3,
    onSuccess: function () {
        console.log("mqtt connected");
        // Connection succeeded; subscribe to our topic, you can add multile lines of these
        client.subscribe('/World', {qos: 1});

        //use the below if you want to publish to a topic on connect
        message = new Paho.MQTT.Message(location.search);
        //message = new Paho.MQTT.Message("Hello12");
        message.destinationName = "/World";
        client.send(message);

    },
    onFailure: function (message) {
        console.log("Connection failed: " + message.errorMessage);
    }
};

var submitOnlyOptions = {
    timeout: 3,
    onSuccess: function () {
        console.log("mqtt connected");

        //use the below if you want to publish to a topic on connect
        //message = new Paho.MQTT.Message(location.search);
        message = new Paho.MQTT.Message(publishMessage);
        message.destinationName = publishTopic;
        client.send(message);
        client.disconnect();
        alert('情報を送信しました。画面を閉じます。');
        window.close();
    },
    onFailure: function (message) {
        console.log("Connection failed: " + message.errorMessage);
    }
};

function onlySubmit(topic, msg){
    publishTopic = topic;
    publishMessage = msg;
    client.connect(submitOnlyOptions);
}

function init() {
    client.connect(options);
}
