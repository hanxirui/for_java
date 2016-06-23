<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<jsp:include page="../menu.jsp">
	<jsp:param name="activeMenu" value="bizplat" />
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
			<div class="box-header">
			   <!--  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; -->
			    <!-- &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; -->
				<div class="btn-group">
					<a id="tongji" class="btn btn-primary"> <i class="fa"></i> 统计
					</a>
				</div>
				<div class="btn-group">
					<a href="${ctx}openBizPlatformDetail.do" class="btn btn-primary"><i class="fa"></i>平台信息</a>
				</div>
				<!-- &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
				&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; -->
				<div class="btn-group">
					<a href="${ctx}openBizplatform.do"><i class="fa fa-reply"></i>返回</a>
				</div>
			</div>
			<div class="box-body">

				<div class="container">
					<div class="cal1" id="canlender"></div>
				</div>

			</div>
		</div>


		<div id="crudWin">
			<form id="crudFrm" class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-3 control-label">平台名称</label>
					<div class="col-sm-3">
						<input id="bizplatTitle" name="bizplatTitle" type="text"
							class="form-control  input-sm"> <input id="bizplatId"
							name="bizplatId" type="hidden" class="form-control  input-sm">
						<!--  同一个平台可能在多天举行 -->
					</div>

				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">邀约客户数</label>
					<div class="col-sm-3">
						<input id="yaoyueNum" name="yaoyueNum" type="text" class="form-control  input-sm">
					</div>
				
					<label class="col-sm-3 control-label">到会客户数</label>
					<div class="col-sm-3">
						<input id="daohuiNum" name="daohuiNum" type="text" class="form-control  input-sm">
					</div>
					
				</div>

				<div class="form-group">
				<label class="col-sm-3 control-label">签单保费</label>
					<div class="col-sm-3">
						<input id="qiandanBaofei" name="qiandanBaofei" type="text" class="form-control  input-sm">
					</div>
					<label class="col-sm-3 control-label">回收保费</label>
					<div class="col-sm-3">
						<input id="receiveBaofei" name="receiveBaofei" type="text" class="form-control  input-sm">
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">签单件数</label>
					<div class="col-sm-3">
						<input id="qiandanNum" name="qiandanNum" type="text" class="form-control  input-sm">
					</div>
					<label class="col-sm-3 control-label">回收件数</label>
					<div class="col-sm-3">
						<input id="receiveNum" name="receiveNum" type="text" class="form-control  input-sm">
					</div>

				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">到会率(%)</label>
					<div class="col-sm-3">
						<input id="daohuilv" name="daohuilv" type="text" class="form-control  input-sm" readonly>
					</div>
					<label class="col-sm-3 control-label">回收率(%)</label>
					<div class="col-sm-3">
						<input id="huishoulv" name="huishoulv" type="text" class="form-control  input-sm" readonly>
					</div>

				</div>
				<div class="form-group">
				<label class="col-sm-3 control-label">签单率(%)</label>
					<div class="col-sm-3">
						<input id="qiandanlv" name="qiandanlv" type="text" class="form-control  input-sm" readonly>
					</div>
					
					<!--   <label class="col-sm-3 control-label">客户经理</label> -->
					<div class="col-sm-3">
						<input id="kehujingli" name="kehujingli" type="hidden"
							class="form-control  input-sm"> <input id="riqi" name="riqi"
							type="hidden" class="form-control  input-sm">
					</div>
				</div>

			</form>
		</div>
		
		<div id="tongjiWin">
			<form id="tongjiFrm" class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-3 control-label">平台名称</label>
					<div class="col-sm-3">
						<input id="tongjibizplatTitle" name="tongjibizplatTitle" type="text"
							class="form-control  input-sm" readonly> <input id="tongjibizplatId"
							name="tongjibizplatId" type="hidden" class="form-control  input-sm" readonly>
						<!--  同一个平台可能在多天举行 -->
					</div>

				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">邀约客户数</label>
					<div class="col-sm-3">
						<input id="tongjiyaoyueNum" name="tongjiyaoyueNum" type="text" readonly class="form-control  input-sm" readonly>
					</div>
					<label class="col-sm-3 control-label">回收保费</label>
					<div class="col-sm-3">
						<input id="tongjireceiveBaofei" name="tongjireceiveBaofei" type="text" class="form-control  input-sm" readonly>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">到会客户数</label>
					<div class="col-sm-3">
						<input id="tongjidaohuiNum" name="tongjidaohuiNum" type="text" class="form-control  input-sm" readonly>
					</div>
					<label class="col-sm-3 control-label">签单保费</label>
					<div class="col-sm-3">
						<input id="tongjiqiandanBaofei" name="tongjiqiandanBaofei" type="text" class="form-control  input-sm" readonly>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-3 control-label">签单件数</label>
					<div class="col-sm-3">
						<input id="tongjiqiandanNum" name="tongjiqiandanNum" type="text" class="form-control  input-sm" readonly>
					</div>
					<label class="col-sm-3 control-label">回收件数</label>
					<div class="col-sm-3">
						<input id="tongjireceiveNum" name="tongjireceiveNum" type="text" class="form-control  input-sm" readonly>
					</div>

				</div>
				
				<div class="form-group">
					<label class="col-sm-3 control-label">到会率(%)</label>
					<div class="col-sm-3">
						<input id="tongjidaohuilv" name="tongjidaohuilv" type="text" class="form-control  input-sm" readonly>
					</div>
					<label class="col-sm-3 control-label">回收率(%)</label>
					<div class="col-sm-3">
						<input id="tongjihuishoulv" name="tongjihuishoulv" type="text" class="form-control  input-sm" readonly>
					</div>

				</div>
				<div class="form-group">
				<label class="col-sm-3 control-label">签单率(%)</label>
					<div class="col-sm-3">
						<input id="tongjiqiandanlv" name="tongjiqiandanlv" type="text" class="form-control  input-sm" readonly>
					</div>
					
					<!--   <label class="col-sm-3 control-label">客户经理</label> -->
					<div class="col-sm-3">
						<input id="tongjikehujingli" name="tongjikehujingli" type="hidden"
							class="form-control  input-sm"> <input id="tongjiriqi" name="riqi"
							type="hidden" class="form-control  input-sm">
					</div>
				</div>

			</form>
		</div>


		<div class="box-body" id="cusWin">
			<table id="cusTable">
				<tr>
					<th w_check="true" w_index="id" width="3%;"></th>
					<th w_index="title">平台名称</th>
					<th w_index="startdate">开始时间</th>
					<th w_index="enddate">结束时间</th>
				</tr>
			</table>
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
			var $tongji = $('#tongji'); // 添加按钮

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
				//pageSize : 10 和 pageSizeSelect : true, 冲突
			});

			crudWin = dialog({
				title : '编辑',
				width : 600,
				content : document.getElementById('crudWin'),
				
				button:[ {
		             value: '计算',
		             callback: function () {
		            	 $("#daohuilv").val(parseFloat($("#daohuiNum").val()*100/$("#yaoyueNum").val()).toFixed(0));
		                 $("#huishoulv").val(parseFloat($("#receiveBaofei").val()*100/$("#qiandanBaofei").val()).toFixed(0));
		                 $("#qiandanlv").val(parseFloat($("#qiandanNum").val()*100/$("#daohuiNum").val()).toFixed(0));
		                 return false;
		             },
		             autoFocus: true
		         }],
		         
				okValue : '保存',
				ok : function() {
					that.save();
					return false;
				},
				
				cancelValue : '关闭',
				cancel : function() {
					this.close();
					return false;
				}
			});

			function search() {
				var schData = getFormData($schFrm);
				gridObj.search(schData);
			}

			// 保存
			this.save = function() {
				var self = this;
				var data = getFormData($crudFrm);
				var validateVal = validator.form();
				if (validateVal) {
					Action.post(submitUrl, data, function(result) {
						location.reload();
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

			var cusGridObj = $.fn.bsgrid.init('cusTable', {
				url : ctx + 'listBizPlatformForCal.do',
				pageSizeSelect : true,
				rowHoverColor : true // 移动行变色
				,
				rowSelectedColor : false // 选择行不高亮
				,
				isProcessLockScreen : false // 加载数据不显示遮罩层
				,
				displayBlankRows : false,
				pagingLittleToolbar : true//,
				//pageSize : 10
			});
			$("#bizplatTitle").click(
					function() {
						cusGridObj.search('date=' + $("#riqi").val()
								+ '&empid=' + $("#kehujingli").val());
						cusWin.showModal();
					});

			var cusWin = dialog({
				title : '选择业务平台',
				width : 800,
				content : document.getElementById('cusWin'),
				okValue : '保存',
				ok : function() {
					var name = cusGridObj.getCheckedValues('title');
					if (name.length != 1) {
						alert("请选择一个业务平台.");
						return false;
					}
					$('#bizplatTitle')
							.val(cusGridObj.getCheckedValues('title'));
					$('#bizplatId').val(cusGridObj.getCheckedValues('id'));
					this.close();
					return false;
				},
				cancelValue : '取消',
				cancel : function() {
					this.close();
					return false;
				},
				onshow : function() {
					$("#cusTable_pt_outTab").width($("#cusTable").width());
				}
			});
			
			var getTongjiInfo = function() {
				
				$.getJSON("${ctx}getBizplatTongjiInfo.do", {'currMonth' : currMonth}, function(result) {
					loadTongjiFormData($crudFrm, result);
				});
				
				
				
				tongjiWin.showModal();
			}
       var currMonth;
			// 初始化事件
		$tongji.click(getTongjiInfo);
			
		var	tongjiWin = dialog({
				title : '统计信息',
				width : 600,
				content : document.getElementById('tongjiWin'),
				cancelValue : '关闭',
				cancel : function() {
					this.close();
					return false;
				}
			});

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
