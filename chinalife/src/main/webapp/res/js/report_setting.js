var ReportSetting = {

	init : function() {
		ReportSetting.recoverDefaultLayout();
	},
	close : function(resInstId, porletId, buttonId) {
		PageCtrl.ajax({
			url :   ctx + "/res/detail/third/button/close?resInstId="+resInstId+"&porletId="+porletId+"&buttonId="+buttonId,
			type : "get",
			dataType: "json",
			success : function(){
				DetailThirdIndex.remove($('#'+porletId)[0]);
			}
		});
	},
	refresh : function(resInstId, porletId, buttonId, callback) {
		MaskShaw.start(porletId, true);
		PageCtrl.ajax({
			  url : ctx + "/res/detail/third/button/refresh?resInstId="+resInstId+"&porletId="+porletId+"&buttonId="+buttonId,
			  type : "post",
			  dataType : "html",
		      success : function(data){
		    	  $('#'+ porletId +'_component').parent().html();
		    	  $('#'+ porletId +'_component').parent().html(data);
		    	  MaskShaw.stop(porletId);
		      }
		});
	},
	recoverDefaultLayout:function(){
		$("#recover").bind("click",function(){
			MaskShaw.start(porletId, true);
			PageCtrl.ajax({
			      url : ctx + "/res/detail/third/restoreFactoryLayout?resInstId="+resInstId,
			      type : "post",
			      dataType:"json",
				  success : function(){
					  location.href=location.href;
				  }
			});
		})
		
	},
	saveSetting : function(resInstId, porletId, buttonId, json, callback){
		MaskShaw.start(porletId, true);
		PageCtrl.ajax({
		      url : ctx + "/res/detail/third/button/saveSetting?resInstId="+resInstId+"&porletId="+porletId,
		      data : json,
		      contentType: 'application/json',
              type : "post",
              dataType : "json",
              success : function(data) {
				ReportSetting.refresh(resInstId, porletId, buttonId, callback);
				
		      }
		});
	},

	realtime : function(resInstId, porletId, buttonId) {
		MaskShaw.start(porletId, true);
		PageCtrl.ajax({
			url: ctx + "/res/detail/third/button/isOpenCCs",
			dataType:"json",
			success: function (data) {
		    	  if(data.ccsException != null){
		    		  MaskShaw.stop(porletId);
						RiilAlert.info(data.ccsException);
		    	  }else{
		    		  PageCtrl.ajax({
		    				url: ctx + "/res/detail/third/button/realtime?resInstId="+resInstId+"&porletId="+porletId+"&buttonId="+buttonId,
		    				dom: "temporary_div",
		    				success: function (data) {
		    			    	  $('#'+ porletId +'_component').parent().html();
		    			    	  $('#'+ porletId +'_component').parent().html(data);
		    			    	  MaskShaw.stop(porletId);
		    				}
		    			});
		    	  }
			}
		});
		
	},
	openNewMain : function(resInstId) {
		PageCtrl.winOpen({
			height : 760,
			width :1350,
			url : ctx+"/resMoni/resourceMonitor/window/openResDetailsView?resId="+resInstId,
			name : resInstId+"res_detail"
		});
	},
	openMain : function(resInstId) {
		window.location.href=ctx+"/res/detail/third/index?resInstId="+resInstId;
	},
	openSub : function(resInstId, subInstId) {
		window.location.href=ctx+"/subRes/detail/third/index?resInstId="+resInstId+"&subInstId="+subInstId;
	},
	openMetricListSetting : function(resInstId,porletId) {
		Panel.iframeShow({
			title : settingInfo,
			width : 550,
			height : 448,
			src : ctx+"/res/detail/third/button/openMetricSetting/index?resInstId="+resInstId+"&porletId="+porletId,
			id : "compSettingWindow"
		});
	},
	addResRelInfo : function(resInstId,porletId){
		Panel.iframeShow({
			title:settingInfo,
			height : 400,
			width :650,
			src : ctx+"/res/detail/third/button/addResRelInfo/index?resInstId="+resInstId+"&porletId="+porletId,
			id : "add_res_rel"
		});
		
	},
	resizePorletInfoHeight : function(porletId) {
		$obj = $("#"+porletId+"_infoDetail");
		if($obj && $obj.length > 0) {
			$obj.height($obj.parent().height() - 20);
		}
		var $buttons = $("#"+porletId+"_component").find('div[role=menu]');
		var $porlet_component = $('#'+porletId+'_component');
		$.each($buttons,function(index,element){
			if($(element).attr("style") != null){
				$(element).attr("style","");
			}
			if($(element).height()>($porlet_component.height()-40)){
				$(element).height($porlet_component.height()-50);
				$(element).width($(element).width()+10);
				$(element).css("overflow-x","hidden").css("overflow-y","auto");
			}
		})
	},
	initPageHeight : function() {
		$("#detail_container").height($(window).height()-30);
	},
	openScan:function(resInstId, porletId, buttonId,resTypeId){
		ReportSetting.scanSourcePorletId = porletId;
		ReportSetting.scanSourceResInstId = resInstId;
		ReportSetting.scanSourceButtonId = 'button_refresh';
		PageCtrl.ajax({
			  url : ctx + "/res/detail/third/button/scan?resInstId="+resInstId+"&resTypeId="+resTypeId,
			  type : "post",
			  dataType : "html",
		      success : function(data){
		      	if(data==""){
		      		RiilAlert.info(scannableResourceInfo);
		      	}else{
		      		Panel.show({
					height:530,
					width: 800,
					url : ctx+"/subRes/detail/pop/openSubResMonitor?resId="+resInstId+"&subModleId="+data+"&subResTypeId="+resTypeId,
					id : "scanProcessPopWindow",
					error : function(){
						}
		      		,success:function(){
		      		}
					});
		      	}
		      }
		});
		
	}
};