package my.edu.utar;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String name;
	private String member_type;
	private boolean excl_reward;
	private List<Booking> bookings;
	
	public boolean getExcl_reward() {
		return excl_reward;
	}
	
	public void removeExcl_Reward() {
		excl_reward = false;
	}
	
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public String getName() {
		return name;
	}
	public List<Booking> getBookings(){
		return bookings;
	}
	
	public String getMember_type() {
		return member_type;
	}
	
	public User(String name, String member_type, boolean excl_reward) {
		this.name = name;
		this.member_type = member_type;
		this.excl_reward = excl_reward;
		bookings = new ArrayList<Booking>();
	}
	
	public void addBooking(Booking booking) {
		bookings.add(booking);
	}
	
	public Booking getBooking(int bookingID) {
		for(Booking theBooking: bookings) {
			if (theBooking.getID() == bookingID) {
				if (theBooking.getAllocatedStatus()) {
					return theBooking;

				}
			}
		}
		return null;
	}
	
	public void removeBooking(Booking booking) {
		boolean found = false;
		Booking bookingToRemove = null;
		
		for(Booking theBooking: bookings) {
			if (theBooking.getID() == booking.getID()) {
					found = true;
					bookingToRemove = theBooking;

			}
		}
		
		if(found)
			bookings.remove(bookingToRemove);
	}
}
