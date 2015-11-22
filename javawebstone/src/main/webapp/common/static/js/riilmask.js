/**
 * 遮罩，组件关联（每个组件类型只能有一个）
 * @class RiilMask
 */
var RiilMask = function(){

    var _MASK_RENDER = [_maskRender1_fn, _maskRender2_fn, _maskRender3_fn, _maskRender4_fn, _maskRender5_fn, _maskRender6_fn];
    var _LOADING_RENDER = [_loadingRender1_fn, _loadingRender2_fn, _loadingRender2_fn, _loadingRender2_fn, _loadingRender1_fn, _loadingRender2_fn];
    
    var _log = Debug.getLogger('debug.riilmask');
    var _maskPool = []; //[$mask, ...]
    var POOL_SIZE = 7;
//    window["Debugging_RiilMask"] = true;
    var debug = Debug.getLogger('Debugging_RiilMask');
    var _renderId = 1;
    
    var poolCallBack = {
        create : function($where) {
            return createMask(this.style, $where);
        },
        destroy : function($mask) {
            closeMask($mask);
        }
    };
    
    var maskFreeListener = function ($mask){
        freeMask($mask);
    };
    
    var _styleMaskPool = [
        new DomPool({
            capacity : 2, 
            initSize : 0, 
            callBack : $.extend({style : 1}, poolCallBack),
            listeners : {onFree : maskFreeListener}
        }),
        new DomPool({
            initSize : 0, 
            callBack : $.extend({style : 2}, poolCallBack),
            listeners : {onFree : maskFreeListener}
        }),
        new DomPool({
            initSize : 0, 
            callBack : $.extend({style : 3}, poolCallBack),
            listeners : {onFree : maskFreeListener}
        }),
        new DomPool({
            initSize : 0,
            callBack : $.extend({style : 4}, poolCallBack),
            listeners : {onFree : maskFreeListener}
        }),
        new DomPool({
            initSize : 0,
            callBack : $.extend({style : 5}, poolCallBack),
            listeners : {onFree : maskFreeListener}
        }),
        new DomPool({
            initSize : 0,
            callBack : $.extend({style : 6}, poolCallBack),
            listeners : {onFree : maskFreeListener}
        })
    ];
    
    var _api;
    
    //////////////////////////////////////////////////////////////////////////////////////////////
    // API
    //////////////////////////////////////////////////////////////////////////////////////////////
    return _api = {
        _debug_ : ['debug.riilmask'],
        mask: mask,
        hidden : hidden,
        visible : visible,
        unmask: unmask,
        underMask : underMask,
        setText : setText,
        DEFAULT_FULLSCREEN_STYLE : 1,
        DEFAULT_AREA_STYLE : 2
    };

    /**
     * 添加遮罩
     * 
     *     // 举例：指定位置 
     *     RiilMask("Panel", "body");
     *     
     *     // 举例：不指定位置
     *     RiilMask("Panel");
     * 
     *     // 指定上市类型
     *     RiilMask({
     *         id : 'panel_mask',
     *         toWhere : 'config_panel',
     *         style : 1
     *     });
     *
     * @method mask
     * @param config {Json} 参数对象
     * @param config.id {string} 组件ID
     * @param config.toWhere {String/dom/jQuery} 或dom对象或jquery对象，指定遮罩层在dom中的位置
     * @param config.opacity {Boolean} 是否不透明
     * @param config.isLoading {Boolean} 是否显示载入动画
     * @param config.style {Integer} 样式类型，1：深色；2：浅色；3：不可用效果。
     * @return {jquery} 遮罩DIV
     */
    function mask(config){
        config = getParam(config, arguments[1], arguments[2], arguments[3]);
        var $mask; // 遮罩DIV对象
        var $toWhere;
        var style = config.style;
        
        // 决定mask div在dom中的位置
        if (config.toWhere){
            $toWhere = $.getJQueryDom(config.toWhere);
        }
        
        if (!$toWhere || !$toWhere[0]) {
            $toWhere = $(document.body);
            style = _api.DEFAULT_FULLSCREEN_STYLE || 1;
        }else {
            style = _api.DEFAULT_AREA_STYLE || 2;
        }

        style = config.style || style;

        $toWhere.attr("position",$toWhere.css("position") || "");
        if($toWhere.css("position") != "absolute"){
            $toWhere.css("position","relative");
        }

        // 放入DOM中
        $mask = getMask(config.id, style);
        $mask.data('toWhere', $toWhere);
        $mask.appendTo($toWhere);
        
        $mask.css({top: 0,
            left: 0,
            height: '100%',
            width: '100%',
            zIndex: config.zIndex || ZIndexMgr.get()
        });
        if(config.isLoading && !$mask.data('loading')){
            var $loading = _renderLoading_fn(style);
            $toWhere.append($loading);
            $mask.data('loading', $loading);

            refineLoading($loading);
        }      
        if (config.opacity) {
            $mask.css({
                filter: "alpha(opacity=100)",
                opacity: 1
            });
        }
        
        return $mask;
    }

    /**
     * 底部遮罩
     *     
     *     // 举例
     *     RiilMask.underMask('underMask_1', $('#panel'), 55555);
     *     
     * @method underMask
     * @param id {String} 遮罩ID
     * @param $target {Jquery} 被遮罩的对象
     * @param zIndex {Number} z轴坐标
     */
    function underMask(id, toWhere, zIndex) {
        var $toWhere = $.getJQueryDom(toWhere);
        
        if($toWhere.css("position") !="absolute"){
            $toWhere.css("position", typeof $toWhere.attr("positoin") != "undefined" ? $toWhere.attr("positoin") :"static" );
        }

         // 放入DOM中
        $mask = getMask(id, 4);
        
        var position = $.getElementAbsolutePosition($toWhere[0]);
        
        var cssStyle = {top: position.y,
                left: position.x,
                width: $toWhere.outerWidth(),
                height: $toWhere.outerHeight(),
                zIndex: zIndex,
                filter: "alpha(opacity=100)",
                opacity: 1
            };
        
        $mask.css(cssStyle);
        $mask.attr("undermask", "true");
        
        $toWhere.find("div.loading-min").remove();
        return $mask;
    }

    /**
     * 删除遮罩
     *     
     *     // 举例
     *     RiilMask.unmask("Panel");
     *     
     * @method unmask
     * @param id {string} 组件ID
     */
    function unmask(id, tryCount){
        if (getMask.releaseMap && getMask.releaseMap[id]) {
            getMask.releaseMap[id]();
        }
    }
    
    function hidden(id) {
        var $mask = $('#' + id + '_mask');
        var $loading = $mask.data('loading');
        
        if ($loading) $loading.css('visibility', 'hidden');
        if ($mask) $mask.css('visibility', 'hidden');
    }
    
    function visible(config) {
        var $mask = $('#' + config.id + '_mask');
        var $loading = $mask.data('loading');
        
        if ($mask[0]) {
            $mask.css('visibility', 'visible');
            if ($loading) $loading.css('visibility', 'visible');
        }else {
            $mask = mask(config);
        }
        
        return $mask;
    }

    /**
     * 设置loading文字，无loading动画时无效
     * @method setText
     * @param id {String} mask id
     * @param text {String} 文字
     * @return {jQuery} loading动画的jquery对象
     */
    function setText(id, text) {
        var $loading = $('#' + id + '_mask').data('loading');
        if (!$loading) return null;

        $loading.text(text);

        refineLoading($loading);

        return $loading;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // 内部函数
    //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 兼容参数列表传参和JSON传参
     * @method getParam
     * @param id {Json|String} JSON参数或原始参数
     * @param toWhere 原始参数
     * @param opacity 原始参数
     * @param isLoading 原始参数
     * @return {Json} JSON参数对象
     */
    function getParam(id, toWhere, opacity, isLoading) {
        if ($.isPlainObject(id)) {
            return id;
        }else {
            return {
                id : id,
                toWhere : toWhere,
                opacity : opacity,
                isLoading : isLoading
            };
        }
    }
    
    function getMask(id, style) {
        _log("getting " + id);
        
        var $mask = $('#' + id + "_mask");
        if (!$mask[0]) {
            $mask = _styleMaskPool[style - 1].getInstance();
        }
        $mask.attr("id", id + "_mask");
        $mask.attr("name", id + "_mask");
        
        if (!getMask.releaseMap) {
            getMask.releaseMap = {};
        }
        getMask.releaseMap[id] = function () {
            _styleMaskPool[style - 1].free($mask);
            delete getMask.releaseMap[id];
        };
        
        return $mask;
    }

    function closeMask($mask) {
        freeMask($mask);
        $mask.remove();
    }
    
    function createMask(style, where) {
        _log("create");
        var $mask = render_fn(null, where, style);
        return $mask;
    }
    
    function freeMask($mask){
        $mask.data('loading') && $mask.data('loading').remove();
        $mask.removeData('loading');
    }
    
    function checkUnmask($mask, tryCount) {
        var id = $mask.attr('id');

        if ($.isNull(tryCount)) tryCount = 15;
        
        if ($mask[0]) {
            closeMask($mask);
            setTimeout(function() {
                checkUnmask($mask, tryCount - 1);
            });
        }else {
            _log('unmask success');
        }
    }

    function refineLoading($loading) {
        var loadingPaddingLeft = -$loading[0].offsetWidth / 2;
        var loadingPaddingTop = -$loading[0].offsetHeight / 2;
        $loading.css({
            marginLeft: loadingPaddingLeft,
            marginTop: loadingPaddingTop
        });
    }
    
    function render_fn(id, $toWhere, style) {
        return _MASK_RENDER[style - 1]({
            id : id,
            toWhere : $toWhere
        });
    }

    function _maskRender1_fn(config) {
        return _maskRender_fn({
            id : config.id, 
            iframeClass : 'overlay',
            bodyClass : 'overlay_body',
            toWhere : config.toWhere
        });
    }

    function _maskRender2_fn(config) {
        return _maskRender_fn({
            id : config.id, 
            iframeClass : 'overlay-min',
            bodyClass : 'overlay_min_body',
            toWhere : config.toWhere
        });
    }
    
    function _maskRender3_fn(config) {
        var $shade = $('<div id="' + config.id + '" class="shade-disable"></div>');
        $shade.attr('render_id', _renderId)
            .css('position', 'absolute')
            .appendTo(config.toWhere);
        
        _renderId += 1;
        
        return $shade;
    }

    function _maskRender4_fn(config) {
        return _maskRender_fn({
            id : config.id, 
            iframeClass : 'overlay-min',
            bodyClass : 'overlay_body',
            toWhere : config.toWhere
        });
    }
    
    function _maskRender5_fn(config) {
        var $shade = $('<div id="' + config.id + '" class="overlay"></div>');
        $shade.attr('render_id', _renderId)
            .css('position', 'absolute')
            .appendTo(config.toWhere);
        
        _renderId += 1;
        
        return $shade;
    }
    
    function _maskRender6_fn(config) {
        var $shade = $('<div id="' + config.id + '" class="overlay-min"></div>');
        $shade.attr('render_id', _renderId)
            .css('position', 'absolute')
            .appendTo(config.toWhere);
        
        _renderId += 1;
        
        return $shade;
    }

    function _maskRender_fn(config) {
        var $mask = $('<iframe render_id="' + _renderId + 
                '" class="' + config.iframeClass +'" frameborder="0" style="opacity:0.7; filter: alpha(opacity=70);"></iframe>')
            .css({position : "absolute"})
            .appendTo(config.toWhere);
            
        if (config.id) {
            $mask.attr('id', config.id);
            $mask.attr('name', config.id);
        }
        
        _renderId += 1;
        
        var iframe_m = $mask[0];

        var iframeDocument = document.all?iframe_m.contentWindow.document:iframe_m.contentDocument;  
        iframeDocument.open();
        iframeDocument.write([
            '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">',
            '<html>',
            '<head>',
            '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">',
            '<style>',
            'html,body {',
            '   padding: 0;',
            '   margin: 0;',
            '   width: 100%;',
            '   height: 100%;',
            '}',
            '</style>',
            (window.COMP_DEV ?  '<link href="../../../css/widget.css" rel="stylesheet" type="text/css">'
                : '<link href="' + ctx + '/static/css/widget.css" rel="stylesheet" type="text/css">'),
            '</head>',
            '<body class="' + config.bodyClass + '">',
            '</body>',
            '</html>'
        ].join(''));
        iframeDocument.close();
        
        $(iframeDocument.body).click(function() {
            $(document.body).click();
        }).mousemove(function(event) {
            // var parentOffset = $mask.offsetParent().offset();
            var offset = $mask.offset(); 
            offset = {
                top : offset.top, 
                left : offset.left
            };
            $(document.body).trigger(event, {
                pageX : offset.left,
                pageY : offset.top
            });
        }).mouseup(function() {
            $(document.body).mouseup();
        });
        
        return $mask;
    }
    
    

    function _renderLoading_fn(style) {
        return _LOADING_RENDER[style - 1]();
    }

    function _loadingRender1_fn() {
        return _loadingRender_fn({loadingClass : 'loading'});
    }

    function _loadingRender2_fn() {
        return _loadingRender_fn({loadingClass : 'loading-min'});
    }

    function _loadingRender3_fn() {
        return _loadingRender_fn({loadingClass : 'loading-min'});
    }

    function _loadingRender_fn(config) {
        return $('<div class="' + config.loadingClass + '"></div>').css('zIndex', ZIndexMgr.get());
    }
    
}();



var MaskShaw = function(){
    // var COMP_ID = 'shadeDisable';

    return {
        start: start,
        stop: stop
    };

    function start(applyId,isLoading,style){
        // if($("#" + applyId + " #" + COMP_ID + "_mask")[0]){
            // return;
        // }

        RiilMask.mask({
            id : applyId, 
            toWhere : applyId, 
            opacity : false, 
            isLoading : isLoading,
            style: style
        });
    }

    function stop(applyId,position){
        RiilMask.unmask(applyId);
    }
}();