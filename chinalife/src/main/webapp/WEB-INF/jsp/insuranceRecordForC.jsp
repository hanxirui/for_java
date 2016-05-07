<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<jsp:include page="../menu.jsp" >
    <jsp:param name="activeMenu" value="cse"/>
</jsp:include>  

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
      <!--   <section class="content-header">
          <h1>订单管理
          	<small>管理您的订单</small> 
          </h1>
        </section>-->
        <ul class="nav nav-pills">
		  <li role="presentation"><a href="${ctx}openCustomerForC.do?id=${customer.id}">详情</a></li>
		  <li role="presentation"  class="active"><a href="${ctx}openInsuranceForC.do?id=${customer.id}">保单记录</a></li>
		  <li role="presentation"><a href="${ctx}openServiceRecordForC.do?id=${customer.id}">服务记录</a></li>
		  <li role="presentation"><a href="${ctx}openSitRecordForC.do?id=${customer.id}">拜访记录</a></li>
		</ul>
        <!-- Main content -->
        <section class="content">

          <!-- Your Page Content Here -->
          <div class="box box-default">
               <div class="box-body">
                 <!-- form start -->
                 <form id="schFrm" class="form-inline" onsubmit="return false;">
					   <!-- 机构:<input name="orgCode" type="text" class="form-control">   -->    
                       保险单号:<input name="insuranceNum" type="text" class="form-control">      
                       <!-- 投保单号:<input name="touInsuranceNum" type="text" class="form-control">  -->     
                       险种代码:<input name="typeCode" type="text" class="form-control">      
                       渠道:<input name="qudao" type="text" class="form-control">      
                      <!--  保费:<input name="insuranceFei" type="text" class="form-control">      
                       满期金额:<input name="totalInsuranceFei" type="text" class="form-control">      
                       满期日期:<input name="manqiDate" type="text" class="form-control">      
                       缴费方式:<input name="feiType" type="text" class="form-control">      
                       缴费期间:<input name="feiQijian" type="text" class="form-control">      
                       保险期间:<input name="insuranceQijian" type="text" class="form-control">      
                       保单状态:<input name="state" type="text" class="form-control">    -->   
                       投保人:<input name="customerIdcardnum" type="text" class="form-control">      
                      <!--  地址:<input name="customerAddr" type="text" class="form-control">      
                       电话:<input name="customerPhone" type="text" class="form-control">      
                       手机:<input name="customerMobile" type="text" class="form-control">      
                       业务员姓名:<input name="account" type="text" class="form-control">      
                       工号:<input name="accountCode" type="text" class="form-control">      
                       银行:<input name="bankName" type="text" class="form-control">      
                       银行账号:<input name="bankCardNum" type="text" class="form-control">      
                       被保险人:<input name="beibaoxianren" type="text" class="form-control">      
                       受益人:<input name="shouyiren" type="text" class="form-control">   -->    
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
																							<th w_index="orgCode">机构</th>
																			<th w_index="insuranceNum">保险单号</th>
																			<th w_index="touInsuranceNum">投保单号</th>
																			<th w_index="typeCode">险种代码</th>
																			<th w_index="qudao">渠道</th>
																			<th w_index="insuranceFei">保费</th>
																			<th w_index="totalInsuranceFei">满期金额</th>
																			<th w_index="manqiDate">满期日期</th>
																			<th w_index="feiType">缴费方式</th>
																			<th w_index="feiQijian">缴费期间</th>
																			<th w_index="insuranceQijian">保险期间</th>
																			<th w_index="state">保单状态</th>
																			<th w_index="customerIdcardnum">投保人</th>
																			<th w_index="customerAddr">地址</th>
																			<th w_index="customerPhone">电话</th>
																			<th w_index="customerMobile">手机</th>
																			<th w_index="account">业务员姓名</th>
																			<th w_index="accountCode">工号</th>
																			<th w_index="bankName">银行</th>
																			<th w_index="bankCardNum">银行账号</th>
																			<th w_index="beibaoxianren">被保险人</th>
																			<th w_index="shouyiren">受益人</th>
													<th w_render="operate" width="10%;">操作</th>
						</tr>
					</table>
				</div><!-- /.box-body -->
			</div>
		    
		    <div id="crudWin">
			    	<form id="crudFrm" class="form-horizontal">
											   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">机构</label>
	                      <div class="col-sm-7">
	                        <input name="orgCode" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">保险单号</label>
	                      <div class="col-sm-7">
	                        <input name="insuranceNum" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">投保单号</label>
	                      <div class="col-sm-7">
	                        <input name="touInsuranceNum" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">险种代码</label>
	                      <div class="col-sm-7">
	                        <input name="typeCode" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">渠道</label>
	                      <div class="col-sm-7">
	                        <input name="qudao" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">保费</label>
	                      <div class="col-sm-7">
	                        <input name="insuranceFei" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">满期金额</label>
	                      <div class="col-sm-7">
	                        <input name="totalInsuranceFei" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">满期日期</label>
	                      <div class="col-sm-7">
	                        <input name="manqiDate" type="text" class="form-control" required="true" onfocus="WdatePicker({skin:'default'})">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">缴费方式</label>
	                      <div class="col-sm-7">
	                        <input name="feiType" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">缴费期间</label>
	                      <div class="col-sm-7">
	                        <input name="feiQijian" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">保险期间</label>
	                      <div class="col-sm-7">
	                        <input name="insuranceQijian" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">保单状态</label>
	                      <div class="col-sm-7">
	                        <input name="state" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">投保人</label>
	                      <div class="col-sm-7">
	                        <input name="customerIdcardnum" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">地址</label>
	                      <div class="col-sm-7">
	                        <input name="customerAddr" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">电话</label>
	                      <div class="col-sm-7">
	                        <input name="customerPhone" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">手机</label>
	                      <div class="col-sm-7">
	                        <input name="customerMobile" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">业务员姓名</label>
	                      <div class="col-sm-7">
	                        <input name="account" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">工号</label>
	                      <div class="col-sm-7">
	                        <input name="accountCode" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">银行</label>
	                      <div class="col-sm-7">
	                        <input name="bankName" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">银行账号</label>
	                      <div class="col-sm-7">
	                        <input name="bankCardNum" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">被保险人</label>
	                      <div class="col-sm-7">
	                        <input name="beibaoxianren" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										   						<div class="form-group">
	                      <label class="col-sm-3 control-label">受益人</label>
	                      <div class="col-sm-7">
	                        <input name="shouyiren" type="text" class="form-control" required="true">
	                      </div>
	                    </div>
					   										</form>
			    </div>
		    
<script type="text/javascript">     
var that = this;

var pk = 'id'; // java类中的主键字段
var listUrl = ctx + 'listInsuranceRecord.do?idcardnum='+${customer.idcardnum}; // 查询
var addUrl = ctx + 'addInsuranceRecord.do'; // 添加
var updateUrl = ctx + 'updateInsuranceRecord.do'; // 修改
var delUrl = ctx + 'delInsuranceRecord.do'; // 删除
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
