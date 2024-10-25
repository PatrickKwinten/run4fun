package org.acme.runforfun.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import org.acme.runforfun.data.Runner;
import org.acme.runforfun.facade.RunnerCRUDFacade;
import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.xsp.designer.context.XSPContext;
import com.ibm.xsp.designer.context.XSPUrl;

public class RunnerBean implements Serializable{

private static final long serialVersionUID = 1L;
	
	public Utils utils;
	
	public Runner runner;
	
	private ArrayList<JsonJavaObject> runners = new ArrayList<JsonJavaObject>();
	
	private RunnerCRUDFacade runnerCrud;
	
	public RunnerBean() {
		super();
		utils = Utils.get();
		try{
			utils.printToConsole("================================");
			Session sess = Factory.getSession(SessionType.CURRENT);
			utils.printToConsole(this.getClass().getSimpleName().toString() + " - RunnerBean() for:" + sess.getEffectiveUserName());
			utils.printToConsole("================================");
			this.runnerCrud = new RunnerCRUDFacade();
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		} 		
	}
	
	public Runner loadRunner() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		this.runner = null;
		try {
			String qParam = utils.getUrlParameterValue("unid");
			utils.printToConsole("qParam = " + qParam);	
			if (null != qParam && !qParam.equals("")) {
				this.runner = runnerCrud.getObjectByUnid(qParam);
			} else {
				this.runner = new Runner();			
				Date today = new Date();
				this.runner.setCreated(today);
				Session sess = Factory.getSession(SessionType.CURRENT); 
				String author = sess.getEffectiveUserName();
				this.runner.setCreator(author);			
				this.runner.setEditable(true);
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return this.runner;
	}
	
	public Runner loadRunnerByUnid(String unid) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		this.runner = null;
		try {
			if (null != unid && !unid.equals("")) {
				if (utils.isValidUnid(unid)) {
					this.runner = runnerCrud.getObjectByUnid(unid);
				}				
			} 
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return this.runner;
	}
	
	public ArrayList<JsonJavaObject> loadRunners() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		this.runners = null;
		try {			
			FacesContext facesContext = FacesContext.getCurrentInstance();
			XSPContext context = XSPContext.getXSPContext(facesContext);
			XSPUrl xspUrl = context.getUrl();
			String qParam = xspUrl.getParameter("q");
			utils.printToConsole("qParam = " + qParam);			
			if (null != qParam && !qParam.equals("")) {
				this.runners = runnerCrud.getObjects(qParam);
			} else {
				this.runners = runnerCrud.getObjects();
			}			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return this.runners;
	}
	
	public ArrayList<JsonJavaObject> loadRunners(String qParam) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		this.runners = null;
		try {			
			utils.printToConsole("qParam = " + qParam);			
			if (null != qParam && !qParam.equals("")) {
				this.runners = runnerCrud.getObjects(qParam);
			} else {
				this.runners = runnerCrud.getObjects();
			}			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return this.runners;
	}
	
	public ArrayList<JsonJavaObject> loadCandidates() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		this.runners = null;
		try {			
			this.runners = runnerCrud.getCandidateObjects();	
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return this.runners;
	}
	
	public boolean save() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		boolean success = false;
		try {
			success = runnerCrud.save(getRunner());
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		} 
		utils.printToConsole("success = " + success);
		return success;
	}
	
	public boolean remove(){
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		boolean success = false;
		try {
			success = runnerCrud.remove(this.runner);
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}	
		return success;
	}	

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(Utils utils) {
		this.utils = utils;
	}

	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	public ArrayList<JsonJavaObject> getRunners() {
		return runners;
	}

	public void setRunners(ArrayList<JsonJavaObject> runners) {
		this.runners = runners;
	}

	public RunnerCRUDFacade getRunnerCrud() {
		return runnerCrud;
	}

	public void setRunnerCrud(RunnerCRUDFacade runnerCrud) {
		this.runnerCrud = runnerCrud;
	}
	
	
}
