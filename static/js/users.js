/*Session info using XHR
*/ 

function setCookie(c_name,value,expiredays)
{
  
  var exdate=new Date();
    exdate.setDate(exdate.getDate()+expiredays);
    document.cookie=c_name+ "=" +escape(value)+";path="+
        ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
}

function getCookie(c_name)
{
    var results = document.cookie.match ( '(^|;) ?' + c_name + '=([^;]*)(;|$)' );

    if ( results )
        return ( unescape ( results[2] ) );
    else
        return null;
}

function createLoginForm(userBar)
{
    userBar.replaceWith("\
            <div id='userBar'><form id='loginForm' method='get'>\
            <label>username:<input id='loginForm-username' type='text' \
            name='username'/></label>\
            <label>password:<input id='loginForm-password' type='password'\
            name='password'/></label>\
            <input type='submit' id='loginSubmit' value='Log in'/> \
            </form></div>");
    $("#loginSubmit").click(login);
}

function createStatusBar(userBar, userData)
{
    var content;
    if(userData.authlevel != null && userData.authlevel == 'root') {
        content = "<div id='userBar'>" + userData.username +
            " <a id='logout' href=''>log out</a>\
            <a id='newPost' href='/create-post'>new post</a></div>";
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
     
        document.cookie = '';
        createLoginForm($("#userBar"));
    } 
    else {
        setCookie('user', data.sessionid, 7);
        createStatusBar($("#userBar"), data);
    }
}

function logout()
{
    var session = unescape(getCookie('user'));
    $.getJSON("/logout", { sid: session },
        function() {
        document.cookie = '';
            document.location = '/'; 
        });
    return false;
}  

$(document).ready(function(){
    var session = getCookie('user');
    
    if( session == null ) {
        createLoginForm($("#userBar"));
    }
    else {
        $.getJSON("/get-user", { sid: unescape(session) }, authResponse);
    }
});
