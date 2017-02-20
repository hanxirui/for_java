<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>后台管理</title>
  <script type="text/javascript" src="${ctx}res/js/MD5.js"></script>
  <script type="text/javascript" src="js/SelectRoleWin.js"></script>
 </head>
<body>
<rms:role operateCode="html_search">
  <div class="container">
    <div class="row">
      <form id="searchForm" class="form-horizontal">
        <div class="row">         
      	  <div class="control-group span8">
            <label class="control-label">用户名：</label>
            <div class="controls">
              <input type="text" class="control-text" name="usernameSch">
            </div>
          </div>    
          <div class="control-group">
            <div class="controls">
              <button  type="button" id="schBtn" class="button button-primary">搜索</button>
            </div>
          </div>
        </div>
      </form>
    </div>
      <hr>
    
    <div class="search-grid-container">
      <div id="grid"></div>
    </div>
    
   </div>
  
   <div id="content" class="hide">
      <form id="J_Form" class="form-horizontal">        
				 		 <div class="row">
          <div class="control-group span8">
            <label class="control-label"><s>*</s>用户名</label>
            <div class="controls">
              <input name="username" data-rules="{required:true,minlength:6,maxlength:16}" class="input-normal control-text" type="text"/>
            </div>
          </div>
        </div>
		<div class="row">
          <div class="control-group span8">
            <label class="control-label"><s>*</s>密码</label>
            <div class="controls">
              <input name="password" 
				 data-rules="{required:true,minlength:5,maxlength:20}"
				 class="input-normal control-text" type="password"/>
            </div>
          </div>
        </div>
					   					
      </form>
    </div>
<script type="text/javascript">
$(function(){
	var TYPE_ADD = 'add', TYPE_EDIT = 'update',TYPE_REMOVE = 'remove';
	var ROLE_TYPE = {
		PERSON : 1
		,GROUP : 2
	}
	
	var UserStateEnum = {OPEN:1,CLOSE:0};
	
	var UserState = {};
	UserState[UserStateEnum.OPEN] = '<span class="label label-success">启用</span>';
	UserState[UserStateEnum.CLOSE] = '<span class="label label-important">禁用</span>';
    
	var listUrl = ctx + 'listRUser.do'; // 查询
	var addUrl = ctx + 'addRUser.do'; // 添加
	var resetUserPasswordUrl = ctx + 'resetUserPassword.do';
	var addRoleUrl = ctx + 'addUserRole.do';
	var delRoleUrl = ctx + 'delUserRole.do';
	var optStateUrl = ctx + 'optState.do';
	
	var Grid = BUI.Grid;
	var Store = BUI.Data.Store;
	var Form = BUI.Form;
	
	var grid; // 表格
	var store; // 数据
	var schForm; // 查询form
	var editing;// 编辑表单
	
	var roleHandlers = {
		selectHandler:function(item){
			postAddRole(item);
		}
	  	,unselectHandler:function(item){
	  		postDelRole(item);
	  	}
	}

    function init() {
    	initSearchForm();
        initEditing();
    	initGrid();
    	initEvent();
    }
    
    function initSearchForm() {
    	var formCfg = {
	      srcNode : '#searchForm' 
	      ,value : {}
	    };
	    schForm = new Form.HForm(formCfg);
	    schForm.render();
    }
    
    function initStore() {
    	store = new Store({
    		url : listUrl,
          	autoLoad:true,
          	remoteSort:true,
    		totalProperty:'total',
          	pageSize:10,  // 配置分页数目
          	proxy : {
	            save : { //也可以是一个字符串，那么增删改，都会往那么路径提交数据，同时附加参数saveType
	                addUrl : addUrl
	            }
    			,method:'POST'
          	}
    		
        });
    	
    	return store;
    }
    
    function initEditing() {
    	editing = new BUI.Editor.DialogEditor({
			contentId:'content'
			,form:{srcNode:$('#J_Form')}
    		,success:function(){ // 回车
    			save();
    		}
            ,buttons:[
				{text:'确定',elCls : 'button button-primary',handler : function(){
	             	save();
                }}
              ,{text:'取消',elCls : 'button',handler : function(){
                	this.close();
               }}
			]
		});
    }
    
    function initGrid() {
    	var columns = [
    		{title:'用户名',dataIndex:'username'}
            ,{title:'用户角色',dataIndex:'',sortable:false,renderer:function(val,obj){
                	return roleFormatter(obj);
                }}
            , {title:'添加时间',dataIndex:'addTime'}
            , {title:'最后登录时间',dataIndex:'lastLoginDate'}
            , {title:'状态',dataIndex:'state',sortable:false,renderer:function(val){
            	return UserState[val];
            }}
            ,{title:'操作',dataIndex:'',sortable:false,renderer : function(value,obj){
           	  var stateOpenStr = '<span class="grid-command btn-openState">启用</span>';
           	  var stateCloseStr = '<span class="grid-command btn-closeState">禁用</span>';
           	  
           	  var stateStr = obj.state == UserStateEnum.OPEN ? stateCloseStr : stateOpenStr;
           	  
       	      var resetPswdStr = '<span class="grid-command btn-resetPswd">重置密码</span>';
              var setRoleStr = '<span class="grid-command btn-setRole">设置角色</span>';
              return RightUtil.auth('grid_optState',stateStr,'') + RightUtil.auth('grid_resetPwsd',resetPswdStr,'') 
              	+ RightUtil.auth('grid_setRole',setRoleStr,'');
            }}
        ];
    	
    	var store = initStore();
    	
    	grid = new Grid.Grid({
          	render:'#grid'
          	,columns : columns
          	,loadMask: true
          	,store: store
          	,forceFit: true
          	,emptyDataTpl : '<div class="centered"><h2>查询的数据不存在</h2></div>'
          	,tbar : {
              items : [
				 RightUtil.auth('btn_addUser',{text : '<i class="icon-plus"></i>新增用户',btnCls : 'button button-small',handler:add})
              ]
            }
          	// 底部工具栏
          	,bbar:{
              	// pagingBar:表明包含分页栏
              	pagingBar:true
    		}
    	});

    	grid.render();
    }
    
    function initEvent() {
    	 //监听事件，删除一条记录
        grid.on('cellclick',function(ev){
        	var sender = $(ev.domTarget); //点击的Dom
        	if(sender.hasClass('btn-resetPswd')){
            	var record = ev.record;
            	resetPassword(record);
        	}
        	if(sender.hasClass('btn-setRole')){
            	var record = ev.record;
            	addRole(record);
        	}
        	if(sender.hasClass('btn-openState')){
            	var record = ev.record;
            	record.state = UserStateEnum.OPEN;
            	optState(record);
        	}
        	if(sender.hasClass('btn-closeState')){
            	var record = ev.record;
            	record.state = UserStateEnum.CLOSE;
            	optState(record);
        	}
        });
        
        $('#schBtn').on('click',function(ev){
           ev.preventDefault();
           search();
    	});
    }
    
    
    //////// functions ////////
    
    function search() {
    	var param = schForm.serializeToObject();
    	param.start = 0;
    	store.load(param);
    }
    
    function save() {
    	editing.valid();
    	if(editing.isValid()) {
    		var editType = editing.get('editType');
    		var form = editing.get('form');
    		var record = form.serializeToObject(); // 表单数据
    		
    		var passwordVal = form.getFieldValue('password');
			record.password = faultylabs.MD5(passwordVal);
    		
    		store.save(editType,record,function(){
    			store.load();
    			editing.close();
    		})
    	}
    }
    
    function add(){
        editing.set('editType',TYPE_ADD);
        showEditor({});
    }
    
    function showEditor(record) {
		editing.set('record',record);
		editing.show();
		editing.setValue(record,true); //设置值，并且隐藏错误
    }
    
    function addRole(row) {
    	var userRoles = row.roles;
    	var userId = row.userId;
   		var roleIds = [];
   		for(var i=0,len=userRoles.length; i<len; i++) {
   			roleIds.push(userRoles[i].roleId);
   		}
   		SelectRoleWin.showPerson(roleIds,userId,function(){
   			store.load();
   		},roleHandlers);
    }
    
    function resetPassword(row){
    	BUI.Message.Confirm("确定给"+row.username+"重置密码吗?",function(){
    		Action.jsonAsyncActByData(resetUserPasswordUrl,row,function(e){
    			Action.execResult(e,function(){
    				store.load();
					BUI.Message.Alert('密码重置成功,新密码为:<br><strong style="font-size:14px;color:red;">' + e.message + '</strong>');
    			});
			});
       	},'question');
    }
    
    function optState(record) {
    	var optName = record.state == UserStateEnum.OPEN ?　'启用' : '禁用';
    	
    	BUI.Message.Confirm("确定" + optName + record.username+"吗?",function(){
    		Action.jsonAsyncActByData(optStateUrl,record,function(e){
    			Action.execResult(e,function(){
    				store.load();
    			});
			});
       	},'question');
    }
    
    
    function roleFormatter(rowData,td,rowIndex){
    	var roles = rowData.roles;
    	if(!roles || roles.length == 0){
    		return '<span style="color:red;">未分配角色</span>';
    	}
    	
    	var roleNameHtml = [];
    	for(var i=0,len=roles.length; i<len; i++) {
    		roleNameHtml.push(roles[i].roleName);
    	}
    	
    	// 所有的角色名
    	var roleNameStr = roleNameHtml.join('、');
    	
    	var resultStr = ['<div title="'+roleNameStr+'">'];
    	
    	if(roleNameHtml.length > 6){ 
    		for(var i=0; i<6; i++) {
    			resultStr.push(roleNameHtml[i] + "、");
    		}
    		resultStr.push('...');
    	}else{
    		resultStr.push(roleNameStr);
    	}
    	
    	resultStr.push('</div>');
    	
    	return resultStr.join('');
    }
    
    function postAddRole(item) {
    	item.url = addRoleUrl;
    	setUserRole(item);
    }
    
    function postDelRole(item) {
    	item.url = delRoleUrl;
    	setUserRole(item);
    }
    
	function setUserRole(param) {
		var roleId = param.roleId;
		var url = param.url;
		
		var data = {
			userId:param.extKey
			,roleId:roleId
			,roleType:param.roleType
			,groupId:param.groupId
		}
		
		Action.post(url,data,function(e){
			Action.execResult(e);
		});
	}
    
    init();
    
});
</script>
</rms:role>
</body>
</html>
