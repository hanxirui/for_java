<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<script type="text/javascript">
	var serverurl = "<%=request.getScheme() + "://" + request.getServerName()%>";
</script>
<jsp:include page="../menu.jsp">
	<jsp:param name="activeMenu" value="report" />
</jsp:include>
<script type="text/javascript">
var notExistTemplate = ${template == null};
var newPortletList = [];
var portletIdList = [];
var templateId = "${template.id}";
</script>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <section class="content">
        <div class="box box-default" style="overflow-y: auto; height:550px; ">
				<div class="gridster">
					<ul id="porlet_ul">
						<c:forEach items="${template.portlets}" var="porlet">
							<c:if test="${porlet.visibility == true }">
								<c:if test="${porlet.x !=0 }">
									<li id="${porlet.id}" data-row="${porlet.x }" data-col="${porlet.y }" data-sizex="${porlet.width }" data-sizey="${porlet.height }">
										<%--  style="width: 100%; height: 100%; background: url(${res}img/bg.png) top left repeat;" --%>
										<div id="${porlet.id}_content" style="width: 100%; height: 100%;"></div>
									</li>
								</c:if>
								<c:if test="${porlet.x ==0 }">
									<script>newPortletList.push(["<li  id='${porlet.id}'><div id='${porlet.id}_content'  style='width:100%;height:100%;'></div></li>", ${porlet.width}, ${porlet.height}]);</script>
								</c:if>
							</c:if>
						</c:forEach>
					</ul>
				</div>
	    </div>
     </section>
</div>
<!-- /.content-wrapper -->

<!-- Main Footer -->
<!-- <footer class="main-footer"> -->
	<!-- Default to the left -->
	<!-- <strong>Copyright &copy; 2016</strong>
</footer> -->

</div>
<!-- ./wrapper -->

<!-- REQUIRED JS SCRIPTS -->
<script type="text/javascript" src="${res}js/report.js" namespace="Report"></script>
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
