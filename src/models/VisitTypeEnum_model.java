package models;

public enum VisitTypeEnum_model {
	ANTENATAL("antenatal"), // before baby
	POSTNATAL("postnatal"); // after baby

	private String visitType;

	private VisitTypeEnum_model(String visitType) {
		this.visitType = visitType;
	}

	public String toString() {
		return this.visitType;
	}
}