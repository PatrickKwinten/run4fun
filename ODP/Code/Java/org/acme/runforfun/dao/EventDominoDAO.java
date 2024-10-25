package org.acme.runforfun.dao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import org.acme.runforfun.app.ApplicationBean;
import org.acme.runforfun.data.Event;
import org.acme.runforfun.data.Runner;
import org.acme.runforfun.facade.RunnerCRUDFacade;
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

public class EventDominoDAO implements EventDAO, Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private Utils utils;
	
	private ApplicationBean appBean;
	private Properties propApplication; 
	private Properties propDataSources;
	
	private Session sess;
	
	private String dtFormatDateOnly;

	public EventDominoDAO() {
		try {		
			//ODA Enablement
			this.sess = Factory.getSession(SessionType.CURRENT);
			utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
			utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
			
			this.appBean = new org.acme.runforfun.app.ApplicationBean();
			propApplication = appBean.getPropApplication();
			propDataSources = appBean.getPropDataSources();
			
			if (utils.validValueInPropertyFile(propApplication,"date_format_date_only")){
				this.dtFormatDateOnly = propApplication.getProperty("date_format_date_only");
			} else {
				this.dtFormatDateOnly = "yyyy-MM-dd";
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
	}	

	@Override
	public boolean save(Event event) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		boolean success = false;
		try {			
			Database db = sess.getCurrentDatabase();
			Document doc = null;
			
			String fieldName = null;
			if (null == event.getUnid()) {				
				doc = db.createDocument();	
				
				fieldName = "Form";
				doc.replaceItemValue(fieldName, "event");				
				
				fieldName = "creator";
				if (null != event.getCreator()) {					
					doc.replaceItemValue(fieldName, event.getCreator());
				}
				
				fieldName="status";
				doc.replaceItemValue(fieldName, "new");	
				
			} else {
				doc = db.getDocumentByUNID(event.getUnid());
			}
			
			fieldName = "name";
			if (null != event.getName()) {
				doc.replaceItemValue(fieldName, event.getName());
			}	
			
			fieldName = "date";
			if (null != event.getDate()){					
				utils.setDateField(fieldName, doc, event.getDate());
			}
			
			fieldName = "start";
			if (null != event.getTime()){					
				doc.replaceItemValue(fieldName, event.getTime());
			}
			
			fieldName = "location";
			if (null != event.getLocation()){
				doc.replaceItemValue(fieldName, event.getLocation());
			}
			
			fieldName = "information";
			if (null != event.getInformation()){				
				doc.replaceItemValue(fieldName, event.getInformation());
			}
			
			fieldName = "status";
			if (null != event.getStatus()){
				doc.replaceItemValue(fieldName, event.getStatus());
			}
			
			//scoring part
			fieldName = "lowBound1";
			if (null != event.getLowBound1()){
				doc.replaceItemValue(fieldName, event.getLowBound1());
			}
			
			fieldName = "lowBound2";
			if (null != event.getLowBound2()){
				doc.replaceItemValue(fieldName, event.getLowBound2());
			}
			
			fieldName = "lowBound3";
			if (null != event.getLowBound3()){
				doc.replaceItemValue(fieldName, event.getLowBound3());
			}
			
			fieldName = "lowBound4";
			if (null != event.getLowBound4()){
				doc.replaceItemValue(fieldName, event.getLowBound4());
			}
			
			fieldName = "lowBound5";
			if (null != event.getLowBound5()){
				doc.replaceItemValue(fieldName, event.getLowBound5());
			}
			
			fieldName = "lowBound6";
			if (null != event.getLowBound6()){
				doc.replaceItemValue(fieldName, event.getLowBound6());
			}
			
			fieldName = "uppBound2";
			if (null != event.getUppBound2()){
				doc.replaceItemValue(fieldName, event.getUppBound2());
			}
			
			fieldName = "uppBound3";
			if (null != event.getUppBound3()){
				doc.replaceItemValue(fieldName, event.getUppBound3());
			}
			
			fieldName = "uppBound4";
			if (null != event.getUppBound4()){
				doc.replaceItemValue(fieldName, event.getUppBound4());
			}
			
			fieldName = "uppBound5";
			if (null != event.getUppBound5()){
				doc.replaceItemValue(fieldName, event.getUppBound5());
			}
			
			fieldName = "uppBound6";
			if (null != event.getUppBound6()){
				doc.replaceItemValue(fieldName, event.getUppBound6());
			}
			
			fieldName = "pointsBound1";
			if (null != event.getPointsBound1()){
				doc.replaceItemValue(fieldName, event.getPointsBound1());
			}
			
			fieldName = "pointsBound2";
			if (null != event.getPointsBound2()){
				doc.replaceItemValue(fieldName, event.getPointsBound2());
			}
			
			fieldName = "pointsBound3";
			if (null != event.getPointsBound3()){
				doc.replaceItemValue(fieldName, event.getPointsBound3());
			}
			
			fieldName = "pointsBound4";
			if (null != event.getPointsBound4()){
				doc.replaceItemValue(fieldName, event.getPointsBound4());
			}
			
			fieldName = "pointsBound5";
			if (null != event.getPointsBound5()){
				doc.replaceItemValue(fieldName, event.getPointsBound5());
			}
			
			fieldName = "pointsBound6";
			if (null != event.getPointsBound6()){
				doc.replaceItemValue(fieldName, event.getPointsBound6());
			}
			
			fieldName = "pointsBonus";
			if (null != event.getPointsBonus()){
				doc.replaceItemValue(fieldName, event.getPointsBonus());
			}
			
			fieldName = "Authors";
			Vector<String> authors = new Vector<>();
			authors.add(event.getCreator());
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
	public boolean remove(Event event){
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName + " -> unid = " + event.getUnid());
		boolean success = false;
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()){
				Document doc = db.getDocumentByUNID(event.getUnid());
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
	public ArrayList<JsonJavaObject> getObjects(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList<JsonJavaObject> getObjectsForward() {		
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		ArrayList<JsonJavaObject> JSONObjects = new ArrayList<JsonJavaObject>();
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				String viewKey = "vw_events_JSON";
				String viewName = "";
				if (utils.validValueInPropertyFile(propDataSources,viewKey)){
					viewName = propDataSources.getProperty(viewKey);
				}
				View vw = db.getView(viewName);
				if (null != vw) {
					vw.refresh();
					vw.setAutoUpdate(false);
					
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
					
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
							
							Date dateCurrentTmp = new Date();
							String modCurrentTmp = new SimpleDateFormat("yyyy-MM-dd").format(dateCurrentTmp);
							
							Date dateEntry = formatter.parse(filter);
							Date dateCurrent = formatter.parse(modCurrentTmp);

					        boolean equal = dateEntry.equals(dateCurrent);
					        boolean after = dateEntry.after(dateCurrent);
					        if (equal || after) {
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
		return JSONObjects;
	}
	
	@Override
	public ArrayList<JsonJavaObject> getObjectsPast() {		
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		ArrayList<JsonJavaObject> JSONObjects = new ArrayList<JsonJavaObject>();
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				String viewKey = "vw_events_JSON";
				String viewName = "";
				if (utils.validValueInPropertyFile(propDataSources,viewKey)){
					viewName = propDataSources.getProperty(viewKey);
				}
				View vw = db.getView(viewName);
				if (null != vw) {
					vw.refresh();
					vw.setAutoUpdate(false);
					
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
					
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
							
							Date dateCurrentTmp = new Date();
							String modCurrentTmp = new SimpleDateFormat("yyyy-MM-dd").format(dateCurrentTmp);
							
							Date dateEntry = formatter.parse(filter);
							Date dateCurrent = formatter.parse(modCurrentTmp);
					        
					        boolean before = dateEntry.before(dateCurrent);					       
					        if (before) {
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
		return JSONObjects;
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
				String viewKey = "vw_events_JSON";
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
		return JSONObjects;
	}

	@Override
	public Event getObject(String key) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		Event event = null;
		try {
			Database db = sess.getCurrentDatabase();
			Document doc = db.getDocumentByUNID(key, false);
			if (null != doc) {
				event = new Event();
				loadFromDocument(event, doc);
			}
			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return event;
	}
	
	private void loadFromDocument(Event event, Document doc) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		try {
			event.setUnid(doc.getUniversalID());
			event.setEditable(false);
			
			DateFormat df = new SimpleDateFormat(this.dtFormatDateOnly);
			Date tmpDate;
			String fieldName = null;

			fieldName = "name";
			if (doc.hasItem(fieldName)) {
				event.setName(doc.getItemValueString(fieldName));
			}			
			
			fieldName = "location";
			if (doc.hasItem(fieldName)) {
				event.setLocation(doc.getItemValueString(fieldName));
			}			
			
			fieldName = "information";
			if (doc.hasItem(fieldName)) {
				event.setInformation(doc.getItemValueString(fieldName));
			}			
			
			fieldName = "date";
			if (doc.hasItem(fieldName)){
				tmpDate = df.parse(doc.getFirstItem(fieldName).getDateTimeValue().getDateOnly());
				event.setDate(tmpDate);
			}
			
			fieldName = "status";
			if (doc.hasItem(fieldName)) {
				event.setStatus(doc.getItemValueString(fieldName));
			} else {
				event.setStatus(null);
			}
			
			fieldName = "start";
			if (doc.hasItem(fieldName)) {
				event.setTime(doc.getItemValueString(fieldName));
			}
			
			//scoring
			fieldName = "lowBound1";
			if (doc.hasItem(fieldName)) {
				event.setLowBound1(doc.getItemValueString(fieldName));
			}
			fieldName = "lowBound2";
			if (doc.hasItem(fieldName)) {
				event.setLowBound2(doc.getItemValueString(fieldName));
			}
			fieldName = "lowBound3";
			if (doc.hasItem(fieldName)) {
				event.setLowBound3(doc.getItemValueString(fieldName));
			}
			fieldName = "lowBound4";
			if (doc.hasItem(fieldName)) {
				event.setLowBound4(doc.getItemValueString(fieldName));
			}
			fieldName = "lowBound5";
			if (doc.hasItem(fieldName)) {
				event.setLowBound5(doc.getItemValueString(fieldName));
			}
			fieldName = "lowBound6";
			if (doc.hasItem(fieldName)) {
				event.setLowBound6(doc.getItemValueString(fieldName));
			}
			
			
			fieldName = "uppBound2";
			if (doc.hasItem(fieldName)) {
				event.setUppBound2(doc.getItemValueString(fieldName));
			}
			fieldName = "uppBound3";
			if (doc.hasItem(fieldName)) {
				event.setUppBound3(doc.getItemValueString(fieldName));
			}
			fieldName = "uppBound4";
			if (doc.hasItem(fieldName)) {
				event.setUppBound4(doc.getItemValueString(fieldName));
			}
			fieldName = "uppBound5";
			if (doc.hasItem(fieldName)) {
				event.setUppBound5(doc.getItemValueString(fieldName));
			}
			fieldName = "uppBound6";
			if (doc.hasItem(fieldName)) {
				event.setUppBound6(doc.getItemValueString(fieldName));
			}
			
			fieldName = "pointsBound1";
			if (doc.hasItem(fieldName)) {				
				event.setPointsBound1(doc.getItemValueString(fieldName));
			}			
			fieldName = "pointsBound2";
			if (doc.hasItem(fieldName)) {				
				event.setPointsBound2(doc.getItemValueString(fieldName));
			}
			fieldName = "pointsBound3";
			if (doc.hasItem(fieldName)) {
				event.setPointsBound3(doc.getItemValueString(fieldName));
			}
			fieldName = "pointsBound4";
			if (doc.hasItem(fieldName)) {
				event.setPointsBound4(doc.getItemValueString(fieldName));
			}
			fieldName = "pointsBound5";
			if (doc.hasItem(fieldName)) {
				event.setPointsBound5(doc.getItemValueString(fieldName));
			}
			fieldName = "pointsBound6";
			if (doc.hasItem(fieldName)) {
				event.setPointsBound6(doc.getItemValueString(fieldName));
			}
			
			fieldName = "pointsBonus";
			if (doc.hasItem(fieldName)) {
				event.setPointsBonus(doc.getItemValueString(fieldName));
			}
			
			
			if (null != doc.getAuthors()) {
				if (!doc.getAuthors().firstElement().isEmpty()) {
					event.setCreator(doc.getAuthors().firstElement());
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
	public ArrayList<JsonJavaObject> loadScoreboard(Event event) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		ArrayList<JsonJavaObject> JSONObjects = new ArrayList<JsonJavaObject>();
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				String viewKey = "vw_categorized_lapses_completed_JSON";
				String viewName = "";
				if (utils.validValueInPropertyFile(propDataSources,viewKey)){
					viewName = propDataSources.getProperty(viewKey);
				}
				View vw = db.getView(viewName);
				if (null != vw) {				
					vw.refresh();
					vw.setAutoUpdate(false);					

					JsonJavaFactory factory = JsonJavaFactory.instanceEx;

					String keyEvent = event.getUnid();
					utils.printToConsole("groupedKey " + keyEvent);
					System.out.println("groupedKey " + keyEvent);
					ViewNavigator navigator = vw.createViewNavFromCategory(keyEvent,0);
					System.out.println("number of entries for category: " + navigator.getCount());
					//all lapses for the event regardless runner
					
					Integer ColIdx = 2;

					navigator.setCacheGuidance(0);
					navigator.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
					
					TreeMap<String, Integer> runners = new TreeMap<>(); 
					
					// and then traverse the view:
					ViewEntry ve = navigator.getFirst();	

					while (null != ve) {
						if (ve.isValid()) {
							Vector<?> columnValues = ve.getColumnValues();
							String colJson = String.valueOf(columnValues.get(ColIdx));							
							try {
								JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(factory, colJson);
								if (null != json) {
									String runnerUnid = null;
									if (json.containsKey("runnerUnid")) {										
										runnerUnid = json.getAsString("runnerUnid");
										utils.printToConsole("runnerUnid = " + runnerUnid + ".");
									}									

									int score = 0;
									if (json.containsKey("score")) {
										utils.printToConsole("score contains value ? " + json.containsValue("score"));
										utils.printToConsole("score = " + json.getAsString("score") + ".");
										score = json.getAsInt("score");
									}

									if (null != runnerUnid) {
										if (runners.containsKey(runnerUnid)) {
											int tmpTotalScore = runners.get(runnerUnid);
											tmpTotalScore += score;
											runners.put(runnerUnid,tmpTotalScore);
										} else {
											runners.put(runnerUnid,score);
										}
									}
								}
							} catch (JsonException e) {
								XspOpenLogUtil.logError(e);
							}					
						}
						//ODA way:
						ViewEntry nextEntry = navigator.getNextSibling();
						ve = nextEntry;
					}
					vw.setAutoUpdate(true);
					System.out.println("entries in runners = " + runners.size());
										
					Set<Map.Entry<String, Integer>> entries = runners.entrySet();
					System.out.println("entries in entries = " + entries.size());
					
					/*TreeMap<Integer,String> runnersReversed = new TreeMap<>(); 
					for (Map.Entry<String, Integer> entry : entries) {						
						runnersReversed.put(entry.getValue(), entry.getKey());
					}
					System.out.println("entries in runnersReversed = " + runnersReversed.size());*/
					
					
					/*TreeSet<Integer> runnersReversedSet = new TreeSet<>();
					for (Map.Entry<String, Integer> entry : entries) {						
						runnersReversedSet.add(entry.getValue());
					}
					System.out.println("entries in runnersReversedSet = " + runnersReversedSet.size());
					runnersReversedSet.descendingSet();*/
					//System.out.println("runnersReversedSet = " + runnersReversedSet.toString());
					
					
					MultiValueMap<Integer, String> multimap = new MultiValueMap();  
					for (Map.Entry<String, Integer> entry : entries) {						
						multimap.putValue(entry.getValue(), entry.getKey());
					}
					//System.out.println("multimap = " + multimap.toString());
					
					MultiMap<Integer, String> multimap2 = new MultiMap();  
					for (Map.Entry<String, Integer> entry : entries) {						
						multimap2.put(entry.getValue(), entry.getKey());
					}
					//System.out.println("multimap2 = " + multimap2.toString());
					for (Integer score : multimap2.keySet()) {  
					//printing key and values  
					System.out.println(score + ": " + multimap2.get(score));  
					}  
					
					TreeSet<Integer> totalsSorted = new TreeSet<Integer>(new Comparator<Integer>() {
		                  @Override
		                  public int compare(Integer o1, Integer o2) {
		                        //using Comparator to sort elements in descending order.
		                        return o2.compareTo(o1);
		                  }
		           });
					for (Integer score : multimap2.keySet()) {  
						totalsSorted.add(score);
					}
					//totalsSorted.descendingSet();
					System.out.println("totalsSorted = " + totalsSorted.toString());
					
					
					RunnerCRUDFacade runnerCRUD = new RunnerCRUDFacade();
					
					//now we now the order of totals we need to pick their values from the multimap and add to array 
					for (Integer score : totalsSorted) {
						System.out.println("score = " + score);
						
						Collection<String> runnersColl = multimap2.get(score);
						System.out.println("runnersColl = " + runnersColl.toString());
						
						if (runnersColl.size()> 0) {
							System.out.println("multiple values, we need to create multiple json objects");							
						}
						for (String runnerObj: runnersColl) {
							System.out.println("runnerObj = " + runnerObj);
							JsonJavaObject jsonRunnerScore = new JsonJavaObject();
							
							jsonRunnerScore.put("score", score);
							jsonRunnerScore.put("runnerUnid", runnerObj);
							
							Runner runner = runnerCRUD.getObjectByUnid(runnerObj);
							jsonRunnerScore.put("runner", runner.getName());
							
							String keyRunner = runnerObj;
							ViewNavigator navigatorRunner = vw.createViewNavFromCategory(keyEvent + "##" +keyRunner);
							utils.printToConsole("number of entries for category: " + keyRunner + " = " + navigatorRunner.getCount());
							int numOfLapses = navigatorRunner.getCount();						
							jsonRunnerScore.put("lapses",numOfLapses);
							JSONObjects.add(jsonRunnerScore);
						}
						
						
						//JSONObjects.add(jsonRunnerScore);
					}

					/*Map<Integer, String> sortedRunners = new TreeMap<>(Comparator.reverseOrder());
					sortedRunners.putAll(runnersReversed);					
					System.out.println("entries in sortedRunners = " + sortedRunners.size());

					
					Set<Entry<Integer, String>> entriesSorted = sortedRunners.entrySet();					
					System.out.println("entries in entriesSorted = " + entriesSorted.size());
					
					for (Map.Entry<Integer, String> entry : entriesSorted) {						
						JsonJavaObject jsonRunnerScore = new JsonJavaObject();
						
						jsonRunnerScore.put("score", entry.getKey());
						jsonRunnerScore.put("runnerUnid", entry.getValue());
						
						Runner runner = runnerCRUD.getObjectByUnid(entry.getValue());
						jsonRunnerScore.put("runner", runner.getName());
					
						String keyRunner = entry.getValue();
						ViewNavigator navigatorRunner = vw.createViewNavFromCategory(keyEvent + "##" +keyRunner);
						utils.printToConsole("number of entries for category: " + keyRunner + " = " + navigatorRunner.getCount());
						int numOfLapses = navigatorRunner.getCount();						
						jsonRunnerScore.put("lapses",numOfLapses);
						
						//JSONObjects.add(jsonRunnerScore);
					}*/

				}
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		System.out.println("entries in JSONObjects = " + JSONObjects.size());
		return JSONObjects;
	}
	
	
	public class MultiValueMap<K,V> {
	    private final Map<K,Set<V>> mappings = new HashMap<K,Set<V>>();

	    public Set<V> getValues(K key) {
	        return mappings.get(key);
	    }

	    public Boolean putValue(K key, V value) {
	        Set<V> target = mappings.get(key);
	        if(target == null) {
	            target = new HashSet<V>();
	            mappings.put(key,target);
	        }
	        return target.add(value);
	    }
	}
	
	
	public class MultiMap<K, V>  
	{  
	//creating a map of key and value (collection)  
	private Map<K, Collection<V>> map = new HashMap<>();   
	//add the specified value with the specified key in this multimap  
	public void put(K key, V value)  
	{  
	if (map.get(key) == null)   
	{  
	map.put(key, new ArrayList<V>());  
	}  
	map.get(key).add(value);  
	}  
	//associate the specified key with the given value if not already associated with a value  
	public void putIfAbsent(K key, V value)  
	{  
	if (map.get(key) == null)   
	{  
	map.put(key, new ArrayList<>());  
	}  
	// if the value is absent, insert it  
	if (!map.get(key).contains(value))   
	{  
	map.get(key).add(value);  
	}  
	}  
	//the method returns the Collection of values to which the specified key is mapped, or null if this multimap contains no mapping for the key  
	public Collection<V> get(Object key)   
	{  
	return map.get(key);  
	}  
	//the method returns a set view of the keys contained in this multimap  
	public Set<K> keySet()   
	{  
	return map.keySet();  
	}  
	//the method returns a set view of the mappings contained in this multimap  
	public Set<Map.Entry<K, Collection<V>>> entrySet()   
	{  
	return map.entrySet();  
	}  
	//the method returns a Collection view of Collection of the values present in this multimap  
	public Collection<Collection<V>> values()   
	{  
	return map.values();  
	}  
	//Returns true if this multimap contains a mapping for the specified key.  
	public boolean containsKey(Object key)   
	{  
	return map.containsKey(key);  
	}  
	//Removes the mapping for the specified key from this multimap if present and returns the Collection of previous values associated with the key, or null if there was no mapping for key  
	public Collection<V> remove(Object key)   
	{  
	return map.remove(key);  
	}  
	//Returns the total number of key-value mappings in this multimap.  
	public int size()  
	{  
	int size = 0;  
	for (Collection<V> value: map.values())   
	{  
	size += value.size();  
	}  
	return size;  
	}  
	//Returns true if this multimap contains no key-value mappings.  
	public boolean isEmpty()   
	{  
	return map.isEmpty();  
	}  
	//Removes all the mappings from this multimap.  
	public void clear()   
	{  
	map.clear();  
	}  
	//Removes the entry for the specified key only if it is currently mapped to the specified value and returns true if removed  
	public boolean remove(K key, V value)  
	{  
	if (map.get(key) != null) // key exists  
	return map.get(key).remove(value);  
	return false;  
	}  
	//Replaces the entry for the specified key only if currently mapped to the specified value and return true if replaced  
	public boolean replace(K key, V oldValue, V newValue)  
	{  
	if (map.get(key) != null)  
	{  
	if (map.get(key).remove(oldValue))   
	{  
	return map.get(key).add(newValue);  
	}  
	}  
	return false;  
	}  
	}  
	
}
