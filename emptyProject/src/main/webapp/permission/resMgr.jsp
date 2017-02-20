<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>后台管理</title>
  <script type="text/javascript" src="js/SelectRoleWin.js"></script>
  <style type="text/css">
  table th{text-align: right;}
  table th s {color:red;}
#sysResTree .bui-tree-list {border: 0px solid #c3c3d6;}
#topTab .tab-panel-inner{border-bottom: 1px solid #c3c3d6;}
  </style>
 </head>
<body><rms:role operateCode="html_search">
  
<div class="container">
  	<div class="row show-grid">
		<div >
			<div id="topTab" style="padding-bottom: 20px;"></div>
			<div class="row show-grid">
				<div class="span8">
				<div class="panel">
					<div class="panel-header clearfix">
					    <h3 class="pull-left">资源</h3>
					    <div class="pull-right">
				            <rms:role operateCode="opt_node">
								<button id="btnAddRoot" class="button button-small"><i class="icon-plus"></i>添加资源</button>
							</rms:role>
				        </div>
					 </div>
					 <div class="panel-body">
						<div id="sysResTree"></div>
					</div>
				</div>
				 </div>
				<div class="span20">
					<div style="padding-left: 20px;">
						<div id="rightTip" ><h3> &lt;&lt; 点击菜单查看信息</h3></div>
						<div id="rightCont" style="display: none;">
							<div class="panel">
						      <div class="panel-header">
						        <h3>基础信息</h3>
						      </div>
						      <div class="panel-body">
						        <form id="baseForm" class="form-horizontal">
									<input type="hidden" name="parentId" value="0">
									<input type="hidden" name="srId" value="0">
									<table width="100%" cellpadding="5">
						        		<caption id="addOptMsg"></caption>
										<tr><th width="100"><s>*</s>节点名称:</th><td>
											<input name="resName"
													data-rules="{required:true,minlength:2,maxlength:20}"
													class="input-normal control-text" type="text" />
										</td></tr>
										<tr><th>URL:</th><td>
											<input name="url"
												data-rules="{minlength:0,maxlength:200}"
												class="input-large control-text"/>
										</td></tr>
										<rms:role operateCode="opt_node">
											<tr><td></td><td> <button id="btnSave" type="button" class="button button-primary">保存</button></td></tr>
										</rms:role>
						       		</table>
								</form>
						      </div>
						    </div>
							<br>
				          	<div>
						         
							       <div class="panel" id="permPanel">
								      <div class="panel-header">
								        <h3>操作权限</h3>
								      </div>
								      <div class="panel-body">
								      	<rms:role operateCode="html_addSysFun">
									        <form id="addOptForm">
									        	<table width="100%" cellpadding="5">
									        		<caption id="addOptMsg"></caption>
													<tr><th width="100"><s>*</s>权限代码:</th><td>
														<input name="operateCode"
															data-rules="{required:true,minlength:2,maxlength:50}"
															class="input-normal control-text" type="text" />
													</td></tr>
													<tr><th><s>*</s>权限名称:</th><td>
														<input name="operateName"
															data-rules="{required:true,minlength:2,maxlength:50}"
															class="input-normal control-text" type="text" />
													</td></tr>
													<tr><th>URL:</th><td>
														<textarea name="url"
															data-rules="{minlength:0,maxlength:200}"
															
															class="control-row4 input-large"></textarea>
															(多个URL用逗号","隔开)
													</td></tr>
													<tr><td></td><td> <button id="btnOptAdd" type="button" class="button button-primary">添加权限</button></td></tr>
									       		</table>
							          	</form></rms:role>
							          	<div>
								          	<div class="tips tips-small tips-info">
										        <span class="x-icon x-icon-small x-icon-info"><i class="icon icon-white icon-info"></i></span>
										        <div class="tips-content">btn_xx表示按钮权限;grid_xx表示表格按钮权限;html_xx表示页面权限</div>
										      </div>
											<div id="gridOpt" style="padding-top:5px;"></div>
										</div>
								      </div>
								  </div>
							    
					        </div>
					</div>
				</div>
				</div>
			</div>
		</div>
	</div>
</div>

	<div id="content" class="hide">
		<form id="J_Form" class="form-horizontal">
			<div class="row">
				<div class="control-group span8">
					<label class="control-label"><s>*</s>顶部菜单名称:</label>
					<div class="controls">
						<input name="tabName"
							data-rules="{required:true,minlength:2,maxlength:20}"
							class="input-normal control-text" type="text" />
					</div>
				</div>
			</div>
		</form>
	</div>
	
	<div id="addNodeContent" class="hide">
		<form id="addNodeForm" class="form-horizontal">
			<input type="hidden" name="parentId" value="0">
			<div class="row">
				<div class="control-group span16">
					<label class="control-label"><s>*</s>节点名称:</label>
					<div class="controls">
						<input name="resName"
							data-rules="{required:true,minlength:2,maxlength:20}"
							class="input-normal control-text" type="text" />
					</div>
				</div>
			</div>
			<div class="row">
				<div class="control-group span16">
					<label class="control-label">URL:</label>
					<div class="controls">
						<input name="url"
							data-rules="{minlength:0,maxlength:200}"
							class="input-large control-text"/>
					</div>
				</div>
			</div>
		</form>
	</div>

	<script type="text/javascript">
$(function() {
	var listTopMenuUrl = ctx + 'listTopMenu.do';
	var listMenuUrl = ctx + 'listMenuByTabId.do';
	var addTopMenuUrl = ctx + 'addTopMenu.do';
	var addNodeUrl = ctx + 'addRSysRes.do';
	var delResUrl = ctx + 'delRSysRes.do';
	var updateResUrl = ctx + 'updateRSysRes.do';
	var addOptUrl = ctx + 'addSysFunction.do';
	var delOptUrl = ctx + 'delRSysFunction.do';
	var listOptUrl = ctx + 'listSysFunctionBySrId.do';
	var listRolePermissionSfIdUrl = ctx + 'listRolePermissionSfId_backuser.do';
	var addRolePermissionUrl = ctx + 'addRolePermission.do';
	var delRolePermissionUrl = ctx + 'delRolePermission.do';

	var tree;
	var tab;
	var treeStore;
	var gridStore;
	var topMenuDialog;
	var $addTabLi;
	var baseForm,addOptForm;
	
	var $rightTip = $('#rightTip');
	var $rightCont = $('#rightCont');
	var $permPanel = $('#permPanel');

	function init() {
		initTab();
		initTree();
		initDialog();
		initForm();
		initGrid();
	}

	function initTab() {
		var store = new BUI.Data.Store({
			url : listTopMenuUrl,
			autoLoad : true
		});
		store.on('beforeprocessload', function(e) {
			var rows = e.data;
			var items = [];
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				items.push({
					text : row.tabName,
					value : row.id
				});
			}
			
			tab = new BUI.Tab.TabPanel({
				render : '#topTab',
				elCls : 'nav-tabs',
				autoRender : true,
				children : items
			});
			
			tab.on('afterAddChild',function(e){
				var item = e.child;
				tab.setSelected(item);
				appendLast($addTabLi);
			});
			
			tab.on('itemselected', function(e) {
				var item = e.item;
				postTreeData(item);
			});

			tab.setSelected(tab.getFirstItem());
			
			appendNewTabBtn();
		});
	}

	function initTree() {
		treeStore = new BUI.Data.TreeStore({
			url : listMenuUrl,
			map : {
				'srId' : 'id',
				'resName' : 'text'
			},
			showLine : true //显示连接线
		});
		
		tree = new BUI.Tree.TreeList({
			render : '#sysResTree',
			showLine : true,
			height : '100%',
			itemTplRender : function(node) {
				var text = node.text;
				text += '&nbsp;' + buildTreeButton(node);

				return '<li>' + text + '</li>'
			},
			store : treeStore
		});

		tree.on('selectedchange', function(ev) {
			var node = ev.item;
			bindData(node.record);
		});

		tree.render();
	}
	
	function initDialog() {
		topMenuDialog = new BUI.Editor.DialogEditor({
			contentId:'content'
			,form:{srcNode:$('#J_Form')}
			,title:'添加顶部菜单'
            ,buttons:[
				{text:'确定',elCls : 'button button-primary',handler : function(){
					addTopMenu();
                }}
              ,{text:'取消',elCls : 'button',handler : function(){
                	this.close();
               }}
			]
		});
		
		addNodeDialog = new BUI.Editor.DialogEditor({
			contentId:'addNodeContent'
			,form:{srcNode:$('#addNodeForm')}
			,success:function() {
				saveNode();
			}
            ,buttons:[
				{text:'确定',elCls : 'button button-primary',handler : function(){
					saveNode();
                }}
              ,{text:'取消',elCls : 'button',handler : function(){
                	this.close();
               }}
			]
		});
	}
	
	function postTreeData(tabItem) {
		var value = tabItem.get('value');
		treeStore.load({
			tabId : value
		});
	}
	
	function initForm() {
		baseForm = new BUI.Form.Form({srcNode:'#baseForm',autoRender:true});
		addOptForm = new BUI.Form.Form({srcNode:'#addOptForm',autoRender:true});
	}
	
	function initGrid() {
		var columns = [
      		{title:'权限代码',dataIndex:'operateCode',sortable:false,width:200}
           , {title:'权限名称',dataIndex:'operateName',sortable:false,width:200}
           , {title:'URL',dataIndex:'url',sortable:false,width:200,renderer:function(val,obj){
        	   var urls = val.split(/\s*,\s*|\s*，\s*/);
        	   var html = [];
        	   for(var i=0,len=urls.length;i<len;i++) {
        		   html.push(urls[i]);
        	   }
        	   html.sort();
        	   return html.join('<br>');
           }}
           , {title:'操作',dataIndex:'',sortable:false,renderer : function(value,obj){
         	   var updateStr = '<span class="grid-command btn-auth">授权</span>';
                var delStr = '<span class="grid-command btn-del">删除</span>';
                return RightUtil.auth('grid_auth',updateStr,'')  
                	+ RightUtil.auth('grid_del',delStr,'');
              }}
       ];
		
		gridStore = new BUI.Data.Store({
    		url : listOptUrl,
    		totalProperty:'total',
          	proxy : {
	            method:'POST'
          	}
    		
        });
      	
      	grid = new BUI.Grid.Grid({
            render:'#gridOpt'
           	,columns : columns
           	,store: gridStore
           	,emptyDataTpl : '<div class="centered"><h2>无数据</h2></div>'
      	});
      	
      	 //监听事件，删除一条记录
        grid.on('cellclick',function(ev){
        	var sender = $(ev.domTarget); //点击的Dom
        	if(sender.hasClass('btn-auth')){
            	var record = ev.record;
            	auth(record);
        	}
        	if(sender.hasClass('btn-del')){
            	var record = ev.record;
            	delOpt(record);
        	}
        });

      	grid.render();
	}
	
	
	function saveNode() {
		var url = addNodeUrl;
		
		addNodeDialog.valid();
    	if(addNodeDialog.isValid()) {
    		var form = addNodeDialog.get('form');
    		var record = form.toObject(); // 表单数据
    		var selectedTabItem = tab.getSelected();
    		record.tabId = selectedTabItem.get('value');
    		Action.post(url,record,function(result){
				Action.execResult(result,function(result){
					addNodeDialog.close();
					loadTree();
				});
			});
    	}
	}
	
	function saveRes(){
		if(baseForm.isValid()) {
			var data = baseForm.toObject();
			Action.post(updateResUrl,data,function(result){
				Action.execResult(result,function(result){
					loadTree();
					BUI.Message.Alert('修改成功');
				});
			});
		}	
	}
	
	function addOpt() {
		if(addOptForm.isValid()) {
			var data = addOptForm.toObject();
			var sysRes = baseForm.toObject();
			data.srId = sysRes.srId;
			doAddOpt(data);
		}
	}
	
	function doAddOpt(row){
		Action.post(addOptUrl,row,function(data){
			Action.execResult(data,function(){
				gridStore.load();
				addOptForm.clean();
			});
		})
	}
	
	function bindData(node) {
		resetForm();
		
		baseForm.setRecord(node);
		
		gridStore.load({srId:node.id});
		
		$rightTip.hide();
		$rightCont.show();
		
		if(node.children && node.children.length > 0) {
			$permPanel.hide();
		}else{
			$permPanel.show();
		}
	}
	
	function resetForm() {
		// 清除错误并重置
		baseForm.clean();
		addOptForm.clean();
	}
	
	
	function appendNewTabBtn() {
		$addTabLi = $('<li><span style="margin-left:10px;" id="addNewTabBtn"><i class="icon-plus" title="新建"></i></span></li>')
		
		$addTabLi.click(function(){
			topMenuDialog.setValue({},true); //设置值，并且隐藏错误
			topMenuDialog.show();
		});
		
		appendLast($addTabLi);
	}
	
	function appendLast($addTabLi) {
		$addTabLi.appendTo($('#topTab').find('ul'));
	}
	
	function addTopMenu() {
		topMenuDialog.valid();
    	if(topMenuDialog.isValid()) {
    		var form = topMenuDialog.get('form');
    		var record = form.toObject(); // 表单数据
			Action.post(addTopMenuUrl,record,function(tab){
				addTabItem({text:tab.tabName,value:tab.id});
				topMenuDialog.close();
			});
    	}
	}
	
	function addTabItem(item) {
		var itemCount = tab.getItemCount();
		tab.addItemAt(item,itemCount);
	}
	
	function reset() {
		if(tab) {
			tab.clearSelected();
			tab.setSelected(tab.getFirstItem());
		}
	}

	function buildTreeButton(node) {
		var html = [];
		if(node.parentId == 0) {
			var addNodeStr = '<a onclick="'
				+ FunUtil.createFunStopProp(window, 'addChildNode', node)
				+ ' return false;">[添加子节点]</a>';
			
			html.push(RightUtil.auth('opt_node',addNodeStr,''));
		}
		if (node.children.length == 0) {
			var delNodeStr = '&nbsp;<a onclick="'
				+ FunUtil.createFunStopProp(window, 'delSysRes', node)
				+ ' return false;">[删除节点]</a>';
				
			html.push(RightUtil.auth('opt_node',delNodeStr,''));
		}
		return html.join('');
	}
	
	function addRootMenu(){
		addNodeDialog.show();
		addNodeDialog.set('title','添加根节点');
		addNodeDialog.setValue({parentId:0},true); //设置值，并且隐藏错误
	}

	window.addChildNode = function(node){
		addNodeDialog.show();
		addNodeDialog.set('title','父节点:' + node.text);
		addNodeDialog.setValue({parentId:node.id},true);
	}
	
	window.delSysRes = function(node){
		var self = this;
		if (node) {
			var msg = '确定要删除<strong>'+node.text+'</strong>吗?';
			BUI.Message.Confirm(msg,function(){
				Action.post(delResUrl,{srId:node.id,resName:node.text},function(result){
					Action.execResult(result,function(result){
						loadTree();
					});
				});
			});
		}
	}
	
	function delOpt(rowData) {
		var msg = '确定要删除<strong>'+rowData.operateName+'</strong>吗?';
		BUI.Message.Confirm(msg,function(){
			doDelOpt(rowData);
		});
	}
	
	function doDelOpt(row){
		Action.post(delOptUrl,row,function(result){
			Action.execResult(result,function(result){
				gridStore.load();
			});
		});
	}
	
	function loadTree() {
		treeStore.load();
	}
	
	$('#btnAddRoot').click(function(){
		addRootMenu();
	});
	
	$('#btnSave').click(function(){
		saveRes();
	});
	$('#btnOptAdd').click(function(){
		addOpt();
	});
	
	/// 
	
	
	var roleHandlers = {
		selectHandler:function(item){
			addSysFunRole(item);
		}
	  	,unselectHandler:function(item){
	  		delSysFunRole(item);
	  	}
	}
	
	function addSysFunRole(item) {
		item.url = addRolePermissionUrl;
		setRolePermission(item);
	}
	
	function delSysFunRole(item) {
		item.url = delRolePermissionUrl;
		setRolePermission(item);
	}
	
	function setRolePermission(param) {
		var url = param.url;
		
		var data = {
			sfId:param.extKey
			,roleId:param.roleId
		}
		
		Action.post(url,data,function(e){
			Action.execResult(e);
		});
	}
	
	
	
	  	
	// 资源管理授权功能
	function auth(rowData) {
		var sfId = rowData.sfId;
		Action.post(listRolePermissionSfIdUrl,{sfId:sfId},function(rolePerms){
			var roleIds = [];
			for(var i=0,len=rolePerms.length; i<len; i++) {
				roleIds.push(rolePerms[i].roleId);
			}
			
			SelectRoleWin.show(roleIds,sfId,function(){
    			gridStore.load();
    		},roleHandlers);
		});
	}
	

	init();
});
</script>
</rms:role>
</body>
</html>
