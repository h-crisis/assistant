/**
 * Created by manabu on 2016/09/05.
 */

// Brokerへの接続
//var wsbroker = "broker.ichilab.org";  //mqtt websocket enabled broker
var wsbroker = "broker.h-crisis.jp";  //mqtt websocket enabled broker
var wsport = 80; // port for above
var client = new Paho.MQTT.Client(wsbroker, wsport,
    "H-CRISIS Assistant MQTT " + parseInt(Math.random() * 100, 10));

client.onConnectionLost = function (responseObject) {
    console.log("connection lost: " + responseObject.errorMessage);
};

function publishMsg(topic, msg, retain){
    //client.connect(mqttOptions);
    client.connect({
        timeout: 3,
        onSuccess: function () {
            console.log("mqtt connected");

            message = new Paho.MQTT.Message(msg);
            message.destinationName = topic; // トピックの設定
            message.retained = retain; // retainする
            client.send(message);
            client.disconnect();
            alert('情報を送信しました。画面を閉じます。');
            window.close();
        },
        onFailure: function (message) {
            console.log("Connection failed: " + message.errorMessage);
            alert('圏外です。通信環境を確認してください。');
        }
    })
}
