package org.acme.runforfun.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;

import javax.faces.context.FacesContext;

import org.acme.runforfun.app.ApplicationBean;
import org.acme.runforfun.data.Event;
import org.acme.runforfun.data.Subscription;
import org.acme.runforfun.facade.EventCRUDFacade;
import org.acme.runforfun.utils.JSFUtils;
import org.acme.runforfun.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.openntf.domino.Database;
import org.openntf.domino.DateTime;
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

public class SubscriptionDominoDAO implements SubscriptionDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Utils utils;
	
	private ApplicationBean appBean;
	private Properties propDataSources;
	
	private Session sess;
	
	private Event event;
	
	public SubscriptionDominoDAO() {
		try {		
			//ODA Enablement
			this.sess = Factory.getSession(SessionType.CURRENT);
			utils = (Utils)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), "utilityBean");
			utils.printToConsole(this.getClass().getSimpleName().toString() + " // constructor");
			
			this.appBean = new org.acme.runforfun.app.ApplicationBean();
			propDataSources = appBean.getPropDataSources();			
			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
	}
	
	@Override
	public boolean save(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		boolean success = false;
		try {			
			Database db = sess.getCurrentDatabase();
			Document doc = null;
			
			String fieldName = null;
			if (null == subscription.getUnid()) {				
				doc = db.createDocument();	
				
				fieldName = "Form";
				doc.replaceItemValue(fieldName, "subscription");				
				
				fieldName = "creator";
				if (null != subscription.getCreator()) {					
					doc.replaceItemValue(fieldName, subscription.getCreator());
				} else {
					subscription.setCreator(sess.getEffectiveUserName());
					doc.replaceItemValue(fieldName, sess.getEffectiveUserName());
				}
				
			} else {
				doc = db.getDocumentByUNID(subscription.getUnid());
			}
			
			fieldName = "event";
			if (null != subscription.getEvent()) {
				doc.replaceItemValue(fieldName, subscription.getEvent());
			}
			
			fieldName = "eventUnid";
			if (null != subscription.getEventUnid()) {
				doc.replaceItemValue(fieldName, subscription.getEventUnid());
			}	
			
			fieldName = "runner";
			if (null != subscription.getRunner()) {
				doc.replaceItemValue(fieldName, subscription.getRunner());
			}
			
			fieldName = "runnerUnid";
			if (null != subscription.getRunnerUnid()) {
				doc.replaceItemValue(fieldName, subscription.getRunnerUnid());
			}	
			
			fieldName = "time";
			if (null != subscription.getTarget()){					
				doc.replaceItemValue(fieldName, subscription.getTarget());
			}			
			
			fieldName = "Authors";
			Vector<String> authors = new Vector<>();
			authors.add(subscription.getCreator());
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
	public ArrayList<JsonJavaObject> getObjects(String qParam) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		ArrayList<JsonJavaObject> JSONObjects = new ArrayList<JsonJavaObject>();
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				String viewKey = "vw_subscriptions_JSON";
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
					Integer columnIdxFilter = 0;
					Integer colIdx = 1;

					navigator.setCacheGuidance(400);
					navigator.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
					
					// and then traverse the view:
					ViewEntry entry = navigator.getFirst();	
					
					while (null != entry) {
						if (entry.isValid()) {
							Vector<?> columnValues = entry.getColumnValues();
							String filter = String.valueOf(columnValues.get(columnIdxFilter));
					        boolean equal = filter.equals(qParam);					        
					        if (equal) {
					        	String colJson = String.valueOf(columnValues.get(colIdx));
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

	@Override
	public Subscription getObjectByUnid(String qParam) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		Subscription subscription = null;
		try {
			if (utils.isValidUnid(qParam)) {
				Database db = sess.getCurrentDatabase();
				Document doc = db.getDocumentByUNID(qParam, false);
				if (null != doc) {
					subscription = new Subscription();
					loadFromDocument(subscription, doc);
					EventCRUDFacade eventCRUD = new EventCRUDFacade();
					Event event = eventCRUD.getObject(subscription.getEventUnid());
					this.event = event;
				}
			}			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return subscription;
	}

	private void loadFromDocument(Subscription subscription, Document doc) {		
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		try {
			subscription.setUnid(doc.getUniversalID());
			subscription.setEditable(false);
			
			String fieldName = null;

			fieldName = "runner";
			if (doc.hasItem(fieldName)) {
				subscription.setRunner(doc.getItemValueString(fieldName));
			}
			
			fieldName = "runnerUnid";
			if (doc.hasItem(fieldName)) {
				subscription.setRunnerUnid(doc.getItemValueString(fieldName));
			}
						
			fieldName = "event";
			if (doc.hasItem(fieldName)) {
				subscription.setEvent(doc.getItemValueString(fieldName));
			}
			
			fieldName = "eventUnid";
			if (doc.hasItem(fieldName)) {
				subscription.setEventUnid(doc.getItemValueString(fieldName));
			}
			
			fieldName = "time";
			if (doc.hasItem(fieldName)) {
				subscription.setTarget(doc.getItemValueString(fieldName));
			}			
			
			if (null != doc.getAuthors()) {
				if (!doc.getAuthors().firstElement().isEmpty()) {
					subscription.setCreator(doc.getAuthors().firstElement());
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
	public boolean startLapse(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		boolean success = false;
		try {
			Database db = sess.getCurrentDatabase();
			Document doc = null;
			
			String fieldName = null;
			doc = db.createDocument();	
			fieldName = "Form";
			doc.replaceItemValue(fieldName, "lapse");	
			
			fieldName = "creator";
			doc.replaceItemValue(fieldName, sess.getEffectiveUserName());
			
			fieldName = "eventUnid";
			doc.replaceItemValue(fieldName, subscription.getEventUnid());
			
			fieldName = "runnerUnid";
			doc.replaceItemValue(fieldName, subscription.getRunnerUnid());
			
			fieldName = "start";
			DateTime dt = sess.createDateTime("Today");
		    dt.setNow();
			doc.replaceItemValue(fieldName, dt);
			
			fieldName = "Authors";
			Vector<String> authors = new Vector<>();
			authors.add(sess.getEffectiveUserName());
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
		return success;
	}

	@Override
	public boolean stopLapse(Subscription subscription) {
		// TODO Auto-generated method stub
		//get latest lapse
		//check if has end-date
		//set end date
		//calculate score
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		boolean success = false;
		try {
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				Document doc = null;
				String viewKey = "vw_lapses_JSON";
				String viewName = "";
				if (utils.validValueInPropertyFile(propDataSources,viewKey)){
					viewName = propDataSources.getProperty(viewKey);
				}
				View vw = db.getView(viewName);
				if (null != vw) {
					vw.refresh();
					vw.setAutoUpdate(false);
					doc = vw.getFirstDocumentByKey(subscription.getEventUnid() + "##" + subscription.getRunnerUnid(),true);
					if (null != doc) {
						utils.printToConsole("the latest lap found = " + doc.getUniversalID());
						String fieldName = null;
						fieldName = "start";
						if (doc.hasItem(fieldName)) {
							utils.printToConsole("doc has start date & time field");
							Item item = doc.getFirstItem(fieldName);
							if (!item.getValueDateTimeArray().isEmpty()) {
								utils.printToConsole("start date & time is not empty: " + item.getValueDateTimeArray().toString());
								
								DateTime dtStart = item.getValueDateTimeArray().get(0);
								
								fieldName = "end";
								if (!doc.hasItem(fieldName)) {
									utils.printToConsole("doc has no end date");
									DateTime dtEnd = sess.createDateTime("Today");
								    dtEnd.setNow();
									doc.replaceItemValue(fieldName, dtEnd);
									
									
									lotus.domino.DateTime dtEndOrig = dtEnd;
									int timeDiff = dtEndOrig.timeDifference(dtStart);
									fieldName = "time";
									doc.replaceItemValue(fieldName, timeDiff);
									
									//set the score...
									String target = subscription.getTarget();
									String end = utils.convertSecondsToMinuteFormat(String.valueOf(timeDiff));
									
									long result = utils.differenceBetweenMinutes(target, end);									
									int score = score((int) result, this.event);
									
									fieldName = "score";
									doc.replaceItemValue(fieldName, score);
									
									success = doc.save();
								} else {
									utils.printToConsole("doc has already end date");
								}
							}
						}
					}
				}				
			}			
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return success;
	}
	
	public int score(int i, Event event) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);
		int score = 0;
		int pI = Math.abs(i);		
		
		int lowBound1 = Integer.valueOf(event.getLowBound1());//5
		int lowBound2 = Integer.valueOf(event.getLowBound2());//5
		int lowBound3 = Integer.valueOf(event.getLowBound3());//10
		int lowBound4 = Integer.valueOf(event.getLowBound4());//20
		int lowBound5 = Integer.valueOf(event.getLowBound5());//30
		int lowBound6 = Integer.valueOf(event.getLowBound6());//50
		
		int uppBound2 = Integer.valueOf(event.getUppBound2());//10
		int uppBound3 = Integer.valueOf(event.getUppBound3());//20
		int uppBound4 = Integer.valueOf(event.getUppBound4());//30
		int uppBound5 = Integer.valueOf(event.getUppBound5());//50
		int uppBound6 = Integer.valueOf(event.getUppBound6());//100
		
		int pointsBound1 = Integer.valueOf(event.getPointsBound1());//200
		int pointsBound2 = Integer.valueOf(event.getPointsBound2());//150
		int pointsBound3 = Integer.valueOf(event.getPointsBound3());//100
		int pointsBound4 = Integer.valueOf(event.getPointsBound4());//75
		int pointsBound5 = Integer.valueOf(event.getPointsBound5());//30
		int pointsBound6 = Integer.valueOf(event.getPointsBound6());//10
		
		int pointsBonus = Integer.valueOf(event.getPointsBonus());//500
		
		try {
			if (pI > lowBound6 && pI <= uppBound6) {
				score = pointsBound6;
			} else if (pI > lowBound5 && pI <= uppBound5) {
				score = pointsBound5;
			} else if (pI > lowBound4 && pI <= uppBound4) {
				score = pointsBound4;//75
			} else if (pI > lowBound3 && pI <= uppBound3) {
				score = pointsBound3;//100
			} else if (pI > lowBound2 && pI <= uppBound2) {
				score = pointsBound2;//150
			} else if (pI > 0 && pI <= lowBound1) {
				score = pointsBound1;//200
			} else if (pI == 0) {
				score = pointsBound1 + pointsBonus;//500 + bonus
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		return score; 
	}

	@Override
	public ArrayList<JsonJavaObject> getLapses(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		ArrayList<JsonJavaObject> JSONObjects = new ArrayList<JsonJavaObject>();
		try {
			Session sess = Factory.getSession(SessionType.CURRENT);
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				String viewKey = "vw_categorized_lapses_JSON";
				String viewName = "";
				if (utils.validValueInPropertyFile(propDataSources,viewKey)){
					viewName = propDataSources.getProperty(viewKey);
				}
				View vw = db.getView(viewName);
				if (null != vw) {					
					vw.refresh();
					vw.setAutoUpdate(false);					
					
					JsonJavaFactory factory = JsonJavaFactory.instanceEx;

					String groupedKey = subscription.getEventUnid()+"##"+ subscription.getRunnerUnid();
					ViewNavigator navigator = vw.createViewNavFromCategory(groupedKey);
					
					Integer ColIdx = 2;

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
	public boolean lastLapseInfo(Subscription subscription, String button) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		boolean visible = false;
		try {
			Database db = sess.getCurrentDatabase();
			if (db.isOpen()) {
				Document doc = null;
				String viewKey = "vw_lapses_JSON";
				String viewName = "";
				if (utils.validValueInPropertyFile(propDataSources,viewKey)){
					viewName = propDataSources.getProperty(viewKey);
				}
				View vw = db.getView(viewName);
				if (null != vw) {					
					vw.refresh();
					vw.setAutoUpdate(false);
					doc = vw.getFirstDocumentByKey(subscription.getEventUnid() + "##" + subscription.getRunnerUnid(),true);

					if (null == doc && button.equalsIgnoreCase("start")) {
						//no first document available 
						visible = true;
					} else if (null == doc && button.equalsIgnoreCase("stop")) {
						//no first document available 
						visible = false;
					} else if (null != doc && button.equalsIgnoreCase("start")) {
						if (doc.hasItem("start")) {
							//doc has already a start
							visible = false;
						} else {
							visible = true;
						}
						
						if (doc.hasItem("end")) {
							//last lapse has ended
							visible = true;
						} else {
							visible = false;
						}
					} else if (null != doc && button.equalsIgnoreCase("stop")) {
						if (doc.hasItem("start")) {
							visible = true;
						} else {
							visible = false;
						}
						
						if (doc.hasItem("end")) {
							visible = false;
						} else {
							visible = true;
						}					
					}
					
					vw.setAutoUpdate(true);
				}
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		} 
		return visible;
	}

	@Override
	public int summOfAllLapses(Subscription subscription) {
		String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
		utils.printToConsole(this.getClass().getSimpleName().toString() + " " + methodName);		
		int summ = 0;
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
					Integer columnIdx = 2;
					
					String groupedKey = subscription.getEventUnid()+"##"+ subscription.getRunnerUnid();
					ViewNavigator navigator = vw.createViewNavFromCategory(groupedKey);
					
					navigator.setEntryOptions(ViewNavigator.VN_ENTRYOPT_NOCOUNTDATA);
					ViewEntry entry = navigator.getFirst();
					while (null != entry) {
						if (entry.isValid()) {
							Vector<?> columnValues = entry.getColumnValues();
							
							String colJson = String.valueOf(columnValues.get(columnIdx));
							try {
								JsonJavaObject json = (JsonJavaObject) JsonParser.fromJson(factory, colJson);
								if (null != json) {	
									utils.printToConsole("json = " + json.toString());
									if (json.containsKey("score")) {
										utils.printToConsole("contains score value ? " + json.containsValue("score"));
										utils.printToConsole("contains score value ? " + json.get("score"));
										if (StringUtils.isNotEmpty(json.getAsString("score"))) {
											int score = json.getAsInt("score");
											summ += score;
										} 
									}									
								}
							} catch (JsonException e) {
								XspOpenLogUtil.logError(e);
							}
						}
						ViewEntry nextEntry = navigator.getNext();				       
						entry=nextEntry;
					}					
				}
			}
		} catch (Exception e) {
			XspOpenLogUtil.logErrorEx(e, JSFUtils.getXSPContext().getUrl().toString(), Level.SEVERE, null);
		}
		utils.printToConsole("summ = " + summ);
		return summ;
	}

}
