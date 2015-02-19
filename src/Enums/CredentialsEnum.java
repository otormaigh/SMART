package Enums;

public enum CredentialsEnum {
	ENCODETYPE("ISO-8859-1"),
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