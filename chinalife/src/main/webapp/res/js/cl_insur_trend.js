var ClInsurTrend = {
		
	init : function(portletId) {

		if (nodata != 'true') {
			ClInsurTrend.line = window.echarts.init(document.getElementById("cl_insur_trend_div"), "macarons");
			ClInsurTrend.renderChart(insurTrendReport, freq);
			ClInsurTrend.initBtn();
		}
		ClInsurTrend.bindEvent();
	},
	bindEvent : function() {
		
	},
	renderChart : function(insurTrendReport, freq) {
		//$("#" + portletId).height("100%");
		//$("#" + portletId + "_component").children().addClass("noborder");
		
		if (!insurTrendReport || insurTrendReport.length <= 0) {
			$("#cl_insur_trend_div")
					.append(
							'<div style="width:100px;height:32px;margin:auto;left:0px;right:0px;top:-30px;bottom:0px;position:absolute;">'
									+ '<ul><li class="ico_none_data"></li><li class="txt_none_data">暂无数据</li></ul>'
									+ '</div>');
			return;
		}

		var xAxisData = [];
		var  values= [];
		$.each( insurTrendReport, function(index, item) {
									xAxisData.push(item.time);
									values.push(item.value);
								  });

		var seriesDatas = [];
		seriesDatas.push({
			name : '数量',
			type : 'line',
			stack: '总量',
            areaStyle: {normal: {color : '#63fd8c',opacity:0.4}},
            lineStyle: {normal: {color : '#0cb038',opacity:0.5}},
            itemStyle: {normal: {color : '#0cb038',opacity:0.6}},
			smooth : true,
			data : values,
			markPoint : {
				clickable : false,
				data : [ {
					type : 'max',
					name : '最大值',
					symbolSize : 10
				}, {
					type : 'min',
					name : '最小值',
					symbolSize : 10
				} ]
			},
			markLine : {
				clickable : false,
				precision : 2,
				data : [ {
					type : 'average',
					name : '平均值'
				} ]
			}
		});

		var option = {
			tooltip : {
				trigger : 'axis',
//				position : function(p) {
//					return [ p[0], p[1] - 40 ];
//				},
				
			},
//			legend : {
//				data : _.map(seriesDatas, "name"),
//				textStyle : {
//					color : 'auto'
//				},
//				itemGap : 20,
//				x : 'left'
//			},
			grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '15%',
		        containLabel: true
		    },
			//calculable : false,
			xAxis : [ {
				type : 'category',
				boundaryGap : false,
				data : xAxisData,
				splitLine : {
					show : false
				},
				axisLine : {
					onZero : false
				},
				axisLabel : {
					rotate : 45,
					textStyle : {
						color : '#afd6fe'
					    //color : '#457ab1'
					}
				}
			} ],
			yAxis : [ {
				type : 'value',
				splitLine : {
					show : false
				},
				axisLabel : {
					textStyle : {
						color : '#afd6fe'
					    //color : '#457ab1'
					}
				}
			} ],
			series : seriesDatas
		};
		ClInsurTrend.line.resize();
		ClInsurTrend.line.clear();
		ClInsurTrend.line.setOption(option);
		//ClInsurTrend.line.refresh();
//		ClInsurTrend.line.on(
//				echarts.config.EVENT.CLICK,
//				function(param) { console.log(param); });
	},
	initBtn : function() {
		
	}
};
ClInsurTrend.init();