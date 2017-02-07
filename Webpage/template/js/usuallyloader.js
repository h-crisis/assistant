/**
 * Created by komori on 2017/02/07.
 */

var tdfkNum;
var lat;
var lon;
var cont = 0;

function hoIndexButton() {
    if (document.getElementById( 'tdfkinfo' ).style.display == 'none') {
        document.getElementById('vishospinfo').style.display = 'none';
        var i = 0;
        var tdfk =
            ['北海道', '青森県', '岩手県', '宮城県', '秋田県', '山形県', '福島県', '茨城県', '栃木県', '群馬県', '埼玉県',
                '千葉県', '東京都', '神奈川県', '新潟県', '富山県', '石川県', '福井県', '山梨県', '長野県', '岐阜県', '静岡県', '愛知県', '三重県',
                '滋賀県', '京都府', '大阪府', '兵庫県', '奈良県', '和歌山県', '鳥取県', '島根県', '岡山県', '広島県', '山口県',
                '徳島県', '香川県', '愛媛県', '高知県', '福岡県', '佐賀県', '長崎県', '熊本県', '大分県', '宮崎県', '鹿児島県', '沖縄県']
        for (i = 0; i < 47; i++) {
            tdfkinfo.innerHTML = tdfkinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=tdfkBtn value=" + (i + 1) + " onclick=choiceTdfk(this)>" + tdfk[i] + "</td></tr>";
        }
        document.getElementById('tdfkinfo').style.display = 'block';
        cont ++;
        return document.getElementById('tdfkBtn').value
    } else {
        tdfkinfo.innerHTML = "";
        document.getElementById( 'tdfkinfo' ).style.display = 'none';
        document.getElementById( 'hospinfo' ).style.display = 'none';
        cont++;
    }
};

// enterキー以外ではsubmitさせない
function onlyEnter(){
    //Enterキー以外なら何もしない
    if(window.event.keyCode!=13) {
        return false
    } else {
        shelterSearch()
    }
}

function HCSearch(){
    // if (document.getElementById('address').value != "") {
    searchPoint = document.getElementById('address').value;
    // var ohg = obj.getAttribute('address');

    var getCSV = d3.dsv(',', 'text/csv; charset=utf-8');
    getCSV('healthcenter/shelter_location.csv', function (data) {
        // dataを処理する

        for (i = 0; i < data.length; i++) {
            code = data[i].scode.substr(0,11);
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
            map.getView().setCenter(shelterPlace);
            map.getView().setZoom(15);
            cont = 0;
            document.getElementById('popup').style.display = 'block';
            overlayPopup.setPosition(shelterPlace);
        } else if (cont == 0) {
            alert("該当する施設がありません！");
        } else {
            cont = 0;
        }
    })};