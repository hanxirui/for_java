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
       

               <div id="crudWin">
			    	<form id="crudFrm" class="form-horizontal">
					   	<div class="form-group">
	                      <label class="col-sm-3 control-label">邀约客户数</label>
	                      <div class="col-sm-3">
	                        <input name="yaoyueNum" type="text" class="form-control">
	                      </div>
	                       <label class="col-sm-3 control-label">当日回收保费</label>
	                      <div class="col-sm-3">
	                        <input name="receiveBaofei" type="text" class="form-control">
	                      </div>
	                    </div>
					   										   		
   						<div class="form-group">
   						  <label class="col-sm-3 control-label">到会客户数</label>
	                      <div class="col-sm-3">
	                        <input name="daohuiNum" type="text" class="form-control">
	                      </div>
	                      <label class="col-sm-3 control-label">当日签单保费</label>
	                      <div class="col-sm-3">
	                        <input name="qiandanBaofei" type="text" class="form-control">
	                      </div>
	                    </div>
					   					
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">到会率</label>
	                      <div class="col-sm-3">
	                        <input name="daohuilv" type="text" class="form-control">
	                      </div>
	                      <label class="col-sm-3 control-label">回收率</label>
	                      <div class="col-sm-3">
	                        <input name="huishoulv" type="text" class="form-control">
	                      </div>
	                      
	                    </div>
					   										   						
					   										   						<div class="form-group">
					   	 <label class="col-sm-3 control-label">签单件数</label>
	                      <div class="col-sm-3">
	                        <input name="qiandanNum" type="text" class="form-control">
	                      </div>
	                      <label class="col-sm-3 control-label">签单率</label>
	                      <div class="col-sm-3">
	                        <input name="qiandanlv" type="text" class="form-control">
	                      </div>
	                      
	                    </div>
					   										   						<div class="form-group">
	                      
	                      <label class="col-sm-3 control-label">当日回收件数</label>
	                      <div class="col-sm-3">
	                        <input name="receiveNum" type="text" class="form-control">
	                      </div>
	                     <!--   <label class="col-sm-3 control-label">客户经理</label> -->
	                      <div class="col-sm-3">
	                        <input id="kehujingli" name="kehujingli" type="hidden" class="form-control">
	                        <input id="riqi" name="riqi" type="hidden" class="form-control">
	                      </div>
	                    </div>
					  </form>
			    </div>
			                   
<script type="text/javascript">  

var that = this;

var pk = 'id'; // java类中的主键字段
var listUrl = ctx + 'listBizRecord.do'; // 查询
var addUrl = ctx + 'addBizRecord.do'; // 添加
var updateUrl = ctx + 'updateBizRecord.do'; // 修改
var delUrl = ctx + 'delBizRecord.do'; // 删除
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
var bizAdd = function() {
	submitUrl = addUrl;
	reset();
	crudWin.title('添加');
	crudWin.showModal();	
}

// 初始化事件
$addBtn.click(bizAdd);

$schBtn.click(function() {
	search();
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
	}
});


function search(){
    var schData = getFormData($schFrm);
    gridObj.search(schData);
}



// 保存
this.save = function() {
	var self = this;
	var data = getFormData($crudFrm);
	var validateVal = validator.form();
	if(validateVal) {
		Action.post(submitUrl, data, function(result) {
			/* Action.execResult(result, function(result) {
				gridObj.refreshPage();
				crudWin.close();
			}); */
			crudWin.close();
		});
	}
}
 // 编辑
var bizEdit = function(row) {
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
