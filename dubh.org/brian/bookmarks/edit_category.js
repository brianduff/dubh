

function init()
{
   var bTit = document.getElementById("categoryTitle");
   bTit.focus();
   bTit.select();
}

function validate()
{
   var bTit = document.getElementById("categoryTitle");
   if (bTit.value == "")
   {
      alert("You must enter a title for the category");
      bTit.focus();
      bTit.select();
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
   

   var title = document.getElementById("categoryTitle").value;
   var id = document.getElementById("catId").value;
   document.location = "secure/do_edit_category.xsql?title="+escape(title)+"&id="+escape(id);

}