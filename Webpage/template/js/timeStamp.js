/**
 * Created by komori on 2017/04/26.
 */

(function() {
    'use strict';

    /*
     今日の日付データを変数todayに格納
     */
    var optionLoop, this_minute, this_hour, this_day, this_month, this_year, today;
    today = new Date();
    this_year = today.getFullYear();
    this_month = today.getMonth() + 1;
    this_day = today.getDate();
    this_hour = today.getHours();
    this_minute = today.getMinutes();

    /*
     ループ処理（スタート数字、終了数字、表示id名、デフォルト数字）
     */
    optionLoop = function(start, end, id, this_day) {
        var i, opt;

        opt = null;
        for (i = start; i <= end ; i++) {
            if (i === this_day) {
                opt += "<option value='" + i + "' selected>" + i + "</option>";
            } else {
                opt += "<option value='" + i + "'>" + i + "</option>";
            }
        }
        return document.getElementById(id).innerHTML = opt;
    };


    /*
     関数設定（スタート数字[必須]、終了数字[必須]、表示id名[省略可能]、デフォルト数字[省略可能]）
     */
    optionLoop(2011, this_year, 'id_year', this_year);
    optionLoop(1, 12, 'id_month', this_month);
    optionLoop(1, 31, 'id_day', this_day);
    optionLoop(0, 23, 'id_hour', this_hour);
    optionLoop(0, 59, 'id_minute', this_minute);
})();

(function() {
    'use strict';

    /*
     都道府県のオブジェクト(キー：値)を変数prefに格納
     */
    var pref, prefLoop;
    pref = {
        "01": "北海道",
        "02": "青森県",
        "03": "岩手県",
        "04": "宮城県",
        "05": "秋田県",
        "06": "山形県",
        "07": "福島県",
        "08": "茨城県",
        "09": "栃木県",
        "10": "群馬県",
        "11": "埼玉県",
        "12": "千葉県",
        "13": "東京都",
        "14": "神奈川県",
        "15": "新潟県",
        "16": "富山県",
        "17": "石川県",
        "18": "福井県",
        "19": "山梨県",
        "20": "長野県",
        "21": "岐阜県",
        "22": "静岡県",
        "23": "愛知県",
        "24": "三重県",
        "25": "滋賀県",
        "26": "京都府",
        "27": "大阪府",
        "28": "兵庫県",
        "29": "奈良県",
        "30": "和歌山県",
        "31": "鳥取県",
        "32": "島根県",
        "33": "岡山県",
        "34": "広島県",
        "35": "山口県",
        "36": "徳島県",
        "37": "香川県",
        "38": "愛媛県",
        "39": "高知県",
        "40": "福岡県",
        "41": "佐賀県",
        "42": "長崎県",
        "43": "熊本県",
        "44": "大分県",
        "45": "宮崎県",
        "46": "鹿児島県",
        "47": "沖縄県"
    };

    /*
     ループ処理（配列、表示id名、デフォルト都道府県キー）
     */
    prefLoop = function(pref, id, def_id) {
        var key, opt, value;
        opt = null;
        for (key in pref) {
            value = pref[key];
            if (key === def_id) {
                opt += "<option value='" + value + "' selected>" + value + "</option>";
            } else {
                opt += "<option value='" + value + "'>" + value + "</option>";
            }
        }
        return document.getElementById(id).innerHTML = opt;
    };

    /*
     関数設定（都道府県配列[必須]、表示id名[必須]、デフォルト都道府県キー[必須]）
     */
    prefLoop(pref, 'id_pref', '31');
})();