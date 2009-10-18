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
    $("#loginSubmit").click(login);
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
    $.getJSON("/login", { username: uname, password: pword }, authResponse);
    
    return false;
}

function authResponse( data )
{
    if( data == null || data.sessionid == null || data.username == null ) {
        $.cookie('user', null);
        createLoginForm($("#userBar"));
    } 
    else {
        $.cookie('user', data.sessionid, { expires: 7 });
        createStatusBar($("#userBar"), data.username);
    }
}

function logout()
{
    var session = unescape($.cookie('user'));
    $.getJSON("/logout", { sid: session }, authResponse);
    return false;
}  

$(document).ready(function(){
    var session = unescape($.cookie('user'));
    if( session == null ) {
        createLoginForm($("#userBar"));
    }
    $.getJSON("/get-user", { sid: session }, authResponse);

    });
