package org.acme.runforfun.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import org.acme.runforfun.dao.SubscriptionDAO;
import org.acme.runforfun.dao.SubscriptionDominoDAO;
import org.acme.runforfun.data.Subscription;
import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class SubscriptionCRUDFacade implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Utils utils;
	
	private SubscriptionDAO dao = new SubscriptionDominoDAO();	
	
	public SubscriptionCRUDFacade() {
		utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
		utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
	}

	public boolean save(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		boolean success = false;
		try {
			success = dao.save(subscription);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return success;
	}

	public ArrayList<JsonJavaObject> getObjects(String qParam) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		ArrayList<JsonJavaObject> objects = new ArrayList<>();
		try {
			objects = dao.getObjects(qParam);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return objects;
	}

	public Subscription getObjectByUnid(String qParam) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		Subscription subscription = null;
		try {
			subscription = dao.getObjectByUnid(qParam);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return subscription;
	}

	public boolean startLapse(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		boolean success = false;
		try {
			success = dao.startLapse(subscription);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}		
		return success;
	}

	public boolean stopLapse(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		boolean success = false;
		try {
			success = dao.stopLapse(subscription);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}		
		return success;
	}

	public ArrayList<JsonJavaObject> getLapses(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		ArrayList<JsonJavaObject> lapses = new ArrayList<>();
		try {
			lapses = dao.getLapses(subscription);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return lapses;
	}

	public boolean lastLapseInfo(Subscription subscription, String button) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		boolean visible = false;
		try {
			visible = dao.lastLapseInfo(subscription, button);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}		
		return visible;
	}

	public int summOfAllLapses(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		int summ = 0;
		try {
			summ = dao.summOfAllLapses(subscription);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return summ;
	}	
}
