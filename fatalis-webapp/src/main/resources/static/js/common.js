/* 获取url参数 */
function getParameter(param) {
    var query = window.location.search;//获取URL地址中？后的所有字符  
    var iLen = param.length;//获取你的参数名称长度  
    var iStart = query.indexOf(param);//获取你该参数名称的其实索引  
    if (iStart == -1) {//-1为没有该参数 
    	return "";
    }
    iStart += iLen + 1;
    var iEnd = query.indexOf("&", iStart);//获取第二个参数的其实索引  
    if (iEnd == -1) {//只有一个参数  
    	return query.substring(iStart);//获取单个参数的参数值  
    }

    return query.substring(iStart, iEnd);//获取第二个参数的值  
}

/* 判断不为空 */
function isValue(param) {

    // undefined时, param为空
    if (undefined == param || null == param) {

        return false;
    }

    // 获取参数类型
    var typecall = Object.prototype.toString;
    var type = typecall.call(param);

    if ('[object Number]' == type) {// 数值类型

    } else if ('[object String]' == type) {// 字符串类型
        if ('' == param) {

            return false;
        }
    } else if ('[object Object]' == type) {// 对象, json类型
        var str = JSON.stringify(param);
        if ('{}' == str || {} == str) {

            return false;
        }
    } else if ('[object Array]' == type) {// 数组类型
        var str = JSON.stringify(param);
        if ('[]' == str || [] == str) {

            return false;
        }
    } else {
        alert('isValue: 错误的参数类型');
    }

    return true;
}


/* 获取当前域名 */
var holeUrl = window.location.href;
function getRealmPath() {
    var path = holeUrl.substring(holeUrl.indexOf('//') + 2, holeUrl.length);
    var array = path.split('/');

    return array[0];
}

/* 获取当前项目名 */
function getContextPath() {
    var path = holeUrl.substring(holeUrl.indexOf('//') + 2, holeUrl.length);
    path = path.substring(path.indexOf('/') + 1, path.length);
    path = path.substring(0, path.indexOf('/'));

    return "/" + path;
}

/* 获取当前完整url */
function getUrl() {

    return holeUrl;
}