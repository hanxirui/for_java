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
			var d = dialog({
	    	    title: '提示',
	    	    width: 300,
	    	    content: errorMsg,
	    	    okValue: '确定',
	    	    ok: function () {}
	    	});
	    	d.showModal();
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
		title = title || "提示";
		var $ = parent.$ || $;
		$.messager.show({
			title: title,
			msg: msg,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
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
			
			return 'FunUtil._runFun(event,'+currentIndex+');';
		}
		// 执行方法
		// index:索引.根据这个索引找到执行函数
		,_runFun:function(e,index){
			var handler = handlerStore[index];
			handler();// 该函数已经传入了参数
		}
	};
	
})();

if (typeof(jQuery) != 'undefined') {
    $(document).ajaxError(function (event, request, settings) {
        if (request.getResponseHeader("X-timeout") && request.status == 401) {
            // 页面跳转
        	top.location.href = ctx + 'needLogin.html';
        }else{
        	alert("系统异常");
        }
    });
}

// 全局函数
var Globle = {
	// 注销
	logout:function(){
		$.ajax({
			type: "POST",
		    url: ctx + 'logout.do',
		  	dataType:'json',
		    success: function(result){
				if (result.success){
					location.reload();
				} 
			},
			error:function(){
				location.reload();
			}
		});
	}
	// 移除某个节点下的iframe.
	,clearPanel:function(){
		return function(){
			var frame = $('iframe', this);
			if(frame.length > 0){
				frame.remove();
			}
		}
	}
}



function getFormData($schForm){
	var fields = $schForm.serializeArray();
	var obj = {};
	$.each( fields, function(i, field){
		var addValue = field.value;
		// 如果有同样的参数名,他们的值要变成数组形式保存
		if(obj[field.name]){
			var val = obj[field.name];

			if($.isArray(val)){
				val.push(addValue);
			}else{
				obj[field.name] = [val,addValue];
			}
		}else{
			obj[field.name] = addValue;
		}
	});

	return obj;
}

function loadFormData($frm,data) {
	for(var name in data) {
		var val = data[name];
		if(name!="sex"&&name!="type"&&name!="zhishibaifang"){
		  $('[name='+name+']').val(val);
		}
	}
//	性别单选框
	$("#sex"+data['sex']).attr("checked","true");
//	拜访性质单选框
	$("#type"+data['type']).attr("checked","true");
	
//	拜访性质单选框
	$("#zhishibaifang"+data['zhishibaifang']).attr("checked","true");
	
}

function loadTongjiFormData($frm,data) {
	for(var name in data) {
		var val = data[name];
		 $('[name=tongji'+name+']').val(val);
	}
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

