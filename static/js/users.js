/*Session info using XHR
*/ 
function createForm(httpauth)
{
    httpauth.replaceWith("\
            <form id='loginForm' method='get'>\
            <label>Username:<input id='loginForm-username' type='text' \
            name='username'/></label>\
            <label>Password:<input id='loginForm-password' type='password'\
            name='password'/></label>\
            <input type='submit' id='loginSubmit' value='Log in'/></form>");
}


function login()
{
    var uname = $("#loginForm-username").val();
    var pword = $("#loginForm-password").val();
    $.get("/login", { username: uname, password: pword } ,
        function(dat){
            var val1 = $.cookie('cookie', 'the_val');
            var val2 = $.cookie('user_auth', dat, { expires: 7, path: '/', secure: true});
            var val3 = $.cookie('the_cookie', 'the_value', { expires: 7, path: '/', secure: true }); 
 
    });
    
    return false;
}

function logout()
{
    http.open("get", this.parentNode.action, false, "null", "null");
    http.send("");
    alert("You have been logged out.");
    return false;
}  


$(document).ready(function(){
    createForm($("#userBar"));
    $("#loginSubmit").click(login);
});
