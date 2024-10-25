package org.acme.runforfun.app;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.commons.util.io.json.JsonJavaObject;

import org.acme.runforfun.data.Event;
import org.acme.runforfun.facade.EventCRUDFacade;

public class EventBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Utils utils;
	
	public Event event;
	
	private ArrayList<JsonJavaObject> events = new ArrayList<JsonJavaObject>();
	
	private EventCRUDFacade eventCrud;
	
	public EventBean(){
		super();
		utils = Utils.get();
		try{
			utils.printToConsole("================================");
			Session sess = Factory.getSession(SessionType.CURRENT);
			utils.printToConsole(this.getClass().getSimpleName().toString() + " - EventBean() for:" + sess.getEffectiveUserName());
			utils.printToConsole("================================");
			eventCrud = new EventCRUDFacade();
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		} 		
	}	
	
	public boolean save() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		boolean success = false;
		try {
			success = eventCrud.save(getEvent());
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
			success = eventCrud.remove(this.event);
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}	
		return success;
	}	
		
	public Event loadEvent() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		this.event = null;
		try {
			String qParam = utils.getUrlParameterValue("unid");
			utils.printToConsole("qParam = " + qParam);	
			if (null != qParam && !qParam.equals("")) {
				this.event = eventCrud.getObject(qParam);
			} else {
				this.event = new Event();			
				Date today = new Date();
				this.event.setCreated(today);
				Session sess = Factory.getSession(SessionType.CURRENT); 
				String author = sess.getEffectiveUserName();
				this.event.setCreator(author);
				this.event.setStatus("new");
				this.event.setEditable(true);
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return this.event;
	}
	
	public Event loadEvent(String unid) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		this.event = null;
		try {			
			utils.printToConsole("unid = " + unid);	
			if (null != unid && !unid.equals("")) {
				this.event = eventCrud.getObject(unid);
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return this.event;
	}
	
	public ArrayList<JsonJavaObject> loadEvents() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		this.events = null;
		try {
			String qParam = utils.getUrlParameterValue("q");
			utils.printToConsole("qParam = " + qParam);			
			if (null != qParam && !qParam.equals("")) {
				this.events = eventCrud.getObjects(qParam);
			} else {
				this.events = eventCrud.getObjects();
			}			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}		
		return this.events;
	}
	
	public ArrayList<JsonJavaObject> loadScoreboard(){
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);	
		ArrayList<JsonJavaObject> scoreboard = new ArrayList<>();
		try {
			String qParam = utils.getUrlParameterValue("unid");
			utils.printToConsole("unid = " + qParam);
			if (StringUtils.isNotBlank(qParam)) {
				this.event = loadEvent(qParam);
				scoreboard = eventCrud.loadScoreboard(this.event);
			}			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return scoreboard;
	}
	
	public Integer loadScoreboardPosition(String eventUnid, String runnerUnid){
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);	
		Integer position = -1;
		try {
			if (StringUtils.isNotBlank(eventUnid) && StringUtils.isNotBlank(runnerUnid)) {
				//position = eventCrud.loadScoreboardPosition(eventUnid, runnerUnid);
				
				this.event = loadEvent(eventUnid);
				if (null != this.event) {
					ArrayList<JsonJavaObject> scoreboard  = eventCrud.loadScoreboard(this.event);
					int index = 1;
					for (JsonJavaObject obj : scoreboard) {
						System.out.println("obj = " + obj.toString());
						
						if (obj.containsKey("runnerUnid")) {
							System.out.println("runner = " + obj.getAsString("runnerUnid"));
							if (runnerUnid.equalsIgnoreCase(obj.getAsString("runnerUnid"))) {
								System.out.println("we have a match for " + runnerUnid);
								System.out.println("position in array = " + (index));
								position = index;
								
								break;
							}
						}
						index++;
					}
				}
				
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return position;
	}
	
	public String eventStyleClass(Event event) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);	
		String styleClass = "label label-default";
		try {
			String status = event.getStatus();
			
			if (null == status) {
				//no change, use default
			} else if (status.equalsIgnoreCase("new")) {
				styleClass = "label label-primary";
			} else if (status.equalsIgnoreCase("open")) {
				styleClass = "label label-info";
			} else if (status.equalsIgnoreCase("closed")) {
				styleClass = "label label-success";
			} else if (status.equalsIgnoreCase("running")) {
				styleClass = "label label-warning blink_me";
			} else if (status.equalsIgnoreCase("ended")) {
				styleClass = "label label-default";
			}else {
				//no change, use default
			}
			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return styleClass;	
	}
	
	public String eventIcon(Event event) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();		
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);	
		String icon = "<i class='fa fa-question-circle-o' aria-hidden='true'></i>";
		try {
			String eventNew = "<i class='fa fa-leaf' aria-hidden='true'></i>";
			String eventOpen = "<i class='fa fa-fire' aria-hidden='true'></i>";
			String eventRunning = "<i class='fa fa-heartbeat' aria-hidden='true'></i>";
			String eventClosed = "<i class='fa fa-lock' aria-hidden='true'></i>";
			String eventEnded = "<i class='fa fa-flag-checkered' aria-hidden='true'></i>";
			String status = event.getStatus();
			if (null == status) {
				//change noting
			} else if (status.equalsIgnoreCase("new")) {
				icon = eventNew;
			} else if (status.equalsIgnoreCase("open")) {
				icon = eventOpen;
			} else if (status.equalsIgnoreCase("closed")) {
				icon = eventClosed;
			} else if (status.equalsIgnoreCase("running")) {
				icon = eventRunning;
			} else if (status.equalsIgnoreCase("ended")) {
				icon = eventEnded;
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}	
		return icon;
	}
		
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setEvents(ArrayList<JsonJavaObject> events) {
		this.events = events;
	}

	public ArrayList<JsonJavaObject> getEvents() {
		return events;
	}

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(Utils utils) {
		this.utils = utils;
	}
	
}
