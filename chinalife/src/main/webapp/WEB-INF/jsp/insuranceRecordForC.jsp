<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<jsp:include page="../menu.jsp">
	<jsp:param name="activeMenu" value="cse" />
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
		<li role="presentation"><a
			href="${ctx}openCustomerForC.do?id=${customer.id}">详情</a></li>
		<li role="presentation" class="active"><a
			href="${ctx}openInsuranceForC.do?id=${customer.id}">保单记录</a></li>
		<li role="presentation"><a
			href="${ctx}openServiceRecordForC.do?id=${customer.id}">服务记录</a></li>
		<li role="presentation"><a
			href="${ctx}openSitRecordForC.do?id=${customer.id}">拜访记录</a></li>
		<li><a href="${ctx}openCustomerBasic.do"><i
				class="fa fa-reply"></i>返回</a></li>
	</ul>
	<!-- Main content -->
	<section class="content">

		<!-- Your Page Content Here -->

		<div class="box" style="overflow-x: auto;">
			<div class="box-header">
				<div class="btn-group">
					<a id="addBtn" class="btn btn-primary"> <i class="fa"></i> 录入
					</a>
				</div>
			</div>
			<!-- /.box-header -->

			<div class="box-body" style="width: 3500px; overflow-x: auto;">
				<table id="searchTable">
					<tr>
						<th w_index="baoxiandanhao" w_sort="baoxiandanhao">保单号</th>
						<th w_index="toubaodanhao">投保单号</th>
						<th w_index="yewuyuandaima">业务员代码</th>
						<th w_index="yewuyuanxingming" w_sort="yewuyuanxingming">业务员姓名</th>
						<th w_index="xianzhongmingcheng" w_sort="xianzhongmingcheng">险种名称</th>
						<th w_index="baodanzhuangtai">保单状态</th>
						<th w_index="toubaoriqi" w_sort="toubaoriqi">投保日期</th>
						<th w_index="shengxiaoriqi">生效日期</th>
						<th w_index="jibenbaoe">基本保额</th>
						<th w_index="jibenbaofei">基本保费</th>
						<th w_index="toubaorenxingming">投保人姓名</th>
						<th w_index="toubaorenxingbie">投保人性别</th>
						<th w_index="toubaorenshenfenzhenghao">投保人身份证号</th>
						<th w_index="toubaorenshoujihao">投保人手机号</th>
						<th w_index="toubaorentongxundizhi" w_sort="toubaorentongxundizhi">投保人通讯地址</th>
						<th w_index="toubaorenzhiye">投保人职业</th>
						<th w_index="beibaoxianrenxingming">被保人姓名</th>
						<th w_index="beibaoxianrenxingbie">被保险人性别</th>
						<th w_index="beibaoxianrenshenfenzhenghao">被保险人身份证号</th>
						<th w_index="beibaoxianrenshoujihao">被保险人手机号</th>
						<th w_index="beibaoxianrentongxundizhi">被保险人通讯地址</th>
						<th w_index="beibaoxianrenzhiye">被保险人职业</th>
						<th w_index="beibaoxianrenyutoubaorenguanxi">被保险人与投保人关系</th>
						<th w_index="shouyirenxingming">受益人姓名</th>
						<th w_index="shouyirenxingbie">受益人性别</th>
						<th w_index="shouyirenshenfenzhenghao">受益人身份证号</th>
						<th w_index="shouyishunxu">受益顺序</th>
						<th w_index="shouyifene">受益份额</th>
						<th w_index="shouyirenyutoubaorenguanxi">受益人与投保人关系</th>
						<th w_index="jiaofeiqi">缴费期</th>
						<th w_index="baoxianqi">保险期</th>
						<th w_index="jiaofeiyinhang">缴费银行</th>
						<th w_index="jiaofeizhanghao">缴费账号</th>
						<th w_index="chushilaiyuan">初始来源</th>
						<th w_index="jigouhao" w_sort="jigouhao">机构号</th>
						<th w_index="xianzhongdaima">险种代码</th>
						<th w_index="qudao">渠道</th>
						<th w_index="zhihang">支行</th>
						<th w_index="wangdian">网点</th>
						<th w_index="dianhua">电话</th>
						<th w_index="chushengriqi">出生日期</th>
						<th w_index="bumengjingli">部门经理</th>
						<th w_index="yuanzhuanguanyuangonghao">原专管员工号</th>
						<th w_index="yuanzhuanguanyuan">原专管员</th>
						<th w_index="xinfenpeirenyuangonghao">新分配人员工号</th>
						<th w_index="xinfenpeirenyuan">新分配人员</th>
						<th w_index="fafangshijian">发放时间</th>
						<th w_index="fafangbiaoshi">发放标识</th>
						<th w_render="operate">操作</th>
					</tr>
				</table>
			</div>
			<!-- /.box-body -->
		</div>

		<div id="crudWin">
			<form id="crudFrm" class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">保单号</label>
					<div class="col-sm-2">
						<input name="baoxiandanhao" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">投保单号</label>
					<div class="col-sm-2">
						<input name="toubaodanhao" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">业务员代码</label>
					<div class="col-sm-2">
						<input name="yewuyuandaima" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">业务员姓名</label>
					<div class="col-sm-2">
						<input name="yewuyuanxingming" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">险种名称</label>
					<div class="col-sm-2">
						<input name="xianzhongmingcheng" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">保单状态</label>
					<div class="col-sm-2">
						<input name="baodanzhuangtai" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">投保日期</label>
					<div class="col-sm-2">
						<input name="toubaoriqi" type="text"
							class="form-control  input-sm" 
							onfocus="WdatePicker({skin:'default'})">
					</div>

					<label class="col-sm-2 control-label">生效日期</label>
					<div class="col-sm-2">
						<input name="shengxiaoriqi" type="text"
							class="form-control  input-sm" 
							onfocus="WdatePicker({skin:'default'})">
					</div>

					<label class="col-sm-2 control-label">基本保额</label>
					<div class="col-sm-2">
						<input name="jibenbaoe" type="text" class="form-control  input-sm"
							>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">基本保费</label>
					<div class="col-sm-2">
						<input name="jibenbaofei" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">投保人姓名</label>
					<div class="col-sm-2">
						<input name="toubaorenxingming" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">投保人性别</label>
					<div class="col-sm-2">
						<input name="toubaorenxingbie" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">投保人身份证号</label>
					<div class="col-sm-2">
						<input name="toubaorenshenfenzhenghao" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">投保人手机号</label>
					<div class="col-sm-2">
						<input name="toubaorenshoujihao" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">投保人通讯地址</label>
					<div class="col-sm-2">
						<input name="toubaorentongxundizhi" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">投保人职业</label>
					<div class="col-sm-2">
						<input name="toubaorenzhiye" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">被保人姓名</label>
					<div class="col-sm-2">
						<input name="beibaoxianrenxingming" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">被保险人性别</label>
					<div class="col-sm-2">
						<input name="beibaoxianrenxingbie" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">被保险人身份证号</label>
					<div class="col-sm-2">
						<input name="beibaoxianrenshenfenzhenghao" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">被保险人手机号</label>
					<div class="col-sm-2">
						<input name="beibaoxianrenshoujihao" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">被保险人通讯地址</label>
					<div class="col-sm-2">
						<input name="beibaoxianrentongxundizhi" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">被保险人职业</label>
					<div class="col-sm-2">
						<input name="beibaoxianrenzhiye" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">被保险人与投保人关系</label>
					<div class="col-sm-2">
						<input name="beibaoxianrenyutoubaorenguanxi" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">受益人姓名</label>
					<div class="col-sm-2">
						<input name="shouyirenxingming" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">受益人性别</label>
					<div class="col-sm-2">
						<input name="shouyirenxingbie" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">受益人身份证号</label>
					<div class="col-sm-2">
						<input name="shouyirenshenfenzhenghao" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">受益顺序</label>
					<div class="col-sm-2">
						<input name="shouyishunxu" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">受益份额</label>
					<div class="col-sm-2">
						<input name="shouyifene" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">受益人与投保人关系</label>
					<div class="col-sm-2">
						<input name="shouyirenyutoubaorenguanxi" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">缴费期</label>
					<div class="col-sm-2">
						<input name="jiaofeiqi" type="text" class="form-control  input-sm"
							>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">保险期</label>
					<div class="col-sm-2">
						<input name="baoxianqi" type="text" class="form-control  input-sm"
							>
					</div>

					<label class="col-sm-2 control-label">缴费银行</label>
					<div class="col-sm-2">
						<input name="jiaofeiyinhang" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">缴费账号</label>
					<div class="col-sm-2">
						<input name="jiaofeizhanghao" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">机构号</label>
					<div class="col-sm-2">
						<input name="jigouhao" type="text" class="form-control  input-sm"
							>
					</div>

					<label class="col-sm-2 control-label">险种代码</label>
					<div class="col-sm-2">
						<input name="xianzhongdaima" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">渠道</label>
					<div class="col-sm-2">
						<input name="qudao" type="text" class="form-control  input-sm"
							>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">支行</label>
					<div class="col-sm-2">
						<input name="zhihang" type="text" class="form-control  input-sm"
							>
					</div>

					<label class="col-sm-2 control-label">网点</label>
					<div class="col-sm-2">
						<input name="wangdian" type="text" class="form-control  input-sm"
							>
					</div>

					<label class="col-sm-2 control-label">电话</label>
					<div class="col-sm-2">
						<input name="dianhua" type="text" class="form-control  input-sm"
							>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">出生日期</label>
					<div class="col-sm-2">
						<input name="chushengriqi" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">部门经理</label>
					<div class="col-sm-2">
						<input name="bumengjingli" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">原专管员</label>
					<div class="col-sm-2">
						<input name="yuanzhuanguanyuan" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">新分配人员</label>
					<div class="col-sm-2">
						<input name="xinfenpeirenyuan" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">发放时间</label>
					<div class="col-sm-2">
						<input name="fafangshijian" type="text"
							class="form-control  input-sm" >
					</div>

					<label class="col-sm-2 control-label">发放标识</label>
					<div class="col-sm-2">
						<input name="fafangbiaoshi" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">原专管员工号</label>
					<div class="col-sm-2">
						<input name="yuanzhuanguanyuangonghao" type="text"
							class="form-control  input-sm" >
					</div>
					<label class="col-sm-2 control-label">新分配人员工号</label>
					<div class="col-sm-2">
						<input name="xinfenpeirenyuangonghao" type="text"
							class="form-control  input-sm" >
					</div>
					<label class="col-sm-2 control-label">初始来源</label>
					<div class="col-sm-2">
						<input name="chushilaiyuan" type="text"
							class="form-control  input-sm" >
					</div>
				</div>
			</form>
		</div>

		<script type="text/javascript">
			var that = this;

			var pk = 'id'; // java类中的主键字段
			var listUrl = ctx
					+ 'listInsuranceRecord.do?idcardnum=${customer.idcardnum}'; // 查询
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
				url : listUrl,
				pageSizeSelect : true,
				rowHoverColor : true // 移动行变色
				,
				rowSelectedColor : false // 选择行不高亮
				,
				isProcessLockScreen : false // 加载数据不显示遮罩层
				,
				displayBlankRows : false
			//
			});

			crudWin = dialog({
				title : '编辑',
				width : 800,
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
