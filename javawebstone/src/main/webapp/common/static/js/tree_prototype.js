
Tree.prototype={
        /**
         * 根据传入的层级展开节点
         * @method expendByHierarchy
         * @param hierarchy {Array} 层级，每个元素标识该层级元素展开的索引
         *                            从根节点开始
         * */
        expendByHierarchy:function(hierarchy){
            var root = this.getRoot();
            for(var i=0,len = hierarchy.length; i < len ;i++){
                var index = hierarchy[i];
                if(!root){
                    root = this.getRoot();
                }else{
                    root = root.children()[index];
                }
                root.expend();
            }
        },
        /**
         * 获得树中左右选中节点信息
         * @method getCheckedHierarchy
         * @param attrKeys {Array}
         * @return Array[{text:节点文本，id：节点ID,childs：子节点数组}]
         * */
        getCheckedHierarchy:function(attrKeys){
            var root = this.getRoot();
            var hierarchy = [];
            
            round(root,hierarchy);
            
            function round(node,childArray){
                var childrens = node.children();
                if(childrens != null){
                    var child = null;
                    var nodeObj = null;
                    for(var  i=0,len = childrens.length;i<len;i++){
                        child = childrens[i];
                        if(child.isChecked()){
                            nodeObj = {};
                            nodeObj.text = child.getText();
                            nodeObj.id = child.getId();
                            nodeObj.parentId = child.parent().getId();
                            nodeObj.childs = [];
                            nodeObj.isLeaf = child.isLeaf();
                            nodeObj.img = child.getImage();
                            if(attrKeys){
                                nodeObj.attrs = {};
                                for(var keyi = 0;keyi<attrKeys.length;keyi++){
                                    nodeObj.attrs[attrKeys[keyi]] = child.getValue(attrKeys[keyi]);
                                }
                            }
                            
                            childArray.push(nodeObj);
                            round(child,nodeObj.childs);
                        }
                    }
                }
            }
            
            return hierarchy;
        },    
        /**
         * 获得树中所有叶子节点
         * @method getLeafNodes
         * @return {Node}
         * */
        getLeafNodes:function(){
            var node = this.getRoot();
            var children = null;
            var leafChild = [];
            round(node);
            
            function round(node){
                var children = node.children();
                var child = null;
                for(var i=0,len = children.length;i<len;i++){
                    child = children[i];
                    if(child.isLeaf()){
                        leafChild.push(child);
                    }
                    round(child);
                }
            }
            return leafChild;
    },        
    /*
     * 绑定折叠按钮事件
     */        
    bindFoldBtn:function(path){
        var self = this;
        if($.isString(path)){
            var btns = $(path).find("div.hitarea");
        }else{
            var btns = path;
        }
        if(btns.attr("binded")){
            return;
        }
        
        btns.bind("click",function(event){
        	//event.stopPropagation();
            var $btn = $(this);
            self.node.setNode($btn.parent());
            self.node.fold();
        }).attr("binded","true");                
    },
    unbindFoldBtn:function(path){
        var self = this;
        if($.isString(path)){
            var btns = $(path).find("div.hitarea");
        }else{
            var btns = path;
        }
        if(!btns.attr("binded")){
            return;
        }
        
        btns.unbind("click").removeAttr("binded");
    },
    /*
     * 绑定节点点击事件
     */
    bindNodeClick:function(path,listeners){
        if(listeners.nodeClick){
            var self = this;
            if($.isString(path)){
                var texts = $(path+" span[type='text'][clickable='true']");
            }else{
                var texts = path;
            }
            
            if(texts.attr("binded")=="true"){
                return;
            }
            
            texts.bind("click",function(event){
            	//event.stopPropagation();
                $text = $(this);
                self.clearCurrentNode();
                self.node.setNode($text.parent());
                self.node.click(listeners);
            }).attr("binded","true");
        }
    },
    /*
     * 绑定复选框事件
     */
    bindCheckbox:function(path,listeners){  //checkboxClickBefore checkboxClick  checkboxClickAfter
        if($.isString(path)){
            var checkboxs = $(path+" input[type='checkbox']");
        }else{
            var checkboxs = path;
        }
    
        var self = this;
        var _setcheckboxbefor=true;
        if(listeners.checkboxClickBefore){
            checkboxs.bind("mousedown",function(){
                var $checkbox = $(this);
                self.node.setNode($checkbox.parent());
                _setcheckboxbefor = listeners.checkboxClickBefore(self.node);
                $checkbox.data("checkBeforeFlag", _setcheckboxbefor).focus();
                if (!_setcheckboxbefor) return false;
            });
        }


        checkboxs.bind("change",function(){
            var $checkbox = $(this);
            if($checkbox.data("checkBeforeFlag") !== false){
                var node = self.node._createNode($checkbox.parent());                
                var flag =     node.checkboxClick();
                if(flag===false) return flag;
                if(listeners.checkboxClick){
                    flag = listeners.checkboxClick(node);
                    if(flag===false) return flag;
                }
            }
        });
    },
    bindToolclick:function(path,listeners){
        if(listeners.toolClick){
            
            if($.isString(path)){
                var tools = $(path+" span[type='tool']");
            }else{
                var tools = path;
            }
            var self = this;
            if(tools.attr("binded")=="true"){
                return;
            }
            
            
            tools.bind("click",function(event){
            	//event.stopPropagation();
                var $tool = $(this);
                self.node.setNode($tool.parent());
                listeners.toolClick(self.node,event);
            }).attr("binded","true");
        }
    },
    /**
     * 获得当前选中的节点
     * @method getCurrentNode
     * @return {Node}
     * */
    getCurrentNode:function(){
        if(!this.currentNode){
            var $text = this.root.find("span[type='text'][isCurrent='true']");
            if($text[0]){
                this.currentNode = $text.parent();
            }
        }
        if(this.currentNode!=null){            
            var node = this.node._createNode(this.currentNode);
            return node;
        }else{
            return null;
        }
    },    
    /**
     * 设置当前选中节点
     * @method setCurrentNode
     * @param nodeId {String} 节点ID
     * @chainable
     * */
    setCurrentNode:function(nodeId){  //设置树当前选中节点
        this.clearCurrentNode();
        var node = this.getNodeById(nodeId);
        node.setCurrentNode();
        return this;
    },
    /**
     * 清除当前选中节点
     * @method clearCurrentNode
     * @chainable
     * */
    clearCurrentNode:function(){  //清除当前选定
        var currentnode = this.getCurrentNode();
        if(currentnode) currentnode.clearCurrentNode();
        return this;
    },
    /**
     * 获得搜索定位节点
     * @method getCurrentNode
     * @return {Node}
     * */
    getSearchNode:function(){
        if(!this.searchnode){
            var $text = this.root.find("span[type='text'][isSearch='true']");
            if($text[0]){
                this.searchnode = $text.parent();
            }
        }
        if(this.searchnode!=null){            
            this.node.setNode(this.searchnode);
            return this.node;
        }else{
            return null;
        }
    },
    /**
     * 清除当前搜索定位节点
     * @method clearSearchNode
     * @chainable
     * */
    clearSearchNode:function(){  //清除当前搜索定位节点
        var searchnode = this.getSearchNode();
        if(searchnode) searchnode.clearSearchNode();
        return this;
    },
    /**
     * 根据节点ID获得节点对象
     * @method getNodeById
     * @param nodeId {String}
     * @return {Node}
     * */
    getNodeById:function(nodeId){  //根据ID所有节点
        var $li = this.root.find("li[nodeid='"+nodeId+"']");
        if(!$li[0]) return null;
        this.node.setNode($li);
        return this.node;
    },
    
    /**
     * 根据节点属性获得节点对象
     * @method getNodeById
     * @param attrName {String} 节点属性名称
     * @param attrVal {String} 节点属性值
     * @return {Node}
     * */
    getNodeByAttr:function(attrName,attrVal){//根据自定义属性查找节点
        var $li = this.root.find("li["+attrName+"='"+attrVal+"']");
        if(!$li[0]) return null;
        this.node.setNode($li);
        return this.node;        
    },
    /**
     * 更改制定节点文本
     * @method udpataNodeTextById
     * @param nodeId {String} 节点ID
     * @param text {String} 节点文本
     * */
    updateNodeTextById:function(nodeId,text){
        var node = this.getNodeById(nodeId);
        return node.setText(text);
    },
    /**
     * 获得单选选中的节点
     * @method getRadioCheckedNode
     * @return {Node}
     * */
    getRadioCheckedNode:function(){
        var $radio = this.root.find("input[type='radio']");
        for(var i=0;i<$radio.length;i++){
            if($radio[i].checked){
                var $li = $($radio[i]).parent();
                this.node.setNode($li);
                return this.node;
            }
        }
        return null;
    },
    /**
     * 搜索节点
     * @method searchNode
     * @param nodeId {String} 节点ID
     * */
    searchNode:function(nodeId){
        var node = this.getNodeById(nodeId);
        var contentTop;
        var nodeTop;
        if(node){
            node.expandUP();
            node.setSearchNode();
            contentTop = node.$li.parents('.tree-wrap:first').position().top;
            nodeTop = node.$li.position().top;
            this.scrollToPos(nodeTop - contentTop);
        }
    },
    /**
     * 定位节点
     * @method locationNode
     * @param nodepath {Array} 节点ID集合
     * */
    locationNode:function(nodepath){
        for(var i=0,len = nodepath.length-1;i<len;i++){
            var node = this.getNodeById(nodepath[i]);
            node.expend(false);
        }
        this.searchNode(nodepath[nodepath.length-1]);
    },
    /**
     * 获得选中节点个数
     * @method getCheckedCount
     * @return {Number} 
     * */
    getCheckedCount:function(){
        var checkboxs = $("#"+this.id+" input[type='checkbox']");
        var checkboxeds = checkboxs.filter(":checked");
        return checkboxeds.length;
    },
    /**
     * 指定id节点复选框取消
     * @method clearCheckedByNodeid
     * @param nodeId {String} 节点ID
     */
    clearCheckedByNodeid:function(nodeId){
        var node = this.getNodeById(nodeId);
        if(node){
            node.clearChecked();
        }
    },
    /**
     * 清除所有选中节点
     * @method clearAllNodeChecked
     * */
    clearAllNodeChecked:function(){
        var checkboxs = $("#"+this.id+" input[type='checkbox']:checked").removeAttr("checked");
    },
    /**
     * 设置某个节点选中
     * @method setCheckedByNodeid
     * @param nodeId {String} 节点ID
     */    
    setCheckedByNodeid:function(nodeId, isRelate){
        var node = this.getNodeById(nodeId);
        if(node){
            node.setChecked(isRelate);
        }
    },
    /**
     * 批量设置节点复选中选中
     * @method setCheckedByNodeIds
     * @param {Array} 节点ID数组
     * @chainable
     */
    setCheckedByNodeIds:function(nodeIds){
        var $li = null, $ul = null;;
        for(var i=0,len  = nodeIds.length;i<len;i++){
            $li = this.$tree.find('li[nodeid="' + nodeIds[i] + '"]>input:checkbox').attr("checked","checked");
        }
        var root = this.getRoot();
        function roundCheck(childrens){
            var $li = null, $ul = null;
            for(var i = 0, len = childrens.length; i < len; i++){
                $li = $(childrens[i]);
                $ul = $li.parent();
                if($ul.attr("isCheckedCount")){
                    continue;
                }
                var childs = $ul.children();
                /*
                var checkedCount = 0;
                console.info();
                for(var j = 0, jlen = childs.length; j < jlen; j++){
                    if($(childs[j]).children("input:checkbox:checked")[0]){                        
                        checkedCount++;
                    }
                }*/
                if($ul.find(">li>input:checkbox:checked").length != 0){
                    $ul.attr("isCheckedCount", true);
                    $ul.prevAll("input:checkbox").attr("checked","checked");
                }
                var $parent = $ul.parent().parent().parent();//ul-li-ul-li
                if($parent.attr("nodeid") != "root"){
                    roundCheck($parent.children("ul").children());    
                }                
            }
        }
        var checked = this.$tree.find("input:checkbox:checked");        
        roundCheck(checked.parent());        
    },
    /**批量展开*/
    expendByIds : function(nodeIds){
        var $li = null;
        for(var i = 0, len = nodeIds.length; i < len; i++){
            $li = this.$tree.find('li[nodeid="' + nodeIds[i] + '"]');
            if($li.hasClass("expandable")){
                $li[0].className = "collapsable";
                $li.children("div")
                $btn.removeClass("expandable-hitarea").addClass("collapsable-hitarea");
            }
            
            if($li.hasClass("lastExpandable")){
                $li.removeClass("lastExpandable").addClass("lastCollapsable");
                $btn.removeClass("lastExpandable-hitarea").addClass("lastCollapsable-hitarea");
            }                        
            if(this._get$Ico().hasClass(closeClassName)){
                this._get$Ico().removeClass(closeClassName).addClass(openClassName);    
            }
        }
        
    },
    /**
     * 设置树自定义参数
     * @method setParam
     * @chainable
     * @param param {JSON}
     * */
    setParam:function(param){
        this.param = param;
        return this;
    },
    /**
     * 定位到第一个选择的节点
     * @method scrollToFirstChecked
     */
    scrollToFirstChecked : function () {
        var aChecked = this.getCheckedHierarchy();
        var oNode = aChecked[0];
        var iTop;
        if (oNode) {
            while (!oNode.isLeaf) oNode = oNode.childs[0];
            iTop = this.$tree.find('li[nodeid="' + oNode.id + '"]').position().top;
        }else {
            oNode = this.getRadioCheckedNode();
            if (oNode) {
                iTop = oNode.$li.position().top;
            } 
        }
        if (null != iTop && undefined !== iTop) {
            this.scrollToPos(iTop);            
        }
    },
    
    scrollToPos : function (yPos) {
        if (this.$tree.parent().parent().hasClass('jspPane')) {
            // jsScrollPane方式
            var api = this.$tree.parent().parent().parent().parent().data('jsp');
            api.scrollToY(yPos);
        }else {
            // 普通方式
            this.$tree.parent().scrollTop(0);
            this.$tree.parent().scrollTop(yPos);
        }
    },
    /**
     * 获得根节点
     * @method getRoot
     * @return {Node}
     * */
    getRoot:function(){
        var $li = this.$tree.children("li");
        this.node.setNode($li);
        return this.node;
    },
    /**
     * 判断树是否为空
     * @method isEmpty
     * @return {Boolean}
     * */
    isEmpty:function(){
        return this.getRoot()._get$Childs()[0]==null ? true : false;
    }
};

var TreePlugins = {
    /*
     * 树节点只能展开一级节点，互斥
     * @param conf {
     *     listeners:{expend:展开事件}
     * }
     * */
    singleExpend:function(conf){
        var listeners = conf.listeners == null ? {} : conf.listeners;
        var self = this;
        var fn = listeners.expend;
        listeners.expend = function(node){
            var silblinguls = node.$li.siblings("li").children("ul:visible");
            if(silblinguls[0]){
                self.node.setNode(silblinguls.parent());
                self.node.collapse();
            }
            if(fn)    fn(node);
        };
    },
    /*
     * 选中一定数量复选框后，将其他复选框disabled,如果低于则恢复
     */
    disableCheckbox:function(conf){
        var selectcount = conf.selectcount;
        var listeners = conf.listeners == null ? {} : conf.listeners;
        var self = this;
        var fn = listeners.checkboxClick;
        listeners.checkboxClick = function(node){
            var checkbox = node.$li.children("input[type='checkbox']");
            if(checkbox.hasClass("disabled")){
                return false;
            }
            var checkboxs = $("#"+conf.id+" input[type='checkbox']");
            var checkboxeds = checkboxs.filter(":checked");
            
            var s = new Date();
            if(checkbox[0].checked){
                if(checkboxeds.length>=selectcount){
                    checkboxs.not(":checked").addClass("disabled");
                }else{
                }
            }else{
                checkboxs.not(":checked").removeClass("disabled");
            }
            if(fn)    fn(node);
        };
    },
    /*
     * 创建一个树节点
     * conf{
     *     isLast:是否为最后一个节点
     *  isLeaf：是否为叶子节点
     *  isExpend:默认是否展开
     *  attrs:自定义属性
     *  isBtn：是否有折叠按钮
     *  isCheckBox：是否有checkbox
     *  isRadio:是否有单选框
     *  isClick:是否可以点击
     *  toolCls：工具按钮样式按钮
     *  icoCls：节点图标样式
     *  nodeId：节点Id
     *  text:节点显示文本
     * }
     */
    createNode:function(conf){
        var isLast = conf.isLast,isLeaf = conf.isLeaf,isExpend = conf.isExpend;
        var cls = "";
        if(isLast && isExpend && !isLeaf){ 
            cls = "collapsable lastCollapsable";  
        }else if(isLast && !isExpend && !isLeaf){ 
            cls = "expandable lastExpandable";
        }else if(!isLast && !isExpend && !isLeaf){  //如果不是最后一个，并且是关闭的，不是叶子节点expandable
            cls = "expandable";
        }else if(!isLast && isExpend && !isLeaf){ //如果不是最后一个，并且是展开的，不是叶子节点collapsable
            cls = "collapsable";
        }else if(isLast && isLeaf){  //如果是最后一个，并且是叶子节点
            cls = "last";
        }
        var li = '<li class="'+cls+'" nodeid="'+conf.nodeId+'" ';
        if(conf.attrs){
            for(var attr in conf.attrs){
                li += attr+"='"+conf.attrs[attr]+"' ";
            }
        }
        li+=">";
        if(conf.isBtn){
            li += this.createButton(isLast,isLeaf,isExpend);
        }
        if(conf.isCheckBox === true){
            li += this.createInput("checkbox",conf.inputAttrs);
        }
        if(conf.icoCls){
            li += this.createIco(conf.icoCls);
        }
        if(conf.img){
            li += this.createImg(conf.img);
        }
        
        if(conf.isRadio === true){
            li += this.createInput("radio",conf.inputAttrs);
        }
        
        li += this.createText(conf.isClick,conf.text);

        if(conf.toolCls){
            li += this.createTool(conf.toolCls);
        }
        li+="</li>";
        return li;
    },
    createTool:function(toolCls){
        return '<span class="'+toolCls+'" type="tool"></span>';
    },
    createIco:function(icoCls){
        return '<span class="'+icoCls+'" type="ico"></span>';
    },
    createImg:function(src){
        return '<img src="'+src+'"/>';
    },
    createText:function(isClick,textVal){
        var pointer = isClick ? 'cursor: pointer;' :'';
        return '<span style="'+pointer+'"  type="text" clickable="'+isClick+'">'+textVal+'</span>';
    },
    createButton:function(isLast,isLeaf,isExpend){
        var cls = "";
        if(isLast && isExpend && !isLeaf){
            cls = "hitarea collapsable-hitarea lastCollapsable-hitarea";
        }else if(isLast && !isExpend && !isLeaf){
            cls = "hitarea expandable-hitarea lastExpandable-hitarea";
        }else if(!isLast && !isExpend && !isLeaf){
            cls="hitarea expandable-hitarea";
        }else if(!isLast && isExpend && !isLeaf){
            cls="hitarea  collapsable-hitarea";
        }else{
            cls="last";
        }
        if(isLeaf){
            cls += " placeholder";
        }
        return '<div class="'+cls+'"></div>';
    },
    createInput:function(type,attrs){
        var input =  '<input type="'+type+'" ';
        if(attrs){
            for(var i=0;i<attrs.length;i++){
                input += attr+"="+attrs[attr]+" ";
            }
        }
        input += "/>";
        return input;
    }
};


/*
 * 两根树左右通过checkbox选中左右移动功能
 * @param fromTree 移除节点的树对象
 * @param fromTreeNodes 移除节点集合，通过调用树的getCheckedHierarchy()方法获得
 * @param toTree 加入节点的树对象
 * 
 * */
function TreeRound(fromTree,fromTreeNodes,toTree,noChange){
    round(fromTreeNodes);
    fromTree.clearAllNodeChecked();
    function round(nodes){
        var node = null;
        for(var i=0,len = nodes.length;i<len;i++){
            node = nodes[i];
            var toTreeParentNode = null; 
            if(!toTree.getNodeById(node.id)){
                TreeRound.conf.nodeId = node.id;
                TreeRound.conf.text = node.text;    
                TreeRound.conf.attrs= node.attrs;
                TreeRound.conf.img = node.img;
                if(node.parentId == "root"){                            
                    toTree.getRoot().appendChild(TreeRound.conf);
                }else{
                    toTree.getNodeById(node.parentId).appendChild(TreeRound.conf);
                }
            }                    
            var fromNode = fromTree.getNodeById(node.id);
            if(fromNode.isLeaf()){
                var fromTreeParentNode = fromNode.parent();
                //fromNode.delNode();
                if(fromTreeParentNode.isLeaf()){
                    //fromTreeParentNode.delNode();
                }else{
                    if(i+1 == len){
                        var text = fromTreeParentNode.getText();
                        if(text){
                            //fromTreeParentNode.setText(text.replace(/\(.+\)/g, "("+fromTreeParentNode.childCount()+")"));
                        }
                    }                        
                }    
            }else{
                round(node.childs);
            }
            if(i+1 == len){
                if(!noChange && toTree.getNodeById(node.parentId)){
                    var text = toTree.getNodeById(node.parentId).getText();                            
                    toTree.getNodeById(node.parentId).setText(text.replace(/\(.+\)/g, "("+toTree.getNodeById(node.parentId).childCount()+")"));
                }
            }
        }
    }    
}


/*
 * 两根树左右通过checkbox选中左右移动功能，异步版本
 * @param fromTree 移除节点的树对象
 * @param fromTreeNodes 移除节点集合，通过调用树的getCheckedHierarchy()方法获得
 * @param toTree 加入节点的树对象
 * @param callback 回调
 * */
function TreeRoundAsyn(fromTree,fromTreeNodes,toTree,callback){
    var copyNodes = $.copyArray(fromTreeNodes);
    fromTree.clearAllNodeChecked();
    var newArray = [];
    var interval = setInterval(function(){        
        if(copyNodes.length <= 0 && newArray.length == 0){
            clearInterval(interval);
            if(callback){callback();}
        }else{
            var childs = [];
            if(copyNodes.length <=0){
                copyNodes = newArray;                            
            }

            var nodes = copyNodes.splice(0,10);
            var node = null;
            for(var i = 0,len = nodes.length; i < len; i++){
                node = nodes[i];
                var toTreeParentNode = null; 
                if(!toTree.getNodeById(node.id)){
                    TreeRound.conf.nodeId = node.id;
                    TreeRound.conf.text = node.text;    
                    TreeRound.conf.attrs= node.attrs;
                    TreeRound.conf.img = node.img;
                    if(node.parentId == "root"){                            
                        toTree.getRoot().appendChild(TreeRound.conf);
                    }else{
                        toTree.getNodeById(node.parentId).appendChild(TreeRound.conf);
                    }
                }                    
                var fromNode = fromTree.getNodeById(node.id);
                if(fromNode.isLeaf()){
                    var fromTreeParentNode = fromNode.parent();
                    //fromNode.delNode();
                    if(fromTreeParentNode.isLeaf()){
                        //fromTreeParentNode.delNode();
                    }else{
                        if(i+1 == len){
                            var text = fromTreeParentNode.getText();
                            if(text){
                                fromTreeParentNode.setText(text.replace(/\(.+\)/g, "("+fromTreeParentNode.childCount()+")"));
                            }
                        }                        
                    }    
                }else{
                    childs = childs.concat(node.childs);
                }
                if(i + 1 == len){
                    if(toTree.getNodeById(node.parentId)){
                        var text = toTree.getNodeById(node.parentId).getText();                            
                        toTree.getNodeById(node.parentId).setText(text.replace(/\(.+\)/g, "("+toTree.getNodeById(node.parentId).childCount()+")"));
                    }
                }    
                newArray = childs;
            }            
        }
    },10);

}


TreeRound.conf = {
    isLast: false,
    isLeaf: true,
    isExpend: true,
    img : null,
    isBtn: true,
    isClick: false,            
    nodeId: "",
    text: "",
    isCheckBox:true
}
/**
 * 删除树节点
 * @param tree 树
 * @param nodes 选中节点
 */
function TreeDelNode(tree, nodes) {
    round(nodes);
    tree.clearAllNodeChecked();
    function round(nodes){
        var node = null;
        for(var i = 0, len = nodes.length; i < len; i++) {
            node = nodes[i];
            var fromNode = tree.getNodeById(node.id);
            
            if(fromNode.isLeaf() || node.childs == null){
                var fromTreeParentNode = fromNode.parent();
                fromNode.delNode();
                if(fromTreeParentNode.isLeaf()){
                    fromTreeParentNode.delNode();
                }
            }else {
                round(node.childs);
                fromNode = tree.getNodeById(node.id);
                if(fromNode != null && fromNode.isLeaf()) {
                    fromNode.delNode();
                }
            }
        }
    }
}