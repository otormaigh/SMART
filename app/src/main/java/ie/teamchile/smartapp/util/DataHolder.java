package ie.teamchile.smartapp.util;

import java.util.ArrayList;

public class DataHolder {
	private static DataHolder dataHolder = null;
	private ArrayList<String> thing;

	private DataHolder() {
	}

	public static synchronized DataHolder getInstance() {
		if (dataHolder == null) {
			dataHolder = new DataHolder();
		}
		return dataHolder;
	}

	public ArrayList<String> getThing() {
		return thing;
	}

	public void setThing(ArrayList<String> thing) {
		this.thing = thing;
	}	
}