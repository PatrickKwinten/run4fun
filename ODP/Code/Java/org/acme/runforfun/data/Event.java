package org.acme.runforfun.data;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.FacesContext;

import org.acme.runforfun.utils.Utils;

import com.ibm.xsp.extlib.util.ExtLibUtil;


public class Event implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Utils utils;
	
	private Date date;
	private String time;
	private String name;
	private String location;
	private String information;
	
	private String unid;
	private Date created;
	private String creator;
	
	private String status;
	
	private boolean editable;
	
	private String lowBound1;
	private String lowBound2;
	private String lowBound3;
	private String lowBound4;
	private String lowBound5;
	private String lowBound6;
	
	private String uppBound2;
	private String uppBound3;
	private String uppBound4;
	private String uppBound5;
	private String uppBound6;
	
	private String pointsBound1;
	private String pointsBound2;
	private String pointsBound3;
	private String pointsBound4;
	private String pointsBound5;
	private String pointsBound6;
	
	private String pointsBonus;
	
	public Event(){
		utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
		utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
	}

	public Utils getUtils() {
		return utils;
	}

	public void setUtils(Utils utils) {
		this.utils = utils;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLowBound1() {
		return lowBound1;
	}

	public void setLowBound1(String lowBound1) {
		this.lowBound1 = lowBound1;
	}

	public String getLowBound2() {
		return lowBound2;
	}

	public void setLowBound2(String lowBound2) {
		this.lowBound2 = lowBound2;
	}

	public String getLowBound3() {
		return lowBound3;
	}

	public void setLowBound3(String lowBound3) {
		this.lowBound3 = lowBound3;
	}

	public String getLowBound4() {
		return lowBound4;
	}

	public void setLowBound4(String lowBound4) {
		this.lowBound4 = lowBound4;
	}

	public String getLowBound5() {
		return lowBound5;
	}

	public void setLowBound5(String lowBound5) {
		this.lowBound5 = lowBound5;
	}

	public String getLowBound6() {
		return lowBound6;
	}

	public void setLowBound6(String lowBound6) {
		this.lowBound6 = lowBound6;
	}

	public String getUppBound2() {
		return uppBound2;
	}

	public void setUppBound2(String uppBound2) {
		this.uppBound2 = uppBound2;
	}

	public String getUppBound3() {
		return uppBound3;
	}

	public void setUppBound3(String uppBound3) {
		this.uppBound3 = uppBound3;
	}

	public String getUppBound4() {
		return uppBound4;
	}

	public void setUppBound4(String uppBound4) {
		this.uppBound4 = uppBound4;
	}

	public String getUppBound5() {
		return uppBound5;
	}

	public void setUppBound5(String uppBound5) {
		this.uppBound5 = uppBound5;
	}

	public String getUppBound6() {
		return uppBound6;
	}

	public void setUppBound6(String uppBound6) {
		this.uppBound6 = uppBound6;
	}

	public String getPointsBound1() {
		return pointsBound1;
	}

	public void setPointsBound1(String pointsBound1) {
		this.pointsBound1 = pointsBound1;
	}

	public String getPointsBound2() {
		return pointsBound2;
	}

	public void setPointsBound2(String pointsBound2) {
		this.pointsBound2 = pointsBound2;
	}

	public String getPointsBound3() {
		return pointsBound3;
	}

	public void setPointsBound3(String pointsBound3) {
		this.pointsBound3 = pointsBound3;
	}

	public String getPointsBound4() {
		return pointsBound4;
	}

	public void setPointsBound4(String pointsBound4) {
		this.pointsBound4 = pointsBound4;
	}

	public String getPointsBound5() {
		return pointsBound5;
	}

	public void setPointsBound5(String pointsBound5) {
		this.pointsBound5 = pointsBound5;
	}

	public String getPointsBound6() {
		return pointsBound6;
	}

	public void setPointsBound6(String pointsBound6) {
		this.pointsBound6 = pointsBound6;
	}

	public String getPointsBonus() {
		return pointsBonus;
	}

	public void setPointsBonus(String pointsBonus) {
		this.pointsBonus = pointsBonus;
	}

	
}
