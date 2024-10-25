package org.acme.runforfun.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.designer.context.XSPContext;
import com.ibm.xsp.designer.context.XSPUrl;
import com.ibm.xsp.extlib.util.ExtLibUtil;

import org.acme.runforfun.utils.JSFUtils;

public class Utils implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Boolean debug;
	private Boolean openlog;	
	
	private HashMap<String,Boolean> debugClasses = new HashMap<String,Boolean>();
	
	public Boolean debugActive;
	
	public Utils() {
		if (debugClasses.isEmpty()) {
			setDebugClasses();
		}
		
		setDebugActive(true);
		setDebug(true);
	}
	
	@SuppressWarnings("unchecked")
	public void setDebugClasses() {	
		//String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		//this.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		try {
			Properties debugs = this.getDebugProperties();			
			Enumeration<String> enums = (Enumeration<String>) debugs.propertyNames();
			while (enums.hasMoreElements()) {
				Boolean check = false;
				String key = enums.nextElement();
				String value = debugs.getProperty(key);
				check = Boolean.valueOf(value);
				debugClasses.put(key, check);
			}
		} catch (IOException e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.WARNING, null);
		}		
	}
		
	public Properties getDebugProperties() throws IOException {
		//String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
		//this.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		InputStream input = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("debug.properties");
		Properties debugs = new Properties();
		debugs.load(input);
		input.close();
		return debugs;
	}
	
	@SuppressWarnings("unchecked")
	public void printToConsole(String msg) {	
		if (this.debug.equals(true) ) {
			Map<String, Object> sesScope = ExtLibUtil.getSessionScope();
			if (sesScope.containsKey("debugClasses")) {				
				Object object = sesScope.get("debugClasses");			
				if (object instanceof ArrayList) {
					ArrayList<Object> scopeClasses = (ArrayList<Object>) object;
					ArrayList<String> selectedClasses = new ArrayList<String>(); 

					for (Object item : scopeClasses) {				
						if (item instanceof String) {					
							selectedClasses.add(item.toString());
						}
					}				

					if (selectedClasses.size()>0) {
						String callerClassName = getCallerCallerClassName();
						if (selectedClasses.contains(callerClassName)) {							
							if (this.openlog.equals(true)) {				
								XspOpenLogUtil.logEvent(new Throwable(), msg, Level.INFO, null);
							} 
						}
					}
				} else {
					XspOpenLogUtil.logEvent(null, null, "scope variable debugClasses contains data in wrong format", Level.INFO, null);
				}
			}
		}		
	}
	
	public static String getCallerCallerClassName() { 
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		String callerClassName = null;
		for (int i=1; i<stElements.length; i++) {
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(Debug.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) {
				if (callerClassName==null) {
					callerClassName = ste.getClassName();
				} else if (!callerClassName.equals(ste.getClassName())) {
					return ste.getClassName();
				}
			}
		}
		return null;
	}
	
	public Boolean validValueInPropertyFile(Properties file,String key) {
		Boolean valid = false;
		try {
			if (null != key) {
				if (null != file) {
					if (file.containsKey(key)) {
						if (!file.getProperty(key).isEmpty()) {
							valid = true;
						} else {
							XspOpenLogUtil.logEvent(null, null, "Property file " + file.getProperty("propertyFileName") + " value is empty for key: " + key, Level.WARNING, null);
						}
					} else {
						XspOpenLogUtil.logEvent(null, null, "Property file " + file.getProperty("propertyFileName") + " is missing key: " + key, Level.SEVERE, null);
					}	
				} else {
					XspOpenLogUtil.logEvent(null, null, "Property file is null", Level.WARNING, null);
				}					
			}	
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString() + " - key = " + key + " for file " + file.stringPropertyNames(), Level.SEVERE, null);
		}
		return valid;
	}
	
	public Properties getPropertiesFile(String propertyFileName) throws IOException {
		InputStream input = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(propertyFileName);
		Properties props = new Properties();
		props.load(input);
		props.setProperty("propertyFileName", propertyFileName);
		input.close();
		return props;
	}
	
	public Boolean canEdit(String dbName, String unid) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		this.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName + " -> " + unid);
		Boolean canEdit = false;
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getDatabase(null, dbName);			
			if (db.isOpen()) {
				if (this.isValidUnid(unid)) {					
					Document doc = db.getDocumentByUNID(unid);
					if (null != doc) {						
						if (StringUtils.isNotBlank(doc.getUniversalID())) {							
							NotesContext nctx = NotesContext.getCurrent();							
							if (nctx.isDocEditable(doc)) {								
								canEdit = true;
							}						} 
					}
				} else {
					String url = JSFUtils.getXSPContext().getUrl().toString();
					XspOpenLogUtil.logEvent(null, methodName + " - unid is invalid UNID. " + url, Level.WARNING, null);
				}
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}		
		this.printToConsole("canEdit = " + canEdit);
		return canEdit;
	}
	
	public boolean isValidUnid(String unid) {
		boolean valid = false;
		try {
			if (unid.replaceAll(" ", "").length() == 32) {
				valid = true;
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		this.printToConsole("valid = " + valid);
		return valid;		
	}
	
	public String getUrlParameterValue(String param) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		this.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);	
		this.printToConsole("param = " + param);
		String paramValue = null;
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			XSPContext context = XSPContext.getXSPContext(facesContext);
			XSPUrl xspUrl = context.getUrl();
			paramValue = xspUrl.getParameter(param);
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return paramValue;
	}
	
	public HashMap<String, String> convertScopeToMap(String scopeName) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		this.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
        HashMap<String, String> map = new HashMap<String, String>();
        try {
        	Map<String, Object> scope = null;
        	if (scopeName.equalsIgnoreCase("application")) {
        		scope = JSFUtils.getApplicationScope();
        	} else if (scopeName.equalsIgnoreCase("session")) {
        		scope = JSFUtils.getSessionScope();
        	} else if (scopeName.equalsIgnoreCase("view")) {
        		scope = JSFUtils.getViewScope();
        	} else if (scopeName.equalsIgnoreCase("request")) {
        		scope = JSFUtils.getRequestScope();
        	}
	       
			for (Map.Entry<String, Object> entry : scope.entrySet()) {
		        map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		    }        
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}       
        return map;
    }
	
	public HashMap<String, String> convertBundleToMap(String propertyFiles) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		this.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
        HashMap<String, String> map = new HashMap<String, String>();
        try {        
	        InputStream input = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(propertyFiles);
			Properties props = new Properties();
			props.load(input);
			props.setProperty("propertyFileName", propertyFiles);
			input.close();			
			for (Map.Entry<Object, Object> entry : props.entrySet()) {
		        map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		    }        
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}       
        return map;
    }
	
	public String ordinalize(Integer p) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		this.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		String position = "";
		try {
			switch(p) {
			  case 1:
			    position = p + "st";
			    break;
			  case 2:
				position = p + "nd";
			    break;
			  case 3:
				position = p + "rd";
			    break;
			  default:
				position = p + "th";
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}  
		return position;
	}
	
	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public Boolean getOpenlog() {
		return openlog;
	}

	public void setOpenlog(Boolean openlog) {
		this.openlog = openlog;
	}

	public HashMap<String, Boolean> getDebugClasses() {
		return debugClasses;
	}

	public void setDebugClasses(HashMap<String, Boolean> debugClasses) {
		this.debugClasses = debugClasses;
	}


	public Boolean getDebugActive() {
		return debugActive;
	}


	public void setDebugActive(Boolean debugActive) {
		this.debugActive = debugActive;
	}


	public static Utils get() {
		return (Utils) resolveVariable("utilityBean");
	}
	
	public static Object resolveVariable(String variable) {
        return FacesContext.getCurrentInstance().getApplication().getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(), variable);
    }


	public boolean setDateField(String fieldName, Document doc, Date dateValue) {
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			DateTime dt = sess.createDateTime(dateValue);
			dt.setAnyTime();	
			doc.replaceItemValue(fieldName, dt);	
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		} 		
		return false;
	}
	
	
	public String convertSecondsToMinuteFormat(String entry) {
		if (null == entry || entry.equals("")) {
			return "";
		} else {
			Format formatter = new SimpleDateFormat("mm:ss");
			long time = Integer.valueOf(entry)*1000;
	        return formatter.format( new Date( time ));
		}		
	}
	
	public long differenceBetweenMinutes(String target, String time) {
		LocalTime start = parseHelper(target);
	    LocalTime stop = parseHelper(time);
	    Duration duration = Duration.between(start, stop);	  
	    return duration.getSeconds();
	}
	
	private static LocalTime parseHelper(String str) {
		return LocalTime.parse("00:" + str);
	}
	
}
