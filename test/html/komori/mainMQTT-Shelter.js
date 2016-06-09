(function() {
  'use strict';

  // ボタン系
  var btnDisconnect = document.querySelector('.btn-disconnect');
  var btnPublish = document.querySelector('.btn-publish');

  // 入力系
  var inputTopicPub = "/hcrisis/shelter/emergency";
  var inputBrokerWs = "http://broker.titech.ichilab.org:80";
    var inputMessage = "";
    var input = new Array(4);
    var radioList = new Array(7);
    for( var i = 0; i < 4; i++) {
        var arrayValue = ".inputvalue00" + i;
    input[i] = document.querySelector(arrayValue);
  }

  var messages = document.querySelector('.messages');
  var client, appendMessage, clearMessages;
    
  btnDisconnect.addEventListener('click', function(e) {
    e.preventDefault();
    client && client.end();
    window.open('about:blank','_self').close();
  });

  btnPublish.addEventListener('click', function(e, message) {
    e.preventDefault();
      for( var i = 0; i < 4; i++) {
          inputMessage = inputMessage + input[i].value + ",";
      }
      for( var i = 0; i < 7; i++) {
          var arrayValue = "q" + i;
          console.log(arrayValue);
          var tmpList = document.getElementsByName(arrayValue);
          for(var j=0; j<tmpList.length; j++) {
              if(tmpList[j].checked) {
                  console.log(tmpList[j]);
                  inputMessage = inputMessage + tmpList[j].value + ",";
                  continue;
              }
          }
          
          
      }

    client = mows.createClient(inputBrokerWs);
    client && client.publish(inputTopicPub, inputMessage);
    alert("送信しました!")
      inputMessage = "";
  });

  window.addEventListener('beforeunload', function(e) {
    e.preventDefault();
    client && client.end();
  }, false);

})();