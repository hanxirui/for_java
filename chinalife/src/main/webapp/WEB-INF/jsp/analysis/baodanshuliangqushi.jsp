<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../taglib.jsp" %>
<jsp:include page="../../menu.jsp" >
    <jsp:param name="activeMenu" value="cse"/>
</jsp:include>  
<%@page import="com.chinal.emp.security.AuthUser"  %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
    AuthUser userDetails = (AuthUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
%>
      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
       <ul class="nav nav-pills">
		  <li role="presentation"><a href="${ctx}openBaodanjinequshi.do">保单金额趋势分析</a></li>
		  <li role="presentation"  class="active"><a href="${ctx}openBaodanshuliangqushi.do">保单数量趋势分析</a></li>
		  <li role="presentation"><a href="${ctx}openBaodanjinetopn.do">下单金额排名</a></li>
		  <li role="presentation" ><a href="${ctx}openBaodanshuliangtopn.do">下单数量排名</a></li>
		  <%-- <li>
                <a href="${ctx}openCustomerBasic.do"><i class="fa fa-reply"></i>返回</a>
           </li> --%>
		</ul>
        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default" style="width:100%;height:100%;">
               <div class="box-body" style="width:100%;height:500px">
                  <div id="echart_div" style="width:100%;height:100%;"></div>
               </div><!-- /.box-body -->
           </div>
           
		    
<script type="text/javascript">     
var insurTrendReport = ${AnalysisResult};
console.log(insurTrendReport);
var line = window.echarts.init(document.getElementById("echart_div"), "macarons");

if (!insurTrendReport || insurTrendReport.length <= 0) {
	$("#echart_div")
			.append(
					'<div style="width:100px;height:32px;margin:auto;left:0px;right:0px;top:-30px;bottom:0px;position:absolute;">'
							+ '<ul><li class="ico_none_data"></li><li class="txt_none_data">暂无数据</li></ul>'
							+ '</div>');
}else{

	var xAxisData = [];
	var  values= [];
	$.each( insurTrendReport, function(index, item) {
								xAxisData.push(item.time);
								values.push(item.count);
							  });
	
	var seriesDatas = [];
	seriesDatas.push({
		name : '保单数量',
		type : 'line',
		stack: '总量',
	    areaStyle: {normal: {color : '#63fd8c',opacity:0.4}},
	    lineStyle: {normal: {color : '#0cb038',opacity:0.5}},
	    itemStyle: {normal: {color : '#0cb038',opacity:0.6}},
		smooth : true,
		data : values,
		/* markPoint : {
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
		}, */
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
	//		position : function(p) {
	//			return [ p[0], p[1] - 40 ];
	//		},
			
		},
	//	legend : {
	//		data : _.map(seriesDatas, "name"),
	//		textStyle : {
	//			color : 'auto'
	//		},
	//		itemGap : 20,
	//		x : 'left'
	//	},
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
			boundaryGap: [0, '100%'],
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
	line.resize();
	line.clear();
	line.setOption(option);
}
</script>

        </section><!-- /.content -->
      </div><!-- /.content-wrapper -->

      <!-- Main Footer -->
      <footer class="main-footer">
        <!-- Default to the left -->
        <strong>Copyright &copy; 2016</strong>
      </footer>

    </div><!-- ./wrapper -->

    <!-- REQUIRED JS SCRIPTS -->
    <!-- Bootstrap 3.3.5 -->
    <script src="${AdminLTE}bootstrap/js/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${AdminLTE}dist/js/app.min.js"></script>

    <!-- Optionally, you can add Slimscroll and FastClick plugins.
         Both of these plugins are recommended to enhance the
         user experience. Slimscroll is required when using the
         fixed layout. -->
  </body>
</html>
