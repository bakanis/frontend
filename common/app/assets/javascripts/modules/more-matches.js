define(['common', 'reqwest', 'bonzo', 'qwery', 'bean'], function (common, reqwest, bonzo, qwery, bean) {

    return {

        nav: null,

        init: function (nav) {

            this.nav = nav;

            // if nav doesn't exist then this will change every <a> on the page...
            if (!nav) { return; }

            bonzo(nav).removeClass('js-not-ajax'); // removes the default left/right float style

            // update nav
            bonzo(qwery('a', nav))
                .addClass('cta')
                .each(function(element, index) {
                    // update text in cta
                    var buttonText = element.getAttribute('data-js-title');
                    buttonText = (buttonText) ? buttonText : 'Show more matches';
                    bonzo(element).text(buttonText);
                });

            common.mediator.on('ui:more-matches:clicked', function (_link) {
                var link = bonzo(_link);
                reqwest({
                    url: link.attr('href') + '?callback=?',
                    type: 'jsonp',
                    success: function (response) {
                        // place html before nav
                        bonzo(nav).before(response.html);
                        // update more link (if there is more)
                        if (response.more) {
                            link.attr('href', response.more);
                        } else {
                            link.remove();
                        }
                    }
                });
            });

            bean.add(nav, 'a', 'click', function(e) {
                common.mediator.emit('ui:more-matches:clicked', [e.target]);
                e.preventDefault();
            });

        }
    };

});