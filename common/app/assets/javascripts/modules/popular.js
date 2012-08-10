define(['common', 'reqwest'], function(common, reqwest){ 

    function Popular(attachTo) {
        
        // View 
        
        this.view = {
        
            attachTo: attachTo,

            render: function(html) {
                attachTo.innerHTML = html;
            }
        
        }

        // Bindings
        
        common.pubsub.on('modules:popular:loaded', this.view.render);
        
        // Model
        
        this.load = function(url){
            return reqwest({
                    url: url,
                    type: 'jsonp',
                    jsonpCallback: 'callback',
                    jsonpCallbackName: 'showMostPopular',
                    success: function(json) {
                        common.pubsub.emit('modules:popular:loaded', [json.html])
                    }
            })
        }  

    }
    
    return Popular;

});