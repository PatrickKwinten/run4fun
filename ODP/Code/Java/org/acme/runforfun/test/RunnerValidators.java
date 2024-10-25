package org.acme.runforfun.test;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class RunnerValidators implements Serializable{

	private static final long serialVersionUID = 1L;
	private Utils utils;
	
	private Properties propStrings;
	
	public RunnerValidators() {		
		utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
		//utils = Utils.get();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
		try {
			propStrings = utils.getPropertiesFile("strings.properties");	
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
	}
	
	public void valName(FacesContext facesContext, UIComponent component, Object value) {		   
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		if (value.toString().replaceAll("\\s+","").equals("")){			
			String msg = propStrings.getProperty("runnerValidateName");
			FacesMessage message = new FacesMessage(msg);
			throw new ValidatorException(message);		
		}		
	}
	
	public void valEmail(FacesContext facesContext, UIComponent component, Object value) {		   
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		if (value.toString().replaceAll("\\s+","").equals("")){			
			String msg = propStrings.getProperty("runnerValidateEmail");
			FacesMessage message = new FacesMessage(msg);
			throw new ValidatorException(message);		
		}		
	}
}