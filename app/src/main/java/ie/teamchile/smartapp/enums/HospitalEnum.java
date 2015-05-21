package ie.teamchile.smartapp.enums;

public enum HospitalEnum {
    //Domion Dublin
	NMH_OPD("NMH OPD"),
    LEOPARDSTOWN("Leopardstown"),
    DUN_LAOGHAIRE("Dun Laoghaire"),
    CHURCHTOWN("Churchtown"),
    NMH("NMH");
	
	//Domino WIcklow
	//ETH Dublin
	//ETH Wicklow
	//Satellite

    private String hospital;

    private HospitalEnum(String hospital) {
        this.hospital = hospital;
    }
    
    public String toString() {
        return hospital;
    }
}