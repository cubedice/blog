// javascript uix


// markdown preview window
var converter = new Showdown.converter();

// code syntax highlighting
hljs.initHighlightingOnLoad();

$(document).ready(function(){
    $("textarea").bind("keyup", function(event){
        var text = this.value;
        var html = converter.makeHtml(text);
        $("td.markdownprev").html(html);
        var code = $('code');
        for (var i = 0; i < code.length; i++) {
            hljs.highlightBlock(code[i], hljs.tabReplace);
            }
        });
});
