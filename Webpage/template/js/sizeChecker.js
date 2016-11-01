/**
 * Created by komori on 2016/06/21.
 */

var timer = false;

function sizeCheck() {

    if (window.innerWidth < window.innerHeight && $(window).width() < 580){
        preCells = "<tr><td width='110px' style=font-size:14px;background-color:#888888;color:white>";
        interCells = "</td><td width='250px' style=font-size:14px;background-color:white;text-align:right;>";
    } else {
        preCells = "<tr><td width='110px' style=font-size:17px;background-color:#888888;color:white>";
        interCells = "</td><td width='250px' style=font-size:17px;background-color:white;text-align:right;>";
    }

};

$(window).resize(function () {
    if (timer !== false) {
        clearTimeout(timer);
    }
    timer = setTimeout(function () {
        if (window.innerWidth < window.innerHeight && $(window).width() < 580){
            preCells = "<tr><td width='110px' style=font-size:4px;background-color:#888888;color:white>";
            interCells = "</td><td width='250px' style=font-size:4px;background-color:white;text-align:right;>";
            createHtmlHall();
            createHtmlHospital();
            createHtmlPass();
        } else {
            preCells = "<tr><td width='110px' style=font-size:17px;background-color:#888888;color:white>";
            interCells = "</td><td width='250px' style=font-size:17px;background-color:white;text-align:right;>";
        }
    }, 200);
})