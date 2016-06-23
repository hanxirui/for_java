/* jshint evil:true */
var BigPipe = function() {
	this.cache = [];
	this.ready = false;
};

BigPipe.prototype = {
	start: function() {
		var self = this;
		var interval = setInterval(function() {
			if (window.$ && window.PageCtrl) {
				clearTimeout(interval);
				self.render();
			}
		}, 1000);
	},

	add: function(conf) {
		this.cache.push(conf);
		this.render();
	},

	render: function() {
		while (true) {
			if (this.cache.length !== 0) {
				var data = this.cache.shift();
				var $source = $('[id^="' + data.id + '"]:eq(1)');
				var $dom = $('#' + data.id).html($source.html());
				this._renderJs($dom);
				$source.remove();
			} else {
				break;
			}
		}
	},

	_renderJs: function($dom) {
		var that = this;
		var scripts = $dom.find('script');
		// 通过Q保证一个一个执行
		Q.try(function() {
			var q = Q();
			_.each(scripts, function(script) {
				if (script.src) {
					q = q.then(function() {
						return that._renderJsFile(script);
					});
				} else {
					q = q.then(function() {
						return that._renderJsText(script);
					});
				}
			});
			return q;
		}).catch(function(e) {
			console.error(e);
		});
	},

	_renderJsText: function(script) {
		// 下边的append虽然在dom上看不到变化，但是脚本确实执行了
		$(document.body).append('<script>' + $(script).html() + '</script>');
	},

	_renderJsFile: function(script) {
		var defer = Q.defer();
		$.getScript(script.src, function() {
			var namespace = $(script).attr('namespace');
			var initparam = $(script).attr('initparam');
			if (window[namespace]) {
				try {
					window[namespace].init(initparam);
				} catch (e) {
					defer.reject(e);
				}
			}
			defer.resolve();
		});
		return defer.promise;
	}
};
