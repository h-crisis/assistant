(function() {
  'use strict';

  // ボタン系
  var btnConnect = document.querySelector('.btn-connect');
  var btnDisconnect = document.querySelector('.btn-disconnect');
  var btnPublish = document.querySelector('.btn-publish');
  var btnSubscribe = document.querySelector('.btn-subscribe');
  var btnUnsubscribe = document.querySelector('.btn-unsubscribe');
  var btnClear = document.querySelector('.btn-clear');

  // 入力系
  var inputTopicPub = document.querySelector('.input-topic-pub');
  var inputMessage001 = document.querySelector('.input-message-001');
  var inputMessage002 = document.querySelector('.input-message-002');
  var inputTopicSub = document.querySelector('.input-topic-sub');
  var inputBrokerWs = "http://broker.titech.ichilab.org:80";

  var messages = document.querySelector('.messages');

  var client, appendMessage, clearMessages;
  


  btnDisconnect.addEventListener('click', function(e) {
    e.preventDefault();
    client && client.end();
  });

  btnPublish.addEventListener('click', function(e, message) {
    e.preventDefault();
    client = mows.createClient(inputBrokerWs);
    console.log(message);
    client && client.publish(inputTopicPub.value, inputMessage001.value, inputMessage002.value);
    var element = document.createElement('p');
    var string = document.createTextNode(message);
    element.appendChild(string);
    messages.appendChild(element);
  });

  btnSubscribe.addEventListener('click', function(e) {
    e.preventDefault();
    client && client.subscribe(inputTopicSub.value);
    appendMessage('subscribe -> ' + inputTopicSub.value);
  });

  btnUnsubscribe.addEventListener('click', function(e) {
    e.preventDefault();
    client && client.unsubscribe(inputTopicSub.value);
    appendMessage('unsubscribe -> ' + inputTopicSub.value);
  });

  btnClear.addEventListener('click', function(e) {
    e.preventDefault();
    clearMessages();
  });

  appendMessage = function(message) {
    var element = document.createElement('p');
    var string = document.createTextNode(message);
    element.appendChild(string);
    messages.appendChild(element);
    console.log(string);
    console.log(element);
    /*ここのファンクションが*/
  }

  clearMessages = function() {
    var count = messages.childNodes.length;
    for(var i=0; i<count; i++) {
      messages.removeChild(messages.firstChild);
    }
  }

})();