<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<script type="text/javascript"	src="${res}js/report.js"	namespace="Report"></script>
<script type="text/javascript"	src="${res}js/report_setting.js"	namespace="ReportSetting"></script>
<jsp:include page="../menu.jsp">
	<jsp:param name="activeMenu" value="report" />
</jsp:include>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Main content -->
	<section class="content">

<div class="pop_body" style="height: 100%;">

		<div class="resourse_menu" id="aaa">
			
			
			<!-- <span class="point_yellow"></span> -->
			<!-- <span onclick="location.reload();"></span> -->
			<span style="margin-left:10px;">${instance.nameDisplay}</span>
			<!-- <span	class="f_right Details_top_setting" style="margin: 2px;"	data-toggle="modal" data-target="#myModal01"></span>  -->
			<span	class="f_right txt_gray" id="show_settings" style="margin-left: 10px;"><fmt:message key="home.portlet.help.resourceAnalyze.settings"/><span id="show_setting_button" class="f_right Details_top_droparrow" style="margin-top: 10px;"></span></span>
			<span	class="f_right txt_gray" id="show_toolslayer" style="margin-left: 10px;"><fmt:message key="resource.monitor.resource.tool"/><span id="show_tools" class="f_right Details_top_droparrow" style="margin-top: 10px;"></span></span>
			<span class=" f_right txt_gray"> <c:forEach
					items="${navigations}" var="navigation" varStatus="status">
					<a itemId="${navigation.id }" class="navigationClass"
						itemUrl="${navigation.url }" openType="${navigation.openType }"
						itemWidth="${navigation.width }"
						itemHeight="${navigation.height }"><fmt:message key="${navigation.name }"/></a>
					<c:if test="${!status.last}">&nbsp;|&nbsp; </c:if>
				</c:forEach>
			</span>
		</div>
		<div style="width: 100%; overflow-y: auto;overflow-x:hidden;" id="detail_container">
			<div class="gridster">
				<ul style="float: left;" id="porlet_ul">
					<c:forEach items="${porlets}" var="porlet">
						<c:if test="${porlet.visibility == true }">
							<c:if test="${porlet.x !=0 }">
								<li id="${porlet.id}" data-row="${porlet.x }"
									data-col="${porlet.y }" data-sizex="${porlet.width }"
									data-sizey="${porlet.height }">
									<div id="${porlet.id}_content"
										style="width: 100%; height: 100%;"></div>
								</li>
							</c:if>
							<c:if test="${porlet.x ==0 }">
								<script>newList.push(["<li  id='${porlet.id}'><div id='${porlet.id}_content'  style='width:100%;height:100%;'></div></li>", ${porlet.width}, ${porlet.height}]);</script>
							</c:if>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="tools_layer"
			style="width: auto; visibility: visible; position: absolute; z-index: 55612; top: 30px; right: 46px; display: none;"
			id="toolslayer" remove="false">
			<ul style="width: 140px;">
				<c:forEach items="${tools}" var="tool" varStatus="status">
					<li itemId=${tool.id } itemUrl="${tool.url }"
						openType="${tool.openType }" itemWidth="${tool.width }"
						itemHeight="${tool.height }"><a><span
							class="${tool.icon }"></span><span><fmt:message key="${tool.name }"/></span></a></li>
				</c:forEach>
			</ul>
		</div>
		
		<div class="tools_layer"
			style="width: auto; visibility: visible; position: absolute; z-index: 55612; top: 30px; right: 0px; display: none;"
			id="settingslayer" remove="false">
			<ul style="width: 110px;">
					<li ><a><span 	id="showModelButton"><fmt:message key="thirdRes.display.module"/></span></a></li>
					<li ><a><span 	id="showApp2ResTypeButton" ><fmt:message key="thirdRes.apply.layout.to.types"/></span></a></li>
					<li ><a><span 	id="showRestoreFactoryLayoutlButton" ><fmt:message key="thirdRes.resume.factory.layout"/></span></a></li>
			</ul>
		</div>

		<div id="temporary_div" style="display: none;"></div>
		<div id="porlet_setting_div"></div>
		<div id="porlet_setting_type"></div>
	</div>

	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<!-- Main Footer -->
<footer class="main-footer">
	<!-- Default to the left -->
	<strong>Copyright &copy; 2016</strong>
</footer>

</div>
<!-- ./wrapper -->

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
