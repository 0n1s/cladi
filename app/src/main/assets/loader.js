document.getElementById("imageFileForm").onsubmit = function()
{

    var img_front=document.getElementById("img_front").value;
	var img_back=document.getElementById("img_back").value;
    var both = img_front +"two"+img_back;
return window.AndroidInterface.processHTML(both);

}