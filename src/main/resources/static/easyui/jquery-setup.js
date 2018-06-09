var contextPath = '/cm';

$.ajaxSetup({
	complete : function(data, ts) {
		//如果是登录页面，跳转
		if (data.responseText.indexOf('id="login-window"') >= 0) {
			window.top.location.href = '/login.html';
		}
	}
});

$.extend($.fn.validatebox.defaults.rules, {
    equals: {
        validator: function(value,param){
            return value == $(param[0]).val();
        },
        message: '字段不相等'
    }
});

var Global = {
		dateTimeFormatter : function(date) {
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			if (m < 10) {
				m = '0' + m;
			}
			var d = date.getDate();
			if (d < 10) {
				d = '0' + d;
			}
			var h = date.getHours();
			if (h < 10) {
				h = '0' + h;
			}
			var mm = date.getMinutes();
			if (mm < 10) {
				mm = '0' + mm;
			}
			var s = date.getSeconds();
			if (s < 10) {
				s = '0' + s;
			}
			console.log(y + '-' + m + '-' + d + ' ' + h + ':' + mm + ':' + s);
			return y + '-' + m + '-' + d + ' ' + h + ':' + mm + ':' + s;
		},
		
		dateTimeParse : function(value) {
			if (value) {
				return new Date(value);
			} else {
				return new Date();
			}
		}
};

function load(msg) {
	$("<div class=\"datagrid-mask\" style=\"z-index: 10000\"></div>").css({
		display : "block",
		width : "100%",
		height : $(window).height()
	}).appendTo("body");
	$("<div class=\"datagrid-mask-msg\" style=\"z-index: 10005\"></div>").html(msg).appendTo(
			"body").css({
		display : "block",
		left : ($(document.body).outerWidth(true) - 190) / 2,
		top : ($(window).height() - 45) / 2
	});
} 
// 取消加载层 
function disLoad() { 
	$(".datagrid-mask").remove();
	$(".datagrid-mask-msg").remove();
}


function download(code, fileName) {
	var saveLink = document.createElement('a');
    saveLink.href = '/download?code=' + code;
    saveLink.download = fileName;
    saveLink.click();
}
