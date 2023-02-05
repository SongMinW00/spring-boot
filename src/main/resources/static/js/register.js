function usernameCheck() {
//    const form = document.register_form;
//    const chkId = checkValidId(form);
//    const chkEmail = checkValidEmail(form);
//    const chkPw = checkValidPassword(form);
//    const chkPw2 = checkValidPassword2(form);
    const userId = $("#userId").val();

    $.ajax({
        type: "get",
        url: "content/register/useridcheck",
        data: {"userId": userId},
        dataType: "JSON",

        success: function (result) {
            if (result.result == "0") {
                if (confirm("이 아이디는 사용 가능합니다. \n사용하시겠습니까?")) {
                    userIdOverlapCheck = 1;
                    $("#userId").attr("readonly", true);
                    $("#userIdOverlay").attr("disabled", true);
                    $("#userIdOverlay").css("display", "none");
                    $("#resetUserId").attr("disabled", false);
                    $("#resetUserId").css("display", "inline-block");
                }
                return false;
            } else if(result.result == "1"){
                alert("이미 사용중인 아이디입니다.");
                $("#userId").focus();
            } else {
                alert("success 이지만 result 값이 undefined 잘못됨");
            }
        },
        error: function (request, status,error){
            alert("ajax 실행 실패");
            alert("code: " + request.status + "\n" + "error : " + error);
        }
    });
}