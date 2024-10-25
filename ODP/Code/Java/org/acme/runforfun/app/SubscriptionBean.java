package org.acme.runforfun.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

import org.acme.runforfun.data.Subscription;
import org.acme.runforfun.facade.SubscriptionCRUDFacade;
import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class SubscriptionBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public Utils utils;
	
	public Subscription subscription;
	
	private ArrayList<JsonJavaObject> subscriptions = new ArrayList<>();
	
	private SubscriptionCRUDFacade subCrud;
	
	public SubscriptionBean() {
		super();
		utils = Utils.get();
		try{
			utils.printToConsole("================================");
			Session sess = Factory.getSession(SessionType.CURRENT);
			utils.printToConsole(this.getClass().getSimpleName().toString() + " - SubscriptionBean() for:" + sess.getEffectiveUserName());
			utils.printToConsole("================================");
			this.subCrud = new SubscriptionCRUDFacade();
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		} 	
	}
	
	public boolean create() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		boolean success = false;
		try {
			Map<String, Object> vwScope = ExtLibUtil.getViewScope();
			Subscription subscription = new Subscription();
			subscription.setRunner((String) vwScope.get("runnerName"));
			subscription.setRunnerUnid((String) vwScope.get("runnerUnid"));
			subscription.setEvent((String) vwScope.get("eventName"));
			subscription.setEventUnid((String) vwScope.get("eventUnid"));
			subscription.setTarget((String) vwScope.get("timelapse"));
			setSubscription(subscription);
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		} 
		utils.printToConsole("success = " + success);
		return success;
	}
	
	public boolean save() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		boolean success = false;
		try {
			success = subCrud.save(getSubscription());
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		} 
		utils.printToConsole("success = " + success);
		return success;
	}
	
	public ArrayList<JsonJavaObject> loadObjects() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		this.subscriptions = null;
		try {
			String qParam = utils.getUrlParameterValue("unid");
			utils.printToConsole("qParam = " + qParam);			
			if (null != qParam && !qParam.equals("")) {
				this.subscriptions = subCrud.getObjects(qParam);
			}			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		utils.printToConsole(this.subscriptions.toString());
		return this.subscriptions;
	}
	
	public Subscription loadSubscription() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		this.subscription = null;
		try {
			String qParam = utils.getUrlParameterValue("unid");
			utils.printToConsole("qParam = " + qParam);	
			if (null != qParam && !qParam.equals("")) {
				this.subscription = subCrud.getObjectByUnid(qParam);
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return this.subscription;
	}
	
	public boolean startLapse(Subscription subscription) {
		boolean success = false;
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		try {
			success = subCrud.startLapse(subscription);
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return success;
	}
	
	public boolean stopLapse(Subscription subscription) {
		boolean success = false;
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		try {
			success = subCrud.stopLapse(subscription);
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return success;
	}
	
	public boolean lastLapseInfo(Subscription subscription, String button) {
		boolean visible = false;
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		try {
			visible = subCrud.lastLapseInfo(subscription, button);
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return visible;
	}
	
	public ArrayList<JsonJavaObject> getLapses(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		ArrayList<JsonJavaObject> lapses = new ArrayList<>();
		try {
			lapses = subCrud.getLapses(subscription);		
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		utils.printToConsole(lapses.toString());
		return lapses;
	}
	
	public int summOfAllLapses(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);	
		int summ = 0;
		try {
			summ = subCrud.summOfAllLapses(subscription);		
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return summ;		
	}

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(Utils utils) {
		this.utils = utils;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public ArrayList<JsonJavaObject> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(ArrayList<JsonJavaObject> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	
}
