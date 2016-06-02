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
               <div class="box-body">
                 <!-- form start -->
                 <form id="schFrm" class="form-inline" onsubmit="return false;">
										                       :<input name="id" type="text" class="form-control">      
					 										                       :<input name="title" type="text" class="form-control">      
					 										                       是否制式拜访:<input name="zhishibaifang" type="text" class="form-control">      
					 										                       彩页:<input name="caiye" type="text" class="form-control">      
					 										                       开始时间:<input name="start" type="text" class="form-control">      
					 										                       结束时间:<input name="end" type="text" class="form-control">      
					 										                       :<input name="huashu" type="text" class="form-control">      
					 										                       技术资料:<input name="jishuziliao" type="text" class="form-control">      
					 										                       其他:<input name="others" type="text" class="form-control">      
					 										                       维护人员:<input name="empId" type="text" class="form-control">      
					 					                   	<button id="schBtn" type="submit" class="btn btn-primary"><i class="fa fa-search"></i> 查询</button>
					<button type="reset" class="btn btn-default"><i class="fa fa-remove"></i> 清空</button>
				</form>
               </div><!-- /.box-body -->
           </div>
           
          <div class="box">
				<div class="box-header">
					 <div class="btn-group">
			         	<a id="addBtn" class="btn btn-primary">
			            	<i class="fa fa-plus"></i> 录入 
			         	</a>
			          </div>
				</div><!-- /.box-header -->
			
				<div class="box-body">	 
					<table id="searchTable">
						<tr>           
								<th w_index="id"></th>
									<th w_index="title">平台名称</th>
									<th w_index="zhishibaifang">是否制式拜访</th>
									<th w_index="caiye">彩页</th>
									<th w_index="start">开始时间</th>
									<th w_index="end">结束时间</th>
									<th w_index="huashu"></th>
									<th w_index="jishuziliao">技术资料</th>
									<th w_index="others">其他</th>
									<th w_index="empId">维护人员</th>
							<th w_render="operate" width="10%;">操作</th>
						</tr>
					</table>
				</div><!-- /.box-body -->
			</div>
		    
		    <div id="crudWin">
			    	<form id="crudFrm" class="form-horizontal">
											   						<div class="form-group">
	                      <label class="col-sm-3 control-label"></label>
	                      <div class="col-sm-7">
	                        <input name="id" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">平台名称</label>
	                      <div class="col-sm-7">
	                        <input name="title" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">是否制式拜访</label>
	                      <div class="col-sm-7">
	                        <input name="zhishibaifang" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">彩页</label>
	                      <div class="col-sm-7">
	                        <input name="caiye" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">开始时间</label>
	                      <div class="col-sm-7">
	                        <input name="start" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">结束时间</label>
	                      <div class="col-sm-7">
	                        <input name="end" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">话术</label>
	                      <div class="col-sm-7">
	                        <input name="huashu" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">技术资料</label>
	                      <div class="col-sm-7">
	                        <input name="jishuziliao" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">其他</label>
	                      <div class="col-sm-7">
	                        <input name="others" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">维护人员</label>
	                      <div class="col-sm-7">
	                        <input name="empId" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										</form>
			    </div>
		    
<script type="text/javascript">     
var that = this;

var pk = 'id'; // java类中的主键字段
var listUrl = ctx + 'listBizplatform.do'; // 查询
var addUrl = ctx + 'addBizplatform.do'; // 添加
var updateUrl = ctx + 'updateBizplatform.do'; // 修改
var delUrl = ctx + 'delBizplatform.do'; // 删除
var submitUrl = ''; // 提交URL

var gridObj; // 表格
var crudWin; // 窗口
var $schFrm = $('#schFrm'); // 查询表单
var $crudFrm = $('#crudFrm'); // 编辑表单

var $schBtn = $('#schBtn'); // 查询按钮
var $addBtn = $('#addBtn'); // 添加按钮

var validator; // 验证器

function reset() {
	$crudFrm.get(0).reset();
	validator.resetForm();
}


// 初始化事件
$addBtn.click(function() {
	submitUrl = addUrl;
	reset();
	crudWin.title('添加');
	crudWin.showModal();	
});

/* $schBtn.click(function() {
	search();
});

var $schBtn = $('#schBtn'); // 查询按钮
var $schFrm = $('#schFrm'); // 查询表单 */
$schBtn.click(function() {
	  //var schData = getFormData($schFrm);
	  top.window.location.href = "${ctx}/openBizPlatformDetail.do?"+$schFrm.serialize();
	 /*  $.post("${ctx}/openBizPlatformDetail.do",schData,
			  function(data,status){
			   alert("Data: " + data + "\nStatus: " + status);
	  }); */
	
});


gridObj = $.fn.bsgrid.init('searchTable', {
	url: listUrl
    ,pageSizeSelect: true
    ,rowHoverColor: true // 移动行变色
    ,rowSelectedColor: false // 选择行不高亮
    ,isProcessLockScreen:false // 加载数据不显示遮罩层
	,displayBlankRows: false
    ,pageSize: 10
});

crudWin = dialog({
	title: '编辑',
	width:400,
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
	}
});

function search(){
    var schData = getFormData($schFrm);
    gridObj.search(schData);
}

function operate(row, rowIndex, colIndex, options) {
	return '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'edit', row)
		+ ' return false;">修改</a>'
		+ '&nbsp;&nbsp;'
		+ '<a href="#" onclick="'
		+ FunUtil.createFun(that, 'del', row)
		+ ' return false;">删除</a>';
}

// 保存
this.save = function() {
	var self = this;
	var data = getFormData($crudFrm);
	var validateVal = validator.form();
	if(validateVal) {
		Action.post(submitUrl, data, function(result) {
			Action.execResult(result, function(result) {
				gridObj.refreshPage();
				crudWin.close();
			});
		});
	}
}
 // 编辑
this.edit = function(row) {
	if (row) {
		submitUrl = updateUrl + '?' + pk + '=' + row[pk];
		reset();
		crudWin.title('修改');
		loadFormData($crudFrm,row);		
		crudWin.showModal();
	}
}

// 删除
this.del = function(row) {
	if (row) {
		var d = dialog({
			title: '提示',
			width: 200,
			content: '确定要删除该记录吗?',
			okValue: '确定',
			ok: function () {
				Action.post(delUrl, row, function(result) {
					Action.execResult(result, function(result) {
						gridObj.refreshPage();
					});
				});
			},
			cancelValue: '取消',
			cancel: function () {}
		});
		d.showModal();
	}
}


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
               url:ctx+"importPlatform.do",
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

validator = $crudFrm.validate();
</script>
    		
    		
         </section><!-- /.content -->
      </div><!-- /.content-wrapper -->

      <!-- Main Footer -->
      <footer class="main-footer">
        <!-- Default to the left -->
        <strong>Copyright &copy; 2016</strong>
      </footer>

    </div><!-- ./wrapper -->
              <div id="importWin">
                    <form id="importFrm"  method="post"   enctype="multipart/form-data"  class="form-horizontal" action="${ctx}importPlatform.do">                   
                       <div class="form-group">
                          <label class="col-sm-4 control-label">选择文件</label>
                          <div class="col-sm-6">
                            <input class="btn btn-default" id="filename" type="file" name="filename"  accept="xls"/>
                            <input id="filetype" type="hidden" name="filetype" />
                            <input id="platid" type="hidden" name="platid" />
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
</html>
