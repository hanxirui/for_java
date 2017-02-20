<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>
<c:set var="bui" value="${res}bui/"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>后台管理</title>
  <script type="text/javascript" src="js/SelectRightWin.js?v="<%=Math.random() %>></script>
 </head>
<body><rms:role operateCode="html_search">
  <div class="container">
    <div class="row">
      <form id="searchForm" class="form-horizontal">
        <div class="row">         
      	  <div class="control-group span8">
            <label class="control-label">角色名：</label>
            <div class="controls">
              <input type="text" class="control-text" name="roleNameSch">
            </div>
          </div>    
      	  <div class="control-group span8">
            <label class="control-label">角色类型：</label>
            <div class="controls">
              <select name="roleTypeSch">
              	<option value="">-请选择-</option>
              	<option value="1">个人角色</option>
              	<option value="2">用户组角色</option>
              </select>
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
            <label class="control-label"><s>*</s>角色名</label>
            <div class="controls">
              <input name="roleName" data-rules="{required:true,minlength:2,maxlength:50}" class="input-normal control-text" type="text"/>
            </div>
          </div>
        </div>
      </form>
    </div>
    
    <div id="delDialog" style="display: none;">
		<span class="require-red">删除后,以下成员将失去该角色及对应的功能.确定删除吗?</span>
    	<div id="delGrid"></div>		
	</div>
  
<script type="text/javascript">
$(function(){
	var TYPE_ADD = 'add', TYPE_EDIT = 'update',TYPE_REMOVE = 'remove';
	var listUrl = ctx + 'listRRole.do'; // 查询
	var addUrl = ctx + 'addRRole.do'; // 添加
	var delUrl = ctx + 'delRRole.do';
	var listRoleRelationInfoUrl = ctx + 'listRoleRelationInfo.do';

	var ROLE_TYPE = {'1':'个人角色','2':'用户组角色'}
	
	var Grid = BUI.Grid;
	var Store = BUI.Data.Store;
	var Form = BUI.Form;
	
	var grid; // 表格
	var store; // 数据
	var schForm; // 查询form
	var editing;// 编辑表单
	var delGrid,delStore;
	var delDialog;
	
	
	function init() {
		initSearchForm();
		initData();
		initGrid();
		initEditing();
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
	
	function initData() {
    	store = new Store({
    		url : listUrl,
          	autoLoad:true,
    		totalProperty:'total',
          	pageSize:10,  // 配置分页数目
          	proxy : {
	           method:'POST'
	           ,save : { //也可以是一个字符串，那么增删改，都会往那么路径提交数据，同时附加参数saveType
	                addUrl : addUrl
	            }
          	}
    		
        });
    }
	
	
	function initGrid() {
    	var columns = [
			{title:'角色名',dataIndex:'roleName',sortable:false}
            ,{title:'角色类型',dataIndex:'roleType',sortable:false,renderer:function(val,obj){
            	return ROLE_TYPE[val] || '其它类型';
                }}
            ,{title:'操作',dataIndex:'',sortable:false,renderer : function(value,obj){
       	   		var updateStr = '<span class="grid-command btn-setRight">设置权限</span>';
              	var delStr = '<span class="grid-command btn-del">删除</span>';
              	if(obj.isAdmin) {
              		delStr = '';
              	}
              	return RightUtil.auth('grid_setRight',updateStr,'') 
              		+ RightUtil.auth('grid_del',delStr,'') ;
            }}
        ];    	
    	
    	grid = new Grid.Grid({
          	render:'#grid'
          	,columns : columns
          	,width:800
          	,loadMask: true
          	,store: store
          	,forceFit: true
          	,emptyDataTpl : '<div class="centered"><h2>查询的数据不存在</h2></div>'
          	,tbar : {
              items : [
				RightUtil.auth('btn_add',{text : '<i class="icon-plus"></i>新增角色',btnCls : 'button button-small',handler:add})
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
	
	function initEditing() {
    	editing = new BUI.Editor.DialogEditor({
			contentId:'content'
			,title:'新增角色'
			,form:{srcNode:$('#J_Form')}
    		,success:function() {
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
	
	function initDelDialog() {
		if(!delDialog) {
			
			delBtn = {text:'删除',elCls : 'button button-primary'}
			
			delDialog = new BUI.Overlay.Dialog({
				contentId:'delDialog'
			    ,mask:true
				,center:true
				,zIndex: 500
				,width:400
				,height:400
				,buttons:[
					delBtn
	              ,{text:'取消',elCls : 'button',handler : function(){
	                	this.close();
	               }}
				]
			});
		}
	}
	
	function initDelGrid() {
		if(!delGrid){
			
			delStore = new Store();
			
			delGrid = new Grid.Grid({
				render:'#delGrid'
				,store:delStore
				,height:300
				,columns:[
					{title:"用户名",dataIndex:"username",width:200}
				]
			});
			
			delGrid.render();
		}
	}
	
	
	function initEvent() {
	    //监听事件，删除一条记录
	    grid.on('cellclick',
	    function(ev) {
	        var sender = $(ev.domTarget); //点击的Dom
	        if (sender.hasClass('btn-setRight')) {
	        	var record = ev.record;
	        	SelectRightWin.show(record.roleId,function(){
	        		store.load();
	        	});
	        }
	        if (sender.hasClass('btn-del')) {
	        	var record = ev.record;
            	del(record);
	        }
	    });

	    $('#schBtn').on('click',
	    function(ev) {
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
	
	function add(){
        editing.set('editType',TYPE_ADD);
        showEditor({});
    }
    
    function edit(record) {
    	editing.set('editType',TYPE_EDIT);
    	showEditor(record);
    }
    
    function showEditor(record) {
		editing.set('record',record);
		editing.show();
		editing.setValue(record,true); //设置值，并且隐藏错误
    }
    
    function save() {
    	editing.valid();
    	if(editing.isValid()) {
    		var editType = editing.get('editType');
    		var form = editing.get('form');
    		var curRecord = editing.get('record'); // 表格中的数据
    		var record = form.toObject(); // 表单数据
    		
    		BUI.mix(curRecord,record); // 将record赋值到curRecord中
    		store.save(editType,curRecord,function(){
    			store.load();
    			editing.close();
    		})
    	}
    }
    
    function del(row) {
    	if (row){
    		var userRoles = listRoleUser(row);

    		if(userRoles.length > 0){
    			initDelGrid();
    			
    			delStore.setResult(userRoles);
    			
    			initDelDialog();
    			
    			var title = '删除[<span style="color:red;">'+row.roleName+'</span>]角色';
    			delDialog.set('title',title);
    			
    			delBtn.handler = function(){
    				doDel(row);
    			}
    			
    			
    			delDialog.show();
    		}else{
    			BUI.Message.Confirm('确定删除角色['+ row.roleName +']吗?',function(){
    				doDel(row);
    			},'question');
    		}
    	}
    }

    function doDel(delRow){
    	if(delRow){
    		Action.post(delUrl,delRow,function(result){
    			Action.execResult(result,function(){
    				BUI.Message.Alert('删除成功',function(){
    					if(delDialog){
	    					delDialog.close();
    					}
    					store.load();
    				});
    			});
    		});
    	}
    }
    
    function listRoleUser(row){
    	var ret = [];
    	
    	Action.postSync(listRoleRelationInfoUrl
    			,{roleId:row.roleId},function(result){
    		if(result.rows.length > 0){
    			ret = result.rows;
    		}
    	});
    	
    	return ret;
    }
	
	init();
});
</script>
</rms:role>
</body>
</html>
