package org.acme.runforfun.dao;

import java.util.ArrayList;

import org.acme.runforfun.data.Runner;

import com.ibm.commons.util.io.json.JsonJavaObject;

public interface RunnerDAO {

	public ArrayList<JsonJavaObject> getObjects(String qParam);

	public ArrayList<JsonJavaObject> getObjects();

	public boolean save(Runner runner);
	
	boolean remove(Runner runner);

	public Runner getObjectByUnid(String qParam);

	public ArrayList<JsonJavaObject> getCandidateObjects();

}
