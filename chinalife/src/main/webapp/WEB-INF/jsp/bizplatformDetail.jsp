<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="bizplat"/>
</jsp:include> 
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

			  <h2>平台查询</h2>


    			</div>
    			

    			<div class="box-body" align="left">
				 <form id="schFrm" class="form-inline" onsubmit="return false;">
			        <div class="form-group">
                      <label>时间段: </label>
                        <input id="nian" name="nian" type="text" class="form-control" required="true">
                      <label >年</label>
                      </div>
                      <div class="form-group">
                        <input name="ji" type="text" class="form-control" required="true">
                      <label>季</label>
                      </div>
                      <div class="btn-group">
			         	<a id="schBtn" class="btn btn-primary">
                           <i class="fa"></i>  查询
                        </a>
			          </div>
			      </form>
			     
			   
			     
			    <!--  <br>流程：
			     
			     
			     <br>核心点： -->

    			</div>
    			<div class="box-body" align="left">

				   <form class="form-inline">
			        <div class="form-group">
                      <label>工具： </label>
                   </div>
                      <div class="btn-group">
			         	<a id="importBtn" class="btn btn-primary">
                           <i class="fa"></i>  导入
                        </a>
			          </div>
			      </form>

                   <c:forEach items="${bizfile}" var="file">
				     <a href="${ctx}${path}/${file}">${file}</a>   <br>
				   </c:forEach>
    			</div>
    			<div class="box-body" align="left">

				 
				     <br>

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
              <div id="importWin">
                    <form id="importFrm"  method="post"   enctype="multipart/form-data"  class="form-horizontal" action="${ctx}importCustomer.do">                   
                       <div class="form-group">
                          <label class="col-sm-3 control-label">选择文件</label>
                          <div class="col-sm-7">
                           <input class="btn btn-default" id="filename" type="file" name="filename"  accept="xls"/>
                          </div>
                        </div>
                    </form>
               </div>
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
  
  var $schBtn = $('#schBtn'); // 查询按钮
  var $schFrm = $('#schFrm'); // 查询表单
  $schBtn.click(function() {
	  //var schData = getFormData($schFrm);
	  top.window.location.href = "${ctx}/openBizPlatformDetail.do?"+$schFrm.serialize();
	 /*  $.post("${ctx}/openBizPlatformDetail.do",schData,
			  function(data,status){
			   alert("Data: " + data + "\nStatus: " + status);
	  }); */
	
  });

 
  var $importBtn= $('#importBtn'); // 导入按钮
  $importBtn.click(function() {   
  	    importWin.showModal();    
  });

  var importWin = dialog({
  	title: '导入',
  	width:400,
  	content: document.getElementById('importWin'),
  	okValue: '导入',
      ok: function () {
             $.ajaxFileUpload({
                 url:ctx+"importCustomer.do",
                 fileElementId:"filename",
                 dataType: 'json',
                 success: function (data, status){
                   if("success"==data.status){
                       gridObj.refreshPage();
                       importWin.close();
                   }else if("error"==data.status){
                       alert("上传失败!");
                      return false; 
                   }
                 }
                 });
          return false;
      },
  	cancelValue: '取消',
  	cancel: function () {
  		this.close();
  		return false;
  	}
  });
</script>
</html>
