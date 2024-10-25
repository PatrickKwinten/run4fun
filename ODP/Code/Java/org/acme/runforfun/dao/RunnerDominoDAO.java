package org.acme.runforfun.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import org.acme.runforfun.app.ApplicationBean;
import org.acme.runforfun.data.Runner;
import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.xsp.extlib.util.ExtLibUtil;

public class RunnerDominoDAO implements RunnerDAO, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Utils utils;
	
	private Session sess;
	
	private ApplicationBean appBean;

	private Properties propDataSources;
	
	public RunnerDominoDAO() {
		//ODA Enablement
		this.sess = Factory.getSession(SessionType.CURRENT);
		utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
		utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");		
		try {
			this.appBean = new ApplicationBean();			
			propDataSources = appBean.getPropDataSources();		
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}		
	}

	@Override
	public ArrayList<JsonJavaObject> getObjects(String qParam) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		ArrayList<JsonJavaObject> JSONObjects = new ArrayList<JsonJavaObject>();
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				String viewKey = "vw_runners_JSON";
				String viewName = "";
				if (utils.validValueInPropertyFile(propDataSources,viewKey)){
					viewName = propDataSources.getProperty(viewKey);
				}
				View vw = db.getView(viewName);
				if (null != vw) {
					vw.refresh();
					vw.setAutoUpdate(false);					
					
					JsonJavaFactory factory = JsonJavaFactory.instanceEx;

					ViewNavigator navigator = vw.createViewNav();
					Integer ColIdxFilter = 0;
					Integer ColIdx = 1;

					navigator.setCacheGuidance(400);
					navigator.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
					
					// and then traverse the view:
					ViewEntry entry = navigator.getFirst();	
					
					while (null != entry) {
						if (entry.isValid()) {
							Vector<?> columnValues = entry.getColumnValues();
							String filter = String.valueOf(columnValues.get(ColIdxFilter));
					        boolean equal = filter.equals(qParam);					        
					        if (equal) {
					        	String colJson = String.valueOf(columnValues.get(ColIdx));
								try {
									JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(factory, colJson);
									if (null != json) {
										JSONObjects.add(json);
									}
								} catch (JsonException e) {
									XspOpenLogUtil.logError(e);
								}
					        }							
						}
						//ODA way:
						ViewEntry nextEntry = navigator.getNextSibling();
						entry = nextEntry;
					}
					vw.setAutoUpdate(true);
				}
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		utils.printToConsole("JSONObjects " + JSONObjects.toString());
		return JSONObjects;
	}	

	public Session getSess() {
		return sess;
	}

	public void setSess(Session sess) {
		this.sess = sess;
	}

	@Override
	public ArrayList<JsonJavaObject> getObjects() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		ArrayList<JsonJavaObject> JSONObjects = new ArrayList<JsonJavaObject>();
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				String viewKey = "vw_runners_JSON";
				String viewName = "";
				if (utils.validValueInPropertyFile(propDataSources,viewKey)){
					viewName = propDataSources.getProperty(viewKey);
				}
				View vw = db.getView(viewName);
				if (null != vw) {
					vw.refresh();
					vw.setAutoUpdate(false);					
					
					JsonJavaFactory factory = JsonJavaFactory.instanceEx;

					ViewNavigator navigator = vw.createViewNav();					
					Integer ColIdx = 1;

					navigator.setCacheGuidance(400);
					navigator.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
					
					// and then traverse the view:
					ViewEntry entry = navigator.getFirst();	
					
					while (null != entry) {
						if (entry.isValid()) {
							Vector<?> columnValues = entry.getColumnValues();
							String colJson = String.valueOf(columnValues.get(ColIdx));
							try {
								JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(factory, colJson);
								if (null != json) {
									JSONObjects.add(json);
								}
							} catch (JsonException e) {
								XspOpenLogUtil.logError(e);
							}					
						}
						//ODA way:
						ViewEntry nextEntry = navigator.getNextSibling();
						entry = nextEntry;
					}
					vw.setAutoUpdate(true);
				}
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		utils.printToConsole("JSONObjects " + JSONObjects.toString());
		return JSONObjects;
	}

	@Override
	public boolean save(Runner runner) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		boolean success = false;
		try {			
			Database db = sess.getCurrentDatabase();
			Document doc = null;
			
			String fieldName = null;
			if (null == runner.getUnid()) {
				doc = db.createDocument();	
				
				fieldName = "Form";
				doc.replaceItemValue(fieldName, "runner");				
				
				fieldName = "creator";
				if (null != runner.getCreator()) {					
					doc.replaceItemValue(fieldName, runner.getCreator());
				}	
				
			} else {
				doc = db.getDocumentByUNID(runner.getUnid());
			}
			
			fieldName = "name";
			if (null != runner.getName()) {
				doc.replaceItemValue(fieldName, runner.getName());
			}	
			
			fieldName = "email";
			if (null != runner.getEmail()){				
				doc.replaceItemValue(fieldName, runner.getEmail());
			}
			
			fieldName = "status";
			if (null != runner.getStatus()){
				System.out.println("location = " + runner.getStatus());
				doc.replaceItemValue(fieldName, runner.getStatus());
			}
			
			fieldName = "information";
			if (null != runner.getInformation()){
				doc.replaceItemValue(fieldName, runner.getInformation());
			}
			
			fieldName = "Authors";
			Vector<String> authors = new Vector<>();
			authors.add(runner.getCreator());
			if (null != authors && authors.size() > 0) {
				Item authorField = doc.replaceItemValue(fieldName, authors);
				authorField.setAuthors(true);
			} else {
				Item authorField = doc.replaceItemValue(fieldName, "");
				authorField.setAuthors(true);
			}
			
			success = doc.save(true, false);
			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		utils.printToConsole("success = " + success);
		return success;	
	}
	
	@Override
	public boolean remove(Runner runner){
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName + " -> unid = " + runner.getUnid());
		boolean success = false;
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()){
				Document doc = db.getDocumentByUNID(runner.getUnid());
				if (doc != null) { 
					try{
						success = doc.remove(true);	
					} catch(Exception e){
						XspOpenLogUtil.logError(e);
					}
				}
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		utils.printToConsole("removed ? " + success);
		return success;
	}

	@Override
	public Runner getObjectByUnid(String qParam) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		Runner runner = null;
		try {
			if (utils.isValidUnid(qParam)) {
				Database db = sess.getCurrentDatabase();
				Document doc = db.getDocumentByUNID(qParam, false);
				if (null != doc) {
					runner = new Runner();
					loadFromDocument(runner, doc);
				}
			}			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return runner;
	}

	private void loadFromDocument(Runner runner, Document doc) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		try {
			runner.setUnid(doc.getUniversalID());
			runner.setEditable(false);
			
			String fieldName = null;

			fieldName = "name";
			if (doc.hasItem(fieldName)) {
				runner.setName(doc.getItemValueString(fieldName));
			}			
			
			fieldName = "email";
			if (doc.hasItem(fieldName)) {
				runner.setEmail(doc.getItemValueString(fieldName));
			}			
			
			fieldName = "information";
			if (doc.hasItem(fieldName)) {
				runner.setInformation(doc.getItemValueString(fieldName));
			}			
			
			fieldName = "status";
			if (doc.hasItem(fieldName)) {
				runner.setStatus(doc.getItemValueString(fieldName));
			}			
			
			if (null != doc.getAuthors()) {
				if (!doc.getAuthors().firstElement().isEmpty()) {
					runner.setCreator(doc.getAuthors().firstElement());
				} else {
					XspOpenLogUtil.logEvent(null, this.getClass().getSimpleName().toString() + " " + methodName + " -> GetAuthors().firstElement() is empty. UNID=" + doc.getUniversalID(), Level.SEVERE, null);
				}
			} else {
				XspOpenLogUtil.logEvent(null, this.getClass().getSimpleName().toString() + " " + methodName + " -> GetAuthors() returns null. UNID=" + doc.getUniversalID(), Level.SEVERE, null);
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
	}

	@Override
	public ArrayList<JsonJavaObject> getCandidateObjects() {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		ArrayList<JsonJavaObject> JSONObjects = new ArrayList<JsonJavaObject>();
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				String viewKey = "vw_runners_JSON";
				String viewName = "";
				if (utils.validValueInPropertyFile(propDataSources,viewKey)){
					viewName = propDataSources.getProperty(viewKey);
				}
				View vw = db.getView(viewName);
				if (null != vw) {
					vw.refresh();
					vw.setAutoUpdate(false);					
					
					JsonJavaFactory factory = JsonJavaFactory.instanceEx;

					ViewNavigator navigator = vw.createViewNav();
					Integer ColIdxFilter = 0;
					Integer ColIdx = 1;

					navigator.setCacheGuidance(400);
					navigator.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
					
					// and then traverse the view:
					ViewEntry entry = navigator.getFirst();	
					
					while (null != entry) {
						if (entry.isValid()) {
							Vector<?> columnValues = entry.getColumnValues();
							String filter = String.valueOf(columnValues.get(ColIdxFilter));
					        boolean equal = filter.equals("active");					        
					        if (equal) {
					        	String colJson = String.valueOf(columnValues.get(ColIdx));
								try {
									JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(factory, colJson);
									if (null != json) {
										//Exclude registered runners for event
										if (json.containsKey("UNID")) {
											String eventId = utils.getUrlParameterValue("unid");
											
											String candidateId = eventId + "##" + json.getAsString("UNID");
											
											viewKey = "vw_candidates_JSON";
											viewName = "";
											if (utils.validValueInPropertyFile(propDataSources,viewKey)){
												viewName = propDataSources.getProperty(viewKey);
											}
											View vwCandidates = db.getView(viewName);
											if (null != vwCandidates) {
												vwCandidates.refresh();
												
												Document candidateDoc = vwCandidates.getFirstDocumentByKey(candidateId,true);
												if (null == candidateDoc) {													
													JSONObjects.add(json);
												}
											}											
											
										}
									}
								} catch (JsonException e) {
									XspOpenLogUtil.logError(e);
								}
					        }							
						}
						//ODA way:
						ViewEntry nextEntry = navigator.getNextSibling();
						entry = nextEntry;
					}
					vw.setAutoUpdate(true);
				}
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		utils.printToConsole("JSONObjects " + JSONObjects.toString());
		return JSONObjects;
	}
	
	

}
