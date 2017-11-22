/* 加载js-sdk
 * 参数: info
 * 描述: 1.info格式为js的json对象, 包含
 *          url(加载当前js-sdk的url),
 *          title(分享标题)
 *          link(分享链接)
 *          imgUrl(分享图标)
 *          desc(分享到朋友圈时的描述)
 *      2.succFunc分享成功后回调函数
 *      3.canFunc分享取消后回调函数
 *
 */
function share(info, succFunc, canFunc) {

    $.ajax({
        url: '/weixinServer/jsapi/getJsapiSign',
        type: "post",
        dataType: "json",
        data: {
            "url" : info.url
        },
        success: function (data) {
            if (data.rspCode == 'SUCCESS') {
                wx.config({
                    debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId : data.appid, // 必填，公众号的唯一标识
                    timestamp : data.timestamp, // 必填，生成签名的时间戳
                    nonceStr : data.nonceStr, // 必填，生成签名的随机串
                    signature : data.signature,// 必填，签名，见附录1
                    jsApiList : [
                        'onMenuShareTimeline',
                        'onMenuShareAppMessage'
                    ]
                    // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                });

                wx.ready(function(){

                    /*wx.hideOptionMenu();*/
                    wx.onMenuShareTimeline({
                        title: info.title,
                        link: info.link,// 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                        imgUrl: info.imgUrl,// 分享图标
                        success: function () {
                            succFunc();

                            // 用户确认分享后执行的回调函数
                        },
                        cancel: function () {
                            canFunc();

                            // 用户取消分享后执行的回调函数
                        }
                    });

                    wx.onMenuShareAppMessage({
                        title: info.title,
                        desc: info.desc,
                        link: info.link,// 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                        imgUrl: info.imgUrl,// 分享图标
                        trigger: function (res) {
                            // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
                        },
                        success: function (res) {
                            succFunc();

                        },
                        cancel: function (res) {
                            canFunc();

                        },
                        fail: function (res) {
                            alert(JSON.stringify(res));
                        }
                    });
                });

            } else {
            	alert(data.Message);
            }
        }
    });

}