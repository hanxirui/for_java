(function() {
	window.Report = {
		_gridster : null,
		websocket : null,
		init : function() {
			Report.params = {};
			Report.initGridster();

			$.each(portletIdList, function(i, portletId) {
				$('#' + portletId + '_mask').css("borderRadius", "10px");
			});

		},

		initGridster : function() {
			Report._gridster = $("#porlet_ul").gridster(
					{

						widget_base_dimensions : [ 10, 10 ],
						widget_margins : [ 5, 5 ],
						helper : 'clone',
						resize : {
							enabled : true,
							min_size : [ 15, 10 ],
							axes : [ 'x', 'y', 'both' ],
							stop : function() {
								if (Report.onModify) {
									Report.onModify.call(this, arguments);
								}
								ButtonComm.resizePorletInfoHeight(arguments[2] .attr("id"));
								Report.executeContentResize(arguments[2].find('[id$="component"]'));
								ButtonComm.refresh(templateId, arguments[2].attr("id"));
							}
						},
						draggable : {
							handle : '.Det_title_blue',
							ignore_dragging : [ 'INPUT', 'TEXTAREA', 'SELECT',
									'BUTTON' ],
							stop : function() {
								if (Report.onModify) {
									Report.onModify.call(this, arguments);
								}
							}
						},
						serialize_params : function($w, wgd) {
							return {
								id : $w.attr('id'),
								'data-col' : wgd.col,
								'data-row' : wgd.row,
								'data-sizex' : wgd.size_x,
								'data-sizey' : wgd.size_y
							};
						}
					}).data('gridster');

			$.each(newPortletList, function(i, widget) {
				Report.append.apply(null, widget);
			});

			Report.getContent();
			OnResize.addLayout(Report.refreshGrister);
			OnResize.bind();
		},

		onModify : function() {
			Action.post(ctx + "updateCoordinate.do?templateId=" + templateId,JSON.stringify(Report.toJson()),function(data) {});
		},

		refreshGrister : function() {
			Report._gridster.generate_grid_and_stylesheet();
			Report._gridster.set_dom_grid_width(Report._gridster.cols);
			Report._gridster.set_dom_grid_height();
			Report._gridster.drag_api.destroy();
			Report._gridster.draggable();
		},
		executeContentResize : function($content) {
			var resizeScript = $content.attr('onresize');
			if (resizeScript) {
				eval(resizeScript);
			}
			ReportScroll.scrollResize($content.attr("id"));
		},

		remove : function(dom) {
			Report._gridster.remove_widget(dom);
		},

		append : function(dom, width, height) {
			Report._gridster.add_widget(dom, width, height);
		},

		toJson : function() {
			return Report._gridster.serialize();
		},

		getContent : function() {
			Report.websocket = io.connect(serverurl + ":19092/cl");
			Report.websocket.on('connect', function() {
				console.log('Client has connected to the server!');
			});
			Report.websocket.on('disconnect', function() {
				console.log('The client has disconnected!');
			});
			Report.websocket.on('ResponseData', function(data) {
				if (data) {
					var portletId = data.portletId;
					var content = data.content;
					$("#" + portletId + "_content").html(content)
				}
			});

			Report.websocket.emit('GetContent', {
				"templateId" : templateId,
				"ctx" : ctx
			});
		}

	};

	Report.init();
})();