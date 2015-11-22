var TreeDemo = {  
	init : function() {
		TreeDemo.treeId="-1";
		TreeDemo.loadTree();
	},
	
	/**加载树*/ 
	loadTree:function(){
		   //非根，非叶子节点  不可点击
            // var $nodes=$.grep($("#locationTree .ico-file"),function(e,i){
			     // return i>0;
			// });
			// $.each($nodes, function(i,e) {
			    // $(e).siblings('span').attr('clickable',false);
                // $(e).siblings('span').css('cursor','default');
			// });
// 		
		   TreeDemo.expandAllNoneLeafNodes();
		   TreeDemo.treedemoIns = new Tree({
					id : "treedemo",
//					url : "/webstone/treedemo/getChildNode",
//					param : "nodeId=",
					listeners : {
						nodeClick : function(node) {
							 if(node.getId()=='-1'){
							 	TreeDemo.treeId=node.getId();
							 	TreeDemo.loadPage('/webstone/treedemo/openNode','layout_right');
							 }else{
							 	TreeDemo.treeId=node.getId();
	    	                    TreeDemo.loadPage('/webstone/treedemo/openNode?nodeId='+node.getId(),'layout_right');
							 }
						}
					}
				});	
		     var firstChild = TreeDemo.treeId ? TreeDemo.treedemoIns.getNodeById(TreeDemo.treeId) : TreeDemo.treedemoIns.getRoot().getFirstChild();
		     if(firstChild){
		         firstChild.expend();
		         firstChild.setCurrentNode();
		         TreeDemo.treeId=firstChild.getId();
		     }
		     
		     new window.DragMoveTree({
		         target : window.locationTree,
		         tree : TreeDemo.treedemoIns,
		         listeners : {
		             accept : function(item_, insertion_) {
	            		 if (insertion_.item.parent().parent().attr("nodeId") == item_.parent().parent().attr("nodeId") && !insertion_.asChild) return true;
		             },
		             move : function(item_, oldParentItem_) {
		            	 var ids = [item_.attr("nodeId")];
		            	 
		            	 item_.nextAll().each(function(i) {
		            		 ids.push($(this).attr("nodeId"));
		            	 });
		            	 
		            	 PageCtrl.ajax({
			          		   url : "/webstone/treedemo/updateOrder?",
			          		   data : "nodeIds=" + ids,
			          		   type : "post",
			          		   dataType : "text",
			          		   success : function(data) {
			          			   if (data == "success") alert("操作成功！");
			          			   else alert("操作失败！");
			          		   }
			          	 });
		             }
		         }
		     });
		},
	/**打开所有的非叶子节点*/
	expandAllNoneLeafNodes:function(){
		
      var $nodes=$("#locationTree .expandable").not('[nodeid="-1"]');
	  $.each($nodes, function($i, $e) {
	     $e.setAttribute('class','collapsable');
	     var $children=$e.children;
	
	     if($children.length==4){
	         $children[0].setAttribute('class','hitarea collapsable-hitarea');
	         $children[1].setAttribute('class','ico-file-leaf');
	         $($children[3]).show();
	     }
	  
	 }); 
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
TreeDemo.init();