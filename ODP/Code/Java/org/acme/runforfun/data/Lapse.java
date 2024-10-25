package org.acme.runforfun.data;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.FacesContext;

import org.acme.runforfun.utils.Utils;
import org.openntf.domino.DateTime;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class Lapse  implements Serializable {

private static final long serialVersionUID = 1L;
	
	private Utils utils;
	
	private DateTime start;
	private DateTime end;
	
	private String event;
	private String runner;
	
	
	private String unid;
	private boolean editable;
	private Date created;
	private String creator;
	
	private String score;
	
	public Lapse() {
		utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
		utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
	}

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(Utils utils) {
		this.utils = utils;
	}

	public DateTime getStart() {
		return start;
	}

	public void setStart(DateTime start) {
		this.start = start;
	}

	public DateTime getEnd() {
		return end;
	}

	public void setEnd(DateTime end) {
		this.end = end;
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

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	
}
