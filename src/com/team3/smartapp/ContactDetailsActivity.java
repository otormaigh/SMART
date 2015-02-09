package com.team3.smartapp;

import android.app.Activity;
import android.os.Bundle;
import connecttodb.AccessDBTable;
import connecttodb.GetToken;

public class ContactDetailsActivity extends Activity {
	String username = "team_chile";
	String password = "smartappiscoming";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_details);
		
/*		GetToken getToken = new GetToken();
		String token = null;
		token = getToken.getAuthKey(username, password);
		
		//get string representation of the response from the database
		AccessDBTable accessTable = new AccessDBTable();
		String response = accessTable.accessDB(token, DBUrl);*/
	}
}
