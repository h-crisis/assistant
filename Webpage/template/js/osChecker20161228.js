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
}

//ブラウザの判定
if (userAgent.indexOf('msie') > -1) {
    brwCss = 'm';
}else if (userAgent.indexOf('firefox') > -1) {
    brwCss = 'f';
}else if (userAgent.indexOf('opera') > -1) {
    brwCss = 'o';
}else if (userAgent.indexOf('chrome') > -1) {
    brwCss = 'c';
}else if (userAgent.indexOf('safari') > -1) {
    brwCss = 's';
}else {
}

//CSSの設定
document.write('<link id="phone" rel="stylesheet" href="../../css/' + osCss + '/' + brwCss +'_Mob.css" type="text/css" media="screen and (min-width: 0px) and (max-width: 470px)">');
document.write('<link id="tablet" rel="stylesheet" href="../../css/' + osCss + '/' + brwCss + '_Tab.css" type="text/css" media="screen and (min-width: 471px) and (max-width: 780px)">');
document.write('<link id="pc" rel="stylesheet" href="../../css/' + osCss + '/' + brwCss + '_PC.css" type="text/css" media="screen and (min-width: 781px)">');



