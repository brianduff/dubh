var isBookmarkSelected = false;
var secureURL = "http://www.dubh.org:8080/brian/bookmarks/secure";

function bookmarkRadioSelected(isSelected)
{
   var bTitLab = document.getElementById("bookmarkTitleLabel");      
   var bTit = document.getElementById("bookmarkTitle");     
   var bULab = document.getElementById("bookmarkURLLabel");    
   var bU = document.getElementById("bookmarkURL");         
   var catLabel = document.getElementById("categoryTitleLabel");     
   var cat = document.getElementById("categoryTitle");
   
   bTit.disabled = !isSelected;
   bU.disabled = !isSelected;
   cat.disabled = isSelected;
   
   bTitLab.style.color = (isSelected ? "#000000" : "#909090");
   bULab.style.color = (isSelected ? "#000000" : "#909090");
   catLabel.style.color = (isSelected ? "#909090" : "#000000");   
   
   if (isSelected)
   {
      bTit.focus();
      bTit.select();
   }
   else
   {
      cat.focus();
      cat.select();
   }
   
   isBookmarkSelected = isSelected;
}

function init()
{
   document.getElementById("bookmarkRadio").checked = true;
   bookmarkRadioSelected(true);
}

function validate()
{
   if (isBookmarkSelected)
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
   else
   {
      var cat = document.getElementById("categoryTitle");
      if (cat.value == "")
      {
         alert("You must enter a title for the category");
         cat.focus();
         cat.select();
         return false;
      }
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
   var catId = document.getElementById("categoryId").value;
   

   if (isBookmarkSelected)
   {
      var title = document.getElementById("bookmarkTitle").value;
      var url = document.getElementById("bookmarkURL").value;
      document.location = secureURL + "/do_add_bookmark_to_category.xsql?bookmarkTitle="+escape(title)+"&bookmarkURL="+escape(url)+"&catid="+escape(catId);
   }
   else
   {
      var title = document.getElementById("categoryTitle").value;
      document.location = secureURL + "/do_add_category_to_category.xsql?categoryTitle="+escape(title)+"&catid="+escape(catId);
   }
}