//function is called using x$("#{id:inputText1}", " parameters").

/*
 
 If you want to use jQuery to select your XPage input text fields it will not work out of the box for two reasons:

1) The server assigns a dynamic name to the field at run time
2) The new id tag name contains colons which are special characters in jQuery selectors.

The x$ function overcomes this issue by escaping the colons and returning the jQuery object. 
The dynamically assigned field name is available using â€œ#{id:inputText1}â€� where inputText1 is the id assigned by the developer
Just add the function to your XPage in script tags and call as if you were using a normal jQuery selector (see below)

Usage - Note the " " before parameters - important as a string is being created for the whole selector.
If the assigned id name is inputText1 the selector is: x$(â€œ#{id:inputText1}â€�, " parameters (optional)"). 

Example #1 (no parameters)
x$(â€œ#{id:inputText1}â€�).css(â€˜borderâ€™, â€™2px solid greenâ€™)

Example #2 (create alternate rows in a viewPanel table)
x$("#{id:viewPanel1}", " tr:even").css(â€˜backgroundâ€™, â€™yellowâ€™)
 
 */

function x$(idTag, param){ //Updated 18 Feb 2012
   idTag=idTag.replace(/:/gi, "\\:")+(param ? param : "");
   return($("#"+idTag));
}

function enterNumbersOnly(){
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		event.returnValue = true;
	} else {
	    event.returnValue = false;
	}
}