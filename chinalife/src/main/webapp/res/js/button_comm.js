var ButtonComm = {

	init : function() {

	},

	close : function(templateId, portletId, buttonId) {
		PageCtrl.ajax({
					url : ctx + "/portal/button/close?templateId=" + templateId
							+ "&portletId=" + portletId + "&buttonId="
							+ buttonId,
					type : "get",
					dataType : "json",
					success : function() {
						window.Report.remove($('#' + portletId)[0]);
					}
				});
	},
	// 刷新
	refresh : function(templateId, portletId, buttonId, callback, param) {
        $('#' + portletId + '_mask').css("borderRadius","10px");
        Report.websocket.on('ResponsePortletData', function(data) {
            if (data) {
                var portletId = data.portletId;
                var content = data.content;
                $("#"+portletId+"_content").html(content);
                if (callback) {
                    callback();
                }
            }
        });
        var args = {};
        if (param) {
            args = param;
        }
        
        var portletParams = Report.params[portletId];
        if (portletParams) {
            for (var key in portletParams) {
                args[key] = portletParams[key];
            }
        }
        
        args.templateId = templateId;
        args.portletId = portletId;
        args.ctx = ctx;
        Report.websocket.emit('GetPortletContent', args);
	},
	updateCoordinate : function (templateId, coords, callback) {
        PageCtrl.ajax({
            url: ctx + "updateCoordinate?templateId=" + templateId,
            type: "post",
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify(coords),
            success: function (data) {
            	if (callback) {
            	   callback();
            	}
            }
        });
	},
	saveSetting : function(templateId, portletId, buttonId, json, callback) {
		MaskShaw.start(portletId, true);
		PageCtrl.ajax({
					url : ctx + "/portal/button/saveSetting?templateId="
							+ templateId + "&portletId=" + portletId,
					data : json,
					contentType : 'application/json',
					type : "post",
					dataType : "json",
					success : function(data) {
						ButtonComm.refresh(templateId, portletId, buttonId, callback);  
						
					}
				});
	},
	/**
	 * 更新配置，saveSetting是整体进行保存，updateSetting是根据key进行更新
	 */
    updateSetting : function(templateId, portletId, buttonId, json, callback, param) {
        MaskShaw.start(portletId, true);
        PageCtrl.ajax({
                    url : ctx + "/portal/button/updateSetting?templateId="
                            + templateId + "&portletId=" + portletId,
                    data : json,
                    contentType : 'application/json',
                    type : "post",
                    dataType : "json",
                    success : function(data) {
                        ButtonComm.refresh(templateId, portletId, buttonId, callback, param);
                    }
                });
    },
    batchUpdateSettingByTemplateId: function(templateId, json, callback) {
    	PageCtrl.ajax({
			url: ctx + '/portal/button/batchUpdateSettingByTemplateId?templateId=' + templateId,
			data: json,
			contentType: 'application/json',
			type: 'post',
			dataType: 'json',
			success: function(data) {
				if (callback) callback();
			}
		});
    },

	resizePorletInfoHeight : function(portletId) {
		$obj = $("#" + portletId + "_infoDetail");
		if ($obj && $obj.length > 0) {
			$obj.height($obj.parent().height() - 20);
		}
		var $buttons = $("#" + portletId + "_component").find('div[role=menu]');
		var $porlet_component = $('#' + portletId + '_component');
		$.each($buttons, function(index, element) {
					if ($(element).attr("style") != null) {
						$(element).attr("style", "");
					}
					if ($(element).height() > ($porlet_component.height() - 40)) {
						$(element).height($porlet_component.height() - 50);
						$(element).width($(element).width() + 10);
						$(element).css("overflow-x", "hidden").css(
								"overflow-y", "auto");
					}
				})
	},

	initPageHeight : function() {
		$("#detail_container").height($(window).height() - 30);
	}
};