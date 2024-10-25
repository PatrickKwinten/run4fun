package org.acme.runforfun.utils;

//Copyright 2011 Paul Withers, Nathan T. Freeman & Tim Tripcony

//Please extend with your JSF/XPages unique methods - Patrick Kwinten

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.Vector;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.servlet.http.Cookie;

import lotus.domino.NotesException;

import com.ibm.commons.util.io.base64.Base64;
import com.ibm.xsp.application.DesignerApplicationEx;
import com.ibm.xsp.component.UIViewRootEx;
import com.ibm.xsp.designer.context.XSPContext;
import com.ibm.xsp.designer.context.XSPUrl;
import com.ibm.xsp.designer.context.XSPUserAgent;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class JSFUtils {
	
	public JSFUtils() throws Exception {
		super();
	}

	public static DesignerApplicationEx getApplication() {
		return (DesignerApplicationEx) getFacesContext().getApplication();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getApplicationScope() {
		return getServletContext().getApplicationMap();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCompositeData() {
		return (Map<String, Object>) getVariableResolver().resolveVariable(getFacesContext(), "compositeData");
	}    

	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRequestScope() {
		return getServletContext().getRequestMap();
	}

	public static ExternalContext getServletContext() {
		return getFacesContext().getExternalContext();
	}

	public static Object getRequest() {
		return getFacesContext().getExternalContext().getRequest();
	}

	public static Object getResponse() {
		return getFacesContext().getExternalContext().getResponse();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSessionScope() {
		return getServletContext().getSessionMap();
	}

	private static VariableResolver getVariableResolver() {
		return getApplication().getVariableResolver();
	}

	public static UIViewRootEx getViewRoot() {
		return (UIViewRootEx) getFacesContext().getViewRoot();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getViewScope() {
		return getViewRoot().getViewMap();
	}

	public static XSPContext getXSPContext() {
		return XSPContext.getXSPContext(getFacesContext());
	}

	public static Object resolveVariable(String variable) {
		return FacesContext.getCurrentInstance().getApplication().getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(), variable);
	}

	public static XSPUserAgent getUserAgent(){
		return getXSPContext().getUserAgent();
	}    

	@SuppressWarnings("unchecked")
	public Vector<String> getCurUserRoles() {
		Vector<String> curUserRoles = null;
		try {
			curUserRoles = ExtLibUtil.getCurrentDatabase().queryAccessRoles(ExtLibUtil.getCurrentSession().getEffectiveUserName());
		} catch (NotesException e) {
			curUserRoles = new Vector<String>();
		}
		return curUserRoles;
	}

	public boolean hasRole(String role) {
		try {
			Vector<String> roles = this.getCurUserRoles();
			if (roles.contains(role))
				return true;
			if (roles.contains("["+role+"]"))
				return true;
			return false;
		} catch (Exception e) {            
			return false;
		}
	}

	public static String getQueryStringParameter(String q){
		try{
			return XSPContext.getXSPContext(getFacesContext()).getUrlParameter(q);
		}catch(Exception e){
			return "";
		}
	}    

	public static void showMessageOnconsole(String smessage){
		System.out.println(smessage);
	}

	public static Object getBean(String expr){
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		ValueBinding binding = app.createValueBinding("#{" + expr + "}");
		Object value = binding.getValue(context);
		return value;
	}
	/**
	 * The method creates a {@link javax.faces.el.ValueBinding} from the
	 * specified value binding expression and returns its current value.<br>
	 * <br>
	 * If the expression references a managed bean or its properties and the bean has not
	 * been created yet, it gets created by the JSF runtime.
	 * 
	 * @param ref value binding expression, e.g. #{Bean1.property}
	 * @return value of ValueBinding
	 * throws javax.faces.el.ReferenceSyntaxException if the specified <code>ref</code> has invalid syntax
	 */
	public static Object getBindingValue(String ref) {
		FacesContext context=FacesContext.getCurrentInstance();
		Application application=context.getApplication();
		return application.createValueBinding(ref).getValue(context);
	}

	/**
	 * The method creates a {@link javax.faces.el.ValueBinding} from the
	 * specified value binding expression and sets a new value for it.<br>
	 * <br>
	 * If the expression references a managed bean and the bean has not
	 * been created yet, it gets created by the JSF runtime.
	 * 
	 * @param ref value binding expression, e.g. #{Bean1.property}
	 * @param newObject new value for the ValueBinding
	 * throws javax.faces.el.ReferenceSyntaxException if the specified <code>ref</code> has invalid syntax
	 */
	public static void setBindingValue(String ref, Object newObject) {
		FacesContext context=FacesContext.getCurrentInstance();
		Application application=context.getApplication();
		ValueBinding binding=application.createValueBinding(ref);
		binding.setValue(context, newObject);
	}

	/**
	 * The method returns the value of a global JavaScript variable.
	 * 
	 * @param varName variable name
	 * @return value
	 * @throws javax.faces.el.EvaluationException if an exception is thrown while resolving the variable name
	 */
	public static Object getVariableValue(String varName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().getVariableResolver().resolveVariable(context, varName);
	}

	/**
	 * Finds an UIComponent by its component identifier in the current
	 * component tree.
	 * 
	 * @param compId the component identifier to search for
	 * @return found UIComponent or null
	 * 
	 * @throws NullPointerException if <code>compId</code> is null
	 */
	public static UIComponent findComponent(String compId) {
		return findComponent(FacesContext.getCurrentInstance().getViewRoot(), compId);
	}

	/**
	 * Finds an UIComponent by its component identifier in the component tree
	 * below the specified <code>topComponent</code> top component.
	 * 
	 * @param topComponent first component to be checked
	 * @param compId the component identifier to search for
	 * @return found UIComponent or null
	 * 
	 * @throws NullPointerException if <code>compId</code> is null
	 */
	@SuppressWarnings("unchecked")
	public static UIComponent findComponent(UIComponent topComponent, String compId) {
		if (compId==null) {
			throw new NullPointerException("Component identifier cannot be null");
		}        
		if (compId.equals(topComponent.getId())) {
			return topComponent;
		}        
		if (topComponent.getChildCount()>0) {
			List<UIComponent> childComponents= topComponent.getChildren();            
			for (UIComponent currChildComponent : childComponents) {
				UIComponent foundComponent=findComponent(currChildComponent, compId);
				if (foundComponent!=null) {
					return foundComponent;
				}
			}
		}
		return null;
	}

	public static Object getSubmittedValue(String compId) {
		UIComponent c = findComponent(FacesContext.getCurrentInstance().getViewRoot(), compId);
		// value submitted from the browser
		Object o = ((UIInput) c).getSubmittedValue();        
		if( null == o ){
			// else not yet submitted
			o = ((UIInput) c).getValue();
		}
		return o;
	}

	public static String getResource(String bundleVar, String name) {
		XSPContext xspContext = XSPContext.getXSPContext(FacesContext.getCurrentInstance());        
		try {
			// There has to be bundle defined with var name <bundlevar>
			ResourceBundle bundle = xspContext.bundle(bundleVar);
			if (bundle != null) {
				return bundle.getString(name);
			}
		} catch (IOException e) {
			XspOpenLogUtil.logError(e);
		}        
		return "";
	}

	public static String getAttachmentURL(String docID, String attachmentName){
		try {
			String base = getBaseURL();
			return base + "/0/" + docID + "/$File/" + attachmentName;
		} catch (Exception e) {
			XspOpenLogUtil.logError(e);
		}
		return "";
	}

	public static String getAttachmentOpenURL(String docID, String attachmentName){
		try {
			String base = getBaseURL();
			String middle = "/xsp/.ibmmodres/domino/OpenAttachment";
			if(base.substring(0,4).equals("/xsp")){
				middle += base.substring(4);
			}else{
				middle += base;
			}
			return base + middle + "/" + docID + "/$File/" + attachmentName + "?Open";
		} catch (Exception e) {
			XspOpenLogUtil.logError(e);
		}
		return "";
	}

	public static String getBaseURL(){
		try {
			XSPUrl url = JSFUtils.getXSPContext().getUrl();
			String address = url.getAddress();
			String rel = url.getSiteRelativeAddress(JSFUtils.getXSPContext());            
			String step1 = address.substring(0,address.indexOf(rel));
			String step2 = step1.substring(step1.indexOf("//")+2);            
			return step2.substring(step2.indexOf("/"));
		} catch (Exception e) {
			XspOpenLogUtil.logError(e);
		}
		return "";
	}    

	public static String showIconCustType(String typ){
		if (typ.equalsIgnoreCase("private")){
			return "<i class=\"fa fa-user\" aria-hidden=\"true\"></i>";
		}else if (typ.equalsIgnoreCase("company")){
			return "<i class=\"fa fa-building\" aria-hidden=\"true\"></i>";
		}else if (typ.equalsIgnoreCase("soletrader")){
			return "<i class=\"fa fa-user-plus\" aria-hidden=\"true\"></i>";
		}else if (typ.equalsIgnoreCase("corpgroup")){
			return "<i class=\"fa fa-industry\" aria-hidden=\"true\"></i>";
		}else {
			return "";
		}
	}

	public static String getLTPAToken(){
		if(getFacesContext().getExternalContext().getRequestCookieMap().containsKey("LtpaToken")){
			javax.servlet.http.Cookie token = (Cookie) getFacesContext().getExternalContext().getRequestCookieMap().get("LtpaToken");            
			//return token.getValue().substring(0,token.getValue().indexOf("/"));
			return token.getValue();
		}else{
			return null;
		}
	}

	public static Vector<String> convertStringListToVector(List<String> list){
		Vector<String> v = new Vector<String>();
		Collections.copy(v,list);
		return v;
	}

	public static Vector<Object> convertListToVector(List<Object> list){
		Vector<Object> v = new Vector<Object>();
		Collections.copy(v,list);
		return v;
	}

	public static List<Object> convertObjectVectorToList(Vector<Object> values){
		return new ArrayList<Object>(values);
	}

	@SuppressWarnings("unchecked")
	public static Vector<Object> getVector(Object object) {
		Vector<Object> result = new Vector<Object>();
		try {
			if (!object.getClass().getName().endsWith("Vector")) {
				result.add(object);
			} else {
				result = (Vector<Object>) object;
			}
		} catch (Exception e) {
			XspOpenLogUtil.logError(e);
		}
		return result;
	}    

	public static boolean parseBool(String s){
		return "1".equals(s);
	}

	public static String parseBoolenToString(boolean b){
		return b ? "1" : "0";
	}

	public static void addMessage(Severity type, String msg) {
		JSFUtils.getFacesContext().addMessage(null,new javax.faces.application.FacesMessage(type, msg, ""));
	}

	public static void addMessageToSpecificField(String fieldId, String message) {
		String clientId = JSFUtils.findComponent(fieldId).getClientId(JSFUtils.getFacesContext());
		JSFUtils.getFacesContext().addMessage(clientId,new javax.faces.application.FacesMessage(message));
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		// //getFacesContext().addMessage(null, new
		// javax.faces.application.FacesMessage(type, msg, ""));
		// To convert the InputStream to String we use the
		// Reader.read(char[] buffer) method. We iterate until the
		// Reader return -1 which means there's no more data to
		// read. We use the StringWriter class to produce the string.
		//
		Writer writer = new StringWriter();
		try {
			if (is != null) {                
				char[] buffer = new char[1024];                
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} else {
				return "xxx";
			}
		} catch (Exception e) {
			XspOpenLogUtil.logError(e);            
		} finally {
			is.close();
		}
		return writer.toString();        
	}

	public static List<String> convertVectorToList(Vector<String> values){
		return new ArrayList<String>(values);
	}

	public static String getUniqueID(){
		return UUID.randomUUID().toString();
	}

	public static boolean hasPasswordChanged(String currentPassword, String newPassword){
		try{
			String base64Password = Base64.encode(newPassword);
			return !base64Password.equals(currentPassword);
		}catch(Exception e){
			XspOpenLogUtil.logError(e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static void clearFacesMessages() {
		FacesContext context = FacesContext.getCurrentInstance();
		Iterator<FacesMessage> it = context.getMessages();
		while (it.hasNext()) {
			it.next();
			try {
				it.remove();
			} catch (Exception e) {
				XspOpenLogUtil.logError(e);
			}
		}
	}

	public static Database getCurrentDatabase() {
		return (Database) getVariableResolver().resolveVariable(getFacesContext(), "database");
	}

	public static Session getCurrentSession() {
		return (Session) getVariableResolver().resolveVariable(getFacesContext(), "session");
	}

	public static String getCurrentUsername() {
		return getCurrentSession().getEffectiveUserName();
	}
}