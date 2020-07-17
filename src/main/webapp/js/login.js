const loginSuccessCallback = function(response){
    if(checkRequestSuccess(response)){
        /*リクエスト正常*/
        sessionStorage.setItem("library_token", response["data"]["token"])
        window.location = "booklist";
    }else{
        /*異常*/
        window.alert(response["text"]);
    }
}

$('form').on('submit',function(){
    const data = $('form').serializeArray();
    const jsonObject = convertSerializeArrayToJsonObject(data);
    $.ajax(
        'api/login',
        {
            type:          'post',
            dataType:      'json',
            contentType:   'application/json',
            scriptCharset: 'utf-8',
            data:          JSON.stringify(jsonObject),
            success:       function (response) {loginSuccessCallback(response)}
        }
    )
    return false;
});
