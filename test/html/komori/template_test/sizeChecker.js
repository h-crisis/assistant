/**
 * Created by komori on 2016/06/21.
 */

if (window.innerWidth < window.innerHeight){
    document.getElementById( 'info' ).style.width = '90%';
    document.getElementById( 'info' ).style.height = '35%';
}

var timer = false;
$(window).resize(function() {
    if (timer !== false) {
        clearTimeout(timer);
    }
    timer = setTimeout(function() {
        if (window.innerWidth < window.innerHeight){
            document.getElementById( 'info' ).style.width = '90%';
            document.getElementById( 'info' ).style.height = '35%';
        } else {
            document.getElementById( 'info' ).style.width = '20%';
            document.getElementById( 'info' ).style.height = '90%';
        }

    }, 200);
});
