/**
 * 权限设置窗口
 * SelectRightWin.show(record.roleId,function(){
		store.load();
	});
 */
var SelectRightWin = (function(){
	
	var listTopMenuUrl = ctx + 'listTopMenu.do'
	var listMenuUrl = ctx + 'listMenuByTabId.do';
	var listRolePermissionByRoleIdUrl = ctx + 'listRolePermissionByRoleId.do';
	var addRolePermissionUrl = ctx + 'addRolePermission.do';
	var delRolePermissionUrl = ctx + 'delRolePermission.do';
	
	var inited = false;
	var tab = null;
	var win = null;
	var tree = null;
	var treeStore;
	var grid = null;
	var gridStore;
	
	var curRoleId,curCallback;
	var curSfIds;
	
	var html = [
		'<div id="rightSelectDialog" class="hide">'
			,'<div id="srw_tab"></div>'
			,'<div class="tab-body">'
				,'<table>' 
					,'<tr>' 
						,'<td id="srw_treeMenu" valign="top"></td>' 
					,'<tr>' 
				,'</table>' 
			,'</div>'
		,'</div>'
	];
	
	function init(roleId,callback) {
		curCallback = callback;
		
		var sfIds = getData(roleId);
		
		setValue(roleId,sfIds);
		
		if(!inited) {
			inited = true;
			
			initPanel();
		}
	}
	
	function getData(roleId) {
		var sfIds = [];
		Action.postSync(listRolePermissionByRoleIdUrl,{roleId:roleId},function(sysFuns) {
			for(var i=0,len=sysFuns.length;i<len;i++){
				sfIds.push(sysFuns[i].sfId);	
			}
		});
		return sfIds;
	}
	
	function initPanel() {
		var panel = html.join('');
		$('body').append(panel);
		
		initTab();
		
		initTree();
				
		initDialog();
	}
	
	function initTab() {
		var store = new BUI.Data.Store({url:listTopMenuUrl,autoLoad:true});
		store.on('beforeprocessload',function(e){
			var rows = e.data;
			var items = [];
			for(var i=0; i<rows.length; i++) {
				var row = rows[i];
				items.push({text:row.tabName,value:row.id});
			}
			
			tab = new BUI.Tab.TabPanel({
	            render : '#srw_tab'
		        ,elCls : 'nav-tabs'
		        ,autoRender: true
				,children:items			       
	        });
	        
	        tab.on('itemselected',function(e){
	        	var item = e.item;
	        	treeStore.load({tabId:item.get('value')});
	        });
	        
	        tab.setSelected(tab.getFirstItem());
		});
	}
	
	function initDialog() {
		
		win = new BUI.Overlay.Dialog({
			contentId:'rightSelectDialog'
			,title:'设置权限'
			,width:850
			,height:490
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
		
		treeStore = new BUI.Data.TreeStore({
	        //返回的数据如果数据有children字段，且children.length == 0 ，则认为存在未加载的子节点
	        //leaf = false，没有children字段也会认为子节点未加载，展开时会自动加载
	        url: listMenuUrl,
			map : {
		      'srId' : 'id'
		      ,'resName' : 'text'
		    }
		    ,showLine : true //显示连接线
	    });
	    
	    tree = new BUI.Tree.TreeList({
	        render: '#srw_treeMenu',
	        showLine: true,
	        width:750,
	        height: 350,
	        expandEvent : 'itemclick', //单击展开节点
	        collapseEvent : 'itemclick',  //单击收缩节点
	        itemTplRender:function(item){
	        	item.expanded = true; // 全部展开
	        	var checkboxes = buildSysFunChecked(item);
	        	if(checkboxes) {
	        		checkboxes = '&nbsp;&nbsp;[' +checkboxes + ']';
	        	}
	        	return '<li>' + item.text + checkboxes + '</li>';
	        },
	        store: treeStore
	    });	  
	    
	    tree.render();
	    
	    // 隐藏tree外部边框线
	    $('#srw_treeMenu').find('.bui-tree-list').css('border','0px');
	    
	    // checkbox点击事件
	    $('#srw_treeMenu').find('li').find('.sys_cbx').live('click',function(){
	    	var sfId = this.value;
	    	if(this.checked) {
	    		addRolePermission(sfId);
	    	}else{
	    		delRolePermission(sfId);
	    	}
	    });
	}
	
	
	// 构建checkbox
	function buildSysFunChecked(item) {
		var sysFuns = item.sysFuns || [];
		var html = [];
		
		for(var i=0; i<sysFuns.length; i++) {
			var sysFun = sysFuns[i];
			var selected = isSelected(sysFun.sfId);
			var checkedStr = selected ? ' checked="checked" ' : '';
			html.push('<label style="margin:0 3px;vertical-align:top;display:inline-block;">' +
					'<input style="vertical-align:middle;" ' +
					'class="sys_cbx" ' +
					'type="checkbox" ' +
					'value="'+sysFun.sfId+'" ' +
					checkedStr + '/>'+sysFun.operateName+'</label>');
		}
		
		return html.join('');
	}
	
	function isSelected(sfId) {
		var ret = false;
		for(var j=0; j<curSfIds.length; j++) {
			if(sfId == curSfIds[j]) {
				ret = true;
				break;
			}
		}
		return ret;
	}
		
	function addRolePermission(sfId) {
		setRolePermission({
			url:addRolePermissionUrl
			,sfId:sfId
			,roleId:curRoleId
		});
	}
	
	function delRolePermission(sfId) {
		setRolePermission({
			url:delRolePermissionUrl
			,sfId:sfId
			,roleId:curRoleId
		});
	}
	
	function setRolePermission(param) {
		var url = param.url;
		
		var data = {
			sfId:param.sfId
			,roleId:param.roleId
		}
		
		Action.post(url,data,function(e){
			Action.execResult(e);
		});
	}
	
	function reset() {		
		if(tab) {
			tab.clearSelected();
			tab.setSelected(tab.getFirstItem());
		}
	}
	
	function setValue(roleId,sfIds) {
		curRoleId = roleId;
		curSfIds = sfIds;
	}
	
	return {
		show:function(roleId,callback) {
			init(roleId,callback);
			
			reset();
			
			win.show();
		}
		,hide:function() {
			win.hide();
		}
	};
	
	
})();
