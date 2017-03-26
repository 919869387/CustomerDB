
function onClik(){  

	var username = document.getElementById('username').value;//注意这里fn是form的名字，而不是id 
	var password = document.getElementById('password').value; 

	var json = {'username':username,'password':password};
	//alert(json); 
	$.ajax({
		type: "POST",
		url: "login.do",
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(json),
		dataType: "json",
		success: function (response) {
			if (response.code > 0) {
				//alert(response.result);
				location.href = response.result;
			}else{
				alert("用户名 密码有误！"+response.result);
				location.href = response.result;
			}
		},
		error: function (response) {
			alert("发生错误");
		}
	});
}
