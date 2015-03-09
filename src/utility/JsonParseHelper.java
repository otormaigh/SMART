package utility;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

/* {
"appointments": {
    "clinic_id": 4, 
    "date": "2015-03-05", 
    "id": 64, 
    "links": {
        "service_options": "/appointments/64/service_options", 
        "service_provider": "service_providers/14", 
        "service_user": "service_users/1"
    }, 
    "priority": "scheduled", 
    "service_option_ids": [], 
    "service_provider_id": 14, 
    "service_user": {
        "gestation": null, 
        "id": 1, 
        "name": "Shannon Mercury"
    }, 
    "service_user_id": 1, 
    "time": "10:15:00", 
    "visit_logs": [], 
    "visit_type": "post-natal"
	}
}
"clinics": [
    {
        "address": "Leopardstown Shopping Centre, Unit 12, Ballyogan Road, Dublin 18", 
        "announcement_ids": [], 
        "appointment_interval": 15, 
        "closing_time": "15:00:00", 
        "days": {
            "friday": false, 
            "monday": false, 
            "saturday": false, 
            "sunday": false, 
            "thursday": false, 
            "tuesday": true, 
            "wednesday": false
        }, 
        "id": 2, 
        "links": {
            "announcements": "announcements", 
            "service_options": "/service_options"
        }, 
        "name": "Leopardstown", 
        "opening_time": "09:00:00", 
        "recurrence": "weekly", 
        "service_option_ids": [
            1
        ], 
        "type": "booking"
    }
]
*/

public class JsonParseHelper {
	private static final String APPOINTMENTS = "appointments";
	private static final String CLINICS = "clinics";
	private static final String CLINIC_ID = "clinic_id";
	private static final String ADDRESS = "address";
	private JSONObject json;
	private String tableName, tableKey, clinicID, address;
	
	public JsonParseHelper() {		
	}
	
	public void jsonParseHelper(JSONObject json, String tableName, String tableKey) {		
		this.json = json;
		this.tableName = tableName;
		this.tableKey = tableKey;
	}

	public void parser() throws JSONException{ 
		switch (tableName) {
		case APPOINTMENTS:
			switch (tableKey) {
			case CLINIC_ID:
				clinicID = json.get("clinic_id").toString();
				AppointmentSingleton.getInstance().setClinicID(clinicID);
				break;
			}
			break;
		case CLINICS:
			switch (tableKey) {
			case ADDRESS:
				address = json.get("address").toString();
				//ClinicSingleton.getInstance().setAddress(address);
				break;
			}
			break;
		}			
	}
	
	public void thing() throws JSONException{
		ArrayList<String> idList = AppointmentSingleton.getInstance().getListOfIDs("2", "2015-03-17");
		JSONObject newJson;
		for(int i = 0; i < idList.size(); i++){
			String apptStr = AppointmentSingleton.getInstance().getAppointmentString(String.valueOf(i));
			newJson = new JSONObject(apptStr);
			jsonParseHelper(newJson, "appointments", "clinic_id");
			//AppointmentSingleton.getInstance().getClinicID();
			jsonParseHelper(newJson, "clinics", "address");
			//ClinicSingleton.getInstance().getAddress();
		}
	}
}