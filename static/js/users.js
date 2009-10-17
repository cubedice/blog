/*Session info using XHR
*/ 
function createLoginForm(userBar)
{
    userBar.replaceWith("\
            <div id='userBar'><form id='loginForm' method='get'>\
            <label>Username:<input id='loginForm-username' type='text' \
            name='username'/></label>\
            <label>Password:<input id='loginForm-password' type='password'\
            name='password'/></label>\
            <input type='submit' id='loginSubmit' value='Log in'/> \
            </form></div>");
}

function createStatusBar(userBar, userName)
{
    userBar.replaceWith("\
            <div id='userBar'>" + userName +
            " <a id='logout' href=''>log out</a></div>");
            $("#logout").click(logout);
}

function login()
{
    var uname = $("#loginForm-username").val();
    var pword = $("#loginForm-password").val();
    $.getJSON("/login", { username: uname, password: pword } ,
        function(dat){
            if( dat == null || dat.sessionid == null || dat.username == null )
                alert("Incorrect username or password"); 
            else {
                $.cookie('user', dat.sessionid, { expires: 7 });
                createStatusBar($("#loginForm"), dat.username);
            }
 
    });
    
    return false;
}

function logout()
{
    var session = unescape($.cookie('user'));
    $.getJSON("/logout", { sid: session },
        function(data){
            $.cookie('user', null);
            createLoginForm($("#userBar"));
        });
    return false;
}  

$(document).ready(function(){
    var session = unescape($.cookie('user'));
    $.getJSON("/get-user", { sid: session },
        function(data){
            if(data == null) {
                $.cookie('user', null);
                createLoginForm($("#userBar"));
                $("#loginSubmit").click(login);
            }
            else
                createStatusBar($("#userBar"), data.username);
        });

    });
