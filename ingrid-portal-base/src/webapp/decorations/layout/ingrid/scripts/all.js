/* Logo
 *
 * Send a request for the icons.svg file and load it to the start of the DOM, under <body>.
 */
(function () {

    var client = new XMLHttpRequest();

    client.onreadystatechange = function () {
        if (client.readyState == 4 && client.status == 200) {
            if (client.responseText) {
                var first = document.body.firstChild;
                var div = document.createElement('div');
                div.innerHTML = client.responseText;

                document.body.insertBefore(div, first);
            }
        }
    }

    client.open('GET', '/decorations/layout/ingrid/images/template/icons.svg');
    client.send();

})();


/* Utilities
 */
(function () {

    window.transitionend = (function () {
        var t, el = document.createElement('fakeelement'),
                transitions = {
                    'transition': 'transitionend',
                    'OTransition': 'oTransitionEnd',
                    'MozTransition': 'transitionend',
                    'WebkitTransition': 'webkitTransitionEnd'
                }

        for (t in transitions) {
            if (el.style[t] !== undefined) {
                return transitions[t];
            }
        }
    })();

})();

/* Init FastClick
 */
$(function () {
    FastClick.attach(document.body);
});

/* Message Box
 *
 * Closes and deletes the messagebox.
 */
(function ($) {

    $('.js-message-close').on('click', function () {

        var box = $(this).parents('.message');

        if (!box.length) {
            return;
        }

        box.addClass('is-deleted');

        box.one(transitionend, function () {
            $(this).remove();
        });

    });

})(jQuery);


/* Sidebar Toggle
 */
(function ($) {

    var sidebar = $('.sidebar');
    var toggle = $(".js-sidebar-toggle");

    toggle.on('click', function () {
        toggle.toggleClass('is-active');
        sidebar.toggleClass('is-active');
    });

})(jQuery);


/* To Top Animation
 */
(function ($) {

    $(".js-top").on('click', function () {
        $("html, body").animate({scrollTop: 0}, 400);
        return false;
    });

})(jQuery);

/* Main Navigation toggles
 */
(function ($) {

    var active;

    $(".js-nav-desktop-toggle").on('click', function (event) {

        event.preventDefault();

        if (active != null && active !== this) {
            $(active).removeClass('is-active');
        }

        $(this).toggleClass('is-active');

        active = this;

    });

})(jQuery);

/* Header Toggle
 */
(function ($) {

    var toggles = $(".js-header-toggle");

    toggles.on('click', function (event) {

        event.preventDefault();

        toggles.not(this).removeClass('is-active');

        $(this).toggleClass('is-active');

    });

})(jQuery);

/* Mobile Navigation
 */
(function ($) {

    var page = $('.page');
    var nav = $('.nav-overlay');
    var header = $('.header');

    var hideNav = function () {
        nav.off($.ontransitionend, hideNav);
        nav.addClass('is-fixed');
    }

    var hidePage = function () {
        nav.off(transitionend, hidePage);
        header.addClass('is-hiding');
        page.addClass('is-fixed');
    }

    $('.js-nav-mobile-toggle').on('click', function () {

        $(this).toggleClass('is-active');

        // We have to use vanilla JS here because jQuery cannot currently manipulate classes on <svg> elements.
        var icons = this.querySelectorAll('.icon');
        for (var i = 0, j = icons.length; i < j; i++) {
            icons[i].classList.toggle('ob-fade');
        }

        if (nav.hasClass('is-active')) {
            nav.on(transitionend, hideNav);
            page.removeClass('is-fixed is-hidden');
            header.removeClass('is-hiding');
            nav.removeClass('is-active');

        }
        else {
            nav.on(transitionend, hidePage);
            nav.removeClass('is-fixed').addClass('is-active');

        }

    });

})(jQuery);
