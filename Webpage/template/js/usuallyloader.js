/**
 * Created by komori on 2017/02/07.
 */

var tdfkNum;
var lat;
var lon;
var cont = 0;

// enterキー以外ではsubmitさせない
function onlyEnter(){
    //Enterキー以外なら何もしない
    if(window.event.keyCode!=13) {
        return false
    } else {
        HCSearch()
    }
}

function HCSearch(){
    // if (document.getElementById('address').value != "") {
    searchPoint = document.getElementById('address').value;
    // var ohg = obj.getAttribute('address');

    var getCSV = d3.dsv(',', 'text/csv; charset=utf-8');
    getCSV('healthcenter/HCList.csv', function (data) {
        // dataを処理する

        cont = 0;
        for (i = 0; i < data.length; i++) {
            code = data[i].hcode.substr(0,11);
            if (code == searchPoint) {
                lat = parseFloat(data[i].lat.substr(0,12));
                lon = parseFloat(data[i].lon.substr(0,12));
                var shelterPlace = [lon,lat];
                cont = 1
            }
        }if(cont == 1){
            document.getElementById('info').innerHTML = "";
            document.getElementById('infoHeader').style.display = 'none';
            document.getElementById('info').style.display = 'none';
            document.getElementById('popup').style.display = 'none';
            document.getElementById('tab001').style.display = 'none';
            document.getElementById('tab002').style.display = 'none';
            document.getElementById('tab003').style.display = 'none';
            cont = 0;
            document.getElementById('popup').style.display = 'block';
            overlayPopup.setPosition(shelterPlace);
            map.getView().setCenter(shelterPlace);
            map.getView().setZoom(15);
            console.log(shelterPlace);
        } else if (cont == 0) {
            alert("該当する施設がありません！");
        } else {
            cont = 0;
        }
    })};

// 右上のアイコンを押した時の挙動
function ninedot(){
    alert("準備中")
}