

//Max Page-Slowik
//toggles the navigation bar for mobile
jQuery(document).ready(function ($) {

    $('#button').mousedown(function () {


        $('.row-offcanvas').removeClass('active');
        $('.row-offcanvas').addClass('active');
        //event.stopPropagation();

    });
});