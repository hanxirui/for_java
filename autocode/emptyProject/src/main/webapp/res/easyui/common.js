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
			MsgUtil.error(errorMsg);
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

/**
 * var crud = Crud.create({
 *  pk:'id' // 实体类主键
	,addUrl:ctx + 'addDataSource.do' // 添加请求
	,updateUrl:ctx + 'updateDataSource.do' // 修改请求
	,delUrl:ctx + 'delDataSource.do' // 删除请求
	,dlgId:'dlg' // 对话框id
	,formId:'fm' // 表单id
	,girdId:'dg' // 表格id
})
 */
var Crud = (function(){
	
	var EF = function(){return true;};
	
	var CrudClass = function(param){
		this.addUrl = param.addUrl;
		this.listUrl = param.listUrl;
		this.updateUrl = param.updateUrl;
		this.delUrl = param.delUrl;
		this.searchFormId = param.searchFormId;
		this.pk = param.pk;
		this.encryptConfig = param.encryptConfig;
		this.onBeforeSave = param.onBeforeSave || EF;
		
		this.$dlg = $('#'+param.dlgId);
		this.$form = $('#'+param.formId);
		this.$grid = $('#'+param.gridId);
		
		this.gridType = 'datagrid';
		
		this.submitUrl;
	}
	
	CrudClass.prototype = {
		add:function(title){
			title = title || '添加'
			this.$dlg.dialog('open').dialog('setTitle',title);
			this.$form.form('reset');
			this.submitUrl = this.addUrl;
			
			if(this._hasPkInput()){
				this.getPkInput().prop('disabled',false);
			}
		}
		,update:function(row,title){
			title = title || '修改'
			if (row){
				this.$dlg.dialog('open').dialog('setTitle',title);
				this.$form.form('clear').form('load',row);
				
				this.submitUrl = this.updateUrl + ['?',this.pk,'=',row[this.pk]].join('');
				
				// 如果表单中有主键控件,则不能被修改
				if(this._hasPkInput()){
					this.getPkInput().prop('disabled',true);
				}
			}
		}
		,load:function(param){
			if(this.listUrl){
				this.runGridMethod('load',param);
			}
		}
		,search:function(){
			var data = Crud.getFormData(this.searchFormId);
			this.load(data);
		}
		,runGridMethod:function(methodName,param){
			this.$grid[this.gridType](methodName,param);
		}
		,_hasPkInput:function(){
			return this.getPkInput().length > 0;
		}
		,getPkInput:function(){
			if(!this.pkInput){
				this.pkInput = this.getByName(this.pk);
			}
			return this.pkInput;
		}
		,getByName:function(name){
			return this.$form.find('[name='+name+']');
		}
		,del:function(row,msg){
			msg = msg || '确定要删除该数据吗?';
			var self = this;
			if (row){
				$.messager.confirm('Confirm',msg,function(r){
					if (r){
						Action.post(self.delUrl,row,function(result){
							Action.execResult(result,function(result){
								self.runGridMethod('reload');	// reload the user data
							});
						});
					}
				});
			}
		}
		,save:function(){
			var self = this;
			this.$form.form('submit',{
				url: this.submitUrl,
				onSubmit: function(){
					var ret = self.onBeforeSave(self);
					if( (typeof ret != undefined) && ret === false ){
						return false;
					}
					return $(this).form('validate');
				},
				success: function(resultTxt){
					var result = $.parseJSON(resultTxt);
					Action.execResult(result,function(result){
						self.$dlg.dialog('close');		// close the dialog
						self.runGridMethod('reload');	// reload the user data
					});
				}
			});
		}
		,createOperColumn:function(buttons){
			return Crud.createOperColumn(buttons);
		}
		,createEditColumn:function(appendButton){
			appendButton = $.isArray(appendButton) ? appendButton : [];
			var that = this;
			var buttons = [
				{text:'修改',onclick:function(row){
					that.update(row);
				}}
				,{text:'删除',onclick:function(row){
					that.del(row);
				}}
			].concat(appendButton);
			
			return this.createOperColumn(buttons);
		}
		,createOperFormatter:function(buttons){
			return Crud.createOperFormatter(buttons);
		}
		/**
		 * 创建datagrid,options为追加的datagird属性
		 * crud.datagrid([    
			    {field:'name',title:'名称'}  
			    ,{field:'driverClass',title:'驱动'}  
			    ,{field:'driverClass',title:'驱动'}
			    ,{field:'jdbcUrl',title:'连接'}
			    
			],{toolbar:"#toolbar"});
		 */
		,buildGrid:function(columns,options){
			// 默认参数
			var settings = {    
			    url:this.listUrl,columns:[columns],toolbar:'#toolbar'
			    ,pagination:true,fitColumns:true,singleSelect:true,striped:true
			    ,pageSize:20
			    ,loadFilter:function(data){
			    	if(data.message){
			    		MsgUtil.error(data.message,'查询出错');
			    	}
			    	return data;
			    }
			};
			// 合并参数
			$.extend(settings, options);
			
			return this.$grid.datagrid(settings);
		}
		,buildTreegrid:function(columns,options){
			this.gridType = 'treegrid';
			// 默认参数
			var settings = {    
			    url:this.listUrl,columns:[columns],toolbar:'#toolbar'
			    ,fitColumns:true,animate:false,collapsible:true
			    ,pageSize:20
			    ,loadFilter:function(data){
			    	if(data.message){
			    		MsgUtil.error(data.message,'查询出错');
			    	}
					return data;
			    }
			};
			// 合并参数
			$.extend(settings, options);
			
			this.$grid.treegrid(settings);
		}
		,closeDlg:function(){
			this.$dlg.dialog('close');
		}
	}
	
	return {
		create:function(param){
			return new CrudClass(param);
		}
		,createOperColumn:function(buttons){
			if($.isArray(buttons) && buttons.length > 0){
				return {field:'_operate',title:'操作',align:'center',formatter:this.createOperFormatter(buttons)};
			}
			return undefined;
		}
		,createOperFormatter:function(buttons){
			buttons = $.isArray(buttons) ? buttons : [];
			// formatter句柄
			var formatterHandler = function(val,row,index){
				var html = [];
				for(var i=0,len=buttons.length; i<len; i++) {
					var button = buttons[i];
					var showFun = button.showFun || function(){return true;}
					// 是否显示
					if(showFun(row)){
						html.push('<a href="javascript:void(0)" onclick="'+FunUtil.createFun(button,'onclick',row,val,index)+'">'+button.text+'</a>')
					}
				}
				return html.join('<span style="color:#808080;padding:0 4px;">|</span>');
			}
			
			return formatterHandler;
		}
		/**
		 * 获取表单提交参数
		 */
		,getFormData:function(searchFormId){
			var fields = $("#"+searchFormId).serializeArray();
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
	};
	
})();


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
			
			// 阻止默认行为并取消冒泡
			if(typeof e.preventDefault === 'function') {
				e.preventDefault();
				e.stopPropagation();
			}else {
				e.returnValue = false;
				e.cancelBubble = true;
			}
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

/**
 * panel关闭时回收内存，主要用于layout使用iframe嵌入网页时的内存泄漏问题
 */
$.fn.panel.defaults.onBeforeDestroy = function() {
	var frame = $('iframe', this);
	try {
		if (frame.length > 0) {
			for ( var i = 0; i < frame.length; i++) {
				frame[i].src = '';
				frame[i].contentWindow.document.write('');
				frame[i].contentWindow.close();
			}
			frame.remove();
			if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
				try {
					CollectGarbage();
				} catch (e) {
				}
			}
		}
	} catch (e) {
	}
};

/**
 * 防止panel/window/dialog组件超出浏览器边界
 * @param left
 * @param top
 */
var easyuiPanelOnMove = function(left, top) {
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	}
	if (t < 1) {
		t = 1;
	}
	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;
	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(window).width();
	var browserHeight = $(window).height();
	if (right > browserWidth) {
		l = browserWidth - width;
	}
	if (buttom > browserHeight) {
		t = browserHeight - height;
	}
	$(this).parent().css({/* 修正面板位置 */
		left : l,
		top : t
	});
};
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

$.fn.datagrid.defaults.loadFilter = function(data) {
	if(data.message){
		MsgUtil.error(data.message,'查询出错');
	}
	return data;
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

var MsgUtil = {
	topMsg:function(msg,title){
		title = title || "提示";
		this.getJQ().messager.show({
			title: title,
			msg: msg,
			showSpeed:300,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
	}
	,alert:function(msg,title,type){
		title = title || "提示";
		type = type || 'info'
		
		if(msg && msg.length > 1000){
			this.getJQ().messager.show({
				title: title,
				msg: '<div style="height:300px;overflow-y: auto; overflow-x:hidden;">'+msg+'</div>',
				width:600,
				height:350,
				showType:null,
				timeout:0,
				style:{
					right:'',
					bottom:''
				}
			});
		}else{
			this.getJQ().messager.alert(title,msg,type);
		}
		
	}
	,error:function(msg,title){
		title = title || "错误";
		this.alert(msg,title,'error')
	}
	,confirm:function(msg,callback,title){
		title = title || "确认";
		this.getJQ().messager.confirm(title,msg,function(r){    
		    if (r){    
		        callback();
		    } 
		});
	}
	,getJQ:function(){
		return top.$ || $;
	}
}  

