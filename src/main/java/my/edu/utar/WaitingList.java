package my.edu.utar;

import java.util.ArrayList;
import java.util.List;

public class WaitingList {
	private List<User> VIP;
	private List<User> member;
	private List<User> normal;
	
	public WaitingList() {
		VIP = new ArrayList<User>();
		member = new ArrayList<User>();
		normal = new ArrayList<User>();
	}
	
	public void addWaiting(User user) {
		switch(user.getMember_type()) {
			case "VIP":
				VIP.add(user);
				break;
			case "Member":
				member.add(user);
				break;
			case "Normal":
				normal.add(user);
				break;
		}
	}
	
	public List<User> getWaiting(String member_type){
		switch(member_type) {
			case "VIP":
				return VIP;
			case "Member":
				return member;
			case "Normal":
				return normal;
			default:
				return null;
		}
	}
	
	public void removeWaiting(User user) {
		boolean found = false;
		User userToRemove = null;
		switch(user.getMember_type()) {
			case "VIP":
				found = false;
				userToRemove = null;
				
				for(User theUser: VIP) {
					if (theUser.getName() == user.getName()) {
					
						userToRemove = theUser;
						found = true;

					}
				}
				if(found) {
					VIP.remove(userToRemove);
				
				}
				break;
			case "Member":
				found = false;
				userToRemove = null;
				
				for(User theUser: member) {
					if (theUser.getName() == user.getName()) {
					
						userToRemove = theUser;
						found = true;

					}
				}
				if(found) {
					member.remove(userToRemove);
				
				}
				break;
			case "Normal":
				found = false;
				userToRemove = null;
				
				for(User theUser: normal) {
					if (theUser.getName() == user.getName()) {
					
						userToRemove = theUser;
						found = true;

					}
				}
				if(found) {
					normal.remove(userToRemove);
				
				}
				break;
			}
	}
}
