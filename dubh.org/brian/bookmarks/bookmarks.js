//
// JavaScript for the Bookmarks Weblet
//
// (C) 2001 Brian Duff

var servletURL = "../servlet/editbookmarks";

// As each title is clicked, hide or restore the corresponding tbody
// We store a list of collapsed tbody ids in a cookie so that the collapsed
// state is persistent
function titleClicked(clickedItem)
{
   if (clickedItem.style.display == "none")
   {
      clickedItem.style.display = "block";
      clickedItem.parentElement.children(0).children(0).children(0).children(0).src = "minus.gif";

      removeFromCollapseList(clickedItem.id);
   }
   else
   {
      clickedItem.style.display = "none";
      clickedItem.parentElement.children(0).children(0).children(0).children(0).src = "plus.gif";                     

      addToCollapseList(clickedItem.id);
   }
}


// Add an item from the collapsed list that is stored in the cookie
function addToCollapseList(itemId)
{
   var collapseList = getCookie("collapseList");
   var aItems = collapseList.split(",");

   // Already in the list?
   for (var i=0; i < aItems.length; i++)
   {
      if (itemId == aItems[i])
      {
         return;
      }
   }

   // OK, not in the list, so add it.
   if (aItems.length == 0)
   {
      collapseList = itemId;
   }
   else
   {
      collapseList = collapseList + "," + itemId;
   }

   // Now set the cookie
   setCookie("collapseList", collapseList);
}

// Remove an item from the collapsed list that is stored in the cookie
function removeFromCollapseList(itemId)
{
   var collapseList = getCookie("collapseList");
   var aItems = collapseList.split(",");

   var newList = "";
   for (var i=0; i < aItems.length; i++)
   {
      if (itemId != aItems[i])
      {
         if (i > 0)
         {
            newList = newList + ",";
         }
         newList = newList + aItems[i];
      }

   }

   // Now set the cookie
   setCookie("collapseList", newList);               
}

// Set a cookie. Replaces any existing cookie with the same name
function setCookie(cookieName, cookieValue)
{
   document.cookie = cookieName + "=" + escape(cookieValue);
}

// Get a cookie.
function getCookie(cookieName)
{
   var aCookie = document.cookie.split("; ");

   for (var i=0; i < aCookie.length; i++)
   {
      var aCrumb = aCookie[i].split("=");

      if (cookieName == aCrumb[0])
      {
         return unescape(aCrumb[1]);
      }
   }
   return "";
}


function imageHoverIn(image, insrc)
{
   image.src = insrc;
   image.className = "toolbarHover";
}

function toggleHoverIn(image, insrc)
{
   image.src = insrc;
}

function imageHoverOut(image, outsrc)
{
   image.src = outsrc;
   image.className = "toolbarFlat";
}

function toggleHoverOut(image, outsrc)
{
   image.src = outsrc;
}

function imageMouseDown(image)
{
   image.className = "toolbarPressed";
}

function imageMouseUp(image)
{
   image.className = "toolbarHover";
}



//
// This function returns the base URL of the document, 
// excluding any parameters expressed in the URL. This
// helps avoid hardcoding the stylesheet target document
// in the stylesheet.
function getBaseURL()
{
   var mylocation = new String(document.location);
   var qmidx = mylocation.lastIndexOf("?");

   if (qmidx < 0)
   {
      return mylocation;
   }
   else
   {
      return mylocation.substring(0, qmidx);
   }
}

function setEditMode(editMode)
{
   document.location =  getBaseURL() + "?editmode=" + editMode;
}

function getServletURL(action, item)
{
   return servletURL + "?action="+action+"&item="+escape(item)+"&srcurl="+escape(document.location);
}

function plusClicked(item)
{
   document.location = getServletURL("add", item);
}               

function deleteClicked(item)
{
   if (confirm("Do you really want to delete this item?"))
   {
      document.location = getServletURL("delete", item);
   }
}

function moveUpClicked(item)
{
   document.location = getServletURL("moveup", item);
}

function moveDownClicked(item)
{
   document.location = getServletURL("movedown", item);
}

function configureClicked(item)
{
   document.location = getServletURL("configure", item);
}

// Collapse any tbody elements that are stored in the collapseList cookie
function restoreCollapseState()
{
   var aItems = getCookie("collapseList").split(",");

   for (i=0; i < aItems.length; i++)
   {
      if (aItems[i] != null && aItems[i].length != null)
      {
         var a = document.all.item(aItems[i]);
         if (a != null)
         {
            // collapse the tbody
            a.style.display = "none";        

            // Use the + image. We have to navigate through the DOM slightly to do this.
            // (table)      (thead)     (tr)        (th)        (img)
            a.parentElement.children(0).children(0).children(0).children(0).src = "plus.gif";
         }
      }
   }
}