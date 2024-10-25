package org.acme.runforfun.facade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import org.acme.runforfun.dao.EventDAO;
import org.acme.runforfun.dao.EventDominoDAO;
import org.acme.runforfun.data.Event;
import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class EventCRUDFacade implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Utils utils;	
	
	private EventDAO dao = new EventDominoDAO();	
	
	public EventCRUDFacade() {
		utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
		utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
	}

	public boolean remove(Event event) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName + " -> " + event.getUnid());
		boolean success = false;
		try {
			success = dao.remove(event);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return success;
	}	

	public boolean save(Event event) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		boolean success = false;
		try {
			success = dao.save(event);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return success;
	}
		
	public Event getObject(String key) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		Event event = null;
		try {
			event = dao.getObject(key);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return event;
	}
	
	public ArrayList<JsonJavaObject> getObjects() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		return dao.getObjects();
	}

	public ArrayList<JsonJavaObject> getObjects(String key) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		ArrayList<JsonJavaObject> objects = new ArrayList<>();
		if (key.equalsIgnoreCase("forward")) {
			objects = dao.getObjectsForward();
		} else if (key.equalsIgnoreCase("past")) {
			objects = dao.getObjectsPast();
		} else {
			objects = dao.getObjects(key);
		}		
		return objects;
	}

	public ArrayList<JsonJavaObject> loadScoreboard(Event event) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString()  + " " + methodName);
		ArrayList<JsonJavaObject> scoreboard = new ArrayList<>();
		try {
			scoreboard = dao.loadScoreboard(event);
		} catch(Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return scoreboard;
	}
}
