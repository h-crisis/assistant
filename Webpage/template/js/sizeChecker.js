/**
 * Created by komori on 2016/06/21.
 */

var timer = false;
$(window).resize(function() {
    if (timer !== false) {
        clearTimeout(timer);
    }
    timer = setTimeout(function() {
        if (window.innerWidth < window.innerHeight){
            document.getElementById( 'info' ).style.width = '90%';
            document.getElementById( 'info' ).style.height = '20%';
            document.getElementById( 'infoHeader' ).style.width = '90%';
            document.getElementById( 'infoHeader' ).style.height = '110px';
        } else {
            document.getElementById( 'info' ).style.width = '380px';
            document.getElementById( 'info' ).style.height = '90%';
            document.getElementById( 'infoHeader' ).style.width = '380px';
            document.getElementById( 'infoHeader' ).style.height = '110px';
        }

    }, 200);
});
