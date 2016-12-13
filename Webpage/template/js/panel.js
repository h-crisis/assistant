/**
 * Created by komori on 2016/12/06.
 */


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
    if (count%2 == 0) {
        document.getElementById("fadeLayer").style.display = 'block';
        count++;
    } else {
        document.getElementById("fadeLayer").style.display = 'none';
        count++
    }
}