var AsynTreeDemo = {  
	init : function() {
		AsynTreeDemo.treeId="1";
		AsynTreeDemo.loadTree();
	},
	
	/**加载树*/ 
	loadTree:function(){
		
		   AsynTreeDemo.asynTree = new Tree({
					id : "asyntreedemo",
					url : "/webstone/asyntreedemo/getChildNode",
					param : "nodeId=",
					isExpand : true,
					listeners : {
						nodeClick : function(node) {
							 if(node.getId()=='-1'){
							 	AsynTreeDemo.treeId=node.getId();
							 	AsynTreeDemo.loadPage('/webstone/AsynTreeDemo/openNode','layout_right');
							 }else{
							 	AsynTreeDemo.treeId=node.getId();
	    	                    AsynTreeDemo.loadPage('/webstone/AsynTreeDemo/openNode?nodeId='+node.getId(),'layout_right');
							 }
						}
					}
				});	
		 
		     var firstChild = AsynTreeDemo.treeId ? AsynTreeDemo.asynTree.getNodeById(AsynTreeDemo.treeId) : AsynTreeDemo.asynTree.getRoot().getFirstChild();
		     if(firstChild){
		         firstChild.expend();
		         firstChild.setCurrentNode();
		         AsynTreeDemo.treeId=firstChild.getId();
		     }
		},
	loadPage:function(url,domId){
		Loading.start();
    	PageCtrl.load({
			url:url,
			dom:domId,
			callback:function(){
			  Loading.stop();
			}
		});
	}

		
};
AsynTreeDemo.init();