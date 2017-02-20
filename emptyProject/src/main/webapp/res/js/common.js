var Action = {
	ajsxSucc:function(e,succFun){
		if(succFun){
			succFun(e);
		}
	},
	/**
	 * 异步请求
	 */
	jsonAsyncActByData:function(url,data,succFun){
		$.ajax({
			type: "POST",
			url: url,
			async:true,
			traditional:true,
			dataType: "json",
			data:data,
			success: function(e){
				Action.ajsxSucc(e,succFun);
			},
			error:function(hxr,type,error){
				Action._showError('后台出错，请查看日志');
			}
		});
	},
	post:function(url,data,succFun){
		this.jsonAsyncActByData(url,data,succFun);
	},
	/**
	 * 同步请求
	 */
	jsonSyncActByData:function(url,data,succFun){
		$.ajax({
			type: "POST",
			url: url,
			async:false,
			traditional:true,
			dataType: "json",
			data:data,
			success: function(e){
				Action.ajsxSucc(e,succFun);
			},
			error:function(hxr,type,error){
				Action._showError('后台出错，请查看日志');
			}
		});
	}
	,postSync:function(url,data,succFun){
		this.jsonSyncActByData(url,data,succFun);
	}
	/**
	 * 获取url后面的参数
	 */
	,getQueryString:function (key){ 
		var url=location.href; 
		url = url.toLowerCase();
		key = key.toLowerCase();
		if(url.indexOf('?')==-1)return "";	
		var urlarr = url.split("?");
		urlarr = urlarr[urlarr.length-1];
		urlarr = urlarr.split("&");	
		for(var i=0;i<urlarr.length;i++){
			var s=urlarr[i].split("=");
			if(s[0]==key){
				return s[1];
			}
		}
		return "";
	}
	,execResult:function(result,successFun){
		if(result && result.success){
			successFun && successFun(result);
		}else{
			var errorMsg = result.message;
			errorMsg = errorMsg + '<br>' + this.buildValidateError(result);
			BUI.Message.Alert(errorMsg);
		}
	}
	,buildValidateError:function(result){
		if(result.messages && result.messages.length > 0) {
			var validateErrors = result.messages;
			return '<span style="color:red;">' + validateErrors.join('<br>') + '</span>';
		}else{
			return "";
		}
	}
	,_showError:function(msg,title){
		BUI.Message.Alert(msg);
	}
}


var EventUtil = {
	/**
	 * 格式化事件对象
	 */
	getEvent : function(){
		if(window.event){
			return this.formatEvent(window.event);
		}else {
			return this.getEvent.caller.arguments[0];
		}
	}
	/**
	 * 格式化事件对象,做到IE与DOM的统一
	 * @param oEvent:事件对象
	 */
	,formatEvent : function(oEvent){
		if($.browser.msie){
			oEvent.charCode = (oEvent.type == "keypress")?oEvent.charCode:0;
			oEvent.eventPhase = 2;
			oEvent.isChar = (oEvent.charCode > 0);
			oEvent.pageX = oEvent.clientX + document.body.scrollLeft;
			oEvent.pageY = oEvent.clientY + document.body.scrollTop;
			// 阻止某个事件的默认行为
			oEvent.preventDefault = function(){
				this.returnValue = false;
			}
			
			if(oEvent.type == "mouseout"){
				oEvent.relateTarget = oEvent.toElement;
			} else if(oEvent.type == "mouseover"){
				oEvent.relateTarget = oEvent.fromElement;
			}
			
			// 阻止冒泡
			oEvent.stopPropagation = function(){
				this.cancelBubble = true;
			}
			
			oEvent.target = oEvent.srcElement;
			oEvent.timestamp = (new Date()).getTime();
		}
		return oEvent;
	}
}
/*
&nbsp;使用方法:
&nbsp;FunUtil.createFun(scope,'some_mothod_name',obj1);
&nbsp;FunUtil.createFun(scope,'some_mothod_name',obj1,obj2);
&nbsp;...
*/
var FunUtil = (function(){
	
	var index = 0; 
	var handlerStore = []; // 存放方法句柄
	
	return {		
		// scope:作用域
		// methodName:方法名,字符串格式
		// ...:参数可放多个
		createFun:function(scope,methodName){
			var currentIndex = index++; // 创建索引
			
			var argu = []; // 用来存放多个参数
			// 构建参数
			for(var i=2,len=arguments.length;i<len;i++){
				argu.push(arguments[i]);	
			}

			// 把函数句柄存在数组里
			handlerStore[currentIndex] = (function(scope,methodName,argu){
				// 生成函数调用句柄
				return function(){
					scope[methodName].apply(scope,argu);
				}

			}(scope,methodName,argu));			
			
			return 'FunUtil._runFun(event,'+currentIndex+',false);';
		}
		// 
		// 阻止事件冒泡
		// scope:作用域
		// methodName:方法名,字符串格式
		// ...:参数可放多个
		,createFunStopProp:function(scope,methodName) {
			var currentIndex = index++; // 创建索引
			
			var argu = []; // 用来存放多个参数
			// 构建参数
			for(var i=2,len=arguments.length;i<len;i++){
				argu.push(arguments[i]);	
			}

			// 把函数句柄存在数组里
			handlerStore[currentIndex] = (function(scope,methodName,argu){
				// 生成函数调用句柄
				return function(){
					scope[methodName].apply(scope,argu);
				}

			}(scope,methodName,argu));			
			
			return 'FunUtil._runFun(event,'+currentIndex+',true);';
		}
		// 执行方法
		// index:索引.根据这个索引找到执行函数
		,_runFun:function(e,index,stopProp){
			var handler = handlerStore[index];
			handler();// 该函数已经传入了参数
			
			if(stopProp) {
				// 阻止默认行为并取消冒泡
				if(typeof e.preventDefault === 'function') {
					e.preventDefault();
					e.stopPropagation();
				}else {
					e.returnValue = false;
					e.cancelBubble = true;
				}
			}
		}
	};
	
})();

if (typeof(jQuery) != 'undefined') {
    $(document).ajaxError(function (event, request, settings) {
        // 页面跳转
        top.location.href = ctx + 'index.jsp';
    });
}
	
var HtmlUtil = (function(){
	
	var parseHtmlMap = {
		"<":"&lt;"
		,">":"&gt;"
		,"\r\n":"<br>"
		," ":"&nbsp;"
		,"\t":"&nbsp;&nbsp;&nbsp;&nbsp;"
	}
	
	var parseTextMap = {
		"\&nbsp;":' '
		,"\<br ?\/?\>":'\r\n'
		,"\&lt;":"<"
		,"\&gt;":">"
	}
	
	function parse(content,map){
		for(var key in map){
			content = content.replace(new RegExp(key, "g"),map[key]);
		}
		return content;
	}
	
	return {
		parseToHtml:function(text){
			return parse(text,parseHtmlMap);
		}
		,parseToText:function(html){
			return parse(html,parseTextMap);
		}
	}
	
}());
/**
 * 使用方法:
 * 开启:MaskUtil.mask();
 * 关闭:MaskUtil.unmask();
 * 
 * MaskUtil.mask('其它提示文字...');
 */
var MaskUtil = (function(){
	
	var $mask,$maskMsg;
	
	var defMsg = '正在处理，请稍待。。。';
	
	function init(){
		if(!$mask){
			$mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");
		}
		if(!$maskMsg){
			$maskMsg = $("<div class=\"datagrid-mask-msg mymask\">"+defMsg+"</div>")
				.appendTo("body").css({'font-size':'12px'});
		}
		
		$mask.css({width:"100%",height:$(document).height()});
		
		var scrollTop = $(document.body).scrollTop();
		
		$maskMsg.css({
			left:( $(document.body).outerWidth(true) - 190 ) / 2
			,top:( ($(window).height() - 45) / 2 ) + scrollTop
		}); 
				
	}
	
	return {
		mask:function(msg){
			init();
			$mask.show();
			$maskMsg.html(msg||defMsg).show();
		}
		,unmask:function(){
			$mask.hide();
			$maskMsg.hide();
		}
	}
	
}());

// 导出工具
var ExportUtil = {
	doExport:function(url,param){
		param = param || {};
		if(!this.$form) {
			this.$form = $('<form method="post"></form>');
			$('body').append(this.$form);
		}
		var html = [];
		
		for(var paramName in param) {
			var val = param[paramName]
			html.push('<input type="hidden" name="'+paramName+'" value="'+val+'"/>');
		}
		this.$form.html(html.join(''));
		this.$form.attr('action',url);
		this.$form.get(0).submit();
	}
}


/**
 * 权限检查<br>
 * 使用方法:RightUtil.setData(datas);
 * var hasPermission = RightUtil.check(1,'view');
 * @class
 */
var RightUtil = (function() {
	
	var config = {
		// 系统资源ID名称
		systemResourceIdName:'srId'
		// 操作代码名称
		,operateCodeName:'operateCode'
		// 强行检查,如果没有定义operateCode属性则没有权限
		,forceCheck:false
	};
	
	var paramMap;
	
	/**
	 * 结构:
	 * //key/value -> srId/operateCodes
	 * var permissionData = {
			{"1":["view","update"],"2":["del"]}
			,{"2":["view","del"]}
		}
	* */
	var permissionData = [];
	
	/**
	 * 数组arr中是否包含o
	 * @param arr 数组
	 * @param o 
	 * @return 包含返回true
	 */
	function contains(arr,o) {
		return indexOf(arr,o) >= 0;
	}
	/**
	 * o在数组elementData中的位置,从0开始,没有则返回-1
	 * @param elementData 数组
	 * @param o 
	 */
	function indexOf(elementData,o) {
		if (o && $.isArray(elementData)) {
			for (var i = 0,size=elementData.length; i < size; i++){
            	if (o == elementData[i]){
                	return i;
                }
			}
        } 
        return -1;
	}
	
	function getParamMap() {
		if(!paramMap) {
			var map = {};
			var query = location.search; 
			if(query && query.length>=1) {
				query = query.substring(1);
				var pairs = query.split("&");
				for(var i = 0; i < pairs.length; i++) { 
					var pos = pairs[i].indexOf('='); 
					if (pos == -1) continue; 
					var argname = pairs[i].substring(0,pos); 
					var value = pairs[i].substring(pos+1); 
					value = decodeURIComponent(value);
					map[argname] = value;
				}
			}
			
			paramMap = map;
		}
		return paramMap;
	}
	
	function getParam(key) {
		return getParamMap()[key];
	}
	
	return {
		config:function(configParam){
			$.extend(config,configParam);
		}
		,setData:function(data) {
			permissionData = data;
			return this;
		}
		,setForceCheck:function(b){
			config.forceCheck = !!b;
			return this;
		}
		/**
		 * 检查权限
		 */
		,check:function(srId,operateCode){
			// 非强制检查,如果operateCode属性没有定义则显示
			if(!config.forceCheck && typeof operateCode === 'undefined') {
				return true;
			}
			
			if(!srId || !operateCode) {
				return false;
			}
			
			var operateCodeArr = permissionData[srId];
			
			return contains(operateCodeArr,operateCode);
		}
		,checkByCode:function(operateCode,callback,failVal){
			var srId = getParam(config.systemResourceIdName);
			var hasPerm = this.check(srId,operateCode);
			if(hasPerm && callback) {
				return callback();
			}else{
				return failVal;
			}
		}
		/**
		 * 验证权限,如果通过则返回successVal,否则返回failVal
		 */
		,auth:function(operateCode,successVal,failVal) {
			var isValid = this.checkByCode(operateCode,function(){
				return true;
			});
			
			return isValid ? successVal : failVal;
		}
	};
	
})();

