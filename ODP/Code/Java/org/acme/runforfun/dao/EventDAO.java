package org.acme.runforfun.dao;

import java.util.ArrayList;

import org.acme.runforfun.data.Event;

import com.ibm.commons.util.io.json.JsonJavaObject;

public interface EventDAO {
	
	boolean save(Event event);
	boolean remove(Event event);
	
	Event getObject(String key);
	
	ArrayList<JsonJavaObject> getObjects();
	ArrayList<JsonJavaObject> getObjects(String key);
	ArrayList<JsonJavaObject> getObjectsForward();
	ArrayList<JsonJavaObject> getObjectsPast();
	ArrayList<JsonJavaObject> loadScoreboard(Event event);
	
}
