function getParent() {
    try {
        !!(window.parent && window.parent.document);
        return window.parent;
    } catch(e) {
        return window;
    }
}

/**
 * 
 */
var Debug = function(){
    var API = {
        log: log,
        getLogger : getLogger,
        deep : 3
    };
    
    return API;
    
    function getLogger(prefix) {
        return function (/*...*/) {
            if (!window[prefix]) return;
            var args = [prefix + ': '];
            for (var i = 0, c = arguments.length; i < c; i += 1) {
                args.push(arguments[i]);
            }
            log.apply(this, args);
        };
    }
    
    function log(/*...*/) {
        var info = ["[" + getTimeStr() + "] " + arguments[0]];
        var i, c;
        for (i = 1, c = arguments.length; i < c; i+=1) {
            info.push(logElem(arguments[i]));
        }
        if (console && console.info) {
            console.info(info.join(''));
        }
    }
    
    function logElem(argument, deep) {
        if (undefined === deep) {
            deep = API.deep;
        }
        
        deep -= 1;
        
        if (deep < 0) return "...";
        
        if ($.isArray(argument)) {
            return logArray(argument, deep);
        }else if ($.isString(argument)) {
            return logText(argument, deep);
        }else if ($.isFunction(argument)) {
            return logFunction(argument, deep);
        }else if (null === argument) {
            return 'null';
        }else if (undefined === argument) {
            return 'undefined';
        }else if (typeof(argument) === "object" && argument.hasOwnProperty) {
            return logObject(argument, deep);
        }else {
            return '' + argument;
        }
    }
    
    function logText(text, deep) {
        return '"' + text + '"';
    }
    
    function logFunction(text, deep) {
        return '<Function>';
    }
    
    function logObject(obj, deep) {
        var info = [];
        
        for (var key in obj) {
            if (!obj.hasOwnProperty(key)) continue;
            info.push(key + '=' + logElem(obj[key], deep));
        }
        
        return '{' + info.join(', ') + '}';
    }
    
    function logArray(arr, deep) {
        var info = [];
        
        for (var i = 0, c = arr.length; i < c; i+=1) {
            info.push(logElem(arr[i], deep));
        }
        
        return '[' + info.join(', ') + ']';
    }
    
    function getTimeStr() {
        return formatDate(new Date(), "hh:mm:ss(SSS)");
    }
    
    function formatDate(date, format) {
        var o = {
            "M+" : date.getMonth() + 1,     // month
            "d+" : date.getDate(),          // day
            "h+" : date.getHours(),         // hour
            "m+" : date.getMinutes(),       // minute
            "s+" : date.getSeconds(),       // second
            "q+" : Math.floor((date.getMonth() + 3) / 3), // quarter
            "S+" : date.getMilliseconds()    // millisecond
        };
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + "")
                    .substr(4 - RegExp.$1.length));
        }
        for ( var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                var value = "" + o[k];
                if (k.substring(0, 1) === "S") {
                    value = RegExp.$1.length == 1 ? o[k] : ("000" + value).substr(value.length);
                }else {
                    value = RegExp.$1.length == 1 ? o[k] : ("00" + value).substr(value.length);
                }
                format = format.replace(RegExp.$1, value);
            }
        }
        return format;
    }
}();
;;
/**************************************************************************************************
 * 元素池
 * =====
 * @class Pool
 * @constructor
 * @param {Object} conf_ 配置信息
 * @param {Object} conf_.callBack 回调信息
 * @param {Function} conf_.callBack.create 元素创建回调
 * @param {Function} conf_.callBack.destory 元素销毁回调
 * @param {Function} conf_.callBack.isValid 元素有效性验证回调
 * @param {Integer} [conf_.capacity=4] 池容量
 * @param {Integer} [conf_.initSize=2] 初始时可用元素个数
 * @param {Object} [conf_.listeners] 事件监听
 * @param {Object} [conf_.listeners.onFree] 元素释放事件监听
 **************************************************************************************************/
function Pool(conf_) {

    if (!conf_) return this;

    this._log('创建Pool，参数：', conf_);

    /**
     * 空闲元素数组
     * @property _poolFreeArray
     * @private
     * @type {Array}
     */
    this._poolFreeArray = [];

    /**
     * 使用中元素数组
     * @property _poolInUseArray
     * @private
     * @type {Array}
     */
    this._poolInUseArray = [];


    if (conf_.callBack) {
        this.callBack = $.extend({}, this.callBack, conf_.callBack);
    }

    if (conf_.listeners) {
        this.listeners = $.extend({}, this.listeners, conf_.listeners);
    }

    if ($.isNumber(conf_.capacity)) {
        this._CAPACITY = conf_.capacity;
    }

    if ($.isNumber(conf_.initSize)) {
        this._INIT_SIZE = conf_.initSize;
    }

    // 创建多个空闲元素
    this._prepare_fn(this._INIT_SIZE);
}

Pool.prototype = {
    /*============================================================================================
    /* API
    /*============================================================================================*/
    /**
     * 池大小
     * @property _CAPACITY
     * @private
     * @type {Integer}
     */
    _CAPACITY: 100,

    /**
     * 初始元素个数
     * @property _INIT_SIZE
     * @private
     * @type {Integer}
     */
    _INIT_SIZE: 0,

    /**
     * 获取元素
     * @method getInstance
     * @return {Object} 元素
     */
    getInstance: function () {
        this._log('开始getInstance，空闲：' + this._poolFreeArray.length +
            '，正在使用: ' + this._poolInUseArray.length);

        var instance = null;

        // 先从空闲列表中获取，或者创建新的
        if (this._poolFreeArray.length > 0) {
            // 从空闲数组中获取一个有效的元素
            do {
                // 销毁无效元素
                if (instance) {
                    this.callBack.destroy.call(this, instance);
                }
                // 从空闲数组中获取
                instance = this._poolFreeArray.pop();
            } while (instance && !this.callBack.isValid.call(this, instance));
        }
        if (!instance) {
            instance = this._createNew_ifn();
        }
        this._poolInUseArray.push(instance);

        this._log('完成getInstance，空闲：' + this._poolFreeArray.length +
            '，正在使用: ' + this._poolInUseArray.length);

        return instance;
    },

    /**
     * 释放元素
     * @method free
     * @param {Object} instance_ 要释放的元素
     */
    free: function (instance_) {
        this._log('开始free，空闲：' + this._poolFreeArray.length +
            '，正在使用: ' + this._poolInUseArray.length);

        // 从正在使用的列表中删除
        var count = this._poolInUseArray.length;
        while (count--) {
            if (instance_ === this._poolInUseArray[count]) {
                break;
            }
        }
        this._poolInUseArray.splice(count, 1);

        if (this.listeners.onFree) {
            this.listeners.onFree(instance_);
        }

        // 池满了后删除，或者放到空闲数组
        if (this._isFull_fn()) {
            this.callBack.destroy(instance_);
        } else {
            this._poolFreeArray.push(instance_);
        }

        this._log('结束free，空闲：' + this._poolFreeArray.length +
            '，正在使用: ' + this._poolInUseArray.length);
    },

    /*============================================================================================
    /* 需要提供的回调函数
    /*============================================================================================*/
    callBack: {
        /**
         * 创建新元素（需要覆盖）
         * @method create
         * @return {Object} 新元素
         */
        create: function () {
            console.info('[[ Pool.create未实现！ ]]');
        },

        /**
         * 销毁元素（需要覆盖）
         * @method destroy
         * @param {Object} instance_ 要销毁的元素
         */
        destroy: function (instance_) {
            console.info('[[ Pool.callBack.destroy未实现！ ]]');
        },

        /**
         * 检查元素有效性（需要覆盖）
         * @method isValid
         * @param {Object} instance_ 要销毁的元素
         * @return {Boolean} 是否有效
         */
        isValid: function (instance_) {
            console.info('[[ Pool.callBack.isValid未实现！ ]]');
            return true;
        }
    },

    /*============================================================================================
    /* 事件监听
    /*============================================================================================*/
    listeners: {
        onFree: function () {}
    },

    /*============================================================================================
    /* 可覆盖实现（_ifn结尾）
    /*============================================================================================*/

    _createNew_ifn: function (arg1) {
        return this.callBack.create(arg1);
    },

    /*============================================================================================
    /* 内部实现（_fn结尾）
    /*============================================================================================*/
    _isFull_fn: function () {
        return this._poolFreeArray.length + this._poolInUseArray.length > this._CAPACITY;
    },

    /**
     * 创建指定个数空闲元素
     * @method _prepare_fn
     * @private
     * @param {Integer} iCount_ 个数
     */
    _prepare_fn: function (iCount_) {
        this._log('开始_prepare_fn，空闲：' + this._poolFreeArray.length +
            '，正在使用: ' + this._poolInUseArray.length);

        var __this__ = this;
        while (iCount_--) {
            this._poolFreeArray.push(this._createNew_ifn());
        }

        this._log('完成_prepare_fn，空闲：' + this._poolFreeArray.length +
            '，正在使用: ' + this._poolInUseArray.length);
    },
    _log: Debug.getLogger('debug.pool'),
    /*============================================================================================
    /*============================================================================================
    /*============================================================================================*/
    constructor: Pool
};
;;
/**************************************************************************************************
 * DOM元素池
 * =====
 *
 * 池中成员会统一安排在固定的DIV中存放。使用时可能会被移出。释放时一定会放回DIV中。
 *
 * @class DomPool
 * @constructor
 * @extends Pool
 *
 * @param {Object} conf_ 参照{{#crossLink "Pool"}}Pool{{/crossLink}}
 **************************************************************************************************/
function DomPool(conf_) {
    var number = DomPool.count;
    if (!number) number = 1;
    else number += 1;
    DomPool.count = number;

    $('#domPool_domHolder').empty();
    this.$domHolder = $('<div id="domPool_domHolder_' + number + '" ' +
            'style="position:absolute; width: 500px; left: 0; top:0; visibility: hidden;"></div>')
        .appendTo(document.body);

    Pool.call(this, conf_);
}

DomPool.prototype = new Pool();
$.extend(DomPool.prototype, {
    /*============================================================================================
    /* API
    /*============================================================================================*/
    /**
     * 参照{{#crossLink "Pool/getInstance:method"}}Pool.getInstance{{/crossLink}}
     * @method getInstance
     */
    getInstance: function () {
        var $instance = Pool.prototype.getInstance.call(this);
        $instance.css('visibility', 'visible');
        // this._unformat_fn($instance);
        return $instance;
    },

    /**
     * 参照{{#crossLink "Pool/free:method"}}Pool.free{{/crossLink}}
     * @method free
     */
    free: function ($instance_) {
        $instance_.removeAttr('id').removeAttr('name');
        $instance_.css({
            visibility: 'hidden',
            zIndex: -1
        });
        this._format_fn($instance_);
        Pool.prototype.free.call(this, $instance_);
    },

    /*============================================================================================
    /* 覆盖父类
    /*============================================================================================*/

    /**
     * 参照{{#crossLink "Pool/_createNew_ifn:method"}}Pool._createNew_ifn{{/crossLink}}
     * @method _createNew_ifn
     * @private
     */
    _createNew_ifn: function () {
        var $instance = Pool.prototype._createNew_ifn.call(this, this.$domHolder);
        $instance.removeAttr('id').removeAttr('name');
        this._format_fn($instance);
        return $instance;
    },

    /*============================================================================================
    /* 内部实现
    /*============================================================================================*/
    /**
     * 参照{{#crossLink "Pool/callBack:attribute"}}Pool.callBack{{/crossLink}}
     * @attribute callBack
     * @private
     */
    callBack: {
        isValid: function ($instance_) {
            return $.contains(this.$domHolder[0], $instance_[0]);
        }
    },

    /**
     * 调式时：表示空闲元素（修改元素样式，达到可视效果）
     * @method _format_fn 将空闲元素显示出来
     * @param ｛JQuery} 目标元素
     * @private
     */
    _format_fn: function ($instance_) {
        // $instance_.data('css', $instance_.attr('style'));
        $instance_.appendTo(this.$domHolder);
        // $instance_.css({
        // width: 20,
        // height: 20,
        // border: 'solid 1px white',
        // position: 'static',
        // display: 'inline-block'
        // });
    },

    /**
     * 调式时：在使用元素之前，还原对元素样式的修改
     * @method _unformat_fn 还原元素样式
     * @param ｛JQuery} 目标元素
     * @private
     */
    _unformat_fn: function ($instance_) {
        // $instance_.attr('style', $instance_.data('css'));
    },

    /*============================================================================================
    /*============================================================================================
    /*============================================================================================*/
    constructor: DomPool
});
;/* jshint node:true */
/* global $ */
(function(window) {'use strict';

    // 用于node测试
    if (!window) window = module.exports;

/**
 * 类继承
 * @method classExtend
 * @param  {Funciton} _c_ 子类
 * @param  {Funciton} _s_ 父类
 * @return {prototype} 子类的prototype
 */
window.classExtend = function (_c_, _s_) {
    var m, has = Object.prototype.hasOwnProperty;
    for (m in _s_) {if (has.call(_s_, m)) _c_[m] = _s_[m];}
    function Ctor() {this.constructor = _c_;}
    Ctor.prototype = _s_.prototype;
    _c_.prototype = new Ctor();
    return _c_.prototype;
};


/**
 * 用一个dom跟多个dom比较（忽略doms中的自己），判断是否临近。
 * 
 * @getHitItem
 * @param  {DOM} dom 这一个dom
 * @param  {DOM} doms* 多个dom
 * @return {DOM} 临近的dom或undefine
 */
window.getHitItem = function (dom, doms) {
    var everyDom, c = doms.length;
    
    while (c--) {
        everyDom = doms[c];
        if (everyDom === dom) continue;
        if (isHit(everyDom, dom)) {
            return everyDom;
        }
    }

	// 检查参数2是否在参数1上面
	function isHit (dom1, dom2) {
	    var offset1 = dom1.offset();
	    var offset2 = dom2.offset();
	    var distance = Math.sqrt(Math.pow(offset2.left - offset1.left, 2) +
	            Math.pow(offset2.top - offset1.top, 2));
	    return distance < 50;
	}
};

/**
 * 获取dom相对与refDom的left(css)和top(css)
 * @method getRectAboutTo
 * @param  {dom或jQuery} dom 被判断元素
 * @param  {dom或jQuery} refDom 参照元素
 * @return {PlainObject} {left: v, top: v}
 */
window.getRectAboutTo = function (dom, refDom) {
    var refParent = $(refDom).offsetParent();
    var parent = $(dom).offsetParent();
    var position = $(dom).position();
    var subPos, target = $(dom);
    
    while (refParent[0] !== parent[0]) {
        target = target.offsetParent();
        subPos = target.position();
        position.left += subPos.left;
        position.top += subPos.top;
    }

    return position;
};


// 补充数组的indexOf函数
window.arrayIndexOf = function(array, el, index){
    var n = array.length>>>0, i = ~~index;
    if(i < 0) i += n;
    for(; i < n; i++) if(i in array && array[i] === el) return i;
    return -1;
};

window.copyFields = function(des, src) {
    if (!src) return des;
    
    var key = '';
    for (key in src) {
        if (!src.hasOwnProperty(key)) continue;
        if (undefined === src[key]) continue;
        des[key] = src[key];
    }

    return des;
};


})(typeof window === 'undefined' ? undefined : window);;/* jshint node:true */
(function(window) {'use strict';

	// 用于node测试
	if (!window) {
		window = module.exports;
		window.classExtend = require('./3_utils').classExtend;
		window.copyFields = require('./3_utils').copyFields;
	}

	/**
	 * 生成组件类型
	 * @class Clazz
	 * @method Clazz
	 * 
	 */
	window.Clazz = function (id, parentClazz, statics, constructor, fields) {
		// 当未提供parentClazz参数时，参数的值从第二个开始右移
		if (typeof parentClazz === 'function') {
			return window.Clazz(id, undefined, undefined, parentClazz, statics);
		}else if (typeof statics === 'function') {
			if (typeof parentClazz === 'object') {
				return window.Clazz(id, undefined, parentClazz, statics, constructor);
			}else {
				return window.Clazz(id, parentClazz, undefined, statics, constructor);
			}
		}

		// 参数检查
		paramCheck(arguments);

		register(id, constructor);

		window.copyFields(constructor, statics);

		var p;
		if (!parentClazz) {
			p = constructor.prototype = {};
		}else {
			p = window.classExtend(constructor, window[parentClazz]);
		}

		window.copyFields(p, fields);

		return constructor;
	};

	/**
	 * 注册组件类型到window下对应的包里
	 * @method register
	 * @param id {String} 组件类型全名
	 * @param clz {Function} 构造函数
	 */
	function register(id, clz) {
		var pkgs = id.split('.');
		id = pkgs.pop();

		var pkg = window;
		var i, c = pkgs.length;
		for (i = 0; i < c; i++) {
			pkg = pkg[pkgs[i]];
		}

		pkg[id] = clz;
	}

	/**
	 * 生成参数“类型错误”信息
	 * @method paramErrText
	 * @static
	 * @private
	 * @param param {String} 参数名称
	 * @param msg {String} 错误信息
	 * @param wrongValue 错误的值
	 */
	function paramErrText(param, msg, wrongValue) {
		var text = 'Clazz(id, parentClazz, statics, constructor, fields)参数“' + param + '”' + msg + '！';
		if (wrongValue) text += '当前的错误值是“' + wrongValue + '”。';
		return text;
	}

	/**
	 * 参数检查
	 * @method paramCheck
	 * @static
	 * @private
	 * @param args {Array} 参数列表
	 */
	function paramCheck(args) {
		// 参数 id
		if (!args[0] || 'string' != typeof args[0]) throw new Error(paramErrText(
			'id', '必须有值，且是一个字符串', args[0]));

		// 参数 parentClazz
		if (args[1] && 'string' != typeof args[1]) throw new Error(paramErrText(
			'parentClazz', '必须有值，且是一个字符串', args[1]));

		// 参数 statics
		if (args[2] && 'object' != typeof args[2]) throw new Error(paramErrText(
			'statics', '只能是一个对象', args[2]));

		// 参数 constructor
		if (!args[3] || 'function' != typeof args[3]) throw new Error(paramErrText(
			'constructor', '必须有值，且是一个函数', args[3]));

		// 参数 fields
		if (args[4] && 'object' != typeof args[4]) throw new Error(paramErrText(
			'fields', '只能是一个对象', args[4]));

		(function() {
			// 检查包定义
			var id = args[0];
			var pkgs = id.split('.');
			id = pkgs.pop();

			var pkg = window;
			var i, c = pkgs.length;
			for (i = 0; i < c; i++) {
				pkg = pkg[pkgs[i]];
				if (!pkg) throw new Error(paramErrText(
					'id', '公共组件的包“' + pkgs[i] + '”未定义', args[0]));
			}

			// 重复定义
			if (pkg[id]) {
				console.info('公共组件“' + args[0] + '”重复定义，可能是web_ui.js重复引用');
				// throw new Error('公共组件“' + args[0] + '”重复定义');
			}
		})();

		// 父类必须已定义
		if (args[1] && !window[args[1]]) throw new Error(paramErrText(
			'parentClazz', '父类型未定义', args[1]));
	}
})(typeof window === 'undefined' ? undefined : window);;/* global console */

/**
 * 事件处理总线
 * ============
 * 
 * 用于在多个模块交互时解耦合。将所有模块交互操作都通过事件传递方式，
 * 避免了直接调用。
 * 每一个Eventer实例共享一套事件环境，不雕事件环境的事件不能共享。
 * 
 * @module utils
 * @class Eventer
 */
(function() {'use strict';

	/**
	 * 构造函数
	 * @constructor
	 * @method Eventer
	 */
	Clazz('Eventer', null, function () {
		this._eventMap = {};
	}, {

		/**
		 * 注册事件
		 * @method on
		 * @param {String} eventId 事件ID
		 * @param {Function} callback 事件处理函数
		 */
		on : function(eventId, callback) {
			if (!eventId) throw new Error('请指定eventId');
			if (!callback) throw new Error('请指定callback');

			var handlers = this._eventMap[eventId];
			if (!handlers) {
				handlers = this._eventMap[eventId] = [];
			}
			handlers.push(callback);
		},

		/**
		 * 清除事件
		 * @method off
		 * @param {String} [eventId] 事件ID。不提供此参数时清空事件环境内所有注册项
		 * @param {Function} [callback] 事件处理函数。不提供此参数时清空`eventId`下所有注册项
		 */
		off : function(eventId, callback) {
			var handlers, c;

			if (arguments.length === 0) {
				this._eventMap = {};
			}else if (arguments.length === 1) {
				delete this._eventMap[eventId];
			}else {
				handlers = this._eventMap[eventId];
				if (handlers) {
					c = handlers.length;
					while (c--) {
						if (handlers[c] === callback) {
							handlers.splice(c, 1);
						}
					}
				}
			}
		},

		/**
		 * 发出事件
		 * 
		 * 发现事件后，不能直接通过返回值获取事件处理结果，因为处理函数可能不止一个
		 * 
		 * @method send
		 * @param {String} eventId 事件ID
		 * @param {any} [data] 事件相关数据，可选参数
		 */
		send : function(eventId, data) {
			var handlers = this._eventMap[eventId];
			var c;
			if (handlers) {
				c = handlers.length;
				while (c--) {
					try {
						handlers[c](data);
					}catch(e) {
						log.call(this, e);
					}
				}
			}else {
				log.call(this, '事件“' + eventId + '”未注册接收函数');
			}
		}
	});


	/**
	 * 记录日志
	 * @method _log
	 * @private
	 * @param {String} text 日志文字
	 */
	function log (text) {
		var logger = console.err || console.log || console.info;
		if (logger) {
			logger.call(console, text);
		}
	};
})();;/* global ctx,jQuery */
/* debug */
'use strict';
window.pushCometdObj = {};
/**
 * @class Jquery扩展
 */
(function($) {
	$.fn.extend({
		widthSpacing : function() {
			var w = (parseInt(this.css('borderLeftWidth'), 10) || 0) +
				(parseInt(this.css('borderRightWidth'), 10) || 0) +
				(parseInt(this.css('marginLeft'), 10) || 0) +
				(parseInt(this.css('marginRight'), 10) || 0);

			// if (!$.browser.msie) {
				w += (parseInt(this.css('paddingLeft'), 10) || 0) +
					(parseInt(this.css('paddingRight'), 10) || 0);
			// }
			return w;
		},
		
		/**
		 * 计算当前元素自动适应最大宽度
		 * @method getAutoWidth
		 * @param {jQuery|Element} 要计算宽度的对象
		 */
		autoWidth: function($others) {
			var fixWidth = this.parent().widthSpacing();

			this.siblings().each(function(index, elem) {
				fixWidth += $(elem).outerWidth(true);
			});

			if ($others) {
				$.each($others, function(index, elem) {
					fixWidth += $(elem).outerWidth(true);
				});
			}

			fixWidth += this.widthSpacing();
			window.console.info('fixWidth:', fixWidth);
			return fixWidth;
		}
	});
	$.extend({
		/**
		 * 扩展JQuery判断是否是字符串
		 * @borrows $
		 * */
		isString: function(val) {
			return Object.prototype.toString.call(val) === '[object String]';
		},
		isNull: function(val) {
			return val === null || val === undefined;
		},
		/**
		 * 扩展JQuery判断是否是数字
		 * */
		isNumber: function(val) {
			return Object.prototype.toString.call(val) === '[object Number]';
		},
		getParentWindow : function(){
			try{
				window.parent.document;
				return window.parent;
			}catch(e){
				return window;
			}
		},
		/**
		 * 扩展JQuery 字符串转成json对象
		 * */
		StringtoJson: function(str) {
			return JSON.parse(str);
		},
		/**
		 * 扩展JQuery判断是否是函数
		 * */
		isFunction: function(val) {
			return Object.prototype.toString.call(val) === '[object Function]';
		},
		isIE8: function() {
			if (navigator.appName == 'Microsoft Internet Explorer') {
				if (navigator.appVersion.match(/8./i) == '8.') {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		},
		/**
		 * 判断是否是IE
		 * @param version IE版本，如果不指定则判断是否是IE
		 */
		isIE: function(version) {
			var browser = navigator.appName;
			if ($.isNull(version)) {
				if (browser == 'Microsoft Internet Explorer' || /.+ \(MSIE .+;/.test(navigator.appVersion)) {
					return true;
				} else {
					return false;
				}
			} else {
				if (browser == 'Microsoft Internet Explorer') {
					var b_version = navigator.appVersion;
					version = b_version.split(';');
					var trim_Version = version[1].replace(/[ ]/g, '');
					if (trim_Version == 'MSIE7.0') {
						return version == 7;
					}
					if (trim_Version == 'MSIE8.0') {
						return version == 8;
					}
					if (trim_Version == 'MSIE9.0') {
						return version == 9;
					}
					return true;
				// modified by zxt {{{ 修正verison验证失效
				/*
				} else if (/.+ \(MSIE .+;/.test(navigator.appVersion)) {
					return true;
				} else {
				*/
				} else {
				// }}}
					return false;
				}
			}
		},
		/**
		 * 扩展JQuery判断是否是对象
		 * */
		isObject: function(val) {
			return Object.prototype.toString.call(val) === '[object Object]';
		},
		/**
		 * 扩展JQuery 获得函数名
		 * */
		getFnName: function(fn) {
			return fn.toString().match(new RegExp('^function\\s+([^\\s\\(]+)', 'i'))[1];
		},
		/**
		 * 扩展JQuery 转换为Jquery函数
		 * */
		getJQueryDom: function(element) {
			if ($.isString(element)) {
				element = $('#' + element);
			} else {
				element = $(element);
			}
			return element;
		},
		/**
		 * 扩展JQuery 原型继承
		 * */
		apply: function(orial, target) {
			for (var t in target) {
				if (!orial.prototype[t]) {
					orial.prototype[t] = target[t];
				}
			}
		},
		copyObject: function(orial, target) {
			for (var t in orial) {
				if (orial[t]) {
					target[t] = orial[t];
				}
			}
			return target;
		},
		/**
		 * 推数据调用
		 * @param cometdURL String 推数据URL
		 * @param receiveFunction  Function 接收数据后的回调函数，参数msg：接收的文本
		 * @param msgType String 需要接受的消息类型
		 * @param clientId String 页面请求客户端ID,确保唯一
		 *
		 * disconnect();关闭
		 * */
		push: function(cometdURL, receiveFunction, msgType, clientId) {
			var $cometd = window.pushCometdObj[cometdURL];
			var _metaUnsuccessfulFunction = function(cometdURL, receiveFunction, msgType, clientId) {
				return function(msg) {
					if (msg) {
						var $cometd = window.pushCometdObj[cometdURL];
						if (msg.channel == '/meta/connect') {
							if (msg.error && '402::Unknown client' == msg.error) {
								$cometd.handshake();
								$cometd.publish('/regist', {
									msgType: msgType,
									clientId: clientId
								});
							}
						}
					}
				};
			};
			var new$cometd = function() {
				var oComet = new $.Cometd();
				window.pushCometdObj[cometdURL] = oComet;

				oComet.websocketEnabled = true;
				oComet.configure({
					url: cometdURL + '/serverpush',
					logLevel: 'info'
				});
				oComet.clearListeners();
				//oComet.addListener('/meta/handshake', _metaHandshake);
				oComet.addListener('/meta/unsuccessful', _metaUnsuccessfulFunction(cometdURL, receiveFunction, msgType, clientId));
				//							oComet.addListener('/meta/connect', _metaConnect);
				oComet.addListener('/push', receiveFunction);
				oComet.handshake();

				return oComet;
			};
			if (!$cometd) {
				$cometd = new$cometd();
			} else {
				if ($cometd.isDisconnected()) {
					$cometd = new$cometd();
				}
			}
			$cometd.publish('/regist', {
				msgType: msgType,
				clientId: clientId
			});
			return $cometd;
		},

		/**
		 * 格式化日期
		 * @time {int}毫秒数
		 * @format 格式
		 * @example
		 * 	$.formatData(1455211411,'{year}-{date}-{month}');
		 * */
		formatDate: function(time, format) {
			format = format ? format : window.defaultLocale == 'en_US' ? '{month}/{date}/{year} {hours}:{minutes}:{seconds}' : '{year}-{month}-{date} {hours}:{minutes}:{seconds}';
			var tempdate = new Date(time);
			var year = /\{year\}/gim;
			var date = /\{date\}/gim;
			var month = /\{month\}/gim;

			var hours = /\{hours\}/gim;
			var minutes = /\{minutes\}/gim;
			var seconds = /\{seconds\}/gim;



			var hour = tempdate.getHours();
			hour = hour < 10 ? '0' + hour : hour;

			var minute = tempdate.getMinutes();
			minute = minute < 10 ? '0' + minute : minute;

			var second = tempdate.getSeconds();
			second = second < 10 ? '0' + second : second;

			var monthD = tempdate.getMonth() + 1;
			monthD = monthD < 10 ? '0' + monthD : monthD;

			var dateD = tempdate.getDate();
			dateD = dateD < 10 ? '0' + dateD : dateD;


			return format.replace(year, tempdate.getFullYear()).replace(month, monthD).replace(date, dateD).replace(hours, hour).replace(minutes, minute).replace(seconds, second);

		},
		/**
		 * 获得指定区间类型
		 * @param regionType 区间类型 f:30等等f分钟，h小时，d天，m月，y年
		 * @param format 获得时间文本格式，非必填，有默认值{year}-{month}-{date} {hours}:{minutes}
		 * */
		getRegionTime: function(regionType, format) {
			format = format || '{year}-{month}-{date} {hours}:{minutes}';
			var nowDate = new Date().getTime();
			var types = regionType.split(':');
			var dateTypes = [{
				type: 'f',
				time: 60
			}, {
				type: 'h',
				time: 60
			}, {
				type: 'd',
				time: 24
			}, {
				type: 'm',
				time: 30
			}, {
				type: 'y',
				time: 12
			}];

			var regiontime = 1000;

			for (var i = 0, len = dateTypes.length; i < len; i++) {
				regiontime = regiontime * dateTypes[i].time;
				if (dateTypes[i].type == types[0]) {
					break;
				}
			}

			if (!window.ServerTime || !window.ClientTime) {
				return null;
			}

			nowDate = window.ServerTime + (nowDate - window.ClientTime);
			regiontime = regiontime * parseInt(types[1]);
			var beforeTime = nowDate - regiontime;
			return {
				start: {
					time: beforeTime,
					timeStr: $.formatDate(beforeTime, format)
				},
				end: {
					time: nowDate,
					timeStr: $.formatDate(nowDate, format)
				}
			};
		},

		/**
		 * 将日期格式字符串转换为毫秒数
		 * @param timeStr 以‘-’分割的日期
		 * */
		parseDate: function(timeStr) {
			var array = timeStr.split(' ');

			var timeArray = array[0].split('-');
			var date = new Date();

			date.setFullYear(parseInt(timeArray[0]));
			date.setMonth(parseInt(timeArray[1], 10) - 1);
			date.setDate(parseInt(timeArray[2], 10));

			if (array.length > 1) {
				timeArray = array[1].split(':');
				date.setHours(parseInt(timeArray[0], 10));
				date.setMinutes(parseInt(timeArray[1], 10));
				date.setSeconds(0);
			}

			return date.getTime();

		},
		/**验证开始时间和结束时间大小*/
		checkDate: function(startDate, endDate) {
			var start = $.parseDate(startDate);
			var end = $.parseDate(endDate);
			return start < end;
		},
		get_previousSibling: function(n) {
			var x = n.previousSibling;
			if (!x) return null;
			while (x && x.nodeType != 1) {
				x = x.previousSibling;
			}
			return x;
		},
		/**
		 * 获得浏览器窗口大小
		 * @returns {json} {width:,height}
		 */
		getViewport: function() {
			if (document.compatMode == 'BackCompat') {　　　　
				return {　　　　　　
					width: document.body.clientWidth,
					height: document.body.clientHeight　　　　　　
				};　　
			} else {　　　　
				return {　　　　　　　
					width: document.documentElement.clientWidth,
					height: document.documentElement.clientHeight　　　　
				};　
			}
		},
		dimensions: function() {
			var winWidth = 0,
				winHeight = 0;
			// 获取窗口宽度
			if (window.innerWidth) {
				winWidth = window.innerWidth;
			} else if ((document.body) && (document.body.clientWidth)) {
				winWidth = document.body.clientWidth;
			}
			// 获取窗口高度
			if (window.innerHeight) {
				winHeight = window.innerHeight;
			} else if ((document.body) && (document.body.clientHeight)) {
				winHeight = document.body.clientHeight;
			}
			// 通过深入Document内部对body进行检测，获取窗口大小
			if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) {
				winHeight = document.documentElement.clientHeight;
				winWidth = document.documentElement.clientWidth;
			}

			return {
				height: winHeight,
				width: winWidth
			};

		},
		/**
		 * 获得网页大小
		 * @returns {json} {width:,height}
		 */
		getPageArea: function() {　　
			if (document.compatMode == 'BackCompat') {　　　　
				return {　　　　　　
					width: Math.max(document.body.scrollWidth, document.body.clientWidth),
					height: Math.max(document.body.scrollHeight, document.body.clientHeight)
				};
			} else {　　　　
				return {　　　　　　
					width: Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth),
					height: Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight)　　　　
				};
			}
		},
		/**
		 * 获得元素全尺寸包括内外边距
		 * @param dom元素
		 * @return json{width:,height}
		 */
		getAllSize: function(dom) {
			var $dom = $.getJQueryDom(dom);
			var borderSize = $.getBorderWidth(dom);
			return {
				height: $.getVertical($dom, 'margin') + $.getVertical($dom, 'padding') + $dom.height() + borderSize.vertical,
				width: $.getLandscape($dom, 'margin') + $.getLandscape($dom, 'padding') + $dom.width() + borderSize.landscape
			};
		},
		/**
		 * 获得字符串长度，汉字占用2个字符，字母占用1个字符
		 * */
		strLen: function(s) {
			var l = 0;
			var a = s.split('');
			for (var i = 0; i < a.length; i++) {
				if (a[i].charCodeAt(0) < 299) {
					l++;
				} else {
					l += 2;
				}
			}
			return l;
		},
		/**判断点击是否在目标体之外
		 * @param position 点击时的坐标
		 * @param targetDom 点击目标dom元素，可以使id,或dom节点
		 * @return boolean false表示点击在目标体外，true表示之内
		 * */
		checkClickPointerIsOuter: function(position, targetDom) {
			var layout = $.getElementAbsolutePosition(targetDom);
			var brPosition = $.getBottomRight(targetDom);
			if (position.x < layout.x || position.x > brPosition.right || position.y < layout.y || position.y > brPosition.bottom) {
				return true;
			} else {
				return false;
			}

		},

		/**获得元素右下角坐标*/
		getBottomRight: function(dom) {
			var layout = $.getElementAbsolutePosition(dom);
			var borderWidth = $.getBorderWidth(dom);
			var $dom = $(dom);
			return {
				right: layout.x + $dom.width() + $.getLandscape(dom, 'padding') + borderWidth.landscape,
				bottom: layout.y + $dom.height() + $.getVertical(dom, 'padding') + borderWidth.vertical

			};
		},
		/**获得元素边框宽度
		 * @return {landspace:左右边框宽度,vertical:上下边框宽度}
		 * */
		getBorderWidth: function(dom) {
			var $dom = $.getJQueryDom(dom);
			var left = parseInt($dom.css('borderLeftWidth'));
			left = isNaN(left) ? 0 : left;
			var right = parseInt($dom.css('borderRightWidth'));
			right = isNaN(right) ? 0 : right;
			var top = parseInt($dom.css('borderTopWidth'));
			top = isNaN(top) ? 0 : top;
			var bottom = parseInt($dom.css('borderBottomWidth'));
			bottom = isNaN(bottom) ? 0 : bottom;

			return {
				landscape: left + right,
				vertical: top + bottom
			};
		},
		/**
		 * 获得元素距离网页左上角（绝对坐标）
		 * @param element 元素（如果为字符串，则为元素id,否则是dom对象）
		 * @returns {json} {x:,y}
		 */
		getElementAbsolutePosition: function(elem) {
			if ($.isString(elem)) {
				elem = document.getElementById(elem);
			}

			if (!elem) return {
				x: 0,
				y: 0
			};
			var top = 0,
				left = 0;
			if ('getBoundingClientRect' in document.documentElement) {
				var box = elem.getBoundingClientRect(),
					doc = elem.ownerDocument,
					body = doc.body,
					docElem = doc.documentElement,
					clientTop = docElem.clientTop || body.clientTop || 0,
					clientLeft = docElem.clientLeft || body.clientLeft || 0;
				top = box.top + (elem.pageYOffset || docElem && docElem.scrollTop || body.scrollTop) - clientTop;
				left = box.left + (elem.pageXOffset || docElem && docElem.scrollLeft || body.scrollLeft) - clientLeft;
			} else {
				do {
					top += elem.offsetTop || 0;
					left += elem.offsetLeft || 0;
					elem = elem.offsetParent;
				} while (elem);
			}
			return {
				x: left,
				y: top
			};
		},
		/**
		 * 获得元素距离浏览器窗口左上角（相对坐标）
		 * @param element 元素（如果为字符串，则为元素id,否则是dom对象）
		 * @returns {json} {x:,y}
		 */
		getElementRelativePosition: function(element) {
			if ($.isString(element)) {
				element = document.getElementById(element);
			}　　
			var actualLeft = element.offsetLeft;　　　　
			var current = element.offsetParent;
			var elementScrollLeft = 0;
			var elementScrollTop = 0;
			　　　　
			while (current !== null) {　　　　　　
				actualLeft += current.offsetLeft;　　　　　　
				current = current.offsetParent;　　　　
			}　　　　
			if (document.compatMode == 'BackCompat') {　　　　　　
				elementScrollLeft = document.body.scrollLeft;　　　　
			} else {　　　　　　
				elementScrollLeft = document.documentElement.scrollLeft;　　　　
			}　　　　
			var actualTop = element.offsetTop;　　　　
			current = element.offsetParent;　　　　
			while (current !== null) {　　　　　　
				actualTop += current.offsetTop;　　　　　　
				current = current.offsetParent;　　　　
			}　　　　
			if (document.compatMode == 'BackCompat') {　　　　　　
				elementScrollTop = document.body.scrollTop;　　　　
			} else {　　　　　　
				elementScrollTop = document.documentElement.scrollTop;　　　　
			}

			　　　　
			return {
				x: actualLeft - elementScrollLeft,
				y: actualTop - elementScrollTop
			};
		},
		/**
		 * 判断当前鼠标事件坐标是否在制定元素上
		 * return {boolean}
		 * */
		mouseEventBodyRange: function($dom, x, y) {
			var offset = $dom.offset();
			if (x > offset.left && x < offset.left + $dom.width() && y > offset.top && y < offset.top + $dom.height()) {
				return true;
			} else {
				return false;
			}

		},
		/**
		 * 计算Dom显示坐标，判断是否超出页面范围，自动调整
		 * @param {Jquery}需要显示的$dom
		 * @param x 原始x
		 * @param y 原始y
		 * @param width 待计算Dom宽度 可选
		 * @param height 待计算Dom高度 可选
		 * @returns {x:y}
		 * */
		checkDomPosition: function($dom, x, y, width, height) {
			var size = $.getAllSize($dom);
			height = height ? height : size.height;
			width = width ? width : size.width;
			var clientWidth = document.body.clientWidth;
			var clientHeight = document.body.clientHeight;
			var scrollTop = document.body.scrollTop;
			var scrollLeft = document.body.scrollLeft;

			var windowInnerY = clientHeight - (y - scrollTop);
			var checkY = windowInnerY - height; //超出页面的高度
			y = checkY < 0 ? y + checkY - 2 : y; //如果超出页面，则在原来y坐标的基础上向上移超出尺寸，保证其展示完全。
			var windowInnerX = clientWidth - (x - scrollLeft);
			var checkX = windowInnerX - width;
			x = checkX < 0 ? x + checkX - 2 : x;
			return {
				x: x,
				y: y,
				checkY: checkY,
				checkX: checkX
			};
		},
		/*
		 * 计算Dom元素是否超出外层容器
		 * @param conf.$dom 待计算的Dom元素
		 * @param conf.x dom的x坐标
		 * @param conf.y dom的y坐标
		 * @param conf.width dom的宽度
		 * @param conf.height dom的高度
		 * @param conf.outer dom的容器，如果不指定，则为dom的parent
		 */
		checkDomRang: function(conf) {

			var size = $.getAllSize(conf.$dom);
			var height = conf.height ? conf.height : size.height;
			var width = conf.width ? conf.width : size.width;
			var $outer = conf.outer ? $.getJQueryDom(conf.outer) : conf.$dom.parent();
			var left = $outer.width() - conf.x - width; //外层的宽度，减去x坐标，减去dom的宽度，的值如果小于0，则表示dom超出范围
			var x = conf.x;
			if (left < 0) {
				x = x + left;
			}
			var top = $outer.height() - conf.y - height;
			var y = conf.y;
			if (y < 0) {
				y = y + top;
			}
			return {
				x: x,
				y: y
			};

		},
		/**
		 * 获得元素垂直方向Margin/Padding值
		 * @param dom {id/dom}元素
		 * @param styleName 样式名称{string} margin/padding
		 * @returns {int}
		 * */
		getVertical: function(dom, style) {
			var $dom = $.getJQueryDom(dom);
			var top = parseInt($dom.css(style + 'Top'));
			top = isNaN(top) ? 0 : top;
			var bottom = parseInt($dom.css(style + 'Bottom'));
			bottom = isNaN(bottom) ? 0 : bottom;
			return top + bottom;
		},
		/**
		 * 获得元素横向Margin/Padding值
		 * @param dom {id/dom}元素
		 * @param styleName 样式名称{string} margin/padding
		 * @returns {int}
		 * */
		getLandscape: function(dom, style) {
			var $dom = $.getJQueryDom(dom);
			var left = parseInt($dom.css(style + 'Left'));
			left = isNaN(left) ? 0 : left;
			var right = parseInt($dom.css(style + 'Right'));
			right = isNaN(right) ? 0 : right;
			return left + right;
		},
		/**
		 * 计算居中，垂直和水平
		 * */
		calculateCenter: function(apply, dom) {
			dom = $.getJQueryDom(dom);
			if (apply == document.body) {
				var clientWidth = document.body.clientWidth;
				var clientHeight = document.body.clientHeight;

				var scrollTop = document.body.scrollTop;
				var scrollLeft = document.body.scrollLeft;

				dom.css({
					top: scrollTop + clientHeight / 2 - dom.height() / 2,
					left: scrollLeft + clientWidth / 2 - dom.width() / 2
				});
			} else {
				apply = $(apply);
				dom.css({
					top: apply.height() / 2 - dom.height() / 2,
					left: apply.width() / 2 - dom.width() / 2
				});
			}
		},
		/**
		 * 上传
		 * @param uploadUrl:上传URL
		 * @param post_params:post提交附带参数
		 * @param limit 上传文件大小限制
		 * @param type 上传文件类型 默认全部 ,例如.gif .jpg
		 * @param btnId 上传按钮ID即页面dom元素ID,组件会将该DOM渲染为按钮
		 * @param progressId 组件Id
		 *
		 * 开始上传startUpload()
		 * 结构
		 * 	&lt;form id='form1' action='index.php' method='post' enctype='multipart/form-data'>
		 *		&lt;div class='fieldset flash' id='fsUploadProgress'>
		 *		&lt;span class='legend'>Upload Queue&lt;/span>
		 *		&lt;/div>
		 *		&lt;div id='divStatus'>0 Files Uploaded&lt;/div>
		 *		&lt;div>
		 *			<span id='spanButtonPlaceHolder'>&lt;/span>
		 *			<input id='btnCancel' type='button' value='Cancel All Uploads' onclick='swfu.cancelQueue();' disabled='disabled' style='margin-left: 2px; font-size: 8pt; height: 29px;' />
		 *		&lt;/div>
		 *	&lt;/form>
		 * */
		upload: function(conf) {
			var post_params = conf.post_params ? conf.post_params : {};
			var limit = conf.limit ? conf.limit : '10240';
			var btnId = conf.btnId ? conf.btnId : 'spanButtonPlaceHolder';
			var progressId = conf.progressId ? conf.progressId : 'fsUploadProgress';
			var settings = {
				flash_url: ctx + '/static/3rd/swfupload/js/swfupload.swf',
				upload_url: conf.uploadUrl,
				post_params: post_params,
				file_size_limit: limit,
				file_types: '*.*',
				file_types_description: 'All Files',
				file_upload_limit: 500,
				file_queue_limit: 0,
				custom_settings: {
					progressTarget: progressId
				},
				debug: false,
				button_image_url: ctx + '/static/3rd/swfupload/images/upload.png',
				button_width: '69',
				button_height: '24',
				button_placeholder_id: btnId,
				button_text: '<span class="theFont">' + window.S_BTNUPLOAD + '</span>',
				button_text_style: '.theFont { font-size: 12; color:#FFFFFF}',
				button_text_left_padding: 24,
				button_text_top_padding: 3,
				button_window_mode: 'transparent',
				button_cursor: -2,
				// The event handler functions are defined in handlers.js

				file_queued_handler: window.fileQueued,
				file_queue_error_handler: window.fileQueueError,
				file_dialog_complete_handler: window.fileDialogComplete,
				upload_start_handler: window.uploadStart,
				upload_progress_handler: window.uploadProgress,
				upload_error_handler: window.uploadError,
				upload_success_handler: window.uploadSuccess,
				upload_complete_handler: window.uploadComplete,
				queue_complete_handler: window.queueComplete // Queue plugin event

			};
			var swfu = new window.SWFUpload(settings);
			return swfu;
		},
		/**获取cookie值
		 * @param name
		 * */
		getCookie: function(name) {
			var arg = name + '=';
			var alen = arg.length;
			var clen = document.cookie.length;
			var i = 0;
			while (i < clen) {
				var j = i + alen;
				if (document.cookie.substring(i, j) == arg) return $.getCookieVal(j);
				i = document.cookie.indexOf(' ', i) + 1;
				if (i === 0) break;
			}
			return null;
		},
		getCookieVal: function(offset) {
			var endstr = document.cookie.indexOf(';', offset);
			if (endstr == -1) endstr = document.cookie.length;
			return window.unescape(document.cookie.substring(offset, endstr));
		},
		/**判断指定的元素在数组中是否存在*/
		isExistArray: function(item, array) {
			for (var i = 0, len = array.length; i < len; i++) {
				if (array[i] == item) {
					return true;
				}
			}
			return false;
		},
		/**两数组比较是否相同*/
		compareArray: function(array1, array2) {
			var result = array1 == array2;
			if (result) return result;
			if (!array1 || !array2 || array1.length != array2.length) {
				return false;
			}
			for (var i = 0, iLen = array1.length; i < iLen; i++) {
				for (var j = 0, jLen = array2.length; j < jLen; j++) {
					if (array1[i] != array2[j]) {
						return false;
					}
				}
			}
			return true;
		},
		copyArray: function(array) {
			var copy = [];
			for (var i = 0, len = array.length; i < len; i++) {
				copy.push(array[i]);
			}
			return copy;
		},
		/**比较两个对象中的属性是否相同*/
		comparePrototype: function(obj1, obj2) {
			if (!obj1 || !obj2) {
				return false;
			}
			for (var key in obj1) {
				if (obj1[key] != obj2[key]) {
					return false;
				}
			}

			for (var key2 in obj2) {
				if (obj1[key2] != obj2[key2]) {
					return false;
				}
			}

			return true;
		},
		/*
		 * 返回将属性列表中dom对象属性值
		 */
		getDomAttrToJSON: function(dom, attrList) {
			var $dom = $.getJQueryDom(dom);
			var attrJSON = {};
			for (var i = 0, len = attrList.length; i < len; i++) {
				attrJSON[attrList[i]] = $dom.attr(attrList[i]);
			}
			return attrJSON;
		},
		/**创建DOM字符串
		 * @param conf.tagName {String}标签名称
		 * @param conf.style {json} style样式
		 * @param conf.attr {json} 标签属性值
		 * @param conf.attrStr {String} 属性字符串形式a="1" b="2"
		 * @param conf.content {String} 标签内部内容
		 *
		 * */
		createDomStr: function(conf) {
			var tagName = conf.tagName.toLowerCase(),
				style = conf.style,
				attr = conf.attr,
				content = conf.content;
			var dom = [];
			dom.push('<');
			dom.push(tagName);
			if (style) {
				dom.push(' style="');
				for (var key in style) {
					if (style[key]) {
						dom.push(key + ':' + style[key] + ';');
					}
				}
				dom.push('" ');
			}
			if (attr) {
				for (var key2 in attr) {
					if (attr[key2] != null && attr[key2] != undefined) {
						dom.push(' ' + key2 + '="' + attr[key2] + '" ');
					}

				}
			}

			if (conf.attrStr) {
				dom.push(conf.attrStr);
			}

			if (tagName === 'input' || tagName === 'img') {
				dom.push('/>');
				return dom.join('');
			}

			dom.push('>');
			dom.push(content);
			dom.push('</' + tagName + '>');

			return dom.join('');
		},
		/**
		 * 设置文本框只能输入数字
		 * @param conf.input {String/dom} 待绑定文本框
		 * @param conf.decimal {Boolean} 是否允许输入小数  默认不允许false
		 * @param conf.faileCallBack {Function} 自定义不允许输入情况
		 * */
		onlyNumber: function(conf) {
			var $input = $.getJQueryDom(conf.input);
			$input.bind('keydown', {
				conf: conf
			}, _onlyNumber);
		},
		/**
		 * 绑定鼠标点击空白处隐藏
		 * @param conf.dom 需要隐藏的元素
		 * @param conf.param 参数
		 **/
		bindAutoHide: function(conf) {
			var $dom = $.getJQueryDom(conf.dom);
			var param = conf.param || {};
			var $a = $dom.children('a[type="blurFlag"]');
			if (!$a[0]) {
				$a = $('<a href="javascript:void(0)" type="blurFlag"> </a>');
				$a.bind('blur', {
					$dom: $dom,
					param: param
				}, blurHide);
				$dom.append($a);
			}
			$dom.bind('mousedown', checkIsCheckInner);
			$a.focus();
		},
		unbindAutoHide: function(conf) {
			var $dom = $.getJQueryDom(conf.dom);
			$dom.children('a[type="blurFlag"]').unbind('blur', blurHide);
			$dom.unbind('mousedown', checkIsCheckInner);
		},
		/*获得页面中选中的内容*/
		getSelectText: function() {
			var content = null;
			if (document.all) {
				content = document.selection.createRange();
			} else {
				content = window.getSelection();
				content.text = content.toString();
			}
			var str = content.text; //选择的内容
			return str;
		},
        /**获得url请求参数*/
        getUrlParam: function(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        }
	});

	function checkIsCheckInner(event) {
		$(event.currentTarget).attr('noHide', 'true');
	}

	//失去焦点隐藏
	function blurHide(event) {
		var a = event.currentTarget;
		window.setTimeout(function() {
			var $dom = event.data.$dom;
			if ($dom.attr('noHide')) {
				$dom.removeAttr('noHide');
				try {
					var scrollTop = $dom.scrollTop();
					a.focus();
					$dom.scrollTop(scrollTop);
				} catch (e) {

				}

				return;
			}
			$dom.unbind('mousedown', checkIsCheckInner);
			var result = null;
			if (event.data.param.callback) {
				result = event.data.param.callback(event.data.param);
			}
			if (result !== false) {
				$dom.hide().removeAttr('noHide');
			}
		}, 200);
	}
	/*文本框只能输入数字具体执行函数*/
	function _onlyNumber(event) {
		var conf = event.data.conf;
		var keyCode = event.keyCode;
		var decimal = conf.decimal; //允许输入小数点
		//如果输入的键值为数字，（大48-57小96-105）退格(8)，删除按钮（大(105)小(110)键盘），home end上下左右（35-39）,numLock(144)，Tab(9) 小数点(190)
		if (keyCode >= 48 && keyCode <= 57 ||
			keyCode >= 96 && keyCode <= 105 ||
			keyCode >= 36 && keyCode <= 39 ||
			decimal === true && keyCode == 190 ||
			keyCode == 8 || keyCode == 110 || keyCode == 46 || keyCode == 9 || keyCode == 144 || keyCode == 18) {
			if (!decimal && keyCode == 110) {
				event.preventDefault();
			}
			return;
		} else {
			if (conf.faileCallBack) {
				conf.faileCallBack(event.currentTarget, event);
			}
			event.preventDefault();

			return false;
		}
	}
})(jQuery);;	var OnResize = {
		flag :false,
		timeout : null,
		onresizeStart:false,
		winSize : {},
		layoutFn : [],
		bind:function(){
			if(!this.flag){
				this.$win = $(window);
				this.winSize.width = this.$win.width();
				this.winSize.height = this.$win.height();
				this._addEvent(window,"resize",function(){	
					var or = OnResize;
					var width = or.$win.width();
					var height = or.$win.height();
					if(width != or.winSize.width || height != or.winSize.height){
						clearTimeout(or.timeout);
						or.timeout = setTimeout(or.timeoutResize,1000);
					}					
					or.winSize.width = width;
					or.winSize.height = height;
				});
				this.flag = true;
			}
		},
		_addEvent:function(obj,type,fun){
			if(obj.addEventListener){
				obj.addEventListener(type,fun);
				return true;
			}else if(obj.attachEvent){
				return obj.attachEvent("on"+type,fun);
			}else{
				return false;
			}
		},
		timeoutResize:function(){
			var or = OnResize;
			for(var i=0;i<or.layoutFn.length;i++){
				if(or.layoutFn[i]){
					or.layoutFn[i]();
				}
			}
		},
		addLayout:function(fn){
			for(var i=0, len = OnResize.layoutFn.length;i<len;i++){
				if(OnResize.layoutFn[i] ==  fn){
					return;
				}
			}
			OnResize.layoutFn.push(fn);
		}
	};window.CursorPosition = {
	get: function (textarea) {
		var rangeData = {text: "", start: 0, end: 0 };
	
		if (textarea.setSelectionRange) { // W3C	
			textarea.focus();
			rangeData.start= textarea.selectionStart;
			rangeData.end = textarea.selectionEnd;
			rangeData.text = (rangeData.start != rangeData.end) ? textarea.value.substring(rangeData.start, rangeData.end): "";
		} else if (document.selection) { // IE
			textarea.focus();
			var i,
				oS = document.selection.createRange(),
				// Don't: oR = textarea.createTextRange()
				oR = document.body.createTextRange();
			oR.moveToElementText(textarea);
			
			rangeData.text = oS.text;
			rangeData.bookmark = oS.getBookmark();
			
			// object.moveStart(sUnit [, iCount]) 
			// Return Value: Integer that returns the number of units moved.
			for (i = 0; oR.compareEndPoints('StartToStart', oS) < 0 && oS.moveStart("character", -1) !== 0; i ++) {
				// Why? You can alert(textarea.value.length)
				if (textarea.value.charAt(i) == '\r' ) {
					i ++;
				}
			}
			rangeData.start = i;
			rangeData.end = rangeData.text.length + rangeData.start;
		}
		
		return rangeData;
	},
	
	set: function (textarea, rangeData) {
		var oR, start, end;
		if(!rangeData) {
			alert("You must get cursor position first.")
		}
		textarea.focus();
		if (textarea.setSelectionRange) { // W3C
			textarea.setSelectionRange(rangeData.start, rangeData.end);
		} else if (textarea.createTextRange) { // IE
			oR = textarea.createTextRange();
			
			// Fixbug : ues moveToBookmark()
			// In IE, if cursor position at the end of textarea, the set function don't work
			if(textarea.value.length === rangeData.start) {
				//alert('hello')
				oR.collapse(false);
				oR.select();
			} else {
				oR.moveToBookmark(rangeData.bookmark);
				oR.select();
			}
		}
	},

	add: function (textarea, rangeData, text) {
		var oValue, nValue, oR, sR, nStart, nEnd, st;
		this.set(textarea, rangeData);
		
		if (textarea.setSelectionRange) { // W3C
			oValue = textarea.value;
			nValue = oValue.substring(0, rangeData.start) + text + oValue.substring(rangeData.end);
			nStart = nEnd = rangeData.start + text.length;
			st = textarea.scrollTop;
			textarea.value = nValue;
			// Fixbug:
			// After textarea.values = nValue, scrollTop value to 0
			if(textarea.scrollTop != st) {
				textarea.scrollTop = st;
			}
			textarea.setSelectionRange(nStart, nEnd);
		} else if (textarea.createTextRange) { // IE
			sR = document.selection.createRange();
			sR.text = text;
			sR.setEndPoint('StartToEnd', sR);
			sR.select();
		}
	}
};/* jshint jquery:true */
'use strict';
/**
 * 列表悬浮工具组
 *
 * _HTML:_
 *    <ul id="operList">
 *      <div>
 *        <span class="icon icon_delete" operation="delete" title="删除"></span>
 *        <span class="icon icon_edit" operation="edit" title="编辑"></span>
 *      </div>
 *      <li><a>无悬浮操作</a></li>
 *      <li operation="edit,delete"><a>数据项1</a></li>
 *      <li operation="edit,delete"><a>数据项2</a></li>
 *      <li operation="delete"><a>只有删除</a></li>
 *      <li operation="edit"><a>只有编辑</a></li>
 *    </ul>
 *
 * @module base
 * @class FloatingOperation
 */
window.Clazz('FloatingOperation', function(attrs) {
	window.ConfigData.check('FloatingOperation.constructor:参数attrs', attrs, {
		type: ['object'],
		elems: {
			$target: {
				type: ['object']
			},
			operations: {type:['object'], required:false}
		}
	});

	window.ConfigData.checkDomElem('FloatingOperation.constructor:参数attrs.$target', attrs.$target);

	$.extend(this, attrs);

	this.$target = $(attrs.$target);

	this._render();
	this._event();
}, {
	_render : function() {
		// 工具条
		this._$operationBar = this.$target.children('div').children('span[operation]').parent();
		this._$operationBar.css('position', 'fixed').hide();

		// 补充数据项中的span，用于计算文字显示宽度
		this.$target.children('li[operation]').children('a').wrapInner('<span></span>');
	},

	_event : function() {
		this.$target.find('li>a,li>span')
			.mouseenter($.proxy(function(event) {
				var $li = $(event.target).closest('li');
				this._show($li);
				event.stopPropagation();
			}, this));

		this._switchEvent = [];
	},

	_eventSwitchOn : function() {
		this.$target
			.mouseleave(this._switchEvent.mouseleave = $.proxy(function(event) {
				if (this._isMoveout(event)) this._hide();
			}, this));

		this._$operationBar.children('span[operation]:visible')
			.click(this._switchEvent.click = $.proxy(function(event) {
				var operation = $(event.target).attr('operation');
				if (this.lastLi) this._onOperation(operation, $(this.lastLi));
 			}, this));
	},

	_eventSwitchOff : function() {
		this.$target.unbind('mouseleave');
		this._$operationBar.children('span[operation]').unbind('click');
	},

	_onOperation : function(operation, $li) {
		if (this.operations) {
			if ($.isFunction(this.operations[operation])) {
				this.operations[operation]($li);
			}
		}
	},

	_isMoveout : function(mouseleaveEvent) {
		var rect = this.$target.offset();
		rect.width = this.$target.width();
		rect.height = this.$target.height();

		var pos = {left:mouseleaveEvent.left, top:mouseleaveEvent.top};

		return !(rect.left <= pos.left && pos.left <= rect.left + rect.width &&
			rect.top <= pos.top && pos.top <= rect.top + rect.height);
	}, 

	_show : function($li) {
		if (!$li[0] || $li[0] === this.lastLi) return;
		if (!$li.attr('operation')) {
			this._hide();
			return;
		}

		this._eventSwitchOff();

		this._$operationBar
			.css('zIndex', window.ZIndexMgr.get())
			.show();

		var operations = $li.attr('operation').split(',');
		this._$operationBar.children('span').each(function(index, span) {
			if ($.inArray($(span).attr('operation'), operations) >= 0) {
				if ($(span).is(':hidden')) $(span).show();
			}else {
				if ($(span).is(':visible')) $(span).hide();
			}
		});

		this._$operationBar
			.offset(function() {
				var offset = $li.offset();
				var $text = $li.children('a:first').children('span:first');
				if ($text.length === 0) $text = $li.children('span:first');
				var left = $text.offset().left + $text.width();
				var maxLeft = offset.left + $li.width() - this._$operationBar.width();
				offset.left = left <= maxLeft ? left : maxLeft;
				offset.top -= ($text.outerHeight(true) - this._$operationBar.height()) / 2;
				return offset;
			}.call(this));

		// 判断是否为树节点
		if (!$li.children('a').length) {
			this._adjustTreeNode($li);
			this._restoreTreeNode(this.lastLi);
		}else {
			$li.children('a,span').css('paddingRight', this._$operationBar.width());
			$(this.lastLi).children('a,span').css('paddingRight', '0px');
		}

		this._eventSwitchOn();

		this.lastLi = $li[0];
	},

	_hide : function() {
		this._eventSwitchOff();
		window.ZIndexMgr.free(this._$operationBar);
		this._$operationBar.hide();

		if (!$(this.lastLi).children('a').length) {
			this._restoreTreeNode(this.lastLi);
		}else {
			$(this.lastLi).children('a,span').css('paddingRight', '0px');
		}
		this.lastLi = null;
	},

	_adjustTreeNode : function($li) {
		var $text = $li.children('a,span');
		var $container = $li.closest('tree-wrap');

		/* 可知：
		 *      文字的X坐标(x1)
		 *      显示区宽度(w2)
		 *      显示区X坐标(x2)
		 *      工具条宽度(w3)
		 * 求:  
		 *      显示工具条时LI宽度(w?)
		 *      
		 * +----------------------------------+
		 * |  +----------------------------+  |
		 * |  |      TEXT_XXXXXXXXXXX #### |  |
		 * |<-- x1 ->|
		 * |x2|<----------- w2 ----------->|  |
		 *    |<-w4->|<----- w? ---->| w3  |
		 * 
		 * w? = w2 - w4 - w3
		 * w? = w2 - (x1 - x2) - w3
		 * w? = w2 - x1 + x2 - w3
		 */
		var w2 = $container.width();
		var x1 = $li.offset().left;
		var x2 = $container.offset().left;
		var w3 = this._$operationBar.width();
		var width = w2 - x1 + x2 - w3;

		// 保存原始样式
		$text.data('style', $text[0].style);
		// 修改样式
		$text.css('text-overflow', 'ellipsis')
			.width(width);
	},

	_restoreTreeNode : function($li) {
		var $text = $li.children('a,span');
		if ($text.data('style')) {
			$text[0].style = $text.data('style');
			$text.removeData('style');
		}
	}
});;/**
 * @class
 * 组件基类
 */
document.ondragstart=function (){return false;};
if(!window.console){
	window.console = {
		info:function(){},
		dir:function(){}
	}
}
var ComponentManager = function(){
	/** @lends ComponentManager# */
	return {
		componentContain:{},
		componentInstanceContain:{},
		registComponent:function(cmpId, component, createType){
			var IComponent = {};
			IComponent.template = function(){
				return '<div></div>';
			};
			var t_methods = ["init","getRenderData","render","bindEvent","finished","reload","destroy"];
			for(var i=0;i<t_methods.length;i++){
				var method = t_methods[i];
				if(!component.prototype[method]){
					component.prototype[method] = function(){return this;};
				}
			}
			var t_constructor = component.prototype.constructor;
			component.prototype.constructor = function(config){
				// 构造前处理。。。
				t_constructor(config);
				// 构造后处理。。。
			};
			// createType 用于标示此组件是否为单例
			this.componentContain[cmpId] = {cmp:component, instanceType:createType};
		},
		/**
		 * 获得组件实例
		 * @param cmpId 组件类型ID
		 * @param config 配置参数<br>
		 * {<br>
		 * 	initType:初始化类型'auto'自动初始化<br>
		 *  bindDomId:"绑定页面上的元素ID"<br>
		 *  ...组件自定义配置参数<br>
		 * }<br>
		 * */
		createComponent:function(cmpId, config){
			var t_component = this.componentContain[cmpId];
			if(null !== t_component){
				var t_instance;
				// 根据在注册组件时定义的组件实例化类型来判断是否需要实例化一个新的组件实例，或者使用单例
				if(t_component.instanceType === 'singleton'){
					t_instance = this.componentInstanceContain[cmpId];
					if(!t_instance){
						t_instance = new (t_component.cmp)(config);
						this.componentInstanceContain[cmpId] = t_instance;
					}
				}else{
					t_instance = new (t_component.cmp)(config);
					this.componentInstanceContain[config.id] = t_instance;
				}
				if(config.initType === 'auto'){
					t_instance.init(config).getRenderData(config).render(config).bindEvent(config).finished(config);
				}
				return t_instance;
			}
		}
	};
}();







// dom 对象描述型参数 例如:div>>h2#{value}>>ul>>li#>>span{value}
// IComponent.template = function(){};


var CompMgr = function() {
    var compPrototypes = {}; // key:compId,val:组件结构类
    var compClasses = {}; //
    var compInsts = {};
	
	var parent = {
		setDomId:function(domId){
			this.domId = domId;
			return this;
		}
	}
	
    return {
		/*
		 * @inner
		 * 注册组件
		 * */
        regComp: function(compID, compProto) {
            compPrototypes[compID]=  new compProto();
        },
        /*
         * @inner
         * 创建组件
         * */
        createComp: function(compID, renderName) {			
            var compClass = compClasses[compID + renderName];
            if (!compClass) {
                compClass = function() {};
                var compProp = compPrototypes[compID];
				 var render = compProp.constructor.render[renderName];
                compClass.prototype = compProp;
                compClass.prototype.constructor = compClass;
                for (var key in render) {// 将某一个渲染放入到组件原型中
                    compClass.prototype[key] = render[key];
                }
				for(var key in parent){// 继承父类方法
					 if(!compClass.prototype[key])  compClass.prototype[key]=parent[key];
				}
                compClasses[compID + renderName] = compClass;
            }
            return compClass;
        },
        /**
         * 获得组件实例
         * @param conf 配置参数<br>
         * ｛<br>
         * 	compId:组件ID<br>
         * 	InstanceId：组件实例ID<br>
         * 	
         * ｝<br>
         * */
        getComp: function(conf, isNew,isAuto) {
			// 实例ID是否有，如果没有组件类型实例是否有
			var compInst = compInsts[conf.compID+conf.InstanceId] ? compInsts[conf.compID+conf.renderName] : null;
			if(!compInst || isNew){// 都没有
				var compClass = this.createComp(conf.compID,conf.renderName);				
				compInst = new compClass(conf);				
			}
			compInst.setDomId(conf.domId);
			if(isAuto){
				compInst.init(conf).render(conf).bindEvent(conf);
			}
			return compInst;
        }
    }
}();

  ;var LocalData = {
    /**
     *创建并获取这个input:hidden实例     
     * @return {HTMLInputElement} input:hidden实例
     * @private
     */
    _getInstance : function (){
        //把UserData绑定到input:hidden上
        var _input = null;
        //是的，不要惊讶，这里每次都会创建一个input:hidden并增加到DOM树种
        //目的是避免数据被重复写入，提早造成“磁盘空间写满”的Exception
        _input = document.createElement("input");
        _input.type = "hidden";
        _input.addBehavior("#default#userData");
        
        document.body.appendChild(_input);

        return _input;
    },
    /**
     * 保存本地数据
     * @method save
     * @param key 数据key
     * @param value 数据值
     * @param expires 数据失效期  毫秒
     */
    save : function(key ,value,expires){
        if(window.localStorage){
            this.__html5setItem({
                key : key,
                value : value,
                expires : expires
            });
        }else{
            this.__setItem(key,{
                key : key,
                value : value,
                expires : expires
            });
        }
    },
    /**
     * 保存本地数据
     * @method get
     * @param key 数据key    
     * @return  数据值
     */    
    get : function(key){
         if(window.localStorage){
            return this.__html5getItem({
                key : key               
            });
        }else{
            return this._getItem({
                key : key                           
            });
        }       
    },
    /**
     * 将数据通过UserData的方式保存到本地，文件名为：文件名为：config.key[1].xml
     * @param {String} key 待存储数据的key，和config参数中的key是一样的
     * @param {Object} config 待存储数据相关配置
     * @cofnig {String} key 待存储数据的key
     * @config {String} value 待存储数据的内容
     * @config {String|Object} [expires] 数据的过期时间，可以是数字，单位是毫秒；也可以是日期对象，表示过期时间
     * @private
     */
    __setItem : function (key,config){
        try {
            var input = this._getInstance();
            //创建一个Storage对象
            var storageInfo = config || {};
            //设置过期时间
            if(storageInfo.expires) {
                var expires;
                //如果设置项里的expires为数字，则表示数据的能存活的毫秒数
                if ('number' == typeof storageInfo.expires) {
                    expires = new Date();
                    expires.setTime(expires.getTime() + storageInfo.expires);
                }                
                input.expires = expires.toUTCString();
            }
                     
            //存储数据
            input.setAttribute(storageInfo.key,storageInfo.value);
            //存储到本地文件，文件名为：storageInfo.key[1].xml
            input.save(storageInfo.key);
        } catch (e) {
        }
    },
    /**
     * 提取本地存储的数据
     * @param {String} config 待获取的存储数据相关配置
     * @cofnig {String} key 待获取的数据的key
     * @return {String} 本地存储的数据，获取不到时返回null
     * @example 
     * qext.LocalStorage.get({
     *      key : "username"
     * });
     * @private
     */
    _getItem : function (config){
        try {            
            var input = this._getInstance();           
            //载入本地文件，文件名为：config.key[1].xml
            input.load(config.key);
     
            //取得数据
            return input.getAttribute(config.key) || null;
        } catch (e) {
            
            return null;
        }
    },
    /**
     * 移除某项存储数据
     * @param {Object} config 配置参数
     * @cofnig {String} key 待存储数据的key
     * @private
     */
    _removeItem : function(config){
        try {
            var input = _getInstance();
            //载入存储区块
            input.load(config.key);
            //移除配置项
            input.removeAttribute(config.key);
            //强制使其过期
            var expires = new Date();
            expires.setTime(expires.getTime() - 1);
            input.expires = expires.toUTCString();
            input.save(config.key);
                   
            //从allkey中删除当前key           
            //下面的代码用来记录当前保存的key，便于以后clearAll
            var result = _getItem({key : _clearAllKey});
            if(result) {
                result = result.replace(new RegExp("(^|\\|)" + config.key + "(\\||$)",'g'),'');
                result = {
                    key : _clearAllKey,
                    value : result 
                };
                //保存键
                __setItem(_clearAllKey,result); 
            }
                   
        } catch (e) {
        }
    },
    __html5setItem : function(config){
        window.localStorage.setItem(config.key,config.value);
        // 如果需要指定生命周期
        if(config.expires) {
            var expires;
            //如果设置项里的expires为数字，则表示数据的能存活的毫秒数
            if ('number' == typeof config.expires) {
                expires = new Date();
                expires.setTime(expires.getTime() + config.expires);
            }
          
            window.localStorage.setItem(config.key + ".expires",expires);
        }
    },
    __html5getItem : function(config){
        var result = window.localStorage.getItem(config.key);
        //过期时间判断，如果过期了，则移除该项
        if(result) {
            var expires = window.localStorage.getItem(config.key + ".expires");
          //  result = {
            //    value : result,
            //    expires : expires ? new Date(expires) : null
           // };
            if(result && result.expires && result.expires < new Date()) {
                result = null;
                window.localStorage.removeItem(config.key);
            }
        }
        return result;
    },
     /**
     * 清除所有本地存储的数据
     * <pre><code>
     * qext.LocalStorage.clearAll();
     * </code></pre>
     */
    __html5clearAll : function(){
        //支持本地存储的浏览器：IE8+、Firefox3.0+、Opera10.5+、Chrome4.0+、Safari4.0+、iPhone2.0+、Andrioid2.0+
         if(_isSupportLocalStorage) {
            window.localStorage.clear();
        } else if(_isSupportUserData) { //IE7及以下版本，采用UserData方式
            _clearAll();
        }
    }

};/* global $,ConfigData,window */
(function() {
	'use strict';
	/**
	 * #元素调序
	 * 
	 * ![Alt text](../assets/img/order_c_1.png "点击“下移”前")
	 * ![Alt text](../assets/img/order_c_2.png "点击“下移”后")
	 *
	 * ##举例
	 * ###HTML:
	 *
	 *     <div class="addlist_l addlist_lw220" style="width:215px">
	 *     	<div class="addlist_k" order="order">
	 *     		<h1>
	 *     			已选指标
	 *     			<span class="icon icon_delete edit_btn" style="float:right;" type="tool" title="删除" orderAction="delete"></span>
	 *     			<span class="icon icon_descending edit_btn" style="float:right;" type="tool" title="下移" orderAction="movedown"></span>
	 *     			<span class="icon icon_sortascending edit_btn" style="float:right;" type="tool" title="上移" orderAction="moveup"></span>
	 *     		</h1>
	 *     		<div class="addlist_k_tree addlist_tree_h240" style="overflow-y:auto;height:200px">
	 *     			<div style="height: 200px; position: static;" id="" position="static">
	 *     				<ul id="resourceSelected">
	 *     					<li orderItem="order">
	 *     						<input type="checkbox" orderCheck="order">CPU1</li>
	 *     					<li orderItem="order">
	 *     						<input type="checkbox" orderCheck="order">CPU2</li>
	 *     					<li orderItem="order">
	 *     						<input type="checkbox" orderCheck="order">CPU3</li>
	 *     				</ul>
	 *     			</div>
	 *     		</div>
	 *     	</div>
	 *     </div>
	 *
	 * ###Javascript:
	 * 
	 *     var oOrder = new (window.Order)({groupId : 'order'});
	 *
	 * @class Order
	 */
	window.Clazz('Order',

		/**
		 * #构造函数
		 * 
		 *     var oOrder = new (window.Order)({groupId : 'order'});
		 *
		 * @constructor
		 * @method Order
		 * @param attrs {Json} 属性
		 * @param attrs.groupId {JQuery} 排序组
		 */
		function(attrs) {
			ConfigData.check('Order.constructor:参数attrs', attrs, {
				type: ['object'],
				elems: {
					groupId: {
						type: ['string']
					}
				}
			});

			$.extend(this, attrs);

			this._event();
		}, {
			_event : function() {
				var $scope = $('[order="' + this.groupId + '"]');

				$('[orderAction="movedown"]', $scope).click($.proxy(function() {
					this._move(false);
				}, this));

				$('[orderAction="moveup"]', $scope).click($.proxy(function() {
					this._move(true);
				}, this));

				$('[orderAction="delete"]', $scope).click($.proxy(function() {
					this._delete();
				}, this));
			},

			/**
			 * #前/后移
		   * 
			 *     oOrder._move(true);
			 *     oOrder._move(false);
			 * 
			 * @method _move
			 * @private
			 * @param isPrev {Boolean} true：前移；false：后移
			 */
			_move: function(isPrev) {
				var $items = this._items();
				var $selected = this._selectedItem($items);
				var $item2 = null;

				if ($selected.length > 1) {
					window.RiilAlert.info(window.T_SELECT_ONLY_ONE_FOR_MOVE || '!!T_SELECT_ONLY_ONE_FOR_MOVE!!');
					return;
				} else if (!$selected[0]) {
					return;
				}

				if (isPrev) $item2 = this._prev($selected, $items);
				else $item2 = this._next($selected, $items);

				this._exchange($selected, $item2);
			},

			_delete : function() {
				$(this._selectedItem()).remove();
			},

			/**
			 * 交换两个元素位置
			 * 
			 * 逻辑：A <-> B
			 * xxxAxxxBxxx -> xxxCAxxxBxxx -> xxxCxxxBAxxx -> xxxBCxxxAxxx -> xxxBxxxAxxx
			 * 
			 *     oOrder._exchange(itemA, itemB);
			 * 
			 * @method _exchange
			 * @private
			 * @param item1 {JQuery} 要交换位置的元素
			 * @param item2 {JQuery} 要交换位置的元素
			 */
			_exchange: function(item1, item2) {
				var $spanC = $('<span></span>');
				$(item1).before($spanC);
				$(item2).after(item1);
				$spanC.before(item2);
				$spanC.remove();
			},

			/**
			 * 获取指定元素前一个元素
			 * @method _prev
			 * @param $item {jQuery} 元素
			 * @param [$items] {jQuery} 所有元素
			 * @return {JQuery} 前一个元素
			 */
			_prev: function($item, $items) {
				$items = $items || this._items();
				var index = $items.index($item[0]) - 1;
				if (index < 0) return null;
				else return $items.eq(index);
			},

			/**
			 * 获取指定元素后一个元素
			 * @method _next
			 * @private
			 * @param $item {jQuery} 元素
			 * @param [$items] {jQuery} 所有元素
			 * @return {JQuery} 后一个元素
			 */
			_next: function($item, $items) {
				$items = $items || this._items();
				var index = $items.index($item[0]) + 1;
				if (index >= $items.length) return null;
				else return $items.eq(index);
			},

			/**
			 * 获取所有元素
			 * 
			 * 已知所有元素都有order属性，且他们的值一样
			 * @method _items
			 * @private
			 */
			_items: function() {
				var $scope = $('[order="' + this.groupId + '"]');
				return $('[orderItem="' + this.groupId + '"]', $scope);
			},

			/**
			 * 获取所有选择的元素
			 * 
			 * 被选择元素前有如下特征：
			 * 
			 * +  内有一个checkbox
			 * +  该checkbox都有order属性
			 * +  该属性值为"check"
			 * 
			 * @method _selectedItem
			 * @param [$items] {JQuery} 所有元素
			 * @return {JQuery} 被选择的元素
			 */
			_selectedItem: function($items) {
				$items = $items || this._items();
				var groupId = this.groupId;
				return $.grep($items, function(elem) {
					return !!$('input[orderCheck="' + groupId + '"]:checked', elem)[0];
				});
			}
		});


})();;/* global $,ConfigData */
(function() {
	'use strict';
	/**
	 * 翻页
	 *
	 *
 	 * 
	 * @class Paging
	 */
	window.Clazz('Paging',

		function(attrs) {
			ConfigData.check('Paging.constructor:参数attrs', attrs, {
				type: ['object'],
				elems: {
					$target: {type: ['object'], required:false},
					bindDomId: {type:['string'], required:false},
					listeners: {type:['object'], required:false, elems:{
						changePageSize : {type:['function'], required:false},
						changePage : {type:['function'], required:false}
					}},
					data: {type:['object'], array:true, required:false, elems:{
						pageIndex : {type:['number'], required:false},
						pageSize : {type:['number'], required:false},
						recordCount : {type:['number'], required:false},
						maxPage : {type:['number'], required:false}
					}},
					pageIndex : {type:['number'], required:false},
					pageSize : {type:['number'], required:false},
					recordCount : {type:['number'], required:false},
					maxPage : {type:['number'], required:false},
					changePageCount : {type:['number'], required:false}
				}});

			if ((!attrs.$target || !attrs.$target[0]) && 
				(!attrs.bindDomId || !$('#' + attrs.bindDomId)[0])) {
				throw new Error('Paging组件初始参数“attrs.$target”和“attrs.bindDomId”至少提供一个.');
			}

			this.conf = {};
			this.conf.bindDomId = 'paging';
			this.conf.listeners = attrs.listeners;
			this.$target = attrs.$target;

			window.GridPanel.plugins.paging.init.call(this, attrs);
		}, {
			startLoading : function() {

			}
		});


})();;/**分页显示panel
 * conf.pagingType  分页类型，支持"page"：按页翻，"item"：每次翻，只翻一个元素距离
 * conf.direction  分页方向，是垂直v，还是水平h
 */
var PanelPaging = {	
	init : function(conf){

		var $dom = $.getJQueryDom(conf.id);	
		var $up = $dom.children(":first");
		var $down = $dom.children(":last");
		var $content = $dom.children(":eq(1)");
		$content.attr("pagingType",conf.pagingType || "page").attr("direction",conf.direction || "v")

		$up.bind("click",{pos : 1, $content : $content}, this._paging);
		$down.bind("click",{pos : -1, $content : $content}, this._paging);

		if($content.children().height() <=$content.height()){
			$up.css("visibility","hidden");
			$down.css("visibility","hidden");
		}else{
			$up.css("visibility","hidden");
		}

	},
	/***/
	_paging:function(event){
		var pos = event.data.pos;
		var $content = event.data.$content;
		var pagingType = $content.attr("pagingType");		
		PanelPaging[pagingType]($content, pos);
	},
	page : function($content, pos){

		var direction = $content.attr("direction");
		var sizeStr = direction == "v" ? "height" : "width";
		var marginStr = direction == "v" ? "Top" : "Left";
		var parentSize = $content[sizeStr]();
		var $inner = $content.children();
		var size = $inner[sizeStr]();

		var margin = $inner.css("margin"+marginStr);		
		margin = margin == "" || margin == null ? 0 : parseInt(margin);
		var move = size - margin;
		move = move >= parentSize ? parentSize : move;

		margin = margin + move * pos;
		 
		if(margin == 0){
			$content.prev().css("visibility","hidden");
			$content.next().css("visibility","visible");	
		}
		if(Math.abs(margin) + parentSize > size){
			$content.next().css("visibility","hidden");
			$content.prev().css("visibility","visible");
		}

		var animateDire = {marginTop : margin};
		if(direction == "v"){
			animateDire.marginTop = margin;
		}

		$content.children().animate(animateDire);
		

	}

};/* global $,ConfigData,setTimeout */
(function() {
	'use strict';
	/**
	 * #list数据
	 *
	 * 根据数据
	 *
	 *      var oQueryData = new (window.QueryData)({
	 *     	$input : $('#filter'),
	 *     	data : [
	 *     		{attr1:'.当前', attr2: 'aaaaaa'},
	 *     		{attr1:'..上一级', attr2: '上一级'},
	 *     		{attr1:'alert文件夹', attr2: '文件夹'},
	 *     		{attr1:'calendar文件夹', attr2: '文件夹'},
	 *     		{attr1:'checkbox文件夹', attr2: '文件夹'},
	 *     		{attr1:'drag文件夹', attr2: '文件夹'},
	 *     		{attr1:'drag_tree文件夹', attr2: '文件夹'},
	 *     		{attr1:'fold_panel文件夹', attr2: '文件夹'},
	 *     		{attr1:'form文件夹', attr2: '文件夹'},
	 *     		{attr1:'grid文件夹', attr2: '文件夹'},
	 *     		{attr1:'icheckbox文件夹', attr2: '文件夹'},
	 *     		{attr1:'itembox文件夹', attr2: '文件夹'},
	 *     		{attr1:'layer文件夹', attr2: '文件夹'},
	 *     		{attr1:'left_navigation文件夹', attr2: '文件夹'},
	 *     		{attr1:'level_menu文件夹', attr2: '文件夹'},
	 *     		{attr1:'list_menu文件夹', attr2: '文件夹'},
	 *     		{attr1:'list_panel文件夹', attr2: '文件夹'},
	 *     		{attr1:'menu_panel文件夹', attr2: '文件夹'},
	 *     		{attr1:'Order文件夹', attr2: '文件夹'},
	 *     		{attr1:'panel文件夹', attr2: '文件夹'},
	 *     		{attr1:'query_list文件夹', attr2: '文件夹'},
	 *     		{attr1:'query_panel文件夹', attr2: '文件夹'},
	 *     		{attr1:'query_tree文件夹', attr2: '文件夹'},
	 *     		{attr1:'riilmask文件夹', attr2: '文件夹'},
	 *     		{attr1:'select文件夹', attr2: '文件夹'},
	 *     		{attr1:'slide文件夹', attr2: '文件夹'},
	 *     		{attr1:'tab文件夹', attr2: '文件夹'},
	 *     		{attr1:'text.json文件', attr2: '文件'},
	 *     		{attr1:'threshold文件夹', attr2: '文件夹'},
	 *     		{attr1:'time文件夹', attr2: '文件夹'},
	 *     		{attr1:'tree文件夹', attr2: '文件夹'},
	 *     		{attr1:'ullikeyselect文件夹', attr2: '文件夹'},
	 *     		{attr1:'window_button文件夹', attr2: '文件夹'}
	 *     	],
	 *     	show : 'attr1',
	 *     	filter : ['attr2'],
	 *     	onSelect : function(value) {
	 *     		alert(JSON.stringify(value));
	 *     	}
	 *     });
	 *
	 * @class QueryData
	 */
	window.Clazz('QueryData',

		function(attrs) {
			ConfigData.check('QueryData.constructor:参数attrs', attrs, {
				type: ['object'],
				elems: {
					$input: {
						type: ['object']
					},
					data: {
						type: ['object'],
						array: true
					},
					show: {
						type: ['string']
					},
					filter: {
						type: ['string'],
						array: true
					},
					onSelect: {
						type: ['function'],
						require: false
					}
				}
			});

			ConfigData.checkDomElem('QueryData.constructor:参数attrs.$input', attrs.$input);

			$.extend(this, attrs);

			this.$input = $(attrs.$input);


			this._$inputBlock = this._renderInput();
			this._event();
		}, {
			_event: function() {
				this.$input
					.keydown(function(event) {
						window.UlLiKeySelect.keyEvent(event);
					})
					.keyup($.proxy(function(event) {
						this._onInput(event);
					}, this))
					.click($.proxy(function(event) {
						this._onInput(event);
					}, this));

				this._$inputBlock.find('a.select_trigger_del').click($.proxy(function() {
					this._onClear();
				}, this));

				this._$inputBlock.find('span.icon_search2').click($.proxy(function() {
					this._onQuery();
				}, this));
			},

			_eventSwitchOn: function() {
				window.UlLiKeySelect.setTarget({
					target: this._$dom.children('ul:first'),
					scrollPanel: this._$dom,
					listeners: {
						onEsc: $.proxy(function() {
							this._hide();
						}, this),
						onChange: $.proxy(function($li) {
							$li.siblings().removeClass('on');
							$li.addClass('on');
						}, this),
						onSelect: $.proxy(function($li) {
							this._onSelect($li);
							this._hide();
						}, this)
					}
				}, true);

				this.$input.blur($.proxy(function() {
					setTimeout($.proxy(function() {
						this._hide();
					}, this), 200);
				}, this));
			},

			_eventSwitchOff: function() {
				window.UlLiKeySelect.dropTarget();
				if (this.$input) this.$input.unbind('blur');
			},

			_render: function(dataList) {
				this._$dom = this._renderList(dataList);
				this._$dom.appendTo('body');

				var offset = this._$inputBlock.offset();
				var width = this._$inputBlock.width();
				offset.top += this._$inputBlock.height();

				this._$dom.offset(offset).width(width);
				this._$dom.css('maxHeight', $(window).height() - offset.top - 50);
			},

			_renderInput: function() {
				var width = this.$input.outerWidth(true);
				var $e = $('<div class="select_body"></div>')
					.insertBefore(this.$input)
					.append('<a class="select_trigger_del"></a>')
					.append(this.$input);

				var $search = $('<span class="icon icon_search2"></span>').insertBefore($e);
				$search.css({
					float : 'right',
					marginTop : '3px'
				});

				$e.width(width - $search.outerWidth(true) - $e.widthSpacing());

				this.$input.width(width - this.$input.autoWidth([$search]));
				return $e;
			},

			_renderList: function(dataList) {
				var $block = null;
				var $ul = null;
				var $item = null;

				//创建备选项列表外层结构 [TODO]
				$block = $('<div comp="QueryData" style="position:absolute;left:0;top:0;display:block;z-index:' +
					window.ZIndexMgr.get() + ';overflow:auto;" class="select_boundtree"></div>');

				$ul = $('<ul></ul>').appendTo($block);

				//创建备选项列表内层数据结构 [TODO]
				$.each(dataList, $.proxy(function(index, data) {
					$item = this._renderItem(data);
					$ul.append($item);
				}, this));

				return $block;
			},

			_renderItem: function(itemData) {
				var filter = itemData[this.filter];
				var show = itemData[this.show];
				var $item = $('<li ' + this.filter + '="' + filter + '" title="' + show + '"><a>' + show + '</a></li>');
				$item.click($.proxy(function() {
					this._onSelect($item);
				}, this)).data('QueryData', itemData);

				return $item;
			},


			_hide: function() {
				this._eventSwitchOff();
				if (this._$dom) this._$dom.remove();
			},


			_show: function(dataList) {
				this._hide();
				if (0 === dataList.length) return;
				this._render(dataList);
				this._eventSwitchOn();
			},

			_getData: function(filterText) {
				return $.grep(this.data, $.proxy(function(data) {
					var c = this.filter.length;
					var attr = '';
					while (c--) {
						attr = this.filter[c];
						if (this._filter(data[attr], filterText)) return true;
					}
					return false;
				}, this));
			},

			_filter: function(data, pattern) {
				pattern = pattern.toLowerCase();
				var text = data.toLowerCase();
				return text.indexOf(pattern) >= 0;
			},

			_onSelect: function($li) {
				if (this.onSelect) this.onSelect($li.data('QueryData'));
			},

			_onInput: function(event) {
				if (27 === event.which) return;
				if (window.UlLiKeySelect.keyEvent(event, false)) return;
				this._onQuery();
			},

			_onQuery : function() {
				var text = this.$input.val();
				if (!text) this._hide();
				else {					
					if (this.query) {
						this.query(text, $.proxy(function(dataList){
							this._show(dataList || []);
						}, this));

					}else {
						this._show(this._getData(text) || []);
					}
				}
			},

			_onClear : function() {
				this._hide();
				this.$input.val('');
				if (this.onSelect) this.onSelect();
			}
		});


})();;/* global $,ConfigData */
(function(){'use strict';
/**
 * #list查询
 * 用于ul/li结构列表查询。
 * 执行查询时，隐藏不符合条件的li元素。
 * 执行清空时，显示所有li元素。
 * 
 * 举例：
 * 
 * ##html:
 *     <h1>待选指标
 *         <button id="btn"></button>
 *     </h1>
 *     <ul id="list">
 *         <li>数据1</li>
 *         <li>数据1</li>
 *         <li>数据1</li>
 *     </ul>
 * 
 * ##javascript:
 *     var oQueryList = new QueryList({$target : $('#list')});
 *     $('#btn').click(function(){
 *         oQueryList.toggle();
 *     })
 * 
 * @class QueryList
 */
window.Clazz('QueryList', 

/**
 * #构造函数
 * 
 *     var oQueryList = new QueryList({$target : $('#list')});
 * 
 * @constructor
 * @method QueryList
 * @param attrs {Json} 属性
 * @param attrs.$target {JQuery} 目标元素的jquery对象
 */
function(attrs) {
	ConfigData.check('QueryList.constructor:参数attr', attrs, {type:['object'], elems: {
		$target : {type:['object']}
	}});

	$.extend(this, attrs);
	
	this.$target = $(this.$target);
	this._render();
	this._event();
}, {
/**
 * #绘制HTML
 * @method _render
 * @private 
 */
_render : function() {
	this.$el = $('<div class="select_body" comp="QueryList">' +
				'<input class="select_text default">' +
				'<div class="select_trigger_wrap">' +
					'<a class="select_trigger_del"></a>' +
				'</div>' +
		'</div>').css({
			position: 'absolute'
		}).insertBefore(this.$target);

		this.$el.find('input.select_text').css({
			width : 'auto',
			display : 'block'
		});

		this.$el.hide();
},
/**
 * #绑定事件
 * @method _event
 * @private 
 */
_event : function() {
	$('.select_trigger_wrap .select_trigger_del', this.$el).click(
		$.proxy(function(){this.clear();}, this));
	$('input.select_text', this.$el).keyup(
		$.proxy(function(event){
			this._filter();
			// 响应上下按键操作
			// window.UlLiKeySelect.keyEvent(event);
		}, this));

},
/**
 * #事件开关
 * @method _eventSwitch
 * @private
 * @param on {Boolean} 事件是否有效
 */
_eventSwitch : function(on) {
	if (on) {
		this.$el.find('input.select_text').focus().blur($.proxy(function() {
			this.toggle();
		}, this));

		// 开启上下按键操作
		// window.UlLiKeySelect.setTarget({
		// 	target : this.$target,
		// 	scrollPanel : this.$scrollPanel,
		// 	listeners : {
		// 		onEsc : $.proxy(function(){
		// 			this.clear();
		// 			this.toggle();
		// 		}, this)
		// 	}
		// }, true);
	}else {
		this.$el.find('input.select_text').unbind('blur');
		
		// 关闭上下按键操作
		// window.UlLiKeySelect.dropTarget();
	}
},
/**
 * #切换搜索框显示/隐藏状态
 * 
 *     oQueryList.toggle();
 * 
 * @method toggle
 * @param [d] {Number} 动画执行时间
 */
toggle : function(d) {
	ConfigData.check('QueryList.toggle:参数d', d, {type:['number'], required:false});

	// 根据列表计算位置和宽度
	if (!this.visible()) {
		this.$el
			.show()
			.offset(this.$target.offset())
			.width(this.$target.width())
			.find('input.select_text')
				.width(this.$target.width()-30)
			.end()
			.hide();
	}else {
		this._eventSwitch(false);
		// 防止在关闭时重复执行
		if (this.toggling) return;
		this.toggling = true;
		setTimeout($.proxy(function() {
			this.toggling = false;
		}, this), 1000);
	}

	this.$el.slideToggle(d, $.proxy(function() {
		if (this.visible()) {
			this.targetPadding = this.$target.css('paddingTop');
			this.$target.css('paddingTop', this.$el.height() + 5);
			this._eventSwitch(true);
		}else{
			this.$target.css('paddingTop', this.targetPadding || 0);
		}
	}, this));
},
/**
 * #获取搜索框显示/隐藏状态
 * 
 *     var isVisible = oQueryList.visible();
 * 
 * @method visible
 * @return {Boolean} 是否显示
 */
visible : function() {
	return this.$el.is(':visible');
},
/**
 * #获取或修改搜索框坐标
 * 
 *     // 获取坐标
 *     var pos = oQueryList.position();
 *     // 修改坐标（左移10个像素）
 *     oQueryList.position(pos.left + 10, pos.top);
 * 
 * @method position
 * @param [left] {Number} x坐标
 * @param [top] {Number} y坐标
 * @return {Json} {left:number, top:number}
 */
position : function(left, top) {
	if (arguments.length !== 2) {
		return this.$el.offset();
	}else {
		ConfigData.check('QueryList.position:参数left', left, {type:['number']});
		ConfigData.check('QueryList.position:参数top', top, {type:['number']});

		this.$el.offset({
			left : left,
			top : top
		});
	}
},
/**
 * #获取或修改搜索框大小
 * 
 *     // 获取大小
 *     var size = oQueryList.size();
 *     // 修改大小（变宽10个像素）
 *     oQueryList.size(size.width + 10, size.height);
 * 
 * @method size
 * @param [width] {Number} x坐标，相当与`style.width = width + 'px'`效果
 * @param [height] {Number} y坐标，相当与`style.height = height + 'px'`效果
 * @return {Json} {width:number, height:number}
 */
size : function(width, height) {
	if (arguments.length !== 2) {
		return {
			width: this.$el.width(),
			height: this.$el.height()
		};
	}else {
		ConfigData.check('QueryList.size:参数width', width, {type:['number']});
		ConfigData.check('QueryList.size:参数height', height, {type:['number']});

		this.$el.width(width);
		this.$el.height(height);
	}
},
/**
 * #过滤li列表
 * 
 * 隐藏所有不符合条件的li
 * 
 * @method _filter
 * @private
 */
_filter : function() {
	var text = this.$el.find('input.select_text').val();
	this.clear(true);
	this.$target.children('li').each($.proxy(function(index, elem) {
		if (!this._filterText(text, $(elem).text())) {
			$(elem).attr('hideByQueryList', 'true').hide();
		}
	}, this));
},
/**
 * #判断文本是否符合条件
 * 
 * @method _filterText
 * @private
 * @param pattern {String} 预期内容
 * @param test {String} 实际内容
 */
_filterText : function(pattern, text) {
	if (text && text.toLowerCase().indexOf(pattern.toLowerCase()) >= 0) {
		return true;
	}else {
		return false;
	}
},
/**
 * #清空搜索框并还原li列表
 * 
 *     // 只还原li列表，不清空搜索框
 *     oQueryList.clear(true);
 *     // 只还原li列表，并清空搜索框
 *     oQueryList.clear();
 *     oQueryList.clear(false);
 * 
 * @method clear
 * @param [keepInput] {boolean} 是否保留搜索框内容
 */
clear : function(keepInput) {
	ConfigData.check('QueryList.clear:参数keepInput', keepInput, 
		{type:['boolean'], required:false});

	if (!keepInput) this.$el.find('input.select_text').val('');
	this.$target.children('[hideByQueryList="true"]')
		.removeAttr('hideByQueryList')
		.show();
}
});


})();;/* global $,ConfigData */
(function(){'use strict';

window.Clazz('ScrollButtons', 

function(attrs) {
	ConfigData.check('ScrollButtons.constructor:attr', attrs, {type:['object'], elems: {
		$target : {type:['object']},
		data : {type:['object'], array:true}
	}});

	$.extend(this, attrs);
	
	this.$target = $(this.$target);

	this._event(this._render());
}, {
	_render : function() {
		this.$el = this.$target
			.addClass('chartbtn')
			.append('<div><ul><li class="bg_none"><a ScrollButtons_action="up" class="up" style="margin-top:5px;margin-left:50px;"></a></li></ul></div>' +
          	'<div style="overflow:hidden;"><ul ScrollButtons="itemList"></ul></div>' +
          	'<div><ul><li class="bg_none"><a ScrollButtons_action="down" class="down" style="margin-top:5px;margin-left:50px;"></a></li></ul></div>');

        var $list = this.$el.find('ul[ScrollButtons="itemList"]');
		$.each(this.data, $.proxy(function(i, everyData) {
			$list.append(this._renderItem(
				everyData[this.textAttr || 'text'],
				everyData,
				everyData.on
				));
		}, this));

		$list.find('li[scrollbuttons="item"]:last').addClass('bg_none');

		this._renderFloat();

		var isScroll = this._resize();

		this.$el.find('*').attr('onselectstart', 'return false');

		return isScroll;
	},

	_renderItem : function(text, data, on) {
		var $item = $('<li ScrollButtons="item"><span></span></li>')
			.data('ScrollButtons', data || {});

		$item.attr('title', text).find('span').text(text);

		if (on) $item.removeClass('bg_none').addClass('on');

		return $item;
	},

	_renderFloat : function() {
		var $list = this.$el.find('ul[scrollbuttons="itemList"]');

		$('<li ScrollButtons_float="up" style="position:absolute" class="on"><span></span></li>')
			.appendTo($list)
			.css('visibility', 'hidden');

		$('<li ScrollButtons_float="down" style="position:absolute" class="on"><span></span></li>')
			.appendTo($list)
			.css('visibility', 'hidden');
	},

	_resize : function() {
		var $list = this.$el.find('ul[ScrollButtons="itemList"]');

		this._itemHeight = $list.innerHeight() / this.$el.find('li[scrollbuttons="item"]').length;
		var height = this.$el.innerHeight();
		var subHeight;
		var isScroll = true;

		if (height < $list.outerHeight(true)) {
			this.$el.find('a[ScrollButtons_action]').each(function(i, a) {
				height -= $(a).closest('div').outerHeight(true);
			});
			subHeight = height % this._itemHeight;
			height -= subHeight;
			$list.parent()
				.height(height)
				.css({
					marginTop : subHeight / 2,
					marginBottom : subHeight / 2
				});
		}else {
			this.$el.find('a[ScrollButtons_action]').closest('div').hide();
			isScroll = false;
		}

		this._maxScrollTop = $list.outerHeight(true) - height;

		this._resizeFloat();

		this._setItemSelected($list.find('li.on[scrollbuttons="item"]'));

		return isScroll;
	},

	_resizeFloat : function() {
		var $list = this.$el.find('ul[ScrollButtons="itemList"]');
		var $down = this.$el.find('li[ScrollButtons_float="down"]');
		var offset = $list.parent().offset();

		this.$el.find('li[ScrollButtons_float="up"]').offset(offset);

		offset.top += $list.parent().innerHeight() - $down.outerHeight(true);
		$down.offset(offset);
	},

	_event : function(isScroll) {
		this.$el.find('li[ScrollButtons="item"]')
			.click($.proxy(function(event){
				var $li = $(event.target).closest('li');
				this._onItemClick($li);
			}, this))
		.end()
		.find('a[ScrollButtons_action="up"]').closest('li')
			.click($.proxy(function() {
				this._onUp();
			}, this))
		.end().end()
		.find('a[ScrollButtons_action="down"]').closest('li')
			.click($.proxy(function() {
				this._onDown();
			}, this))
		.end().end();

		this._eventSwitch(isScroll);

		window.OnResize.addLayout($.proxy(function() {
			this._eventSwitch(this._resize());
		}, this));
	},

	_eventSwitch : function(on) {
		if (on) {
			this._addMouseScrollEvent(this.$el, $.proxy(function(step) {
				if (step > 0) this._onUp();
				else this._onDown();
			}, this));
		}else {
			this._removeMouseScollEvent(this.$el);
		}
	},

	_onItemClick : function($li) {
		this._setItemSelected($li);

		if ($.isFunction(this.click)) {
			var data = $li.data('ScrollButtons');
			this.click.call(this, data);
		}
	},

	_onUp : function() {
		this._scroll(-this._itemHeight);
	},

	_onDown : function() {
		this._scroll(this._itemHeight);
	},

	_scroll : function(iDistance) {
		var $listUl = $('ul[scrollbuttons="itemList"]');
		var value = $listUl.parent().stop(true, true).prop('scrollTop');
		var toValue = value + iDistance;
		if (toValue < 0) toValue = 0;
		else if (toValue > this._maxScrollTop) toValue = this._maxScrollTop;

		if (value !== toValue) {

			if (this._min <= toValue && toValue <= this._max) {
				// 隐藏
				$listUl.parent().animate({scrollTop : toValue}, 200, $.proxy(function() {
					this.$el.find('li[ScrollButtons_float]').css('visibility', 'hidden');
				}, this));
			}else {
				// 显示
				$listUl.parent().animate({scrollTop : toValue}, 200);
				this._showSelected(toValue);
			}

			this._showAction(toValue);
		}

	}, 

	_setItemSelected : function($li) {
		if (!$li || !$li[0]) return;

		this.$el.find('li.on[ScrollButtons="item"]').removeClass('on');
		this.$el.find('li[ScrollButtons="item"]:last').addClass('bg_none');
		$li.removeClass('bg_none').addClass('on');

		/*
		 * 假使备选元素可见，计算scrollTop的值的范围
		 *
		 *                |--------------[w2]-------------|
		 * |===================[w1]==================|[w3]|====================================|
		 *                :                          |--------------[w2]-------------|
		 *                :                          :
		 *            (最小值)                    (最大值)
		 * (最小值) = w1 + w3 - w2
		 * (最大值) = w1
		 */
		this._max = $li.offset().top - 
			this.$el.find('li[scrollbuttons="item"]:first').offset().top;
		this._min = this._max + $li.outerHeight(true) - 
			this.$el.find('ul[scrollbuttons="itemList"]').parent().innerHeight();

		this.$el.find('li[ScrollButtons_float]')
			.children('span')
				.text($li.text())
			.end()
			.css('visibility', 'hidden');

		this._showSelected();

		this._showAction();
	},

	_showSelected : function(scrollTop) {
		if (isNaN(scrollTop)) scrollTop = this.$el.find('ul[scrollbuttons="itemList"]').parent().prop('scrollTop');
		if (scrollTop > this._max) {
			this.$el.find('li[ScrollButtons_float="up"]').css('visibility', 'visible');
		}else if (this._min > scrollTop) {
			this.$el.find('li[ScrollButtons_float="down"]').css('visibility', 'visible');
		}
	},

	_showAction : function(scrollTop) {
		if (isNaN(scrollTop)) scrollTop = this.$el.find('ul[scrollbuttons="itemList"]').parent().prop('scrollTop');
		this.$el.find('a[ScrollButtons_action="up"]').closest('li').css('visibility', scrollTop <= 0 ? 'hidden' : 'visible');
		this.$el.find('a[ScrollButtons_action="down"]').closest('li').css('visibility', scrollTop >= this._maxScrollTop ? 'hidden' : 'visible');
	},

	_addMouseScrollEvent : function($obj, action){
		var isFirefox = typeof document.body.style.MozUserSelect != 'undefined'; 
		var eventName = isFirefox ? 'DOMMouseScroll' : 'mousewheel';
		$obj.bind(eventName, function(e) {
			var value = 0;
			if (e.wheelDelta) {
				value = e.wheelDelta/120; 
			}else {
				value = -( e.detail%3 === 0 ? e.detail/3 : e.detail );
			}
			action.call(this, value);
		});
	},

	_removeMouseScollEvent : function($obj) {
		var isFirefox = typeof document.body.style.MozUserSelect != 'undefined'; 
		var eventName = isFirefox ? 'DOMMouseScroll' : 'mousewheel';
		$obj.unbind(eventName);
	},

	selectByIndex : function(index, quiet) {
		var $li = this.$el.find('li[ScrollButtons="item"]:eq(' + index + ')');
		this._setItemSelected($li);

		if (!quiet && $.isFunction(this.click)) {
			var data = $li.data('ScrollButtons');
			this.click.call(this, data);
		}
	}
});


})();;var Struct = {};
/*
 * 复选框菜单选项
 * method checkItem
 * @param conf.checked
 * @param conf.disabled
 * @param conf.value
 * @param conf.text
 * @param conf.text
 * <li><a><input type="checkbox"><span></span></a></li>
 */

Struct.checkItem = function(conf){
	var random = new Date().getTime();
	 var input = $.createDomStr({
	                tagName : "input",
	                attr : {
	                          type : "checkbox",
	                          id : conf.id + random,
	                          disabled : conf.disabled,
	                          checked : conf.checked,
	                          value : conf.value
	                }
                });

    var text = $.createDomStr({
                    tagName : "span",
                    attr : {
                            label : conf.id + random,
                            title : conf.text
                    },
                    content : conf.text
                });

    var a = $.createDomStr({
                    tagName : "a",
                    content : input + text
                });

    var li = $.createDomStr({
                    tagName : "li",
                    id : conf.id,
                    style : {
                        "white-space" : "nowrap"
                    },
                    content : a
                });
    return li;
};/* jshint node:true */
(function(window) {
	'use strict';

	var refreshDate = new Date('Sep 9,2009');

	// 用于node测试
	if (!window) {
		window = module.exports;
		window.Clazz = require('./utils/Clazz.js').Clazz;
		window.classExtend = require('./utils/3_utils').classExtend;
		window.copyFields = require('./utils/3_utils').copyFields;
		window.TimeTask = require('./TimeTask').TimeTask;
		window.location = {};
		Object.defineProperty(window.location, 'href', {
			set : function(/*val*/) {
				if (window.onload) window.onload();
			},
			get : function() {
				return '';
			}
		});
		Object.defineProperty(window, 'refreshDate', {
			set : function(val) {
				refreshDate = val;
			},
			get : function() {
				return refreshDate;
			}
		});
	}

	window.TaskManager = window.Clazz('TaskManager', {
		INSTANCE : null,
		switchTask : null,
		addTimeTask : function(date, act, cycle) {
			return new (window.TaskManager)().addTimeTask(date, act, cycle);
		},
		remove : function(task) {
			new (window.TaskManager)().remove(task);
		},
		switchRefresh : function(on, cycle) {
			if (on) {
				if (window.TaskManager.switchTask) return;
				window.TaskManager.switchTask = new (window.TaskManager)()
					.addTimeTask(refreshDate, function() {
						window.location.href = window.location.href;
					}, cycle);
			}else {
				if (window.TaskManager.switchTask) {
					new (window.TaskManager)()
						.remove(window.TaskManager.switchTask);
					window.TaskManager.switchTask = undefined;
				}
			}
		}
	}, function(attrs) {
		if (window.TaskManager.INSTANCE) return window.TaskManager.INSTANCE;

		window.copyFields(this, attrs);

		this._taskMap = {};
	}, {
		addTimeTask : function(date, act, cycle) {
			var d = null;
			if (typeof date === 'string') d = new Date(date);
			else if (date instanceof Date) d = date;
			else if (typeof date === 'number') d = new Date(date);

			var task = new (window.TimeTask)({
				time : d,
				cycle : cycle,
				action : act
			});

			this.add(task);

			return task;
		},

		remove : function(task) {
			task = this.get(task);
			this.stop(task);
			delete this._taskMap[task.id];
		}, 

		add : function(task) {
			this._prepare(task);
			this._taskMap[task.id] = task;
		},

		_prepare : function(task) {
			var that = this;
			task.timeoutId = setTimeout(function() {
				task.run();
				that._prepare(task);
			}, task.next());
		},

		stop : function(task) {
			task = this.get(task);
			if (!task && isNaN(task.timeoutId)) return;
			clearTimeout(task.timeoutId);
			task.stop();
		},

		get : function(task) {
			if (typeof task === 'number') return this._taskMap[task];
			return task;
		}
	});
})(typeof window === 'undefined' ? undefined : window);;/* jshint jquery:true */

(function() {
	'use strict';

	$.fn.textEditor = function(arg1, arg2) {
		if (arg1 === 'text') {
			if (null !== arg2 && undefined !== arg2) {
				insertText.call(this, arg2);
			}else {
				return getText.call(this);
			}
		}else if (arg1 === 'html') {
			return getHtml.call(this);
		}else if (arg1 === 'uninstall') {
			uninstall.call(this);
		}else {
			install.call(this);
		}
	};

	$.textEditor = {
		toHtml : function(text) {
			return getHtml(text);
		}
	};

	function getText() {
		/* jshint validthis:true */
		return this.val();
	}

	function getHtml(text) {
		/* jshint validthis:true */
		return (undefined ===  text ? this.val() : text).replace(/(\$\{.+?\})/g, '<span class="rt">$1</span>');
	}

	function install() {
		/* jshint validthis:true */
		var data = {that : this};

		this.focus(data, beginPosCheck);
		this.blur(data, stopPosCheck);
		this.keydown(data, onKey);
	}

	function uninstall() {
		/* jshint validthis:true */
		this.unbind('focus', beginPosCheck);
		this.unbind('blur', stopPosCheck);
		this.unbind('keydown', onKey);
	}

	function insertText(replace) {
		/* jshint validthis:true */
		var node = this[0];
		var text = this.val();
		// 起始位置：默认0（最开始）
		var cursor = this.data('cursor') || CursorPosition.get(node);
		var start = cursor.start;
		var end = cursor.end;
		cursor.start = cursor.end = start + replace.length + 3;

		this.val(text.substring(0, start) + '${' + replace + '}' + text.substring(end));

		CursorPosition.set(node, cursor);

		this.data('cursor', cursor);
	}

	function rememberPos(event) {
		var that = event.data.that;
		var temp;
		var cursor = CursorPosition.get(that[0]);

		if (cursor.start > cursor.end) {
			temp = cursor.end;
			cursor.end = cursor.start;
			cursor.start = temp;
		}

		that.data('cursor', cursor);
	}

	function beginPosCheck(event) {
		var that = event.data.that;
		var interval = that.data('interval');

		if (interval) return;
		interval = setInterval(function() {
			rememberPos(event);
		}, 200);

		that.data('interval', interval);
	}

	function stopPosCheck(event) {
		var that = event.data.that;
		var interval = that.data('interval');

		if (interval) {
			clearInterval(interval);
			that.removeData('interval');
		}
	}

	function onKey(event) {
		if (event.which === 13) {
			event.preventDefault();
		}else {
		}
	}

	function getTextNode(node) {
		var children = node.childNodes;
		var i = children.length;
		while (i--) {
			if (children[i].nodeType === 3) return children[i];
		}
		return null;
	}

})();;/* jshint node:true */
(function(window) {
	'use strict';
	// 用于node测试
	if (!window) {
		window = module.exports;
		window.Clazz = require('./utils/Clazz.js').Clazz;
		window.copyFields = require('./utils/3_utils').copyFields;
		window.ConfigData = require('./config_data').ConfigData;
	}

	var __id = 0;
	function nextId() {
		return ++__id;
	}

	window.TimeTask = window.Clazz('TimeTask', {
		'1d' : 24 * 60 * 60 * 1000
	}, function(attrs) {
		window.ConfigData.check('TimeTask.constructor(attrs)', attrs, {
			type: ['object'],
			elems: {
				time: {type: ['object']},
				cycle: {type: ['number'], required:false},
				action: {type: ['function']}
			}
		});

		window.copyFields(this, attrs);

		if (!this.cycle) this.cycle = window.TimeTask['1d'];

		this.id = nextId();
	}, {
		id : null,

		next : function() {
			var time = this.time % this.cycle;
			var now = Date.now() % this.cycle;
			if (now <= time) {
				return time - now;
			}else {
				return time + this.cycle - now;
			}
		},

		run : function() {
			this.action();
		},

		stop : function() {

		}
	});
})(typeof window === 'undefined' ? undefined : window);;if (!window.RiilAlert) {
   
/**
 * 消息提示组件
 * @class RiilAlert
 * */
var RiilAlert = function(){
    
    if ($.getParentWindow() !== window && $.getParentWindow().RiilAlert) {
        return $.getParentWindow().RiilAlert;
    }

    //-----------------------------------------------------
    // 属性
    //-----------------------------------------------------
    
    /**
     * 组件ID
     * @property S_ID
     * @private
     * @final
     * @type {string}
     * @default "RiilAlert"
     */
    var S_ID = "RiilAlert";
    
    var $m_alert = null;
    
    //-----------------------------------------------------
    // 内部类
    //-----------------------------------------------------

    /**
     * 应用信息到某个区域居中显示类
     * 
     * */
    var ApplyMessage = function(){
        return {
            start : start_fn,
            stop : stop_fn
        };
        
        
        function start_fn (applyId, message, mini) {
            stop_fn (applyId);
            var $apply  = $("#"+applyId);
            var width = $apply.width();
            var height = $apply.height();
    //      var $shade = $('<div id="shadeDisable" class="shade-disable"></div>');
            var $nodata = $('<div nodata="nodata" class="nodata'+(mini? "-min":"")+'">'+message+'</div>');
            $apply.append($nodata);
            //$nodata.css({marginTop:$append.height()/2-$nodata.height()/2,marginLeft:$append.width()/2-$nodata.width()/2});
            $nodata.css({position:"absolute",top:$apply.height()/2-$nodata.height()/2,left:$apply.width()/2-$nodata.width()/2});
            $apply.css("position","relative").append($shade);
        }
        
        
        function stop_fn (applyId) {
        //  $("#shadeDisable").remove();
            var $apply  = $("#"+applyId).css("position","static");
            $apply.find('[nodata]').remove();
        }
    }();
    
    //-----------------------------------------------------
    // API
    //-----------------------------------------------------
    return {
        
        /**
         * 消息提示，在页面底部copyright左侧展示，不需要用户确认
         * 
         *      RiilAlert.message("保存成功");
         *      
         * @method message
         * @param text {string} 显示文本
         * */
        message : message_fn,
        

        /**
         * 需要用户点击确认提示
         * 
         *      RiilAlert.info("请输入用户名");
         *      
         * @method info
         * @async
         * @param text {string} 显示文本
         * @param successCallBack {function} 点击确定按钮
         * */
        info : info_fn,
        
        /**
         * 需要用户点击确认提示错误
         * 
         *      RiilAlert.error("系统错误"); 
         *      
         * @method error
         * @async
         * @param text {String} 显示文本
         * @param successCallBack 点击确定按钮
         * */ 
        error : error_fn,
        
        /**
         * 询问用户确定或取消
         * 
         *      RiilAlert.confirm("是否删除",function(){
         *          //删除代码
         *      });
         *      RiilAlert.confirm("是否删除",function(){
         *          //删除代码
         *      },function(){
         *          //点击取消按钮事件
         *      });
         *      
         * @method confirm
         * @async
         * @param text {String} 显示文本
         * @param successCallback {function} 确定按钮回调
         * @param [cancelCallBack] {function} 取消按钮回调
         * */
        confirm : confirm_fn,
        
        /**
         * 页面顶部悬浮提示
         * 
         *      RiilAlert.toast("保存成功");
         *      
         * @method toast
         * @param text {string} 显示文本
         * @param [cls] {string} 文字的样式类名列表
         * */
        toast : toast_fn,
        
        
        applyMessage : {
            
            /**
             * 手动开始显示
             * 
             *      RiilAlert.applyMessage.start("domId","当前没有数据",true);
             * @method start
             * @param applyId {string} 制定消息显示的位置元素ID
             * @param message {string} 显示的消息文本
             * @param [mini] {boolean} 显示的消息是否为迷你样式
             * */
            start : ApplyMessage.start_fn,
            
            
            /**
             * 手动停止信息显示
             * 
             *      RiilAlert.applyMessage.stopt("domId"); 
             * @method stop
             * @param applyId {string} 消息应用到的元素ID
             * */
            stop : ApplyMessage.stop_fn
        }
    };


    //-----------------------------------------------------
    // 公有函数
    //-----------------------------------------------------
    
    function message_fn (text) {
        var info = parent.document.getElementById("RiilAlert_info");
        if(info){
            parent.document.getElementById("RiilAlert_info").innerHTML = text;
            setTimeout(RiilAlert._clearMessage,2000);
        }
    }
    

    function info_fn (text, width, successCallBack) {
        
        if ($m_alert) return;
        
        if (arguments.length < 3) {
            successCallBack = width;
            width = undefined;
        }
        
        if(getParent() != window && getParent().RiilAlert){
            getParent().RiilAlert.info(text, successCallBack);
            return;
        }
        
        _show_fn ({
            text:text,
            title:window.S_INFO || '!!S_INFO!!', //提示
            width: width,
            className:"pop-show-info",
            ok:true,
            alertType:"info",
            listeners: {
                ok : successCallBack
            }
        });
    }
    

    function error_fn (text, width, successCallBack) {
        
        if ($m_alert) return;

        if (arguments.length < 3) {
            successCallBack = width;
            width = undefined;
        }
        
        if(getParent() != window && getParent().RiilAlert){
            getParent().RiilAlert.error(text,successCallBack);
            return;
        }
        
        _show_fn ({
            text:text,
            title:window.S_INFO || '!!S_INFO!!', //提示
            className:"pop-show-alert",
            width: width,
            ok:true,
            alertType:"error",
            listeners: {
                ok : successCallBack
            }
        });
    }
    

    function confirm_fn (text, successCallBack, cancelCallBack, width, isDoubleLine) {
        
        if ($m_alert) return;

//        if (arguments.length < 4) {
//            cancelCallBack = successCallBack;
//            successCallBack = width;
//            width = undefined;
//        }
        
        if(getParent() != window && getParent().RiilAlert){
            getParent().RiilAlert.confirm(text, successCallBack, cancelCallBack, width, isDoubleLine);
            return;
        }
        
        
        _show_fn ({
            text:text,
            title:window.S_INFO || '!!S_INFO!!', //提示
            className:" pop-show-help" + (isDoubleLine ? ' double' : ''),
            width: width,
            ok:true,
            cancel:true,
            alertType:"confirm",
            listeners: {
                ok : successCallBack,
                cancel : cancelCallBack
            }
        });     
    
    }
    
    
    function toast_fn (text, cls) {
        if (!cls) cls = 'ico ico_confirm';
        else if (cls === 'warn') cls = 'ico ico_warn';
        
        var top = $("#header").height() - 50;
        var $toastMsg = $('<div class="toast-l" style="top:'+ top +'px;z-index:' + ZIndexMgr.get() + '"><div class="toast-r"><div class="toast-m"><span class="'+ (cls || "") +'"></span>' + text + '</div></div></div>');
        $toastMsg.animate({
            opacity : 0
        },0);
        $(document.body).append($toastMsg);       
        var clientWidth = document.body.clientWidth;
        var scrollLeft = document.body.scrollLeft;  
        $toastMsg.css("left",scrollLeft + clientWidth/2 - $toastMsg.width()/2);               
        $toastMsg.animate({top:top + 55,opacity:1}, "fast",function(){
            setTimeout(function(){
                ZIndexMgr.get($toastMsg);
                $toastMsg.animate({
                    top : top,
                    opacity : 0
                },"fast",function(){
                    $toastMsg.remove(); 
                });             
            },2000);        
        });
    }
    

    //-----------------------------------------------------
    // 私有函数
    //-----------------------------------------------------
    function _clearMessage_fn () {
        parent.document.getElementById("RiilAlert_info").innerHTML="";
    }

    
    function _show_fn (conf) {
        var $alert = $(_createAlertPanel_fn(conf));
        var width  = $alert.width();
        var height = $alert.height();
        var scrollTop = document.body.scrollTop;
        var scrollLeft = document.body.scrollLeft;
        var offsetHeight = document.body.clientHeight;
        var offsetWidth = document.body.clientWidth;
        var actionEvent = {};
        var listeners = conf.listeners;
        
        $alert.css({height:1,width:1,left:scrollLeft+offsetWidth/2,top:scrollTop + offsetHeight / 2});
        $alert.animate({top:scrollTop + offsetHeight / 2 - height/2,left:scrollLeft+offsetWidth/2-width/2,width:width,height:height},"fast");
        
        if (conf.listeners) {
            actionEvent.onOk_fn = listeners.ok;
            actionEvent.onCancel_fn = conf.cancel ? listeners.cancel : listeners.ok;
        }
        actionEvent.info = conf.listeners;
        
        $(document).bind("keydown", actionEvent, _keydown_fn);
        setTimeout(function() {
            $alert.find('.button>a:first').focus().blur();
        }, 200);
    }
    
    
    function _keydown_fn (event) {
        var which = event.which,
            data = event.data;
        
        if (which !== 13 && which !== 27) return;
        
        event.preventDefault();
        
        if(which==13){
            _close_fn(data.onOk_fn);
        }else if(which==27){
            _close_fn(data.onCancel_fn);
        }
    }
    
    
    function _createButton_fn (onclick, className, text){
        var button = [];
        button.push('<a style="width:71px" class="btn_body" onclick="'+onclick+'"><div class="btn_l"><div  class="btn_r"><div class="btn_m">');
        button.push('<span class="ico '+className+'"></span>');
        button.push('<span class="text">'+text+'</span>');
        button.push('<span class="pull"></span></div></div></div></a>');
        return button.join('');
    }
    
// 旧实现：留做参考
//---------------------------------------------------------
//    function _createAlertPanel_fn_old (conf) {
//       var panel = [];
//       panel.push('<div class="pop-blue" style="width:500px;left:0px;top:0px;" id="'+S_ID+'">');
//       panel.push('<div class="pop-top-l"><div class="pop-top-r"><div class="pop-top-m">');
//       
//       if(conf.alertType == "confirm"){
//           panel.push('<a class="pop-ico pop-ico-quit" onclick="RiilAlert._close_fn();"></a>');
//       }else{
//           var ok = "'ok'"
//           panel.push('<a class="pop-ico pop-ico-quit" onclick="RiilAlert._close_fn('+ok+');"></a>');
//       }
//       
//       panel.push('<span class="pop-title">'+conf.title+'</span>');
//       panel.push('</div></div></div>');   
//       panel.push('<div class="pop-middle-l"><div class="pop-middle-r"><div class="pop-middle-m">');
//       panel.push('<div class="pop-show double '+conf.className+'">'+conf.text+'</div>');          
//       
//       panel.push('<div class="pop-btngroup-bottom">');          
//       
//       if(conf.cancel){
//           panel.push(_createButton_fn("RiilAlert._close_fn('cancel');","ico_cancel",window.S_BTNCANCEL));
//       }
//       if(conf.ok){
//           panel.push(_createButton_fn("RiilAlert._close_fn('ok');","ico_confirm",window.S_BTNOK));
//       }             
//       panel.push('</div></div></div></div><div class="pop-bottom-l"><div class="pop-bottom-r"><div class="pop-bottom-m"></div></div></div></div>');                       
//       return panel.join("");
//    }
    
    function _createAlertPanel_fn (conf) {
        
        var id = _createAlertPanel_fn.nextId || 1,
            panelConf,
            buttons = [],
            listeners = conf.listeners;
            
        _createAlertPanel_fn.nextId = id + 1;
        
        panelConf = {
            id: S_ID,
            width: conf.width || 550,
            scroll: false,
            style : 'alert',
            content: '<div class="pop-show ' + conf.className + ' alert_text">' + conf.text + '</div>',
            title: conf.title,
            closeBack: function() {
                _close_fn(conf.cancel ? listeners.cancel : listeners.ok);
                return true;
            }
        };

        if(conf.ok){
            buttons.push({
                type: 'ok',
                click: function () {
                    _close_fn(listeners.ok);
                }
            });
        }
        if(conf.cancel){
            buttons.push({
               type: 'cancel',
               click: function () {
                   _close_fn(listeners.cancel);
               }
            });
        }
        
        panelConf.buttons = buttons;
        
        return $m_alert = Panel.htmlShow(panelConf);
    }
    
    
    function _createShaw_fn () {
        return '<div class="pop-white-shade" id="alert_shaw" style="z-index:1000;width:100%;height:100%"></div>';
    }
    

    function _close_fn (onEvent) {
        if (!$m_alert) return;
        $m_alert.animate({top:$(window).height() / 2,left:$(window).width()/2,opacity: 0.25,width:0,height:0},"fast",function(){
            Panel.close(S_ID);
            if ($.isFunction(onEvent)) {
                onEvent();
            }
        });
        
        $m_alert = null;
        $(document).unbind("keydown", _keydown_fn);
    }

}();


};/**
 * @class AutoComplete
 * 自动完成组件
 */
var AutoComplete = {
		suffix:"_autoComplete",
		hide:"_hidden",
		/**
		 * @example
		 * AutoComplete.init({
		 * 		id:"complate",
		 * 		url:"http://127.0.0.1:9090/riil.portal.web/common/temp.jsp",
		 * 		param:"param",
		 * 		render:function(data){
		 * 			var returnHTML = [];		
		 * 			for(var i=0;i<data.length;i++){
		 * 				returnHTML.push("<li val='"+data[i].value+"'><a>"+data[i].text+"</a></li>");
		 * 			}
		 * 			return returnHTML.join("");
		 * 		},
		 * 		selectAfter:function($item){
		 * 			return {value:$item.attr("val"),text:$item.children("a").html()};
		 * 		}
		 * });
		 * 
		 * @param conf
		 * <ul>
		 * 	<li>配置参数：</li>
		 * 	<li>id:"组件id"</li>
		 * 	<li>url:"请求数据url"</li>
		 * 	<li>param:请求附带的参数</li>
		 * 	<li>render:请求回来后渲染回调函数，返回选项html</li>
		 * 	<li>selectAfter:选择某一选项后会回调函数，该函数返回选择后的{text:"",value:""}</li>
		 * </ul>
		 * 
		 * */
		init:function(conf){
			var $input = AutoComplete._getInput(conf.id);
			if(!$input[0]){
				$input = $('input[name="'+conf.id+'"]');
				$input.attr("id",conf.id);
			}
			
			$input.after('<input type="hidden" id="'+conf.id+AutoComplete.hide+'" name="'+$input.attr("name")+'"/>').removeAttr("name");
			this._initShowContent(conf);
			this._bindEvent(conf);
		},
		/*
		 * @inner
		 * 内部绑定事件
		 * */
		_bindEvent:function(conf){
			var $input = AutoComplete._getInput(conf.id);
			var lastTime = 0;
			$input.bind("keypress",function(event){
				var now = new Date().getTime();
				var interval = now -lastTime;
				if(interval > 150){				
					lastTime = now;
					$.ajax({
						url:conf.url,
						data:conf.param+"="+$input.val(),
						dataType:"json",
						success:function(data){
							var listHtml = "";
							if(conf.render){
								listHtml  = conf.render(data);
							}else{
								listHtml = AutoComplete.render(data);
							}
							AutoComplete._bindItemEvent(conf,listHtml);
						}
					});		
				}
			});
		},
		/*
		 * @inner
		 * 调整内容位置
		 * */
		_justContent:function(id,$content){
			var $input = AutoComplete._getInput(id);
			var layout = $.getElementAbsolutePosition($input[0]);
			$content.css({left:layout.x,top:layout.y+$input.height()}).show();
			
		},
		/**
		 * 获得组件的值
		 * @param id {string} 组件ID
		 * @returns {string}
		 * */
		getValue:function(id){
			return AutoComplete._getHidden(id).val();
		},
		/**
		 * 获得组件的显示文本
		 * @param id {string} 组件ID
		 * @returns {string}
		 * */		
		getText:function(id){
			return AutoComplete._getInput(id).val();
		},
		/*
		 * @inner
		 * 获得隐藏于元素
		 * */		
		_getHidden:function(id){
			return $("#"+id+AutoComplete.hide);
		},
		/*
		 * @inner
		 * 绑定下拉选项点击事件
		 * */
		_bindItemEvent:function(conf,content){
			if(content && $content!=""){
				var $content = AutoComplete._getContent(conf.id);
				AutoComplete._justContent(conf.id,$content);
				$content.children("ul").find("*").unbind();			
				$content.children("ul").html(content).children("li").bind("click",function(){
					var text_value  = conf.selectAfter($(this));
					AutoComplete._getHidden(conf.id).val(text_value.value);
					AutoComplete._getInput(conf.id).val(text_value.text);
					$content.hide();
				});	

				// 增加焦点离开自动隐藏功能 {{{
				function hide(event) {
					if (!$.contains($content[0], event.target) &&
						event.target !== AutoComplete._getInput(conf.id)[0]) {
						$content.hide();
						$(document.body).unbind('click', hide);
					}
				}
				$(document.body).click(hide);
				// }}}
			}

		},
		/*
		 * @inner
		 * 获得输入框对象
		 */
		_getInput:function(id){
			return $("#"+id);
		},
		/*
		 * @inner
		 * 获得下拉选择区域对象
		 */
		_getContent:function(id){
			return $("#"+id+AutoComplete.suffix);
		},
		/*
		 * @inner
		 * 初始化下拉框显示区域，type类型，确定是下拉树，还是一般的下拉框
		 * */
		_initShowContent : function(conf){
			var $completeDiv = $('<div class="select_boundtree" id="'+conf.id+AutoComplete.suffix+'"><ul></ul></div>');
			$(document.body).append($completeDiv);
			
		}
	};var RiilAi = {
	data : [],
	startTime : new Date().getTime(),
	init : function(){
		return;
		if(!jQuery.aop){
			return;
		}
		/*jQuery.aop.around( {target: window.PageCtrl, method: 'load'},
			  function(invocation, method) {
			  	invocation.proceed();
			  	//console.info(invocation.arguments[0].dom);
			  	setTimeout(function(){
			  		RiilAi.initEvent(invocation.arguments[0].dom);
			  	}, 1000);
			  }
		);*/
		jQuery.aop.around( {target: $, method: 'append'},
			  function(invocation, method) {
			  	setTimeout(function(){
			  		RiilAi.initEvent();
			  	}, 1000);
			  	return  invocation.proceed();
			  	//console.info(invocation.arguments[0].dom);
			  	
			  }
		);
		jQuery.aop.around( {target: $, method: 'html'},
			  function(invocation, method) {
			  	setTimeout(function(){
			  		RiilAi.initEvent();
			  	}, 1000);
			  	return invocation.proceed();
			  	//console.info(invocation.arguments[0].dom);
			  	
			  }
		);

		jQuery.aop.around( {target: PageCtrl, method: 'ajax'},
			  function(invocation, method) {
			  RiilAi.put({
			  		sessionId:$.getCookie("JSESSIONID"), 
					time:new Date().getTime(), 
					url : location.href,
					ajaxUrl : invocation.arguments[0].url,
					referrer : document.referrer
				});



			  	return  invocation.proceed();
			  	//console.info(invocation.arguments[0].dom);
			  	
			  }
		);

		RiilAi.unload();
		setTimeout(function(){
			RiilAi.initEvent();
		},2000);
	},
	getDomByTag : function(tagName, dom){
		var quickExpr = /^(?:[^#<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/;
		var $dom = null;
		if(dom){
			if(typeof(dom) == "string"){
				var match = quickExpr.exec( dom );
				if ( match && match[1]) {
					$dom = $(dom);
				}
			}
		}

		if($dom){
			return $dom.find(tagName);
		}else{
			return $(tagName);
		}
	},
	initEvent : function(){
		if(RiilAi.onceStop === true){
			//RiilAi.onceStop = false;
			return;			
		}
		var tagName = ["layerMenu","subsetul","btnbody","icon", "ico", "navAreabginner", "tbodypart"];

		for(var i = 0; i < tagName.length; i++){
			this.appendEvent(this.initType[tagName[i]]());
		}
	},
	initType : {
		layerMenu : function(){
			return $("ul.layer-menu>li");
		},
		subsetul : function(){
			return $("ul.subsetul>li");
		},
		btnbody : function(){
			return $('a.btn_body');
		},
		icon : function(){
			return $("a.icon");
		},
		ico : function(){
			return $("a.ico");
		},
		navAreabginner : function(){
			return $('ul.navAreabginner li');
		},
		tbodypart:function(){
			return $('div.tbodypart a[class=""]');
		}
	},
	appendEvent : function(doms){
		for(var i = 0, len = doms.length; i < len; i++){
			RiilAi.bindEvent(doms[i]);
		}
	},
	bindEvent : function(dom){
		var $dom = $(dom);
		if($dom.attr('aiInit')){
			return;
		}
		var event = $._data(dom, "events");
		if(event){
			var click = event.click;
			if(click && click.length > 0){
				var handler = click[0].handler;
				//console.info(handler)
				if(handler){
					$dom.attr('aiInit', 'true');
					event.click[0].handler = function(event){
						handler.call(this,event);
						RiilAi.getOprationData(this);
					}
				}
			}
		}else if(dom.onclick){
			$dom.attr('aiInit', 'true');
			$dom.click(function(event){
				RiilAi.getOprationData(this);
			});
		}
	},
	getOprationData : function(dom){
		var $dom = $(dom);
		var label = "";
		if($dom.children().length > 0){
			label = $dom.children().text();
		}else{
			label = $dom.text();
		}
		label = label.replace(/[\n]/ig,'');
		label = label.replace(/[\n]/ig,'');
		label = $.trim(label);
		RiilAi.put({sessionId:$.getCookie("JSESSIONID"), 
					domId:$dom.attr("id"),
					name:$dom.attr("name"), 
					title:$dom.attr("title"),
					time:new Date().getTime(), 
					label : label,
					url : location.href,
					referrer : document.referrer
		});
	},
	put : function(data){
		RiilAi.data.push(data);
		if(RiilAi.data.length == 10){
			RiilAi.submit(RiilAi.data, true);
			RiilAi.data = [];
		}
	},
	submit : function(data, async){
		$.ajax({
			url : ctx + '/collector/collData',
			type : 'post',
			dataType : 'json',
			async : async,
			data : {data : JSON.stringify(data)}
		});
	},
	unload : function(){
		$(window).bind("unload", function(){
			if(RiilAi.data.length > 0){
				RiilAi.submit(RiilAi.data, false);
			}
			var nowTime = new Date().getTime();
			RiilAi.submit([{
				sessionId:$.getCookie("JSESSIONID"), 
				url : location.href,
				pageTitle : document.title,
				time : nowTime,
				stayTime :  nowTime- RiilAi.startTime
			}], false);
		})
	}
}

$(function(){
	
		RiilAi.init();
	
	
})
/**
 *
 * a=$("#a-1020000000446531 .post-big-vote");
$._data(a[0],"events").click[0].handler.call(a);
 */;/**
 * 手动伸拉扩展层
 * @class Broaden
 * */
var Broaden = {
	items:{},//用于存放需要伸拉的元素
	$body:null,
	currentId:null,//当前操作的元素ID
	LEFT_TOP:"left_top",
	LEFT_BOTTOM:"left_bottom",
	RIGHT_TOP:"right_top",
	RIGHT_BOTTOM:"right_bottom",
	LEFT:"left",
	RIGHT:"right",
	TOP:"top",
	BOTTOM:"bottom",
	BROADEN_POSITION:"broadenPosition",
	handlerRange : 20,//显示拖动样式的句柄宽度或高度
	
	/**
	 * 初始化
	 * @method init
	 * @param conf {object} 配置信息
	 */
	init:function(conf){
		this.items[conf.id]= {id:conf.id,innerId:conf.innerId || conf.id};
		$("#"+conf.id).find("*").addClass("ban-drag");
		this.$body =  this.$body || $(document.body);
		if(!this.$body.attr("isBroaden")){
			this._bind(conf);
			this.$body.attr("isBroaden","true");
		}
		if(!this.POSITIONS){
			this.POSITIONS = [this.LEFT_TOP,this.LEFT_BOTTOM,this.RIGHT_TOP,this.RIGHT_BOTTOM,this.LEFT,this.RIGHT,this.TOP,this.BOTTOM];
		}
	},	
	/**
	 * 鼠标移动事件
	 * */	
	_mouseMoveListener:function(event){
		var x = event.pageX,y = event.pageY;
		var conf = event.data.conf;
		var offset = null,position = null,left = null,top = null,width = null,height = null,innerHeight = null;
		var lastBodyMoveX = null,lastBodyMoveY = null;
		var cutX = null,cutY = null;
		var $layer = null;
		var $innerLayer = null;
		for(var id in Broaden.items){
			$layer = $("#"+id);
			if(!$layer[0]){
				delete Broaden.items[id];
				continue;
			}
			$innerLayer = $("#"+Broaden.items[id].innerId);
			position = $layer.position();
			offset = $layer.offset();
			left = position.left;
			top = position.top;
			width = $layer.width();
			height = $layer.height();
			innerHeight = $innerLayer.height();
			lastBodyMoveX = parseInt($layer.attr("lastBodyMoveX"));
			lastBodyMoveY = parseInt($layer.attr("lastBodyMoveY"));
			cutX = x - lastBodyMoveX;
			cutY = y - lastBodyMoveY;
			var isBroaden = $layer.attr("isBroaden");
			var broadenPosition = $layer.attr("broadenPosition");			
			Broaden._checkBroaden(x,y,left,top,offset,broadenPosition,width,height,innerHeight,$layer,$innerLayer,cutX,cutY,isBroaden,conf.customPosition);
			return false;
		}
	},
	/*
	 * 根据坐标检查是否扩展方向 
	 * */
	_checkBroaden:function(x,y,left,top,offset,broadenPosition,width,height,innerHeight,$layer,$innerLayer,cutX,cutY,isBroaden,customPosition){
		var checkResult = null;
		if($.isString(customPosition)){
			customPosition = Broaden.POSITIONS;
		}
		for(var i=0,len = customPosition.length;i<len; i++){
			checkResult = Broaden.checks[customPosition[i]](x,y,width,height,offset,broadenPosition);
			if(checkResult === true){
				Broaden.currentId = $layer.attr("id");
				if(isBroaden == "true"){
					var conf = {
						width : width,
						innerHeight:innerHeight,
						left:left,
						top:top,
						cutX:cutX,
						cutY:cutY,
						$layer:$layer
					};
					Broaden.broadenSize[customPosition[i]](conf);
		        	$layer.css({
		            	left:conf.left,
		            	top:conf.top,
		            	width:conf.width
		            }).attr("lastBodyMoveX",x).attr("lastBodyMoveY",y);
		        	$innerLayer.height(conf.innerHeight);
				}
				break;
			}
		}
		if(!checkResult){
        	$(document.body).css("cursor","auto");
        	$layer.removeAttr(Broaden.BROADEN_POSITION);
		}
	},
	/**
	 * 伸缩尺寸
	 * */
	broadenSize:{
		"left_top":function(conf){
			conf.left = conf.left + conf.cutX;
    		conf.width = conf.width - conf.cutX;        		
    		conf.top = conf.top + conf.cutY;
    		conf.innerHeight = conf.innerHeight - conf.cutY;
    		conf.$layer.attr(Broaden.BROADEN_POSITION,Broaden.LEFT_TOP);
		},
		"left_bottom":function(conf){
        	conf.width = conf.width - conf.cutX;    
        	conf.left = conf.left + conf.cutX;
        	conf.innerHeight = conf.innerHeight + conf.cutY;
        	conf.$layer.attr(Broaden.BROADEN_POSITION,Broaden.LEFT_BOTTOM);
		},
		"right_top":function(conf){
    		conf.width = conf.width + conf.cutX;
    		conf.top = conf.top + conf.cutY;
    		conf.innerHeight = conf.innerHeight - conf.cutY;
    		conf.$layer.attr(Broaden.BROADEN_POSITION,Broaden.RIGHT_TOP);
		},
		"right_bottom":function(conf){
			conf.width = conf.width + conf.cutX;
			conf.innerHeight = conf.innerHeight + conf.cutY;
			conf.$layer.attr(Broaden.BROADEN_POSITION,Broaden.RIGHT_BOTTOM);     
		},
		"left":function(conf){
    		conf.left = conf.left + conf.cutX;
    		conf.width = conf.width - conf.cutX;
    		conf.$layer.attr(Broaden.BROADEN_POSITION,Broaden.LEFT);
		},
		"right":function(conf){
    		conf.width = conf.width + conf.cutX;
    		conf.$layer.attr(Broaden.BROADEN_POSITION,Broaden.RIGHT);
		},
		"top":function(conf){
			conf.top = conf.top + conf.cutY;
			conf.innerHeight = conf.innerHeight - conf.cutY;
			conf.$layer.attr(Broaden.BROADEN_POSITION,Broaden.TOP);
		},
		"bottom":function(conf){
    		conf.innerHeight = conf.innerHeight + conf.cutY;
    		conf.$layer.attr(Broaden.BROADEN_POSITION,Broaden.BOTTOM);
		}		
	},
	/**
	 * 验证扩张方位（左上、左下、右上、右下、左、右、上、下）
	 * */
	checks:{
		"left_top":function(x,y,width,height,offset,broadenPosition){
			if( x >= offset.left &&  x < offset.left + Broaden.handlerRange && y > offset.top && y < offset.top + Broaden.handlerRange || broadenPosition == Broaden.LEFT_TOP){
				Broaden.$body.css("cursor","nw-resize");
				return true;
			}
		},
		"left_bottom":function(x,y,width,height,offset,broadenPosition){
			if(x <= offset.left + Broaden.handlerRange  &&  x > offset.left && y <= offset.top + height && y > offset.top + height - Broaden.handlerRange || broadenPosition == Broaden.LEFT_BOTTOM){
				Broaden.$body.css("cursor","sw-resize");
				return true;
			}
		},
		"right_top":function(x,y,width,height,offset,broadenPosition){
			if(x <= offset.left + width && x > offset.left + width - Broaden.handlerRange  && y >= offset.top  && y < offset.top + Broaden.handlerRange || broadenPosition == Broaden.RIGHT_TOP){
				Broaden.$body.css("cursor","ne-resize");   
				return true;
			}
		},
		"right_bottom":function(x,y,width,height,offset,broadenPosition){
			if(x <= offset.left + width && x > offset.left + width - Broaden.handlerRange && y <= offset.top + height && y > offset.top + height - Broaden.handlerRange || broadenPosition == Broaden.RIGHT_BOTTOM){
				Broaden.$body.css("cursor","se-resize");   
				return true;
			}
		},
		"left":function(x,y,width,height,offset,broadenPosition){
			if( x >= offset.left && x < offset.left + Broaden.handlerRange || broadenPosition == Broaden.LEFT){
				Broaden.$body.css("cursor","e-resize");
				return true;
			}
		},
		"right":function(x,y,width,height,offset,broadenPosition){
			if(x <= offset.left + width && x > offset.left + width - Broaden.handlerRange  || broadenPosition == Broaden.RIGHT){
				Broaden.$body.css("cursor","e-resize");
				return true;
			}
		},
		"top":function(x,y,width,height,offset,broadenPosition){
			if(y >= offset.top  && y < offset.top + Broaden.handlerRange   || broadenPosition == Broaden.TOP){
				Broaden.$body.css("cursor","n-resize");
				return true;
			}
		},
		"bottom":function(x,y,width,height,offset,broadenPosition){
			if(y <= offset.top + height && y > offset.top + height - Broaden.handlerRange || broadenPosition == Broaden.BOTTOM){
				Broaden.$body.css("cursor","n-resize");
				return true;
			}
		}
	},
	
	/**
	 * 解除手动伸拉效果
	 * @method unbind
	 */
	unbind:function(){
		if(this.$body){
			this.$body.unbind("mousemove",this._mouseMoveListener)
				.unbind("mousedown",this.mouseDownBroaden)
				.unbind("mouseup",this.clearBroaden).removeAttr("isBroaden");
		}
	},
	_bind:function(conf){
		this.$body.bind("mousemove",{conf:conf},this._mouseMoveListener).bind("mousedown",this._mouseDownBroaden).bind("mouseup",this._clearBroaden);
	},
	_mouseDownBroaden:function(event){
		var $layer = $("#"+Broaden.currentId);
		$layer.attr("isBroaden","true").attr("lastBodyMoveX",event.pageX).attr("lastBodyMoveY",event.pageY);
	},
	_clearBroaden:function(event){
		var $layer = $("#"+Broaden.currentId);
		$layer.attr("isBroaden","false").removeAttr(Broaden.BROADEN_POSITION);
		Broaden.$body.css("cursor","auto");
	}		
};var _CAL = null;
var _input = null;
var _trig = null;
var _format = null;
var _week = false;
var _time = false;
var _lang = "cn";
function loadcalendar(input, trig, format, week, time, language,min,max) {

	loadcalendarConf({
		input:input,
		trig:trig,
		format:format,
		week:week,
		time:time,
		language:language,
		min:min,
		max:max
		
	});
	
}
/**
 * conf {input:文本框id,trig:触发显示日历id,format：日期格式,week：是否显示星期,time：是否显示时间，language：语言，min：最小日期毫秒数,max：最大日期毫秒数，onSelect:选择时间后触发事件fnunction
*/
function loadcalendarConf(conf) {
	var $trig = null;
	if($.isString(conf.trig)){
		$trig = $(document.getElementById(conf.trig));
	}else{
		$trig = $(conf.trig);
	}
	
	var $input = null;
	if($.isString(conf.input)){
		$input = $(document.getElementById(conf.input));
	}else{
		$input = $(conf.input);
	}		
	
	$trig.unbind().bind("click",function(){
		if(conf.trigger){
			conf.trigger();
		}
	
		var layout = $.getElementAbsolutePosition(this);
		var $calendarWrapper =$("#calendarWrapper");
		if($calendarWrapper[0]){
		    if (conf.debug) {
		        //console.info("use exists, document.getElementById('TimeChooser'):" + document.getElementById('TimeChooser'));
		    }
			$calendarWrapper.css({width:300,height:300});
            var isShowTodayBtn = conf.isShowTodayBtn === undefined ? true : conf.isShowTodayBtn;
            var isShowClearBtn = conf.isShowClearBtn || false;
            var timechooser = document.getElementById('TimeChooser');
            if(timechooser && timechooser.refreshChooser){
            	document.getElementById('TimeChooser').refreshChooser($input.val(),conf.min,conf.max,conf.time === true,"",isShowTodayBtn,isShowClearBtn);	
            }
			
		}else{
            if (conf.debug) {
               // console.info("create new");
            }
			$calendarWrapper = $('<div id="calendarWrapper" style="position:absolute;height:300px;width:300px;top:'+layout.y+'px;left:'+layout.x+'px"><div id="calendarInner"></div></div>');
			 $(document.body).append($calendarWrapper);
            var swfVersionStr = "11.1.0";
            var xiSwfUrlStr = ctx+"/static/flex/common/playerProductInstall.swf";
            var flashvars = {};
            var params = {};
            params.quality = "high";
            params.bgcolor = "#333333";
            params.allowscriptaccess = "sameDomain";
            params.allowfullscreen = "true";
            params.wmode  = "transparent";
            
            var attributes = {};
            attributes.id = "TimeChooser";
            attributes.name = "TimeChooser";
            attributes.align = "middle";
			
            flashvars.selectTimeStr = $input.val();
            flashvars.rangStartTimeStr = conf.min;
            flashvars.rangEndTimeStr = conf.max;
            flashvars.isShowTimeSelector = conf.time === true;
            flashvars.errorInfo = "";
            flashvars.isShowTodayBtn = conf.isShowTodayBtn === undefined ? true : conf.isShowTodayBtn;
            flashvars.isShowClearBtn = conf.isShowClearBtn || false;
            swfobject.embedSWF(
            		ctx+"/static/flex/common/TimeChooser.swf", "calendarInner", 
                "100%", "100%", 
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);
            // JavaScript enabled so display the flashContent div in case it is not replaced with a swf object.
            swfobject.createCSS("#calendarInner", "display:block;text-align:left;");

		}
		
		layout = $.checkDomPosition($calendarWrapper,layout.x,layout.y + 10);
		$calendarWrapper.css({left:layout.x,top:layout.y, zIndex: ZIndexMgr.get()});
		window.timeChooserHandler = function(time){	
            if (conf.debug) {
                console.info("window.timeChooserHandler");
            }
			$input.val(time).focus().blur();
			
			if(conf.onSelect){
				conf.onSelect();
			}      
			closeCalendar();
		};
		
		window.closeTimeChooser = function(time){
            if (conf.debug) {
                console.info("window.closeTimeChooser");
            }
			closeCalendar();
		};
		
		window.clearTime = function(time){
            if (conf.debug) {
                console.info("window.clearTime");
            }
			$input.val("");
		};	
		setTimeout(function(){
			$calendarWrapper.bind("click",closeCalendar);
			$(document.body).bind("mousedown",closeCalendar);
		},100);

        if (conf.debug) {
            console.info("document.getElementById('TimeChooser').refreshChooser:" + 
		        document.getElementById('TimeChooser').refreshChooser);
        }
	});

};

function closeCalendar(event) {
	var id = event && event.target.id;
	if(event && id == "calendarWrapper"){
		event.stopPropagation();
		return false;
	}else if(event && id == "TimeChooser"){
	}else {
	    ZIndexMgr.free();
        console.info("function closeCalendar(event)");
        console.info("document.getElementById('TimeChooser').refreshChooser:" + 
                document.getElementById('TimeChooser').refreshChooser);
		$("#calendarWrapper").css({width:0,height:0}).unbind("click",closeCalendar);
		$(document.body).unbind("mousedown",closeCalendar);
        console.info("document.getElementById('TimeChooser').refreshChooser:" + 
                document.getElementById('TimeChooser').refreshChooser);
	}
};
;/**
 * 自定义复选框
 * @class CustomCheck
 * 
 */
var CustomCheck = {
     CHECKED : "icon_checked",  //选中样式
     CHECKED_DIS : "icon_checked_dis", //选中禁用样式
     UNCHECK : "icon_uncheck", //未选中样式
     UNCHECK_DIS : "icon_uncheck_dis", //未选中禁用样式
     CHECK_HARF : "icon_check_harf", //半选中样式
     /**
      * 批量初始化复选框
      * @method batchInit
      * @param conf.outer 复选框共同的外层元素
      */
     batchInit : function(conf){
     	var inputs = $.getJQueryDom(conf.outer).find('input[type="checkbox"]');
     	for(var i = 0, len = inputs.length; i < len; i++){
     		this.init({
     			apply : inputs[i]
     		});
     	}
     },
	/**
	 * 初始化 
	 * @method init
     * @param conf.apply 复选框需要附加在页面中的元素
	 */
	init : function(conf){
		
		var $input = $.getJQueryDom(conf.apply);
		if($input.parent().attr("type") == "checkbox"){
			return;
		}
		$input.hide();
		$input.next('[label="' + $input.attr("id") + '"]').bind("click",{$input:$input},this._labeClick).css("cursor","default")
		var $wrapper = $input.wrap(this._dom($input));
		this.bindEvent($wrapper);		
		this.setOppositeVal($input);

		
	},
	_labeClick : function(event){
		event.data.$input.parent().click();
	},
	/*
     * conf.disabled
     * conf.checked
     * conf.name
     * conf.value
	 */
	_dom : function($input){
		var cls = $input.attr("checked") ? this.CHECKED : this.UNCHECK;		
		if($input.attr("half")){
			cls = this.CHECK_HARF;
		}
		cls += $input.attr("disabled")  ? " dis" : "";

		return $.createDomStr({
					tagName : "a",
					attr : {
						type : "checkbox",
						href : "javascript:void(0)",
						"class" : cls
					}
		});
	},
	bindEvent : function($dom,conf){
		$dom.parent().bind("mousedown", {conf : conf}, this._mousedown)
			.bind("mouseup", {conf : conf}, this._mouseup)
			.bind("click", {conf : conf}, this._click);
	},
	/**
	 * 绑定click事件 
	 * @method click
     * @param $input input复选框，（id，dom对象，jQuery对象）
     * @param clickListener 事件处理函数
     * @param data 事件附带参数，与Jquery绑定事件相同
	 */	 
	click : function($input, clickListener, data){		
		$input = $.getJQueryDom($input);
		if(!data){
			data = {};
		}
		data.listener = clickListener;
		var $wrapper = this.input2custom($input);
		if($wrapper !== false){
			$wrapper.unbind().bind("click", data , this._click);
		}
		
	},
	/*绑定mousedown*/
	mousedown : function($input, mosueDownListener, data){
		$input = $.getJQueryDom($input);
		var $wrapper = this.input2custom($input);
		if($wrapper !== false){
			$wrapper.bind("mousedown", {listener : mosueDownListener, data : data},this._mousedown);
		}
		
	},
	/*绑定mouseup*/
	mouseup : function($input, mouseupListener, data){
		var $wrapper = this.input2custom($input);
		if($wrapper !== false){
			$wrapper.bind("mouseup", {listener : mouseupListener, data : data},this._mouseup);
		}
	},
	_mousedown:function(event){
		var listener = event.data.listener;
		if(listener){
			return listener(event);
		}
		return true;
	},
	_mouseup : function(event){
		var listener = event.data.listener;
		if(listener){
			return listener(event);
		}
		return true;
	},
	_click : function(event){
		var listener = event.data.listener;
		var $wrapper = $(this);
		var $input = $wrapper.children("input");
		if(CustomCheck.isAvailable($input) == true){
			return;
		}
		var wishCheck = $input.attr("wishCheck"); //api指定要设置为选中或者取消选中
		$input.removeAttr("wishCheck");
		var isCheck = wishCheck ? (wishCheck == "true" ? true : false) : !CustomCheck.isChecked($input);
		CustomCheck._changeState($input, isCheck);
		if(listener){
			listener.call($input[0], event);			
		}
		CustomCheck._relateCheck($input);
		return true;
	},
	/**复选框更改状态关联操作*/
	_relateCheck : function($input){
		var relateId = $input.attr("relateId");
		var $all = null;
		var isChecked = false;
		if(relateId){ //如果是被关联复选框
			var id = $.getJQueryDom(relateId).attr("id");
			$all = $.getJQueryDom(id);
			var $relates = $('input[type="checkbox"][relateId="' + id + '"]');
			var checkedLen = $relates.filter(":checked").length;
			isChecked = checkedLen == $relates.length;
			if(!isChecked && checkedLen !=0){
				CustomCheck.setHref($all);
			}else{
				CustomCheck.setCheck($all, isChecked, false);	
			}
		}else if($input.attr("related") == "true"){
			CustomCheck._checkAll($input);
		}
		
	},
	/*
     * 选中与该复选框关联的复选框
	 */
	_checkAll : function($input){
		var $relates = $('input[type="checkbox"][relateId="' + $input.attr("id") + '"]');
		CustomCheck.setCheck($relates, CustomCheck.isChecked($input), false);
	},
	/**内部方法，设置复选框选中未选中状态*/
	_changeState : function($input, isChecked, $customInput){
		$customInput = $customInput || $input.parent();
		if(isChecked){				
			$input.attr("checked","checked");
			$customInput.removeClass().addClass(this.CHECKED);
		}else{				
			$input.removeAttr("checked");
			$customInput.removeClass().addClass(this.UNCHECK);
		}
		this.setCheckedVal($input, isChecked, $customInput);
	},
	/**
	 * 获得复选框
	 * @param $wrapper 复选框外层包装
	 * @param isChecked 是否选中
	 * @param  isDisabled 是否禁用
	 */
	get : function($wrapper,isChecked,isDisabled){
		$wrapper = $.getJQueryDom($wrapper);
		var checked = "";//选择全部复选框
		if(isChecked === true){ //选择选中的复选框
			checked = "[checked]";
		}else if (isChecked === false){ //选择没有选中的复选框
			check = ":not([checked])";
		}
		var disabled = "";//
		if(isDisabled === true){
			disabled = "[disabled]";
		}else if (isDisabled === false){
			disabled = ":not([disabled])";
		}
		return $wrapper.find('a[type="checkbox"]'+ checked + disabled).children('input[type="checkbox"]');		
	},
	input2custom : function($input){
		$input = $.getJQueryDom($input);
		var $custom = $input.parent();
		if($custom.attr("type") == "checkbox" && $custom[0].tagName.toLowerCase() == "a"){
			return $custom;
		}
		return false;
	},
	/**
	 * 判断复选框是否可用
	 * @method isAvailable
	 * @param $input 复选框对象
	 * @return true/false
	 */
	isAvailable : function($input){
		var $input = $.getJQueryDom($input);
		return $input.attr("disabled") ? true : false;		
	},
	/**
	 * 设置复选框是否可用
	 * @method setAvailable
	 * @param $input 复选框对象
	 * @param isAvailable 是否可用 true/false	 
	 */	
	setAvailable:function($input, isAvailable){
		var $input = $.getJQueryDom($input);
		for(var i = 0, len = $input.length; i < len; i++){
			if(CustomCheck.input2custom($input[i]) === false){
				continue;
			}
			if(isAvailable){
				$($input[i]).removeAttr("disabled").parent().removeClass("dis");
			}else{			
				$($input[i]).attr("disabled","disabled").parent().addClass("dis");
			}
		}
	},
	/**
     * 设置复选框是否选中
     * @method setCheck
     * @param $input 复选框对象
     * @param isChecked 是否选中 true/false
     * @param callback 方法回调 非必填
     * @param isTrigger 是否需要触发点击事件 默认true
	 */
	setCheck : function($input, isChecked, callback, isTrigger){
		if(arguments.length == 3){
			if(!$.isFunction(arguments[2])){
				CustomCheck.setCheck($input, isChecked, null, arguments[2]);
				return;
			}
		}
		var $input = $.getJQueryDom($input), $inp = null, $customInput = null;;
		for(var i = 0, len = $input.length; i < len; i++){
			$customInput = CustomCheck.input2custom($input[i]);
			if($customInput === false){
				return;
			}			
			$inp = $($input[i]);			
			if(isTrigger !== false){  
				$inp.attr("wishCheck", isChecked);
				$customInput.click();	
			}else{
				CustomCheck._changeState($input, isChecked, $customInput);
			}
			
			if(callback && $.isFunction(callback)){
				callback($($input[i]));
			}	
		}
	},
	/**
	 * 复选框是否选中
	 * @method isChecked
	 * @param $input 复选框对象
	 * @return true/false
	 */	
	isChecked : function($input){
		var $input = $.getJQueryDom($input);
		if($input.attr("checked")){
			return true;
		}
		return false;		
	},	
	/**
     * 设置复选框为半选状态
     * @method setHref
     * @param $input 复选框对象
	 */
	setHref : function($input){
		var $input = $.getJQueryDom($input);
		if(CustomCheck.input2custom($input) === false){
			return;
		}
		$input.parent().removeClass().addClass(this.CHECK_HARF);
	},
	/**
     * 设置相反值
	 */
	setOppositeVal : function($input){
		var $input = $.getJQueryDom($input);
		if(CustomCheck.input2custom($input) === false){
			return;
		}
		if($input.attr("checkedvalue")){
			$input.attr("checkedvalue", $input.attr("checkedvalue"));
		}
		if($input.attr("uncheckedvalue")){
			$input.attr("uncheckedvalue", $input.attr("uncheckedvalue"));
		}
	},
	/**
     * 将当前值设置为选中值或为未选值
     * @param $input 复选框对象
     * @param isChecked 是否选中
     * @param $customInput 复选框外层的自定义元素
	 */
	setCheckedVal : function($input, isChecked, $customInput){
		if(!$customInput && CustomCheck.input2custom($input) === false){
			return;
		}
		var $input = $.getJQueryDom($input);
		this.setValue($input, $input.attr(isChecked === true ? "checkedvalue" : "uncheckedvalue"));			
	},
	/**
     * 设置复选框值
     * @param $input 复选框对象
     * @param value
	 */	
	setValue : function($input, value){		
		if(value){	
			$.getJQueryDom($input).val(value);			
		}
	},
	/**
     * 获得复选框的值
     * @param $input 复选框对象
	 */
	getValue : function($input){
		return $input.val();
	}

};/**
 * 加载状态鼠标周围时钟显示
 * 
 * @class Clock
 * 
 */
var Clock = {
	clockId : "loadingClock",
	/**
	 * 开始显示时钟
	 * 
	 *     Clock.start();
	 *     
	 * @method start
	 * 	
	 * */
	start:function(){
		var clock = document.getElementById(Clock.clockId);
		if(!clock){
			$(document.body).append($('<div id="'+Clock.clockId+'" style="position:absolute;height:16px;width:16px;z-index:1000"><div id="clockswf"></div></div>'));
			var flash = new RiiLWidgets(ctx + "/static/flex/common/busy.swf", "clock" , "100%", "100%");
			flash.render("clockswf");
		}else{
			clock.style.display = "block";
		}
		$(document.body).bind("mousemove",Clock._move);
	},
	/**
     * 停止显示时钟
     * 
     *     Clock.stop();
     *     
     * @method stop
     *  
     * */
	stop:function(){
		$("#"+Clock.clockId).hide();
		$(document.body).unbind("mousemove",Clock._move);
	},
	/*
	 * @inner
	 * 鼠标移动处理事件
	 * */
	_move:function(event){
		var x = event.pageX;
		var y = event.pageY;
		$("#"+Clock.clockId).css({left:x,top:y});
	}
};var ColorPanel  = {};

ColorPanel.init = function(left,top,targetId,color){
	color = color || "";
	$("#colorPanel").remove();
	var ColorHex=new Array('00','33','66','99','CC','FF')
	var SpColorHex=new Array('FF0000','00FF00','0000FF','FFFF00','00FFFF','FF00FF')
	ColorPanel.current=null;
	ColorPanel.targetId = targetId;
	
	var colorTable=''
		for (i=0;i<2;i++){
			for (j=0;j<6;j++){
			    colorTable=colorTable+'<tr height=12>'
			    colorTable=colorTable+'<td width=11 style="background-color:#000000">'
			
			    if (i==0){
			    colorTable=colorTable+'<td width=11 style="background-color:#'+ColorHex[j]+ColorHex[j]+ColorHex[j]+'">'}
			    else{
			    colorTable=colorTable+'<td width=11 style="background-color:#'+SpColorHex[j]+'">'}
			
			
			    colorTable=colorTable+'<td width=11 style="background-color:#000000">'
			    for (k=0;k<3;k++){
				       for (l=0;l<6;l++){
				        colorTable=colorTable+'<td width=11 style="background-color:#'+ColorHex[k+i*3]+ColorHex[l]+ColorHex[j]+'">'
				       }
			     }
			}
		}
		colorTable='<table width="253" border="0" cellspacing="0" cellpadding="0" style="border:1px #000000 solid;border-bottom:none;border-collapse: collapse" bordercolor="000000">'
		           +'<tr height=30><td colspan=21 bgcolor=#cccccc>'
		           +'<table cellpadding="0" cellspacing="1" border="0" style="border-collapse: collapse">'
		           +'<tr><td width="3"><td><input type="text" name="DisColor" id="DisColor" size="6" disabled style="border:solid 1px #000000;background-color:'+color+'"/></td>'
		           +'<td width="3"><td><input type="text" name="HexColor" id="HexColor" size="7" style="border:inset 1px;font-family:Arial;" value="'+color+'"></td></tr></table></td></table>'
		           +'<table width="252" border="0" cellspacing="0" cellpadding="0" style="border-collapse: collapse" bordercolor="000000" onmouseover="ColorPanel.doOver()" onmouseout="ColorPanel.doOut()" onmousedown="ColorPanel.doclick()" style="cursor:hand;border:1px #000000 solid;border-bottom:none;border-collapse: collapse;">'
		           +colorTable+'</table>';
		var $div = $("<div id='colorPanel' style='position:absolute;width:253px;height:177px;left:"+left+"px;top:"+top+"px;z-index:" + ZIndexMgr.get() + "'></div>");
		$div.append(colorTable);
		$(document.body).append($div);
		
		$(document.body).unbind("mousedown",ColorPanel.bodyClick);
		setTimeout(ColorPanel.bindBodyClick,500);
}

ColorPanel.bindBodyClick = function(){
	$(document.body).bind("mousedown",ColorPanel.bodyClick);
}

ColorPanel.bodyClick = function(){
	setTimeout(function(){
	    ZIndexMgr.free($("#colorPanel"));
		$("#colorPanel").remove();
		$(document.body).unbind("mousedown",ColorPanel.bodyClick);
	},100);
}



ColorPanel.doclick = function(){
	var evt=ColorPanel.getEvent();
	var element=evt.srcElement || evt.target;
	if (element.tagName=="TD"){
	   var bg=ColorPanel.rgbToHex(element._background);
	   var input = document.getElementById(ColorPanel.targetId);
	   if(input.tagName.toLowerCase() == "input"){
	   	input.value=bg;	
	   	input.focus();
	   input.blur();
	   }else{
	   	input.style.backgroundColor = bg;
	   	document.getElementById(input.id+"_hidden").value = bg;
	   }
   
	}	
	$("#colorPanel").remove();
	$(document.body).unbind("mousedown",ColorPanel.bodyClick);
}

ColorPanel.getEvent =  function(){
	if(document.all){
	   return window.event;
	}
	func=ColorPanel.getEvent.caller;
	while(func!=null){
	   var arg0=func.arguments[0];
	   if(arg0){
	    if((arg0.constructor==Event || arg0.constructor ==MouseEvent)||(typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation)){
	    	return arg0;
	    }
	   }
	   func=func.caller;
	}
	return null;
}

ColorPanel.doOver = function(){
	var evt=ColorPanel.getEvent();
	var element=evt.srcElement || evt.target;
	var DisColor=document.getElementById("DisColor");
	var HexColor=document.getElementById("HexColor");
	if ((element.tagName=="TD") && (ColorPanel.current!=element)) {
	        if (ColorPanel.current!=null){ColorPanel.current.style.backgroundColor = ColorPanel.current._background};
	        element._background = element.style.backgroundColor;
	        DisColor.style.backgroundColor = ColorPanel.rgbToHex(element.style.backgroundColor);
	        HexColor.value = ColorPanel.rgbToHex(element.style.backgroundColor);
	        element.style.backgroundColor = "white";
	        ColorPanel.current = element;
	    }
}

ColorPanel.rgbToHex = function(aa){
	if(aa.indexOf("rgb") != -1)	{
	    aa=aa.replace("rgb(","");
	    aa=aa.replace(")","");
	    aa=aa.split(",");
	    r=parseInt(aa[0]);
	    g=parseInt(aa[1]);
	    b=parseInt(aa[2]);
	    r = r.toString(16);
	    if (r.length == 1) { r = '0' + r; }
	    g = g.toString(16);
	    if (g.length == 1) { g = '0' + g; }
	    b = b.toString(16);
	    if (b.length == 1) { b = '0' + b; }
	    return ("#" + r + g + b).toUpperCase();
	}
	else{
	    return aa;
	}
}

ColorPanel.doOut = function() {
    if (ColorPanel.current!=null) ColorPanel.current.style.backgroundColor = ColorPanel.current._background;
};

/**
 * 控制页面左右结构折叠操作
 * */
var LeftFold = {
    doLayout : [],
    addLayout : function(fn){
        this.doLayout.push(fn);
    },
    init:function(){
        var $layoutRight = $("#layout_right");
        var $layoutLeft = $("#layout_left");
        var $hold = $('#layoutHold');
        var holdClass = "shrink-ico-right";
        var interval = 0;
        function fold(method){
            step = 15;

            interval = setInterval(function(){
                var leftWidth = $layoutLeft.width();
                var newleftWidth = leftWidth+method*step;
                var flag = true;
                if(newleftWidth < 0){
                    newleftWidth = 0;
                    flag = false;
                }
                if(newleftWidth > 190){
                    newleftWidth = 190;
                    flag = false;
                }
                var cut = newleftWidth - leftWidth;
                $layoutRight.width($layoutRight.width()+cut*-1);
                $layoutLeft.width(newleftWidth);
                if(flag===false){
                    clearInterval(interval);

                    if(window.QueryPanel){
                        QueryPanel.refreshWidth(QueryPanel.id);
                    }
                    if(method>0){
                        $hold.removeClass("shrink-ico-right");
                        $layoutLeft.removeClass("zy-shrink-left-close");    
                        LayoutManager.layoutsObj.portletLayout($layoutLeft);
                        LayoutManager.layoutsObj.leftNavigationLayout();                    
                    }else{                      
                        $hold.addClass("shrink-ico-right");
                        $layoutLeft.addClass("zy-shrink-left-close");
                    }
                    
                    for(var i = 0; i < LeftFold.doLayout.length; i++){
                        LeftFold.doLayout[i]();
                    }
                }
            },20);
        }
        
        
        $('#layoutHold').unbind().click(function() {
            var folded = !!$('#layoutHold').data('folded');
            if (folded) {
                fold(1);
            }else {
                fold(-1);
            }
            $('#layoutHold').data('folded', !folded);
        }); 
                
    }
}
    
LeftFold.init();
;/**
 * 配置信息检查和存取工具
 * 
 * @class ConfigData
 */
/* jshint node:true */
(function(window) {
    'use strict';
    var $;
    // 用于node测试
    if (!window) {
        window = module.exports;
        $ = require('jquery');
    }else {
        $ = window.$;
    }

    var CHECK_NO_DATA = 'parameter is required';
    var CHECK_NO_KEY = 'parameter "{1}" is required';
    var CHECK_MUSH_ARRAY = 'parameter "{1}" must be array';
    var CHECK_MUSH_NOT_ARRAY = 'parameter "{1}" shouldn\'t be array';
    var CHECK_WRONG_TYPE = 'parameter "{1}" must be {2}, but got "{3}"';
    var CHECK_MUST_DOM = 'parameter "{1}" must be element or "id" of document element';
    var CHECK_EMPTY_STRING = 'parameter "{1}" shouldn\'t be an empty string';
    var CHECK_EMPTY_ARRAY = 'parameter "{1}" shouldn\'t be an empty array';
    
    var DEFAULT_CHECK_INFO = {
        checkId: null,      // 递归检查是引用
        required: true,    // 是否可以不给值
        empty: false,        // 是否可以为空白值，空字符串或空数组
        type: [],           // 支持多种类型
        array: false,     // true:必须是数组, false:不能是数组, null:可以是也可以不是
        elems:{}            // 内部结构
    };
    
    var CHECK_CONSTRAINS = {type:['object', 'string'], checkId:'checkElem', elems: {
        checkId: {type:['string'], required:false},
        required: {type:['boolean'], required:false},
        empty: {type:['boolean'], required:false},
        type: {type:['string'], array:true},
        array: {type:['boolean'], required:false},
        elems: {type:['object', 'string'], required:false, elems: {
            '*': 'checkElem'
        }}
    }};
    
    /*
    var sampleCheckInfo = {
        key1: {type: ["string", "function"]},
        key2: {required: false, type:["string"]},
        key3: {type: ["string", "function"], array: true},
        key4: {type: ["string", "object"], elems: {
            key5: {type: ["string", "function"]},
            key6: {required: false, type:["string"]},
            key7: {type: ["object"], array: true, elems:sampleCheckInfo}
        }}
    };
    */
    
    var m_checkInfoMap = {};
    
    window.ConfigData = {
        /**
         * 无用处，仅用于兼容
         * @method init
         */
        init: init,
        /**
         * 为jquery对象添加配置信息
         *      
         *      ConfigData.set($("#item1"), {
         *          a: 1,
         *          b: 2
         *      });
         *      
         * @method set
         * @param $object {jquery} jquery对象
         * @param config 配置信息
         */
        set: setConfigData, // config ($object, config)
        
        /**
         * 获取jquery对象配置信息（之前必须通过set添加过）
         *      
         *      var config = ConfigData.get($("#item1"));
         *      
         * @method get
         * @param $object {jquery} jquery对象
         * @return {object} 配置信息
         */
        get: getConfigData, // config ($object)
        
        /**
         * 检查配置信息是否符合规则，出错时抛出异常
         * 
         *      基本类型检查：
         *      
         *      var value = false;
         *      
         *      var checkInfo = {type:["boolean"]};
         *      
         *      try {
         *          ConfigData.check("value", value, checkInfo);
         *      }catch(errStr){
         *          // 参数错误的处理
         *          console.log(errStr);
         *      }
         *      
         *      复杂对象类型检查：
         *      
         *      var config = {
         *          a: null,
         *          b: [],
         *          c: "111"
         *      };
         *      
         *      var checkInfo = {
         *          type: ["object"],
         *          elems: {
         *              a: {type: ["string"], required: false},
         *              b: {type: ["object"]},
         *              c: {type: ["string"]}
         *          }
         *      };
         *      
         *      try {
         *          ConfigData.check("sampleConfig", config, checkInfo);
         *      }catch(errStr){
         *          // 参数错误的处理
         *          console.log(errStr);
         *      }
         *      
         * @method check
         * @param key {string} 对当前配置信息的描述，必须有
         * @param data {object} 配置信息
         * @param checkInfo {Json} 配置信息的约束
         * @param [checkInfo.checkId] {String} 检查ID，可被子检查调用，如例子中的sampleCheckInfo
         * @param [checkInfo.required] {Boolean} 是否必须，默认为true
         * @param [checkInfo.empty] {Boolean} 是否必须有值（不能为空白字符串），默认为false
         * @param checkInfo.type {String Array} 类型集合，定义所有允许类型，必须提供
         * @param [checkInfo.array] {Boolean} 是否为数组，不提供时不做检查
         * @param elems {Object | String} 属性信息，字符串时为父检查信息的名称，如例子中的sampleCheckInfo
         */
        check: function(key, data, checkInfo) {
            check('ConfigData.check:checkInfo', checkInfo, CHECK_CONSTRAINS);
            check(key, data, checkInfo);
        },
        
        /**
         * 为配置信息补充默认值
         * 
         *      ConfigData.setDefaultValue(config, DEFULT_CONFIG);
         *      
         * @method setDefaultValue
         * @param config {object} 配置信息
         * @param DEFULT_CONFIG {object} 默认配置信息
         */
        setDefaultValue: setDefaultValue,    // (data, defaultData)
        
        /**
         * 检查参数domId指定的元素是否存在
         * 
         *      ConfigData.checkDomElem("item1");
         *      ConfigData.checkDomElem($item);
         *      ConfigData.checkDomElem("fun的param1", "item1");
         *      ConfigData.checkDomElem("fun的param1", $item1);
         * 
         * @method checkDomElem
         * @param info {String} 描述，用于说明是哪个参数错了
         * @param value {String|Dom|JQuery} dom id或dom
         * @return {boolean} 存在时为true否则为false
         */
        checkDomElem: checkDomElem // boolean (info, value)
    };
    
    function init() {
        
    }
    
    function checkDomElem(info, value) {
        if (arguments.length < 2) {
            value = info;
            info = undefined;
        }

        var $elem;
        if ('string' === typeof value) {
            $elem = $('#' + value);
        }else {
            $elem = $(value);
        }

        if ($elem.length === 0) {
            msg(CHECK_MUST_DOM, info || value);
        }
    }
    
    function setConfigData($object, config) {
        $object.data('config', config);
    }
    
    function getConfigData($object) {
        $object.data('config');
    }
    
    function setDefaultValue(data, defaultData) {
        if ($.isArray(defaultData)) {
            var i;

            if (!data) data = [];
            if (defaultData.length === 0) return data;
            
            i = Math.max(data.length, defaultData.length);
            while (i--) {
                data[i] = setDefaultValue(data[i], defaultData[i]);
            }
        }else if ($.isPlainObject(defaultData)) {
            if (!data) data = {};
            for (var key in defaultData) {
                if (!defaultData.hasOwnProperty(key)) continue;
                data[key] = setDefaultValue(data[key], defaultData[key]);
            }
        }else {
            data = getValueByDefault(data, defaultData);
        }
        
        return data;
    }
    
    function getValueByDefault(value, defaultValue) {
        if (undefined === value || null === value) return defaultValue;
        else return value;
    }
    
    function check(key, data, checkInfo) {
        if (typeof checkInfo === 'string') {
            checkInfo = getCheckInfo(checkInfo);
        }else {
            var checkId = checkInfo.checkId;
            
            setDefaultValue(checkInfo, DEFAULT_CHECK_INFO);
            
            if (null !== checkId && undefined !== checkId) {
                setCheckInfo(checkId, checkInfo);
            }
        }
        
        if (!checkRequired(key, data, checkInfo)) return;
        checkEmpty(key, data, checkInfo);
        checkArray(key, data, checkInfo);
        checkType(key, data, checkInfo);
    }
    
    function checkElems(parentKey, data, checkInfo) {
        var elems = checkInfo.elems,
            otherElemCheckInfo = elems['*'];
        var key, curKey, value, elemCheckInfo;
        
        if (!$.isPlainObject(elems)) {
            elems = getCheckInfo(elems);
        }
        
        for (key in elems) {
            if (!elems.hasOwnProperty(key) || '*' === key) {
                continue;
            }
            value = data[key];
            elemCheckInfo = elems[key];
            curKey = parentKey + '.' + key;
                
            check(curKey, value, elemCheckInfo);
        }
        
        if (otherElemCheckInfo) {
            for (key in data) {
                if (!data.hasOwnProperty(key) || elems.hasOwnProperty(key)) {
                    continue;
                }
                
                value = data[key];
                curKey = parentKey + '.' + key;
                    
                check(curKey, value, otherElemCheckInfo);
            }
        }
    }
    
    function checkRequired(key, data, checkInfo) {
        if (null === data || undefined === data) {
            if (checkInfo.required){
                if (key) {
                    msg(CHECK_NO_KEY, key);
                }else {
                    msg(CHECK_NO_DATA);
                }
            }else {
                return false;
            }
        }else {
            return true;
        }
    }
    
    function checkEmpty(key, data, checkInfo) {
        var empty = checkInfo.empty;
        
        if (typeof data === 'string') {
            if (data === '' && !empty) {
                msg(CHECK_EMPTY_STRING, key);
            }
        }else if ($.isArray(data)) {
            if (data.length < 0 && !empty) {
                msg(CHECK_EMPTY_ARRAY, key);
            }
        }
    }
    
    function msg(msgType, param1, param2, param3) {
        
        if (undefined !== param1 && null !== param1) {
            msgType = msgType.replace(/\{1\}/g, param1);
        }
        
        if (undefined !== param2 && null !== param2) {
            msgType = msgType.replace(/\{2\}/g, param2);
        }

        if (undefined !== param3 && null !== param3) {
            msgType = msgType.replace(/\{3\}/g, param3);
        }
        
        console.info(msgType);
        if (window.alert) window.alert(msgType);
    }
    
    function checkArray(key, data, checkInfo) {
        var isArray = checkInfo.array;
        
        if (null === isArray || undefined === isArray) {
            return;
        }else if ($.isArray(data) && true !== isArray) {
            msg(CHECK_MUSH_NOT_ARRAY, key);
        }else if (!$.isArray(data) && true === isArray) {
            msg(CHECK_MUSH_ARRAY, key);
        }
    }
    
    function checkType(key, data, checkInfo) {
        var type = checkInfo.type,
        count = type.length;
        
        if (0 === count) return;
        
        if ($.isArray(data)) {
            for (var i = 0, c = data.length; i < c; i++) {
                var value = data[i];
                if (value) checkType(key + '[' + i + ']', value, checkInfo);
            }
        }else {
            var curType = typeof data;
            while(count--) {
                if (type[count] === curType) {
                    if ('object' === curType) {
                        checkElems(key, data, checkInfo);
                    }
                    return;
                }
            }
            msg(CHECK_WRONG_TYPE, key, typeListToString(type), curType);
        }
    }
    
    function typeListToString(typeList) {
        var count = typeList.length,
            ret = '';
        while (count--) {
            ret += ' or "' + typeList[count] + '"';
        }
        if (ret) ret = ret.substring(4);
        return ret;
    }
    
    function setCheckInfo(checkId, checkInfo) {
        m_checkInfoMap[checkId] = checkInfo;
    }
    
    function getCheckInfo(checkId) {
        return m_checkInfoMap[checkId];
    }

})(typeof window === 'undefined' ? undefined : window);;/**
 * @class Counter
 * @constructor
 * 时间计时器
 * @example
 * <pre>
 * 	var counter = new Counter("id");
 *  counter.add();
 *  counter.add();
 *  </pre>
 * */
var Counter = function(renderId) {
	this.hour = 0;
	this.minute = 0;
	this.second = 0;
	this.renderTo = document.getElementById(renderId);
}

Counter.prototype = {
	constructor : Counter,
	/**
	 * 计时器加一秒
	 * */
	add : function() {
		if (++this.second == 60) {
			if (++this.minute == 60) {
				this.hour++;
				this.minute = 0;
			}
			this.second = 0;
		}

		
		this.renderTo.innerHTML = (this.hour < 10 ? "0"+this.hour : this.hour) + ":" + (this.minute < 10 ? "0"+this.minute : this.minute ) + ":"
				+ (this.second < 10 ? "0"+this.second : this.second);
	}
};/**
 * 
 */
var Debug = function(){
    var API = {
        log: log,
        getLogger : getLogger,
        deep : 3
    };
    
    return API;
    
    function getLogger(prefix) {
        return function (/*...*/) {
            if (!window[prefix]) return;
            var args = [prefix + ': '];
            for (var i = 0, c = arguments.length; i < c; i += 1) {
                args.push(arguments[i]);
            }
            log.apply(this, args);
        }
    }
    
    function log(/*...*/) {
        var info = ["[" + getTimeStr() + "] "];
        var i, c;
        for (i = 0, c = arguments.length; i < c; i+=1) {
            info.push(logElem(arguments[i]));
        }
        if (console && console.info) {
            console.info(info.join(''));
        }
    }
    
    function logElem(argument, deep) {
        if (undefined === deep) {
            deep = API.deep;
        }
        
        deep -= 1;
        
        if (deep < 0) return "...";
        
        if ($.isArray(argument)) {
            return logArray(argument, deep);
        }else if ($.isString(argument)) {
            return logText(argument, deep);
        }else if ($.isFunction(argument)) {
            return logFunction(argument, deep);
        }else if (null === argument) {
            return 'null';
        }else if (undefined === argument) {
            return 'undefined';
        }else if (typeof(argument) === "object" && argument.hasOwnProperty) {
            return logObject(argument, deep);
        }else {
            return '' + argument;
        }
    }
    
    function logText(text, deep) {
        return '"' + text + '"';
    }
    
    function logFunction(text, deep) {
        return '<Function>';
    }
    
    function logObject(obj, deep) {
        var info = [];
        
        for (var key in obj) {
            if (!obj.hasOwnProperty(key)) continue;
            info.push(key + '=' + logElem(obj[key], deep));
        }
        
        return '{' + info.join(', ') + '}';
    }
    
    function logArray(arr, deep) {
        var info = [];
        
        for (var i = 0, c = arr.length; i < c; i+=1) {
            info.push(logElem(arr[i], deep));
        }
        
        return '[' + info.join(', ') + ']';
    }
    
    function getTimeStr() {
        return formatDate(new Date(), "hh:mm:ss(SSS)");
    }
    
    function formatDate(date, format) {
        var o = {
            "M+" : date.getMonth() + 1,     // month
            "d+" : date.getDate(),          // day
            "h+" : date.getHours(),         // hour
            "m+" : date.getMinutes(),       // minute
            "s+" : date.getSeconds(),       // second
            "q+" : Math.floor((date.getMonth() + 3) / 3), // quarter
            "S+" : date.getMilliseconds()    // millisecond
        };
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + "")
                    .substr(4 - RegExp.$1.length));
        }
        for ( var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                var value = "" + o[k];
                if (k.substring(0, 1) === "S") {
                    value = RegExp.$1.length == 1 ? o[k] : ("000" + value).substr(value.length);
                }else {
                    value = RegExp.$1.length == 1 ? o[k] : ("00" + value).substr(value.length);
                }
                format = format.replace(RegExp.$1, value);
            }
        }
        return format;
    }
}();
;	var DiskUse = {
			init : function(conf){
				var data = this.parseObjParam(conf);

				var configed = conf.configed = +data.configed.value;
				var allAapacity = conf.allAapacity = +data.allAapacity.value;
				var used = conf.used = +data.used.value;
				var surplus = conf.surplus = +data.surplus.value;
				conf.allAapacityOrginalValue = +data.allAapacity.orginalValue;
				conf.configedOrginalValue = +data.configed.orginalValue;
				if(conf.configedOrginalValue > conf.allAapacityOrginalValue){
					conf.configedWidth = '100%';
					if(allAapacity * 2 < configed){
						conf.allAapacityWidth = "95%";
					}else{
						conf.allAapacityWidth = ((+data.allAapacity.orginalValue) / (+data.configed.orginalValue) * 100) + "%";
					}
					if(data.allAapacity.value == "-"){
						conf.allAapacityWidth = "0";
					}
				}else{
					conf.allAapacityWidth = "100%";
					var configedWidth = ((+data.configed.orginalValue) / (+data.allAapacity.orginalValue) * 100);
					if(configedWidth > 100){
						configedWidth = 100;
					}
					conf.configedWidth =  configedWidth + '%';
					if(conf.configedWidth == 'NaN%'){
						conf.configedWidth = "0";
					}
				}
				var usedWidth = ((+data.used.orginalValue) / (+data.allAapacity.orginalValue) * 100) ;
				if(usedWidth > 100){
					conf.usedWidth = "100%";
				}else{
					conf.usedWidth = usedWidth +　"%";
				}
				

				if(conf.used.value == '-'){
					conf.usedWidth = '0';
				}


				conf.usedLabel = data.used.label;
				conf.allAapacityLabel = data.allAapacity.label;
				conf.configedLabel = data.configed.label;
				conf.surplusLabel = data.surplus.label;



				conf.usedValue = data.used.value + data.used.unit;
				
				if(data.used.value == '-'){
					conf.usedValue = data.used.value;
				}else{
					conf.usedValue = data.used.value + data.used.unit;
				}


				if(data.allAapacity.value == '-'){
					conf.allAapacityValue = data.allAapacity.value;
				}else{
					conf.allAapacityValue = data.allAapacity.value + data.allAapacity.unit;
				}


				if(data.configed.value == "-"){
					conf.configedValue = data.configed.value ;
				}else{
					conf.configedValue = data.configed.value + data.configed.unit;
				}

				conf.surplusValue = data.surplus.value + data.surplus.unit;

				$("#" + conf.apply).append(this.dom(conf.data, conf));

			},
			parseObjParam : function(conf){
				var data = {};

				var paramData = conf.data;
				var typeFetch = conf.typeFetch;
				var dataFetch = conf.dataFetch;
				for(var key in typeFetch){
					var pd = paramData[typeFetch[key]];
					var obj = {};
					for(var key1 in dataFetch){
						obj[key1] = pd[dataFetch[key1]];
					}
					data[key] = obj;
				}
				return data;
			},
			dom : function(data, conf){
				var dom = [];
				dom.push('<div class="discUseStatisticsWrapper">');
/*				dom.push('	<div class="disUseTitle-r">');
				dom.push('		<div class="disUseTitle">' + conf.title + '</div>');
				dom.push('	</div>');
*/

				var high = 'padding-top:45px;';
				var low = 'padding-top:26px;';
				var configedPadding = low;
				var allPadding = low;
				if(conf.allAapacityOrginalValue > conf.configedOrginalValue){
					allPadding = "padding-top:45px;";
				}else{
					configedPadding = "padding-top:65px;";
				}
				dom.push('<div class="discUseStatistics">');
				dom.push('	<div class="configed" style="'+configedPadding+'width:' + conf.configedWidth + '">' + conf.configedValue + '</div>');
				dom.push('	<div class="allAapacity" style="width:' + conf.allAapacityWidth + '">');




				dom.push('		<div class="used" style="width:' + conf.usedWidth + ';">' + conf.usedValue + '</div>');
				
				dom.push('		<div class="text-label" style="'+allPadding+'">' + conf.allAapacityValue + '</div>');
				dom.push('	</div>');
				dom.push('</div>');
				dom.push('<table class="discUseStatisticsLegend">');
				dom.push('	<tr>');
				dom.push('		<td><div class="allAapacity-legend"></div>' + conf.allAapacityLabel + '：' + conf.allAapacityValue + '</td>');
				dom.push('		<td><div class="used-legend"></div>' + conf.usedLabel + '：' + conf.usedValue + '</td>');
				dom.push('	</tr>');
				dom.push('	<tr>');
				dom.push('		<td>' + conf.surplusLabel + '：' + conf.surplusValue + '</td>');
				dom.push('		<td><div class="configed-legend"></div>' + conf.configedLabel + '：' + conf.configedValue + '</td>');
				dom.push('	</tr>');
				dom.push('</table>');
				dom.push('</div>');
				return dom.join('');
			}
		}
;window.EventQueue = function(){
    var _aEventList = [];
    var TIME_SLIP = 1000;// 间隔多少毫秒内算同时执行
    var _bLooping = false;
    var _iEventId = 1;
    var _logger = Debug.getLogger("Debug EventQueue");
    var _cancelEventIds = [];
    
    return {
        fireEvent : fireEvent,
        event : fireEvent,
        cancel : cancelEvent
    };
    
    /**
     * 发出一个事件
     * 
     *     // 发出事件，延时1秒执行，执行时弹出alert
     *     window.EventQueue.fireEvent(function(data, datas) {
     *         var key1 = data.key1;
     *         var key2 = data.key2;
     *         var otherProcessKey1 = datas[0].key1;
     *         alert("事件被执行，key1的值是" + key1 + 
     *             "，key2的值是" + key2 + 
     *             "，共同执行的处理的key1的值是" + otherProcessKey1 + "。");
     *     }, {key1: "value1", key2: "value2"}, 1000);
     *     
     * @method fireEvent
     * @param fnProcess_ {Function} 响应事件的“处理函数”，触发事件时被调用
     * @param jsonData_ {Json} 额外的事件信息，“处理函数”的参数。
     * @param iTiming_ {Integer} 执行延时，单位毫秒，延时指定毫秒数后执行
     *     与setInterval和setTimeout用法一致
     * @return {Integer} 事件ID
     */
    function fireEvent (fnProcess_, jsonData_, iTiming_) {
        var oEvent = createEvent(fnProcess_, jsonData_, iTiming_);
        _logger("new event", oEvent);
        addEvent(oEvent);
        loopEvent();
        return oEvent.id;
    }
    
    /**
     * 取消一个已经提交的事件
     * @method cancelEvent
     * @param iEventId_ {Integer} 事件ID
     */
    function cancelEvent (iEventId_) {
        _cancelEventIds.push(iEventId_);
    }
    
    /**
     * 创建一个事件对象
     * @method createEvent
     * @private
     * @param fnProcess_ {Function} 响应事件的“处理函数”，触发事件时被调用
     * @param jsonData_ {Json} 额外的事件信息，“处理函数”的参数。
     * @param iTiming_ {Integer} 执行延时，单位毫秒，延时指定毫秒数后执行
     *     与setInterval和setTimeout用法一致
     */
    function createEvent (fnProcess_, jsonData_, iTiming_) {
        var id = _iEventId;
        _iEventId = id + 1;
        
        var iTime = null;
        if (iTiming_) {
            iTime = new Date().getTime() + parseInt(iTiming_, 10);
        }
        
        var oEvent = {
            id : id,
            process : fnProcess_,
            data : jsonData_,
            time : iTime
        };
        return oEvent;
    }
    
    /**
     * 向事件队列中添加事件
     * @method addEvent;
     * @private
     * @param oEvent_ {Event} 事件对象
     */
    function addEvent (oEvent_) {
        _aEventList.push(oEvent_);
    }
    
    /**
     * 处理下一个事件
     * @method runNextEvent
     * @private
     */
    function runNextEvent () {
        var aEvent = getNextEvent();
        var i, c = aEvent.length;
        var j, l;
        var oEvent;
        var data;
        
        _logger("begin process >>>>>>>>>>>>>>>>>>>>>>>", aEvent);
        
        if (c === 1) {
            oEvent = aEvent[0];
            oEvent.process(oEvent.data, [], 0);
        }else {
            for (i = 0; i < c; i += 1) {
                // 其他同时处理的事件的data
                data = [];
                for (j = 0, l = aEvent.length; j < l; j += 1) {
                    if (i !== j) data.push(aEvent[j].data); 
                }

                oEvent = aEvent[i];
                _logger("    begin process event ", oEvent);
                oEvent.process(oEvent.data, data, i);
                _logger("    end process event ", oEvent);
            }
        }
        
        _logger("end process <<<<<<<<<<<<<<<<<<<<<<<", aEvent);
        return aEvent.length > 0;
    }
    
    /**
     * 获取下一个要处理的时间。同时从事件队列中清除。
     * @method getNextEvent
     * @private
     * @return {Array} 同时处理的事件
     */
    function getNextEvent () {
        var iTime = new Date().getTime();
        var i, c;
        var oEvent;
        var aEvents = [];
        var bTiming = false; // 标记将要执行的事件是否为定时事件（此时会有多个）
        
        _logger("find event in >>>>>>>>>>>>>>>>>>>", _aEventList);
        
        for (i = 0, c = _aEventList.length; i < c; i+=1) {
            oEvent = _aEventList[i];
            
            if (isCanceled(oEvent.id)) {
                _aEventList.splice(i, 1);
                i -= 1;
                c -= 1;
                _logger("    event was canceled", oEvent);
                continue;
            }
            
            if (!bTiming && $.isNull(oEvent.time)) {// 非定时事件
                aEvents.push(oEvent);
                _aEventList.splice(i, 1);
                _logger("    immediate event found", oEvent);
                break;
            }else if (iTime > oEvent.time){// 定时事件
                aEvents.push(oEvent);
                bTiming = true;
                _aEventList.splice(i, 1);
                i -= 1;
                c -= 1;
                _logger("    timing event found", oEvent);
            }else if (bTiming && iTime + TIME_SLIP > oEvent.time) { // 同时执行的定时事件
                aEvents.push(oEvent);
                _aEventList.splice(i, 1);
                i -= 1;
                c -= 1;
                _logger("     1s delay event found", oEvent);
            }
        }
        _logger("    events queue", _aEventList);
        _logger("events found <<<<<<<<<<<<<<<<<<<<<<<<", aEvents);
        
        return aEvents;
    }
    
    /**
     * 开始事件循环
     * @method loopEvent
     * @private
     */
    function loopEvent() {
        if (_bLooping) return;
        _bLooping = true;
        setTimeout(oneLoop, getNextDelay());
        _logger("looping start");
        function oneLoop() {
            var timeStart = new Date().getTime();
            var time;
            var delay;
            while (_bLooping) {
                if (!runNextEvent()) {
                    _logger("no more event to be run immediatly, process is waiting for delay ", _aEventList);
                    break;
                }
                time = new Date().getTime();
                if (time - timeStart > 300) {
                    _logger("more than 300ms process need a snap, startTime ", timeStart, "，now ", time); 
                    break;
                }
            }
            delay = getNextDelay();
            if (null !== delay) {
                setTimeout(oneLoop, delay);
                _logger("next looping start after ", delay, "ms.");
            }else {
                _bLooping = false;
                _logger("event queue is empty, looping stop");
            }
        }
        
    }
    
    /**
     * 获取下一次执行的延迟
     * @method getNextDelay
     * @return {Integer} 延迟毫秒数，为null时说明没有可执行的事件了
     */
    function getNextDelay() {
        var iNow = new Date().getTime();
        var i, c = _aEventList.length;
        var oEvent;
        var iNextTime = null;
        var delay = null;
        _logger("calculate delay>>>>>>>>>>>>>>>");
        
        for (i = 0; i < c; i += 1) {
            oEvent = _aEventList[i];
            if ($.isNull(oEvent.time)) {
                iNextTime = -1;
                _logger("    immediate event found, no delay");
                break; // 如果有立即执行的事件，不需要判断其他事件
            }else {
                if (iNow > oEvent.time) {
                    iNextTime = -1;
                    _logger("    timeout event found, no delay");
                    break; // 如果有超时的事件，不需要判断其它事件
                }else if (null === iNextTime || iNextTime > oEvent.time) {
                    iNextTime = oEvent.time; // 查找最近需要处理的事件
                    _logger("    delay event found, delay " + (oEvent.time - iNow) + "ms");
                }
            }
        }
        
        if (null === iNextTime) {
            delay = null;
        }else if (iNextTime > 0 && iNextTime !== iNow){
            delay = iNextTime - iNow;
        }else {
            delay = 1;
        }
        
        if (null === delay) {
            _logger("no delay <<<<<<<<<<<<<<<<<<<<<");
        }else {
            _logger("delay is " + delay + "ms <<<<<<<<<<<<<<<<<<<<<");
        }
        
        return delay;
    }
    
    function isCanceled(iEventId_) {
        var c = _cancelEventIds.length;
        while (c--) {
            if (_cancelEventIds[c] === iEventId_) {
                _cancelEventIds.splice(c, 1);
                return true;
            }
        }
        return false;
    }
}();;var ExceptionAlert = {
    _style : "exception pop-blue",
	init:function(){
		if(!$("#exception")[0]){
			return;
		}
		var scrollTop = document.body.scrollTop;
		var offsetHeight = document.body.clientHeight;
		var offsetWidth = document.body.clientWidth;
		var $exception = $("#exception");
		
		$(document.body).append(this._createShaw()).append($exception);
		$exception.css({
			"top": scrollTop + offsetHeight / 2 - $exception.height(),
			"left":offsetWidth/2-$exception.width()/2,
			"zIndex" : ZIndexMgr.get()
		});
		$exception.show();
		
		$exception.find(".portlet-ico-quit").bind("click",exit);

		function exit(){
			$exception.find("*").unbind();
			$exception.remove();	
			$("#alert_shaw").remove();
			ZIndexMgr.free();
		}
			$("#exceptionDetail").toggle(function(){
				$(this).parent().next("div").show();
			},function(){
				$(this).parent().next("div").hide();
			});			
			$("#exceptionExit").bind("click",exit);
			$("#exceptionExitIco").bind("click",exit);
			if(window.Loading){
				window.Loading.stop();
			}
	},
	_createShaw:function(){
		return '<div class="overlay" id="alert_shaw"></div>';
	},
	createFrame:function(errorMessage,errorCode,stackTrack,hasErrDetail){
		var dom = [];
		dom.push('<div id="exception" class="' + ExceptionAlert._style + '" style="position:absolute; overflow:hidden;">');
	    	dom.push('<div class="top-l"><div class="top-r"><div class="top-m">');
	        		dom.push('<a id="exceptionExitIco" class="pop-ico pop-ico-quit ico-quit"></a><span class="pop-title">'+S_EXCEPTION_SYS_ERROR+'</span>');
	        dom.push('</div></div></div>');
	        dom.push('<div class="middle-l"><div class="middle-r"><div class="middle-m">');
	                dom.push('<div class="pop-middle-m-inner">');
	                	dom.push('<div class="exclamation pop-show double" style="overflow:auto">'+S_EXCEPTION_ERROR_INFO+'：'+errorMessage+'</div>');
	                	dom.push('<div style="text-align:right;overflow:hidden">');
	                	if(hasErrDetail == "on"){
	                		dom.push('<a href="javascript:void(0)"  style="float:left" id="exceptionDetail" title="'+S_EXCEPTION_SHOW_DETAIL+'" class="btn_body btn_body_min ico_hide">');
	                		dom.push('<div class="btn_l">');
	                		dom.push('<div class="btn_r">');
	                		dom.push('<div class="btn_m">');
	                		dom.push('<span class="null"></span>');
	                		dom.push('<span class="text">'+S_EXCEPTION_SHOW_DETAIL+'</span>');
	                		dom.push('<span class="pull"></span>');
	                		dom.push('</div>');
	                		dom.push('</div>');
	                		dom.push('</div>');
	                		dom.push('</a>');
	                	}
	                	
		               		dom.push('<a href="javascript:void(0)" style="float:right" id="exceptionExit" title="'+S_EXCEPTION_QUIT+'" class="btn_body btn_body_min ico_hide">');
		               			dom.push('<div class="btn_l">');
		               				dom.push('<div class="btn_r">');
		               					dom.push('<div class="btn_m">');
		               						dom.push('<span class="null"></span>');
		               						dom.push('<span class="text">'+S_EXCEPTION_QUIT+'</span>');
		               						dom.push('<span class="pull"></span>');
		                					dom.push('</div>');
		               				dom.push('</div>');
		               			dom.push('</div>');
		               		dom.push('</a></div>');
		               	dom.push('<div class="detailmessage" style="overflow-y:auto;clear:both">');
		               		dom.push('<p>'+S_EXCEPTION_ERROR_NUM+'：'+errorCode+'</p>');
		               		dom.push('<br><br><p></p>');
		               		dom.push('<p>'+stackTrack+'</p>');
		               	dom.push('</div>');	
		         dom.push('</div></div></div></div>');        
		  dom.push('<div class="bottom-l"><div class="bottom-r"><div class="bottom-m"></div></div></div>');      
		  dom.push('</div>');
		return dom.join('');
	}
};
;   /*----------------------------------------*\ 
     * 使用 javascript HTML DOM 高亮显示页面特定字词 By shawl.qiu 
     * 参数说明: 
     * o: 对象, 要进行高亮显示的对象. 
     * flag: 字符串, 要进行高亮的词或多个词, 使用 竖杠(|) 分隔多个词 . 
     * rndColor: 布尔值, 是否随机显示文字背景色与文字颜色, true 表示随机显示. 
     * url: URI, 是否对高亮的词添加链接.  
    \*----------------------------------------*/ 
    //--------begin function fHl(o, flag, rndColor, url)------------------// 
    function fHl(o, flag, rndColor, url){ 
        var bgCor=fgCor=''; 
        if(rndColor){ 
            bgCor=fRndCor(10, 20); 
            fgCor=fRndCor(230, 255); 
        } else { 
            bgCor='yellow'; 
            fgCor='black'; 
        } 
        var re=new RegExp(flag, 'i'); 
        for(var i=0; i<o.childNodes.length; i++){     
            var o_=o.childNodes[i]; 
            var o_p=o_.parentNode; 
            if(o_.nodeType==1) { 
                fHl(o_, flag, rndColor, url);                 
             } else if (o_.nodeType==3) { 
                if(!(o_p.nodeName=='A')){ 
                    if(o_.data.search(re)==-1)continue; 
                    var temp=fEleA(o_.data, flag); 
                    o_p.replaceChild(temp, o_); 
                } 
            }  // shawl.qiu script 
        } 
        //------------------------------------------------ 
        function fEleA(text, flag){ 
            var style=' style="background-color:'+bgCor+';color:'+fgCor+';" ' 
            var o=document.createElement('span'); 
            var str=''; 
            var re=new RegExp('('+flag+')', 'gi'); 
            if(url){ 
                str=text.replace(re, '<a href="'+url+ 
                '$1"'+style+'>$1</a>'); //这里是给关键字加链接，红色的$1是指上面链接地址后的具体参数。
            } else { 
                str=text.replace(re, '<span '+style+'>$1</span>'); //不加链接时显示
            } 
            o.innerHTML=str; 
            return o; 
        }     // shawl.qiu script 
        //------------------------------------------------ 
        function fRndCor(under, over){ 
            if(arguments.length==1){ 
                var over=under; 
                    under=0; 
            }else if(arguments.length==0){ 
                var under=0; 
                var over=255; 
            } 
            var r=fRandomBy(under, over).toString(16); 
                r=padNum(r, r, 2); 
            var g=fRandomBy(under, over).toString(16); 
                g=padNum(g, g, 2); 
            var b=fRandomBy(under, over).toString(16); 
                b=padNum(b, b, 2); 
                //defaultStatus=r+' '+g+' '+b 
            return '#'+r+g+b; 
            function fRandomBy(under, over){ 
                switch(arguments.length){ 
                    case 1: return parseInt(Math.random()*under+1); 
                    case 2: return parseInt(Math.random()*(over-under+1) + under); 
                    default: return 0; 
                } 
            } // shawl.qiu script 
            function padNum(str, num, len){ 
                var temp='' 
                for(var i=0; i<len;temp+=num, i++); 
                return temp=(temp+=str).substr(temp.length-len); 
            } // shawl.qiu script 
        } 
    } // shawl.qiu script 
    //--------end function fHl(o, flag, rndColor, url)--------------------// 
//]]> 
;if (!window.FoldPanel) {

    /**
     * 折叠Panel
     * 
     * 该类用于自动抓去制定区域内符合规则结构的元素，自动绑定
     * 结构：
     * 
     *     <div class="fold-panel" id="组件Id" isExpend="true/false">
     *         <div class="h3" onselectstart="return false;" >
     *             <span class="图标样式 panel-ico"></span>
     *             <span class="panel-title" style="cursor:default">标题文字</span>
     *             <span class="<%=foldPanelButton%>"></span>
     *         </div>
     *         <div class="panel-content"></div>
     *     </div>
     *     
     * @class FoldPanel
     * 
     * */   
    var FoldPanel = {
        /**
         * 初始化
         * @method init
         * @param [$dom] {jquery} 如果没有该参数，则表示从全局搜索
         * */
        init:function($dom){
            $dom = $dom ? $dom : $(document.body);
            var $foldPanelDom = $dom.find("div.fold-panel");
            if($foldPanelDom.length ==0){       
                $foldPanelDom = $dom.find("div.blue-panel");
            }
            
            for(var i=0; i<$foldPanelDom.length;i++){
                var $btn = $($foldPanelDom[i]).children("div:eq(0)");
                if($btn.attr("isBind")=="true"){
                    return;
                }
                $btn.click(function(){
                    var $dom = $(this).parent();            
                    if($dom.attr("isExpend")=="true"){
                        FoldPanel.collapse($dom);
                    }else{
                        FoldPanel.expand($dom);
                    }
                });
                $btn.attr("isBind","true");
            }
        },
        /**
         * 展开
         * 
         *     FoldPanel.expand("id");
         *     
         * @method expand
         * @param domId {String} 需要展开的id，在结构中指定的ID
         *      
         * */
        expand:function(domId){
            var $dom = domId;
            if($.isString(domId)){
                $dom = $("#"+domId);
            }
            $dom.children("div:eq(1)").show();
            $dom.attr("isExpend","true");
            
            $dom.find("span.fold-panel-button").addClass("ico-arrow-up").removeClass("ico-arrow-down");
        },
        /**
         * 折叠
         * 
         *     FoldPanel.collapse("id");
         *     
         * @method collapse
         * @param domId {String} 需要折叠的id，在结构中指定的ID
         *  
         * */
        collapse:function(domId){
            var $dom = domId;
            if($.isString(domId)){
                $dom = $("#"+domId);
            }
            $dom.children("div:eq(1)").hide();
            $dom.attr("isExpend","false");
            
            $dom.find("span.fold-panel-button").addClass("ico-arrow-down").removeClass("ico-arrow-up");
        }
    }

    /**
     * 折叠panel portal版本使用
     * 适用HTML结构:
     * 
     *     <div>
     *         <div>标题</div>
     *         <div>内容</div>
     *     </div>
     *     
     * @class Fold
     * */
    var Fold = {
        /**
         * 初始化
         * 
         * @method init
         * @param conf {json} 配置参数
         * @param conf.id {String} 组件id
         * @param conf.listeners {json} 回调事件列表
         * @param conf.listeners.expand {function} 展开事件
         * @param conf.listeners.collapse {function} 折叠事件
         * @param conf.listeners.getParam {function} 动态加载获得加载请求参数
         * 
         * */
        init:function(conf){
            Fold._getTitle(conf.id).bind("click",{conf:conf},Fold._controll).css("cursor","pointer");
        },
        _getTitle:function(id){
            return $("#"+id).children("div:eq(0)");
        },
        _getBody:function(id){
            return $("#"+id).children("div:eq(1)");
        },
        _getArrow:function(id){
            return Fold._getTitle(id).children("span:eq(0)");
        },
        _controll:function(event){
            var conf = event.data.conf || {};
            var listeners = conf.listeners || {};
            var id = conf.id;       
            var $body = Fold._getBody(id);
            if($body.attr("animate")=="true"){
                return;
            }       
            if($body.height() > 0){
                Fold.collapse(id,conf);
            }else{
                Fold.expand(id,conf);
                //if(listeners.expand){
                  //  listeners.expand();
                //}
            }
        },
        _ajaxLoadContent:function($title,$body,conf){
            conf = conf || {};
            var url = $title.attr("url");
            if(url){
                if($body.html() === "" || $title.attr("once") !== "true"){
                    PageCtrl.load({
                        url:url,
                        param:conf.getParam ? conf.getParam() :null,                         
                        dom:$body       
                    });
                }
            }
        },
        isExpandable : function (id) {
            var expandable = Fold._getBody(id).attr("expandable");
            if (undefined === expandable || null === expandable || "" === expandable) {
                return true;
            }else {
                expandable = expandable.toLowerCase();
                return "false" !== expandable;
            }
        },
        setExpandable : function (id, expandable) {
            expandable = !!expandable;
            Fold._getBody(id).attr("expandable", expandable);
            if (!expandable) {
                Fold.collapse(id);
            }
        },
        /**
         * 展开
         * @method expand
         * @param id {String} 组件ID
         * @param [conf] {json} 配置参数
         * @param [conf.getParam] {function} 回调函数，获取ajax的url参数
         * */
        expand:function(id,conf){
            var $body = Fold._getBody(id).show(),
                expandable = $body.attr("expandable"),
                available = $body.attr("available");
            
            if (!Fold.isExpandable(id)) {
                return;
            }
            
            if(conf && conf.listeners && conf.listeners.expand){
                conf.listeners.expand(id);
            }

            $body.attr("animate","true");
            Fold._getArrow(id).removeClass("arrowdown").addClass("arrowup");
            var height = parseInt($body.attr("height"));
            if(!isNaN(height)){
                $body.animate({height:height},function(){
                    $body.removeAttr("animate");
                    if($body.attr("autoHeight")){
                        $body.css("height","auto");
                    }
                   
                });
            }else{
                $body.removeAttr("animate");
            }
            Fold._ajaxLoadContent(Fold._getTitle(id),$body,conf);
        },
        /**
         * 收缩
         * @method collapse
         * @param id {String} 组件ID
         * */
        collapse:function(id,conf){
            var $body = Fold._getBody(id);
            var height = $body.height();
            if(isNaN(height)) return; 
            if(!$body.attr("height")){
                $body.attr("autoHeight","true");
                $body.attr("height",$body.height());
            }                        
            $body.attr("animate","true");
            Fold._getArrow(id).removeClass("arrowup").addClass("arrowdown");
            $body.animate({height:0},function(){
                $body.hide();
                $body.removeAttr("animate");
            });
            if(conf && conf.listeners && conf.listeners.collapse){
                conf.listeners.collapse(id);
            }
        },
        /**
         * 互斥折叠
         * 在标题上的元素中设置other属性，标识与其互斥的panelId,如有多个用逗号分割
         * @method exclusion
         * @param id {string} 
         * @param conf.listeners.expand 展开事件
         * @param conf.listeners.collapse 折叠事件
         * */
        exclusion:function(id,conf){
            Fold._getTitle(id).bind("click",{id:id},function(event){
                var other = $(this).attr("other").split(",");
                
                var $body = Fold._getBody($(this).parent().attr("id"));
                if($body[0].style.display!="none"){
                    return;
                }
                
                Fold.expand($(this).parent().attr("id"),conf);
                for(var i=0;i<other.length;i++){
                    Fold.collapse(other[i],conf);
                }
            });
        },
        /**
         * 异步加载的内容，重新加载
         * @method reloadContent
         * @param id {String} 组件id
         * @param conf {Json} 配置信息
         * @param [conf.getParam] {function} 回调函数，获取ajax的url参数
         * */
        reloadContent:function(id,conf){
            Fold._ajaxLoadContent(Fold._getTitle(id),Fold._getBody(id),conf);
        }
    }


};if (!window.HelpComp) {

/**
 * 帮助信息组件
 * @class HelpComp
 * */
var HelpComp  = {
    /**
     * 初始化
     * 
     *     var $btn1 = button("指定标题");
     *     
     *     $btn1.attr("title", "指定标题");
     *     
     *     HelpComp.init({
     *         id : $btn1,
     *         helpId : 'RIIL_MOTOROOM_MANAGER_ELEMENT',
     *         width : 510,
     *         height : 300
     *     });    
     * 
     * 注意：conf.id指向的dom的title信息作为弹出panel的标题
     * 
     * @method init
     * @param conf {Json} 配置信息
     * @param conf.id {String | dom | jQuery} 需要触发显示帮助信息的元素id,或dom对象
     * @param conf.width {Number} 显示宽度
     * @param conf.height {Number} 显示高度
     * @param conf.helpId {String} 在元素节点上添加helpId，帮助信息ID,帮助系统生成的ID
     */
	init:function(conf){
		var id = conf.id;
		var width = conf.width;
		var height = conf.height;
		var $dom = null;
		if($.isString(id)){
			$dom = $("#"+id);
		}else{
			$dom = $(id);
		}
		$dom.attr("helpId",conf.helpId);
		if(width){
			$dom.attr("width",width);
		}
		if(height){
			$dom.attr("height",height);
		}
		
		$dom.bind("click", {
			width: width,
			height: height,
			helpId: conf.helpId,
			title: $dom.attr("title"),
			scrollH : conf.scrollH
		}, this.showHelp);

		var $body = $(document.body);
		if(!$body.attr("helpAutoHideBind")){
			$body.bind("click",HelpComp.autoHide);
			$body.attr("helpAutoHideBind","true");
		}

	},
	showHelp:function(event){
		event.stopPropagation();
		$("div.pop_help").remove();
		var $help = $(this);
		var helpId = $help.attr("helpId");
		if(document.getElementById(helpId+"_helpId")) return;
		var width = $help.attr("width");
		var height = $help.attr("height");
		HelpComp._createDom(helpId, {
			x: event.pageX,
			y: event.pageY + 50,
			width: width,
			height: height,
			scrollH: event.data.scrollH
		}, event.data.title);
		var local = window.defaultLocale == "en_US" ? "_EN" :"";
		
		PageCtrl.ajax({
			url:ctx+"/static/help/"+ helpId+local+".html",
			dataType:"html",
			success:function(data){
				var $helpDom = $(data);
				var $img = $helpDom.find("img");
				for(var i=0,len = $img.length;i<len;i++){
					var path = $($img[i]).attr("src");
					
					path = path.substring(path.lastIndexOf("/")+1);
					$img[i].src = ctx+"/static/help/images/"+path;
				}				
				$("#"+helpId+"_helpContent").html($helpDom);
			}
		});
	},
	
	/**
	 * 修改helpId
	 * @method setHelpId
	 * @param domId {String | dom | jQuery} 需要触发显示帮助信息的元素id,或dom对象
	 * @param helpId {String} 在元素节点上添加helpId，帮助信息ID,帮助系统生成的ID
	 */
	setHelpId:function(domId,helpId){
		var $dom = null;
		if($.isString(domId)){
			$dom = $("#"+domId);
		}else{
			$dom = $(domId);
		}
		$dom.attr("helpId",helpId);
	},
	
	/**
     * 修改helpId
     * @method setSize
     * @param domId {String | dom | jQuery} 需要触发显示帮助信息的元素id,或dom对象
     * @param width {Number} 显示宽度
     * @param height {Number} 显示高度
     */
	setSize:function(domId,width,height){
		var $dom = null;
		if($.isString(domId)){
			$dom = $("#"+domId);
		}else{
			$dom = $(domId);
		}		
		$dom.attr("width",width);
		$dom.attr("height",height);
	},
	
	/**
     * 隐藏帮助信息
     * @method hideHelp
     */
	hideHelp:function(){
		$("div.pop_help").remove();
	},
	_createDom:function(helpId,layout,title){
		var scrollH = layout.scrollH ? 'overflow-x:auto' : '';
		
		var dom = [];
		dom.push('<div id="'+helpId+'_helpId" style="position: absolute;width:'+layout.width+'px" class="panel pop-blue pop_help">');
		dom.push('	<div class="pop-top-l top-l">');
		dom.push('		<div class="pop-top-r top-r">');
		dom.push('			<div class="pop-top-m top-m">');
		dom.push('				<a class="ico-quit pop-ico pop-ico-quit" onclick="$(\'#'+helpId+'_helpId\').remove()"></a><span class="pop-title">'+(title || window.S_HELP)+'</span>');
		dom.push('			</div>');
		dom.push('		</div>');
		dom.push('	</div>');
		dom.push('	<div class="pop-middle-l middle-l">');
		dom.push('		<div class="pop-middle-r middle-r">');
		dom.push('			<div class="pop-middle-m middle-m">');
		dom.push('              <div class="p-content" style="height:'+layout.height+'px;' + scrollH + '">');
		dom.push('				    <div id="'+helpId+'_helpContent"><div class="loading-min" style="margin-top:'+(layout.height/2-30)+'px;margin-left:'+(layout.width/2-30)+'px;z-index:9999"></div></div>');
		dom.push('              </div>');
		dom.push('			</div>');
		dom.push('		</div>');
		dom.push('	</div>');
		dom.push('	<div class="pop-bottom-l bottom-l">');
		dom.push('		<div class="pop-bottom-r bottom-r">');
		dom.push('			<div class="pop-bottom-m bottom-m"></div>');
		dom.push('		</div>');
		dom.push('	</div>');
		dom.push('</div>');
		
		
		var $body = $(document.body);
		$body.append(dom.join(""));
		
		var $panel = $("#"+helpId+"_helpId");
		
		layout = $.checkDomPosition($panel,layout.x,layout.y);		
		$panel.css({
			"top": layout.y-50,
			"left":layout.x-50,
			"zIndex":ZIndexMgr.get()
		});
		
		
		$panel.find('a.pop-ico-quit').click(function(){
			$(this).unbind();
			ZIndexMgr.free($panel);
			$panel.remove();
		});
		
	},
	autoHide:function(event){
		var $help = $("div[id$='_helpId']");
		var $he = null;
		for(var i=0;i<$help.length;i++){
			$he = $($help[i]);
			if(!$.mouseEventBodyRange($he,event.pageX,event.pageY)){
				HelpComp.hideHelp();
			}
		}
	}
};
/*
 * 	<div class="h-nobg f-right-btn">
						<a class="btn_body" id="cancelButton">
							<div class="btn_l">
								<div class="btn_r">
									<div class="btn_m">
										<span class="ico ico_cancel"></span>
										<span class="text">取消</span>
										<span class="pull"></span>
									</div>
								</div>
							</div>
						</a>	
						<a class="btn_body" id="btn_ok">
							<div class="btn_l">
								<div class="btn_r">
									<div class="btn_m">
										<span class="ico ico_confirm"></span>
										<span class="text">应用</span>
										<span class="pull"></span>
									</div>
								</div>
							</div>
						</a>
				   </div>
 * */

};;		/**
 * 仿苹果复选框设计
 * conf:{
 * 	checked:选中值
 *  unchecked:未选中值
 *  checkedText:选中显示文本
 *  uncheckedText:未选中显示文本
 *  domId:绘制到的目标ID
 * }
 * */			
var iCheckBox = {				
				init:function(conf){
					var $dom = $(this._createDom(conf));
					$("#"+conf.domId).append($dom);
					this._bindEvent($dom, conf.onclick);
				},
				switchCheck:function($dom) {
					$dom.attr('no_message', "true");
					$dom.click();
				},
				_createDom:function(conf){
					var dom = [];
					var ischecked = conf.checked == conf.value;
					dom.push('<div style="margin:auto" compType="icheckbox" class="new_setshow_display" checkedValue="'+conf.checked+'" checkedText="'+conf.checkedText+'" uncheckedText="'+conf.uncheckedText+'" uncheckedValue="'+conf.unchecked+'" value="'+conf.value+'">');
					dom.push('<div itype="textWrapper" style="margin-left:'+(ischecked ? 0:-53)+'px;" class="new_setshow_text">');
					dom.push('<span itype="checktext" style="-moz-user-select:none;" style="visibility:'+(ischecked ? "visible":"hidden")+'" class="new_setshow_textdis">'+conf.checkedText+'</span>');
					dom.push('<span itype="unchecktext" style="-moz-user-select:none;" style="visibility:'+(ischecked ? "hidden":"visible")+'" class="new_setshow_texthide">'+conf.uncheckedText+'</span>');
					dom.push('</div>');
					dom.push('<div itype="backbround" style="margin-right:'+(ischecked ? 0: 60)+'px;" class="new_setshow_hide"></div>');
					dom.push('<div itype="button" style="-moz-user-select:none;margin-left:'+(ischecked ? 53: 0)+'px;" position="'+(ischecked ? "right": "left")+'" class="new_setshow_but"></div>');
					dom.push('</div>');
								
					return dom.join("");
				},
				/**获得移动按钮j元素*/
				_getButton:function($dom){
					return $dom.find("div[itype='button']");
				},
				_getTextWrapper:function($dom){
					return $dom.find("div[itype='textWrapper']");
				},				
				_getChecktext:function($dom){
					return $dom.find("div[itype='checktext']");
				},				
				_getUnChecktext:function($dom){
					return $dom.find("div[itype='unchecktext']");
				},
				_getBackground:function($dom){
					return $dom.find("div[itype='backbround']");
				},
				_bindEvent:function($dom,moveEndCallback){
					iCheckBox._getButton($dom).bind("mousedown",{$dom:$dom,moveEndCallback:moveEndCallback},iCheckBox._mouseDownListener)
					.bind("mouseup",{$dom:$dom,moveEndCallback:moveEndCallback},iCheckBox._mouseUpListeners);
					$dom.bind("click",{$dom:iCheckBox._getButton($dom),moveEndCallback:moveEndCallback},iCheckBox._clickListeners);
					$dom.css("cursor","pointer");
				},
				_clickListeners:function(event){
					//if(iCheckBox.isMove) return;
					var $btn = event.data.$dom;
					if($btn.attr("isMove")){
						return;
					}
					var $dom = $(this);
					var margin = null;
					var moveEndCallback = event.data.moveEndCallback;
					var $textWrapper = iCheckBox._getTextWrapper($dom);
					var $back = iCheckBox._getBackground($dom);
					var $btn = iCheckBox._getButton($dom);						
					if($btn.attr("position")=="left"){
						var interval = setInterval(function(){	
								var btnLeft = parseInt($btn.css("marginLeft"))+2;
								var backRight = parseInt($back.css("marginRight"))+ 2;
								var textLeft = parseInt($textWrapper.css("marginLeft"))+2;
								var margin = iCheckBox._setRight(btnLeft,backRight,textLeft,function($dom){
									if(moveEndCallback && !$dom.attr('no_message')) moveEndCallback($dom);
									else $dom.removeAttr('no_message');
									clearInterval(interval);
								},$dom);	
								$textWrapper.css("marginLeft",margin.textLeft);
								$back.css("marginRight",margin.backRight);
								$btn.css("marginLeft",margin.btnLeft);			
						},10);						
					}else{
						var interval = setInterval(function(){
								var btnLeft = parseInt($btn.css("marginLeft"))-2;
								var backRight = parseInt($back.css("marginRight"))+ 2;
								var textLeft = parseInt($textWrapper.css("marginLeft"))-2;
								var margin = iCheckBox._setLeft(btnLeft,backRight,textLeft,function($dom){
									if(moveEndCallback && !$dom.attr('no_message')) moveEndCallback($dom);
									else $dom.removeAttr('no_message');
									clearInterval(interval);
								},$dom);	
								$textWrapper.css("marginLeft",margin.textLeft);
								$back.css("marginRight",margin.backRight);
								$btn.css("marginLeft",margin.btnLeft);			
						},10);							
					}
				},
				_mouseDownListener:function(event){
					//iCheckBox.isMove = false;				
					var $btn = $(this);
					$btn.removeAttr("isMove");
					
					var $dom = event.data.$dom;
					$dom.attr("flagMove","true")
						.attr("pageX",event.pageX)
						.bind("mousemove",{$dom:$dom,moveEndCallback:event.data.moveEndCallback},iCheckBox._mouseMoveListeners);
					$(document.body).bind("mouseup",{$dom:$dom,moveEndCallback:event.data.moveEndCallback},iCheckBox._mouseUpListeners);
					$(document.body).bind("mousemove",{$dom:$dom,moveEndCallback:event.data.moveEndCallback},iCheckBox._mouseMoveListeners);
				},
				/**移动句柄事件*/
				_mouseMoveListeners:function(event){			
					//iCheckBox.isMove = true;
					var $dom = null;
					if(event && event.data && event.data.$dom){
						$dom = event.data.$dom;
					}
					var moveEndCallback = null;
					if(event.data && event.data.moveEndCallback){
						moveEndCallback = event.data.moveEndCallback;
					}	
					var $btn = iCheckBox._getButton($dom);
					$btn.attr("isMove","true");
					if($dom.attr("flagMove")=="true"){
						var margin = null;
						var $textWrapper = iCheckBox._getTextWrapper($dom);
						var $back = iCheckBox._getBackground($dom);
						var $btn = iCheckBox._getButton($dom);					
						var pageX = event.pageX;
						var lastPageX = parseInt($dom.attr("pageX"));						
						var cutX = pageX - lastPageX;
						if($btn.attr("position")=="left" && cutX > 0){
							var btnLeft = parseInt($btn.css("marginLeft"))+cutX;
							var backRight = parseInt($back.css("marginRight"))+ cutX;
							var textLeft = parseInt($textWrapper.css("marginLeft"))+cutX;
							margin = iCheckBox._setRight(btnLeft,backRight,textLeft,moveEndCallback,$dom);
						}else if($btn.attr("position")=="right" && cutX < 0){
							var btnLeft = parseInt($btn.css("marginLeft"))-Math.abs(cutX);
							var backRight = parseInt($back.css("marginRight"))+Math.abs(cutX);
							var textLeft = parseInt($textWrapper.css("marginLeft"))- Math.abs(cutX);
							margin = iCheckBox._setLeft(btnLeft,backRight,textLeft,moveEndCallback,$dom);
						}
						if(margin){
							$textWrapper.css("marginLeft",margin.textLeft);
							$back.css("marginRight",margin.backRight);
							$btn.css("marginLeft",margin.btnLeft);			
						}
											
					}
					return false;
				},				
				_setRight:function(btnLeft,backRight,textLeft,moveEndCallback,$dom){
					var $btn = iCheckBox._getButton($dom);	
					if(btnLeft>=53){
						btnLeft = 53;
						backRight = 0;
						textLeft = 0;
						$btn.attr("position","right");
						$dom.attr("value",$dom.attr("checkedValue"));
						if(moveEndCallback){
							moveEndCallback($dom);
						}
					}
					var $textWrapper = iCheckBox._getTextWrapper($dom);
					$textWrapper.children("span.new_setshow_textdis").css("visibility","visible");
					$textWrapper.children("span.new_setshow_texthide").css("visibility","hidden");
					
					return {btnLeft:btnLeft,backRight:backRight,textLeft:textLeft};
				},
				_setLeft:function(btnLeft,backRight,textLeft,moveEndCallback,$dom){
					var $btn = iCheckBox._getButton($dom);	
					if(btnLeft<= 0){
						btnLeft = 0;
						backRight = 64;
						textLeft = -53;
						$btn.attr("position","left");
						$dom.attr("value",$dom.attr("uncheckedValue"));
						if(moveEndCallback){
							moveEndCallback($dom);
						}
					}	
					var $textWrapper = iCheckBox._getTextWrapper($dom);		
					$textWrapper.children("span.new_setshow_textdis").css("visibility","hidden");
					$textWrapper.children("span.new_setshow_texthide").css("visibility","visible");		
					return {btnLeft:btnLeft,backRight:backRight,textLeft:textLeft};				
				},
				_mouseUpListeners:function(event){
					var $dom = null;
					if(event && event.data && event.data.$dom){
						$dom = event.data.$dom;
					}
					
					var moveEndCallback = null;
					if(event && event.data && event.data.moveEndCallback){
						moveEndCallback = event.data.moveEndCallback;
					}
					$dom.removeAttr("flagMove");
					$(document.body).unbind("mouseup",iCheckBox._mouseUpListeners);
					$(document.body).unbind("mousemove",iCheckBox._mouseMoveListeners);
					$dom.unbind("mousemove",iCheckBox._mouseMoveListeners);
					
					var $textWrapper = iCheckBox._getTextWrapper($dom);
					var $back = iCheckBox._getBackground($dom);
					var $btn = iCheckBox._getButton($dom);
					var btnLeft = parseInt($btn.css("marginLeft"));
					var margin = null;
					if(53 - btnLeft < 53/2){
						margin = iCheckBox._setRight(53,0,0,moveEndCallback,$dom);	
					}else{
						margin = iCheckBox._setLeft(0,0,0,moveEndCallback,$dom);	
					}
					if(margin){
						$textWrapper.css("marginLeft",margin.textLeft);
						$back.css("marginRight",margin.backRight);
						$btn.css("marginLeft",margin.btnLeft);			
					}					
				}
			};
var Shortcut = {
	scheme:{},//保存快捷键配置
	ctrlKey:0,
	key:0,
	activeKey:0,
	init:function(){
		for(var code in this.code2Text){
			this.text2Code[this.code2Text[code]] = code;
		}
		var self = this;
		$(window.document).bind("keydown",function(event){		
			var keycode = event.keyCode;
			if(Shortcut.activeKey == keycode) return;
			Shortcut.activeKey = keycode;
			if(Shortcut.ctrlCode[keycode]){
				Shortcut.ctrlKey += keycode;
			}else{
				Shortcut.key +=keycode;
			}
			var callback = Shortcut.scheme[Shortcut.ctrlKey+""+Shortcut.key];
			if(callback){
				Shortcut.ctrlKey = 0;
				Shortcut.key = 0;
				callback();
			}
		}).bind("keyup",function(event){
			var keycode = event.keyCode;
			if(Shortcut.ctrlCode[keycode]){
				Shortcut.ctrlKey -= keycode;
				Shortcut.ctrlKey = Shortcut.ctrlKey  < 0 ? 0 : Shortcut.ctrlKey ;
			}else{
				Shortcut.key -=keycode;
				Shortcut.key  = Shortcut.key<0 ? 0 : Shortcut.key;
			}			
		});
	},
	addShort:function(conf){
		var ctrlKeys  = conf.ctrlKey.split(",");
		var ctrlCode = 0;
		for(var i=0;i<ctrlKeys.length;i++){
			ctrlCode += +Shortcut.text2Code[ctrlKeys[i]];
		}
		var keys  = conf.key.split(",");
		var code = 0;
		for(var i=0;i<keys.length;i++){
			code += +Shortcut.text2Code[keys[i]];
		}
		Shortcut.scheme[ctrlCode+""+code] = conf.callback;
	},
	ctrlCode:{
		16 :"Shift",
		17 :"Control",
		18 :"Alt"
	},
	text2Code:{},
	code2Text:{
		8  :"BackSpace",
		9  :"Tab",
		12 :"Clear",
		13 :"Enter",
		16 :"Shift",
		17 :"Control",
		18 :"Alt",
		19 :"Pause",
		20 :"Caps_Lock",
		27 :"Escape",
		32 :"space",
		33 :"Prior",
		34 :"Next",   
		35 :"End",   
		36 :"Home",   
		37 :"Left",   
		38 :"Up",   
		39 :"Right",   
		40 :"Down",   
		41 :"Select",   
		42 :"Print",   
		43 :"Execute",   
		45 :"Insert",   
		46 :"Delete",   
		47 :"Help",   
		48 :"equal",  
		49 :"1",
		50 :"2",
		51 :"3",
		52 :"4",
		53 :"5",
		54 :"6",
		55 :"7",
		56 :"8",
		57 :"9",        
		65 :"a",  
		66 :"b",  
		67 :"c",  
		68 :"d",  
		69 :"e",  
		70 :"f",  
		71 :"g",  
		72 :"h",  
		73 :"i",  
		74 :"j",
		75 :"k",  
		76 :"l",  
		77 :"m",  
		78 :"n",  
		79 :"o",  
		80 :"p",  
		81 :"q",  
		82 :"r",  
		83 :"s",  
		84 :"t",  
		85 :"u",  
		86 :"v",  
		87 :"w",  
		88 :"x",  
		89 :"y",  
		90 :"z",
		97 :'A',	
		98 :'B',	
		99 :'C',	
		100 :'D',	
		101 :'E',	
		102 :'F',	
		103 :'G',	
		104 :'H',	
		105 :'I',	
		106 :'J',	
		107 :'K',	
		108 :'L',	
		109 :'M',	
		110 :'N',	
		111 :'O',	
	    112 :'P',	
		113 :'Q',	
		114 :'R',	
		115 :'S',	
		116 :'T',	
		117 :'U',	
		118 :'V',	
		119 :'W',	
		120 :'X',	
		121 :'Y',
		122 :'Z',		   
		136:"Num_Lock",   
		137:"Scroll_Lock",
		221:"]"
	}
}
;/**
 * 
 * 悬浮层
 * @class Layer
 *  
 */
var Layer = {
    INNER:"_inner",
    BUTTONS:"_inner_buttons",
    showed:{},//已经显示的层
    showedLen : 0,//已经显示层的个数
    haveHide : true,//所有悬浮层是否关闭
    /** * 
     * 初始化
     * 
     *     Layer.init({
     *         id:"id",
     *         width:200
     *         html:"显示内容"
     *         x:23,
     *         y:45,
     *         inithide:true
     *         autoHide:function(){
     *         }
     *     });
     *     
     * @method init
     * @param conf {json} 配置参数
     * @param conf.id {String} dom id
     * @param conf.width {Number} 宽
     * @param conf.html {String} 文本
     * @param conf.okFn {Function} 确定按钮回调，如果不设置则层不显示按钮
     * @param conf.cancelFn {Function} 取消按钮回调
     * @param conf.x {Number} 显示坐标
     * @param conf.y {Number} 显示坐标
     * @param conf.initHide {Boolean} 初始化隐藏
     * @param conf.autoHide {Function} 失去焦点隐藏的回调事件
     * @param conf.isList {Boolean} 层内显示的是否是列表UL结构的
     * */
    init:function(conf){
        
//        if (conf.id.substring(0, 13) === "operationList") {
//            conf.width = 170;
//        }else if (conf.id.substring(0, 13) === "operationTool") {
//            conf.width = 150;
//        }
        
        this._createDom(conf.id,conf.width,conf.height,conf.html,conf.okFn,conf.cancelFn,conf.x,conf.y,conf.remove,conf.initHide,conf.autoHide,conf.isAutoHide,conf.isList,conf.extendWidth,conf.style);
        var $inner = Layer._getInner(conf.id);
        if(conf.url){
            var $children = $inner.children();
            $inner.parent().append($children);
            PageCtrl.load({
                url:conf.url,
                param:conf.data,
                type:"post",
                dataType:"html",
                dom:$inner,                
                callback:function(){
                    var html = $inner.html();
                    if(html  == 'null'){
                        var nodata =  window.S_DATA_EMPTY || "";
                        $inner.html("<div class='nodata-min' style='margin-top: 25%; margin-bottom: 25%;'>"+nodata+"</div>");
                    }else {
                        $inner.append($children);
                    }
                    Layer._justPosition(conf.id,conf.x,conf.y);
                    if(conf.callback){
                        conf.callback();
                    }
                }
            });
        }

        if(conf.apply){
            var $mask = Panel.getUnderMask(conf.id + Layer.INNER);           
            $.getJQueryDom(conf.apply).append($("#" + conf.id)).append($mask);
           
        }
    },
    /*
     * @inner
     * 创建结构内部方法
     * */
    _createDom:function(id,width,height,html,okFn,cancelFn,x,y,isRemove,initHide,autoHide,isAutoHide,isList,extendWidth,style){
        //Layer.close(id);
        height = height ? height+"px" : "auto";
        isList = isList ? "":"layer-middle-content";
        isRemove = isRemove===false ? false : true;
        html = html ? html : "";
        
        if (!document.getElementById(id)) {
            var _okFn = okFn ? function(event){
                var nowTime = new Date().getTime();
                var $btn = $(this);
                var lastTime = $btn.data("clickTime");
                if(lastTime){
                    if(nowTime - lastTime <200){
                        $btn.data("clickTime").data(nowTime);
                        return;
                    }
                }
                okFn(id);
            } : function(event){
                Layer.close(id);
            };
            
            var _cancelFn = cancelFn ? function(event){
                cancelFn(id);
            } : function(event){
                Layer.close(id);
            };
            
            var buttonConf;
            if(okFn){
                buttonConf = [{
                    type: "cancel", click: _cancelFn
                }, {
                    type: "ok", click: _okFn
                }];
            }else {
                buttonConf = [];
            }
            
            
            Panel.htmlShow({
                id : id+Layer.INNER,
                left : x,
                top : y,
                width : width,
                height : height,
		        force : true,
                content : html,
                style : style || "layer",
                mask : false,
                extendWidth : function () {
                    if (undefined !== extendWidth && null !== extendWidth) {
                        return parseInt(extendWidth, 10);
                    }else if ('columnLayer' === id) {
                        return 30;
                    }else {
                        return 0;
                    }
                },
                buttons : buttonConf,
                sizeFix : {width:10, height:10, sideWidth:28},
                initHide : initHide === true
            }).attr("id", id).attr("remove", isRemove).width("auto");
//            $("#"+id+Layer.INNER).html(html);
        }
        
        if(initHide!==true){
            Layer.show(id,{x:x,y:y,autoHide:autoHide,isAutoHide:isAutoHide});
        }
    },
    /*
     * @inner
     * 调整显示位置
     * */
    _justPosition:function(id,x,y,outer){
        var $layer = $("#"+id);        
        var layout = outer ?  $.checkDomRang({ $dom : $layer,x : x ,y : y, outer : outer})  : $.checkDomPosition($layer,x,y);
        Layer.setPosition(id, layout.x, layout.y);
    },
    /**
     * 相对另一个层定位左右，用于多层菜单展示
     * @method relativeJustPositoin
     * @param id {String} 待调整层ID
     * @param relativeId {String} 相对层ID
     * @param layout {Json} 现有布局
     * @param layout.x {Number} 位置
     * @param layout.y {Number} 位置
     * */
    relativeJustPositoin:function(id,relativeId,layout, outer){
        var left = layout.x;
        var $filterLayer = $("#"+id);
        var newPosition = outer ? $.checkDomRang({ $dom : $filterLayer,x : left ,y :layout.y, outer : outer}) : $.checkDomPosition($filterLayer, left, layout.y, $filterLayer.width(), $filterLayer.height());
        left = newPosition.x != left ? left  - $filterLayer.width() - $("#"+relativeId).width() : left;
        Layer._justPosition(id,left,layout.y);
    },
    /*
     * @inner
     * 获得层内部对象
     * @returns {Jquery}
     * */
    _getInner : function(id){
        return $("#"+id+Layer.INNER);
    },
    /**
     * 设置层显示的内容
     * 
     *     Layer.setContent("layerid","dsfafddf");
     *     
     * @method setContent
     * @param id {String} 设定的组件ID
     * @param html {String} 显示的内容
     *     
     * */
    setContent:function(id,html){
//        Layer._getInner(id).append(html);
        Panel.setContent(id+Layer.INNER, html);
    },
    getContent:function(id){
        return Layer._getInner(id).html();
    },
    setPosition: function(id, x, y) {
        Panel.setPosition(id+Layer.INNER, x, y);
    },
    /**
     * 关闭层
     * 
     *     Layer.close("layerid"); 
     *     
     * @method close
     * @param id {String} 层ID
     *     
     * */
    close:function(id){
        var $layerDom = $("#"+id);
        ZIndexMgr.free($layerDom);
        if($layerDom.attr("remove")=="true"){
            Panel.close(id+Layer.INNER);    
        }else{
            Panel.hide(id+Layer.INNER);
        }
        //$(document.body).unbind("click",Layer.autoHide);
        //console.info("closeBefor=="+id+"   =="+Layer.showed[id]+" len = "+Layer.showedLen);
        if(Layer.showed[id]){
            delete Layer.showed[id]; 
            Layer.showedLen = Layer.showedLen  - 1;
        }
        //console.info("closeAfter=="+id+"   =="+Layer.showed[id]+" len = "+Layer.showedLen);

        if(Layer.showedLen == 0){
            //console.info("closeBodyUnbind")
            Layer.unBindBodyClick();    
        }        
    },
    /**
     * 销毁层
     * 
     *     Layer.destory("layerid");
     *     
     * @method destory
     * @param id {String} 层ID
     *     
     * */
    destory:function(id){
        Panel.close(id + Layer.INNER);            
        if(Layer.showed[id]){
            delete Layer.showed[id]; 
            Layer.showedLen = Layer.showedLen  - 1;
        }
        if(Layer.showedLen == 0){            
            Layer.unBindBodyClick();    
        }
    },
    unBindBodyClick:function(){
        var $body = $(document.body);
        $body.removeAttr("clickbody");
        $body.unbind("mousedown",Layer.autoHide);    
    },
    bindBodyClick:function(){
        var $body = $(document.body);
        $body.bind("mousedown",Layer.autoHide);
        $body.attr("clickbody","true");
    },
    /**
     * 显示层
     *     
     *     Layer.show("layerid",{});
     *
     * @method show
     * @param id {String} 层ID
     * @param conf {Json} 配置参数
     * 
     * */    
    show:function(id,conf){
        Panel.htmlShow({
            id : id+Layer.INNER,
            top : conf.y,
            left : conf.x
        });

        Layer._justPosition(id,conf.x,conf.y);
        
        var $body = $(document.body);
        if(conf.isAutoHide!==false && !$body.attr("clickbody")){
            setTimeout(function(){
                Layer.bindBodyClick();
            },10);
        };
        
        if(!Layer.showed[id]){
            Layer.showed[id] = {
                hideCallback:conf.autoHide
            }; 
            //console.info("add=="+id+"  Layer.showed[id]="+Layer.showed[id]);
            Layer.showedLen = Layer.showedLen + 1;
        }
        
//        if($('.tree-wrap')[0]){
//            $("#"+id).width("auto");
//            //$("#"+id+" ul").width("100%");
//            var lis = $("#"+id+" li").children();
//            var maxWidth = 0;
//            var awidth  = 0;
//            for(var i=0,len = lis.length;i < len;i++){
//                awidth = $(lis[i]).css("whiteSpace","nowrap").width();            
//                maxWidth = awidth >  maxWidth ? awidth : maxWidth; 
//            }
//            if(maxWidth < 120){
//                maxWidth = 120;
//            }
//            $("#"+id).width(maxWidth+30);
//        }
    },
    /**
     * 隐藏层
     * 
     * @hide
     * @param id {String} 层ID
     * */
    hide:function(id){
        setTimeout(function(){
            Layer._hide(id);
        },10);
    },
    _hide:function(id){
        Panel.hide(id+Layer.INNER);
        
        if(Layer.showed[id]){
            delete Layer.showed[id]; 
            Layer.showedLen = Layer.showedLen  - 1;
        }
        if(Layer.showedLen == 0){
            //console.info("hideBodUnbind");
            Layer.unBindBodyClick();
        }        
    },
    removeFocusA:function(id){
        $("#"+id+" a[focusa]").remove();
    },
    addFocusA:function(id,blur){
        var $layerDom = $("#"+id);
        blur = blur || "Layer.hide('"+id+"')";
        $a = $('<a href="javascript:void(0)" onblur="'+blur+'" focusa="focusa"></a>');
        $layerDom.append($a);
        $a.focus();
    },
    focusA:function(id){
        $("#"+id+" a[focusa]").focus();
    },
    isInsideLayer: function(id, dom) {
        return $(dom).parents('#' + id).length > 0;
    },
    /*
     * @inner
     * 自动隐藏内部方法
     * */
    autoHide:function(event){
//        if ("BODY" !== event.currentTarget.nodeName) return;
//        if ("INPUT" === event.target.nodeName) return;
        
        if("false"==Layer.haveHide) {
            Layer.haveHide = true;
            return;
        }
        if(Layer.showedLen !=0){
            var tag = true;
            for(var key in Layer.showed){
                tag = Layer.checkHide($("#"+key),event);
                if(!tag) break;
            }
            if(tag){
                var hideCallback = null;
                
                for(var key in Layer.showed){
                    hideCallback = Layer.showed[key].hideCallback;
                    if(hideCallback){
                        if(hideCallback(event) === false){  //隐藏前回调，如果返回false，则表示不能隐藏层
                            tag = false;
                            break;
                        }
                    } 
                }
                if(tag){
                    for(var key in Layer.showed){
                        if (!Layer.isInsideLayer(key, event.target)) {
                            Layer.close(key);
                        }
                    }                
                }
            }
        }else{
            var $body = $(document.body);
            $body.unbind("click",Layer.autoHide);    
            $body.removeAttr("clickbody");
        }
    },
    /**
     * 检查点击页面的坐标似乎否在制定层上
     * @method checkHide
     * @return {Boolean} false：点在层上不需要隐藏，true没有点在层上隐藏
     * */
    checkHide:function($layer,event){
        if($layer[0]){
            var x = event.pageX,y = event.pageY;
            var offset = $layer.offset();
            if (x > offset.left && x < offset.left + $layer.width() && y > offset.top && y < offset.top + $layer.height()) {
                return false;
            } else {
                return true;
            }    
        }        
    }
};
;var LayoutMgr = {};

/**
 * portlet框架自适应
 * @param dom portlet外层dom对象，可以是id或者是dom
 * */
LayoutMgr.portletLayout = function(dom){
	var $portlet = $.getJQueryDom(dom);
	var $portletComp  = $portlet.children();//portlet三部分组成结构
	
	var topHeight = $($portletComp[0]).height();
	var bottomHeight = $($portletComp[2]).height();
	
	
	$($portletComp[1]).children().children().height($portlet.parent().height() - topHeight - bottomHeight-1);
	
	
	//var portletHeight = $portlet.parent().height();
	//var portletComponent = $portlet.children(); 
	//var $portletMInner = $(portletComponent[1]).children().children();
	//$portletMInner.height(portletHeight - $(portletComponent[0]).height() - $(portletComponent[2]).height()-2 - $.getVertical($portletMInner,"padding"));
}



;/**
 * 页面左侧导航
 */
var LeftNavigation = function(domId,listeners){
	this.domId = domId;
	this.listeners = listeners;
	this.lastExpand = null;
	var self = this;
	$("#"+domId+" .h-subnav").click(function(){
		var $firstBtn = $(this);
		if(!$firstBtn.next()[0]){ //如果没有二级
			var result = self.listeners($(this));
			if(result === false){
				return;
			}

			$("#"+domId+">ul>li").removeClass("open");
			$(this).parent().addClass("open");
			
		}else{
			self.expand(this);
		}
	});
	//var items = $("#"+domId+">ul>li");
	//var $item = $(items[0]);
	//this.height = $("#"+domId).parent().height()- ( $item.height())*items.length-10;
	$("#"+domId+" .subnav-sub-set-ul>li").bind("click",{self:this},LeftNavigation.itemClickHandler);
	$("#"+domId+" .h-subnav:first").click();
	//this.expand($("#"+domId+" .h-subnav:first"));
}
LeftNavigation.prototype = {
	constructor:LeftNavigation,
	expand:function(obj){		
		var self = this;
		if(this.lastExpand == obj){
			return;
		}
		var $obj = $.isString(obj) ? $("#menu_"+obj) : $(obj);
		
		var $next  = $obj.next();
		var $nextChild = $next.children();
		
		if(this.lastExpand){
			this.collapse(this.lastExpand);
		}

		$next.animate({height:parseInt($("#"+this.domId).attr("itemHeight"))},function(){
			if(self.listeners && self.listeners.expand){
				self.listeners.expand($obj);
			}
		});
		$obj.parent().addClass("open");
		this.lastExpand = obj;
		$next.children().children("li:eq(0)").click();
		
	},
	collapse:function(obj){
		var $obj = $.isString(obj) ? $("#menu_"+obj) : $(obj);
		$obj.next().animate({height:0});
		//$obj.next().css({height:0});
		$obj.parent().removeClass("open");
	}
}
LeftNavigation.itemClickHandler = function(event){
	var self = event.data.self;
	if(self.lastItem){
		self.lastItem.removeClass("on");
	}
	var $item = $(this).addClass("on");
	self.lastItem = $item;
	if(self.listeners){
		self.listeners($(this));
	}		
};/**
 * 多级菜单
 * 
 *     conf{
 *         id:菜单ID
 *         x,y坐标
 *         click：点击菜单项事件（$li）
 *     }
 *     
 * @class LevelMenu
 * */
var LevelMenu = {
    menuIds : [],
    
    /**
     * 初始化
     * 
     *      LevelMenu.init({
     *          id : "operationList",
     *          targetDom : $("body"),
     *          click : function ($li) {
     *              alert($li);
     *          }
     *      });
     * 
     * @method init
     * @param conf {Json} 配置信息
     * @param conf.id {String} 菜单DOM ID
     * @param conf.x {Number} X坐标
     * @param conf.y {Number} Y坐标
     * @param conf.click {Funtion} 点击菜单项的事件
     */
    init:function(conf){
        clearTimeout(this.timeout);
        LevelMenu._hide();
        conf.callback,conf.position;
        var id = conf.id+"_layer";
        if(!document.getElementById(id)){
            Layer.init({
                id:id,
                html:$("#"+conf.id).show(),
                x:conf.x || 0,
                y:conf.y|| 0,
                initHide:false,
                remove:false
            });
//            Layer.setContent(id,$("#"+conf.id).show());
            $("#"+conf.id+" li:not([childId])").bind("click",{id:conf.id,conf:conf},LevelMenu.menuItemClick);
            $("#"+conf.id+" li").bind("mouseover",{conf:conf,parentId:id},LevelMenu.showChildMenu);
        }   

        var x,y;
        if(conf.targetDom){
             var layout = $.getElementAbsolutePosition(conf.targetDom);
             var $layerDom = $("#"+id);
             var newLayout = $.checkDomPosition($layerDom,layout.x,layout.y);
             x = layout.x != newLayout.x ? layout.x - $layerDom.width() :   layout.x; 
             y = layout.y != newLayout.y ? layout.y - $layerDom.height() :   layout.y; 
        }else{
            x = conf.x;
            y = conf.y;
        }
        
        Layer.show(id,{x:x,y:y,isAutoHide:true});
        Layer.focusA(id);
        LevelMenu.menuIds.push(id);
    },
    menuItemClick:function(event){
        var conf = event.data.conf;
        var id = event.data.id;
        Layer.close(id+"_layer");
        if(conf.click){
            conf.click($(this));
        }
        if(event.data.parentId){
            Layer.close(event.data.parentId);
        }
    },
    hide:function(){
        if(!LevelMenu.menuIds) return;
        this.timeout = setTimeout(LevelMenu._hide,800);
    },
    _hide:function(){
        for(var i=0;i<LevelMenu.menuIds.length;i++){
            Layer.close(LevelMenu.menuIds[i]);
        }
        LevelMenu.menuIds = [];     
    },
    showChildMenu:function(event){
        
        var $li = $(this);
        var conf = event.data.conf;
        var liOffset = $li.offset();
        var childId = $li.attr("childId");
        if(childId){
            var id = childId+"_layer";
            if(!document.getElementById(id)){
                Layer.init({
                    id:id,
                    html:$("#"+childId).show(),
                    x:liOffset.left,
                    y:liOffset.top,
                    initHide:true,
                    remove:false,
                    autoHide:false
                });
//                Layer.setContent(id,$("#"+childId).show());
                conf.id = childId;
                $("#"+id+" li").bind("click",{id:childId,conf:conf,parentId:event.data.parentId},LevelMenu.menuItemClick).bind("mouseover",{conf:conf},LevelMenu.showChildMenu);
            }
            LevelMenu.menuIds.push(id);
            Layer.show(id,{x:liOffset.left,y:liOffset.top,isAutoHide:false});
            var childLeft = liOffset.left+$li.width();
            var childPosition = $.checkDomPosition($("#"+id),liOffset.left+$li.width());
            if(childLeft!=childPosition){
                childLeft = childLeft - $("#"+id).width();
            }
            Layer._justPosition(id,liOffset.left-$("#"+id).width(),liOffset.top);
            
        }
        var $lis =  $li.parent().children();
        $lis.children().removeClass("on");
        $li.children().addClass("on");
        var $tempLi = null;
        for(var i=0,len = $lis.length;i<len;i++){
            $tempLi = $($lis[i]);
            if($tempLi.attr("childId")!= childId && document.getElementById($tempLi.attr("childId")+"_layer")){
                Layer.close($tempLi.attr("childId")+"_layer");
            }
        }
    }
};/**
 * 列表菜单通用组件
 * 使用举例：
 *
 *     <ul id="listMenu1">
 *         <li class="on"><a>选项1（默认选中）</a><li>
 *         <li><a>选项2</a></li>
 *         <li><a>选项3</a></li>
 *         <li><a>选项4</a></li>
 *         <li><a>选项5</a></li>
 *     </ul>
 *     <script type="text/javascript">
 *     ListMenu.init("listMenu1", {
 *         click: function(itemId, $itemLi) {
 *             alert($itemLi.text() + '被选中，它的ID为"' + itemId + '"');
 *         },
 *         clickBefore: function(itemId, $itemLi) {
 *             alert($itemLi.text() + '将要被选中，它的ID为"' + itemId + '"');
 *             return true; // false时不触发click事件
 *         }
 *     });
 *     </script>
 * 
 * @class ListMenu
 */
var ListMenu = {
	/**
	 * 初始化
     * @method init
	 * @param id {String} 组件ID
	 * @param listeners {json} 事件监听<br>
     * @param listeners.click 点击菜单项事件回调（参数是被选想的ID和li的jquery对象）
     * @param listeners.clickBefore 点击菜单项触发click之前事件回调，如果返回false,则不触发click（参数是被选想的ID和li的jquery对象）
     * 
	 *     
	 * */
	init:function(id,listeners){		
		$.getJQueryDom(id).bind("click",{listeners:listeners},ListMenu._itemClick);
	},
	/*
	 * 选项点击事件回调  
     * @method _itemClick
     * @private
     * @param event {jqueryEvent}
	 */
	_itemClick:function(event){

		var listeners = event.data.listeners;
		var tagName = event.target.tagName.toLowerCase();
		//如果点击的是li，或者点击a的时候，获得a的父节点
		var $target = $(event.target).parentsUntil("li");
		var $li = null;
		if($target[0]){
			$li = $($target[$target.length - 1]).parent();
		}else{
			$li = $(event.target).parent();
		}
		if(tagName == "li"){
			$li = $(event.target);
		}
		//var $li = tagName == "li" ? $(event.target) : tagName == "a" ? $(event.target).parent() : null;

		if($li){			
			var flag = true;
			if(listeners.clickBefore){
				flag = listeners.clickBefore($li.attr("id"),$li);
			}
			if(flag){
				$li.parent().children("li").removeClass("on");
				$li.addClass("on");	
			}else{
				return;
			}
		
			if(listeners.click){
				listeners.click($li.attr("id"),$li);
			}

		}



	},
	/**
	 * 获得当前选中项
     * @method getCurrentItem
     * @param id {String} 组件ID
     * @return {jquery} 当前选中项的li标签的jquery对象
	 */
	getCurrentItem:function(id){
		return $.getJQueryDom(id).children("li.on");
	},
	/**
	 * 是否有列表项,是否为空
     * @method isHasItem
	 * @param id 组件ID
	 * @return {Number|false} 如果有列表项则返回列表项个数，否则返回false
	 * */
	isHasItem:function(id){
		return $.getJQueryDom(id).children("li").length || false;
	},
	/**
	 * 清除所有选中项
     * @method clearAllCurrent
     * @param id 组件ID
	 */
	clearAllCurrent:function(id){
		$.getJQueryDom(id).children("li").removeClass("on");
	},
	/**
	 * 触发某个选项点击事件
     * @method clickItem
	 * @param id 列表菜单ID
	 * @param index 列表项索引
	 * */
	clickItem:function(id,index){
		$.getJQueryDom(id).children("li:eq("+index+")").click();	
	},
	/**
	 * 触发某个选项点击事件
     * @method clickItemById
	 * @param id 组件ID
	 * */
	clickItemById:function(id){
		$.getJQueryDom(id).click();
	},
	/**
     * 获得某索引选项的id
	 */
	getItemIdByIndex : function(id, index){
		return $.getJQueryDom(id).children("li:eq("+index+")").attr("id");
	},
	/**
      * 更改某个ID的文本
 	  */
	setItemTextById:function(id, itemId, text){
		this._setItemText($.getJQueryDom(id).children('[id="'+itemId+'"]'), text);
	},
	_setItemText : function($item, text){
		$item.children("a").html(text).attr("title",text);
		$item.attr("title", text);
	},
	/**
     * 添加选项
     * @method addItem
     * @param id {String} 组件ID
     * @param itemDom {String|html|jquery} 新选项结构
	 */
	addItem : function(id,itemDom){
		$.getJQueryDom(id).append(itemDom);
	},
	refreshItem : function(id, data){
		var dom = [];
		for(var i = 0; i<data.length;i++){
			dom.push(this.getItemTemplate(data[i]));
		}
		var $ul = $.getJQueryDom(id);
		$ul.children().unbind();
		$ul.html(dom.join(""));		
	},
	/**
     * 获得选项的dom结构
	 */
	getItemTemplate : function(conf){
		var dom = [];
		dom.push('<li class="secondli" id="' + conf.id + '">');
		dom.push('<a href="javascript:void(0)" title="' + conf.name + '">' + conf.name + '</a>');
		dom.push('</li>');
		return dom.join("");
	},
    /**
     * 删除选项
     * @method delItem
     * @param id {String} 组件ID
     * @param index {Number} 要删除选项的索引号
     */
	delItem : function(id,index){
		$.getJQueryDom(id).children(":eq("+index+")").remove();
	},
    /**
     * 批量删除选项
     * @method delItemByFlag
     * @param id {String} 组件ID
     * @param flag {jquery Selector} jquery选择器
     */
	delItemByFlag : function(id,flag){
		var $li = $(flag).parentsUntil("li").parent();
		if($li.length ==0){
			$li = $(flag).parent();
		}
		var li = $li[$li.length -1];
		var $ul = $("#"+id);
		var index = $ul.children().index(li);
		$.getJQueryDom(id).children(":eq("+index+")").remove();
	}
};/**
 * 列表
 * 
 * @class ListPanel
 * 
 * */
var ListPanel = function(conf){
	this.data = null;
}
ListPanel.prototype = {
	constructor:ListPanel,
    init: function(conf) {
		this.data = conf.data;
		this.renderType = conf.renderType;
		this.listeners = conf.listeners;
		this.conf = conf;
        return this;
    },
    render: function(conf) {	
		this.renderDom(conf);
        return this;
    },
    bindEvent: function(conf,$item) {
		var plugin = ListPanel.plugin[this.renderType];	
		
		if(plugin){
			var arg = null;			
			if(arguments.length>1){
				arg = arguments[1];				
			}
			data = plugin.bindEvent.call(this,arg);
		}
    },
    /**
     * 更新列表项内容
     * @method update
     * @param data {Array}
     * */
	update:function(data){
		ListPanel.template.create.call(this,data);
		this.bindEvent(this.conf);
		this.data = data;
	},
	/**
	 * 添加一项
	 * @method addItem
	 * @param obj {JSON}
	 * */
	addItem:function(obj){
		this.data.push(obj);
		var $item = ListPanel.template.addItem.call(this,obj);
		this.bindEvent("",$item);
	},
	/**
	 * 删除项
	 * @method delItem
	 * @param index {Number} 索引号
	 * */
	delItem:function(index){
		var $cItems = null;
		if(!index){
			var chooseIndexs = [];
			var data = [];
			for(var index  in this.chooseIndexs){
				if (this.chooseIndexs[index]) chooseIndexs.push(index);
			}
			if(chooseIndexs.length==0) return 0;
			$cItems = this.getChooseItems(chooseIndexs);		
			
			for(var i=0,len = this.data.length; i<len; i++){
				if(this.chooseIndexs[i]){
					delete this.chooseIndexs[i];
				}else{
					data.push(this.data[i]);
				}
			}
			this.data = data;
		}else{
			$cItems.push(this.getItemByIndex(index));
		}
		return ListPanel.template.delItemDom.call(this,$cItems);
	},
	/*
	 * 
	 * */
	deleteAll:function(){
		ListPanel.template.deleteAll.call(this);
		this.data = [];
	},
	/**
	 * 获得选中项的值
	 * @method getChooseVal
	 * @returns {Array}
	 * */
	getChooseVal:function(){
		var plugin = ListPanel.plugin[this.renderType];			
		if(plugin){
			return  plugin.getValue.call(this);
		}	
		return data;
	},
	/**
	 * 控制显示指定项目
	 * @method ctrlItems
	 * @param propName {String} 根据该属性的值确定隐藏某项
	 * @param datas {Array} 需要隐藏或显示的集合
	 */
	ctrlItems:function(propName,datas){
		var dateItem = null;		
		for(var i=0, len= this.data.length;i<len;i++){
			dateItem = this.data[i];
			this.getItemByIndex(i).hide();
			for(var j=0,jlen = datas.length;j<jlen;j++){
				if(dateItem[propName]==datas[j][propName]){
					this.getItemByIndex(i).show();
				}
			}
		}
	},
	setChooseByValue:function(value){
		var plugin = ListPanel.plugin[this.renderType];			
		if(plugin){
			return  plugin.setChooseByValue.call(this,value);
		}	
	}
	
}

if(!ListPanel.render){
	ListPanel.render = {};
}


ListPanel.render.server2HTML = {
	renderDom:function(conf){
		this.domId = conf.domId;
		var $dom = this.getDom(this.domId);
		this.renderType = $dom.attr("rendertype");		
		
		this.valuefield = $dom.attr("valuefield");
		this.textfield = $dom.attr("textfield");
				
		this.data = [];//��ʼ���ڲ�data
		var $lis = this.getItems();

		for(var i=0,len = $lis.length; i<len ;i++){
			var $li = $($lis[i]);
			var itemData =  new Function("return "+$li.attr("data")+";")();
			this.data.push(itemData);
		}
		return this;
	},
	getItems:function(){
		return this.getDom().children();
	},
	getDom:function(){
		return $("#"+this.domId);
	},
	getChecks:function(){
		return $("#"+this.domId).find("input:checkbox");
	},
	getCheckeds:function(){
		return this.getChecks().filter(":checked");
	},
	getItemByIndex:function(index){
		return this.getItems().filter(":eq("+index+")");
	},
	getChooseItems:function(indexs){
		var chooses  = [];
		for(var i=0,len = indexs.length;i<len;i++){
			chooses.push(this.getItemByIndex(indexs[i]));
		}
		return chooses;
	},
	getChooseItem:function(){
		return this.getItems().filter(".on");
	}
}

ListPanel.template = {
	createLine:function(data,rowData){
		var plugin = ListPanel.plugin[this.renderType];	
		if(plugin){
			data = plugin.render.call(this,data,rowData);
		}
		return "<li>"+data+"</li>";
	},
	create:function(data){
		var lis = [];
		var $dom  = this.getDom();
		$dom.find("*").unbind().remove();	
		
		for(var i=0,len = data.length;i<len;i++){
			lis.push(ListPanel.template.createLine.call(this,data[i][this.textfield],data[i]));
		}
		$dom.html(lis.join(""));
	},
	addItem:function(obj){
		var $li = $(ListPanel.template.createLine.call(this,obj[this.textfield]));
		this.getDom().append($li);
		return $li;
	},
	delItemDom:function($items){
		for(var i= 0,len = $items.length;i<len;i++){
			$items[i].unbind().find("*").unbind();
			$items[i].remove();				
		}
		return i;
	},
	delItem:function(item){
		item = $(item);
		item.unbind().find("*").unbind();
		item.remove();
	},
	deleteAll:function(){
		var items = this.getItems();
		for(var i=0,len = items.length;i<len;i++){
			ListPanel.template.delItem(items[i]);
		}
	}
}

ListPanel.plugin = {
	checkbox:{
		render:function(text){
			return '<label><input type="checkbox"/>'+text+'</label>';
		},
		bindEvent:function(conf){
			var listeners = this.listeners;
			if(!$items){
				var $items = this.getItems();			
			}
			$items.find("input:checkbox").bind("click",{self:this},function(event){
				var self = event.data.self;
				var $lis = self.getItems();							
				var index = $lis.index(this.parentNode.parentNode);
				if(!self.chooseIndexs){
					self.chooseIndexs = {};
				}
				if(self.chooseIndexs[index]){
					self.chooseIndexs[index] = null;
				}else{
					self.chooseIndexs[index] = self.data[index];
				}	
				if(listeners && listeners.click){
					listeners.click($li,self.data[index]);
				}
			});			
		},
		getValue:function(){
			var $checks =this.getCheckeds();
			var values = [];
			var $lis = this.getItems();		
			for(var i=0;i<$checks.length;i++){
				var li = $checks[i].parentNode.parentNode;
				var index = $lis.index(li);
				values.push(this.data[index]);
			}
			return values;		
		},
		setChooseByValue:function(value){
			for(var i=0,len = this.data.length;i<len;i++){
				if(value == this.data[i][this.valuefield]){
					this.getChecks().filter(":eq("+i+")").attr("checked","checked");
				}
			}
		}
	},
	choose:{
		render:function(text){
			return '<a href="javascript:void(0)">'+text+'<span class="<%=ico%>"></span></a>';
		},
		bindEvent:function($items){		
			var listeners = this.listeners;
			if(!$items){
				var $items = this.getItems();			
			}			
			$items.bind("click",{self:this},function(event){
				
				var self = event.data.self;
				var $lis = self.getItems();
				var $li = $(this);				
				var index = $lis.index(this);
				
				if(!self.chooseIndexs){
					self.chooseIndexs = {};
				}	
				
				if(self.conf.singleSelect){
					self.chooseIndexs = {};
					$lis.removeClass("on");
					$li.addClass("on");					
					self.chooseIndexs[index] = self.data[index];
				}else if(self.chooseIndexs[index]){
					$li.removeClass("on");
					self.chooseIndexs[index] = null;
				}else{
					$li.addClass("on");					
					self.chooseIndexs[index] = self.data[index];
				}
				
				
				
				
				if(listeners && listeners.click){
					listeners.click($li,self.data[index]);
				}else{
				
				}
			});
		},
		getValue:function(){		
			var values = [];
			for(var index in this.chooseIndexs){			
				if (this.chooseIndexs[index]) values.push(this.chooseIndexs[index]);
			}
			return values;
		},
		setChooseByValue:function(){
			
		}
	}
}
	
CompMgr.regComp("ListPanel",ListPanel);;if (!window.Loading) {
    
/**
 * 页面加载Loading
 * @class Loading
 */
var Loading = function(){
    // 只在最外层展示
    if(!document.getElementById("contentIframe") && $.getParentWindow() !== window){
        if($.getParentWindow().Loading){
            return $.getParentWindow().Loading;
        }
    }
    
    var S_ID = 'Loading';
    
    /**
     * 载入动画
     * @property _$loading
     * @private
     * @default null
     * @type {jquery}
     */
    var _$loading = null;
    
    /**
     * 动画的宽度
     * @property S_MOVIE_WIDTH_i
     * @private
     * @final
     * @default 100
     * @type {number}
     */
    var S_MOVIE_WIDTH_i = 100;
    
    /**
     * 动画的高度
     * @property S_MOVIE_HEIGHT_i
     * @private
     * @final
     * @default 100
     * @type {number}
     */
    var S_MOVIE_HEIGHT_i = 100;
    
    var S_API_EXPORT = {
        /**
         * 是否正在展示载入动画
         * @property useing
         * @default false
         * @type {boolean}
         */
        useing: false,
        
        /**
         * 开始loading
         * 
         *     // 指定展示文本
         *     Loading.start("正在载入<br>XXX数据...");
         *     
         *     // 不指定展示文本
         *     Loading.start();
         * 
         * @method start
         * @param [text] {string} html文本，展示在动画旁边
         */
        start : start_fn,
        
        /**
         * 结束loading
         * 
         *     Loading.stop();
         *     
         * @method stop
         */
        stop : stop_fn,
        
        /**
         * 当Loading已经显示时，改变Loading的文字
         * 
         *     // 修改展示内容的html
         *     Loading.text("XXX数据载入完成，正在优化...");
         *     
         *     // 获得展示内容的html
         *     var html = Loading.text();
         * 
         * @method text
         * @param text {string} html 文本
         * @return 展示文字的html
         */
        text : text_fn
    };
    
    return S_API_EXPORT;
    
    /**
     * 开始loading
     * @method start_fn
     * @private
     * @param [text] {string} 文字信息，展示在动画旁边
     */
    function start_fn (text) {
        if (!_$loading) {
            RiilMask.mask(S_ID, null, false, true);
            _$loading = render_fn(text);
            
            S_API_EXPORT.useing = true;
        }else if (text){
            text_fn(text);
        }
    };
    
    /**
     * 绘制一个载入动画
     * @method render_fn
     * @private
     * @param [text] {string} 文字信息，展示在动画旁边
     * @return {jquery} 载入动画对象
     */
    function render_fn(text) {
        return RiilMask.setText(S_ID, text || window.S_LOADING || '!!S_LOADING!!');
    }
    
    /**
     * 结束loading
     * @method stop_fn
     * @private
     */
    function stop_fn () {
        if (!_$loading) return;
                
        ZIndexMgr.free(_$loading);
        
        RiilMask.unmask(S_ID);
        
        _$loading = null;
        
        S_API_EXPORT.useing = false;
    }
    
    
    /**
     * 当Loading已经显示时，改变Loading的文字
     * @method text_fn
     * @private
     * @param text {string} 文字
     * @return 展示文字的html
     */
    function text_fn (text) {
        if (!_$loading) return;
        
        if (text) {
            _$loading.html(text);
            
        } else {
            return _$loading.html();
        }
    }
}();


};/**
 * 菜单
 * @class Menu
 * */
var Menu = {
	/**
	 * 初始化
	 *
	 *     Menu.init({
	 *			id:"menu1",
	 *			items:[  // 菜单项
	 *              {id:"2323",text:"aaaaaa",icon:"图标样式"},
	 *              {id:"bbbbb",text:"bbbbbb",icon:"图标样式"}
	 *          ],
	 *			click:function(id){// 点击事件
	 *				alert(id);
	 *			},
	 *			
	 *	        x:x,y:y // 坐标
	 *     });
	 *     
	 * @method init
	 * @param conf 配置参数
	 * @param conf.id {String} 组件id
	 * @param conf.items {Array} [{id:"项id",name:"项文字"，icon:"项图标样式"}]菜单项
	 * @param conf.click {Function} 点击项事件
	 * @param conf.x {Number} x坐标
	 * @param conf.y {Number} y坐标
	 * */
	init:function(conf){
		var menuItems = ['<ul class="layer-menu">'];
		var item = null;
		for(var i=0;i<conf.items.length;i++){
			item = conf.items[i];
			menuItems.push('<li id="'+item.id+'"><a><img class="'+item.icon+'" src="'+ctx+'/static/images/comm/window/ex.png"><span>'+item.text+'</span></a></li>');
		}

		menuItems.push("</ul>");
		Layer.init({
			id:conf.id,
			html:menuItems.join(""),
			initHide:true,
			remove:true
		});
		$("#"+conf.id+" li").bind("click",{id:conf.id,click:conf.click},Menu._itemClick);
		Layer.show(conf.id,{x:conf.x,y:conf.y});
		$("*").unbind("scroll",Menu.bindScroll).bind("scroll", {id : conf.id}, Menu.bindScroll);
	},
	/*
	 * 点击项事件回调
	 * */
	_itemClick:function(event){
		var click = event.data.click;
		click(this.id);
		Layer.close(event.data.id);
		$("*").unbind("scroll", Menu.bindScroll);
	},
	bindScroll :　function(event){

		if(document.getElementById(event.data.id)){
			Layer.close(event.data.id);	
		}		
		$("*").unbind("scroll", Menu.bindScroll);
	}
};///////////////////////////////////////////////////////////////////////////////////////
//使用说明：
//1.引入样式表
//	  <script type="text/javascript" src="js/menu.js"></script>
//    <link rel="stylesheet" type="text/css" href="css/widget.css">
//    <link rel="stylesheet" type="text/css" href="css/comm.css">
//		  var menu = CompMgr.getComp({
//            compID: "MenuPanel",
//            renderName: "js2HTML",
//            domId: "abc",
//            handler: function(id){点击菜单项回调，
//			}
//        }, false, true);
//		添加菜单项
//		menu.addMenuItem([{text:"添加挺假添加挺假添加挺假添加挺假添加挺假添加挺假添加挺假添加挺假添加挺假添加挺假添加挺假",id:"asdf",icon:"ico-lock"}]);
//	    显示菜单
//      menu.show();
//3.构建示例如下，
//		var calendar = new MonthCalendar("calendar","2,27");
//4.菜单项配置参数：
//  text:显示文本
//  id:id标识
//  icon:图标样式
//5.属性：
// param:使用者自定义参数值
//6.注意：尽可能只获得一个menu对象，show()，如果需要更改菜单中的项，调用addMenuItem，会将之前的菜单项清除，替换为新的菜单项。 
//
////////////////////////////////////////////////////////////////////////////////////////
var MenuPanel = function(conf) {
	this.param  = null;
}
MenuPanel.prototype = {
    constructor: MenuPanel,
    init: function(conf) {
        this.conf = conf;
        return this;
    },
    render: function(conf) {
        this.renderDom(conf);
        return this;
    },
    bindEvent: function(conf) {
        $("#" + conf.domId + " li").bind("click", {
            self: this
        },
        function(event) {
            if (conf.handler) {
                var self = event.data.self;
                self.hide();
                conf.handler(this.attributes["menuItemId"].value);
            }
        });
        return this;
    }
};

if (!MenuPanel.render) {
    MenuPanel.render = {};
};

/**
	 * JS生成HTML组件方式
	 * @param {Object} conf
	 */
MenuPanel.render.js2HTML = {
    /**
	 * JS生成菜单外框架
	*/
    renderDom: function(conf) {
        this.domId = conf.domId;

        var layer = $("#" + this.domId+"_ul");
        if (layer[0]) {
        	layer.find("*").unbind().remove();
        } else {
        	layer = [];
    		layer.push('<div class="layer" style="position:absolute;display:none" id="'+this.domId+'"><div class="layer-top-l"><div class="layer-top-r"><div class="layer-top-m"></div></div></div>');
    		layer.push('<div class="layer-middle-l"><div class="layer-middle-r"><div class="layer-middle-m" >');
    		layer.push('<ul id="' + conf.domId + '_ul" class="layer-menu"></ul>');
            layer.push('</div></div></div>');
            layer.push('<div class="layer-bottom-l"><div class="layer-bottom-r"><div class="layer-bottom-m"></div></div></div>');
            layer.push('</div>');
            
            $(document.body).append(layer.join(""));
        }
        this.addMenuItem(conf.items);

        return this;
    },
	/**
	 *添加菜单项
	 *@param items Array[{}]
	*/
    addMenuItem: function(items) {
        if (!items) return;
        if (!$.isArray(items)) {
            items = [items];
        }
        var $item = null;
        var $menuDom = $("#" + this.domId+"_ul");
        $menuDom.children().unbind().remove();
        for (var i = 0, len = items.length; i < len; i++) {
            var item = items[i];
            var $li = $('<li menuItemId="' + item.id + '" ><a href="javascript:void(0)"><img class="' + item.icon + '" src="'+ctx+'/static/images/comm/window/ex.png"><span>' + item.text + '</span></a></li>').click(function() {
                if (items.handler) {
                    items.handler();
                }
            });
            
           
            $menuDom.append($li);
        }
       
        this.bindEvent(this.conf);
    },
	/**
	 *显示菜单
	 *@param position 菜单需要显示的位置坐标｛x:,y:｝
	*/
    show: function(position) {
        var $dom = $("#" + this.domId).show();
        var x = position.x;
        var y = position.y;
        var height = $dom.height();
        var width = $dom.width();
        var scrollWidth = document.body.scrollWidth;
        var scrollHeight = document.body.scrollHeight;
        var clientWidth = document.body.clientWidth;
        var clientHeight = document.body.clientHeight;
        var scrollTop = document.body.scrollTop;
        var scrollLeft = document.body.scrollLeft;

        var windowInnerY = clientHeight - (y - scrollTop);
        var checkY = windowInnerY - height;
        y = checkY < 0 ? y + checkY - 2 : y;
        var windowInnerX = clientWidth - (x - scrollLeft);
        var checkX = windowInnerX - width;
        x = checkX < 0 ? x + checkX - 2 : x;

        $dom.css({
            left: x,
            top: y
        });
        
        var self = this;
        
        setTimeout(function(){
        	$(document.body).bind("mousedown", {
        		$dom: $dom,
        		self: self
        	},
        	self.autoHide);
        	
        },100);
        this.showTime = new Date().getTime();
        
		if($("#"+this.domId+" ul")[0]){
			
			$("#"+this.domId).width("auto");
			$("#"+this.domId+" ul").width("100%");
			var lis = $("#"+this.domId+" li").children();
			var maxWidth = 0;
			var awidth  = 0;
			for(var i=0,len = lis.length;i < len;i++){
				awidth = $(lis[i]).css("whiteSpace","nowrap").width();    		
				maxWidth = awidth >  maxWidth ? awidth : maxWidth; 
			}
			
			$("#"+this.domId).width(maxWidth+20);
		}
        
    },
	/*
	 * 自动隐藏菜单，用于内部事件，当在页面其他位置点击时触发
	*/
    autoHide: function(event) {
        var $dom = event.data.$dom;
        var self = event.data.self;
        if(new Date().getTime()-self.showTime<=1){
        	return;
        }
        var x = event.pageX,
        y = event.pageY;
        var offset = $dom.offset();
        if (x > offset.left && x < offset.left + $dom.width() && y > offset.top && y < offset.top + $dom.height()) {
            return;
        } else {
            self.hide();
        }
    },
	/*
	 *隐藏菜单项
	*/
    hide: function() {
        $("#" + this.domId).hide();
        $(document.body).unbind("mousedown", this.autoHide);
    }
};
CompMgr.regComp("MenuPanel", MenuPanel);;/**
 * 迷你tab页签
 * @class MiniTab
 */
var MiniTab = {
    buttonClassName:"tab-min-arrow",
    leftButtonClassName:"tab-min-arrow-left",
    rightButtonClassName:"tab-min-arrow-right",
    tabInnerClassName:"tab-min-foot",
    onClassName:"tab-min-l-on",
    moveIndex:"moveIndex",
    surplus:"surplus",
    itemCount:"itemCount",
    
    /**
     * 初始化
     * 
     *     MiniTab.init("tab", function () {
     *         alert("!");
     *     };
     *     
     * @method init
     * @param id {String} dom id
     * @param changeListener {Function} 按钮点击回调
     */
    init:function(id,changeListener){
        var $tab = $("#"+id);
        var $inner = $tab.find("."+this.tabInnerClassName).css("left",0);
        
        var $tabItems = $inner.children();
        var width = 0;
        for(var i=0,len = $tabItems.length;i<len;i++){
            var $item = $($tabItems[i]);
            width = 2+width +$item.width();// + parseInt($item.css("paddingLeft")); //+ parseInt($item.css("paddingRight"));
        }
        width+=0.5;
        $tabItems.bind("click",{changeListener:changeListener,$tabItems:$tabItems},MiniTab.itemClick);

        $inner.width(width);
        
        var surplus = width - $inner.parent().width();
        
        $tab.attr(this.surplus,surplus).attr(this.itemCount,len-1);
        
        var $a = $tab.children("a."+this.buttonClassName).bind("click",{$tab:$tab},function(event){
            var $tab = event.data.$tab;         
            var $a = $(this);
            MiniTab.move($tab,$a);
        });
        $tab.attr(this.moveIndex,"-1");
        $tab.attr(this.moveIndex,surplus>0 ? "0" :"-1");
    },
    itemClick:function(event){
        var $tabItems = event.data.$tabItems;
        var changeListener = event.data.changeListener;
        $tabItems.removeClass(MiniTab.onClassName);
        var $item = $(this);
        $item.addClass(MiniTab.onClassName);
//      alert(changeListener);
        if(changeListener){
//          alert($item.attr("url"));
            changeListener($item.attr("id"),$item);
        }
    },
    move:function($tab,$a){
        var $inner = this._getTabInner($tab);
        if($inner.attr("isAnimate")=="true"){
            return;
        }
        var left = $inner.position().left;
        var surplus = parseFloat($tab.attr(this.surplus));
        var itemCount = parseFloat($tab.attr(this.itemCount));
        var moveIndex = parseInt($tab.attr(this.moveIndex));
        $inner.attr("isAnimate","true");
        if($a.hasClass(MiniTab.leftButtonClassName)){
            if(left==0 || moveIndex == -1){
                $inner.attr("isAnimate","false");
                return;
            }
            var $item = $inner.children(":eq("+moveIndex+")");
            var moveWidth = $item.width()+ $.getLandscape($item[0],"padding");
            left = left + moveWidth;
            
            if(left > 0){
                moveIndex = 0;              
                left = 0;
            }else{
                moveIndex = moveIndex -1;
            }
            $a.attr(this.moveIndex,moveIndex);
            
            $inner.animate({left:left},function(){
                $inner.attr("isAnimate","false");
            });
            var $next = $a.next();
            $tab.attr(this.moveIndex,moveIndex);
            
        }else{
            if(left == surplus || moveIndex == -1){
                $inner.attr("isAnimate","false");
                return;
            }
            var $item = $inner.children(":eq("+moveIndex+")");
            var moveWidth = $item.width()+ $.getLandscape($item[0],"padding");
            moveWidth *=-1;
            left = left + moveWidth;
            if(left < surplus*-1){
                moveIndex = -1;
                left = surplus*-1;
                moveIndex = itemCount;
            }else{
                moveIndex  = moveIndex + 1;

            }
            
            $tab.attr(this.moveIndex,moveIndex);
            $inner.animate({left:left},function(){
                $inner.attr("isAnimate","false");
            });
            var $prev = $a.prev();
            $tab.attr(this.moveIndex,moveIndex);            
        }
    },
    _getTabInner:function($tab){    
        return $tab.children().children("."+this.tabInnerClassName);
    }
};/**
 * 拖动组件
 * 使指定HTML元素具有可拖拽效果
 * @class MoveUtil
 */
var MoveUtil ={
		/**
		 * 初始化，使元素可拖动
		 * 
		 *     MoveUtil.init({
		 *         handler : $('#movee'),
		 *         acton : $('#movee>.title'),
		 *         validate : function(event) {
	     *             var $clickee = $(event.target);
	     *             return $clickee.hasClass('top-m');
		 *         }
		 *     });
		 * 
		 * @method init
		 * @param conf {json} 配置信息
		 * @param conf.handler {String | Dom | jQuery} 鼠标拖动的热区，可拖动的位置
		 * @param conf.acton {String | Dom | jQuery} 需要移动的位置，被移动的元素
		 * @param conf.validate {Function} 回调函数，返回false时，终止拖拽行为，参数是鼠标按下的事件
		 */
		init:function(conf){
			var $acton = this._getJDOM(conf.acton);
			var $handler = conf.handler ? this._getJDOM(conf.handler) : $acton;
			
			$handler.bind("mousedown",{conf:conf,$acton:$acton},this._mouseDown).bind("mouseup",{$acton:$acton,conf:conf},MoveUtil._mouseUp);;
		},
		
		/**
		 * 去掉元素可拖动效果
		 * 
		 *     MoveUtil.disable($('#movee'), $('#movee>.title'));
		 * 
		 * @method disable
         * @param handlerDom {String | Dom | jQuery} 鼠标拖动的热区，可拖动的位置
         * @param actionDom {String | Dom | jQuery} 需要移动的位置，被移动的元素
		 */
        disable: function(actionDom, handlerDom) {
            var $dom = $(handlerDom || actionDom);
            $dom.unbind("mousedown", this._mouseDown)
                .unbind("mouseup", this._mouseUp);
        },
		_mouseDown:function(event){
			if (false === MoveUtil._isValidate(event.data.conf, event)) return;	// 调用回调函数，判断是否为有效点击
            event.preventDefault();// 阻止被拖动元素内部的input处理事件（否则在其它位置拖动，target仍然指向该元素）
            
			var $acton = event.data.$acton;
			var conf = event.data.conf;
			var x = event.pageX;
			var y = event.pageY;
			var conf = event.data.conf;
			$acton.data("position",$acton.css("position"));
			$acton.css("position","absolute");
			$acton.css("z-index",ZIndexMgr.get());
			$acton.data("pos",{x:x,y:y});
			if(conf.listeners && conf.listeners.mouseDown){
				conf.listeners.mouseDown($acton);
			}
			
			$(document.body).scroll()
				.bind("mousemove",{conf:conf,$acton:$acton},MoveUtil._bodyMove)
				.bind("mouseup",{$acton:$acton,conf:conf},MoveUtil._mouseUp);

			MoveUtil._showMask();
		},
		_mouseUp:function(event){
			$(document.body).unbind("mousemove",MoveUtil._bodyMove);		
			var conf = event.data.conf;
			var $acton = event.data.$acton;
		//	$acton.css("position",$acton.data("postion")|| "static");
			if(conf.listeners && conf.listeners.mouseUp){
				conf.listeners.mouseUp($acton);
			}
			ZIndexMgr.free($acton);
			$(document.body).unbind("mouseup",MoveUtil._mouseUp);
			MoveUtil._hideMask();
		},
		_getJDOM:function(d){
			return $dom = typeof d =="string" ? $("#"+d) : $(d);
		},
		_bodyMove:function(event){
			var $acton = event.data.$acton;
			var conf = event.data.conf;
			var listeners = conf.listeners || {};
			var position = $acton.data("pos");
			var x = event.pageX - position.x ; 
			var y = event.pageY - position.y;
			var pos = $acton.position();
			if (conf.rect) {
			    var $rect = $.getJQueryDom(conf.rect);
                maxLeft = $rect.width() - $acton.width();
                maxTop = $rect.height() - $acton.height();
			}else {
                maxLeft = $(window).width() - $acton.width();
                maxTop = $(window).height() - $acton.height();
			}
			var left = pos.left + x;
			var top = pos.top + y;
			var xFix = 0;
			var yFix = 0;
			
			if (left < 0) {
			    xFix = -left;
			}else if (left > maxLeft) {
			    xFix = maxLeft - left;
			}
			
			if (top < 0) {
                yFix = -top;
            }else if (top > maxTop) {
                yFix = maxTop - top;
            }
			
			$acton.css({
				left:left + xFix,
				top :top + yFix
			});
            $acton.data("pos",{x:event.pageX + xFix,y:event.pageY + yFix});
            
			if(listeners.moveAfter){
				listeners.moveAfter($acton,{x:event.pageX + xFix,y:event.pageY + yFix});
			}
			return false;
		},
		_isValidate: function(conf, event) {
			var fnValidate = conf.validate;
			if ($.isFunction(fnValidate)) {
				return fnValidate(event);
			}
		},
		_showMask : function() {
			var $maskDiv = $('#move_maskDiv');
			if (!$maskDiv[0]) {
				$maskDiv = $('<div id="move_maskDiv" style="position:absolute;top:0;left:0;width:100%;height:100%;z-index:99999"></div>')
					.appendTo(document.body);
			}
			return $maskDiv.show();
		},
		_hideMask : function() {
			$('#move_maskDiv').hide();
		}
};;/* global $,LayoutManager,RiilAlert,ExceptionAlert */
(function() {
	'use strict';
	if (window.PageCtrl) {
		return;
	}

	window.PageCtrl = {
		loadJs: function(url, callback) {
			var script = document.createElement('script');
			script.type = 'text/javascript';
			if (script.readyState) { //IE
				script.onreadystatechange = function() {
					if (script.readyState == 'loaded' || script.readyState == 'complete') {
						script.onreadystatechange = null;
						if (callback) callback();
					}
				};
			} else { //Others
				script.onload = function() {
					if (callback) callback();
				};
			}
			script.src = url;
			document.getElementsByTagName('head')[0].appendChild(script);
		},
		/*
		 *
		 * @param {Object} conf
		 * {url:'请求页面url',dom:'填充元素可以是id或者dom对象,param:{url参数},callback:请求页面回来以后回调',refresh:是否刷新容器，默认刷新}
		 */
		load: function(conf, isBlock) {
			var $dom = null;
			var url = conf.url,
				dom = conf.dom,
				param = conf.param ? conf.param : {},
				type = conf.type ? conf.type : 'Post',
				callback = conf.callback ? conf.callback : null,
				callbackParam = conf.callbackParam ? conf.callbackParam : null,
				contentType = conf.contentType ? conf.contentType : 'application/x-www-form-urlencoded';
			var refresh = conf.refresh === false ? false : true;
			if (dom) {
				if (typeof conf.dom === 'string') {
					$dom = $('#' + conf.dom);
				} else {
					$dom = $(conf.dom);
				}
			} else {
				new Error('load page is Error');
			}
			if (window.Loading) {
				//	window.Loading.start();
			}
			window.PageCtrl.ajax({
				url: url,
				cache: false,
				dataType: 'html',
				data: param,
				contentType: contentType,
				type: type,
				error: conf.error,
				success: function(data /*,textStatus,jqXHR*/ ) {

					if (refresh) {
						$dom.find('*').unbind();
						$dom.html('');
					}
					var rscript = /(<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>)/gim;
					var rsrc = /src="(\S+.js)"/i;
					var rname = /namespace="(\S+)"/i;
					var rinitparam = /initparam="(\S+)"/i;
					var rblock = /block="(\S+)"/i;
					var scripts = data.match(rscript);

					data = trim(data.replace(rscript, ''));
					var $content = $(data);

					$dom.append(data);
					if (window.FoldPanel) {
						window.FoldPanel.init();

					}
					if (window.LayoutManager) {
						LayoutManager.doLayout($dom);
					}
					if (window.Loading) {
						// window.Loading.stop();
					}
					if (callback) {
						callback(callbackParam, $content);
					}

					if (!scripts) return;

					var loadConfs = []; //用于阻塞加载js保存配置信息

					for (var i = 0, len = scripts.length; i < len; i++) {
						var script = scripts[i];
						var src = parseTxt(script, rsrc);

						if (!src) {
							$dom.append(trim(script));
							continue;
						}

						var initparam = parseTxt(script, rinitparam);
						var scriptBlock = parseTxt(script, rblock);
						var name = parseTxt(script, rname);
						var fnRunInit = prepareInit(name, initparam);

						if (!name) {
							window.PageCtrl.loadJs(src);
						}else if (!window[name]) {
							if (isBlock || scriptBlock === 'true') {
								loadConfs.push({src: src, init: fnRunInit});
							} else {
								window.PageCtrl.loadJs(src, fnRunInit);
							}
						} else {
							fnRunInit();
						}
					}

					var loadConfStep = 0;
					blockScript(loadConfs[loadConfStep++]);

					function parseTxt(txt, reqTxt) {
						var m = reqTxt.exec(txt);
						if (m) return m[1];
					}

					function prepareInit(name, initparam) {
						return function() {
							if (false !== conf.autoInit && name) window[name].init(initparam);
						};
					}

					function blockScript(loadConf) {
						if (!loadConf) return;
						window.PageCtrl.loadJs(loadConf.src, function() {
							loadConf.init();
							blockScript(loadConfs[loadConfStep++]);
						});
					}
				}
			});
		},
		submit: function(conf) {
			window.PageCtrl.ajax(conf);
		},
		ajax: function(conf) {
			var success = conf.success;
			var error = conf.error;

			conf.success = function(data, textStatus, jqXHR) {
				if (conf.loading === true) {
					window.Loading.stop();
				}
				if (success) success(data, textStatus, jqXHR);
			};
			conf.error = function(jqXHR, textStatus, errorThrown) {
				if (window.Loading) {
					window.Loading.stop();
				}
				if (jqXHR) {
					var errorFrame = '';

					if (jqXHR.status === 404 || jqXHR.status === 0) {
						if (error) {
							error(jqXHR, textStatus, errorThrown);
						}
						if (conf.error404) { //404错误自定义处理
							conf.error404(jqXHR, textStatus, errorThrown);
						}
					} else {
						if (jqXHR.status == 501) {
							var errorJson = $.StringtoJson(jqXHR.responseText);
							if (error) {
								error(jqXHR, textStatus, errorThrown);
							}
							if (errorJson.errLevel == '1') { //错误提示方式
								RiilAlert.info(errorJson.errorMessage);
								return;
							}
							errorFrame = ExceptionAlert.createFrame(
								errorJson.errorMessage, errorJson.errorCode || '', 
								errorJson.stackTrack, errorJson.hasErrDetail);
							$(document.body).append(errorFrame);
							ExceptionAlert.init();

						} else {
							if (error) {
								error(jqXHR, textStatus, errorThrown);
							}
							errorFrame = jqXHR.responseText;
							$(document.body).append(errorFrame);
						}

					}
				}
			};

			if (conf.loading === true) {
				window.Loading.start();
			}

			if (conf.url.indexOf('.html') == -1) {

				var url = conf.url;

				if (url.indexOf('?') != -1) {
					url += '&';
				} else {
					url += '?';
				}
				url += 'aaaabbbb2=' + new Date().getTime();

				conf.data = conf.data || {};
				if(typeof conf.data != "string"){
					conf.data.aaaabbbb = new Date().getTime();
				}
				
				conf.url = url;
			}
			return $.ajax(conf).responseText;
		},
		/*
		 * 弹出普通窗口
		 * */
		winOpen: function(obj) {
			var resizeable = obj.resizeable === true ? 'yes' : 'no';
			var scrollable = obj.scrollable === true ? 'yes' : 'no';
			var height = obj.height;
			var width = obj.width;

			if(height > window.screen.availHeight){
				height = window.screen.availHeight - 100;
			}


			var left = obj.left || window.screen.availWidth / 2 - width / 2;
			var top = obj.top || window.screen.availHeight / 2 - (height + 50) / 2;

			if (top < 0) {
				top = 0;
			}


			var windowLeft = window.screenLeft;
			var windowTop = window.screenTop;
			if (windowLeft < 0 || windowLeft >= window.screen.width) {
				left = windowLeft + left;
			}

			if (windowTop < 0 || windowTop >= window.screen.height) {
				top = windowTop + top;
			}

			var name = obj.name ? obj.name : 'newwin';
			var url = obj.url;
			//默认不设置参数，不缓存加参数，如果设置true，则不加参数缓存请求
			if (!obj.noCache) {
				if (url.indexOf('?') != -1) {
					url += '&';
				} else {
					url += '?';
				}
				url += 'aaaa=' + new Date().getTime();
			}
var newWin = window.open(url, name, 'height=' + height + ', width=' + width + 
				', top=' + top + ', left=' + left + ', toolbar=no, menubar=no, scrollbars=' + scrollable + 
				',resizable=' + resizeable + ',location=no, status=no');
			newWin.focus();
			return newWin;
		},
		/*
		 * 弹出模态窗口
		 * */
		winModalOpen: function(obj) {

			if(!window.showModalDialog){
				window.PageCtrl.winOpen(obj);
				return;
			}

			var height = obj.height;
			var width = obj.width;
			var left = obj.left || window.screen.availWidth / 2 - width / 2;
			var top = obj.top || window.screen.availHeight / 2 - height / 2;
			// var name = obj.name ? obj.name : "newwin";
			var url = obj.url;
			//默认不设置参数，不缓存加参数，如果设置true，则不加参数缓存请求
			if (!obj.noCache) {
				if (url.indexOf('?') != -1) {
					url += '&';
				} else {
					url += '?';
				}
				url += 'aaaa=' + new Date().getTime();
			}
			var windowLeft = window.screenLeft;
			var windowTop = window.screenTop;
			if (windowLeft < 0 || windowLeft >= window.screen.width) {
				left = windowLeft + left;
			}

			if (windowTop < 0 || windowTop >= window.screen.height) {
				top = windowTop + top;
			}
			//window.showModalDialog('Noname2.html',window,'dialogWidth:400px;dialogHeight:400px');
			var popstyle = 'dialogTop:' + top + 'px;dialogLeft:' + left + 
				'px;help:no;center:no;dialogHeight:' + height + 'px;dialogWidth:' + 
				width + 'px;status:no;resizable:no;scroll:no';
			window.showModalDialog(url, window, popstyle);
		},
		/*
		 * 显示无数据标签
		 * @param applyId 将提示标签应用到元素位置
		 * @param mini 如果为true，则为小提示
		 * @param text 提示信息
		 * */
		loadNoData: function(applyId, mini, text) {
			var $append = $('#' + applyId);
			if ($.isString(mini)) {
				text = mini;
			} else if (!text) {
				text = window.S_DATA_EMPTY;
			}


			var $nodata = $('<div class="nodata' + (mini === true ? '-min' : '') + '">' + text + '</div>');
			$append.html($nodata);
			$nodata.css({
				marginTop: $append.height() / 2 - $nodata.height() / 2,
				marginLeft: $append.width() / 2 - $nodata.width() / 2
			});
			return $nodata;
		}


	};
})();


function trim(string) {
	'use strict';
	return string.replace(/^[\s\r\n]*/, '');
}
window.ltrim = function(s) {
	'use strict';
	return s.replace(/^\s*/, '');
};;if (!window.Panel) {

/**
 * 窗口样式组件
 * @class Panel
 */
var Panel = function () {
    var S_COMP_ID = this.COMP_ID = 'Panel';
    
    var _popedPanel = {};
    
    var STYLE = [
        {
            PANEL: ['pop-blue', 'alert'],
            BUTTON: null,
            BUTTON_AREA: "pop-btngroup-bottom",
            BUTTON_AREA_TEXT: "",
            BORDER_SIZE: {width: 12, height: 33, title: 12}
        }, {
            PANEL: ['layer'],
            BUTTON: 'btn_body',
            BUTTON_AREA: "h-nobg",
            BUTTON_AREA_TEXT: "text-align:center",
            BORDER_SIZE: {width: 10, height: 10, title: 20}
        }, {
            PANEL: ['layer bgblue:panel'],
            BUTTON: null,
            BUTTON_AREA: "pop-btngroup-bottom",
            BUTTON_AREA_TEXT: "text-align:center",
            BORDER_SIZE: {width: 10, height: 10, title: 20}
        }, {
            PANEL: ['portlet', 'panel'],
            BUTTON: null,
            BUTTON_AREA: "pop-btngroup-bottom",
            BUTTON_AREA_TEXT: "",
            BORDER_SIZE: {width: 10, height: 29, title: 12}
        }, {
            PANEL: ['panel'],
            BUTTON: null,
            BUTTON_AREA: "pop-btngroup-bottom",
            BUTTON_AREA_TEXT: "",
            BORDER_SIZE: {width: 10, height: 29, title: 10}
        }, {
            PANEL: ['tt'],
            BUTTON: 'btn_body',
            BUTTON_AREA: "h-nobg",
            BUTTON_AREA_TEXT: "text-align:center",
            BORDER_SIZE: {width: 1, height: 1, title: 1}
        }
    ];
    
    var arrtMap = {};

    var api;
    
    return api = {
        STYLE : STYLE,
        DEFAULT_STYLE : STYLE[0],
        /**
         * 关闭层
         * @method close
         * @param id {string} 层的dom id
         */
        close : close_fn,
        
        
        /**
         * 异步显示弹出层
         *
         *    var conf = {
         *        id: "panel1",
         *        width: 300,
         *        url : 'content.html',
         *        maskConfig : {id: 'aaaaa', opacity: true, style: 2},
         *        closeBack: function() {
         *            console.log("关闭事件");
         *        },
         *        buttons: [
         *            {type : "ok", click : function() {
         *                console.log("确定事件");
         *            }}, {type :  "cancel", click : function() {
         *                console.log("取消事件，删除面板");
         *                Panel.close("panel1");
         *            }}]
         *    };
         *    Panel.show(conf);
         * 
         * @method show
         * @param conf {json} 异步请求层配置
         * @param conf.id {string} 该层ID
         * @param conf.url {string} 请求的URL，注意，url路径中应有layer，才可以装载到窗口
         * @param conf.data 请求参数,
         * @param conf.height {number} 高度,
         * @param conf.width {number} 宽度,
         * @param conf.top {number} y坐标,
         * @param conf.left {number} x坐标,
         * @param conf.mask {Boolean} 是否使用遮罩，默认使用
         * @param conf.maskConfig {Boolean} 遮罩（RiilMask）配置参数
         * @param conf.callback {function} 回调
         * @param conf.contentType 与form的一样
         * 
         */
        show : show_fn,
        
        
        /**
         * 同步显示弹出层
         * @method htmlShow
         * @param conf {json} 配置参数
         * @param conf.id {string} content直接上级元素的dom id
         * @param conf.top {number} 纵坐标，单位：px
         * @param conf.width {number} 宽度，单位：px
         * @param conf.height {number} 高度，单位：px
         * @param [conf.style] {string} 样式名称,可用值："alert", "layer"
         * @param conf.content {string} 内容HTML字符串
         * @param conf.title {string} 标题
         * @param conf.mask {Boolean} 是否使用遮罩，默认使用
         * @param conf.maskConfig {Boolean} 遮罩（RiilMask）配置参数
         * @param conf.closeBack {function} 关闭时的回调函数
         * @param conf.buttons {array} 按钮列表详情参见WindowButton
         * @return {jquery} 弹出层对象
         */
        htmlShow : htmlShow_fn,
        /**
         * 同步显示弹出层
         * @method htmlShow
         * @param conf {json} 配置参数
         * @param conf.id {string} content直接上级元素的dom id
         * @param conf.top {number} 纵坐标，单位：px
         * @param conf.width {number} 宽度，单位：px
         * @param conf.height {number} 高度，单位：px
         * @param [conf.style] {string} 样式名称,可用值："alert", "layer"
         * @param conf.src {string} iframe请求的链接
         * @param conf.title {string} 标题
         * @param conf.mask {Boolean} 是否使用遮罩，默认使用
         * @param conf.maskConfig {Boolean} 遮罩（RiilMask）配置参数
         * @param conf.closeBack {function} 关闭时的回调函数
         * @param conf.buttons {array} 按钮列表详情参见WindowButton
         * @return {jquery} 弹出层对象
         */        
        iframeShow : iframeShow_fn,
        /**
         * 隐藏弹出层
         * @method hide
         * @param id {String} dom id
         */
        hide : hide_fn,

        /**
         * 修改大小
         * @method resize
         * @param id {String} dom id
         * @param width {Number} 宽度
         * @param height {Number} 高度
         */
        resize : resize,
        
        /**
         * 修改内容
         * @method setContent
         * @param id {String} dom id
         * @param content {String} html字符串
         * @param width {Number} 宽度
         * @param height {Number} 高度
         */
        setContent : setContent, 
        
        setPosition : setPosition,
        
        getUnderMask : getUnderMask
    };
    
    
    function attr(panelId, attrId, attrValue) {
        var argCount = arguments.length;
        
        // 检查参数
        switch (argCount) {
        case 3:
            if ($.isNull(attrValue)) {
                console.info('parameter error');
                alert('parameter error');
            }
        case 2:
            if ($.isNull(attrId)) {
                console.info('parameter error');
                alert('parameter error');
            }
        }
        
        // 获取panelId
        if (!$.isString(panelId)) {
            var $inner = $.getJQueryDom(panelId);
            if (!$inner[0]) {
                console.info('parameter error');
                alert('parameter error');
            }
            panelId = $inner.attr("id");
            if ($.isNull(panelId)) {
                console.info('parameter error');
                alert('parameter error');
            }
        }
        
        // 获取数据实体（不存在时补充）
        var data = arrtMap[panelId];
        if ($.isNull(data)) {
            arrtMap[panelId] = data = {};
        }
        
        // 处理
        var retValue = undefined;
        if (3 == argCount) {
            retValue = data[attrId] = attrValue;
        }else if (2 == argCount) {
            retValue = data[attrId];
        }else if (1 == argCount) {
            retValue = data;
        }
        
        // 返回
        return retValue;
    }
    
    function removeData(panelId) {
        if (arrtMap[panelId]) {
            delete arrtMap[panelId];
        }
    }
    
    function laterInit($panel, $inner, conf, borderSize, style) {
        var $buttonArea = null;
        if (conf.buttons && conf.buttons.length > 0) {
            $buttonArea = createButtonArea($inner, conf, style);
        }

        attr(conf.id, "$panel", $panel);
        attr(conf.id, "$inner", $inner);
        
        _initEvent_fn(conf);
        
        attr(conf.id, "onShow", function (left_i, top_i, size) {
            beforeShow(conf, $panel, borderSize, left_i, top_i, size);
            if (null !== $buttonArea) {
                $buttonArea.appendTo($inner);
            }
        });
        
        attr(conf.id, "onResize", function (width, height) {
            
            switch (arguments.length) {
            case 2:
                if (!$.isNull(width)) conf.width = width;
            case 1:
                if (!$.isNull(height)) conf.height = height;
            }
            
            onResize(conf, $panel, borderSize);
            
            var $underMask = attr(conf.id, "underMask");
            
            if ($underMask) {
                attr(conf.id, "underMask").css({
                    top: $panel.css('top'),
                    left: $panel.css('left'),
                    width : $panel.width(),
                    height : $panel.height()
                });
            }
        });
        
        attr(conf.id, "onSetPosition", function (x, y) {
            if (!$.isNull(x)) {
                conf.left = x;
            } 
            if (!$.isNull(y)) {
                conf.top = y;
            }
            
            $panel.css({
                top : y,
                left : x
             });
            
            var $underMask = attr(conf.id, "underMask");
            
            if ($underMask) {
                attr(conf.id, "underMask").offset($panel.offset());
            }
        });
        
        if (!conf.initHide) {
            attr(conf.id, "onShow")();
        }
    }
    
    function setPosition(id, x, y) {
        attr(id, "onSetPosition") (x, y);
    }
    
    function getUnderMask(id) {
        return attr(id, 'underMask');
    }
    
    /**
     * 初始化事件
     * @method _init_fn
     * @private
     * @param id {string} dom id
     * @param closeBack {function} 关闭时的回调函数
     * @param buttons {array} 按钮列表详情参见WindowButton
     */
    function _initEvent_fn (conf) {
        var id_s = conf.id,
            closeBack_fn = conf.closeBack;
        _getCloseBtn_fn(id_s).bind("click", id_s, function(event){
            var holdPanel = false;
            if(closeBack_fn) {
                try {
                    holdPanel = closeBack_fn();
                }catch(e){};
            }
            if (!holdPanel) {
                close_fn(event.data);
            }
        });
        
        // 移动，仅当movable参数不等于false时
        // 暂时屏蔽
        // if (conf.movable !== false) {
        //     var $panel = attr(conf.id, "$panel");
        //     var $title = _getTitle_fn(conf.id);
            
        //     if (!$title[0]) {
        //         console.info('Panel._initEvent_fn : $panel.find(\'middle-m\'); Can not find target.');
        //     }else {
        //         MoveUtil.init({
        //             handler : $title.addClass('pro_cursor'),
        //             acton : $panel
        //         });
        //     }
        // }
    }
    
    
    /**
     * 获得面板
     * @method _getPanel_fn
     * @private
     * @param id {string} dom id
     */
    function _getPanel_fn (id) {
        var $panel = attr(id, '$panel');
//        if (!$panel || !$panel[0]) alert('parameter error');
        return $panel;
//        var $content = $.getJQueryDom(id).parent().parent().parent().parent();
//        if(!$content.hasClass("pop-blue") && !$content.hasClass("portlet") 
//                && !$content.hasClass("panel") && !$content.hasClass("layer")){
//            return $content.parent();
//        }else{
//            return $content;
//        }
    }
    
    function _getInner_fn (id) {
        var $inner = attr(id, '$inner');
//        if (!$inner || !$inner[0]) alert('parameter error');
        return $inner;
    }
    
    
    function _getTitle_fn(id) {
        return attr(id, '$panel').children('div:eq(0)').children('div:eq(0)').
                children('div:eq(0)');
    }
    
    /**
     * 获得关闭按钮
     * @method _getCloseBtn_fn
     * @private
     * @param id {string} dom id
     * @return {jquery} 关闭按钮的jquery对象
     */
    function _getCloseBtn_fn (id) {
        var $closeBtn = _getPanel_fn(id).children(":eq(0)").children(":eq(0)").children(":eq(0)").children("span.portlet-ico");
        if($closeBtn[0]){
            return $closeBtn;
        }else{
            return _getPanel_fn(id).children().children().children().children("a");
        }
    }
    
    function resize(id, width, height) {
        attr (id, "onResize") (width, height);
    }
    
    function setContent(id, content, conf) {
        var $inner = _getInner_fn(id);
        if (!$inner[0]) {
            console.info('parameter error');
            alert('parameter error');
        }
        
        $inner.empty().append(content);
        
        if (!conf) {
            resize(id);
        }else {
            resize(id, conf.width, conf.height);
        }
    }

    function close_fn (id) {
        // return;
        var $panel = _getPanel_fn (id);
        if ($.isNull($panel)) return;
        $panel.find("*").unbind();
        ZIndexMgr.free($panel);
        removeData(id);

        RiilMask.unmask(S_COMP_ID + '_' + id);
        
        unPop(id);
        $panel.remove();
    }
    
    function hide_fn(id) {
        var $panel = _getPanel_fn (id);
        if ($.isNull($panel)) return;
        
        $panel.hide();
        ZIndexMgr.free($panel);
        
        RiilMask.unmask(S_COMP_ID + '_' + id);        
    }
    
    function getStyle_fn ($panel) {
        if (null === $panel || undefined === $panel) {
            return api.DEFAULT_STYLE;
        }
        
        var styleCount = STYLE.length,
            style,
            styleName,
            nameCount;
        
        while (styleCount--) {
            style = STYLE[styleCount];
            styleName = style.PANEL;
            nameCount = styleName.length;
            
            if ($.isString($panel)) {
                while (nameCount--) {
                    if ($panel === styleName[nameCount]) {
                        return style;
                    }
                }
            }else {
                while (nameCount--) {
                    if ($panel.hasClass(styleName[nameCount])) {
                        return style;
                    }
                }
            }
        }
        
        return api.DEFAULT_STYLE;
    }
    
    function getPanelFromInner ($inner, $elems) {
        var $panel = $inner.parents('div[class$="middle-l"]:eq(0)').parent();
        return $panel;
    }
    
    function show_fn (conf) {
        if(isPoped(conf)){
            return;
        }

        if(conf.height > window.innerHeight){
            conf.height = window.innerHeight - 100;
        }
        
        var $body = $(document.body);
        



        PageCtrl.load({
            url:conf.url,
            param:conf.data,
            type:"post",
            dataType:"html",
            contentType:conf.contentType,
            dom:$body,
            refresh:false,
            callback:function(param, $content){
                var id = conf.id;
                var $body = $(document.body);
                var $inner = $('#' + id).parent();
                var $panel = getPanelFromInner($inner, $content); // 根据结构取
                var $buttons = $('#' + id + "_buttons,.f-right-btn");
                var style = getStyle_fn($panel);
                var borderSize = style.BORDER_SIZE;

                if(conf.callback){
                    conf.callback();
                }
                
                laterInit($panel, $inner, conf, borderSize, style);
            },
            error : conf.error
        });
    }
    
    
    function iframeShow_fn(conf){

        conf.content =  $.createDomStr({
                tagName : "iframe",
                attr : {
                    id : conf.id + "iframe",
                    name : conf.id + "iframe",
                    frameborder : "0",
                    marginheight : "0",
                    marginwidth : "0",
                    scrolling : "no",
                    height : "100%",
                    width : "100%",
                    allowtransparency : "true",
                    src : conf.src                
                },
                style : {
                    backgroundColor : "transparent"
                }
            });
       
        Panel.htmlShow(conf);
    }

    function htmlShow_fn (conf) {
        if(isPoped(conf)){
            return attr(conf.id, "$panel");
        }
        
        var id_s = conf.id,
            title_s = conf.title,
            content_s = conf.content,
            buttons_a = [],
            btnCount,
            style = getStyle_fn(conf.style),
            borderSize = style.BORDER_SIZE,
            $buttonArea;
        
        var panelStyle = style.PANEL[0].split(":")[0];

        if (title_s) {
            title_s = '<a class="ico-quit"></a><span class="title">' + title_s + '</span>';
        }else {
            title_s = '';
        }
        
        var $panel = $([
           '<div class="' + panelStyle + '" style="width:auto;">',
               '<div class="top-l">',
                   '<div class="top-r">',
                       '<div class="top-m">', title_s, '</div>',
                    '</div>',
               '</div>',
               '<div class="middle-l">',
                   '<div class="middle-r">',
                       '<div class="middle-m" style="height:auto;" id="', id_s, '"></div>',
                   '</div>',
               '</div>',
               '<div class="bottom-l">',
                   '<div class="bottom-r">',
                       '<div class="bottom-m"></div>',
                   '</div>',
               '</div>',
           '</div>'
        ].join(''));
        
        var $inner = $panel.find('.middle-m');
        
        if (content_s) {
            $inner.prepend(content_s);
        }
        
        if (conf.initHide) {
            $panel.css("visibility", "hidden");
        }

        $panel.appendTo("body");
        
        laterInit($panel, $inner, conf, borderSize, style);
        
        return $panel;
        
    }
        
    function onResize(conf, $html, borderSize, conf_) {
        fixSize_fn($html, conf, conf_, borderSize);
        center_fn($html, conf);
    }
    
    function beforeShow (conf, $html, borderSize, left, top, size) {
        var zIndex;
        var maskConf = {id: S_COMP_ID + '_' + conf.id};
        if (conf.maskConfig) {
            maskConf = $.extend(conf.maskConfig, maskConf);
        }
        if (conf.mask !== false) {
            RiilMask.mask(maskConf);
        }else {
            zIndex = ZIndexMgr.get();
        }
        if (left) {
            conf.left = left;
        }
        if (top) {
            conf.top = top;
        }
        fixSize_fn($html, conf, size, borderSize);
        center_fn($html, conf);
        
        if (conf.mask === false) {
            attr(conf.id, "underMask", RiilMask.underMask(S_COMP_ID + '_' + conf.id, $html, zIndex));
        }
    }
    
    function setOutterWidth($panel, width, fix) {
        
        if ($panel && $panel.length > 0 && width && fix) {

            $inner.width(width);
            $panel.width(width + fix);
        }
        
        if (size.width) {
            size.width = formatNumber(conf.width);
            
            if (minWidth && minWidth > size.width) {
                size.width = minWidth;
            }
            
            $inner.width(size.width);
            $panel.width(size.width + sizeFixer.width);
        }else {
            $inner.width("auto");
            $panel.width("auto");
        }
    }
    
    function createButtonArea($parent, conf, style) {
        var styleText_s = style.BUTTON_AREA_TEXT ? ' style="' + style.BUTTON_AREA_TEXT + '"': "";
        var $buttonArea = $('<div class="' + style.BUTTON_AREA + '"' + styleText_s + ' id="' + conf.id + '_buttons"></div>');
        
        addButtons($buttonArea, conf, style);
        
        $buttonArea.appendTo($parent).css({"white-space": "nowrap"});
        
        return $buttonArea;
    }
    
    function addButtons($parent, conf, style) {
        var buttons_a = conf.buttons,
            revButtons_a = [],
            count = buttons_a.length;
        
        while (count > 0) {
            count -= 1;
            revButtons_a.push(buttons_a[count]);
        }
        WindowButton.init(revButtons_a, $parent, style.BUTTON);
    }
    
    function isPoped(conf) {
        var id_s = conf.id,
            $panel;
        if (_popedPanel[id_s] && conf.force !== true) {
            $panel = _getPanel_fn(id_s);
            if (!$.isNull($panel) && $panel.length > 0) {
                if ($panel.css("visibility") !== "hidden" && $panel.css("display") !== "none") {
                    // 已经显示的话，不做其它处理
                }else {
                    $panel.show();
                    $panel.css("visibility", "visible");
                    attr(id_s, "onShow")(conf.left, conf.top);
                }
            }
            
            return true;
        } else {
            _popedPanel[id_s] = id_s;  
        }
        return false;
    }
    
    function unPop(id) {
        delete _popedPanel[id];
    }
    
    function fixSize_fn($panel, conf, size, borderSize) {        
        var $inner = _getInner_fn(conf.id);//$panel.find('#' + conf.id),
        var contSpaceHeight = getSpace($inner, true);
        var contSpaceWidth = getSpace($inner);
        var sizeFixer = {
            width: borderSize.width,
            height: borderSize.height,
            title: borderSize.title
        };
        var size = size ? size : {width:conf.width, height:conf.height};
        // 使外层能够自适应

        
        // 调整宽度
        if (!$.isNull(size.width) && conf.width !== "auto") {
            size.width = formatNumber(size.width) + contSpaceWidth;
            
            if (conf.extendWidth) {
                if ($.isFunction(conf.extendWidth)) {
                    size.width += conf.extendWidth();
                }else if (!isNaN(conf.extendWidth)){
                    size.width += conf.extendWidth;
                }
            }
            
            $inner.width(size.width - sizeFixer.width - contSpaceWidth);
        }

        // 调整高度
        if (!$.isNull(size.height) && conf.height !== "auto") {
            size.height = formatNumber(size.height) + contSpaceHeight;
            
            $inner.height(size.height - sizeFixer.height - contSpaceHeight);
        }
    }
    
    function fixSize_old2_fn($panel, conf, size, borderSize) {        
        var $inner = _getInner_fn(conf.id),//$panel.find('#' + conf.id),
            contHeight = $inner.height(),
            contWidth = $inner.width(),
            contSpaceHeight = getSpace($inner, true),
            contSpaceWidth = getSpace($inner),
            minWidth = 0,
            sizeFixer = {width:borderSize.width, height:borderSize.height, title: borderSize.title},
            size = size ? size : {width:conf.width, height:conf.height},
            $topBottom = $panel.find(".top-m,.bottom-m");
            
        if (conf.buttons && conf.buttons.length > 0) {
            minWidth = 150;
        }

        size.width = size.width || 0;
        size.height = size.height || 0;
        
        size.width = formatNumber(size.width);
        size.height = formatNumber(size.height);
        
        // 计算内容宽度和高度
        if (!size.width) size.width = contWidth + sizeFixer.width;
        if (size.width < minWidth) size.width = minWidth;
        
        if (!size.height) size.height = contHeight + sizeFixer.height;

        if (conf.extendWidth) {
            if ($.isFunction(conf.extendWidth)) {
                size.width += conf.extendWidth();
            }else if (!isNaN(conf.extendWidth)){
                size.width += conf.extendWidth;
            }
        }
        size.width += contSpaceWidth;
        size.height += contSpaceHeight;
        
        
        $panel.width("auto");
//        $inner.width(size.width - sizeFixer.width - contSpaceWidth);
//        $topBottom.width(size.width - sizeFixer.title);
//
//        if (!$.isNull(conf.height) && conf.height !== "auto") {
//            $panel.height(size.height);
//            $inner.height(size.height - sizeFixer.height - contSpaceHeight);
//        }
    }
    
    function fixSize_old_fn($panel, conf, size, borderSize) {        
        var $inner = _getInner_fn(conf.id),//$panel.find('#' + conf.id),
            $innerParent = $inner.parent(),
            spaceH = getSpace($inner), // 内外宽度差
            spaceV = getSpace($innerParent, true), // 内外高度差
            showWidth = $innerParent.width(), // 内总体显示宽度
            showHeight = $innerParent.height(),// 内总体显示高度
            innerShowHeight = $inner.height(), // 内容（除按钮外）显示高度
            innerSpace = getSpace($inner, true); // 内容（除按钮外）高度差
            minWidth = 0,
            sizeFixer = {width:borderSize.width, height:borderSize.height, title: borderSize.title},
            size = size ? size : {width:conf.width, height:conf.height},
            $topBottom = $panel.find(".top-m,.bottom-m");
            
        if (conf.buttons && conf.buttons.length > 0) {
            minWidth = 150;
        }

        size.width = size.width || 0;
        size.height = size.height || 0;
        
        size.width = formatNumber(size.width);
        size.height = formatNumber(size.height);
        
        // 计算内容宽度和高度
        if (!size.width) {
            size.width = showWidth;
        }

        if (conf.extendWidth) {
            if ($.isFunction(conf.extendWidth)) {
                size.width += conf.extendWidth();
            }else if (!isNaN(conf.extendWidth)){
                size.width += conf.extendWidth;
            }
        }
        
        if (size.width < minWidth) {
            size.width = minWidth;
        }
        
        if (!size.height) {
            size.height = innerShowHeight;
        }
        
        spaceV += showHeight - innerShowHeight;
        
        $innerParent.width(size.width);
        $panel.width(size.width + sizeFixer.width + spaceH);
        $topBottom.width(size.width + spaceH);
        
        //console.info('sizeFixer.width: ' + sizeFixer.width);
        
        if (!$.isNull(conf.height) && conf.height > 0) {
            $innerParent.height(size.height);
            $panel.height(size.height + sizeFixer.height + spaceV);
        }
        
    }
    
    function getWidth($inner) {
        var $innerParent = $inner.parent(),
            width = 0;
        
        $innerParent.children().each(function (index, elem) {
            var curWidth = $(elem).width();
            
            if (width < curWidth) {
                width = curWidth;
            }
        });
        
        return width;
    }
    
    function getHeight($inner) {
        var $innerParent = $inner.parent(),
            height = 0;
        
        $innerParent.children().each(function (index, elem) {
            var curHeight = $(elem).height();
            
            if (height < curHeight) {
                height = curHeight;
            }
        });
        
        return height;
    }
    
    function getSpace($target, isVertical) {
        var func = isVertical ? $.getVertical : $.getLandscape;
        var padding = func($target, "margin"),
            margin = func($target, "padding"),
            space;
        
        if (isNaN(padding)) {
            padding = 0;
        }
        
        if (isNaN(margin)) {
            margin = 0;
        }
        
        space = padding + margin;
        
        return space;
    }
    
    function getHorizantalSpace($inner, isCurrent) {
        var $innerParent = $inner.parent(),
            outterSpace = $.getLandscape($innerParent, "padding"),
            innerSpace = 0;
        
        if (isNaN(outterSpace)) {
            outterSpace = 0;
        }
        
        $innerParent.children().each(function(index, elem) {
            if (!elem) return;
            
            space = getSpace($(elem));
            
            if (innerSpace < space) {
                innerSpace = space;
            }
        });
        
        return outterSpace + innerSpace;
    }
    
    function getVerticalSpace($inner, isCurrent) {
        var $innerParent = $inner.parent(),
            outterSpace = $.getVertical($innerParent, "padding"),
            innerSpace = 0;
        
        if (isNaN(outterSpace)) {
            outterSpace = 0;
        }
        
        $innerParent.children().each(function(index, elem) {
            if (!elem) return;
            
            space = getSpace($(elem), true);
            
            innerSpace += space;
        });
        
        return outterSpace + innerSpace;
    }
    
    function formatNumber(number) {
        return parseInt(number, 10);
    }
    
    function center_fn($panel, conf) {
        var $window = $(window),
            maxWidth_i,
            maxHeight_i,
            width_i,
            height_i,
            top_i,
            left_i;
        
        $panel.css({
            position : "absolute",
            zIndex : ZIndexMgr.get()
        });
        
        if (undefined !== conf.left && null !== conf.left) {
            left_i = conf.left;
        }else {
            maxWidth_i = $window.width();
            width_i = $panel.width();
            left_i = ($window.width() - $panel.width()) / 2;
        }
        
        if (undefined !== conf.top && null !== conf.top) {
            top_i = conf.top;
        }else {
            maxHeight_i = $window.height();
            height_i = $panel.height();
            top_i = ($window.height() - $panel.height()) / 2;
        }
        
        $panel.css({
            top : top_i,
            left : left_i
        });
        
//        if (conf.sizeFix && conf.sizeFix.sideWidth) {
//            $panel.find(".top-m,.bottom-m").width($panel.width() - conf.sizeFix.sideWidth);
//        }
    }
    
}();

};/**
 * @class Portlets
 * @param conf 配置参数
 * {<br>
 * 	id:portlet ID,<br>
 *  sortableStopCallBack:function(portletLayout){} 拖动后回调函数,参数portlets位置配置参数{left:[portlet id参数],right:[portlet id参数]}<br>
 * }<br>
 * */		
var Portlets = function(conf){
	var self = this;
	this.id = conf.id;
	this.listeners = conf.listeners;
	var $portlets = $("#"+this.id);
	var portletWindows = $portlets.find(".por_window");
	var column = $portlets.children(".column");
	column.sortable({
		connectWith: '.column',
		handle: '.por_window_bj',
		cursor: 'move',
		stop:function(){
			self.adjustColumnHeight();
			if(self.listeners && self.listeners.stop){
				self.listeners.stop(self.getLayout());
			}
		}
	});
	this.items = {};
	for(var i=0,len = portletWindows.length;i<len;i++){
		var portlet  = new Portlets.Item(portletWindows[i],this.listeners);
		this.items[portlet.id] = portlet;
	}
	if(len!=0){
		this.loadPortlet();
	}
}
Portlets.prototype = {
	constructor:Portlets,
	/*
	 * @inner
	 * 调整左右两列高度，取高的一列为基准
	 * */
	adjustColumnHeight:function(){
		var $portlets = $("#"+this.id);
		var column = $portlets.children(".column");
		var $leftColumn = $(column[0]);
		var $leftPortlets = $leftColumn.children();
		var leftHeight = 0;
		for(var i=0,len = $leftPortlets.length; i<len;i++){
			var $lp = $($leftPortlets[i]);
			leftHeight = leftHeight + $lp.height() + $.getVertical($lp[0],"margin") + 2;
		}
		var $rightColumn = $(column[1]);
		var $rightPortlets = $rightColumn.children();
		var rightHeight = 0;
		for(var i=0,len = $rightPortlets.length; i<len;i++){
			var $rp = $($rightPortlets[i]);
			rightHeight = rightHeight + $rp.height() + $.getVertical($rp[0],"margin") +2;
		}
		$rightColumn.height(rightHeight);
		$leftColumn.height(leftHeight);
		
		if(leftHeight > rightHeight){
			$rightColumn.height(leftHeight);
		}else{
			$leftColumn.height(rightHeight);
		}
	},
	loadPortlet:function(){
		var loaded = 0;
		var portletIds = [];
		
		for(var id in this.items){
			portletIds.push(id);
		}
		var self = this;
		var interval = setInterval(function(){
			self.items[portletIds[loaded]].refresh();
			loaded++;
			if(loaded == portletIds.length){
				clearInterval(interval);
			}
		},300);
	},
	/**
	 * 获得portlet布局配置
	 * @param removePortletId {Array} 待删除portletId 如果有该参数，则返回的布局配置中不包含此参宿内的portlet
	 * @returns json {left:"id,id,id",right:"id,id,id"}
	 * */
	getLayout:function(removePortletId){
		var $portlets = $("#"+this.id);
		var column = $portlets.children(".column");
		var $leftColumn = $(column[0]);
		var $rightColumn = $(column[1]);
		
		var layout = {};
		
		var $leftPortlet = $leftColumn.children(".por_window");
		var $rightPortlet = $rightColumn.children(".por_window");
		layout.left = [];
		var portletId = null;
		for(var i=0,len = $leftPortlet.length;i<len;i++){
			portletId = $($leftPortlet[i]).attr("id");
			if(removePortletId == portletId){
				continue;
			}
			layout.left.push(portletId);
		}
		layout.right = [];
		for(var i=0,len = $rightPortlet.length;i<len;i++){
			portletId = $($rightPortlet[i]).attr("id");
			if(removePortletId == portletId){
				continue;
			}			
			layout.right.push(portletId);
		}
		return {left:layout.left.join(","),right:layout.right.join(",")};
	},
	/**
	 * 删除指定portlet
	 * @param portletId 指定portletID
	 * */
	deletePortlet:function(portletId){
		var $portlet = $("#"+portletId);
		$portlet.find("*").unbind();
		$portlet.remove();
		this.adjustColumnHeight();
	}
	
}
/*
 * @inner
 * portlet单独项
 * */
Portlets.Item = function(dom,listeners){
	var $dom = $(dom);
	this.id = $dom.attr("id");
	this.url = $dom.attr("url");
	this.param = $dom.attr("param");
	this.listeners = listeners;
	$dom.children(":eq(0)").children("a").bind("click",{self:this,listeners:listeners},this.toolClick);
}

Portlets.Item.prototype = {
	/*
	 * @inner单独portal更新
	 * */
	refresh:function(conf){
		conf = conf ? conf : {};
		var tempUrl = conf.url ? conf.url : this.url;
		var tempParam = conf.param ? conf.param : this.param;
		if(conf.isRecordUrl){
			this.url = tempUrl;
		}
		if(conf.isRecordParam){
			this.param = tempParam;
		}
//		alert(this.url);
		var self = this;
		if(!this.url){
			return;
		}
		
		var $portlet = $("#"+this.id);
		var attr = $portlet.attr("contentId");
		
		var loadingId = null;
		
		if(attr){
			loadingId = attr;
		}else{
			//loadingId = this.id+"MastLoading";
			//$portlet.children(":eq(1)").attr("id",loadingId);
			loadingId = this.id;
		}
		loadingId = this.id;
		MaskShaw.start(loadingId, true);
		PageCtrl.load({
			url:tempUrl,
			param:tempParam,
			type:"post",
			dataType:"html",
			dom: attr ? $("#"+attr) : $portlet.children(":eq(1)"),
			callback:function(){
				MaskShaw.stop(loadingId);
				if(self.listeners && self.listeners.refreshAfter){
					self.listeners.refreshAfter();
				}
			},
			error:function(){
				MaskShaw.stop(loadingId);
			}
		});		
	},
	/*
	 * portlet上操作按钮点击事件
	 * */
	toolClick:function(event){
		var self = event.data.self;//portlet对象
		var $tool = $(this);
		if(self.listeners && self.listeners.toolClick){
			self.listeners.toolClick($tool.attr("type"),self.id,$tool);
		}
	}
};/**
 * 
 * 页面查询面板折叠效果
 * @class QueryPanel
 *  
 */
var QueryPanel = {
	expandBtnClassName:"searchpart-ico-up",
	collapseBtnClassName:"searchpart-ico-down",
	/**
	 * 初始化
	 * 
	 *     // 悬浮效果，展开关闭时不影响其它组件
	 *     QueryPanel.init("id");
	 *     
	 *     // 流效果，展开关闭时影响其它组件
     *     QueryPanel.init("id");
	 *     
	 * @method init
	 * @param id {String} 组件ID
	 * @param targetId {String} 当组件展开或收缩时，对应拉高或降低目标ID
	 *   
	 * */
	init:function(id,targetId){
		this.id = id;
		var $a = QueryPanel._getBtn(id);
		$a.bind("click",{id:id,targetId:targetId},QueryPanel._fold);
		var $div = $("#"+id);
		var $parent = $div.parent();
		var width = $div.width();
		var cssConf = {
				position: targetId ? "relative" : "absolute",
				width:width,
				"zIndex":ZIndexMgr.get()
		};
		//
		if($div.attr("initHide")!="false"){
			$div.children(":eq(1)").hide();
			cssConf.height = 0;
			cssConf.border = "0px solid rgb(19, 126, 194)";
			cssConf.backgroundColor = "transparent";
			$a.removeClass(QueryPanel.expandBtnClassName).addClass(QueryPanel.collapseBtnClassName);
			$div.addClass("searchpart-close");
		}else{
			cssConf.backgroundColor = "rgb(0, 36, 77)";
			cssConf.border = "1px solid rgb(19, 126, 194)";
		}		
		
		$div.css(cssConf);

		OnResize.addLayout(function(id) {
			return function() {
				QueryPanel.refreshWidth(id);
			}
		}(id));
		//QueryPanel.bindAutoHide();
		
//		if ($parent.length > 0) {
//	        if ($parent[0].nodeName.toUpperCase() === "FORM") {
//	            $parent = $parent.parent();
//	        }
//	        
//	        var paddingTop = parseInt($parent.css("padding-top"), 10);
//	        if (!isNaN(paddingTop)) {
//	            $div.css("margin-top", -paddingTop);
//	        }else {
//	            $div.css("margin-top", 0);
//	        }
//		}
		
	},
	/*
	 * @inner
	 * 获得按钮元素
	 * */
	_getBtn:function(id){
		return $("#"+id).children(".searchpart-ico");
	},
	/*
	 * @inner
	 * 获得显示区域元素
	 * */
	_getContent:function(id){
		return $("#"+id).children(":eq(1)");
	},
	/*
	 * @inner
	 * 折叠处理事件
	 * */
	_fold:function(event){
		var id = event.data.id;
		var targetId = event.data.targetId;
		var $a = $(this);
		if($a.hasClass(QueryPanel.expandBtnClassName)){
			QueryPanel.collapse(id,targetId);
		}else{
			QueryPanel.expand(id,targetId);
		}
	},
	/**
	 * 去掉层悬浮
	 * @method setState
	 * @param {String} dom id
	 * */
	setState:function(id){
		$("#"+id).css("position","relative");
	},
	/**
	 *设置层为悬浮  
     * @method setLayer
     * @param {String} dom id
	 **/
	setLayer:function(id){
		$("#"+id).css("position","absolute");
	},
	refreshWidth:function(id){
		$("#"+id).width($("#"+id).parent().width());
	},
	isExpand:function(id){
		var $a = QueryPanel._getBtn(id);
		return !$a.hasClass(QueryPanel.collapseBtnClassName);
	},
	/**
	 * 展开
	 * @method expand
	 * @param id {String} 组件ID
	 * */
	expand:function(id,targetId){
		var $a = QueryPanel._getBtn(id);
		var $content = QueryPanel._getContent(id).show();
		//$content.css("opacity",1);
		QueryPanel.refreshWidth(id);
		var height = $content.height();
		$("#"+id).animate({height:height},function(){
			$a.parent().removeClass("searchpart-close");
			var $target = $("#"+targetId);
			if($target[0]){
				$target.height($target.height()-height);
			}
			$a.removeClass(QueryPanel.collapseBtnClassName).addClass(QueryPanel.expandBtnClassName);
		}).css({"backgroundColor":"rgb(0, 36, 77)"}).css("border","1px solid rgb(19, 126, 194)");
		
		
	},
	/**
	 * 折叠
	 * @method collapse
	 * @param id {String} 组件ID
	 * */	
	collapse:function(id,targetId){
		var $a = QueryPanel._getBtn(id);
		$a.parent().addClass("searchpart-close");
		var $content = QueryPanel._getContent(id);
		var height = $content.height();
		//$content.hide();
		$("#"+id).animate({height:0},function(){
			$content.hide();
			
			var $target = $("#"+targetId);
			if($target[0]){
				$target.height($target.height()+height);
			}			
			$a.removeClass(QueryPanel.expandBtnClassName).addClass(QueryPanel.collapseBtnClassName);
		}).css({"backgroundColor":"transparent"}).css("border","0px solid rgb(19, 126, 194)");
	},
	bindAutoHide:function(){
		var $body = $(document.body);
		if(!$body.attr("searchAutoHideBind")){
			$body.bind("click",QueryPanel.autoHide);
			$body.attr("searchAutoHideBind","true");
		}
	},
	autoHide:function(event){
		var $searchPart = $("div.searchpart");
		var $search = null;
		for(var i=0;i<$searchPart.length;i++){
			$search = $($searchPart[i]);
			if(!$.mouseEventBodyRange($search,event.pageX,event.pageY)){
				if(QueryPanel.isExpand($search.attr("id"))){
					QueryPanel.collapse($search.attr("id"));
				}
			}
		}
	}
	
};/**
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
}();;if (!window.Scale) {

/**
 * 刻度组件
 * @class Scale
 */
var Scale = {
	allWidth:300,
	EVERYWIDTH:"everyWidth",
	/**
	 * 初始化
	 * 
	 *     Scale.init({id:"scale",applyTo:"aaa",scaleVal:20,markCount:5,max:100,target:"targetId"});
	 * 
	 * @method init
	 * @param conf {Json} 配置信息
     * @param conf.id {String} dom id
     * @param conf.applyTo {String} dom id, 绘制到制定元素中
     * @param conf.scaleVal {Number} 当前刻度值",
     * @param conf.markCount {Number} 分为几个刻度"
     * @param conf.max {Number} 刻度长度"
     * @param conf.target {String} dom id, 数值更改目标元素ID
     * }
	 */
	init:function(conf){
		var id = conf.id;
		var scaleVal = conf.scaleVal;		
		var applyTo = conf.applyTo;		
		var _toString = Object.prototype.toString;
		
		if(_toString.call(applyTo)=="[object String]"){
			applyTo = $("#"+applyTo);
		}else{
			applyTo = $(applyTo); 
		}
		applyTo.append(this._createDom(id,scaleVal,conf.markCount,conf.max,conf.target));
		Scale._bindMouseDown(id);
	},
	_bindMouseDown:function(id){
		$("#"+id).next().bind("mousedown",Scale._mousedown);
	},
	/**
	 * 获得值
	 * @method getValue
	 * @param id 组件ID
	 * @return {String} 值
	 * */
	getValue:function(id){
		var $Scale = $("#"+Scale.currentId);	
		return $Scale.children("input").val();
	},
	_createDom:function(id,scaleVal,markCount,max,target){
	
		var width = scaleVal/max*Scale.allWidth;				
		var everyWidth = Scale.allWidth/markCount;
		var scale = ['<div class="scale-part"><div class="scalebar"><div target="'+target+'" class="scale" style="width:',width,'px" id="',id,'" everyWidth="',everyWidth,'" markCount="',markCount,'"></div><div class="scale-ico"></div><input max="',max,'" readonly value="',scaleVal,'"/></div><div class="scaledate5"></div></div>'];		
		return scale.join("");
	},
	_bindEvent:function(id){
		var $scale = $("#"+id);
		var $btn = $scale.find("scale-ico");
	},
	_move:function(event){
		var moveTime = new Date().getTime();
		if(moveTime - Scale.mousedownTime <100){
			return false;
		}
		var x = event.pageX;
		var spacing = x - Scale.x;
		var position = spacing < 0 ? -1 : 1;	
		var $Scale = $("#"+Scale.currentId);		
		var everyWidth = parseInt($Scale.attr(Scale.EVERYWIDTH));
		if(Math.abs(spacing) >= everyWidth-10){
			var scaleWidth = $Scale.width();
			var newWidth = scaleWidth+everyWidth*position;
			if(newWidth < 0){
				newWidth = 0;
			}else if(newWidth > Scale.allWidth){
				newWidth = Scale.allWidth;
			}else{
				Scale.x = x;
			}
			$Scale.width(newWidth);	
			var $input = $Scale.next().next("input");
			var max =  parseInt($input.attr("max"));
			var value = max*(newWidth/Scale.allWidth);
			$input.val(value);
			$("#"+$Scale.attr("target")).val(value);
			
		}
		return false;
	},
	/*
	 * 内部方法 鼠标按下事件 
	 */
	_mousedown:function(event,btn){
		Scale.mousedownTime = new Date().getTime();
		Scale.x = event.pageX;
		Scale.currentId = $(this).prev().attr("id");
		var $body = $(document.body).addClass("ban-drag");
		$body.bind("mouseup",function(){
			$body.unbind("mousemove",Scale._move).unbind("selectstart",Scale._selectstart).removeClass("ban-drag");
		}).bind("mousemove",Scale._move).bind("selectstart",Scale._selectstart);
	},
	/*
	 *  内部方法 body不能鼠标划选事件处理函数
	 * */
	_selectstart:function(){
		return false;
	}
	
};

};/**
 * 将最近几小时，几天，几个月，几年换算成开始日期和结束日期，并设置页面为相应的日期字段值
 * @class processLastestTime
 */
var processLastestTime = {
    /**
     * 初始化
     * @method init
     * @param options {Json} 配置信息
     * @param options.startTimeControl {String} 开始日期页面元素（JQUERY选择器语法）,缺省值#startTime
     * @param options.endTimeControl {String} 结束日期页面元素（JQUERY选择器语法）,缺省值#endTime
     * @param options.timeTypeControl {String} 时间段选择SELECT元素（JQUERY选择器语法）,缺省值#timeType
     * @param options.datePattern {String} 日期格式，缺省值yyyy-MM-dd HH:mm
     */
	init : function(options) {
		var settings = $.extend({
			startTimeControl : "#startTime",
			endTimeControl : "#endTime",
			timeTypeControl : "#timeType",
			datePattern : "{year}-{month}-{date} {hours}:{minutes}:{seconds}"
		}, options || {});

		var timeType = $(settings.timeTypeControl).val();
		
		var timeTypePattern = /^[dDmMhHyY]\:(\d|([1-9]\d*))$/;
		if (timeTypePattern.test(timeType)) {
			var endTime = new Date();
			var startTime = new Date(options.startDate);

			var tokens = timeType.split(":");
			this.setTimeField(startTime, tokens[0], new Number(tokens[1]));
            
			$(settings.startTimeControl).val(
					$.formatDate(startTime, settings.datePattern)).attr("disabled", "disabled");
			var parent = $(settings.endTimeControl).attr("disabled", "disabled").val(
					$.formatDate(endTime, settings.datePattern)).parent().hide();
		} else {
			$(settings.startTimeControl).removeAttr("disabled").parent().show();
			$(settings.endTimeControl).removeAttr("disabled");
		}
	},
	/**
	 * 更改日期，将某个时间段减去特定的数值
	 * @method setTimeField
	 * @param date Date对象
	 * @param 时间段 h:小时,d:天,m:月,y:年
	 * @param 数值
	 */
	setTimeField : function(date, timeField, number) {
		if ("h" == timeField) {
			date.setHours(date.getHours() - number);
		} else if ("d" == timeField) {
			date.setFullYear(date.getFullYear(), date.getMonth(), date.getDay()
					- number);
		} else if ("m" == timeField) {
			date.setFullYear(date.getFullYear(), date.getMonth() - number, date
					.getDay());
		} else if ("y" == timeField) {
			date.setFullYear(date.getFullYear() - number);
		}
	}
};/**
  * {Portlet -- 拖动面板，支持翻页} <br>
  * <p>
  * Create on : 2013-1-5<br>
  * <p>
  * 
  * card更换位置方式: 
  * 	exchange		互换方式，被拖动面板与目标位置面板的位置互换
  *     queue 			队列方式，被拖动面板插入面板队列
  * 
  * 初始化参数：
  * conf {
  *     applyId:		"将组件应用到dom的元素ID",
  *     targetId:		"元素集合容器domID，用于获得所有卡片元素",
  *     isPlusButton:	boolean 是否显示添加按钮,
  *     changeMethod:	"exchange"或"queue"
  *     listeners:{
  *        	addBtnClick:function(){}
  *     }
  * }
  * 
  * 属性：
  * 	screenItemCount	每屏显示卡片个数
  *     columnCount		每屏列数
  *     lineCount		每屏行数
  *     itemWidth		元素宽
  *     itemHeight		元素高
  *     screenCount		屏个数
  *     itemCount		元素个数
  *     screenWidth		显示区域宽度
  *     screenHeight	显示区域高度
  * </p>
  * <br>
  * 
  * @author fangzhixin<br>
  * @version riil_common_web v1.0
  *          <p>
  *          <br>
  *          <strong>Modify History:</strong><br>
  *          user modify_date modify_content<br>
  *          -------------------------------------------<br>
  *          zhaoxintian 2013-1-5 格式调整
  *          <br>
  */
var SlidePaging = {
	//---------------------------------------------------------------------------
	// 属性
	//---------------------------------------------------------------------------
	screens: [],
	currentScreen: null,
	
	/** <code>conf</code> - {初始化信息} */
	conf: null,
	
	/** <code>listeners</code> - {事件} */
	listeners: null,
	$screenOuter: null,
	init: function(conf){
		this.conf = conf;
		this.listeners = conf.listeners;
		this.$screenOuter = $('<div style="overflow:hidden;"></div>');
		
		$("#"+conf.applyId).css({
			"height":"100%",
			"position":"relative"
		}).append(this.$screenOuter);
		
		var $items = $("#"+conf.targetId).children();//获得目标容器中的元素
		this._calculateScreenItemsCount(this.$screenOuter,$items);
		
		//this.targets = {};
		var screens = this._getScreens();
		
		var changeMethod = conf.changeMethod || "exchange";  //移动元素方式，互换或者顺序
		SlidePaging._moveChange = SlidePaging["_"+changeMethod];
		
		for(var i=0,l = screens.length;i<l;i++){
			var screen  = screens[i];	
			this._bindMoveCards(screen);
		}
		this.currentScreen  = SlidePaging._getFirstScreen();
	},
	/*计算屏个数和每屏显示卡片行数和列数，间距*/
	_calculateScreenItemsCount:function($screenOut,items){
		var $item = null;
		var len = this.itemCount = items.length;
		this._initScreenSize();
		if(len > 0){
			$item = $(items[0]);
			var $screen = this._getContainer();
			this.itemWidth = $item.width(); //获得卡片高度宽度
			this.itemHeight = $item.height();
			this.lineCount = parseInt(this.screenHeight/this.itemHeight);
			this.columnCount = parseInt(this.screenWidth/this.itemWidth);
			var screenItemCount = this.screenItemCount = this.lineCount * this.columnCount;
			
			this.marginLeft = parseInt((this.screenWidth - this.itemWidth* this.columnCount)/(this.columnCount+1));
			this.marginTop = parseInt((this.screenHeight - this.itemHeight*this.lineCount)/(this.lineCount+1));
			var screenCount = 1;
			if(items.length > screenItemCount){
				screenCount = parseInt(items.length/screenItemCount);					
				screenCount += items.length%screenItemCount !=0 ? 1 : 0;
			}
			this.screenCount = screenCount;
			
			this._initTargetToApply(items,$screenOut,screenItemCount);
			
			this._initScreenOuterSize();
		}
	},
	/**初始化目标卡片集合到指定展示位置上*/
	_initTargetToApply:function(items,$screenOut,screenItemCount){
		var sp = SlidePaging;

		var screenIndex = 0;
		var $screen = null;
		for(var i=0,len = items.length; i<len; i++){
			var $li = $(sp._createItem());
			$li.append(items[i]);
			if(i==0 || i+1 == screenItemCount*screenIndex + 1){
				$screen = $(sp._createScreen($screenOut.parent().width(),$screenOut.parent().height()));
				screenIndex++;
			}
			$screen.append($li);
			$screenOut.append($screen);					
		}
		var show = sp.conf.isPlusButton ? "" : "none";
//			$screen.append($(sp._createItem()).append('<img addCard="true" src="cardadd.png" style="display:'+show+'">'));
		var $add = $('<div addCard="true" class="room-card-add" style="display:'+show+'"></div>');
		
		if(sp.listeners && sp.listeners.positionChange){
			$add.bind("click",sp.listeners.addBtnClick);
		}
		
	
		$screen.append($(sp._createItem()).append($add));			
		
	},		
	_initScreenSize:function(){
		var $container = this._getContainer();
		var $parent = $container.parent();
		this.screenWidth = $parent.width();
		this.screenHeight = $parent.height();
		$container.width(this.screenWidth).height(this.screenHeight);
	},
	/**初始化分屏总和宽度，高度*/
	_initScreenOuterSize : function(){
		var $container = this._getContainer();
		this._getScreenOuter().width($container.width() * this.screenCount).height($container.height());
	},
	_createItem:function(marginLeft,marginTop){
		return '<li  style="float:left;display:block;margin-left:'+this.marginLeft+'px;margin-top:'+this.marginTop+'px;width:'+this.itemWidth+'px;height:'+this.itemHeight+'px"></li>';
	},		
	_createScreen:function(width,height){
		return '<ul  style="float:left;height:'+this.screenHeight+'px;width:'+this.screenWidth+'px"></ul>';
	},
	/**
	  * 获得元素所在的屏
	  */
	_getScreenByItem:function($item){			
		return  $item.parent();			
	},
	_getContainer:function(){
		return $("#"+this.conf.applyId);
	},
	/*获得屏幕最外层*/
	_getScreenOuter:function(){
		return $("#"+this.conf.applyId).children();
	},
	/*获得所有屏幕dom对象返回数组*/
	_getScreens:function(){
		return $("#"+this.conf.applyId).children().children("ul");
	},
	/**获得屏个数*/
	_getScreenCount:function(){
		return this._getScreens().length;
	},
	/**获得指定索引屏*/
	_getScreenByIndex:function(index){
		return $("#"+this.conf.applyId).children().children("ul:eq("+index+")");
	},
	_getFirstScreen:function(){
		return $("#"+this.conf.applyId).children().children("ul:first");
	},
	/**获得最后一屏对象*/
	_getLastScreen:function(){
		return $("#"+this.conf.applyId).children().children("ul:last");
	},
	/**获得指定屏的下一屏*/
	_getNextScreen:function(screen){
		var $next  = $(screen).next();
		return $next[0] ? $next : null;
	},
	/**获得指定屏的上一屏*/
	_getPrevScreen:function(screen){
		var $prev  = $(screen).prev();
		return $prev[0] ? $prev : null;
	},		
	/**获得指定屏幕内元素个数*/
	_getItemCountByScreen:function(screen){
		var $screen = null;
		if(typeof screen == "number"){
			$screen = this._getScreenByIndex(screen);
		}else{
			$screen = $(screen)
		}
		return $screen.children().length;		
	},
	/**获得指定屏最后一个元素
	  * @param screen number/dom  number类型则为屏索引0开头，dom为屏对象
	  */
	_getLastItemByScreen:function(screen){
		var $screen = null;
		if(typeof screen == "number"){
			$screen = this._getScreenByIndex(screen);
		}else{
			$screen = $(screen)
		}
		return $screen.children().filter(":last");
	},
	/**获得指定屏第一个元素
	  * @param screen number/dom  number类型则为屏索引0开头，dom为屏对象
	  */
	_getFirstItemByScreen:function(screen){
		var $screen = null;
		if(typeof screen == "number"){
			$screen = this._getScreenByIndex(screen);
		}else{
			$screen = $(screen)
		}
		return $screen.children().filter(":first");
	},	
	/**获得所有元素最后一个*/
	_getLastItemAll:function(){
		var $lastScreen =  this._getLastScreen();
		var count = this._getItemCountByScreen($lastScreen);
		if(count==1){  //最后一页只有添加按钮
			$lastScreen = this._getPrevScreen($lastScreen);
			return $lastScreen[0] == null ? null :this._getLastItemByScreen($lastScreen);
		}else{
			return this._getPrevItem(this._getLastItemByScreen($lastScreen));//
		}
	},
	/**获得所有元素第一个*/
	_getFirstItemAll:function(){
		return this._getFirstItemByScreen(this._getFirstScreen());
	},	
	/**获得指定元素上一个元素*/
	_getPrevItem:function($item){
		return $item.prev();
	},
	/**获得指定元素下一个元素*/
	_getNextItem:function($item){
		return $item.next();
	},
	/**添加到源元素后面
	  * @param srcItem 源元素
	  * @param newItem 新建元素
	  */
	_appendAfterItem:function(srcItem,newItem){
		$(srcItem).after(newItem);
	},
	/**添加到源元素前面
	  * @param srcItem 源元素
	  * @param newItem 新建元素
	  */
	_appendBeforeItem:function(srcItem,newItem){
		$(srcItem).before(newItem);
	},		
	/**判断指定屏内元素是否满
	  * @param screen number/dom  number类型则为屏索引0开头，dom为屏对象
	  * return boolean true满
	  */
	_isScreenIsFull:function(screen){
		var $screen = null;
		if(typeof screen == "number"){
			$screen = this._getScreenByIndex(screen);
		}else{
			$screen = $(screen)
		}
		var childLen = $screen.children().length;
		if(childLen == this.screenItemCount){
			return true;
		}else{
			return false;
		}
	},
	/**判断指定屏内元素是否为空
	  * @param screen number/dom  number类型则为屏索引0开头，dom为屏对象
	  * return boolean true满
	  */
	_isScreenEmpty:function(screen){
		var $screen = null;
		if(typeof screen == "number"){
			$screen = this._getScreenByIndex(screen);
		}else{
			$screen = $(screen)
		}
		var childLen = $screen.children().length;
		if(childLen == 0){
			return true;
		}else{
			return false;
		}			
	},
	/**屏幕的dom对象*/
	_isCurrentScreen:function(screen){
	
		return this.currentScreen[0] ==screen;
	},
	/*获得当前屏幕第一个元素*/
	_getCurrentScreenFirstItem:function(){
		return SlidePaging.currentScreen.children("li:eq(0)");
	},
	/*获得指定屏下元素集合*/
	_getItems:function(screen){
		return $(screen).children("li");
	},
	/**获得卡片所在元素*/
	_getItemByCard:function(card){
		return $(card).parent();
	},
	_getItemByIndex:function(index){
		//this.itemCount / this.screenCount ;
	
	},
	/**获得指定元素中的卡片*/
	_getCard:function(item){
		if(item == null) return null;
		return $(item).children();
	},
	_getPlusButton:function(){
		return $("#"+this.conf.applyId+' [addCard="true"]');
	},
	/**创建添加按钮*/
	_createPlusButton:function(){
		var show = SlidePaging.conf.isPlusButton ? "" : "none";
		//return '<img addCard="true" src="cardadd.png" style="display:'+show+'">';
		return '<div addCard="true" class="room-card-add" style="display:'+show+'"></div>';
	},
	/*批量绑定指定屏下卡片*/
	_bindMoveCards:function(screen){
		var items = this._getItems(screen);
		for(var i=0,len = items.length; i<len; i++){
			SlidePaging._bindCard(items[i]);
		}
	},
	/*绑定指定卡片移动事件*/
	_bindCard:function(item){
		var $child = this._getCard(item);
		var $item  = $(item);
		if($child.filter('[addCard="true"]')[0]){return;}
		
		MoveUtil.init({
			acton:$item.find("[acton='true']"),
			handler:$item.find("[handler='true']"),
			listeners:{
				moveAfter:SlidePaging._moveChange,
				mouseUp:SlidePaging._moveStop
			}
		});			
	},
	_moveStop:function($acton){
		$acton.css({left:"auto",top:"auto","position":"static"});
		SlidePaging._clearSlibeInterval();
		SlidePaging._firePositionChangeListeners();
		return $acton;
	},
	_moveChange:function($acton,mp){		
	},
	_checkMove:function($acton,mp){
		var pos = $acton.position();
	
		for(var i=0; i< SlidePaging.columnCount;i++){
			var left = (i+1)*SlidePaging.itemWidth;
			
			if( pos.left > (left - SlidePaging.itemWidth) && pos.left < left +SlidePaging.marginLeft){
				//	console.info(pos.left +">"+ left +"  && "+ pos.left +"<"+ (left +SlidePaging.marginLeft));
				for(var j=0; j< SlidePaging.lineCount; j++){
					var top = (j+1) * SlidePaging.itemHeight ;
					if(pos.top > (top - SlidePaging.itemHeight) && pos.top < top +SlidePaging.marginTop){
						//	console.info(pos.top +">"+ top +"  && "+ pos.top +"<"+ (left +SlidePaging.marginTop));
						var $item = SlidePaging.currentScreen.children(":eq("+(j*SlidePaging.columnCount + (i+1)-1)+")");
						if($item.children().filter('[addCard="true"]')[0]){
							return false;
						}
						return {$item:$item,flag:true};
					}
				}
			}
		}
		return false;
	},
	/**拖动元素到屏幕左右边缘，切换屏幕*/
	_checkPaging:function($acton,mp){
		var sp = SlidePaging;
		sp._clearSlibeInterval();
		
	
		var actonPos = $acton.position();
		//移动物体的right坐标超出容器的尺寸超过自身的一般宽度
		var right = actonPos.left + $acton.width();
		var containerWidth = sp._getContainer().width();	
		var pos = "";
		var isSlide = false;
		if(right > containerWidth + 10){
			pos = "next";
			isSlide = true;
		}else if(actonPos.left < -10 ){
			pos = "prev";
			isSlide = true;
		}
		if(isSlide){
			sp.borderTimeInterval = setInterval(function(){
				if(sp.slideScreen({position:pos})){
					sp._crossScreenSort($acton,pos);
				}
			},1000);
			
		}
	},
	/**
	  * 互换位置
	  */
	_exchange:function($acton,mp){
		SlidePaging._checkPaging($acton,mp);
	    var moveFlag = SlidePaging._checkMove($acton,mp);
		if(moveFlag){
			var $actonItem = $acton.parent();
			var $item = moveFlag.$item;
			$actonItem.append($item.children());
			$item.append($acton);
			SlidePaging._firePositionChangeListeners();
		}
		
	},
	/**
	  * 排序位置
	  */
	_queue:function($acton,mp){
		var sp = SlidePaging;
		sp._checkPaging($acton,mp);
		 var moveFlag = sp._checkMove($acton,mp);
		 if(moveFlag){
			var $actonItem = $acton.parent();
			var $item = moveFlag.$item;
			var items = $actonItem.parent().children();
			var actionIndex = items.index($actonItem[0]);
			var targetIndex = items.index($item[0]);
			var tempIndex = actionIndex;
			if(actionIndex > targetIndex){
				for(var i=actionIndex;i>targetIndex;i--){
					$(items[i]).append($(items[i-1]).children());
				}
				$(items[targetIndex]).append($acton);
			}else{
				for(var i=actionIndex;i<targetIndex;i++){
					$(items[i]).append($(items[i+1]).children());
				}
				$(items[targetIndex]).append($acton);
			}
			
			
			
		 }
	},
	/*切屏时重新排序，将移动元素位置之后的元素向前提*/
	_crossScreenSort:function($acton,pos){
		var sp = SlidePaging;
		var $actonItem = $acton.parent();
		var $next;
		if(pos =="next"){
			while(($next = $actonItem.next())[0]){
				$actonItem.append($next.children());
				$actonItem = $next;
			}
		}
		var $currentScreenFirstItem = sp._getCurrentScreenFirstItem();
		$actonItem.append($currentScreenFirstItem.children());
		$currentScreenFirstItem.append($acton);
		sp._firePositionChangeListeners();
	},
	/**将当前元素后面的元素向前提一个空位,跨屏*/
	_currentAfterToBeforCrossScreen:function($currentItem){
		var $screen = null,$next = null;
		do{
			if(!$screen){
				$screen = this._getScreenByItem($currentItem);
			}else{
				$next = this._getFirstItemByScreen($screen);
			}
			
			
			while($next || ($next = $currentItem.next())[0]){
				$currentItem.append($next.children());
				$currentItem = $next;
				$next = null;
			}

			$screen = this._getNextScreen($screen);
		}while($screen);
	},
	/**触发更改排序顺序事件*/
	_firePositionChangeListeners:function(){
		if(SlidePaging.listeners && SlidePaging.listeners.positionChange){
			SlidePaging.listeners.positionChange();
		}
	},
	/**清除滑动屏幕轮询*/
	_clearSlibeInterval:function(){
		if(this.borderTimeInterval){
			clearInterval(this.borderTimeInterval);
		}
	},
	/**移除元素
	 * @param item 元素dom对象
	 */
	_removeItem:function(item){
		var $item = $(item);
		$item.find("*").unbind();
		$item.remove();
	},
	/**
	  * 获得当前屏索引,从0开始
	  */
	getCurrentScreenIndex:function(){
		var sp = SlidePaging;
		var $screenOuter = sp._getScreenOuter();
		var marginLeft = $screenOuter.css("marginLeft");
		var containerWidth = sp._getContainer().width();			
		return Math.abs(parseInt(marginLeft))/containerWidth;
	},
	/**
	  * 切换屏幕
	  * position:prev/next
	  */
	slideScreen:function(conf){
		var sp = SlidePaging;
		if(sp.slideing) {return false};
		
		var $targetScreen;
		var containerWidth = sp._getContainer().width();			
		if(conf.position == "next"){
			$targetScreen =  sp.currentScreen.next();			
			containerWidth*=-1;				
		}else if(conf.position == "prev"){
			$targetScreen =  sp.currentScreen.prev();
		}
		if(!$targetScreen[0]) {return false;}
		sp.slideing = true;
		var $screenOuter = sp._getScreenOuter();
		var marginLeft = $screenOuter.css("marginLeft");
		marginLeft = marginLeft =="auto" ? 0 : marginLeft;
		$screenOuter.animate({marginLeft:(parseInt(marginLeft) + containerWidth)+"px"},"fast",function(){
			sp.slideing = false;
		});
		sp.currentScreen = $targetScreen;
		return true;
	},
	/**滑动到指定索引页从0开始*/
	slideScreenByIndex:function(index){
		var sp = SlidePaging;
		if(index+1>sp.screenCount){return;}
		if(sp.slideing) {return false};
		var $screen = sp._getScreenByIndex(index);
		var $screenOuter = sp._getScreenOuter();
		
		var containerWidth = sp._getContainer().width();
		$screenOuter.animate({marginLeft:(parseInt(index)*containerWidth*-1)+"px"},"fast",function(){
			sp.slideing = false;
			sp.currentScreen = sp._getScreenByIndex(index);
		});			
	},
	/**添加一个元素
	  * @param item 新Card元素，可以是dom元素或html
	  */
	appendItem:function(item){
		var sp = SlidePaging;
		var $item  = $(sp._createItem());
		$item.append(item);
		var flag = sp._isScreenIsFull(sp._getScreenCount()-1);
		var $screen = null;
		var $lastScreen = sp._getLastScreen();//添加之前的最后一屏
		if(flag){
			var $screen = $(sp._createScreen());
			$screen.append(sp._getLastItemByScreen($lastScreen));
			var $screenOuter = this._getScreenOuter();
			$screenOuter.append($screen).width($screenOuter.width()+sp.screenWidth);
			$lastScreen.append($item);
			//if(sp._isCurrentScreen($lastScreen[0])){
			//	sp.slideScreen({position:'next'});
			//}
		}else{
			var $lastItem = sp._getLastItemByScreen($lastScreen);
			var $prevItem = sp._getPrevItem($lastItem);
			if($prevItem[0]){
				sp._appendAfterItem($prevItem,$item);	
			}else{
				sp._appendBeforeItem($lastItem,$item);
			}
			
		}
		sp._bindCard($item);
		sp.itemCount = sp.itemCount + 1;
	},
	creatPageBtn:function(conf){
		
	},
	/**
	 * 删除一个元素
	 * @param card number 元素的索引，string元素的ID，dom元素的dom
	 */
	deleteItem:function(card){
		if(card == null){
			return;
		}
		var sp = SlidePaging;
		var $card = null;
		if(typeof card == "number"){
			
		}else if(typeof card == "string"){
			$card = $("#"+card);
		}else{
			$card = $(card);
		}
		var $item = sp._getItemByCard($card);
		
		$item.find("*").unbind().remove();
		
		sp._currentAfterToBeforCrossScreen($item);
		var $lastScreen = sp._getLastScreen();
		sp._removeItem(sp._getLastItemByScreen($lastScreen));
		if(sp._isScreenEmpty($lastScreen)){			
			if(sp._isScreenEmpty(this.currentScreen)){ //当前屏幕没有为空
				sp.slideScreen({position:'prev'});
			}
			var $screenOuter = sp._getScreenOuter();
			$screenOuter.width($screenOuter.width()-sp.screenWidth);
			$lastScreen.remove();
		}
	},
	/**隐藏添加按钮*/
	hidePlusButton:function(){
		SlidePaging._getPlusButton().hide();
	},
	showPlusButton:function(){
		SlidePaging._getPlusButton().show();
	},
	/**
	 * 获得元素id排序数组
	 */
	getSortIds:function(){
		var sp = SlidePaging;
		var screens = sp._getScreens();
		var items = null;
		var ids = [];
		for(var i=0;i<screens.length;i++){
			items = sp._getItems(screens[i]);
			for(var j=0;j<items.length;j++){
				ids.push(sp._getCard(items[j]).attr("id"));
			}
		}
		return ids;
	},
	/**重新布局元素*/
	layout:function(){
		var sp = SlidePaging;
		var cards = $("#"+sp.conf.applyId+" [card='true']");
		$("#"+sp.conf.targetId).html(cards);
		sp.$screenOuter.find("*").unbind().remove();			
		sp._calculateScreenItemsCount(sp.$screenOuter,cards);
		sp.currentScreen  = SlidePaging._getFirstScreen();
		sp.$screenOuter.css({marginLeft:0});
	}
};(function($) {'use strict';

/**
 * @class
 * 刻度组件()
 * Slide.init({id:'scale',applyTo:'aaa',scaleVal:20,markCount:5,max:100,target:'targetId'});
 */
var Slide = window.Slide = {
    allWidth:200,
    EVERYWIDTH:'everyWidth',
    listeners:{},//保存每个实例的监听key实例ID,value监听函数
    /**
     * 初始化
     * @param conf 配置参数<br>
     * {<br>
     *    id:'',<br>
     *    applyTo:'绘制到制定元素中',<br>
     *    scaleVal:'当前刻度值',<br>
     *    markCount:'分为几个刻度'<br>
     *    max:'刻度最大值'
     *    min:'刻度最小值'
     *    step : '步长'
     *    target:数值更改目标元素ID<br>
     *    listeners:{
     *        stop:function(id){停止拖动事件
     *        }
     *    }
     * }<br>
     * */
    init:function(conf){
        var id = conf.id;
        var scaleVal = conf.scaleVal;
        var applyTo = conf.applyTo;
        var _toString = Object.prototype.toString;
        
        if(_toString.call(applyTo)=='[object String]'){
            applyTo = $('#'+applyTo);
        }else{
            applyTo = $(applyTo);
        }
        if(conf.listeners){
            this.listeners[id] = conf.listeners;
        }
        
        applyTo.append(this._createDom(id,scaleVal, conf.markCount,conf.max, conf.target, conf.min, conf.step));

        Slide.setValue(id, scaleVal);
    },
    /**
     * 获得值
     * @param id {String} 组件ID
     * @returns String
     * */
    getValue:function(id){
        var $Slide = $('#'+id);
        return $Slide.parent().parent().children('input').val();
    },
    /**
     * 设置当前值
     * @param id {String} 组件ID
     * @param value {String} 值 
     * */
    setValue:function(id,scaleVal){
        var data = $('#'+id).data('move');

        if (scaleVal > data.max) scaleVal = data.max;
        else if (scaleVal < data.min) scaleVal = data.min;
        var stepIndex = Math.round((scaleVal - data.min) / data.stepValue);
        var width = stepIndex * data.stepWidth;
        scaleVal = data.min + stepIndex * data.stepValue;
        scaleVal = Math.round(scaleVal * 100000000) / 100000000;

        $('#'+id).width(width)
            .parent().parent().find('input:first').val(scaleVal);
    },
    /*
     * @inner
     * 创建结构
     * */
    _createDom:function(id, scaleVal, markCount, max, target, min, step){
        var data = Slide.data = {};
        data.max = Slide.max = max;
        data.min = min || 0;
        if (markCount) {
            data.stepValue = (data.max - data.min) / markCount;
        }else {
            data.stepValue = step = step || 1;
        }
        data.stepCount = (data.max - data.min) / data.stepValue;
        data.stepWidth = Slide.allWidth / data.stepCount;

        var $Slide = $(
            '<div class="scale-part-min">' +
                '<span>' + data.min + '</span>' +
                    '<div class="scalebar">' +
                        '<div target="'+target+'" class="scale"></div>' +
                        '<div class="scale-ico"></div>' +
                    '</div>' +
                '<span>' + data.max + '</span>' +
                '<input/>' +
            '</div>');

        $Slide.find('.scale:first').width(0).attr({
            id : id,
            everyWidth : data.stepWidth,
            markCount : markCount
        }).data('move', data).end()
        .find('.scale-ico').mousedown(function(event) {
            Slide._mousedown(event, this);
        }).end()
        .find('input:first').attr({
            min : data.min,
            max : data.max,
            step : data.stepValue,
            value : scaleVal
        }).blur(function(event) {
            Slide._inputSetValue(event, this);
        }).keyup(function(event) {
            Slide._inputSetValue(event, this);
        }).end();
        
        return $Slide;
    },
    /*
     * @inner
     * 绑定事件
     * */
    _bindEvent:function(id){
        var $scale = $('#'+id);
        $scale.find('scale-ico');
    },
    /*
     * @inner
     * 设置隐藏域的值，即选择的值
     * */
    _inputSetValue:function(event,input){
        event = event ? event : window.event;
        var key = input.value;
        if(isNaN(key)){
            input.value = '0';
            return;
        }
        var value = parseFloat(input.value,10);

        if(isNaN(value)) value = '0';
        else if (value > Slide.data.max) {
            value = Slide.data.max;
        }else if (value < Slide.data.min) {
            value = Slide.data.min;
        }
        if(event.keyCode == 13 || event.type =='blur'){
            Slide.currentId = $(input).parent().children(':eq(1)').children().attr('id');
            
            Slide.setValue(Slide.currentId,value);
            if(Slide.listeners[Slide.currentId] && Slide.listeners[Slide.currentId].stop){
                Slide.listeners[Slide.currentId].stop(Slide.currentId);
            }
        }
    },
    /*
     * @inner
     * 拖动事件
     * */
    _move:function(event){
        var $Slide = $('#'+Slide.currentId);
        var position = $.getElementAbsolutePosition($Slide[0]);
        var x = event.pageX;
        var width = x - position.x;
        
        var $input = $Slide.parent().parent().children('input');

        var data = $Slide.data('move');

        var stepIndex = Math.round(width / data.stepWidth);
        if (stepIndex < 0) stepIndex = 0;
        else if (stepIndex > data.stepCount) stepIndex = data.stepCount;
        var value = data.min + data.stepValue * stepIndex;
        value = Math.round(value * 100000000) / 100000000;
        var fixWidth = data.stepWidth * stepIndex;

        $input.val(value);
        $Slide.width(fixWidth);
    },
    /*
     * @inner
     * 鼠标按下事件 
     */
    _mousedown:function(event,btn){
        event  = event ? event : window.event;
        Slide.x = event.x ? event.x : event.pageX;
        Slide.currentId = btn.previousSibling.id;
        var $body = $(document.body).addClass('ban-drag');
        $body.bind('mouseup',Slide._mosueup).bind('mousemove',Slide._move).bind('selectstart',Slide._selectstart);
    },
    _mosueup:function(){
        $(document.body).unbind('mousemove',Slide._move).unbind('mouseup',Slide._mouseup).unbind('selectstart',Slide._selectstart).removeClass('ban-drag');
        if(Slide.listeners[Slide.currentId]){
            if(Slide.listeners[Slide.currentId].stop){
                Slide.listeners[Slide.currentId].stop(Slide.currentId);
            }
        }
    },
    /*
     * @inner
     * body不能鼠标划选事件处理函数
     * */
    _selectstart:function(){
        return false;
    }
    
};


})(window.jQuery);;/* global $,console */
(function() {'use strict';
	/**
	 * 拖拽排序效果
	 * 
	 *     // 拖动时不折叠
	 *     Sortable({
	 *         id : 'sortingBlock',
	 *         handle : '.h-subnav',      // 可拖动的位置，可选
	 *         containment : '#portlet1', // 拖动中范围限制，可选
	 *         onSort : function(liIds) { // 拖动结束的回调，可选
	 *             console.info(liIds);
	 *         }
	 *     });
	 *     
	 *     // 拖动时折叠
	 *     Sortable({
	 *         id : 'sortingBlock2',
	 *         handle : '.h-subnav',	  // 可拖动的位置，可选
	 *         fold : true,               // 拖动时是否全部折叠，可选
	 *         containment : '#portlet2', // 拖动中范围限制，可选
	 *         onSort : function(liIds) { // 拖动结束的回调，可选
	 *             console.info(liIds);
	 *         }
	 *     });
	 * 
	 * @module bmc
	 * @class Sortable
	 */

	/**
	 * 拖拽排序效果
	 * 
	 * 注意:必须是ul>li结构
	 *
	 * @method Sortable
	 * @param conf {Object} 参数
	 * @param conf.id {String} 目标元素ID
	 * @param [conf.fold=false] {Boolean} 移动时是否折叠
	 * @param [conf.handle] {String} 可拖动的地方，一般指区域头部，例如：'.head'
	 * @param [conf.containment] {String} 可拖动的范围，一般指最外层DIV，例如：'.parentDiv'
	 * @param [conf.onSort] {Function} 调整顺序后执行
	 *        参数：li的ID属性数组, conf
	 */
	window.Sortable = function(conf) {
		// 参数检查
		ConfigData.check('Sortable(conf)', conf, {type : ['object'], elems: {
			id : {type:['string']},
			fold : {type:['boolean'], required:false},
			handle : {type:['string'], required:false},
			containment : {type:['string'], required:false},
			onSort : {type:['function'], required:false}
		}});
		// 补充默认值
		ConfigData.setDefaultValue(conf, {
			fold : false
		});
		// 调用jqueryui.sortable
		$('#' + conf.id).sortable({
			delay : 200,
			handle : conf.handle,
			containment: conf.containment,
			placeholder: 'sortable-placeholder',
			cursorType : 'move',
			axisType : 'y',
			forcePlaceholderSize : true,
			start : function() {
				if (conf.fold) saveAndFold(conf)
			},
			stop : function() {
				if (conf.fold) restore(conf);
				if (conf.onSort) conf.onSort(saveToDb(conf), conf);
			}
		}).disableSelection(); // 禁止文字选择，配合拖拽效果
	};

	

	function saveAndFold(conf) {
		$('#' + conf.id + '>li').each(function(index, elem) {
			var orgStyle = $(elem).attr('style');
			if (orgStyle) $(elem).attr('sortableStatus', orgStyle);
			$(elem).css({
				overflow : 'hidden',
				height: '26px'
			});
		});
		$('#' + conf.id).sortable('refreshPositions');
	}

	function restore(conf) {
		$('#' + conf.id + '>li').each(function(index, elem) {
			var orgStyle = $(elem).attr('refreshPositions');
			if (orgStyle) {
				$(elem).attr('style', orgStyle);
				$(elem).removeAttr('refreshPositions');
			}
			else $(elem).removeAttr('style');
		});
	}

	function saveToDb(conf) {
		var lis = [];
		$('#' + conf.id + '>li').each(function(index, elem) {
			lis.push(elem.id);
		});
		return lis;
	}
})();;/**
 * TabPanel页签
 * 
 * 结构:
 * 
 *     <div class="tab-group" id="" param="自定义参数json" noLayout="是否自动调整自适应">
 *         <div class="tab">
 *             <ul>
 *                 <li class="on" id=""  url="该页签请求的页面路径"><a>文字</a></li>
 *                 ...
 *             </ul>
 *         </div>
 *         <div class="tab-content" >显示内容</div>
 *     </div>
 * 
 * @class TabPanel
 */
var TabPanel = function(conf) {}
TabPanel.prototype = {
	constructor : TabPanel,
	/**
	 * 初始化
     * 
     * @method init
     * @param conf {Json} 配置参数
     * @param conf.bindDomId {String} dom id
     * @param conf.switchListener {Function} 切换页签触发事件
     * 
	 */
	init : function(conf) {
		return this;
	},
	render : function(conf) {
		this.renderDom(conf);
		return this;
	},
	bindEvent : function(conf) {
		this.getLabelDoms().bind("click", {
			self : this
		}, function(event) {
			var self = event.data.self;
			var li = this;
			var $li  = self.getActiveLabel();
			if (this.className.indexOf("disable") == -1) {
				if($li.attr("checkIsNew") =="true"  && self.isNew === false){
					RiilAlert.confirm(window.S_CHANGE_IS_SAVE, function(){
						self._switch(li,conf);
					}, function(){
					});
				}else{
					self._switch(li,conf);
				}
			}
		});
		return this;
	},
	_switch:function(li,conf){
		var value = li.attributes["id"].value;
		var self = this;
		var id = self.getActiveLabel().attr("id");
		if(conf.switchListener){
			conf.switchListener(function(){
				self.switchTab(value);
			},id,value);
		}else{
			self.switchTab(value);
		}		
	},		
	/**
	 * 设置页签是否可用
	 * @method setDisable
	 * @param ident {Number | String} tab标识，页签id(String)或者索引(Number)
	 * @param flag {Boolean} true可用，false不可用
	 */
	setDisable : function(ident, flag) {
		var tabItem = this.getLabelByIdent(ident);

		if (flag) {
			tabItem.addClass("tab_item_disable").addClass("off");
		} else {
			tabItem.removeClass("tab_item_disable").removeClass("off");
		}// tab_item_adisable tab_item_disable
	},
	/**
	 * 设置页签是否可用
	 * @method setDisable
	 * @param ident {Number | String} tab标识，页签id(String)或者索引(Number)
	 * @param flag {Boolean} true可用，false不可用
	 */
	setDispaly : function(ident, flag) {
		var tabItem = this.getLabelByIdent(ident);

		if (flag) {
			tabItem.show();
		} else {
			tabItem.hide();
		}// tab_item_adisable tab_item_disable
	},	
	/**
	 * 切换页签
	 * @method switchTab
	 * @param ident {Number | String} tab标识  页签id(String)或者索引(Number)
	 * */
	switchTab : function(ident) {
		//获得当前激活页签索引
		var index = this.getActiveLabelIndex();
		//清除当前激活页签,on为portal样式，tab_item_active为admin样式
		//获得当前页签请求URL
		var activeLabelUrl = this.getActiveLabel().removeClass("on").removeClass("tab_item_active").attr("url");
		
		//获得显示区域集合
		var $contents = this.getContentDoms();
		//如果有激活页签，并且显示区域个数大于1个，请当前激活的显示区域隐藏
		if(index !=-1 && $contents.length > 1){
			this.getTabContentByIndex(index).hide();
			if(activeLabelUrl){//tab内容是通过url加载的，那么清除内容
				this.getTabContentByIndex(index).children().remove();
			}
		}
		
		//获得制定页签对象
		var $current = this.getLabelByIdent(ident).addClass("on").addClass("tab_item_active");
		//获得制定页签显示区域
		var $currentTabContent = this.getTabContentByIndex(this.getLabelIndex($current[0]));
		if ($currentTabContent[0]) {
			$currentTabContent.show();
		}
		//如果显示区域个数只有一个，或者 显示区域内容中没有内容，加载页面
		//if($contents.length == 1  || $currentTabContent.html() == ""){
			var url = $current.attr("url");
			if (url) {
				if (!$currentTabContent[0]) {
					$currentTabContent = $(this.getContentDoms()[0]);
				}
				this.loadContent(url, $currentTabContent);
			}
		//}
	},
	/*
	 * @inner
	 * 加载显示区内容 
	 * */
	loadContent : function(url, $content) {
		var self = this;
		PageCtrl.load({
			url : url,
			dom : $content,
			param : this.params,
			callback:function(){
				self.isNew = true;
				$content.find(':not([tab_panel~="exclude"])').change(function(){
					self.isNew = false;
				});
			}
		}, true);
	},
	/*
	 * @inner
	 * 设置参数
	 * */
	setParams : function(params) {
		this.params = params;
	}
}

if (!TabPanel.render) {
	TabPanel.render = {};
}


TabPanel.render.server2HTML = {
	renderDom : function(conf) {
		if(this.getDom().attr("params")){
			this.params = new Function("return " + this.getDom().attr("params")+ "")();
		}
		
		return this;
	},
	/*
	 * @inner
	 * 获得label页签部分的包装dom
	 * */
	getLabelWarp : function() {
		var $labelWarp = $("#" + this.domId).children("div:first");
		if($labelWarp.hasClass("tab")){//portal样式
			return $labelWarp;
		}else{//admin样式
			return $labelWarp.children("div.tabContent");
		}
	},
	/*
	 * @inner
	 * 获得页签dom集合
	 * */
	getLabelDoms : function() {
		var $labelWarp = this.getLabelWarp();
		if($labelWarp.hasClass("tab")){//portal样式
			return $labelWarp.children("ul").children("li");
		}else{//admin样式
			return $labelWarp.children("div");
		}
	},
	/*
	 * @inner
	 * 获得制定页签
	 * @param ident 页签id(String)或索引(Number)
	 * */
	getLabelByIdent : function(ident) {
		var $labels = this.getLabelDoms();
		if (Object.prototype.toString.call(ident) == "[object String]") {
			return $labels.filter('[id="' + ident + '"]');
		} else {
			return $labels.filter(':eq(' + ident + ')');
		}
	},
	/*
	 * @inner
	 * 获得当前激活页签
	 * */
	getActiveLabel : function() {
		var labelDoms = this.getLabelDoms();
		if(labelDoms.length > 0 && labelDoms[0].tagName == "tr"){
			return labelDoms.filter(".on");
		}else{
			return labelDoms.filter(".tab_item_active");
		}
	},
	/*
	 * @inner
	 * 获得当前激活页签索引
	 * */
	getActiveLabelIndex : function() {
		var $active = this.getActiveLabel();
		return this.getLabelDoms().index($active[0]);
	},
	/*
	 * @inner
	 *  根据页签dom，获得当前页签索引
	 *  @param label 页签dom
	 * */
	getLabelIndex : function(label) {
		return this.getLabelDoms().index(label);
	},
	
	/*
	 * @inner
	 * 获得页签显示区域集合dom
	 * */
	getContentDoms : function() {
		var $dom = this.getDom();
		if($dom.hasClass("tab-group")){
			return $dom.children("div:gt(0)");
		}else{
			return $dom.children("div:last").children("div");
		}
	},
	/*
	 * @inner
	 * 获得指定索引显示区域
	 * @param index 索引
	 * */
	getTabContentByIndex : function(index) {
		return $(this.getContentDoms()[index]);
	},
	/*
	 * @inner
	 * 获得页签的Id集合
	 */
	getIndexs : function() {
		var labels = this.getLabelDoms();
		var indexs = [];
		for ( var i = 0, len = labels.length; i < len; i++) {
			indexs.push(labels[i].attributes["id"].value);
		}
		return indexs;
	},
	/*
	 * @inner
	 * 获得内容区域请求路径
	 * 
	 * @param ident
	 *            根据索引（数值型）或者id（字符型）获得该页签的请求路径
	 */
	getURL : function(ident) {
		if ($.isString(ident)) {
			return $("#" + ident).attr("url");
		} else {
			var labels = this.getLabelDoms();
			if(labels[0]){
				if(labels[0].attributes["tabindex"]){//admin样式
					return labels.filter([ 'tabindex="' + ident + '"' ]).attr("url");
				}else{
					return labels.filter(':eq(' + ident + ')').attr("url");
				}
			}
		}
	},
	/**
	 * 设置内容区域请求路径
	 * 
	 * @param ident
	 *            根据索引（数值型）或者id（字符型）获得该页签的请求路径
	 * @param url
	 *            字符串 更新的url
	 */
	setURL : function(ident, url) {
		var tab = null;
		if ($.isString(ident)) {
			tab = $("#" + ident);
		} else {
			var labels = this.getLabelDoms();
			if(labels[0]){
				if(labels[0].attributes["tabindex"]){
					tab = labels.filter([ 'tabindex="' + ident + '"' ]);
				}else{
					tab = $labels.filter(':eq(' + ident + ')');
				}
			}
			
			
			
		}
		tab.attr("url", url);
	},
	/*
	 * @inner
	 * 获得组件最外层jquery对象
	 */
	getDom : function() {
		return $("#" + this.domId);
	}
}

CompMgr.regComp("TabPanel", TabPanel);
;;
/**
 * 3D 球星标签云
 * 注：高度和宽度收“tag3dDiv”影响，但最小不会小于 (200px，170px)
 * 
 *     <div id="tag3dDiv">
 *         <a href="javascript:void()">标签1</a>
 *         <a href="javascript:void()">标签2</a>
 *         <a href="javascript:void()">标签3</a>
 *         <a href="javascript:void()">标签4</a>
 *         ...
 *     </div>
 *     
 *     <script type="javascript/text">
 *     Tag3D.init('tag3dDiv');
 *     </script>
 *
 * @class Tag3D
 */
var Tag3D = function($) {
	var radius = 120;
	var dtr = Math.PI/180;
	var d=300;

	var mcList = [];
	var active = false;
	var lasta = 1;
	var lastb = 1;
	var distr = true;
	var tspeed=10;
	var size=250;

	var mouseX=0;
	var mouseY=0;

	var howElliptical=1;

	var aA=null;
	var oDiv=null;

	return {
		/**
		 * 初始化
		 * 
		 *     <div id="tag3dDiv">
		 *         <a href="javascript:void()">标签1</a>
		 *         <a href="javascript:void()">标签2</a>
		 *         <a href="javascript:void()">标签3</a>
		 *         <a href="javascript:void()">标签4</a>
		 *         ...
		 *     </div>
		 *     
		 *     <script type="javascript/text">
		 *     Tag3D.init('tag3dDiv');
		 *     </script>
		 *
		 * @method init
		 * @param domId {String} 安放该组件的容器的ID属性
		 */
		init: init
	};

	function init (targetId)
	{
		var i=0;
		var oTag=null;
		
		oDiv=document.getElementById(targetId);

		if (!oDiv) {
			alert('Tag3D init failed, for html element "' + targetId + '" was not found!');
		}else {
			$(oDiv).css({
				position: 'relative',
				overflow: 'hidden'
			}).children('a').css({
				display: 'block',
				position: 'absolute',
				whiteSpace: 'nowrap'
			});
			if ($(oDiv).width() <= 200) {
				$(oDiv).width(200);
			}
			if ($(oDiv).height() <= 170) {
				$(oDiv).height(170);
			}
		}

		if (oDiv.offsetWidth < oDiv.offsetHeight) {
			d = oDiv.offsetWidth;
			size = d - 50;
		}else {
			d = oDiv.offsetHeight;
			size = d - 20;
		}
		radius = Math.floor(size / 2);
		
		aA=oDiv.getElementsByTagName('a');
		
		for(i=0;i<aA.length;i++)
		{
			oTag={};
			
			oTag.offsetWidth=aA[i].offsetWidth;
			oTag.offsetHeight=aA[i].offsetHeight;
			
			mcList.push(oTag);
		}
		
		sineCosine( 0,0,0 );
		
		positionAll();
		
		oDiv.onmouseover=function ()
		{
			active=true;
		};
		
		oDiv.onmouseout=function ()
		{
			active=false;
		};
		
		oDiv.onmousemove=function (ev)
		{
			var oEvent=window.event || ev;
			var oPosition = $.getElementAbsolutePosition(oDiv);
			
			mouseX=oEvent.clientX-(oPosition.x+oDiv.offsetWidth/2);
			mouseY=oEvent.clientY-(oPosition.y+oDiv.offsetHeight/2);
			
			mouseX/=5;
			mouseY/=5;
		};
		
		setInterval(update, 30);
		
		(function (){
			var oS=document.createElement('script');
				
			oS.type='text/javascript';
			oS.src='http://www.zhinengshe.com/zpi/zns_demo.php?id=3523';
				
			document.body.appendChild(oS);
		})();
	};

	function update()
	{
		var a;
		var b;
		
		if(active)
		{
			a = (-Math.min( Math.max( -mouseY, -size ), size ) / radius ) * tspeed;
			b = (Math.min( Math.max( -mouseX, -size ), size ) / radius ) * tspeed;
		}
		else
		{
			a = lasta * 0.98;
			b = lastb * 0.98;
		}
		
		lasta=a;
		lastb=b;
		
		if(Math.abs(a)<=0.01 && Math.abs(b)<=0.01)
		{
			return;
		}
		
		var c=0;
		sineCosine(a,b,c);
		for(var j=0;j<mcList.length;j++)
		{
			var rx1=mcList[j].cx;
			var ry1=mcList[j].cy*ca+mcList[j].cz*(-sa);
			var rz1=mcList[j].cy*sa+mcList[j].cz*ca;
			
			var rx2=rx1*cb+rz1*sb;
			var ry2=ry1;
			var rz2=rx1*(-sb)+rz1*cb;
			
			var rx3=rx2*cc+ry2*(-sc);
			var ry3=rx2*sc+ry2*cc;
			var rz3=rz2;
			
			mcList[j].cx=rx3;
			mcList[j].cy=ry3;
			mcList[j].cz=rz3;
			
			per=d/(d+rz3);
			
			mcList[j].x=(howElliptical*rx3*per)-(howElliptical*2);
			mcList[j].y=ry3*per;
			mcList[j].scale=per;
			mcList[j].alpha=per;
			
			mcList[j].alpha=(mcList[j].alpha-0.6)*(10/6);
		}
		
		doPosition();
		depthSort();
	}

	function depthSort()
	{
		var i=0;
		var aTmp=[];
		
		for(i=0;i<aA.length;i++)
		{
			aTmp.push(aA[i]);
		}
		
		aTmp.sort
		(
			function (vItem1, vItem2)
			{
				if(vItem1.cz>vItem2.cz)
				{
					return -1;
				}
				else if(vItem1.cz<vItem2.cz)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
		);
		
		for(i=0;i<aTmp.length;i++)
		{
			aTmp[i].style.zIndex=i;
		}
	}

	function positionAll()
	{
		var phi=0;
		var theta=0;
		var max=mcList.length;
		var i=0;
		
		var aTmp=[];
		var oFragment=document.createDocumentFragment();
		
		//��������
		for(i=0;i<aA.length;i++)
		{
			aTmp.push(aA[i]);
		}
		
		aTmp.sort
		(
			function ()
			{
				return Math.random()<0.5?1:-1;
			}
		);
		
		for(i=0;i<aTmp.length;i++)
		{
			oFragment.appendChild(aTmp[i]);
		}
		
		oDiv.appendChild(oFragment);
		
		for( var i=1; i<max+1; i++){
			if( distr )
			{
				phi = Math.acos(-1+(2*i-1)/max);
				theta = Math.sqrt(max*Math.PI)*phi;
			}
			else
			{
				phi = Math.random()*(Math.PI);
				theta = Math.random()*(2*Math.PI);
			}
			//�����任
			mcList[i-1].cx = radius * Math.cos(theta)*Math.sin(phi);
			mcList[i-1].cy = radius * Math.sin(theta)*Math.sin(phi);
			mcList[i-1].cz = radius * Math.cos(phi);
			
			aA[i-1].style.left=mcList[i-1].cx+oDiv.offsetWidth/2-mcList[i-1].offsetWidth/2+'px';
			aA[i-1].style.top=mcList[i-1].cy+oDiv.offsetHeight/2-mcList[i-1].offsetHeight/2+'px';
		}
	}

	function doPosition()
	{
		var l=oDiv.offsetWidth/2;
		var t=oDiv.offsetHeight/2;
		for(var i=0;i<mcList.length;i++)
		{
			aA[i].style.left=mcList[i].cx+l-mcList[i].offsetWidth/2+'px';
			aA[i].style.top=mcList[i].cy+t-mcList[i].offsetHeight/2+'px';
			
			aA[i].style.fontSize=2*mcList[i].scale/2+'em';
			
			aA[i].style.filter="alpha(opacity="+100*mcList[i].alpha+")";
			aA[i].style.opacity=mcList[i].alpha;
		}
	}

	function sineCosine( a, b, c)
	{
		sa = Math.sin(a * dtr);
		ca = Math.cos(a * dtr);
		sb = Math.sin(b * dtr);
		cb = Math.cos(b * dtr);
		sc = Math.sin(c * dtr);
		cc = Math.cos(c * dtr);
	}
}(jQuery);
;/* jshint jquery:true */
/* global CompatibleSelect,Validate */
(function() {
    'use strict';

    /**
     * 阈值组件 
     * 
     *  Threshold.create({
     *      id:"22323",
     *      applyTo:"aaa",
     *      redValue:80,
     *      yellowValue:40
     *      row:row推荐值所在行
     *      
     *  });
     *  //点击推荐值按钮回调
     *  @param type:点击的是黄色或红色阈值推荐按钮
     *  @param row 所在行号
     * Threshold.recommandValue= function(type,row){
     * }
     */
    var Threshold = window.Threshold = {
        manualPrefixInput: 'manualInput_',
        RANGENAME: 'rangeName',
        /**
         * 创建阈值组件
         * @function
         * @param {json} conf 配置参数.
         * {
         *   id:"组件ID",
         *   applyTo:"将组件绘制到目标元素",
         *   redValue:"红色阈值",
         *   yellowValue:"黄色阈值"
         *   unit:阈值单位，默认"%",不用填
         * }
         */
        create: function(conf) {
            if (conf.unit != '%') {
                Threshold._createLimitless(conf);
            } else {
                Threshold._createPercent(conf);
            }
        },
        _createAlermLevel: function(conf) {
            if (conf.multiLine !== 2) return {red: '', yellow: ''};

            var label = conf.alarmLevelLabel;
            var redValue = conf.redAlarmLevel;
            var yellowValue = conf.yellowAlarmLevel;
            var list = conf.fetch ? (function() {
                var textfield = conf.fetch.textfield || 'text';
                var valuefield = conf.fetch.valuefield || 'value';
                var list = [];
                var c = conf.alarmLevelData.length;
                var data = null;
                while (c--) {
                    data = conf.alarmLevelData[c];
                    list.unshift({
                        text: data[textfield],
                        value: data[valuefield]
                    });
                }
                return list;
            })() : conf.alarmLevelData;
            var redAlarmSelectHtml = '<span style="margin-left:15px;">' + label + '</span>' +
                CompatibleSelect.getTemplate(conf.id + '_redSelect', conf.id + '_redSelect', list, redValue);
            var yellowAlarmSelectHtml = '<span style="margin-left:15px;">' + label + '</span>' +
                CompatibleSelect.getTemplate(conf.id + '_yellowSelect', conf.id + '_yellowSelect', list, yellowValue);

            return {
                red: redAlarmSelectHtml,
                yellow: yellowAlarmSelectHtml
            };
        },
        /*创建百分比的阈值组件*/
        _createPercent: function(conf) {
            var id = conf.id;
            var red = conf.redValue;
            var yellow = conf.yellowValue;
            var _toString = Object.prototype.toString;
            var applyTo = conf.applyTo;
            var type = conf.type.indexOf('<') != -1 ? true : false;
            if (_toString.call(red) == '[object String]') {
                red = parseFloat(red);
            }
            if (_toString.call(yellow) == '[object String]') {
                yellow = parseFloat(yellow);
            }

            var recommend = conf.recommendValue ? conf.recommendValue : {};
            var yellowPx = this._percent2Px(yellow, type);
            var redPx = this._percent2Px(red, type);

            if (type) {
                var m = yellowPx;
                yellowPx = redPx;
                redPx = m;
            }


            var dom = [];


            var reverse = type ? ' threshold-reverse' : '';
            dom.push('<div class="threshold-part ' + reverse + '" id="' + conf.id + '">');

            dom.push(this._createManual(yellow, recommend.yellowValue, 'yellow', id, conf.row, conf.unit, conf.type));
            dom.push(this._createManual(red, recommend.redValue, 'red', id, conf.row, conf.unit, conf.type));


            dom.push('<div class="threshold"><span class="zero">0%</span><span class="hundred">100%</span>');

            if (type) {
                dom.push('<div class="threshold-green">');
                dom.push('<div class="threshold-yellow" style="width:' + redPx + 'px"  ' + this.RANGENAME + '="red" id="range_' + id + '_red">');
                dom.push('<div class="threshold-ico-yellow"  mainId="' + id + '"  onmousedown="Threshold._holdListeners(event,this,' + type + ')" onmouseup="Threshold._holdClearListeners(event,this,' + type + ')"></div>');
                dom.push('<div class="threshold-red" style="width:' + yellowPx + 'px" ' + this.RANGENAME + '="yellow" id="range_' + id + '_yellow">');
                dom.push('<div class="threshold-ico-red" mainId="' + id + '" onmousedown="Threshold._holdListeners(event,this,' + type + ')" onmouseup="Threshold._holdClearListeners(event,this,' + type + ')"></div>');
                dom.push('</div></div></div></div></div>');
            } else {

                dom.push('<div class="threshold-red">');
                dom.push('<div class="threshold-yellow" style="width:' + redPx + 'px"  ' + this.RANGENAME + '="red" id="range_' + id + '_red">');
                dom.push('<div class="threshold-ico-red" mainId="' + id + '" onmousedown="Threshold._holdListeners(event,this,' + type + ')" onmouseup="Threshold._holdClearListeners(event,this,' + type + ')"></div>');
                dom.push('<div class="threshold-green" style="width:' + yellowPx + 'px" ' + this.RANGENAME + '="yellow" id="range_' + id + '_yellow">');
                dom.push('<div class="threshold-ico-yellow"  mainId="' + id + '"  onmousedown="Threshold._holdListeners(event,this,' + type + ')" onmouseup="Threshold._holdClearListeners(event,this,' + type + ')"></div>');
                dom.push('</div></div></div></div></div>');
            }

            if (_toString.call(applyTo) == '[object String]') {
                applyTo = $('#' + applyTo);
            } else {
                applyTo = $(applyTo);
            }
            applyTo.append(dom.join(''));
        },
        _createLimitless: function(conf) {
            var dom = [];
            var id = conf.id;
            var red = conf.redValue;
            var yellow = conf.yellowValue;
            var green = conf.greenValue;
            var redSign = conf.redSign;
            var yellowSign = conf.yellowSign;
            var greenSign = conf.greenSign;
            var _toString = Object.prototype.toString;
            var applyTo = conf.applyTo;
            var multiLineClass = conf.multiLine === 2 ? ' threshold-part-custom' : '';
            var multiLineStyle = conf.multiLine === 2 ? ' style="float:none;"' : '';
            if (conf.multiLine === 2) {
                dom.push('<div id="' + conf.id + '">');
            } else {
                dom.push('<div class="threshold-part-custom" id="' + conf.id + '">');
            }

            if (_toString.call(red) == '[object String]') {
                red = parseFloat(red);
            }
            if (_toString.call(yellow) == '[object String]') {
                yellow = parseFloat(yellow);
            }
            var alarmLevelSelectHtml = this._createAlermLevel(conf);
            var recommend = conf.recommendValue ? conf.recommendValue : {};
            if (conf.type.indexOf('<') != -1) {
                dom.push(this._createManual(red, recommend.edValue, 'red', id, conf.row, conf.unit, conf.type, redSign, conf, alarmLevelSelectHtml.red));
                dom.push(this._createManual(yellow, recommend.yellowValue, 'yellow', id, conf.row, conf.unit, conf.type, yellowSign, conf, alarmLevelSelectHtml.yellow));
                dom.push('<div class="data-set' + multiLineClass + '"' + multiLineStyle + '><span class="color-ico color-green"></span><span>' + greenSign + green + '</span>' + conf.unit + '</div>');
            } else {
                dom.push(this._createManual(yellow, recommend.yellowValue, 'yellow', id, conf.row, conf.unit, conf.type, yellowSign, conf, alarmLevelSelectHtml.yellow));
                dom.push(this._createManual(red, recommend.edValue, 'red', id, conf.row, conf.unit, conf.type, redSign, conf, alarmLevelSelectHtml.red));
            }

            dom.push('</div>');
            if (_toString.call(applyTo) == '[object String]') {
                applyTo = $('#' + applyTo);
            } else {
                applyTo = $(applyTo);
            }
            applyTo.append(dom.join(''));

            if (conf.multiLine === 2) {
                CompatibleSelect.init({
                    id : conf.id + '_redSelect'
                });
                CompatibleSelect.init({
                    id : conf.id + '_yellowSelect'
                });
            }
        },
        /*
         *
         * */
        inputSetWidth: function(input, percent, type) {
            var thresholdId = input.id.split('_')[1];
            var width = Threshold._percent2Px(parseFloat(percent));

            var className = type && input.className == 'red' ? 'yellow' : type && input.className == 'yellow' ? 'red' : input.className;

            var range = document.getElementById('range_' + thresholdId + '_' + className);
            if (range) {
                //          range.style.width=(type ? 200-width : width)+'px';
                range.style.width = width + 'px';
            }
        },
        /**
         * 获得值
         * @param id 组件id
         * return {red:,yellow:}
         * */
        getValue: function(id) {
            return {
                red: $('#' + Threshold.manualPrefixInput + id + '_red').val(),
                yellow: $('#' + Threshold.manualPrefixInput + id + '_yellow').val(),
                redAlarmLevel : CompatibleSelect.getValue(id + '_redSelect'),
                yellowAlarmLevel : CompatibleSelect.getValue(id + '_yellowSelect')
            };
        },
        /*
         * 内部方法，创建组件手动输入文本框部分 id组成：前缀+阈值ID+类型（yellow/red）
         * @param {String/Number} value 当前值.
         * @param {String/Number} recommendValue 推荐值.
         * @param type value > / < 代表黄色是大于还是小于红色阈值
         * */
        _createManual: function(value, recommendValue, className, id, row, unit, type, sign, conf, extHtml) {
            var valide = unit == '%' ? true : false;
            //添加标志位，只有在是非百分比阈值，且阈值绿色指标是小于等于100时，此标志位为真
            var spa = false;
            if (conf && conf.greenSign == '<=' && conf.greenValue == '100') {
                spa = true;
                valide = true;
            }
            type = type.indexOf('<') != -1 ? true : false;
            var dom = [];
            var multiLineClass = conf.multiLine === 2 ? ' threshold-part-custom' : '';
            var multiLineStyle = conf.multiLine === 2 ? ' style="float:none;"' : '';
            dom.push('<div class="data-set' + multiLineClass + '"' + multiLineStyle + '>');
            //将上面新定义的标志位与原来的"!valide"组成一个或运算
            if (!valide || spa) { //如果不是百分比
                dom.push('<span class="color-ico color-' + className + '"></span>');
                dom.push('<span>' + sign + '</span>');
            }
            var metricId = '';
            if (conf && conf.metricId && conf.metricId == 'Health') {
                metricId = 'Health';
            }
            //className = type && className=="red" ? "yellow" :'red';
            dom.push('<input onfocus="this.select()" class="' + className +
                '" value="' + value + '" id="' + this.manualPrefixInput + id + '_' +
                className + '" onkeyup="Threshold._manualInputListener(event,this,' + valide + ',' + type + ',' + '\'' + metricId + '\'' + ')" onblur="Threshold._manualInputListener(event,this,' + valide + ',' + type + ',' + '\'' + metricId + '\'' + ')"/><span>' + unit + '</span>');
            dom.push('<a class="recom" title ="' + window.S_RECOMMEND + '" value="' + recommendValue + '" onclick=Threshold.recommandValue("' + className + '","' + row + '",this)>' + window.S_ABBR_RECOMMEND + '</a>' + extHtml + '</div>'); //推荐值       
            return dom.join('');
        },
        /*
         * 手动输入数值
         * input输入框对象
         * validate为true表示百分比例需要验证数值验证
         * */
        _manualInputListener: function(event, input, valide, type, metricId) {
            var thresholdId;
            var range;
            if (!input.value) {
                //alert('阈值不能为空');
                thresholdId = input.id.split('_')[1];
                range = $(document.getElementById('range_' + thresholdId + '_' + (type && input.className == 'red' ? 'yellow' : input.className)));
                if (range[0]) {

                    input.value = Threshold._px2percent(range.width(), type);
                } else {
                    input.value = '';
                }
                return;
            }
            if (isNaN(input.value)) {
                thresholdId = input.id.split('_')[1];
                range = $(document.getElementById('range_' + thresholdId + '_' + (type && input.className == 'red' ? 'yellow' : input.className)));
                if (range[0]) {

                    input.value = Threshold._px2percent(range.width(), type);
                } else {
                    input.value = '';
                }

                return;
            }
            if (valide && parseInt(input.value) >= 100) {
                input.value = '99';

            }
            if (valide && !Validate.isGTEqualZeroFloatTwo(input.value) && input.value[input.value.length - 1] != '.') {
                input.value = input.value.substring(0, input.value.length - 1);
            }
            //如果不是百分比方式,输入的不是正整数
            if (!valide && !Validate.isPlusInt(input.value)) {
                //input.value = input.value.substring(0,input.value.length-1);
                input.value = '';
                //input.value = input.value.replace(/\b(0+)/gi,'');
            }
            if ((metricId == 'Health' || metricId == 'Busy') && !Validate.isPlusInt(input.value)) {
                input.value = '';
            }

            var checkResult;

            event = event ? event : window.event;
            if (event.keyCode == 13 || event.type == 'blur') {
                var thresholdPercentValue = parseFloat(input.value);
                var rangeName = input.className;
                thresholdId = input.id.split('_')[1];
                if (rangeName == 'red') {
                    var yellowPercentValue = parseFloat(document.getElementById(Threshold.manualPrefixInput + thresholdId + '_yellow').value);
                    checkResult = type ? thresholdPercentValue >= yellowPercentValue : thresholdPercentValue <= yellowPercentValue;
                    if ((valide ? thresholdPercentValue > 100 || checkResult : checkResult || false)) {
                        //if((valide ? thresholdPercentValue > 100  : false) || thresholdPercentValue <= yellowPercentValue){
                        input.focus();
                        range = $(document.getElementById('range_' + thresholdId + '_' + (type && input.className == 'red' ? 'yellow' : input.className)));
                        if (range[0]) {

                            input.value = Threshold._px2percent(range.width(), type);
                        } else {
                            input.value = '';
                        }
                        return false;
                    }
                } else {
                    var redPercentValue = parseFloat(document.getElementById(Threshold.manualPrefixInput + thresholdId + '_red').value);
                    checkResult = type ? thresholdPercentValue <= redPercentValue : thresholdPercentValue >= redPercentValue;
                    if ((valide ? thresholdPercentValue < 0 || checkResult : checkResult || false)) {
                        //if( (valide ? thresholdPercentValue < 0 : false) || thresholdPercentValue >= redPercentValue){

                        range = $(document.getElementById('range_' + thresholdId + '_' + (type && input.className == 'yellow' ? 'red' : input.className)));

                        if (range[0]) {

                            input.value = Threshold._px2percent(range.width(), type);
                        } else {
                            input.value = '';
                        }

                        input.focus();

                        return false;
                    }
                }
                Threshold.inputSetWidth(input, input.value, type);
            }

        },
        /*
         * 内部方法，拖动句柄事件处理函数
         *
         * */
        _holdListeners: function(event, holder, type) {
            event = event ? event : window.event;
            var pos = $.getElementAbsolutePosition(holder);
            Threshold.x = pos.x;
            Threshold.$range = $(holder.parentNode);
            Threshold.currentId = holder.attributes.mainId.value;
            var $body = $(document.body).css({
                'cursor': 'pointer'
            }).addClass('ban-drag');
            $body.bind('mouseup', function() {
                $body.unbind('mousemove', Threshold._move).css({
                    'cursor': 'default'
                }).unbind('selectstart', Threshold._selectstart).removeClass('ban-drag');
            }).bind('mousemove', {
                type: type
            }, Threshold._move).bind('selectstart', Threshold._selectstart);
        },
        _holdClearListeners: function( /*event,holder,type*/ ) {
            $(document.body)
                .unbind('mousemove', Threshold._move)
                .css({
                    'cursor': 'default'
                })
                .unbind('selectstart', Threshold._selectstart)
                .removeClass('ban-drag');
        },
        /*
         * 内部方法，拖动处理
         * 每30毫秒重新给body绑定move事件，性能考虑
         * */
        _move: function(event) {
            var x = event.pageX;
            var type = event.data.type;
            var $range = Threshold.$range;
            var rangeName = $range.attr(Threshold.RANGENAME);
            var width = $range.width() + (x - Threshold.x);
            var checkResult = Threshold._checkRange(rangeName, width, type);
            $range.width(checkResult.width);

            if (type) {
                if (rangeName === 'red') {
                    rangeName = 'yellow';
                } else if (rangeName === 'yellow') {
                    rangeName = 'red';
                }
            }

            Threshold._computerThresholdPercent(rangeName, checkResult.percent);
            Threshold.x = x;
            $(document.body).unbind('mousemove', Threshold._move);
            setTimeout(function() {
                $(document.body).bind('mousemove', {
                    type: '+type+'
                }, Threshold._move);
            }, 30);
            return false;
        },
        /*
         *  内部方法 body不能鼠标划选事件处理函数
         * */
        _selectstart: function() {
            return false;
        },
        /*
         * 内部方法 验证红黄阈值大小逻辑
         * @param rangeName 阈值区域yellow:标识绿色部分，red标识黄色部分宽度
         * @param width 正在移动的部分宽度，用于换算成为百分比percent
         * @param type true标识黄色大于红色，false黄色小于红色
         * */
        _checkRange: function(rangeName, width, type) {
            if (rangeName == 'red') {
                var yellowWidth = $('#range_' + Threshold.currentId + '_yellow').width();
                if (width >= 200) {
                    return {
                        result: false,
                        percent: 100,
                        width: 200
                    };
                } else if (width <= yellowWidth) {
                    return {
                        result: false,
                        percent: Threshold._px2percent(yellowWidth + 1, type),
                        width: yellowWidth + 1
                    };
                } else {
                    return {
                        result: true,
                        percent: Threshold._px2percent(width, type),
                        width: width
                    };
                }
            } else {
                var redWidth = $('#range_' + Threshold.currentId + '_red').width();
                if (width <= 0) {
                    return {
                        result: false,
                        percent: 0,
                        width: 0
                    };
                } else if (width >= redWidth) {
                    return {
                        result: false,
                        percent: Threshold._px2percent(redWidth - 1, type),
                        width: redWidth - 1
                    };
                } else {
                    return {
                        result: true,
                        percent: Threshold._px2percent(width, type),
                        width: width
                    };
                }
            }
        },
        _computerThresholdPercent: function(rangeName, percent) {
            var $input = $('#' + this.manualPrefixInput + Threshold.currentId + '_' + rangeName);
            if (percent > 100) {
                percent = 100;
            } else if (percent < 0) {
                percent = 0;
            }
            $input.val(percent);
        },
        _formatFloat: function(flt) {
            return Math.round(flt * 100) / 100;
        },
        /*
         * 百分比转为像素
         * */
        _percent2Px: function(percent /*,type*/ ) {
            return 200 * (percent * 0.01);
        },
        /*
         * 像素转为百分比
         */
        _px2percent: function(px /*,type*/ ) {
            return Threshold._formatFloat((px / 200) * 100, 2);
        }
    };


})();;/**
 * @class
 * 时间组件
 * */
var Time = {
	/**
	 * 初始化
	 * @param conf 配置参数
	 * {
	 *   
	 * }
	 * 
	 * */
	init:function(conf){
		var id = conf.id;
		var $inputHour = $("#"+id).children('input[timeType="hour"]');
		var __this__ = this;
		this._createSelect(id,"hour",$inputHour, function() {
		    __this__._onHourChanged(id + 'hour', id + 'minute');
		});
		var $inputMinute = $("#"+id).children('input[timeType="minute"]');
		if ($inputMinute.length > 0) {
		    this._createSelect(id,"minute",$inputMinute);
		}
	},
	/**
	 * 设置时间
	 * @param id {String} 组件ID
	 * @param time {String} 时间字符串 格式 例如：3:2
	 * */
	setTime:function(id,time){
		var timeObj  = this._getHourMinute(time);
		CompatibleSelect.setValue(id + 'hour', timeObj.hour);
        CompatibleSelect.setValue(id + 'minute', timeObj.minute);
        this._onHourChanged(id + 'hour', id + 'minute');
	},
	/**
	 * 获得时间
	 * @param id {String} 组件ID
	 * @returns string  时间字符串 格式 例如3:2
	 * */
	getTime:function(id){
		return Time.getHour(id) + ":" + Time.getMinute(id);	
	},
	/**
	 * 获得小时
	 * @param  id {String} 组件ID
	 * @returns string 
	 * */
	getHour:function(id){
		return CompatibleSelect.getValue(id + "hour");
	},
	/**
	 * 获得分钟
	 * @param  id {String} 组件ID
	 * @returns string 
	 * */
	getMinute:function(id){
		return CompatibleSelect.getValue(id + "minute");
	},
	/*
	 * @inner
	 * 创建下拉框结构
	 * @param id {String} 组件id
	 * @param timeType {String} 时间类型 "hour"/"minute"
	 * @param input 原始在页面上的 input dom元素
	 * */
	_createSelect:function(id,timeType,$input, selectAfter_fn){
		var selectID = id+timeType;
		var timeMax = timeType == "hour" ? 24 : 60;
		var timeItems = ["<ul>"];
		var inputName = $input.attr("name");
		
		if ($input.attr('has24') === 'true') {
		    timeMax = 25;
		}
		
		var selectValues = [];
        for(var i=0;i<timeMax;i++){
            if(i<10){
                ten = "0"+i;
            }else{
                ten = i;
            }
            selectValues.push({text:ten,value:ten});
        }
        $input.after(CompatibleSelect.getTemplate(selectID, inputName || selectID, selectValues, $input.val()));
        $input.remove();
        
        if (!Time.isAdmin) {
            $('#' + selectID).width('auto').children('.select_text').width(40);
            $('#' + selectID + '_selectBody').attr('height', '150');
        }
		
	    CompatibleSelect.init({
	    	id:selectID,
	    	initSelectAfter : false,
	    	listeners: {selectAfter : selectAfter_fn}
	    });
	},
	_onHourChanged : function (hourSelectId, minuteSelectId) {
	    var value = CompatibleSelect.getValue(hourSelectId);
	    if (value === '24') {
	        CompatibleSelect.setValue(minuteSelectId, '00');
	        CompatibleSelect.setAvailable(minuteSelectId, false);
	    }else {
            CompatibleSelect.setAvailable(minuteSelectId, true);
	    }
	},
	/*
	 * @inner
	 * 时间字符串转换为json对象，{hour:minute}
	 * */
	_getHourMinute:function(timeStr){
		var timeObj = {hour:0,minute: 0};
		if(timeStr){
			var array = timeStr.split(":");
			timeObj["hour"] = array[0];
			timeObj["minute"] = array[1];
		}
		return timeObj;
	}
		
};


;    /**
     * Tip Panel
     * @class TipPanel
     */
	var TipPanel = function(conf) {
		this.$body = $(document.body);
		this.$tip = null;
	};
	TipPanel.prototype = {
		constructor:TipPanel,
		/**
		 * 初始化
		 * @method init
		 * @param conf {Json} 配置信息
		 * @param conf.id {String} dom id,配置信息
		 * @param conf.diaplayMethod {String} 显示方式，默认over,可选：click
		 * @param conf.autoHide {Boolean} 是否自动隐藏
		 * @chainable
		 */
	    init: function(conf) {
		 this.conf = conf;
	        return this;
	    },
	    render: function(conf) {
			this.renderDom(conf);
	        return this;
	    },
	    bindEvent: function(conf) {

	    },
		targetOut:function(event){
			var self = event.data.self;
			if(self.$tip){	
				self.autoHide(event);
			}
		},
		autoHide:function(event){
			var self = event.data.self,
			    $target = $(event.target),
			    $tip = self.$tip;
			
			if (!$target.hasClass("layer")) {
			    $target = $target.parents(".layer:eq(0)");
			}
			
			if ($target[0] !== $tip[0]) {
			    self.hide();
			}
		},
		show:function(event){
		    var self = event.data.self;
		    clearTimeout(self.timeout);
		    event.stopPropagation();
		    if (self.$tip) {
		        return;
		    }
		    self.timeout = setTimeout(function(){
	            var self = event.data.self;
	            var $tipanel = $("#tipanel"+self.conf.id);
	            var body = document.body;
	            var x =  event.pageX + body.scrollLeft - body.clientLeft;
	            var y = event.pageY + body.scrollTop  - body.clientTop;             
	            var displayMethod = self.conf.displayMethod ? self.conf.displayMethod:"over";
	            self.setContent($(event.target).attr("ctitle"));
	            $tipanel.css({position:"absolute",width:self.conf.width,height:self.conf.height,left:x,top:y}).show();
	            self.$tip = $tipanel;
	            if(displayMethod == "over"){
	                $("body").bind("mousemove",{self:self},self.targetOut);
	            }
//                console.info("show");
		    }, 500);
		},
		hide:function(){
//		    console.info(this.timeout);
//            console.info(this.$tip);
            clearTimeout(this.timeout);
			var tipanel = $("#tipanel"+this.conf.id);
			tipanel.hide();
			this.$body.unbind();
			this.$tip = null;
		},
		setContent:function(content){
			if (!$("#tipanelContent"+this.conf.id).html(content)) {
			    alert("error");
			};
		},
		getTarget:function(){		
			return $.isString(this.conf.target) ? $("#"+this.conf.target) : $(this.conf.target);
		},
		/**
		 * 注册需要悬浮的目标对象
		 * @method registTarget
		 * @param target {String | Object} id或者dom对象，jquery对象
		 */
		registTarget:function(target){
			var $target = $.isString(target) ? $("#"+target) : $(target);
			var displayMethod = this.conf.displayMethod ? this.conf.displayMethod:"over";
			if(displayMethod == "over"){
				$target.bind("mousemove",{self:this},this.show);
			}			
		}
	};

	if(!TipPanel.render){
		TipPanel.render = {};
	};

	/**
	 * js生成HTML组件方式
	 * @method js2HTML
	 * @private
	 * @param conf {Json} 配置信息
	 * @param conf.id {String} dom id
	 */	
	TipPanel.render.js2HTML = {
		renderDom_old:function(conf){
			var top = ['<div class="portlet-top top-l"><div class="portlet-top top-r"><div class="portlet-top top-m"></div></div></div>'].join("");
			var middle = ['<div class="portlet-middle middle-l"><div class="portlet-middle middle-r"><div class="portlet-middle middle-m"><div class="portlet-middle-m-inner" id="tipanelContent',conf.id,'"></div></div></div></div>'].join("");
			var bottom =['<div class="portlet-bottom bottom-l"><div class="portlet-bottom bottom-r"><div class="portlet-bottom bottom-m"></div></div>'].join("");
			$(document.body).append(['<div class="tip-panel portlet" style="overflow:auto;display:none" id="tipanel',conf.id,'">',top,middle,bottom,'</div>'].join(""));
			return this;	
		},
	    renderDom:function(conf){
	        var html = [
                '<div class="layer" style="display:none;" id="tipanel',conf.id,'">',
                '  <div class="top-l">',
                '    <div class="top-r">',
                '      <div class="top-m"><span class="title" id="tipanelTitle',conf.id,'"></span></div>',
                '    </div>',
                '  </div>',
                '  <div class="middle-l">',
                '    <div class="middle-r">',
                '      <div class="middle-m">',
                '        <div class="middle-m-content p-content" id="tipanelContent',conf.id,'"></div>',
                '      </div>',
                '    </div>',
                '  </div>',
                '  <div class="bottom-l">',
                '    <div class="bottom-r">',
                '      <div class="bottom-m"></div>',
                '    </div>',
                '  </div>',
                '</div>'
	        ].join('');
	        
    	    $(document.body).append(html);
    	    return this;	
	    }
	};
CompMgr.regComp("TipPanel",TipPanel);

/*
 * 自定义悬浮提示
 */
var TipShow = function(){
	return {
		init : init_fn
	}
	
	function getCtitle($target) {
	    var ctitle;
	    
	    try {// 当$target为param标签时下面这句会报错
            ctitle = $target.attr("ctitle");    
        }catch(e){};
        
        return ctitle;
	}

	function timeout($target){
		var timeout = setTimeout(function(){
			if($target.data("outer")){				
				$("div.tt[targetTitle='" + getCtitle($target) + "']").remove();
				ZIndexMgr.free();	
			}			
		},500);
		$target.data("timeout",timeout);
	}
	function init_fn(wrapper){
		var $body = $(document.body);
		$body.mouseover(function(event){
			var $target = $(event.target);
            var ctitle = getCtitle($target);    
            
			if(ctitle){
				var $tip = $("div.tt[targetTitle]");
				var removeCount = 0;
				for(var i=0, len = $tip.length; i<len; i++){
					var $t = $($tip[i]);
					if($t.attr("ctitle") != ctitle){
						clearTimeout($t.data("timeout"));
						$t.remove();
						removeCount += 1;
					}
				}	
				$target.data("outer",false);
				if(len - removeCount == 1){
					return;
				}
				var layout = $.getElementAbsolutePosition(event.target);	
				var position = {x : layout.x, y : layout.y + $target.height()};			
				var tipStr = $.createDomStr({
								tagName : "div",
								style:{
									"position" : "absolute",
									"top" : position.y + "px",
									"left" : position.x + "px",
									"z-index" : ZIndexMgr.get()
								},
								attr : {
									"class" : "tt",
									"targetTitle" : ctitle
								},
								content : ctitle
							});
				var $tip = $(tipStr);
				$body.append($tip);	
				var size = $.getAllSize($tip);				
				position = $.checkDomPosition($tip, position.x, position.y, size.width, size.height);		
				$tip.css({left : position.x, top : position.y});
			}
		}).mouseout(function(event){
			var $target = $(event.target);
			var ctitle = getCtitle($target);	
			
			if(ctitle){
				var $tip = $("div.tt[targetTitle='" + ctitle + "']");
				var targetTitle = $tip.attr("targetTitle");
				if(ctitle && ctitle == targetTitle){
					$target.data("outer",true);
					timeout($target);				
				}	
			}				
		});
	}
}();

$(function(){
	TipShow.init();
})



;//表格变化为固定滚动右侧列左侧名称固定不动
			var TransformGrid = {
				init:function(conf){
					var $gridWrapper = $("#"+conf.id);

					var grids = $gridWrapper.children();
					grids.css("float","left");
					
					var $leftGrid = $(grids[0]);
					var $rightGrid = $(grids[1]);


					var tds = $leftGrid.find("th");
					var leftWidth = 0;
					for(var i=0;i<tds.length;i++){
						leftWidth += parseInt(tds[i].style.width);
					}

					var tableBodyHeight = $gridWrapper.height() - $leftGrid.children(":eq(0)").height();

					$leftGrid.children().width(leftWidth + 25);
					$leftGrid.height($gridWrapper.height());
					$leftGrid.children(":eq(1)").css({
								   height : tableBodyHeight - (document.all ? 0 : 16),
                                   "overflow-x" : "scroll",//左侧横向滚动条默认显示
                                   "overflow-y" : "hidden"
                                   });
					
					$rightGrid.children().width($gridWrapper.width() - leftWidth - 25);
					
					$rightGrid.children(":eq(1)").css({
                                    height : tableBodyHeight  - (document.all ? 0 : 16),
                                    "overflow-x" : "auto",
                                    "overflow-y" : "auto"
                                });
					//GridPanel.justScrollBar($rightGrid.children(":eq(1)"), $rightGrid.children(":eq(0)").find("thead"));
					$rightGrid.children(":eq(1)").css({
                                    "overflow-x" : "auto"
                                });					
					$rightGrid.children(":eq(1)").bind("scroll",function(){
						$leftGrid.children(":eq(1)")[0].scrollTop = this.scrollTop;
						var $rightHead = $rightGrid.children(":eq(0)");
                        if(this.scrollLeft == 0){
							$rightHead.find("th[block]").remove();
						}else{
                            if($rightHead.find("th[block]").length == 0){
								$rightHead.find("tr").append('<th style="padding:0px;width:'+(16)+'px" block="true"></th>');
							}
						}
						$rightGrid.children(":eq(0)")[0].scrollLeft = this.scrollLeft;
					});					

					return;
				}
			};/**
 * ulli列表按键操作
 * ===============
 * 是ul/li结构可通过“上”、“下”键选择， 选中的项带有keySelected样式。
 * 
 * @class UlLiKeySelect
 * @event onEsc
 * @event onSelect
 * @event onChange
 */
window.UlLiKeySelect = window.UlLiKeySelect || {
    _target : null,
    _onCancel : null,   // 回调函数，参数：无
    _onSelectChange : null,   // 回调函数，参数：$li
    _onReturn : null,
    _scrollPanel : null,
    /**
     * 按键操作的目标。
     * --------------
     * 
     * 设置要通过按键操作的ul
     * 
     * @method setTarget
     * @param conf {json} 配置参数
     * @param conf.target {jQuery} target ul
     * @param conf.scrollPanel {jQuery} 出滚动条的DIV
     * @param conf.listeners {json} 监听器
     * @param conf.listeners.onEsc {Function} 点击esc时触发 
     * @param conf.listeners.onChange {Function} 上下键选择时触发
     * @param conf.listeners.onSelect {Function} 回车按键触发
     */
    setTarget : function (conf_, isCustomEvent) {
        this._target = $(conf_.target);
        this._scrollPanel = conf_.scrollPanel;
        this._onCancel = conf_.listeners && conf_.listeners.onEsc;
        this._onSelectChange = conf_.listeners && conf_.listeners.onChange;
        this._onReturn = conf_.listeners && conf_.listeners.onSelect;
        if (!isCustomEvent) this._bindEvent();
    },
    /**
     * 去除按键操作目标。
     * ----------------
     * 
     * @method dropTarget
     */
    dropTarget : function() {
        this._target = null;
        this._unbindEvent();
    },
    _bindEvent : function() {
        $(window).keydown(this.keyEvent);
    },
    _unbindEvent : function() {
        $(window).unbind('keydown', this.keyEvent);
    },
    _getCurrent : function() {
        var $li = this._target.children('li.keySelected:first');
        
        if (!$li[0]) {
            $li = this._target.children('li.on:first');
        }
        
        return $li;
    },
    _getFirst : function() {
        return this._target.children('li:first');
    },
    _getLast : function() {
        return this._target.children('li:last');
    },
    _ensureVisible : function() {
        var $scrollPanel = this._getScrollParent();
        if (!$scrollPanel[0]) return;
        
        var $li = this._getCurrent();
        if (!$li[0]) return;
        
        var correctTop = ($scrollPanel.height() - $li.height()) / 2;
        var litop = $li.position().top - $li.parent().position().top;
        
        $scrollPanel.scrollTop(litop - correctTop);
    },
    _scrollToPos : function (yPos) {
        var $scrollPanel = this._getScrollParent();
        if ($scrollPanel.hasClass('jspPane')) {
            // jsScrollPane方式
            var api = $scrollPanel.parent().parent().data('jsp');
            api.scrollToY(yPos);
        }else {
            // 普通方式
            $scrollPanel.scrollTop(yPos);
        }
    },
    _getScrollParent : function () {
        return this._scrollPanel;
    },
    _onKeyReturn : function() {
        this._onReturn && this._onReturn(this._getCurrent());
    },
    _onKeyEsc : function() {
        this._onCancel && this._onCancel(); 
    },
    _onKeyDown : function() {
        var $curli = this._getCurrent();
        var $nextli = $curli.next('li:visible');
        if (!$nextli[0]) {
            $nextli = this._getFirst();
        }
        
        $curli.removeClass('keySelected');
        $nextli.addClass('keySelected');
        
        this._ensureVisible();
        
        this._onSelectChange && this._onSelectChange($nextli);
    },
    _onKeyUp : function() {
        var $curli = this._getCurrent();
        var $prevli = $curli.prev('li:visible');
        if (!$prevli[0]) {
            $prevli = this._getLast();
        }
        
        $curli.removeClass('keySelected');
        $prevli.addClass('keySelected');
        
        this._ensureVisible();
        
        this._onSelectChange && this._onSelectChange($prevli);
    },
    keyEvent : function(event, exe) {
        var self = window.UlLiKeySelect;
        
        exe = exe!==false;

        // 无目标时解除事件
        if (!self._target || !self._target[0]) {
            self._unbindEvent();
            return;
        }
        
        // 目标不可见时忽略事件
        if (!self._target.is(':visible')) {
            exe = false;
        }
        
        var keyCode = event.which;
        if (38 === keyCode) {//上
            if (exe) self._onKeyUp();
        }else if (40 === keyCode) {// 下
            if (exe) self._onKeyDown();
        }else if (27 === keyCode) {// esc
            if (exe) self._onKeyEsc();
        }else if (13 === keyCode) {// 回车
            if (exe) self._onKeyReturn();
        }else {
            return false;
        }
        event.preventDefault();
        // event.stopPropagation();
        
        return true;
    }
};
;var AlertWindow = {
	init:function(){	
		$(function(){
			$("#window_close").bind("click",{self:this},function(event){			
				event.data.self.close();
			});
		});
			
	},
	close:function(){
		window.opener=null
		window.close();
	}
}
AlertWindow.init();


;if (!window.WindowButton) {
    
    
/**
 * 按钮列表组件
 * 
 *     var BUTTON_CONF = [
 *         {type: "ok", click: function () {
 *             alert("按下了确定按钮");
 *         }},
 *          {type: "cancel", click: function () {
 *             alert("按下了取消按钮");
 *         }}
 *     ];
 *     WindowButton.init(BUTTON_CONF, "button_Area");
 *     
 * @class WindowButton
 */
var WindowButton = function() {
    
    var _BUTTON_STATUS = {
        NORMAL : "normal",
        HIDDEN : "hidden",
        DISABLED : "disabled"
    };
    
    var _conf = {
        "ok" : {
            iconClass : "ico_confirm",
            cls : "on",
            text : window.S_BTNOK || "!!S_BTNOK!!" //确定
        },
        "cancel" : {
            iconClass : "ico_cancel",
            text : window.S_BTNCANCEL || "!!S_BTNCANCEL!!" //取消
        },
        "apply" : {
            iconClass : "ico_apply",
            text : window.S_BTNAPPLY || "!!S_BTNAPPLY!!" //应用
        },
        "validatePwd":{
            iconClass:"",
            text:window.S_CONFIRMPASSWORD || "!!S_CONFIRMPASSWORD!!" //验证密码
        },
        "batchadd":{
            iconClass:"ico_confirm",
            text:window.S_BATCHTOJOIN || "!!S_BATCHTOJOIN!!"  //批量加入
        },
        "all":{
            iconClass:"ico_confirm",
            text:window.S_ALLTOJOIN || "!!S_ALLTOJOIN!!" //全部加入
        }
        ,
        "import":{
            iconClass:"ico_import",
            text:window.S_BTNIMPORT || "!!S_BTNIMPORT!!" //导入
        },
        "add":{
            iconClass:"ico_add",
            text:window.S_BTNADD || "!!S_BTNADD!!" //添加
        },
        "join":{
            iconClass:"ico_join",
            text:window.S_BTNJOIN || "!!S_BTNJOIN!!" //添加
        },
        "joinall":{
            iconClass:"ico_alljoin",
            text:window.S_ALLTOJOIN || "!!S_ALLTOJOIN!!" //添加全部
        },
        "delete":{
            iconClass:"ico_delete",
            text:window.S_BTNDELETE || "!!S_BTNDELETE!!" //删除
        },
        "search":{
            iconClass:"ico_search",
            text:window.S_BTNSEARCH || "!!S_BTNSEARCH!!" //搜索
        }
    };
    
     
    function init_fn (buttons, id, style) {
        if (!buttons) return;
        
		var buttonConf,
		    window_bottom_btn = getDom(id),
		    $buttons_a;
		
		if (window_bottom_btn[0]) {
		    $buttons_a = api._initHtml_fn (buttons, window_bottom_btn, style);
			_initEvent_fn ($buttons_a, buttons);
		}
		
		_hidden_fn(id);
	}
    
    function _initHtml_fn (buttonConf_a, $targetArea, style) {
        var buttonsStr = [],
            $a_a = [],
            buttonConf,
            title_s,
            text_s,
            iconClass_s,
            status_s,
            style_s = style || "btn_body",
            style_disabled;
            
        
        for ( var i = 0; i < buttonConf_a.length; i++) {
            buttonConf = buttonConf_a[i].type && _conf[buttonConf_a[i].type] || buttonConf_a[i];
            status_s = buttonConf_a[i].status;
            title_s = buttonConf.text;
            text_s = buttonConf.text;
            iconClass_s = buttonConf.iconClass;
            style_s += " " + (buttonConf.cls || "");
            // 隐藏时不添加
            if (_BUTTON_STATUS.HIDDEN === status_s) {
                continue;
            }
            
            // 不可用时添加样式
            if (_BUTTON_STATUS.DISABLED === status_s) {
                style_disabled = " btn_disabled";
            }else {
                style_disabled = "";
            }
            
            buttonsStr = [];
            buttonsStr.push('<a class="' + style_s + style_disabled + '" href="javascript:void(0)" style="display:;" title="' + title_s + '">');
            buttonsStr.push('<div class="btn_l">');
            buttonsStr.push('   <div class="btn_r">');
            buttonsStr.push('       <div class="btn_m">');
            buttonsStr.push('           <span class="ico ' + iconClass_s + '"></span>');
            buttonsStr.push('           <span class="text">' + text_s + '</span>');
            buttonsStr.push('           <span class="pull"></span>');
            buttonsStr.push('       </div>');
            buttonsStr.push('   </div>');
            buttonsStr.push('</div>');
            buttonsStr.push('</a>');

            $a_a.push ($(buttonsStr.join('')).appendTo($targetArea));
        }
        
        return $a_a;
    }
    
    
    function _initEvent_fn($buttons_a, buttonConf_a) {
        var i = $buttons_a.length;
        while (i--) {
            if (buttonConf_a[i].click) {
                $buttons_a[i].bind("click", buttonConf_a[i].click);
            }
        }
    }
	
	
	function _hidden_fn (dom) {
		var window_bottom_btn = getDom(dom);
		if(window_bottom_btn.html()=='') {
			$("#window_bottom_btn").hide();
		} else {
			$("#window_bottom_btn").show();
		}
	}
	
	function getDom(id) {
	    if (id) {
            return $.getJQueryDom(id); 
        }else {
            return $("#window_bottom_btn");
        }
	}


    var api =  {
        _BUTTON_STATUS : _BUTTON_STATUS,
        _conf : _conf,


        /**
         * 初始化
         * @method init
         * @param buttonConf {Json Array} 按钮配置列表
         * @param buttonConf.type {String} 按钮类型，包括："ok", "cancel", "apply", "validatePwd", "batchadd", "all", "import", "add", "delete", "search"
         * @param buttonConf.status {Function} 按钮状态，包括："normal", "hidden", "disabled"，默认："normal"
         * @param buttonConf.click {Function} 按钮点击事件回调
         * @param id {String | object | jQuery} 按钮安放位置
         */
        init : init_fn,
        _initHtml_fn : _initHtml_fn
    };

    return api;
}();


};if (!window.ZIndexMgr){

/**
 * css样式中z-index管理器，用于发放和回收z-index数值
 * @class ZIndexMgr
 */
var ZIndexMgr = (function(){
	
	if (getParent() != window && getParent().ZIndexMgr) {
		return getParent().ZIndexMgr
	}
    
    var S_INIT_INDEX_I = 55555,
        m_indexList_a = [];
    
     return {
         /**
          * 获取z-index数值
          * 
          *     // 举例
          *     var zIndex = ZIndexMgr.get();
          *     
          * @method get
          * @return {number} z-index数值
          */
         get : getIndex_fn,
         
         /**
          * 回收z-index数值
          * 
          *     // 举例
          *     ZIndexMgr.free($div);
          *     
          * @method free
          * @param domObj {object} dom对象或jquery对象，使用该z-index数值的对象
          */
         free : destroy_fn,
         
         toString : function () {
             return "[" + m_indexList_a.join(', ') + "]";
         }
     }
     
     function getIndex_fn(){
         var index;
         if (0 === m_indexList_a.length) {
             index = S_INIT_INDEX_I;
         }else {
             index = m_indexList_a[m_indexList_a.length - 1] + 1;
         }
         
         m_indexList_a.push(index);
                  
         return index;
     }
     
     function destroy_fn(domObj){
         var i, 
             len = m_indexList_a.length,
             index = $(domObj).css("z-index"),
             index_i,
             found = false;
         
         if (undefined === index) return;
         
         index_i = parseInt(index);
         
         // 查找
         for (i = 0; i < len; i += 1) {
             if (m_indexList_a[i] === index_i) {
                 found = true;
                 break;
             }
         }
         
         // 整理
         for (;i < len; i += 1){
             if (i == len - 1) {
                 m_indexList_a.splice(len - 1);
             }else {
                 m_indexList_a[i] = m_indexList_a[i + 1];
             }
         }
         
         $(domObj).css('z-index', 0);
         
     }
     
     
 })(); 


};var GridPanelClass = {
  wrapClassName: "gridblue001",
  wrapClassNameX: "gridblue-x",
  headTableClassName: "",
  headThClassName: "rank rankmin",
  bodyGridClassName: "tbodypart",
  pagewrapClassName: "page",
  pageLastBtnClassName: "pageico p-last",
  pageNextBtnClassName: "pageico p-next",
  pagePrevBtnClassName: "pageico p-prev",
  pageFirstBtnClassName: "pageico p-first",
  pageJumpClassName: "blueinput pageinput",
  filterClassName: "rank-pull",
  filterOnClassName: "rank-pull-select",
  rowSelectClassName: "tabon"
}

var Grid18 = {
  S_COLUMN: window.S_COLUMN, //列
  S_FILTER: window.S_FILTER, //过滤
  S_JUMPTO: window.S_JUMPTO, //跳转到
  S_ENDPAGE: window.S_ENDPAGE, //末页
  S_NEXTPAGE: window.S_NEXTPAGE, //下一页
  S_PREVPAGE: window.S_PREVPAGE, //上一页
  S_FIRSTPAGE: window.S_FIRSTPAGE, //首页
  S_COUNTRECORD: window.S_COUNTRECORD //总条数
}
var GridPanelCONST = {
  COLID: "colid",
  ROWINDEXCUSTOMER: "rowIndexcustumer",
  PARENTLEVEL: "parentLevel"
}

/**
 * #表格组件
 * 
 * ##使用举例：
 *
 *     EacListDataPage.dataGrid = ComponentManager.createComponent('GridPanel', {
 *         initType : 'auto',
 *         bindDomId : EacPageCommon.dataGridDomId,
 *         height:'auto',
 *         emptyText:window.S_DATA_EMPTY,
 *         uniqueId : window.EacPageCommon.uniqueId,
 *         colModule : window.EacPageCommon.viewColumns,
 *         plugins : _plugins,
 *         noSort : _noSort,
 *         sortFirst : 'desc',
 *         changePageCount : true,
 *         data : window.EacPageDatas ? window.EacPageDatas : {} , 
 *         listeners : {
 *             changePage : function(pageNum, pageSize) {
 *                 // ...
 *             },
 *             sort : function(colId, sortOrder) {
 *                 // ...
 *             },
 *             filter : function(){
 *                 // ...
 *             }
 *         }
 *     });
 * 
 * ##每页显示页数举例：
 *     
 *     var data = ...;
 *     data.pageSize = 20; // 默认值
 *     var grid = ComponentManager.createComponent('GridPanel', {
 *       ...
 *       changePageCount : [5, 10, 50, 100], // 列表内容
 *       data : data , 
 *       listeners : {
 *         ...
 *         changePageSize : function(pageSize) { // 修改事件
 *           console.info('pageSize : ' + pageSize);
 *         },
 *         ...
 *       },
 *       ...
 *     });
 * 效果如下：
 * >![多选效果](../assets/img/gridpanel_changePageSize.png "多选效果")
 *     
 * ##自定义单选列表举例：
 * 
 *     window.grid = ComponentManager.createComponent('GridPanel', {
 *         initType : 'auto',
 *         bindDomId : 'grid',
 *         plugins : ['paging'],
 *         noColumn : true,
 *         colModule : { // 列模式中的list指定列在列表的对应值
 *             id : {text : '编号', width : 10, index : 0, align : 'center'},
 *             metric1 : {text : '指标1', width : 30, index : 1, list : 'metric1'},
 *             metric2 : {text : '指标2', width : 30, index : 2, list : 'metric2'},
 *             metric3 : {text : '指标3', width : 30, index : 3, list : 'metric3'}
 *         },
 *         list : { // 初始参数中指定列表内容
 *             '指标' : [
 *                 {text:'指标1', value:'metric1'},
 *                 {text:'指标2', value:'metric2'},
 *                 {text:'指标3', value:'metric3'},
 *                 {text:'指标4', value:'metric4'},
 *                 {text:'指标5', value:'metric5'},
 *                 {text:'指标6', value:'metric6'},
 *                 {text:'指标7', value:'metric7'},
 *                 {text:'指标8', value:'metric8'},
 *                 {text:'指标9', value:'metric9'},
 *                 {text:'指标10', value:'metric10'}
 *             ]
 *         },
 *         listeners : {
 *             // list事件在选择值时触发
 *             list : function(colid, val) {
 *                 console.info(colid, val);
 *             }
 *         }
 *     });    
 *     
 *     
 * @class GridPanel
 * @constructor 
 * @param conf {Json} 配置信息
 * @param conf.bindDomId {String/Dom} 需要绑定id,
 * @param conf.uniqueId  表格的唯一ID
 * @param conf.colModule {Map} 列模式，形如{id : colConf}，详见列模式配置
 *  {列ID:{index:列索引,headeRender:自定义函数，或者预置渲染,width:{number}列宽,render:列渲 自定义函数，或者预置渲染}}
 * @param conf.plugins {Array} 插件列表
 * @param conf.children {Json} 树表中子节点集合属性
 * @param conf.listeners {Json} 事件
 * @param [conf.listeners.changePageSize] 每页显示条数更改事件，function(pageSize)
 * @param conf.data {Array} 表格数据
 * @param conf.noColumn {String} 不具有隐藏列功能"true"
 * @param conf.initLineCount {Number} 初始绘制行数  默认150
 * @param [conf.sortFirst] {String} 'desc'或'asc' 首次排序顺序
 * @param [conf.changePageCount] {Array|boolean} 每页显示下拉框列表中的内容，有值时显示
 * 
 * 预定义渲染包括：select下拉框, editor可编辑表格, checkbox复选框， ico单元格图片限于一个， move移动单元格， date 日期格式化
 * 插件包括：filter列过滤， sort排序 rowSelect行可点击 tree表格树 paging分页
 *
 */
var GridPanel = function (conf) {
  this.$bindDomd = $.getJQueryDom(conf.bindDomId);
  if (this.$bindDomd.length === 0) {
    new Error("dom is not exist");
    return;
  }
  if (this.$bindDomd.children().length !== 0) {
    return;
  }
  this.url = conf.url;
  this.colModule = conf.colModule;
  this.plugins = conf.plugins;
  this.initLineCount = conf.initLineCount || 150;
  this.unit = conf.unit ? conf.unit : "%";
  this.state = {}; // key:行索引，value:行状态0新添，1修改，2删除
  this.listeners = conf.listeners;
  this.data = conf.data ? conf.data.pageData : [];
  this.conf = conf;
  this.emptyUsed = conf.emptyUsed === false ? false : true; //是否使用emptyUsed为空提示
  //树表的属性
  this.levelData = {}; //树表按照节点层级存储数据
  this.expandableColumnId = null; //用于标识树折叠的列Id（有折叠按钮的列）根据列模式:expandable属性指定
  this.expandableColumnHiddenIds = []; //有折叠按钮的行，隐藏该行指定列的内容
}

GridPanel.prototype = {
  constructor: window.GridPanel,
  init: function (conf) {
    return this;
  },
  render: function (conf) {
    this.$bindDomd.addClass(GridPanelClass.wrapClassName).append(GridPanel.template.thead.call(this, conf));
    if (conf.data) {
      this.refreshData(conf.data, false);
    } else {
      this.loadData(conf);
    }
    if (conf.width) {
      this.$bindDomd.addClass(GridPanelClass.wrapClassNameX).css({
        width: conf.width
      });
      var width = 0;
      for (var key in this.colModule) {
        width += this.colModule[key].width + 11;
      }
      this.$bindDomd.children().css({
        width: width,
        "overflow-x": "hidden"
      });
    }
    return this;
  },
  /*
   * 绑定事件
   *
   * @param conf
   *            配置参数
   */
  bindEvent: function (conf) {
    var $tbody = this.getTBody();
    var trs = $tbody.children();
    for (var i = 0, len = trs.length; i < len; i++) {
      this.bindEventRow(trs[i], false, "render");
    }
    var tr = this.getTHead().children().children()[0];
    this.bindEventRow(tr, true, "headRender");

    return this;
  },

  /*
   * 初始化完成时调用
   *
   * @param conf
   *            配置参数
   */
  finished: function (conf) {
    if (!this.plugins) {
      return
    }
    for (var i = 0, len = this.plugins.length; i < len; i++) {
      var plugin = GridPanel.plugins[this.plugins[i]];
      if (plugin) {
        plugin.init.call(this, conf);
      }
    }
    this.doLayout();
    var self = this;
    OnResize.addLayout(function () {
      self.doLayout();
    });
    return this;
  },
  /*
   * 根据提供的url配置参数自动获取数据
   *
   * @param conf
   *            配置参数
   */
  loadData: function (conf) {
    conf = conf ? conf : {};
    $.ajax({
      url: conf.url ? conf.url : this.url,
      type: "POST",
      dataType: "json",
      data: conf.data,
      context: this,
      success: function (json) {
        this.refreshData(json);
      }
    });
  },
  /**
   * 重置表格状态
   * */
  reSet: function () {
    //如果表头有复选框，清除表头复选框选中状态
    this.getTHead().find('input[type="checkbox"]').removeAttr("checked");

    //移除表格体内容
    var $tablebody = this.$bindDomd.children("div.tbodypart");
    $tablebody.find("*").unbind();
    $tablebody.remove();

  },
  refreshDataAsyn: function (datas, isbind) {
    this.dataIndex = 0;
    this.data = datas;
    this.$bindDomd.children('div.tbodypart').remove();
    this.$bindDomd.append(GridPanel.template.tbody.call(this, []));
    this.fillData();
    this.doLayout();
    this._bindScrollEvent();
    this.stopLoading();
    /* this.reSet();
    this.$bindDomd.append(GridPanel.template.tbody.call(this, []));
    this.data = [];
    var self = this;
    var copyNodes = $.copyArray(datas);
    var newArray = [];
    var interval = setInterval(function() {
      if (copyNodes.length <= 0 && newArray.length == 0) {
        clearInterval(interval);
        self.refreshDataState();
        self.getTBodyOuter().scrollTop(0);
        self.doLayout();
        self.stopLoading();
      } else {
        var childs = [];
        if (copyNodes.length <= 0) {
          copyNodes = newArray;
        }
        var nodes = copyNodes.splice(0, 10);
        self.batchRow(nodes);
        // self.getTBodyOuter().scrollTop(0); 
        newArray = childs;
      }
    }, 100);*/
  },
  _bindScrollEvent: function () {
    var gridSelf = this;
    var $tbodyPart = gridSelf.getTBodyOuter(gridSelf.$dom);
    $tbodyPart.bind("scroll", function () {
      var $tbodyPart = $(this);
      var scrollTop = $tbodyPart[0].scrollTop;
      var tbodyHeight = $tbodyPart.children().height();
      if (scrollTop + $tbodyPart.height() == tbodyHeight) {
        gridSelf.fillData();
        var addHeight = $tbodyPart.children().height() - tbodyHeight;
        //gridSelf.reSet();
        //$tbodyPart.scrollTop($tbodyPart[0].scrollTop - addHeight);
      }
    });
  },
  selectRow: function (row) {
    var clzName = GridPanelClass.rowSelectClassName;
    var $tbody = this.getTBody();
    $tbody.find('tr.' + clzName).removeClass(clzName);
    if (typeof row === 'number') {
      $tbody.children('tr:eq(' + row + ')').addClass(clzName);
    } else if (row instanceof $) {
      row.closest('tr').addClass(clzName);
    } else if (row instanceof Element) {
      $(row).closest('tr').addClass(clzName);
    } else throw new Error('Grid.selectRow(row) parameter could not be accepted.');
  },
  /**
   * 分批向表格绘制数据
   * @return {[type]} [description]
   */
  fillData: function () {
    var gridSelf = this;
    var gridData = gridSelf.data;
    if (gridSelf.dataIndex == gridData.length) return;
    if (gridData.length > gridSelf.initLineCount) {
      var endIndex = gridSelf.dataIndex + gridSelf.initLineCount;
      endIndex = endIndex > gridData.length ? gridData.length : endIndex;
    } else {
      endIndex = gridData.length;
    }

    var $tbody = this.getTBody();
    //$tbody.find("*").unbind().remove();
    var startIndex = gridSelf.dataIndex;
    var data = this.data.slice(gridSelf.dataIndex, endIndex);
    gridSelf.batchRow(data, false, true);
    gridSelf.dataIndex = endIndex;

  },
  /**
   * 刷新表格数据
   * @method refreshData
   * @param datas
   *            数据对象{pageDatas:[],...分页参数},如果没有pageData属性，则datas表示数组
   * @param isbind
   *            boolean 加载数据后时候绑定事件 外部调用时不需要该参数
   */
  refreshData: function (datas, isbind) {
    this.conf = this.conf || {};
    var gridData = datas.pageData || datas;
    //如果传入的数据是字符串格式的JSON，将其转为JSON对象
    if ($.isString(gridData)) {
      gridData = $.StringtoJson(gridData);
    }

    if (gridData.length > this.initLineCount) {
      this.refreshDataAsyn(gridData, isbind);
      return;
    }

    this.levelData = [];
    this.data = gridData;


    this.reSet();


    //当前无数据
    if (!this.data || this.data.length === 0 && this.emptyUsed === true) { //允许使用为空标识
      this.$bindDomd.append(GridPanel.template.empty.call(this,
        this.conf.emptyText, this.conf.emptyMini, this.conf.height, this.conf.nodataClass));
      this.isNodeData = true; //无数据标识
    } else {
      this.isNodeData = false;
      this.$bindDomd.append(GridPanel.template.tbody.call(this, this.data));
    }

    this.refreshRowColor();
    if (isbind !== false) {
      if (this.bindEvent) {
        this.bindEvent();
      }
      for (var i = 0, len = this.plugins.length; i < len; i++) {
        var plugin = GridPanel.plugins[this.plugins[i]];
        if (plugin && plugin.refresh) {
          plugin.refresh.call(this, datas);
        }
      }
    }
    $tablebody = this.getTBodyOuter();
    $tablebody.height(this.conf.height || "auto");

    //GridPanel.justScrollBar($tablebody[0], this.getTHead().children()[0]);
    this.stopLoading();
    //this.ctrlColumnShow();
    //无数据则隐藏分页
    $.getJQueryDom(this.conf.bindDomId + "Page").css("visibility", this.isNodeData ? "hidden" : "visible");
    this.doLayout();
    $(document.body).unbind("mousemove", GridPanel.trOver)
      .bind("mousemove", GridPanel.trOver);
    this.refreshDataState();
    if (this.bifurcate && this.data != null && this.data.length != 0) {
      this.getTBody().prepend(GridPanel.template.placeholder.call(this, this.data));
    }
  },
  /**
   * 刷新制定列的数据,通过指定列的值，从表格数据对象中循环查出是某行的数据，然后再去更新     *
   * @method refreshColumnsData
   * @param identColumnId
   *            根据指定列Id的值获得
   * @datas array[{identColumnId:值,需要更新列键值对..}] 需要刷新列的数据
   */
  refreshColumnsData: function (identColumnId, datas) {
    var refreshRowData = null,
      rowData = null;
    for (var i = 0, len = datas.length; i < len; i++) {
      refreshRowData = datas[i];
      for (var j = 0, jlen = this.data.length; j < jlen; j++) {
        rowData = this.data[j];
        if (rowData[identColumnId] == refreshRowData[identColumnId]) { //根据指定列的值，确定行
          for (var columnId in refreshRowData) { //遍历
            if (columnId != identColumnId) { //不更新标识列
              //  var columnModule = this.colModule[columnId];
              rowData[columnId] = refreshRowData[columnId];
            }
          }
          this.updateRow(j, rowData);
        }
      }
    }
  },
  /**
   * 刷新表格列控制哪些显示哪些隐藏
   *
   * */
  ctrlColumnShow: function () {

    var hidesWidth = 0; //待隐藏列总宽度
    var shows = 0; //需要显示列的个数
    var endColId = null; //
    var targetColumnId = null; //将隐藏列的宽度附加给目标列，如果没有指定就是最后一列
    var targetRemainderColumnId = null; //
    for (var colId in this.colModule) {
      var module = this.colModule[colId];
      targetColumnId = targetColumnId || module.targetColumnId;
      targetRemainderColumnId = targetRemainderColumnId || module.targetRemainderColumnId;
      if (module.hideColunm === true || module.hideColunm === "true" && module.unit == "%") {
        hidesWidth += +module.origWidth;
      } else {
        endColId = colId;
        if (module.fixed) {
          continue;
        }
        shows++;
      }
    }
    //平分每列相同宽度
    var everyAppendWidth = hidesWidth ? parseInt(hidesWidth / shows) : 0;
    //剩余未分配宽度
    var remainder = hidesWidth - everyAppendWidth * shows;
    var $thead = this.getTHead();
    var $tbody = this.getTBody();
    var $th = null;
    var $tds = null;
    for (var col in this.colModule) {
      var module = this.colModule[col];
      $th = $thead.find("th[colid='" + col + "']");
      $tds = $tbody.find("td[colid='" + col + "']");
      if (module.hideColunm === true || module.hideColunm === "true") {
        $th.hide();
        if (this.isNodeData != true) {
          $tds.hide();
        }

      } else {
        $th.show();
        if (this.isNodeData != true) {
          $tds.show();
        }

        if (module.fixed) {
          continue;
        } //分配列宽，如果该列设置为固定，则不被分配
        if (module.unit == "%") {
          var width = everyAppendWidth;
          if (targetColumnId) { //如果有目标列，则将宽度都指定给目标列
            if (targetColumnId == col) {
              if ($th[0]) {
                $th[0].style.width = (+module.origWidth + hidesWidth || 0 + hidesWidth) + "%";
                $tds.css({
                  width: (+module.origWidth + hidesWidth || 0 + hidesWidth) + "%"
                });
              }
            }
          } else {
            if (col == (targetRemainderColumnId || endColId)) {
              width += remainder;
            } //最后一列,将余数宽度分给最后一列
            if ($th[0]) {
              $th[0].style.width = (+module.origWidth + width || 0 + width) + "%";
              $tds.css({
                width: (+module.origWidth + width || 0 + width) + "%"
              });
            }
          }
        }
      }
    }
  },
  /**
   * 绑定一行事件
   *
   * @param row
   *            行dom对象
   * @param isAll
   *            绑定每一行数据，是否对一些可以进行全局操作的事件进行绑定，如全选框
   * @param render
   *            对列渲染的对象进行绑定
   */
  bindEventRow: function (row, isAll, render) {
    if (!row) {
      return;
    }
    var tds = row.children;
    var plugin = null;
    for (var i = 0, len = tds.length; i < len; i++) {
      var $td = $(tds[i]);
      plugin = GridPanel.render[$td.attr(render)];
      if (plugin) {
        plugin.bindEvent.call(this, $td, isAll);
      }
    }
  },
  /*
   * 获得表体tbody对象
   * <tbody>
   */
  getTBody: function () {
    return this.getTBodyOuter().children().children("tbody");
  },
  /*
   * 获得表体tbody容器对象div JQuery对象
   * <div class="tbodypart">
   */
  getTBodyOuter: function () {
    return this.$bindDomd.children("div." + GridPanelClass.bodyGridClassName);
  },
  /*
   * 获得表体所有行Dom集合
   * <tr>
   */
  getRows: function () {
    return this.getTBody().children("tr:not([placeholder])");
  },
  /*
   * 获得表头table Dom对象JQuery对象
   * <table>
   */
  getTHead: function () {
    return this.$bindDomd.children("div:first").children("table");
  },
  /**
   * 设置某列表头文字
   * @method setHeadTextByColId
   * @param colId 列ID
   * @param text 表头文字
   * @param title 悬浮提示，如果不指定，则使用表头显示文字
   * */
  setHeadTextByColId: function (colId, text, title) {
    return this.$bindDomd.find("div.theadpart th[colid='" + colId + "'] span").text(text).attr("title", title || text);
  },
  /**
   * 获得表格全部数据，该数据为单元格内显示的文本，与data不同，不是原始数据
   */
  getAllData: function () {
    var trs = this.setRows(); //getTBody().children("tr");
    var data = [];
    var line = null;
    for (var i = 0, len = trs.length; i < len; i++) {
      var $tr = $(trs[i]);
      var tds = $tr.children("td");
      line = {};
      for (var j = 0, jlen = tds.length; j < jlen; j++) {
        var $td = $(tds[j]);
        line[$td.attr("colid")] = GridPanel.template.getTDVal($td);
      }
      data.push(line);
    }
    return data;
  },
  /**
   * 添加行到指定索引行位置
   * conf.data 行数据
   * conf.rowIndex 相对行索引
   * conf.direction {boolean} 上方还是下方 false标识下方，true表示上方 默认false
   */
  addRowByIndex: function (conf) {
    var $tr = this.getRows().filter(":eq(" + conf.rowIndex + ")");
    this.addRow(conf.data, conf.direction, true, $tr);
  },
  /**
   * 添加一行数据
   * @method addRow
   * @param data  {json}  {列ID:值}
   * @param isFirst｛boolean｝ 是否在表格头部添加
   * @param isRefresh｛boolean ｝ 是否刷新表格判断是否需要出滚动条默认刷新，如果不刷新，输入false
   * @param applyTr {jquery/dom} 新建行相对添加的行位置
   * @param noAddData 不将数据添加到表格中，只是绘制表格行
   */
  addRow: function (data, isFirst, isRefresh, applyTr, noAddData) {
    var $tbody = this.getTBody();
    var $tbodyout = this.getTBodyOuter();
    if (this.data.length == 0) {
      $tbody.children().remove();
      $tbodyout.children(":eq(0)").css({
        height: "auto"
      });
    }
    $tbodyout.scroll(function () {
      return false
    });
    var $tr = $(GridPanel.template.createRow.call(this, data)).hide();
    var $applyTr = applyTr ? $.getJQueryDom(applyTr) : null;
    if (isFirst) {
      if ($applyTr) {
        applyTr.before($tr);
      } else {
        $tbody.prepend($tr);
        $tbodyout.scrollTop(0);
      }
    } else {
      if ($applyTr) {
        $applyTr.after($tr);
      } else {
        $tbody.append($tr);
        // var tbodyoutHeight = $tbodyout.height();
        // if ($tbody.height() > tbodyoutHeight) {
        //   $tbodyout.scrollTop($tbody.height() - tbodyoutHeight);
        // }
      }

    }
    this.isNodeData = false;
    if (!data) {
      data = {};
      for (var colM in this.colModule) {
        data[colM] = null;
      }
    }

    if (noAddData !== true) {
      this.data.push(data);
    }

    this.bindEventRow($tr[0], false, "render");

    $tr.show().css("display", "table-row");
    if (isRefresh !== false) {
      this.refreshDataState();
    }

  },
  /**
   * 批量添加数据
   * @method batchRow
   * @param datas {array} 数据集合 [{json}]
   * @param isFirst {boolean} 是否在表格头部添加
   * */
  batchRow: function (datas, isFirst, noAddData) {
    for (var i = 0, len = datas.length; i < len; i++) {
      this.addRow(datas[i], isFirst, i + 1 == len ? true : false, false, noAddData); //批量最后一个，才允许刷新表格状态
    }
  },
  /*
   * 刷新表格展现，包括隔行颜色，隐藏显示列列宽，滚动条对齐，表头全选清除
   * */
  refreshDataState: function () {
    //this.getTHead().find('input[type="checkbox"]').removeAttr("checked");
    this.refreshRowColor();
    this.ctrlColumnShow();
    this.loopColumnRenderRefresh();
    GridPanel.justScrollBar(this.getTBodyOuter(), this.getTHead().children()[0]);
    if (this.plugins) {
      for (var i = 0, len = this.plugins.length; i < len; i++) {
        var plugin = GridPanel.plugins[this.plugins[i]];
        if (plugin && plugin.reset) {
          plugin.reset.call(this);
        }
      }
    }
    if (this.isNodeData) {
      this.getTBodyOuter().css("overflow", "hidden");
    } else {
      this.getTBodyOuter().css("overflow", "auto");
    }

  },
  /**
   * 在某行后添加多行数据
   * @method appendExpandRow
   * @param rowIndex
   *            {Number} 在该索引下添加数据,某一等级下的索引
   * @param datas
   *            [{}] 表格行数据 return [tr:dom] 返回新添加行dom对象数组
   * @param level {number} 数据所在树节点级别
   */
  appendExpandRow: function (rowIndex, datas, level) {
    var $tr = $(this.getRows().filter('tr[rowIndexcustumer="' + rowIndex + '"]'));
    if (level) {
      $tr = $tr.filter("[level='" + level + "']");
    }
    var nextLevel = parseInt(level) + 1;
    for (var i = 0; i < datas.length; i++) {
      GridPanel.plugins.tree.appendExpandRow.call(this, rowIndex, [datas[i]], level);
      GridPanel.plugins.tree.setLevelData.call(this, nextLevel, datas[i]);
    }
  },

  /**
   * 删除行,可以删除一行，也可以删除多行
   * @method delRow
   * @param {Number / Array} indexs 行索引
   */
  delRow: function (indexs) {
    indexs = $.isNumber(indexs) ? [indexs] : indexs;
    var $tbody = this.getTBody();
    var trs = this.getRows();
    var indexMap = {};

    for (var i = indexs.length - 1; i >= 0; i--) {
      var $tr = trs.filter(":eq(" + indexs[i] + ")");
      indexMap[indexs[i]] = $tr;
    }

    var tempdata = []; //用于存放没有被删除的行数据
    for (var i = 0, len = this.data.length; i < len; i++) {
      if (indexMap[i]) {
        indexMap[i].find("*").unbind();
        indexMap[i].remove();
      } else {
        tempdata.push(this.data[i]);
      }
    }
    this.data = tempdata;
    if (tempdata.length == 0) {
      this.refreshData(this.data);

    }

    this.refreshDataState();

  },
  /**
   * 更新一行数据
   * @method updateRow
   * @param rowIndex {Number}行索引
   * @param  data {JSON} 行数据
   */
  updateRow: function (rowIndex, data) {
    var $tbody = this.getTBody();
    var $tr = this.getRows().filter(":eq(" + rowIndex + ")");
    $tr.find("*").unbind();
    $tr.html("");
    GridPanel.template.createRow.call(this, data, $tr);
    this.bindEventRow($tr[0], false, "render");
    this.data[rowIndex] = data;
  },
  /*
   * 隔行显示颜色
   */
  refreshRowColor: function () {
    var $tbody = this.getTBody();
    $tbody.children().removeClass("white blue cbg");
    $tbody.children(":even").addClass("whie");
    $tbody.children(":odd").addClass("blue cbg");
  },
  /**
   * 获得选中的复选框
   * @method getChecks
   * @param {number/String} index 复选框所在列ID或者列索引
   * @return {jQuery} 选中的复选框数组
   */
  getChecks: function (index) {
    return GridPanel.render.checkbox.getCheck.call(this, index);
  },
  /**
   * 获得选中的单选框
   * @method getRadio
   * @param {number/String} index 复选框所在列ID或者列索引
   * @return {jQuery} 选中的单选框jquery对象
   * */
  getRadio: function (index) {
    return GridPanel.render.radio.getCheck.call(this, index);
  },
  /**
   * 获得表头的checkbox
   * @method getHeadCheck
   * @param {number/String} index 复选框所在列ID或者列索引
   * @return {jQuery} 选中的复选框jquery对象
   * */
  getHeadCheck: function (index) {
    return GridPanel.render.checkbox.getHeadCheck.call(this, index);
  },
  /**
   * 设置表头显示文字
   * @method setHeadText
   * @param {String｝colId 列ID
   * @param {String} text 显示文字
   * @param {String} title 悬浮提示，如果不指定，悬浮提示显示text
   * */
  setHeadText: function (colId, text, title) {
    this.getTHead().find("th[colid='" + colId + "']").children("span").attr("title", title || text).html(text);
  },
  /**
   * 获得复选框选中行数据
   * @method getCheckedDatas
   * @param {number/String} index 选中标识所在列索引或者列ID
   * @param {String} colId 列ID 需要过滤获得某列的ID,如果不配置，则返回所有选中行数据 return []
   */
  getCheckedDatas: function (index, colId) {
    var indexs = this.getIndexByCheckBox(this.getChecks(index));
    var checked_datas = [];
    var data = null;
    for (var i = 0, len = indexs.length; i < len; i++) {
      data = this.data[indexs[i]];
      if (colId) {
        data = data[colId];
      }
      checked_datas.push(data);
    }
    return checked_datas;
  },
  /**
   * 获得单选框选中的行数据
   * @mthod getRadioCheckedData
   * @param {number} index 单选框所在列索引
   * @param {String} colId 需要取得列的值 如果不传入，则标识选中行数据
   * */
  getRadioCheckedData: function (index, colId) {
    var index = GridPanel.render.radio.getIndexByRadio.call(this, this.getRadio(index));
    if (index == -1) return [];
    if (colId) {
      return this.data[index][colId];
    } else {
      return this.data[index];
    }

  },
  /**
   * 通过复选框获得所在行索引
   * @getIndexByRadio
   * @param ｛dom/Array:dom｝ checks 单个复选框对象，或者数组
   */
  getIndexByRadio: function (radio) {
    return GridPanel.render.radio.getIndexByRadio.call(this, radio);
  },
  /**
   * 通过单选框获得所在行索引
   * @param  checks ｛dom/Array:dom｝单个复选框对象，或者数组
   */
  getIndexByCheckBox: function (radio) {
    return GridPanel.render.checkbox.getIndexByCheckBox.call(this, radio);
  },
  /**
   * 设置复选框选中或取消
   * @setChecked
   * @param {number/String} columnIndex 列索引/列Id
   * @param {Array} rowIndexs 需要选中行的行号
   * @param {boolean} type 选中/未选中
   * */
  setChecked: function (columnIndex, rowIndexs, type) {
    if (Object.prototype.toString.call(rowIndexs) != "[object Array]") {
      rowIndexs = [rowIndexs];
    }
    return GridPanel.render.checkbox.setChecked.call(this, columnIndex, rowIndexs, type);
  },
  /**
   * 获得单元格显示的文本值
   * @method getCellVal
   * @param {number} rowIndex 行索引
   * @param {number} colIndex 列索引
   * @returns {String}
   */
  getCellVal: function (rowIndex, colIndex) {
    var $tr = this.getRows().filter(":eq(" + rowIndex + ")");
    var query = "td";
    if ($.isString(colIndex)) {
      query += '[colId="' + colIndex + '"]';
    } else {
      query += ":eq(" + colIndex + ")";
    }

    var $td = $tr.children(query);
    return GridPanel.template.getTDVal.call(this, $td);
  },
  /**
   * 设置单元格显示的文本
   * @method getCellVal
   * @param {number} rowIndex 行索引
   * @param {number} colIndex 列索引
   * @param {String} innerHTML 显示的文本
   */
  setCellInnerHTML: function (rowIndex, colIndex, innerHTML) {
    var $tr = this.getRows().filter(":eq(" + rowIndex + ")");
    var query = "td";
    if ($.isString(colIndex)) {
      query += '[colId="' + colIndex + '"]';
    } else {
      query += ":eq(" + colIndex + ")";
    }

    var $td = $tr.children(query);
    $td.html(innerHTML);
  },
  /**
   * 获得指定列的值数组
   * @method getColumnValues
   * @param {Stirng} columnID 列ID
   * @returns Array
   */
  getColumnValues: function (columnID) {
    var columnVals = [];
    var rowData = null;
    for (var i = 0, len = this.data.length; i < len; i++) {
      rowData = this.data[i];
      columnVals.push(rowData[columnID]);
    }
    return columnVals;
  },
  /**
   * @method setColumnValue
   * 统一设置表格列值
   * @param {String} colId 列Id
   * @param {Object} value 值
   * */
  setColumnValue: function (colId, value) {
    var trs = this.getRows(); //getTBody().children("tr");
    var setValue = null;
    var render = this.colModule[colId].render;
    if ($.isString(render)) {
      setValue = GridPanel.render[render].setValue;
    } else {
      setValue = this.colModule[colId].setValue;
    }
    for (var i = 0, len = trs.length; i < len; i++) {
      var $td = $(trs[i]).children('td[colid="' + colId + '"]');
      if (setValue) {
        setValue.call(this, {
          $td: $td,
          rowVal: this.data[i],
          value: value
        });
      }
    }
  },
  /**
   * 销毁该表格显示
   * */
  destory: function () {
    this.$bindDomd.find("*").unbind().remove();
    if (this.filterIds) {
      for (var key in this.filterIds) {
        Layer.destory(this.filterIds[key]);
      }
    }
    $("#" + this.conf.bindDomId + "Page").find("*").unbind();
    $("#" + this.conf.bindDomId + "Page").remove();

  },
  startLoading: function () {
    if (!this.loading) {
      MaskShaw.start(this.conf.bindDomId, true);
      this.loading = true;

    }
  },
  stopLoading: function () {
    if (this.loading) {
      MaskShaw.stop(this.conf.bindDomId);
      this.loading = null;
    }
  },
  /*
   * 设置无数据显示文字
   */
  setEmptyText: function (text, ismin) {
    GridPanel.template.setEmptyText.call(this, text, ismin);
  },
  doLayout_old: function () {
    var $grid = $("#" + this.conf.bindDomId);
    if (!$grid[0]) return;
    if (this.conf.height !== "auto") return;
    var $container = $grid.parent();
    var prevAll = $grid.prevAll();
    if (!$container.hasClass("portlet-middle-m")) {
      prevAll = $container.prevAll();
      $container = $container.parent();
    }
    //  if(!$container.hasClass("portlet-middle-m")) return;

    var prevHeight = 0;
    for (var i = 0; i < prevAll.length; i++) {
      if ($(prevAll[i]).css("display") == "none") continue;
      if ($(prevAll[i]).css("position") == "absolute") continue;
      prevHeight += $.getAllSize(prevAll[i]).height;
    }
    var nextAll = $grid.nextAll();
    var nextHeight = 0;
    for (var i = 0; i < nextAll.length; i++) {
      if (!nextAll[i]) continue;
      if ($(nextAll[i]).css("display") == "none") continue;
      if ($(nextAll[i]).css("position") == "absolute") continue;
      nextHeight += $.getAllSize(nextAll[i]).height;
    }
    var parentHeight = $container.height(); // - $.getVertical($container,"padding");
    var gridHeadHeight = $.getAllSize(this.getTHead()).height;
    var height = parentHeight - prevHeight - $.getVertical($grid, "margin") - gridHeadHeight - nextHeight;

    var $bodyOuter = this.getTBodyOuter();
    var tbodyHeight = $bodyOuter.children().height();
    if (height > tbodyHeight && this.data.length != 0) {
      // height = tbodyHeight;
    }
    this.getTBodyOuter().height(height);

    GridPanel.justScrollBar(this.getTBodyOuter(), this.getTHead().children("thead"));
    this.refreshRowColor();
  },
  doLayout: function () {
    var $grid = $("#" + this.conf.bindDomId);
    if (!$grid[0]) return;
    if (this.conf.height !== "auto") return;

    var $container = $grid.parent();
    // 为解决【告警发送规则】的表格高度计算问题修改于2014-10-22
    /* 
    if (!$container.hasClass("portlet-middle-m")) {
      $container = $container.parent();
    }*/
    if (!$container.hasClass("portlet-middle-m") && !$container.hasClass("portlet-middle-m-inner")) {
      $container = $container.parent();
    }
    $container.css('overflow-y', 'auto');
    // ==============================================================

    var initHeight = $container[0].scrollHeight;

    // 处理带有小数的高度
    var styleHeight = parseInt($container[0].style.height, 10);
    if (!isNaN(styleHeight) &&
      styleHeight !== initHeight &&
      $container[0].style.height.indexOf('%') <= 0) {
      initHeight = styleHeight;
    }

    this.getTBodyOuter().height(initHeight);

    var wholeHeight = $container[0].scrollHeight;
    var viewHeight = $container.height();
    var height = viewHeight - (wholeHeight - initHeight);

    this.getTBodyOuter().height(height);

    GridPanel.justScrollBar(this.getTBodyOuter(), this.getTHead().children("thead"));
    this.refreshRowColor();
  },
  /*绑定鼠标悬浮行变色*/
  _rowHover: function () {
    var $grid = $("#" + this.conf.bindDomId);
    $grid.hover(function (event) {
      if (event.target.tagName.toLowerCase() == "tr") {
        $(event.target).addClass("over");
      }
    }, function (event) {
      if (event.target.tagName.toLowerCase() == "tr") {
        $(event.target).removeClass("over");
      }
    });
  },
  /**
   * 调整滚动条
   */
  justScrollBar: function () {
    GridPanel.justScrollBar(this.getTBodyOuter(), this.getTHead().children("thead"));
  },
  /*
   * 循环列，刷新每一列的render
   */
  loopColumnRenderRefresh: function () {
    var renderName = null;
    for (var colId in this.colModule) {
      renderName = this.colModule[colId].render;
      if ($.isString(renderName) && GridPanel.render[renderName]) {
        if (GridPanel.render[renderName].reset) {
          GridPanel.render[renderName].reset.call(this, {
            colId: colId
          });
        }
      }
    }
  }
}

/**
 * 表格出现滚动条，防止错位
 * @method  GridPanel.justScrollBar
 * @static
 * @param gridbodyDiv 表格体div元素
 * @param gridhead 表格头部thead元素
 * */
GridPanel.justScrollBar = function (gridbodyDiv, gridhead) {

  gridbodyDiv = $.getJQueryDom(gridbodyDiv).css("overflow-x", "hidden");
  gridhead = $.getJQueryDom(gridhead);

  // if ($.isIE(7) || document.documentMode == 7) {
  //   return;
  // }
  if (!gridbodyDiv[0]) {
    return;
  }
  var scrollWidth = parseInt(gridbodyDiv[0].offsetWidth) - parseInt(gridbodyDiv[0].scrollWidth);
  var $gridHeadTr = gridhead.children("tr");
  if (gridbodyDiv.height() < gridbodyDiv.children().height()) {
    if ($gridHeadTr.children('th[block="true"]').length == 0) {
      $gridHeadTr.append(
        $.createDomStr({
          tagName: "th",
          style: {
            padding: "0px",
            width: (16) + "px"
          },
          attr: {
            block: true
          }
        })
      );
    }
    var $table = gridbodyDiv.children("table");
  } else {
    $gridHeadTr.find('th[block="true"]').remove();
  }
}

GridPanel.trOver = function ($event) {
  var gridbodyDiv = $($event.target).parents('[id$="_gridbody"]');
  var bNoOver = gridbodyDiv.parent().attr('no-over') === 'true';
  if (gridbodyDiv[0] && !bNoOver) {
    // 前行增加over，其它行去掉over
    $($event.target).parents("tr:not([type='empty'])").addClass("over").siblings().removeClass("over");
  } else {
    // 去掉所有行的over
    $("tr").removeClass("over");
  }
}
GridPanel.trOut = function (tr) {
  $(tr).removeClass("over");
}

ComponentManager.registComponent('GridPanel', GridPanel, "");

/**
 * 设置列模式隐藏列
 * @param {} [colMod] [列模式]
 * @param {array } [hideIds] [隐藏列]
 */

GridPanel.setColumnHide = function (colMod, hideIds) {
  for (var i = 0; i < hideIds.length; i++) {
    colMod[hideIds[i]].hideColunm = 'true';
  }
}

GridPanel.clearHover = function () {
  $('div.tbodypart tr').removeClass('over');
}

$(document).unbind('mouseout', GridPanel.clearHover).bind('mouseout', GridPanel.clearHover);;/*
 * 表格插件部分
 */
GridPanel.plugins = {
}


;/*
 * 表格树插件
 * 
 * 表格配置参数
 * children：表格数据子资源属性key
 * 事件：
 * initTree(rowData)绘制树回调，需要确定该行是否需要绘制树节点折叠，如果需要绘制返回true
 * isExpand(rowData) 是否展开树节点  
 */
GridPanel.plugins.tree = {
        /*
         * 初始化 
         * 列模式
         *  expandable：{boolean} 该列可以折叠，即需要展示树层的列
         *  expandableHidden:boolean 折叠行该列隐藏
         */
        init : function(conf) {
            GridPanel.plugins.tree.refresh.call(this, conf.data);
            // this.getRows().filter("[parentLevel]").children("td[isExpand]").css("paddingLeft","35px");
        },
        refresh : function(data) {
            var self = this;//表格实例对象
            self.expandableColumnId = null;//用于标识树折叠的列Id（有折叠按钮的列）
            self.expandableColumnHiddenIds = [];//当节点折叠的时候，需要隐藏内容的列id
            for ( var colId in self.colModule) {
                if(self.colModule[ colId ].expandable){
                    self.expandableColumnId =  colId;
                }                
                if (self.colModule[ colId ].expandableHidden) {
                    self.expandableColumnHiddenIds.push( colId );
                }
            }            
            var rowIndex = 0, level = 0;            
            GridPanel.plugins.tree.onceLoadData.call(self, data.pageData || data, self.getRows(), rowIndex, level);            
            self.justScrollBar();         
        },        
        /**
         * 一次加载全部数据
         * 
         * @params datas 数据
         * @params rows 行集合
         * @params rowIndex 行索引
         * @params level 树节点级别
         */
        onceLoadData : function(datas, rows, rowIndex, level) {
            var PluginTree = GridPanel.plugins.tree; 
            var self = this;//表格实例对象
            var levelDatas = [];//某一级数据
            var data = null;
            for ( var i = 0, len = datas.length; i < len; i++) {
                data = datas[i];
                levelDatas.push(data);
                PluginTree.setLevelData.call(self, level, data);
            }
            for ( var i = 0, len = levelDatas.length; i < len; i++) {
                data = levelDatas[i];
                GridPanel.plugins.tree.createExpandBtn.call(this, data, rows[i], {"level" : level, "rowIndexcustumer" : rowIndex});
                var childData = data[this.conf.children];//子行数据
                if (childData && childData.length > 0) {
                    var newrows = PluginTree.appendExpandRow.call(this, rowIndex, childData);
                    // 判断是否该级展开
                    if (this.conf.listeners && this.conf.listeners.isExpand && this.conf.listeners.isExpand(data) === true){
                        $(newrows).show();
                    }else{
                        $(newrows).hide();
                    }
                    rowIndex += childData.length;
                    rowIndex = PluginTree.onceLoadData.call(this, childData, newrows, rowIndex + 1, level + 1);
                    rowIndex--;
                }
                rowIndex++;
            }
            return rowIndex;
        },
        /**
         * 按树级别存放数据
         * @param level 树节点级别
         * @param data 数据
         * 表格对象levelData属性为{level : []}｛级别标识：该级别数据｝
         * 
         * */
        setLevelData : function(level, data) {
            /*if (!this.levelData[level] && data) {
                this.levelData[level] = [];
            }*/
            this.levelData[level] = this.levelData[level] || [];
            this.levelData[level].push(data);
        },
        /*
         * 创建行中指定列折叠按钮，调用事件initTree处理函数。
         * 在创建折叠按钮的时候，给该行标注level 和 rowIndexcustumer属性
         * 判断是否需要折叠，如果返回true,则表示需要折叠，反之false
         * @param data {josn}行数据
         * @param $row{tr dom}当前行$tr对象
         * @rowIndex_level {"level" : level, "rowIndexcustumer" : rowIndex} 行索引树的层级 
         */
        createExpandBtn : function(data, tr, rowIndex_level) {            
            var self = this;//表格对象
            var $tr = $(tr).attr(rowIndex_level);
            var listeners = self.conf.listeners || {};
            if (listeners.initTree && listeners.initTree(data)) {                
                var $td = $tr.children('td[' + GridPanelCONST.COLID + '="' + this.expandableColumnId + '"]');
                var foldStr = $.createDomStr({
                    tagName : "span",
                    attr : {
                        "class" : listeners.isExpand  && listeners.isExpand(data) === true ? "expand-node" : "collapse-node"
                    },
                    style : {
                        "margin-right" : "5px",
                        "vertical-align": "middle"
                    }
                 });
                $(foldStr).attr(rowIndex_level).prependTo($td)
                         .bind("click", {self : self}, GridPanel.plugins.tree.treeFold);
                //有折叠按钮的行，隐藏指定字段内容
                for ( var j = 0, jlen = this.expandableColumnHiddenIds.length; j < jlen; j++) {
                    $tr.children('td[' + GridPanelCONST.COLID + '="' + this.expandableColumnHiddenIds[j] + '"]').children().hide();
                }
            }else{
                $tr.attr("leafRow", true);//标识该行为叶子节点
            }
        },        
        /*
         * 创建子行
         * @param rowIndex 行索引号
         * @param datas 数据
         * @param level 树级别
         * */
        appendExpandRow : function(rowIndex, datas, level) {
            var $trs = this.getRows();
            var $tr = $($trs.filter('tr[' + GridPanelCONST.ROWINDEXCUSTOMER + '="' + rowIndex + '"]'));
            if(level){ //如果指定了级别，直接获得该级别的li
                $tr = $tr.filter("[level='"+level+"']");
            }            
            //将父节点中的children元素取出来，放在父元素的兄弟元素（将tr的顺序和data的顺序一致）
            var index = $tr.index();

            for ( var i = 0, len = datas.length; i < len; i++) {
                index++;
                this.data.splice(index , 0, datas[i]);
                
            }
            
            var trs = [];
            var level = $tr.attr("level"); 
            var tr = null;
            for ( var i = 0, len = datas.length; i < len; i++) {
                var $newtr = $(GridPanel.template.createRow.call(this, datas[i], i));
                $newtr.attr({
                    "parentLevel" : level + rowIndex,
                    "rowIndexcustumer" : rowIndex + 1,
                    "level" : parseInt(level) + 1
                });              
                $tr.after($newtr);
                $tr = $newtr;
                $tr.children('td[' + GridPanelCONST.COLID + '="'+ this.expandableColumnId + '"]').attr("isExpand","true");
                
                $tr.filter("[parentLevel]").children("td[isExpand]").css("paddingLeft", ((parseInt(level) + 1)*35) + "px");
                tr = $newtr[0];
                this.bindEventRow(tr, false, "render");
                trs.push(tr);
            }



            // 计算滚动条（标头增加滚动条空白）
            var $tablebody = this.$bindDomd.children("div:eq(1)");
            GridPanel.justScrollBar($tablebody[0],this.getTHead().children()[0]);
            this.refreshRowColor();
            return trs;
        },
        /*
         * 折叠按钮事件处理函数
         * 子节点的parentLevel是level + rowIndex
          */
        treeFold : function(event) {
            var $foldBtn = $(this);
            var self = event.data.self;
            var rowIndex = parseInt($foldBtn.attr("rowIndexcustumer"));
            var level = parseInt($foldBtn.attr("level"));
            if ($foldBtn.hasClass("collapse-node")) {
                $foldBtn.removeClass("collapse-node").addClass("expand-node");
                if ($foldBtn.attr("ajaxed") != "true") {//防止多次动态加载子节点
                    $foldBtn.attr("ajaxed", "true");
                    if (self.listeners && self.listeners.expand) {
                        //index为tr实际dom索引，rowIndex为同一个级别的索引
                        var index = self.getRows().index($foldBtn.parent().parent()[0]);
                        self.listeners.expand.call(self, self.data[index], index, rowIndex, $foldBtn.attr("level"));
                    }
                }
                GridPanel.plugins.tree.recursionHide(self.getRows(), level , rowIndex,  true);
                
            } else {
                $foldBtn.removeClass("expand-node").addClass("collapse-node");
                GridPanel.plugins.tree.recursionHide(self.getRows(), level, rowIndex, false);
            }            
            self.justScrollBar();
        },
        recursionHide : function($rows, level, rowIndex, isShow){
            var childRows = $rows.filter('[parentLevel="' + level +""+ rowIndex+ '"]');
            var $tr = null;
            for(var i = 0, len = childRows.length; i < len; i++){
                $tr = $(childRows[i]);
                if(isShow){
                    $tr.show();
                    if($tr.find("span.collapse-node")[0]){
                        continue;
                    }
                    
                }else{
                    $tr.hide();
                }
                GridPanel.plugins.tree.recursionHide($rows, parseInt($tr.attr("level")) , $tr.attr("rowIndexcustumer"), isShow);
            }
        }
    };


;/**
  * 隐藏列+列过滤
  */

/* global GridPanel,Layer,$,Struct,CustomCheck,PageCtrl,ctx,FormValidate,RiilAlert,loadcalendarConf,Tree */
'use strict';
GridPanel.plugins.columnTools = {
        init:function(){
            this.filters = {};
            this.filterIds  = {};//该表格过滤层ID集合
            var $headTable  = this.getTHead();
            var $aBtn = $headTable.find('a.rank-pull');
            var $a = null;
            for(var i = 0; i < $aBtn.length; i++){
                $a = $($aBtn[i]);
                //没有隐藏列设置并且列中没有过滤功能，则隐藏按钮
                if((this.conf.noColumn === 'true' || this.conf.noColumn === true) && 
                        (!$a.attr('filterType') || this.colModule[$a.attr('colid')].hideFilter === true) &&
                        (!this.conf.list || undefined === this.colModule[$a.attr('colid')].list)){
                    $a.hide();
                }else{
                    $a.bind('click', {self : this}, GridPanel.plugins.columnTools.clickEvent);
                }
            }
        },
        hide:function(){
            Layer.close('columnTool');
        },
        /*点击表格功能按钮事件*/
        clickEvent:function(event){
            event.stopPropagation();
            Layer.close('columnTool');
            var layerInner = $('div[id$="filterLayer"]');
            for(var i=0; i < layerInner.length; i++){
                Layer.close($(layerInner[i]).attr('id'));
            }
            var self = event.data.self;
            var $a = $(this);
            var filterType = $a.attr('filterType');// 过滤的类型，String文本型,CheckboxGroup复选框组
            
            // filterDomId:从页面指定ID获取过滤选项显示内容
            var param = $.getDomAttrToJSON($a,['colId','gridId','filterType','filterDomId','filterHeight','filterWidth']);
            var toolshtml = ['<ul class="layer-menu layer-menu-tool">'];
            if(filterType){
                if(self.colModule[param.colId].hideFilter !== true){//隐藏过滤功能,如果hideFilter===true则隐藏过滤
                    toolshtml.push('<li type="filter"><a><span class="arrow"></span><span>' + window.S_FILTER + '</span></a></li>');
                }
            }
            if(self.conf.noColumn !== 'true' && self.conf.noColumn !== true){
                toolshtml.push('<li type="column"><a><span class="arrow"></span><span>'+window.S_COLUMN+'</span></a></li>');
            }
            if (self.conf.list && undefined !== self.colModule[param.colId].list) {
                $.each(self.conf.list, function(listText/*, listData*/) {
                    toolshtml.push('<li type="list"><a><span class="arrow"></span><span>'+listText+'</span></a></li>');
                });
            }
            toolshtml.push('</ul>');
            var layout = $.getElementAbsolutePosition(this);

            var gridOffset = self.$bindDomd.offset();

            Layer.init({
                id : 'columnTool',
                html : toolshtml.join(''),
                x : layout.x - gridOffset.left,
                y : layout.y  - gridOffset.top + $a.parent().height(),
                width:120,
                apply : self.$bindDomd,
                initHide:false,
                isAutoHide:true,
                remove:true
            });
            self.$bindDomd.css('position','relative');
            $('#columnTool .middle-m').css('overflow', 'hidden');
            $('#columnTool li').bind('mouseenter',{self : self, param : param, parentMenu : 'columnTool'}, GridPanel.plugins.columnTools.showTypeMenu)
                .bind('mouseout',{self:self},GridPanel.plugins.columnTools.hideTypeMenu).filter(':last').addClass('last');
            Layer._justPosition('columnTool', layout.x - gridOffset.left, $.getElementAbsolutePosition(this.parentNode).y - gridOffset.top + $a.parent().height(), self.$bindDomd);
        },
        /**显示列、过滤菜单插件*/
        showTypeMenu:function(event){
            Layer.removeFocusA('columnTool');
            var $parentMenu = $.getJQueryDom(event.data.parentMenu);
            var liOffset = $parentMenu.offset();
            var $li = $(this);
            //var childLeft = liOffset.left + $li.width()+5;
            //根据一级菜单显示过滤或列
            GridPanel.plugins.columnTools[$li.attr('type')].init.call(event.data.self, $li, liOffset.left + $parentMenu.width(),event.data.param);
        },
        hideTypeMenu:function(/*event*/){
            // var $li = $(this);
            // var type = $li.attr('type');
        },
        /**
         * 隐藏列
         * @param 上一次菜单对象
         * @param childLeft 左定位坐标
         * @param param 参数
         * */
        column:{
            init:function($li, childLeft/*, param*/){
                if(document.getElementById('columnLayer')){return;}
                var self = this;
                GridPanel.plugins.columnTools.filter.hide.call(self, $li);
                $li.children('a').addClass('on');
                var liOffset = $li.offset();
                var colMod = self.colModule;
                var colConf = null;
                 var colCount = 0, hideCount = 0;//总列数和隐藏列个数
                var columnHtml = ['<ul style="width:100%">'];               
                for(var colKey in colMod){                    
                    colCount++;
                    colConf = colMod[colKey];                    
                    var disabled = null, checked = 'checked';
                    if(colConf.isNessaryShow !== 'true'){//不是必须显示
                        hideCount++;
                        if(self.filterIds && self.filters[colKey]){//该列正在过滤，不能隐藏列
                            disabled = 'disabled';
                        }
                        if(colConf.hideColunm === 'true'){ //如果该列未被隐藏
                            checked = null;
                        }
                        var itemStr = Struct.checkItem({
                            id : 'columnLayer' + colKey,
                            checked : checked,
                            disabled : disabled,
                            text : colConf.text,
                            value : colKey
                        });                        
                        columnHtml.push(itemStr);
                    }
                }
                columnHtml.push('</ul>');
                var gridOffset = self.$bindDomd.offset();
                Layer.init({
                    id : 'columnLayer',
                    html : columnHtml.join(''),
                    x : childLeft - gridOffset.left,
                    y : liOffset.top - 5 - gridOffset.top,
                    extendWidth : 20,
                    initHide: false,
                    remove : true,
                    isAutoHide : true,
                    apply : self.$bindDomd,
                    autoHide : function(){
                        Layer.close('columnTool');
                        Layer.close('columnLayer');
                    }
                });
                CustomCheck.batchInit({outer : 'columnLayer'});
                GridPanel.plugins.columnTools.column.checkDiable(colCount, hideCount);
                var left = childLeft;
                var newPosition = $.checkDomPosition($('#columnLayer'),left,liOffset.top-5,$('#columnLayer').width(),$('#columnLayer').height());
                left = newPosition.x != left ? left  - $('#columnLayer').width() - $('#columnTool').width() : left;
                Layer._justPosition('columnLayer', left - gridOffset.left, liOffset.top - 5 - gridOffset.top,  self.$bindDomd);
                $('#columnLayer .middle-m').css('overflow', 'hidden');
                CustomCheck.click($('#columnLayer input'), function(/*event*/){
                    var checkeds  = $('#columnLayer input:checked:not(:disabled)');
                    if(!this.checked){
                        colMod[$(this).val()].hideColunm = 'true';
                        self.ctrlColumnShow();
                    }else{
                        delete colMod[$(this).val()].hideColunm;
                        if(checkeds.length !=1){
                            CustomCheck.setAvailable(checkeds,true);
                        }
                        self.ctrlColumnShow();
                    }
                    GridPanel.plugins.columnTools.column.checkDiable(colCount, hideCount);
                    //隐藏显示列事件回调
                    if(self.listeners && self.listeners.ctrlColumn){
                        self.listeners.ctrlColumn();
                    }
                    var ucheckeds  = $('#columnLayer input:not(:checked):not(:disabled)');
                    var uncheckedIds = [];
                    for(var i = 0;i < ucheckeds.length;i++){
                        uncheckedIds.push(ucheckeds[i].value);
                    }

                    if(self.conf.uniqueId){
                        PageCtrl.ajax({
                              url : ctx + '/userConf/saveconf',
                              data : JSON.stringify({confKey : self.conf.uniqueId, confValue: '"'+uncheckedIds.join('","') + '"'}),
                              type : 'post',
                              dataType : 'json',
                              contentType:'application/json;charset=UTF-8',
                              success : function(/*data*/) {}
                        });
                    }

                });
            },
            /**
             * 判断列选择是否需要disabled,条件是：如果被选中的只有一个,并且可以隐藏列个数小于所有列数
            */
            checkDiable : function(colCount, hideCount){
                var unchecked = $('#columnLayer input:checked');
                if(unchecked.length == 1 && colCount - hideCount <= 2){
                    CustomCheck.setAvailable(unchecked, false);                    
                }
            },
            hide:function($li){
                Layer.close('columnLayer');
                $li.next().children('a').removeClass('on');
            }
        },
        /**
         * 列过滤
         * */
        filter:{
            init:function($li,childLeft,param){
                var self = this;
                GridPanel.plugins.columnTools.column.hide.call(self,$li);
                $li.children('a').addClass('on');
                this.filters = this.filters || {};
                this.filterIds  = this.filterIds || {};//该表格过滤层ID集合
                var liOffset = $li.offset();
                var typeFn = GridPanel.plugins.columnTools.filter.filterListeners[param.filterType];
                if(typeFn){// 过滤的类型，String文本型,CheckboxGroup复选框组
                    typeFn.call(self,
                                param.gridId,
                                param.colId,
                                '_filterLayer',
                                param.filterDomId,
                                {
                                     x : childLeft,
                                     y : liOffset.top -5,
                                     filterHeight : param.filterHeight,
                                     filterWidth : param.filterWidth
                                });
                }
            },
            hide:function($li){
                Layer.close(this.currentFilterLayerId);
                $li.prev().children('a').removeClass('on');
                $('#filterFiledNameFormValidateDiv').remove();
            },
            /* 过滤层显示模式 */
            filterListeners:{
                /* 文本框模式 */
                String:function(gridId,colId,id,filterDomId,layout){
                    var self = this;
                    id = gridId+colId+id;
                    self.currentFilterLayerId = id;
                    self.filterIds[id] = id;
                    var inputId = id +'_filterInput';
                    function inputListeners(){
                        if(!FormValidate.validate(id)){
                            document.getElementById(inputId).focus();
                            return false;
                        }
                        if(self.colModule[colId].hideColunm == 'true'){
                            return;
                        }
                        var filterValue = $('#'+inputId).val();
                        //判断与上次过滤条件是否相同，如果相同，不进行过滤,没有条件的时候，也不过滤
                        if(self.filters[colId] === filterValue || (!self.filters[colId] && ''===filterValue)) return;

                        self.filters[colId] = filterValue;//给过滤属性设置过滤的条件

                        var $span = self.getTHead().find('th[colid="'+colId+'"]').find('span');
                        if(!self.filters[colId]){
                            delete self.filters[colId];
                            $span.removeClass('filter');
                        }else{
                            $span.addClass('filter');
                        }
                        if(self.listeners && self.listeners.filter){
                            self.startLoading();
                            self.listeners.filter();
                        }
                    }
                    var value = self.filters[colId] || '';
                    if(document.getElementById(id)) return;

                    var validate = self.colModule[colId].filterValidate || 'isSearchIllegality,';
                    var validateErrorMsg = self.colModule[colId].filterValidateErrorMsg || '';
                    var validateRule = self.colModule[colId].filterValidateRule || '';
                    var gridOffset = self.$bindDomd.offset();
                    layout.x = layout.x - gridOffset.left;
                    layout.y = layout.y - gridOffset.top;
                    Layer.init({
                        id : id,
                        x: layout.x,
                        y: layout.y,                        
                        remove:true,
                        isAutoHide:true,
                        apply : self.$bindDomd,
                        html : '<input class="blueinput120 search" style="margin:0px" '+( self.colModule[colId].isIllegality !== false ? 'validate="'+ validate + '"' : '')+' name="filterFiledName" isSearchIllegalityErrorMsg="'+window.S_VALIDATE_INVALID_SEARCH+'" '+ validateErrorMsg + ' ' + validateRule +' type="text" id="'+inputId+'" value="'+value+'"/>',
                        autoHide:inputListeners
                    });

                    Layer.relativeJustPositoin(id,'columnTool', layout, self.$bindDomd);
                    $('#'+inputId).bind('keypress',function(event){
                        if(event.keyCode == 13){
                            var result = inputListeners();
                            if(result === false){
                                this.blur();
                                this.focus();
                                return;
                            }
                            Layer.close(id);
                            Layer.close('columnTool');
                        }
                    }).focus();
                    FormValidate.initDefault(id);
                },
                /* 复选框模式 */
                CheckboxGroup:function(gridId,colId,id,filterDomId,layout){
                    var self = this;
                    id = gridId+colId+id;
                    self.currentFilterLayerId = id;
                    self.filterIds[id] = id;
                    function hideListeners(/*event*/){
                        if(self.colModule[colId].hideColunm === 'true' || self.colModule[colId].hideColunm === true){
                            return;
                        }
                        var checkValues = [];
                        var checkboxs = $('#'+filterDomId+'clone').find('input[type="checkbox"]:checked');
                        var $span = self.getTHead().find('th[colid="'+colId+'"]').find('span');
                        for(var i=0,len = checkboxs.length;i<len;i++){
                            checkValues.push(checkboxs[i].value);
                        }
                        if($.compareArray(self.filters[colId],checkValues)) return;

                        self.filters[colId] = checkValues;
                        if(checkValues.length === 0){
                            delete self.filters[colId];
                            $span.removeClass('filter');
                        }else{
                            $span.addClass('filter');
                        }
                        if(self.listeners && self.listeners.filter){
                            Layer.close('columnTool');
                            self.startLoading();
                            self.listeners.filter();
                        }
                    }
                    if(document.getElementById(id)) return;

                    var gridOffset = self.$bindDomd.offset();
                    layout.x = layout.x - gridOffset.left;
                    layout.y = layout.y - gridOffset.top;

                    Layer.init({
                        id : id,
                        x:layout.x,
                        y:layout.y,
                        extendWidth:45,
                        width:'100%',
                        height:layout.filterHeight,
                        isAutoHide:true,
                        remove:true,
                        html : '',
                        autoHide : hideListeners,
                        apply : self.$bindDomd
                    });

                    var cloneFilterDom= $('#'+filterDomId).clone();
                    cloneFilterDom.attr('id',filterDomId+'clone');
                    Layer.setContent(id,cloneFilterDom.show());
                    CustomCheck.batchInit({outer:id});
                    Layer.relativeJustPositoin(id,'columnTool', layout, self.$bindDomd);
                    var values = self.filters[colId];
                    if(values){
                        var inputs = cloneFilterDom.find('input[type="checkbox"]');
                        for(var i=0;i<values.length;i++){
                            CustomCheck.setCheck(inputs.filter('[value="'+values[i]+'"]'),true);                            
                        }
                    }
                },
                /* 单选选框模式 */
                RadioboxGroup:function(gridId,colId,id,filterDomId,layout){
                    var self = this;
                    id = gridId+colId+id;
                    self.currentFilterLayerId = id;
                    self.filterIds[id] = id;
                    function hideListeners(/*event*/){
                        if(self.colModule[colId].hideColunm === 'true' || self.colModule[colId].hideColunm === true){
                            return;
                        }
                        var radioValue = '';
                        var radio = $('#'+filterDomId+'clone').find('input[type="radio"]:checked');
                        var $span = self.getTHead().find('th[colid="'+colId+'"]').find('span');
                        radioValue = radio.val();
                        //与上次过滤条件对比，如果相同不进行过滤
                        if(self.filters[colId] == radioValue) return;
                        self.filters[colId] = radioValue;
                        if(radioValue === '' || radioValue === null || radioValue === undefined){
                            delete self.filters[colId];
                            $span.removeClass('filter');
                        }else{
                            $span.addClass('filter');
                        }
                        if(self.listeners && self.listeners.filter){
                            Layer.close('columnTool');
                            self.startLoading();
                            self.listeners.filter();
                        }
                    }
                    if(document.getElementById(id)) return;
                    var gridOffset = self.$bindDomd.offset();
                    layout.x = layout.x - gridOffset.left;
                    layout.y = layout.y - gridOffset.top;
                    Layer.init({
                        id : id,
                        x:layout.x,
                        y:layout.y,
                        height:layout.filterHeight,
                        width:'100%',
                        isAutoHide:true,
                        remove:true,
                        html : '',
                        autoHide : hideListeners,
                        apply : self.$bindDomd
                    });
                    var cloneFilterDom= $('#'+filterDomId).clone();
                    cloneFilterDom.attr('id',filterDomId+'clone');                    
                    Layer.setContent(id,cloneFilterDom.show());
                    Layer.relativeJustPositoin(id, 'columnTool', layout, self.$bindDomd);
                    var values = self.filters[colId]; 
                    if(values){
                        var inputs = cloneFilterDom.find('input[type="radio"]');
                        inputs.filter('[value="'+values+'"]').attr('checked','true');
                    }
                },                
                /**
                 * 日期过滤
                 * <ul id="filterDate" style="display:none">
                *       <!--  <li><label><input type="radio" name="filtername" value="" /><span>最近一小时</span></label></li>
                *        <li><label><input type="radio" name="filtername"/><span>最近一天</span></label></li>
                *        <li><label><input type="radio" name="filtername"/><span>最近一个月</span></label></li>
                *        <li><label><input type="radio" name="filtername"/><span>最近一年</span></label></li> -->
                *        <li>
                *           <!-- <h2><label><input type="radio" name="filtername" dateType="custome"/><span>自定义时间</span></label></h2> -->
                *           <div class="layer-part">
                *               <span>从</span>
                *               <div class="time_body">
                *                   <input class="time_text" type="text" dateType="custome_from_date"/>
                *                 <div class="time_trigger_wrap">
                *                     <a  class="time_trigger"></a>
                *                 </div>
                *               </div>
                *           </div>
                *           <div class="layer-part">
                *               <span>到</span>
                *               <div class="time_body">
                *                   <input class="time_text" type="text" dateType="custome_to_date"/>
                *                 <div class="time_trigger_wrap">
                *                     <a  class="time_trigger"></a>
                *                 </div>
                *               </div>
                *           </div>
                *        </li>
                *     </ul>
                 * */
                date:function(gridId, colId, id, filterDomId, layout){
                    colId = colId.replace('.','');
                    var self = this;
                    id = gridId + colId + id;
                    self.currentFilterLayerId = id;
                    self.filterIds[id] = id;
                    function getCustomDate(){
                         var from  = $('#'+filterDomId+'clone input[type="text"][dateType="custome_from_date"]').val();
                         var to = $('#'+filterDomId+'clone input[type="text"][dateType="custome_to_date"]').val();
                         return {from : from, to : to};
                    }
                    function hideListeners(event){

                        if(event && (event.target && event.target.id == 'calendarWrapper' || event.target.id == 'TimeChooser')){
                            return false;
                        }
                        if(self.colModule[colId].hideColunm === 'true' || self.colModule[colId].hideColunm === true){
                            return;
                        }
                        window.calendarShowTag = null;
                        var radio = $('#'+filterDomId+'clone').find('input[type="radio"]:checked');
                        var $span = self.getTHead().find('th[colid="'+colId+'"]').find('span');
                        var filterValue = {};
                        if(!radio.attr('dateType')){//有多种日期选择
                            filterValue = radio.val();
                            if(self.filters[colId] != filterValue) return;
                        }else{//只有自定义选择
                            filterValue = getCustomDate();
                            if($.comparePrototype(self.filters[colId],filterValue)) return;
                        }
                        self.filters[colId] = filterValue;
                        if(filterValue && filterValue.from === '' && filterValue.to === '' || radio.val() === ''){
                            delete self.filters[colId];
                            $span.removeClass('filter');
                        }else{
                            if(filterValue && !$.checkDate(filterValue.from,filterValue.to)){
                                RiilAlert.info(window.S_VALIDATE_DATE);
                                delete self.filters[colId];
                                return false;
                            }
                            $span.addClass('filter');
                        }
                        if(self.listeners && self.listeners.filter){
                            Layer.close('columnTool');                            
                            self.startLoading();
                            self.listeners.filter();
                        }
                    }
                    var gridOffset = self.$bindDomd.offset();
                    layout.x = layout.x - gridOffset.left;
                    layout.y = layout.y - gridOffset.top;


                    if(!document.getElementById(id)){
                       
                        Layer.init({
                            id : id,
                            x : layout.x,
                            y : layout.y,
                            height : layout.filterHeight,
                            width : layout.filterWidth,
                            isAutoHide : true,
                            remove : false,
                            html : '',
                            autoHide : hideListeners,
                            apply : self.$bindDomd
                        });
                        var cloneFilterDom= $('#'+filterDomId).clone();
                        cloneFilterDom.attr('id',filterDomId + 'clone');
                        Layer.setContent(id, cloneFilterDom.show());
                        var $calendarInput = $('#' + filterDomId + 'clone input.time_text');
                        var $calendarTrigger = $('#' + filterDomId + 'clone a.time_trigger');
                        if($calendarInput.length !== 0){
                            for(var i = 0; i < $calendarInput.length; i++){
                                loadcalendarConf({
                                    input : $calendarInput[i],
                                    trig : $calendarTrigger[i],
                                    isShowClearBtn : true,
                                    time : true,
                                    onSelect:onSelect,
                                    trigger:trigger,
                                    cancel:cancel });
                            }
                        }
                    }

                    
                    Layer.show(id,{x:layout, y:layout, autoHide:hideListeners});                    
                    Layer.relativeJustPositoin(id, 'columnTool', layout, self.$bindDomd);                                        

                    function onSelect (){
                        window.calendarShowTag = 'null';
                    }
                    function trigger (){
                        window.calendarShowTag = 'closed';
                    }
                    function cancel (){                                        
                        window.calendarShowTag = null;  
                    }
                },
                tree:function(gridId,colId,id,filterDomId,layout){

                    var self = this;
                    id = gridId+colId+id;
                    self.currentFilterLayerId = id;
                    self.filterIds[id] = id;
                    function hideListeners(/*event*/){
                        if(self.colModule[colId].hideColunm === 'true' || self.colModule[colId].hideColunm === true){
                            return;
                        }
                        //获取过滤树值
                        var checkValues = self.colModule[colId].getFilterTreeValue(tree);
                        
                        if($.isString(checkValues) && self.filters[colId] == checkValues){
                            return;
                        }else if($.compareArray(self.filters[colId],checkValues)){
                            return;
                        }
                        
                        var $span = self.getTHead().find('th[colid="'+colId+'"]').find('span');
                        self.filters[colId] = checkValues;
                        if(!checkValues || checkValues.length === 0){
                            delete self.filters[colId];
                            $span.removeClass('filter');
                        }else{
                            $span.addClass('filter');
                        }             
                        if(self.listeners && self.listeners.filter){
                            Layer.close('columnTool');
                            self.startLoading();
                            self.listeners.filter();
                        }
                    }
                    if(document.getElementById(id)) return;

                    var gridOffset = self.$bindDomd.offset();
                    layout.x = layout.x - gridOffset.left;
                    layout.y = layout.y - gridOffset.top;
                    Layer.init({
                        id : id,
                        x:layout.x,
                        y:layout.y,
                        height:layout.filterHeight,
                        width:layout.filterWidth,
                        isAutoHide:true,
                        remove:true,
                        html : '',
                        autoHide : hideListeners,
                        apply : self.$bindDomd
                    });
                    
//                    var $layerDom = $("#"+id)
//                    
//                    $layerDom.width("auto");
                    var cloneFilterDom= $('#'+filterDomId).clone();
                    cloneFilterDom.attr('id',filterDomId+'clone');
                    Layer.setContent(id,cloneFilterDom.show());
//                    $layerDom.width(layout.filterWidth);
                    Layer.relativeJustPositoin(id, 'columnTool', layout, self.$bindDomd);
                    
                    var isCheckbox = $('#'+filterDomId).find('input').length > 0;
                    
                    var treeConf = {
                        id:filterDomId+'clone'    
                    };
                    if(!isCheckbox){
                        treeConf.listeners = {
                            nodeClick:function(){}
                        };
                    }
                    
                    
                    var tree = new Tree(treeConf);
                    
                    var values = self.filters[colId]; 
                    if(values){
                        if($.isString(values)){ //多选情况
                            tree.getNodeById(values).setCurrentNode().expandUP();
                        }else{//单选情况
                            for(var i=0;i<values.length;i++){
                                tree.getNodeById(values[i]).setChecked();
                            }                            
                        }
                    }
                }
            }
        }
    };


;/* global GridPanel,Layer,CustomCheck */
/* jshint jquery:true */
(function() {
	'use strict';
	var clz = GridPanel.plugins.columnTools.list = {

		init: function($li, childLeft, param) {
			var renderArgs = $.merge([$li, childLeft], clz._fetchData.call(this, param, $.trim($li.text())));
			renderArgs.push(param.colId);
			clz._renderLayer.apply(this, renderArgs);
			clz._event.call(this, param.colId);
		},

		hide: function(/*$li*/) {
			Layer.close('columnLayer');
		},

		_fetchData: function(param, listName) {
			var data = $.merge([], this.conf.list[listName]); // 配置信息conf.list:{text,value}
			var exclusion = [];
			var curValue = null;

			// 统计已使用值和当前数据
			$.each(this.colModule, function(colId, colConf) {
				if (colConf.list) { // 配置信息 colModule.colId.list:String
					if (colId !== param.colId) exclusion.push(colConf.list);
					else curValue = colConf.list;
				}
			});

			// 不做重复限制：例如当只有两个指标又需要修改顺序时
			// data = $.grep(data, function(every) {
			// 	return $.inArray(every.value, exclusion) < 0;
			// });

			return [data, curValue];
		},

		_renderLayer: function($li, childLeft, data, value, id) {

			var gridOffset = this.$bindDomd.offset();
			var liOffsetTop = $li.offset().top;

			clz.hide($li);
			Layer.init({
				id: 'columnLayer',
				html: clz._renderItems(data, value, id + '_item'),
				x: childLeft - gridOffset.left,
				y: liOffsetTop - 5 - gridOffset.top,
				extendWidth: 20,
				initHide: false,
				remove: true,
				isAutoHide: true,
				apply: this.$bindDomd,
				autoHide: function() {
					Layer.close('columnLayer');
				}
			});

			CustomCheck.batchInit({
				outer: 'columnLayer'
			});

			var width = $('#columnLayer').width();
			var height = $('#columnLayer').height();
			var left = childLeft;
            var newPosition = $.checkDomPosition($('#columnLayer'), left, liOffsetTop-5, width, height);
            left = newPosition.x != left ? left  - width - $li.width() : left;
            Layer._justPosition('columnLayer', left - gridOffset.left, liOffsetTop - 5 - gridOffset.top,  this.$bindDomd);
            if ($('#columnLayer').position().left !== left) {
            	left = $('#columnLayer').position().left - $li.closest('.layer').width();
            	Layer.setPosition('columnLayer', left);
            }
		},

		_renderItems: function(data, value, id) {
			var html = '<ul style="width:100%">';

			$.each(data, function(index, elem) {
				html += '<li style="white-space:nowrap;"><label><input type="radio" name="' + id + '" value="' + 
					elem.value + '"' + (value === elem.value ? ' checked="checked"' : '') + 
					'><span>' + elem.text + '</span></label></li>';
			});

			return html += '</ul>';
		},

		_event: function(colId) {
			if (this.listeners.list) {
				$('#columnLayer input').change($.proxy(function(event) {
					this.listeners.list(colId, $(event.target).val());
				}, this));
			}
		}
	};

})();;
(function(){
/* global GridPanel,$,Validate,CustomSelect */
'use strict';

/*
 * 表格插件部分
 */
GridPanel.plugins.paging = {
	/*
	 * 初始化，配置参数 pageIndex 页索引 pageSize:每页显示条数 recordCount:总条数 maxPage:最大页数
	 */
	init : function(conf) {
		if($.isArray(conf)) return;
		var PluginPaging = GridPanel.plugins.paging;
		this.pagingConf = {
			pageIndex   : $.isNull(conf.pageIndex) ? conf.data.pageIndex : conf.pageIndex, 
			pageSize    : $.isNull(conf.pageSize)  ? conf.data.pageSize : conf.pageSize,
			recordCount : $.isNull(conf.recordCount) ?  conf.data.recordCount : conf.recordCount,
			maxPage     : $.isNull(conf.maxPage) ? conf.data.maxPage : conf.maxPage,
			changePageCount : this.conf.changePageCount
		};

		var nextCursor = this.pagingConf.pageIndex == this.pagingConf.maxPage ? 'cursor:default': '';
		var prevCursor = this.pagingConf.pageIndex == 1 ? 'cursor:default' : '';
		
		var pageId = this.conf.bindDomId + 'Page';
		var $page = $('#' + pageId);
		if($page[0]){
			$page.find('*').unbind().remove();
		}else if (this.$bindDomd){
			$page = $($.createDomStr({
				tagName : 'div',
				attr : {
					id : pageId,
					'class' : 'page'
				},
				content : ''
			}));
			this.$bindDomd.after($page);
		}else {
			$page = this.$target
				.attr('id', pageId)
				.addClass('page');
		}
		
		//跳转文本框Id
		var jumpInputId =  this.conf.bindDomId + '_pageSelect';
		var $jump = $(PluginPaging.createJumpInput(this.pagingConf.maxPage, jumpInputId));
		var $pageBtns = $(PluginPaging.createPageBtns(nextCursor, prevCursor)); 
		var pageMsgStr = PluginPaging.createPageMsgStr(this.pagingConf);

		$page
			.append($jump)
			.append($pageBtns)
			.append(pageMsgStr);
		
		if (this.pagingConf.maxPage == '0' || this.pagingConf.maxPage === 0) {
			if (this.conf.hideWhenNoData) $page.hide();
			else $page.css('visibility', 'hidden');
			return;
		}
		
		//绑定分页按钮
		$pageBtns.children('a').bind('click', {
			'self' : this
		}, PluginPaging.pagingHandler);
		
		$.onlyNumber({input : document.getElementById(jumpInputId)});//不允许输入非数字
		//绑定
		$('#' + jumpInputId).bind('keyup', {
			input : $('#' + jumpInputId),
			max : this.pagingConf.maxPage,
			pageIndex : this.pagingConf.pageIndex,
			conf : this.conf,
			'self' : this
		}, PluginPaging.jumpPage);
		
		
		$('#' + jumpInputId + 'jump').bind('click', {
			input : $('#' + jumpInputId),
			isButton : true,
			max : this.pagingConf.maxPage,
			pageIndex : this.pagingConf.pageIndex,
			conf : this.conf,
			'self' : this
		}, PluginPaging.jumpPage);

		// 初始化“每页条数”下拉框

		if (this.conf.changePageCount) {
			$page.append(PluginPaging.createPageCountChange(this.pagingConf, 
				this.conf.bindDomId + '_pageCountSelect'));

			CustomSelect.init({
				id : this.conf.bindDomId + '_pageCountSelect',
				initSelectAfter : false,
				listeners : {
					selectAfter : $.proxy(function(pageSize) {
						if (this.conf.listeners && this.conf.listeners.changePageSize) {
							this.conf.listeners.changePageSize(pageSize);
						}
					}, this)
				}
			});
		}
	},
	/**
	 * 跳转页面事件处理
	 * 该事件处理文本框输入事件，和按钮点击事件，isButton判断两者
	 * */
	jumpPage : function(event) {
		var $input = event.data.input;
		var iMaxPage = event.data.max;
		var listeners = event.data.conf.listeners;
		var self_grid = event.data.self;
		var isButton = event.data.isButton;
		var iPage = parseInt($input.val(), 10); //输入页数

		if (iPage === event.data.pageIndex) {
			return;
		}

		if (isNaN(iPage) || !Validate.isPlusInt(iPage)) {
			$input.val('');
			return;
		}

		if (iPage > iMaxPage) {
			$input.val(iPage = iMaxPage);
		}else if (iPage === 0) {
			$input.val(iPage = 1);
		}

		if (listeners && listeners.changePage && (event.keyCode == 13 || isButton)) {
			self_grid.startLoading();
			listeners.changePage(iPage, self_grid.pagingConf.pageSize);
		}
	},
	/**
	 * 创建“每页显示多少个”设置项
	 * 
	 *     <span style="float:left;">每页显示 :  </span>
	 * @method createPageCountChange
	 * @param pagingConf {Json} 翻页配置
	 * @return {String} html字符串
	 */
	createPageCountChange : function(conf, id) {
		var text = window.S_COUNTPREPAGE || '!!S_COUNTPREPAGE!!';
		var text2 = window.S_COUNTPREPAGE_2 || '!!S_COUNTPREPAGE_2!!';
		var idName = id;
		var curValue = conf.pageSize + '';
		var selectValues = (function(){
			var values;
			var result = [];
			var val, c;

			if ('boolean' === typeof conf.changePageCount) {
				values = [5, 10, 12, 15, 20, 30, 50, 100];
			}else {
				values = conf.changePageCount;
			}

			result = [];
			val = '';
			c = values.length;
			while (c--) {
				val = values[c] + '';
				result.unshift({text:val, value:val});
			}

			return result;
		}());

		var $label = $('<span style="float:left;">' + text + '</span>');
		var $list = $(window.CustomSelect.getTemplate(idName, idName, selectValues, curValue));
		$list.eq(0)
			.css({
				width : 50 + 'px', 
				marginLeft : 5 + 'px',
				marginRight : 3 + 'px',
				float : 'left'
			});
			
		return $label.add($list).add('<span style="float:left;">' + text2 + '</span>');
	},
	/*
	 * 创建分页信息
	 * <span type="recordCount">26</span><span>总条数</span><span type="maxPage">2</span><span>/</span><span>1</span>
	 * */
	createPageMsgStr : function(pagingConf){
		var pagmsgStr = [];
		pagmsgStr.push('<span type="recordCount">'	+ (pagingConf.recordCount || '') + '</span>');
		pagmsgStr.push('<span>' + window.S_COUNTRECORD + '</span>');
		pagmsgStr.push('<span type="maxPage">' + (pagingConf.maxPage || '') + '</span>');
		pagmsgStr.push('<span>/</span>');
		pagmsgStr.push('<span type="pageIndex">' + (pagingConf.pageIndex || '') + '</span>');
		
		return pagmsgStr.join('');
	},
	/*
	 * 创建分页按钮 
	 * */
	createPageBtns : function(nextCursor, prevCursor){
		var pageBtns = [{type : 'last', 'class' : 'p-last', cursor : nextCursor, title : window.S_ENDPAGE}, 
						{type : '1', 'class' : 'p-next', cursor : nextCursor, title : window.S_NEXTPAGE},
						{type : '-1', 'class' : 'p-prev', cursor : prevCursor, title : window.S_PREVPAGE},
						{type : 'first', 'class' : 'p-first', cursor : prevCursor, title : window.S_FIRSTPAGE}];
		
		var btnsStr = [];
		
		for(var i = 0; i < 4; i++){
			btnsStr.push($.createDomStr({
				tagName : 'a',
				attr : {
					type : pageBtns[i].type,
					title : pageBtns[i].title,
					'class' : 'pageico '+pageBtns[i]['class']
				},
				style : {
					cursor : pageBtns[i].cursor
				},
				content : ''//pageBtns[i].title
			}));
		}		
		
		return $.createDomStr({
			tagName : 'span',
			attr : {
				'class' : 'paging-btns'
			},
			content : btnsStr.join('') 
		});
	},
	/*
	 * 生成分页跳转按钮结构
	 * <span class="jump" style="float:right">
	 * 		<input id="" onfocus="" type="text" class="blueinput" value=""/><a id="" class="page-go" title=""></a>
	 * </span>
	 * */
	createJumpInput : function(maxPage, id, value) {
		return $.createDomStr({
			tagName : 'span',
			attr : {
				'class' : 'jump'
			},
			style : {
				float : 'right'
			},
			content : $.createDomStr({
				tagName : 'input',
				attr : {
					id : id,
					onfocus : 'this.select()',
					type : 'text',
					'class' : 'blueinput pageinput',
					value : value || ''
				},
				style : {
					width : '30px'
				}
			}) + ' ' + $.createDomStr({
				tagName : 'a',
				attr : {
					id : id + 'jump',
					'class' : 'page-go',
					title : window.S_JUMPTO
				}
			})
		});
	},
	/*
	 * 刷新分页-内部使用
	 */
	refresh : function(conf) {
		var PluginPaging = GridPanel.plugins.paging;
		PluginPaging.init.call(this, conf);
	},
	/*
	 * 分页请求事件处理函数
	 */
	pagingHandler : function(event) {
		var $btn = $(this);
		if (this.style.cursor == 'default') {
			return;
		}

		var self_grid = event.data.self;
		var pageNum = $btn.attr('type');

		if (pageNum == 'last') {
			pageNum = self_grid.pagingConf.maxPage;
		} else if (pageNum == 'first') {
			pageNum = 1;
		} else {
			pageNum = parseInt(self_grid.pagingConf.pageIndex, 10) + parseInt(pageNum, 10);
			pageNum = pageNum < 1 ? 1
					: pageNum > self_grid.pagingConf.maxPage ? self_grid.pagingConf.maxPage
							: pageNum;
		}

		if(self_grid.pagingConf.maxPage == self_grid.pagingConf.pageIndex && pageNum == self_grid.pagingConf.maxPage){
			return;
		}
		if(self_grid.pagingConf.pageIndex == 1 && pageNum == 1){
			return;
		}


		var listeners = self_grid.conf.listeners;
		if (listeners && listeners.changePage) {
			if (self_grid.isNodeData){
				return;
			}
			self_grid.startLoading();
			listeners.changePage(pageNum, self_grid.pagingConf.pageSize);
		}
	},
	/*
	 * 跳转页事件处理函数
	 */
	jumpPagingHandler : function(event) {
		var self_grid = event.data.self;
		var listeners = self_grid.conf.listeners;
		if (listeners && listeners.changePage) {
			if (self_grid.isNodeData){
				return;
			}
			self_grid.startLoading();
			listeners.changePage(this.value, self_grid.pagingConf.pageSize);
		}
	}
};

})();;/**
  * 行可点击插件
  * 事件：
  * rowClick :  点击行事件 参数：rowData点击行的数据
  * 
  */
GridPanel.plugins.rowSelect = {
        init: function(conf) {
            var rows = this.getRows();
            var $tr = null;
            for(var i=0,len = rows.length;i<len;i++){
                $tr = $(rows[i]);
                if($tr.attr("type") != "empty"){
                    $tr.css("cursor","pointer").bind("click", {
                        self : this
                    }, GridPanel.plugins.rowSelect.rowClickHandler);
                }
            }
        },
        /**
         * 点击行事件回调函数
         * */
        rowClickHandler:function(event){
        	event.preventDefault();
        	event.stopPropagation();
            var self = event.data.self;
            var index = self.getRows().index(this);
            if(self.listeners.rowClick){
                self.listeners.rowClick(self.data[index], /*$tr*/$(this), index);
            }
        },
        refresh : function(conf){
          GridPanel.plugins.rowSelect.init.call(this,conf);
        }
    };


;/**
 * 表格插件部分
 * 排序
 */
GridPanel.plugins.serial = {
	init : function(conf) {
		GridPanel.plugins.serial.reset.call(this);
	},
	reset : function(event) {		
 		var rows = this.getRows();
		var $tr = null;
		for(var i = 0; i< rows.length; i++){
			$tr = $(rows[i]);
			$tr.children(":first").html(i+ 1);
		}     
	}
}


;/**
 * 表格插件部分
 * 排序
 */
GridPanel.plugins.sort = {
	init : function(conf) {
		var $headTable = this.getTHead();
		var $as = $headTable.find("a.rank");
		for ( var i = 0; i < $as.length; i++) {
			var $a = $($as[i]);
			var sortTag = $a.attr("sortTag");
			var $headText = $a.parent();
			if ($headText.data("isBind") == true)
				continue;
			if(sortTag){
				$headText.attr("sortTag",sortTag);
				$a.removeAttr("sortTag");
			}
			$headText.bind("click", {
				self : this,				
				$headTable : $headTable
			}, GridPanel.plugins.sort.clickEvent).css("cursor", "pointer")
					.data("isBind", true);
		}
		$headTable.find("th[onlySort][sorttag]").css("cursor","default");


	},
	clickEvent : function(event) {
		Layer.close("columnTool");
		var layerInner = $('div[id$="filterLayer"]');
        for(var i=0; i < layerInner.length; i++){
           Layer.close($(layerInner[i]).attr("id"));
        }
		var self = event.data.self;
        if (self.isNodeData == true){return;}
		var $sortTH = $(this);


		var sortTag = $sortTH.attr("sortTag");
		var sortFirst = $sortTH.attr('sortFirst');
		if (sortTag) {
			sortTag = sortTag == "asc" ? "desc" : "asc";
		} else {
			sortTag = sortFirst || "asc";
		}
		var isNoEvent = null;//不更改排序列
		//指定只能单项排序,如果onlySort值为"desc,noEvent",或者"asc,noEvent"那么标识点击以后没有任何操作
		if($sortTH.attr("onlySort")){
			var onSortValue = $sortTH.attr("onlySort").split(',');
			isNoEvent = onSortValue[1];
			sortTag = onSortValue[0];
			$sortTH.css("pointer","default");
		}


		var $oldSortTag = event.data.$headTable.find("th[sortTag]").css("pointer","pointer");
		if ($oldSortTag[0] != $sortTH[0]) {  //如果点击不是上一次点击的列
			$oldSortTag.removeAttr("sortTag");
		}else if($sortTH.attr("onlySort") && !isNoEvent){ //点击两次是同一列，并且只有单项排序
			return;
		}
		


		

		$sortTH.attr("sortTag", sortTag);
		var $sortA = $sortTH.children('a[sortMethod="' + sortTag + '"]');

		if(!isNoEvent){ //如果没有值，则标识更改排序列，否则不更改排序列显示，添加该属性是因为smrat，排序如果后台异常，前台不应该更改排序
			event.data.$headTable.find("a.rank").css("display", "none");//隐藏上一次的排序按钮
			$sortA.css("display", "inline-block");//将本次排序按钮显示
		}
		
		self.startLoading();
		if (self.listeners && self.listeners.sort) {
			self.sortId = $sortA.attr("sortValue");
			self.sortType = $sortA.attr("sortMethod");
			var sortResult = self.listeners.sort(self.sortId, self.sortType,$sortA.attr("sortClassType"));
			if(sortResult === false) {
				self.stopLoading();
			}
		}
		
	}
}


;/*
 * 单元格特殊渲染
 */
GridPanel.render = {
    /*
     * 渲染公共方法
     * */
	common :{
		/**
		 * 是否渲染
		 * @param conf.isRender 列模式指定，绘制该列时动态判断是否需要渲染
		 * 如果判断返回false,则不渲染，直接将原始值返回。
		 * 如果判断返回字符串，怎直接返回字符串，
		 * 如果没有指定判断条件，则返回null
		 * */
		isRender : function(conf){
			var isRenderListeners = conf.colModule.isRender; 
            if(isRenderListeners){
            	var renderResult = isRenderListeners(conf.rowVal);
                if( renderResult === false){
                    return conf.cellVal;
                }
                if($.isString(renderResult)){ //如果会滴撒哦返回
                	return renderResult;
                }
            }
            return null;
		}
	}
};



;/**
 * 针对不同单元格的render
 * 行数据必须有dataType属性，用于指定渲染方式
 * */
GridPanel.render.cellRender = {
    render:function (cellVal, colModule, rowVal, colId) {
        var gridRender = GridPanel.render[rowVal.dataType];
        if (gridRender) {
            return gridRender.render.call(this,cellVal, colModule, rowVal, colId)
        }
        return "";
    },
    bindEvent:function($td){
        var renderType  = $td.find('[renderType]').attr("renderType");
        var gridRender = GridPanel.render[renderType];
        if(gridRender){
            return gridRender.bindEvent.call(this,$td);
        }
    },
    /*设置单元格值
     * @param conf.$td 单元格
     * @param conf.value 值
     * @param conf.rowVal 单元格所在行数据
     * */
    setValue : function(conf){
        var gridRender = GridPanel.render[conf.rowVal.dataType];
        if (gridRender) {
            return gridRender.setValue.call(this,conf);
        }   	
    }
    
}


;/**
 * 复选框
 * 
 * 表格监听 beforeCheckRenderDisable 渲染复选框之前调用，确定是否不可用 beforeCheckRender 渲染是否选中复选框
 * beforeCheck,
 * 
 */

GridPanel.render.checkbox = {
	/*
	 * 渲染 如果设置了渲染复选框前事件回调，则执行回调，如果需要选中，返回true,否则false
	 */
	render : function(cellVal, colModule, rowVal, colId) {
		var isRender = GridPanel.render.common.isRender({
			colModule : colModule,
			cellVal : cellVal,
			rowVal : rowVal
		});
		if (isRender !== null) {
			return isRender;
		}

		var listeners = this.conf.listeners || {};

		var attr = {
			type : "checkbox",
			value : cellVal,
			colid : colId,
			disabled : listeners.beforeCheckRenderDisable
					&& listeners.beforeCheckRenderDisable(rowVal, colId) ? "disabled"
					: null
		}

		if (colModule.fetch) {
			var fetch = colModule.fetch;
			attr.checkedvalue = fetch.checked;
			attr.uncheckedvalue = fetch.unchecked;
			if (cellVal == fetch.checked) {
				attr.checked = "checked";
			}
			if (fetch.half == rowVal[colId]) {
				attr.half = "half";
			}
		}else{

			attr.checked = listeners.beforeCheckRender
				&& listeners.beforeCheckRender(rowVal, colId) ? "checked"
				: null;			
		}


		return $.createDomStr({
			tagName : "input",
			attr : attr
		}) +(rowVal ? "" :(colModule.text ? colModule.text : ''));
	},
	bindEvent : function($td, isAll) {
		var RenderCheck = GridPanel.render.checkbox;
		var listeners = this.conf.listeners || {};
		var data = {
			self : this,
			$td : $td
		};
		if (isAll) {// 全选按钮
			var $headCheckbox = $td.find("input[type='checkbox']");
			CustomCheck.init({apply : $headCheckbox});
			CustomCheck.click($headCheckbox, RenderCheck._allCheck, data);			
			var index = $td.parent().children().index($td[0]);
			if (this.data.length > 0
					&& this.getChecks(index).length == this.data.length) {
				$headCheckbox.attr("checked", "checked");
			}
			RenderCheck.judgeAllChecked.call(this, {colId : $td.attr("colid")} );
		} else {
			var $checkbox = $td.find("input[type='checkbox']");
				CustomCheck.init({apply : $checkbox});
				/*
			     * 复选框点击事件// 触发复选之前调用自定义事件
				 */
				function _clickListener(event){
					var self = event.data.self;
					var listeners = self.listeners;
					// 触发复选之前调用自定义事件
					if(listeners && listeners.beforeCheck){
						listeners.beforeCheck.call(this, event);
					}
					// 触发复选之前调用自定义事件
					if(listeners && listeners.checking){
						listeners.checking.call(this, event);
					}
					RenderCheck.childCheck.call(this, event);
				}
				CustomCheck.click($checkbox, _clickListener, data);
		}

	},
	/**
	 * 判断表格指定列复选框是否全选,如果全选，全选按钮选中，否则不选中。
	 * 
	 * @param conf.colId
	 *            指定列ID
	 */
	judgeAllChecked : function(conf) {
		var colId = conf.colId;
		var self = this;
		var $headCheckbox = self.getTHead().find(
				'th[colid="' + colId + '"] input[type="checkbox"]');
		var $bodyCheckboxs = self.getTBody().find(
				'td[colid="' + colId + '"] input[type="checkbox"]:not(:disabled)');
		var $checkeds = $bodyCheckboxs.filter(":checked");
		var checkedLen = $checkeds.length;//子复选框选中的个数
		var allLen = $bodyCheckboxs.length;//全部子复选框的个数
		var isCheck =  allLen > 0 && checkedLen == allLen;//有数据并且全选的个数等于总个数		
		CustomCheck.setCheck($headCheckbox, isCheck, false);
		if(!isCheck && checkedLen != 0){ //半选状态
			CustomCheck.setHref($headCheckbox);	
		}
		//如果无数据，则全选按钮不可用
		if(self.isNodeData === true || $bodyCheckboxs.length == 0){
			CustomCheck.setCheck($headCheckbox, false, false);
			CustomCheck.setAvailable($headCheckbox, false);
		}else{
			CustomCheck.setAvailable($headCheckbox, true);
		}
		return isCheck;
	},
	/**
	 * 单个复选框选中状态变更
	 * 
	 * @param {Object}
	 *            event
	 */
	childCheck : function(event) {
		var RenderCheck = GridPanel.render.checkbox;
		var self = event.data.self;
		
		var $td = event.data.$td;
		var colId = $td.attr("colid");
		var queryStr = '[colid="' + colId + '"] input[type="checkbox"]';
		var $headCheckbox = self.getTHead().find('th' + queryStr);
		
		var $checks = self.getTBody().find('td' + queryStr);
			
		RenderCheck._treeCheck($td, self);
		RenderCheck.judgeAllChecked.call(self, {colId : colId});
		var rowIndex = self.getRows().index($td.parent()[0]);
		self.data[rowIndex][colId] = this.value;
		
	},
	/*
	 * tree复选
	 */
	_treeCheck : function($td, self){
		var $tr = $td.parent();
		var $rows = self.getRows();
		var parentlevel = $tr.attr("parentlevel");
		//var level = $tr.attr("level");
		//var rowindexcustumer = $tr.attr("rowindexcustumer");
		//如果点击的是树节点子复选框
		if (parentlevel) { 
			$checks = self.getTBody().find(
					'tr[parentlevel="' + parentlevel + '"] td[colId="'
							+ $td.attr("colId")
							+ '"] input[type="checkbox"]:not([disabled])');
			var treeChildCheckLen = $checks.length;
			var treeChildCheckedLen = $checks
					.filter(":checked:not([disabled])").length;
			//parentlevel = parentlevel.split("");
			var $parentCheckbox = self.getTBody().find(
					"tr[level='" + parentlevel[0] + "'][rowindexcustumer='"
							+ parentlevel.substring(1) + "'] td[colId='"
							+ $td.attr("colId")
							+ "'] input[type='checkbox']:not([disabled])");
			if(treeChildCheckedLen != 0 && treeChildCheckLen != treeChildCheckedLen){//半选
				CustomCheck.setHref($parentCheckbox);
			}else{
				//父节点根据子节点是否全选中决定
				CustomCheck.setCheck($parentCheckbox, treeChildCheckLen == treeChildCheckedLen, false);			
			}
		}
		//点击的是父节点,需要循环选中后代节点
		GridPanel.render.checkbox._checkDescendant($td, self);		
		
	},
	/**选中后代节点*/
	_checkDescendant : function($td, self){
		var $tr = $td.parent();
		var $rows = self.getRows();
		var parentlevel = $tr.attr("parentlevel");
		var level = $tr.attr("level");
		var rowindexcustumer = $tr.attr("rowindexcustumer");
		
		//点击的是父节点
		if (level && rowindexcustumer) {			
			var colId = $td.attr("colId");
			var $parentcheckbox = $tr.find("input:checkbox[colid='" + colId + "']:not([disabled])");
			var $childcheckbox = self.getTBody().find(
					"tr[parentlevel='" + level + rowindexcustumer
							+ "'] input:checkbox[colid='" + colId + "']:not([disabled])");
			

			var $until = $($childcheckbox[0]).parentsUntil("tr");
			var $tr = $($until[$until.length - 1]).parent();
			var index = $rows.index($tr[0]);
			var newArray = [];
			var copyNodes = $.copyArray($childcheckbox);
			
			var interval = setInterval(function(){
				if(copyNodes.length <= 0){
					clearInterval(interval);

				}else{
					 var childs = [];
		            if(copyNodes.length <=0){
		                copyNodes = newArray;                            
		            }
		            var nodes = copyNodes.splice(0,30);
					CustomCheck.setCheck(nodes, CustomCheck.isChecked($parentcheckbox[0]), function($checkbox){							
						self.data[index++][colId] = $checkbox.val();
						var $td = $checkbox.parent().parent().parent();
						if(!$td.parent().attr("leafRow")){
							GridPanel.render.checkbox._checkDescendant($td, self);	
						}						
					}, false);
				}
			},10);

		}
	},
	/*
	 * 全选事件处理函数-不对外公布
	 */
	_allCheck : function(event) {
		var self = event.data.self;
		if (self.isNodeData == true) {
			return false;
		}		
		var colId  = event.data.$td.attr("colid");
		var $tbody = self.getTBody();
		var inputs = $tbody.find('input:checkbox[colid="' + colId + '"]:not([disabled])');
		var $checkbox = null;		
		for(var i = 0, len = inputs.length; i < len; i++){
			$checkbox = $(inputs[i]);
			CustomCheck.setCheck($checkbox, this.checked, false);			
			var trs = self.getRows();
			self.data[trs.index($checkbox.parent().parent().parent().parent()[0])][colId] = $checkbox.val();
		}
		if(self.listeners && self.listeners.allCheck){
			self.listeners.allCheck(this);
		}
	},

	getValue : function($td) {
		return $td.children().children().val();
	},
	/**
	 * 获得选中的复选框，--不对外提供
	 * 
	 * @param {Object}
	 *            index 列ID 或列索引 return 复选框jquery对象抓去结果
	 */
	getCheck : function(index) {
		if ($.isString(index)) {
			var query = 'tr td[colid="' + index + '"]';
			return this.getTBody().find(
					query + ' input[type="checkbox"]:checked').toArray();
		} else {
			var array = [];
			var trs = this.getRows();
			for ( var i = 0, len = trs.length; i < len; i++) {
				var input = $(trs[i]).find(
						'td:eq(' + index + ') input[type="checkbox"]:checked');
				if (input && input[0]) {
					array.push(input);
				}
			}
			return array;
		}
	},
	/**
	 * 设置复选框选中或取消
	 * 
	 * @param columnIndex
	 *            列索引/列Id
	 * @param rowIndexs
	 *            需要选中行的行号 Array
	 * @param type
	 *            选中/未选中
	 */
	setChecked : function(columnIndex, rowIndexs, type) {
		var $input = null;
		rowIndexs = !$.isArray(rowIndexs) ? [rowIndexs] : rowIndexs;
		for ( var i = 0, len = rowIndexs.length; i < len; i++) {
			if ($.isString(columnIndex)) {
				var query = 'tr:not([placeholder]):eq(' + rowIndexs[i] + ') td[colid="'
						+ columnIndex + '"]';
				$input = this.getTBody()
						.find(query + ' input[type="checkbox"]').toArray();
			} else {
				$input = this.getTBody().find(
						'tr:not([placeholder]):eq(' + rowIndexs[i] + ') td:eq(' + columnIndex
								+ ') input[type="checkbox"]');
			}
			CustomCheck.setCheck($input, type);
		}
	},
	getHeadCheck : function(index) {
		if ($.isString(index)) {
			var query = 'tr td[colid="' + index + '"]';
		} else {
			var query = 'tr td:eq(' + index + ')';
		}
		return this.getTHead().find(query + ' input[type="checkbox"]')
				.toArray();
	},
	/**
	 * 获得复选框所在行 --不对外提供
	 * 
	 * @param {Object}
	 *            checks 选中复选框集合
	 */
	getIndexByCheckBox : function(checks) {
		var $trs = this.getTBody().children("tr:not([placeholder])");
		var indexs = [];
		for ( var i = 0, len = checks.length; i < len; i++) {
			var check = checks[i];
			if (!check.tagName)
				check = check[0];
			var tr = check.parentNode.parentNode.parentNode.parentNode; // checkbox->div->td->tr
			indexs.push($trs.index(tr));
		}
		return indexs;
	},
	/*
	 * 重置指定列复选框状态
	 */
	reset : function(conf){
		var RenderCheck = GridPanel.render.checkbox;
		RenderCheck.judgeAllChecked.call(this, {colId : conf.colId});
	}
}
;/**
 * 复选框组
 * 当前值用逗号分割,
 * 枚举值如果行数据中指定，使用行数据，否则从列模式中指定，列模式属性enumVal:{text:,value:}
 * 列模式参数：
 * enumKey：在行数据中备选数据的属性key
 * 
 * */
GridPanel.render.checkBoxGroup = {
	render : function(cellVal, colModule, rowVal, colId) {
		var enumVal = rowVal[colModule.enumKey] || colModule.enumVal; 
		
		var currentVal = [];
		if (cellVal) {
			currentVal = cellVal.split(",");
		}
		var _dom = [];
		for ( var i = 0; i < enumVal.length; i++) {
			var isChecked = null;
			for ( var j = 0, jlen = currentVal.length; j < jlen; j++) {
				if (currentVal[j] == enumVal[i].value) {
					isChecked = 'checked"';
				}
			}
			
			var checkboxStr = $.createDomStr({
									tagName : "input",
									attr : {
										colId : colId,
										type : "checkbox",
										value : enumVal[i].value,
										checked : isChecked
									}
								});
			
			var itemStr = $.createDomStr({
								tagName : "label",
								attr :{
									renderType : "checkBoxGroup"
								},
								content : checkboxStr	+ enumVal[i].text
								});
			
			_dom.push(itemStr);
		}
		return _dom.join("");
	},
	bindEvent : function($td) {
		var self = this;
		var checkboxs = $td.find('input[type="checkbox"]');

		checkboxs.bind("change", function(event) {
			var values = [];
			var checkeds = checkboxs.filter(":checked");
			for ( var i = 0; i < checkeds.length; i++) {
				values.push(checkeds[i].value);
			}
			values = values.join(",");
			var $trs = self.getRows();;
			var index = $trs.index($td.parent());
			if (self && self.data) {
				self.data[index][$td.attr("colid")] = values;
			}
			if(self.conf.listeners && self.conf.listeners.checkboxGroupChange){
				self.conf.listeners.checkboxGroupChange(self.data[index],$td.attr("colid"));
			}
		});

	}
}
;/*
 * 日期格式化
 */
GridPanel.render.date = {
	  render: function(cell, colModule) {
            if(cell == "") return "-";
            if(isNaN(cell)){
                cell = new Date(cell).getTime();
            }else{
                cell = parseInt(cell);
            }
            if(colModule.dateFormate){
            	var formate = colModule.dateFormate;
            }else{
            	if(window.defaultLocale == "en_US"){
            		var formate = "{month}/{date}/{year} {hours}:{minutes}:{seconds}";
            	}else{
            		var formate = "{year}-{month}-{date} {hours}:{minutes}:{seconds}";
            	}
            }
            var date = $.formatDate(cell, formate); 
            return '<span title="' + date + '">' +date + '</span>';
        },
        bindEvent: function() {},
        getValue: function() {}
    }
;/**
 * 可编辑单元格
 * 列模式添加属性
 * inputWidth : 文本框宽度
 * isDisable : 根据行数据动态判断文本框是否可用
 * rowFlag : 文本框行标识表示文本框唯一标示，rowFlag用于指定行数据中的某个唯一属性值
 * validate : 验证类型，参考FormValidate
 * isBindRemote : 文本框是否有验证功能
 * remoteUrlFn  : 远程验证url
 * customValidate : 文本框自定义验证
 * */
GridPanel.render.editor = {
        /*
         * 渲染         * 
         */
        render : function(cellVal, colModule, rowVal, colId) {
        	var isRender = GridPanel.render.common.isRender({
        		colModule : colModule,
        		cellVal : cellVal,
                rowVal : rowVal
        	});
        	if(isRender !== null){
        		return isRender;
        	}
        	
        	var attr = {
        		"class" : "cell_editor_input blueinput "+(colModule.isbordernone ? 'input_none':''),
        		colId : colId,
        		type : "text",
        		value : cellVal,
        		name : colId + rowVal[colModule.rowFlag],
        		disabled : colModule.isDisable && colModule.isDisable(rowVal) === true ? "disabled" : null
        	};
        	
        	if(colModule.validate){
        		attr.validate = colModule.validate;
        	}
        	
            if(colModule.ErrorMsg){
                for(var key in colModule.ErrorMsg){
                	attr[key] = colModule.ErrorMsg[key];
                }
            }
            
            //添加例如max="3"验证规则
            if(colModule.validateRule){
                for(var key in colModule.validateRule){
                	attr[key] = colModule.validateRule[key];
                }                
            }
        	
            //每一行的该列验证规则不同，规则从行数据中获取validateRuleKey为数组json
            if(colModule.validateRowRule){
                var validateRule = colModule.validateRowRule(rowVal);
                if(validateRule){
                    for(var r=0;r<validateRule.length;r++){
                        for(var rule in validateRule[r]){
                        	attr[rule] = validateRule[r][rule];
                        }
                    }
                }
            }
        	var inputStr = $.createDomStr({
		        		tagName : "input",
		        		attr : attr,
		        		style : {
		        			display : "inline",
		        			width : colModule.inputWidth
		        		}
		        	});
            return inputStr + (colModule.append ? colModule.append(rowVal) : "")
        },
        /**
         * 绑定事件 对每个表格分别生成一个文本框，用于编辑单元格
         * 
         * @param $td
         *            单元格jquery对象
         */
        bindEvent : function($td) {
            var $input = $td.children().children('input[type="text"]');
            if($input.length == 0) return;
            var $next = $input.next();
            var colId = $input.attr("colId");
            if(!$input[0].style.width){
                $input.css("width","98%");
            }
            //将input从外层div中脱离出来，直接在td元素下，并将div删除
            $td.append($input).append($next).css("textOverflow","clip");
            $input.prev().remove();
            
            $input.css("display","inline");
            $input.bind("keyup",{self:this,$td:$td},GridPanel.render.editor.keypressListeners);
            //文本框具有验证功能
            if($input.attr("validate")){
                FormValidate.initDefault(this.conf.bindDomId, true);
            }            
            if(this.colModule[colId].isBindRemote !== true && this.colModule[colId].remoteUrlFn){
                FormValidate.addRemoteUrl(this.conf.bindDomId,colId,this.colModule[colId].remoteUrlFn);
                this.colModule[colId].isBindRemote = true;
            }
            
            if(!$input.attr("isBindCustomValidate") && this.colModule[colId].customValidate){
                FormValidate.addCustomValidate(this.conf.bindDomId, $input, null, this.colModule[colId].customValidate, this.colModule[colId].ErrorMsg.customValidateErrorMsg);
                $input.attr("isBindCustomValidate","true");
            }

            if($input.hasClass('input_none')){
                var oldvalue = '';
                $input.focus(function(){
                    oldvalue = this.value;
                    $(this).removeClass('input_none');
                });

                $input.blur(function(){
                    if(oldvalue != this.value){
                        $(this).addClass('edited');
                    }
                    $(this).addClass('input_none');
                });

            }
            



        },
        keypressListeners:function(event){
            GridPanel.template.setTDVal.call(event.data.self,event.data.$td, this.value);
        },
        getValue : function($td) {
            return $td.children().children('input[type="text"]').val();
        },
        /*设置单元格值
         * @param conf.$td 单元格
         * @param conf.value 值
         * @param conf.rowVal 单元格所在行数据
         * */          
        setValue : function (conf) {
        	var self = this;
        	conf.$td.children().children('input[type="text"]').val(conf.value);
        	conf.rowVal[$td.attr("colid")] = value;
        }
    }




/*
 * 编辑文本框显示处理函数，内部使用
 
showEditor : function($td) {
    if (!GridPanel.render.editor[this.conf.bindDomId]) {
        GridPanel.render.editor[this.conf.bindDomId] = {};
    }
    
    if (!GridPanel.render.editor[this.conf.bindDomId].$cellEditor
            || !GridPanel.render.editor[this.conf.bindDomId].$cellEditor[0]) {
        GridPanel.render.editor[this.conf.bindDomId].$cellEditor = $('<input class="cell_editor_input" type="text" style="width:99%"/>');
    }
    GridPanel.render.editor[this.conf.bindDomId].$cellEditor.unbind();
    
    GridPanel.render.editor[this.conf.bindDomId].$cellEditor.bind(
            "blur", {
                self : this
            }, GridPanel.render.editor.hideEditor).bind("keydown",
                    {
                self : this
                    }, GridPanel.render.editor.hideEditor).focus(
                            function() {
                                $("#t").append("<p>focus</p>")
                            }).click(function() {
                                this.focus();
                                $("#t").append("<p>click</p>")
                            });
    $td.append(GridPanel.render.editor[this.conf.bindDomId].$cellEditor);
    var $text = $td.children("div.cell_inner").hide();
    GridPanel.render.editor[this.conf.bindDomId].$cellEditor.val($text.text()).width($td.width()-8).show().focus();
},*/
/*
 * 编辑文本框隐藏处理函数，内部使用

hideEditor : function(event) {
    var self = event.data.self;
    var flag = true;
    if (event.keyCode) {//回车确认
        if (event.keyCode == 13) {
            flag = true;
        } else {
            flag = false;
        }
    }
    if (flag) {
        var editorValue = GridPanel.render.editor[self.conf.bindDomId].$cellEditor.val();
        var $td = GridPanel.render.editor[self.conf.bindDomId].$cellEditor.parent();
        GridPanel.template.setTDVal    .call(self,$td, editorValue).show();
        GridPanel.render.editor[self.conf.bindDomId].$cellEditor.hide();
        if($td.next().attr("render")=="editor" && event.keyCode == 13){
            GridPanel.render.editor.showEditor.call(self, $td.next());
        }
    }
},
 
 */

;/**
 * 仿苹果复选框 列模式 checked 选中值 unchecked 未选中值 checkedText 选中显示文本 uncheckedText 未选中文本
 * 
 */
GridPanel.render.iCheckbox = {
	render : function(cell, colModule, rowData, columnName) {
		return iCheckBox._createDom({
			checked : colModule.checked,
			unchecked : colModule.unchecked,
			checkedText : colModule.checkedText,
			uncheckedText : colModule.uncheckedText,
			value : cell
		});
	},
	bindEvent : function($td) {
		var self = this;
		iCheckBox._bindEvent($td.children().children(
				"div[compType='icheckbox']"), function($dom) {
			var $trs = self.getRows();//getTBody().children("tr");
			var $li = $td.parent();
			var index = $trs.index($li[0]);
			if (self && self.data) {
				self.data[index][$td.attr("colid")] = $dom.attr("value");
			}
		});
	}
}; /**
	 * 单元格内为图标按钮 监听 
	 * 列模式
	 * icon  图标样式
	 * 
	 * icoEvent {} 自定义图标事件
	 * 
	 */
 GridPanel.render.ico = { 
	 render: function(cell, colModule) {
           return '<span type="ico" class="' + colModule.icon + '"></span>';
     },
     bindEvent: function($td) {
          var listeners = this.listeners || {};
             if (listeners.icoEvent) {
                    var $ico = $td.children("div").children("span[type='ico']");
                    for (var eventName in listeners.icoEvent) {
                        $ico.bind(eventName, {
                            self: this
                        },
                        function(event) {
                            var self = event.data.self;
                            var $trs = self.getRows();//getTBody().children("tr");
                            var li = this.parentNode.parentNode.parentNode;
                            listeners.icoEvent[eventName]($trs.index(li));
                        });
                    }
                }
        },
        getValue: function() {
            return "";
        }
}


;/**
 * 行移动渲染
 */
GridPanel.render.move = {
        /*
         * 绘制移动按钮
         */
        render: function(cellVal, colModule, rowData, columnName) {
            var isRender = GridPanel.render.common.isRender({
                colModule : colModule,
                cellVal : cellVal,
                rowVal : rowData
            });
            
            var goTopText = window["S_BTN_GO_TOP"] || "!!S_BTN_GO_TOP!!";
            var goBottomText = window["S_BTN_GO_BOTTOM"] || "!!S_BTN_GO_BOTTOM!!";
            
            if (isRender !== null) {
                return isRender;
            }
            
            return '<span type="move" class="ico-go-top" title="' + goTopText + 
                '"></span><span type="move" class="ico-go-bottom" title="' + 
                goBottomText + '"></span>';
        },
        /*
         * 绑定事件
         */
        bindEvent : function($td) {
            var $ico = $td.children("div").children("span[type='move']");
            if(!$ico[0]) return;
            var $li = $td.parent();
            var $tbody = $li.parent();
            var trs = $tbody.children("tr:not([placeholder])");
            var index  = trs.index($li[0]);
            GridPanel.render.move._inspect($tbody);
            
            $ico.bind("click", {
                self : this
            }, function(event) {
                var $move = $(this);
                var colId = $move.parent().parent().attr("colId");
                var self = event.data.self;
                var $trs = self.getRows();//getTBody().children("tr");
                var $li = $move.parent().parent().parent();// div>td>tr
                var index = $trs.index($li[0]);
                var currentRowData = self.data[index]; // 待移动行数据
                if ($move.hasClass("ico-go-top") && index != 0) {
                    $li.insertBefore($li.prev());
                    var prevRowData = self.data[index - 1]; // 上一行数据
                    var preColVal = prevRowData[colId];
                    var currentColVal = currentRowData[colId];
                    prevRowData[colId] = currentColVal;
                    currentRowData[colId] = preColVal;
                    self.data[index] = prevRowData;
                    index--;
                    self.data[index] = currentRowData;
                } else if ($move.hasClass("ico-go-bottom")
                        && index < $trs.length) {
                    $li.insertAfter($li.next());
                    var nextRowData = self.data[index + 1]; // 上一行数据
                    
                    var nextColVal = nextRowData[colId];
                    var currentColVal = currentRowData[colId];
                    nextRowData[colId] = currentColVal;
                    currentRowData[colId] = nextColVal;                    
                    
                    self.data[index] = nextRowData;
                    index++;
                    self.data[index] = currentRowData;
                }
                GridPanel.render.move._inspect.call(self,self.getTBody());
                
                self.refreshRowColor();
            });
        },
        getValue : function() {
            return "";
        },
        _inspect:function($tbody){
            $tbody.find("span[type='move']").show();
            var $trs = $tbody.children("tr:not([placeholder])");
            var $levelTr = $trs.filter("[level]");
            // 树表
                
            for(var i=0;i<$levelTr.length;i++){
                   var $li = $($levelTr[i]);
                   $li.prev().find("span.ico-go-bottom").hide();
                   $li.next().find("span.ico-go-top").hide();
            }
             
            $($trs[0]).find("span.ico-go-top").hide();
            var icos = $($trs[$trs.length-1]).find("span.ico-go-bottom").hide();            
            if(icos.length == 0){
                $($trs[$trs.length-2]).find("span.ico-go-bottom").hide();            
            }

            if(this.plugins){
                for (var i = 0, len = this.plugins.length; i < len; i++) {
                    if(this.plugins[i] == "serial"){
                       var plugin = GridPanel.plugins[this.plugins[i]];
                        if (plugin && plugin.reset) {
                            plugin.reset.call(this);
                        } 
                    }
                }
            }
        },
        reset : function(){
             GridPanel.render.move._inspect.call(this,this.getTBody());
        }
    };    /**
     * 
     * 单选框
     * 列模式：
     * beforeRadioRenderDisable  渲染复选框之前调用，确定是否不可用 
     * beforeCheckRender 渲染是否选中复选框
     * 
     * 
     * */
 GridPanel.render.radio = {
    	render : function (cellVal, colModule, rowVal, colId) {
    		var isRender = GridPanel.render.common.isRender({
    			colModule : colModule,
    			cellVal : cellVal,
                rowVal : rowVal
    		});
    		if (isRender !== null) {
    			return isRender;
    		}
    		
            var listeners = this.conf.listeners || {};
            var checked = "";
            var disabled = "";
    		var attr = {
    				type : "radio",
    				value : cellVal,
    				name : colId,
    				disabled : listeners.beforeRadioRenderDisable
    						&& listeners.beforeRadioRenderDisable(rowVal, colId) ? "disabled"
    						: null
    			}

    			if (colModule.fetch) {
    				var fetch = colModule.fetch;
    				attr.checkedvalue = fetch.checked;
    				attr.uncheckedvalue = fetch.unchecked;
    				if (cellVal == fetch.checked) {
    					attr.checked = " checked ";
    				}
    			}

    			attr.checked = listeners.beforeCheckRender
    					&& listeners.beforeCheckRender(rowVal, colId) ? "checked"
    					: null;
                attr.colId = colId;
    			return $.createDomStr({
    				tagName : "input",
    				attr : attr
    			});
    	},
    	bindEvent: function($td, isAll) {
    		
    	},
    	/**
    	 * 获得选中的单选框
    	 * */
    	getCheck : function(index){
            if ($.isString(index)) {                
                return this.getTBody().find('input[type="radio"][colid="' + index + '"]:checked')[0];
            } else {
                var trs = this.getRows();
                for(var i = 0; i < trs.length; i++){
                    var $radio = $(trs[i]).children(":eq(" + index + ")").find('input[type="radio"]:checked')
                    if($radio && $radio[0]){
                        return $radio[0];
                    }
                }
            }
    	},
        /**
         * 获得单选框所在行 --不对外提供
         * 
         * @param {Object}
         *            单选框 选中复选框集合
         */
        getIndexByRadio: function(radio) {
            if(!radio) return -1;
            var $trs = this.getRows();//getTBody().children("tr");
            var tr = radio.parentNode.parentNode.parentNode; // radio->div->td->tr
            return $trs.index(tr);
        }
    }
 


;/**
 * 范围值
 * 当前值格式：开始值,结束值
 * 
 * 行数据范围值，在行数据中定义属性rangeValue
 * 行数据单位   在行数据中定义属性unit
 * 
 * */
GridPanel.render.range = {
        /**
         * unitValidate:{}//根据单位确定验证规则
         * */
        render:function(cellVal, colModule, rowVal, colId){
            var startVal = "",endVal = "";
            if(cellVal !=""){
                var rangeVal = cellVal.split(",");
                startVal = rangeVal[0] !== undefined && rangeVal[0] !== null ? rangeVal[0] :"";
                endVal = rangeVal[1] !== undefined && rangeVal[1] !== null ? rangeVal[1] :"";
            }
            
            //范围值，用"-"区分开始和结束
            var rangeValue = rowVal.rangeValue.split("-");
            
            var _dom = [];
            var width = colModule.rangWidth ? colModule.rangWidth +"px" : "auto";

            var attr = colModule.validateErrorMsg;

            var attrConf = {};
            attrConf = $.copyObject(attr, attrConf);

            attrConf["rangeErrorMsg"] = attr.rangeErrorMsg +rowVal.rangeValue;   
            attrConf["rangeEqualsErrorMsg"] = attr.rangeEqualsErrorMsg +rowVal.rangeValue;                       
            attrConf["type"] = "text";
            attrConf["range"] = rowVal.rangeValue;
            attrConf["rangeEquals"] = rowVal.rangeValue;            
            attrConf["class"] = "cell_editor_input blueinput";
            attrConf["renderType"] = "range";
            attrConf["rangeType"] = "start";
            attrConf["value"] = startVal;
            attrConf["rangeVal"] = rangeValue[0];
            attrConf["validate"] = colModule.validate;
            attrConf["name"] = colId + startVal + new Date().getTime();

            _dom.push($.createDomStr({
                            tagName : "input",
                            attr : attrConf,
                            style : {
                                width : width
                            }
                        }));
            _dom.push('<span class="t">'+(rowVal.unit || "") + "</span>");
            _dom.push('<span class="t">-</span>');
            attrConf["rangeType"] = "end";
            attrConf["value"] = endVal;        
            attrConf["name"] = colId + endVal + new Date().getTime();    
            _dom.push($.createDomStr({
                            tagName : "input",
                            attr : attrConf,
                            style : {
                                width : width
                            }
                        }));

            _dom.push('<span class="t">'+ (rowVal.unit || "") + "</span>" );            
            return _dom.join("");
        },
       
        bindEvent:function($td){
            var self = this;
            var $inputs = $td.find('input[type="text"][rangeType]');
            var colModule = self.colModule[$td.attr("colid")];
            var errorMsg = window["S_RANGE_GREATER_THAN_ERROR_MSG"]
                    || "!!S_RANGE_GREATER_THAN_ERROR_MSG!!";



            FormValidate.initDefault(self.conf.bindDomId, self.$bindDomd.attr("rangBinded") ? true : false  );
            self.$bindDomd.attr("rangBinded", true);//标识已经初始化一次，下一次初始化不清除数据
            function checkRange($input){
                var rangeType = $input.attr("rangeType");
                var $inputs = $input.parent().find('input[type="text"][rangeType]');
                var number = +$input.val();
                var newValue = "";
                if(rangeType === "start"){
                    var $endInput = $inputs.filter('[rangeType="end"]');
                    var endVal = +$endInput.val();
                    if(number > endVal){
                        if (!FormValidate.isErrorField($endInput)) {
                            FormValidate.setError($endInput, errorMsg);
                        }
                        return false;
                    }else if ($endInput.attr("nowerror") === errorMsg){
                        FormValidate.hideErrorMessage($endInput);
                    }
                    newValue = number + "," + endVal;
                }else{
                    var $startInput = $inputs.filter('[rangeType="start"]');
                    var startVal = +$startInput.val();
                    if(number < startVal){
                        if (!FormValidate.isErrorField($startInput)) {
                            FormValidate.setError($startInput, errorMsg);
                        }
                        return false;
                    }else if ($startInput.attr("nowerror") === errorMsg){
                        FormValidate.hideErrorMessage($startInput);
                    }
                    newValue = startVal + "," + number;
                }

                var $trs = self.getRows();//getTBody().children("tr");
                var index = $trs.index($td.parent());
                if(index !=-1){
                    if(self && self.data){
                         self.data[index][$td.attr("colid")] = newValue;
                     }    
                }

                return true;
                  
            }
            
            for(var i=0;i<$inputs.length;i++){
                $.onlyNumber({
                    input:$inputs[i],
                    decimal:colModule.decimal
                });
                var $input = $($inputs[i]);
                
                FormValidate.addCustomValidate(self.conf.bindDomId,
                        $inputs[i], null, checkRange, errorMsg);
            } 
 

                       
        },
        /*设置单元格值
         * @param conf.$td 单元格
         * @param conf.value 值
         * @param conf.rowVal 单元格所在行数据
         * */        
        setValue:function(conf){
            var self = this;
            var $td = conf.$td, value = conf.value;
            var $inputs = $td.find('input[type="text"][rangeType]');
            if(value !=""){
                var rangeVal = value.split(",");
                startVal = rangeVal[0] !== undefined && rangeVal[0] !== null ? rangeVal[0] :"";
                endVal = rangeVal[1] !== undefined && rangeVal[1] !== null ? rangeVal[1] :"";
                
                $inputs.filter('[rangeType="start"]').val(startVal);
                $inputs.filter('[rangeType="end"]').val(endVal);
                
                    var $trs = self.getRows();//getTBody().children("tr");
                 var index = $trs.index($td.parent());
                 if(self && self.data){
                     self.data[index][$td.attr("colid")] = startVal + ","+ endVal;
                 }
            }
        }
    
}

;/*
 * 下拉框
 * 列模式：
 * selectValues 属性 备选项
 * fetch :{valuefield:"选项值在备选项集合中的key",textfield:"显示文本在备选项集合中的key",selectfield:"备选项在行数据属性key"}
 * 默认fetch:{valuefield:"value",textfield:"text",selectfield:"select"}
 * selectHead  为下拉框选项中动态头部添加选项
 * 
 * 
 */
GridPanel.render.select = {
        /**
         * 渲染
         * @param cellVal 单元格原始值
         * @param colModule 表格列模式
         * @param rowVal 行数据
         * @param colId 列ID
         * 列模式中有
         *  paramFn    方法，用于给select render的时候往select中添加attr,方法返回属性键值对a="" b="" 字符串形式          
         *  selectHead 方法，为select添加自定义头部选项 函数
         */
        render: function(cellVal, colModule, rowVal, colId) {
        	var isRender = GridPanel.render.common.isRender({
        		colModule : colModule,
        		cellVal : cellVal,
                rowVal : rowVal
        	});
        	if(isRender !== null){
        		return isRender;
        	}
        
            var selectValues = null, selectedValue = colModule.getSelection 
                ? colModule.getSelection(rowVal) // 如果提供了回调，回调执行结果代表选中的值
                : cellVal;  // 如果cellVal是字符串，代表它为下拉框选中值

            var disable = colModule.isDisable 
                ? colModule.isDisable(rowVal) // 如果提供了回调，回调执行结果代表选中的值
                : false;

            var fetch = colModule.fetch ||  {
								                valuefield : "value", //选项值
								                textfield : "text", //选项文本
								                selectfield : "select" //必选项
								            }
            //如果列模式中指定备选项，否则备选项从行数据中
            selectValues = colModule.selectValues || rowVal[fetch["selectfield"]]  
            selectValues = $.isString(selectValues) ?  $.StringtoJson(selectValues) : selectValues;
            rowVal[colId] = selectedValue;
            if (!selectValues) {
            	 return "";
            } else {
            	var appendItemStr = colModule.selectHead ? colModule.selectHead(rowVal) : "";//自定义动态添加选项
            	var paramStr =  colModule.paramFn ? colModule.paramFn(rowVal) : "";//自定义参数，列模式用户指定
            	var selectId = colId + '_select' + new Date().getTime() + Math.floor(Math.random() * 1000);
                var items = [];
                for (var i = 0, len = selectValues.length; i < len; i++) {
                    option = selectValues[i];
                    items.push({text : option[fetch["textfield"]], value :option[fetch["valuefield"]] } )
                }
                return CompatibleSelect.getTemplate(selectId, "", items, $.isString(selectedValue) ? selectedValue : "", paramStr, appendItemStr, '100%', disable);
            }
        },
        getValue: function($td) {
        	return $td.find("div.select_body").attr("val");
        },
        /*
         * 绑定事件
         */
        bindEvent: function($td) {
        	var self = this;
            var selectId = $td.find("div.select_body").attr("id");
            if(!selectId) return;
            CompatibleSelect.init({
                id : selectId,
                initSelectAfter : self.colModule[$td.attr("colid")].initSelectAfter,
                listeners : {
                    selectAfter : function (val) {
                        var td = document.getElementById(selectId).parentNode.parentNode;
                        var tr = td.parentNode;
                        var $trs = self.getRows();
                        var index = $trs.index(tr);
                        var colId = td.attributes["colid"].value;
                        self.data[index][colId] = val;
                        if(self.colModule[colId].selectChange){  //下拉框更改选项回调事件
                            self.colModule[colId].selectChange(selectId,index,val);
                        }                        
                    }
                }
            });
        },
        /*设置单元格值
         * @param conf.$td 单元格
         * @param conf.value 值
         * @param conf.rowVal 单元格所在行数据
         * */          
        setValue : function(conf){
        	 var selectId = $td.find("div.select_body").attr("id");
        	 if(!selectId) return;
        	 CompatibleSelect.selecting(selectId, conf.value);
        }
		
}



;/**
 * 可编辑单元格
 * 列模式添加属性
 * inputWidth : 文本框宽度
 * isDisable : 根据行数据动态判断文本框是否可用
 * rowFlag : 文本框行标识表示文本框唯一标示，rowFlag用于指定行数据中的某个唯一属性值
 * validate : 验证类型，参考FormValidate
 * isBindRemote : 文本框是否有验证功能
 * remoteUrlFn  : 远程验证url
 * customValidate : 文本框自定义验证
 * */
/* global GridPanel,Slide */
'use strict';
GridPanel.render.slide = {
    /*
     * 渲染         *
     */
    render: function(cellVal, colModule, rowVal) {
        var isRender = GridPanel.render.common.isRender({
            colModule: colModule,
            cellVal: cellVal,
            rowVal: rowVal
        });
        if (isRender !== null) {
            return isRender;
        }
        return '<div id="' + rowVal.id + '_slideDiv" slideId="' + rowVal.id + '_slide" style="width:350px;" slideValue="' + cellVal + '"></div>';
    },
    /**
     * 绑定事件 对每个表格分别生成一个文本框，用于编辑单元格
     *
     * @param $td
     *            单元格jquery对象
     */
    bindEvent: function($td) {
        var $slide = $td.find('div[slideId]');
        var slideDivId = $slide.attr('id');
        var slideId = $slide.attr('slideId');
        var value = $slide.attr('slideValue');
        var colid = $td.attr('colid');
        var colModule = this.colModule[colid];
        var data = this.data;
        var self = this;
        Slide.init({
            id: slideId,
            applyTo: slideDivId,
            scaleVal: value || 0,
            markCount: colModule.slide.markCount,
            max: colModule.slide.max,
            listeners: {
                stop: function(id) {
                    var $trs = self.getRows();
                    var index = $trs.index($td.parent());
                    data[index][colid] = Slide.getValue(id);
                }
            }
        });
    },
    getValue: function($td) {
        var $slide = $td.find('div[slideId]');
        var slideId = $slide.attr('slideId');
        return Slide.getValue(slideId);
    },
    /*设置单元格值
     * @param conf.$td 单元格
     * @param conf.value 值
     * @param conf.rowVal 单元格所在行数据
     * */
    setValue: function(conf) {
        var $slide = conf.$td.find('div[slideId]');
        var slideId = $slide.attr('slideId');
        return Slide.setValue(slideId, conf.value);
    }
};;/**
 * 单元格悬浮提示 列模式 ctitle
 */
GridPanel.render.suspension = {
	render : function(cell, colModule, rowData, columnName) {
		if (colModule.ctitle) {
			var ctitle = colModule.ctitle.split("\.");
			var dataVal = rowData[ctitle[0]] ? rowData[ctitle[0]] : "";
			for ( var i = 1; i < ctitle.length; i++) {
				if (dataVal) {
					dataVal = dataVal[ctitle[i]] ? dataVal[ctitle[i]] : "";
				}
			}
		} else {
			dataVal = cell;
		}

		return '<span ctitle="' + dataVal + '">' + dataVal + '</span>';
	},
	bindEvent : function($td) {
		$td.mouseout(function(event){
			if(event.target.tagName.toLowerCase() == "span"){
				$("#" + $(this).attr("colid") + "tip").remove();	
			}
			
		}).mouseover(function(event){
			if(event.target.tagName.toLowerCase() == "span"){
				$("#" + $(this).attr("colid") + "tip").remove();
				var tip = '<div style="top:'+event.pageY+'px;left:'+event.pageX+'px;position:absolute" id="' + $(this).attr("colid") + 'tip" class="tt">' + $td.children("div").children('span[ctitle]').attr("ctitle") + '</div>';
				$(document.body).append(tip)	
			}			
		});
	
	}
}
;/*
 * 表格模版类，不对外公开
 * @inner
 * @ignore
 */
GridPanel.template = {
    /*
     * 创建表头行结构
     * @private
     * @method _createTheadDomStr
     * @param conf.cls 自定义样式
     * @param conf.content 内容元素
     * <div class="theadpart">
     * 		<table>
     * 			<thead>
     * 				<tr>
     *
     * 				</tr>
     * 			</thead>
     * 		</table>
     * </div>
     * */
    _createTheadDomStr: function(conf) {
        return $.createDomStr({
            tagName: "div",
            attr: {
                "class": "theadpart"
            },
            content: $.createDomStr({
                tagName: "table",
                attr: {
                    "class": GridPanelClass.headTableClassName + " " + conf.cls
                },
                content: $.createDomStr({
                    tagName: "thead",
                    content: $.createDomStr({
                        tagName: "tr",
                        content: conf.content
                    })
                })
            })
        });
    },

    /* 创建TH结构
     * @private
     * @method  _creatTHDomStr
     * @param conf.colId  列ID
     * @param conf.headRender 表头渲染器
     * @param conf.rowspan 跨行数
     * @param conf.colspan 跨列数
     * @param conf.width 列宽
     * @param conf.unit 单位
     * @param conf.align 对齐方式
     * @param conf.content 内容元素
     * <th>
     * 	  <div class="grid-th">
     * 		<a class="rank-pull"></a>
     * 		<div class="grid-title">
     * 			<span>文字</span>
     * 			<div class="rank">
     * 				<a class="rank rank-up"></a>
     * 				<a class="rank rank-down"></a>
     * 			</div>
     * 		</div>
     * 	 </div>
     * </th>
     *
     * */
    _creatTHDomStr: function(conf) {
        var thAttr = {
            colid: conf.colId || "null",
            headRender: conf.headRender,
            rowspan: conf.rowspan,
            colspan: conf.colspan,
            onlySort: conf.onlySort,
            sortFirst: conf.sortFirst
        };

        if (conf.rowspan) { //跨行
            thAttr["class"] = "rowspan";
        }
        return $.createDomStr({
            tagName: "th",
            attr: thAttr,
            style: {
                width: conf.width + conf.unit, //列宽单位
                "text-align": conf.align //列对齐方式
            },
            content: conf.content
        });
    },
    /*
     * 表头单元格包围层
     * @private
     * @method _createTHWrapper
     * @param conf.sort  列排序设置
     * @param conf.onlySort  单项排序设置
     * @param conf.noSort 全局设置不排序
     * @param conf.headRender 表头渲染器
     * @param conf.noTools 全局设置不使用工具
     * @param conf.content 内容元素
     * <div class="grid-th">
     * 		<a class="rank-pull"></a>
     * 		<div class="grid-title">
     * 			<span>文字</span>
     * 			<div class="rank">
     * 				<a class="rank rank-up"></a>
     * 				<a class="rank rank-down"></a>
     * 			</div>
     * 		</div>
     * 	</div>
     * */
    _createTHWrapper: function(conf) {
        var className = "grid-th";
        var paddingRight = null;
        if ((!conf.sort || conf.noSort === true) && (conf.headRender == "checkbox" || conf.noTools === true)) {
            className += " grid-th-bothnone";
            paddingRight = "0px";
        } else if (!conf.sort || conf.noSort === true) {
            className += " grid-th-norank";
        } else if (conf.headRender == "checkbox" && this.conf.noTools === true) {
            className += " grid-th-norankpull";
        }
        if (conf.headRender == "checkbox") {
            className = "rankcenter";
        }
        return $.createDomStr({
            tagName: "div",
            attr: {
                "class": className
            },
            style: {
                "padding-right": paddingRight
            },
            content: conf.content
        });

    },
    /*
     * 创建表头工具按钮
     * @param conf.headRender 表头渲染
     * @param conf.noTools 全局设置是没有工具按钮
     * @param conf.bindDomId 表格渲染ID
     * @param conf.colId  列ID
     * @param conf.filterType 列模式定义过滤类型
     * @param conf.filterWidth 过滤层宽
     * @param conf.filterHeight 过滤层高
     * @param conf.filterDomId 过滤层内容在页面中的ID
     * <a class="rank-pull"></a>
     * */
    _createToolButton: function(conf) {
        if (conf.headRender != "checkbox" && conf.noTools !== true) {
            return $.createDomStr({
                tagName: "a",
                attr: {
                    "class": GridPanelClass.filterClassName,
                    href: "javascript:void(0)",
                    gridId: conf.bindDomId,
                    colId: conf.colId,
                    filterType: conf.filterType,
                    filterWidth: conf.filterWidth,
                    filterHeight: conf.filterHeight,
                    filterDomId: conf.filterDomId
                }
            });
        } else {
            return "";
        }
    },
    /*
     * 创建表头文字
     * @param conf.headRender {String/Function}  表头渲染
     * @param conf.text {String}  表头文字
     * @param conf.align {String} 表头对齐方式
     * @param conf.colId {String} 列ID
     * @param conf.title {String} 表头title
     * @param conf.colM 列模式
     * @param conf.content 内容元素
     *		<a class="rank rank-up"></a>
     *		<a class="rank rank-down"></a>
     *	<span>文字</span>
     * */
    _createGridTitle: function(conf) {

        var headRender = GridPanel.render[conf.headRender];
        if (headRender) {
            return headRender.render.call(this, conf.text, conf.colM, "", conf.colId);
        } else {
            return (conf.content || "") +
                $.createDomStr({
                    tagName: "span",
                    attr: {
                        title: conf.title
                    },
                    style: {
                        "text-align": conf.align
                    },
                    content: conf.text
                });



        }
    },
    /*
     * 创建排序按钮
     * @param conf.sort 列模式是否指定排序标识
     * @param conf.onlySort 列模式指定该列单项排序标识
     * @param conf.noSort {boolean} 全局设置不使用排序
     * @param conf.defaultSoft 指定列的默认排序规则  值为asc 或 desc
     *
     * 		<a class="rank rank-up"></a>
     * 		<a class="rank rank-down"></a>
     *
     * */
    _createSortButton: function(conf) {
        if (conf.sort && conf.noSort !== true) { //该列设置有排序，并且整个表格没有设置为不能排序
            return $.createDomStr({
                    tagName: "a",
                    attr: {
                        "class": "rank " + (conf.onlySort ? "rank-up-grace" : "rank-up"),
                        sortMethod: "asc",
                        sortValue: conf.sort,
                        sortClassType: conf.sortClassType,
                        sortTag: conf.defaultSort === "desc" ? "desc" : conf.defaultSort === "asc" ? "asc" : null
                    },
                    style: {
                        display: conf.defaultSort == "asc" ? "inline-block" : "none",
                        cursor: conf.onlySort ? "default" : "pointer"
                    }
                }) +
                $.createDomStr({
                    tagName: "a",
                    attr: {
                        "class": "rank " + (conf.onlySort ? "rank-down-grace" : "rank-down"),
                        sortMethod: "desc",
                        sortValue: conf.sort,
                        sortClassType: conf.sortClassType,
                        sortTag: conf.defaultSort === "desc" ? "desc" : conf.defaultSort === "asc" ? "asc" : null
                    },
                    style: {
                        display: conf.defaultSort == "desc" ? "inline-block" : "none",
                        cursor: conf.onlySort ? "default" : "pointer"
                    }
                })



        } else {
            return "";
        }
    },

    /*
     * @ignore
     * 表头模版
     * <div class="theadpart">
     * 		<table>
     * 			<thead>
     * 				<tr>
     * 					<th>
     * 						<div class="grid-th">
     * 							<a class="rank-pull"></a>
     * 							<div class="grid-title">
     * 								<span>文字</span>
     * 								<div class="rank">
     * 									<a class="rank rank-up"></a>
     * 									<a class="rank rank-down"></a>
     * 								</div>
     * 							</div>
     * 						</div>
     * 					</th>
     * 				</tr>
     * 			</thead>
     * 		</table>
     * </div>
     * */
    thead: function(conf) {
        var template = GridPanel.template;
        var colModule = this.colModule;

        var isSortPlugin = false; //根据列模式是否指定排序确定是否具有排序功能
        var isFilterPlugin = false;

        this.plugins = this.plugins || [];
        this.plugins.push("columnTools");
        var bifurcateRow = [];
        var ths_doms = [];
        for (var colId in colModule) {
            var col = colModule[colId];
            col.origWidth = col.width; //保存列宽原始值，用于隐藏显示列的恢复
            col.unit = col.unit || this.unit; //如果列指定单位，否则使用全局单位
            col.isNessaryShow = col.isNessaryShow === "true" ? "true" : col.headRender != "checkbox" ? "false" : "true"; //列必须显示，如果列不管用户是否指定必须显示
            var align = col.headAlign || col.align || "left"; //表头对齐

            var sortBtnStr = null;
            //生存排序按钮
            if (col.sort && conf.noSort !== true) {
                if (isSortPlugin === false) {
                    this.plugins.push("sort");
                    isSortPlugin = true;
                }
                sortBtnStr = template._createSortButton.call(this, {
                    sort: col.sort,
                    sortClassType: col.sortClassType, //列排序数据类型
                    defaultSort: col.defaultSort,
                    noSort: conf.noSort,
                    onlySort: col.onlySort
                });
                if (col.defaultSort) {
                    this.sortId = col.sort;
                    this.sortType = col.defaultSort;
                }
            }
            //生成表头文字和排序按钮
            var gridTitle = template._createGridTitle.call(this, {
                headRender: col.headRender,
                colId: colId,
                colM: col,
                text: col.text,
                align: align,
                title: col.title || col.text,
                content: sortBtnStr
            });

            //生成功能按钮
            var toolBtnStr = template._createToolButton.call(this, {
                headRender: col.headRender,
                noTools: this.conf.noTools,
                bindDomId: conf.bindDomId,
                colId: colId,
                filterType: col.filter,
                filterWidth: col.filterWidth,
                filterHeight: col.filterHeight,
                filterDomId: col.filterDomId
            });
            //生成th包含容器
            var thWrapperStr = template._createTHWrapper.call(this, {
                sort: col.sort,
                noSort: conf.noSort,
                headRender: col.headRender,
                noTools: col.noTools,
                content: toolBtnStr + gridTitle
            });
            //创建th结构
            var thStr = template._creatTHDomStr.call(this, {
                colId: colId,
                noSort: conf.noSort,
                headRender: col.headRender,
                rowspan: col.rowspan,
                colspan: col.colspan,
                align: col.align,
                unit: col.unit,
                width: col.width,
                onlySort: col.onlySort,
                sortFirst: col.sortFirst || conf.sortFirst,
                content: toolBtnStr + gridTitle
            });

            //列为跨行列头
            if (col.bifurcate) {
                this.bifurcate = true;
                bifurcateRow.push(thStr);
            } else {
                ths_doms[parseInt(col.index)] = thStr;
            }
        }


        //如果有跨行的表头，生成第二行
        var bifurcateRowTHStr = "";
        if (bifurcateRow.length != 0) {
            bifurcateRowTHStr = $.createDomStr({
                tagName: "tr",
                content: bifurcateRow.join('')
            });
        }

        return template._createTheadDomStr.call(this, {
            cls: conf.cls,
            content: ths_doms.join("") + bifurcateRowTHStr
        });
    },
    /*
     * 表体
     *
     * */
    tbody: function(datas) {
        var data = null;
        var unit = this.conf ? this.conf.unit || "px" : "px";

        var tbodyContent = [];
        for (var i = 0, len = datas.length; i < len; i++) {
            data = datas[i];
            tbodyContent.push(GridPanel.template.createRow.call(this, data, i));
        }

        return $.createDomStr({
            tagName: "div",
            attr: {
                id: this.conf.bindDomId + '_gridbody',
                "class": GridPanelClass.bodyGridClassName + " admingrid " + (this.conf.wordWrap ? "full-txt" : "")
            },
            style: {
                overflow: "auto",
                height: this.conf.height ? this.conf.height + unit : "auto"
            },
            content: $.createDomStr({
                tagName: "table",
                content: $.createDomStr({
                    tagName: "tbody",
                    content: tbodyContent.join("")
                })
            })
        });
    },
    //无数据
    empty: function(empytText, ismini, height, className) {
        var nodata;
        if (className) nodata = className;
        else if (ismini) nodata = "nodata-min";
        else nodata = "nodata";

        var m = "";
        
        var height = this.conf.height ? this.conf.height + "px" : "auto";

        return $.createDomStr({
            tagName: "div",
            attr: {
                id: this.conf.bindDomId + "_gridbody",
                "class": GridPanelClass.bodyGridClassName + " admingrid"
            },
            style: {
                height: height || "auto",
                "overflow-x": "hidden"
            },
            content: $.createDomStr({
                tagName: "table",
                "class": "grid",
                style: {
                    height: "100%"
                },
                content: $.createDomStr({
                    tagName: "tbody",
                    content: $.createDomStr({
                        tagName: "tr",
                        attr: {
                            type: "empty"
                        },
                        content: $.createDomStr({
                            tagName: "td",
                            attr: {
                                height: "100%"
                            },
                            style: {
                                "text-align": "center"
                            },
                            content: $.createDomStr({
                                tagName: "div",
                                attr: {
                                    "class": nodata
                                },
                                content: empytText
                            })
                        })
                    })
                })
            })
        });
    },
    /*设置为空显示文本*/
    setEmptyText: function(text, ismini) {
        var nodata = "nodata";
        if (ismini) {
            nodata = nodata + "-min";
        }
        $("#" + this.conf.bindDomId).find("div." + nodata).html(text);
    },
    /*
     * 分解列数据，获得列值
     * 如果列ID是以.分割的，则查找到最后一层的值
     *
     * */
    _getCellValue: function(conf) {
        var data = conf.data;
        var colId = conf.colId;
        var colM = conf.colM;
        var names = colId.split("\."); //可能列Id为对象套对象属性.属性，需要找到最底层的属性值
        var dataValue;

        if (data[names[0]] !== undefined && data[names[0]] !== null) {
            dataValue = data[names[0]];
        } else {
            dataValue = "";
        }
        if ($.isObject(dataValue)) {
            for (var i = 1; i < names.length; i++) {
                if (dataValue) {
                    dataValue = dataValue[names[i]] ? dataValue[names[i]] : "";
                }
            }
        }
        //如果指定字段值为定义的标识替换成自定义的内容
        if (colM.valueEmptyIndent) {
            var indents = colM.valueEmptyIndent.split("**");
            for (var i = 0; i < indents.length; i++) {
                if (indents[i] === dataValue) {
                    dataValue = colM.valueEmptyDefault;
                    break;
                }
            }
        }
        return dataValue;
    },
    /*
     * 创建单元格容器结构
     * @param conf.align 对齐方式
     * @param conf.displayStyle 显示模式，如果单元格内容为下拉框，则为块级元素
     * */
    _createCellStr: function(conf) {
        return $.createDomStr({
            tagName: "div",
            attr: {
                "class": "cell_inner "
            },
            style: {
                "text-align": conf.align || "left",
                "display": conf.displayStyle
            },
            content: conf.content
        });
    },
    /*创建单元格里面td
     *width : 列宽,
     *align :对齐方式,
     *renderAttr : 渲染属性,
     *colId : lieId,
     *title : 悬浮提示,
     *unit  : 单位,
     *origWidth : 原始列宽,
     *content : 单元格内容,
     *style : 单元格td上附加的style样式 json形式
     *class : 单元格td上附加的class样式名
     */
    _createTdStr: function(conf) {
        var style = conf.style || {};
        style.width = conf.width + conf.unit;
        style["text-align"] = conf.align || "left";

        return $.createDomStr({
            tagName: "td",
            style: style,
            attr: {
                render: conf.renderAttr,
                colid: conf.colId,
                //title : conf.title,
                unit: conf.unit,
                origWidth: conf.width,
                "class": conf["class"]
            },
            content: conf.content
        });
    },
    placeholder: function(conf) {
        var template = GridPanel.template;
        var colModule = this.colModule;

        var bifurcateRow = [];
        var tds_doms = [];
        for (var colId in colModule) {
            var col = colModule[colId];
            col.origWidth = col.width; //保存列宽原始值，用于隐藏显示列的恢复
            col.unit = col.unit || this.unit; //如果列指定单位，否则使用全局单位                  
            var tdStr = $.createDomStr({
                tagName: "td",
                attr: {
                    colId: colId,
                    rowspan: col.rowspan,
                    colspan: col.colspan,
                    unit: col.unit,
                    width: col.width
                },
                style: {
                    width: col.width + col.unit,
                    height: "0",
                    "line-height": "0",
                    "border-bottom": "none"
                }
            });

            //列为跨行列头
            if (col.bifurcate) {
                bifurcateRow.push(tdStr);
            } else {
                tds_doms[parseInt(col.index)] = tdStr;
            }
        }

        //如果有跨行的表头，生成第二行
        var bifurcateRowTHStr = "";
        if (bifurcateRow.length != 0) {
            bifurcateRowTHStr = $.createDomStr({
                tagName: "tr",
                attr: {
                    placeholder: "true"
                },
                content: bifurcateRow.join(''),
                style: {
                    height: "0",
                    "line-height": "0",
                    "border-bottom": "none"
                }
            });
        }

        var trStr = $.createDomStr({
            tagName: "tr",
            attr: {
                placeholder: "true"
            },
            style: {
                height: "0",
                "line-height": "0",
                "border-bottom": "none"
            },
            content: tds_doms.join("")
        })


        return trStr + bifurcateRowTHStr;
    },
    /**
     * 创建一行结构
     *
     * @param data行数据
     * @$tr 已存在行Jquery对象，如果有参数，则往该行中添加单元格，否则，内部生成行
     */
    createRow: function(data, $tr) {
        data = data ? data : {}; // 行值
        var template = GridPanel.template;
        var tds = [];
        for (var colId in this.colModule) {
            var colM = this.colModule[colId];
            if (colM.colspan) {
                continue;
            }

            var dataVal = template._getCellValue({
                data: data,
                colId: colId,
                colM: colM
            });

            var renderAttr = "";
            var title = ""; //内容悬浮提示
            var displayStyle = null; //如果单元格里面放下拉框，则显示模式是块级元素
            if (colM.render) {
                if ($.isString(colM.render) && GridPanel.render[colM.render]) { //预置
                    dataVal = GridPanel.render[colM.render] ? GridPanel.render[colM.render].render.call(this, dataVal, colM, data, colId) : dataVal;
                    renderAttr = colM.render;
                    if (colM.render == "date") {
                        title = dataVal;
                    }
                } else if ($.isFunction(colM.render)) {
                    dataVal = colM.render(data, $tr);
                    if (!$.isString(dataVal)) {
                        dataVal = $("<div></div>").append(dataVal).html();
                    }
                    renderAttr = "";
                }
                dataVal = dataVal + "";
                if (dataVal.indexOf("select") != -1) {
                    displayStyle = "display:block;";
                }
            } else {
                title = dataVal;
                dataVal = '<span title="' + title + '">' + dataVal + '</span>';
            }

            var align = colM.align || "left";
            var unit = colM.unit || this.unit;
            var cellInnerStr = template._createCellStr({
                align: align,
                displayStyle: displayStyle,
                content: dataVal
            })


            //listeners.applyStyle(colId,data) 渲染单元格时，根据行数据动态附加给td样式
            var style = null;
            if (this.listeners && this.listeners.applyStyle) {
                style = this.listeners.applyStyle(colId, data);
            }

            tds[parseInt(colM.index)] = template._createTdStr({
                width: colM.width,
                align: align,
                renderAttr: renderAttr,
                colId: colId,
                title: title,
                unit: unit,
                origWidth: colM.width,
                content: cellInnerStr,
                style: style,
                "class": colM["class"]
            });
        }

        var tdsStr = tds.join("");

        //更新制定行
        if ($tr != null && !$.isNumber($tr)) {
            $tr.append(tdsStr);
        }

        return $.createDomStr({
            tagName: "tr",
            //        	attr : {
            //        		"onmousemove" : 'GridPanel.trOver(this);',
            //        		"onmouseout" : 'GridPanel.trOut(this);'
            //        	},
            content: tdsStr
        });

    },
    /*
     * 设置单元格值
     *
     * @param $td
     *            单元格jquery对象 val 值
     */
    setTDVal: function($td, val) {
        var $trs = this.getRows();
        var index = $trs.index($td.parent($td));
        this.data[index][$td.attr("colid")] = val;
        //return $td.children("div:eq(0)").text(val);
    },
    /*
     * 获得单元格值
     *
     * @param {Object}
     *            $td 单元格jquery对象
     */
    getTDVal: function($td) {
        var render = GridPanel.render[$td.attr("render")];
        if (render && render.getValue) {
            return render.getValue($td);
        } else {
            return $td.children("div.cell_inner ").text();
        }
    }
};/**
 * 下拉框
 * ======
 * 
 * 要求有以下结构或调用getTemplate生成结构：
 * 
 *     <div class="select_body" id="list1" value="1">
 *         <input class="select_text" name="" value="请选择"/>
 *         <div class="select_trigger_wrap">
 *             <a class="select_trigger"></a>
 *         </div>
 *         <div class="select_boundtree" id="list1_selectBody">
 *             <ul>
 *                 <li val="1"><a href="javascript:void(0)" title="选项1">选项1</a></li>
 *                 <li val="2"><a href="javascript:void(0)" title="选项2">选项2</a></li>
 *                 <li val="3"><a href="javascript:void(0)" title="选项3">选项3</a></li>
 *                 <li val="4"><a href="javascript:void(0)" title="选项4">选项4</a></li>
 *                 <li val="5"><a href="javascript:void(0)" title="选项5">选项5</a></li>
 *                 <li val="6"><a href="javascript:void(0)" title="选项6">选项6</a></li>
 *             </ul>
 *         </div>
 *     </div>
 *     
 * 注意：
 * 
 * 1. select_body的value属性是默认值
 * 2. select_text的value属性是默认显示文字
 * 3. select_boundtree的type属性是下拉框类型，如果是树下拉值为tree,不设置该属性，默认为普通下来框
 *     
 * @class CustomSelect
 * */
window.CustomSelect = window.CompatibleSelect = window.__RIIL_SELECT__ = {
    _SELECTBODY : "_selectBody",
    /**
     * 初始化下拉框，绑定行为
     * 
     * @method init
     * @param conf {Json} 初始化配置参数
     * @param conf.id {String} 绑定的DOM id，select展开内容容器id规则为该id_selectBody
     * @param conf.type {String} "button",按钮样式的下拉框
     * @param conf.listeners {Object} 绑定事件
     * @param conf.listeners.selectAfter {Function} (text_value,[node])  //text_value当前选中项文字和值，[node]如果是树，则为选中树节点
     * @param conf.initSelectAfter {Boolean} 初始化是否出发selectAfter，默认不触发
     * */
    init:function(conf){
        var id = conf.id;
        conf.listeners = conf.listeners ? conf.listeners : {};
        __RIIL_SELECT__._bindEvent(id,conf);
        __RIIL_SELECT__._initShowContent(conf);
    },
    /**
     * 刷新下拉选项内容
     * 
     * @method refresh
     * @param conf {Json} 配置参数
     * @param conf.id {String} 下拉框id
     * @param conf.listeners {Object} 绑定事件
     * @param conf.listeners.selectAfter {Function} (text_value,[node])  //text_value当前选中项文字和值，[node]如果是树，则为选中树节点
     * @param contentHTML {String} 刷新的内容HTML `<ul><li></li>...</ul>`
     * @param value 默认选中值，如果没有则默认选中第一个
     * */
    refresh:function(conf,contentHTML,value){
        var $content = __RIIL_SELECT__._getShowContent(conf.id);
        var type = $content.attr("type");
        if(!type || type=="flexselect"){
            type = "select";
        }
        $("#" + conf.id).attr("value", value || "");
        var plugin = __RIIL_SELECT__._plugins[type];
        if(plugin){
            plugin.refresh(conf,value,contentHTML);
        }
    },
    /**
     * 添加下拉选项内容
     * 
     * @method append
     * @param conf {Json} 配置参数
     * @param conf.id {String} 下拉框id
     * @param conf.listeners {Object} 绑定事件
     * @param conf.listeners.selectAfter {Function} (text_value,[node])  //text_value当前选中项文字和值，[node]如果是树，则为选中树节点
     * @param contentHTML {String} 添加的内容HTML `<li></li>...`
     * @param value 默认选中值，如果没有则默认选中第一个
     * */   
    append:function(conf,contentHtml,value){
        var $content = __RIIL_SELECT__._getShowContent(conf.id);
        var type = $content.attr("type");
        if(!type || type=="flexselect"){
            type = "select";
        }
        var plugin = __RIIL_SELECT__._plugins[type];
        if(plugin){
            plugin.append(conf,value,contentHtml);
        }               
    },
    /**
     * 设置下拉框是否可用
     * 
     * @method setAvailable
     * @param id {String} 组件ID
     * @param flag {Boolean} true可用，false不可用
     * */
    setAvailable:function(id,flag){
        $("#"+id).attr("available",flag);
        if (!flag) $("#"+id).addClass('select_body_disabled');
        else $("#"+id).removeClass('select_body_disabled');
    },
    /*
     * 内部方法,获得下拉框内部的文本框
     * */
    _get$Input:function($select){
        var $input = $select.children("div.select_text");
        if(!$input[0]){
            $input = $select.find("span.text"); //按钮式的下拉框
        }
        if(!$input[0]){
            $input = $select.children("div.flexselect_text"); //按钮式的下拉框         
        }
        if(!$input[0]){
            $input = $select.children("input.select_text"); //按钮式的下拉框           
        }
        return $input;
    },
    /*
     * 内部方法，获得隐藏域
     */
    _getHidden:function($select){
        return $select.children('input[type="hidden"]');
    },
    _getBtn:function($select){
        return $select.children(":eq(1)").children();
    },
    _getShowContent :function(id){
        return $.getJQueryDom(id + __RIIL_SELECT__._SELECTBODY);        
    },
    /*
     * 内部方法，绑定事件
     * */
    _bindEvent:function(id,conf){
        var $select = $("#"+id);
        var value = $select.attr("value");
        var $textInput = __RIIL_SELECT__._get$Input($select);
        
        if($textInput[0] && $textInput[0].tagName == "INPUT"){      
            $textInput.bind("keyup",function(event){
                if(event.keyCode==38){
                    __RIIL_SELECT__.nextSelect(id,"up");
                }else if(event.keyCode == 40){
                    __RIIL_SELECT__.nextSelect(id,"down");
                }else if(event.keyCode == 13){
                    __RIIL_SELECT__._getShowContent(id).find("li.over").click();
                    __RIIL_SELECT__.query(id,conf);
                }else{
                    __RIIL_SELECT__.query(id,conf);
                }
            });
            InputEmpty.init($textInput);
            $select.css("position","relative");
            $select.children("a.delete").click(function(){
                $textInput.val("");
                __RIIL_SELECT__.query(id,conf);
            });
        }
        
        $select.append('<input type="hidden" name="'+$textInput.attr("name")+'" value="'+value+'"/>');
                
        $textInput.removeAttr("name");
        
        function clickEvent(event){
            //event.stopPropagation();
            if($(this).attr("available")=="false"){
                return;
            }
            if(__RIIL_SELECT__._getShowContent(id).css("display") != "none"){
                __RIIL_SELECT__.collapse(id,conf);
            }else{
                __RIIL_SELECT__.expand(id,conf);
            }
            var $select = $(this);
            $select.unbind("click",clickEvent);
            setTimeout(function(){
                $select.bind("click",clickEvent);
            },10);
        }
        
        $select.unbind('click').click(clickEvent);
        
    },
    /*
     * 内部方法，初始化下拉框显示区域，type类型，确定是下拉树，还是一般的下拉框
     * */
    _initShowContent : function(conf){
        var $select = $("#"+conf.id);
        var currentValue = $select.attr("value");       
        var $content = __RIIL_SELECT__._getShowContent(conf.id);
        var type = $content.attr("type");
        if(type !="icoselect"){
            $content.width($select.width());
        }   
        //$(document.body).append($content);
        if(!type || type=="flexselect" || type == "icoselect"){
            type = "select";
        }
        if(type=="tree"){
            $content.height(300);
        }
        $content.css("overflowX","auto");
        var plugin = __RIIL_SELECT__._plugins[type];
        if(plugin){
            plugin.init($content,conf,currentValue);
        }
    },
    /**
     * 获得当前显示文本
     * @param id {String}
     * @return {String}
     */
    getText:function(id){
        var $input = __RIIL_SELECT__._get$Input($("#"+id));
        if($input[0] && $input[0].tagName == "INPUT"){
            return $input.val();
        }else{
            return $input.html();
        }   
    },
    /*内部使用方法*/
    _setText:function(id, text, title){
        var $input = __RIIL_SELECT__._get$Input($("#" + id));
        if($input[0] && $input[0].tagName == "INPUT"){
            $input.val(title || text);
        }else{
            $input.html((title || text) === '<' ? '&lt;' : (title || text));
        }   
        $input.attr("title",title== undefined ? text : title);
    },
    /**
     * 折叠
     * 
     * @method collapse
     * @private
     * @param id {String} select的id
     * @param conf {JSON} 配置参数，主要用于调用收缩开事件
     * @callback conf.listeners.collapse
     * */
    collapse:function(id,conf){    
        var $content = __RIIL_SELECT__._getShowContent(id);
        if($content.attr("showingChild")){
            return false;
        }        
        if(conf && conf.hideChild !== true && $content[0]){//隐藏是由于鼠标悬浮离开，多用于子菜单                    
            $("*").unbind("scroll",__RIIL_SELECT__._blurHide);
            $(document.body).unbind("keypress",__RIIL_SELECT__.locationItem);
        }

        var $selectBtn = $.getJQueryDom(id);
        if(!$selectBtn[0]){
            $selectBtn = $('li[child="' + id + '"]').parent();
        }

        if($content.parent()[0] && $content.parent()[0].tagName.toLowerCase() != "body"){
            $content = $(document.body).children('div[id="' + $content.attr("id") + '"]');   
            $content.find("*").unbind();
            $content.remove();            
        }else{
            if($selectBtn[0]){
                $selectBtn.after($content.hide());//将展开层缩回原位    
            }else{
                $content.find("*").unbind();
                $content.remove();
            }
        }
       
        
        if(conf && conf.listeners && conf.listeners.collapse){
            conf.listeners.collapse();  //下拉折叠事件
        }
        if($content.attr("type") == "flexselect"){
            __RIIL_SELECT__._getBtn($selectBtn).removeClass("flexselect_trigger_click");
        }
        if($content.attr("type") == "icoselect"){
            $selectBtn.removeClass("click");
        }
        $content.children("ul").children("li").removeClass("hover").removeClass("over").hide().show();
        ZIndexMgr.free($content);       
        var childBody = $content.find('div[id$="_selectBody"]').hide().removeAttr("hasOver");        
        for(var i = 0; i < childBody.length; i++){
            $(childBody[i]).find("li").removeClass("on").removeClass("over").removeClass("hover");
        }
        
		RiilMask.unmask(id + '_undermask');
    },
    /**
     * 展开
     * 
     * @private
     * @param id {String} select的id
     * @param conf {JSON} 配置参数，主要用于调用触发展开事件
     * @callback  conf.listeners.expand
     * */
    expand:function(id,conf){
        if(conf && conf.listeners && conf.listeners.expand){
            conf.listeners.expand();
        }
        var $selectBtn = $.getJQueryDom(id);
        var $content = __RIIL_SELECT__._getShowContent(id);

        var layout = $.getElementAbsolutePosition(id);
        var selectBtnHeight = $selectBtn.height();
        __RIIL_SELECT__.setSelectBodyPosition(id, {x : layout.x, y : layout.y + selectBtnHeight + 2}, {offset : {height:selectBtnHeight} });
        __RIIL_SELECT__._initContentWidth({$content : $content, $selectBtn : $selectBtn});
        //__RIIL_SELECT__._init3rdScroll($content);
        __RIIL_SELECT__._bindBlurHide($content, id, conf);
        $content.scrollTop(0);
        
        // 鉴于flash3d会挡住弹出层，在弹出层下垫一个透明iframe
        var zindex = $content.css('zIndex');
        if (zindex) zindex = parseInt(zindex, 10) - 1;
        else zindex = 1;
        RiilMask.underMask(id + '_undermask', $content, zindex);
        //__RIIL_SELECT__._bindAutoHide(id, conf);
    },
    /**初始化第三方滚动条smart使用了第三方滚动条*/
    _init3rdScroll : function($content){        
        if($content.jScrollPane && !$content.attr("isScroll")){
            $content.data("jsp","");
            $content.attr("isScroll","true").jScrollPane();
        }
    },
    /**
     * 初始化下拉显示项的宽度
     * @param conf.$content 选项显示区域
     * @param conf.$selectBtn 下拉按钮区域
     */
    _initContentWidth : function(conf){
        var $content = conf.$content;
        var $selectBtn = conf.$selectBtn;
        if(!$content.attr("type")){
            var contentWidth = $content.find("a:first").width();
            var width = $selectBtn.width() - $.getLandscape($content.find("a:first")[0],"padding");
            $content.width(Math.max(width, contentWidth));
          //  $content.find("a").width(width);
        }else if($content.attr("type") == "flexselect"){
            $content.css({
                width : $selectBtn.width(),
                top : parseInt($content.css("top")) - 5
            });            
            __RIIL_SELECT__._getBtn($.getJQueryDom(conf.id)).addClass("flexselect_trigger_click");
        }else if($content.attr("type") == "icoselect"){ //按钮式下拉
            $content.css({
                left : $selectBtn.offset().left - $content.width() + $selectBtn.width(),
                top : $content.position().top - 5
            });
            $selectBtn.addClass("click");
        }else if($content.attr("type") =="tree"){
            $content.width($content.attr("width") || 250);
        }
    },
    /**
     * 显示下拉选项框，同时定位
     * */
    setSelectBodyPosition : function(id, layout, conf){
        conf = conf || {};
        var $selectBody = __RIIL_SELECT__._getShowContent(id);
        $selectBody.css({"left" : layout.x, "top" : layout.y});
        $(document.body).append($selectBody);
        $selectBody.show().attr("show", "true");        
        $selectBody.css({
                            "height" : $selectBody.attr("height") || "auto",//使用自定义height设置的高度，否则，高度自适应。
                            "z-index" : ZIndexMgr.get()
                        });
        var newlayout = $.checkDomPosition($selectBody, layout.x, layout.y);
        var offset = conf.offset || {height : 0, width : 0};
        if(newlayout.y != layout.y){ //如果展示层坐标超出窗口，则重新计算新的坐标在向上展示
            var newy = layout.y - $selectBody.height()- 14 - offset.height || 0;
            if(newy < 0){//如果向上展示超出窗口，则还是向下展示，计算距离窗口底部的高度
                var topHeight = layout.y - 10;//在下拉框上方展示时，高度
                var winHeight = $.dimensions().height;
                var bottomHeight = winHeight - layout.y - 50;//50为了防止过于靠近窗口底部
                var maxHeight = bottomHeight;
                if(topHeight > bottomHeight){
                    newY = 10;
                    $selectBody.css({top : newY});
                    maxHeight = topHeight;
                }
                
                //maxHeight = maxHeight > 322 ? 322 : maxHeight;

                $selectBody.height(maxHeight);
            }else{
                $selectBody.css({"top" : newy});
            }
        }
        return $selectBody;
    },
    _bindBlurHide : function($content, id, conf){
        $.bindAutoHide({
            dom : $content,
            param : {selectId : id, conf : conf, callback :__RIIL_SELECT__._blurHide}
        });
        if(conf.isLoactionItem === true){ //选项定位功能
            var $body = $(document.body);
            $body.unbind("keypress", __RIIL_SELECT__.locationItem);
            $body.bind("keypress", {selectId : id, conf : conf, callback :__RIIL_SELECT__._blurHide}, __RIIL_SELECT__.locationItem);
        }

        $("*").unbind("scroll", __RIIL_SELECT__._blurHide).bind("scroll", {selectId : id, conf : conf}, __RIIL_SELECT__._blurHide);                
        $("#" + id + __RIIL_SELECT__._SELECTBODY).unbind("scroll", __RIIL_SELECT__._blurHide);
        $("#" + id + __RIIL_SELECT__._SELECTBODY).find("*").unbind("scroll", __RIIL_SELECT__._blurHide);
    },
    _blurHide : function(param){
        var id = param.selectId || param.data.selectId;
        var conf = param.conf || param.data.conf;
        return __RIIL_SELECT__.collapse(id, conf);  
    },
    /*根据输入的守字母查询定位*/
    locationItem:function(event){
        var letter = Shortcut.code2Text[event.keyCode || event.charCode];
        if(!letter) return;
        letter = letter.toLowerCase();
        var id = event.data.selectId;
        var $content = __RIIL_SELECT__._getShowContent(id);
        var $lis = $content.children().children('li');
        for(var i = 0, len = $lis.length; i < len; i++){
            var $li = $($lis[i]);
            var text = $li.children().text();
            if(text.toLowerCase().indexOf(letter) == 0){
                var index = $lis.index($li[0]);
                var height = $.getAllSize($li).height;              
                var scrollTop = height * index;
                
                $content.scrollTop(scrollTop);
                if($.isIE()){
                    $content[0].scrollTop = scrollTop;
                }
                $lis.removeClass("over");
                $li.addClass("over");
                break;
            }
        }
    },
    /**
     * 查询匹配项
     * 
     * @method query
     * @param id {String} select的id
     * @param conf {JSON} 配置参数，主要用于调用触发展开事件
     * @param conf.filterAttr* {String} 用于过滤的自定义属性
     * */   
    query:function(id,conf){
        var $content = __RIIL_SELECT__._getShowContent(id);
        var $textInput = __RIIL_SELECT__._get$Input($("#"+id));
        
        
        var val = $textInput.val();
        var lis = $content.find("li");
        lis.show();
        if($textInput.val() == $textInput.attr(InputEmpty.EmptyVal) || val == ""){
            return;
        }
        
        var Lower = val.toLowerCase();
        
        for(var i=0,len=lis.length;i<len;i++){
            var li = lis[i];
            if(conf.filterAttr){//匹配自定义属性
                var flag = true;
                for(var j=0,jlen = conf.filterAttr.length;j<jlen;j++){
                    if(li.attributes[conf.filterAttr[j]]){
                        var v = li.attributes[conf.filterAttr[j]].value.toLowerCase();
                        if( v.indexOf(Lower)==-1){
                            flag = false;
                        }else{
                            flag = true;
                            break;
                        }
                    }
                }
                if(!flag){
                    li.style.display = "none";
                }
            }           
            var text = li.childNodes[0].innerHTML.toLowerCase();
            
            if(!flag && text.toLowerCase().indexOf(Lower)==-1){
                li.style.display = "none";
            }else{
                li.style.display = "block";
            }
        }
        __RIIL_SELECT__.expand(id,conf);
    },
    /**
     * 设置Select选中值
     * 
     * @method setValue
     * @param id {String} select的id
     * @param id “值”
     * */
    setValue:function(id,value){
        var $selectBtn = $("#"+id);
        var type = __RIIL_SELECT__._getShowContent(id).attr("type");
        if(!type || type=="flexselect" || type=="icoselect"){
            type = "select";
        }
        __RIIL_SELECT__._plugins[type].setValue(id,value);
        __RIIL_SELECT__._getHidden($selectBtn).val(value);
    },
    /**
     * 设置Select选中文本
     * 
     * @method setText
     * @param id {String} select的id
     * @param value “选中值”
     * @param text 文本
     * @param title 悬浮提示，如果不指定，使用text
     * */
    setText:function(id,value,text, title){
        var $selectBtn = $("#" + id);
        var type = __RIIL_SELECT__._getShowContent(id).attr("type");
        if(!type || type=="flexselect"){
            type = "select";
        }
        var isCurrent = __RIIL_SELECT__._plugins[type].setText(id, value, text, title);
        if(isCurrent){
            __RIIL_SELECT__._setText(id, text, title);
        }
    },
    /**
     * 选中下一个或选中上一个
     * 
     * @method nextSelect
     * @param id {String} 组件ID
     * @param position {String} "up"或"down"
     * 
     */
    nextSelect:function(id,position){
        var $content = __RIIL_SELECT__._getShowContent(id);
        var lis = $content.find("li");
        var $over = lis.filter(".over");
        lis.removeClass("over");
        
        if($over[0]){
            if(position == 'up'){
                
                var flag = false;
                while($over = $over.prev()){
                    if($over[0]){
                        if($over[0].style.display != "none"){
                            $over.addClass("over");
                            flag = true;
                            break;
                        }
                    }else{
                        break;
                    }
                }
                if(!flag){
                    for(var i=lis.length-1;i>0;i--){
                        if(lis[i].style.display!="none"){
                            $(lis[i]).addClass("over");
                            break;
                        }
                    }                   
                }
            }else{
                var flag = false;
                while($over = $over.next()){
                    if($over[0]){
                        if($over[0].style.display != "none"){
                            $over.addClass("over");
                            flag = true;
                            break;
                        }
                    }else{
                        break;
                    }
                }
                if(!flag){
                    for(var i=0,len = lis.length;i<len;i++){
                        if(lis[i].style.display!="none"){
                            $(lis[i]).addClass("over");
                            break;
                        }
                    }           
                }
                
            }
        }else{
            for(var i=0,len = lis.length;i<len;i++){
                if(lis[i].style.display!="none"){
                    $(lis[i]).addClass("over");
                    break;
                }
            }
        }
    },
    /**
     * 选中指定索引选项
     * 
     * @method selectedByIndex
     * @param id {String} 组件ID
     * @param index {Number} 选项索引
     * */
    selectedByIndex:function(id,index){
        var $selectBtn = $("#"+id);
        var type = __RIIL_SELECT__._getShowContent(id).attr("type");
        if(!type || type=="flexselect" || type=="icoselect"){
            type = "select";
            var val = __RIIL_SELECT__._plugins[type].selectedByIndex(id,index);
            __RIIL_SELECT__._getHidden($selectBtn).val(val);        
        }
    },  
    selectAfter:function(conf,keyvalue,node){       
        __RIIL_SELECT__.collapse(conf.id,{});
        if(!$.getJQueryDom(conf.id).attr("dontSelect")){
            __RIIL_SELECT__.setValue(conf.id,keyvalue);    
        }
        
        var selectAfter = conf.listeners.selectAfter;
        if(selectAfter){
            selectAfter(keyvalue,node);
        }
    },
    /**
     * 获得选中值
     * 
     * @method getValue
     * @param id {String} 组件ID
     * @return “值”
     * */
    getValue:function(id){
        return __RIIL_SELECT__._getHidden($("#"+id)).val();
    },
    /**
     * 自动隐藏处理事件
     * 
     * @method _autoHide
     * @private
     * */
    _autoHide:function(event){
        if(event.target.className.indexOf("select_boundtree")!=-1) return;
        var selectId = event.data.selectId;
        var conf = event.data.conf;
        var $selectConent = __RIIL_SELECT__._getShowContent(selectId);
        var width = $selectConent.width();
        var height = $selectConent.height();
        var offset = {left:parseInt($selectConent.css("left")),top:parseInt($selectConent.css("top"))};
        if(event.pageX > offset.left && event.pageX < offset.left+10 + width && event.pageY > offset.top && event.pageY < offset.top + height+10){
            return;
        }else{
            conf.hideChild = false;
            __RIIL_SELECT__.collapse(selectId,conf);
        }
    },
    /**
     * 根据节点的val值，获得自定义属性值
     * 
     * @method getItemProperty
     * @param id {String} 组件id
     * @param val 选项val值
     * @param proName {String} 自定义属性名
     * @return {String} 属性值，如果不存在属性返回false
     * */
    getItemProperty:function(id,val,proName){
        var cs = __RIIL_SELECT__;
        var $content = cs._getShowContent(id);
        var $li = $content.find('li[val="'+val+'"]');
        if($li[0]){
            return $li.attr(proName);
        }else{
            var value = cs.getValue(id);
            $li = $content.find('li[nodeid="'+value+'"]');
            if($li[0]){
                return $li.attr(proName);
            }else{
                return false;
            }
        }
    },
    /**
     * 选择指定值
     * 
     * @method selecting
     * @private
     * @param id {String} 组件ID
     * @param value “值”
     */
    selecting:function(id,value){
        var $selectBtn = $("#"+id);
        var type = CompatibleSelect._getShowContent(id).attr("type");
        type = type ? type : "select";
        CompatibleSelect._plugins[type].selecting(id,value);        
    },  
    /**
     * 获得选中项自定义属性值
     * 
     * @method getSelectedItemProerty
     * @param id {String} 下拉框id
     * @param proName {String} 自定义属性名 
     * @return 属性值，如果不存在属性返回false
     * */
    getSelectedItemProerty:function(id,proName){
        var cs = __RIIL_SELECT__;
        var $content = cs._getShowContent(id);
        var $li = $content.find('li.on');
        if($li[0]){  //下拉项
            return $li.attr(proName);
        }else{//下拉树
            var value = cs.getValue(id);
            $li = $content.find('li[nodeid="'+value+'"]');
            if($li[0]){
                return $li.attr(proName);
            }else{
                return false;
            }
        }       

    },
    _hide:function(event,id){
        event = event || window.event;
        var $selectConent = __RIIL_SELECT__._getShowContent(id);
        var width = $selectConent.width();
        var height = $selectConent.height();
        var offset = {left:parseInt($selectConent.css("left")),top:parseInt($selectConent.css("top"))};
        
        x = event.pageX || event.x;
        y = event.pageY || event.y;
        if(x> offset.left && x < offset.left + width && y> offset.top && y < offset.top + height){
            this.focus();
        }else{
            setTimeout(function(){
                __RIIL_SELECT__.collapse(id);
            },1000);
        }
    },
    remove:function(id){
        var $select = $("#"+id);
        $select.find("*").unbind().remove();
        $select.remove();
        var $selectBody = $("#" + id + __RIIL_SELECT__._SELECTBODY);
        $selectBody.find("*").unbind().remove();
        $selectBody.remove();       
    },
    /**
     * 获得某选项元素
     * @indent 选项标识，可以使选项索引从0开始，或者是选项的值
     * */
    getItem:function(id,indent){
        var $content = __RIIL_SELECT__._getShowContent(id);
        var $li = $content.find('li[val="'+indent+'"]');
        if(!$li[0]){
            $li = $content.find("li:eq("+indent+")");
        }
        return $li;
    },
    /**隐藏或显示某元素
     * @id 组件id
     * @indent 选项标识，可以是选项索引从0开始 或者 选项val
     * @isShow boolean 显示或隐藏
     * */
    displayItem:function(id,indent,isShow){
        var self = __RIIL_SELECT__;
        var $item = self.getItem(id,indent);
        if(isShow){
            $item.show();
        }else{
            $item.hide();
            if(self.getValue(id) == $item.attr("val")){
                var $slibing = $item.next();
                if(!$slibing[0]){
                    $slibing = $item.prev();
                }
                if($slibing[0]){
                    self.setValue(id,$slibing.attr("val"));
                }
            }
        }
    },
    /**
     * 获得自定义下拉框模板
     * 
     * @method getTemplate
     * @param selectId {String} 下拉框id
     * @param name {String} 字段name
     * @param selectValues* {Json} 下拉选框项值[{text:"",value:""}]
     * @param selectedValue 当前选中值
     * */
    getTemplate:function(selectId,name,selectValues,selectedValue,attrs,appendItems, width, disable){
        selectedValue = selectedValue ? selectedValue : "";
        var disableClass = '';
        if (disable === true) disableClass = ' select_body_disabled';
        var select = ['<div  id="'+selectId+'" class="select_body' + disableClass + '" value="'+selectedValue+'" '];
        if (disable === true) select.push('available="false" ');
        if (width) {
            if (width === '100%') select.push('style="width:' + width + '; margin-left:-1px; margin-right:-1px;" ');
            else select.push('style="width:' + width + ';" ');
        }
        if(attrs){
            if($.isString(attrs)){
                select.push(" " + attrs + " ");
            }else{         
                for(var key in attrs){
                    select.push(" "+key+"="+attrs[key]);
                }
            }       
        }
        select.push(">");
        select.push('<div class="select_text" name="'+name+'" ></div><div class="select_trigger_wrap"><a class="select_trigger"></a></div></div>');
        select.push('<div  id="' + selectId + __RIIL_SELECT__._SELECTBODY + '" class="select_boundtree"><ul>');
        if(appendItems){
            if($.isString(appendItems)){
                select.push(appendItems);
            }else{
                for(var i = 0, len = appendItems.length; i < len; i++){
                    select.push(appendItems[i]);    
                }
            }
        }
        var option = null;
        for (var i = 0, len = selectValues.length; i < len; i++) {
            option = selectValues[i];
            select.push('<li val="'+option.value+'"><a  title="'+option.text+'">'+option.text+'</a></li>');
        }
        select.push("</ul></div>");
        return select.join("");


    }
};/*
 * @inner
 * 内部插件机制
 * */
__RIIL_SELECT__._plugins = {};


/*普通下拉框*/
__RIIL_SELECT__._plugins.select = {
	init : function($content, conf, value){
		var plugin = __RIIL_SELECT__._plugins.select;	
		plugin.bindEvent($content, conf, value);		
		//如果选项是复选框形式，初始化复选框操作
		if(plugin.isCheckboxItem(plugin.getItems($content))){
			conf.value = value;
			plugin.initCheckboxItem(conf);
		}
		$content.attr("mainContent","mainContent");
		//选项是否有子菜单，如果有，添加下级指示按钮
		plugin.hasChild($content, conf);
	},
	/**
     * 绑定事件
     * @param $content 下拉选项层
     * @param conf
     * @param value 初始选中值
	 */
	bindEvent : function($content, conf, value){
		var plugin = __RIIL_SELECT__._plugins.select;
		$content.children("ul");
		value = value || $.getJQueryDom(conf.id).attr("value");
		var $items = plugin.getItems($content);
		//绑定选项点击事件，悬浮事件（显示子菜单和悬浮颜色）
		$items.unbind().bind("click",function(event){
			event.stopPropagation();			
			plugin.getItems($content).removeClass("on");
			var $li = $(this).addClass("on");
			
			//$li.children("a").css("color", $li.css("color"));
			if(__RIIL_SELECT__._plugins.select.isCheckboxItem($li)){
				if(event.target.tagName.toLowerCase() != "input"){
					__RIIL_SELECT__._plugins.select.changeCheckboxItemState($li);
				}
				__RIIL_SELECT__._plugins.select.checkboxItem(conf);
				return;
			}			
			$.getJQueryDom($content.attr("parentId") + __RIIL_SELECT__._SELECTBODY).removeAttr("showingChild");
			$content.removeAttr("showingChild");
			__RIIL_SELECT__.selectAfter(conf,$li.attr("val"),$li);
		}).hover(function(event){
					var $item = $(this);
					plugin.hover($item, true);
					var $content = plugin._getCotnentByItem($item);
					$content.attr("hasOver", true);//标识父菜单已经显示出子菜单id
				},function(event){
					var $item = $(this);
					plugin.hover($item, false);
					var $content = plugin._getCotnentByItem($item);
					$content.removeAttr("hasOver");//标识父菜单已经显示出子菜单id
					conf.hoverHide = true;//子菜单悬浮离开，隐藏					
			});
		//下拉选项层悬浮事件，标识该层是否在悬浮显示
		$content.hover(function(){
			$content.attr("show","true");
		},function(){
			$content.removeAttr("show");
		});

		if(conf.isHover){ return;}//如果是悬浮显示出来的，而不是第一级菜单，则不需要选中对选项
		var $firstLi = plugin.getItems($content).filter(":eq(0)");
		if(!value || value == ""){
			value = $firstLi.attr("val");
			if(plugin.isCheckboxItem(plugin.getItems($content))){
				return;
			}
		}
		//初始化，不触发选中事件，只赋值
		if(conf.initSelectAfter ===  false){
			__RIIL_SELECT__.setValue(conf.id,value);
		}else{
			__RIIL_SELECT__.selectAfter(conf,value,$firstLi);
		}
	},
	hover : function($item, isHover){
		if(isHover == true){
			$item.addClass("hover").addClass("over");
		}else{
			$item.removeClass("hover").removeClass("over");
		}
	},
	_getUl : function($content){
		return $content.children("ul");
	},
	getItems : function($content, param){
		return __RIIL_SELECT__._plugins.select._getUl($content).children("li" + (param || ""));
	},
	//根据li获取所在的div层
	_getCotnentByItem : function($item){
		return $item.parent().parent();
	},
	/*
	 * 选项是否有子菜单，如果有，添加下级指示按钮,绑定显示子菜单
	 */
	hasChild : function($content, conf){
		if($content.attr("isHasChild")){return;}//已经判断过标识		
		$content.attr("isHasChild",true);
		var plugin = __RIIL_SELECT__._plugins.select;
		var items = plugin.getItems($content,"[child]");		
		var $li = null, childContent = null;
		for(var i = 0, len = items.length; i < len; i++){
			$li = $(items[i]);

			var $childContent = $.getJQueryDom( $li.attr("child") + __RIIL_SELECT__._SELECTBODY);
			if($childContent[0]){
				$content.css("overflowX","visible").attr("hasChild", true);
				$li.append($childContent).children("a").append('<span class="icon icon_right"></span>');
				$childContent.attr("parentId",conf.id);
				$li.hover(function(event){
					var $item = $(this);
					var childId = $item.attr("child");
					var $content = plugin._getCotnentByItem($item);
					var showChildId = $content.attr("showChildId");					
					if(showChildId){
						plugin.hideChild(showChildId, $content.find('li[child="' + showChildId + '"]'), conf);
					}
					$content.attr("showChildId", childId);//标识父菜单已经显示出子菜单id
					plugin.showChild(childId, $item, conf);
					plugin.hover($item,true);
					$item.attr("showChild","true");
					$content.attr("showingChild",true);
				},function(event){
					var $item = $(this);					
					var $content = plugin._getCotnentByItem($item);
					$content.removeAttr("showingChild");
					if($content.attr("mainContent")){
						if($item.offset().left > event.pageX){
							plugin.hideChild($item.attr("child"), $item, conf);
						}						
					}			
				});
			}
		}
		if(!$content.attr("hasChild")){
			$content.css("overflowX", "auto");
		}
	},
	/*
	 * 显示子菜单
	 * @param $item 选项对象，用于显示该选项的子菜单*
	 */
	showChild : function(childId, $item, conf){
		var $selectBody = $.getJQueryDom(childId + __RIIL_SELECT__._SELECTBODY);
		if(!$selectBody[0]) return;		
		var plugin = __RIIL_SELECT__._plugins.select;
		$selectBody.show();
		if($selectBody.height() > 322){
			$selectBody.height(322);
		}
		var offset = $item.offset();
		var firstLiOffset = $item.find('li:first').offset();
		var selectBodyOffset = $selectBody.offset();
		var offsetDiference = {
			top: firstLiOffset.top - selectBodyOffset.top,
			left: firstLiOffset.left - selectBodyOffset.left
		};
		offset.left += $item.outerWidth(true);
		var heightDifference = offset.top + $selectBody.outerHeight(true) - $(document.body).innerHeight();
		if (heightDifference > 0) {
			offset.top -= heightDifference;
		}

		offset.top -= offsetDiference.top;
		offset.left += offsetDiference.left;
		
		$selectBody.offset(offset);
		//var layout = $.getElementAbsolutePosition($item[0]);
		//var $selectBody = __RIIL_SELECT__.setSelectBodyPosition(childId, {x : layout.x + $.getAllSize($item).width, y : layout.y});
		//$selectBody.css("overflowX","auto");
		if($selectBody.attr("isBind")){return;}
		$selectBody.attr("isBind","true");
		conf.isHover = true;//确保不触发默认第一项
		plugin.bindEvent($selectBody, conf);
		plugin.hasChild($selectBody, conf);		
	},
	hideChild : function(childId, $item, conf){		
		var $selectBody = $.getJQueryDom(childId + __RIIL_SELECT__._SELECTBODY);		
		if(!$selectBody[0]) return;
		var plugin = __RIIL_SELECT__._plugins.select;
		if($selectBody.attr("hasOver")){ //用来标识鼠标悬浮在该层上，不能隐藏
			plugin.hover($item, true);
			return false;
		}
		$selectBody.hide();		
		//var $parentContent = plugin._getCotnentByItem($item);
		//上一菜单已经消失，或者
		//if(!$parentContent[0] || ($parentContent.parent()[0].tagName.toLowerCase() == "body" && $parentContent[0].style.display == "none")){
		//	$selectBody.find("*").unbind();
		//	$selectBody.remove();
		//}else{
		//	$parentContent.append($selectBody)
		//}
	},	
	//判断是否是复选框想
	isCheckboxItem:function($li){
		return $li.find('input[type="checkbox"]').length != 0;
	},
	/**初始化复选框选中（回选）*/
	initCheckboxItem : function(conf){
		if(!conf.value) return;
		var value = conf.value.split(",");
		var id = conf.id;
		var $content = __RIIL_SELECT__._getShowContent(id);
		var lis = __RIIL_SELECT__._plugins.select.getItems($content);
		for(var i = 0, len = value.length; i < len; i++){
			var $li = lis.filter('[val="' + value[i] + '"]');
			$li.click();
		}
	},
	//复选框更改状态互斥
	changeCheckboxItemState : function($li){
		var $checkbox = $li.find('input[type="checkbox"]');
		$checkbox[0].checked = $checkbox[0].checked ? false : true;
	},
	/**
     * 选项为复选框,选中后更新选中值
	 */
	checkboxItem : function(conf){
		var id = conf.id;
		var $content = __RIIL_SELECT__._getShowContent(id);
		var lis = __RIIL_SELECT__._plugins.select.getItems($content);
		var text = [];
		var value = [];
		for(var i = 0, len = lis.length; i < len; i++){
			var $li = $(lis[i]);
			var checked = $li.find('input[type="checkbox"]:checked');
			if(checked.length > 0){				
				text.push($li.children("a").attr("title"));				
				value.push($li.attr("val"));
			}
		}		
		var value = value.join(",");
		__RIIL_SELECT__._getHidden($("#" + id)).val(value);
		__RIIL_SELECT__._setText(id,text.join(","));
		if(conf.listeners && conf.listeners.selectAfter){
			conf.listeners.selectAfter(value);
		}
	},
	/*
	 * 更改选项文本
	 * id组件id
	 * value组件选项值
	 * text更改文本
	 * */
	setText:function(id, value, text, title){
		var $content = __RIIL_SELECT__._getShowContent(id);
		var $li = __RIIL_SELECT__._plugins.select.getItems($content).filter("[val='" + value + "']");
		$li.children("a").html(text).attr("title", title || text);
		if($li.hasClass("on")){
			return true;
		}else{
			return false;
		}
	},
	/*
	 * api 选中某个节点，模拟选中效果
	 * @param id 组件id
	 * @param val 选中的索引或者值
	 * */
	selecting:function(id,value){
		var $content = CompatibleSelect._getShowContent(id);
		var $li = null; 
		var lis = __RIIL_SELECT__._plugins.select.getItems($content);
		if($.isString(value)){			
			for(var i = 0, len = lis.length; i < len; i++){
				if(lis[i].attributes["val"].value ==  value){
					$li = $(lis[i]);
					break;
				}
			}
		}else{
			$li = lis.filter(":eq(" + value + ")");
		}
		$li.click();
	},
	setValue:function(id,value){
		var plugin = __RIIL_SELECT__._plugins.select;
		var $content = __RIIL_SELECT__._getShowContent(id);
		var items = plugin.getItems($content);
		var $selectLi =  items.filter("[val='" + value + "']");
		if(!$selectLi[0]){
			$selectLi = plugin.roundItemByValue($content, value);
			if(!$selectLi || !$selectLi[0]){
				return;
			}
		}
		$("#" + id).attr("value", value);
		items.removeClass("on");
		var $aon = $selectLi.addClass("on").children("a");
		var text = $aon.html();
		__RIIL_SELECT__._setText(id,text,$aon.attr("title"));
	},
	/**
	 * 遍历查找子菜单，是否有指定val值的选项（li）
	 * */
	roundItemByValue : function($content, value){
		$content.children("ul");
		var plugin = __RIIL_SELECT__._plugins.select;
		var $selectItem = null;
		var lis = plugin.getItems($content);
		for(var i = 0, len = lis.length; i < len; i++){
			var $li = $(lis[i]);
			if($li.attr("val") == value){
				return $li;
			}
			var child = $(lis[i]).attr("child");
			if(child){
				var $content = __RIIL_SELECT__._getShowContent(child);
				if($content[0]){
					$selectItem = lis.filter("[val='" + value + "']");
					if(!$selectItem[0]){
						$selectItem = plugin.roundItemByValue($content, value);
						if($selectItem && $selectItem[0]) return $selectItem;
					}else{
						return $selectItem;
					}
				}
			}
		}
		return $selectItem;
	},
	selectedByIndex : function(id, index){
		var $content = __RIIL_SELECT__._getShowContent(id);
		var plugin = __RIIL_SELECT__._plugins.select;
		var lis = plugin.getItems($content);
		lis.removeClass("on");
		var $selectLi = lis.filter(":eq(" + index + ")");
		$selectLi.addClass("on");
		$("#" + id).attr("value", $selectLi.attr("val"));
		var text = $selectLi.attr("title");
		text = text || $selectLi.children("a").html();
		__RIIL_SELECT__._setText(id,text);
		return $selectLi.attr("val");
	},			
	refresh:function(conf,value,contentHTML){
		var $content = __RIIL_SELECT__._getShowContent(conf.id);
		$content.html(contentHTML);
		$content.css({"height":"auto","width": "auto"});		
		$content.removeAttr("isScroll");
		this.bindEvent($content, conf, value);

	},
	append:function(conf,value,contentHTML){
		var $content = __RIIL_SELECT__._getShowContent(conf.id);
		var $ul = __RIIL_SELECT__._plugins.select._getUl($content).append(contentHTML);
		$content.html($ul);		
		$content.removeAttr("isScroll");
		this.bindEvent($content, conf, value);
		
	}
};
;/*下拉树*/
__RIIL_SELECT__._plugins.tree = {
			init:function($content,conf,currentValue){
				$content.addClass("select_boundtree_broaden");				
				var treeId = $content.children(":eq(0)").attr("id");
				if(!__RIIL_SELECT__.trees){
					__RIIL_SELECT__.trees={};
				}
				__RIIL_SELECT__.trees[conf.id] = new Tree({
					id : treeId,
					listeners : {
						nodeClick : function(node) {
							__RIIL_SELECT__.selectAfter(conf,node.getId(),node);
						}
					}
				});
				if(currentValue!=null){
					var node = __RIIL_SELECT__.trees[conf.id].getNodeById(currentValue);
					if(node){
						node.expandUP();
						__RIIL_SELECT__.setValue(conf.id, currentValue);
						if(conf.initSelectAfter !== false){
							__RIIL_SELECT__.selectAfter(conf,node.getId(),node);
						}
					}else{
						node = __RIIL_SELECT__.trees[conf.id].getRoot().getFirstChild();
						if(node && conf.initSelectAfter !== false){
							__RIIL_SELECT__.selectAfter(conf,node.getId(),node);
						}
					}
				}
			},refresh:function(conf,value,contentHTML){
				var $content = __RIIL_SELECT__._getShowContent(conf.id);
				$content.html(contentHTML);
				
				this.init($content,conf,value || "");
				
			},
			/*
			 * api 选中某个节点
			 * @param id 组件id
			 * @param val 选中的节点ID
			 * */
			selecting:function(id,value){
				var tree = CompatibleSelect.trees[id];
				if(!tree){
					return;
				}
				var node = tree.getNodeById(value);
				if(node){
					node.click();					
				}
			},				
			setValue:function(id,value){
				var tree = __RIIL_SELECT__.trees[id];
				if(!tree){
					return;
				}
				var node = tree.getNodeById(value);
				if(node){
					node.setCurrentNode();
					__RIIL_SELECT__._setText(id,node.getText());
				}
			},
			setText:function(id,text){
				
			}
		
};/**
 * @class
 * 表单组件
 * */
var FormPanel = function() {
};

FormPanel.prototype = {
    constructor : FormPanel,
    /**
     * 初始化
     * */
    init : function(conf) {
        this.conf = conf;
        return this;
    },
    render : function(conf) {
        this.renderDom(conf);
        return this;
    },
    bindEvent : function(conf) {
        
        this.getSubmit().bind("click", {
            self : this
        }, function(event) {
            var self = event.data.self;
            FormValidate.validate(conf.domId,function(){
                
                if (conf.listeners) {
                    if (conf.listeners.submitBefore) {
                        if (conf.listeners.submitBefore(self) === false) {
                            return;
                        }
                    }
                    if (conf.listeners.submit) {
                        conf.listeners.submit(self);
                    } else {
                        self.submit();
                    }
                } else {
                    self.submit();
                }
                
            });
            
        });
        
        FormValidate.initDefault(this.getForm());
        
        return this;
    },
    /**
     * 提交表单
     * */
    submit : function() {
        var $form = this.getForm();
        var dataType = this.conf.dataType ? this.conf.dataType : "json";
        var success = this.conf.success ? this.conf.success : function() {
        };
        PageCtrl.ajax({
            url : $form.attr("href"),
            data : $form.serialize(),
            type : "post",
            dataType : dataType,
            success : success
        });
    },
    /**
     * 根据字段name获得该表单字段Jquery对象
     * 
     * @param {Object}
     *            FieldName return 返回字段的JQuery对象
     */
    getField : function(FieldName) {
        return this.getForm().find('[name="' + FieldName + '"]');
    },
    /**
     * 获得该form的提交路径
     * @returns {String}
     * */
    getHref : function() {
        return this.getForm().attr("href");
    },
    /**
     * 序列化form中字段为url请求参数格式
     * @returns {String}
     * */
    serialize : function() {
        return this.getForm().serialize();
    },
    /**
     * 将form字段和值转换为json
     * @returns {JSON}
     * */
    formText2Json : function() {
        var $form = this.getForm();
        var $fields = $form.find("[name]");
        var formData = {};
        for ( var i = 0, len = $fields.length; i < len; i++) {
            var $field = $($fields[i]);
            var type = $field.attr("type");
            type = type ? type.toLowerCase() : "input";
            var text = null;
            var name = $field.attr("name");
            if (type == "text" || type == "password" || type == "hidden" ) {
                text = $field.val();
            } else if(type === "radio" ){
                if (!formData[name]) {
                    text = $form.find('[name="' + name + '"]:checked').val();
                }
            } else if ($field[0].tagName == "TEXTAREA") {
                text = $field.val();
            } else if ($field[0].tagName == "SELECT") {
                text = $field.find('option:selected').text();
            } else if (type == "checkbox") {
                if (!formData[name]) {
                    var checkboxs = $form.find('input[type="checkbox"][name='
                            + name + ']');
                    var texts = [];
                    for ( var i = 0, len = checkboxs.length; i < len; i++) {
                        var $checkbox = $(checkboxs[i]);
                        texts.push($checkbox.attr("text"));
                    }
                    text = texts.join(",");
                }
            }
            if(text){
                formData[name] = text;
            }
        }
        return formData;
    },
    initHelp : function(callback) {
        this.getForm().find("span.icon").click(callback);
    }
}
if (!FormPanel.render) {
    FormPanel.render = {};
}
/*
 * 服务器端生成HTML组件方式
 * 
 * @param {Object}
 *            conf
 */
FormPanel.render.server2HTML = {
    renderDom : function(conf) {
        var $form = this.getForm();
        //查询域类型，是否有插件域（自定义的表单域）
        var fieldTypeDoms = $form.find('[fieldType]');
        var plugin = null;
        var $fieldDom = null;
        for(var i=0,len = fieldTypeDoms.length; i<len; i++){
            $fieldDom = $(fieldTypeDoms[i]);
            plugin = FormPanel.plugin[$fieldDom.attr("fieldType")];
            if(plugin){
                plugin.render.call(this,$fieldDom);
            }
        }
        return this;
    },
    /*获得表单*/
    getSubmit : function() {
        return $("#" + this.domId + "submit");
    },
    getForm : function() {
        return $("#" + this.domId);
    }
}
/*
 * 表单插件
 * */
FormPanel.plugin = {
        /**
         * 列表项
         * */
    list : {
        render : function() {
            var $listTypes = this.getForm().find("ul").children("li.list");
            var id = null;
            this.listPanels = {};
            for ( var i = 0, len = $listTypes.length; i < len; i++) {
                id = $($listTypes[i]).find("ul:eq(1)").attr("id");
                this.listPanels[id] = CompMgr.getComp({
                    compID : "ListPanel",
                    renderName : "server2HTML",
                    domId : id
                }, false, true);
            }
        }
    },
    /**
     * 复选框插件
     * */
    checkbox : {
        render : function() {
            this.getForm().find("input:checkbox[checkedVal]").click(function() {
                var $checkbox = $(this);
                if (this.checked) {
                    $checkbox.next().val($checkbox.attr("checkedval"));
                } else {
                    $checkbox.next().val($checkbox.attr("uncheckval"));
                }
            });
        }
    },
    /**
     * 下拉树
     * 
     * */
    selectTree : {
        render : function($selectTreeDom) {
            if (!this.selectTrees) {
                this.selectTrees = {};
            }

            var $form = this.getForm();
            var formSelf = this;
            var customSelect = CompMgr.getComp({
                compID : "CustomSelect",
                renderName : "server2HTML",
                applyDom : $selectTreeDom,
                listeners : {
                    selectAfter : function(self, node) {
                        var name = self.getName();
                        $form.find('input[name="' + name + '"]')
                                .val(self.value);
                        if (formSelf.conf.listeners
                                && formSelf.conf.listeners.selectAfter) {
                            formSelf.conf.listeners.selectAfter(self, node);
                        }
                    }
                }
            }, false, true);
            this.selectTrees[$selectTreeDom.attr("name")] = customSelect;

        }
    }
}
CompMgr.regComp("FormPanel", FormPanel);
;/*jshint jquery:true*/
/*jslint browser:true,unparam:true,continue:true*/
/*global ZIndexMgr,PageCtrl,Validate,InputEmpty */
(function () {
  'use strict';

  /**
   * @class
   * 表单验证组件
   * */
  var FormValidate = (function () {
    //内部保存每个表单字段验证处理函数
    //属性：customValidate为用户自定义验证函数
    //属性：remoteUrl:远程验证的url,可能是静态url字符串,或者函数，动态生成url
    //属性：validating:标识正在验证中
    var formResult = {};

    return {
      /**
       *  初始化表单项
       *  @param element ｛dom/id｝ 需要在验证的表单域
       *  @param isRetain boolean  是否保持数据
       * */
      initDefault: function (element, isRetain) {
        var $element = $.getJQueryDom(element);
        var $targets = $element.find('[validate][isValidate!="false"]');
        var formId = $element.attr('id');
        //if(!formResult[formId]){}

        if (!isRetain || !formResult[formId]) {
          formResult[formId] = {};
          formResult[formId].customValidate = {};
          formResult[formId].remoteUrl = {};
        }

        var target = null;
        var i, len;
        for (i = 0, len = $targets.length; i < len; i++) {
          target = $targets[i];
          this.bindValidate(target, null, $element.attr('id'));
        }
      },
      /**
       * 添加自定义验证
       * @param formId {String} 表单ID
       * @param targetDom {String} 验证的字段对象或id
       * @param eventName {String} 出发验证事件名
       * @param validateHandler {function}验证函数
       * @param errorMsg {String} 错误信息
       * */
      addCustomValidate: function (formId, targetDom, eventName, validateHandler, errorMsg) {
        var $targetDom = $.getJQueryDom(targetDom);
        if (!$targetDom[0]) {
          $targetDom = $('#' + formId + ' [name="' + targetDom + '"]');
        }
        FormValidate.bindValidate($targetDom, eventName, formId, validateHandler, 'custome');
        var targetName = $targetDom.attr('name');
        if (!formResult[formId].customValidate) {
          formResult[formId].customValidate = {};
        }
        if (!formResult[formId].customValidate[targetName]) {
          formResult[formId].customValidate[targetName] = [];
        }
        $targetDom.attr('ErrorMsg', errorMsg);
        formResult[formId].customValidate[targetName].push(validateHandler);
      },
      /**
       * 停止某个字段验证
       * @param formId 表单外层ID，如果不是字符串则表示为dom对象
       * @param filedName 字段Name属性值,如果不是字符串则表示为dom对象
       * */
      stopValidate: function (formId, fieldName) {
        var $form = $.isString(formId) ? $('#' + formId) : $(formId);
        var $field = $.isString(fieldName) ? $form.find('[name="' + fieldName + '"]') : $(fieldName);
        $field.attr('isValidate', 'false').removeClass('border-red').removeAttr('nowError');
      },
      /**
       * 开始某个字段验证
       * @param formId 表单外层ID
       * @param filedName 字段Name属性值
       * */
      startValidate: function (formId, fieldName) {
        var $form = $.isString(formId) ? $('#' + formId) : $(formId);
        var $field = $.isString(fieldName) ? $form.find('[name="' + fieldName + '"]') : $(fieldName);
        $field.removeAttr('isValidate');
      },
      /**
       * 远程验证添加验证url
       * formId： {String} formId
       * fieldName: {String} 字段Name
       * url:远程验证地址，{function} 用于动态生成url,该函数参数为需要验证的值。
       * */
      addRemoteUrl: function (formId, fieldName, url) {
        if (!formResult[formId].remoteUrl) {
          formResult[formId].remoteUrl = {};
        }
        formResult[formId].remoteUrl[fieldName] = [];
        formResult[formId].remoteUrl[fieldName].push({
          'fieldName': fieldName,
          'url': url
        });
        FormValidate.bindValidate($('#' + formId + ' [name="' + fieldName + '"]'), null, formId);
      },

      /**
       * 提示表单域有错误，但是不显示错误信息，帮定当鼠标悬浮的触发显示错误信息事件
       * @param targetDom {dom/id} 需要显示信息的表单域
       * @param info {String} 信息
       * */
      showErrorMessage: function (targetDom, info) {

        FormValidate.setError(targetDom, info);
        FormValidate.bindValidate(targetDom);
      },
      /*
       * @inner
       * 设置当前验证错误信息，将其变为红色边框
       * @param  {jquery} $target 需要显示错误信息的表单域对象
       * @param  {String} errormessage 错误信息
       * */
      setError: function ($target, errormessage) {
        $target.attr('nowError', errormessage);
        $target.addClass('border-red');
      },
      /**
       *
       * */
      showError: function (event) {
        event.stopPropagation();
        event.preventDefault();
        if (!FormValidate.isErrorField(this)) {
          return;
        }
        var $targetDom = $(this);

        $targetDom.attr('hasHideError', 'true');
        var nowError = $targetDom.attr('nowError');
        FormValidate.showErrorMsg(this, nowError);
      },
      /**
       * 指定字段是否有错误
       * @param 字段对象，可以是元素ID或dom
       * @return true有错误，false正确
       * */
      isErrorField: function ($target) {
        $target = $.getJQueryDom($target);
        return $target.hasClass('border-red');
      },
      /**
       * 对外提供显示错误信息方法
       * @param target 验证字段
       * @param nowError 错误信息
       * */
      showErrorMsg: function (target, nowError) {
        var $targetDom = $.getJQueryDom(target);
        var layout = $.getElementAbsolutePosition($targetDom[0]);
        if (document.documentElement && document.documentElement.scrollTop) {
          layout.y = layout.y + document.documentElement.scrollTop;
        }
        // var $arrows = $('<div class="point-bottom-arrow"></div>');
        var name = $targetDom.attr('name');
        if (name) {
          name = name.replace('.', '_');
        }
        var $FormValidateDiv = $('div.validate-div');
        if ($FormValidateDiv[0]) {
          if ($FormValidateDiv.attr('id').indexOf(name) !== -1) {
            return;
          }
          $FormValidateDiv.remove();
        }

        $FormValidateDiv = $('<div class="validate-div" id="' + name + 'FormValidateDiv"><span class="t">' + nowError + '</span></div>');
        if (nowError) {
          $(document.body).append($FormValidateDiv);
        }
        var validateDivClass = 'tt1';
        var validateDivWidth = $.getAllSize($FormValidateDiv.children()).width;

        if (validateDivWidth >= 130 && validateDivWidth <= 210) {
          validateDivClass = 'tt2';
        } else if (validateDivWidth > 210) {
          validateDivClass = 'tt3';
        }
        $FormValidateDiv.addClass(validateDivClass);
        var newLayout = $.checkDomPosition($FormValidateDiv, layout.x, layout.y - $FormValidateDiv.height());
        var x = newLayout.x;
        var y = newLayout.y;
        if (newLayout.y < 0) { //超出窗口顶部
          y = y + $targetDom.height() + $FormValidateDiv.height() + 10;
          if (layout.x !== x) {
            x = layout.x - 200 + $targetDom.width();
          }
          validateDivClass = validateDivClass + 'd';
        } else {
          y = y - 10;
          if (layout.x !== newLayout.x) {
            x = layout.x - 200 + $targetDom.width();
          }
        }
        $FormValidateDiv.removeClass('').addClass('validate-div ' + validateDivClass);
        $FormValidateDiv.css({
          left: x,
          top: y,
          'z-index': ZIndexMgr.get()
        });
      },
      /**
       * 隐藏错误信息
       * */
      hideErrorMessage: function (targetDom) {
        var $targetDom = $.getJQueryDom(targetDom);
        $targetDom.removeAttr('nowError');
        $targetDom.removeClass('border-red');
        FormValidate._hideErrorDiv($targetDom);
      },
      /**隐藏错误信息层*/
      _hideErrorDiv: function ($targetDom) {
        var name = $targetDom.attr('name');
        if (name) {
          name = name.replace('.', '_');
        }
        var $errorDiv = $('#' + name + 'FormValidateDiv');
        if (window.ZIndexMgr) {
          ZIndexMgr.free($errorDiv);
        }
        $errorDiv.remove();

      },
      blurHideErrorMessage: function (event) {
        event.stopPropagation();
        event.preventDefault();
        var $targetDom = $.getJQueryDom(this);
        if (!FormValidate.isErrorField($targetDom)) {
          return;
        }
        $targetDom.removeAttr('hasHideError');
        FormValidate._hideErrorDiv($targetDom);
      },
      /*
       * @inner
       * 绑定验证事件
       * @param targetDom 绑定验证的表单域对象
       * @param eventName 验证触发事件  如果为null，默认为blur
       * @param formId 所属表单
       * */
      bindValidate: function (targetDom, eventName, formId, validateHandler, bindType) {
        var $targetDom = $.getJQueryDom(targetDom); //.css("overflow","hidden");
        if (!$targetDom[0]) {
          $targetDom = $('#' + formId + ' [name="' + targetDom + '"]');
        }
        var flag = true;
        if ($targetDom.attr('isBind') === 'true') {
          flag = false;
        }

        if (bindType && $targetDom.attr('isBindCustome') === 'true') {
          flag = false;
        } else if (bindType && $targetDom.attr('isBindCustome') !== 'true') {
          flag = true;
        }
        if (!flag) {
          return;
        }
        eventName = eventName || 'blur';

        // 防止重复绑定事件
        if ($targetDom.attr('isBind') !== 'true') {
          $targetDom.bind('mouseover', {}, FormValidate.showError);
          $targetDom.bind('mouseout', {
            targetDom: $targetDom
          }, FormValidate.blurHideErrorMessage);

          $targetDom.bind(eventName, {
            formId: formId,
            validateHandler: validateHandler
          }, FormValidate.validateHandler).attr('isBind', 'true');
        }

        if (bindType) {
          $targetDom.attr('isBindCustome', 'true');
        }
      },
      unbind: function (formID) {
        $('#' + formID + ' *').unbind('focus', FormValidate.showError).unbind('blur', FormValidate.blurHideErrorMessage).unbind('blur', FormValidate.validateHandler).removeAttr('isBind');
      },
      /*
       * @inner
       * 验证事件处理函数,单独提出来，目的是不会在每次绑定事件的时候，都生成一个函数，处于性能考虑
       * */
      validateHandler: function (event) {
        var formValidate = FormValidate;
        var $target = $(this);
        if ($target.attr('isValidate')) {
          return;
        }
        var formId = event.data.formId;
        var validateHandler = event.data.validateHandler;
        var result = false;
        var validateModules = $target.attr('validate').split(',');
        var i, len;
        for (i = 0, len = validateModules.length - 1; i < len; i++) {
          result = FormValidate.validateType[validateModules[i]]($target);
          if (result === false) {
            formValidate.setError($target, $target.attr(validateModules[i] + 'ErrorMsg'));
            return;
          }
          formValidate.hideErrorMessage($target);
        }

        // 获取自定义验证
        if (!validateHandler) {
          validateHandler = (formResult[formId] &&
            formResult[formId].customValidate &&
            formResult[formId].customValidate[$target.attr('name')]) ||
            null;
        }

        if (validateHandler) {
          if (!$.isArray(validateHandler)) {
            validateHandler = [validateHandler];
          }
          var err = false;
          $.each(validateHandler, function (index, eachValidateHandler) {
            result = eachValidateHandler($target);
            if ($.isString(result) && result !== 'true') {
              formValidate.setError($target, result);
              err = true;
              return false;
            }
            if (result === false) {
              formValidate.setError($target, $target.attr(validateModules[i] + 'ErrorMsg'));
              err = true;
              return false;
            }
            formValidate.hideErrorMessage($target);
          });
          if (err) {
            return;
          }
        }

        //搜索该字段是否有ajax验证
        var remoteUrls = formResult[formId] && formResult[formId].remoteUrl ? formResult[formId].remoteUrl[$target.attr('name')] : null;
        if (remoteUrls) {
          $.each(remoteUrls, function (_, remoteField) {
            var urls;
            var paramsArray;
            var data = {};

            urls = (function () {
              var url = remoteField.url;
              if ($.isFunction(url)) {
                url = url($target.map(function (_, elem) {
                  return $(elem).val();
                }).get().join(','));
              }
              return url.split('?');
            }());

            paramsArray = urls[1].split('&');

            $.each(paramsArray, function (_, paramsText) {
              var params = paramsText.split('=');
              data[params[0]] = params[1];
            });

            PageCtrl.ajax({
              url: urls[0],
              cache: false,
              type: 'post',
              data: data,
              dataType: 'text',
              success: function (text) {
                if (text !== 'true') {
                  if (text !== 'false') { //如果返回不是false，那么表示返回的是错误信息
                    $target.attr('ajaxErrorMsg', text);
                  }
                  formValidate.setError($target, $target.attr('ajaxErrorMsg'));
                  return;
                }
                formValidate.hideErrorMessage($target);
              }
            });

          });
        }
      },
      /**
       * 手动全部验证
       * @param formId 表单外层ID
       * @param callback 验证通过callback
       * @param errorback 验证失败errorback
       * */
      validate: function (formId, callback, errrorback) {
        var formValidate = FormValidate;
        var $Form = $.getJQueryDom(formId);
        var $targets = $Form.find('[validate][isValidate!="false"]');
        var $target = null;
        var validateModules = null;
        var result = true;
        var validResult = true;
        var defaultResult;
        //先验证默认
        var i, len;
        var j, jlen;
        for (i = 0, len = $targets.length; i < len; i++) {
          $target = $($targets[i]);
          validateModules = $target.attr('validate').split(',');
          for (j = 0, jlen = validateModules.length - 1; j < jlen; j++) {
            defaultResult = FormValidate.validateType[validateModules[j]]($target);
            if (defaultResult === false) {
              result = defaultResult;
              formValidate.setError($target, $target.attr(validateModules[j] + 'ErrorMsg'));
              validResult = false;
              break;
            }
            formValidate.hideErrorMessage($target);
          }
        }

        if (result === false) {
          if (errrorback) {
            errrorback();
          }
          return;
        }

        //如果默认验证通过并且有自定义的验证
        var customValidate = formResult[formId] ? formResult[formId].customValidate : null;
        if (customValidate) {
          var customValidateFns = null;
          var targetName;
          var ErrorMsg;
          for (targetName in customValidate) {
            if (customValidate.hasOwnProperty(targetName)) {
              customValidateFns = customValidate[targetName];
              for (i = 0, len = customValidateFns.length; i < len; i++) {
                $target = $Form.find('input[name="' + targetName + '"]');
                defaultResult = customValidateFns[i]($target);
                if ($.isString(defaultResult) && defaultResult !== 'true') {
                  formValidate.setError($target, result);
                  continue;
                }
                if (defaultResult !== true) {
                  ErrorMsg = $target.attr('ErrorMsg');
                  if ($target.attr('type') === 'hidden') {
                    $target = $target.parent();
                  }
                  formValidate.setError($target, ErrorMsg);
                  validResult = false;
                  result = false;
                } else {
                  formValidate.hideErrorMessage($target);
                }
              }
            }
          }
        }

        if (result === false) {
          if (errrorback) {
            errrorback();
          }
          return;
        }

        var remoteUrlFns = formResult[formId] ? formResult[formId].remoteUrl : null;

        var targetNames = [];
        var key;
        var remoteUrlFnsPoint = 0;
        var fieldRemote = null;

        for (key in remoteUrlFns) {
          if (remoteUrlFns.hasOwnProperty(key)) {
            targetNames.push(key);
          }
        }

        function validateRound() {
          fieldRemote = remoteUrlFns[targetNames[remoteUrlFnsPoint]];
          if (fieldRemote && fieldRemote[0]) {
            var $targetsByName = $Form.find('[name="' + fieldRemote[0].fieldName + '"]');
            var data = {};
            var urlStr = '';

            var url = fieldRemote[0].url;
            if (!$.isString(url)) {
              url = url($targetsByName.map(function (_, elem) {
                return $(elem).val();
              }).get().join(','));
            }
            var urls = url.split('?');
            urlStr = urls[0];

            var paramsArray = urls[1].split('&');

            $.each(paramsArray, function (_, paramsText) {
              var params = paramsText.split('=');
              data[params[0]] = params[1];
            });

            PageCtrl.ajax({
              url: urlStr,
              cache: false,
              type: 'post',
              dataType: 'text',
              data: data,
              success: function (text) {
                var texts;
                if ($targetsByName.length > 1) {
                  texts = JSON.parse(text);
                } else {
                  texts = [text];
                }

                $.each(texts, function (index, text) {
                  var $targetByName = $($targetsByName.get(index));
                  if (text !== 'true') { //异步返回true表示验证通过，反之不通过
                    if (text !== 'false') { //如果返回不是false，那么表示返回的是错误信息
                      $targetByName.attr('ajaxErrorMsg', text);
                    }
                    formValidate.setError($targetByName, $targetByName.attr('ajaxErrorMsg'));
                    validResult = false;
                    return false;
                  }
                  formValidate.hideErrorMessage($targetByName);
                });
                if (validResult === false) {
                  return;
                }

                remoteUrlFnsPoint++;
                validateRound();
              }
            });
          } else {
            if (callback) {
              callback();
            }
          }
        }

        if (remoteUrlFns) {
          validateRound();
        } else if (validResult) {
          if (callback) {
            callback();
          }
        } else {
          if (errrorback) {
            errrorback();
          }
        }

        return result;
      },
      getValue : function($target){
        var getValue = $target.attr('getValue');
        if(getValue){ 
          var namespace = getValue.split('.');
          var fn = window;
          for(var i = 0, len = namespace.length;i < len;i++){
            fn = fn[namespace[i]];
          }
          if(fn){
            getValue = fn($target);
          }
        }else{
          getValue = $target.val();
        }
        return getValue;
      },
      /**
       * 实际验证表单域
       * */
      validateType: {
        /**
         * ajax验证默认占位
         * */
        ajax: function () {
          return true;
        },
        /**不能为空,如果和默认值相同的值，也判定是为空*/
        reqiured: function ($target) {
          var val = FormValidate.getValue($target);
          return !Validate.isEmpty(val) && val !== $target.attr('emptyValue');
        },
        required: function ($target) {
          var val = FormValidate.getValue($target);
          return !Validate.isEmpty(val) && val !== $target.attr('emptyValue');
        },
        /**是否是字符串*/
        isString: function () {
          return true;
        },
        /**
         * 是否是英文字母和数字
         */
        isLetterNum: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isLetterNum(val);
        },
        /**是否是数字*/
        isNumber: function ($target) {
          return Validate.isNumber(FormValidate.getValue($target));
        },
        /**是否是整型*/
        isInt: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isInt(FormValidate.getValue($target));
        },
        /*
         *正整数
         * */
        isPlusInt: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isPlusInt(FormValidate.getValue($target));
        },
        /*
         *负整数
         * */
        isMinusInt: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isMinusInt(FormValidate.getValue($target));
        },
        /**自然数*/
        isNaturalNum: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isNaturalNum(FormValidate.getValue($target));
        },
        /**大于等于0，且可以为小数点一位*/
        isGTEqualZeroFloatOne: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isGTEqualZeroFloatOne(FormValidate.getValue($target));
        },
        /**大于等于0，且可以为小数点二位*/
        isGTEqualZeroFloatTwo: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isGTEqualZeroFloatTwo(FormValidate.getValue($target));
        },
        /**大于等于0，且可以为小数点三位*/
        isGTEqualZeroFloatThree: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isGTEqualZeroFloatThree(FormValidate.getValue($target));
        },
        /**大于等于0，且可以为小数点四位*/
        isGTEqualZeroFloatFour: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isGTEqualZeroFloatFour(FormValidate.getValue($target));
        },
        /**验证小数位*/
        isDecimalsNum: function ($target) {
          var isDecimalsNum = parseInt($target.attr('isDecimalsNum'), 10);
          if (isDecimalsNum === 1) {
            return this.isGTEqualZeroFloatOne($target);
          }
          if (isDecimalsNum === 2) {
            return this.isGTEqualZeroFloatTwo($target);
          }
          if (isDecimalsNum === 3) {
            return this.isGTEqualZeroFloatThree($target);
          }
          if (isDecimalsNum === 4) {
            return this.isGTEqualZeroFloatFour($target);
          }
        },
        /**是否是域名*/
        isDomain: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isDomain(FormValidate.getValue($target));
        },
        /**是否是ip或域名*/
        isIpOrDomain: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isIpOrDomain(FormValidate.getValue($target));
        },
        /**是否是IP*/
        isIp: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val || val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isIp(val);
        },
        /**是否是IP,允许最后一位输入0255*/
        isIp0255: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val || val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isIp0255(val);
        },
        isIpRange: function ($target) {
          var val;
          var ips;
          var i, len;
          val = FormValidate.getValue($target);
          if (!val || val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          ips = val.split('~');
          if (ips.length !== 2 || ips[0] > ips[1]) {
            return false;
          }
          for (i = 0, len = ips.length; i < len; i++) {
            if (!Validate.isIp(ips[i])) {
              return false;
            }
          }
          return true;
        },
        /**验证多个IP,用逗号分割,*/
        isIps: function ($target) {
          var val;
          var ips;
          var i, len;
          val = FormValidate.getValue($target);
          if (!val || val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          ips = val.split(',');
          for (i = 0, len = ips.length; i < len; i++) {
            if (!Validate.isIp(ips[i])) {
              return false;
            }
          }
          return true;
        },
        /**
         * 是否是IPV6
         * */
        isIpV6: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isIpV6(val);
        },
        /**最大值*/
        max: function ($target) {
          var val = FormValidate.getValue($target);
          if (Validate.isNumber(val)) {
            return parseFloat(val) <= parseFloat($target.attr('max'));
          }
          return false;
        },
        /**最小值*/
        min: function ($target) {
          var val = FormValidate.getValue($target);
          if (Validate.isNumber(val)) {
            return parseFloat(val) >= parseFloat($target.attr('min'));
          }
          return false;
        },
        /**
         * 数值范围
         * */
        range: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (Validate.isNumber(val)) {
            var range = $target.attr('range').split('-');

            var start = range[0];
            if (start.indexOf('*') === 0) {
              start = '-' + start.substring(1);
            }
            var end = range[1];
            if (end.indexOf('*') === 0) {
              end = '-' + end.substring(1);
            }
            return (parseFloat(val) > parseFloat(start) && parseFloat(val) < parseFloat(end));
          }
          return false;
        },
        rangeEquals: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (Validate.isNumber(val)) {
            var range = $target.attr('rangeEquals').split('-');

            var start = range[0];
            if (start.indexOf('*') === 0) {
              start = '-' + start.substring(1);
            }
            var end = range[1];
            if (end.indexOf('*') === 0) {
              end = '-' + end.substring(1);
            }
            return (parseFloat(val) >= parseFloat(start) && parseFloat(val) <= parseFloat(end));
          }
          return false;
        },
        /**端口号1-65535*/
        port: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (Validate.isInt(val)) {
            val = parseInt(val, 10);
            return val >= 1 && val <= 65535;
          }
          return false;
        },
        /**字符串最大长度汉字2个字符，字母1个字符*/
        maxLen: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          var len = $.strLen(val);
          return len <= parseInt($target.attr('maxLen'), 10);
        },
        /**字符串最小长度*/
        minLength: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return val.length >= parseInt($target.attr('minLength'), 10);
        },
        /*TODO*/
        password: function () {
          return true;
        },
        /**是否Email*/
        isEmail: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isEmail(val);
        },
        /**是否手机*/
        isMobile: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isMobile(val);
        },
        /**是否固定电话*/
        isTel: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isTel(val);
        },
        /**是否固定电话或手机*/
        isTelOrMobile: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isTelOrMobile(val);
        },
        /**是否邮编*/
        isZipCode: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isZipCode(val);
        },
        /**是否身份证*/
        isCard: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isCard(val);
        },
        /**
         * 验证特殊字符，如果有返回false,否则true
         * */
        isIllegality: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isIllegality(val);
        },
        /**
         * 全部非法字符''%()/\\:?<>;|&@*#
         * */
        isIllegalityAll: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isIllegalityAll(val);
        },
        /**
         * 脚本路径特殊字符'"%\\:?<>|;&@*#"
         * */
        isScriptPathIllegality: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isScriptPathIllegality(val);
        },
        /**
         * 手机，电话，传真特殊字符''%/:?<>|;&@*#
         * */
        isTelIllegality: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isTelIllegality(val);
        },
        /**
         * 搜索特殊字符''%()?<>|#*
         */
        isSearchIllegality: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isSearchIllegality(val);
        },
        /**
         * 资源显示名称特殊字符''%()\\:?<>|;&@#*
         * */
        isResourceNameIllegality: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isResourceNameIllegality(val);
        },
        /**
         * 资源组名称特殊字符''%/\\:?<>;|&@#*
         * */
        isResourceGroupNameIllegality: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isResourceGroupNameIllegality(val);
        },
        /**
         * URL特殊字符'"%\\<>;|@#*
         */
        isURLIllegality: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isURLIllegality(val);
        },
        /**
         * EMail特殊字符''%/\\:?<>;|&#*
         */
        isEmailIllegality: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isEmailIllegality(val);
        },
        /**
         * 用户名特殊字符''%/:?<>;|&@#*
         */
        isUserNameIllegality: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isUserNameIllegality(val);
        },
        /**
         * 验证特殊字符不包括冒号，如果有返回false,否则true
         * */
        isIllegalityExcludeColon: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isIllegalityExcludeColon(val);
        },
        /**验证非法字符包括斜杠*/
        isIllegalityIncludeSlash: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isIllegalityIncludeSlash(val);
        },
        /**
         * 是否符合mac格式，返回true符合反之.
         * */
        isMac: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isMac(val);
        },
        /**子网掩码*/
        isMask: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          return Validate.isMask(val);

        },
        /**验证子网和掩码输入  ip/1-32*/
        isIpAndMas: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val || val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          var ips = val.split('/');
          if (ips.length !== 2) {
            return false;
          }
          if (!Validate.isIp0255(ips[0])) {
            return false;
          }

          if (!Validate.isInt(ips[1])) {
            return false;
          }

          var mask = +ips[1];
          if (mask < 1 || mask > 32) {
            return false;
          }



          return true;
        },
        /**是否为四位PIN码数,即四位数字*/
        isSIMPinNumber: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isSIMPinNumber(val);
        },
        /**
         * 是否子网IP地址
         */
        isSubIp: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isSubIp(val);
        },
        /**
         * 验证vlanID
         */
        isValidateVlanID: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isValidateVlanID(val);
        },
        /**
         * 全部非法字符''%()/\\:?<>;|&@*#
         * */
        isIllegalityknowledge: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isIllegalityknowledge(val);
        },
        /**
         * 全部非法字符''%()/\\:?<>;|&*# 不包含@ 为用户录入的 用户名字字段
         * */
        isIllegalityForUser: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isIllegalityForUser(val);
        },

        /**
         * 大于0的正整数
         * */
        isValidateNum: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isValidateNum(val);
        },

        /**
         * 大于等于0的正整数
         * */
        isValidatePort: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }

          return Validate.isValidatePort(val);
        },
        //日志管理
        isIllegality4Logmnt: function ($target) {
          var val = FormValidate.getValue($target);
          if (!val) {
            return true;
          }
          if (val === $target.attr(InputEmpty.EmptyVal)) {
            return true;
          }
          return Validate.isIllegality4Logmnt(val);
        }

      }

    };
  }());

  window.FormValidate = FormValidate;

  /**
   * 文本框默认值，当文本框值为空时，文本框显示默认值，当获得焦点时去掉默认值
   * */
  var InputEmpty = {
    EmptyVal: 'emptyValue',
    init: function (id) {
      var $input = null;
      if ($.isString(id)) {
        $input = $('#' + id);
      } else {
        $input = $(id);
      }
      if ($input.attr('isBindinput')) {
        return;
      }

      InputEmpty.setEmptyVal($input);

      $input.bind('focus', function () {
        InputEmpty.clearEmptyVal($(this));
      }).bind('blur', function () {
        InputEmpty.setEmptyVal($(this));
      }).attr('isBindinput', 'true');
    },
    setEmptyVal: function ($input) {
      $input = $.getJQueryDom($input);
      if (!$input.val()) {
        $input.val($input.attr(InputEmpty.EmptyVal)).addClass('default');
      }
    },
    clearEmptyVal: function ($input) {
      var emptyValue = $input.attr(InputEmpty.EmptyVal);
      if (emptyValue === $input.val()) {
        $input.val('').removeClass('default');
      }
    }
  };

  window.InputEmpty = InputEmpty;

}());;/*global $*/
(function () {
  'use strict';

  var Validate = window.Validate = {
    /**
     * 判断是否为空
     */
    isEmpty: function (val) {
      val = $.trim(val);
      if (!val || '' === val) {
        return true;
      } else {
        return false;
      }
    },
    /**
     * 判断是否为数字
     */
    isNumber: function (val) {
      if (isNaN(val)) return false;
      else if ($.isString(val)) {
        var trim = $.trim(val);
        if (trim !== val) return false;
        if (trim.charAt(0) === '+') return false;
      }
      return true;
    },
    /**
     * 是否仅包含英文字母及数字
     */
    isLetterNum: function (val) {
      return /^[A-Za-z0-9]+$/.test(val);
    },
    /**
     * 判断是否整数
     */
    isInt: function (val) {
      return /^-?\d+$/.test(val);
    },
    /**
     *判断是否正整数
     */
    isPlusInt: function (val) {
      return /^[1-9]\d*$/.test(val);
    },
    /**
     * 判断是否负整数
     */
    isMinusInt: function (val) {
      return /^-[0-9]*[1-9][0-9]*$/.test(val);
    },
    /**是否浮点数*/
    isFloat: function (val) {
      return /^(-?\d+)(\.\d+)?$/.test(val);
    },
    /**是否正浮点数*/
    isPlusFloat: function (val) {
      return /^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test(val);
    },
    /**是否负浮点数*/
    isMinusFloat: function (val) {
      return /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/.test(val);
    },
    /**是否符合域名*/
    isDomain: function (val) {
      return /^([a-z0-9-]{1,}.)?[a-z0-9-]{2,}.([a-z0-9-]{1,}.)?[a-z0-9]{2,}$/.test(val);
    },
    /**符合域名或IP*/
    isIpOrDomain: function (val) {
      var MobileFlag = true;
      var TelFlag = true;
      if (!Validate.isIp(val)) {
        MobileFlag = false;
      }
      if (!Validate.isIpV6(val)) {
        MobileFlag = false;
      }
      if (!Validate.isDomain(val)) {
        TelFlag = false;
      }
      return (MobileFlag || TelFlag);
    },
    /**是否IP地址*/
    isIp: function (val) {
      return /^(([01]?[\d]{1,2})|(2[0-4][\d])|(25[0-5]))(\.(([01]?[\d]{1,2})|(2[0-4][\d])|(25[0-5]))){2}(\.(([1-9]|[1-9][\d]{1}|1[\d]{2})|2[0-4][\d]|(25[0-4])))$/.test(val);
    },
    /**是否IP地址,允许输入0，255*/
    isIp0255: function (val) {
      if (val == '0.0.0.0') {
        return true;
      }
      return /^(([01]?[\d]{1,2})|(2[0-4][\d])|(25[0-5]))(\.(([01]?[\d]{1,2})|(2[0-4][\d])|(25[0-5]))){2}(\.(([1-9]|[1-9][\d]{1}|1[\d]{2})|2[0-5][\d]|0|(25[0-5])))$/.test(val);
    },
    isIpV6: function (val) {
      return /^([\da-fA-F]{1,4}:){7}[\da-fA-F]{1,4}$|^:((:[\da-fA-F]{1,4}){1,6}|:)$|^[\da-fA-F]{1,4}:((:[\da-fA-F]{1,4}){1,5}|:)$|^([\da-fA-F]{1,4}:){2}((:[\da-fA-F]{1,4}){1,4}|:)$|^([\da-fA-F]{1,4}:){3}((:[\da-fA-F]{1,4}){1,3}|:)$|^([\da-fA-F]{1,4}:){4}((:[\da-fA-F]{1,4}){1,2}|:)$|^([\da-fA-F]{1,4}:){5}:([\da-fA-F]{1,4})?$|^([\da-fA-F]{1,4}:){6}:$/.test(val);
    },
    /**是否Email*/
    isEmail: function (val) {
      return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(val);
    },
    /**是否手机*/
    isMobile: function (val) {
      //         return /^(13|15)[0-9]{9}$/.test(val);
      return /^([+]{0,1}(\d){1,4}){0,1}((13|15|18)[0-9]{9})$/.test(val);
    },
    /**是否是自然数*/
    isNaturalNum: function (val) {
      return /^[0-9]*$/.test(val);
    },
    /**大于等于0，且可以为小数点一位*/
    isGTEqualZeroFloatOne: function (val) {
      return /^-?[0-9]*[\.]?[0-9]?$/.test(val);
    },
    /**大于等于0，且可以为小数点二位*/
    isGTEqualZeroFloatTwo: function (val) {
      return /^-?[0-9]*[\.]?[0-9]{1,2}?$/.test(val);
    },
    /**大于等于0，且可以为小数点三位*/
    isGTEqualZeroFloatThree: function (val) {
      return /^-?[0-9]*[\.]?[0-9]{1,3}?$/.test(val);
    },
    /**大于等于0，且可以为小数点四位*/
    isGTEqualZeroFloatFour: function (val) {
      return /^-?[0-9]*[\.]?[0-9]{1,4}?$/.test(val);
    },
    /**验证手机和固定电话*/
    isTelOrMobile: function (val) {
      var MobileFlag = true;
      var TelFlag = true;
      if (!Validate.isMobile(val)) {
        MobileFlag = false;
      }
      if (!Validate.isTel(val)) {
        TelFlag = false;
      }
      return (MobileFlag || TelFlag);
    },
    /**是否固定电话*/
    isTel: function (val) {
      return /^[0-9\(\)+-]*$/.test(val);
      //         return /^d{3}-d{8}|d{4}-d{7}$/.test(val);
    },
    /**是否邮编*/
    isZipCode: function (val) {
      return /^\\d{6}$/.test(val);
    },
    /**是否身份证*/
    isCard: function (val) {
      return /^[1-9]([0-9]{14}|[0-9]{17})$/.test(val);
    },
    /**
     * 其他普通文本特殊字符'%/\\:?<>|;&@#*"
     * */
    isIllegality: function (val) {
      return 'null' != val && /^$|^[^'\/\"%\\\\:?<>|;&@#*]+$/.test(val);
    },
    /**
     * 全部非法字符'"%()/\\:?<>;|&@*#
     * */
    isIllegalityAll: function (val) {
      return 'null' != val && /^$|^[^'\/\"%\(\)\\\\:?<>|;&@#*]+$/.test(val);
    },
    /**
     * 脚本路径特殊字符'"%\\:?<>|;&@*#"
     * */
    isScriptPathIllegality: function (val) {
      return 'null' != val && /^$|^[^'\"%\\\\:?<>|;&@#*]+$/.test(val);
    },
    /**
     * 手机，电话，传真特殊字符'"%/:?<>|;&@*#
     * */
    isTelIllegality: function (val) {
      return 'null' != val && /^$|^[^'\"%\/:?<>|;&@#*]+$/.test(val);
    },
    /**
     * 搜索特殊字符'"%_<>\&
     */
    isSearchIllegality: function (val) {
      return 'null' != val && /^$|^[^'\"%\\\\<>_&]+$/.test(val);
    },
    /**
     * 资源显示名称特殊字符'"<>
     * */
    isResourceNameIllegality: function (val) {
      return 'null' != val && /^$|^[^'\"<>]+$/.test(val);
    },
    /**
     * 资源组名称特殊字符'"%/\\:?<>;|&@#*
     * */
    isResourceGroupNameIllegality: function (val) {
      return 'null' != val && /^$|^[^'\"\/\\\\:%?<>|;&@#*]+$/.test(val);
    },
    /**
     * URL特殊字符'"%\\<>;|@#*
     */
    isURLIllegality: function (val) {
      return 'null' != val && /^$|^[^'\"\\\\%<>|;@#*]+$/.test(val);
    },
    /**
     * EMail特殊字符'"%/\\:?<>;|&#*
     */
    isEmailIllegality: function (val) {
      return 'null' != val && /^$|^[^'\"\/\\\\:?<>|;&#*]+$/.test(val);
    },
    /**
     * 用户名特殊字符'"%/:?<>;|&@#*
     * */
    isUserNameIllegality: function (val) {
      return 'null' != val && /^$|^[^'\"\/:?<>|;&@#*]+$/.test(val);
    },
    /**
     * 是否包含非法字符包括正反斜杠'\"%\\:?<>|;&$#*"
     * */
    isIllegalityIncludeSlash: function (val) {
      return 'null' != val && /^$|^[^'\/\"%\\\\:?<>|;&@#*]+$/.test(val);
    },
    /**
     * 是否包含非法字符'\"%\\?<>|;&$#*"   不包括冒号
     * */
    isIllegalityExcludeColon: function (val) {
      return 'null' != val && /^$|^[^'\"%\\?<>|;&@#*]+$/.test(val);
    },
    /**
     * 是否符合mac地址格式
     * */
    isMac: function (val) {
      return /^[a-fA-F0-9]{2}[:-][a-fA-F0-9]{2}[:-][a-fA-F0-9]{2}[:-][a-fA-F0-9]{2}[:-][a-fA-F0-9]{2}[:-][a-fA-F0-9]{2}$/.test(val);
    },
    /**
     * 全部非法字符'"%()/\\:?<>;|&@*#~!。.
     * */
    isIllegalityknowledge: function (val) {
      return 'null' != val && /^$|^[^'\/\"%\(\)\\\\:?<>|;&@#*~!。\\\\.]+$/.test(val);
    },
    /**  
     * 函数名： validateMask
     *   函数功能： 验证子网掩码的合法性
     *   函数作者： 236F(fuwei236#gmail.com)
     * 传入参数： MaskStr:点分十进制的子网掩码(如：255.255.255.192)
     *   主调函数：
     * 调用函数： _checkIput_fomartIP(ip)
     * 返回值：   true:   MaskStr为合法子网掩码
     *      false: MaskStr为非法子网掩码
     **/
    isMask: function (MaskStr) {
      /* 有效性校验 */
      var IPPattern = /^\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}$/;
      if (!IPPattern.test(MaskStr)) return false;

      /* 检查域值 */
      var IPArray = MaskStr.split('.');
      var ip1 = parseInt(IPArray[0]);
      var ip2 = parseInt(IPArray[1]);
      var ip3 = parseInt(IPArray[2]);
      var ip4 = parseInt(IPArray[3]);
      if (ip1 < 0 || ip1 > 255 /* 每个域值范围0-255 */ || ip2 < 0 || ip2 > 255 || ip3 < 0 || ip3 > 255 || ip4 < 0 || ip4 > 255) {
        return false;
      }
      /* 检查二进制值是否合法 */
      //拼接二进制字符串
      var ip_binary = (ip1 + 256).toString(2).substring(1) + (ip2 + 256).toString(2).substring(1) + (ip3 + 256).toString(2).substring(1) + (ip4 + 256).toString(2).substring(1);
      if (-1 != ip_binary.indexOf('01')) return false;
      return true;
    },
    /**
     * 四位的数字验证
     */
    isSIMPinNumber: function (val) {
      var patrn = /^[0-9]{4}$/;
      return patrn.test(val);
    },
    /**是否子网IP地址*/
    isSubIp: function (val) {
      return /^(([01]?[\d]{1,2})|(2[0-4][\d])|(25[0-5]))(\.(([01]?[\d]{1,2})|(2[0-4][\d])|(25[0-5]))){2}(\.(([0-9]|[1-9][\d]{1}|1[\d]{2})|2[0-4][\d]|(25[0-4])))$/.test(val);
    },
    /**
     *判断是否合法的VLAN ID
     */
    isValidateVlanID: function (val) {
      if (/^[1-9]*[1-9][0-9]*$/.test(val)) {
        if (parseInt(val) >= 1 && parseInt(val) <= 4094) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    },
    /**
     * 全部非法字符'"%()/\\:?<>;|&*# 不包含@ 为用户录入的 用户名字字段
     * */
    isIllegalityForUser: function (val) {
      return 'null' != val && /^$|^[^'\/\"%\(\)\\\\:?<>|;&#*]+$/.test(val);
    },
    /**大于0的正整数*/
    isValidateNum: function (val) {
      // 赵鑫田修改于2015-03-04
      // -------------------------------------------------------
      // 应该是：数字组成的字符串，并且首位不是0
      // 而不是：1-9组成的字符串
      // 感谢刘通提供的解决方案
      // -------------------------------------------------------
      // return /^[1-9]*$/.test(val);
      return /^[1-9]\d*$/.test(val);
      // =======================================================
    },
    /**大于等于0的正整数*/
    isValidatePort: function (val) {
      return /^[0-9]{1,5}$/.test(val);
    },
    //日志管理验证
    isIllegality4Logmnt: function (val) {
      return /^[^\\;\+\$&\^*()\|\?\[\]:"\/!~\{\}]+$/.test(val);
    }
  };


}());;/*
 * 元素添加器
 *conf{
	applyId:将组件渲染到指定ID节点上
	id:"组件ID"
    createType:添加项类型【string,ip,ipBlock,DIRECT,SNMPCONF】
 }
 */


var ItemBox = function () {

	return {
		init: init_fn,
		addItem: addItem_fn,
		deleteSelectedItem: deleteSelectedItem_fn,
		getItemJquery: getItemJquery_fn,
		getItemCount: getItemCount_fn,
		getValue: getValue_fn,
		setErrorByIndex: setErrorByIndex_fn,
		layout: layout_fn
	}

	function layout_fn(conf) {
		var $dom = $("#" + conf.id);
		var width = $dom.parent()
			.width();

		var $ul0 = $dom.children("ul:eq(0)");
		var $ul1 = $dom.children("ul:eq(1)");
		$ul0.width(width - 81 - 6);


	}

	/*
	 * 初始化数据
	 * 如果有默认值，调用添加元素方法，添加
	 * */
	function _initData(conf) {
		var self = this;
		var data = conf.data || [];
		for (var i = 0, len = data.length; i < len; i++) {
			addItem_fn(data[i], conf);
		}
	}

	/*
	 * 组件结构
	 * */
	function _dom(conf) {
		var dom = [];
		dom.push('<div id="' + conf.id + '" class="list-panel button-panel" createType="' + conf.createType + '" custom="' +
			!!conf.custom + '">');
		dom.push('<ul class="list-content" compType="content"></ul>');
		dom.push('<ul class="list-buttons" compType="ctrlBtns">');
		dom.push(
			'<li><a class="btn_body" compType="addBtn"><div class="btn_l"><div class="btn_r"><div class="btn_m"> <span class="ico ico_add"></span> <span class="text">' +
			(window.S_BTNADD || "S_BTNADD") + '</span> <span class="pull"></span> </div></div></div></a></li>');
		dom.push(
			'<li><a class="btn_body" compType="deleteBtn"><div class="btn_l"><div class="btn_r"><div class="btn_m"> <span class="ico ico_delete"></span> <span class="text">' +
			(window.S_BTNDELETE || "S_BTNDELETE") + '</span> <span class="pull"></span> </div></div></div></a></li>');
		dom.push('</ul>');
		dom.push('</div>');
		return dom.join("");
	}

	/*
	 * 绑定事件
	 * */
	function _bindEvent(conf) {
		var _btns = _getCtrlBtn(conf.id);
		_btns.addBtn.bind("click", {
			conf: conf
		}, _addItemListener);
		_btns.deleteBtn.bind("click", {
			conf: conf
		}, _deleteItemListener);
	}

	/*
	 * 删除元素监听回调
	 * */
	function _deleteItemListener(event) {
		var conf = event.data.conf;
		deleteSelectedItem_fn(conf.id);
	}

	/*
	 * 添加元素监听回调
	 * */
	function _addItemListener(event) {
		var conf = event.data.conf;
		var createType = conf.createType;
		addItem_fn(null, conf);
	}


	/*
	 * 获得操控按钮
	 * @param {String} 组件ID
	 * @return {
	 * 		addBtn:添加按钮jdom
	 *      deleteBtn:删除按钮jdom
	 * }
	 * */
	function _getCtrlBtn(id) {
		var $lis = $.getJQueryDom(id)
			.children('ul[compType="ctrlBtns"]')
			.children();
		return {
			addBtn: $lis.children('a[compType="addBtn"]'),
			deleteBtn: $lis.children('a[compType="deleteBtn"]')
		};
	}

	/*
	 * 获得成员项容器
	 * @param id {String} 组件ID
	 * @retur {jdom}
	 * */
	function _getContent(id) {
		return $.getJQueryDom(id)
			.children('ul[compType="content"]');
	}

	/*
	 * 创建元素dom
	 * @param creater 元素类型创建器
	 * @param index 元素索引
	 * @param data 初始化数据
	 * @param conf 配置
	 * */
	function _createItemDom(creater, index, data, conf) {
		if (!creater || !creater.init) {
			new Error("元素类型错误");
			return;
		}
		var $item = $('<li><input compType="ItemBoxFlag" type="checkbox"/></li>');
		creater.init({
			index: index,
			randomId: conf.randomId,
			outer: $item,
			value: data,
			itemboxConf: conf
		});
		if (conf.custom) {
			var $customParent = $('<div></div>')
				.attr('type', 'custom')
				.css('display', 'inline-block')
				.appendTo($item);
			if (_.isFunction(conf.custom.html)) {
				$customParent.append(conf.custom.html(data, index, conf));
			} else {
				$customParent.append(conf.custom.html);
			}
		}
		return $item;
	}

	/**设置元素错误提示*/
	function _setError($item, isError) {
		if (isError) {
			$item.addClass("warning");
		} else {
			$item.removeClass("warning");
		}
	}

	/*
	 * 获得行标识的复选框对象集合
	 * @param id 组件ID
	 * @return {Array} jdom集合
	 *
	 * */
	function _getRowFlag(id) {
		var $content = _getContent(id);
		return $content.find('input[type="checkbox"][compType="ItemBoxFlag"]');
	}

	/**
	 * 添加新元素
	 * @param data 初始值
	 * @param conf 配置
	 * */
	function addItem_fn(data, conf) {
		var id = conf.id;
		var length = getItemCount_fn(id);
		if (conf.count == length) {
			if (conf.countMsg) {
				RiilAlert.info(conf.countMsg);
			}

			return;
		}
		var randomId = new Date()
			.getTime();
		var createType = conf.createType;
		var self = this;
		var $content = _getContent(id);
		conf.randomId = randomId;
		$content.append(_createItemDom(
			ItemBox.item[createType],
			length,
			data,
			conf));
		CustomCheck.batchInit({
			outer: $content
		});
		if (ItemBox.item[createType].bindEvent) {
			ItemBox.item[createType].bindEvent({
				index: length - 1,
				value: data,
				randomId: randomId,
				config: conf
			});
		}
	}

	/**
	 * 删除选中的元素
	 * @param 组件ID
	 */
	function deleteSelectedItem_fn(id) {
		var $flags = _getRowFlag(id);
		var $checkeds = $flags.filter(":checked");
		for (var i = 0, len = $checkeds.length; i < len; i++) {
			var $parent = $($checkeds[i])
				.parent()
				.parent();
			$parent.find("*")
				.unbind();
			$parent.remove();
		}
	}

	/**
	 * 获得元素的Jdom集合
	 * @param 组件ID
	 * @return
	 * */
	function getItemJquery_fn(id) {
		return _getContent(id)
			.children();
	}

	/**
	 * 获得元素个数
	 * @param 组件ID
	 * @return {Number} 元素个数
	 * */
	function getItemCount_fn(id) {
		return getItemJquery_fn(id)
			.length;
	}

	/**
	 * 获得值
	 * 如果返回false,表示输入有误
	 * @param {String} 组件Id
	 * */
	function getValue_fn(id, conf) {
		var $items = getItemJquery_fn(id);
		var createType = $.getJQueryDom(id)
			.attr("createType");
		var ItemBoxItem = ItemBox.item;
		var value = [];
		var flag = true;

		for (var i = 0, len = $items.length; i < len; i++) {
			var val = ItemBoxItem[createType].getValue($items[i], conf);
			// TODO $items === 'li'，应该处理li.children
			var $custom = $($items[i])
				.find('[type="custom"]');

			if (val !== false && $custom.length > 0) {
				val = [val];
				if (conf.custom && conf.custom.value) {
					if (_.isFunction(conf.custom.value)) {
						val.push(conf.custom.value($custom.children()));
					} else {
						val.push(conf.custom.value);
					}
				}
			}

			if (val === false) {
				flag = false;
				_setError($($items[i]), true);
			} else {
				_setError($($items[i]), false);
			}
			value.push(val);
		}
		if (flag === false) {
			return flag;
		}
		return value;
	}

	function setErrorByIndex_fn(id, index) {
		if (!$.isArray(index)) {
			index = [index];
		}
		var items = getItemJquery_fn(id);
		for (var i = 0; i < index.length; i++) {
			_setError($(items[index[i]]), true);
		}
	}


	/**
	 * 初始化
	 *
	 * */
	function init_fn(conf) {
		$.getJQueryDom(conf.applyId)
			.append(_dom(conf));
		_initData(conf);
		_bindEvent(conf);
		layout_fn(conf);
	}

}();

ItemBox.item = {};
;/**针对无线option*/
ItemBox.item.DHCPOPTION = function(Item){
	
	return {
		init : init_fn,
		getValue : getValue_fn
	}

	function _bindEvent ($outer){
		$.onlyNumber({
			input : $outer.find('input[inputType="stringInput"]')
		});
	}
	
	function init_fn (conf) {
		var $outer = $(conf.outer);
		$outer.append("<span>OPTION</span>");
		
		var $wrapper = $($.createDomStr({
			tagName:"div",
			attr:{
				"class" : "option"
			}
		}));
		
		
		
		Item.string.init({
			outer : $wrapper,
			value : conf.value ? conf.value.code : ""
		});

		
		$outer.append($wrapper).append("<span>IP</span>");
		ItemBox.item.ip.init({
			outer:$outer,
			value:conf.value ? conf.value.ip : null
		});
		_bindEvent($outer);			
	}
	

	
	
	function getValue_fn (wrapper){
		var ipItem = Item.ip;
		var $wrapper = $(wrapper);
		
		var code = $(wrapper).find('input[inputType="stringInput"]').val();
		if(code === ""){
			return false;
		}
		if(!Validate.isPlusInt(code)){
			return false;
		}
		code = +code;
		if(code <0 || code >254){
			return false;
		}
		
		
		var ipVal = ipItem.getValue(ipItem.getIpWrapper($wrapper));
		if(ipVal === false){
			return ipVal;
		}
		
		
		
		return {
				code : code,
				ip :  ipVal
			};
	}
	
}(ItemBox.item);;/**
 * 针对无线直通地址DIRECT
 * 
 * 
 * */

ItemBox.item.DIRECT = function(Item){
	return {
		init : init_fn,
		getValue : getValue_fn
	}
	
	
	function init_fn (conf){
		var $outer = $(conf.outer);
		var itemboxConf = conf.itemboxConf
		var labels = itemboxConf.labels;
		
		$outer.append("<span>" + labels[0] + "</span>");
		
		Item.ip.init({
			outer : $outer,
			value : conf.value ? conf.value.ip : null
		});
		
		var arp = conf.value ? conf.value.arp : false;
		
		$outer.append('<span><input type="checkbox" ' + ( arp ? 'checked="checked"' : '')+' >');
		$outer.append(labels[1] + "</span>");		
	}
	
	function getValue_fn (wrapper){
		var ipItem = ItemBox.item.ip;
		var $wrapper = $(wrapper);		
		return {
				ip : ipItem.getValue(ipItem.getIpWrapper($wrapper)),
				arp : $(wrapper).find('input[type="checkbox"]:checked').length > 0 ? true : false
			};
	}	
	
}(ItemBox.item);


;/**
 * 端口
 * */
ItemBox.item.Port = function(Item){
	
	return {
		init : init_fn,
		getValue : getValue_fn
	}
	
	function _bindEvent ($outer){
		$.onlyNumber({
			input : $outer.find('input[type="text"]')
		});
	}
	
	function _initData(data){
		return data ? data.split("-") : null;
	}
	
	function init_fn (conf) {
		
		var $outer = $(conf.outer);
		var itemboxConf = conf.itemboxConf
		var labels = itemboxConf.labels;
		var value = _initData(conf.value);
		
		Item.string.init({
			outer : $outer,
			value : value ? value[0] : ""
		});
		
		$outer.append("<span>-</span>");
		
		Item.string.init({
			outer : $outer,
			value : value ? value[1] : ""
		});		
		_bindEvent($outer);
	}
	
	function getValue_fn (wrapper) {
		var $inputs = $.getJQueryDom(wrapper).find('input[type="text"]');
		
		var startPort = $inputs[0].value;
		var endPort = $inputs[1].value;
		
		if(startPort === "" || parseInt(startPort) < 0 || parseInt(startPort) > 65535){
			return false;
		}
		
		if(endPort === ""){
			return startPort;
		}
		
		if(parseInt(endPort) > 65535){
			return false;
		}
		
		if(startPort > endPort){
			return false;
		}
		return startPort + "-" + endPort;
		
	}
	
}(ItemBox.item)

;/**
 * 针对无线SNMP配置
 * 
 * 
 * 
 * */
ItemBox.item.SNMPCONF = function(Item){
	
	return {
		init : init_fn,
		getValue : getValue_fn
	}
	
	
	function init_fn (conf){
		var self = this;		
		var $outer = $(conf.outer);
		var itemboxConf = conf.itemboxConf
		var labels = itemboxConf.labels;
		
		$outer.append(labels[0]);
		
		Item.ip.init({
			outer : $outer,
			value : conf.value ? conf.value.ip : null,
			listeners : {
				mouseUpEnd : function () {
					if(listeners.ipBlockEnd){  //结束ip输入结束
						listeners.ipBlockEnd();
					}
				}
			}
		});
		
		$outer.append("<span>" + labels[1] + "</span>");
		Item.string.init({
			outer : $outer,
			value : conf.value ? conf.value.community : ""
		});
	
	}
	
	/**
	 * 获取值
	 * */
	function getValue_fn (wrapper){
		var ipItem = Item.ip;
		var inputItem = Item.string;
		
		var ipVal = ipItem.getValue(ipItem.getIpWrapper(wrapper));
		if(ipVal === false){
			return ipVal;
		}
		
		var communityVal = inputItem.getValue(wrapper);
		if(communityVal === false){
			return communityVal;
		}
		return {community : communityVal, ip : ipVal};
	}
}(ItemBox.item);


;/**IP选项
 * @param conf.outer dom ip组件外层容器,生成ip组件，添加到该dom下 * 
 * */
ItemBox.item.ip = function(){
	return {
		init : init_fn,
		render : render_fn,
		/**
		 * 获得ip第一位文本框jdom
		 * @param ip组件外层容器 [span]
		 * */
		getFirstInput : getFirstInput_fn,
		/**
		 * 获得IP 4位文本框
		 * @param ip组件外层容器 [span]
		 * */
		getIpItems : getIpItems_fn,
		/**
		 * 获得ip组件外围层jdom [span]
		 * @param $outer 组件选项外层 [li]
		 * */
		getIpWrapper : getIpWrapper_fn,
		/**
		 * 获得IP值
		 * 验证输入的IP是否合法，如果不合法，返回false
		 * */
		getValue : getValue_fn,
		/**
		 * 控制ip是否可以输入
		 * @param ip组件最外层 [span]
		 * @param avaiable {boolean} 是否可以输入 
		 * */
		availability : availability_fn
	}
	
	function _initData (data){
		return data.split(".");
	}
	
	function _bindEvent ($wrapper,conf){
		var self = this;		
		var $inputs = getIpItems_fn($wrapper);
		
		$.onlyNumber({
			input:$inputs
		});
		$inputs.bind("keyup",{conf:conf},_checkingMouseup);		
	}
	
	function _nextInputFocus(index,$inputs,mouseUpEnd){
		$inputs.filter(":eq(" + index + ")").focus().select().attr("autonext","autonext");
		if(index == 4 && mouseUpEnd){
			mouseUpEnd();
		}
	}

	function _prevInputFocus(index,$inputs,mouseUpEnd){
		$inputs.filter(":eq(" + index + ")").focus().select().attr("autonext","autonext");
	}
	
	/**
	 * 检测ip框输入内容合法性
	 * 检测数值在0-255之间
	 * */
	function _checkingMouseup (event){
		if($(this).attr("autonext")){
			$(this).removeAttr("autonext");
			return false;
		}
		
		var conf = event.data.conf;
		var listeners = conf.listeners || {};
		var $inputs  = getIpItems_fn($(this).parent());
		var index = $inputs.index(this);
		var _value = $.trim(this.value);
		if(event.keyCode === 8){
			if(_value == ""){
				_prevInputFocus(index - 1, $inputs);
				return;
			}			
		}

		if(event.keyCode === 190 || event.keyCode === 110){
			_nextInputFocus(index + 1, $inputs, listeners.mouseUpEnd);
			return false;
		}

		if(_value == ""){
			return false;
		}
		var _ipNum = +_value;
		if(_ipNum > 255 || _ipNum < 0){
			this.value = "";
			return false;
		}
		


		
		if(_value.length == 3){
			_nextInputFocus(index + 1, $inputs, listeners.mouseUpEnd);
		}
		
	}
	
	
	function init_fn (conf){
		var self = this;
		conf.values = _initData(conf.value || "");
		var $ipWrapper = $(self.render(conf));		
		$(conf.outer).append($ipWrapper);
		_bindEvent($ipWrapper,conf);	
	}
	
	function render_fn(conf){
		var _dom = [];
		var param = conf.param || {};
		var values = conf.values || [];
		for(var i=0;i<4;i++){
			_dom.push($.createDomStr({
				tagName:"input",
				attr:{
					type : "text",
					inputType : "ipInput",
					maxLength : "3",
					value : values[i] || ""
				}
			}));
		}
		param["class"] = "ip";
		param["compType"] = "ipWrapper";
		param["index"] = conf.index;
		

		return 	$.createDomStr({
			tagName:"div",
			attr:param,
			content:_dom.join(".")
		});
	}	
	

	function getFirstInput_fn($wrapper){
		return getIpItems_fn($wrapper).filter(":eq(0)");
	}

	function getIpItems_fn ($wrapper){
		return $.getJQueryDom($wrapper).find('input[inputType="ipInput"]');
	}

	function getIpWrapper_fn (outer){
		return $.getJQueryDom(outer).find('div[compType="ipWrapper"]');
	}

	function getValue_fn ($wrapper,validateType){
		validateType  = validateType ||  {};
		var inputs = getIpItems_fn($wrapper);
		var value = [];
		var val = "";
		for(var i = 0; i < 4 ; i++){
			val = inputs[i].value;
			if(val == "" && value.length !=0){
				return "";
			}
			value.push(val);
		}
		
		var ipVal = value.join(".");
		
		if(validateType.ip0255 ? Validate.isIp0255(ipVal) : Validate.isIp(ipVal)){
			return ipVal;
		}else{
			return false;
		}
	}	

	function availability_fn ($wrapper,avaiable){
		var self = this;
		var $ipInputs = self.getIpItems($wrapper);
		if(avaiable){
			$ipInputs.removeAttr("disabled");
		}else{
			$ipInputs.attr("disabled","disbaled");	
		}
	}

}();;/**
 * IP段
 * 
 * */
ItemBox.item.ipBlock = function(IpItem){
	return {
		init : init_fn,
		/**
		 * 获得IP段值
		 * 如果IP段开始位IP输入不正确，或者开始位大于结束位，则返回false
		 * 如果只输入IP段开始段，则返回开始位，视同输入IP
		 * IP段返回格式为 开始段 - 结束段
		 * @param wrapper 组件外层元素
		 * */
		getValue : getValue_fn		
	}
	
	
	
	function _initData (data) {
		return data ? data.split("-") : null;
	}
	
	function init_fn (conf) {
		conf = conf || {};
		var listeners = conf.listeners || {};
		var self = this;
		var values = _initData(conf.value || "");

		var $outer = $(conf.outer);
		IpItem.init({
			outer : $outer,
			value : values ? values[0] : null,
			listeners : {
				mouseUpEnd : function () {
					nextIpFocus($outer);
				}
			}
		});
		
		$outer.append("-");
		
		IpItem.init({
			outer : $outer,
			value : values ? values[1] : null,
			listeners : {
				mouseUpEnd : function (){
					if(listeners.ipBlockEnd){  //结束ip输入结束
						listeners.ipBlockEnd();
					}
				}
			}
		});
	}	
	
	//验证IP段在相同ip段前三位
	function checkSomeIpBlock(startIp,endIp){
		var starts = startIp.split("\.");
		var ends = endIp.split("\.");
		for(var i = 0; i < 3; i++){
			if(starts[i] != ends[i]){
				return false;
			}
		}
		return true;
	}

	function nextIpFocus($outer){
		var self = this;
		IpItem.getFirstInput(_getEndIp($outer)).focus();//结束IP第一位获得焦点		
	}
	
	function _getStartIp ($outer){
		return $outer.find('div[compType="ipWrapper"]:eq(0)');
	}
	
	function _getEndIp ($outer){
		return $outer.find('div[compType="ipWrapper"]:eq(1)');
	}
	
	function getValue_fn (wrapper,validateType){
		var self = this;
		var $wrapper = $.getJQueryDom(wrapper);
		var startVal = IpItem.getValue(_getStartIp($wrapper),validateType);

		if(startVal === false || startVal === ""){
			return false;
		}
		var endVal = IpItem.getValue(_getEndIp($wrapper),validateType);
		if(endVal === false){
			return false;
		}
		if(endVal === ""){
			return startVal
		}



		if(startVal == endVal){
			return false;
		}

		var startIps = startVal.split("\.");
		var endIps = endVal.split("\.");
		for(var i = 0; i < 4; i++){
			if(parseInt(startIps[i]) > parseInt(endIps[i])){
				return false;
			}
		}
		
		if(validateType){
			if(validateType.sameBlock){
				var resultVal = false;
				if($.isFunction(validateType.sameBlock)){
					resultVal = validateType.sameBlock(startVal, endVal);
				}else{
					resultVal = checkSomeIpBlock(startVal, endVal);	
				}				
				if(resultVal === false){
					return false;
				}
			}
		}


		
		return startVal + "-" + endVal;
	}	
	
}(ItemBox.item.ip);

;
/**
 * oui格式
 * */
ItemBox.item.mac = function(){
	return {
		init : init_fn,
		getValue : getValue_fn
	}
	
	function init_fn (conf){
		var self = this;		
		var $outer = $(conf.outer).append(_render(conf));
		_bindEvent($outer);
	}
	
	

	function _bindEvent($outer){
		var inputs = $outer.find('input[inputType="stringInput"]');
		inputs.bind("keydown",function(event){
			var $input = $(this);
			var keyCode = event.keyCode;

			//文本框没有选中东西,文本框值已经有两位，跳转到下一个文本框，选中下一个文本框的内容
			if(!$input.attr("selecting") && $input.val().length == 2 && keyCode != 8 && keyCode != 38 && keyCode != 36 && keyCode != 37 && keyCode != 39){
				$input.nextAll(":input:first").focus().select();
				return false;
			}else if($input.attr("selecting") && $input.val().length == 2){
							
			}else if($input.val().length == 2 && keyCode != 8 && keyCode != 38 && keyCode != 36 && keyCode != 37 && keyCode != 39 && keyCode != 40){
				$input.nextAll(":input:first").focus().select();
				$input.removeAttr("selecting");
				return false;
			}
			$input.removeAttr("selecting");
			if(keyCode >= 48 && keyCode <=57 ||
					keyCode >=96 && keyCode <=105 || keyCode >=65 && keyCode <=90 || keyCode == 8  || keyCode == 110 || keyCode == 46 || keyCode == 9 || keyCode == 144 || keyCode >=36 && keyCode <= 39){
					return;
			}else{
				return false;
			}

			if($input.val().length == 2){
				//$input.nextAll(":input:first").focus().select();
			}
		}).bind("select", function(){
			$(this).attr("selecting",true);
		}).bind("keyup", function(event){
			var $input = $(this);
			var keyCode = event.keyCode;
			if($input.val().length == 2){
				$input.nextAll(":input:first").focus().select();
			}
			if($input.val() == ''){
				$input.prevAll(":input:first").focus();
			}
		});
	}
	
	/**
	 * conf.value
	 * 
	 * */
	function _render (conf){
		var _inputDom  = [];		
		var value = conf.value ? conf.value.split(":") : "";
		for(var i = 0; i < 6; i++){
			_inputDom.push(
				$.createDomStr({
					tagName:"input",
					attr:{
						type : "text",						
						inputType : "stringInput",
						"class" : "blueinput inputbg",
						maxLength :2,
						value : value[i] || ""
					},
					style:{
						width : "25px"
					}
				})	
			);
		}		
		return _inputDom.join("<span>:</span>");		
	}
	
	function getValue_fn(wrapper,validateType){
		var inputs = $.getJQueryDom(wrapper).find('input[inputType="stringInput"]');
		var val = [];
		for(var i = 0; i< inputs.length; i++){
			var value = inputs[i].value; 
			if($.trim(value) == ""){
				return false;
			}
			if(value.length == 1){
				value = "0" + value
			}
			val.push(value);
		}
		return val.join(":").toLowerCase();
	}	
	
}();;
/**
 * oui格式
 * */
ItemBox.item.oui = function(){
	return {
		init : init_fn,
		getValue : getValue_fn
	}
	
	function init_fn (conf){
		var self = this;		
		var $outer = $(conf.outer).append(_render(conf));
		_bindEvent($outer);
	}
	
	function _bindEvent($outer){
		var inputs = $outer.find('input[inputType="stringInput"]');
		inputs.bind("keydown",function(event){
			var $input = $(this);
			var keyCode = event.keyCode;
			
			//文本框没有选中东西,文本框值已经有两位，跳转到下一个文本框，选中下一个文本框的内容
			if(!$input.attr("selecting") && $input.val().length == 2 && keyCode != 8 && keyCode != 38 && keyCode != 36 && keyCode != 37 && keyCode != 39){
				$input.nextAll(":input:first").focus().select();
				return false;
			}else if($input.attr("selecting") && $input.val().length == 2 ){
							
			}else if($input.val().length == 2 && keyCode != 8 && keyCode != 38 && keyCode != 36 && keyCode != 37 && keyCode != 39 && keyCode != 40){
				$input.nextAll(":input:first").focus().select();
				$input.removeAttr("selecting");
				return false;
			}
			$input.removeAttr("selecting");

			
			if(keyCode >= 48 && keyCode <=57 ||
					keyCode >=96 && keyCode <=105 || keyCode >=65 && keyCode <=90 || keyCode == 8  || keyCode == 110 || keyCode == 46 || keyCode == 9 || keyCode == 144 || keyCode >=36 && keyCode <= 39){
				return;
			}else{
				return false;
			}
		}).bind("select", function(){
			$(this).attr("selecting",true);
		}).bind("keyup", function(event){
			var $input = $(this);
			var keyCode = event.keyCode;
			if($input.val().length == 2 && keyCode <=36 && keyCode >= 39){
				$input.nextAll(":input:first").focus().select();
			}
		});
	}
	
	/**
	 * conf.value
	 * 
	 * */
	function _render (conf){
		var _inputDom  = [];		
		var value = conf.value ? conf.value.split(":") : "";
		for(var i = 0; i < 3; i++){
			_inputDom.push(
				$.createDomStr({
					tagName:"input",
					attr:{
						type : "text",						
						inputType : "stringInput",
						"class" : "blueinput inputbg",
						maxLength : 2,
						value : value[i] || ""
					},
					style:{
						width : "20px"
					}
				})	
			);
		}		

		return 	$.createDomStr({
			tagName:"div",
			attr:{
				"class" : "ip"
			},
			content : _inputDom.join("<span>:</span>")
		});
		
	}
	
	function getValue_fn(wrapper,validateType){
		var inputs = $.getJQueryDom(wrapper).find('input[inputType="stringInput"]');
		var val = [];
		for(var i = 0; i< inputs.length; i++){
			var value = inputs[i].value; 
			if($.trim(value) == ""){
				return false;
			}
			if(value.length == 1){
				value = "0" + value
			}
			val.push(value);
		}
		return val.join(":").toLowerCase();
	}	
	
}();;
/**
 * 
 * 下拉框选择类型
 * 
 * */
ItemBox.item.selectItem = function(Item){
	
	return {
		init : init_fn,
		bindEvent : _bindEvent,
		getValue : getValue_fn
	}

	function _bindEvent (conf){
		CustomSelect.init({
			id :conf.config.id + "CustomIpType" + conf.randomId,
			listeners:{
				selectAfter : function(val){			
					var $wrapper = $("#" + conf.config.id + "CustomIpType" + conf.randomId + "wrapper");	
					var value = $wrapper.attr("value");
					if(value){
						$wrapper.removeAttr("value");
					}
					$wrapper.find("*").remove();
					Item[val].init({
							outer:$wrapper,
							value:value,
							itemboxConf : {inputWidth : 189}
						});
				}
			}
		});
	}
	
	/**
	 * 	index : index,
	 *	outer : $item,
	 *	value : data,
	 *	itemboxConf : conf
	 * */
	function init_fn (conf) {
		var $outer = $(conf.outer);
		
		var selectVal = conf.itemboxConf.items;// [{text:"IP地址",value:"ip"},{text:"IP子网",value:"subnet"},{text:"mac地址",value:"mac"},{text:"厂商唯一标识（OUI）",value:"oui"}];
		var selectStr = CustomSelect.getTemplate(conf.itemboxConf.id + "CustomIpType" + conf.randomId, "CustomIpType" + conf.index, selectVal, conf.value ? conf.value.key : null);
		
		$outer.append(selectStr);
		$outer.append($.createDomStr({
			tagName:"div",
			attr:{
				id : conf.itemboxConf.id + "CustomIpType" + conf.randomId + "wrapper",
				"class" : "inline",
				value : conf.value ? conf.value.value : null
			}
		}))
	}
	

	
	
	function getValue_fn (wrapper){
		var $wrapper = $(wrapper);
		var id = $wrapper.find("div.select_body").attr("id");
		var value = CustomSelect.getValue(id);
		var val = null;
		if(value == "ip"){
			val = Item.ip.getValue(Item.ip.getIpWrapper($wrapper));
			if(val == ""){
				return false;
			}	
		}else{
			val = Item[value].getValue($wrapper);
		}
		
		if(val === false){
			return val;
		}else{
			return {key : value, value : val}
		}
		
	}
	
}(ItemBox.item);;/**
 * 文本框形式
 * */
ItemBox.item.string = function(){
	return {
		init : init_fn,
		getValue : getValue_fn
	}
	
	function init_fn (conf){
		var self = this;		
		$(conf.outer).append(_render(conf));
	}
	/**
	 * conf.value
	 * 
	 * */
	function _render (conf){
		var _inputDom  = [];
		var itemboxConf = conf.itemboxConf || {};

		return 	$.createDomStr({
			tagName:"div",
			attr:{"class":"option"},
			content:$.createDomStr({
					tagName:"input",
					attr:{
						type : "text",						
						inputType : "stringInput",
						"class" : "blueinput inputbg",
						maxLength : conf.maxLength,
						value : conf.value || ""
					},
					style:{
						width : itemboxConf.inputWidth ? itemboxConf.inputWidth + "px" : null
					}
				})
		});	 
	}
	
	function getValue_fn(wrapper,validateType){
		var val = $.getJQueryDom(wrapper).find('input[inputType="stringInput"]').val();
		if(validateType){			
			if(validateType.mac && !Validate.isMac(val)){
				return false;
			}			
		}
		return val;
		
	}	
	
}();

;/**IP选项
 * @param conf.outer dom ip组件外层容器,生成ip组件，添加到该dom下 * 
 * */
ItemBox.item.subnet = function(){
	return {
		init : init_fn,
		render : render_fn,
		getValue : getValue_fn
		
	}
	
	function _initData (data){
		return data.split("/");
	}
	
	function _bindEvent (conf){
		var self = this;		
		var $inputs = getSubItems_fn($(conf.outer));
		
		$.onlyNumber({
			input:$inputs
		});
		$inputs.bind("keyup",{conf:conf},_checkingMouseup);		
	}
	

	
	/**
	 * 检测ip框输入内容合法性
	 * 检测数值在0-255之间
	 * */
	function _checkingMouseup (event){
		var _value = this.value;
		if(_value == ""){
			return false;
		}
		var _ipNum = +_value;
		if(_ipNum > 32){
			this.value = "";
			return false;
		}
	}
	
	
	function init_fn (conf){
		var self = this;
		conf.values = _initData(conf.value || "");
		self.render(conf);		
		_bindEvent(conf);	
	}
	
	function render_fn(conf){
		var _dom = [];
		var param = conf.param || {};
		var values = conf.values || [];
		var ipVal = "",subVal = "";
		var Item = ItemBox.item;
		if(values.length == 2){
			ipVal = values[0];
			subVal = values[1];
		}
		var $outer = $(conf.outer);
		Item.ip.init({
			outer:$outer,
			value:ipVal,
			itemboxConf : {inputWidth : 189}
		});			
		$outer.append("<span class='t'>/</span>");
		Item.string.init({
			outer:$outer,
			value:subVal,
			itemboxConf : {inputWidth : 25}
		});		
		
	}	
	

	
	function getSubItems_fn ($wrapper){
		return $.getJQueryDom($wrapper).find('input[inputType="stringInput"]');
	}

	function getIpWrapper_fn (outer){
		return $.getJQueryDom(outer).find('div[compType="ipWrapper"]');
	}

	function getValue_fn ($wrapper,validateType){
		var Item = ItemBox.item;
		var ipVal = Item.ip.getValue(Item.ip.getIpWrapper($wrapper),{ip0255 : true});
		if(ipVal == ""){
			return false;
		}
		var subVal = Item.string.getValue($wrapper);	
		if(subVal == ""){
			return false;
		}

		var _subVal = +subVal;
		if(_subVal > 32){			
			return false;
		}
		return ipVal + "/" + subVal;
	}	


}();;/**************************************************************************************************
 * # 查询树
 * 
 * 根据用户输入，下拉展示查询结果。
 * 
 * ## 基本使用方法：
 * 
 * 代码如下：
 * 
 *     // 先初始化树组件
 *     var tree3 = new Tree({id:'resInstTree3'});
 *     // 在初始化树的查询组件
 *     QueryTree.init({
 *         tree:tree3, // 树对象
 *         emptyText: '无数据', // 无数据时的提示文字
 *         filterAttr:[{id:'groupId',text:'资源组'}], // 根据属性查询时的设置
 *         isMulti: true // 是否多选
 *     });
 * 
 * 使用效果如下：
 *     >![多选效果](../assets/img/query_tree_1.png "多选效果")
 * 
 * 
 * @class QueryTree
 * @module tree
 */
var QueryTree = {
    
    /**
     * 初始化
     * @method init
     * @param conf {Json} 配置信息
     * @param conf.tree {Object} 树信息
     * @param conf.tree.id {Object} 树组件id
     * @param conf.filterAttr {Object} 需要过滤的属性信息
     * @param conf.filterAttr.id {String} 属性名
     * @param conf.expandInfo {Object} 在原有文字基础上显示额外文字
     * @param conf.expandInfo.attr {String} 属性名，该属性的值作为额外显示信息
     * @param conf.expandInfo.getText {Function} 参数是treeNode，返回文字时，该文字作为额外显示信息；返回undefined时，使用attr
     * @param conf.inputVal {String} 输入内容
     * @param conf.isHideFilterAttr 是否隐藏过滤分组属性
     * @param conf.isDispNode {nodeId} 过滤节点时回调判断是否显示该节点
     * @param conf.emptyText {String} 未输入内容时的文字提示，例如：“请输入...”
     * @param conf.listeners {Object} 事件
     * @param conf.listeners.onChanged {Object} 定位事件，回调的参数是nodeid和treeid
     */
    init:function(conf){
        var treeId = conf.tree.id;
        var $tree = $("#"+treeId);
        var $input = $("#"+treeId+"_query input");
        var $showBtn = $('#' + treeId + '_query_button');
        var $pop = null;
        var onShow_fn = null;
        var data = {conf : conf, $input : $input};
        
        if (!$input[0]) {
            data.noFix = true;
            $pop = QueryTree._renderPop($showBtn);
            
            $input = $pop.find('input:first').attr('id', treeId + "_query_input");
            $pop.bind('mousedown', function(event) {
                if (event.target.nodeName.toUpperCase() !== 'INPUT') {
                    $input.attr('noHide', 'true');
                }
            }).click(function(event) {
                event.stopPropagation();
            });
            
            
            data.$input = $input;
            
            if (conf.emptyText) {
                $input.attr('emptyvalue', conf.emptyText);
            }
            
            $showBtn.click(onShow_fn = function(event) {
                event.stopPropagation();
                $showBtn.unbind('click');
                
                if ($pop[0].parentNode !== $showBtn[0].offsetParent) {
                    $pop.appendTo($showBtn[0].offsetParent);
                }

                var $btnParent = $showBtn.parent();
                
                var offsetLeft = 0;
                if ($showBtn[0].parentNode !== $showBtn[0].offsetParent) {
                    offsetLeft = $showBtn[0].parentNode.offsetLeft;
                }
                
                $pop.css({
                    left : offsetLeft + 5,
                    top : $showBtn[0].offsetTop + $showBtn.height(),
                    display : 'block',
                    zIndex : ZIndexMgr.get()
                });
                
                var popInnerWidth = $pop.width();
                var popExtWidth = popInnerWidth
                    + QueryTree._getSideFix($pop, 'left')
                    + QueryTree._getSideFix($pop, 'right');
                
                var parentInnerWidth = $btnParent.width();
                var parentWidth = parentInnerWidth
                    + QueryTree._getSideFix($btnParent, 'left')
                    + QueryTree._getSideFix($btnParent, 'right');
                
                if ($.isIE()) {
                    parentWidth = parentWidth
                        - (parseInt($pop.css('padding-left'), 10) || 0)
                        - (parseInt($pop.css('padding-right'), 10) || 0);
                }
                    
                var widthFix = parentWidth - 10 - popExtWidth;
                var inputWidth = $input.width() + widthFix;
                
                if (widthFix) {
                    $pop.width(popInnerWidth + widthFix);
                    // 针对FF和IE的问题：设置宽度是212，再次读取就变成了206；补充差值。
                    $input.width(inputWidth);
                    $input.width(inputWidth * 2 - $input.width());
                    // -------------------------------------------------------
                }
                
                setTimeout(function() {
                    $input.focus();
                });
            });
        }


        $input[0].closeMe = function() {
            if (!$input.attr('noHide')) {
                ZIndexMgr.free($pop);
                QueryTree._blurDiv(null, treeId);
                if ($pop) {
                    $pop.hide();
                    $showBtn.click(onShow_fn);
                }
            }else {
                $input.focus().select();
                $input.removeAttr('noHide');
            }
        };

        $input.blur(function(event_) {
            if (window['dontHidePop']) {
                return;
            }
            if ($input.parent().css('display') !== 'none') {
                setTimeout($input[0].closeMe, 200);
            }
        }).bind("keydown",data, function(event) {
            UlLiKeySelect.keyEvent(event);
        }).bind("keyup",data,QueryTree.query)
            .bind("click",data,QueryTree.query)
            .parent().find('a.select_trigger_del').bind("click", data, QueryTree.reset);

        InputEmpty.init($input);
    },
    
    _getExpandText : function (conf, $li) {
        var attrVal = null;
        var expText = null;
        
        if (conf.expandInfo && conf.expandInfo.attr) {
            attrVal = $li.attr(conf.expandInfo.attr);
        }
        
        if (conf.expandInfo && conf.expandInfo.getText) {
            expText = conf.expandInfo.getText($li, conf);
        }
        
        if (!expText) {
            expText = attrVal;
        }
        
        if (expText) {
            return expText;
        }else {
            return '';
        }
    },
    
    _renderPop : function ($posRelateTo) {
        var $pop = $('<div class="select_body">' +
            '<input class="select_text default">' +
            '<div class="select_trigger_wrap"><a class="select_trigger_del"></a></div>' +
        '</div>').css({
            top: 0,
            position : 'absolute',
            display : 'none'
        }).appendTo($posRelateTo.offsetParent());
        
        return $pop;
    },
    
    reset : function(event){    
        event.data.$input.val("");
        event.data.$input.attr('title', '');
        QueryTree._blurDiv(null, event.data.conf.tree.id);
        event.data.conf.tree.clearSearchNode();
    },
    
    clear : function(treeId) {
        var $clearBtn = $('#' + treeId + '_query_input').next();
        $clearBtn.click(); 
    },
    /**
     * 获得匹配属性值的节点
     * @method filterAttributesNode
     * @param conf {Json} 配置信息
     * @param conf.tree {Object} 树信息
     * @param conf.tree.id {Object} 树组件id
     * @param conf.filterAttr {Object} 需要过滤的属性信息
     * @param conf.filterAttr.id {String} 属性名
     * @param conf.inputVal {String} 输入内容
     * @return {String} 查找出来的项的HTML结构 
     */
    filterAttributesNode:function(conf){
        var tree = conf.conf.tree;
        var filterAttr = conf.conf.filterAttr;//需要过滤的属性
        var lis = $("#"+tree.id).find('li');
        var input = (conf.inputVal || "").toLowerCase();
        var attrCollection = {};
        var $li = null,attrVal = null,attr = null;
        var expandText = '';
        for(var i=0,ilen = lis.length;i<ilen;i++){
            $li = $(lis[i]);
            for(var k=0,len=filterAttr.length; k<len; k++){
                attr = filterAttr[k]; attrVal = $li.attr(attr.id);
                if(attrVal && attrVal.toLowerCase().indexOf(conf.inputVal) !=-1){
                    var attrVals = attrVal.split("#");
                    expandText = QueryTree._getExpandText(conf.conf, $li);
                    for(var j=0;j<attrVals.length;j++){
                        if(attrVals[j].indexOf(input) == -1){
                            continue;
                        }
                        if(!attrCollection[attr.id]){
                            attrCollection[attr.id] = [];
                        }
                        if(!QueryTree._existInArray(attrVals[j], attrCollection[attr.id])){
                            attrCollection[attr.id].push({val : attrVals[j], ext: expandText});
                        }
                    }
                }
            }
        }
        var items = [];
        for(var i = 0, len = filterAttr.length; i < len; i++){
            items.push('<li>');
            items.push('<a>'+filterAttr[i].text+'</a>');
            items.push('</li>');
            var attrValues = attrCollection[filterAttr[i].id];
            if(attrValues){
                for(var j = 0, jlen = attrValues.length; j < jlen; j++){
                    items.push('<li title="' + attrValues[j].val + attrValues[j].ext + 
                       '" val="attribute" attrId="' + filterAttr[i].id + '" titleVal="' + attrValues[j].val + '">');
                    items.push('<a><span class="t">' + attrValues[j].val + attrValues[j].ext + '</span></a>');
                    items.push('</li>');
                }
            }
        }
        return items.join("");
    },
    
    _existInArray : function (valObj, valArray) {
        if (!valObj) return true;
        if (!valArray) return true;
        
        var count = valArray.length;
        while (count--) {
            if (valArray[count].val === valObj) return true;
        }
        return false;
    },
    /**
     * 过滤节点文本
     * @method filterTextNode
     * @param conf {Json} 配置信息
     * @param conf.tree {Object} 树信息
     * @param conf.tree.id {Object} 树组件id
     * @param conf.inputVal {String} 输入内容
     * @return {String} 查找出来的项的HTML结构 
     */
    filterTextNode:function(conf){
        var tree = conf.conf.tree;
        var $textSpan = $("#"+tree.id).find('span[type="text"]');
        var span  = null, text = null,selectedNode = [];
        var input = (conf.inputVal || "").toLowerCase();
        var extText = '';
        //抓取显示文本匹配
        for(var i = 0, len = $textSpan.length; i < len; i++){
            span = $textSpan[i];
            extText = QueryTree._getExpandText(conf.conf, $(span).parent());
            text = span.innerHTML;        
            if(text.toLowerCase().indexOf(input) != -1){
                var nodeId = $(span).parent().attr("nodeId");
                var isdisp = true;
                if(conf.conf.isDispNode){
                    isdisp = conf.conf.isDispNode(nodeId);
                }
                if(isdisp){
                    selectedNode.push({id : nodeId, text : text, ext: extText});    
                }                
            }
        }
        var snode = null;
        var items = [];
        if(!conf.conf.isMulti && !conf.conf.isHideFilterAttr){
            items.push('<li title="' + S_REROURCE + '">');
            items.push('<a>' + S_REROURCE + '</a>');
            items.push('</li>');
        }
        for(var i = 0, len = selectedNode.length; i < len; i++){
            snode = selectedNode[i];
            if (conf.conf.isMulti) {
                items.push('<li val="' + snode.id + '" title="' + snode.text + snode.ext + '" style="overflow:hidden;text-overflow:ellipsis;">');
            }else {
                items.push('<li val="' + snode.id + '" title="' + snode.text + snode.ext + '">');
            }
            if (conf.conf.isMulti) {
                items.push('<span class="t"><input type="checkbox">' + snode.text + snode.ext + '</span>');
            }else {
                items.push('<a><span class="t">' + snode.text + snode.ext + '</span></a>');
            }
            items.push('</li>');
        }
        return items.join("");
    },
    query:function(event){
        if (UlLiKeySelect.keyEvent(event, false)) return;
        
        var treeId = event.data.conf.tree.id;
        var input = this;
        
        // 没输入内容是，不搜索
        if (!$.trim(input.value)) {
            QueryTree._blurDiv(null, treeId);
            return;
        }
        
        var inputPos = $.getElementAbsolutePosition(this);
        var querycontentId = treeId+"_query_content";
        var $qc = document.getElementById(querycontentId);
        var $heightParent = QueryTree._getHeightParent($("#" + treeId));
        var height = 0;
        var isNewCreated = false;
        var width;
        if($qc){
            $qc = $($qc);
            isNewCreated = false;
        }else{
            $qc = $('<div id="'+treeId+"_query_content"+'" style="z-index:' + 
                ZIndexMgr.get() + ';overflow:auto;" class="select_boundtree"></div>');
            isNewCreated = true;
        }
        $(document.body).append($qc);
        width = QueryTree._getEqualsWidth($qc, $(this).parent());
        $qc.width(width);
        
        if ($('#' + treeId + '_query_button')[0]) {
            height = - QueryTree._getShowHeight($('#' + treeId + '_query_button'));
        }
        height += (event.data.conf.height || QueryTree._getEqualsHeight($qc, $heightParent));
        
        
        $qc.css({
            left : inputPos.x - 1,
            top : inputPos.y + $(this).height(),
            // 支持自定弹出高度
            height : height
        });
        
        if (!event.data.noFix) {
            QueryTree._setWidthEquals($(this).parent(), $qc);
        }
        var items = ['<ul>'];
        if(event.data.conf && !event.data.conf.isMulti && event.data.conf.filterAttr){
            items.push(QueryTree.filterAttributesNode({conf : event.data.conf, inputVal : this.value}));
        }
        items.push(QueryTree.filterTextNode({conf : event.data.conf,inputVal:this.value}));
        items.push('</ul>');
        $qc.find("*").unbind();
        $qc.html(items.join("")).show().mousedown($.proxy(function(){
            $(input).attr('noHide', 'true');
        }, this));
        if (!event.data.conf.isMulti) {
            $qc.find("li").bind("click", function(){
                var $li = $(this);
                var val = $li.attr("val");
                if (val) {
                    var title=$li.attr("title");
                    var titleVal = $li.attr("titleVal");
                    var nodeId = '';
                    if(val=="attribute"){
                        var lis = $("#"+treeId).find('li['+$li.attr("attrId")+'*="'+titleVal+'"]');
                        for(var i=0,len = lis.length;i<len;i++){
                            QueryTree._checkAll(event.data.conf.tree.getNodeById(nodeId = $(lis[i]).attr("nodeId")));
                        }
                    }else{
                        event.data.conf.tree.searchNode(val);
                        QueryTree._checkAll(event.data.conf.tree.getNodeById(nodeId = val));
                    }
                    input.value = $(this).attr("title");
                    input.title = input.value;
                }

                QueryTree._close(treeId, event.data.conf, true, val ? nodeId : null);
            });
        }else {
            // 全选按钮
            var $toolBar = $('<div style="height:29px; line-height:20px;"><input type="checkbox">' + 
                (window.S_LABEL_ALL_SELECT || '!!S_LABEL_ALL_SELECT!!') + '</div>').prependTo($qc);
            var $input = $toolBar.children('input');

            // 确定按钮
            $('<a class="btn_min_body" style="float:right; margin:0;">' +
                '<div class="btn_l">' +
                  '<div class="btn_r">' +
                    '<div class="btn_m"> ' +
                        '<span class="text">' + (window.S_BTNOK || '!!S_BTNOK!!') + '</span>' +
                    '</div>' +
                  '</div>' +
                '</div>' +
            '</a>').prependTo($toolBar).click($.proxy(function(){
                event.data.conf.tree.getRoot().$li.find('ul[ischeckedcount]').removeAttr('ischeckedcount');
                var nodeIds = $qc.find('li>span input:checked').map($.proxy(function(_, input){
                    return $(input).parents('li:first').attr('val');
                }, this));
                if (nodeIds.length > 0) {
                    _.each(nodeIds, function(nodeid) {
                        var node = event.data.conf.tree.getNodeById(nodeid);
                        node.setChecked(true);
                        node.expandUP();
                        node.expend();
                        if (!firstNode) {
                            firstNode = node;
                        }
                    });
                    var firstNode = event.data.conf.tree.getNodeById(nodeIds[0]);

                    event.data.conf.tree.scrollToPos(firstNode.$li.position().top);
                }

                QueryTree._close(treeId, event.data.conf, nodeIds.length > 0, firstNode ? firstNode.$li.attr('nodeId') : null);
            }, this));

            $qc.children('ul').css({
                borderTop: '1px solid #0D65A9',
                overflowY: 'auto',
                overflowX: 'hidden',
                whiteSpace : 'nowrap',
                height : height - 30
            });


            CustomCheck.batchInit({outer:$qc});

            CustomCheck.click($input, $.proxy(function() {
                var value = $input.attr('checked');
                $qc.find('li>span input').each(function(_, input) {
                    CustomCheck.setCheck($(input), !!value, undefined, false);
                });
            }, this));

            var count = $qc.find('li>span input').length;
            var onOtherClicked = $.proxy(function() {
                var selectedCount = $qc.find('li>span input:checked').length;
                if (selectedCount === 0) {
                    CustomCheck.setCheck($input, false, undefined, false);
                }else if (selectedCount === count) {
                    CustomCheck.setCheck($input, true, undefined, false);
                }else {
                    CustomCheck.setHref($input);
                }
            }, this);

            $qc.find('li>span input').each($.proxy(function(_, input){
                CustomCheck.click($(input), onOtherClicked);
                $(input).parent('a').css({
                    padding: 0,
                    display: 'inline-block',
                    height: 16
                });
            }, this));
        }

        $qc.find("li[val]").hover(function(){
            $(this).addClass("over");
        },function(){
            $(this).removeClass("over");
        });
        
        if (!event.data.conf.isMulti) {
            UlLiKeySelect.setTarget({
                target : $("#"+treeId+"_query_content").children('ul:first'),
                scrollPanel : $("#"+treeId+"_query_content"),
                listeners : {
                    onEsc : function() {
                        QueryTree._blurDiv(null, treeId);
                    },
                    onChange : function($li) {
                        $li.siblings().removeClass('on');
                        $li.addClass('on');
                    },
                    onSelect : function($li) {
                        $li.click();
                    }
                }
            }, true);
        }

        var ulHeight = QueryTree._getShowHeight($qc.children());
        if(ulHeight < height){
            $qc.height('auto');
        }
    },

    _close : function(treeId, conf, success, nodeId) {
        if (document.getElementById(treeId + "_query_input")) {
            var closePop = document.getElementById(treeId + "_query_input").closeMe;
            if (closePop) {
                closePop(true);
            }
        }
        
        if (success && conf.listeners && conf.listeners.onChanged) {
            conf.listeners.onChanged(nodeId, treeId);
        }
        
        QueryTree._blurDiv(null, treeId);
    },
    
    _getShowHeight : function (dom) {
        var $dom = $(dom);
        return $dom.height()
            + QueryTree._getSideFix($dom, 'top')
            + QueryTree._getSideFix($dom, 'bottom');
    },
    
    _getShowWidth : function (dom) {
        var $dom = $(dom);
        return $dom.width()
            + QueryTree._getSideFix($dom, 'left')
            + QueryTree._getSideFix($dom, 'right');
    },
    
    _getSideFix : function (dom, side) {
        var $dom = $(dom);
        return (parseInt($dom.css('padding-' + side), 10) || 0)
            + (parseInt($dom.css('margin-' + side), 10) || 0)
            + (parseInt($dom.css('border-' + side + '-width'), 10) || 0);
    },
    
    _getEqualsWidth : function (resizeDom, refDom, fix) {
        var resizeDomSideFix = QueryTree._getSideFix(resizeDom, 'left')
            + QueryTree._getSideFix(resizeDom, 'right');
        var refDomSize = QueryTree._getShowWidth(refDom);
        
        return refDomSize - resizeDomSideFix + (fix || 0);
    },
    
    _getEqualsHeight : function (resizeDom, refDom, fix) {
        var resizeDomSideFix = QueryTree._getSideFix(resizeDom, 'top')
            + QueryTree._getSideFix(resizeDom, 'bottom');
        var refDomSize = QueryTree._getShowHeight(refDom);
        
        return refDomSize - resizeDomSideFix + (fix || 0);
    },
    
    _checkAll: function (treeNode_) {
        if (!treeNode_) return;
        
        // 选中当前节点
        treeNode_.setChecked();
        
        // 选中子节点
        if (!treeNode_.isLeaf()){
            var childrenNodes = treeNode_.children();
            var count = childrenNodes.length;
            while (count--) {
                QueryTree._checkAll(childrenNodes[count]);
            }
        }
    },
    
    _setWidthEquals: function ($relator, $elem) {
        var relatorWidth = $relator.width();
        if (!$.isIE()) {
           relatorWidth += (parseInt($relator.css('padding-left')) || 0) +
           (parseInt($relator.css('padding-right')) || 0);
        }
           
        var elemGap = 0;
        if (!$.isIE()) {
           elemGap += (parseInt($elem.css('padding-left')) || 0) +
           (parseInt($elem.css('padding-right')) || 0);
        }
        
        $elem.width(relatorWidth - elemGap);
    },
    _blurDiv:function(event, id){
        if (window['dontHidePop']) return;
        if (undefined === id) {
            var isOut = $.checkClickPointerIsOuter({x:event.pageX,y:event.pageY},document.getElementById(event.data.id+"_query_content"));
            if(!isOut){
                return;
            }
            id = event.data.id;
        }
        ZIndexMgr.free($("#" + id + "_query_content"));
        $("#" + id + "_query_content").remove();
        $(document.body).unbind("mousedown",QueryTree._blurDiv);
    },
    _scrollDiv:function(event){
        $("#"+event.data.id+"_query_content").remove();
        $("*:not([id='"+event.data.id+"'])").unbind("scroll",QueryTree._scrollDiv);
    },
    _onQueryContentKeyPress : function (event) {
        var $qc = event.data;
        
        // 查询结果DIV不存在时或不是“上下”键时，不执行操作
        if (!$qc[0] || event.which !== 38 && event.which !== 40) return;
        
        var $curLi = $qc.find('ul>li.over');
        var $moveToLi = null;
        
        if (!$curLi[0]) {
            $moveToLi = $qc.find('ul>li:first');
        }else if (event.which === 38/*up key*/){
            $moveToLi = $curLi.prev();
        }else if (event.which === 40/*down key*/) {
            $moveToLi = $curLi.next();
        }
        if ($moveToLi) {
            $moveToLi.addClass('over');
            $curLi.removeClass('over');
        }
    }, 
    _getHeightParent : function ($dom) {
        var $heightParent = $dom.parent();
        var overflowY;
        while ($heightParent[0]) {
            overflowY = $heightParent.css('overflow-y');
            if ($heightParent.hasClass('jspContainer') || overflowY === 'auto' || overflowY === 'scroll') {
                return $heightParent;
            }else {
                $heightParent = $heightParent.parent();
            }
        }
        return undefined;
    }
};;/*
 * conf{currentColor:当前选中的颜色，color:节点文本颜色}
 */
/**
 * 树组件
 * @class Tree
 * @uses Node
 * */
function Tree(conf){
    var closeClassName = "ico-file";
    var openClassName = "ico-file-leaf";
    /**
     * 树节点
     * @class Node
     * */
    function Node(){
        this.$li =null;
        this.childs = [];
    }
    Node.prototype={
        /*
         * 获得节点文本jQ对象
         */
        _get$Text:function(){
            var $text = this.$li.children("span[type='text']");
            return $text;
        },
        /*
         * 获得节点图标jQ对象
         */
        _get$Ico:function(){
            var $ico = this.$li.children("span[type='ico']");
            return $ico;
        },
        /*
         * 获得节点图标jQ对象
         */
        _get$Img:function(){
            var $img = this.$li.children("img");
            return $img;
        },        
        /*
         * 获得工具按钮jQ对象
         * */
        _get$Tool:function(){
            var $ico = this.$li.children("span[type='tool']");
            return $ico;
        },
        /*
         * 获得节点折叠按钮jQ对象
         */        
        _get$Btn:function(){
            var $btn = this.$li.children("div:first");
            return $btn;            
        },
        /*
         * 获得节点子节点集合jQ对象
         */        
        _get$Childs:function(){
            var $ul = this.$li.children("ul:last-child");
            return $ul;
        },
        /**子节点是否全选*/
        isChildAllChecked : function(isCheck){
            var lis = this.$li.children("ul:last-child").children("li");
            var  checkboxs = 0;
            var  checked = 0;
            var $check = null;
            for(var i = 0; i < lis.length; i++){                
                $check = $(lis[i]).children("input[type='checkbox']");
                if($check[0]){
                    checkboxs++;
                    if($check.attr("checked")){
                        checked++;
                    }
                }                
            }
            if(checkboxs != 0 && checkboxs == checked){
                return true;
            }
            return false;
        },
        /*
         * 
         * 获得最后一个子节点jQ对象
         * */
        _get$lastChild:function(){
            var $li = this.$li.children("ul:last-child li:last");
            return $li;
        },
        /*
         * 获得复选框文本jQ对象
         */
        _get$Checkbox:function(){
            var $checkbox = this.$li.children("input[type='checkbox']");
            return $checkbox;
        },
 
        /*
         * 获得复选框文本jQ对象
         */        
        _get$Radiobox:function(){
            var $radio = this.$li.children("input[type='radio']");
            return $radio;
        },
        _createNode :function($li){
            var node = new Node();
            node.setNode($li);
            return node;
        },
        _bindChildEvent : function(){
            if(this.$li.attr("isBind")){
                return;
            }
            this.$li.attr("isBind",true);
            var ul = this.$li.children("ul")[0];            
            var lis = ul ? ul.childNodes : null;
            var listeners = conf.listeners || {};
            var $li = null;   
            if(lis){
                for(var i = 0, len = lis.length; i < len; i++){
                    $li = $(lis[i]);
                    tree.bindFoldBtn($li.children("div.hitarea"), listeners);
                    tree.bindNodeClick($li.children("span[type='text'][clickable='true']"), listeners);
                    tree.bindCheckbox($li.children("input[type='checkbox']"), listeners);
                    tree.bindToolclick($li.children("span[type='tool']"), listeners);
                }    
            } 
        },
        /**
         * 该节点是否是同级最后一个节点
         * @method isLast
         * @for Node
         * @return {Boolean}
         * @lends Node.property
         * */
        isLast:function(){
            return this.$li[0].className.indexOf("last")!=-1;
        },
        /**
         * 塞入树节点$li 
         * @method setNode
         * @chainable
         */
        setNode:function($li){ 
            this.$li = $li;
             return this;
        },
        /**
         * 当前节点是展开状态还是折叠状态
         * @method state
         * @return {Boolean}
         */
        state:function(){   
            return this.$li.hasClass("collapsable");
        },
        /**
         * 该节点是否是叶子节点
         * @method isLeaf
         * @return {Boolean}
         */
        isLeaf:function(){
           var id = this.getId();
            
            if(this._get$Childs()[0]==null && id && id != "root"){ //没有子节点UL则为子节点
                var clsName = this._get$Btn()[0].className;
                if(this._get$Btn()[0] && clsName.indexOf("placeholder") !=-1){ //折叠按钮为placeholder样式则为子节点
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        },
        /**
         * 该节点是否显示
         * @method isNodeDisplay
         * @return {Boolean}
         */
        isNodeDisplay:function(){
            return this.$li.css("display")!="none";
        },
        /**
         * 该节点是否展开
         * @method isExpend
         * @return {Boolean}
         */
        isExpend:function(){
            return this.$li[0].className.indexOf("collapsable")!=-1;
        },
        /**
         * 隐藏该节点
         * @method setNodeDispNone
         * @chainable
         */
        setNodeDispNone:function(){
            this.$li.hide();
            return this;
        },
        /**
         * 显示该节点
         * @method setNodeDisp
         * @chainable
         */        
        setNodeDisp:function(){
            this.$li.show();
            return this;
        },
        /**
         * 设置节点文本
         * @method setText
         * @param {String} text
         * @chainable
         */
        setText:function(text){
            var $text = this._get$Text();
            $text.html(text);
            return this;
        },
        /**
         * 获得节点文本
         * @method getText
         * @return {String} text
         */        
        getText:function(){
            var $text = this._get$Text();
            return $text.html();
        },
        /**
         * 设置结点title属性
         * @method getText
         * @chainable
         * @param {String} title
         */ 
        setTitle:function(title){
            var $text = this._get$Text();
             $text.attr("title",title);
             return this;
        },
        /**
         * 获取结点title属性
         * @method getTitle
         * @chainalbe
         * @return {String}
         */
        getTitle:function(){
            return this._get$Text().attr("title");
        },        
        /**
         * 获得节点ID
         * @method getId
         * @return {String}
         */                
        getId:function(){
            return this.$li.attr("nodeid");
        },
        /**
         * 设置节点ID
         * @method setId
         * @param {String} nodeid 节点ID
         * */
        setId:function(nodeid){
            return this.$li.attr("nodeid",nodeid);
        },
        /**
         * 设置文本节点样式
         * @method setTextStyle
         * @param styleName {String} 样式名称
         * @param styleVal {String} 样式值
         * @chainable
         */                
        setTextStyle:function(styleName,styleVal){
            var $text = this._get$Text();
            $text.css(styleName,styleVal);
            return this;
        },
        /**
         * 设置节点为当前选中的节点
         * @method setCurrentNode
         * @chainable
         */            
        setCurrentNode:function(){
            if(tree.currentNode){//如果有当前节点
                tempNode.setNode(tree.currentNode).clearCurrentNode();
            }
            //if(tree.searchnode){//
              //  tempNode.setNode(tree.searchnode).clearSearchNode();
            //}
            this._get$Text().attr("isCurrent","true").addClass("treenodecolor-current");
            tree.currentNode = this.$li;
            return this;
        },
        /**
         * 清除该节点为当前选中节点
         * @method clearCurrentNode
         * @chainable
         */                    
        clearCurrentNode:function(){
            this._get$Text().removeAttr("isCurrent").removeClass("treenodecolor-current");
            tree.currentNode = null;
            return this;    
        },
        /**
         * 判断该节点是否为当前节点
         * @method isCurrentNode
         * @return {Boolean}
         */                
        isCurrentNode:function(){
            return this._get$Text().attr("isCurrent")==="true";
        },
        /**
         * 设置节点为搜索选中的节点
         * @method setCurrentNode
         * @chainable
         */            
        setSearchNode:function(){
            if(tree.searchnode){//如果有当前节点
                tempNode.setNode(tree.searchnode).clearSearchNode();
            }
            this._get$Text().attr("isSearch","true").addClass("selectednode");
            tree.searchnode = this.$li;
            return this;
        },
        /**
         * 清除该节点为搜索选中节点
         * @method clearSearchNode
         * @chainable
         */                    
        clearSearchNode:function(){
            this._get$Text().removeAttr("isSearch").removeClass("selectednode");
            tree.searchnode = null;
            return this;    
        },
        /**
         * 判断该节点是否为搜索节点
         * @method isCurrentNode
         * @return {Boolean}
         */                
        isSearchNode:function(){
            return this._get$Text().attr("isSearch")==="true";
        },        
         /**
          * 折叠节点
          * @method fold
          * @chainable
          * @listeners折叠按钮操作事件
          * collapseBefore，expendBefore折叠前发生，方法返回false不发生折叠
          * collapseAfter，expendAfter折叠后发生
          */
        fold:function(){
            if(this.state()){//true展开状态，可以折叠
                this.collapse();
            }else{
                this.expend();
            }
            return this;
        },
        /**
         * 展开节点 expendBefore  expend
         * @method expend
         * @chainable
         */
        expend:function(async){
            if(this.state() || this.isLeaf()) return;//如果已经是展开状态或叶子节点，返回，不执行
            if(listeners.expendBefore){//如果展开前有事件，执行返回false，直接返回，不执行展开操作
                var flag = listeners.expendBefore(this);
                if(flag===false) return this; 
            }
            var $child = this._get$Childs();//获得他的子对象集合UL层
            if(!$child[0]){  //如果没有子节点集合，则取节点
                if(url){
                    this.asynNode(true);
                }
            }else{
                $child.show();
            }
            
            
            var $btn = this._get$Btn();
            if(this.$li.hasClass("expandable")){
                this.$li[0].className = "collapsable";
                $btn.removeClass("expandable-hitarea").addClass("collapsable-hitarea");
            }
            
            if(this.$li.hasClass("lastExpandable")){
                this.$li.removeClass("lastExpandable").addClass("lastCollapsable");
                $btn.removeClass("lastExpandable-hitarea").addClass("lastCollapsable-hitarea");
            }                        
            if(this._get$Ico().hasClass(closeClassName)){
                this._get$Ico().removeClass(closeClassName).addClass(openClassName);    
            }
            
            
            if(listeners.expend){//如果展开后有事件
                listeners.expend(this);
            }
            
            this._bindChildEvent();               
            
            return this;            
        },
        /**
         * 折叠节点  collapseBefore collapse
         * @method collapse
         * @chainable
        */
        collapse:function(){
            if(!this.state()) return;//如果已经是折叠状态，返回，不执行
            if(listeners.collapseBefore){//如果展开前有事件，执行返回false，直接返回，不执行展开操作
                var flag = listeners.collapseBefore(this);
                if(flag===false) return this; 
            }            
            
            var $btn = this._get$Btn();
            var $child = this._get$Childs();//获得他的子对象集合UL层
            
            if(this.$li.hasClass("collapsable")){
                this.$li[0].className="expandable";
                //this.$li.removeClass("collapsable").addClass("expandable");
                $btn.removeClass("collapsable-hitarea").addClass("expandable-hitarea");
            }
            if(this.$li.hasClass("lastCollapsable")){
                this.$li.removeClass("lastCollapsable").addClass("lastExpandable");
                $btn.removeClass("lastCollapsable-hitarea").addClass("lastExpandable-hitarea");
            }
            $child.css("display","none");
            if(listeners.collapse){//如果折叠后有事件
                listeners.collapse(this);
            }            
            if(this._get$Ico().hasClass(openClassName)){
                this._get$Ico().removeClass(openClassName).addClass(closeClassName);    
            }            
            return this;            
        },
        /**
         * 展开当前节点，同时展开它的父节点
         * @method expandUP
        */
        expandUP:function(){
            if(this.childCount()>0)
            {
                this.expend();
            }
            var parent = this.parent();
            
            while(parent){
                parent.expend();
                parent = parent.parent();
            }
            
        },
        /**
         * 获得该节点路径
         * @method getPathId
         * @return {Array} 根节点到该节点ID路径数组  
         */
        getPathId:function(){
            var path = [];
            function roundParent(node){
                if(node){
                    var id = node.getId();
                    if(id && id!="root"){
                        path.push(id);
                        roundParent(node.parent());
                    }
                }
            };
            path.push(this.getId());
            roundParent(this.parent());
            return path.reverse();
        },
        /**
         * 节点点击事件
         * @method click
         * @listeners 
         * clickBefore   nodeClick
         */
        click:function(){
            var $text = this._get$Text();
            if($text.css("cursor")=="pointer"){
                if(listeners.clickBefore){
                    var flag = listeners.clickBefore(this);
                    if(flag === false) return this;
                }
                listeners.nodeClick(this);
                this.setCurrentNode();
                if(listeners.clickAfter){
                    listeners.clickAfter(this);
                }
            }
            
        },
        /**
         * 设置图标
         * @method setIco
         * @param ico {String}
         * */
        setIco:function(ico){
            var $ico = this._get$Ico();
            $ico.removeClass().addClass(ico);
        },
        /**
         * 设置图片
         * @method setImage
         * @param imagePath {String} 图片路径
         * */
        setImage:function(imagePath){
            var $img = this._get$Img();
            $img.attr("src",imagePath);
        },
        /**
         * 获得图片路径
         * @method getImage
         * @return 图片路径
         * */        
        getImage : function(){
          var $img = this._get$Img();
          return $img.attr("src");  
        },
        /**
         * 获得所有带复选框的节点
         * @method getCheckboxNodes
         * @param isLeaf {boolean} 如果为true则只获得叶子节点
         * @return {Node}
         * */
        getCheckboxNodes:function(isLeaf){
            var checkboxeds = this._get$Childs().find("input[type='checkbox']");
            //var checkboxeds = checkboxs.filter(":checked");
            var checkedNode = [];
            var node = null;
            for(var i = 0,len = checkboxeds.length; i<len; i++){
                var $checkbox = $(checkboxeds[i]);
                node = new Node();
                if(isLeaf){
                    var $ul = $checkbox.siblings("ul");
                    if($ul.length==0){
                        node.setNode($checkbox.parent());
                        checkedNode.push(node);
                    }
                }else{
                    node.setNode($checkbox.parent());
                    checkedNode.push(node);
                }
            }
            return checkedNode;
        },
        /**
         * 获得所有复选框选中的节点
         * @method getCheckedNodes
         * @param isLeaf {boolean} 如果为true则只获得选中的叶子节点
         * @return {Node}
         * */
        getCheckedNodes:function(isLeaf){
            var checkboxs = this._get$Childs().find("input[type='checkbox']");
            var checkboxeds = checkboxs.filter(":checked");
            var checkedNode = [];
            var node = null;
            for(var i = 0,len = checkboxeds.length; i<len; i++){
                var $checkbox = $(checkboxeds[i]);
                node = new Node();
                if(isLeaf){
                    var $ul = $checkbox.siblings("ul");
                    if($ul.length==0){
                        node.setNode($checkbox.parent());
                        checkedNode.push(node);
                    }
                }else{
                    node.setNode($checkbox.parent());
                    checkedNode.push(node);
                }
            }
            return checkedNode;
        },        
        
        /**
         * 设置该节点复选框或单选框选中
         * @method setChecked
         * @param isRelative 是否判断相关
         * */
        setChecked : function(isRelative){
            var $checkbox = this._get$Checkbox();
            if($checkbox.attr("checked")){
                return;
            }

            var $radiobox = this._get$Radiobox();
            if($radiobox[0]){
                $radiobox.attr("checked","checked");
                return;
            }

            $checkbox.mousedown();
            
            $checkbox.focus();
            $checkbox.attr("checked","checked");
            $checkbox.blur();
            $checkbox.change();
            this.checkboxClick(isRelative);
/*
            if($checkbox[0]){
                $checkbox.attr("checked","true");
                this.checkboxClick();
            }*/
        },
        /**
         * 清除该节点复选框选中
         * @method clearChecked
         * */        
        clearChecked : function(){
            var $checkbox = this._get$Checkbox();
            $checkbox.mousedown();
            $checkbox.removeAttr("checked");
            $checkbox.focus();
            $checkbox.blur();    
            $checkbox.change();
            this.checkboxClick();
        },
        checkslibnext : function(){
            
        },
        /**
         * 该节点是否被选中
         * @method isChecked
         * @return {Boolean}
         */
        isChecked : function(){
            var $checkbox = this._get$Checkbox();
            if($checkbox[0]){
                return $checkbox[0].checked;
            }else{
                return false;
            }
            
        },
        /*
         * 复选框点击事件
         * @checkboxClick 
         * */
        checkboxClick : function(isRelative){
            if(isRelative === false) return;
            this._roundDownChecked();
            this._roundUpChecked();
        },
        /*
         * 级联向上复选节点
         * */
        _roundUpChecked : function(){   //向上检查
            function roundchecked($li){
                var checkbox = $li.children("input[type='checkbox']")[0];
                if(!checkbox) return;
                var checked = checkbox.checked;
                var slibingLis = $li.siblings("li");
                var slibingCheckbox = slibingLis.children("input[type='checkbox']");
                var checkeds = slibingCheckbox.filter(":checked");
                var $parentli = $li.parent().parent();
                var parentCheck = $parentli.children("input[type='checkbox']");
                if(checkeds.length==0 && !checked){
                    parentCheck.removeAttr("checked");
                }else if(slibingCheckbox.length===checkeds.length && checked){
                    parentCheck.attr("checked","true").css("filter","");
                }else{
                    parentCheck.attr("checked","true").css("filter","gray");
                }
                roundchecked($parentli);
            }
            roundchecked(this.$li);
        },
        /*
         * 级联向下复选节点
         * */        
        _roundDownChecked : function(){//向下检查
            
            function roundchecked($li){
                var checkbox = $li.children("input[type='checkbox']")[0];
                if(!checkbox) return;
                var checked = checkbox.checked;
                var checkboxs = $li.children("ul").find("input[type='checkbox']");
                if(checked){
                    checkboxs.attr("checked","true");
                }else{
                    checkboxs.removeAttr("checked");
                }
            }
            roundchecked(this.$li);
        },
        /**
         * 添加子节点集合
         * @method addChilds
         * @param childsHtml {String} html
         */
        addChilds:function(childsHtml){
            var id = this.getId();
            this.$li.append(childsHtml);
            tree.bindFoldBtn("#"+conf.id+" li[nodeid='"+id+"'] ul");
            tree.bindNodeClick("#"+conf.id+" li[nodeid='"+id+"'] ul", listeners);
            tree.bindCheckbox("#"+conf.id+" li[nodeid='"+id+"'] ul", listeners);
            tree.bindToolclick("#"+conf.id+" li[nodeid='"+id+"'] ul", listeners);
            if(this.isChecked()){
                this.checkboxClick();
            }
        },
        /*
         * 绑定事件
         * */
        bindEvent:function(node){
                tree.bindFoldBtn(node._get$Btn());
                tree.bindNodeClick(node._get$Text(), listeners);
                tree.bindCheckbox(node._get$Checkbox(), listeners);
                tree.bindCheckbox(node._get$Radiobox(), listeners);
                tree.bindToolclick(node._get$Tool(), listeners);
        },
        /**
         * js方式添加节点
         * @method appendChild
         * @param nodeConf {Json} 节点配置对象
         * @return {Node} 新创建的节点
         */
        appendChild:function(nodeConf){
            var $ul = null;
            nodeConf.isLast = true;//新添加的这个默认是最后一个节点
            var thisIsLeaf = this.isLeaf();//新添加节点的父节点是一个叶子节点
            var $btn = this._get$Btn();//获得当前节点的折叠按钮
            
            var lastChildNode = this.getLastChild();
            if(lastChildNode){ //调整最后一个
                lastChildNode.setNotLast(lastChildNode.isLeaf(),lastChildNode.isExpend());
            }
            
            if(this._get$Childs()[0]==null){
                if($btn[0]){
                    this.setNotLeaf(this.isLast()); //将折叠按钮设置
                    tree.bindFoldBtn($btn);
                }
                $ul = $("<ul></ul>");
                this.$li.append($ul);
            }
            if($btn[0]){ //用于判断现有树是否支持折叠按钮
                nodeConf.isBtn=true;
            }
            
            $ul = this._get$Childs();
            var nodeStr = TreePlugins.createNode(nodeConf);
            $ul.append(nodeStr);
            var newnode  = tree.getNodeById(nodeConf.nodeId);
            this.bindEvent(newnode);
            return newnode;
        },
        /**
         * 删除该节点
         * @method delNode
         * @return {String} 被删除节点的内部html
         * */
        delNode:function(){
            this.$li.find("*").unbind();
            var curLast = this.isLast();
            var prevnode = this.getPrevNode();
            var nextnode = this.getNextNode();
            if(curLast && prevnode){
                prevnode.setLast(prevnode.isLeaf(),prevnode.isExpend());
            }
            if(this.isChecked()){
                this.parent().clearChecked();
            }
            if(!prevnode && !nextnode){
                if(this.parent().getId()!="root"){
                    this.parent().setLeaf(this.parent().isLast());
                }
            }
            
            
            var div = document.createElement("div");
            div.appendChild(this.$li[0]);
            var html = div.innerHTML;
            div.innerHTML="";
            div=null;
            return html;
        },
        /**
         * 设置节点不是叶子
         * @method setNotLeaf
         * @param isLast
         */
        setNotLeaf:function(isLast){
            tree.bindFoldBtn(this._get$Btn());
            if(isLast){
                this.$li.removeClass().addClass("collapsable lastCollapsable");
                this._get$Btn().removeClass().addClass("hitarea collapsable-hitarea lastCollapsable-hitarea");
            }else{
                this.$li.removeClass().addClass("collapsable");
                this._get$Btn().removeClass().addClass("hitarea collapsable-hitarea");                
            }
        },
        /**
         * 设置节点为叶子节点
         * @method setLeaf
         * @param isLast
         * */
        setLeaf:function(isLast){
            if(isLast){
                if(this.$li[0]){
                    this.$li[0].className = "last";
                }
                var $btn  = this._get$Btn();
                if($btn[0]) $btn[0].className = "placeholder last";
    
            }else{
                if(this.$li[0]){
                    this.$li[0].className = "";
                }                
                var $btn  = this._get$Btn();
                if($btn[0]) $btn[0].className = "placeholder";
                
            }            
            var div = document.createElement("div");
            div.appendChild(this.$li.children("ul")[0]);
            div.innerHTML="";
            div = null;
            tree.unbindFoldBtn(this._get$Btn());
        },
        /**
         * 设置节点不是最后一个节点
         * @method setNotLast
         * @param isLeaf {Boolean}
         * @param isExpend {Boolean}
         * */
        setNotLast:function(isLeaf,isExpend){
            if(isLeaf){
                this.$li.removeClass();
                this._get$Btn().removeClass().addClass("placeholder");
            }else{
                var btnCls = isExpend ? "collapsable" : "expandable";
                this.$li.removeClass().addClass(btnCls);
                this._get$Btn().removeClass().addClass("hitarea "+btnCls+"-hitarea");                
            }            
        },
        /**
         * 设置节点为最后一个节点
         * @method setLast
         * @param isLeaf {Boolean}
         * @param isExpend {Boolean}
         * */
        setLast:function(isLeaf,isExpend){
            if(isLeaf){
                this.$li.removeClass().addClass("last");
                this._get$Btn().removeClass().addClass("placeholder last");
            }else{
                var liCls = "collapsable lastCollapsable";
                var btnCls = "hitarea collapsable-hitarea lastCollapsable-hitarea";
                if(!isExpend){
                    liCls = "expandable lastExpandable";
                    btnCls = "hitarea expandable-hitarea lastExpandable-hitarea";
                }
                this.$li.removeClass().addClass(liCls);
                this._get$Btn().removeClass().addClass(btnCls);                
            }                        
        },
        /**
         * 动态获取树节点
         * @method asynNode
         * @param async 是否异步,默认true, false不是异步
         */
        asynNode:function(async){
            var id = this.getId();
            var self = this;
            $.ajax({
                type: "POST",
                url: url,
                dataType: "html",
                data: tree.param+id,
                async:async==null?true:false,
                success:function(msg){
                    self.addChilds(msg);
                }
            });
        },
        /**
         * 获得父节点
         * @method parent
         * @return {Node} treeNode
         */
        parent:function(){
            var $li = this.$li.parent().parent();
            if($li[0] && $li.attr("nodeid")){
                var node = new Node();
                node.setNode($li);
                return node;
            }else{
                return null;
            }
        },
        /**
         * 获得子节点
         * @method children
         * @return {Array} Node
         * */
        children:function(){
            var nodes = [];
            var ul = this._get$Childs()[0];
            if(ul){
                var lis = ul.childNodes;
                var node = null;
                for(var i=0,len = lis.length;i<len;i++){
                    if(lis[i].nodeType == 3) continue;
                    node = new Node();
                    node.setNode($(lis[i]));
                    nodes.push(node);
                }
            }
            return nodes;
        },
        /**
         * 该节点子节点个数
         * @method childCount
         * @return {Number} number
         */
        childCount:function(){
            var ul  = this._get$Childs()[0];
            if(ul){
                var lis = ul.children;    
                return lis.length;
            }else{
                return 0;
            }
        },
        /**
         * 获得第一个子结点
         * @method getFirstChild
         * @return {Node} treeNode
         */
        getFirstChild:function(){
            var ul = this._get$Childs()[0];
            var node = null;
            if(ul){
                var li  = ul.children[0];
                if(li){
                    node = new Node();
                    node.setNode($(li));
                }
            }
            return node;
        },
        /**
         * 获得上一个节点
         * @method getPrevNode
         * @return {Node} treeNode
         * */
        getPrevNode:function(){
            var li = $.get_previousSibling(this.$li[0]);
            if(li){
                var node = new Node();
                node.setNode($(li));
                return node;            
            }else{
                return null;
            }
                
        },
        /**
         * 获得下一个节点
         * @method getNextNode
         * @return {Node} treeNode
         * */        
        getNextNode:function(){
            var li = this.$li[0].nextSibling
            if(li){
                var node = new Node();
                node.setNode($(li));
                return node;            
            }else{
                return null;
            }
        },
        /**
         * 获得最后一个子结点
         * @method getLastChild
         * @return {Node} treeNode
         */
        getLastChild:function(){
            var ul = this._get$Childs()[0];
            var node = null;
            if(ul && ul.children && ul.children.length > 0){
                var child = ul.children;
                var li  = child[child.length - 1];
                if(li){
                    node = new Node();
                    node.setNode($(li));
                }
            }
            return node;
        },
        /**
         * 获得指定索引的子节点
         * @method getIndexChild
         * @param {number} 索引
         * @return {Node} treeNode
         */        
        getIndexChild:function(index){
            var li = this._get$Childs()[0].children[index];
            if(li){
                var node = new Node();
                node.setNode($(li));
                return node;
            }else{
                return null;
            }                        
        },
        /**
         * 获得节点自定义属性值
         * @method getValue
         * @for Node
         * @param key {String} 属性名
         * @return {String} 属性值
         * */
        getValue:function(key){
            return this.$li.attr(key);
        }
    };
    
    
    var $tree= this.$tree= $("#"+conf.id);//树最外层对象
    this.id = conf.id;
    this.root = $tree.children("li").children("ul");  //根节点
    var url = conf.url;  //异步请求节点的URL
    var param = this.param = conf.param; //异步请求的参数
    this.node = new Node();
    var tempNode =this.tempNode= new Node();
    this.currentNode = null;  //当前选中节点$li
    this.searchnode = null;//检索的节点$li
    var lis = this.root.find("li");
    var tree = this;//tree对象的引用
    var listeners = conf.listeners == null? {}:conf.listeners; 
    
    if(conf.plugins){
        for(var i=0;i<conf.plugins.length;i++){
            var plugin = TreePlugins[conf.plugins[i]];
            if(plugin){
                plugin.call(this,conf);
            }
        }
    }
    
    var self = this;

    if(conf.inputAttrs && conf.inputAttrs.name){
        $("#" + conf.id + ' input[type="radio"]').attr("name", conf.inputAttrs.name);
    }
    /*
    setTimeout(function(){
        self.bindFoldBtn("#"+conf.id);
    },10);
    setTimeout(function(){
        self.bindNodeClick("#"+conf.id, listeners);
    },10);
    setTimeout(function(){
        self.bindCheckbox("#"+conf.id, listeners);
    },10);
    setTimeout(function(){
        self.bindToolclick("#"+conf.id, listeners);
    },10);
    */
    var root = this.getRoot();
    root._bindChildEvent();

    

    function roundBind(children){
        if(children){
            for(var i = 0, len = children.length; i < len; i++){                
                if(children[i].isExpend()){
                    children[i]._bindChildEvent();
                    roundBind(children[i].children());
                }
            }
        }
    }
    roundBind(root.children());

    if(root && conf.isExpand === false ? false : true){
        var firstChild = root.getFirstChild();
        if(firstChild){
            firstChild.expend();
        }
    }
    
    
    
};
;
Tree.prototype={
        /**
         * 根据传入的层级展开节点
         * @method expendByHierarchy
         * @param hierarchy {Array} 层级，每个元素标识该层级元素展开的索引
         *                            从根节点开始
         * */
        expendByHierarchy:function(hierarchy){
            var root = this.getRoot();
            for(var i=0,len = hierarchy.length; i < len ;i++){
                var index = hierarchy[i];
                if(!root){
                    root = this.getRoot();
                }else{
                    root = root.children()[index];
                }
                root.expend();
            }
        },
        /**
         * 获得树中左右选中节点信息
         * @method getCheckedHierarchy
         * @param attrKeys {Array}
         * @return Array[{text:节点文本，id：节点ID,childs：子节点数组}]
         * */
        getCheckedHierarchy:function(attrKeys){
            var root = this.getRoot();
            var hierarchy = [];
            
            round(root,hierarchy);
            
            function round(node,childArray){
                var childrens = node.children();
                if(childrens != null){
                    var child = null;
                    var nodeObj = null;
                    for(var  i=0,len = childrens.length;i<len;i++){
                        child = childrens[i];
                        if(child.isChecked()){
                            nodeObj = {};
                            nodeObj.text = child.getText();
                            nodeObj.id = child.getId();
                            nodeObj.parentId = child.parent().getId();
                            nodeObj.childs = [];
                            nodeObj.isLeaf = child.isLeaf();
                            nodeObj.img = child.getImage();
                            if(attrKeys){
                                nodeObj.attrs = {};
                                for(var keyi = 0;keyi<attrKeys.length;keyi++){
                                    nodeObj.attrs[attrKeys[keyi]] = child.getValue(attrKeys[keyi]);
                                }
                            }
                            
                            childArray.push(nodeObj);
                            round(child,nodeObj.childs);
                        }
                    }
                }
            }
            
            return hierarchy;
        },    
        /**
         * 获得树中所有叶子节点
         * @method getLeafNodes
         * @return {Node}
         * */
        getLeafNodes:function(){
            var node = this.getRoot();
            var children = null;
            var leafChild = [];
            round(node);
            
            function round(node){
                var children = node.children();
                var child = null;
                for(var i=0,len = children.length;i<len;i++){
                    child = children[i];
                    if(child.isLeaf()){
                        leafChild.push(child);
                    }
                    round(child);
                }
            }
            return leafChild;
    },        
    /*
     * 绑定折叠按钮事件
     */        
    bindFoldBtn:function(path){
        var self = this;
        if($.isString(path)){
            var btns = $(path).find("div.hitarea");
        }else{
            var btns = path;
        }
        if(btns.attr("binded")){
            return;
        }
        
        btns.bind("click",function(event){
        	//event.stopPropagation();
            var $btn = $(this);
            self.node.setNode($btn.parent());
            self.node.fold();
        }).attr("binded","true");                
    },
    unbindFoldBtn:function(path){
        var self = this;
        if($.isString(path)){
            var btns = $(path).find("div.hitarea");
        }else{
            var btns = path;
        }
        if(!btns.attr("binded")){
            return;
        }
        
        btns.unbind("click").removeAttr("binded");
    },
    /*
     * 绑定节点点击事件
     */
    bindNodeClick:function(path,listeners){
        if(listeners.nodeClick){
            var self = this;
            if($.isString(path)){
                var texts = $(path+" span[type='text'][clickable='true']");
            }else{
                var texts = path;
            }
            
            if(texts.attr("binded")=="true"){
                return;
            }
            
            texts.bind("click",function(event){
            	//event.stopPropagation();
                $text = $(this);
                self.clearCurrentNode();
                self.node.setNode($text.parent());
                self.node.click(listeners);
            }).attr("binded","true");
        }
    },
    /*
     * 绑定复选框事件
     */
    bindCheckbox:function(path,listeners){  //checkboxClickBefore checkboxClick  checkboxClickAfter
        if($.isString(path)){
            var checkboxs = $(path+" input[type='checkbox']");
        }else{
            var checkboxs = path;
        }
    
        var self = this;
        var _setcheckboxbefor=true;
        if(listeners.checkboxClickBefore){
            checkboxs.bind("mousedown",function(){
                var $checkbox = $(this);
                self.node.setNode($checkbox.parent());
                _setcheckboxbefor = listeners.checkboxClickBefore(self.node);
                $checkbox.data("checkBeforeFlag", _setcheckboxbefor).focus();
                if (!_setcheckboxbefor) return false;
            });
        }


        checkboxs.bind("change",function(){
            var $checkbox = $(this);
            if($checkbox.data("checkBeforeFlag") !== false){
                var node = self.node._createNode($checkbox.parent());                
                var flag =     node.checkboxClick();
                if(flag===false) return flag;
                if(listeners.checkboxClick){
                    flag = listeners.checkboxClick(node);
                    if(flag===false) return flag;
                }
            }
        });
    },
    bindToolclick:function(path,listeners){
        if(listeners.toolClick){
            
            if($.isString(path)){
                var tools = $(path+" span[type='tool']");
            }else{
                var tools = path;
            }
            var self = this;
            if(tools.attr("binded")=="true"){
                return;
            }
            
            
            tools.bind("click",function(event){
            	//event.stopPropagation();
                var $tool = $(this);
                self.node.setNode($tool.parent());
                listeners.toolClick(self.node,event);
            }).attr("binded","true");
        }
    },
    /**
     * 获得当前选中的节点
     * @method getCurrentNode
     * @return {Node}
     * */
    getCurrentNode:function(){
        if(!this.currentNode){
            var $text = this.root.find("span[type='text'][isCurrent='true']");
            if($text[0]){
                this.currentNode = $text.parent();
            }
        }
        if(this.currentNode!=null){            
            var node = this.node._createNode(this.currentNode);
            return node;
        }else{
            return null;
        }
    },    
    /**
     * 设置当前选中节点
     * @method setCurrentNode
     * @param nodeId {String} 节点ID
     * @chainable
     * */
    setCurrentNode:function(nodeId){  //设置树当前选中节点
        this.clearCurrentNode();
        var node = this.getNodeById(nodeId);
        node.setCurrentNode();
        return this;
    },
    /**
     * 清除当前选中节点
     * @method clearCurrentNode
     * @chainable
     * */
    clearCurrentNode:function(){  //清除当前选定
        var currentnode = this.getCurrentNode();
        if(currentnode) currentnode.clearCurrentNode();
        return this;
    },
    /**
     * 获得搜索定位节点
     * @method getCurrentNode
     * @return {Node}
     * */
    getSearchNode:function(){
        if(!this.searchnode){
            var $text = this.root.find("span[type='text'][isSearch='true']");
            if($text[0]){
                this.searchnode = $text.parent();
            }
        }
        if(this.searchnode!=null){            
            this.node.setNode(this.searchnode);
            return this.node;
        }else{
            return null;
        }
    },
    /**
     * 清除当前搜索定位节点
     * @method clearSearchNode
     * @chainable
     * */
    clearSearchNode:function(){  //清除当前搜索定位节点
        var searchnode = this.getSearchNode();
        if(searchnode) searchnode.clearSearchNode();
        return this;
    },
    /**
     * 根据节点ID获得节点对象
     * @method getNodeById
     * @param nodeId {String}
     * @return {Node}
     * */
    getNodeById:function(nodeId){  //根据ID所有节点
        var $li = this.root.find("li[nodeid='"+nodeId+"']");
        if(!$li[0]) return null;
        this.node.setNode($li);
        return this.node;
    },
    
    /**
     * 根据节点属性获得节点对象
     * @method getNodeById
     * @param attrName {String} 节点属性名称
     * @param attrVal {String} 节点属性值
     * @return {Node}
     * */
    getNodeByAttr:function(attrName,attrVal){//根据自定义属性查找节点
        var $li = this.root.find("li["+attrName+"='"+attrVal+"']");
        if(!$li[0]) return null;
        this.node.setNode($li);
        return this.node;        
    },
    /**
     * 更改制定节点文本
     * @method udpataNodeTextById
     * @param nodeId {String} 节点ID
     * @param text {String} 节点文本
     * */
    updateNodeTextById:function(nodeId,text){
        var node = this.getNodeById(nodeId);
        return node.setText(text);
    },
    /**
     * 获得单选选中的节点
     * @method getRadioCheckedNode
     * @return {Node}
     * */
    getRadioCheckedNode:function(){
        var $radio = this.root.find("input[type='radio']");
        for(var i=0;i<$radio.length;i++){
            if($radio[i].checked){
                var $li = $($radio[i]).parent();
                this.node.setNode($li);
                return this.node;
            }
        }
        return null;
    },
    /**
     * 搜索节点
     * @method searchNode
     * @param nodeId {String} 节点ID
     * */
    searchNode:function(nodeId){
        var node = this.getNodeById(nodeId);
        var contentTop;
        var nodeTop;
        if(node){
            node.expandUP();
            node.setSearchNode();
            contentTop = node.$li.parents('.tree-wrap:first').position().top;
            nodeTop = node.$li.position().top;
            this.scrollToPos(nodeTop - contentTop);
        }
    },
    /**
     * 定位节点
     * @method locationNode
     * @param nodepath {Array} 节点ID集合
     * */
    locationNode:function(nodepath){
        for(var i=0,len = nodepath.length-1;i<len;i++){
            var node = this.getNodeById(nodepath[i]);
            node.expend(false);
        }
        this.searchNode(nodepath[nodepath.length-1]);
    },
    /**
     * 获得选中节点个数
     * @method getCheckedCount
     * @return {Number} 
     * */
    getCheckedCount:function(){
        var checkboxs = $("#"+this.id+" input[type='checkbox']");
        var checkboxeds = checkboxs.filter(":checked");
        return checkboxeds.length;
    },
    /**
     * 指定id节点复选框取消
     * @method clearCheckedByNodeid
     * @param nodeId {String} 节点ID
     */
    clearCheckedByNodeid:function(nodeId){
        var node = this.getNodeById(nodeId);
        if(node){
            node.clearChecked();
        }
    },
    /**
     * 清除所有选中节点
     * @method clearAllNodeChecked
     * */
    clearAllNodeChecked:function(){
        var checkboxs = $("#"+this.id+" input[type='checkbox']:checked").removeAttr("checked");
    },
    /**
     * 设置某个节点选中
     * @method setCheckedByNodeid
     * @param nodeId {String} 节点ID
     */    
    setCheckedByNodeid:function(nodeId, isRelate){
        var node = this.getNodeById(nodeId);
        if(node){
            node.setChecked(isRelate);
        }
    },
    /**
     * 批量设置节点复选中选中
     * @method setCheckedByNodeIds
     * @param {Array} 节点ID数组
     * @chainable
     */
    setCheckedByNodeIds:function(nodeIds){
        var $li = null, $ul = null;;
        for(var i=0,len  = nodeIds.length;i<len;i++){
            $li = this.$tree.find('li[nodeid="' + nodeIds[i] + '"]>input:checkbox').attr("checked","checked");
        }
        var root = this.getRoot();
        function roundCheck(childrens){
            var $li = null, $ul = null;
            for(var i = 0, len = childrens.length; i < len; i++){
                $li = $(childrens[i]);
                $ul = $li.parent();
                if($ul.attr("isCheckedCount")){
                    continue;
                }
                var childs = $ul.children();
                /*
                var checkedCount = 0;
                console.info();
                for(var j = 0, jlen = childs.length; j < jlen; j++){
                    if($(childs[j]).children("input:checkbox:checked")[0]){                        
                        checkedCount++;
                    }
                }*/
                if($ul.find(">li>input:checkbox:checked").length != 0){
                    $ul.attr("isCheckedCount", true);
                    $ul.prevAll("input:checkbox").attr("checked","checked");
                }
                var $parent = $ul.parent().parent().parent();//ul-li-ul-li
                if($parent.attr("nodeid") != "root"){
                    roundCheck($parent.children("ul").children());    
                }                
            }
        }
        var checked = this.$tree.find("input:checkbox:checked");        
        roundCheck(checked.parent());        
    },
    /**批量展开*/
    expendByIds : function(nodeIds){
        var $li = null;
        for(var i = 0, len = nodeIds.length; i < len; i++){
            $li = this.$tree.find('li[nodeid="' + nodeIds[i] + '"]');
            if($li.hasClass("expandable")){
                $li[0].className = "collapsable";
                $li.children("div")
                $btn.removeClass("expandable-hitarea").addClass("collapsable-hitarea");
            }
            
            if($li.hasClass("lastExpandable")){
                $li.removeClass("lastExpandable").addClass("lastCollapsable");
                $btn.removeClass("lastExpandable-hitarea").addClass("lastCollapsable-hitarea");
            }                        
            if(this._get$Ico().hasClass(closeClassName)){
                this._get$Ico().removeClass(closeClassName).addClass(openClassName);    
            }
        }
        
    },
    /**
     * 设置树自定义参数
     * @method setParam
     * @chainable
     * @param param {JSON}
     * */
    setParam:function(param){
        this.param = param;
        return this;
    },
    /**
     * 定位到第一个选择的节点
     * @method scrollToFirstChecked
     */
    scrollToFirstChecked : function () {
        var aChecked = this.getCheckedHierarchy();
        var oNode = aChecked[0];
        var iTop;
        if (oNode) {
            while (!oNode.isLeaf) oNode = oNode.childs[0];
            iTop = this.$tree.find('li[nodeid="' + oNode.id + '"]').position().top;
        }else {
            oNode = this.getRadioCheckedNode();
            if (oNode) {
                iTop = oNode.$li.position().top;
            } 
        }
        if (null != iTop && undefined !== iTop) {
            this.scrollToPos(iTop);            
        }
    },
    
    scrollToPos : function (yPos) {
        if (this.$tree.parent().parent().hasClass('jspPane')) {
            // jsScrollPane方式
            var api = this.$tree.parent().parent().parent().parent().data('jsp');
            api.scrollToY(yPos);
        }else {
            // 普通方式
            this.$tree.parent().scrollTop(0);
            this.$tree.parent().scrollTop(yPos);
        }
    },
    /**
     * 获得根节点
     * @method getRoot
     * @return {Node}
     * */
    getRoot:function(){
        var $li = this.$tree.children("li");
        this.node.setNode($li);
        return this.node;
    },
    /**
     * 判断树是否为空
     * @method isEmpty
     * @return {Boolean}
     * */
    isEmpty:function(){
        return this.getRoot()._get$Childs()[0]==null ? true : false;
    }
};

var TreePlugins = {
    /*
     * 树节点只能展开一级节点，互斥
     * @param conf {
     *     listeners:{expend:展开事件}
     * }
     * */
    singleExpend:function(conf){
        var listeners = conf.listeners == null ? {} : conf.listeners;
        var self = this;
        var fn = listeners.expend;
        listeners.expend = function(node){
            var silblinguls = node.$li.siblings("li").children("ul:visible");
            if(silblinguls[0]){
                self.node.setNode(silblinguls.parent());
                self.node.collapse();
            }
            if(fn)    fn(node);
        };
    },
    /*
     * 选中一定数量复选框后，将其他复选框disabled,如果低于则恢复
     */
    disableCheckbox:function(conf){
        var selectcount = conf.selectcount;
        var listeners = conf.listeners == null ? {} : conf.listeners;
        var self = this;
        var fn = listeners.checkboxClick;
        listeners.checkboxClick = function(node){
            var checkbox = node.$li.children("input[type='checkbox']");
            if(checkbox.hasClass("disabled")){
                return false;
            }
            var checkboxs = $("#"+conf.id+" input[type='checkbox']");
            var checkboxeds = checkboxs.filter(":checked");
            
            var s = new Date();
            if(checkbox[0].checked){
                if(checkboxeds.length>=selectcount){
                    checkboxs.not(":checked").addClass("disabled");
                }else{
                }
            }else{
                checkboxs.not(":checked").removeClass("disabled");
            }
            if(fn)    fn(node);
        };
    },
    /*
     * 创建一个树节点
     * conf{
     *     isLast:是否为最后一个节点
     *  isLeaf：是否为叶子节点
     *  isExpend:默认是否展开
     *  attrs:自定义属性
     *  isBtn：是否有折叠按钮
     *  isCheckBox：是否有checkbox
     *  isRadio:是否有单选框
     *  isClick:是否可以点击
     *  toolCls：工具按钮样式按钮
     *  icoCls：节点图标样式
     *  nodeId：节点Id
     *  text:节点显示文本
     * }
     */
    createNode:function(conf){
        var isLast = conf.isLast,isLeaf = conf.isLeaf,isExpend = conf.isExpend;
        var cls = "";
        if(isLast && isExpend && !isLeaf){ 
            cls = "collapsable lastCollapsable";  
        }else if(isLast && !isExpend && !isLeaf){ 
            cls = "expandable lastExpandable";
        }else if(!isLast && !isExpend && !isLeaf){  //如果不是最后一个，并且是关闭的，不是叶子节点expandable
            cls = "expandable";
        }else if(!isLast && isExpend && !isLeaf){ //如果不是最后一个，并且是展开的，不是叶子节点collapsable
            cls = "collapsable";
        }else if(isLast && isLeaf){  //如果是最后一个，并且是叶子节点
            cls = "last";
        }
        var li = '<li class="'+cls+'" nodeid="'+conf.nodeId+'" ';
        if(conf.attrs){
            for(var attr in conf.attrs){
                li += attr+"='"+conf.attrs[attr]+"' ";
            }
        }
        li+=">";
        if(conf.isBtn){
            li += this.createButton(isLast,isLeaf,isExpend);
        }
        if(conf.isCheckBox === true){
            li += this.createInput("checkbox",conf.inputAttrs);
        }
        if(conf.icoCls){
            li += this.createIco(conf.icoCls);
        }
        if(conf.img){
            li += this.createImg(conf.img);
        }
        
        if(conf.isRadio === true){
            li += this.createInput("radio",conf.inputAttrs);
        }
        
        li += this.createText(conf.isClick,conf.text);

        if(conf.toolCls){
            li += this.createTool(conf.toolCls);
        }
        li+="</li>";
        return li;
    },
    createTool:function(toolCls){
        return '<span class="'+toolCls+'" type="tool"></span>';
    },
    createIco:function(icoCls){
        return '<span class="'+icoCls+'" type="ico"></span>';
    },
    createImg:function(src){
        return '<img src="'+src+'"/>';
    },
    createText:function(isClick,textVal){
        var pointer = isClick ? 'cursor: pointer;' :'';
        return '<span style="'+pointer+'"  type="text" clickable="'+isClick+'">'+textVal+'</span>';
    },
    createButton:function(isLast,isLeaf,isExpend){
        var cls = "";
        if(isLast && isExpend && !isLeaf){
            cls = "hitarea collapsable-hitarea lastCollapsable-hitarea";
        }else if(isLast && !isExpend && !isLeaf){
            cls = "hitarea expandable-hitarea lastExpandable-hitarea";
        }else if(!isLast && !isExpend && !isLeaf){
            cls="hitarea expandable-hitarea";
        }else if(!isLast && isExpend && !isLeaf){
            cls="hitarea  collapsable-hitarea";
        }else{
            cls="last";
        }
        if(isLeaf){
            cls += " placeholder";
        }
        return '<div class="'+cls+'"></div>';
    },
    createInput:function(type,attrs){
        var input =  '<input type="'+type+'" ';
        if(attrs){
            for(var i=0;i<attrs.length;i++){
                input += attr+"="+attrs[attr]+" ";
            }
        }
        input += "/>";
        return input;
    }
};


/*
 * 两根树左右通过checkbox选中左右移动功能
 * @param fromTree 移除节点的树对象
 * @param fromTreeNodes 移除节点集合，通过调用树的getCheckedHierarchy()方法获得
 * @param toTree 加入节点的树对象
 * 
 * */
function TreeRound(fromTree,fromTreeNodes,toTree,noChange){
    round(fromTreeNodes);
    fromTree.clearAllNodeChecked();
    function round(nodes){
        var node = null;
        for(var i=0,len = nodes.length;i<len;i++){
            node = nodes[i];
            var toTreeParentNode = null; 
            if(!toTree.getNodeById(node.id)){
                TreeRound.conf.nodeId = node.id;
                TreeRound.conf.text = node.text;    
                TreeRound.conf.attrs= node.attrs;
                TreeRound.conf.img = node.img;
                if(node.parentId == "root"){                            
                    toTree.getRoot().appendChild(TreeRound.conf);
                }else{
                    toTree.getNodeById(node.parentId).appendChild(TreeRound.conf);
                }
            }                    
            var fromNode = fromTree.getNodeById(node.id);
            if(fromNode.isLeaf()){
                var fromTreeParentNode = fromNode.parent();
                //fromNode.delNode();
                if(fromTreeParentNode.isLeaf()){
                    //fromTreeParentNode.delNode();
                }else{
                    if(i+1 == len){
                        var text = fromTreeParentNode.getText();
                        if(text){
                            //fromTreeParentNode.setText(text.replace(/\(.+\)/g, "("+fromTreeParentNode.childCount()+")"));
                        }
                    }                        
                }    
            }else{
                round(node.childs);
            }
            if(i+1 == len){
                if(!noChange && toTree.getNodeById(node.parentId)){
                    var text = toTree.getNodeById(node.parentId).getText();                            
                    toTree.getNodeById(node.parentId).setText(text.replace(/\(.+\)/g, "("+toTree.getNodeById(node.parentId).childCount()+")"));
                }
            }
        }
    }    
}


/*
 * 两根树左右通过checkbox选中左右移动功能，异步版本
 * @param fromTree 移除节点的树对象
 * @param fromTreeNodes 移除节点集合，通过调用树的getCheckedHierarchy()方法获得
 * @param toTree 加入节点的树对象
 * @param callback 回调
 * */
function TreeRoundAsyn(fromTree,fromTreeNodes,toTree,callback){
    var copyNodes = $.copyArray(fromTreeNodes);
    fromTree.clearAllNodeChecked();
    var newArray = [];
    var interval = setInterval(function(){        
        if(copyNodes.length <= 0 && newArray.length == 0){
            clearInterval(interval);
            if(callback){callback();}
        }else{
            var childs = [];
            if(copyNodes.length <=0){
                copyNodes = newArray;                            
            }

            var nodes = copyNodes.splice(0,10);
            var node = null;
            for(var i = 0,len = nodes.length; i < len; i++){
                node = nodes[i];
                var toTreeParentNode = null; 
                if(!toTree.getNodeById(node.id)){
                    TreeRound.conf.nodeId = node.id;
                    TreeRound.conf.text = node.text;    
                    TreeRound.conf.attrs= node.attrs;
                    TreeRound.conf.img = node.img;
                    if(node.parentId == "root"){                            
                        toTree.getRoot().appendChild(TreeRound.conf);
                    }else{
                        toTree.getNodeById(node.parentId).appendChild(TreeRound.conf);
                    }
                }                    
                var fromNode = fromTree.getNodeById(node.id);
                if(fromNode.isLeaf()){
                    var fromTreeParentNode = fromNode.parent();
                    //fromNode.delNode();
                    if(fromTreeParentNode.isLeaf()){
                        //fromTreeParentNode.delNode();
                    }else{
                        if(i+1 == len){
                            var text = fromTreeParentNode.getText();
                            if(text){
                                fromTreeParentNode.setText(text.replace(/\(.+\)/g, "("+fromTreeParentNode.childCount()+")"));
                            }
                        }                        
                    }    
                }else{
                    childs = childs.concat(node.childs);
                }
                if(i + 1 == len){
                    if(toTree.getNodeById(node.parentId)){
                        var text = toTree.getNodeById(node.parentId).getText();                            
                        toTree.getNodeById(node.parentId).setText(text.replace(/\(.+\)/g, "("+toTree.getNodeById(node.parentId).childCount()+")"));
                    }
                }    
                newArray = childs;
            }            
        }
    },10);

}


TreeRound.conf = {
    isLast: false,
    isLeaf: true,
    isExpend: true,
    img : null,
    isBtn: true,
    isClick: false,            
    nodeId: "",
    text: "",
    isCheckBox:true
}
/**
 * 删除树节点
 * @param tree 树
 * @param nodes 选中节点
 */
function TreeDelNode(tree, nodes) {
    round(nodes);
    tree.clearAllNodeChecked();
    function round(nodes){
        var node = null;
        for(var i = 0, len = nodes.length; i < len; i++) {
            node = nodes[i];
            var fromNode = tree.getNodeById(node.id);
            
            if(fromNode.isLeaf() || node.childs == null){
                var fromTreeParentNode = fromNode.parent();
                fromNode.delNode();
                if(fromTreeParentNode.isLeaf()){
                    fromTreeParentNode.delNode();
                }
            }else {
                round(node.childs);
                fromNode = tree.getNodeById(node.id);
                if(fromNode != null && fromNode.isLeaf()) {
                    fromNode.delNode();
                }
            }
        }
    }
};(function($) {'use strict';
////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部变量
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部函数
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造
////////////////////////////////////////////////////////////////////////////////////////////////////
	var _c_ = window.DragMove = function(conf_) {
		this._target = conf_.target;
		this._sameLevelOnly = conf_.sameLevel;
		this._listeners = {};
		this._initUI();
		this._bindEvent();
	};
	
	var _p_ = _c_.prototype = {};


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象变量
////////////////////////////////////////////////////////////////////////////////////////////////////
	_p_._listeners = null;

////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取所有元素的dom
	 * @method  _getItems
	 * @return {Array} doms
	 */
	_p_._getItems = null; // function();

	/**
	 * 获取元素Y轴坐标
	 * @method _getItemTop
	 * @param  {dom 或 jQuery对象} item 元素
	 * @return {Numeric} Y轴坐标
	 */
	_p_._getItemTop = function (item) {
		var offset = $(item).offset();
		if (offset) return offset.top;
	};

	/**
	 * 获取元素元素高度
	 * @method _getItemHeight
	 * @param  {dom 或 jQuery对象} item 元素
	 * @return {Numeric} 高度
	 */
	_p_._getItemHeight = function (item) {
		return $(item).height();
	};

	/**
	 * 根据鼠标位置确定插入位置
	 * @method _getItemByOffset
	 * @param  {Plain Object} 鼠标坐标 {left, top}
	 * @return {Plain Object} 插入方式 {
	 *     item: 相对与这个元素, 
	 *     before: 这个元素之前或之后, 
	 *     asChild:作为这个元素的子节点
	 * }
	 */
	_p_._getItemByOffset = function(offset_) {
		var insertion1 = this._getItemByTop(offset_.top);
		if (!insertion1) return null;

		var insertion2 = this._getItemByLeft(offset_.left, insertion1.item);

		if (insertion2 && insertion1.item === insertion2.item && insertion1.before) {
			return insertion1;
		}else return insertion2 || insertion1;
	};

	_p_._getMovingItem = function(top_) {
		var doms = this._getItems();
		var prevDom, dom, top, prevTop, middle, bottom, c = doms.length;

		while (c--) {
			dom = doms[c];
			top = this._getItemOffset(dom).top;
			bottom = top + this._getItemHeight(dom);
			
			if (prevDom) {
				// 当前元素与现在元素中间分界线，前一个遍历元素即为所求
				middle = (prevTop - bottom) / 2 + bottom;
				// 首次线越过点时，前一个便利
				if (middle <= top_) {
					return prevDom;
				}
			}else if (bottom <= top_) {
				// 如果第一个元素的底线越过了点，当前元素即为所求
				return dom;
			}
			prevTop = top;
			prevDom = dom;
		}

		// 如果没有任何线越过点，最后一个（最上边）元素即为所求
		return dom;
	};

	/**
	 * 根数Y轴初步确定插入位置
	 * @method  _getItemByTop
	 * @param {Numeric} top_ Y轴
	 * @return {Plain Object} {item: 目标元素, before: 是否前插}
	 */
	_p_._getItemByTop = function(top_) {
		var doms = this._getItems();
		var dom, vcenter, c = doms.length;
		while (c--) {
			dom = doms[c];
			vcenter = this._getItemOffset(dom).top + this._getItemHeight(dom) / 2;
			// 有越线时，在该元素后插
			if (vcenter <= top_) {
				return {
					item : dom,
					before : false
				};
			}
		}

		// 无越线时，在首个元素前插
		return {
			item: dom,
			before : true
		};
	};

	/**
	 * 根数X轴确定插入位置
	 * @method _getItemByLeft
	 * @param {Numeric} left_ X轴
	 * @return {Plain Object} {item: 目标元素, before: 是否前插, asChild: 是否作为子节点}
	 */
	_p_._getItemByLeft = function(left_, startItem) {
		var parentItem;
        var prevDragData;
		var leftStart = left_ - startItem.offset().left;
		if (leftStart < this._getSameLevelLeft()) {// 小于该范围时
			parentItem = this._getParentItem(startItem);
			if (parentItem) {// 有父节点时，找父节点判定
				prevDragData = this._getItemByLeft(left_, parentItem);
                return prevDragData || {
                    item : parentItem,
                    before : false,
                    asChild : false
                };
			}else {
				return {
					item : startItem
				};
			}
		}else if (leftStart > this._getSubLevelLeft()) {// 大于该范围时
			return {// 作为首个子节点
				item : startItem,
				before : false,
				asChild : true
			};
		}
		// 其它情况维持原判定
	};

	/**
	 * 获取坐标信息
	 * @method _getItemOffset
	 * @param  {item} item
	 * @return {Plain Object} {left, top}
	 */
	_p_._getItemOffset = null; // function (item) {};

	/**
	 * 获取指定元素的父级元素
	 * @method _getParentItem
	 * @param {元素} item 指定元素
	 * @return {元素} 父级元素
	 */
	_p_._getParentItem = null; // function (item) {};

	_p_._endDrag = function (cancel) {
		if (this._insertion) {
			this._clearInsert(this._insertion.item);
			if (this._insertion.accept && !cancel) {
				this._commitInsert(this._movingItem, this._insertion);
			}
		}
		if (this._movingItem) this._clearMoving(this._movingItem);
		this._movingItem = null;
		this._insertion = null;
		this._onItem = null;
	};

	_p_._startDrag = function () {
		this._showMoving(this._movingItem);
	};

	_p_._moving = function (offset_) {
		if (this._insertion) this._clearInsert(this._insertion.item);
		this._insertion = this._getItemByOffset(offset_);
		if (this._insertion) {
			this._onItem = this._getMovingItem(offset_.top);
			if (this._sameLevelOnly) {
				if (this._insertion.asChild && this._onItem.parent()[0] === this._insertion.item.parent()[0]) {
					this._insertion.asChild = false;
				} else if (this._insertion.asChild && this._onItem[0] !== this._insertion.item.parent()[0]) {
					return;
				} else if (!this._insertion.asChild && this._onItem.parent()[0] !== this._insertion.item.parent()[0]) {
					return;
				}
			}
			this._insertion.accept = this._isAcceptable(this._movingItem, this._insertion);
			this._showInsert(this._insertion);
		}
	};

	/**
	 * 提交修改
	 * @method _commitInsert
	 * @param {元素} item_ 移动的元素
	 * @param {Plain Object} insertion_ 插入位置 {
	 *     item: 相对与这个元素, 
	 *     before: 这个元素之前或之后, 
	 *     asChild:作为这个元素的子节点
	 * }
	 */
	_p_._commitInsert = null; // function(item_, insertion_) {};

	/**
	 * 显示插入效果
	 * @method _showInsert
	 * @param {Plain Object} insertion 插入位置 {
	 *     item: 相对与这个元素, 
	 *     before: 这个元素之前或之后, 
	 *     asChild:作为这个元素的子节点
	 * }
	 * @param {Boolean} accepted 是否可以
	 */
	_p_._showInsert = null; // function(insertion, accepted) {};

	/**
	 * 显示移动效果
	 * @method _showMoving
	 * @param {元素} item 受影响的元素
	 */
	_p_._showMoving = function(/*item*/) {
	};

	/**
	 * 清除插入效果
	 * @method _clearInsert
	 * @param {元素} item 受影响的元素
	 */
	_p_._clearInsert = null; // function() {};

	/**
	 * 清除移动效果
	 * @method _clearMoving
	 * @param {元素} item 受影响的元素
	 */
	_p_._clearMoving = function(/*item*/) {
		this._dragDiv.css('cursor', 'default');
	};

	_p_._initUI = function() {
		this._dragDiv = $('<div drag="drag"></div>').css({
			padding: 0,
			margin: 0,
			position: 'absolute'
		}).appendTo(this._target);
	};

	_p_._showDraggingArea = function() {
		if (this._dragDivShow) return;
		var targetOffset = $(this._target).offset();
		targetOffset.top -= 20;
		targetOffset.left -= 20;

		this._dragDivShow = true;
		this._dragDiv
			.height($(this._target).outerHeight() + 50)
			.width($(this._target).outerWidth() + 40)
			.css('zIndex', window.ZIndexMgr.get())
			.show()
			.offset(targetOffset);
	};

	_p_._resizeDraggingArea = function() {
		if (!this._dragDivShow) return;
		this._dragDiv
			.height($(this._target).outerHeight() + 50)
			.width($(this._target).outerWidth() + 40);
	};

	_p_._hideDraggingArea = function() {
		if (!this._dragDivShow) return;
		this._dragDivShow = false;
		var $dragDiv = $('div[drag="drag"]', this._target).hide();
		window.ZIndexMgr.free($dragDiv);
	};

	_p_._bindEvent = function() {
		this._bindTargetMousedown();
		this._bindMousemove();
		this._bindMouseup();
	};

	_p_._bindTargetMousedown = function() {
		var _this_ = this;
		$(this._target).mousedown(this._onTargetMousedown = function(event) {
			if (event.button !== 0 && ($.isIE() && event.button !== 1)) return;

			_this_._movingItem = _this_._getMovingItem(event.pageY);

			_this_._unbindTargetMousedown();
			_this_._bindTargetMousemove();
			_this_._bindTargetMouseup();
		});
	};

	_p_._bindTargetMouseup = function() {
		var _this_ = this;
		$(this._target).mouseup(this._onTargetMouseup = function() {
			_this_._unbindTargetMouseup();
			_this_._unbindTargetMousemove();
			_this_._bindTargetMousedown();
		});
	};

	_p_._bindTargetMousemove = function() {
		var _this_ = this;
		$(this._target).mousemove(this._onTragetMousemove = function(event) {
			if (_this_._movingItem) {
				_this_._showDraggingArea();
				_this_._startDrag({left:event.pageX, top:event.pageY});
			}else {
				_this_._unbindTargetMousemove();
				_this_._unbindTargetMouseup();
				_this_._bindTargetMousedown();
			}
		});
	};

	_p_._unbindTargetMousedown = function() {
		$(this._target).unbind('mousedown', this._onTargetMousedown);
	};

	_p_._unbindTargetMouseup = function() {
		$(this._target).unbind('mouseup', this._onTargetMouseup);
	};

	_p_._unbindTargetMousemove = function() {
		$(this._target).unbind('mousemove', this._onTragetMousemove);
	};

	_p_._bindMousemove = function() {
		var _this_ = this;
		$(this._dragDiv).mousemove(function(event) {
			if (event.which === 1) {
				_this_._moving({left:event.pageX, top:event.pageY});
			}else {
				_this_._endDrag(true);
				_this_._hideDraggingArea();
			}
		});
	};

	_p_._bindMouseup = function() {
		var _this_ = this;
		$(this._dragDiv).mouseup(function(event) {
			if (event.button === 0 || ($.isIE() && event.button === 1)) {
				_this_._endDrag();
			}else {
				_this_._endDrag(true);
			}
			_this_._hideDraggingArea();
		});
	};

////////////////////////////////////////////////////////////////////////////////////////////////////
// 类型变量
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 类型函数
////////////////////////////////////////////////////////////////////////////////////////////////////


})(window.jQuery);;(function($) {'use strict';
////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部变量
////////////////////////////////////////////////////////////////////////////////////////////////////
	var G_INSERT_ACCPETED = 'tree_drag';
	var G_INSERT_FOBIDDON = 'tree_drag_false';

////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部函数
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造
////////////////////////////////////////////////////////////////////////////////////////////////////
	var _s_ = window.DragMove;
	var _c_ = window.DragMoveTree = function(conf_) {
		_s_.apply(this, arguments);
		
		this._tree = conf_.tree;
		if (conf_.listeners && conf_.listeners.accept) {
			this._listeners.accept = conf_.listeners.accept;
		}
		if (conf_.listeners && conf_.listeners.move) {
			this._listeners.move = conf_.listeners.move;
		}
	};
	
	var _p_ = window.classExtend(_c_, _s_);


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象变量
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	_p_._isAcceptable = function(item, insertion) {
		var parentItem;
		if (insertion.item[0] === item[0]) {
			// window.console.log('移动：禁止，同一个元素');
			return false;
		}

		if (!insertion.asChild) {
			if (insertion.before && insertion.item.prev('li')[0] === item[0]) {
				// window.console.log('移动：禁止，前一个元素后插');
				return false;
			}
			if (!insertion.before && insertion.item.next('li')[0] === item[0]) {
				// window.console.log('移动：禁止，后一个元素前插');
				return false;
			}
		}else if (!item.prev('li')[0]) {
			// window.console.log('移动：禁止，作为首个元素时，不能作为父元素首个子');
			parentItem = this._getParentItem(item);
			if (parentItem && parentItem[0] === insertion.item[0]) return false;
		}

		if ($.contains(item[0], insertion.item[0])) {
			// window.console.log('移动：禁止，作为自己的子');
			return false;
		}
	
		if (this._listeners.accept) return this._listeners.accept(item, insertion);

		return true;
	};

	/**
	 * 获取所有元素的dom
	 * @method  _getItems
	 * @return {Array} doms
	 */
	_p_._getItems = function(item, items) {
		var _this_ = this;
		var isRoot = !item;

		if (!item) {
			items = [];
			item = this._tree.getRoot().$li;
		}

		if (!isRoot) items.push(item);
		$('ul:first:visible>li', item).each(function(index, sub) {
			_this_._getItems($(sub), items);
		});

		return items;
	};

	/**
	 * 获取元素元素高度
	 * @method _getItemHeight
	 * @param  {dom 或 jQuery对象} item 元素
	 * @return {Numeric} 高度
	 */
	_p_._getItemHeight = function (item, withSub) {
		var $ul = item.children('ul:first:visible');
		if ($ul[0] && !withSub) {
			return item.outerHeight() - $ul.outerHeight();
		}else {
			return item.outerHeight();
		}
	};

	/**
	 * 获取坐标信息
	 * @method _getItemOffset
	 * @param  {item} item
	 * @return {Plain Object} {left, top}
	 */
	_p_._getItemOffset = function (item) {
		return item.offset();
	};

	/**
	 * 获取指定元素的父级元素
	 * @method _getParentItem
	 * @param {元素} item 指定元素
	 * @return {元素} 父级元素
	 */
	_p_._getParentItem = function (item) {
		var $parent = item.parent('ul').parent('li');
		if ($parent[0] && !$parent.parent().hasClass('tree-wrap')) return $parent;
	};

	/**
	 * 提交修改
	 * @method _commitInsert
	 * @param {元素} item_ 移动的元素
	 * @param {Plain Object} insertion_ 插入位置 {
	 *     item: 相对与这个元素, 
	 *     before: 这个元素之前或之后, 
	 *     asChild:作为这个元素的子节点
	 * }
	 */
	_p_._commitInsert = function(item_, insertion_) {
		var dummyNode = this._tree.getRoot()._createNode();
		var desItem = insertion_.item;
		var nowParentItem = insertion_.asChild ? desItem : this._getParentItem(desItem);
		var oldParentItem = this._getParentItem(item_);
		var $ul;

		/*****************************************************************************/
		// var msg;
		// if (insertion_.asChild) msg = '(子)';
		// else if (insertion_.before) msg = '(前)';
		// else msg = '(后)';
		// window.console.log('移动：' + item_.children('span:last').text() +
		// 	' --> ' + insertion_.item.children('span:last').text() + msg);
		/*****************************************************************************/

		// 修改移动节点位置
		if (insertion_.asChild) {
			$ul = desItem.children('ul:first');
			if (!$ul[0]) $ul = $('<ul></ul>').appendTo(desItem);
			$ul.prepend(item_);
		}else if (insertion_.before) {
			desItem.before(item_);
		}else {
			desItem.after(item_);
		}
		dummyNode.setNode(item_);
		if (dummyNode.update) dummyNode.update();

		// 修改原来父节点“叶子状态”
		if (oldParentItem) {
			dummyNode.setNode(oldParentItem);
			if (oldParentItem.children('ul').children('li')[0]) {
				dummyNode.setNotLeaf(!oldParentItem.next('li')[0]);
			}else {
				dummyNode.setLeaf(!oldParentItem.next('li')[0]);
			}
		}

		// 修改现在父节点“叶子状态”
		if (nowParentItem) {
			dummyNode.setNode(nowParentItem);
			dummyNode.expend();
			if (insertion_.asChild) {
				if (nowParentItem.children('ul').children('li')[0]) {
					dummyNode.setNotLeaf(!nowParentItem.next('li')[0]);
				}else {
					dummyNode.setLeaf(!nowParentItem.next('li')[0]);
				}
			}
		}

		if (this._listeners.move) {
			this._listeners.move(item_, oldParentItem);
		}
	};

	/**
	 * 显示插入效果
	 * @method _showInsert
	 * @param {Plain Object} insertion 插入位置 {
	 *     item: 相对与这个元素, 
	 *     before: 这个元素之前或之后, 
	 *     asChild:作为这个元素的子节点
	 * }
	 * @param {Boolean} accepted 是否可以
	 */
	_p_._showInsert = function(insertion) {
		var offset = this._getInsertOffset(insertion);
		var className = insertion.accept ? G_INSERT_ACCPETED : G_INSERT_FOBIDDON;
		this._dragDiv
			.removeClass(G_INSERT_ACCPETED + ' ' + G_INSERT_FOBIDDON).addClass(className)
			.css('backgroundPosition', offset.left + 'px ' + offset.top + 'px');
		this._dragDiv.css('cursor', insertion.accept ? 'default' : 'no-drop');

		this._delayExpand(insertion.item);
	};

	/**
	 * 清除插入效果
	 * @method _clearInsert
	 */
	_p_._clearInsert = function(/*item*/) {
		this._dragDiv.removeClass(G_INSERT_ACCPETED + ' ' + G_INSERT_FOBIDDON);
	};

	_p_._getInsertOffset = function(insertion) {
		var dragDivOffset = this._dragDiv.offset();
		var itemOffset = insertion.item.offset();
		var offset = {
			left : itemOffset.left - dragDivOffset.left,
			top : itemOffset.top - dragDivOffset.top
		};
		
		if (!insertion.before) {
			offset.top += this._getItemHeight(insertion.item, !insertion.asChild);
		}

		if (!insertion.asChild) {
			offset.left += this._getSameLevelLeft(insertion.item);
		}else {
			offset.left += this._getSubLevelLeft(insertion.item);
		}

		// if (insertion.before) {
		// 	offset.top -= 1.5;
		// }else {
		// 	offset.top += 1.5;
		// }

		return offset;
	};

	_p_._getSameLevelLeft = function(/*item*/) {
		return 12;
	};

	_p_._getSubLevelLeft = function(/*item*/) {
		return 32;
	};

	/**
	 * 显示移动效果
	 * @method _showMoving
	 * @param {元素} item 受影响的元素
	 */
	_p_._delayExpand = function() {
		var _this_ = this;
		if (!this._movingItem || !this._onItem) return;
		if (this._movingItem[0] === this._onItem[0]) return;
		if (this._openingItem && this._openingItem[0] === this._onItem[0]) return;
		this._openingItem = this._onItem;
		if (this._openTimeout) clearTimeout(this._openTimeout);
		this._openTimeout = setTimeout(function(){
			if (!_this_._openingItem || !_this_._onItem) return;
			var dummyNode = _this_._tree.getRoot().setNode(_this_._openingItem);
			if (!dummyNode.isLeaf()) {
				dummyNode.expend();
			}
			_this_._openingItem = null;

			_this_._resizeDraggingArea();
		}, 2000);
	};

	/**
	 * 清除移动效果
	 * @method _clearMoving
	 * @param {元素} item 受影响的元素
	 */
	_p_._clearMoving = function(/*item*/) {
		_s_.prototype._clearMoving.apply(this, arguments);

		this._openingItem = null;
	};

////////////////////////////////////////////////////////////////////////////////////////////////////
// 类型变量
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 类型函数
////////////////////////////////////////////////////////////////////////////////////////////////////


})(window.jQuery);;(function ($) {'use strict';

////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部变量
////////////////////////////////////////////////////////////////////////////////////////////////////
    var HTML = {
        DRAG_AREA :
        '<div drag="drag_area" style="position:absolute; top:0; left:0; width:100%; height:100%;' +
        ' margin:0; padding:0; "></div>'
    };

////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造
////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 拖拽管理器
     * ==========
     *
     * 数据类型：
     * # 拖拽数据：
     * <pre>
     *    dragData : {
     *        start : offset，之前位置
     *        end : offset，现在的位置
     *        item : item，正在拖拽的元素
     *    }
     *    offset : {
     *        left : X坐标，数值
     *        top : Y坐标，数值
     *    }
     * </pre>
     * @class Drag
     * @constructor
     */
    var _c_ = window.Drag = function (/*conf_*/) {
        this._initUI();
        this._initEvent();
    };

    var _p_ = _c_.prototype = {};



////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_._dragingItem = null;

    _p_._startOffset = null;
    
    _p_._dragAreaDiv = null;


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////

    // 初始化
    //------------------------------------------
    _p_._initUI = function() {
        this._dragAreaDiv = $(HTML.DRAG_AREA).appendTo(document.body).hide();
        this._hideDragArea();
    };

    _p_._initEvent = function() {
        this._bindMousedownOnBody();
        this._bindMousemoveOnDragdiv();
        this._bindMouseupOnDragdiv();
    };


    // 鼠标事件
    //------------------------------------------
    _p_._bindMousedownOnBody = function() {
        if (this._onMousedownOnBody) throw new Error('事件绑定错误');
        $(document.body).mousedown(this._onMousedownOnBody = $.proxy(function(event) {
            this._startOffset = {left: event.pageX, top: event.pageY};
            // 进入“等待点击”状态
            this._unbindMousedownOnBody();
            this._bindMouseupOnBody();
            this._bindMousemoveOnBody();
        }, this));
    };

    _p_._bindMousemoveOnBody = function() {
        if (this._onMousemoveOnBody) throw new Error('事件绑定错误');
        $(document.body).mousemove(this._onMousemoveOnBody = $.proxy(function(event) {
            // 进入“拖拽”状态
            var offset = {left: event.pageX, top: event.pageY};

            var moveDistance = Math.sqrt(
                Math.pow((this._startOffset.left - offset.left), 2) + 
                Math.pow((this._startOffset.top - offset.top), 2));

            if (moveDistance > 5 && this._startDrag(this._startOffset)) {
                this._unbindMouseupOnBody();
                this._unbindMousemoveOnBody();
                this._showDragArea();
                this._dragging(offset);
            }
        }, this));
    };
    
    _p_._bindMouseupOnBody = function() {
        if (this._onMouseupOnBody) throw new Error('事件绑定错误');
       $(document.body).mouseup(this._onMouseupOnBody = $.proxy(function(/*event*/) {
           // 没有移动直接鼠标抬起时，恢复“等待点击”状态
           this._unbindMouseupOnBody();
           this._unbindMousemoveOnBody();
           this._bindMousedownOnBody();
       }, this));
    };
    
    _p_._bindMousemoveOnDragdiv = function() {
        $(this._dragAreaDiv).mousemove($.proxy(function(event) {
            var offset = {left: event.pageX, top: event.pageY};
            this._dragging(offset);
        }, this));
    };
    
    _p_._bindMouseupOnDragdiv = function() {
        $(this._dragAreaDiv).mouseup($.proxy(function(event) {
            var offset = {left: event.pageX, top: event.pageY};
            this._endDrag(offset);
            this._bindMousedownOnBody();
            this._hideDragArea();
            event.stopPropagation();
        }, this));
    };

    _p_._unbindMousedownOnBody = function() {
        if (!this._onMousedownOnBody) throw new Error('事件解绑错误');
        $(document.body).unbind('mousedown', this._onMousedownOnBody);
        this._onMousedownOnBody = null;
    };

    _p_._unbindMousemoveOnBody = function() {
        if (!this._unbindMousemoveOnBody) throw new Error('事件解绑错误');
        $(document.body).unbind('mousemove', this._onMousemoveOnBody);
        this._onMousemoveOnBody = null;
    };
    
    _p_._unbindMouseupOnBody = function() {
        if (!this._unbindMouseupOnBody) throw new Error('事件解绑错误');
       $(document.body).unbind('mouseup', this._onMouseupOnBody);
       this._onMouseupOnBody = null;
    };

    _p_._showDragArea = function() {
        this._dragAreaDiv.css('zIndex', window.ZIndexMgr.get());
        this._dragAreaDiv.show();
        setTimeout($.proxy(function() {
            this._dragAreaDiv.focus().click();
        }, this), 100);
    };

    _p_._hideDragArea = function() {
        window.ZIndexMgr.free(this._dragAreaDiv);
        this._dragAreaDiv.hide();
    };


    // 拖拽事件
    //------------------------------------------
    _p_._startDrag = function(mouseOffset_) {
        // 根据鼠标位置找到“拖拽对象”
        this._dragingItem = this._findResponser(mouseOffset_);

        // 通知“拖拽对象”它将被拖拽
        if (this._dragingItem) {
            this._dragingItem.dragFrom(mouseOffset_);
            return true;
        }else {
            return false;
        }
    };

    _p_._dragging = function(offset_) {
        var dragData = {start: this._startOffset, end: offset_, item: this._dragingItem};

        // 根据鼠标位置找到新的“经过目标”，“拖拽对象”不在参考范围
        if (this._findDragToTimeout) clearTimeout(this._findDragToTimeout);
        this._findDragToTimeout = setTimeout($.proxy(function() {
            var responser = this._findResponser(offset_, this._dragingItem);
            if (this._responser !== responser) {
                // 通知原来的“经过目标”，拖拽对象离开了它
                if (this._responser) this._responser.passed();
                this._responser = responser;
            }
        }, this), 100);

        // 通知拖拽对象正在拖拽
        this._dragingItem.dragging(dragData);

        // 通知“经过目标”，拖拽对象正在经过它
        if (this._responser) this._responser.passing(dragData);
    };

    _p_._endDrag = function(offset_) {
        var dragData = {start: this._startOffset, end: offset_, item: this._dragingItem};

        // 通知拖拽目标结束拖拽
        var accepted = false;
        if (this._responser) {
            accepted = this._responser.dragTo(dragData);
        }

        // 通知拖拽对象结束拖拽
        this._dragingItem.end(!accepted);

        this._startOffset = null;
        this._dragingItem = null;
        this._responser = null;
    };


    /**
     * 获取所有“有相应”的“相应对象”
     * @method _findResponser
     * @param  {Offset} offset_ 鼠标位置
     * @return {Responser} 响应对象
     */
    _p_._findResponser = function(offset_, exclusion_) {
        var responsers = _c_.Responser.all();
        var responser = null;

        $.each(responsers, function(_, responser_) {
            if (exclusion_ === responser_) return;
            // 找到最内层的有响应的元素
            if (!responser_.isResponse(offset_)) return;
            if (responser && !responser.isAncestorOf(responser_)) return;

            responser = responser_;
        });

        return responser;
    };


////////////////////////////////////////////////////////////////////////////////////////////////////
// 类型函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    _c_.createResponser = function(conf_) {
        var responser = _c_.Responser.create(conf_);
        return responser;
    };

})(window.jQuery);;(function($) {'use strict';
////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部变量
////////////////////////////////////////////////////////////////////////////////////////////////////
	var SELECTOR = {
		ALL_RESPONSERS : '[drag~="responser"]'	// 全部相应者
	};


////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造
////////////////////////////////////////////////////////////////////////////////////////////////////
	var _c_ = window.Drag.Responser = function(conf_) {
		if (conf_.elem) this._elem = conf_.elem;
        $(this._elem).data('drag', this).attr('drag', 'responser');
	};
	var _p_ = _c_.prototype = {};


////////////////////////////////////////////////////////////////////////////////////////////////////
// 类型变量
////////////////////////////////////////////////////////////////////////////////////////////////////
	_c_.type = {};
	

////////////////////////////////////////////////////////////////////////////////////////////////////
// 类型函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	_c_.create = function(conf_) {
		// TODO[ZXT]:参数检查
		var type = conf_.type;
		var responser = new _c_.type[type](conf_);
		return responser;
	};

	_c_.all = function() {
		var responsers = $(SELECTOR.ALL_RESPONSERS).map(function(index, elem) {
			return $(elem).data('drag');
		});
		return responsers;
	};


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象变量
////////////////////////////////////////////////////////////////////////////////////////////////////
	_p_._elem = null;
	_p_._locked = false;


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////

	// 需要覆盖实现的函数
	//-------------------------------------------
	_p_.isResponse = function(offset_){
		// 判定鼠标是否在目标的显示范围内
		var rect = this.getRect();
		if (offset_.left < rect.left) return false;
		if (offset_.left > rect.left + rect.width) return false;
		if (offset_.top < rect.top) return false;
		if (offset_.top > rect.top + rect.height) return false;
		return true;
	};
	
    _p_.dragFrom = function(/*offset*/){
        // throw new Error('未实现');
    };

    _p_.dragTo = function(/*dragData*/){
        // throw new Error('未实现');
    };

    _p_.dragging = function(/*dragData*/){
        // throw new Error('未实现');
    };

	_p_.passing = function(/*dragData*/){
        // throw new Error('未实现');
	};

    _p_.passed = function(/*dragData*/){
        // throw new Error('未实现');
    };

    _p_.end = function(/*rollback*/){
        // throw new Error('未实现');
    };

	_p_.getRect = function(){
		var $hotarea = $(this._elem);
		var rect = $hotarea.offset();
		rect.width = $hotarea.outerWidth();
		rect.height = $hotarea.outerHeight();
		return rect;
	};

	_p_.isAncestorOf = function(responser) {
		var hotarea = this._elem;
		var givenHotarea = responser._elem;
		return $.contains(hotarea, givenHotarea);
	};

	_p_.lock = function() {
		this._lock = true;
	};

	_p_.unlock = function() {
		this._lock = false;
	};

})(window.jQuery);;(function($) {'use strict';
// TODO[ZXT]: 修正问题：一个元素拖拽到另一个元素上时，被拖拽元素回到原来位置。
////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造
////////////////////////////////////////////////////////////////////////////////////////////////////
	var _s_ = window.Drag.Responser;
	/**
	 *
	 * 以移动方式响应拖拽操作
     * @class Move
     * @constructort
     * @extends Drag.Responser
     */
	var _c_ = window.Drag.Responser.Move = function(/*conf_*/) {
        _s_.apply(this, arguments);
	};
	var _p_ = window.classExtend(_c_, _s_);


////////////////////////////////////////////////////////////////////////////////////////////////////
// 类型初始化
////////////////////////////////////////////////////////////////////////////////////////////////////
    _s_.type.move = _c_;

////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	_p_._statusForRollback = null;

////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_.dragFrom = function(offset_){
        // 结束正在进行的动画
        $(this._elem).stop(true, false);

        // 记录移动对象与鼠标的相对位置
        var offsetNow = this._getRelativeOffset();
        this._fixOffset = {
            top: offset_.top - offsetNow.top,
            left: offset_.left - offsetNow.left
        };

        if (!this._statusForRollback) {
            // 保存原始状态（用于还原）
            var rect = this._getRelativeOffset();
            rect.width = $(this._elem).width();
            rect.height = $(this._elem).height();

            this._statusForRollback = {
                rect: rect,
                style: $(this._elem).attr('style')
            };

            // 修改对象样式，变成移动状态
            $(this._elem).css({
                // position: 'absolute',
                // display: 'inline-block',
                position: 'relative',
                lineHeight: rect.height + 'px',
                zIndex: window.ZIndexMgr.get()
            }).css(rect);
        }
    };

    _p_._getRelativeOffset = function() {
        var offset = {};
        offset.top = $(this._elem).css('top');
        offset.left = $(this._elem).css('left');

        if (offset.top === 'auto') {
            offset.top = '0';
        }else {
            offset.top = parseInt(offset.top, 10);
        }
        if (offset.left === 'auto') {
            offset.left = '0';
        }else {
            offset.left = parseInt(offset.top, 10);
        }

        return offset;
    };

    _p_._getAbsoluteOffset = function() {
        return $(this._elem).offset();
    };

	_p_.dragging = function(dragData){
		// 移位
		if (dragData.item !== this) return;
		var end = dragData.end;
		var offset = {
			top : end.top - this._fixOffset.top,
			left : end.left - this._fixOffset.left
		};
		$(this._elem).css(offset);
    };

    _p_.end = function(rollback){
        // 还原
        if (rollback) {
            $(this._elem).stop(true, false).animate(this._statusForRollback.rect, $.proxy(function() {
                this.rollbackStatus();
            }, this));
        }else {
            this.rollbackStatus();
        }
    };

    _p_.rollbackStatus = function(){
        if (this._cssBeforeMove) $(this._elem).attr('style', this._cssBeforeMove);
        else $(this._elem).removeAttr('style');
        this._statusForRollback = null;
    };

})(window.jQuery);;/**
 * portlet类
 * =========
 * 
 * 使用方法举例：
 *
 *     <!-- HTML中的结构 -->
 *     <div id="portlet" style="width:100%; height:100%;"></div>
 *
 *     <!-- javascript脚本 -->
 *     <script type="text/javascript">
 *     // 添加自定义按钮
 *     RiilPortlet.addButton({type: "delete1",listeners: {
 *             click: function(event, defaultClick, id, $item, itemInfo) {
 *                 alert("before delete");
 *                 defaultClick();
 *             }
 *         }
 *     }, "delete");
 *
 *     // 添加自定义列表
 *     RiilPortlet.addButton({
 *         render: "list", type: "list1", value: "value1", valueList: [
 *             {text: "值1", value: "value1"}, {text: "值2", value: "value2"}, {text: "值3", value: "value3"},
 *             {text: "值4", value: "value4"}, {text: "值5", value: "value5"}
 *         ], listeners: {
 *             change: function(text_value) {
 *                 alert(text_value);
 *             }
 *         }
 *     });
 *
 *     // 从后台获取的配置信息
 *     var config = {debug: true, id: "portlet", isPaging: true, changeType : "queue", currentPageNo : 1, closable: false, pageSize: 4, autoPaging: false, defaultLayoutData: {colCount: 2, maxHeight: null }, refresh: {refresh: {value: 0, valueList: [{text: "5秒", value: 5000 }, {text: "2分", value: 120000 }, {text: "5分", value: 300000 }, {text: "10分", value: 600000 }] }, paging: {value: 0, valueList: [{text: "测试", value: 2000 }, {text: "5分", value: 300000 }, {text: "10分", value: 600000 }] } }, itemIds: ["6"], itemInfoList: [{id: "6", title: "11111", buttonList: ["list1", "refresh", "help1", "delete1"], loadData: "itemDiv1.html"},{id: "2", title: "22222", buttonList: ["flip", "config1", "refresh", "helpfn"], loadData: "itemDiv2.html"} ] };
 *
 *     // 根据个模块需求修改配置信息
 *     // 添加自定义数据，组件在进行ajax操作是都会带上
 *     config.data = {
 *         data1: "aaaaaa",
 *         data2: {
 *             key1: "key1value",
 *             key1: "key1value"
 *         }
 *     };
 *
 *     // 添加“+”按钮点击操作
 *     config.showDialog = function(fnSetResult) {
 *         alert('添加新的portlet');
 *     };
 *
 *     // 利用配置信息初始化组件
 *     RiilPortlet.init(config);
 *     </script>
 *
 * @class RiilPortlet
 */
(function(Debug, ConfigData, $) {'use strict';
////////////////////////////////////////////////////////////////////////////////////////////////////
// 全局变量
////////////////////////////////////////////////////////////////////////////////////////////////////
    var DEFAULT_CONFIG = {
        id: null,
        layout: 'fixLayout',
        loader: 'defaultLoader',
        loaderConf: {poolSize: 2},
        autoPaging: true,
        isPaging: true,
        maxPage: null,
        pageCount: null,
        closable: true,
        defaultLayoutData: {
            colCount: 2,
            minHeight: null,
            height: null,
            maxHeight: null
        },
        currentPageNo : 1,
        pageSize: 4,
        changeType: 'queue',
        portletRenderer: 'defaultRenderer',
        itemRenderer: 'defaultRenderer',
        screenRenderer: 'defaultRenderer',
        pagingRenderer: 'defaultRenderer',
        pageChangeType: 'defaultChange',
        itemIds: [],
        itemInfoList: [],
        save: '',
        deleteUtl: '',
        showDialog: null,
        data: null,
        requestUrl: {
            save: '',
            'delete': '',
            modify: ''
        },
        listeners : {
            onConfig : null
        }
    };
    var REQUIRED_CONFIG = {type : ['object'], elems : {
        id: {array:false, type:['string']},
        itemIds: {array:true, type:['string', 'null']},
        itemInfoList: {array:true, type:['object']},
        data: {type:['object'], require: false, array:false}
    }};
    var g_instance = null;
////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 组件初始化，调用后展示portlet组件，见例子中的实现。
     *
     *     <!-- HTML中的结构 -->
     *     <div id="portlet" style="width:100%; height:100%;"></div>
     *
     *     <!-- javascript脚本 -->
     *     <script type="text/javascript">
     *     // 添加自定义按钮
     *     RiilPortlet.addButton({type: "delete1",listeners: {
     *             click: function(event, defaultClick, id, $item, itemInfo) {
     *                 alert("before delete");
     *                 defaultClick();
     *             }
     *         }
     *     }, "delete");
     *
     *     // 添加自定义列表
     *     RiilPortlet.addButton({
     *         render: "list", type: "list1", value: "value1", valueList: [
     *             {text: "值1", value: "value1"}, {text: "值2", value: "value2"}, {text: "值3", value: "value3"},
     *             {text: "值4", value: "value4"}, {text: "值5", value: "value5"}
     *         ], listeners: {
     *             change: function(text_value) {
     *                 alert(text_value);
     *             }
     *         }
     *     });
     *
     *     // 从后台获取的配置信息
     *     var config = {debug: true, id: "portlet", isPaging: true, changeType : "queue", currentPageNo : 1, closable: false, pageSize: 4, autoPaging: false, defaultLayoutData: {colCount: 2, maxHeight: null }, refresh: {refresh: {value: 0, valueList: [{text: "5秒", value: 5000 }, {text: "2分", value: 120000 }, {text: "5分", value: 300000 }, {text: "10分", value: 600000 }] }, paging: {value: 0, valueList: [{text: "测试", value: 2000 }, {text: "5分", value: 300000 }, {text: "10分", value: 600000 }] } }, itemIds: ["6"], itemInfoList: [{id: "6", title: "11111", buttonList: ["list1", "refresh", "help1", "delete1"], loadData: "itemDiv1.html"},{id: "2", title: "22222", buttonList: ["flip", "config1", "refresh", "helpfn"], loadData: "itemDiv2.html"} ] };
     *
     *     // 根据个模块需求修改配置信息
     *     // 添加自定义数据，组件在进行ajax操作是都会带上
     *     config.data = {
     *         data1: "aaaaaa",
     *         data2: {
     *             key1: "key1value",
     *             key1: "key1value"
     *         }
     *     };
     *
     *     // 添加“+”按钮点击操作
     *     config.showDialog = function(fnSetResult) {
     *         alert('添加新的portlet');
     *     };
     *
     *     // 利用配置信息初始化组件
     *     RiilPortlet.init(config);
     *     </script>
     *
     * @method init
     * @param conf {object} 配置参数
     * @param conf.id {String} 组件安放的标签的ID，默认是null
     * @param conf.layout {String} 组件每个页的整体布局，默认是"fixLayout"
     * @param conf.loader {String} 组件的载入器，默认是"defaultLoader"
     * @param conf.loaderConf {Json} 载入器的配置信息
     * @param conf.loaderConf.poolSize {Number} 同时载入的portlet窗口的个数，默认是8
     * @param conf.autoPaging {Boolean} 是否使用自动翻页，默认是true
     * @param conf.isPaging {Boolean} 是否使用翻页，默认是true
     * @param conf.maxPage {Number} 最大页数，默认是null
     * @param conf.closable {String} 是否不可删除，false时自动为每个portlet窗口添加close按钮，默认是true
     * @param conf.defaultLayoutData {Json} 默认布局信息，在每个portlet窗口中，没有具体配置的都会应用这个配置
     * @param conf.defaultLayoutData.colCount {Number} 每行列数，默认是2
     * @param conf.defaultLayoutData.minHeight {Number} 最小高度，默认是null
     * @param conf.defaultLayoutData.height {Number} 固定高度，默认是null
     * @param conf.defaultLayoutData.maxHeight {Number} 最大高度，默认是null
     * @param conf.currentPageNo {Number} 默认显示第几页，默认是1
     * @param conf.pageSize {String} 每页窗口个数，默认是4
     * @param conf.changeType {Number} 翻页方式，默认是"queue"
     * @param conf.portletRenderer {String} 组件渲染器，默认是"defaultRenderer"
     * @param conf.itemRenderer {String} 窗口渲染器，默认是"defaultRenderer"
     * @param conf.screenRenderer {String} 显示屏渲染器，默认是"defaultRenderer"
     * @param conf.pagingRenderer {String} 翻页效果渲染器，默认是"defaultRenderer"
     * @param conf.pageChangeType {String} 翻页动作逻辑，默认是"defaultChange"
     * @param conf.itemIds {String Array} 窗口列表，决定每页每个位置先是那个portlet窗口，默认是[]
     * @param conf.itemInfoList {Array} portlet窗口配置信息列表。他是个库，存在不一定显示，默认是[]
     * @param conf.itemInfoList.id {String} portlet Id，默认是""
     * @param conf.itemInfoList.title {String|Function} 标题""
     * @param conf.itemInfoList.buttonList {String Array} 按钮列表 []
     * @param conf.itemInfoList.loader {String} "url"或"html"，默认是"url"
     * @param conf.itemInfoList.loadData {String|Function} URL载入方式时是URL，HTML载入方式时是HTML字符串，默认是null,
     * @param conf.itemInfoList.renderer {String} 定制portlet窗口渲染器，默认是"defaultRenderer"
     * @param conf.itemInfoList.layoutData {Json} 渲染器数据，默认是null
     * @param conf.itemInfoList.listeners {Json} 事件监听器列表
     * @param conf.itemInfoList.listeners.resize {Function} 大小改变事件，默认是null
     * @param conf.save {String} 默认是""
     * @param conf.deleteUtl {String}: 默认是""
     * @param conf.showDialog {Function} 此函数的入参数是一个函数，通过调用该入参函数动态添加portlet，请参阅例子中的实现，默认是null
     * @param conf.data {Json} 它的值是任意对象。此处数据由portlet组件的后台部分提供，不使用后台部分时需要填写，默认是null
     * @param conf.requestUrl {Json} 与后台交互数据的URL列表
     * @param conf.requestUrl.save {String} portlet窗口拖动后、添加或删除窗口后用于提交保存当前顺序的URL地址，默认是""
     * @param conf.requestUrl.delete {String} 删除portlet窗口操作时提交数据的URL，默认是""
     * @param conf.requestUrl.modify {String} 删除portlet窗口操作时提交数据的URL，默认是""
     * @param conf.listeners {Json} 事件监听器列表
     * @param conf.listeners.onConfig {Function} : 配置按钮点击事件
     * @param conf.listeners.onMoving {Function} : 移动中事件
     * @param conf.listeners.onStopMoving {Function} : 结束移动事件
     */
    var RiilPortlet = window.RiilPortlet = function(conf) {
        g_instance = this;
        var $struct, _this_ = this;
        ConfigData.check('RiilPortlet.constructor', conf, REQUIRED_CONFIG);
        ConfigData.setDefaultValue(conf, DEFAULT_CONFIG);
        this.conf = conf;
        $struct = $('#' + conf.id);
        if (!$struct[0]) {
            window.alert('document object not found! (id = "' + conf.id + '")');
            return;
        }
        this.$struct = $struct;
        // 创建界面
        this._initUI();
        // 保存顺序状态，用于判断是否需要保存
        this._oldItemOrder = this.conf.itemIds.join(' ');
        window.OnResize.addLayout(function() {
            _this_._onResize();
        });
        window.OnResize.bind();
        if (conf.isPaging) {
            var auto = this._getAutoInfo(true);
            if (null !== auto) {
                this._setAutoRefresh(auto.refresh);
                this._setAutoPaging(auto.paging);
            }
        }
    };
    var _c_ = RiilPortlet;
////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象变量
////////////////////////////////////////////////////////////////////////////////////////////////////
    var _p_ = RiilPortlet.prototype = {
        /**
         * 布局对象
         * @attribute layout
         * @type RiilPortletPageRender
         */
        _render : null,
        /**
         * 配置信息
         * @attribute conf
         * @json
         */
        conf : null,
        /**
         * 所有portlet小窗口
         * @attribute items
         * @type Array
         */
        items : [],
        /**
         * 保存前顺序
         * @attribute _oldItemOrder
         * @type String
         */
        _oldItemOrder : '',
        /**
         * 当前顺序
         * @attribute _itemOrder
         * @type Array
         */
        _itemOrder : [],
        /**
         * portletList容器
         * @attribute $struct
         * @type jQuery
         */
        $struct : null,
        /**
         * 自动刷新的配置信息
         * @attribute _autoRefresh
         * @type json
         */
        _autoRefresh : null,
        /**
         * 自动翻页有效页数（排除空白页）
         * @attribute _autoPageCount
         * @type Integer
         */
        _autoPageCount : -1,
        /**
         * 自动翻页的配置信息
         * @attribute _autoPaging
         * @type json
         */
        _autoPaging : null,
        /**
         * 翻页部分的功能
         * @attribute _paging
         * @type {jQuery}
         */
        _paging : null,
        /**
         * 页加载数据，用于记录每个页加载状态
         * 
         * var data = {
         *     pageNo : [portletId1, portletId2 ... portletIdN],
         *     pageNo2 : [portletId1, portletId2 ... portletIdN],
         *     ...
         *     pageNoN : [portletId1, portletId2 ... portletIdN]
         * }
         */
        _pagingLoadingData : null
    };
////////////////////////////////////////////////////////////////////////////////////////////////////
// API函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_.setAutoRefresh = function () {
    };
    _c_.init = function(conf) {
        return new RiilPortlet(conf);
    };
    _c_.setTitle = function(portletId, text) {
        var item = g_instance._getItemByPortletId(portletId);
        item.setTitle(text);
    };
    _c_.getParam = function(obj, data) {
        var key, param = '';
        for (key in data) {
            param += '&' + key + '=' + JSON.stringify(data[key]);
        }
        for (key in obj) {
            param += '&' + key + '=' + JSON.stringify(obj[key]);
        }
        if (param) param = param.substring(1);
        return param;
    };
    _c_.setComplete = function (id, isComplete) {
        var item;
        if (typeof(id) === 'string') {
            item = g_instance._getItemByPortletId(id);
        }else {
            item = id;
        }

        var isPageLoading = g_instance._isPageLoading(item.getPageNo());
        if (isComplete) {
            item.hideLoading(isPageLoading);
            item.setLoadComplete();
            g_instance._progressPagingLoading(item.getPageNo());
        }else {
            item.showLoading(isPageLoading);
        }
    };
    _c_.getItemByPortletId = function($portlet, portletId) {
        var items = $portlet.data('items');
        if (!items) return null;
        var c = items.length;
        var item = null;
        while (c--) {
            item = items[c];
            if (item.getPortletId() === portletId) return item;
        }
        return null;
    };
    // 翻到指定页
    _c_.pageTo = function($portlet, pageNo) {
        var self = RiilPortlet;
        var $paging = $portlet.data('paging');
        self.Paging.pageTo($paging, pageNo);
    };
    _c_.resize = function() {
        if (RiilPortlet.onResize) RiilPortlet.onResize();
    };
    /**
     * 添加自定义按钮和下拉列表。
     *
     * 举例：添加自定义按钮
     *
     *      RiilPortlet.addButton({
     *          type: "help4",
     *          text: "帮助4信息",
     *          listeners: {
     *              click: function() {
     *                  console.info("!!!!");
     *              }
     *          }
     *      }, "help");
     *
     * 举例：添加下拉列表
     *
     *      RiilPortlet.addButton({
     *          render: "list",
     *          type: "list1",
     *          value: "value1",
     *          valueList: [{
     *              text: "值1", value: "value1"
     *          }, {
     *              text: "值2", value: "value2"
     *          }, {
     *              text: "值3", value: "value3"
     *          }, {
     *              text: "值4", value: "value4"
     *          }, {
     *              text: "值5", value: "value5"
     *          }],
     *          listeners: {
     *              change: function(text_value) {
     *                  console.info(text_value);
     *              }
     *          }
     *      });
     *
     * @method addButton
     * @param buttonConf {object} 按钮配置信息
     * @param {String} buttonConf.render: "button"或"list"，按钮分类：按钮或下拉列表
     * @param {String} buttonConf.type: 自定义按钮时是按钮ID，通用按钮时是
     * @param {String} buttonConf.text: 国际国际化文字,
     * @param {String} buttonConf.classList: 样式（class的值）,
     * @param {String} buttonConf.value: 默认值,
     * @param {Json Array} buttonConf.valueList: 按钮是下来列表时，值列表,
     * @param {String} buttonConf.valueList.text 国际化文字,
     * @param {String} buttonConf.valueList.value: 值,
     * @param {String} buttonConf.listeners: {
     * @param {String} buttonConf.listeners.click: null,
     * @param {String} buttonConf.listeners.change: null
     * @param type {string} 按钮模板
     */
    _c_.addButton = function(buttonConf, type) {
        window.RiilPortletButton.add(buttonConf, type);
    };
    /**
     * 设置portlet窗口加载参数
     *
     *     RiilPortlet.addButton({
     *         render: "list",
     *         type: "list1",
     *         value: "NEAREST_1_HOUR",
     *         valueList: [{
     *             text: TYPE_HOUR, value: "NEAREST_1_HOUR"
     *         }, {
     *             text: TYPE_DAY, value: "NEAREST_1_DAY"
     *         }, {
     *             text: TYPE_WEEK, value: "NEAREST_1_WEEK"
     *         }, {
     *             text: TYPE_MONTH, value: "TYPE_MONTH"
     *         }],
     *         listeners: {
     *             change: function(text_value, conf) {
     *              var param = "dateType=" +text_value;
     *
     *              RiilPortlet.setItemLoadParam(conf, param);
     *
     *             }
     *         }
     *     });
     *
     * @method setItemLoadParam
     * @param conf portlet窗口配置信息
     * @param param 加载参数
     *
     */
    _c_.setItemLoadParam = function(conf, param, reload) {
        var portletId = $.isString(conf) ? conf : conf.id;
        var item = g_instance._getItemByPortletId(portletId);
            item.setLoadParam(param);
        if (reload !== false) {
            item.reload();
        }
    };

    _c_.setAutoRefresh = function(_, value) {
        g_instance._setAutoRefresh(value);
    };

    _c_.setAutoPaging = function(_, value) {
        g_instance._setAutoPaging(value);
    };

    _c_.pauseAutoPaging = function(_, resume) {
        g_instance._pauseAutoPaging(resume);
    };

    /**
     * 重新加载或刷新单个窗口
     * 
     *     RiilPortlet.reloadItem($('#portletId1'));
     *
     * @method reloadItem
     * @param {JQuery} 窗口jquery对象
     */
    _c_.reloadItem = function($item) {
        var uid = $item.attr('id');
        var itemObj = g_instance._getItemByPortletId(uid);
        itemObj.reload();
    };

    _c_.setButtonVisible = function (portletId, buttonId, isVisible) {
        var item = g_instance._getItemByPortletId(portletId);
        if (!item) return;
        var btn = item.getButton(buttonId);
        if (!btn) return;
        btn._setVisible(isVisible);
    };

    _c_.setButtonText = function (portletId, buttonId, text) {
        var item = g_instance._getItemByPortletId(portletId);
        if (!item) return;
        var btn = item.getButton(buttonId);
        if (!btn) return;
        btn._setText(text);
    };


    _c_.setButtonIcon = function (portletId, buttonId, className) {
        var item = g_instance._getItemByPortletId(portletId);
        if (!item) return;
        var btn = item.getButton(buttonId);
        if (!btn) return;
        btn._setClass(className);
    };


    /**
     * 修改ITEM，换成其它ITEM
     * @method modItem
     * @param event {object} jquery事件对象，按钮点击时的事件
     * @param itemInfo {object} 窗口配置信息，与itemInfoList数组元素一致
     */
    _c_.modItem = function(event, itemInfo) {
        var orgItem = event.data;
        if (!orgItem) orgItem = g_instance._getItemByPortletId(event);
        g_instance._replaceItem(orgItem, itemInfo);
    };
    /**
     * 删除portlet
     *
     *     RiilPortlet.delItem($('#portletId'));
     *
     * @method delItem
     * @param  {RiilPortletItem} item 要删除的portlet
     */
    _c_.delItem = function(_, $item) {
        if (!$item) $item = _;
        var uid = $item.attr('uid');
        var item = g_instance._getItem(uid);
        g_instance._delItem(item);
    };

    _c_.onResize = function() {
        if (g_instance) g_instance._onResize();
    };
////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_._createItem = function(itemInfo) {
        var item = new window.RiilPortletItem(
            this._render ? this._render.getPortletContainer() : this.$struct.children('div:first'),
            itemInfo || {}, this.conf, this);
        if (this._render) item._enableMove();
        return item;
    };
    _p_._getItem = function(uid) {
        var item, c = this._render.count();
        while (c--) {
            item = this._render.get(c);
            if (item.getUid() === uid) return item;
        }
    };

    _p_._getItemByPortletId = function (portletId) {
        var item, c = this._render.count();
        while (c--) {
            item = this._render.get(c);
            if (item.getPortletId() === portletId) return item;
        }
    };

    _p_._replaceItem = _p_._addItem = function(desItem, itemInfo) {
        var newItem = this._createItem(itemInfo);
        this._render.replace(newItem, desItem);
        this._updateOrder();

        // 固定页数时不自动调整页数
        if (!this.conf.pageCount) {
            this._adjustPage();
        }
        
        // 载入新Item的内容
        this._loadItems(newItem);
        this._submitSave();
    };

    _p_._delItem = function(item) {
        // 用空白portlet替换原有portlet
        var emptyItem = this._createItem();
        this._render.replace(emptyItem, item);
        this._updateOrder();

        // 固定页数时不自动调整页数
        if (!this.conf.pageCount) {
            this._adjustPage();
        }

        this._submitDelete(item);
    };
    _p_._addPage = function() {
        var c = this._render.getPageSize();
        var emptyItems = [];
        while (c--) {
            emptyItems.push(this._createItem());
        }
        this._render.addPage(emptyItems);
        RiilPortlet.Paging.addPage(this._paging);
    };
    _p_._adjustPage = function() {
        // 固定页数时不执行以下处理
        if (this.conf._pageCount) return;
        
        // 最后一页是空白页，且倒数第二页最后一个是空白元素是，删除最后一页
        this._autoDeleteRearBlankPage();
        
        // 最后一页的最后一个元素不是空白元素时，在最后增加空白页
        this._autoAppendRearBlankPage();
        
        // 除了最后一页，删除所有空白页
        this._autoDeleteMedialBlankPage();
    };
    _p_._autoDeleteRearBlankPage = function() {
        // 至少有2页时，最后一页是空白页，且倒数第二页最后一个是空白元素是，删除最后一页
        var pageCount = this._render.getPageCount();
        if (pageCount < 2) return;
        var pageSize = this._render.getPageSize();
        var isRearPageEmpty = this._isEmptyPage(pageCount);
        var itemIndex = (pageCount - 1) * pageSize - 1;
        var isEmptyItem = this._render.get(itemIndex).isEmpty();
        if (isRearPageEmpty && isEmptyItem) this._removePage();
    };
    _p_._autoAppendRearBlankPage = function() {
        // 最后一页的最后一个元素不是空白元素时，在最后增加空白页
        var maxPage = this.conf.maxPage;
        var pageCount = this._render.getPageCount();

        if (maxPage && maxPage <= pageCount) return;

        var pageSize = this._render.getPageSize();
        var lastItemIndex = pageCount * pageSize - 1;
        var isItemEmpty = this._render.get(lastItemIndex).isEmpty();
        if (!isItemEmpty) this._addPage();
    };
    _p_._autoDeleteMedialBlankPage = function() {
        // 除了最后一页，删除所有空白页
        var pageCount = this._render.getPageCount();
        var c = pageCount - 1;
        while (c--) {
            if (this._isEmptyPage(c + 1)) this._removePage(c + 1);
        }
    };
    _p_._isEmptyPage = function(pageNo) {
        var pageSize = this._render.getPageSize();
        var start = pageSize * (pageNo - 1);
        var end = Math.min(this._render.count(), start + pageSize);
        var e, i;
        for (i = start; i < end; i++) {
            e = this._render.get(i);
            if (!e.isEmpty()) return false;
        }
        return true;
    };
    /**
     * 删除页
     * @method _removePage
     * @param  {Number} [pageNo] 页号，不提供是删除最后一页
     */
    _p_._removePage = function(pageNo) {
        this._render.removePage(pageNo);
        RiilPortlet.Paging.delPage(this._paging);
    };
    _p_._submitSave = function() {
        var _this_ = this;
        var conf = this.conf;
        var oldItemIdsStr = this._oldItemIds;
        var itemIds = conf.itemIds;
        var newItemIdsStr = itemIds.join(' ');
        if (oldItemIdsStr === newItemIdsStr) return;
        var url = conf.requestUrl.save;
        var param = _c_.getParam({itemIds: itemIds}, conf.data);
        if (url) {
            var ctx = window.ctx || '';
            window.PageCtrl.ajax({
                url: ctx[ctx.length - 1] === '/' ? ctx + url : ctx + '/' + url,
                data: param,
                type: 'post',
                dataType: 'json',
                success: function() {
                    _this_._oldItemIds = newItemIdsStr;
                }
            });
        }
    };
    _p_._submitDelete = function(itemDel) {
        var delItemId = itemDel.getPortletId();
        var itemIds = this.conf.itemIds;
        var url = this.conf.requestUrl['delete'];
        var param = _c_.getParam({deleteItemId: delItemId, itemIds: itemIds}, this.conf.data);
        if (url) {
            var ctx = window.ctx || '';
            window.PageCtrl.ajax({
                url: ctx[ctx.length - 1] === '/' ? ctx + url : ctx + '/' + url,
                data: param,
                type: 'post',
                dataType: 'json'
            });
        }
    };
    _p_._setAutoRefresh = function(value) {
        var auto = this._autoRefresh;
        var conf = this.conf;
        if ($.isNull(auto)) {
            // 无记录是创建
            auto = {
                pause: false,
                value: value
            };
            this._autoRefresh = auto;
        }
        if ($.isNull(value)) {
            // value参数不提供是，回复执行
            auto.pause = false;
            return;
        }else {
            // 有记录，提供值时，记录该值
            auto.value = parseInt(value, 10);
            auto.pause = false;
        }
        // 保存状态
        if (!conf.dontSaveCookie) {
            var cookieStr = window.escape(JSON.stringify({
                value: auto.value
            }));
            document.cookie = 'autoRefresh=' + cookieStr;
        }
        // 清除上一次设置时产生的事件
        clearInterval(window.riilportletAutorefreshInterval);
        TaskManager.switchRefresh(false);
        if (auto.value > 1000) {
            // 开启定时器
            window.riilportletAutorefreshInterval = setInterval($.proxy(function () {
                if (!auto.pause) {
                    this._setPageLoading(this._autoPageCount);
                    this._loadItems();
                }
            }, this), value);
            TaskManager.switchRefresh(true);
        }
        if (conf.listeners && conf.listeners.onAutoRefresh) {
            conf.listeners.onAutoRefresh(value);
        }
    };
    _p_._setAutoPaging = function(value) {
        var _this_ = this;
        var conf = this.conf;
        var pageCount = this._autoPageCount;
        var auto = this._autoPaging;
        if ($.isNull(auto)) {
            // 无记录是创建
            auto = {
                pause: false,
                value: value
            };
            this._autoPaging = auto;
        }
        if ($.isNull(value)) {
            // value参数不提供是，回复执行
            auto.pause = false;
            return;
        }else {
            // 有记录，提供值时，记录该值
            auto.value = value;
            auto.pause = false;
        }
        // 保存状态
        if (!conf.dontSaveCookie) {
            var cookieStr = window.escape(JSON.stringify({
                value: auto.value
            }));
            document.cookie = 'autoPaging=' + cookieStr;
        }
        clearInterval(window.riilportletAutoPagingInterval);
        if (auto.value > 1000) {
            window.riilportletAutoPagingInterval = setInterval(function () {
                if (!auto.pause) {
                    var page = conf.currentPageNo;
                    if (page >= pageCount) {
                        page = 1;
                    }else {
                        page++;
                    }
                    _this_._pageChange(null, page);
                }
            }, value);
        }
        if (conf.listeners && conf.listeners.onAutoPaging) {
            conf.listeners.onAutoPaging(value);
        }
    };
    _p_._getAutoInfo = function(useConfig) {
        var conf = this.conf;
        var cookieStr = document.cookie + ';';
        var refresh = -1;
        var paging = -1;
        var arrValue, value;
        if (conf.dontSaveCookie) {
            refresh = this._autoRefresh && this._autoRefresh.value;
            paging = this._autoPaging && this._autoPaging.value;
        }else {
            if (!useConfig || undefined === conf.initAutoRefresh) {
                arrValue = /autoRefresh=(.*?);/.exec(cookieStr);
                if (null !== arrValue) {
                    value = $.parseJSON(window.unescape(arrValue[1])).value;
                    refresh = parseInt(value, 10);
                }
            }else {
                refresh = conf.initAutoRefresh;
            }
            if (!useConfig || undefined === conf.initAutoPaging) {
                arrValue = /autoPaging=(.*?);/.exec(cookieStr);
                if (null !== arrValue) {
                    value = $.parseJSON(window.unescape(arrValue[1])).value;
                    paging = parseInt(value, 10);
                }
            }else {
                paging = conf.initAutoPaging;
            }
        }
        if (!conf.autoPaging) {
            return null;
        }else {
            return {refresh: refresh, paging: paging};
        }
    };
    // 创建portlets界面
    _p_._initUI = function() {
        var items;
        // 初始化portlet界面
        items = this._initPortletsUI();
        // 初始化翻页组件
        this._initPagingUI();
        // 设置为整页loading
        this._setPageLoading(this._autoPageCount);
        // 初始化加载管理器
        this._initLoader();
        // 载入每个Item的内容
        this._loadItems(items);
        // 补充空白尾页
        if (this.conf.isPaging && !this.conf.pageCount) this._autoAppendRearBlankPage();
        // 使portlet可拖动
        this._enableMove();
    };
    _p_._initPortletsUI = function() {
        var width = this.conf.defaultLayoutData.colCount;
        var height = this.conf.pageSize / width;
        // 构建外层
        var $portletParent = $('<div id="screenContainer"></div>').css({
                width: '100%',
                height: this._getScreenSize().height
            }).appendTo(this.$struct);
        // 填充portlet到外层
        var items = this._initItems();
        // 初始化外层界面
        this._render = new window.RiilPortletPageRenderAbsolute({
            items: items,
            container : $portletParent,
            layout : {width : width, height : height}
        });

        this._render._setPageNo(this.conf.currentPageNo);

        return items;
    };
    _p_._getScreenSize = function() {
        var bottomSpace = (this.conf.isPaging ? 29 : 0);
        var rowCount = this.conf.pageSize / this.conf.defaultLayoutData.colCount;
        var minHeight = this.conf.defaultLayoutData.minHeight * rowCount;
        return {
            width: this.$struct.width(),
            height : Math.max(this.$struct.height() - bottomSpace, minHeight)
        };
    };
    _p_._initPagingUI = function() {
        if (this.conf.isPaging) {
            var $paging = this._initPaging();
            this.$struct.append($paging);
            this._paging = $paging;
        }else {
            this._autoPageCount = 1;
        }
    };
    _p_._initLoader = function() {
        this._loader = new window.PortletLoader({
            workSize : this.conf.loaderConf.poolSize,
            callBack : {
                load : $.proxy(function (item, onLoaded_fn) {
                    item.load(onLoaded_fn, this.conf.isAsyncLoad === true, this._isPageLoading(item.getPageNo()));
                }, this),
                isLoadable : function (item) {
                    return !item.isEmpty();
                }
            },
            listeners : {
                onItemLoaded : $.proxy(function(item) {
                    this._progressPagingLoading(item.getPageNo());
                }, this)
            }
        });
    };
    _p_._enableMove = function(items) {
        if (!items) items = this._render.getAll();
        var c = this._render.count();
        while (c--) {
            this._render.get(c)._enableMove();
        }
    };
    // 初始化当前页所有portlet界面
    _p_._initItems = function() {
        var _this_ = this;
        var itemIds = this.conf.itemIds;
        var itemList = [];
        var pageSize = this.conf.pageSize;
        var pageCount = this.conf.pageCount || Math.ceil(itemIds.length / pageSize);
        var c = itemIds.length;
        while (c--) {
            itemList.unshift(this._createItem(getItemInfo(itemIds[c])));
        }

        // 补充空白元素
        c = Math.max(pageCount * pageSize - itemIds.length, 0);
        while (c--) {
            itemList.push(this._createItem());
        }

        return itemList;
        function getItemInfo (portletId) {
            var allItemInfoList = _this_.conf.itemInfoList;
            var itemInfo, c = allItemInfoList.length;
            while (c--) {
                itemInfo = allItemInfoList[c];
                if (itemInfo.id === portletId) {
                    return itemInfo;
                }
            }
        }
    };
    // 创建翻页组件界面
    _p_._initPaging = function() {
        var _this_ = this;
        var conf = this.conf;
        var pageCount = Math.ceil(1.0 * conf.itemIds.length / conf.pageSize);
        this._autoPageCount = pageCount;
        if (undefined !== conf.pageCount && null !== conf.pageCount) {
            pageCount = conf.pageCount;
        }else {
            if (conf.maxPage && pageCount > conf.maxPage) {
                pageCount = conf.maxPage;
            }
        }
        return RiilPortlet.Paging.init(this, {
            renderer: conf.pagingRenderer,
            pageCount: pageCount,
            currentPageNo: conf.currentPageNo,
            configBtn : conf.autoPaging && conf.enableConfig !== false,
            addBtn : !!conf.showEditPop,
            onConfig : conf.listeners.onConfig,
            ok : function(info) {
                _this_._pauseAutoPaging(false);
                _this_._setAutoRefresh(info.refresh || 0);
                _this_._setAutoPaging(info.paging || 0);
            },
            cancel : function() {
                _this_._pauseAutoPaging(false);
            }
        });
    };
    // 加载每个item内容
    _p_._loadItems = function(items, onFinished) {
        var pageNo = this._render.getPageNo();
        var start = 0;
        var item;
        if (!items) items = this._render.getAll();
        if ($.isArray(items)) {
            items.sort(function(item1, item2) {
                return item1.getPageNo() - item2.getPageNo();
            });
            $.each(items, function(index, item) {
                if (item.getPageNo() === pageNo) {
                    start = index;
                    return false;
                }
                item.setLoadComplete(false);
            });
            while (start--) {
                item = items[0];
                items.splice(0, 1);
                items.push(item);
            }
        }else {
            items.setLoadComplete(false);
        }
        this._loader.load(items, onFinished);
        setTimeout($.proxy(function(){
            this._progressPagingLoading(this._render.getPageNo());
        }, this));
    };
    _p_._pageChange = function(fromPageNo, toPageNo) {
        if (toPageNo <= 0) return 1;
        else if (fromPageNo === toPageNo) return fromPageNo;
        this.conf.currentPageNo = toPageNo;
        this._render.pageTo(toPageNo);
        RiilPortlet.Paging._pageToNow(this._paging, toPageNo);

        // 如果当前页没有载入完成，显示大Loading
        if (this._isPageLoading(toPageNo)) this._firePageLoading();
        return toPageNo;
    };
    _p_._updateOrder = function() {
        var c = this._render.count();
        this.conf.itemIds = [];
        while (c--) {
            this.conf.itemIds.unshift(this._render.get(c).getPortletId());
        }
    };
    _p_._dragMoving = function(item) {
        var itemTo = this._render.getHitPortlet(item);
        var onMoving = this.conf.listeners.onMoving;
        var moveActions;
        var path, c;

        if (!this._moving && onMoving) {
            this._moving = true;
            onMoving();
        }

        // 移动的目的地没有变化时不重新计算
        if (!itemTo || (itemTo !== this._moveToItem && item.getPageNo() === itemTo.getPageNo())) {
            // 保存目的地
            this._moveToItem = itemTo || item;

            // 安排动作
            moveActions = [];
            if (!itemTo) {// 没有换位操作时，值显示归位动作
                moveActions.push(this._render.prepareMove(item, item, true));
            }else {// 否则显示所有换位动作
                path = this._render.getPath(item, itemTo);
                c = path.length - 1;
                moveActions.push(this._render.prepareMove(path[0], path[c], true));
                while (c--) {
                    moveActions.push(this._render.prepareMove(path[c + 1], path[c]));
                }
            }

            // 执行动作
            this._moveCommit = this._render.move(moveActions)[0];
        }
    };
    _p_._dragMoveEnd = function($item, callBack) {
        var _this_ = this;
        var onStopMoving = this.conf.listeners.onStopMoving;
        if (this._moveCommit) this._moveCommit(function() {
            if (callBack) callBack();
            if (onStopMoving) {
                _this_._moving = false;
                onStopMoving();
            }
        });
        this._moveCommit = null;
        this._moveToItem = null;
        this._updateOrder();
        this._submitSave();
        
        return $item;
    };
    _p_._pauseAutoPaging = function(pause) {
        if (undefined !== this._autoPaging && null !== this._autoPaging) {
            this._autoPaging.pause = pause;
        }
        if (undefined !== this._autoRefresh && null !== this._autoRefresh) {
            this._autoRefresh.pause = pause;
        }
    };

    // 见init中对this._onResize的定义
    _p_._onResize = function() {
        this.$struct.children('div:first').css(this._getScreenSize());
        this._render.resize();
    };

    _p_._firePageLoading = function() {
        this._showPageLoading();
        var listener = this.conf.listeners && this.conf.listeners.pageLoading;
        if (listener) {
            listener(this._render.getPageNo());
        }
    };

    _p_._firePageLoadingComplete = function() {
        this._hidePageLoading();
        var listener = this.conf.listeners && this.conf.listeners.pageLoadingComplete;
        if (listener) {
            listener(this._render.getPageNo());
        }
    };

    _p_._showPageLoading = function() {
        window.Loading.start();
        // window.RiilMask.visible({
        //     id : 'riilprotlet_pagingloading',
        //     toWhere : this.conf.id,
        //     isLoading : true,
        //     style: 1
        // });
    };

    _p_._hidePageLoading = function() {
        window.Loading.stop();
        // window.RiilMask.hidden('riilprotlet_pagingloading');
    };

    _p_._isPageLoading = function(pageNo) {
        if (!this._pagingLoadingData) return false;
        return $.inArray(pageNo, this._pagingLoadingData) >= 0;
    };

    _p_._progressPagingLoading = function(pageNo) {
        if (!this._pagingLoadingData) return;

        var index = $.inArray(pageNo, this._pagingLoadingData);

        // 记录页面载入状态
        var loaded = this._render.isPageLoaded(pageNo);
        if (loaded && index >= 0) {
            this._pagingLoadingData.splice(index, 1);
        }

        // 如果当前页状态变化，发出事件
        if (pageNo === this._render.getPageNo()) {
            if (loaded) {
                this._firePageLoadingComplete();
            }else if (this._isPageLoading()){
                this._firePageLoading();
            }
        }
    };

    _p_._setPageLoading = function(pageCount) {
        this._pagingLoadingData = [];
        while (pageCount-- > 0) {
            if (!this._isEmptyPage(pageCount + 1)) this._pagingLoadingData.push(pageCount + 1);
        }
        if (this._pagingLoadingData.length > 0) this._firePageLoading();
    };

})(window.Debug, window.ConfigData, window.jQuery);;/**
 * 自动翻页和自动刷新的配置窗口
 *     
 *     var refreshConf = {
 *         top: 100,
 *         left: 100,
 *         refresh: {    // 自动刷新配置信息，无值时不显示次配置项
 *             value: 0, // 自动刷新的默认值
 *             valueList: [{
 *                 text: window.S_INTERVAL_2MIN || '!!S_INTERVAL_2MIN!!',
 *                 value: 120000
 *             }, {
 *                 text: window.S_INTERVAL_5MIN || '!!S_INTERVAL_5MIN!!',
 *                 value: 300000
 *             }, {
 *                 text: window.S_INTERVAL_10MIN || '!!S_INTERVAL_10MIN!!',
 *                 value: 600000
 *             }] 
 *         },
 *         paging: {     // 自动翻页配置信息，无值时是不显示次配置项
 *             value: 0, // 自动翻页的默认值
 *             valueList: [{
 *                 text: window.S_INTERVAL_5MIN || '!!S_INTERVAL_5MIN!!',
 *                 value: 300000
 *             }, {
 *                 text: window.S_INTERVAL_10MIN || '!!S_INTERVAL_10MIN!!',
 *                 value: 600000
 *             }] 
 *         },
 *         listeners: {
 *             ok: onOkClick,           // 确定按钮事件，参数：{refresh: val, paging: val}
 *             cancel: onCancelClick,   // 取消按钮事件
 *         }
 *     };
 * 
 *     AutoRefresh.init(refreshConf);
 *     
 *     function onOkClick(val) {
 *         var refresh = val.refresh;
 *         var paging = val.paging;
 *         alert('点击了确定，自动刷新的值是" '+ refresh + '"，自动翻页的值是" '+ paging + '"。');
 *     }
 *     
 *     function onCancelClick() {
 *         alert('点击了取消');
 *     }
 * 
 * @class AutoRefresh
 */
AutoRefresh = RiilPortlet.AutoRefresh = function(){
    var MULTI_LANG = {
            
    };
    
    var S_CONF_CONSTRAINS = {
        type: ["object"], elems: {
            paging: {checkId: "pagingCheck", type:["object"], required:false, elems: {
                value: {type: ["number"], required: false},
                valueList: {array:true, type: ["object"], elems: {
                    text: {type: ["string"]},
                    value: {type: ["number"]}
                }}
            }},
            refresh: "pagingCheck"
        }
    };
    
    var S_ID_PREFIX = "RiilPortlet_AutoFresh",
        nextId = 1;
    
    return {
        init: init
    };
    
    function createId() {
        return S_ID_PREFIX + nextId++;
    }
    
    function init($portlet, conf) {
        if (!conf) {
            conf = $portlet;
            $portlet = undefined;
        }
        if (!conf) return;
        ConfigData.check(S_ID_PREFIX, conf, S_CONF_CONSTRAINS);
        
        initUI($portlet, conf).data("init")();
    }
    
    //------------------------------------------------
    // 创建界面
    //------------------------------------------------
    // function initUI_old ($portlet, conf) {
//         
        // var selectId1 = createId(),
            // selectId2 = createId(),
            // $self = $(['<div class="panel panel_null">',
                // '<div class="top-l">',
                  // '<div class="top-r">',
                    // '<div class="top-m"><span class="ico-quit"></span><span class="title">' + (window.S_SETTING || '!!S_SETTING!!') + '</span></div>',
                  // '</div>',
                // '</div>',
                // '<div class="middle-l">',
                  // '<div class="middle-r">',
                    // '<div class="middle-m">  ',
                        // '<div class="simple m_bottom">',
                          // '<table><tbody></tbody></table>',
                        // '</div>',
                        // '<div class="button">',
                              // '<a class="btn_s2">'+ (window.S_BTNOK || '!!S_BTNOK!!') + '</a>',
                                // '<a class="btn_s dialog_close">'+ (window.S_BTNCANCEL || '!!S_BTNCANCEL!!') + '</a>',
                            // '</div>',
                    // '</div>',
                  // '</div>',
                // '</div>',
                // '<div class="bottom-l">',
                  // '<div class="bottom-r">',
                    // '<div class="bottom-m"></div>',
                  // '</div>',
                // '</div>',
            // '</div>'].join('')).data("init", function() {
                // if (conf.refresh) {
                    // CustomSelect.init({
                        // id: selectId1,
                        // listeners:{
                            // selectAfter: function(text_value){
                                // onChangeRefresh(text_value);
                            // }
                        // }
                    // });
                // }
// 
                // if (conf.paging) {
                    // CustomSelect.init({
                        // id: selectId2,
                        // listeners:{
                            // selectAfter: function(text_value){
                                // onChangePaging(text_value);
                            // }
                        // }
                    // });
                // }
            // });
// 
// 
        // if (conf.refresh) {
            // $self.find('tbody').append(['<tr>',
              // '<td width="30%">'+ (window.S_FRESH_FREQUENCY || '!!S_FRESH_FREQUENCY!!') + '</td>',
                // '<td width="70%">',
                // '<div class="select_body" id="'+ selectId1 + '" value=" '+ conf.refresh.value + '">',
                     // '<div class="select_trigger_wrap"><a class="select_trigger"></a></div>',
                     // '<div class="select_text">'+ (window.S_NEVER || '!!S_NEVER!!') + '</div>',
                // '</div>',
                // '<div class="select_boundtree" id="'+ selectId1 + '_selectBody" style="width: 100px; display: none;">',
                  // '<ul>',
                    // '<li><a class="on" val="0"><span class="t">'+ (window.S_NEVER || '!!S_NEVER!!') + '</span></a></li>',
                  // '</ul>',
                // '</div>',
                // '</td>',
            // '</tr>'].join(''));
//         
            // fillData($self.find("#" + selectId1 + "_selectBody"), conf.refresh.valueList);
        // }
// 
        // if (conf.paging) {
            // $self.find('tbody').append(['<tr>',
              // '<td width="30%">'+ (window.S_PORTLETAUTOPAGING || '!!S_PORTLETAUTOPAGING!!') + '</td>',
                // '<td width="70%">',
                // '<div class="select_body" id="'+ selectId2 + '" value=" '+ conf.paging.value + '">',
                     // '<div class="select_trigger_wrap"><a class="select_trigger"></a></div>',
                     // '<div class="select_text">'+ (window.S_NEVER || '!!S_NEVER!!') + '</div>',
                // '</div>',
                // '<div class="select_boundtree" id="'+ selectId2 + '_selectBody" style="width: 100px; display: none;">',
                  // '<ul>',
                    // '<li><a class="on"><span class="t">'+ (window.S_NEVER || '!!S_NEVER!!') + '</span></a></li>',
                  // '</ul>',
                // '</div>',
              // '</td>',
            // '</tr>'].join(''));
// 
            // fillData($self.find("#" + selectId2 + "_selectBody"), conf.paging.valueList);
        // }
// 
//                             
// 
        // RiilMask.mask(selectId1);
// 
        // $self.css("z-index", ZIndexMgr.get()).appendTo("body");
//         
        // initEvent($self, $portlet, selectId1, selectId2, conf);
//         
        // return $self;
    // }

    function initUI($portlet, conf) {
        
        var selectId1 = createId(),
            selectId2 = createId(),
            $self = $(['<div class="simple m_bottom">',
                          '<table><tbody></tbody></table>',
                        '</div>'].join('')).data("init", function() {
                if (conf.refresh) {
                    CustomSelect.init({
                        id: selectId1
                    });
                }

                if (conf.paging) {
                    CustomSelect.init({
                        id: selectId2
                    });
                }
            });


        if (conf.refresh) {
            $self.find('tbody').append(['<tr>',
              '<td width="40%"><span class="fixedvalue">'+ (window.S_FRESH_FREQUENCY || '!!S_FRESH_FREQUENCY!!') + 
                    '</span>' + (window.S_LABEL_COLON || '!!S_LABEL_COLON!!') + '</td>',
                '<td width="60%">',
                '<div class="select_body" id="'+ selectId1 + '" value=" '+ conf.refresh.value + '">',
                     '<div class="select_trigger_wrap"><a class="select_trigger"></a></div>',
                     '<div class="select_text">'+ (window.S_NEVER || '!!S_NEVER!!') + '</div>',
                '</div>',
                '<div class="select_boundtree" id="'+ selectId1 + '_selectBody" style="width: 100px; display: none;">',
                  '<ul>',
                    '<li><a class="on" val="0" title="'+ (window.S_NEVER || '!!S_NEVER!!') + 
                        '"><span class="t">'+ (window.S_NEVER || '!!S_NEVER!!') + '</span></a></li>',
                  '</ul>',
                '</div>',
                '</td>',
            '</tr>'].join(''));
        
            fillData($self.find("#" + selectId1 + "_selectBody"), conf.refresh.valueList);
        }

        if (conf.paging) {
            $self.find('tbody').append(['<tr>',
              '<td width="40%"><span class="fixedvalue">'+ (window.S_PORTLETAUTOPAGING || '!!S_PORTLETAUTOPAGING!!') + 
                    '</span>' + (window.S_LABEL_COLON || '!!S_LABEL_COLON!!') + '</td>',
                '<td width="60%">',
                '<div class="select_body" id="'+ selectId2 + '" value=" '+ conf.paging.value + '">',
                     '<div class="select_trigger_wrap"><a class="select_trigger"></a></div>',
                     '<div class="select_text">'+ (window.S_NEVER || '!!S_NEVER!!') + '</div>',
                '</div>',
                '<div class="select_boundtree" id="'+ selectId2 + '_selectBody" style="width: 100px; display: none;">',
                  '<ul>',
                    '<li><a class="on" title="'+ (window.S_NEVER || '!!S_NEVER!!') + 
                        '"><span class="t">'+ (window.S_NEVER || '!!S_NEVER!!') + '</span></a></li>',
                  '</ul>',
                '</div>',
              '</td>',
            '</tr>'].join(''));

            fillData($self.find("#" + selectId2 + "_selectBody"), conf.paging.valueList);
        }

        initEvent($self, $portlet, selectId1, selectId2, conf);
        
        return $self;
    }
    
    function initEvent($self, $portlet, selectId1, selectId2, conf) {
        Panel.htmlShow({
            id : 'AutoRefresh_panel',
            title : window.S_SETTING || '!!S_SETTING!!',
            content : $self,
            width: 400,
            closeBack : closeAction,
            buttons : [{type : "ok", click : okAction}, {type :  "cancel", click : closeAction}]
        });
        
        MoveUtil.init({
            acton:$self,
            handler:$self.find('.top-m')
        });
        
        function okAction() {
            var refreshVal;
            var pagingVal;

            if (conf.refresh) {
                refreshVal = CustomSelect.getValue(selectId1);
            }

            if (conf.refresh) {
                pagingVal = CustomSelect.getValue(selectId2);
            }

            if (conf.listeners && conf.listeners.ok) {
                conf.listeners.ok({refresh: refreshVal, paging: pagingVal});
            }

            Panel.close('AutoRefresh_panel');
        }
        
        function closeAction() {
            if (conf.listeners && conf.listeners.cancel) {
                conf.listeners.cancel();
            }

            Panel.close('AutoRefresh_panel');
        }
    }
    
    function fillData($select, valueList) {
        var $t_ul = $select.find("ul");
        for (var i = 0, c = valueList.length; i < c; i++) {
            var valueObj = valueList[i],
                text = valueObj.text,
                value = valueObj.value;
            var $li = $('<li val=" '+ value + '"><a title=" '+ text + '"><span class="t">'+ text + '</span></a></li>');
            $li.attr('title', $li.text());
            $t_ul.append($li);
        }
    }
}();;/**
 * 刷新逻辑
 * @class AutoRefreshModel
 * @constructor
 * @param {String} portletJQselector_ portlet的选择器
 */
function AutoRefreshModel (portletJQselector_) {
    this._aQueue = [];
    this._portlet= portletJQselector_;
}

AutoRefreshModel.prototype = {
    constructor : AutoRefreshModel,
    _log : Debug.getLogger('debug.autorefreshmodel'),

    /**
     * 刷新
     * @method refresh
     * @param {Number} iTimeScope 时间间隔
     */
    refresh : function (iTimeScope_) {
        this._arrageRefresh(iTimeScope_);
        this._refresh();
    },
    
    _refresh : function (afterLoaded_fn_) {
        // 队列为空时，刷新结束
        if (this._aQueue.length === 0) {
            afterLoaded_fn_ && afterLoaded_fn_();
        
            this._log('本次刷新结束');
            return;
        }
        
        // 刷新队头的页
        var $ul = this._aQueue.shift();
        var $itemList = [];
        var __this__ = this;
        
        $ul.find('li>div.portlet:not([loading])').each(function(index_, item_) {
            $itemList.push($(item_));
        });
        
        if ($itemList.length === 0) {
            this._log('当前页无需刷新');
            // 刷新下一页
            __this__._refresh(afterLoaded_fn_);
            return;
        }
        
        this._log('开始刷新页', $itemList.length + '项');
        
        RiilPortlet._loadItems(this._portlet, $itemList, function() {
            __this__._log('完成刷新页', $itemList.length + '项');
            // 刷新下一页
            __this__._refresh(afterLoaded_fn_);
        });
    },
    
    /**
     * 根据当前页安排刷新任务
     * @method _arrageRefresh
     * @param {Number} timeSpace 与当前时间对最小间隔，超过的要入队
     * @private
     */
    _arrageRefresh : function (timeSpace) {
        var updateTime = new Date().getTime() - timeSpace/2;
        var currentPageNo = this._portlet.conf.currentPageNo;
        var pages = RiilPortlet._pages;
        var i = 0;
        var c = pages.length;
        var iPageIndex = 0;
        var $ul = null;
        
        // 从当前页开始，沿着正方向逐个将每一个需要刷新的页入队（不包括正在刷新的）
        this._aQueue = [];
        for (i = 0; i < c; i++) {
            iPageIndex = (i + currentPageNo - 1) % c;
            $ul = pages[iPageIndex];
            if ($ul) {
                this._aQueue.push($ul);
            }
        }
        
        this._log('计划:', this._aQueue.length + '页');
    }
};;(function($) {'use strict';
	
////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	var _c_ = window.RiilPortletButton = function($parent, item, conf) {
		this._item = item;
		this._id = conf.id;
		this._type = conf.type;
		if (conf.text) this._textes = conf.text;
        if (conf.classList) this._classes = conf.classList;
        if (conf.listeners) {
            this._listeners = $.extend({}, conf.listeners);
        }
	};
	var _p_ = window.RiilPortletButton.prototype = {};


////////////////////////////////////////////////////////////////////////////////////////////////////
// 类函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	_c_.newInstance = function($container, item, id) {
		var conf = window.RiilPortletButtonTypes[id.toLowerCase()];

		if (!conf) {
			window.alert('button of portlet "' + id +
                '" was not defined! Call "RiilPortlet.addButton" to append.');
		}

		if ('list' === conf.render) {
			return new window.RiilPortletButtonList($container, item, conf);
		}else if ('icolist' === conf.render) {
			return new window.RiilPortletButtonListIco($container, item, conf);
		}else if ('text' === conf.render){
            return new window.RiilPortletButtonPressText($container, item, conf);
        }else {
            return new window.RiilPortletButtonPress($container, item, conf);
        }
	};

	_c_.add = function (conf, type) {
        conf.type = conf.type.toLowerCase();
        conf.id = conf.type;
        
        if (type){
            var refConf = window.RiilPortletButtonTypes[type];
            window.ConfigData.setDefaultValue(conf, refConf);
            conf.type = refConf.type;
        }
        
        window.RiilPortletButtonTypes[conf.id] = conf;
    };


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象变量
////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 所依附的portlet item对象
	 * @attribute _item
	 * @type {RiilPortletItem}
	 */
	_p_._item = null;

	/**
	 * 按钮ID，参数传入
	 * @attribute id
	 * @type {String}
	 */
	_p_._id = '';

	/**
	 * 按钮类型
	 * @attribute type
	 * @type {String}
	 */
    _p_._type = 'undefined';

    /**
     * 按钮样式列表
     * ============
     * 按钮在不同时刻可能应用不同样式
     * @attribute classes
     * @type {Array}
     */
    _p_._classes = [];

    /**
     * 按钮文本信息列表
     * ================
     * 按钮文本可能会有多种值
     * @attribute textes
     * @type {Array}
     */
    _p_._textes = [];

    /**
     * 事件监听
     * @attribute listeners
     * @type {Object}
     */
    _p_._listeners = {};


    /**
     * 结构
     * @attribute _$struct
     * @type {jQuery}
     */
    _p_._$struct = null;


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_.getType = function() {return this._type;};

    _p_.getId = function() {return this._id;};
    
    _p_._createId = function() {
        return 'RiilPortletButton_' + g_lastId++;
    };

    _p_._get = function(arrayOrString, index) {
        // 如果是回调函数，执行函数，获取文本或字符串
        if (typeof(arrayOrString) === 'function') arrayOrString = arrayOrString(this._item._conf);

        // 获取文本数组中指定位置文本或首个文本
        if (typeof(arrayOrString) === 'string') return arrayOrString;
        else return arrayOrString[index || 0];
    };

    _p_._getText = function(index) {
        return this._get(this._textes, index);
    };

    _p_._getClass = function(index) {
        return this._get(this._classes, index);
    };

    _p_._setVisible = function (isVisible) {
        if (isVisible) {
            this._$struct.css('display', 'block');
        }else {
            this._$struct.css('display', 'none');
        }
    };

    _p_._setText = function (text) {
        this._$struct.attr('title', text);
    };

    _p_._setClass = function (className) {
        this._$struct.removeClass().addClass(className);
    };


////////////////////////////////////////////////////////////////////////////////////////////////////
// 全局变量
////////////////////////////////////////////////////////////////////////////////////////////////////
	var g_lastId = 0;

////////////////////////////////////////////////////////////////////////////////////////////////////
// 全局函数
////////////////////////////////////////////////////////////////////////////////////////////////////


})(window.jQuery);;(function($) {'use strict';

////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	var _s_ = window.RiilPortletButton;
	var _c_ = window.RiilPortletButtonList = function($parent, item, conf) {
		_s_.apply(this, arguments);

        if (typeof conf.value === 'function') this._value = conf.value(item._conf, conf);
        else this._value = conf.value;

        if (typeof conf.valueList === 'function') this._valueList = conf.valueList(item._conf, conf);
		else this._valueList = conf.valueList;

		this._render($parent);
	};
	var _p_ = window.classExtend(_c_, _s_);


////////////////////////////////////////////////////////////////////////////////////////////////////
// API
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象变量
////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 默认选中的值
	 * @attribute value 值
	 * @type {String}
	 */
    _p_.value = '';

    /**
     * 所有列表中的值和
     * @attribute valueList
     * @type {Array} [{text: '', value: v}...]
     */
    _p_.valueList = [];

    /**
     * 回调函数：更改下拉列表值的时候触发
     * @method change
     * @param {String} value 当前选中的值
     * @param {json} itemConf portlet配置信息
     */
    _p_._listeners.change = null;// function(value, itemConf) {};

////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	_p_._render = function($parent) {
        var _this_ = this;
        var id = this._createId();
        this._$struct = $(window.CustomSelect.getTemplate(id, id, this._valueList, this._value))
            .prependTo($parent);
        
        $(this._$struct).css('float', 'right').css('margin-top', -2);
        
        window.CustomSelect.init({
            id: id,
            initSelectAfter : false,
            listeners:{
                selectAfter: function(text_value){
                    _this_._onChange(text_value);
                }
            }
        });
        $('#' + id).css('cursor', 'default');
    };

    _p_._onChange = function(value) {
        if (this._listeners.change) this._listeners.change(value, this._item._conf);
    };

////////////////////////////////////////////////////////////////////////////////////////////////////
// 全局变量
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 全局函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	

})(window.jQuery);;(function($) {'use strict';
	
////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	var _s_ = window.RiilPortletButtonList;
	var _c_ = window.RiilPortletButtonListIco = function() {
        _s_.apply(this, arguments);
	};
	var _p_ = window.classExtend(_c_, _s_);


////////////////////////////////////////////////////////////////////////////////////////////////////
// API
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象变量
////////////////////////////////////////////////////////////////////////////////////////////////////



////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	_p_._render = function($parent) {
        var _this_ = this;
        var id = this._createId();
        var valueList = this._valueList;
        var defaultValue = this._value || (this._valueList[0] && this._valueList[0].value);
        var className = this._getClass();
        var title = this._getText();
        var $li, valueObject, c;

        if (title) title = ' title="' + title + '"';
        else title = '';
        
        this._$struct = $('<span class="' + className + '" id="' + id + '"' + title + '></span>' +
            '<div class="icoselect_boundtree" id="' + id + '_selectBody" type="icoselect" style="display:none;">' +
                '<ul></ul>' +
            '</div>').prependTo($parent);
        
        c = valueList.length;
        while (c--) {
            valueObject = valueList[c];
            $li = $('<li><a>' + valueObject.text + '</a></li>').attr('val', valueObject.value);
            $li.prependTo(this._$struct.find('ul:first')).find('a:first').attr('title', $li.text());
        }
        
        if (defaultValue) {
            this._$struct.attr('value', defaultValue);
        }
        
        window.CustomSelect.init({
            id: id,
            initSelectAfter : false,
            listeners:{
                selectAfter: function(text_value){
                    _this_._onChange(text_value);
                }
            }
        });
        $('#' + id).css('cursor', 'pointer');
    };
////////////////////////////////////////////////////////////////////////////////////////////////////
// 全局变量
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 全局函数
////////////////////////////////////////////////////////////////////////////////////////////////////


})(window.jQuery);;(function ($) {'use strict';

////////////////////////////////////////////////////////////////////////////////////////////////////
// 私有函数
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    var _s_ = window.RiilPortletButton;
    var _c_ = window.RiilPortletButtonPress = function($parent, item, conf) {
        // 调用父类构造函数
        _s_.apply(this, arguments);

        if (conf.action) this._action = conf.action;
        
        this._render($parent);
        this._bindEvent();
    };
    var _p_ = window.classExtend(_c_, _s_);


////////////////////////////////////////////////////////////////////////////////////////////////////
// API函数
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部变量
////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 默认动作，不一定被执行
     * @method _action
     * @param {jQueryEvent} event 点击事件
     */
    _p_._action = null;// function(event){};

    /**
     * 回调函数：按钮点击时触发
     * @method click
     * @param  {jQueryEvent} event    [description]
     * @param  {Function} defaultAction 默认，无参
     * @param  {String} itemId portlet Id
     * @param  {jQuery} $item portlet的jQuery对象
     * @param  {json} itemData itemInfo.data
     */
    _p_._listeners.click = null;// function(event, defaultAction, itemId, $item, itemData){};


////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_._render = function ($parent) {
        var title = this._getText();
        var className = this._getClass();
        
        this._$struct = $('<span class="' + className + '" type="' + this._type+ '"></span>')
            .prependTo($parent);

        if (title) this._$struct.attr('title', title);
    };
    
    _p_._bindEvent = function(){
        var click = this._listeners.click;
        var action = this._action;
        var _this_ = this;

        // 有点击事件是加上点击手型
        if (click || action) {
            this._$struct.click(this._item, function(event) {
                _this_._onClick(event);
            });
            this._$struct.css('cursor', 'pointer');
        }else {
            this._$struct.css('cursor', 'default');
        }
        
        // 禁用鼠标按下和抬起事件
        this._$struct.bind('mousedown', stop).bind('mouseup', stop);
        function stop(event) {
            event.stopPropagation();
            event.preventDefault();
        }
    };
    
    _p_._onClick = function (event) {
        var _this_ = this;
        // 收回弹出的下拉框
        $(document.body).scroll();

        event.stopPropagation();
        event.preventDefault();
        
        if (this._listeners.click) {
            // 注册了自定义行为时
            this._listeners.click(event, function(param) {
                if (_this_._action) _this_._action(param || false);
            }, this._item.getPortletId(), this._item._$struct, this._item._conf);
        }else if (this._action) {
            // 未自定义行为时触发默认行为
            this._action(false);
        }
    };
})(window.jQuery);;(function($) {'use strict';

////////////////////////////////////////////////////////////////////////////////////////////////////
// 私有函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	

////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造函数
////////////////////////////////////////////////////////////////////////////////////////////////////
	var _s_ = window.RiilPortletButtonPress;
	var _c_ = window.RiilPortletButtonPressText = function($parent, item, conf) {
		this._buttonText = conf.buttonText;
		// 调用父类构造函数
		_s_.apply(this, arguments);
	};
	var _p_ = window.classExtend(_c_, _s_);


////////////////////////////////////////////////////////////////////////////////////////////////////
// API函数
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部变量
////////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////////////////////
// 内部函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_._render = function ($parent) {
    	var text = this._buttonText;
        var title = this._getText();
        var className = this._getClass();
        
        this._$struct = $('<span class="' + className + '" type="' + this._type+ '">' + text + '</span>')
            .prependTo($parent);

        if (title) this._$struct.attr('title', title);
    };
    
})(window.jQuery);;(function($) {'use strict';
    /**
     * 标准按钮样式
     * ============
     *
     * 数据格式：
     *     {
     *         id: {
     *             type: '分类',
     *             classes: ['按钮样式名'],
     *             textes: ['按钮悬浮文字'],
     *             action: function(){}, // 默认动作
     *             refer: id// 引用，使用是会将引用的信息补充进来，自定义按钮都有该参数
     *         }
     *     }
     *     
     * 
     * @class RiilPortletButtonTypes
     */
    
    //------------------------------------------------------
    // 默认行为
    //------------------------------------------------------
    var actions = {
        refreshAction : function() {
            this._item.reload();
        },
        
        closeAction : function(force) {
            var _this_ = this;
            if (force) {
                this._item.deleteItem();
            }else {
                window.RiilAlert.confirm(window.S_DELETE_PORTLET_ALERT ||
                    '!!window.S_DELETE_PORTLET_ALERT!!', function() {
                    _this_._item.deleteItem();
                });
            }
        },

        flipAction : function(noAnimate) {
            var _this_ = this;
            var $t_item = this._item._$struct;
            var itemConf = this._item._conf;
            
            if (false === noAnimate) {
                // 动画执行完成之前不响应
                if (this._flipping) {
                    return;
                }else {
                    this._flipping = true;
                    setTimeout(function() {
                        _this_._flipping = false;
                    }, 500);
                }
            }
            
            // 获取fliper
            var $t_fliper;
            var iframe = $t_item.find('iframe')[0];
            if (itemConf.loader === 'iframe') {
                if (!iframe) return;
                $t_fliper = $('div.quickFlip', iframe.contentDocument);
            }else {
                $t_fliper = $t_item.find('.quickFlip');
            }
            if ($t_fliper.length <= 0) return;
            
            // 获取当前状态
            var buttonStatus = $t_fliper.children('div:eq(0)').css('display') === 'none' ? 0 : 1;
            
            // 修改按钮状态
            this._setClass(this._getClass(buttonStatus));
            this._setText(this._getText(buttonStatus));
            
            // 设置刷新的URL
            var portletConf = this._item.portletConf;
            var flipParam = {
                paramName : 'flip',
                chartValue : 'chart',
                tableValue : 'table'
            };
            if (portletConf.flip) {
                if (portletConf.flip.paramName) {
                    flipParam.paramName = portletConf.flip.paramName;
                }
                if (portletConf.flip.chartValue) {
                    flipParam.chartValue = portletConf.flip.chartValue;
                }
                if (portletConf.flip.tableValue) {
                    flipParam.tableValue = portletConf.flip.tableValue;
                }
            }
            
            this._item.setFlipParam(flipParam.paramName + '=' +
                    (buttonStatus ? flipParam.tableValue : flipParam.chartValue), false);
                    
            // 翻转
            if (false === noAnimate) {
                $t_fliper.quickFlipper({}, buttonStatus);
            }
        },
        
        maxAction : function() {
            var _this_ = this;
            var $item = this._item._$struct;
            var onNormalSize = this._item.portletConf.listeners.onNormalSize;
            var onEnlarge = this._item.portletConf.listeners.onEnlarge;
            var itemConf = this._item._conf;

            this._item.portlet._pauseAutoPaging(true);
            this._maxing = true;
            
            if (this._maxed) {
                // 还原
                this._item.restore(function() {
                    _this_._maxing = false;
                    _this_._item._enableMove();
                    _this_._item.portlet._pauseAutoPaging(false);
                    
                    if (onNormalSize) onNormalSize($item, itemConf);
                });
            }else {
                // 最大化
                _this_._item._disableMove();
                this._item.maximize(function() {
                    _this_._maxing = false;
                    
                    if (onEnlarge) onEnlarge($item, itemConf);
                });
            }

            this._setClass(this._getClass(this._maxed ? 0 : 1));
            this._setText(this._getText(this._maxed ? 0 : 1));
            this._maxed = !this._maxed;
        }

    };

    var types = window.RiilPortletButtonTypes = {};
    types.refresh = {
        type: 'refresh',
        classList: 'ico-update',
        text: window.S_REFRESH || '!!S_REFRESH!!',
        action: actions.refreshAction
    };
    types.smart_refresh = {
        type: 'refresh',
        classList: 'ico-update f_right',
        text: window.S_REFRESH || '!!S_REFRESH!!',
        action: actions.refreshAction
    };
    types.close = {
        type: 'close',
        classList: 'ico-quit',
        text: window.S_BTNDELETE || '!!S_BTNDELETE!!',
        action: actions.closeAction
    };
    types.smart_close = {
        type: 'close',
        classList: 'ico-del',
        text: window.S_BTNDELETE || '!!S_BTNDELETE!!',
        action: actions.closeAction
    };
    types.max = {
        type: 'max',
        classList: ['icon icon_enlarge f-right', 'ico-narrow'],
        text: [window.S_MAXIMIZE || '!!S_MAXIMIZE!!',
            window.S_RESTORE || '!!S_RESTORE!!'],
        action: actions.maxAction
    };
    types.smart_max = {
        type: 'max',
        classList: ['ico-zoom', 'ico-resize'],
        text: [window.S_MAXIMIZE || '!!S_MAXIMIZE!!',
            window.S_RESTORE || '!!S_RESTORE!!'],
        action: actions.maxAction
    };
    types.flip = {
        type: 'flip',
        classList: ['ico-table', 'ico-chart'],
        text: [window.S_BTNSHOWTABLE || '!!S_BTNSHOWTABLE!!',
            window.S_BTNSHOWCHART || '!!S_BTNSHOWCHART!!'],
        action: actions.flipAction
    };
    types.smart_flip = types.flip;
    types.showall = {
        type: 'showall',
        classList: 'ico-popup',
        text: window.S_SHOWALL || '!!S_SHOWALL!!' // 显示全部
    };
    types.smart_showall = types.showall;
    types.help = {
        type: 'help',
        classList: 'ico-help'
    };
    types.smart_help = {
        type: 'help',
        classList: 'ico-info'
    };
    types.config = {
        type: 'config',
        classList: 'ico-set',
        text: window.S_BTNPORTLETCONFIG || '!!S_BTNPORTLETCONFIG!!' // Portlet设置
    };
    types.smart_config = {
        type: 'config',
        classList: 'ico-set f_right',
        text: window.S_BTNPORTLETCONFIG || '!!S_BTNPORTLETCONFIG!!' // Portlet设置
    };
    types['delete'] = {
        type: 'delete',
        classList: 'ico-quit',
        text: window.S_BTNDELETE || '!!S_BTNDELETE!!', // 删除
        action: actions.closeAction
    };
    types.smart_delete = {
        type: 'delete',
        classList: 'ico-del',
        text: window.S_BTNDELETE || '!!S_BTNDELETE!!', // 删除
        action: actions.closeAction
    };

})(window.jQuery);;/**
 * Portlet的每个小窗口
 * ===================
 * 
 * 负责portlet和他的自功能的调用和管理
 * 
 * @class RiilPortlet_Item
 */
(function($) {'use strict';

////////////////////////////////////////////////////////////////////////////////////////////////////
// 全局变量
////////////////////////////////////////////////////////////////////////////////////////////////////

    var g_lastUid = 0;

    var G_BORDER_SIZE = {width:2, height:30};

////////////////////////////////////////////////////////////////////////////////////////////////////
// 全局函数
////////////////////////////////////////////////////////////////////////////////////////////////////

    function createId() {
        return ++g_lastUid;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * @constructor
     * @param {jQuery} $parent 存放位置
     * @param {json} conf 自己的配置信息
     * @param {json} portletConf portlet组件整体的配置信息
     * @portlet {jQuery} portlet portlet组件最外层
     */
    var _c_ = window.RiilPortletItem = function ($parent, conf, portletConf, portlet) {
        this._uid = createId();
        this._conf = $.extend({}, this._conf, conf);
        this.portlet = portlet;
        this.portletConf = portletConf;
        
        // 初始化界面
        this._initUI($parent);
        this._$struct.attr('uid', this._uid);
    };
    var _p_ = _c_.prototype = {};


////////////////////////////////////////////////////////////////////////////////////////////////////
// API函数
////////////////////////////////////////////////////////////////////////////////////////////////////
   
    _p_.getPageNo = function() {
        return this.portlet._render.getItemPageNo(this);
    };

    _p_.deleteItem = function() {
        this.portlet._delItem(this);
    };

    // 载入该portlet的内容
    _p_.load = function(onLoadComplete, manualFinish, dontShowLoading) {
        var _this_ = this;
        this.showLoading(dontShowLoading);
        manualFinish = manualFinish && this._conf.loader != 'html';
        window.RiilPortletItemLoader.load(this, this._conf.loader, function() {
            if (!manualFinish || _this_._loaded) onLoaded();
            else _this_._onLoaded = onLoaded;
        });

        function onLoaded() {
            _this_.hideLoading(dontShowLoading);
            if (onLoadComplete) onLoadComplete(_this_);
        }
    };

    _p_.setLoadComplete = function(yeah) {
        if (undefined === yeah) yeah = true;
        this._loaded = yeah;
        if (yeah && this._onLoaded) this._onLoaded();
    };

    _p_.isLoaded = function() {
        return this._loaded;
    };
    
    // 重新载入（需要通知portlets安排load行为）
    _p_.reload = function(onFinished) {
        this.portlet._loadItems(this, onFinished);
    };
    
    _p_.getContent = function() {
        return this._$struct.find('.middle-m');
    };

    _p_.isEmpty = function() {
        return ! this._conf.id;
    };

    _p_.setTitle = function(text) {
        this._conf.title = text;
        this._$struct.find('div.top-m>span.title').text(text).attr('title', text);
    };

    _p_.setPosition = function (xy) {
        this._$struct.css(xy);
    };

    _p_.setSize = function (size) {
        this._$struct.find('.middle-m:first').css(this.calcSize(size));
        this._$struct.find('.top-m:first').css(this.calcTitleSize(size));
        if (this._conf.listeners && this._conf.listeners.resize) {
            this._conf.listeners.resize(this._$struct);
        }
    };

    _p_.calcSize = function(size) {
        return {
            width: size.width - G_BORDER_SIZE.width,
            height: size.height - G_BORDER_SIZE.height
        };
    };
    _p_.calcTitleSize = function(size) {
        return {
            width: size.width - G_BORDER_SIZE.width - 18
        };
    };

    _p_.destroy = function () {
        this.portlet = null;
        // this._conf = null;
        this.portletConf = null;
        this._$struct.remove();
        this._$struct = null;
        this._onLoaded = null;
    };

    _p_.getButton = function (id) {
        var btn, c = this._btns.length;
        while (c--) {
            btn = this._btns[c];
            if (btn.getId() === id || btn.getType() === id) return btn;
        }
    };

    _p_.getFlipParam = function() {return this._flipParam;};
    _p_.setFlipParam = function (param) {this._flipParam = param;};
    _p_.getItemConfData = function(){return this._conf.data;};
    _p_.getLoadData = function(){return this._conf.loadData;};
    _p_.getLoadParam = function(){return this.loadParam;};
    _p_.setLoadParam = function(v){this.loadParam = v;};
    _p_.getPortletId = function(){return this._conf.id;};
    _p_.getUid = function(){return this._uid;};


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象变量
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_.portlet = null;
    _p_._conf = {};
    _p_.portletConf = {};
    _p_._$struct = null;
    _p_._loaded = false;
    _p_._onLoaded = null;
    _p_._uid = -1;
    _p_._btns = [];
    _p_._flipParam = undefined;
    

////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_._addDelete = function() {
        var type, c = this._btns.length;
        while (c--) {
            type = this._btns[c].getType();
            if (type === 'delete' || type === 'close') return;
        }

    };
    
    // 初始化界面
    _p_._initUI = function($parent) {
        this._render($parent);
    };

    _p_._onBigAddClick = function() {
        var _this_ = this;
        if (this.portletConf.showDialog) this.portletConf.showDialog(function(itemInfo) {
            _this_.portlet._addItem(_this_, itemInfo);
        });
    };

    // 开启拖动支持
    _p_._enableMove = function() {
        if (!this._isMovable()) return;
        var _this_ = this;
        // 准备拖动
        window.MoveUtil.init({
            acton:this._$struct,
            handler:this._$struct.find('.top-m'),
            rect: this._$struct.parent(),
            listeners:{
                moveAfter: function() {
                    if (!_this_._moving) {
                        _this_._moving = true;
                        _this_._$struct.addClass('bgblue');
                        _this_.portlet._pauseAutoPaging(true);
                    }
                    _this_.portlet._dragMoving(_this_);
                },
                mouseUp: function() {
                    _this_._moving = false;
                    _this_.portlet._pauseAutoPaging(false);
                    _this_.portlet._dragMoveEnd(_this_, function() {
                        _this_._$struct.removeClass('bgblue');
                    });
                }
            },
            validate: function(event) {
                var $clickee = $(event.target);
                return $clickee.hasClass('top-m') || $clickee.hasClass('title');
            }
        });

        this._$struct.find('.top-l').addClass('pro_cursor');
    };

    // 关闭拖动支持
    _p_._disableMove = function() {
        window.MoveUtil.disable(this._$struct, this._$struct.find('.top-m'));

        this._$struct.find('.top-l').removeClass('pro_cursor');
    };

    _p_.showLoading = function(noMask) {
        var uid = this._$struct.attr('uid');

        this._loaded = false;
        if (noMask) return;
        if (window.RiilPortlet.isSmart) {
            window.RiilMask.visible({
                id : 'portlet_' + uid,
                toWhere : this._$struct,
                isLoading : true,
                zIndex : 10,
                style : 6
            });
        }else {
            window.RiilMask.visible({
                id : 'portlet_' + uid,
                toWhere : this._$struct,
                isLoading : true,
                zIndex : 10
            });
        }
    };

    _p_.hideLoading = function(noMask) {
        var uid = this._$struct.attr('uid');
        this._loaded = true;
        if (noMask) return;
        window.RiilMask.hidden('portlet_' + uid);
    };

    _p_._getTitle = function() {
        var title = this._conf.title || '';
        if ($.isFunction(title)) title = title(this._conf);
        return title;
    };

    _p_._isMovable = function() {
        return this.portletConf.enableMove !== false;
    };

    _p_._isAddable = function() {
        return this.portletConf.enableAdd !== false;
    };

    _p_._render = function($parent) {
        var _this_ = this;
        var addDelBtn = this.portletConf.closable !== false;
        var title = this._getTitle();
        var cursor = this._isMovable() ? ' pro_cursor' : '';
        var bigAddStyle;
        if (this._conf.id || this._isAddable() === false) {
            bigAddStyle = ' style="display:none"';
        }else {
            bigAddStyle = '';
            addDelBtn = false;
        }

        this._$struct = $('<div class="portlet" style="position:absolute">' +
                '<div class="top-l' + cursor + '">' +
                '<div class="top-r">' +
                    '<div class="top-m">' +
                        '<span class="title">' + title + '</span>' +
                    '</div>' +
                '</div>' +
            '</div>' +
            '<div class="middle-l">' +
                '<div class="middle-r">' +
                    '<div class="middle-m"><a id="addItemBtn" class="bigadd"' + bigAddStyle +'"></a></div>' +
                '</div>' +
            '</div>' +
            '<div class="bottom-l">' +
                '<div class="bottom-r">' +
                    '<div class="bottom-m"></div>' +
                '</div>' +
            '</div>' +
        '</div>').appendTo($parent);

        var titleText = this._$struct.find('.top-l>.top-r>.top-m .title:first').css({
            'text-overflow': 'ellipsis',
            'white-space': 'nowrap',
            'display': 'block'
        }).text();
        
        this._$struct.find('.top-l>.top-r>.top-m .title:first').attr('title', titleText);

        this._$struct.attr('id', this._conf.id);
        
        this._$struct.find('#addItemBtn').click(function() {
            _this_._onBigAddClick();
        });
        
        // 在标题栏添加按钮
        if (this._conf.buttonList || addDelBtn) {
            var $titleBar = this._$struct.find('.top-m');
            var btn;
            var that = this;
            this._btns = [];

            $.each(this._conf.buttonList, function(_, conf){
                btn = window.RiilPortletButton.newInstance($titleBar, that, conf);
                that._btns.push(btn);
                if ('delete' === btn.getType() || 'close' === btn.getType()) {
                    addDelBtn = false;
                }
            });

            if (addDelBtn) {
                btn = window.RiilPortletButton.newInstance($titleBar, this, 'delete');
                this._btns.push(btn);
            }
        }

    };

    _p_.rectTo = function(rect, callback, duration) {
        var _this_ = this;
        var pos, size;
        var onResize;

        if (undefined !== rect.top || undefined !== rect.left) {
            pos = {};
            if (undefined !== rect.left) pos.left = rect.left;
            if (undefined !== rect.top) pos.top = rect.top;
        }

        if (undefined !== rect.width || undefined !== rect.height) {
            size = {};
            if (undefined !== rect.width) size.width = rect.width - G_BORDER_SIZE.width;
            if (undefined !== rect.height) size.height = rect.height - G_BORDER_SIZE.height;
            onResize = _this_._conf.listeners && _this_._conf.listeners.resize;
        }

        if (pos) {
            this._$struct.stop(true, true).animate(pos, duration, function() {
                if (callback) callback();
                callback = null;
            });
        }

        if (size) {
            if (null !== size.width && undefined !== size.width) {
                this._$struct.find('.top-m').stop(true, true).animate({width:size.width-18}, duration);
            }
            this._$struct.find('.middle-m').stop(true, true).animate(size, duration, function() {
                if (onResize) onResize(_this_._$struct);
                if (callback) callback();
                callback = null;
            });
        }
    };

    _p_.maximize = function (onFinished, $parent) {
        this.portlet._render._maximize(this, function() {
            if (onFinished) onFinished();
        }, $parent);
    };

    _p_.restore = function (onFinished) {
        this.portlet._render._restore(this, function() {
            if (onFinished) onFinished();
        });
    };
})(window.jQuery);;RiilPortletItemLoader = function ($) {
    var _getUrl = function(item_) {
        var loadParam = item_.getLoadParam();
        var flipParam = item_.getFlipParam();
        var confData = item_.getItemConfData();
        var portletConfData = item_.portlet.conf.data;
        var loadData = item_.getLoadData();

        var url = _readLoadData(item_.getLoadData());

        var param = 'portletId=' + item_.getPortletId();
        if (flipParam) param += '&' + flipParam;
        if (loadParam) param += '&' + loadParam;
        if (confData) param += "&" + RiilPortlet.getParam({}, confData);
        if (portletConfData) param += "&" + RiilPortlet.getParam({}, portletConfData);

        return [url, param];
    };

    var loader = {

        iframe : function (url_, param_, item_, onLoadComplete) {
            var $content = item_.getContent();
            _reloadIframe($content, url_, param_, function() {
                onLoadComplete && onLoadComplete(item_);
            });
        },

        url : function (url_, param_, item_, onLoadComplete) {
            var $content = item_.getContent();
            // div 方式
            PageCtrl.load({
                dom: $content,
                url: url_,
                param: param_,
                dataType: "html",
                callback: function() {
                    onLoadComplete && onLoadComplete(item_);
                },
                callbackParam: item_,
                error: function() {
                    onLoadComplete && onLoadComplete(item_);
                }
            });        
        },
        
        html : function (html_, _, item_, onLoadComplete) {
            var $content = item_.getContent();

            // div 方式
            $content.empty().append($(html_));            
            onLoadComplete && onLoadComplete(item_);
        }
    
    };
    var _reloadIframe = function ($target, url, param, onLoadComplete) {
        var $iframe = _getIframe($target);
        var spliter;
        if (url.indexOf("?") > 0) spliter = "&";
        else spliter = "?";
        
        $iframe[0].src = url + spliter + param + '&aabb=' + new Date().getTime();
        
        $iframe.unbind().bind('load', onLoadComplete);
    };
    
    var _setIframeHtml = function ($target, html) {
        var $iframe = _getIframe($target);
        var iframe = $iframe[0];
        var iframeDocument = document.all ? iframe.contentWindow.document : iframe.contentDocument;
        iframeDocument.body.innerHTML = "";
        iframeDocument.open();
        iframeDocument.write($(html).html());
        iframeDocument.close();
    };
    
    var _getIframe = function ($target) {
        var $iframe = $target.find("iframe");
        if ($iframe.length <= 0) {
            $iframe = _createIframe();
            $target.append($iframe);
        }
        
        return $iframe;
    };
    
    var _createIframe = function () {
        var $iframe = $('<iframe style="width:100%; height:100%;" '
                + 'frameborder="0" allowTransparency="true"></iframe>');
        
        $iframe.bind("beforeunload", function(event) {
            alert("unload frame");
            event.preventDefault();
            return false;
        });
        
        return $iframe;
    };
    
    // 读取“配置信息”的“载入信息”，如果该信息是函数，获取它的执行结果
    var _readLoadData = function (loadData) {
        var data;
        
        if ($.isFunction(loadData)) {
            data = loadData();
        }else {
            data = loadData;
        }

        // 只有在有值是才增加ctx信息
        if (loadData) {
            data = ctx[ctx.length - 1] === "/" ? ctx + data : ctx + "/" + data;
        }
        
        return data;
    };

    return {
        load : function (item_, type_, onLoadComplete) {
            var urlParam = _getUrl(item_);

            if (urlParam[0]) {
                urlParam.push(item_);
                urlParam.push(onLoadComplete);

                loader[type_ || 'url'].apply(this, urlParam);
            }else {
                onLoadComplete && onLoadComplete(item_);
            }
        }
    };
}(window.jQuery);;/* jslint node: true */
/* global $ */
(function(){'use strict';

var $;
if ('undefined' !== typeof module) {
    $ = require('jquery');
}else {
    $ = window.jQuery;
}

function PortletLoader(conf_) {    
    this._loadArray = [];
    this._timeoutMap = {};
    this._loadingCount = 0;


    if (conf_.workSize) this._workSize = conf_.workSize;
    if (conf_.timeout) this._timeout = conf_.timeout;

    if (conf_.callBack) {
        this._callBack = $.extend({
            load : function (/*items_*/) {},
            isLoadable : function (/*item*/) {}
        }, conf_.callBack);
    }

    if (conf_.listeners) {
        this._listeners = $.extend({
            onItemLoadBegin : function (/*item*/){},
            onItemLoaded : function (/*item*/){},
            onLoadTimeout : function (/*item*/){},
            onAllLoaded : function (){}
        }, conf_.listeners);
    }
}

if ('undefined' !== typeof module) module.exports = PortletLoader;
else window.PortletLoader = PortletLoader;

PortletLoader._loaderId = 0;

PortletLoader.prototype = {
    _workSize : 2,
    _timeout : 30000,
    _callBack : {},
    _listeners : {},
    
    
    load: function(items, onLoaded){
        this._listeners.onAllLoaded = onLoaded;
        
        if ($.isArray(items)) {
            $.merge(this._loadArray, items);
        }else {
            this._loadArray = [items];
        }

        this._doLoad();
    },
    
    
    setItemLoaded : function (item) {
        this._loaded(item);
    },
    
    
    _doLoad: function() {
        if (this._loadArray.length === 0) {
            if (this._loadingCount === 0 && this._listeners.onAllLoaded) {
                this._listeners.onAllLoaded();
            }
            return;
        }

        if (this._loadingCount >= this._workSize) {
            return;
        }
        
        var item = this._loadArray.shift();
        if (this._callBack.isLoadable(item) && !item.loading_id) {
            if (this._listeners.onItemLoadBegin) this._listeners.onItemLoadBegin(item);
            this._loadingCount += 1;
            
            var loadingId = this._createLoadId();
            item.loading_id = loadingId;
            this._timeoutMap[loadingId] = setTimeout($.proxy(function() {
                this._loadingCount -= 1;
                if (this._listeners.onLoadTimeout) this._listeners.onLoadTimeout(item);
                this._doLoad();
            }, this), this._timeout);
            
            this._callBack.load(item, $.proxy(function () {
                this._loaded(item);
            }, this));
        }
            
        this._doLoad();
    },
    
    
    _createLoadId : function() {
        return PortletLoader._loaderId += 1;
    },
    
    
    _loaded : function(item) {
        var loadingId = item.loading_id;
        clearTimeout(this._timeoutMap[loadingId]);
        delete this._timeoutMap[loadingId];
        delete item.loading_id;
        
        if (this._listeners.onItemLoaded) this._listeners.onItemLoaded(item);
        this._loadingCount -= 1;
        
        this._doLoad();
    }
};


})();
;(function() {'use strict';
	var _c_ = window.RiilPortletPageRender = function () {
	};

	_c_.prototype = {
	};

})();;/**
 * Portlet的页渲染器
 * ================
 * 
 * HTML结构:
 * ---------
 *
 *     <div id="slice">
 *         <div class="portlet"></div>
 *         <div class="portlet"></div>
 *         ...
 *     </div>
 * 
 * 
 * 效果:
 * -----
 *                                   
 *     |<--- target(parent) -->|  \
 *                                 > 一屏显示内容
 *    +------------+------------+ /
 *    |+-----------+-----------+|----------+-----------+-----------+-----------+
 *    ||           |           ||          :           :           :           :
 *    ||           |           ||          :           :           :           :
 *    ||  portlet  |  portlet  || portlet  :  portlet  :  portlet  :  portlet  :
 *    ||           |           ||          :           :           :           :
 *    ||           |           ||          :           :           :           :
 *    |+-----------+-----------+|----------+-----------+-----------+-----------+
 *    ||           |           ||          :           :           :           :
 *    ||           |           ||          :           :           :           :
 *    ||  portlet  |  portlet  || portlet  :  portlet  :  portlet  :  portlet  :
 *    ||           |           ||          :           :           :           :
 *    ||           |           ||          :           :           :           :
 *    |+-----------+-----------+|----------+-----------+-----------+-----------+
 *    +-------------------------+
 * 
 *     |<------------------------------- slice ------------------------------->|
 * 
 * javascript:
 * -----------
 * 
 *     RiilPortletPageRenderAbsolute.init({
 *         items : $('#portletParent>div'),
 *         layout : {width: 2, height: 2, padding: 5}
 *     });
 *
 *
 * 特性：
 * -----
 *
 *     - 支持调整大小resize
 *     - 支持翻页
 *     - 支持换位
 *     - 支持移动
 * 
 * 
 * @class RiilPortletPageRenderAbsolute
 */
(function($) {'use strict';

////////////////////////////////////////////////////////////////////////////////////////////////////
// 构造函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    var _s_ = window.RiilPortletPageRender;
    /**
     * @constructor
     * @param conf_ {JSON} 配置信息
     * @param conf_.items* {RiilPortletItem} 配置信息
     * @param conf_.layout {JSON} 布局信息
     * @param conf_.layout.width {Integer} 每页列数
     * @param conf_.layout.height {Integer} 每页行数
     * @param conf_.layout.padding {Integer} 各portlet相互间距
     */
    var _c_ = window.RiilPortletPageRenderAbsolute = function (conf_) {
        this.target = conf_.container;
        if (conf_.layout) this.layout = $.extend({}, this.layout, conf_.layout);
        if (conf_.pageNo) this.pageNo = conf_.pageNo;

        this._posMap = {};
        this._pageNoMap = {};
        this._idItemMap = {};
        this._order = [];
        
        this.target.css('overflow', 'hidden');
        this.target.wrapInner('<div style="position: relative;"></div>');
        
        this._arrange(conf_.items);
        this.resize();
    };
    var _p_ = window.classExtend(_c_, _s_);


////////////////////////////////////////////////////////////////////////////////////////////////////
// API
////////////////////////////////////////////////////////////////////////////////////////////////////
    _p_.pageTo = function (pageNo) {
        this._setPageNo(pageNo, true);
    };
    
    _p_.resize = function () {
        this._calulateSize();

        var item = null;
        var id = null;
        var pos = null;
        
        // 调整slice大小
        this.target.children('div')
            .width(this.pageCount * this.targetWidth)
            .height(this.targetHeight);
        
        // 调整每个portlet大小
        this._setItemSize();

        // 如果出现滚动条，造成宽度变化，重新计算
        if (this.target.parent()[0].scrollWidth !== this.targetWidth) {
            setTimeout($.proxy(this.resize, this), 1000);
        }
        
        // 调整每个portlet的位置
        for (id in this._idItemMap) {
            if (!this._idItemMap.hasOwnProperty(id)) continue;

            item = this._idItemMap[id];
            pos = this._posMap[id];
            this._setPosition(item, pos);
        }
        
        // 调整页码
        this._setPageNo(this.getPageNo());
    };

    _p_.getPath = function (srcItem_, desItem_) {
        var srcUid = srcItem_.getUid();
        var desUid = desItem_.getUid();
        var srcIndex = window.arrayIndexOf(this._order, srcUid);
        var desIndex = window.arrayIndexOf(this._order, desUid);
        var step, i;
        var path = [];
        if (srcIndex < desIndex) {
            step = 1;
        }else {
            step = -1;
        }

        for (i = srcIndex; i !== desIndex; i += step) {
            path.push(this._idItemMap[this._order[i]]);
        }
        path.push(this._idItemMap[this._order[desIndex]]);

        return path;
    };

    _p_.prepareMove = function(srcItem_, desItem_, isDragging_) {
        var desPos = this._getPosition(desItem_);
        var desPageNo = this.getPageNo(desItem_);
        var desXy = this._getXy(desPos);
        var desUid = desItem_.getUid();
        var srcUid = srcItem_.getUid();
        var desOrder = window.arrayIndexOf(this._order, desUid);
        var _this_ = this;

        return function() {
            _this_._order[desOrder] = srcUid;
            _this_._posMap[srcUid] = desPos;
            _this_._pageNoMap[srcUid] = desPageNo;
            if (isDragging_) {
                return function(callback) {
                    srcItem_.rectTo(desXy, callback, 200);
                };
            }else {
                srcItem_.rectTo(desXy);
            }
        };
    };

    _p_.move = function (moveActions_) {
        var count = moveActions_.length;
        var action;
        var callback, callbacks = [];
        while (count--) {
            action = moveActions_[count];
            if (action) {
                callback = action();
                if (callback) {
                    if (!callbacks) callbacks = [callback];
                    else callbacks.push(callback);
                }
            }
        }

        return callbacks;
    };

    _p_.getPortletContainer = function () {
        return this.target.children('div:first');
    };

    _p_.getHitPortlet = function (item) {
        var moveXy = item._$struct.position();
        var itemUid = item.getUid().toString();
        var uid, pos, distance, xy, has = Object.prototype.hasOwnProperty;
        for (uid in this._posMap) {
            if (!has.call(this._posMap, uid)) continue;
            if (uid === itemUid) continue;
            pos = this._posMap[uid];
            xy = this._getXy(pos);
            distance = Math.sqrt(Math.pow(moveXy.left - xy.left, 2) + Math.pow(moveXy.top - xy.top, 2));
            if (distance < 50) return this._idItemMap[uid];
        }
    };

    /**
     * 添加新页
     * @method addPage
     * @param {Doms or Dom} items* portlet的dom元素或数组
     */
    _p_.addPage = function (items) {
        items = items instanceof window.RiilPortletItem ? [items] : items;
        var item, uid, posPage, pos, pageNo, i, c = items.length;

        for (i = 0; i < c; i++) {
            item = items[i];
            uid = item.getUid();
            posPage = this._getPosFromIndex(this._order.length);
            pos = posPage.pos;
            pageNo = posPage.pageNo;

            this._idItemMap[uid] = item;
            this._posMap[uid] = pos;
            this._pageNoMap[uid] = pageNo;
            this._order.push(uid);
        }

        this.resize();
    };

    /**
     * 删除整页内容
     * @method removePage
     * @param pageNo {Integer} 要删除的页号
     * @return {Array} RiilPortlet_Item对象数组
     */
    _p_.removePage = function (pageNo) {
        var pageSize = this.layout.width * this.layout.height;
        var movStartIndex;
        if (undefined === pageNo) {
            movStartIndex = this._order.length;
        }else {
            movStartIndex = (pageNo) * pageSize;
        }

        var item1, item2, i, c = this._order.length;
        var moveActions = [];

        // 从后向前每个迁移pageSize个位置，同时删除指定页
        for (i = c - 1; i >= movStartIndex; i--) {
            item1 = this._idItemMap[this._order[i]];
            item2 = this._idItemMap[this._order[i - pageSize]];
            // 只删除前pageSize个元素
            moveActions.push(this.prepareMove(item1, item2));
            if (i >= movStartIndex && i < movStartIndex + pageSize) {
                delete this._idItemMap[item2.getUid()];
                item2.destroy();
            }
        }

        // 删除空出来的位置
        this._order.splice(-pageSize, pageSize);

        var pageCount = this.getPageCount();
        if (this.pageNo > pageCount) this.pageTo(pageCount);

        this.resize();

        this.move(moveActions);
    };


    /**
     * 替换Portlet
     * ===========
     * 
     * 流程：
     *     - 为replacer补充uid属性
     *     - 复制replacee的信息到replacer
     *     - 删除replacee极其信息
     * 
     * @method replace
     * @param {RiilPortlet_Item} replacer 替换成
     * @param {RiilPortlet_Item} replacee 被替换
     * @param {Boolean} keep 是否保留被替换portlet
     */
    _p_.replace = function(replacer, replacee) {
        var uidReplacer = replacer.getUid();
        var uidReplacee = replacee.getUid();
        var pos = this._posMap[uidReplacee];
        var pageNo = this._pageNoMap[uidReplacee];
        var order = window.arrayIndexOf(this._order, uidReplacee);

        this._posMap[uidReplacer] = pos;
        this._pageNoMap[uidReplacer] = pageNo;
        this._order[order] = uidReplacer;
        this._idItemMap[uidReplacer] = replacer;
        this._setPosition(replacer, pos);
        this._setItemSize(replacer);

        delete this._posMap[uidReplacee];
        delete this._pageNoMap[uidReplacee];
        delete this._idItemMap[uidReplacee];
        replacee.destroy();
    };

    _p_.indexOf = function(item) {
        var uid = item.getUid();
        return window.arrayIndexOf(this._order, uid);
    };

    _p_.get = function(index) {
        var uid = this._order[index];
        if (uid) {
            return this._idItemMap[uid];
        }
    };

    _p_.getAll = function() {
        var items = [];
        var item, c = this._order.length;
        while (c--) {
            item = this._idItemMap[this._order[c]];
            items.push(item);
        }
        return items;
    };

    _p_.count = function() {
        return this._order.length;
    };

    _p_.getPageSize = function() {
        return this.layout.width * this.layout.height;
    };

    _p_.getPageCount = function() {
        return Math.ceil(this._order.length / this.getPageSize());
    };

    _p_._getPortletSize = function() {
        var padding = this.layout.padding;
        var width = this.itemWidth - padding;
        var height = this.itemHeight - padding;
        return {width: width, height: height};
    };


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象变量
////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * portlet的uid与位置对应表
     * 
     *     {uid : pos}
     *     
     * @attribute _posMap
     * @type Map
     */
    _p_._posMap = {};
    _p_._pageNoMap = {};

    /**
     * portlet的uid与portlet对应表
     * 
     *     {uid : item}
     *     
     * @attribute _idItemMap
     * @type Map
     */
    _p_._idItemMap = {};

    /**
     * portlet的顺序
     * 
     *     [id]
     *     
     * @attribute _order
     * @type {Array}
     */
    _p_._order = [];

    /**
     * target(parent)见类注释
     * @attribute target
     * @type {[type]}
     */
    _p_.target = null;

    /**
     * 页号，从1开始
     * @attribute pageNo
     * @type {Number}
     */
    _p_.pageNo = 1;

    /**
     * 布局信息
     * @attribute layout
     * @type {JSON}
     */
    _p_.layout = {width : 2, height : 2, padding: 5};


////////////////////////////////////////////////////////////////////////////////////////////////////
// 对象函数
////////////////////////////////////////////////////////////////////////////////////////////////////
    
    _p_._getPosition = function (item_) {
        return this._posMap[item_.getUid()];
    };

    _p_.getItemPageNo = function (item_) {
        return this._pageNoMap[item_.getUid()];
    };

    _p_.isPageLoaded = function (pageNo) {
        var yeah = true;
        $.each(this._idItemMap, $.proxy(function(id_, item_){
            if (item_.isEmpty()) return;
            if (this.getItemPageNo(item_) === pageNo) {
                yeah = item_.isLoaded();
                return yeah;
            }
        }, this));

        return yeah;
    };

    _p_.getPageNo = function () {
        return this.pageNo;
    };
    
    _p_._setPosition = function (item_, pos_) {
        var uid = '';
        if (!pos_) {
            uid = item_.getUid();
            pos_ = this._posMap[uid];
        }
        item_.setPosition(this._getXy(pos_));
    };

    _p_._getXy = function (pos_) {
        var padding = this.layout.padding / 2;
        return {
            left : pos_.left * this.itemWidth + padding,
            top : pos_.top * this.itemHeight + padding
        };
    };
    
    _p_._setPageNo = function (number_, isAnimate_) {
        var width = this.target.width();

        if (!isAnimate_) {
            this.target.children('div').css('marginLeft', width * (1 - number_) + 'px');
        }else {
            this.target.children('div').stop(true, true).animate({'marginLeft': width * (1 - number_) + 'px'}, 300);
        }

        this.pageNo = number_;
        return this.pageNo;
    };
    
    _p_._getPosFromIndex = function (index_) {
        var w = this.layout.width;
        var h = this.layout.height;
        var pageSize = w * h;
        var pageIndex = Math.floor(index_ / pageSize);
        var indexInPage = index_ % pageSize;
        var rowIndex = Math.floor(indexInPage / w);
        var indexInRow = indexInPage % w;
        
        return {
            pos: {
                left : pageIndex * w + indexInRow,
                top : rowIndex
            },
            pageNo : pageIndex + 1
        };
    };

    _p_._getIndexFromPos = function (pos_) {
        var w = this.layout.width;
        var h = this.layout.width;
        var pageSize = w * h;
        var pageIndex = Math.floor(pos_.left / w);
        var rowIndex = pos_.top;
        var colIndex = pos_.left % w;

        var index = pageIndex * pageSize + w * rowIndex + colIndex;

        return index;
    };
    
    _p_._arrange = function (items_) {
        var item, uid, posPage, pos, pageNo, c = items_.length;

        this._posMap = {};
        this._idItemMap = {};
        this._order = [];

        while (c--) {
            item = items_[c];
            uid = item.getUid();
            posPage = this._getPosFromIndex(c);
            pos = posPage.pos;
            pageNo = posPage.pageNo;

            this._idItemMap[uid] = item;
            this._posMap[uid] = pos;
            this._pageNoMap[uid] = pageNo;
            this._order.unshift(uid);
        }
    };

    _p_._setItemSize = function (item_) {
        var size = this._getPortletSize();
        var uid, item, has = Object.prototype.hasOwnProperty;

        if (item_) {
            item_.setSize(size);
        }else {
            for (uid in this._idItemMap) {
                if (! has.call(this._idItemMap, uid)) continue;
                item = this._idItemMap[uid];
                item.setSize(size);
            }
        }
    };

    _p_._calulateSize = function () {
        var w = this.layout.width;
        var h = this.layout.height;
        var targetWidth = this.target.parent()[0].clientWidth;
        var targetHeight = this.target.height();

        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        this.itemWidth = targetWidth / w;
        this.itemHeight = targetHeight / h;
        this.pageCount = this._order.length / w / h;
    };

    _p_._maximize = function (item, onFinished) {
        var desRect;
        var initPos = item._$struct.offset();

        item._$struct.addClass('bgblue').css('z-index', window.ZIndexMgr.get())
            .appendTo(document.body).css(initPos);

        desRect = {left: 0, top: 0};
        desRect.width = $(window).width();
        desRect.height = $(window).height();

        item.rectTo(desRect, onFinished);
    };

    _p_._restore = function (item, onFinished) {
        var _this_ = this;
        var $parent = this.target.find('div:first');
        var desRect = this._getXy(this._getPosition(item));
        var parentOffset = $parent.offset();
        var size = this._getPortletSize();
        desRect.left += parentOffset.left;
        desRect.top += parentOffset.top;
        desRect.width = size.width;
        desRect.height = size.height;

        item.rectTo(desRect, function(){
            item._$struct.appendTo($parent).removeClass('bgblue');
            _this_._setPosition(item);
            window.ZIndexMgr.free(item._$struct);
            onFinished();
        });
    };

})(window.jQuery);;/*global window*/
(function($) {'use strict';

/**
 * 
 */
window.RiilPortlet.Paging = {
    // “配置信息”在界面数据中的KEY
    _CONFIG: 'config',
    
    // 配置信息及默认值
    DEFAULT_CONFIG: {
        renderer: 'defaultRenderer',
        pageCount: 0,
        currentPageNo: 1
    },
    
    // 初始化
    init: function(portlet, conf) {
        if (!conf) return;
        var self = window.RiilPortlet.Paging;

        // 补充默认值
        window.ConfigData.setDefaultValue(conf, self.DEFAULT_CONFIG);
        
        // 初始化界面
        var $paging = self._initUI(conf);
        
        $paging.data('portlet', portlet);
        
        // 绑定事件
        self._bindEvent($paging, conf.onConfig, conf.ok, conf.cancel);
        
        return $paging;
    },
    
    addPage : function ($paging, count) {
        var self = window.RiilPortlet.Paging,
            conf = $paging.data('conf');
        if (!count) count = 1;
        
        self.render[conf.renderer].addPage($paging, count);
    },
    
    delPage : function ($paging, count) {
        var self = window.RiilPortlet.Paging,
            conf = $paging.data('conf');
        if (!count) count = 1;
        
        self.render[conf.renderer].delPage($paging, count);
    },
    getCount : function ($paging) {
        var self = window.RiilPortlet.Paging,
            conf = $paging.data('conf');
        
        return self.render[conf.renderer].getPageCount($paging);
    },
    
    // 绑定事件
    // _bindEvent:function($paging){
    //     var self = window.RiilPortlet.Paging;
    //     self.getPageBtns$.bind('click', $paging, self._pageTo);
    // },
    
    // 初始化界面
    _initUI: function(conf) {
        var self = window.RiilPortlet.Paging;
        
        var $paging = self.render[conf.renderer].init(conf);

        $paging.data('conf', conf);
        
        return $paging;
    },
    
    _bindEvent: function($paging, onConfig, onOK, onCancel) {
        var self = window.RiilPortlet.Paging;
        var portlet = $paging.data('portlet');
        var portletConf = portlet.conf;
        
        $paging.click(self._pageTo);
        
        $paging.find('.setting').click(portletConf.refresh, onConfig || function(event) {
            portlet._pauseAutoPaging(true);

            var refreshConf = event.data;
            if (undefined === refreshConf) {
                refreshConf = {
                   refresh: {
                       value: 0,
                       valueList: [{
                           text: window.S_INTERVAL_2MIN || '!!S_INTERVAL_2MIN!!',
                           value: 120000
                       }, {
                           text: window.S_INTERVAL_5MIN || '!!S_INTERVAL_5MIN!!',
                           value: 300000
                       }, {
                           text: window.S_INTERVAL_10MIN || '!!S_INTERVAL_10MIN!!',
                           value: 600000
                       }]
                   },
                   paging: {
                       value: 0,
                       valueList: [{
                           text: window.S_INTERVAL_5MIN || '!!S_INTERVAL_5MIN!!',
                           value: 300000
                       }, {
                           text: window.S_INTERVAL_10MIN || '!!S_INTERVAL_10MIN!!',
                           value: 600000
                       }]
                   }
                };
            }
            var auto = portlet._getAutoInfo();
            if (refreshConf.refresh && auto && auto.refresh >= 0) {
                refreshConf.refresh.value = parseInt(auto.refresh, 10);
            }
            if (refreshConf.paging && auto && auto.paging >= 0) {
                refreshConf.paging.value = parseInt(auto.paging, 10);
            }

            refreshConf.listeners = {ok : onOK, cancel : onCancel};
            window.RiilPortlet.AutoRefresh.init(portlet._$struct, refreshConf);
        });
        
        $paging.find('a.add').click(function() {
            if (portletConf.showEditPop) portletConf.showEditPop();
        });
    },
    
    _pageTo: function(event) {
        if ('a' !== event.target.nodeName.toLowerCase()) {
            return;
        }else if ($(event.target).hasClass('setting') || $(event.target).hasClass('add')) {
            return;
        }
        
        var self = window.RiilPortlet.Paging,
            $btn = $(event.target),
            pageNo = $btn.data('pageNo'),
            $paging = $btn.data('paging');
        self.pageTo($paging, pageNo);
    },
    
    // 外部调用，用于翻到指定页
    pageTo: function($paging, pageNoTo) {
        var conf = $paging.data('conf');
        var pageNoFrom = conf.currentPageNo;
        var portlet = $paging.data('portlet');
        // 发出翻页事件
        portlet._pageChange(pageNoFrom, pageNoTo);
    },
    
    _pageToNow: function($paging, pageNo) {
        var $btns = $paging.data('btn');
        for (var i = 0, count = $btns.length; i < count; i++) {
            var $btn = $btns[i];
            if (parseInt($btn.data('pageNo'), 10) === pageNo) {
                $btn.parent().addClass('current');
            }else {
                $btn.parent().removeClass('current');
            }
        }

        var conf = $paging.data('conf');
        conf.currentPageNo = pageNo;
    },
    
    // 外部调用，获取所有翻页按钮
    getPageBtns$:function($paging){
        return $paging.data('btn');
    },
    
    // 外部调用，根据索引获取翻页按钮
    getPageBtn$ByIndex:function($page, index){
        var self = window.RiilPortlet.Paging;
        return self.getPageBtns$($page).filter('[pageNo="' + (index + 1) + '"]');
    },
    
    getPageCount: function($page) {
        return $page.data('conf').pageCount;
    }
};

window.RiilPortlet.Paging.render = {
    defaultRenderer: {
        init : function(conf){
            var $paging = $('<ul class="pagination"><li><a class="add" title="' +
                (window.S_PORTLET_CONFIG_ADD || '!!S_PORTLET_CONFIG_ADD!!') +
                '"></a></li></ul>');
            var $btns = [];

            if (conf.configBtn) {
                $paging.prepend('<li><a class="setting" title="' +
                    (window.S_PAGE_SETTING || '!!S_PAGE_SETTING!!') + '"></a></li>');
            }
            
            if (!conf.addBtn) {
                $paging.find('a.add').css('display', 'none');
            }

            $paging.data('btn', $btns);
            this.addPage($paging, conf.pageCount);
            
            $btns[conf.currentPageNo - 1].parent().addClass('current');
            
            return $paging;
        },
        addPage : function ($paging, addCount) {
            var $btns = $paging.data('btn'),
                startNo = $btns.length > 0 ? parseInt($btns[$btns.length - 1].data('pageNo'), 10) : 0;
            
            startNo += 1;
            
            for (var i = 0; i < addCount; i++) {
                var pageNo = i + startNo,
                    $li = $('<li></li>').insertBefore($paging.find('a.add').parent()),
                    $btn = $('<a href="javascript:void(0)"></a>')
                        .data('paging', $paging)
                        .data('pageNo', pageNo)
                        .appendTo($li);

                $btns.push($btn);
            }

        },
        delPage : function ($paging, delCount) {
            var $btns = $paging.data('btn'),
                btnCount = $btns.length;
            
            for (var i = 1; i <= delCount; i++) {
                $btns[btnCount - i].parent().remove();
            }
            
            $btns.splice(btnCount - delCount, 1);

        },
        getPageCount : function ($paging) {
            var $btns = $paging.data('btn'),
                btnCount = $btns.length;
            
            
            return btnCount;
        }
    }
};


})(window.jQuery);