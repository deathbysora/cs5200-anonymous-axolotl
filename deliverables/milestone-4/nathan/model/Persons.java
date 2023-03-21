package BeerApp.model;

public class Persons {
	protected String userName;
	
	public Persons(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
