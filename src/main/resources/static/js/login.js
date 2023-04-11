let index = {
	init: function(){
		$("#btn-update").on("click",()=>{	//function(){} 대신 ()=>{} : this를 바인딩하기 위해서
		this.update();
		});
	},

	update: function(){
		//alert('user의 save함수 호출됨');
		let data = {
			id: $("#id").val(),
			password: $("#password").val(),
			email: $("#email").val(),
		};
 var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
		    beforeSend: function(xhr){
                                    xhr.setRequestHeader(header,token);
                                    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                                    xhr.setRequestHeader("Content-type","application/json");
                                    },
			//회원정보 수정 요청
			type: "PUT",
			url: "/user/myinfo_check",
			data: JSON.stringify(data),		//http body 데이터
			contentType: "application/json; charset=utf-8",	//body 데이터가 어떤 타입인지(MIME)
			dataType: "json"	//요청을 서버로 해서 응답이 왔을 때 기본적으로 모든 것이 문자열(String)=>javascript 오브젝트로 변경

			//응답 결과가 정상일 때
		}).success(function(resp){
			alert("회원수정이 완료되었습니다.");
			console.log(resp);
			location.href="/";
			//실패일 때
		}).fail(function(error){
			alert(JSON.stringify(error));
		});

	}
}

index.init();