package com.team3.smartapp;

import android.app.Activity;
import android.os.Bundle;
import connecttodb.AccessDBTable;
import connecttodb.GetAuthKey;

public class ContactDetailsActivity extends Activity {
	String DBUrl = "http://54.72.7.91:8888/service_users/14";
	String loginUrl = "http://54.72.7.91:8888/login";
	String username = "team_chile";
	String password = "smartappiscoming";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_details);
		
/*		GetAuthKey getToken = new GetAuthKey();
		String token = null;
		token = getToken.getAuthKey(username, password);
		
		//get string representation of the response from the database
		AccessDBTable accessTable = new AccessDBTable();
		String response = accessTable.accessDB(token, DBUrl);*/
	}
}
