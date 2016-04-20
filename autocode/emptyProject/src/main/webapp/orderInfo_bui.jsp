<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp" %>
<c:set var="bui" value="${res}bui/"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
 <head>
  <title>后台管理</title>
   <script type="text/javascript">var ctx = '${ctx}';</script>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="${bui}css/dpl-min.css" rel="stylesheet" type="text/css" />
   <link href="${bui}css/bui-min.css" rel="stylesheet" type="text/css" />
   <link href="${bui}css/page-min.css" rel="stylesheet" type="text/css" />
 </head>
<body>
  
  <div class="container">
    <div class="row">
      <form id="searchForm" class="form-horizontal span32">
        <div class="row">
          <div class="control-group span8">
            <label class="control-label">订单号：</label>
            <div class="controls">
              <input type="text" class="control-text" name="orderIdSch">
            </div>
          </div>
          <div class="control-group span8">
            <label class="control-label">手机号：</label>
            <div class="controls">
              <input type="text" class="control-text" name="mobileSch">
            </div>
          </div>
          <div class="control-group span8">
            <label class="control-label">地址：</label>
           <div class="controls">
              <input type="text" class="control-text" name="addressSch">
            </div>
          </div>
          <div class="control-group span8">
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
            <label class="control-label"><s>*</s>手机号</label>
            <div class="controls">
              <input name="mobile" type="text" data-rules="{required:true}" class="input-normal control-text">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="control-group span8">
            <label class="control-label"><s>*</s>城市名</label>
            <div class="controls">
              <input name="cityName" data-rules="{required:true}" type="text" class="input-normal control-text">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="control-group span15">
            <label class="control-label">地址</label>
            <div class="controls control-row4">
              <textarea name="address" class="input-large" type="text"></textarea>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="control-group span15">
            <label class="control-label"><s>*</s>下单时间</label>
            <div class="controls">
              <input name="createDate" data-rules="{required:true}" class="calendar" type="text"/>
            </div>
          </div>
        </div>
      </form>
    </div>
  
  <script type="text/javascript" src="${bui}js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${bui}js/bui-min.js"></script>
  <script type="text/javascript" src="${res}js/common.js"></script>
 
<script type="text/javascript">
$(function(){
	var TYPE_ADD = 'add', TYPE_EDIT = 'update',TYPE_REMOVE = 'remove';
	
	var listUrl = ctx + 'listBuiOrderInfo.do'; // 查询
	var addUrl = ctx + 'addBuiOrderInfo.do'; // 添加
	var updateUrl = ctx + 'updateBuiOrderInfo.do'; // 修改
	var delUrl = ctx + 'delBuiOrderInfo.do'; // 删除
	var exportUrl = ctx + 'exportOrderInfo.do'; // 导出
	var $schBtn = $('#schBtn'); // 查询按钮
	
	var Grid = BUI.Grid;
	var Store = BUI.Data.Store;
	var Form = BUI.Form;
	
	var grid; // 表格
	var store; // 数据
	var schForm; // 查询form
	var editing;// 编辑表单

    function init() {
    	initForm();
    	initData();
    	initEditing();
    	initGrid();
    	initEvent();
    }
    
    function initForm() {
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
	            save : { //也可以是一个字符串，那么增删改，都会往那么路径提交数据，同时附加参数saveType
	                addUrl : addUrl,
	                updateUrl : updateUrl,
	                removeUrl : delUrl
	            }
    			,method:'POST'
          	}
    		
        });
    }
    
    function initEditing() {
    	editing = new BUI.Editor.DialogEditor({
			contentId:'content'
			,form:{srcNode:$('#J_Form')}
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
    	   {title:'订单编号',dataIndex:'orderId',width:'6%'}
          ,{title:'手机号',dataIndex:'mobile',width:'10%'}
          ,{title:'城市名',dataIndex:'cityName',width:'10%'}
          ,{title:'地址',dataIndex:'address',width:'30%'}
          ,{title:'下单时间',dataIndex:'createDate'}
          ,{title:'操作',dataIndex:'',width:200,renderer : function(value,obj){
       	   var updateStr = '<span class="grid-command btn-edit">修改</span>';
              var delStr = '<span class="grid-command btn-del">删除</span>';
              return updateStr + delStr;
            }}
        ];
    	
    	grid = new Grid.Grid({
          	render:'#grid'
          	,columns : columns
          	,loadMask: true
          	,store: store
          	,emptyDataTpl : '<div class="centered"><h2>查询的数据不存在</h2></div>'
          	,tbar : {
              items : [
                {text : '<i class="icon-plus"></i>新建',btnCls : 'button button-small',handler:add}
                ,{text : '<i class="icon-download"></i>导出',btnCls : 'button button-small',handler:exportFile}
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
        	if(sender.hasClass('btn-del')){
            	var record = ev.record;
            	del(record);
        	}
        	if(sender.hasClass('btn-edit')){
            	var record = ev.record;
            	edit(record);
        	}
        });
        
    	$schBtn.on('click',function(ev){
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
    
    function exportFile() {
    	var data = schForm.serializeToObject();
    	ExportUtil.doExport(exportUrl,data);
    }
    
    function save() {
    	editing.valid();
    	if(editing.isValid()) {
    		var editType = editing.get('editType');
    		var form = editing.get('form');
    		var curRecord = editing.get('record'); // 表格中的数据
    		var record = form.serializeToObject(); // 表单数据
    		
    		BUI.mix(curRecord,record); // 将record赋值到curRecord中
    		store.save(editType,curRecord,function(){
    			store.load();
    			editing.close();
    		})
    	}
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
	
    function del(record){
	    if(record){
	    	BUI.Message.Confirm('确认要删除选中的记录么？',function(){
	    		store.save(TYPE_REMOVE,record,function(){
	    			store.load();
	    		})
	       	},'question');
	    }
    }
    
    init();
	
});
</script>

<body>
</html>