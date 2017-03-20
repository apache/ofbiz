

/*************** COLORS TO BE ERASED WHEN INSTALLING THE THEME ***********/

$(document).ready(function() {   

    var s = document.createElement("script");
    s.type = "text/javascript";
    s.src = "js-plugin/jquery-cookie/jquery.cookie.js";
    $("body").append(s);  

    if($.cookie("css")) {
        $("#colors").attr("href",$.cookie("css"));
    }
    $(".switcher li a").click(function() { 

        $("#colors").attr("href",$(this).attr("href"));
        $.cookie("css",$(this).attr("href"));
        return false;
    });

});

/*************** COLORS TO BE ERASED WHEN INSTALLING THE THEME ***********/


/*************** REPLACE WITH YOUR OWN UA NUMBER ***********/
var UA = 'XXX';
/*************** REPLACE WITH YOUR OWN UA NUMBER ***********/


/*
|--------------------------------------------------------------------------
| DOCUMENT READY
|--------------------------------------------------------------------------
*/  

$(document).ready(function() {
    /***** form placeholder for IE *****/
    if(!Modernizr.input.placeholder){

        $('[placeholder]').focus(function() {
            var input = $(this);
            if (input.val() == input.attr('placeholder')) {
                input.val('');
                input.removeClass('placeholder');
            }
        }).blur(function() {
            var input = $(this);
            if (input.val() == '' || input.val() == input.attr('placeholder')) {
                input.addClass('placeholder');
                input.val(input.attr('placeholder'));
            }
        }).blur();
        $('[placeholder]').parents('form').submit(function() {
            $(this).find('[placeholder]').each(function() {
                var input = $(this);
                if (input.val() == input.attr('placeholder')) {
                    input.val('');
                }
            })
        });

    }									






    /*
    |--------------------------------------------------------------------------
    | BOOTSTRAP ELEMENTS 
    |--------------------------------------------------------------------------
    */ 


   


    /*
    |--------------------------------------------------------------------------
    | PRETTY PHOTOS
    |--------------------------------------------------------------------------
    */      
    if( $("a.prettyPhoto").length){
        $("a.prettyPhoto").prettyPhoto({
            animation_speed:'fast',
            slideshow:10000, 
            hideflash: true,
            social_tools:false
        });
    }

    /*
    |--------------------------------------------------------------------------
    | TOOLTIP
    |--------------------------------------------------------------------------
    */
    $('.tips').tooltip();
    
    /*
    |--------------------------------------------------------------------------
    | COLLAPSE
    |--------------------------------------------------------------------------
    */
    

    $('.accordion').on('show hide', function(e){
        $(e.target).siblings('.accordion-heading').find('.accordion-toggle i').toggleClass('icon-right-circle icon-down-circle', 200);
    });

    /*
    |--------------------------------------------------------------------------
    | CONTACT
    |--------------------------------------------------------------------------
    */   
    $('.slideContact').click(function(e){

        if ( $(window).width() >= 800){

            $('#contact').slideToggle('normal', 'easeInQuad',function(){

                $('#contactinfoWrapper').css('margin-left', 0);
                $('#mapSlideWrapper').css('margin-left', 3000);
                $('#contactinfoWrapper').fadeToggle();
                

            });
            $('#closeContact').fadeToggle(); 
            return false;
            
        }else{

            return true;
            
        }
    });
    
    
    $('#closeContact').click(function(e){


        $('#contactinfoWrapper').fadeOut('normal', 'easeInQuad',function(){
            $('#contactinfoWrapper').css('margin-left', 0);
            $('#mapSlideWrapper').css('margin-left', 3000);
        });
        
        $('#contact').slideUp('normal', 'easeOutQuad');

        $(this).fadeOut();

        e.preventDefault();
        
    });
    



    
    
    /* MAP */
    $('#mapTrigger').click(function(e){


        $('#mapSlideWrapper').css('display', 'block');
        initialize('mapWrapper');
        
        $('#contactinfoWrapper, #contactinfoWrapperPage').animate({
            marginLeft:'-2000px' 
        }, 400, function() {}); 
        
        
        $('#mapSlideWrapper').animate({
            marginLeft:'15px' 
        }, 400, function() {});  
        
        appendBootstrap();

        e.preventDefault();
    });
    
    
    $('#mapTriggerLoader').click(function(e){


        $('#mapSlideWrapper, #contactinfoWrapperPage').css('display', 'block');

        $('#contactinfoWrapper, #contactinfoWrapperPage').animate({
            marginLeft:'-2000px' 
        }, 400, function() {}); 
        
        
        $('#mapSlideWrapper').animate({
            marginLeft:'15px' 
        }, 400, function() {});  

        
        appendBootstrap();
        
        e.preventDefault();
    });
    
    
    $('#mapReturn').click(function(e){
        //$('#mapWrapper').css('margin-bottom', '3em');
        

        $('#mapSlideWrapper').animate({
            marginLeft:'3000px' 
        }, 400, function() {});       
        

        $('#contactinfoWrapper, #contactinfoWrapperPage').animate({
            marginLeft:'0' 
        }, 400, function() {
            $('#mapSlideWrapper').css('display', 'none');
        }); 

        e.preventDefault();
    }); 


    /*
    |--------------------------------------------------------------------------
    | SCROLL (portfolio horizontal)
    |--------------------------------------------------------------------------
    */    
    if($("div#makeMeScrollable").length){
        $("div#makeMeScrollable").smoothDivScroll({

            touchScrolling:true,
            mousewheelScrolling: "vertical"
        });


        $('.showInfo').click(function(e){

            $('.pInfo').find('h3').css('margin-top' , '-400px');
            $('.pInfo').css('display', 'none');
            $(this).next('div').slideDown(300);
            $('.showInfo').not($(this)).animate({
                marginTop:0
            });
            $(this).animate({
                marginTop:$(this).width()
            }, 300);
            $(this).next('div').slideDown(300);
            $(this).next('div').children('h3').animate({
                marginTop:-5
            }, 300);

            e.preventDefault();

        });
        
        $('.closeInfo').click(function(e){

            $(this).parent().slideUp('normal');
            $(this).parent().children('h3').animate({
                marginTop:-400
            }, 200, function() {
                $(this).parent().css('display', 'none');
            });
            $(this).parent().parent().find('img').animate({
                marginTop:0
            });
            
            e.preventDefault();

        });

    }

    /*
    |--------------------------------------------------------------------------
    | FLEXSLIDER
    |--------------------------------------------------------------------------
    */ 
    if($('.flexslider').length){
        $('.flexslider').flexslider({
            animation: "slide",
            controlNav: true,
            directionNav: true,
            slideshow: true,
            start: function(slider){

                setTimeout("animateTxt("+slider.currentSlide+", 'off')", 300);    
            },
            before: function(slider){

                animateTxt(slider.currentSlide, 'on');

            },
            after: function(slider){
                setTimeout("animateTxt("+slider.currentSlide+", 'off')", 300);  

            }

        });
        
        
        /* SILDER OVERLAY ON RESIZE */
        $(window).resize(function() {
            $('.tramOverlay').height($('.slides li img').height());
        });

    }
	    /*
    |--------------------------------------------------------------------------
    | SEQUENCE SLIDER
    |--------------------------------------------------------------------------
    */

    if($('#sequence').length){
        var options = {
            nextButton: true,
            prevButton: true,
            pagination: true,
            animateStartingFrameIn: true,
            autoPlayDelay: 3000,
            preloader: true,
            preloadTheseFrames: [1],
            preloadTheseImages: [
            "images/slider/sequence/tn-model1.png",
            "images/slider/sequence/tn-model2.png",
            "images/slider/sequence/tn-model3.png"
            ]
        };

        var mySequence = $("#sequence").sequence(options).data("sequence");
    }


    /*
    |--------------------------------------------------------------------------
    | DIRECTIONAL ROLLOVER AND HOVER EFFECTS
    |--------------------------------------------------------------------------
    */     

    if($('.da-thumbs').length){
        $('.da-thumbs article a').hoverdir();
    }
    
    
    if($('.da-thumbs ').length){

        $('article a').hover(
            function () {

                /*var centerArticle = ($('a section', this).height()/2 - $('a div span', this).outerHeight(true)/2) + 4; 
                $('a div', this).css('padding-top', centerArticle+'px');*/

                var fromTop = ($('section img', this).height()/2 - $('.iconLink', this).outerHeight()/2) ;
                $('.iconLink', this).css('margin-top', fromTop);


                if ($.browser.chrome || $.browser.webkit ) {

                    $(this).find('section img').css('-webkit-transform', 'scale(1.5) ');
                }
                else if ($.browser.mozilla){

                    $(this).find('section img').css('-moz-transform', 'scale(1.5) rotate(0.1deg)');

                }else if($.browser.opera){

                    $(this).find('section img').css('-o-transform', 'scale(1.5)'); 
                    
                }else if($.browser.msie){

                    $(this).find('section img').css('-ms-transform', 'scale(1.5)'); 

                } 



            }, 
            function () {

                if ($.browser.chrome || $.browser.webkit) {

                    $(this).find('section img').css('-webkit-transform', 'scale(1) ');

                } else if ($.browser.mozilla){

                    $(this).find('section img').css('-moz-transform', 'scale(1) rotate(0deg)');

                } else if($.browser.opera){

                    $(this).find('section img').css('-o-transform', 'scale(1)'); 
                    
                } else if($.browser.msie){

                    $(this).find('section img').css('-ms-transform', 'scale(1)'); 

                }    
            }
            );
}



    /*
    |--------------------------------------------------------------------------
    | ROLLOVER BTN
    |--------------------------------------------------------------------------
    */ 

    $('.socialIcon').hover(
        function () {
            $(this).stop(true, true).addClass('socialHoverClass', 300);
        },
        function () {
            $(this).removeClass('socialHoverClass', 300);
        });





    $('.tabs li, .accordion h2').hover(
        function () {
            $(this).stop(true, true).addClass('speBtnHover', 300);
        },
        function () {
            $(this).stop(true, true).removeClass('speBtnHover', 100);
        });



    

    /*
    |--------------------------------------------------------------------------
    | ALERT
    |--------------------------------------------------------------------------
    */ 
    $('.alert').delegate('button', 'click', function() {
        $(this).parent().fadeOut('fast');
    });
    
    
    /*
    |--------------------------------------------------------------------------
    | CLIENT
    |--------------------------------------------------------------------------
    */   
    
    if($('.colorHover').length){
        var array =[];
        $('.colorHover').hover(

            function () {

                array[0] = $(this).attr('src');
                $(this).attr('src', $(this).attr('src').replace('-off', ''));

            }, 

            function () {

                $(this).attr('src', array[0]);

            });
    }


    /*
    |--------------------------------------------------------------------------
    | UP AND DOWN & MENU BTNS PORTFOLIO STATIC
    |--------------------------------------------------------------------------
    */ 

    $('.goDown').click(function(e){

        var offset = $(this).parents().next('section').offset();
        var variation = ($('.navbar-fixed-top').length)?$('.navbar-fixed-top').outerHeight(true) +20 :90;
        var finalPos  = offset.top - variation;
        
        scrollTo(finalPos, 500);
        e.preventDefault();
        
    });


    $('.goUp').click(function(e){

        var offset = $(this).parents().prev('section').offset();
        var variation = ($('.navbar-fixed-top').length)?$('.navbar-fixed-top').outerHeight(true) +20:90;
        var finalPos  = offset.top - variation;
        
        scrollTo(finalPos, 500);
        e.preventDefault();
    }); 
    

    $('.PortfolioStickyMenu ul li a').click(function(e){


        var targetId =  $(this).attr('href');
        var offset = $(targetId).offset() ;
        var variation = ($('.navbar-fixed-top').length)?$('.navbar-fixed-top').outerHeight(true) +20:90;
        var finalPos  = offset.top - variation;

        scrollTo(finalPos , 500); 
        e.preventDefault();

        
    });
    
    /*
    |--------------------------------------------------------------------------
    | CAMERA SLIDER
    |--------------------------------------------------------------------------
    */ 
    if($('.camera_wrap').length){

        jQuery('.camera_wrap').camera({
            thumbnails: true,
            pagination: true,
            height:'35%'
        });

    }

    if($('.camera_wrap_nonav').length){

        jQuery('.camera_wrap_nonav').camera({
            pagination: false,
            thumbnails: true,
            height:'70%'
        });

    }  
    if($('.camera_wrap_nothumb').length){

        jQuery('.camera_wrap_nothumb').camera({
            pagination: true,
            thumbnails: false,
            height:'40%'
        });

    }  



/*
|--------------------------------------------------------------------------
| SHARRRE
|--------------------------------------------------------------------------
*/
if($('#shareme').length){
    $('#shareme').sharrre({
      share: {
        googlePlus: true,
        facebook: true,
        twitter: true
    },
    buttons: {
        googlePlus: {size: 'tall', annotation:'bubble'},
        facebook: {layout: 'box_count'},
        twitter: {count: 'vertical'},    
    },
    enableHover: false,
    enableCounter: false,
    enableTracking: true,
    url:document.location.href
});
}



//END DOCUMENT READY   
});



/*
|--------------------------------------------------------------------------
| EVENTS TRIGGER AFTER ALL IMAGES ARE LOADED
|--------------------------------------------------------------------------
*/
$(window).load(function() {

    /*
    |--------------------------------------------------------------------------
    | RS SLIDER
    |--------------------------------------------------------------------------
    */   
    if($('.fullwidthbanner').length){

        $('.fullwidthbanner').css('display', 'block');
        
        if(jQuery().revolution) {

            $('.fullwidthbanner').revolution(
            {
                delay:9000,
                startwidth:940,
                startheight:432,

                onHoverStop:"on",// Stop Banner Timet at Hover on Slide on/off

                thumbWidth:100,// Thumb With and Height and Amount (only if navigation Type set to thumb !)
                thumbHeight:50,
                thumbAmount:3,

                hideThumbs:200,
                navigationType:"both",//bullet, thumb, none, both	 (No Shadow in Fullwidth Version !)
                navigationArrows:"verticalcentered",//nexttobullets, verticalcentered, none
                navigationStyle:"round",//round,square,navbar

                touchenabled:"on",// Enable Swipe Function : on/off

                navOffsetHorizontal:0,
                navOffsetVertical:20,

                stopAtSlide:-1,// Stop Timer if Slide "x" has been Reached. If stopAfterLoops set to 0, then it stops already in the first Loop at slide X which defined. -1 means do not stop at any slide. stopAfterLoops has no sinn in this case.
                stopAfterLoops:-1,// Stop Timer if All slides has been played "x" times. IT will stop at THe slide which is defined via stopAtSlide:x, if set to -1 slide never stop automatic



                fullWidth:"off",

                shadow:0, //0 = no Shadow, 1,2,3 = 3 Different Art of Shadows -  (No Shadow in Fullwidth Version !)
                
                
                wrapper:'#sliderWrapperRS',
                wrapperheight:500

            });

} 

}

    /*
    |--------------------------------------------------------------------------
    | ISOTOPE USAGE FILTERING
    |--------------------------------------------------------------------------
    */ 
    if($('.isotopeWrapper').length){

        var $container = $('.isotopeWrapper');
        var $resize = $('.isotopeWrapper').attr('id');
        // initialize isotope
        
        $container.isotope({
            itemSelector: '.isotopeItem',
            containerStyle: { overflow: 'visible', position: 'relative'},
            resizable: false, // disable normal resizing
            masonry: {
                columnWidth: $container.width() / $resize
            }


            
        });

        $('#filter a').click(function(){
            $('#filter a').removeClass('current');
            $(this).addClass('current');
            var selector = $(this).attr('data-filter');
            $container.isotope({
                filter: selector
            });
            return false;
        });
        
        
        $(window).smartresize(function(){
            $container.isotope({
                // update columnWidth to a percentage of container width
                masonry: {
                    columnWidth: $container.width() / $resize
                }
            });
        });
        
        $container.delegate('.masoneryBloc a.sizer', 'click', function(){
            var $this = $(this);
            var resizeElement = $(this).parent();
            $this.find('div').css('display', 'none');
            if(resizeElement.hasClass('span3')){

                resizeElement.removeClass('span3');
                resizeElement.addClass('span6');
                $this.find('div>span>i').attr('class', 'icon-minus');
                
            }else{

                resizeElement.addClass('span3');
                resizeElement.removeClass('span6');
                $this.find('div>span>i').attr('class', 'icon-plus');
                
            }



            
            if($(this).parent().children('.hiddenInfo').css('display') == 'block'){

                $(this).parent().children('.hiddenInfo').css('display', 'none');
                $(this).parent().find('.iconZoom').css('background-position', '217px 702px');
                $(this).parent().find('.mask span').html('Read More');
                
            }else{

                $(this).parent().children('.hiddenInfo').css('display', 'block');
                $(this).parent().find('.iconZoom').css('background-position', '164px 689px');
                $(this).parent().find('.mask span').html('Minimize');
                
            }
            
            $container.isotope('reLayout');
            return false;
        });
    }     




//END WINDOW LOAD
});


/*
|--------------------------------------------------------------------------
| FUNCTIONS
|--------------------------------------------------------------------------
*/



/* FLEXSLIDER INNER INFO CUSTOM ANIMATION */
function animateTxt(curSlide, state){


    if(state == 'off'){

        //change the display state
        // $('.slideN'+curSlide).children('.txt').css('display', 'block');
        // $('.slideN'+curSlide).children('.txt').children('h2').css('display', 'block');
        // $('.slideN'+curSlide).children('.txt').children('div').css('display', 'block');
        // $('.slideN'+curSlide).children('.txt').children('a').css('display', 'inline-block');
        // 

        $('.slideN'+curSlide).children('.txt').children('h2').css('display', 'none');
        $('.slideN'+curSlide).children('.txt').children('div').css('display', 'none');
        $('.slideN'+curSlide).children('.txt').children('a').css('display', 'none');

        $('.slideN'+curSlide).children('.txt').fadeIn(200, 'easeInOutQuart', function () {
            $('.slideN'+curSlide).children('.txt').children('h2').fadeIn(200, 'easeInOutQuart', function () {
               $('.slideN'+curSlide).children('.txt').children('div').fadeIn(200, 'easeInOutQuart', function () {
                   $('.slideN'+curSlide).children('.txt').children('a').fadeIn(200);
               });
           });
        });


        

        //the animation
        // $('.slideN'+curSlide).children('.txt').children('h2').stop(true, true).animate({
        //     marginLeft: 10
        // }, 400, 'easeOutQuad', function() {}).animate({
        //     marginLeft: 0
        // }, 200, 'easeOutQuad', function() {});


        // $('.slideN'+curSlide).children('.txt').children('div').stop(true, true).delay(10).animate({
        //     marginLeft:-10,
        //     opacity:0.8
        // }, 300, 'easeOutQuad', function() {

        // }).animate({
        //     marginLeft: 0,
        //     opacity:1
        // }, 200, 'easeOutQuad', function() {});

        // $('.slideN'+curSlide).children('.txt').children('a').stop(true, true).delay(40).animate({
        //     marginTop: -10 
        // }, 300, 'easeOutQuad', function() {}).animate({
        //     marginTop: 0
        // }, 200, 'easeOutQuad', function() {});

}else{


        //change the display state
        $('.slideN'+curSlide).children('.txt').fadeOut(400);
        $('.slideN'+curSlide).children('.txt').children('h2').css('display', 'none');
        $('.slideN'+curSlide).children('.txt').children('div').css('display', 'none');
        $('.slideN'+curSlide).children('.txt').children('a').css('display', 'none');

        //reposition the elements
        // $('.slideN'+curSlide).children('.txt').children('h2').css('margin-left', '-3000px');
        // $('.slideN'+curSlide).children('.txt').children('div').css('margin-left', '490px');
        // $('.slideN'+curSlide).children('.txt').children('a').css('margin-top', '230px');

    }
}




/* MAIN MENU (submenu slide and setting up of a select box on small screen)*/
(function() {

    var $mainMenu = $('#mainMenu').children('ul');

    $mainMenu.on('mouseenter', 'li', function() {


        var $this = $(this),
        
        $subMenu = $this.children('ul');


        if( $subMenu.length ) $this.addClass('hover').stop();
        else {
            if($this.parent().is($(':gt(1)', $mainMenu))){
                $this.addClass('Shover').stop(false, true).hide().fadeIn('slow');
            }else{
                $this.addClass('Shover').stop(false, true);
            }
        }


        if($this.parent().is($(':gt(1)', $mainMenu))){

            $subMenu.css('display', 'block');
            $subMenu.stop(false, true).animate({
                left:144, 
                opacity:1
            }, 300,'easeOutQuad');



            
        }else{

            $subMenu.stop(false, true).slideDown('fast','easeInQuad'); 
            
        }


    }).on('mouseleave', 'li', function() {


        var $nthis = $(this);
        if($nthis.parent().is($(':gt(1)', $mainMenu))){

            $nthis.children('ul').stop(false, true).css('left', 130).css('opacity', 0).css('display', 'none');

        }else{

            $nthis.removeClass('hover').removeClass('Shover').children('ul').stop(false, true).hide();
        }
        
        $subMenu = $nthis.children('ul');
        
        if( $subMenu.length ) $nthis.removeClass('hover');
        else $nthis.removeClass('Shover');
        
        
    }).on('touchend', 'li ul li a', function(e) {

        var el = $(this);
        var link = el.attr('href');
        window.location = link;
        
    });

    
    // ul to select
    var optionsList = '<option value="" selected>Navigate...</option>';
    $mainMenu.find('li').each(function() {
        var $this   = $(this),
        $anchor = $this.children('a'),
        depth   = $this.parents('ul').length - 1,
        indent  = '';

        if( depth ) {



            
            while( depth > 0 ) {

                indent += ' - ';
                depth--;
                if($this.has('ul').length){
                $anchor.addClass('hasSubMenu');
            }

            }
        }

        optionsList += '<option value="' + $anchor.attr('href') + '">' + indent + ' ' + $anchor.text() + '</option>';
    }).end().after('<select class="responsive-nav">' + optionsList + '</select>');

    $('.responsive-nav').on('change', function() {
        window.location = $(this).val();
    }); 

})();


/* BACK TO TOP (BTN back to top by Matt Varone)*/
(function() {

    var defaults = {
        sym: '<i class="icon-up-open"></i>',
        min: 50,
        inDelay:600,
        outDelay:400,
        containerID: 'to-top',
        containerCLASS: 'iconWrapper',
        containerHoverID: 'to-top',
        scrollSpeed: 300,
        easingType: 'linear'
    },
    settings = $.extend(defaults),
    containerIDhash = '#' + settings.containerID,
    containerHoverIDHash = '#'+settings.containerHoverID;

    $('body').append('<a href="#" id="'+settings.containerID+'" class="'+settings.containerCLASS+'">'+settings.sym+'</a>');
    $(containerIDhash).hide().on('click.UItoTop',function(){
        $('html, body').animate({
            scrollTop:0
        }, settings.scrollSpeed, settings.easingType);
        $('#'+settings.containerHoverID, this).stop().animate({
            'opacity': 0
        }, settings.inDelay, settings.easingType);
        return false;
    })
    //.prepend('<span class="'+settings.containerHoverID+'"></span>')
    .hover(function() {

        $(containerHoverIDHash, this).stop().animate({
            'opacity': 1
        }, 600, 'linear');
    }, function() { 


        $(containerHoverIDHash, this).stop().animate({
            'opacity': 0
        }, 700, 'linear');
    });

    $(window).scroll(function() {
        var sd = $(window).scrollTop();
        if(typeof document.body.style.maxHeight === "undefined") {
            $(containerIDhash).css({
                'position': 'absolute',
                'top': sd + $(window).height() - 50
            });
        }
        if ( sd > settings.min ) 
            $(containerIDhash).fadeIn(settings.inDelay);
        else 
            $(containerIDhash).fadeOut(settings.Outdelay);
    });

})();


/*
|--------------------------------------------------------------------------
| SIDEBAR MENU FOLLOWING WINDOW SCROLL
|--------------------------------------------------------------------------
*/             

function scrollTo($position, $animationTime){

    $('html,body').animate({
        scrollTop: $position
    }, $animationTime);
    
}

/*
|--------------------------------------------------------------------------
| STICKY MENU
|--------------------------------------------------------------------------
*/   

$(function() {
    $window  = $(window);
    if( $(".PortfolioStickyMenu").length && $window.width() >= 754) {
        var $sidebar   = $(".PortfolioStickyMenu"), 

        offset     = $sidebar.offset(),
        topPadding = 90;


        $window.scroll(function() {
            if ($window.scrollTop() > offset.top) {
                if(($window.scrollTop() - offset.top + topPadding) <= $('.lastArticle').position().top + $('.lastArticle').height() + 200){
                    $sidebar.stop().animate({
                        marginTop: $window.scrollTop() - offset.top + topPadding
                    });
                }

            } else {
                $sidebar.stop().animate({
                    marginTop: 0
                });
            }
        });
    }
    
});


/*
|--------------------------------------------------------------------------
| GOOGLE MAP
|--------------------------------------------------------------------------
*/

function appendBootstrap() {
    var script = document.createElement("script");
    script.type = "text/javascript";
    script.src = "http://maps.google.com/maps/api/js?sensor=false&callback=initialize";
    document.body.appendChild(script);
}    




function initialize(id) {

    var image = 'images/icon-map.png';

    var overlayTitle = 'Agencies';

    var locations = [
        //point number 1
        ['Madison Square Garden', '4 Pennsylvania Plaza, New York, NY'],

        //point number 2
        ['Best town ever', 'Santa Cruz', 36.986021, -122.02216399999998],

        //point number 3 
        ['Located in the Midwestern United States', 'Kansas'],

        //point number 4
        ['I\'ll be there one day', 'Chicago', 41.8781136, -87.62979819999998] 
      
        ];

        /*** DON'T CHANGE ANYTHING PASSED THIS LINE ***/
        id = (id == undefined) ? 'mapWrapper' : id;

        var map = new google.maps.Map(document.getElementById(id), {
          mapTypeId: google.maps.MapTypeId.ROADMAP,
          zoom: 14

      });

        var myLatlng;
        var marker, i;
        var bounds = new google.maps.LatLngBounds();
        var infowindow = new google.maps.InfoWindow({ content: "loading..." });

        for (i = 0; i < locations.length; i++) { 


            if(locations[i][2] != undefined && locations[i][3] != undefined){
                var content = '<div class="infoWindow">'+locations[i][0]+'<br>'+locations[i][1]+'</div>';
                (function(content) {
                    myLatlng = new google.maps.LatLng(locations[i][2], locations[i][3]);

                    marker = new google.maps.Marker({
                        position: myLatlng,
                        icon:image,  
                        title: overlayTitle,
                        map: map
                    });

                    google.maps.event.addListener(marker, 'click', (function(marker, i) {
                        return function() {
                          infowindow.setContent(content);
                          infowindow.open(map, this);
                      }

                  })(this, i));

                    if(locations.length > 1){
                        bounds.extend(myLatlng);
                        map.fitBounds(bounds);
                    }else{
                        map.setCenter(myLatlng);
                    }

                })(content);
            }else{

             geocoder   = new google.maps.Geocoder();
             var info   = locations[i][0];
             var addr   = locations[i][1];
             var latLng = locations[i][1];

             (function(info, addr) {

                 geocoder.geocode( {

                    'address': latLng

                }, function(results, status) {

                    myLatlng = results[0].geometry.location;

                    marker = new google.maps.Marker({
                        position: myLatlng,
                        icon:image,  
                        title: overlayTitle,
                        map: map
                    });
                    var $content = '<div class="infoWindow">'+info+'<br>'+addr+'</div>';
                    google.maps.event.addListener(marker, 'click', (function(marker, i) {
                        return function() {
                          infowindow.setContent($content);
                          infowindow.open(map, this);
                      }
                  })(this, i));

                    if(locations.length > 1){
                        bounds.extend(myLatlng);
                        map.fitBounds(bounds);
                    }else{
                        map.setCenter(myLatlng);
                    }
                });
})(info, addr);

}
}
}




