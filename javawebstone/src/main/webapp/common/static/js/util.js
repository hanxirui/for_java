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
})(jQuery);;

;;

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


////////////////////////////////////////////////////////////////////////////////////////////////////
//构造
////////////////////////////////////////////////////////////////////////////////////////////////////
var _s_ = window.DragMove;
var _c_ = window.DragMoveTree = function(conf_) {
////_s_.apply(this, arguments);

this._tree = conf_.tree;
if (conf_.listeners && conf_.listeners.accept) {
   ////this._listeners.accept = conf_.listeners.accept;
}
if (conf_.listeners && conf_.listeners.move) {
   ////this._listeners.move = conf_.listeners.move;
}
};

////var _p_ = window.classExtend(_c_, _s_);


