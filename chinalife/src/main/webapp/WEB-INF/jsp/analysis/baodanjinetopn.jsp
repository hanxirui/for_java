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
		  <li role="presentation"><a href="${ctx}openBaodanshuliangqushi.do">保单数量趋势分析</a></li>
		  <li role="presentation"  class="active"><a href="${ctx}openBaodanjinetopn.do">下单金额排名</a></li>
		  <li role="presentation" ><a href="${ctx}openBaodanshuliangtopn.do">下单数量排名</a></li>
		  <%-- <li>
                <a href="${ctx}openCustomerBasic.do"><i class="fa fa-reply"></i>返回</a>
           </li> --%>
		</ul>
        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default">
               <div class="box-body">
               
               </div><!-- /.box-body -->
           </div>
           
		    
<script type="text/javascript">     

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
