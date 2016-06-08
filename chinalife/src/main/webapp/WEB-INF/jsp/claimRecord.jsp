<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<jsp:include page="../menu.jsp">
	<jsp:param name="activeMenu" value="claim" />
</jsp:include>
<%@page import="com.chinal.emp.security.AuthUser"  %>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
    AuthUser userDetails = (AuthUser) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
%>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
	<!-- Content Header (Page header) -->
	<!--   <section class="content-header">
          <h1>订单管理
          	<small>管理您的订单</small> 
          </h1>
        </section>-->

	<!-- Main content -->
	<section class="content">

		<!-- Your Page Content Here -->
		<div class="box box-default">
			<div class="box-body">
				<!-- form start -->
				<form id="schFrm" class="form-inline" onsubmit="return false;">
					客户身份证号:<input name="idcardnum" type="text" class="form-control">
					<!-- 投诉原因:<input name="reason" type="text" class="form-control">    -->
					涉及保单:<input name="insuranceid" type="text" class="form-control">
					客户经理:<input name="firstaccount" type="text" class="form-control">
					<!--  处理内容:<input name="firstcontent" type="text" class="form-control">      
                      赔偿金额:<input name="firstclaim" type="text" class="form-control">      
                      处理时间:<input name="firsttime" type="text" class="form-control">      
                      区域经理:<input name="secondaccount" type="text" class="form-control">      
                      处理内容:<input name="secondcontent" type="text" class="form-control">      
                      处理时间:<input name="secondtime" type="text" class="form-control">      
                      赔偿金额:<input name="secondclaim" type="text" class="form-control">      
                      部门经理:<input name="thirdaccount" type="text" class="form-control">      
                      处理内容:<input name="thirdcontent" type="text" class="form-control">      
                      赔偿金额:<input name="thirdclaim" type="text" class="form-control">      
                      处理时间:<input name="thirdtime" type="text" class="form-control">      
                      经理室:<input name="fourthaccount" type="text" class="form-control">      
                      处理内容:<input name="fourthcontent" type="text" class="form-control">      
                      赔偿金额:<input name="fourthclaim" type="text" class="form-control">      
                      赔偿时间:<input name="fourthtime" type="text" class="form-control">      
                      投诉时间:<input name="claimtime" type="text" class="form-control">   -->
					<button id="schBtn" type="submit" class="btn btn-primary">
						<i class="fa fa-search"></i> 查询
					</button>
					<button type="reset" class="btn btn-default">
						<i class="fa fa-remove"></i> 清空
					</button>
				</form>
			</div>
			<!-- /.box-body -->
		</div>

		<div class="box">
			<div class="box-header">
				<div class="btn-group">
					<a id="addBtn" class="btn btn-primary"> <i class="fa"></i>
						录入
					</a>
				</div>
			</div>
			<!-- /.box-header -->

			<div class="box-body">
				<table id="searchTable">
					<tr>
						<th w_index="cusname">客户</th>
						<th w_index="reason">投诉原因</th>
						<th w_index="claimtime">投诉时间</th>
						<th w_index="insuranceid">涉及保单</th>
						<!-- <th w_index="firstaccount">客户经理</th> -->
						<th w_index="firstcontent">处理内容</th>
						<th w_index="firstclaim">赔偿金额</th>
						<th w_index="firsttime">处理时间</th>
						<!-- <th w_index="secondaccount">区域经理</th>
																			<th w_index="secondcontent">处理内容</th>
																			<th w_index="secondtime">处理时间</th>
																			<th w_index="secondclaim">赔偿金额</th>
																			<th w_index="thirdaccount">部门经理</th>
																			<th w_index="thirdcontent">处理内容</th>
																			<th w_index="thirdclaim">赔偿金额</th>
																			<th w_index="thirdtime">处理时间</th>
																			<th w_index="fourthaccount">经理室</th>
																			<th w_index="fourthcontent">处理内容</th>
																			<th w_index="fourthclaim">赔偿金额</th>
																			<th w_index="fourthtime">赔偿时间</th> -->

						<th w_render="operate" width="10%;">操作</th>
					</tr>
				</table>
			</div>
			<!-- /.box-body -->
		</div>

		<div id="crudWin" style="margin-bottom: 0em;">
			<form id="crudFrm" class="form-horizontal">
				<div class="form-group"  style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">客户</label>
					<div class="col-sm-3">
						<input id="cusname"  name="cusname" type="text" class="form-control"
							required="true"> 
						<input id="idcardnum" name="idcardnum" type="hidden"
							class="form-control" >
					</div>
					<label class="col-sm-2 control-label">涉及保单</label>
					<div class="col-sm-3">
						<input name="insuranceid" type="text" class="form-control"
							required="true">
					</div>
				</div>
				<div class="form-group"  style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">投诉时间</label>
					<div class="col-sm-3">
						<input name="claimtime" type="text" class="form-control"
							required="true"  onfocus="WdatePicker({skin:'default'})">
					</div>
					<label class="col-sm-2 control-label">投诉原因</label>
					<div class="col-sm-3">
						<input name="reason" type="text" class="form-control"
							required="true">
					</div>
				</div>
				<div class="form-group"  style="margin-bottom: 0em;">
					<label class="col-sm-5 control-label">客户经理处理情况：</label>
				</div>
				<div class="form-group" style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">处理内容</label>
					<div class="col-sm-8">
						<input name="firstcontent" type="text" class="form-control"
							required="true">
					</div>
				</div>

				<div class="form-group" style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">处理时间</label>
					<div class="col-sm-3">
						<input name="firsttime" type="text" class="form-control"
							required="true"  onfocus="WdatePicker({skin:'default'})">
					</div>
					<label class="col-sm-2 control-label">赔偿金额</label>
					<div class="col-sm-3">
						<input name="firstclaim" type="text" class="form-control"
							required="true">
					</div>
				</div>
				
				<%if(userDetails.getLevel()>=2){ %>
				
				<div class="form-group"  style="margin-bottom: 0em;">
					<label class="col-sm-5 control-label">区域经理处理情况：</label>
				</div>
				<div class="form-group" style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">处理内容</label>
					<div class="col-sm-8">
						<input name="secondcontent" type="text" class="form-control"
							required="true">
					</div>
				</div>
				<div class="form-group" style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">处理时间</label>
					<div class="col-sm-3">
						<input name="secondtime" type="text" class="form-control"
							required="true"  onfocus="WdatePicker({skin:'default'})">
					</div>
					<label class="col-sm-2 control-label">赔偿金额</label>
					<div class="col-sm-3">
						<input name="secondclaim" type="text" class="form-control"
							required="true">
					</div>
				</div>
				<%} %>
				<%if(userDetails.getLevel()>=3){ %>
				<div class="form-group"  style="margin-bottom: 0em;">
					<label class="col-sm-5 control-label">部门经理处理情况：</label>
				</div>
				<div class="form-group" style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">处理内容</label>
					<div class="col-sm-8">
						<input name="thirdcontent" type="text" class="form-control"
							required="true">
					</div>
				</div>
				<div class="form-group" style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">处理时间</label>
					<div class="col-sm-3">
						<input name="thirdtime" type="text" class="form-control"
							required="true"  onfocus="WdatePicker({skin:'default'})">
					</div>
					<label class="col-sm-2 control-label">赔偿金额</label>
					<div class="col-sm-3">
						<input name="thirdclaim" type="text" class="form-control"
							required="true">
					</div>
				</div> 
				<%} %>
				<%if(userDetails.getLevel()>=4){ %>
				<div class="form-group"   style="margin-bottom: 0em;">
				  <label class="col-sm-5 control-label">经理室处理情况：&nbsp;&nbsp;&nbsp;&nbsp;</label>
				</div>
				<div class="form-group" style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">处理内容</label>
					<div class="col-sm-8">
						<input name="fourthcontent" type="text" class="form-control"
							required="true">
					</div>
				</div>
				<div class="form-group" style="margin-bottom: 0em;">
					<label class="col-sm-2 control-label">赔偿金额</label>
					<div class="col-sm-3">
						<input name="fourthclaim" type="text" class="form-control"
							required="true">
					</div>
					<label class="col-sm-2 control-label">处理时间</label>
					<div class="col-sm-3">
						<input name="fourthtime" type="text" class="form-control"
							required="true"  onfocus="WdatePicker({skin:'default'})">
					</div>
				</div>
				<%} %>
			</form>
		</div>


		<div class="box-body" id="cusWin">	 
					<table id="cusTable">
						<tr>          
						    <th w_check="true" w_index="idcardnum" width="3%;"></th> 
							<th w_index="name">客户姓名</th>
							<th w_index="sex"  w_render="sexRender">性别</th>
							<th w_index="idcardnum">身份证号</th>
							<th w_index="addr">地址</th>
						</tr>
					</table>
		</div>
		<script type="text/javascript">
			var that = this;

			var pk = 'id'; // java类中的主键字段
			var listUrl = ctx + 'listClaimRecord.do'; // 查询
			var addUrl = ctx + 'addClaimRecord.do'; // 添加
			var updateUrl = ctx + 'updateClaimRecord.do'; // 修改
			var delUrl = ctx + 'delClaimRecord.do'; // 删除
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
				url : listUrl,
				pageSizeSelect : true,
				rowHoverColor : true // 移动行变色
				,
				rowSelectedColor : false // 选择行不高亮
				,
				isProcessLockScreen : false // 加载数据不显示遮罩层
				,
				displayBlankRows : false//,
				//pageSize : 10
			});

			crudWin = dialog({
				title : '编辑',
				width : 600,
				content : document.getElementById('crudWin'),
				okValue : '保存',
				ok : function() {
					that.save();
					return false;
				},
				cancelValue : '取消',
				cancel : function() {
					this.close();
					return false;
				}
			});

			function search() {
				var schData = getFormData($schFrm);
				gridObj.search(schData);
			}

			function operate(row, rowIndex, colIndex, options) {
				return '<a href="#" onclick="'
						+ FunUtil.createFun(that, 'edit', row)
						+ ' return false;">修改</a>' + '&nbsp;&nbsp;'
						+ '<a href="#" onclick="'
						+ FunUtil.createFun(that, 'del', row)
						+ ' return false;">删除</a>';
			}

			// 保存
			this.save = function() {
				var self = this;
				var data = getFormData($crudFrm);
				var validateVal = validator.form();
				if (validateVal) {
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
					loadFormData($crudFrm, row);
					crudWin.showModal();
				}
			}

			// 删除
			this.del = function(row) {
				if (row) {
					var d = dialog({
						title : '提示',
						width : 200,
						content : '确定要删除该记录吗?',
						okValue : '确定',
						ok : function() {
							Action.post(delUrl, row, function(result) {
								Action.execResult(result, function(result) {
									gridObj.refreshPage();
								});
							});
						},
						cancelValue : '取消',
						cancel : function() {
						}
					});
					d.showModal();
				}
			}
			
			$("#cusname").click(function() {
				cusWin.showModal();
			});
			
			var cusGridObj = $.fn.bsgrid.init('cusTable', {
				url: ctx + 'listCustomerForEmp.do'
			    ,pageSizeSelect: true
			    ,rowHoverColor: true // 移动行变色
			    ,rowSelectedColor: false // 选择行不高亮
			    ,isProcessLockScreen:false // 加载数据不显示遮罩层
				,displayBlankRows: false
				,pagingLittleToolbar: true
			    //
			});

			var cusWin = dialog({
				title: '选择客户',
				width:800,
				content: document.getElementById('cusWin'),
				okValue: '保存',
				ok: function () {
					var name = cusGridObj.getCheckedValues('name');
					if(name.length!=1){
						alert("请选择一个客户.");
						return false;
					}
					$('#cusname').val(cusGridObj.getCheckedValues('name'));
					$('#idcardnum').val(cusGridObj.getCheckedValues('idcardnum'));
					this.close();
					return false;
				},
				cancelValue: '取消',
				cancel: function () {
					this.close();
					return false;
				},
				onshow: function () {
					$("#cusTable_pt_outTab").width($("#cusTable").width());
			    }
			});

			var sexRender = function(record, rowIndex, colIndex, options){
				if(record.sex==0){
					return "女";
				}else{
					return "男";
				}
				
			}
			
			validator = $crudFrm.validate();
		</script>

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
