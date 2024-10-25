package org.acme.runforfun.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;

import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.context.FacesContextEx;


public class ApplicationBean  implements Serializable{

	public static final long serialVersionUID = 1L;

	public Utils utils;
	public JSFUtils utilsJSF;
	private Properties propApplication;
	private Properties propDataSources;
	
	//Session with Authenticated User Access
	private Session sess;
	
	public ApplicationBean() throws Exception {
		super();
		utils = Utils.get();		
		utils.printToConsole("================================");
		Session sess = Factory.getSession(SessionType.CURRENT);
		utils.printToConsole(this.getClass().getSimpleName().toString() + " - ApplicationBean() for: " + sess.getEffectiveUserName());
		utils.printToConsole("================================");
		try {
			this.propApplication = utils.getPropertiesFile("application.properties");
			this.propDataSources = utils.getPropertiesFile("datasources.properties");
			this.sess = Factory.getSession(SessionType.CURRENT);
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
	}
	
	public ArrayList<String> getAllowedDebugClasses(){
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		ArrayList<String> allowedClasses = new ArrayList<>();
		try {
			Properties props = getPropertiesFromFile("debug.properties");
			Enumeration<?> enumeration = props.propertyNames();
			
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				utils.printToConsole("key = " + key);
				utils.printToConsole("file contains key ? " + props.containsKey(key));
				utils.printToConsole("get property with key= " + props.getProperty(key));
				utils.printToConsole(key + " -- " + props.getProperty(key));
				if (props.getProperty(key).equalsIgnoreCase("true")) {
					utils.printToConsole("*******************");
					utils.printToConsole("allowed class found -> " + key);
					allowedClasses.add(key);
					utils.printToConsole("*******************");
				}
			}
			Collections.sort(allowedClasses);
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}		
		return allowedClasses;
	}
	
	public Properties getPropertiesFromFile(String fileName) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		Properties prop = new Properties();
		try {
			if (null != fileName && !fileName.isEmpty()) {
				InputStream is = FacesContextEx.getCurrentInstance().getExternalContext().getResourceAsStream(fileName);
				
				BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				if (r.ready()) {
					prop.load(r);
				}				
				r.close();				
				is.close();
			}			
		} catch (Exception e) {
			XspOpenLogUtil.logEvent(null, null, fileName, Level.WARNING, null);
		}
		return prop;
	}	
	
	public Properties getPropDataSources() {
		return propDataSources;
	}

	public void setPropDataSources(Properties propDataSources) {
		this.propDataSources = propDataSources;
	}
	
	public Properties getPropApplication() {
		return propApplication;
	}

	public void setPropApplication(Properties propApplication) {
		this.propApplication = propApplication;
	}
	
	public String getCurrentUserName() {		
		return sess.getEffectiveUserName();
	}
	
	public boolean isAnonymous() {
		boolean value = false;
		if (getCurrentUserName().equalsIgnoreCase("anonymous")) {
			value = true;
		}
		return value;
	}
	
	public boolean canCreateDocuments() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		boolean value = false; 
		try {
			Database db = sess.getCurrentDatabase();
			String user = sess.getEffectiveUserName();
			int accPriv = db.queryAccessPrivileges(user);			
			if ((accPriv & Database.DBACL_CREATE_DOCS) > 0) {
				utils.printToConsole("User " + user + " can create documents");
				value = true;
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return value;
	}

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(Utils utils) {
		this.utils = utils;
	}
	
	
	
}
