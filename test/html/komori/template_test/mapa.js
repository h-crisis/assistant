/**
 * Created by komori on 2016/06/14.
 */

var additionalmapOsm = new ol.source.MapQuest({ layer: 'osm' });
var osmQuestLayer = new ol.layer.Tile({
    source: additionalmapOsm,
    opacity: 1
});
var additionalmapSat = new ol.source.MapQuest({ layer: 'sat' });
var satQuestLayer = new ol.layer.Tile({
    source: additionalmapSat,
    opacity: 1
});
var additionalmapHyb = new ol.source.MapQuest({ layer: 'hyb' });
var hybQuestLayer = new ol.layer.Tile({
    source: additionalmapHyb,
    opacity: 1
});


var basemap = new ol.source.XYZ({
    url : 'http://cyberjapandata.gsi.go.jp/xyz/std/{z}/{x}/{y}.png',
    attribution  : "<a href='http://www.gsi.go.jp/kikakuchousei/kikakuchousei40182.html' target='_blank'>国土地理院</a>"
});
var baseLayer = new ol.layer.Tile({
    source: basemap,
    opacity: 1
});
var map = new ol.Map({
    layers: [baseLayer, osmQuestLayer, satQuestLayer, hybQuestLayer
    ],
    target: 'map',
    view: new ol.View({
        center: ol.proj.transform([134.261129, 33.574460, 13], 'EPSG:4326', 'EPSG:3857'),
        zoom: 11,
        minZoom: 7,
        maxZoom: 15
    }),
    controls: ol.control.defaults({
        attributionOptions: ({
            collapsible: false
        })
    }).extend([
        new ol.control.ZoomSlider(),
        new ol.control.ScaleLine()
    ])
});

var goHypoCenter = document.getElementById('goToHypoCenter');
goHypoCenter.addEventListener('click', goHypoButton);
function goHypoButton() {
    var nowLatLon = document.getElementById('latlonCurr').value;
    var nowLatLon = nowLatLon.split(',');
    var latNow = nowLatLon[1].substr(0,8);
    var lonNow = nowLatLon[0].substr(0,8);
    latNow = latNow * 21481508.34 / 180;
    lonNow = lonNow * 20037508.34 / 180;
    console.log(latNow,lonNow);
    map.getView().setCenter([lonNow,latNow]);
    map.getView().setZoom(12);
}

var flagSelected = false ;
var lon, lat;
var goFacility = document.getElementById('goToSelect');
goFacility.addEventListener('click', function(evt) {

    if (flagSelected == true) {
        console.log(flagSelected);
        map.getView().setCenter([lon,lat]);
        map.getView().setZoom(14);
    }
});

var mapSwitch   = document.getElementById('map-vis');
osmQuestLayer.setVisible(false);
satQuestLayer.setVisible(false);
hybQuestLayer.setVisible(false);
mapSwitch.addEventListener('click', mapButton);
var mapcount = 0;
function mapButton() {
    if (mapcount%3==0) {
        osmQuestLayer.setVisible(true);
        satQuestLayer.setVisible(false);
        baseLayer.setVisible(false);
        this.style.backgroundColor = "#86B81B";
        mapcount++;
    } else if (mapcount%3==1) {
        osmQuestLayer.setVisible(false);
        satQuestLayer.setVisible(true);
        baseLayer.setVisible(false);
        this.style.backgroundColor = "blue";
        mapcount++;
    } else {
        osmQuestLayer.setVisible(false);
        satQuestLayer.setVisible(false);
        baseLayer.setVisible(true);
        this.style.backgroundColor = "whitesmoke";
        mapcount++;
    }
};

map.addLayer(layerSI);
//map.addLayer(shltrJPN);
map.addLayer(hinanLayer1);
map.addLayer(hinanLayer4);
map.addLayer(hinanLayer14);
map.addLayer(officeLayer);
map.addLayer(hospLayer);
layerSI.setVisible(false);
//shltrJPN.setVisible(false);
hinanLayer1.setVisible(false);
hinanLayer4.setVisible(false);
hinanLayer14.setVisible(false);
officeLayer.setVisible(false);
hospLayer.setVisible(false);

var distribOnOff   = document.getElementById('distribution-vis');
distribOnOff.addEventListener('click', siButton);


var hinanOnOff1   = document.getElementById('hinan1-vis');
hinanOnOff1.addEventListener('click', hinanButton1);

/*
var chs = 0;
function hinanButton1() {
    if (chs % 2 == 0) {
        shltrJPN.setVisible(true);
        this.style.backgroundColor = "green";
        chs++;
    } else if (chs % 2 == 1) {
        shltrJPN.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
        chs++;
    }
};
*/

var chs = 0;
var day = 0;
function hinanButton1() {
    if (chs%4 == 0) {
        day = 1;
        hinanLayer1.setVisible(true);
        hinanLayer4.setVisible(false);
        hinanLayer14.setVisible(false);
        this.style.backgroundColor = "green";
        chs++;
    } else if (chs%4 == 1) {
        day = 4;
        hinanLayer1.setVisible(false);
        hinanLayer4.setVisible(true);
        hinanLayer14.setVisible(false);
        this.style.backgroundColor = "pink";
        chs++;
    } else if (chs%4 == 2) {
        day = 14;
        hinanLayer1.setVisible(false);
        hinanLayer4.setVisible(false);
        hinanLayer14.setVisible(true);
        this.style.backgroundColor = "yellow";
        chs++;
    } else {
        day = 0;
        hinanLayer1.setVisible(false);
        hinanLayer4.setVisible(false);
        hinanLayer14.setVisible(false);
        this.style.backgroundColor = "whitesmoke";
        chs++;
    }
};

var officeOnOff   = document.getElementById('office-vis');
officeOnOff.addEventListener('click', officeButton);

var hospOnOff   = document.getElementById('hospital-vis');
hospOnOff.addEventListener('click', hospButton);

var overlayInfo = new ol.Overlay({
    element: document.getElementById('overlayInfo'),
    positioning: 'top-left'
});

var displayFeatureInfo = function(pixel) {
    var feature = map.forEachFeatureAtPixel(pixel, function(feature) {
    })};

// 選択したアイコンに代替アイコンをオーバーレイさせる
function makeMarkerOverlay(imgSrc, coordinate) {
    var imgElement = document.createElement('img');
    imgElement.setAttribute('src', imgSrc);
    var markerOverlay = new ol.Overlay({
        element: imgElement,
        position: coordinate,
        positioning: 'center-center'
    });
    return markerOverlay;
};
// 座標変換系
function convertCoordinate(longitude, latitude) {
    return ol.proj.transform([longitude, latitude], "EPSG:4326","EPSG:3857");
}

map.on('click', function(evt) {
    displayFeatureInfo(evt.pixel);
    var coordinate = evt.coordinate;
    var pixel = map.getPixelFromCoordinate(coordinate);
    var info = document.getElementById('info');
    document.getElementById('info').innerHTML = '';
    var labelB = '';
    var labelC = '';
    var labelD = '';
    var labelHosp = new Array(5)
    var labelHinan = new Array(80);
    var label = new Array(10);
    var labelShltrJPN = new Array(20);
    var labelSelf = '';
    var arrayH14 =
        ['男性', '女性', '高齢者', '身体障害者', '乳幼児', '妊婦', '自立歩行不', 'その他要', '人工透析', '人工呼吸器', '電気',
            '水', 'ガス', '通信', '換気', '暖房', 'トイレ', '汚物', 'ゴミ', '食品', '衛生', '飲料水', '食事', '有無と方法',
            '医師', '看護師', '薬剤師', '事務', '総数', 'インフルエンザ', '呼吸器疾患', '呼吸困難', '発熱', '下痢', '嘔気・嘔吐',
            '発疹', '不眠・不安', '精神科疾患', '病院搬送', '高血圧', '糖尿病', '潰瘍性大腸炎', 'パーキンソン病', '備考']
    var arrayShltr = ['避難所コード','避難所名','都道府県','市区町村','住所']
    var labelHosp = new Array(10);
    var arrayHosp =
            ['都道府県', '二次医療圏', '支援要請', '医療派遣ステータス', '医療機関名', 'チーム数', '最終更新日時', '医師出勤状況'];
    var i = 0;
    var j = 0;
    var k = 0;
    var coordinate4326;

    map.forEachFeatureAtPixel(pixel, function(feature){
        labelB = feature.get('名称');
        labelC = feature.get('住所');
        label[0] = feature.get('P34_003');
        label[1] = feature.get('P34_004');
        labelD = feature.get('避難者数');
        labelHosp[0] = feature.get('prefecture');
        labelSelf = feature.getId();
        labelHinan[0] = feature.get('男');
        labelHinan[1] = feature.get('女');
        labelHinan[2] = feature.get('高齢者');
        labelHinan[3] = feature.get('身体障害者');
        labelHinan[4] = feature.get('乳幼児');
        labelHinan[5] = feature.get('妊婦');
        labelHinan[6] = feature.get('自立歩行不');
        labelHinan[7] = feature.get('その他の要');
        labelHinan[8] = feature.get('人工透析');
        labelHinan[9] = feature.get('人工呼吸器');
        labelHinan[10] = feature.get('電気');
        labelHinan[11] = feature.get('水');
        labelHinan[12] = feature.get('ガス');
        labelHinan[13] = feature.get('通信');
        labelHinan[14] = feature.get('換気');
        labelHinan[15] = feature.get('暖房');
        labelHinan[16] = feature.get('トイレ');
        labelHinan[17] = feature.get('汚物');
        labelHinan[18] = feature.get('ゴミ');
        labelHinan[19] = feature.get('食品');
        labelHinan[20] = feature.get('衛生');
        labelHinan[21] = feature.get('飲料水');
        labelHinan[22] = feature.get('食事');
        labelHinan[23] = feature.get('有無と方法');
        labelHinan[24] = feature.get('医師');
        labelHinan[25] = feature.get('看護師');
        labelHinan[26] = feature.get('薬剤師');
        labelHinan[27] = feature.get('事務');
        labelHinan[28] = feature.get('総数');
        labelHinan[29] = feature.get('インフルエ');
        labelHinan[30] = feature.get('呼吸器疾患');
        labelHinan[31] = feature.get('呼吸困難');
        labelHinan[32] = feature.get('発熱');
        labelHinan[33] = feature.get('下痢');
        labelHinan[34] = feature.get('嘔気・嘔吐');
        labelHinan[35] = feature.get('発疹');
        labelHinan[36] = feature.get('不眠・不安');
        labelHinan[37] = feature.get('精神科疾患');
        labelHinan[38] = feature.get('病院搬送・');
        labelHinan[39] = feature.get('高血圧');
        labelHinan[40] = feature.get('糖尿病');
        labelHinan[41] = feature.get('潰瘍性大腸');
        labelHinan[42] = feature.get('パーキンソ');
        labelHinan[43] = feature.get('備考');

        labelShltrJPN[0] = feature.get('避難所コー');
        labelShltrJPN[1] = feature.get('避難所名');
        labelShltrJPN[2] = feature.get('都道府県');
        labelShltrJPN[3] = feature.get('市区町村');
        labelShltrJPN[4] = feature.get('都道府県コ');
        labelShltrJPN[5] = feature.get('市区町村コ');

        arrayHosp =
            ['都道府県', '医療機関コード', '支援要否', '医療派遣ステータス', '医療機関名', 'チーム数', '最終更新日時', '医師出勤状況', 
                '入院病棟の倒壊・倒壊の恐れ', '電気使用', '水使用', '医療ガス使用', '医療品衛生資機材使用', '多数患者受信', '職員状況',
                'その他', '情報取得日時', '電話番号', 'メールアドレス', '更新日時'];
        labelHosp[1] = feature.get('code');
        labelHosp[2] = feature.get('assist');
        labelHosp[3] = feature.get('mds');
        labelHosp[4] = feature.get('name');
        labelHosp[5] = feature.get('team');
        labelHosp[6] = feature.get('rd');
        labelHosp[7] = feature.get('atd');
        labelHosp[8] = feature.get('e001');
        labelHosp[9] = feature.get('e002');
        labelHosp[10] = feature.get('e003');
        labelHosp[11] = feature.get('e004');
        labelHosp[12] = feature.get('e005');
        labelHosp[13] = feature.get('e006');
        labelHosp[14] = feature.get('e007');
        labelHosp[15] = feature.get('e008');
        labelHosp[16] = feature.get('e009');
        labelHosp[17] = feature.get('e010');
        labelHosp[18] = feature.get('e011');
        labelHosp[19] = feature.get('e012');

        coordinate4326 = feature.getGeometry().getExtent();
        lon = coordinate4326[0]
        lat = coordinate4326[1]
        ordLon = feature.get('経度');
        ordLat = feature.get('緯度');
    });
    if (labelSelf === "") {
        labelSelf = "noPlace";
    }
    info.innerHTML="";
    if(labelSelf == "noPlace") {
        flagSelected = false;
        document.getElementById( 'info' ).style.display = 'none';
        overlayInfo.setPosition();
        map.addOverlay(overlayInfo);
    } else if(typeof labelC === "undefined" && typeof labelHosp[0] === "undefined") {
        flagSelected = true;
        document.getElementById( 'info' ).style.display = 'none';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  label[0];
        while (i < 2) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white>a</><td style=font-size:24px;background-color:white;>b</td></tr>";
            i++
        }
        info.innerHTML = info.innerHTML + "<a href=mqtt/shelter-emergency.html id=niphLonLat target=_blank>NIPH</a>" + "<br>";
        var niphAddress=document.getElementById('niphLonLat');
        niphAddress.href='http://niph.go.jp';
        for (i = 0; i < 2; i++){
            info.rows[0].cells[0].innerHTML = "名称";
            info.rows[1].cells[0].innerHTML = "住所";
            info.rows[i].cells[1].innerHTML = label[i];
        }
        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        element.innerHTML =
            "名称    :" + label[0] + '<br>' +
            "住所    :" + label[1];
        map.addOverlay(overlayInfo);
    }

/*
    else if(labelShltrJPN[0] !== "" && typeof labelHosp[0] === "undefined"){
        flagSelected = true;
        document.getElementById( 'info' ).style.display = 'block';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center;' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelShltrJPN[1];
        info.innerHTML = info.innerHTML +  "<div style='background-color:#888888; text-align:center' type=button ><a href=mqtt/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLat target=_blank>入力画面を開く</a></div>";
        var niphAddress=document.getElementById('niphLonLat');
        niphAddress.href='mqtt/shelter-emergency_2.html' + '?' + 'ID=' + labelShltrJPN[0] + ',Name=' + labelShltrJPN[1] + ',x=' + ordLon + ',y=' + ordLat;
        while (i < 4) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        for (i = 0; i < 4; i++){
            info.rows[i].cells[0].innerHTML = arrayShltr[i];
            if (labelShltrJPN[i] === ""){
                info.rows[i].cells[1].innerHTML = labelShltrJPN[i];
            } else if(labelShltrJPN[i] >= 0) {
                info.rows[i].cells[1].innerHTML = labelShltrJPN[i];
            } else {
                info.rows[i].cells[1].innerHTML = labelShltrJPN[i];
                if (info.rows[i].cells[1].innerHTML === "undefined"){
                    info.rows[i].cells[1].innerHTML = "情報無し";
                }
            }
        }
        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        element.innerHTML =
            "名称    :" + labelShltrJPN[1] + '<br>' +
            "住所    :" + labelC;
        map.addOverlay(overlayInfo);
    }
*/

    else if (labelShltrJPN[0] !== "" && typeof labelHosp[0] !== "undefined") {
        
        flagSelected = true;
        document.getElementById( 'info' ).style.display = 'block';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center;' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelHosp[0];
        info.innerHTML = info.innerHTML +  "<div style='background-color:#888888; text-align:center' type=button ><a href=mqtt/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLat target=_blank>概要</a></div>" +
            "<button style='background-color:#888888; text-align:center' type=button ><a style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLat>概要</a></button>";
        var niphAddress=document.getElementById('niphLonLat');
            // 'mqtt/hospital-emergency.html' + '?' + 'ID=' + labelHosp[1] + ',Name=' + labelHosp[0] + ',MedDist=' + labelHosp[3] + ',TEL=' + labelHosp[2] + ',x=' + lon + ',y=' + lat;
            while (i < 20) {
                info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
                i++
            }
            for (i = 0; i < 20; i++) {
                info.rows[i].cells[0].innerHTML = arrayHosp[i];
                info.rows[i].cells[1].innerHTML = labelHosp[i];
            }
            overlayInfo.setPosition(coordinate);
            var element = overlayInfo.getElement();
            element.innerHTML =
                "名称    :" + labelHosp[4] + '<br>' +
                //   "住所    :" + labelHosp[1] + '<br>' +
                //   "電話番号        :" + labelHosp[2] + '<br>' +
                "支援要否    :" + labelHosp[3];
            map.addOverlay(overlayInfo);
        }


        


    else if (day == 1 || day == 4) {
        flagSelected = true;
        document.getElementById( 'info' ).style.display = 'block';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center;' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelB;
        info.innerHTML = info.innerHTML +  "<div style='background-color:#888888; text-align:center' type=button ><a href=mqtt/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLat target=_blank>入力画面を開く</a></div>";
        var niphAddress=document.getElementById('niphLonLat');
        niphAddress.href='mqtt/shelter-emergency_2.html' + '?' + 'ID=' + labelC + ',Name=' + labelB + ',x=' + lon + ',y=' + lat;
        while (i < 15) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        info.rows[0].cells[0].innerHTML = "総数";
        info.rows[0].cells[1].innerHTML = labelD + "人";
        for (i = 1; i < 15; i++){
            info.rows[i].cells[0].innerHTML = arrayH14[i-1];
            if (labelHinan[i-1] === ""){
                info.rows[i].cells[1].innerHTML = labelHinan[i-1];
            } else if(labelHinan[i-1] >= 0) {
                info.rows[i].cells[1].innerHTML = labelHinan[i-1] + "人";
            } else {
                info.rows[i].cells[1].innerHTML = labelHinan[i-1];
                if (info.rows[i].cells[1].innerHTML === "undefined"){
                    info.rows[i].cells[1].innerHTML = "情報無し";
                }
            }
        }
        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        element.innerHTML =
            "名称    :" + labelB + '<br>' +
            "住所    :" + labelC + '<br>' +
            "避難者数 :" + labelD + "人";
        map.addOverlay(overlayInfo);
    } else if (day == 14) {
        flagSelected = true;
        var urlInputScreen = 'mqtt/shelter-emergency_2.html' + '?' + 'ID=' + labelC + ',Name=' + labelB + ',x=' + lon + ',y=' + lat;
        document.getElementById( 'info' ).style.display = 'block';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center;' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelB;
        info.innerHTML = info.innerHTML +  "<div style='background-color:#888888; text-align:center' type=button ><a href=mqtt/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLat target=_blank>入力画面を開く</a></div>";
        var niphAddress=document.getElementById('niphLonLat');
        niphAddress.href='mqtt/shelter-emergency_2.html' + '?' + 'ID=' + labelC + ',Name=' + labelB + ',x=' + lon + ',y=' + lat;

        // 画面サイズで表示変更
        if (window.innerWidth < window.innerHeight) {

            while (i < 15) {
                info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
                i++
            }
            if (labelHinan[43] === "") {
                info.innerHTML = info.innerHTML + "備考:情報無し" + "<br>";
            }
            else {
                info.innerHTML = info.innerHTML + "備考:" + labelHinan[43] + "<br>";
            }
            info.rows[0].cells[0].innerHTML = "総数";
            info.rows[0].cells[1].innerHTML = labelD + "人";

            for (i = 1; i < 16; i++) {
                for (j = 0; j < 5 ; j++) {
                    if(i > 1 && j == 0) {
                        info.rows[i - 1].cells[j].innerHTML = arrayH14[k];
                        if (labelHinan[k] === "") {
                            info.rows[i - 1].cells[j + 1].innerHTML = "情報無し";
                        } else if (labelHinan[k] >= 0) {
                            info.rows[i - 1].cells[j + 1].innerHTML = labelHinan[k] + "人";
                        } else {
                            info.rows[i - 1].cells[j + 1].innerHTML = labelHinan[k];
                            if (info.rows[i - 1].cells[j + 1].innerHTML === "undefined") {
                                info.rows[i - 1].cells[j + 1].innerHTML = "情報無し";
                            }
                        }
                        k++
                    } else if(j == 2 && k < 43){
                        info.rows[i - 1].cells[j].innerHTML = arrayH14[k];
                        if (labelHinan[k] === "") {
                            info.rows[i - 1].cells[j + 1].innerHTML = "情報無し";
                        } else if (labelHinan[k] >= 0) {
                            info.rows[i - 1].cells[j + 1].innerHTML = labelHinan[k] + "人";
                        } else {
                            info.rows[i - 1].cells[j + 1].innerHTML = labelHinan[k];
                            if (info.rows[i - 1].cells[j + 1].innerHTML === "undefined") {
                                info.rows[i - 1].cells[j + 1].innerHTML = "情報無し";
                            }
                        }
                        k++
                    } else if(j == 4 && k < 43){
                        info.rows[i - 1].cells[j].innerHTML = arrayH14[k];
                        if (labelHinan[k] === "") {
                            info.rows[i - 1].cells[j + 1].innerHTML = "情報無し";
                        } else if (labelHinan[k] >= 0) {
                            info.rows[i - 1].cells[j + 1].innerHTML = labelHinan[k] + "人";
                        } else {
                            info.rows[i - 1].cells[j + 1].innerHTML = labelHinan[k];
                            if (info.rows[i - 1].cells[j + 1].innerHTML === "undefined") {
                                info.rows[i - 1].cells[j + 1].innerHTML = "情報無し";
                            }
                        }
                        k++
                    }
                }
            }


        } else {
            while (i < 44) {
                info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
                i++
            }
            if (labelHinan[43] === "") {
                info.innerHTML = info.innerHTML + "備考:情報無し" + "<br>";
            }
            else {
                info.innerHTML = info.innerHTML + "備考:" + labelHinan[43] + "<br>";
            }
            info.rows[0].cells[0].innerHTML = "総数";
            info.rows[0].cells[1].innerHTML = labelD + "人";
            for (i = 1; i < 44; i++) {
                info.rows[i].cells[0].innerHTML = arrayH14[i - 1];
                if (labelHinan[i - 1] === "") {
                    info.rows[i].cells[1].innerHTML = "情報無し";
                } else if (labelHinan[i - 1] >= 0) {
                    info.rows[i].cells[1].innerHTML = labelHinan[i - 1] + "人";
                } else {
                    info.rows[i].cells[1].innerHTML = labelHinan[i - 1];
                    if (info.rows[i].cells[1].innerHTML === "undefined") {
                        info.rows[i].cells[1].innerHTML = "情報無し";
                    }
                }
            }
        }



        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        element.innerHTML =
            "名称    :" + labelB + '<br>' +
            "住所    :" + labelC + '<br>' +
            "避難者数 :" + labelD + "人";
        map.addOverlay(overlayInfo);


        var markerA
        map.removeOverlay(markerA);
        markerA = makeMarkerOverlay('img/shelter_green.gif', [lon,lat]);
        map.addOverlay(markerA);

    }


     else {
        flagSelected = false;
        overlayInfo.setPosition();
        map.addOverlay(overlayInfo);
    }
    
});
function showHide(){
    document.getElementById('info').style.display = 'none';
}

function sho(evt) {
        displayFeatureInfo(evt.pixel);
        var coordinate = evt.coordinate;
        var pixel = map.getPixelFromCoordinate(coordinate);
        var info = document.getElementById('info');
        document.getElementById('info').innerHTML = '';
        var labelHosp = new Array(5);
    console.log("adada");
    map.forEachFeatureAtPixel(pixel, function(feature) {
        var arrayHosp =
            ['都道府県', '二次医療圏', '支援要請', '医療派遣ステータス', '医療機関名', 'チーム数', '最終更新日時', '医師出勤状況'];
        labelHosp[1] = feature.get('二次医療圏');
        labelHosp[2] = "要・不要・未"
        labelHosp[3] = "要手配・手配済・未入力"
        labelHosp[4] = feature.get('医療機関名');
        labelHosp[5] = "チーム数";
        labelHosp[6] = "更新日時";
        labelHosp[7] = "医師出勤状況";

        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center;' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelHosp[0];
        info.innerHTML = info.innerHTML +  "<div style='background-color:#888888; text-align:center' type=button ><a href=mqtt/shelter-emergency.html style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLat target=_blank>概要</a></div>" +
            "<button style='background-color:#888888; text-align:center' type=button ><a style='display:block; width:100%; color:white; text-decoration:none' id=niphLonLat>概要</a></button>";
        var niphAddress=document.getElementById('niphLonLat');
        // 'mqtt/hospital-emergency.html' + '?' + 'ID=' + labelHosp[1] + ',Name=' + labelHosp[0] + ',MedDist=' + labelHosp[3] + ',TEL=' + labelHosp[2] + ',x=' + lon + ',y=' + lat;
        while (i < 8) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        for (i = 0; i < 8; i++) {
            info.rows[i].cells[0].innerHTML = arrayHosp[i];
            info.rows[i].cells[1].innerHTML = labelHosp[i];
        }

    })
}
