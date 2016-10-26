/**
 * Created by komori on 2016/07/12.
 */

// var getCSV = d3.dsv(',', 'text/csv; charset=shift_jis');
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


function shelterSearch(){
    // if (document.getElementById('address').value != "") {
    searchPoint = "SH" + document.getElementById('address').value;
    // var ohg = obj.getAttribute('address');

    var getCSV = d3.dsv(',', 'text/csv; charset=utf-8');

    getCSV('shelter/shelter_location.csv', function (data) {
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
            map.getView().setCenter(shelterPlace);
            map.getView().setZoom(15);
            cont = 0;
        } else if (cont == 0) {
            alert("該当する施設がありません！");
        } else {
            cont = 0;
        }
    })};


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
                code = data[i].ecode.substr(0, 3);
                if (code > 99) {
                    code = code.substr(0, 1);
                    if (tdfkNum == code) {
                        hospinfo.innerHTML = hospinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=tdkBtn value=" + i + " onclick=choiceHosp(this)>" + data[i].name1 + "</td></tr>";
                    }
                }
            } else {
                code = data[i].ecode.substr(0, 3);
                if (code < 99) {
                    code = data[i].ecode.substr(0, 1) + data[i].ecode.substr(2, 1);
                    if (tdfkNum == code) {
                        hospinfo.innerHTML = hospinfo.innerHTML + "<tr><td style='font-size:24px;color:white;background-color:#888888;text-align:center' type=button id=tdkBtn value=" + i + " onclick=choiceHosp(this)>" + data[i].name1 + "</td></tr>";
                    }
                }
            }
        }
        document.getElementById('hospinfo').style.display = 'block';
    })};

/*
 function visHoButton() {
 if (document.getElementById('vishospinfo').style.display == 'none') {
 document.getElementById('tdfkinfo').style.display = 'none';
 document.getElementById('hospinfo').style.display = 'none';
 vishospinfo.innerHTML = ""

 var lat, lon, name, city, hospIconNum, hospIcon, iconColor;
 var num8, num4, num2, num1 = 0;

 $(document).ready(function () {
 $.getJSON('http://h-crisis.niph.go.jp/wp-content/uploads/data/medical_status/latest/hcrisis_medical_status.geojson', function (data) {
 var items = [];
 $.each(data.features, function (key, val) {
 $.each(val.geometry, function(g,h){
 if (this.length != 5) {
 items.push[h];
 lon = this[0];
 lat = this[1];
 }
 });
 $.each(val.properties, function(i,j){
 items.push(i,j);
 if (i == "name") {
 name = j;
 } else if (i == "city") {
 city = j;
 }
 if (i == "saigai") {
 num8 = j;
 }
 if (i == "kyukyu") {
 num4 = j;
 }
 if (i == "hibaku") {
 num2 = j;
 }
 if (i == "dmat") {
 num1 = j;
 }
 if (i == "assist") {
 if (j == "要") {
 iconColor = "r";
 } else if (j == "未") {
 iconColor = "g";
 } else {
 iconColor = "b";
 }
 }
 if (i == "mds"){
 if(j != "未入力") {
 iconColor = "b";
 }
 }
 });
 var extent = map.getView().calculateExtent(map.getSize());
 var bottomLeft = ol.proj.transform(ol.extent.getBottomLeft(extent),
 'EPSG:3857', 'EPSG:4326');
 var topRight = ol.proj.transform(ol.extent.getTopRight(extent),
 'EPSG:3857', 'EPSG:4326');
 if (lat <= topRight[1] && lat >= bottomLeft[1]) {
 if (lon <= topRight[0] && lon >= bottomLeft[0]) {
 hospIconNum = (num8 * 8) + (num4 * 4) + (num2 * 2) + (num1 * 1);
 if (iconColor == "r") {
 hospIcon = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/r/" + hospIconNum + ".png";
 } else if (iconColor == "g") {
 hospIcon = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/g/" + hospIconNum + ".png";
 } else {
 hospIcon = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/b/" + hospIconNum + ".png";
 }
 vishospinfo.innerHTML = vishospinfo.innerHTML + "<tr><td style='font-size:20px;color:white;background-color:#888888;text-align:left' type=button id=tdkBtn value=" + lat + ","+ lon + " onclick=choiceHosp(this)>" + name + "<br>" + "<img src=" + hospIcon + "/ width=20 height=20 >" + "(" + city + ")" + "</td></tr>";
 }
 }
 });
 });
 });

 /*
 getCSV('geojson/hcrisis_medical_status.csv', function (data, feature) {
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
 */

function visShButton() {
    if (document.getElementById('vishospinfo').style.display == 'none') {
        vishospinfo.innerHTML = ""

        var lat, lon, name, city, hospIconNum, hospIcon, iconColor;
        var num8, num4, num2, num1 = 0;

        var urlS = pass + 'geojson/shelter.geojson';

        $(document).ready(function () {
            $.getJSON(urlS, function (data) {
                var items = [];
                $.each(data.features, function (key, val) {
                    $.each(val.geometry, function(g,h){
                        if (this.length != 5) {
                            items.push[h];
                            lon = this[0];
                            lat = this[1];
                        }
                    });
                    $.each(val.properties, function(i,j){
                        items.push(i,j);
                        if (i == "name") {
                            name = j;
                        } else if (i == "city") {
                            city = j;
                        }
                        if (i == "saigai") {
                            num8 = j;
                        }
                        if (i == "kyukyu") {
                            num4 = j;
                        }
                        if (i == "hibaku") {
                            num2 = j;
                        }
                        if (i == "dmat") {
                            num1 = j;
                        }
                        if (i == "assist") {
                            if (j == "要") {
                                iconColor = "r";
                            } else if (j == "未") {
                                iconColor = "g";
                            } else {
                                iconColor = "b";
                            }
                        }
                        if (i == "mds"){
                            if(j != "未入力") {
                                iconColor = "b";
                            }
                        }
                    });
                    var extent = map.getView().calculateExtent(map.getSize());
                    var bottomLeft = ol.proj.transform(ol.extent.getBottomLeft(extent),
                        'EPSG:3857', 'EPSG:4326');
                    var topRight = ol.proj.transform(ol.extent.getTopRight(extent),
                        'EPSG:3857', 'EPSG:4326');
                    if (lat <= topRight[1] && lat >= bottomLeft[1]) {
                        if (lon <= topRight[0] && lon >= bottomLeft[0]) {
                            hospIconNum = (num8 * 8) + (num4 * 4) + (num2 * 2) + (num1 * 1);
                            if (iconColor == "r") {
                                hospIcon = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/r/" + hospIconNum + ".png";
                            } else if (iconColor == "g") {
                                hospIcon = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/g/" + hospIconNum + ".png";
                            } else {
                                hospIcon = "http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/img/b/" + hospIconNum + ".png";
                            }
                            vishospinfo.innerHTML = vishospinfo.innerHTML + "<tr><td style='font-size:20px;color:white;background-color:#888888;text-align:left' type=button id=tdkBtn value=" + lat + ","+ lon + " onclick=choiceHosp(this)>" + name + "<br>" + "(" + city + ")" + "</td></tr>";
                        }
                    }
                });
            });
        });

        /*
         getCSV('geojson/hcrisis_medical_status.csv', function (data, feature) {
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
         */

        document.getElementById('vishospinfo').style.display = 'block';
    } else {
        document.getElementById('vishospinfo').style.display = 'none';
        vishospinfo.innerHTML = "";
    }
}

$(document).ready( function(){
    cont = 2;
    shelterSearch()
});