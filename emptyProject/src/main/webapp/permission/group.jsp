<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户组管理</title>
<style type="text/css">
table th {
	text-align: right;
}

table th s {
	color: red;
}

.bui-tree-list {
	border: 0px solid #c3c3d6;
}

.form-horizontal .valid-text {
	display: inline-block;
}
</style>
</head>
<body>
<rms:role operateCode="html_search">
	<div class="container">
		<div class="row show-grid">
			<div>
				<div id="topTab"></div>
				<div class="row show-grid">
					<div class="span8">
						<div class="panel">
							<div class="panel-header clearfix">
								<h3 class="pull-left">用户组</h3>
								<div class="pull-right">
									<button id="btnAddRoot" class="button button-small">
										添加用户组</button>
								</div>
							</div>
							<div class="panel-body">
								<div id="groupTree"></div>
							</div>
						</div>
					</div>
					<div class="span16">
						<div style="padding-left: 20px;">
							<div id="rightTip">
								<h3>&lt;&lt; 点击菜单查看信息</h3>
							</div>
							<div id="rightCont" style="display: none;">
								<div class="panel">
									<div class="panel-header">
										<h3>基础信息</h3>
									</div>
									<div class="panel-body">
										<form id="baseForm" class="form-horizontal" name="baseForm">
											<input type="hidden" name="parentId" value="0"> <input
												type="hidden" name="groupId" value="0">
											<table width="100%" cellpadding="5">
												<caption id="addOptMsg"></caption>
												<tr>
													<th width="100"><s> * </s> 用户组名称:</th>
													<td><input name="groupName"
														data-rules="{required:true,minlength:2,maxlength:20}"
														class="input-normal control-text" type="text"></td>
												</tr>
												<tr>
													<td></td>
													<td>
														<button id="btnSave" type="button"
															class="button button-primary">保存</button>
													</td>
												</tr>
											</table>
										</form>
									</div>
								</div>
								<br>
								<div id="tabPanelCont">
									<div id="groupTab"></div>
									<div id="tabPanel" class="tab-body">
										<div>
											<form id="searchForm" class="form-horizontal"
												name="searchForm">
												<table cellpadding="5">
													<tr>
														<td>用户名:</td>
														<td><input name="usernameSch"
															class="input-normal control-text" type="text"></td>
														<td>
															<button type="button" id="btnUserSch"
																class="button button-small">搜索</button>
														</td>
													</tr>
												</table>
											</form>
											<hr>
											<div>
												<div id="gridUser"></div>
											</div>
										</div>
										<div>
											<form id="addRoleForm" name="addRoleForm">
												<table cellpadding="5">
													<tr>
														<th>角色名称:</th>
														<td><input name="roleName"
															data-rules="{required:true,minlength:2,maxlength:20}"
															class="input-normal control-text" type="text"></td>
														<td>
															<button type="button" id="btnRoleAdd"
																class="button button-small">添加角色</button>
														</td>
													</tr>
												</table>
											</form>
											<hr>
											<div>
												<div id="gridRole"></div>
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
	<div id="addNodeWin" class="hide">
		<form id="addNodeForm" name="addNodeForm">
			<input type="hidden" name="parentId" value="0">
			<table width="100%">
				<tr>
					<th width="100"><s> * </s> 用户组名称:</th>
					<td><input name="groupName"
						data-rules="{required:true,minlength:2,maxlength:20}"
						class="input-normal control-text" type="text"></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="addUserWin" class="hide">
		<div>
			<form id="addUserForm" name="addUserForm">
				<table cellpadding="5">
					<tr>
						<td>用户名:</td>
						<td><input name="usernameSch"
							class="input-normal control-text" type="text"></td>
						<td>
							<button type="button" id="btnAddUserSch"
								class="button button-small">查询</button>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<hr>
		<div>
			<div>
				请选择待添加的用户,已选择 <strong id="addUserCnt"> 0 </strong> 位用户
			</div>
			<div id="gridAddUser"></div>
		</div>
	</div>


<script type="text/javascript">
$(function(){
	var listAllGroupUrl = ctx + 'listAllGroup.do';
	var addGroupUrl = ctx + 'addRGroup.do';
	var updateGroupUrl = ctx + 'updateRGroup.do';
	var delRGroupUrl = ctx + 'delRGroup.do';
	
	var listUserUrl = ctx + 'listRGroupUser.do';
	var delUserUrl = ctx + 'delRGroupUser.do';
	
	var tree;
	var treeStore;
	var tab;
	var tabStore;
	var userGrid;
	var userGridStore,roleGridStore;
	var baseForm,searchForm;
	var addNodeDialog;
	
	var $rightTip = $('#rightTip');
	var $rightCont = $('#rightCont');
	var $tabPanelCont = $('#tabPanelCont');
	
	function init() {
		initTree();
		initForm();
		initTab();
		initGrid();
		initDialog();
	}
	
	function initTree() {
		treeStore = new BUI.Data.TreeStore({
	        //返回的数据如果数据有children字段，且children.length == 0 ，则认为存在未加载的子节点
	        //leaf = false，没有children字段也会认为子节点未加载，展开时会自动加载
	        url: listAllGroupUrl,
			map : {
		      'groupId' : 'id'
		      ,'groupName' : 'text'
		    }
		    ,showLine : true //显示连接线
	        ,autoLoad: true
	    });
		
	    tree = new BUI.Tree.TreeList({
	        render: '#groupTree',
	        showLine: true,
	        width:250,
	        itemTplRender:function(node){
	        	var text = node.text;
				text += '&nbsp;' + buildTreeButton(node);

				return '<li>' + text + '</li>';
	        },
	        store: treeStore
	    });
	    
	    // 全部展开
		treeStore.on('load',function(ev){
			var params = ev.params,
			id = params.id,
			node = treeStore.findNode(id);
			
			var children = node.children;
			
			for(var i=0; i<children.length; i++) {
				children[i].expanded = true;
			}
		});
	    
	    tree.on('selectedchange',function(ev){
	    	var node = ev.item;
		    bindData(node.record);
	    });
	    
	    tree.render();
	}
	
	function initForm() {
		baseForm = new BUI.Form.Form({srcNode:'#baseForm',autoRender:true});
		searchForm = new BUI.Form.Form({srcNode:'#searchForm',autoRender:true});
	}
	
	function initTab() {
		tab = new BUI.Tab.TabPanel({
          render : '#groupTab',
          elCls : 'nav-tabs',
          panelContainer : '#tabPanel',//如果内部有容器，那么会跟标签项一一对应，如果没有会自动生成
          autoRender: true
          ,children:[
             {title:'用户组成员',value:'1',selected : true},
             {title:'用户组角色',value:'2'}
           ]
        });
	}
	
	function initGrid() {
		var columns = [
       		{title:'用户名',dataIndex:'username',width:300}
            ,{title:'操作',dataIndex:'',sortable:false,renderer : function(value,obj){
                 var delStr = '<span class="grid-command btn-del">删除</span>';
                 return RightUtil.auth('opt_groupUser',delStr,'');
             }}
        ];
       	
       	var store = initUserGridStore();
       	
       	userGrid = new BUI.Grid.Grid({
           	render:'#gridUser'
           	,columns : columns
           	,loadMask: true
           	,store: store
           	,emptyDataTpl : '<div class="centered"><h2>无成员</h2></div>'
           	,tbar : {
               items : [
                 RightUtil.auth('opt_groupUser',{text : '<i class="icon-plus"></i>添加成员',btnCls : 'button button-small',handler:addUser})
               ]
             }
           	,bbar:{
               	pagingBar:true
     		}
       	});
       	
       	userGrid.on('cellclick',function(ev){
       		var sender = $(ev.domTarget); //点击的Dom
        	if(sender.hasClass('btn-del')){
            	var record = ev.record;
            	delUser(record);
        	}
       	});

       	userGrid.render();
	}
	
	
	function initUserGridStore() {
		userGridStore = new BUI.Data.Store({
    		url : listUserUrl,
    		remoteSort:true,
    		totalProperty:'total',
          	pageSize:10,  // 配置分页数目
          	autoLoad:false,
          	proxy : {
	            method:'POST'
          	}
    		
        });
    	
    	return userGridStore;
	}
	
	function initDialog() {
		addNodeDialog = new BUI.Editor.DialogEditor({
			contentId:'addNodeWin'
			,form:{srcNode:$('#addNodeForm')}
			,success:function() {
				saveNode();
			}
			,width:500
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
	
	function addUser() {
		initAddUser();
		searchNoAddUser();
		addUserWin.show();
	}
	
	function searchUser() {
		var data = searchForm.toObject();
		var groupId = baseForm.getFieldValue('groupId');
		data.groupIdSch = groupId;
		userGridStore.load(data);
	}
	
	function delUser(rowData) {
		var msg = '确定要移除<strong>'+rowData.username+'</strong>吗?';
		BUI.Message.Alert(msg,function(r){
			doDelUser(rowData);
		});
	}
	
	function doDelUser(row){
		Action.post(delUserUrl,row,function(result){
			Action.execResult(result,function(result){
				searchUser();
			});
		});
	}
	
	function buildTreeButton(node) {
		var html = [];
		if(!node.parentId) {
			var addNodeStr = '<a onclick="'
					+ FunUtil.createFunStopProp(window, 'addChildNode', node)
					+ ' return false;">[添加子节点]</a>';
			html.push(RightUtil.auth('opt_node',addNodeStr,''));
		}
		if (node.children.length == 0) {
			var delNodeStr = '&nbsp;<a onclick="'
					+ FunUtil.createFunStopProp(window, 'delGroup', node)
					+ ' return false;">[删除节点]</a>';
			html.push(RightUtil.auth('opt_node',delNodeStr,''));
		}
		return html.join('');
	}
	
	function bindData(node) {
		resetForm();
		
		baseForm.setRecord(node);
		
		searchUser();
		searchRole();
		
		tab.setSelected(tab.getFirstItem());
		
		$rightTip.hide();
		$rightCont.show();
		
		if(node.children && node.children.length > 0) {
			$tabPanelCont.hide();
		}else{
			$tabPanelCont.show();
		}
		
	}
	
	function saveGroup(){
		if(baseForm.isValid()) {
			var data = baseForm.toObject();
			Action.post(updateGroupUrl,data,function(result){
				Action.execResult(result,function(result){
					treeStore.load();
					BUI.Message.Alert('修改成功');
				});
			});
		}	
	}

	
	function resetForm() {
		// 清除错误并重置
		baseForm.clean();
		
		addRoleForm.clean();
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
	
	function saveNode() {
		// 
		var url = addGroupUrl;
		
		addNodeDialog.valid();
    	if(addNodeDialog.isValid()) {
    		var form = addNodeDialog.get('form');
    		var data = form.toObject();
    		Action.post(url,data,function(result){
    			Action.execResult(result,function(result){
    				treeStore.load();
    				addNodeDialog.close();
    			});
    		});
    	}
	}
	
	window.delGroup = function(node){
		var self = this;
		if (node) {
			var msg = '确定要删除<strong>'+node.text+'</strong>吗?'+
					'<br><span class="red">角色及权限将一并删除</span>';
			BUI.Message.Confirm(msg,function(){
				Action.post(delRGroupUrl,{groupId:node.id,groupName:node.text},function(result){
					Action.execResult(result,function(result){
						treeStore.load();
					});
				});
			});
		}
	}
	
	$('#btnUserSch').click(function(){
		searchUser();
	});
	
	$('#btnSave').click(function(){
		saveGroup();
	});
	
	$('#btnAddRoot').click(function(){
		addRootMenu();
	});
	
	init();
	
	//--------添加用户组成员win-------
	var noAddUserForm;
	var gridAddUser;
	var gridAddUserStore;
	var addUserWin;
	var listAddUserUrl = ctx + 'listGroupNoAddUser.do';
	var addGroupUserUrl = ctx + 'addGroupUser.do';
	
	var $addUserCnt = $('#addUserCnt');
	var selectedData = {};
	
	function initAddUser() {
		if(!initAddUser.init) {
			initAddUser.init = true;
			initAddUserWin();
			initAddUserGrid();
			initAddUserForm();
		}
	}
	
	function initAddUserWin() {
		addUserWin = new BUI.Overlay.Dialog({
			contentId:'addUserWin'
			,width:500
			,height:450
			,zIndex:100
			,title:'添加成员'
			,success:function(){
				addGroupUser();
			}
			
		});
	}
	
	function initAddUserGrid() {
		var columns = [
        	{title:'用户名',dataIndex:'username',sortable:false,width:300}
        ];
        	
		gridAddUserStore = new BUI.Data.Store({
    		url : listAddUserUrl,
    		totalProperty:'total',
          	pageSize:10,  // 配置分页数目
          	autoLoad:false,
          	proxy : {
	            method:'POST'
          	}
    		
        });
       	
       	gridAddUser = new BUI.Grid.Grid({
           	render:'#gridAddUser'
           	,columns : columns
           	,loadMask: true
           	,store: gridAddUserStore
           	,idField :'userId'
           	
           	,emptyDataTpl : '<div class="centered"><h2>无数据</h2></div>'
           	,bbar:{
               	pagingBar:true
     		}
           	,plugins : [BUI.Grid.Plugins.CheckSelection]
       	});
       	
       	gridAddUser.on('itemselected',function(e){
       		var item = e.item;
       		selectedData[item.userId] = true;
       		refreshGridMsg();
       	});
       	
       	gridAddUser.on('itemunselected',function(e){
       		var item = e.item;
       		delete selectedData[item.userId];
       		refreshGridMsg();
       	});

       	gridAddUser.render();
	}
	
	function initAddUserForm() {
		noAddUserForm = new BUI.Form.Form({srcNode:'#addUserForm',autoRender:true});
	}
	
	function searchNoAddUser() {
		var data = noAddUserForm.toObject();
		var groupId = baseForm.getFieldValue('groupId');
		data.groupIdSch = groupId;
		
		gridAddUserStore.load(data);
		
		resetAddUser();
	}

	function resetAddUser() {
		selectedData = {};
		
		noAddUserForm.clean();
		
		$addUserCnt.text(0);
	}
	
	function addGroupUser() {
		var userIds = getChecked();
		if(userIds && userIds.length > 0){
			var groupId = baseForm.getFieldValue('groupId');
			var params = {userIds:userIds,groupId:groupId};
			
			Action.post(addGroupUserUrl,params ,function(e){
				if(e.success) {
					addUserWin.close();
					userGridStore.load();
				}else{
					BUI.Message.Alert('添加失败');
				}
			});
			
		}else{
			BUI.Message.Alert('请选择成员');
		}
	}
	
	function getChecked() {
		var userIds = [];
		for(var key in selectedData) {
			userIds.push(key);
		}
		return userIds;
	}
	
	function refreshGridMsg() {
		var count = getChecked().length;
		$addUserCnt.text(count);
	}
	
	$('#btnAddUserSch').click(function(){
		searchNoAddUser();
	});
	
	// groupRole
	var addRoleForm;
	var gridRole;
	var addRoleUrl = ctx + 'addRGroupRole.do';
	var delRoleUrl = ctx + 'delRGroupRole.do';
	var listRoleUrl = ctx + 'listRoleByGroupId.do';

	function initGroupRole() {
		if(!addRoleForm) {
			addRoleForm = new BUI.Form.Form({srcNode:'#addRoleForm',autoRender:true});
			
			var columns = [
          		{title:'角色名',dataIndex:'roleName',sortable:false,width:300}
               ,{title:'操作',dataIndex:'',sortable:false,renderer : function(value,obj){
                    var delStr = '<span class="grid-command btn-del">删除</span>';
                    return RightUtil.auth('grid_delRole',delStr,'');;
                }}
           ];
			
			roleGridStore = new BUI.Data.Store({
	    		url : listRoleUrl,
	    		totalProperty:'total',
	          	autoLoad:false,
	          	proxy : {
		            method:'POST'
	          	}
	    		
	        });
			
			gridRole = new BUI.Grid.Grid({
	           	render:'#gridRole'
               	,columns : columns
               	,loadMask: true
               	,store: roleGridStore
               	,emptyDataTpl : '<div class="centered"><h2>无角色,请添加</h2></div>'
               	,bbar:{
                   	pagingBar:true
         		}
           	});
			
			gridRole.on('cellclick',function(ev){
	       		var sender = $(ev.domTarget); //点击的Dom
	        	if(sender.hasClass('btn-del')){
	            	var record = ev.record;
	            	delRole(record);
	        	}
	       	});
			
			gridRole.render();
			
		}
	}

	function searchRole() {
		var data = {}
		data.groupId = baseForm.getFieldValue('groupId');
		roleGridStore.load(data);
	}

	function addRole() {
		if(addRoleForm.isValid()) {
			var data = addRoleForm.toObject();
			data.groupId = baseForm.getFieldValue('groupId');
			doAddRole(data);
		}
	}

	function doAddRole(row){
		Action.post(addRoleUrl,row,function(data){
			Action.execResult(data,function(){
				searchRole();
				addRoleForm.clean();
			});
		})
	}
	
	function delRole(rowData) {
		var msg = '确定要删除<strong>'+rowData.roleName+'</strong>吗?';
		BUI.Message.Confirm(msg,function(r){
			doDelRole(rowData);
		});
	}

	function doDelRole(row){
		Action.post(delRoleUrl,row,function(result){
			Action.execResult(result,function(result){
				searchRole();
			});
		});
	}
	
	$('#btnRoleAdd').click(function(){
		addRole();
	});
	
	initGroupRole();
	
});
</script>
</rms:role>
</body>
</html>
