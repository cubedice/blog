// javascript uix


// markdown preview window

var converter = new Showdown.converter();

$(document).ready(function(){
    $("textarea").bind("keyup", function(event){
        var text = this.value;
        var html = converter.makeHtml(text);
        $("td.markdownprev").html(html);
    });
});
