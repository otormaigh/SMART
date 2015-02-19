package Enums;

public enum CredentialsEnum {
	API_KEY("1e7db54d-df97-407a-868b-9dc50dce7883"),
	ENCODETYPE("ISO-8859-1"),
	URL("http://54.72.7.91:8888/");
	private String content;
	
	private CredentialsEnum(String content){
		this.content = content;
	}
	public String toString(){
		switch (this) {
        case API_KEY:
             return content;
        case ENCODETYPE:
        	return content;
        case URL:
        	return content;
		}
		return null;
	}
}