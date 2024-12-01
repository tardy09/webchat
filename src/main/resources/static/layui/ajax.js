function dataAjax($,url,params,blackFuction) {
    $.ajax({
        type : 'post',
        url : url,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        cache:false,
        data: JSON.stringify(params),
        success: function (result) {
            blackFuction(result);
        }
    });
}