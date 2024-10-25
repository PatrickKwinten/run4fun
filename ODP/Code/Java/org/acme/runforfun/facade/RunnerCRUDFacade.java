package org.acme.runforfun.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import org.acme.runforfun.dao.RunnerDAO;
import org.acme.runforfun.dao.RunnerDominoDAO;
import org.acme.runforfun.data.Runner;
import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class RunnerCRUDFacade implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	private Utils utils;
	
	private RunnerDAO dao = new RunnerDominoDAO();
	
	public RunnerCRUDFacade() {
		utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
		utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
	}

	public ArrayList<JsonJavaObject> getObjects(String qParam) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		ArrayList<JsonJavaObject> objects = new ArrayList<>();
		try {			
			if (null != qParam && StringUtils.isNotBlank(qParam)) {				
				objects = dao.getObjects(qParam);
			} else {				
				objects = dao.getObjects();
			}		
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return objects;
	}	

	public ArrayList<JsonJavaObject> getObjects() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		ArrayList<JsonJavaObject> objects = new ArrayList<>();
		try {
			objects = dao.getObjects();
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return objects;
	}

	public Runner getObjectByUnid(String qParam) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		Runner runner = null;
		try {
			runner = dao.getObjectByUnid(qParam);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return runner;
	}

	public boolean save(Runner runner) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		boolean success = false;
		try {
			success = dao.save(runner);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return success;
	}
	
	public boolean remove(Runner runner) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName + " -> " + runner.getUnid());
		boolean success = false;
		try {
			success = dao.remove(runner);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return success;
	}

	public ArrayList<JsonJavaObject> getCandidateObjects() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		ArrayList<JsonJavaObject> objects = new ArrayList<>();
		try {			
			objects = dao.getCandidateObjects();
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return objects;
	}

}
