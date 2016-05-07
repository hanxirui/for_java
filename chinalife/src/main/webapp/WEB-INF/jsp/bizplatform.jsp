<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="bizplat"/>
</jsp:include> 
<link rel="stylesheet" href="${res}css/clndr.css">
<script src="${res}js/underscore-min.js"></script>
<script src="${res}js/moment.min.js"></script>
<script src="${res}js/clndr.js"></script>
<script src="${res}js/bizcalendar.js"></script>

<!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
      
        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default">
               <div class="box-body" align="center">

				 <h1>  业务平台搭建系统  </h1>

    			</div>
    		</div>
          <div class="box box-default">
               <div class="box-body" align="center">

			  <h3>  <a href="${ctx}openBizPlatformDetail.do">  历史平台查询 </a> </h3>


    			</div>
    			<div class="box-body" align="center">

			  <h3>  <a href="#">本季平台查询 </a> </h3>

    			</div>
    			<div class="box-body" align="center">

				 <h3>  <a href="${ctx}openBizCalendar.do">本季平台行事历及KPI</a>  </h3>

    			</div>
    			<div class="box-body" align="center">

				 <h3>  &nbsp;  </h3>

    			</div>
    			<div class="box-body" align="center">

				 <h3>  &nbsp; </h3>

    			</div>
    		</div>
    		
    		
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
  <script type="text/javascript">
	function myFormatter(date) {

		var y = date.getFullYear();

		var m = date.getMonth() + 1;

		return y + '-' + (m < 10 ? ('0' + m) : m);

	}

	function myParser(s) {

		if (!s) {

			return new Date();

		}

		var ss = s.split('-');

		var y = parseInt(ss[0], 10);

		var m = parseInt(ss[1], 10);

		if (!isNaN(y) && !isNaN(m)) {

			return new Date(y, m - 1);

		}

	}

	$(document).ready(function() {

		if (!$("#myDate").datebox('getValue')) {

			$("#myDate").datebox('setValue', myFormatter(new Date()));

		}

	});
</script>
</html>
