/**
 * 角色选择窗口
 */
var SelectRoleWin = (function(){
	
	var ROLE_TYPE = {
		PERSON : 1
		,GROUP : 2
	}
	var listTreeRoleUrl = ctx + 'listTreeRole.do';
	var listPersonRoleUrl = ctx + 'listAllRRole.do?roleTypeSch=' + ROLE_TYPE.PERSON;
	
	
	var ROOT_ID = -1;
	
	var inited = false;
	var tab = null;
	var win = null;
	var tree = null;
	var grid = null;
	var gridStore;
	
	var curValues,curExtKey,curCallback;
	/**
	  {
		  selectHandler:function(item){}
		  ,unselectHandler:function(item){}
	  }
	 */
	var handlers;
	
	var html = [
		'<div id="roleSelectDialog" class="hide">'
			,'<div id="srw_tab"></div>'
			,'<div id="roleTabPanel" class="tab-body">'
				,'<div id="groupTabPannel">'
					,'<table>' 
						,'<tr>' 
							,'<td id="srw_treeRole" valign="top"></td>' 
							,'<td id="srw_gridRole" valign="top" style="padding-left:20px;"></td>' 
						,'<tr>' 
					,'</table>' 
				,'</div>'
				,'<div id="srw_personRole"></div>'
			,'</div>'
		,'</div>'
	];
	
	function init(roleIds,extKey,callback,_handlers) {
		curCallback = callback;
		handlers = _handlers;
		setValue(roleIds,extKey);
		
		if(!inited) {
			inited = true;
			
			initPanel();
		}else{
			var treeStore = tree.get('store');
			var data = treeStore.getResult();
			treeStore.reloadNode(data);
		}
		
	}
	
	function initPanel() {
		var panel = html.join('');
		$('body').append(panel);
		
		initTab();
		
		initTree();
		
		initGrid();
		
		initDialog();
		
		buildPersonRole();
	}
	
	function initTab() {
		tab = new BUI.Tab.TabPanel({
            render : '#srw_tab'
	        ,elCls : 'nav-tabs'
	        ,panelContainer : '#roleTabPanel'
	        ,autoRender: true
	        ,children:[
	          {title:'用户组角色',value:1,selected : true}
	          ,{title:'个人角色',value:2}
	        ]
        });
	}
	
	function initDialog() {
		
		win = new BUI.Overlay.Dialog({
			contentId:'roleSelectDialog'
			,title:'选择角色'
			,width:650
			,height:480
			,zIndex:20
			,mask:true  
			,buttons:[
				 { text:'关闭', elCls : 'button', handler : function(){ 
				 	this.close(); 
				 	} 
				 }
			]
		});
		
		win.on('closed',function(){
			curCallback();
		});
	}
	
	function initTree() {
		
		var store = new BUI.Data.TreeStore({
	        //返回的数据如果数据有children字段，且children.length == 0 ，则认为存在未加载的子节点
	        //leaf = false，没有children字段也会认为子节点未加载，展开时会自动加载
	        url: listTreeRoleUrl,
			map : {
		      'groupId' : 'id'
		      ,'groupName' : 'text'
		    }
		    ,showLine : true //显示连接线
	        ,autoLoad: true
	    });
	    
	    tree = new BUI.Tree.TreeList({
	        render: '#srw_treeRole',
	        showLine: true,
	        width:250,
	        height: 350,
	        expandEvent : 'itemclick', //单击展开节点
	        collapseEvent : 'itemclick',  //单击收缩节点
	        itemTplRender:function(item){
	        	var count = getRoleIdCount(item);
	        	var span = count > 0 
		        	? '<strong style="color:red;margin-left:5px;">'+count+'</strong>'
		        	: '';
		        	
	        	return '<li>' + item.text + span + '</li>'
	        },
	        store: store
	    });
	    
	    tree.on('selectedchange',function(ev){
	    	var node = ev.item;
	    	gridStore.setResult(node.roles);
	    });
	    
	    tree.render();
	}
	
	function initGrid() {
		var Grid = BUI.Grid;
		var columns = [
			{title : '角色',dataIndex :'roleName',sortable : false}
		];
		
		gridStore = new BUI.Data.Store();
		
		gridStore.on('beforeprocessload',function(e){
			var rows = e.data;
			for(var i=0;i<rows.length;i++) {
				var row = rows[i];
				var roleId = row.roleId;
				for(var j=0; j<curValues.length; j++) {
					row.selected = false;
					if(roleId == curValues[j]){
						row.selected = true;
						break;
					}
				}
			}
			return rows;
		});
		
		grid = new Grid.Grid({
			render:'#srw_gridRole',
	         idField : 'roleId',
	         columns : columns,
	         forceFit : true,
	         width:250,
	         height: 330,
	         emptyDataTpl : '<h4 class="centered">无角色</h4>',
	         store: gridStore,
	         itemStatusFields : {
			 	selected : 'selected',
				disabled : 'disabled'
			 },
	         plugins : [Grid.Plugins.CheckSelection]
        });
        
        grid.on('itemselected',function(e){
        	var item = e.item;
        	if(item){
        		initParam(item);
        		handlers.selectHandler(item);
        	}
        });
        grid.on('itemunselected',function(e){
        	var item = e.item;
        	if(item){
        		initParam(item);
        		handlers.unselectHandler(item);
        	}
        });
        
      	grid.render();
	}
	
	function initParam(item) {
		item.extKey = curExtKey;
        item.roleType = ROLE_TYPE.GROUP;
        //item.groupId = tree.getSelected();
        var treeSelected = tree.getSelected();
        var groupId = treeSelected.record.groupId;
        item.groupId = groupId;
        return item;
	}
	
	function getRoleIdCount(item) {
		var count = 0;
    	var roles = item.roles || [];
    	for(var i=0; i<roles.length; i++) {
    		var roleId = roles[i].roleId
    		for(var j=0; j<curValues.length; j++) {
    			if(roleId == curValues[j]) {
    				count++;
    			}
    		}
    		
    	}
    	
    	var children = item.children || [];
    	for(var i=0; i<children.length; i++) {
    		var child = children[i];
    		count +=getRoleIdCount(child);
    	}
    	
    	
    	return count;
	}
	
	
	function buildPersonRole() {
		
		Action.postSync(listPersonRoleUrl,{},function(data){
			var roles = data.rows;
			var html = [];
			var role = null;
			for(var i=0,len=roles.length;i<len;i++){
				role = roles[i];
				if(i > 0) {
					html.push('&nbsp;&nbsp;');
				}
				html.push('<label>')
				html.push('<input name="roleId" style="vertical-align:middle;" type="checkbox" value="'+role.roleId+'" />'+role.roleName);
				html.push('</label>');
			}
			
			var checkboxHtml = html.join('');
			$('#srw_personRole').html(checkboxHtml);
			
			$('#srw_personRole input').on('click',function(){
				var roleId = this.value;
				var roleName = $(this).text();
				var item = {roleId:roleId,roleName:roleName,extKey:curExtKey};
				item.roleType = ROLE_TYPE.PERSON;
				if(this.checked) {
					handlers.selectHandler(item);
				}else{
					handlers.unselectHandler(item);
				}
			});
			
		})
	}
	
	function reset() {
		gridStore.setResult([]);
		tree.clearSelected();
		tree.collapseAll();
		
		// 展开根节点
		var node = tree.findNode(ROOT_ID);
		tree.expandNode(node);
		
		tab.setSelectedByField('value',1);
	}
	
	function setValue(roleIds,extKey) {
		curValues = roleIds;
		curExtKey = extKey;
	}
	
	function setPersonRoleValue(roleIds) {
		$('#srw_personRole')
				.find('input[name=roleId]')
				.attr('checked',false)
				.val(roleIds);
	}
	
	function hideGroupTab() {
		var item = tab.getFirstItem();
		item.get('el').hide();
		var personItem = tab.getItemAt(1); 
		tab.setSelected(personItem);
		tree.pauseEvent('selectedchange');
	}
	
	function showGroupTab() {
		var item = tab.getFirstItem();
		item.get('el').show();
		tab.setSelected(item);
		tree.resumeEvent('selectedchange');
	}
	
	return {
		show:function(roleIds,extKey,callback,handlers) {
			init(roleIds,extKey,callback,handlers);
			reset();
			setPersonRoleValue(roleIds);
			win.show();
			showGroupTab();
		}
		,showPerson:function(roleIds,extKey,callback,handlers) {
			this.show(roleIds,extKey,callback,handlers);
			hideGroupTab();
		}
		,hide:function() {
			win.hide();
		}
	};
	
	
})();
