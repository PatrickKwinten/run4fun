package org.acme.runforfun.data;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.FacesContext;

import org.acme.runforfun.utils.Utils;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class Runner implements Serializable{

private static final long serialVersionUID = 1L;
	
	private Utils utils;
	
	private String name;
	private String email;
	private String status;
	private String information;
	
	private String unid;
	private Date created;
	private String creator;
	
	private boolean editable;
	
	public Runner() {
		utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
		utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
	}

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(Utils utils) {
		this.utils = utils;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
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

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	
}
