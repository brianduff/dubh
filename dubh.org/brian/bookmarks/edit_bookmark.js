

function init()
{
   var bTit = document.getElementById("bookmarkTitle");
   bTit.focus();
   bTit.select();
}

function validate()
{
   var bTit = document.getElementById("bookmarkTitle");
   if (bTit.value == "")
   {
      alert("You must enter a title for the bookmark");
      bTit.focus();
      bTit.select();
      return false;
   }
   var bU = document.getElementById("bookmarkURL");
   if (bU.value == "")
   {
      alert("You must enter a URL for the bookmark");
      bU.focus();
      bU.select();
      return false;
   }
   return true;
}

function returnToBookmarks()
{
   document.location="showbookmarks.xsql?editmode=true";
}

function doSubmit()
{

   if (!validate())
   {
      return false;
   }
   

   var title = document.getElementById("bookmarkTitle").value;
   var url = document.getElementById("bookmarkURL").value;
   var id = document.getElementById("bookmarkId").value;
   document.location = "secure/do_edit_bookmark.xsql?title="+escape(title)+"&url="+escape(url)+"&id="+escape(id);

}