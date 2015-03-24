package edu.upc.eetac.dsa.FelipeBoix.books.api.model;

public class User {

		private String username;
		private String userpass;
		private String name;
		private boolean loginSuccessful;	
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getUserpass() {
			return userpass;
		}
		public void setUserpass(String userpass) {
			this.userpass = userpass;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isLoginSuccessful() {
			return loginSuccessful;
		}
		public void setLoginSuccessful(boolean loginSuccessful) {
			this.loginSuccessful = loginSuccessful;
		}

	 
	}

	
