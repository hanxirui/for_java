/**
 * 按钮表格
 * @class ButtonTable
 */
var ButtonTable = function(){
    var BUTTON_STATUS = {
        DISABLED : "disabled",
        HIDDEN : "hidden",
        NORMAL : "normal"
    };
    
    var DEFAULT_CONF = {
        debug: false,
        domId: null,        // Document id，对应标签安放组件内容
        tableData: null,    // 已选表格数据与grid_panel组件的data一致
        data: null,         // 自定义数据，提交数据时以展开形式传回，例如{key1: value1, key2: value2}传回key1=json1&key2=json2（json1和json2是分别把value1和value2转换成json字符串）
        checkColId: null,   // 行选列的ID
        dataRefreshSchema : "_", // 表格刷新URL参数格式
        colModule: null,    // 已选表格列模式
        popColModule: null, // 可选表格列模式，为空时与colModule相同
        pageSize: 10,       // 表格每页显示行数（影响组件高度）
        emptyText: "",      // 不可为空时的文字（多国语言支持）
        needSelectText: "",  // 未选择数据时提示的文字（多国语言支持）
        requestUrl: {
            getSelectedData: "",    // 获取已选数据的URL，参数除了data展开形式
            getUnselectedData: "",  // 获取未选数据的URL，参数除了data展开形式
            addData: "",    // 添加数据时的URL，参数除了data展开形式，还有新加的数据{data:tableData}（与tableData格式相同的json字符串)
            delData: ""     // 删除数据时的URL, 参数除了data展开形式，还有删除的数据{data:tableData}（与tableData格式相同的json字符串)
        },
        callback: {
            formatData: null // 表格刷新之前调用这个函数来格式化url的参数数据
        },
        filter: [],         // 见DEFAULT_FILTER
        plugIn: [],//"domain", "addAll"
        plugInData: {
            domainList: {
                key: "customKey1",
                value: null,
                text: window.S_DOMAIN || '!!S_DOMAIN!!',
                valueList: [] // {text: "未选择", value: ""}
            }
        },
        listeners: {
            beforeShowAdd: null,    // 弹出添加页前执行，返回true/false，只有true时，才能弹出
            beforeSubmitAdd: null,  // 提交添加数据前执行，返回json，与data同时提交
            afterSubmitAdd: null,   // 提交添加之后执行
            afterSubmitDelete: null // 提交删除之后执行
        },
        buttons: {
            "delete" : BUTTON_STATUS.NORMAL
        },
        customArea: null,               // 待选表上边的自定义空间的内容
        customAreaOnFoot: null,         // 待选表下边的自定义空间的内容
        customAreaOnSelectedTop: null,  // 已选表上边的自定义空间的内容
        customAreaOnSelectedFoot: null  // 已选表下边的自定义空间的内容
    };
    
    var DEFAULT_FILTER_CONF = {
        type: "list",       // 类型
        text: "",           // label文字
        value: "",          // 当前值
        valueList: [{       // 值列表
            text: "",       // 显示文字
            value: ""       // 值
        }],
        key: ""             // 查询条件标识，对象或字符串
    };
    
    var CONF_CONSTRAINS = {
        type: ["object"],
        elems: {
            domId: {type: ["string"]},
            // tableData: {type: ["object"]},
            data: {type: ["object"], required: false},
            dataRefreshSchema: {type: ["string"], required: false, empty:true},
            checkColId: {type: ["string"]},
            colModule: {type: ["object"]},
            popColModule: {type: ["object"], required: false},
            emptyText: {type: ["string"]},
            requestUrl: {type: ["object"], elems: {
                getSelectedData: {type: ["string"], empty:true},
                getUnselectedData: {type: ["string"], empty:true},
                addData: {type: ["string"], empty:true},
                delData: {type: ["string"], empty:true}
            }},
            callback: {type: ["object"], elems: {
                formatData: {type: ["function"], required: false}
            }},
            filter: {type:["object"], array:true, empty:true},
            listeners: {type: ["object"], elems: {
                beforeShowAdd: {type: ["function"], required: false},
                beforeSumbmitAdd: {type: ["function"], required: false},
                afterSubmitAdd: {type: ["function"], required: false},
                afterSubmitDelete: {type: ["function"], required: false}
            }},
            buttons: {type: ["object"], elems: {
                "delete": {type: ["string"], required: false}
            }},
            customeArea: {type: ["string", "object"], required: false}
        }
    };
    
    var FILTER_CONF_CONSTRAINS = {
        type: ["object"],
        elems: {
            type: {type: ["string"]},
            text: {type: ["string"]},
            value: {type: ["string"], empty:true},
            valueList: {type: ["object"], array:true, empty:true, elems: {
                text: {type: ["string"]},
                value: {type: ["string"], empty:true}
            }},
            key: {type: ["string"]}
        }
    };
    
    var FORMAT_DATA_SCHEMA = {
        _ : formatDataDefault
    };
    
    var m_grids = {},
        m_selectInfos = {},// sortColId, sortType, filters[], pageNum, maxPage
        m_confs = {},
        m_parentIds = {},
        m_nextId = 1,
        m_searchRender = {
            list: createList
        };

    var popWindowId = null;
    var windowId = null;
    
    function createId() {
        return "button_table_" + (m_nextId++);
    }
    

//================================================================================================//
// API
//================================================================================================//
    return {
        init: init,
        reloadUnSelected: reloadUnSelected,
        reloadSelected: reloadSelected,
        addToSelected: addToSelected,
        delFromSelected: delFromSelected,
        showDialog: showDialog
    };

    /**
     * 初始化
     * @method init
     * 
     * @param conf {Json} 配置信息,
     * @param conf.debug {Boolean} 默认：false,
     * @param conf.domId {String} Document id，对应标签安放组件内容
     * @param conf.tableData {Json} 已选表格数据与grid_panel组件的data一致
     * @param conf.data {Json} 自定义数据，提交数据时以展开形式传回，例如{key1: value1, key2: value2}传回key1=json1&key2=json2（json1和json2是分别把value1和value2转换成json字符串）
     * @param conf.checkColId {String} 行选列的ID
     * @param conf.dataRefreshSchema {String} 默认："_"，表格刷新URL参数格式
     * @param conf.colModule {Json} 已选表格列模式
     * @param conf.popColModule {Json} 可选表格列模式，为空时与colModule相同
     * @param conf.pageSize {Number} 默认: 6，表格每页显示行数（影响组件高度）
     * @param conf.emptyText {String} 不可为空时的文字（多国语言支持）
     * @param conf.needSelectText {String} 未选择数据时提示的文字（多国语言支持）
     * @param conf.requestUrl {Json} url配置信息
     * @param conf.requestUtl.getSelectedData {String} 获取已选数据的URL，参数除了data展开形式
     * @param conf.requestUtl.getUnselectedData {String} 获取未选数据的URL，参数除了data展开形式
     * @param conf.requestUtl.addData {String} 添加数据时的URL，参数除了data展开形式，还有新加的数据{data:tableData}（与tableData格式相同的json字符串)
     * @param conf.requestUtl.delData {String} 删除数据时的URL, 参数除了data展开形式，还有删除的数据{data:tableData}（与tableData格式相同的json字符串)
     * @param conf.callback {Object} 回调函数配置
     * @param conf.callback.formatData {Function} 表格刷新之前调用这个函数来格式化url的参数数据
     * @param conf.filter* {Json} 见DEFAULT_FILTER
     * @param conf.filter.type {String} 默认："list"，类型
     * @param conf.filter.text {String} label文字
     * @param conf.filter.value {String} 当前值
     * @param conf.filter.valueList* {Json} 值列表
     * @param conf.filter.valueList.text {String} 显示文字
     * @param conf.filter.valueList.value {String} 值
     * @param conf.filter.key {String} 查询条件标识，对象或字符串
     * @param conf.listeners {Object} 事件监听配置信息
     * @param conf.listeners.beforeShowAdd {Function} 弹出添加页前执行，返回true/false，只有true时，才能弹出
     * @param conf.listeners.beforeSubmitAdd {Function} 提交添加数据前执行，返回json，与data同时提交
     * @param conf.listeners.afterSubmitAdd {Function} 提交添加之后执行，参数是ajax response的内容
     * @param conf.listeners.afterSubmitDelete {Function} 提交删除之后执行，参数是ajax response的内容
     * @param conf.customArea {HTML|jQuery} 待选表上边的自定义空间的内容
     * @param conf.customAreaOnFoot {HTML|jQuery} 待选表下边的自定义空间的内容
     * @param conf.customAreaOnSelectedTop {HTML|jQuery} 已选表上边的自定义空间的内容
     * @param conf.customAreaOnSelectedFoot {HTML|jQuery} 已选表下边的自定义空间的内容
     * @param conf.selectPaging {Boolean} 已选表格是否有翻页
     */
    function init(conf) {
        if (undefined === conf || null === conf) return;
        ConfigData.setDefaultValue(conf, DEFAULT_CONF);
        ConfigData.check("buttonTable_conf", conf, CONF_CONSTRAINS);
        ConfigData.checkDomElem(conf.domId);
        var laterCall = createUI(windowId = conf.domId, conf);
        if (laterCall) {
            laterCall();
        }
    }
    
    /**
     * 设置按钮不可用状态
     * @param btn {String} 按钮类型，可以是"delete", "add", "submit"
     */
    function setButtonDisabled(btn) {
        
    }


    /**
     * 根据给定检索条件更新“待选”表格
     *     
     *    ButtonTable.reloadUnSelected({gender: 'female'});
     *    
     * @method reloadUnSelected
     * @param filterMap {json} 检索条件{key1: value1, key2: value2...}
     */
    function reloadUnSelected (filterMap) {
        if (!popWindowId) alert('error unselected window not popped!');
        m_selectInfos[popWindowId].customFilters = filterMap;
        reloadData(popWindowId);
    }

    /**
     * 根据给定检索条件更新“待选”表格
     *     
     *    ButtonTable.reloadSelected({gender: 'male'});
     *    
     * @method reloadSelected
     * @param filterMap {json} 检索条件{key1: value1, key2: value2...}
     */
    function reloadSelected (filterMap) {
        if (!popWindowId) alert('error unselected window not popped!');
        m_selectInfos[windowId].customFilters = filterMap;
        reloadData(windowId);
    }

    /**
     * 将用户在“待选”表格选择的数据添加“已选”表格
     *
     *     ButtonTable.addToSelected();
     * 
     * @method addToSelected
     */
    function addToSelected() {
        if (!popWindowId) alert('error unselected window not popped!');
        onAddForPop(popWindowId);
    }
    
    /**
     * 将用户在“已选”表格选择的数据删除，被删除数据将在“待选”表格显示
     *
     *     ButtonTable.delFromSelected();
     * 
     * @method delFromSelected
     */
    function delFromSelected() {
        onDelete(windowId);
    }

    /**
     * 弹出添加窗口
     * 
     *    ButtonTable.showDialog();
     * 
     * @methos showDialog
     */
    function showDialog() {
        if (_showAdd) _showAdd();
    }

//================================================================================================//
// 界面创建
//================================================================================================//
    function createUI(id, conf, isPop, data) {
        var $parent = $("#" + id),
            laterCall;
        

        m_confs[id] = conf;
        m_parentIds[id] = conf.domId;
        
        if (conf.filter.length > 0) {
            laterCall = createSearchArea($parent, id, conf);
        }
        
        if (id === conf.domId) {
            createUpperButtonArea($parent, id, conf);
        }else {
            createUpperButtonAreaForPop($parent, id, conf);
        }
        
        createGridArea($parent, id, conf, isPop, data);

        if (isPop && conf.customAreaOnFoot) {
            createCustomAreaOnFoot($parent, id, conf.customAreaOnFoot);
        }
        
        initData(id, conf);
        
        return laterCall;
    }

    function showAdd(id) {
        var conf = m_confs[id];

        Loading.start();
        var $window = createWindow(conf);
        if ($window) {
            conf._window = $window;
        }
    }
    
    function createWindow(conf) {
        var id = popWindowId = popWindowId || createId(),
            winWidth = $(window).width(),
            width = winWidth * 0.9;
        
        var buttonsConf = [
            {type : "ok", status : conf.buttons.submit, click : function() {
                onOk(id);
            }}, {type :  "cancel", click : function() {
                onCancel(id);
            }}
        ];
        
        if ($.isExistArray('addAll', conf.plugIn)) {
            buttonsConf.unshift({
                type : "joinall", 
                status : conf.buttons["addAll"],
                click : function () {
                    onAddAllForPop(id);
                }
            });
            buttonsConf.unshift({
                type : "join",
                status : conf.buttons["addForPop"],
                click : function () {
                    onAddForPop(id);
                }
            });
        }
        
        var inited = !!$('#' + id)[0];
        var panelConf = {
            id: id,
            width: width,
            title: conf.title,
            initHide: true,
            // mask: false,
            closeBack: function() {
                onClose(id);
                return true;
            },
            buttons: buttonsConf
        };
        var $window = window.Panel.htmlShow(panelConf);

        if (!inited) {
            getData(conf, {
                isSelected: false,
                pageNum: 1,
                pageSize: conf.tableData && conf.tableData.pageSize
            }, function (data,textStatus,jqXHR) {
                var laterCall = createUI(id, conf, true, data);
                
                Panel.htmlShow(panelConf);
                if ($.isFunction(laterCall)) {
                    laterCall();
                }
                Panel.resize(panelConf.id);
                Loading.stop();
            });
            
            return $window;

        }else {
            reloadData(id);
            Loading.stop();
            return null;
        }
        
    }
    
    function createSearchArea($container, id, conf) {
        var searchAreaId = createId(),
            $search = $('<div id="' + searchAreaId + '" class="searchpart searchpart-close" style="width:100%; min-height: 6px;">' +
                '<a class="searchpart-ico searchpart-ico-down"></a>' +
                '<table style="display: none;"><tbody><tr></tr></tbody></table>' +
            '</div>'),
            $optionArea = $search.find("tr"),
            $selectList = $search.find("table"),
            $searchDownBtn = $search.find(".searchpart-ico-down");
        
        $search.appendTo($container);
          
        for (var i = 0, c = conf.filter.length; i < c; i++) {
            var filterConf = conf.filter[i];
            createSearchOption($optionArea, id, filterConf);
        }
        
        if (conf.filter.length > 0) {
            var $td = $('<td></td>').appendTo($optionArea);
            
            WindowButton.init([{
                type : "search",
                click : function () {
                    onSearch(id, searchAreaId);
                }
            }], $td);
        }
        
        return function () {
            QueryPanel.init(searchAreaId);
        };
        
    }
    
    function createSearchOption($container, id, filterConf) {
        ConfigData.setDefaultValue(filterConf, DEFAULT_FILTER_CONF);
        
        ConfigData.check("ButtonTable_conf.filter", filterConf, FILTER_CONF_CONSTRAINS);
        
        var label = filterConf.text,
            render = filterConf.type,
            fnRender = m_searchRender[render];
            
        $container.append('<td>' + label + '</td>');
        
        var $td = $('<td></td>').appendTo($container);
        
        if (undefined === m_searchRender[render]) {
            throw "filter type \"" + render + "\" was not provided";
        }
        
        fnRender($td, id, filterConf, function onChange (text_value) {
            var m_selectInfo = m_selectInfos[id];
            var key = filterConf.key;
            if (undefined === m_selectInfo) return;
            m_selectInfo.customFilters[key] = text_value;
        });
    }
    
    function createUpperButtonArea($container, id, conf) {
        var $buttonArea = $('<div class="search-h3 f-right-btn"></div>').appendTo($container);
        
        WindowButton.init([{
            type : "delete",
            status : conf.buttons["delete"],
            click : function () {
                if (conf.buttons["delete"] !== BUTTON_STATUS.DISABLED) {
                    onDelete(id);
                }
            }
        }, {
            type : "add", 
            status : conf.buttons.add,
            click : function () {
                onAdd(id);
            }
        }], $buttonArea);

        _showAdd = function() {
            showAdd(id);
        };
        
        // 添加域选择
        if ($.isExistArray("domain", conf.plugIn)) {
            createDomainList($buttonArea, id, conf);
        }

        if (conf.customAreaOnSelectedTop) {
            $buttonArea.append(conf.customAreaOnSelectedTop);
        }
    }
    
    function createUpperButtonAreaForPop_old($container, id, conf) {
        
        if (!$.isExistArray('domain', conf.plugIn) 
                && !$.isExistArray('addAll', conf.plugIn)) {
            return;
        }
        
        var $buttonArea = $('<div class="search-h3 f-right-btn"></div>').appendTo($container);

        // “添加”和“全部添加”按钮
        if ($.isExistArray('addAll', conf.plugIn)) {
            var buttonConf = [{
                type : "all", 
                status : conf.buttons["addAll"],
                click : function () {
                    onAddAllForPop(id);
                }
            }, {
                type : "add2",
                status : conf.buttons["addForPop"],
                click : function () {
                    onAddForPop(id);
                }
            }];
            WindowButton.init(buttonConf, $buttonArea);
        }

        // “域”选择
        if ($.isExistArray('domain', conf.plugIn)) {
            createDomainList($buttonArea, id, conf);
        }
    }
    
    function createUpperButtonAreaForPop($container, id, conf) {
        
        if (!$.isExistArray('domain', conf.plugIn) && !conf.customArea) {
            return;
        }
        
        var $buttonArea = $('<div class="search-h3 f-right-btn"></div>').appendTo($container);

        // “域”选择
        if ($.isExistArray('domain', conf.plugIn)) {
            createDomainList($buttonArea, id, conf);
        }

        // 添加自定义内容
        if (conf.customArea) {
            createCustomArea($buttonArea, conf);
        }
    }
    

    function createCustomArea($parent, conf) {
        if (conf.customArea) {
            $('<div style="float:left"></div>').appendTo($parent).append($(conf.customArea).show());
        }
    }

    function createCustomAreaOnFoot($parent, id, html) {
        $parent.find('div.pop-btngroup-bottom').append($(html).show());
    }

    function createDomainList($parent, id, conf) {
        // 添加域选择
        if ($.isExistArray('domain', conf.plugIn)) {
            var $domain = $('<div style="float:left"></div>').appendTo($parent);
            $domain.append('<span>' + (window.S_DOMAIN || '!!S_DOMAIN!!') 
                    + (window.S_LABEL_COLON || '!!S_LABEL_COLON!!') + '</span>');
            
            var $list = createList($domain, id, conf.plugInData.domainList, 
                    function onChange (text_value) {
                var m_selectInfo = m_selectInfos[id];
                var key = "domain";
                if (undefined === m_selectInfo) return;
                m_selectInfo.customFilters[key] = text_value;
                
                onDomainChange(id);
            });
            
        }
    }
    
    
    function createGridArea($container, id, conf, isPop, data) {
        var areaId = createId(),
            $gridArea = $('<div id="' + areaId + '" tableContainer="' + areaId + '"></div>')
            .appendTo($container);
        
        return createGrid(areaId, id, conf, isPop, data);
    }
    
    function createList($container, id, filterConf, onChange) {
        var number = createId(),
            headId = "search_option" + number,
            bodyId = headId + "_selectBody",
            valueList = filterConf.valueList,
            defaultValue = filterConf.value;
        
        var $list = $('<div class="select_body" id="' + headId + '" value="' + defaultValue + '">' +
            '<div class="select_text"></div>' +
            '<div class="select_trigger_wrap"><a class="select_trigger"></a></div>' +
        '</div>' +
        '<div style="display: none;" class="select_boundtree" id="' + bodyId + '">' +
            '<ul>' +
            '</ul>' +
        '</div>').appendTo($container);
        
        var $ul = $container.find("#" + bodyId + ">ul"),
            $selectHead = $container.find("#" + headId);
        
        for (var i = 0, c = valueList.length; i < c; i++) {
            var valueConf = valueList[i],
                text = valueConf.text,
                value = valueConf.value;
            
            $ul.append('<li val="' + value + '"><a>' + text +'</a></li>');
        }
        
        CustomSelect.init({
            id: headId,
            listeners:{
                selectAfter: function(text_value){
                    if ($.isFunction(onChange)) {
                        onChange(text_value);
                    }
                }
            }
        });
        
        return $list;
    }
    
    // 实例化表格组件
    function createGrid(areaId, id, conf, isPop, tableData) {
        var pageSize = isPop ? tableData.pageSize : conf.tableData && conf.tableData.pageSize;
        var colModule = isPop && conf.popColModule ? conf.popColModule : conf.colModule;
        var plugins;
        if (isPop) {
            plugins = conf.unselectTablePlugin || ["paging", "sort"];
        }else {
            plugins = conf.selectTablePlugin || ["paging", "sort"];
        }
        var tableData = isPop ? tableData : conf.tableData;
        
        var gridConf = {
                initType : 'auto',                  
                bindDomId : areaId,
                noColumn:"true",
                noTools: conf.noTools,
                // unit : "%",
                height: undefined === pageSize ? "auto" : ROWHEIGHT * pageSize,
                colModule : colModule,
                emptyText: conf.emptyText,
                plugins : plugins,
                data : tableData,
                listeners : {
                    changePage : onChangePage,
                    filter: onFilter,
                    sort : onSort
                }
            };

        if (!isPop && undefined !== conf.selectedChildren) {
            gridConf.children = conf.selectedChildren;
        }

        if (!isPop && undefined !== conf.selectedListeners) {
            gridConf.listeners = conf.selectedListeners;
        }

        var grid = ComponentManager.createComponent('GridPanel', gridConf);
        
        var selectInfo = {
            filters: grid.filters,
            sortColId: null,
            sortType: null,
            pageNum: conf.tableData && conf.tableData.pageIndex,
            pageSize: conf.tableData && conf.tableData.pageSize,
            isSelected: !isPop,
            customFilters: {}
        };
        
        m_grids[id] = grid;
        m_selectInfos[id] = selectInfo;
        
        return grid;
        

        function onChangePage(pageNum, pageSize) {
            selectInfo.pageNum = parseInt(pageNum, 10);
            selectInfo.pageSize = pageSize;
            reloadData(id, true/*不显示loading*/);
        }
        
        function onFilter(){
            selectInfo.filters = grid.filters;
            resetPage(id);
            reloadData(id, true/*不显示loading*/);
        }
        
        function onSort(sortId, sortType) {
            if (sortType) sortType = sortType.toUpperCase();
            selectInfo.sortColId = sortId;
            selectInfo.sortType = sortType;
            resetPage(id);
            reloadData(id, true);
        }
    }
    
    function getEmptyData(data) {
        var emptyData = {};
        
        $.extend(emptyData, data);
        emptyData.pageData = [];
        
        return emptyData;
    }
    
    function createBottomButtonArea($container, data) {
        return;
        var $area = $('<div class="h-nobg f-right-btn"></div>')
            .appendTo($container);

        createCancelBtn($area, data);
        createOkBtn($area, data);
    }

    function createOkBtn($container, data) {
        $('<a class="btn_body">' +
                '<div class="btn_l">' +
                '<div class="btn_r">' +
                    '<div class="btn_m"><span class="ico ico_confirm"></span> <span class="text">确定</span> <span class="pull"></span></div>' +
                '</div>' +
            '</div>' +
        '</a>').appendTo($container).click(data, onOk);
    }
    
    function createCancelBtn($container, data) {
        $('<a class="btn_body">' +
            '<div class="btn_l">' +
                '<div class="btn_r">' +
                    '<div id="closelayer2" class="btn_m"><span class="ico ico_cancel"></span> <span class="text">取消</span> <span class="pull"></span></div>' +
                '</div>' +
            '</div>' +
        '</a>').appendTo($container).click(data, onCancel);
    }
    
    function initData(id, conf) {
        var customFilters = m_selectInfos[id].customFilters,
            filters = conf.filter;
        
        for (var i = 0, c = filters.length; i < c; i++) {
            var filterConf = filters[i];
            customFilters[filterConf.key] = filterConf.value;
        }
    }

//================================================================================================//
// 业务功能
//================================================================================================//
    function onAdd(id) {
        var conf = m_confs[id];
        if (null !== conf.listeners.beforeShowAdd) {
            if (!conf.listeners.beforeShowAdd()) return;
        }

        showAdd(id);
    }
    
    function onDelete(id) {
        var data = getSelectedData(id),
            conf = m_confs[id];
        
        if (data.length > 0) {
            RiilAlert.confirm(window.S_OPERATION_CONFIRMED 
                    || '!!S_OPERATION_CONFIRMED!!', function() {
                if (data.length > 0) {
                    delData(id, data, function(resData) {
                        reloadData(id);
                        if (conf.listeners.afterSubmitDelete) {
                            conf.listeners.afterSubmitDelete(resData);
                        }
                    });
                    resetPage(id);
                }
            });
        }else {
            RiilAlert.info(window.S_OPERATION_REQUIRED 
                    || '!!S_OPERATION_REQUIRED!!');
        }
        
    }
    
    function onSearch(id, panelId) {
        reloadData(id);
        QueryPanel.collapse(panelId);
    }
    
    function getListValue(id) {
        return CustomSelect.getValue(id);
    }
    
    function onClose(id) {
        var conf = m_confs[id];
        if (conf._window) {
            Panel.hide(id);
            
            // delete m_confs[id];
            // delete m_selectInfos[id];
            // delete m_grids[id];
        }
    }
    
    function onAddForPop(id) {
        add(id, true);
    }
    
    function onAddAllForPop(id) {
        addAll(id);
        onClose(id);
    }
    
    function onDomainChange(id) {
        var selectInfo = m_selectInfos[id];
        
        selectInfo.filters = {};
        selectInfo.customFilters = {"domain" : selectInfo.customFilters.domain};
        selectInfo.sortColId = null;
        selectInfo.sortType = null;
        
        resetPage(id);
        reloadData(id);
    }
    
    function onOk(id) {
        if (add(id)) {
            onClose(id);
        }
    }
     
    function onCancel(id) {
        onClose(id);
    }
    
    function add(id, refreshAll) {
        var grid = m_grids[id],
            parentId = m_parentIds[id],
            conf = m_confs[id];
        
        var data = getSelectedData(id);
        if (data.length > 0) {
            addData(parentId, data, function(resData) {
                if (refreshAll) {
                    reloadData(id);
                }
                reloadData(parentId);
                if (conf.listeners.afterSubmitAdd) {
                    conf.listeners.afterSubmitAdd(resData);
                }
            });
            resetPage(id);
            return true;
        }else {
            RiilAlert.info(window.S_REPORT_ADD_REQUIRED || '!!S_REPORT_ADD_REQUIRED!!');
            return false;
        }
    }
    
    function addAll(id) {
        var parentId = m_parentIds[id];
        var conf = m_confs[id];
        var selectInfo = m_selectInfos[id];
        var filters = selectInfo.filters;
        var customFilters = selectInfo.customFilters;
        var allFilters = {};

        $.extend(allFilters, filters, customFilters);

        startLoading(conf.domId);
        addData(parentId, allFilters, function(resData) {
            stopLoading(conf.domId);
// 删除：全部添加时，弹出窗口要关闭，无需刷新
//            reloadData(id);
// =================================================
            reloadData(parentId);
            if (conf.listeners.afterSubmitAdd) {
                conf.listeners.afterSubmitAdd(resData);
            }
        }, true);
        resetPage(id);
    }
    
    function reloadData(id, noLoading) {
        var conf = m_confs[id],
            selectInfo = m_selectInfos[id],
            grid = m_grids[id],
            filters = selectInfo.filters,
            sortColId = selectInfo.sortColId,
            sortType = selectInfo.sortType,
            isSelected = selectInfo.isSelected,
            pageNum = selectInfo.pageNum,
            pageSize = selectInfo.pageSize,
            customFilters = selectInfo.customFilters,
            allFilters = {};
        
        $.extend(allFilters, filters, customFilters);
        
        if (!noLoading) startLoading(conf.domId);
        getData(conf, {
            filters: allFilters,
            sortColId: sortColId,
            sortType: sortType,
            isSelected: isSelected,
            pageNum: pageNum,
            pageSize: pageSize
        }, function (data,textStatus,jqXHR) {
            selectInfo.pageNum = data.pageIndex;
            selectInfo.pageSize = data.pageSize;
            grid.refreshData(data);
            if (!noLoading) stopLoading(conf.domId);
        });
    }
    

    function formatData(param, schema) {
        return FORMAT_DATA_SCHEMA[schema](param);
    }
    
    function formatDataDefault(param) {
        var filters = param.filters,
            sortColId = param.sortColId,
            sortType = param.sortType,
            searchItems = [],
            condition = {
                searchItems : searchItems,
                sortItem : null
            },
            formatedParam = {
                condition : condition,
                isSelected: param.isSelected,
                pageIndex: param.pageNum,
                pageSize: param.pageSize
            };
        
        for (var filterKey in filters) {
            if (!filters.hasOwnProperty(filterKey)) {
                continue;
            }
            
            searchItems.push({
                item : filterKey,
                value : filters[filterKey]
            });
        }
        
        if (sortColId) {
            condition.sortItem = {
                item : sortColId,
                sortType : sortType
            };
        }
        
        return formatedParam;
            
    }
    
    function getData(conf, param, callBack) {
        var url = param.isSelected ? conf.requestUrl.getSelectedData : conf.requestUrl.getUnselectedData,
            dataRefreshSchema = conf.dataRefreshSchema,
            formatData_fn = conf.callback.formatData;
        
        if (formatData_fn) {
            param = getParam(formatData_fn(param, dataRefreshSchema), conf);
        }else {
            param = getParam(formatData(param, dataRefreshSchema), conf);
        }
        
        if (conf.debug) {
            alert("ajax载入数据：" + JSON.stringify({
                url: url, param: param
            }, null, 4));
        }
        
        if (url) {
            PageCtrl.ajax({
                url: url,
                data: param,
                type: "post",
                dataType: "json",
                success : function (data) {
                    if (callBack) {
                        callBack(data.gridData);
                    }
                },
                error : function (data) {
                    if (callBack) {
                        callBack(data.gridData);
                    }
                }
            });
        }else {
            callBack(dummyGetData());
        }
    }
    
    function startLoading(domId) {
        var loadingCount = parseInt($('#' + domId).attr('masking'), 10);
        if (isNaN(loadingCount) || loadingCount <= 0) {
            Loading.start();
            loadingCount = 1;
        }else {
            loadingCount += 1;
        }
        $('#' + domId).attr('masking', loadingCount);
        console.info('startLoading:' +loadingCount);
    }
    
    function stopLoading(domId) {
        var loadingCount = parseInt($('#' + domId).attr('masking'), 10);
        // 在loadingCount未定义时调用这个函数，说明有逻辑错误
        loadingCount -= 1;
        if (loadingCount <= 0) {
            Loading.stop();
            $('#' + domId).removeAttr('masking');
        }else {
            $('#' + domId).attr('masking', loadingCount);
        }
        console.info('stopLoading:' +loadingCount);
    }
    
    function dummyGetData() {
        return {"empty":false,"first":true,"last":true,"maxPage":1,"middle":false,"msSqlServerEndIndex":6,"msSqlServerStartIndex":0,"mysqlLimitIndex":0,"mysqlLimitOffset":20,"oracleEndIndex":6,"oracleStartIndex":0,"pageData":[{"UUIDKeyName":"","availStatus":"-1","availStatus4Display":"不可用","availStatuss":[],"busy":"-","busy4Display":"-","busyStatus":"Na","busyStatus4Display":"未知","cmdb":true,"companyId":"","contact":"","contactAddr":"","contactName":"-","contactPhone":"","cpuRate":"-","cpuRate4Display":"-","desc":"","devType":"Host","devTypeName":"主机","domainId":"domain-root","domainName":"集团","endIndex":0,"gatherIfIndex":"","id":"4440a352-ec0c-d009-fbb0-d262fb0e05c1","ip":"172.16.36.212","ip4Display":"172.16.36.212","ipList":"","ipNumber":"0000000000000000000000000000002886739156","ipNumber4Display":"","ipNumberV6":"0000000000000000000000000000002886739156","ipV6":"","isCmdb":1,"isMain":0,"isManaged":1,"key":"4440a352-ec0c-d009-fbb0-d262fb0e05c1","lastGatherTime":{"date":29,"day":2,"hours":14,"minutes":26,"month":4,"seconds":0,"time":1338272760000,"timezoneOffset":-480,"year":112},"latestBizePojo":null,"location":"","locationName":"","mac":"00:1d:0f:1e:45:70","managed":true,"memRate":"-","memRate4Display":"-","metricDataMap":null,"modelId":"RIIL_RMM_HOST_WINDOWS_SNMP","modelNumber":"Windows server","moniTempId":"RIIL_RMT_HOST_WINDOWS","moniTempName":"Windows主机","name":"1-BE0080B434664","nameDisplay":"1-BE0080B434664","nameEn":"","orderBy":"","pageCountSqlId":"","pageIndex":1,"pageSize":10,"pageSqlId":"","policyId":"RIIL_RMP_RES_HOST_WINDOWS_SNMP_DEFAULT","policyName":"Windows监控策略","resCatalog":"Host","resGroupId":"","series":"Windows server","serverId":"172.16.36.25:18002","serverName":"RIIL Monitor Server","sortColumn":[],"sortType":null,"source":"","startIndex":0,"subInstId":"","subInstIndex":"","sysoid":"1.3.6.1.4.1.311.1.1.3.1.2","tag1":"","tag2":"","tag3":"","tag4":"","treeNodeId":"00.01.01","vendorId":"Microsoft","vendorIds":[],"vendorName":"微软"},{"UUIDKeyName":"","availStatus":"-1","availStatus4Display":"不可用","availStatuss":[],"busy":"-","busy4Display":"-","busyStatus":"Na","busyStatus4Display":"未知","cmdb":false,"companyId":"","contact":"","contactAddr":"","contactName":"-","contactPhone":"","cpuRate":"-","cpuRate4Display":"-","desc":"","devType":"Host","devTypeName":"主机","domainId":"domain-root","domainName":"集团","endIndex":0,"gatherIfIndex":"","id":"642ca066-fde4-61b2-c0a8-98706a60011b","ip":"172.16.13.113","ip4Display":"172.16.13.113","ipList":"","ipNumber":"0000000000000000000000000000002886733169","ipNumber4Display":"","ipNumberV6":"0000000000000000000000000000002886733169","ipV6":"","isCmdb":-1,"isMain":0,"isManaged":1,"key":"642ca066-fde4-61b2-c0a8-98706a60011b","lastGatherTime":{"date":29,"day":2,"hours":14,"minutes":26,"month":4,"seconds":0,"time":1338272760000,"timezoneOffset":-480,"year":112},"latestBizePojo":null,"location":"","locationName":"","mac":"f0:4d:a2:29:f7:6a","managed":true,"memRate":"-","memRate4Display":"-","metricDataMap":null,"modelId":"RIIL_RMM_HOST_WINDOWS_SNMP","modelNumber":"Windows workstation","moniTempId":"RIIL_RMT_HOST_WINDOWS","moniTempName":"Windows主机","name":"R02145","nameDisplay":"R02145","nameEn":"","orderBy":"","pageCountSqlId":"","pageIndex":1,"pageSize":10,"pageSqlId":"","policyId":"RIIL_RMP_RES_HOST_WINDOWS_SNMP_DEFAULT","policyName":"Windows监控策略","resCatalog":"Host","resGroupId":"","series":"Windows workstation","serverId":"172.16.36.25:18002","serverName":"RIIL Monitor Server","sortColumn":[],"sortType":null,"source":"","startIndex":0,"subInstId":"","subInstIndex":"","sysoid":"1.3.6.1.4.1.311.1.1.3.1.1","tag1":"","tag2":"","tag3":"","tag4":"","treeNodeId":"00.01.01","vendorId":"Microsoft","vendorIds":[],"vendorName":"微软"},{"UUIDKeyName":"","availStatus":"-1","availStatus4Display":"不可用","availStatuss":[],"busy":"-","busy4Display":"-","busyStatus":"Na","busyStatus4Display":"未知","cmdb":false,"companyId":"","contact":"","contactAddr":"","contactName":"-","contactPhone":"","cpuRate":"-","cpuRate4Display":"-","desc":"","devType":"Host","devTypeName":"主机","domainId":"domain-root","domainName":"集团","endIndex":0,"gatherIfIndex":"","id":"79adc8df-cb68-5835-7733-ff4c0694d52b","ip":"172.16.36.213","ip4Display":"172.16.36.213","ipList":"","ipNumber":"0000000000000000000000000000002886739157","ipNumber4Display":"","ipNumberV6":"0000000000000000000000000000002886739157","ipV6":"","isCmdb":-1,"isMain":0,"isManaged":1,"key":"79adc8df-cb68-5835-7733-ff4c0694d52b","lastGatherTime":{"date":29,"day":2,"hours":14,"minutes":26,"month":4,"seconds":0,"time":1338272760000,"timezoneOffset":-480,"year":112},"latestBizePojo":null,"location":"","locationName":"","mac":"00:50:56:c0:00:08","managed":true,"memRate":"-","memRate4Display":"-","metricDataMap":null,"modelId":"RIIL_RMM_HOST_WINDOWS_SNMP","modelNumber":"Windows server","moniTempId":"RIIL_RMT_HOST_WINDOWS","moniTempName":"Windows主机","name":"RUIJIE-FFDF9A37","nameDisplay":"RUIJIE-FFDF9A37","nameEn":"","orderBy":"","pageCountSqlId":"","pageIndex":1,"pageSize":10,"pageSqlId":"","policyId":"RIIL_RMP_RES_HOST_WINDOWS_SNMP_DEFAULT","policyName":"Windows监控策略","resCatalog":"Host","resGroupId":"","series":"Windows server","serverId":"172.16.36.25:18002","serverName":"RIIL Monitor Server","sortColumn":[],"sortType":null,"source":"","startIndex":0,"subInstId":"","subInstIndex":"","sysoid":"1.3.6.1.4.1.311.1.1.3.1.2","tag1":"","tag2":"","tag3":"","tag4":"","treeNodeId":"00.01.01","vendorId":"Microsoft","vendorIds":[],"vendorName":"微软"},{"UUIDKeyName":"","availStatus":"-1","availStatus4Display":"不可用","availStatuss":[],"busy":"-","busy4Display":"-","busyStatus":"Na","busyStatus4Display":"未知","cmdb":false,"companyId":"","contact":"","contactAddr":"","contactName":"-","contactPhone":"","cpuRate":"-","cpuRate4Display":"-","desc":"","devType":"Host","devTypeName":"主机","domainId":"domain-root","domainName":"集团","endIndex":0,"gatherIfIndex":"","id":"dfcd29f8-de74-e47c-6d2c-4b5a44a6e284","ip":"172.16.13.250","ip4Display":"172.16.13.250","ipList":"","ipNumber":"0000000000000000000000000000002886733306","ipNumber4Display":"","ipNumberV6":"0000000000000000000000000000002886733306","ipV6":"","isCmdb":-1,"isMain":0,"isManaged":1,"key":"dfcd29f8-de74-e47c-6d2c-4b5a44a6e284","lastGatherTime":{"date":29,"day":2,"hours":14,"minutes":26,"month":4,"seconds":0,"time":1338272760000,"timezoneOffset":-480,"year":112},"latestBizePojo":null,"location":"","locationName":"","mac":"00:0c:29:73:63:7a","managed":true,"memRate":"-","memRate4Display":"-","metricDataMap":null,"modelId":"RIIL_RMM_HOST_WINDOWS_SNMP","modelNumber":"Windows workstation","moniTempId":"RIIL_RMT_HOST_WINDOWS","moniTempName":"Windows主机","name":"RIILSERVER","nameDisplay":"RIILSERVER","nameEn":"","orderBy":"","pageCountSqlId":"","pageIndex":1,"pageSize":10,"pageSqlId":"","policyId":"RIIL_RMP_RES_HOST_WINDOWS_SNMP_DEFAULT","policyName":"Windows监控策略","resCatalog":"Host","resGroupId":"","series":"Windows workstation","serverId":"172.16.36.25:18002","serverName":"RIIL Monitor Server","sortColumn":[],"sortType":null,"source":"","startIndex":0,"subInstId":"","subInstIndex":"","sysoid":"1.3.6.1.4.1.311.1.1.3.1.1","tag1":"","tag2":"","tag3":"","tag4":"","treeNodeId":"00.01.01","vendorId":"Microsoft","vendorIds":[],"vendorName":"微软"},{"UUIDKeyName":"","availStatus":"-1","availStatus4Display":"不可用","availStatuss":[],"busy":"-","busy4Display":"-","busyStatus":"Na","busyStatus4Display":"未知","cmdb":true,"companyId":"","contact":"user-admin","contactAddr":"","contactName":"系统管理员","contactPhone":"","cpuRate":"-","cpuRate4Display":"-","desc":"","devType":"","devTypeName":"","domainId":"domain-root","domainName":"集团","endIndex":0,"gatherIfIndex":"","id":"e8b554e1-2eee-0a19-2926-5c7454d81861","ip":"172.16.36.23","ip4Display":"172.16.36.23","ipList":"","ipNumber":"0000000000000000000000000000002886738967","ipNumber4Display":"","ipNumberV6":"0000000000000000000000000000002886738967","ipV6":"","isCmdb":1,"isMain":0,"isManaged":1,"key":"e8b554e1-2eee-0a19-2926-5c7454d81861","lastGatherTime":{"date":29,"day":2,"hours":14,"minutes":26,"month":4,"seconds":0,"time":1338272760000,"timezoneOffset":-480,"year":112},"latestBizePojo":null,"location":"c18d8567-3b23-871e-ef16-247efebf68c5","locationName":"中国/天津","mac":"","managed":true,"memRate":"-","memRate4Display":"-","metricDataMap":null,"modelId":"RIIL_RMM_PORT","modelNumber":"","moniTempId":"RIIL_RMT_PORT","moniTempName":"Port","name":"172.16.36.23port","nameDisplay":"172.16.36.23port80","nameEn":"","orderBy":"","pageCountSqlId":"","pageIndex":1,"pageSize":10,"pageSqlId":"","policyId":"RIIL_RMP_RES_PORT_DEFAULT","policyName":"Port","resCatalog":"BaseService","resGroupId":"","series":"","serverId":"172.16.36.25:18002","serverName":"","sortColumn":[],"sortType":null,"source":"","startIndex":0,"subInstId":"","subInstIndex":"","sysoid":"","tag1":"","tag2":"","tag3":"","tag4":"","treeNodeId":"00.05.03","vendorId":"Other","vendorIds":[],"vendorName":"其他"},{"UUIDKeyName":"","availStatus":"-1","availStatus4Display":"不可用","availStatuss":[],"busy":"-","busy4Display":"-","busyStatus":"Na","busyStatus4Display":"未知","cmdb":false,"companyId":"","contact":"","contactAddr":"","contactName":"-","contactPhone":"","cpuRate":"-","cpuRate4Display":"-","desc":"","devType":"Host","devTypeName":"主机","domainId":"domain-root","domainName":"集团","endIndex":0,"gatherIfIndex":"","id":"fb8b0413-56ea-ded0-6be1-cbcd2b9ad6f2","ip":"172.16.36.44","ip4Display":"172.16.36.44","ipList":"","ipNumber":"0000000000000000000000000000002886738988","ipNumber4Display":"","ipNumberV6":"0000000000000000000000000000002886738988","ipV6":"","isCmdb":-1,"isMain":0,"isManaged":1,"key":"fb8b0413-56ea-ded0-6be1-cbcd2b9ad6f2","lastGatherTime":{"date":29,"day":2,"hours":14,"minutes":26,"month":4,"seconds":0,"time":1338272760000,"timezoneOffset":-480,"year":112},"latestBizePojo":null,"location":"","locationName":"","mac":"00:13:21:5b:15:33","managed":true,"memRate":"-","memRate4Display":"-","metricDataMap":null,"modelId":"RIIL_RMM_HOST_HPUX_SNMP","modelNumber":"rx2620","moniTempId":"RIIL_RMT_HOST_HPUX","moniTempName":"HP-UX OS","name":"rx2620","nameDisplay":"rx2620","nameEn":"","orderBy":"","pageCountSqlId":"","pageIndex":1,"pageSize":10,"pageSqlId":"","policyId":"RIIL_RMP_RES_HOST_HPUX_SNMP_DEFAULT","policyName":"HPUX 主机(SNMP)","resCatalog":"Host","resGroupId":"","series":"HP-UX","serverId":"172.16.36.25:18002","serverName":"RIIL Monitor Server","sortColumn":[],"sortType":null,"source":"","startIndex":0,"subInstId":"","subInstIndex":"","sysoid":"1.3.6.1.4.1.11.2.3.2.6","tag1":"","tag2":"","tag3":"","tag4":"","treeNodeId":"00.01.06","vendorId":"HP","vendorIds":[],"vendorName":"惠普"}],"pageIndex":1,"pageSize":10,"recordCount":6};
    }
    
    function resetPage(id) {
        var selectInfo = m_selectInfos[id];
        selectInfo.pageNum = 1;
    }
    
    function addData(id, data, onFinished, isAll) {
        var conf = m_confs[id],
            url = conf.requestUrl.addData,
            addtionData;
        
        if (null !== conf.listeners.beforeSubmitAdd) {
            addtionData = conf.listeners.beforeSubmitAdd();
        }
        
        var param;
        
        if (isAll) {
            param = getParam({filter: data}, conf, addtionData);
        }else {
            param = getParam({data: data}, conf, addtionData);
        }


        if (conf.debug) {
            alert("ajax添加数据：" + JSON.stringify({
                url: url, param: param
            }, null, 4));
        }
        
        if (url) {
            startLoading(conf.domId);
            PageCtrl.ajax({
                url: url,
                data: param,
                type: "post",
                dataType: "json",
                success: function(data,textStatus,jqXHR) {
                    stopLoading(conf.domId);
                    onFinished(data);
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    stopLoading(conf.domId);
                    onFinished(errorThrown);
                }
            });
        }else {
            onFinished();
        }
    }
    
    function delData(id, data, onFinished) {
        var conf = m_confs[id],
            url = conf.requestUrl.delData,
            param = getParam({data: data}, conf);

        if (conf.debug) {
            alert("ajax删除数据：" + JSON.stringify({
                url: url, param: param
            }, null, 4));
        }
            
        if (url) {
            PageCtrl.ajax({
                url: url,
                data: param,
                type: "post",
                dataType: "json",
                success: function(resData,textStatus,jqXHR) {
                    onFinished(resData);
                }
            });
        }
    }
    
    function getSelectedData(id) {
        var grid = m_grids[id],
            conf = m_confs[id],
            checkColId = conf.checkColId,
            rowData = grid.getCheckedDatas(checkColId);
        
        return rowData;
    }
    
    function getParam(obj, conf, addtionData) {
       // url参数方式
       //------------------------------------------------------------------
       var param = "";
       var and = encodeURIComponent('&');
       var percent = encodeURIComponent('%');
       
       for (var key in obj) {
           param += '&' + key + "=" + encode(JSON.stringify(obj[key]));
       }
       
       if (null !== conf.data && undefined !== conf.data) {
           for (var key in conf.data) {
               param += '&' + key + "=" + encode(JSON.stringify(conf.data[key]));
           }
       }
       
       if (null !== addtionData && undefined !== addtionData) {
           for (var key in addtionData) {
               param += '&' + key + "=" + encode(JSON.stringify(addtionData[key]));
           }
       }
       
       if (param) param = param.substring(1);
       
       function encode(str) {
          if (!str) return str;
          str = str.replace(/&/g, and);
          str = str.replace(/%/g, percent);
          return str;
       }
        
       // 参数方式
       //------------------------------------------------------------------
        // var paramObj = $.extend(obj, conf.data, addtionData);
        
        // var param = {};
        // var paramValue, paramKey;
        
        // for (paramKey in paramObj) {
            // if (!paramKey || !paramObj.hasOwnProperty(paramKey)) continue;
            // paramValue = paramObj[paramKey];
            // if ($.isPlainObject(paramValue)) paramValue = JSON.stringify(paramValue);
            // else paramValue = '' + paramValue;
            // param[paramKey] = paramValue;
        // }
        
        return param;
    }
}();;if (!window.TimeScope) {

/**
 * 时间段选择器
 * 
 *     TimeScope.init({
 *         id : "exsample",
 *         startTime : "2013-01-01 08:11",
 *         endTime : "2013-03-01 08:11",
 *         frequency : "1D",
 *         textMap : {
 *             TITLE : "-设定时间段",
 *             TIME_REQUIRED : "TIME_REQUIRED开始和结束时间必须有值",
 *             END_LT_START : "END_LT_START结束时间必须在开始时间之后",
 *             START_TIME : "-开始时间",
 *             END_TIME : "-结束时间",
 *             FREQUENCY : "-频度",
 *             NO_EMPTY : "不能为空"
 *         },
 *         listeners : {
 *             startTimeChange : function (startTime, endTime) {
 *                 alert("开始时间切换：" + startTime + " <--> " + endTime);
 *             },
 *             endTimeChange : function (startTime, endTime) {
 *                 alert("结束时间切换：" + startTime + " <--> " + endTime);
 *             },
 *             frequencyChange : function (value) {
 *                 alert("频度切换：" + value);
 *             },
 *             ok : function(value) {
 *                 alert("OK:" + JSON.stringify(value, null, '    '));
 *             },
 *             cancel : function(value) {
 *                 alert("CANCEL:" + JSON.stringify(value, null, '    '));
 *             }
 *         }
 *     });
 *     
 * @class TimeScope
 */
window.TimeScope = function () {
    
    //-------------------------------------------------------------------------------------------
    // 常量定义
    //-------------------------------------------------------------------------------------------
    var S_CONF_CONSTRAINS = {
            type:["object"], elems:{
                id : {type:["string"]},
                left : {type:["number"], required:false},
                top : {type:["number"], required:false},
                startTime : {type:["string"], required:false},
                endTime : {type:["string"], required:false},
                frequency : {type:["string"], required:false},
                textMap : {type:["object"], elems:{
                    TITLE : {type:["string"]},
                    TIME_REQUIRED : {type:["string"]},
                    END_LT_START : {type:["string"]},
                    START_TIME : {type:["string"]},
                    END_TIME : {type:["string"]},
                    FREQUENCY : {type:["string"]},
                    NO_EMPTY : {type:["string"]}
                }},
                listeners : {type:["object"], elems:{
                    startTimeChange : {type:["function"], required:false},
                    endTimeChange : {type:["function"], required:false},
                    frequencyChange : {type:["function"], required:false},
                    ok : {type:["function"], required:false},
                    cancel : {type:["function"], required:false}
                }}
            }
        },
        S_ID_PREFIX = 'TimeScope_',
        LIST_ID = '_collectionIntervalSelect',
        S_UNIT = {
            M : 1000 * 60,     // 一分钟毫秒值
            H : 1000 * 60 * 60, // 一小时毫秒值
            D : 1000 * 60 * 60 * 24  // 一天毫秒值
        },
        S_TEXT = {
            "1M" : "1" + (window.S_MINUTE || '!!S_MINUTE!!'),
            "1H" : "1" + (window.S_HOUR || '!!S_HOUR!!'),
            "6H" : "6" + (window.S_HOUR || '!!S_HOUR!!'),
            "1D" : "1" + (window.S_DAY || '!!S_DAY!!'),
            "7D" : "1" + (window.S_WEEKS || '!!S_WEEKS!!'),
            "30D" : "1" + (window.S_MONTH || '!!S_MONTH!!'),
            "60D" : "2" + (window.S_MONTH || '!!S_MONTH!!'),
            "365D" : "365天" + (window.S_DAY || '!!S_DAY!!')
        },
        S_LIST_SCHEMA = {
            _ : function (){
                var textMap = {
                        MIN: "1" + (window.S_MINUTE || '!!S_MINUTE!!'),
                        HOUR: "1" + (window.S_HOUR || '!!S_HOUR!!'),
                        HOUR6: "6" + (window.S_HOUR || '!!S_HOUR!!'),
                        DAY: "1" + (window.S_DAY || '!!S_DAY!!'),
                        WEEK: "1" + (window.S_WEEKS || '!!S_WEEKS!!'),
                        MONTH: "1" + (window.S_MONTH || '!!S_MONTH!!')
                    },
                    type1h = ["MIN"],
                    type6h = ["MIN", "HOUR"],
                    type1d = ["HOUR", "HOUR6"],
                    type7d = ["HOUR", "HOUR6", "DAY"],
                    type30d = ["DAY", "WEEK"],
                    type60d = ["DAY", "WEEK", "MONTH"],
                    h1s = getMilisecond("1H"),
                    h6s = getMilisecond("6H"),
                    d1s = getMilisecond("1D"),
                    d7s = getMilisecond("7D"),
                    d60s = getMilisecond("60D");
                
                return getValue;     
                
                function getValue(interval) {
                    var listData;
                    if(interval<=h1s){
                        listData = getListData(type1h);
                    }else if(h1s<interval && interval<=h6s){
                        listData = getListData(type6h);
                    }else if(h6s<interval && interval<=d1s){
                        listData = getListData(type1d);
                    }else if(d1s<interval && interval<=d7s){
                        listData = getListData(type7d);
                    }else if(d7s<interval && interval<=d60s){
                        listData = getListData(type30d);
                    }else if(d60s<=interval){
                        listData = getListData(type60d);
                    }
                    return listData;
                }
                
                function getListData (type){
                    var listData = [],
                        contentList = type,
                        dataCount = contentList.length,
                        val,
                        text,
                        i;
                    
                    for (i = 0; i < dataCount; i++) {
                        val = contentList[i];
                        text = textMap[val];
                        listData.push({
                            text : text,
                            value : val
                        });
                    }
                    
                    return listData;
                }
            }()
        };

    //-------------------------------------------------------------------------------------------
    // API定义
    //-------------------------------------------------------------------------------------------
    return {
        /**
         * 初始化
         * 
         *     TimeScope.init({
         *         id : "exsample",
         *         startTime : "2013-01-01 08:11",
         *         endTime : "2013-03-01 08:11",
         *         frequency : {
         *             valueList : [{
         *                 text: "5分钟",
         *                 value: "5MIN"
         *             },{
         *                 text: "1小时",
         *                 value: "1H"
         *             },{
         *                 text: "1天",
         *                 value: "1D"
         *             },{
         *                 text: "1月",
         *                 value: "1MON"
         *             }],
         *             defaultValue : "1D"
         *             
         *         },
         *         textMap : {
         *             TITLE : "-设定时间段",
         *             TIME_REQUIRED : "TIME_REQUIRED开始和结束时间必须有值",
         *             END_LT_START : "END_LT_START结束时间必须在开始时间之后",
         *             START_TIME : "开始时间",
         *             END_TIME : "结束时间",
         *             FREQUENCY : "频度",
         *             NO_EMPTY : "不能为空"
         *         },
         *         listeners : {
         *             startTimeChange : function (startTime, endTime) {
         *                 alert("开始时间切换：" + startTime + " <--> " + endTime);
         *             },
         *             endTimeChange : function (startTime, endTime) {
         *                 alert("结束时间切换：" + startTime + " <--> " + endTime);
         *             },
         *             frequencyChange : function (value) {
         *                 alert("频度切换：" + value);
         *             },
         *             ok : function(value) {
         *                 alert("OK:" + JSON.stringify(value, ' ', 4));
         *             },
         *             cancel : function(value) {
         *                 alert("CANCEL:" + JSON.stringify(value, ' ', 4));
         *             }
         *         }
         *     });
         * 
         * @method init
         * @param conf {json} 配置信息
         * @param conf.id {String} dom ID
         * @param [conf.startTime] {String} 开始时间
         * @param [conf.endTime] {String} 结束时间
         * @param conf.frequency {String} 频度选项的显示文本
         * @param conf.textMap {json} 界面文字
         * @param conf.textMap.TITLE {string} 标题　
         * @param conf.textMap.START_TIME {string} 开始时间　
         * @param conf.textMap.END_TIME {string} 结束时间　
         * @param conf.textMap.FREQUENCY {string} 频度　
         * @param conf.textMap.TIME_REQUIRED {string} 开始时间和结束时间必须有值！　
         * @param conf.textMap.NO_EMPTY {string} 不能为空
         * @param conf.listeners {josn} 回调事件
         * @param conf.listeners.startTimeChange {function} 更改开始时间时调用
         * @param conf.listeners.endTimeChange {function} 更改结束时间时调用
         * @param conf.listeners.frequencyChange {function} 更改频度时调用
         * @param conf.listeners.ok {function} 点击确定时调用
         * @param conf.listeners.cancel {function} 点击取消时调用
         * 
         *     // 确定和取消回调函数有一个参数，它的内容如下：
         *     {
         *         "startTime": "2013-01-01 08:11",
         *         "endTime": "2013-03-01 08:11",
         *         "frequency": "1D"
         *     }
         */
        init : init,
        
        
        /**
         * 获取界面配置的值
         * 
         *     var value = TimeScope.getValue("sample");
         * 
         * @method getValue
         * @param id {string} dom id
         * @return {json} 界面配置的值
         * 
         *     // 内容如下：
         *     {
         *         "startTime": "2013-01-01 08:11",
         *         "endTime": "2013-03-01 08:11",
         *         "frequency": "1D"
         *     }
         */
        getValue : getValue,
        
        
        /**
         * 关闭
         * 
         *     TimeScope.close("sample");
         * 
         * @method close
         * @param id {string} dom id
         */
        close : close
        
    };
    
    

    //-------------------------------------------------------------------------------------------
    // API函数
    //-------------------------------------------------------------------------------------------
    function init(conf) {
        if (undefined === conf) return;
        
        ConfigData.check("TimeScope config", conf, S_CONF_CONSTRAINS);
        
        conf.listConf = {
            id: S_ID_PREFIX + conf.id + LIST_ID,
            listeners : {}
        };
        
        var html_s = createStructText(conf),
            buttons = createButtonConfig(conf);
        
        Panel.htmlShow({
            id: conf.id,
            left: conf.left,
            top: conf.top,
            width: 300,
            style: "layer bgblue:panel",
            content: html_s,
            mask: false,
            closeBack: function() {
//                console.log("关闭事件");
            },
            buttons: buttons
        });
        
        $('#' + conf.id).data(S_ID_PREFIX + "config", conf);

        
        initTimeCtrl('startTime', function () {
            var lastValue = $('#startTime').data(S_ID_PREFIX + "_lastValue");
            var newValue = $('#startTime').val();
            if (newValue && lastValue !== newValue) {
                $('#startTime').data(S_ID_PREFIX + "_lastValue", newValue);
                onTimeChange(conf, conf.listeners.startTimeChange);
            }
        });
        
        if (!$.isNull(conf.startTime)) {
            $('#startTime').data(S_ID_PREFIX + "_lastValue", 
                    $.parseDate(conf.startTime));
        }
        
        initTimeCtrl('endTime', function () {
            var lastValue = $('#endTime').data(S_ID_PREFIX + "_lastValue");
            var newValue = $('#endTime').val();
            if (newValue && lastValue !== newValue) {
                $('#endTime').data(S_ID_PREFIX + "_lastValue", newValue);
                onTimeChange(conf, conf.listeners.endTimeChange);
            }
        });
        if (!$.isNull(conf.endTime)) {
            $('#endTime').data(S_ID_PREFIX + "_lastValue", 
                    $.parseDate(conf.endTime));
        }
        
        initFrequency(conf);
        
        onTimeChange(conf, conf.listeners.startTimeChange || conf.listeners.endTimeChange);
    }
    
    function getValue(conf) {
        var id = $.isString(conf) ? conf : conf.id;
        var startTime_s = getStartTime(id);
        var endTime_s = getEndTime(id);
        var startTime_i;
        var endTime_i;
        
        if (startTime_s) {
            startTime_i = $.parseDate(startTime_s);
        }
        
        if (endTime_s) {
            endTime_i = $.parseDate(endTime_s);
        }
        
        return {
            startTime : startTime_s,
            startTime_i : startTime_i,
            endTime : endTime_s,
            endTime_i : endTime_i,
            frequency : getFrequency(id)
        };
    }
    
    function close (id) {
        Panel.close(id);
    }
    
    function getStartTime(id) {
        return $('#' + id).find("#startTime").val();
    }
    
    function getEndTime(id) {
        return $('#' + id).find("#endTime").val();
    }
    
    function getFrequency(conf) {
        conf = getConfig(conf);
        
        return CustomSelect.getValue(conf.listConf.id);
    }
    

    //-------------------------------------------------------------------------------------------
    // 私有函数
    //-------------------------------------------------------------------------------------------

    /**
     * 设置开始时间的值
     * 
     *     TimeScope.setStartTime("sample", "2013-01-01 08:11");
     *     
     * @method setStartTime
     * @private
     * @param id {string} dom id
     * @param value {string} 日期时间信息
     */
    function setStartTime (id, value_s) {
        $html.find('#' + id).find('#startTime').val(value_s);
    }
    
    
    /**
     * 设置结束时间的值
     * 
     *     TimeScope.setEndTime("sample", "2013-03-01 08:11");
     *     
     * @method setEndTime
     * @private
     * @param id {string} dom id
     * @param value {string} 日期时间信息
     */
    function setEndTime (id, value_s) {
        $html.find('#' + id).find('#endTime').val(value_s);
    }
    
    
    /**
     * 设置频度列表
     * 
     *     TimeScope.setFrequencyList("sample", [{
     *         text : "一天",
     *         value : "1D"
     *     }, {
     *         text : "二天",
     *         value : "2D"
     *     }, {
     *         text : "三天",
     *         value : "3D"
     *     }], "1D");
     *     
     * @method setFrequencyList
     * @private
     * @param id {string} dom id
     * @param valueList {Array} 频度列表中的内容
     * @param defaultValue {String} 选中的值
     */
    function setFrequencyList (conf, valueList_a, defaultValue_s) {
        conf = getConfig(conf);
        
        var listConf = conf.listConf,
            listId_s = listConf.id,
            html_a = ['<ul>'];
            
        for (var i = 0, c = valueList_a.length; i < c; i++) {
            var timeType = valueList_a[i],
                value = timeType.value,
                text = timeType.text;
            html_a.push('<li val="' + value + '"><a>' + text + '</a></li>');
        }
        html_a.push('</ul>');
        
        CustomSelect.refresh(listConf, html_a.join(''), defaultValue_s);
    }
    
    
    /**
     * 设置频度的值
     * 
     *     TimeScope.setFrequency("sample", "1D");
     * 
     * @method setFrequency
     * @private
     * @param id {string} dom id
     * @param value {String} 频度值
     */
    function setFrequency (conf, value_s) {
        conf = getConfig(conf);

        CustomSelect.setValue(conf.listConf.id, value_s);
    }
    
    function getMilisecond (value) {
        var len = value.length,
            num = value.substring(0, len - 1),
            unit = value.substring(len - 1).toUpperCase();
        
        return S_UNIT[unit] * parseInt(num, 10);
    }
    
    function getConfig(conf) {
        if ($.isString(conf)) {
            return $('#' + conf).data(S_ID_PREFIX + "config");
        }else {
            return conf;
        }
    }
        
    function createStructText(conf) {
        var textMap = conf.textMap,
            valueList_a = conf.frequency.valueList,
            listId_s = conf.listConf.id,
            defaultValue_s = conf.frequency.defaultValue,
            customSelectStr = CustomSelect.getTemplate(listId_s, listId_s, []);
        
        var startTime, endTime;
        if (!$.isNull(conf.startTime)) {
            startTime = conf.startTime;
        }else {
            startTime = "";
        }
        if (!$.isNull(conf.startTime)) {
            endTime = conf.endTime;
        }else {
            endTime = "";
        }
        
        var html_s = [
            '<div class="listtable" id="', conf.id, '" style="width:300px;">',
            '    <table id="validateArea" border="0" cellpadding="0" cellspacing="0">',
            '    <tbody>',
            '    <tr>',
            '        <td width="30%">', textMap.START_TIME, '</td>',
            '        <td width="10%">：</td>',
            '        <td width="60%">',
            '        <div class="time_body" id="startTimeDiv">',
            '            <input readonly="readonly" id="startTime" class="time_text" value="', startTime, '" validate="reqiured," reqiurederrormsg="', textMap.NO_EMPTY, '">',
            '            <div class="time_trigger_wrap">',
            '                <a class="time_trigger" id="startTimeButton"></a>',
            '            </div>',
            '        </div>',
            '        </td>',
            '    </tr>',
            '    <tr>',
            '        <td>', textMap.END_TIME, '</td>',
            '        <td>：</td>',
            '        <td>',
            '        <div class="time_body" id="endTimeDiv">',
            '            <input readonly="readonly" id="endTime" class="time_text" value="', endTime, '" validate="reqiured," reqiurederrormsg="', textMap.NO_EMPTY, '">',
            '            <div class="time_trigger_wrap">',
            '                <a class="time_trigger" id="endTimeButton"></a>',
            '            </div>',
            '        </div>',
            '        </td>',
            '    </tr>',
            '    <tr>',
            '        <td>', textMap.FREQUENCY, '</td>',
            '        <td>：</td>',
            '        <td>', customSelectStr, '</td>',
            '    </tr>',
            '    </tbody>',
            '    </table>',
            '</div>'
        ].join('');
        
        return html_s;
    }
    
    function initTimeCtrl(ctrlId, onSelect) {
        
        loadcalendarConf({
            input : ctrlId,
            trig : ctrlId +'Button',
            format : '%Y-%m-%d %H:%M',
            week : false,
            time : true,
            min : null,
            max : ServerTime + "",
            onSelect : onSelect
        }); 
    }
    
    function initFrequency(conf) {
        var onChange = conf.listeners.frequencyChange,
            listConf = conf.listConf;
        
        if (onChange) {
            listConf.listeners.selectAfter = function () {
                var lastValue = $('#' + listConf.id).data(S_ID_PREFIX + "_lastValue");
                var newValue = getFrequency(conf);
                if (newValue && lastValue !== newValue) {
                    $('#' + listConf.id).data(S_ID_PREFIX + "_lastValue", newValue);
                    onChange(getFrequency(conf));
                }
            };
        }
        
        CustomSelect.init(listConf);
        
        $('#' + listConf.id).data(S_ID_PREFIX + "_lastValue", conf.frequency);
    }
    
    function createButtonConfig(conf) {
        var $buttonArea = $('<div class="f-right-btn"></div>'),
            callBackOk = conf.listeners.ok,
            callBackCancel = conf.listeners.cancel,
            onOk,
            onCancel,
            fnClose = function () {
                close(conf.id);
            };
        
        if (callBackOk) {
            onOk = function () {
                var value = getValue(conf);
                var interVal_i = value.endTime_i - value.startTime_i;
                    
                if(!value.endTime || !value.startTime){
                    RiilAlert.info(conf.textMap.TIME_REQUIRED);//"开始时间或结束时间不能为空!"
                    return;
                }
                
                if (undefined !== interVal_i && !isNaN(interVal_i) && interVal_i <= 0){
                    RiilAlert.info(conf.textMap.END_LT_START);//"开始时间必须小于结束时间。"
                    return;
                }
                
                if (!callBackOk(value)) {
                    fnClose();
                }
            };
        }else {
            onOk = fnClose;
        }
        
        if (callBackCancel) {
            onCancel = function () {
                if (!callBackCancel(getValue(conf))) {
                    fnClose();
                }
            };
        }else {
            onCancel = fnClose;
        }
        
        return [{
            type : "ok",
            click : onOk
        }, {
            type : "cancel",
            click : onCancel
        }];
    }
    
    
    function getListData(schema, interval){
        if (!schema) {
            schema = "_";
        }
        
        return S_LIST_SCHEMA[schema](interval);
    }
    

    //-------------------------------------------------------------------------------------------
    // 事件处理
    //-------------------------------------------------------------------------------------------
    function onTimeChange (conf, onChange) {
        var textMap = conf.textMap,
            endTime_s = getEndTime(conf.id),
            startTime_s = getStartTime(conf.id),
            endTime_i,
            startTime_i,
            interval_i,
            listData;
        
        if (!$.isNull(endTime_s)) {
            endTime_i=$.parseDate(endTime_s);
        }
        if (!$.isNull(startTime_s)) {
            startTime_i=$.parseDate(startTime_s);
        }
        
        if (undefined !== endTime_i && undefined !== startTime_i) {
            interval_i = endTime_i - startTime_i;
        }
        
        if (undefined !== interval_i && interval_i <= 0){
            RiilAlert.info(textMap.END_LT_START);//"开始时间必须小于结束时间。"
            return;
        }
        
        if (undefined !== interval_i) {
            listData = getListData(conf.schema, interval_i);
        }
        
        if (onChange) {
            listData = onChange(startTime_i, endTime_i, listData) || listData;
        }
        
        if (listData) {
            setFrequencyList(conf, listData);
        }
    }
    
    
}();
    
}