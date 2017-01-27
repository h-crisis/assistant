/**
 * Created by komori on 2016/12/06.
 */

var count = 0;
var fadeDisp = document.getElementById("fadeLayer").style.display;

$("div#option").click(function(){
    $(".panel").toggleClass("hidden");
    $(".button").toggleClass("hidden");
    $(".logo").toggleClass("hidden");
});
$("#fadeLayer").click(function(){
    $(".panel").toggleClass("hidden");
    $(".button").toggleClass("hidden");
    $(".logo").toggleClass("hidden");
});
$(".button").click(function(){
    $(".panel").toggleClass("hidden");
    $(".button").toggleClass("hidden");
    $(".logo").toggleClass("hidden");
});

function fade() {
    if (document.getElementById("fadeLayer").style.display == "block") {
        document.getElementById("fadeLayer").style.display = 'none';
    } else {
        document.getElementById("fadeLayer").style.display = 'block';
    }
}

function detailInfoOnOff() {
    if (document.getElementById("fadeLayerDetail").style.display == "block") {
        document.getElementById("fadeLayerDetail").style.display = 'none';
    } else {
        document.getElementById("fadeLayerDetail").style.display = 'block';
    }
}

function detailInfoOff() {
   
}