(function() {
  'use strict';

  // ボタン系
  var btnDisconnect = document.querySelector('.btn-disconnect');
  var btnPublish = document.querySelector('.btn-publish');

  // 入力系
    var inputTopicPub = "/hcrisis/shelter/emergency";
    var inputBrokerWs = "http://broker.titech.ichilab.org:80";
    var href = window.location.href;
    var lat = href.substr(href.indexOf("x="));
    lat = lat.substr(2, lat.indexOf(",")-2);
    var lon = href.substr(href.indexOf("y="));
    lon = lon.substr(2, lon.indexOf(",")-2);
    var id = href.substr(href.indexOf("ID="));
    id = id.substr(3, id.indexOf(",")-3);
    id = decodeURI(id);
    document.getElementById('inputCode').value= id;
    document.getElementById('inputCode').disabled = 'true';
    var name = href.substr(href.indexOf("Name="));
    name = name.substr(5);
    name = decodeURI(name);
    document.getElementById('inputName').value= name;
    document.getElementById('inputName').disabled = 'true';

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
      inputMessage = lat;
      inputMessage = inputMessage + "," + lon;
      inputMessage = inputMessage + "," + id;
      inputMessage = inputMessage + "," + name;
      for( var i = 2; i < 4; i++) {
          inputMessage = inputMessage  + "," + input[i].value;
      }
      for( var i = 0; i < 7; i++) {
          var arrayValue = "q" + i;
          var tmpList = document.getElementsByName(arrayValue);
          for(var j=0; j<tmpList.length; j++) {
              if(tmpList[j].checked) {
                  inputMessage = inputMessage + "," + tmpList[j].value;
                  continue;
              }
          }
      }
    client = mows.createClient(inputBrokerWs);
    client && client.publish(inputTopicPub, inputMessage);
    alert("送信しました!");
      inputMessage = "";
  });

  window.addEventListener('beforeunload', function(e) {
    e.preventDefault();
    client && client.end();
  }, false);

})();