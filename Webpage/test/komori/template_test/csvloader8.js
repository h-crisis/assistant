/**
 * Created by komori on 2016/07/12.
 */

var getCSV = d3.dsv(',', 'text/csv; charset=shift_jis');
var tdfkNum;
var lat;
var lon;
var hospLatLon;
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
        for (var i = 0; i < data.length; i++) {
            if (tdfkNum < 10) {
                code = data[i].code.substr(0, 3);
                if (code > 99) {
                    code = code.substr(0, 1);
                    if (tdfkNum == code) {
                        hospinfo.innerHTML = hospinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=tdkBtn value=" + i + " onclick=choiceHosp(this)>" + data[i].name1 + "</td></tr>";
                    }
                }
            } else {
                code = data[i].code.substr(0, 3);
                if (code < 99) {
                    code = data[i].code.substr(0, 1) + data[i].code.substr(2, 1);
                    if (tdfkNum == code) {
                        hospinfo.innerHTML = hospinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=tdkBtn value=" + i + " onclick=choiceHosp(this)>" + data[i].name1 + "</td></tr>";
                    }
                }
            }
        }
        document.getElementById('hospinfo').style.display = 'block';
    })};


function visHoButton() {
    if (document.getElementById('vishospinfo').style.display == 'none') {
        document.getElementById('tdfkinfo').style.display = 'none';
        document.getElementById('hospinfo').style.display = 'none';
        vishospinfo.innerHTML = ""
        getCSV('geojson/hcrisis_medical_status.csv', function (data) {
            for (var i = 0; i < data.length; i++) {
                var emisLat = data[i].lat;
                var emisLon = data[i].lon;
                var extent = map.getView().calculateExtent(map.getSize());
                var bottomLeft = ol.proj.transform(ol.extent.getBottomLeft(extent),
                    'EPSG:3857', 'EPSG:4326');
                var topRight = ol.proj.transform(ol.extent.getTopRight(extent),
                    'EPSG:3857', 'EPSG:4326');
                if (emisLat <= topRight[1] && emisLat >= bottomLeft[1]) {
                    if (emisLon <= topRight[0] && emisLon >= bottomLeft[0]) {
                        vishospinfo.innerHTML = vishospinfo.innerHTML + "<tr><td style='font-size:20px;color:white;background-color:#888888;text-align:left' type=button id=tdkBtn value=" + i + " onclick=choiceHosp(this)>" + data[i].name1 + "(" + data[i].city_name + ")" + "</td></tr>";
                    }
                }
            }
        });
        document.getElementById('vishospinfo').style.display = 'block';
    } else {
        document.getElementById('vishospinfo').style.display = 'none';
        vishospinfo.innerHTML = "";
    }
}


