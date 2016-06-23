(function ($) {
	window.Report = {
		_gridster: null,
		_config: null,
		websocket : null,

		onModify: function () {
			PageCtrl.ajax({
				url: ctx + "/res/detail/third/button/updateCoordinate?resInstId=" + resInstId,
				type: "post",
				contentType: 'application/json',
				dataType: "json",
				data: JSON.stringify(Report.toJson()),
				success: function (data) {
					
				}
			});
		},

		init: function () {
			Report._gridster = $(".gridster ul").gridster({
				widget_base_dimensions: [50, 60],
				widget_margins: [5, 5],
				//autogrow_cols: true,
				helper: 'clone',
				resize: {
					enabled: true,
					min_size: [1, 1],
					axes: ['x', 'y', 'both'],
					stop: function () {
						if (Report.onModify) {
							Report.onModify.call(this, arguments);
						}
						DetailThirdCommon.resizePorletInfoHeight(arguments[2].attr("id"));
						Report.executeContentResize(arguments[2].find('[id$="component"]'));
						//如果是列表,回调方法，以调整滚动条显示
						$input = arguments[2].find('[id$="component"]').find("input[name='callbackfunction']");
						if($input.length > 0) {
							eval($input.val());
						}
					}
				},
				draggable: {
					handle: '.Det_title_blue',
					ignore_dragging :  ['INPUT', 'TEXTAREA', 'SELECT', 'BUTTON'],
					stop: function () {
						if (Report.onModify) {
							Report.onModify.call(this, arguments);
						}
					}
				},
				serialize_params: function ($w, wgd) {
					return {
						id: $w.attr('id'),
						'data-col': wgd.col,
						'data-row': wgd.row,
						'data-sizex': wgd.size_x,
						'data-sizey': wgd.size_y
					};
				}
			}).data('gridster');

			$.each(newList, function (i, widget) {
				Report.append.apply(null, widget);
			});

			$(document).click(function(e){
				 e = window.event || e; // 兼容IE7
	        	 obj = $(e.srcElement || e.target);
	        	 if($('#toolslayer:visible').length > 0){
		        	    if (!$(obj).is($("#toolslayer")) && !$(obj).is($('#show_toolslayer')) && !$(obj).is($('#show_tools'))) { 
		        	     $('#toolslayer').hide();
		        		 $("#show_toolslayer").removeClass("tools_layer_on");
		        	   }
	        	 }
	        	 if($('#settingslayer:visible').length > 0){
	        		 if(!$(obj).is($("#settingslayer")) && !$(obj).is($('#show_settings')) && !$(obj).is($('#show_setting_button'))) {
	        	    	$('#settingslayer').hide();
		        		 $("#show_settings").removeClass("tools_layer_on");
	        	    }
	        	 }
	        });
			Report.getContent();
			Report.bindNavigation();
			Report.bindToolList();
			Report.bindShowTool();
			Report.bindShowSettings();
			Report.openPorletSetting();
			Report.openApp2ResType();
			Report.openRestoreFactoryLayout();
			DetailThirdCommon.initPageHeight();
			OnResize.addLayout(DetailThirdCommon.initPageHeight);
			OnResize.addLayout(Report.refreshGrister);
	        OnResize.bind();
	        
		},
		
		refreshGrister : function() {
			Report._gridster.generate_grid_and_stylesheet();
			Report._gridster.set_dom_grid_width(Report._gridster.cols);
			Report._gridster.set_dom_grid_height();
			Report._gridster.drag_api.destroy();
			Report._gridster.draggable();
		},
		executeContentResize: function ($content) {
			var resizeScript = $content.attr('onresize');
			if (resizeScript) {
				eval(resizeScript);
			}
			my_scroll.scrollResize($content.attr("id"));
		},

		remove: function (dom) {
			Report._gridster.remove_widget(dom);
		},

		append: function (dom, width, height) {
			Report._gridster.add_widget(dom, width, height);
		},

		toJson: function () {
			return Report._gridster.serialize();
		},

		getContent: function () {
			Report.websocket = io.connect(httpurl + ":19092/detailThirdContent");
			Report.websocket.on('load', function(data) {
				$('#temporary_div').html(data);
			});
			var jsonObject = {};
			jsonObject.ctx = ctx;
			jsonObject.id = resInstId;
			Report.websocket.emit('load', jsonObject);
		},
		bindNavigation: function () {
			$(".navigationClass").click(function () {
				var itemId = $(this).attr("itemId");
				var itemUrl = $(this).attr("itemUrl");
				var openType = $(this).attr("openType");
				var itemWidth = $(this).attr("itemWidth");
				var itemHeight = $(this).attr("itemHeight");
				if (openType == "pageLoad") {
					PageCtrl.winOpen({
						name: itemId + "Window",
						height: itemHeight,
						width: itemWidth,
						url: ctx + "/res/detail/third/window/pageLoadNavigation?resInstId="+resInstId+"&navigationId="+itemId
					});
				} else if (openType == "windowOpen") {
					PageCtrl.winOpen({
						name: itemId + "Window",
						height: itemHeight,
						width: itemWidth,
						url: ctx + itemUrl
					});
				}
			});
		},
		bindShowTool: function () {
			$("#show_toolslayer").click(function () {
				$(this).addClass("tools_layer_on");
				$("#toolslayer").toggle(100);
			});
		},
		bindShowSettings: function () {
			$("#show_settings").click(function () {
				$(this).addClass("tools_layer_on");
				$("#settingslayer").toggle(100);
			});
		},
		bindToolList: function () {
			$("#toolslayer ul li").each(function () {
				$(this).click(function () {
					Report.requestOpenToolWindow($(this));
				});
			});
		},
		requestOpenToolWindow: function (object) {
			var itemId = object.attr("itemId");
			var itemUrl = object.attr("itemUrl");
			var itemWidth = object.attr("itemWidth");
			var itemHeight = object.attr("itemHeight");
			PageCtrl.winModalOpen({
				width: itemWidth,
				height: itemHeight,
				url: ctx + itemUrl,
				name: itemId+"res_detail_tool"
			});
		},
		//显示模块
		openPorletSetting: function () {
			$("#showModelButton").click(function () {
				PageCtrl.load({
					url: ctx + "/res/detail/third/button/openPorletVisibility",
					param: "resInstId=" + resInstId,
					dom: "porlet_setting_div",
					callback: function (data) {
						$("#settingModal").modal("show");
					}
				});
			});
		},
		//应用布局到类型
		openApp2ResType: function() {
			$("#showApp2ResTypeButton").click(function () {
				PageCtrl.load({
					url: ctx + "/res/detail/third/button/openPorletTypeSetting",
					param: "resInstId=" + resInstId,
					dom: "porlet_setting_type",
					callback: function (data) {
						$("#settingModalType").modal("show");
					}
				});
			});
		},
		//恢复出厂设置
		openRestoreFactoryLayout : function() {
			$("#showRestoreFactoryLayoutlButton").click(function () {
				RiilAlert.confirm(resumeFactoryLayoutInfo, function(){
					Report.doRevert();
				});
			});
		},
		doRevert : function(){
			PageCtrl.ajax({
				url : ctx+"/res/detail/third/restoreFactoryLayout?resInstId="+resInstId,
				dataType:"json",
			    success : function(){
			    	location.reload();
			    }
			});
		}
	};
	Report.init();

})();