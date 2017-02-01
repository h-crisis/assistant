/**
 * Created by komori on 2016/12/21.
 */

//OS、ブラウザの情報を取得
var userAgent = window.navigator.userAgent.toLowerCase();
var ieVersion = window.navigator.appVersion.toLowerCase();
var osCss, brwCss;

//OSの判定
if(userAgent.indexOf('win') > -1){
    osCss = 'win';
}else if(userAgent.indexOf('mac') > -1){
    osCss = 'mac';
}else if(userAgent.indexOf('android') > -1){
    osCss = 'adr';
} else {
    osCss = 'win';
}

//ブラウザの判定
if (userAgent.indexOf('msie') > -1) {
    brwCss = 'm';
} else if (userAgent.indexOf('edge') > -1) {
    brwCss = 'f';
} else if (userAgent.indexOf('safari') > -1) {
    brwCss = 's';
} else if (userAgent.indexOf('chrome') > -1) {
    brwCss = 'c';
} else if (userAgent.indexOf('opera') > -1) {
    brwCss = 'o';
} else {
    brwCss = 'f';
}

if ((userAgent.indexOf("windows") != -1 && userAgent.indexOf("touch") != -1 && userAgent.indexOf("tablet pc") == -1)
    || userAgent.indexOf("ipad") != -1
    || (userAgent.indexOf("android") != -1 && userAgent.indexOf("mobile") == -1)
    || (userAgent.indexOf("firefox") != -1 && userAgent.indexOf("tablet") != -1)
    || userAgent.indexOf("kindle") != -1
    || userAgent.indexOf("silk") != -1
    || userAgent.indexOf("playbook") != -1){
    document.write('<link id="tablet" rel="stylesheet" href="../../css/' + osCss + '/' + brwCss + '_Tab.css" type="text/css">');
} else if ((userAgent.indexOf("windows") != -1 && userAgent.indexOf("phone") != -1)
    || userAgent.indexOf("iphone") != -1
    || userAgent.indexOf("ipod") != -1
    || (userAgent.indexOf("android") != -1 && userAgent.indexOf("mobile") != -1)
    || (userAgent.indexOf("firefox") != -1 && userAgent.indexOf("mobile") != -1)
    || userAgent.indexOf("blackberry") != -1) {
    document.write('<link id="phone" rel="stylesheet" href="../../css/' + osCss + '/' + brwCss +'_Mob.css" type="text/css">');
} else {
    document.write('<link id="pc" rel="stylesheet" href="../../css/' + osCss + '/' + brwCss + '_PC.css" type="text/css">');
}

//CSSの設定
console.log(navigator.userAgent.toLowerCase());

