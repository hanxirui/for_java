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
               <div class="box-body">

					    <div class="container">
					        <div class="cal1" id="canlender"></div>
					    </div>

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

               <div id="crudWin">
                    <div id="date"></div>
			    	<form id="crudFrm" class="form-horizontal">
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">邀约客户数</label>
	                      <div class="col-sm-3">
	                        <input name="yaoyueNum" type="text" class="form-control" required="true">
	                      </div>
	                       <label class="col-sm-3 control-label">当日回收保费</label>
	                      <div class="col-sm-3">
	                        <input name="receiveBaofei" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   		
					   										   						<div class="form-group">
					   										   						<label class="col-sm-3 control-label">到会客户数</label>
	                      <div class="col-sm-3">
	                        <input name="daohuiNum" type="text" class="form-control" required="true">
	                      </div>
	                      <label class="col-sm-3 control-label">当日签单保费</label>
	                      <div class="col-sm-3">
	                        <input name="qiandanBaofei" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   					
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">到会率</label>
	                      <div class="col-sm-3">
	                        <input name="daohuilv" type="text" class="form-control" required="true">
	                      </div>
	                      <label class="col-sm-3 control-label">回收率</label>
	                      <div class="col-sm-3">
	                        <input name="huishoulv" type="text" class="form-control" required="true">
	                      </div>
	                      
	                    </div>
					   										   						
					   										   						<div class="form-group">
					   	 <label class="col-sm-3 control-label">签单件数</label>
	                      <div class="col-sm-3">
	                        <input name="qiandanNum" type="text" class="form-control" required="true">
	                      </div>
	                      <label class="col-sm-3 control-label">签单率</label>
	                      <div class="col-sm-3">
	                        <input name="qiandanlv" type="text" class="form-control" required="true">
	                      </div>
	                      
	                    </div>
					   										   						<div class="form-group">
	                      
	                      <label class="col-sm-3 control-label">当日回收件数</label>
	                      <div class="col-sm-3">
	                        <input name="receiveNum" type="text" class="form-control" required="true">
	                      </div>
	                       <label class="col-sm-3 control-label">客户经理</label>
	                      <div class="col-sm-3">
	                        <input name="kehujingli" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					  </form>
			    </div>
			                   
<script type="text/javascript">     
var crudWin = dialog({
	title: '编辑',
	width:600,
	content: document.getElementById('crudWin'),
	okValue: '保存',
	ok: function () {
		that.save();
		return false;
	},
	cancelValue: '取消',
	cancel: function () {
		this.close();
		return false;
	},
	onshow:function(){
		
	}
});

validator = $crudFrm.validate();
</script>
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
