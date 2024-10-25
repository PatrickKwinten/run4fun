function dummy(){
	//library need sat least one function :-?
}

function sendHTMLEmail(){
	try {
		var mail = new HTMLMail(); 
		var email = null;
		if (viewScope.containsKey("tmpEmail")) {
			email = viewScope.get("tmpEmail");
		}
		var message = null;
		if (viewScope.containsKey("tmpTxt")) {
			message = viewScope.get("tmpTxt");
		}
		var page = null;
		if (viewScope.containsKey("tmpPage")) {
			page = viewScope.get("tmpPage");
		}
		if (null != message) {
			message = @ReplaceSubstring(message,"<link>",page)
		}
	
		if (null != email && null != message && null != page) {
			mail.setTo(email);
			mail.setSubject( "Hi there, a notification from Run 4 Fun for you!" );
			mail.addHTML(message);
			mail.send();
		}
	} catch(e) {
		var msg = "Error: " + e;
		log.logError(msg, SEVERITY_HIGH, e, null, context.getUrl().toString(), "ssjsUtils -> sendHTMLMail", null, null);
	}
}