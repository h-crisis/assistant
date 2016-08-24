/**
 * Created by komori on 2016/07/12.
 */

var getCSV = d3.dsv(',', 'text/csv; charset=shift_jis');
var tdfkNum;
var mareaNum;
var lat;
var lon;
var hospLatLon;
var cont = 0;
var tdfk =
    ['北海道', '青森県', '岩手県', '宮城県', '秋田県', '山形県', '福島県', '茨城県', '栃木県', '群馬県', '埼玉県',
        '千葉県', '東京都', '神奈川県', '新潟県', '富山県', '石川県', '福井県', '山梨県', '長野県', '岐阜県', '静岡県', '愛知県', '三重県',
        '滋賀県', '京都府', '大阪府', '兵庫県', '奈良県', '和歌山県', '鳥取県', '島根県', '岡山県', '広島県', '山口県',
        '徳島県', '香川県', '愛媛県', '高知県', '福岡県', '佐賀県', '長崎県', '熊本県', '大分県', '宮崎県', '鹿児島県', '沖縄県'];
var marea = new Array(47);
marea[0] = ['101:南渡島','102:南檜山','103:北渡島檜山','104:札幌','105:後志','106:南空知','107:中空知','108:北空知','109:西胆振','110:東胆振','111:日高','112:上川中部','113:上川北部',
    '114:富良野','115:留萌','116:宗谷','117:北網','118:遠紋','119:十勝','120:釧路','121:根室'];


function hoIndexButton() {
    if (document.getElementById( 'tdfkinfo' ).style.display == 'none') {
        document.getElementById('vishospinfo').style.display = 'none';
        var i = 0;
        for (i = 0; i < 47; i++) {
            tdfkinfo.innerHTML = tdfkinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=tdfkBtn value=" + i + " onclick=choiceTdfk(this)>" + tdfk[i] + "</td></tr>";
        }
        document.getElementById('tdfkinfo').style.height
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

function choiceTdfk(obj) {
    tdfkNum = obj.getAttribute('value');

    getCSV('geojson/hcrisis_medical_status.csv', function (data) {
        // dataを処理する

        hospinfo.innerHTML = "";
        var code = "";
        var aa = 0;
        var bb = 0;
        var text = "";

        /*
         if(tdfkNum == 0) {
         for (var i = 0; i < 21; i++) {
         code = marea[0][i].substr(0,3);
         hospinfo.innerHTML = hospinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=mdsBtn value=" + code + " onclick=choiceMds(this)>" + marea[0][i] + "</td></tr>";
         }
         }
         */
        hospinfo.innerHTML =

            "<form action='' method='post'> + <p>国を選択して下さい</p>" +
            "<select name='country'>" +
            "<option value='Japan' selected='selected' class='msg'>1番目の要素を選択して下さい</option>" +
            "<option value='Japan' class='japan'>日本</option>" +
            "<option value='America' class='America'>アメリカ</option>" +
            "<option value='Australia' class='Australia'>オーストラリア</option>" +
            "</select>" +

            "<p>都市を選択して下さい</p>" +
            "<select name='city'>" +
            "<option value='Japan' selected='selected' class='msg'>都市を選択して下さい</option>" +
            "<option value='Tokyo' class='japan'>東京</option>" +
            "<option value='Kyoto' class='japan'>京都</option>" +
            "<option value='Osaka' class='japan'>大阪</option>" +
            "<option value='NY' class='America'>ニューヨーク</option>" +
            "<option value='LA' class='America'>ロサンゼルス</option>" +
            "<option value='Sydney' class='Australia'>シドニー</option>" +
            "</select>" +

            "</form>" +

            "<button id='abb' style='' value='>送信</button>" +
            "<label><input id='aff' value=''></label>"

        document.getElementById('hospinfo').style.display = 'block';
    })};

function choiceMds(obj) {
    mareaNum = obj.getAttribute('value');

    getCSV('geojson/hcrisis_medical_status.csv', function (data) {

        for (var i = 0; i < data.length; i++){
            if (data[i].marea_code == mareaNum) {
                console.log(data[i].name1)

            }
        }
    })}

function visHoButton() {
    if (document.getElementById('vishospinfo').style.display == 'none') {
        document.getElementById('tdfkinfo').style.display = 'none';
        document.getElementById('hospinfo').style.display = 'none';
        var extent = map.getView().calculateExtent(map.getSize());
        var bottomLeft = ol.proj.transform(ol.extent.getBottomLeft(extent),
            'EPSG:3857', 'EPSG:4326');
        var topRight = ol.proj.transform(ol.extent.getTopRight(extent),
            'EPSG:3857', 'EPSG:4326');
        getCSV('geojson/hcrisis_medical_status.csv', function (data) {
            for (var i = 0; i < data.length; i++) {
                var emisLat = data[i].緯度;
                var emisLon = data[i].経度;
                if (emisLat <= topRight[1] && emisLat >= bottomLeft[1]) {
                    if (emisLon <= topRight[0] && emisLon >= bottomLeft[0]) {
                        vishospinfo.innerHTML = vishospinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=tdkBtn value=" + i + " onclick=choiceHosp(this)>" + data[i].医療機関名 + "</td></tr>";
                    }
                }
            }
        });
        document.getElementById('vishospinfo').style.display = 'block';

    } else{
        document.getElementById('vishospinfo').style.display = 'none';
        vishospinfo.innerHTML = "";
    }
}
