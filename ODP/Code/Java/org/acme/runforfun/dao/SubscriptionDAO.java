package org.acme.runforfun.dao;

import java.util.ArrayList;

import org.acme.runforfun.data.Subscription;

import com.ibm.commons.util.io.json.JsonJavaObject;

public interface SubscriptionDAO {

	boolean save(Subscription subscription);

	ArrayList<JsonJavaObject> getObjects(String qParam);

	Subscription getObjectByUnid(String qParam);

	boolean startLapse(Subscription subscription);

	boolean stopLapse(Subscription subscription);

	ArrayList<JsonJavaObject> getLapses(Subscription subscription);

	boolean lastLapseInfo(Subscription subscription, String button);

	int summOfAllLapses(Subscription subscription);

}
