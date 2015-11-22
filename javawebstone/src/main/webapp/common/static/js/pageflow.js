/* global $,LayoutManager,RiilAlert,ExceptionAlert */
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
};