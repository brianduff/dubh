//
// $Id: validation.js,v 1.1 2001-04-16 02:50:57 briand Exp $
//
// Assorted useful utility functions for validating HTML forms.
//
// (C) 2001 Brian Duff
//


//
// Validate a mandatory field.
//
// Parameters:
//		id - the id of the element whose value is mandatory
//
// Returns:
//		true if the field has been set, false if not.
//
function validateMandatory(id)
{
	return !(document.getElementById(id).value == "");

}

//
// Validates an email address
//
// Parameters:
//     id - the id of an element whose value contains an email address
//   
// Returns:
//     true if the email address is valid, false otherwise.
//
function validateEmail(id)
{
	var strAddress = document.getElementById(id).value;

  if (strAddress == "username@host.com")
  {
    alert("username@host.com is just an example email address. Please enter your real email address.");
    return false;
  }

	// The field must contain a @ character	
	var atIdx = strAddress.indexOf('@');
	if (atIdx == -1)
	{
		return false;
	}
	
	// The at symbol can't be the first character
	if (atIdx == 0)
	{
		return false;
	}
	
	// ... or the last character
	if (atIdx == strAddress.length - 1)
	{
		return false;
	}
	
	// And there cannot be more than one @ character
	atIdx = strAddress.indexOf('@', atIdx+1);
	if (atIdx != -1)
	{
		return false;
	}
	
	
	// But we're not too anal about the rest of the address.
	
	return true;
}


//
// Focus and select a field
//
// Parameters:
//
// 		id - the field to focus
//
function grabFocus(id)
{
	document.getElementById(id).focus();
	document.getElementById(id).select();
}

//
// Clear the contents of a field
//
// Parameters:
//
//		id - the field to clear
//
function clear(id)
{
	document.getElementById(id).value = "";
}

//
// Validate that the values of two elements match.
//
// Parameters:
//
//		firstId - id of the first field
//		secondId - id of the second field
function validateMatch(firstId, secondId)
{
	return (document.getElementById(firstId).value == document.getElementById(secondId).value);
}


//
// Shows some hint text (the title property) on the page.
//
// Parameters:
//    idElement - the id of the element to show the title property of
//    idHintElement - the id of an element with a text node (e.g. a
//      paragraph, which will display the hint text.
//
function showHint(idElement, idHintElement)
{
  var strHint = document.getElementById(idElement).getAttribute("title");
  document.getElementById(idHintElement).firstChild.nodeValue = strHint;
}