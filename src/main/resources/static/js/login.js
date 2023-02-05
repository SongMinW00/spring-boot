var user = {id : 'fe', password : 'fe'}

function login() {
    const form = document.login_form;
    const chkId = checkValidId(form);

}

function checkValidId(form) {
    if (form.id.value == user.id) {
        if(form.password.value == user.password){

            location.href = "/index";
        }
        //form.id.focus();
        else
            alert("아이디나 패스워드가 일치하지 않습니다. 다시 입력해주세요.")

    }
    else
        alert("아이디나 패스워드가 일치하지 않습니다. 다시 입력해주세요. ")
}


