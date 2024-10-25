package org.acme.runforfun.data;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.FacesContext;

import org.acme.runforfun.utils.Utils;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class Subscription implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Utils utils;
	
	private String unid;
	private Date created;
	private String creator;
	
	private String event;
	private String eventUnid;
	private String runner;
	private String runnerUnid;
	
	private String target;
	
	private boolean editable;
	
	public Subscription() {
		utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
		utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
	}

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(Utils utils) {
		this.utils = utils;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getRunner() {
		return runner;
	}

	public void setRunner(String runner) {
		this.runner = runner;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getEventUnid() {
		return eventUnid;
	}

	public void setEventUnid(String eventUnid) {
		this.eventUnid = eventUnid;
	}

	public String getRunnerUnid() {
		return runnerUnid;
	}

	public void setRunnerUnid(String runnerUnid) {
		this.runnerUnid = runnerUnid;
	}
	
	
}
