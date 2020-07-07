const convertSerializeArrayToJsonObject = function(data){
   let jsonObject = {};
    for(let item of data){
        jsonObject[item.name] = item.value;
    }
    return jsonObject;
}

const checkRequestSuccess = function (responseObject) {
    return responseObject["ok"];
}