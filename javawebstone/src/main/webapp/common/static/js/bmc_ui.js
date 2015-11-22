var LayoutManager = {
		start: function () {
			if (document.getElementById("header")) {
				LayoutManager.headerHeight = $("#header").height();
			} else {
				LayoutManager.headerHeight = 0;
			}
			if (document.getElementById("footer")) {
				var $footer = $("#footer");
				LayoutManager.footerHeight = $footer.height() + $.getVertical($footer, "margin") + $.getVertical($footer,
					"padding");
			} else {
				LayoutManager.footerHeight = 0;
			}

			OnResize.bind();
			OnResize.addLayout(LayoutManager.doLayout);
			this.doLayout();
		},
		layoutsObj: {},
		layouts: [],
		/**
		 * 重新布局调整
		 * @param $dom jqueryObj 如果有该参数则表示局部调整，否则整个界面全部调整
		 * */
		doLayout: function ($dom) {
			if (!document.getElementById("homepageBody")) {
				$(document.body).css("overflow", "hidden");
			}
			var content = document.getElementById("content");
			if (content) {

				$(document.body).removeClass("homepage");
				$(document.getElementsByTagName("html")[0]).removeClass("homepage");
			}
			var layout = LayoutManager.findDimensions();
			if (!$dom || $.isNumber($dom)) {
				var width = layout.width;
				var height = layout.height - LayoutManager.headerHeight - LayoutManager.footerHeight - 6;
				if ($("#header").css("position") == "absolute") {
					height = $("#content").height();
				}
				$("#content").css({
					height: height,
					width: width
				});
				$(document.body).width(width);
			}
			if (content) {

				$(document.body).addClass("homepage");
				$(document.getElementsByTagName("html")[0]).addClass("homepage");
			}
			LayoutManager.layoutsObj.frameLayout(layout.width);
			LayoutManager.layoutsObj.portletLayout($dom);
			LayoutManager.layoutsObj.leftNavigationLayout($dom);
			LayoutManager.layoutsObj.leftNavigationPanelLayout($dom);
			LayoutManager.layoutsObj.tabPanelLayout($dom);
		}
	}
	/**
	 * 获得当前浏览器尺寸
	 * */
LayoutManager.findDimensions = function () {
	var winWidth = 0,
		winHeight = 0;
	// 获取窗口宽度
	if (window.innerWidth) {
		winWidth = window.innerWidth;
	} else if ((document.body) && (document.body.clientWidth)) {
		winWidth = document.body.clientWidth;
	}
	// 获取窗口高度
	if (window.innerHeight) {
		winHeight = window.innerHeight;
	} else if ((document.body) && (document.body.clientHeight)) {
		winHeight = document.body.clientHeight;
	}
	// 通过深入Document内部对body进行检测，获取窗口大小
	if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth) {
		winHeight = document.documentElement.clientHeight;
		winWidth = document.documentElement.clientWidth;
	}

	return {
		height: winHeight,
		width: winWidth
	};

}



/**
 * 页面上中下框架布局
 * @param layout {json} 这个窗口大小
 * */
LayoutManager.layoutsObj.frameLayout = function (width) {
	var $container = $("#layout_left").parent();
	var padding = $.getLandscape($container, "padding");

	var width = width - $("#layout_left").width() - 6;
	var $layoutRight = $("#layout_right").width(width - 3 - padding);
	//$layoutRight.children(".portlet").css({width:"100%"});
}


/**
 * portlet高度自适应
 * portlet元素添加doLayout属性
 *
 * */
LayoutManager.layoutsObj.portletLayout = function (wrapper) {
	var queryStr = 'div.portlet[dolayout]';
	var $portlets = null;
	if (!wrapper || $.isNumber(wrapper)) {
		$portlets = $(queryStr);
	} else {
		var $wrapper = $.getJQueryDom(wrapper);
		$portlets = $wrapper.find(queryStr);
	}

	var containerHeight = $portlets.parent().height();
	var $portlet = null,
		$portletT = null,
		$portletB = null,
		$portletM = null;
	for (var i = 0; i < $portlets.length; i++) {
		$portlet = $($portlets[i]);
		$portletT = $portlet.children(":eq(0)");
		$portletM = $portlet.children(":eq(1)").children().children();
		$portletB = $portlet.children(":eq(2)");
		$portletM.height(containerHeight - $.getAllSize($portletT).height - $.getAllSize($portletB).height - $.getVertical(
			$portletM, "padding") - $.getVertical($portletM, "margin"));
	}
}

/**
 * 页面左侧导航互斥折叠菜单，内容显示区域高度
 * @param $dom jqueryObj 如果有该参数则表示局部调整，否则整个界面全部调整
 * */
// LayoutManager.layoutsObj.leftNavigationLayout = function ($dom) {

// 	var $subnav_div = null;
// 	if (!$dom || $.isNumber($dom)) {
// 		$subnav_div = $('div.subnav:first');
// 	} else {
// 		$subnav_div = $dom.find('div.subnav:first');
// 	}
// 	if ($subnav_div.length == 0) return;
// 	var items = $subnav_div.children().children();
// 	var $item = $(items.filter(":not(.open)")[0]);
// 	var height = $subnav_div.parent().height() - ($item.height()) * items.length;

// 	var $prev = $subnav_div.prev();
// 	if ($prev[0]) {
// 		height = height - $.getAllSize($prev).height; //.height()-$.getVertical($prev[0],"padding")-$.getVertical($prev[0],"margin");
// 	}
// 	$subnav_div.attr("itemHeight", height);
// 	items.filter(".open").children(".subnav-subset").height(height);
// }

LayoutManager.layoutsObj.leftNavigationLayout = function ($dom) {

	var $subnav_div = null;
	if (!$dom || $.isNumber($dom)) {
		$subnav_div = $('div.subnav:first');
	} else {
		$subnav_div = $dom.find('div.subnav:first');
	}
	if ($subnav_div.length == 0) return;

	var $scrollDiv = $subnav_div.parent();
	var height = $scrollDiv[0].clientHeight;
	var $openContent = $subnav_div.find('ul>li.open>.subnav-subset');
	if (!$openContent[0]) return;

	$openContent.height(height);
	var clientHeight = $scrollDiv[0].clientHeight;
	var scrollHeight = $scrollDiv[0].scrollHeight;

	height -= scrollHeight - clientHeight;

	$subnav_div.attr("itemHeight", height);
	$openContent.height(height);
};

/**
 * 页面左侧导航Panel折叠菜单，内容显示区域高度
 * @param $dom jqueryObj 如果有该参数则表示局部调整，否则整个界面全部调整
 * */
LayoutManager.layoutsObj.leftNavigationPanelLayout = function ($dom) {
	var $subnav_window_div = null;
	if (!$dom || $.isNumber($dom)) {
		$subnav_window_div = $('div.subnav-window');
	} else {
		$subnav_window_div = $dom.find('div.subnav-window');
	}
	if (!$subnav_window_div[0]) {
		return;
	}
	var items = $subnav_window_div.children().children();
	var $dolayout = items.filter('[dolayout]');
	var $nodolayout = items.filter(':not([dolayout])');

	var blocked = 0;
	var $noItem = null;

	for (var i = 0, len = $nodolayout.length; i < len; i++) {
		$noItem = $($nodolayout[i]);
		blocked += $noItem.height() + $.getVertical($noItem, "margin") + $.getVertical($noItem, "padding") + 2;
	}
	var allHeight = $subnav_window_div.parent().height();

	var $itemHead = null;
	for (var i = 0, len = $dolayout.length; i < len; i++) {
		$noItem = $($dolayout[i]);
		$itemHead = $noItem.children(":eq(0)");
		blocked += $itemHead.height() + $.getVertical($itemHead, "margin") + $.getVertical($itemHead, "padding") + 2;
		blocked += $.getVertical($noItem, "margin") + $.getVertical($noItem, "padding");
	}

	var height = (allHeight - blocked - $.getVertical($subnav_window_div, "padding") + 2) / len;

	for (i = 0; i < len; i++) {
		$noItem = $($dolayout[i]);
		$noItem.children(":eq(1)").height(height - 1);
	}

}



LayoutManager.layoutsObj.tabPanelLayout = function ($dom) {
	if (!$dom || $.isNumber($dom)) {
		$tabGroup = $('div.tab-group[doLayout="true"]');
	} else {
		$tabGroup = $dom.find('div.tab-group[doLayout="true"]');
	}

	//兼容admin
	if ($tabGroup.length == 0) {
		if (!$dom || $.isNumber($dom)) {
			$tabGroup = $('div.tabpanel');
		} else {
			$tabGroup = $dom.find('div.tabpanel');
		}
	}


	if ($tabGroup.length == 0) return;
	var $tab = null;
	for (var i = 0, len = $tabGroup.length; i < len; i++) {
		$tab = $($tabGroup[i]);
		var containerHeight = $tab.parent().height();
		$tab.children(":not(.tabSplit):eq(1)").height(containerHeight - $tab.children(":not(.tabSplit):eq(0)").height() - 26 -
			$.getVertical($tabGroup[0], "margin") - $.getVertical($tabGroup.parent()[0], "padding"));
	}
}

LayoutManager.layoutsObj.treeNavigaterLayout = function () {
		var leftTreeContainer = $("div.left-tree-container");
		var len = leftTreeContainer.length;

		var $leftTreeContainer = null;
		for (var i = 0; i < len; i++) {
			$leftTreeContainer = $(leftTreeContainer[i]);
			$leftTreeContainer.height($leftTreeContainer.parent().parent().height() - $leftTreeContainer.prev().height() - 5);
		}

	}
	/**
	 * 页面自适应放到 LayoutManager.doLayout的最后.
	 * 告警规则内容页面自适应
	 * */
LayoutManager.layoutsObj.alarmRuleSelfAdaptionHeight = function () {
	var $alarmRuleContent = $("#alarmRuleContent");
	if ($alarmRuleContent.length === 0) {
		return;
	}
	var fatherHeight = $alarmRuleContent.parent().height();
	var brothersHeight = 0;
	var $brothers = $alarmRuleContent.parent().children(':not(#alarmRuleContent)');
	$.each($brothers, function (i, e) {
		brothersHeight += $(e).height();
	});
	$alarmRuleContent.height(fatherHeight - brothersHeight - 30);

	var $tabbodyWrap = $("#AlarmRuleTabPanel_bodyWrap");
	$tabbodyWrap.height(fatherHeight - brothersHeight - $tabbodyWrap.prev().height() - $tabbodyWrap.prev().prev().height() -
		50);
}


LayoutManager.start();

javascript: window.history.forward(1);
;var FirstBtn = {};
(function() {

    var EMPTY_URL = 'javascript:void(0)';

    window.Navigator = {
        _log: Debug.getLogger('debug.navigator'),
        init: function(id, param) {
            var $ul = $('#' + id);
            if (!$ul[0]) return;
            var lis = $ul.children('li');
            var $li = null;
            var width = $ul.width();

            lis.bind('mouseover', function() {
                    if (window.isFullScreenMoveing) {
                        return;
                    }

                    var $li = $(this);
                    var li_id = $li.attr('id');
                    if (!li_id) {
                        return;
                    }
                    var $div = $('#' + li_id + 'second');
                    if (!$div[0]) {
                        return;
                    }

                    $(document.body)
                        .append($div);
                    $div.removeAttr('hasHide');
                    lis.filter(':not([current])')
                        .removeClass('over');
                    //多于一个子菜单选项，或者有一个子菜单选项但是第一级菜单有请求链接，需要展示子菜单
                    if ($div.children().length != 2 || ($div.children().length == 2 && $li.children('a')
                            .attr('loadurl') != EMPTY_URL)) {
                        var layout = $.getElementAbsolutePosition(this);
                        var underMaskZindex = ZIndexMgr.get();
                        var divZindex = ZIndexMgr.get();
                        var maxX = 0;
                        $div.css({
                                'position': 'absolute',
                                left: layout.x,
                                top: layout.y + 39,
                                zIndex: divZindex
                            })
                            .show();
                        $li.addClass('over');
                        var width = $li.width() + $.getLandscape(this, 'padding');
                        if ($div.children().width() < width) {
                            $div.width(width + 20);
                        }
                        $div.children('a.blur')
                            .focus();

                        maxX = $(window).width() - $div.width();
                        if (maxX < layout.x) {
                            layout.x = maxX;
                        }
                        $div.css({
                            left: layout.x
                        });
                        // 为弹出菜单增加iframe垫底
                        RiilMask.underMask($div.attr('id'), $div, underMaskZindex);
                    }


                })
                .bind('mouseout', function() {
                    var $li = $(this);
                    var $div = $('#' + $li.attr('id') + 'second');


                    if ($div.children()
                        .length != 2) {
                        $div.attr('hasHide', 'true');
                        setTimeout(function() {
                            hideSecond($div);
                        }, 200);

                        if (!$div[0]) {
                            $('#' + $li.attr('id') + ':not([current])')
                                .removeClass('over');
                        }
                    }

                })
                .bind('click', function() {
                    var $li = $(this);
                    if ($li.attr('openType') == '-1') {
                        $li.removeClass('over');
                        return;
                    }
                    lis.removeClass('over')
                        .removeAttr('current');
                    $li.addClass('over')
                        .attr('current', 'current');

                });

            function hideSecond($div) {
                if (!window.dontHideMenu && $div.attr('hasHide')) {
                    $div.hide();
                    var id = $div.attr('id');
                    id = id.replace('second', '');
                    $('#' + id + ':not([current])')
                        .removeClass('over');
                    // 为弹出菜单增加iframe垫底

                    var info = 'menu hide, id:' + $div.attr('id');

                    if ($div.length > 0) {
                        info += ' hiding';
                        ZIndexMgr.free($div);
                        RiilMask.unmask($div.attr('id'));
                    } else {
                        info += ' !!';
                    }


                }

            }

            _.each(lis, function(li, i) {
                $li = $(li);
                // width += $li.width();

                $li.children('a')
                    .bind('click', function() {
                        var $a = $(this);
                        if ($a.attr('openType') == '-1') {
                            window.Navigator.openWindow($a.attr('loadurl'));
                            return;
                        }
                        lis.removeAttr('current')
                            .removeClass('over');
                        $a.closest('div.hsubnav')
                            .addClass('over')
                            .attr('current', 'current');
                        if ($a.attr('loadurl') != EMPTY_URL) {
                            window.Navigator.refreshIframe($a.attr('loadurl'), $a.attr('flag'));
                        } else {
                            var $div = $('#' + $a.closest('div.hsubnav').attr('id') + 'second');
                            $div.find('a:eq(0)').click();
                        }
                    })
                    .next('div')
                    .bind('mouseover', function() {
                        $(this)
                            .removeAttr('hasHide');
                    })
                    .bind('mouseout', function() {
                        var $div = $(this);
                        $div.attr('hasHide', 'true');
                        setTimeout(function() {
                            hideSecond($div);
                        }, 100);
                    })
                    .attr('id', $li.attr('id') + 'second')
                    .find('a')
                    .bind('click', function() {
                        var $a = $(this);
                        if ($a.attr('openType') == '-1') {
                            window.Navigator.openWindow($a.attr('loadurl'));
                            return;
                        }
                        window.Navigator.refreshIframe($a.attr('loadurl'), $a.attr('flag'));
                        var $div = $a.closest('div.hsubnav');
                        $div.hide();
                        lis.removeAttr('current')
                            .removeClass('over');
                        $('#' + $div.attr('id')
                                .replace('second', ''))
                            .addClass('over')
                            .attr('current', 'current');
                    });

            }); // _.each(lis, function (li, i)


            $ul.width(width).attr('widthpx', width);

            width = $('div.container').width() - 240;
            $('#navArea').width(width);
            $ul.parent().width(width);

            if ($ul.width() < width) {
                $ul.width(width);
            }

            if ($ul.width() > width) {
                $('div.nav>a').show();
                $ul.parent().width(width - 29);
            }else {
                $('div.nav>a').hide();
            }

            $ul.find('li a[loadUrl^="http"]:first').click();

            OnResize.addLayout(this.layout);
            OnResize.bind();

            var interval = null;
            $('div.nav>a')
                .bind('mousedown', function() {
                    var position;
                    if ($(this).hasClass('icon_right')) {
                        position = 'right';
                    }else {
                        position = 'left';
                    }
                    interval = setInterval(function() {
                        scrollTab(position);
                    }, 10);

                })
                .bind('mouseup', function() {
                    clearInterval(interval);
                });



            function scrollTab(position) {
                var $ul = $('#sddm');
                var $wrapper = $ul.parent();
                var wrapperWidth = $wrapper.width();
                var marginLeft = parseInt($ul.css('marginLeft'));
                var abs = Math.abs(marginLeft);
                var ulWidth = $ul.width();
                var step = 5;
                if (position == 'right') {
                    if (abs + step < ulWidth - wrapperWidth) {
                        $ul.css('marginLeft', (abs + step) * -1);
                    }
                } else {
                    if (abs - step > 0) {
                        $ul.css('marginLeft', (abs - step) * -1);
                    }
                }
            }

            var $handler = $('<div class="fullscreen-handler"></div>')
                .css({
                    position: 'absolute',
                    top: 0,
                    left: '50%',
                    height: 12,
                    width: 29,
                    marginLeft: 14.5,
                    background: 'url(' + ctx + '/static/images/header/handler.png) 0 0 no-repeat',
                    zIndex: 699,
                    cursor: 'pointer'
                })
                .hide();
            $(document.body)
                .append($handler);
            $handler.bind('mouseover', function() {
                expandHeader();
            });


            //收缩全屏
            function clloapseHeader() {
                window.isFullScreenMoveing = true; //是否在全屏中
                $('#header:not(:animated)')
                    .animate({
                        top: ($('#header')
                            .height()) * -1
                    }, function() {
                        $(this)
                            .css({
                                'borderBottom': '1px solid #32598d'
                            });
                        $('#top-content')
                            .css({
                                'borderBottom': '1px solid #5e8bc9'
                            });
                        $handler.show();
                        window.isFullScreenMoveing = false;
                    });
            }

            function expandHeader() {
                window.isFullScreenMoveing = true; //是否在全屏中
                $('#header')
                    .css({
                        'borderBottom': 'none'
                    })
                    .animate({
                        top: 0
                    }, function() {
                        window.isFullScreenMoveing = false;
                    });
                $('#top-content')
                    .css({
                        'borderBottom': 'none'
                    });
                $handler.hide();

            }

            $('#contentIframe')
                .bind('mouseover', function() {
                    var $header = $('#header');
                    if ($header.css('position') == 'absolute') {
                        clloapseHeader();
                    }
                });
            $('#controlBtn')
                .toggle(function() {
                    var $content = $('#content')
                        .attr('isMove', 'true');
                    $('#content')
                        .animate({
                            height: $content.height() + $('#header')
                                .height() + $('#footer')
                                .height() + 20 - 6
                        });
                    $('#header')
                        .css({
                            'position': 'absolute',
                            'background': 'url(' + ctx + '/static/images/bg.png) top left repeat'
                        });
                    var $a = $(this);
                    $a.html('<span class="icon_hide"></span>' + $a.attr('lockText'))
                        .parent()
                        .removeClass('hide')
                        .addClass('lock');
                    clloapseHeader();
                    $('#footer')
                        .css({
                            position: 'absolute'
                        })
                        .animate({
                            bottom: $('#footer')
                                .height() * -1
                        }, function() {
                            $(this)
                                .hide();
                        });

                    // 记录隐藏状态
                    document.cookie = 'hideHeader=true';
                }, function() {
                    var $a = $(this);
                    $a.html('<span class="icon_hide"></span>' + $a.attr('hideText'))
                        .parent()
                        .removeClass('lock')
                        .addClass('hide');
                    expandHeader();
                    $('#header')
                        .css({
                            'position': 'relative',
                            'background': 'url(' + ctx + '/static/images/header/header-bg.png) repeat'
                        });
                    var $content = $('#content')
                        .attr('isMove', 'true');
                    $('#footer')
                        .css({
                            position: 'static',
                            'display': 'block'
                        });
                    $content.animate({
                        height: $content.height() - $('#header')
                            .height() - $('#footer')
                            .height() - 20 + 6
                    }, function() {

                    });

                    // 记录显示状态
                    document.cookie = 'hideHeader=false';
                });

            $('#header')
                .width($('#content')
                    .width());

            // 读取cookie，设置隐藏状态
            var arrValue = /hideHeader=(.*?);/.exec(document.cookie + ';');
            if (null !== arrValue) {
                if ('true' === arrValue[1]) {
                    $('#controlBtn')
                        .click();
                }
            }
        },
        layout: function() {
            var width = $('div.container').width() - 240;
            var $ul = $('#sddm');
            $('#header').width($('#content').width());
            $('#navArea').width(width);
            $ul.parent().width(width);
            var widthpx = $ul.attr('widthpx') - 0;
            if (widthpx < width) {
                $ul.width(width);
                $('div.nav>a').hide();
            } else {
                $ul.parent()
                    .width(width - 29);
                $('div.nav>a').show();
                $ul.width(widthpx);
            }
            $ul.css('marginLeft', 0);
        }
    };
    window.Navigator.refreshIframe = function(url, flag) {
        if (url === EMPTY_URL || !url) {
            return;
        }
        if (flag && flag != '2') {
            Loading.start();
        }

        $('#contentIframe')
            .attr('src', url);
    };
    window.Navigator.openWindow = function(url) {
        PageCtrl.winOpen({
            url: url,
            width: window.screen.availWidth,
            height: window.screen.availHeight,
            name: 'outerWindow',
            left: 0,
            top: 0
        });
    };

    window.Navigator.init('sddm');


})();
window.flashcallme111 = function(id, url) {
    window.frames.middleFrame.location.href = url;
};
$('#contentIframe')
    .unbind()
    .attr('scrolling', 'no');