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
var basemap = new ol.source.XYZ({
    url : 'http://cyberjapandata.gsi.go.jp/xyz/std/{z}/{x}/{y}.png',
    attribution  : "<a href='http://www.gsi.go.jp/kikakuchousei/kikakuchousei40182.html' target='_blank'>国土地理院</a>"
});
var baseLayer = new ol.layer.Tile({
    source: basemap,
    opacity: 1
});
var map = new ol.Map({
    layers: [baseLayer, osmQuestLayer, satQuestLayer
    ],
    target: 'map',
    view: new ol.View({
        center: ol.proj.transform([140.461129, 35.774460, 13], 'EPSG:4326', 'EPSG:3857'),
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
    map.getView().setCenter(ol.proj.transform([140.461129, 37.787558], 'EPSG:4326', 'EPSG:3857'))
    map.getView().setZoom(11);
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
map.addLayer(hinanLayer1);
map.addLayer(hinanLayer4);
map.addLayer(hinanLayer14);
map.addLayer(officeLayer);
map.addLayer(hospLayer);
layerSI.setVisible(false);
hinanLayer1.setVisible(false);
hinanLayer4.setVisible(false);
hinanLayer14.setVisible(false);
officeLayer.setVisible(false);
hospLayer.setVisible(false);

var distribOnOff   = document.getElementById('distribution-vis');
distribOnOff.addEventListener('click', siButton);

var hinanOnOff1   = document.getElementById('hinan1-vis');
hinanOnOff1.addEventListener('click', hinanButton1);
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
    var labelSelf = '';
    var arrayHosp =
        ['医療機関名','住所','電話番号','二次医療圏']
    var arrayH14 =
        ['男性', '女性', '高齢者', '身体障害者', '乳幼児', '妊婦', '自立歩行不', 'その他要', '人工透析', '人工呼吸器', '電気',
            '水', 'ガス', '通信', '換気', '暖房', 'トイレ', '汚物', 'ゴミ', '食品', '衛生', '飲料水', '食事', '有無と方法',
            '医師', '看護師', '薬剤師', '事務', '総数', 'インフルエンザ', '呼吸器疾患', '呼吸困難', '発熱', '下痢', '嘔気・嘔吐',
            '発疹', '不眠・不安', '精神科疾患', '病院搬送', '高血圧', '糖尿病', '潰瘍性大腸炎', 'パーキンソン病', '備考']
    var i = 0;
    var coordinate4326;

    map.forEachFeatureAtPixel(pixel, function(feature){
        labelB = feature.get('名称');
        labelC = feature.get('住所');
        label[0] = feature.get('P34_003');
        label[1] = feature.get('P34_004');
        labelD = feature.get('避難者数');
        labelHosp[0] = feature.get('医療機関名');
        labelHosp[1] = feature.get('住所');
        labelHosp[2] = feature.get('電話番号');
        labelHosp[3] = feature.get('二次医療圏');
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
        coordinate4326 = feature.getGeometry().getExtent();
        lon = coordinate4326[0]
        lat = coordinate4326[1]
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
    } else if(typeof labelB === "undefined" && typeof labelHosp[0] === "undefined") {
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
    else if (typeof label[0] === "undefined" && typeof labelB === "undefined") {
        flagSelected = true;
        document.getElementById( 'info' ).style.display = 'block';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelHosp[0];
        while (i < 4) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        info.innerHTML = info.innerHTML + "<a href=mqtt/shelter-emergency.html id=niphLonLat target=_blank>NIPH</a>" + "<br>";
        var niphAddress=document.getElementById('niphLonLat');
        niphAddress.href='http://niph.go.jp';
        for (i = 0; i < 4; i++){
            info.rows[i].cells[0].innerHTML = arrayHosp[i];
            info.rows[i].cells[1].innerHTML = labelHosp[i];
        }
        overlayInfo.setPosition(coordinate);
        var element = overlayInfo.getElement();
        element.innerHTML =
            "名称    :" + labelHosp[0] + '<br>' +
            "住所    :" + labelHosp[1] + '<br>' +
            "電話番号        :" + labelHosp[2] + '<br>' +
            "二次医療圏    :" + labelHosp[3];
        map.addOverlay(overlayInfo);
    } else if (day == 1 || day == 4) {
        flagSelected = true;
        document.getElementById( 'info' ).style.display = 'block';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelB;
        while (i < 15) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        info.innerHTML = info.innerHTML + "<a href=mqtt/shelter-emergency.html id=niphLonLat target=_blank>NIPH</a>" + "<br>";
        var niphAddress=document.getElementById('niphLonLat');
        niphAddress.href='http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/mqtt/shelter-emergency.html' + '?' + 'x=' + lon + ',y=' + lat + ',ID=' + labelC + ',Name=' + labelB;
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
        document.getElementById( 'info' ).style.display = 'block';
        info.innerHTML = "<div style='background-color:#888888; color:white; text-align:center' type=button id=showBtn value=隠す onclick=showHide()>閉じる</div>";
        info.innerHTML = info.innerHTML +  labelB;
        while (i < 44) {
            info.innerHTML = info.innerHTML + "<tr><td style=font-size:24px;background-color:#888888;color:white></td><td style=font-size:24px;background-color:white;text-align:right;></td></tr>";
            i++
        }
        if (labelHinan[43] === ""){
            info.innerHTML = info.innerHTML + "備考:情報無し" + "<br>";
        }
        else {
            info.innerHTML = info.innerHTML + "備考:" + labelHinan[43] + "<br>";
        }
        info.innerHTML = info.innerHTML + "<a href=mqtt/shelter-emergency.html id=niphLonLat target=_blank>NIPH</a>" + "<br>";
        var niphAddress=document.getElementById('niphLonLat');
        niphAddress.href='http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/test/mqtt/shelter-emergency.html_2' + '?' + 'x=' + lon + ',y=' + lat + ',ID=' + labelC + ',Name=' + labelB;
        info.rows[0].cells[0].innerHTML = "総数";
        info.rows[0].cells[1].innerHTML = labelD + "人";
        for (i = 1; i < 44; i++){
            info.rows[i].cells[0].innerHTML = arrayH14[i-1];
            if (labelHinan[i-1] === ""){
                info.rows[i].cells[1].innerHTML = "情報無し";
            } else if(labelHinan[i-1] >= 0) {
                info.rows[i].cells[1].innerHTML = labelHinan[i-1] + "人";
            } else  {
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
    } else {
        flagSelected = false;
        overlayInfo.setPosition();
        map.addOverlay(overlayInfo);
    }
    
});
function showHide(){
    document.getElementById('info').style.display = 'none';
}