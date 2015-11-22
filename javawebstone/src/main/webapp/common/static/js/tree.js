/*
 * conf{currentColor:当前选中的颜色，color:节点文本颜色}
 */
/**
 * 树组件
 * @class Tree
 * @uses Node
 * */
function Tree(conf){
    var closeClassName = "ico-file";
    var openClassName = "ico-file-leaf";
    /**
     * 树节点
     * @class Node
     * */
    function Node(){
        this.$li =null;
        this.childs = [];
    }
    Node.prototype={
        /*
         * 获得节点文本jQ对象
         */
        _get$Text:function(){
            var $text = this.$li.children("span[type='text']");
            return $text;
        },
        /*
         * 获得节点图标jQ对象
         */
        _get$Ico:function(){
            var $ico = this.$li.children("span[type='ico']");
            return $ico;
        },
        /*
         * 获得节点图标jQ对象
         */
        _get$Img:function(){
            var $img = this.$li.children("img");
            return $img;
        },        
        /*
         * 获得工具按钮jQ对象
         * */
        _get$Tool:function(){
            var $ico = this.$li.children("span[type='tool']");
            return $ico;
        },
        /*
         * 获得节点折叠按钮jQ对象
         */        
        _get$Btn:function(){
            var $btn = this.$li.children("div:first");
            return $btn;            
        },
        /*
         * 获得节点子节点集合jQ对象
         */        
        _get$Childs:function(){
            var $ul = this.$li.children("ul:last-child");
            return $ul;
        },
        /**子节点是否全选*/
        isChildAllChecked : function(isCheck){
            var lis = this.$li.children("ul:last-child").children("li");
            var  checkboxs = 0;
            var  checked = 0;
            var $check = null;
            for(var i = 0; i < lis.length; i++){                
                $check = $(lis[i]).children("input[type='checkbox']");
                if($check[0]){
                    checkboxs++;
                    if($check.attr("checked")){
                        checked++;
                    }
                }                
            }
            if(checkboxs != 0 && checkboxs == checked){
                return true;
            }
            return false;
        },
        /*
         * 
         * 获得最后一个子节点jQ对象
         * */
        _get$lastChild:function(){
            var $li = this.$li.children("ul:last-child li:last");
            return $li;
        },
        /*
         * 获得复选框文本jQ对象
         */
        _get$Checkbox:function(){
            var $checkbox = this.$li.children("input[type='checkbox']");
            return $checkbox;
        },
 
        /*
         * 获得复选框文本jQ对象
         */        
        _get$Radiobox:function(){
            var $radio = this.$li.children("input[type='radio']");
            return $radio;
        },
        _createNode :function($li){
            var node = new Node();
            node.setNode($li);
            return node;
        },
        _bindChildEvent : function(){
            if(this.$li.attr("isBind")){
                return;
            }
            this.$li.attr("isBind",true);
            var ul = this.$li.children("ul")[0];            
            var lis = ul ? ul.childNodes : null;
            var listeners = conf.listeners || {};
            var $li = null;   
            if(lis){
                for(var i = 0, len = lis.length; i < len; i++){
                    $li = $(lis[i]);
                    tree.bindFoldBtn($li.children("div.hitarea"), listeners);
                    tree.bindNodeClick($li.children("span[type='text'][clickable='true']"), listeners);
                    tree.bindCheckbox($li.children("input[type='checkbox']"), listeners);
                    tree.bindToolclick($li.children("span[type='tool']"), listeners);
                }    
            } 
        },
        /**
         * 该节点是否是同级最后一个节点
         * @method isLast
         * @for Node
         * @return {Boolean}
         * @lends Node.property
         * */
        isLast:function(){
            return this.$li[0].className.indexOf("last")!=-1;
        },
        /**
         * 塞入树节点$li 
         * @method setNode
         * @chainable
         */
        setNode:function($li){ 
            this.$li = $li;
             return this;
        },
        /**
         * 当前节点是展开状态还是折叠状态
         * @method state
         * @return {Boolean}
         */
        state:function(){   
            return this.$li.hasClass("collapsable");
        },
        /**
         * 该节点是否是叶子节点
         * @method isLeaf
         * @return {Boolean}
         */
        isLeaf:function(){
           var id = this.getId();
            
            if(this._get$Childs()[0]==null && id && id != "root"){ //没有子节点UL则为子节点
                var clsName = this._get$Btn()[0].className;
                if(this._get$Btn()[0] && clsName.indexOf("placeholder") !=-1){ //折叠按钮为placeholder样式则为子节点
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        },
        /**
         * 该节点是否显示
         * @method isNodeDisplay
         * @return {Boolean}
         */
        isNodeDisplay:function(){
            return this.$li.css("display")!="none";
        },
        /**
         * 该节点是否展开
         * @method isExpend
         * @return {Boolean}
         */
        isExpend:function(){
            return this.$li[0].className.indexOf("collapsable")!=-1;
        },
        /**
         * 隐藏该节点
         * @method setNodeDispNone
         * @chainable
         */
        setNodeDispNone:function(){
            this.$li.hide();
            return this;
        },
        /**
         * 显示该节点
         * @method setNodeDisp
         * @chainable
         */        
        setNodeDisp:function(){
            this.$li.show();
            return this;
        },
        /**
         * 设置节点文本
         * @method setText
         * @param {String} text
         * @chainable
         */
        setText:function(text){
            var $text = this._get$Text();
            $text.html(text);
            return this;
        },
        /**
         * 获得节点文本
         * @method getText
         * @return {String} text
         */        
        getText:function(){
            var $text = this._get$Text();
            return $text.html();
        },
        /**
         * 设置结点title属性
         * @method getText
         * @chainable
         * @param {String} title
         */ 
        setTitle:function(title){
            var $text = this._get$Text();
             $text.attr("title",title);
             return this;
        },
        /**
         * 获取结点title属性
         * @method getTitle
         * @chainalbe
         * @return {String}
         */
        getTitle:function(){
            return this._get$Text().attr("title");
        },        
        /**
         * 获得节点ID
         * @method getId
         * @return {String}
         */                
        getId:function(){
            return this.$li.attr("nodeid");
        },
        /**
         * 设置节点ID
         * @method setId
         * @param {String} nodeid 节点ID
         * */
        setId:function(nodeid){
            return this.$li.attr("nodeid",nodeid);
        },
        /**
         * 设置文本节点样式
         * @method setTextStyle
         * @param styleName {String} 样式名称
         * @param styleVal {String} 样式值
         * @chainable
         */                
        setTextStyle:function(styleName,styleVal){
            var $text = this._get$Text();
            $text.css(styleName,styleVal);
            return this;
        },
        /**
         * 设置节点为当前选中的节点
         * @method setCurrentNode
         * @chainable
         */            
        setCurrentNode:function(){
            if(tree.currentNode){//如果有当前节点
                tempNode.setNode(tree.currentNode).clearCurrentNode();
            }
            //if(tree.searchnode){//
              //  tempNode.setNode(tree.searchnode).clearSearchNode();
            //}
            this._get$Text().attr("isCurrent","true").addClass("treenodecolor-current");
            tree.currentNode = this.$li;
            return this;
        },
        /**
         * 清除该节点为当前选中节点
         * @method clearCurrentNode
         * @chainable
         */                    
        clearCurrentNode:function(){
            this._get$Text().removeAttr("isCurrent").removeClass("treenodecolor-current");
            tree.currentNode = null;
            return this;    
        },
        /**
         * 判断该节点是否为当前节点
         * @method isCurrentNode
         * @return {Boolean}
         */                
        isCurrentNode:function(){
            return this._get$Text().attr("isCurrent")==="true";
        },
        /**
         * 设置节点为搜索选中的节点
         * @method setCurrentNode
         * @chainable
         */            
        setSearchNode:function(){
            if(tree.searchnode){//如果有当前节点
                tempNode.setNode(tree.searchnode).clearSearchNode();
            }
            this._get$Text().attr("isSearch","true").addClass("selectednode");
            tree.searchnode = this.$li;
            return this;
        },
        /**
         * 清除该节点为搜索选中节点
         * @method clearSearchNode
         * @chainable
         */                    
        clearSearchNode:function(){
            this._get$Text().removeAttr("isSearch").removeClass("selectednode");
            tree.searchnode = null;
            return this;    
        },
        /**
         * 判断该节点是否为搜索节点
         * @method isCurrentNode
         * @return {Boolean}
         */                
        isSearchNode:function(){
            return this._get$Text().attr("isSearch")==="true";
        },        
         /**
          * 折叠节点
          * @method fold
          * @chainable
          * @listeners折叠按钮操作事件
          * collapseBefore，expendBefore折叠前发生，方法返回false不发生折叠
          * collapseAfter，expendAfter折叠后发生
          */
        fold:function(){
            if(this.state()){//true展开状态，可以折叠
                this.collapse();
            }else{
                this.expend();
            }
            return this;
        },
        /**
         * 展开节点 expendBefore  expend
         * @method expend
         * @chainable
         */
        expend:function(async){
            if(this.state() || this.isLeaf()) return;//如果已经是展开状态或叶子节点，返回，不执行
            if(listeners.expendBefore){//如果展开前有事件，执行返回false，直接返回，不执行展开操作
                var flag = listeners.expendBefore(this);
                if(flag===false) return this; 
            }
            var $child = this._get$Childs();//获得他的子对象集合UL层
            if(!$child[0]){  //如果没有子节点集合，则取节点
                if(url){
                    this.asynNode(true);
                }
            }else{
                $child.show();
            }
            
            
            var $btn = this._get$Btn();
            if(this.$li.hasClass("expandable")){
                this.$li[0].className = "collapsable";
                $btn.removeClass("expandable-hitarea").addClass("collapsable-hitarea");
            }
            
            if(this.$li.hasClass("lastExpandable")){
                this.$li.removeClass("lastExpandable").addClass("lastCollapsable");
                $btn.removeClass("lastExpandable-hitarea").addClass("lastCollapsable-hitarea");
            }                        
            if(this._get$Ico().hasClass(closeClassName)){
                this._get$Ico().removeClass(closeClassName).addClass(openClassName);    
            }
            
            
            if(listeners.expend){//如果展开后有事件
                listeners.expend(this);
            }
            
            this._bindChildEvent();               
            
            return this;            
        },
        /**
         * 折叠节点  collapseBefore collapse
         * @method collapse
         * @chainable
        */
        collapse:function(){
            if(!this.state()) return;//如果已经是折叠状态，返回，不执行
            if(listeners.collapseBefore){//如果展开前有事件，执行返回false，直接返回，不执行展开操作
                var flag = listeners.collapseBefore(this);
                if(flag===false) return this; 
            }            
            
            var $btn = this._get$Btn();
            var $child = this._get$Childs();//获得他的子对象集合UL层
            
            if(this.$li.hasClass("collapsable")){
                this.$li[0].className="expandable";
                //this.$li.removeClass("collapsable").addClass("expandable");
                $btn.removeClass("collapsable-hitarea").addClass("expandable-hitarea");
            }
            if(this.$li.hasClass("lastCollapsable")){
                this.$li.removeClass("lastCollapsable").addClass("lastExpandable");
                $btn.removeClass("lastCollapsable-hitarea").addClass("lastExpandable-hitarea");
            }
            $child.css("display","none");
            if(listeners.collapse){//如果折叠后有事件
                listeners.collapse(this);
            }            
            if(this._get$Ico().hasClass(openClassName)){
                this._get$Ico().removeClass(openClassName).addClass(closeClassName);    
            }            
            return this;            
        },
        /**
         * 展开当前节点，同时展开它的父节点
         * @method expandUP
        */
        expandUP:function(){
            if(this.childCount()>0)
            {
                this.expend();
            }
            var parent = this.parent();
            
            while(parent){
                parent.expend();
                parent = parent.parent();
            }
            
        },
        /**
         * 获得该节点路径
         * @method getPathId
         * @return {Array} 根节点到该节点ID路径数组  
         */
        getPathId:function(){
            var path = [];
            function roundParent(node){
                if(node){
                    var id = node.getId();
                    if(id && id!="root"){
                        path.push(id);
                        roundParent(node.parent());
                    }
                }
            };
            path.push(this.getId());
            roundParent(this.parent());
            return path.reverse();
        },
        /**
         * 节点点击事件
         * @method click
         * @listeners 
         * clickBefore   nodeClick
         */
        click:function(){
            var $text = this._get$Text();
            if($text.css("cursor")=="pointer"){
                if(listeners.clickBefore){
                    var flag = listeners.clickBefore(this);
                    if(flag === false) return this;
                }
                listeners.nodeClick(this);
                this.setCurrentNode();
                if(listeners.clickAfter){
                    listeners.clickAfter(this);
                }
            }
            
        },
        /**
         * 设置图标
         * @method setIco
         * @param ico {String}
         * */
        setIco:function(ico){
            var $ico = this._get$Ico();
            $ico.removeClass().addClass(ico);
        },
        /**
         * 设置图片
         * @method setImage
         * @param imagePath {String} 图片路径
         * */
        setImage:function(imagePath){
            var $img = this._get$Img();
            $img.attr("src",imagePath);
        },
        /**
         * 获得图片路径
         * @method getImage
         * @return 图片路径
         * */        
        getImage : function(){
          var $img = this._get$Img();
          return $img.attr("src");  
        },
        /**
         * 获得所有带复选框的节点
         * @method getCheckboxNodes
         * @param isLeaf {boolean} 如果为true则只获得叶子节点
         * @return {Node}
         * */
        getCheckboxNodes:function(isLeaf){
            var checkboxeds = this._get$Childs().find("input[type='checkbox']");
            //var checkboxeds = checkboxs.filter(":checked");
            var checkedNode = [];
            var node = null;
            for(var i = 0,len = checkboxeds.length; i<len; i++){
                var $checkbox = $(checkboxeds[i]);
                node = new Node();
                if(isLeaf){
                    var $ul = $checkbox.siblings("ul");
                    if($ul.length==0){
                        node.setNode($checkbox.parent());
                        checkedNode.push(node);
                    }
                }else{
                    node.setNode($checkbox.parent());
                    checkedNode.push(node);
                }
            }
            return checkedNode;
        },
        /**
         * 获得所有复选框选中的节点
         * @method getCheckedNodes
         * @param isLeaf {boolean} 如果为true则只获得选中的叶子节点
         * @return {Node}
         * */
        getCheckedNodes:function(isLeaf){
            var checkboxs = this._get$Childs().find("input[type='checkbox']");
            var checkboxeds = checkboxs.filter(":checked");
            var checkedNode = [];
            var node = null;
            for(var i = 0,len = checkboxeds.length; i<len; i++){
                var $checkbox = $(checkboxeds[i]);
                node = new Node();
                if(isLeaf){
                    var $ul = $checkbox.siblings("ul");
                    if($ul.length==0){
                        node.setNode($checkbox.parent());
                        checkedNode.push(node);
                    }
                }else{
                    node.setNode($checkbox.parent());
                    checkedNode.push(node);
                }
            }
            return checkedNode;
        },        
        
        /**
         * 设置该节点复选框或单选框选中
         * @method setChecked
         * @param isRelative 是否判断相关
         * */
        setChecked : function(isRelative){
            var $checkbox = this._get$Checkbox();
            if($checkbox.attr("checked")){
                return;
            }

            var $radiobox = this._get$Radiobox();
            if($radiobox[0]){
                $radiobox.attr("checked","checked");
                return;
            }

            $checkbox.mousedown();
            
            $checkbox.focus();
            $checkbox.attr("checked","checked");
            $checkbox.blur();
            $checkbox.change();
            this.checkboxClick(isRelative);
/*
            if($checkbox[0]){
                $checkbox.attr("checked","true");
                this.checkboxClick();
            }*/
        },
        /**
         * 清除该节点复选框选中
         * @method clearChecked
         * */        
        clearChecked : function(){
            var $checkbox = this._get$Checkbox();
            $checkbox.mousedown();
            $checkbox.removeAttr("checked");
            $checkbox.focus();
            $checkbox.blur();    
            $checkbox.change();
            this.checkboxClick();
        },
        checkslibnext : function(){
            
        },
        /**
         * 该节点是否被选中
         * @method isChecked
         * @return {Boolean}
         */
        isChecked : function(){
            var $checkbox = this._get$Checkbox();
            if($checkbox[0]){
                return $checkbox[0].checked;
            }else{
                return false;
            }
            
        },
        /*
         * 复选框点击事件
         * @checkboxClick 
         * */
        checkboxClick : function(isRelative){
            if(isRelative === false) return;
            this._roundDownChecked();
            this._roundUpChecked();
        },
        /*
         * 级联向上复选节点
         * */
        _roundUpChecked : function(){   //向上检查
            function roundchecked($li){
                var checkbox = $li.children("input[type='checkbox']")[0];
                if(!checkbox) return;
                var checked = checkbox.checked;
                var slibingLis = $li.siblings("li");
                var slibingCheckbox = slibingLis.children("input[type='checkbox']");
                var checkeds = slibingCheckbox.filter(":checked");
                var $parentli = $li.parent().parent();
                var parentCheck = $parentli.children("input[type='checkbox']");
                if(checkeds.length==0 && !checked){
                    parentCheck.removeAttr("checked");
                }else if(slibingCheckbox.length===checkeds.length && checked){
                    parentCheck.attr("checked","true").css("filter","");
                }else{
                    parentCheck.attr("checked","true").css("filter","gray");
                }
                roundchecked($parentli);
            }
            roundchecked(this.$li);
        },
        /*
         * 级联向下复选节点
         * */        
        _roundDownChecked : function(){//向下检查
            
            function roundchecked($li){
                var checkbox = $li.children("input[type='checkbox']")[0];
                if(!checkbox) return;
                var checked = checkbox.checked;
                var checkboxs = $li.children("ul").find("input[type='checkbox']");
                if(checked){
                    checkboxs.attr("checked","true");
                }else{
                    checkboxs.removeAttr("checked");
                }
            }
            roundchecked(this.$li);
        },
        /**
         * 添加子节点集合
         * @method addChilds
         * @param childsHtml {String} html
         */
        addChilds:function(childsHtml){
            var id = this.getId();
            this.$li.append(childsHtml);
            tree.bindFoldBtn("#"+conf.id+" li[nodeid='"+id+"'] ul");
            tree.bindNodeClick("#"+conf.id+" li[nodeid='"+id+"'] ul", listeners);
            tree.bindCheckbox("#"+conf.id+" li[nodeid='"+id+"'] ul", listeners);
            tree.bindToolclick("#"+conf.id+" li[nodeid='"+id+"'] ul", listeners);
            if(this.isChecked()){
                this.checkboxClick();
            }
        },
        /*
         * 绑定事件
         * */
        bindEvent:function(node){
                tree.bindFoldBtn(node._get$Btn());
                tree.bindNodeClick(node._get$Text(), listeners);
                tree.bindCheckbox(node._get$Checkbox(), listeners);
                tree.bindCheckbox(node._get$Radiobox(), listeners);
                tree.bindToolclick(node._get$Tool(), listeners);
        },
        /**
         * js方式添加节点
         * @method appendChild
         * @param nodeConf {Json} 节点配置对象
         * @return {Node} 新创建的节点
         */
        appendChild:function(nodeConf){
            var $ul = null;
            nodeConf.isLast = true;//新添加的这个默认是最后一个节点
            var thisIsLeaf = this.isLeaf();//新添加节点的父节点是一个叶子节点
            var $btn = this._get$Btn();//获得当前节点的折叠按钮
            
            var lastChildNode = this.getLastChild();
            if(lastChildNode){ //调整最后一个
                lastChildNode.setNotLast(lastChildNode.isLeaf(),lastChildNode.isExpend());
            }
            
            if(this._get$Childs()[0]==null){
                if($btn[0]){
                    this.setNotLeaf(this.isLast()); //将折叠按钮设置
                    tree.bindFoldBtn($btn);
                }
                $ul = $("<ul></ul>");
                this.$li.append($ul);
            }
            if($btn[0]){ //用于判断现有树是否支持折叠按钮
                nodeConf.isBtn=true;
            }
            
            $ul = this._get$Childs();
            var nodeStr = TreePlugins.createNode(nodeConf);
            $ul.append(nodeStr);
            var newnode  = tree.getNodeById(nodeConf.nodeId);
            this.bindEvent(newnode);
            return newnode;
        },
        /**
         * 删除该节点
         * @method delNode
         * @return {String} 被删除节点的内部html
         * */
        delNode:function(){
            this.$li.find("*").unbind();
            var curLast = this.isLast();
            var prevnode = this.getPrevNode();
            var nextnode = this.getNextNode();
            if(curLast && prevnode){
                prevnode.setLast(prevnode.isLeaf(),prevnode.isExpend());
            }
            if(this.isChecked()){
                this.parent().clearChecked();
            }
            if(!prevnode && !nextnode){
                if(this.parent().getId()!="root"){
                    this.parent().setLeaf(this.parent().isLast());
                }
            }
            
            
            var div = document.createElement("div");
            div.appendChild(this.$li[0]);
            var html = div.innerHTML;
            div.innerHTML="";
            div=null;
            return html;
        },
        /**
         * 设置节点不是叶子
         * @method setNotLeaf
         * @param isLast
         */
        setNotLeaf:function(isLast){
            tree.bindFoldBtn(this._get$Btn());
            if(isLast){
                this.$li.removeClass().addClass("collapsable lastCollapsable");
                this._get$Btn().removeClass().addClass("hitarea collapsable-hitarea lastCollapsable-hitarea");
            }else{
                this.$li.removeClass().addClass("collapsable");
                this._get$Btn().removeClass().addClass("hitarea collapsable-hitarea");                
            }
        },
        /**
         * 设置节点为叶子节点
         * @method setLeaf
         * @param isLast
         * */
        setLeaf:function(isLast){
            if(isLast){
                if(this.$li[0]){
                    this.$li[0].className = "last";
                }
                var $btn  = this._get$Btn();
                if($btn[0]) $btn[0].className = "placeholder last";
    
            }else{
                if(this.$li[0]){
                    this.$li[0].className = "";
                }                
                var $btn  = this._get$Btn();
                if($btn[0]) $btn[0].className = "placeholder";
                
            }            
            var div = document.createElement("div");
            div.appendChild(this.$li.children("ul")[0]);
            div.innerHTML="";
            div = null;
            tree.unbindFoldBtn(this._get$Btn());
        },
        /**
         * 设置节点不是最后一个节点
         * @method setNotLast
         * @param isLeaf {Boolean}
         * @param isExpend {Boolean}
         * */
        setNotLast:function(isLeaf,isExpend){
            if(isLeaf){
                this.$li.removeClass();
                this._get$Btn().removeClass().addClass("placeholder");
            }else{
                var btnCls = isExpend ? "collapsable" : "expandable";
                this.$li.removeClass().addClass(btnCls);
                this._get$Btn().removeClass().addClass("hitarea "+btnCls+"-hitarea");                
            }            
        },
        /**
         * 设置节点为最后一个节点
         * @method setLast
         * @param isLeaf {Boolean}
         * @param isExpend {Boolean}
         * */
        setLast:function(isLeaf,isExpend){
            if(isLeaf){
                this.$li.removeClass().addClass("last");
                this._get$Btn().removeClass().addClass("placeholder last");
            }else{
                var liCls = "collapsable lastCollapsable";
                var btnCls = "hitarea collapsable-hitarea lastCollapsable-hitarea";
                if(!isExpend){
                    liCls = "expandable lastExpandable";
                    btnCls = "hitarea expandable-hitarea lastExpandable-hitarea";
                }
                this.$li.removeClass().addClass(liCls);
                this._get$Btn().removeClass().addClass(btnCls);                
            }                        
        },
        /**
         * 动态获取树节点
         * @method asynNode
         * @param async 是否异步,默认true, false不是异步
         */
        asynNode:function(async){
            var id = this.getId();
            var self = this;
            $.ajax({
                type: "POST",
                url: url,
                dataType: "html",
                data: tree.param+id,
                async:async==null?true:false,
                success:function(msg){
                    self.addChilds(msg);
                }
            });
        },
        /**
         * 获得父节点
         * @method parent
         * @return {Node} treeNode
         */
        parent:function(){
            var $li = this.$li.parent().parent();
            if($li[0] && $li.attr("nodeid")){
                var node = new Node();
                node.setNode($li);
                return node;
            }else{
                return null;
            }
        },
        /**
         * 获得子节点
         * @method children
         * @return {Array} Node
         * */
        children:function(){
            var nodes = [];
            var ul = this._get$Childs()[0];
            if(ul){
                var lis = ul.childNodes;
                var node = null;
                for(var i=0,len = lis.length;i<len;i++){
                    if(lis[i].nodeType == 3) continue;
                    node = new Node();
                    node.setNode($(lis[i]));
                    nodes.push(node);
                }
            }
            return nodes;
        },
        /**
         * 该节点子节点个数
         * @method childCount
         * @return {Number} number
         */
        childCount:function(){
            var ul  = this._get$Childs()[0];
            if(ul){
                var lis = ul.children;    
                return lis.length;
            }else{
                return 0;
            }
        },
        /**
         * 获得第一个子结点
         * @method getFirstChild
         * @return {Node} treeNode
         */
        getFirstChild:function(){
            var ul = this._get$Childs()[0];
            var node = null;
            if(ul){
                var li  = ul.children[0];
                if(li){
                    node = new Node();
                    node.setNode($(li));
                }
            }
            return node;
        },
        /**
         * 获得上一个节点
         * @method getPrevNode
         * @return {Node} treeNode
         * */
        getPrevNode:function(){
            var li = $.get_previousSibling(this.$li[0]);
            if(li){
                var node = new Node();
                node.setNode($(li));
                return node;            
            }else{
                return null;
            }
                
        },
        /**
         * 获得下一个节点
         * @method getNextNode
         * @return {Node} treeNode
         * */        
        getNextNode:function(){
            var li = this.$li[0].nextSibling
            if(li){
                var node = new Node();
                node.setNode($(li));
                return node;            
            }else{
                return null;
            }
        },
        /**
         * 获得最后一个子结点
         * @method getLastChild
         * @return {Node} treeNode
         */
        getLastChild:function(){
            var ul = this._get$Childs()[0];
            var node = null;
            if(ul && ul.children && ul.children.length > 0){
                var child = ul.children;
                var li  = child[child.length - 1];
                if(li){
                    node = new Node();
                    node.setNode($(li));
                }
            }
            return node;
        },
        /**
         * 获得指定索引的子节点
         * @method getIndexChild
         * @param {number} 索引
         * @return {Node} treeNode
         */        
        getIndexChild:function(index){
            var li = this._get$Childs()[0].children[index];
            if(li){
                var node = new Node();
                node.setNode($(li));
                return node;
            }else{
                return null;
            }                        
        },
        /**
         * 获得节点自定义属性值
         * @method getValue
         * @for Node
         * @param key {String} 属性名
         * @return {String} 属性值
         * */
        getValue:function(key){
            return this.$li.attr(key);
        }
    };
    
    
    var $tree= this.$tree= $("#"+conf.id);//树最外层对象
    this.id = conf.id;
    this.root = $tree.children("li").children("ul");  //根节点
    var url = conf.url;  //异步请求节点的URL
    var param = this.param = conf.param; //异步请求的参数
    this.node = new Node();
    var tempNode =this.tempNode= new Node();
    this.currentNode = null;  //当前选中节点$li
    this.searchnode = null;//检索的节点$li
    var lis = this.root.find("li");
    var tree = this;//tree对象的引用
    var listeners = conf.listeners == null? {}:conf.listeners; 
    
    if(conf.plugins){
        for(var i=0;i<conf.plugins.length;i++){
            var plugin = TreePlugins[conf.plugins[i]];
            if(plugin){
                plugin.call(this,conf);
            }
        }
    }
    
    var self = this;

    if(conf.inputAttrs && conf.inputAttrs.name){
        $("#" + conf.id + ' input[type="radio"]').attr("name", conf.inputAttrs.name);
    }
    /*
    setTimeout(function(){
        self.bindFoldBtn("#"+conf.id);
    },10);
    setTimeout(function(){
        self.bindNodeClick("#"+conf.id, listeners);
    },10);
    setTimeout(function(){
        self.bindCheckbox("#"+conf.id, listeners);
    },10);
    setTimeout(function(){
        self.bindToolclick("#"+conf.id, listeners);
    },10);
    */
    var root = this.getRoot();
    root._bindChildEvent();

    

    function roundBind(children){
        if(children){
            for(var i = 0, len = children.length; i < len; i++){                
                if(children[i].isExpend()){
                    children[i]._bindChildEvent();
                    roundBind(children[i].children());
                }
            }
        }
    }
    roundBind(root.children());

    if(root && conf.isExpand === false ? false : true){
        var firstChild = root.getFirstChild();
        if(firstChild){
            firstChild.expend();
        }
    }
    
    
    
};
