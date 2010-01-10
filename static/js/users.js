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

function createStatusBar(userBar, userData)
{
    var content;
    if(userData.authlevel != null && userData.authlevel == 'root') {
        url = '/create-post?sid=' + userData.sessionid;
        content = "<div id='userBar'>" + userData.username +
            " <a id='logout' href=''>log out</a>\
            <a id='newPost' href=" + url + ">new post</a></div>";
    }
    else {
        content = "\
            <div id='userBar'>" + userData.username +
            " <a id='logout' href=''>log out</a></div>";
    }
    userBar.replaceWith(content);
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
        createStatusBar($("#userBar"), data);
    }
}

function logout()
{
    var session = unescape($.cookie('user'));
    $.getJSON("/logout", { sid: session },
        function() {
            $.cookie('user', null);
            document.location = '/'; 
        });
    return false;
}  

$(document).ready(function(){
    var session = $.cookie('user');
    if( session == null ) {
        createLoginForm($("#userBar"));
    }
    else {
        $.getJSON("/get-user", { sid: unescape(session) }, authResponse);
    }
});
