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
                        if (templateId == 'ete_biz_bizapp' && window.EteBizAppPortal) {
                            window.EteBizAppPortal.remove($('#' + portletId)[0]);
                        } else if (templateId == 'ete_biz_basis' && window.EteBizBasisPortal) {
							window.EteBizBasisPortal.remove($('#' + portletId)[0]);
						}else if (templateId == 'wl_biz_exp' && window.WlBizExperience) {
							window.WlBizExperience.remove($('#' + portletId)[0]);
						}
					}
				});
	},
	// 刷新
	refresh : function(templateId, portletId, buttonId, callback, param) {
        MaskShaw.start(portletId, true);
        $('#' + portletId + '_mask').css("borderRadius","10px");
        EtePortalIndex.socket.on('ResponsePortletData', function(data) {
            if (data) {
                var portletId = data.portletId;
                var content = data.content;
                $("#"+portletId+"_content").html(content);
                if (callback) {
                    callback();
                }
            }
            MaskShaw.stop(portletId);
        });
        var args = {};
        if (param) {
            args = param;
        }
        if (templateId == 'ete_user' || templateId == 'ete_biz_bizapp' || templateId == 'ete_biz_basis' || templateId == 'ete_exit_biz') {
            var portletParams = EtePortalIndex.portletParams[portletId];
            if (portletParams) {
                for (var key in portletParams) {
                    args[key] = portletParams[key];
                }
            }
        }
        args.templateId = templateId;
        args.portletId = portletId;
        args.userId = userId;
        args.ctx = ctx;
        EtePortalIndex.socket.emit('GetPortletContent', args);
	},
	// 无线应用体验刷新
	wlrefresh : function(templateId, portletId, buttonId, callback, param) {
        MaskShaw.start(portletId, true);
        $('#' + portletId + '_mask').css("borderRadius","10px");
        WlPortalIndex.socket.on('ResponsePortletData', function(data) {
            if (data) {
                var portletId = data.portletId;
                var content = data.content;
                $("#"+portletId+"_content").html(content);
                if (callback) {
                    callback();
                }
            }
            MaskShaw.stop(portletId);
        });
        var args = {};
        if (param) {
            args = param;
        }
     /*  
       if (templateId == 'ete_user' || templateId == 'ete_biz_bizapp' || templateId == 'ete_biz_basis' || templateId == 'ete_exit_biz') {
            var portletParams = WlPortalIndex.portletParams[portletId];
            if (portletParams) {
                for (var key in portletParams) {
                    args[key] = portletParams[key];
                }
            }
        }
        */
        args.templateId = templateId;
        args.portletId = portletId;
        args.userId = userId;
        args.ctx = ctx;
        WlPortalIndex.socket.emit('GetPortletContent', args);
	},
	
	openTree:function(templateId, portletId, buttonId, callback) {	    
		var btnPos = $("#" + buttonId).offset();
		var type = "wl_user_exp" === templateId ? "usr" : "wifi";
      
		$("#locationTreeWrapper").empty();
		PageCtrl.ajax({
			url : ctx + "/wireless/hotspot/osAlarmTree",
			data : {},
			type : "post",
			dataType : "text",
			success : function(data) {
				if (!data) {
					RiilAlert.toast("请先添加地理位置。", "warn");
					return;
				}
				
				var treeId = "locationTree_" + portletId;
				var treeHtml = $($(data)[2]).attr("id", treeId);
				treeHtml.find('[nodeid="-1"]>span[type=text]').attr('clickable',false);
				treeHtml.find('[nodeid="-1"]>span[type=text]').css('cursor','default');
				
				var layerConf = {
					id : "locationLayer_" + portletId,
					x : btnPos.left - 216,
					y : btnPos.top + 9,
					width : 216,
					height : 270,
					html : treeHtml,
					initHide : false,
					remove : true,
					isAutoHide : true
				};
				if ($("#locationLayer_" + portletId)[0]) {
					Layer.show("locationLayer_" + portletId, {
						x : btnPos.left - 216,
						y : btnPos.top + 9
					});
					return;
				} else {
					Layer.init(layerConf);
				}
				
				var bizInfoUl = new Tree({
					id : treeId,
					listeners : {
						nodeClick : function(node) {
							var inputDom = node.$li.find("input").first();
							
							if (inputDom.attr("checked")) {
								inputDom.removeAttr("checked");
							} else {
								inputDom.attr("checked", "checked");
							}
							
							saveLoc();
						}
					}
				});
				
				treeHtml.before('<div class="select_body" id="' + treeId + '_query" style="width: auto;"><input class="select_text default" id="' + treeId + '_query_input" style="width:186px;" emptyValue="请输入地理位置名称" /><div class="select_trigger_wrap"><a class="select_trigger_del"></a></div></div>');
				QueryTree.init({
					tree: bizInfoUl,
					listeners: {
						onChanged: function(nodeId,treeId){
							var $node = bizInfoUl.getNodeById(nodeId);
							if($node!=undefined){
								var treeDom = $("#locationLayer_" + portletId + " .middle-m");
								treeDom.scrollTop($node.$li.offset().top - treeDom.offset().top + treeDom.scrollTop());
							}
						}
					},
					isHideFilterAttr: true,
					isMulti: false
				});
				
				$("li[nodeid=-1] li>div").each(function() {
					$(this).after('<span><input name=' + $(this).parent().attr("fullname") + ' value="' + $(this).parent().attr("nodeid") + '" type="checkbox" style="margin-top:-1px" /></span>');
				});
				$("#locationTree_" + portletId + " input[type=checkbox]").click(function() {
					saveLoc();
				});
				$("#locationTree_" + portletId + " span[type=ico]:gt(0)").remove();
				loadLoc();
			}
		});
		
		function saveLoc() {
			var locIds = [];
			
			$("#locationTree_" + portletId + " input[type=checkbox]:checked").each(function() { locIds.push($(this).attr("value")); });
			PageCtrl.ajax({
				url : ctx + "/education/wirelessexp/saveLoc?",
				type : "post",
				dataType : "text",
				data: "locIds=" + locIds.toString() + "&type=" + type,
				success : function(data) {
					if (data != "success") console.error(data);		
					
					if (type === "usr") {
						var widget = WlUser._gridster.$el.find(">li[id=" + portletId + "]");
						WlUser._gridster.resize_widget(widget, widget.coords().grid.size_x, Math.ceil(Math.ceil(locIds.length / 2) * 155 / WlUser._gridster.options.widget_base_dimensions[1]),
								function() {
							updateCoords(WlUser._gridster.serialize());
							EteButtonComm.refresh(templateId, portletId);
						});
					} else {
						var widget = WlDevice._gridster.$el.find(">li[id=" + portletId + "]");
						WlDevice._gridster.resize_widget(widget, widget.coords().grid.size_x, locIds.length < 7 ? 5 : Math.ceil((275 + (locIds.length - 6) * 30) / WlDevice._gridster.options.widget_base_dimensions[1]) - 1,
								function() {
							updateCoords(WlDevice._gridster.serialize());
							EteButtonComm.refresh(templateId, portletId);
						});
					}
				}
			});
		}
		
		function loadLoc() {
			PageCtrl.ajax({
				url : ctx + "/education/wirelessexp/loadLoc?",
				type : "post",
				dataType : "text",
				data: "type=" + type,
				success : function(data) {
					if (data) {
						var locIds = JSON.parse(data);
						
						if (locIds.length > 0 && !(locIds.length == 1 && !locIds[0])) {
							$.each(locIds, function() {
								$("#locationTree_" + portletId + " input[type=checkbox][value=" + this + "]").attr("checked", "checked");
							});
						}
					}
				}
			});
		}
		
		function updateCoords(coords) {
			PageCtrl.ajax({
                url: ctx + "/portal/button/updateCoordinate?templateId=" + templateId,
                type: "post",
                contentType: 'application/json',
                dataType: "json",
                data: JSON.stringify(coords),
                success: function (data) {
                }
            });
		}
	},
	updateCoordinate : function (templateId, coords, callback) {
        PageCtrl.ajax({
            url: ctx + "/portal/button/updateCoordinate?templateId=" + templateId,
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
					    if(templateId.indexOf("wl_")>=0){
					        EteButtonComm.wlrefresh(templateId, portletId, buttonId, callback);     
					    }else{
					        EteButtonComm.refresh(templateId, portletId, buttonId, callback);  
					    }
						
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
                        EteButtonComm.refresh(templateId, portletId, buttonId, callback, param);
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
	},
	
    // 用户端-用户体验趋势分析-关注地理位置设置
    openUserExpTrendSetting : function(templateId, portletId) {
        PageCtrl.load({
            url: ctx + '/portal/button/openUserExpTrendSetting?templateId=' + templateId + '&portletId=' + portletId,
            type: 'post',
            param: '',
            dom: 'userExpTrendSettingContent',
            callback: function() {
                $('#userExpTrendSettingDiv').modal({});
            }
        });
    },
    
    // 用户端-用户分布-用户分布设置
    openUserDistributionSetting : function(templateId, portletId) {
    	if (Loading) Loading.start();
    	$('#userDistributionSettingContent').empty();
        PageCtrl.load({
            url: ctx + '/portal/button/openUserDistributionSetting?templateId=' + templateId + '&portletId=' + portletId,
            type: 'post',
            param: '',
            dom: 'userDistributionSettingContent',
            callback: function() {
                $('#userDistributionSettingDiv').modal({});
                if (Loading) Loading.stop();
            }
        });
    },
    
	// 出口业务端-出口流量区间-出口实时分析
    openOutletFlowRangeAnalysis : function(templateId, portletId) {
    	if (Loading) Loading.start();
    	$('#outletFlowRangeAnalysisContent').empty();
        PageCtrl.load({
            url: ctx + '/portal/button/openOutletFlowRangeAnalysis?templateId=' + templateId + '&portletId=' + portletId,
            type: 'post',
            param: '',
            dom: 'outletFlowRangeAnalysisContent',
            callback: function() {
	        	$('#outletFlowRangeAnalysisDiv').on('hidden.bs.modal', function(e) {
	        		if (OutletFlowRangeAnalysis.intervalId) {
	        			clearInterval(OutletFlowRangeAnalysis.intervalId);
	        		}
	        	});
                $('#outletFlowRangeAnalysisDiv').modal({});
                if (Loading) Loading.stop();
            }
        });
    },
    
    // 业务端-业务应用-业务卡片设置
    openBizCardSetting : function(templateId, portletId) {
        PageCtrl.load({
            url: ctx + '/portal/button/openBizCardSetting?templateId=' + templateId + '&portletId=' + portletId,
            type: 'post',
            param: '',
            dom: 'bizCardSettingContent',
            callback: function() {
                $('#bizCardSettingDiv').modal({});
            }
        });
    },
    // 业务端-业务应用-业务体验度设置
    openBizExperienceSetting : function(templateId, portletId) {
        PageCtrl.load({
            url: ctx + '/portal/button/openBizExperienceSetting?templateId=' + templateId + '&portletId=' + portletId,
            type: 'post',
            param: '',
            dom: 'bizExperienceSettingContent',
            callback: function() {
                $('#bizExperienceSettingDiv').modal({});
            }
        });
    },
    
    //用户体验趋势分析-用户关注区域设置
    openAreaTree:function(templateId, portletId) {	    
    	 PageCtrl.load({
    		   url: ctx + '/education/wirelessexp/openWlUserTrendSetting?templateId=' + templateId + '&portletId=' + portletId,
             type: 'post',
             param: '',
             dom: 'wlExperienceTrendSettingContent',
             callback: function() {
                 $('#wlExperienceTrendSettingDiv').modal({});
             }
         });		
    },
    // 业务端-基础支撑-基础支撑卡片设置
    openBasicSchemaCardSetting : function(templateId, portletId) {
        PageCtrl.load({
            url: ctx + '/portal/button/openBasicSchemaCardSetting?templateId=' + templateId + '&portletId=' + portletId,
            type: 'post',
            param: '',
            dom: 'basicSchemaCardSettingContent',
            callback: function() {
                $('#basicSchemaCardSettingDiv').modal({});
            }
        });
    }
};