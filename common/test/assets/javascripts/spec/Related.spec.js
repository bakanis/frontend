define(['common', 'modules/related'], function(common, Related) {

    describe("Related", function() {
       
        var callback;

        beforeEach(function() {
            callback = sinon.spy(function(){});
            common.mediator.on('modules:related:loaded', callback);
        });

        // json test needs to be run asynchronously 
        it("should request the related links and graft them on to the dom", function(){
            
            appendTo = document.getElementById('related-1');
            
            runs(function() {
                var r = new Related(appendTo, { relatedContent : true }).load('fixtures/json');
            });

            waits(500);

            runs(function(){
                expect(callback).toHaveBeenCalledOnce();
                expect(appendTo.innerHTML).toBe('<b>1</b>');
            });
        });

        // json test needs to be run asynchronously
        it("should not request related links if switched off", function(){

            appendTo.innerHTML = ""

            appendTo = document.getElementById('related-1');

            runs(function() {
                var r = new Related(appendTo, { relatedContent : false }).load('fixtures/json');
            });

            waits(500);

            runs(function(){

                expect(appendTo.innerHTML).toBe('');
            });
        });
        
        xit("should request the related links per edition", function(){
            expect(0).toBeTruthy();
        });
    
    });
})
