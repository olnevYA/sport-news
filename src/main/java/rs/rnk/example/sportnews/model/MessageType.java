package rs.rnk.example.sportnews.model;

public enum MessageType {
	SUCCESS("success"), ERROR("danger"), INFO("info");
	
	private String name;
	
	MessageType(String type) {
		name = type;
	}
	
	public String getName() {
		return name;
	}
}
