/**
 * ������Ҫ��js��css
 */
var scriptEls = document.getElementsByTagName('script'), path = scriptEls[scriptEls.length - 1].src,i = 1,type = 'fresh-style';

if (path.lastIndexOf('=') > -1) {
	type = path.substring(path.lastIndexOf('=') + 1);
}

while (i--) {
	path = path.substring(0, path.lastIndexOf('/'));
}
//jquery
document.write('<script type="text/javascript" src="' + path + '/jquery/jquery-1.8.0.min.js"></script>');

//easyui
document.write('<script type="text/javascript" src="' + path + '/jquery.easyui.min.js"></script>');
document.write('<script type="text/javascript" src="' + path + '/plugins/jquery.form.js"></script>');
document.write('<script type="text/javascript" src="' + path + '/plugins/datagrid-groupview.js"></script>');
document.write('<script type="text/javascript" src="' + path + '/locale/easyui-lang-zh_CN.js"></script>');
document.write('<link rel="stylesheet" type="text/css" href="' + path + '/themes/theme1/zealyoui.css" />');


/*//My97DatePicker
document.write('<script type="text/javascript" src="' + path + '/My97DatePicker/WdatePicker.js"></script>');
document.write('<link rel="stylesheet" type="text/css" href="' + path + '/My97DatePicker/skin/WdatePicker.css" />');*/


//HTML5��ǩ����
(function() {
    if (!
    /*@cc_on!@*/
    0) return;
    var e = "abbr, article, aside, audio, canvas, datalist, details, dialog, eventsource, figure, footer, header, hgroup, mark, menu, meter, nav, output, progress, section, time, video".split(', ');
    var i= e.length;
    while (i--){
        document.createElement(e[i]);
    }
})()