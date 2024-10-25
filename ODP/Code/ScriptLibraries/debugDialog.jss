function dumpObjectValue(){
	var s = viewScope.dumpName;
	//print("what is s? " + s);
	if (!s) {
		//print("s is no good");
		return null;
	}
	if (s=="jsSimple") {
		var o = new Object()
		o.propNull = null 
		o.propUndefined = undefined 
		o.propString = "a String" 
		o.propNumber = 79
		o.propBoolean = true
		return o;	
	} else if (s=="httpServletRequest") { 
		return facesContext.getExternalContext().getRequest();	
	} else if (s=="httpSession") { 
		return facesContext.getExternalContext().getSession(true);	
	} else if (s=="document1") {
		document1.getDocument().replaceItemValue("dateTime",session.createDateTime(new Date()))
		document1.getDocument().replaceItemValue("dateRange",session.createDateRange(new Date(),new Date()))
		return document1;
	} else if (s=="systemProperties") {
		return java.lang.System.getProperties();
	} else if (s=="debug") {
		return utilityBean.convertBundleToMap("debug.properties");
	} else if (s=="strings") {
		return utilityBean.convertBundleToMap("strings.properties");
	} else if (s=="datasources") {
		return utilityBean.convertBundleToMap("datasources.properties");		
	} else if (s=="application") {
		return utilityBean.convertBundleToMap("application.properties");
	} else if (s=="applicationScope") {
		return utilityBean.convertScopeToMap("application");
	} else if (s=="sessionScope") {
		return utilityBean.convertScopeToMap("session");
	} else if (s=="viewScope") {
		return utilityBean.convertScopeToMap("view");
	} else if (s=="requestScope") {
		return utilityBean.convertScopeToMap("request");
	} else if (s=="userAgent") {
		return jsfUtils.getUserAgent();
	} else if (s=="httpServletRequest") { 
		return facesContext.getExternalContext().getRequest().toString();
	} else if (s=="httpSession") { 
		return facesContext.getExternalContext().getSession(true);
	} else {
		//print("all other options")
		// Assume this is just the object name 
		// Issue with 8.5.2 when the eval parem is a Java String (need to convert it to a JS object before)
		return eval((new String(s)).toString());
	}

}