var extensionMethods = {
 
        slide: function (type, next) {
          if(!$.support.transition && this.$element.hasClass('slide')) {
            this.$element.find('.item').stop(true, true); //Finish animation and jump to end.
          }
          var $active = this.$element.find('.active')
            , $next = next || $active[type]()
            , isCycling = this.interval
            , direction = type == 'next' ? 'left' : 'right'
            , fallback  = type == 'next' ? 'first' : 'last'
            , that = this
            , e = $.Event('slide')
    
          this.sliding = true
    
          isCycling && this.pause()
    
          $next = $next.length ? $next : this.$element.find('.item')[fallback]()
    
          if ($next.hasClass('active')) return
    
          if ($.support.transition && this.$element.hasClass('slide')) {
            this.$element.trigger(e)
            if (e.isDefaultPrevented()) return
            $next.addClass(type)
            $next[0].offsetWidth // force reflow
            $active.addClass(direction)
            $next.addClass(direction)
            this.$element.one($.support.transition.end, function () {
              $next.removeClass([type, direction].join(' ')).addClass('active')
              $active.removeClass(['active', direction].join(' '))
              that.sliding = false
              setTimeout(function () { that.$element.trigger('slid') }, 0)
            })
          }else if(!$.support.transition && this.$element.hasClass('slide')) {
            this.$element.trigger(e)
            if (e.isDefaultPrevented()) return
            $active.animate({left: (direction == 'right' ? '100%' : '-100%')}, 600, function(){
                $active.removeClass('active')
                that.sliding = false
                setTimeout(function () { that.$element.trigger('slid') }, 0)
            })
            $next.addClass(type).css({left: (direction == 'right' ? '-100%' : '100%')}).animate({left: '0'}, 600,  function(){
                $next.removeClass(type).addClass('active')
            })
          } else {
            this.$element.trigger(e)
            if (e.isDefaultPrevented()) return
            $active.removeClass('active')
            $next.addClass('active')
            this.sliding = false
            this.$element.trigger('slid')
          }
    
          isCycling && this.cycle()
    
          return this
        }
    };
 
    $.extend(true, $[ "fn" ][ "carousel" ][ "Constructor" ].prototype, extensionMethods);