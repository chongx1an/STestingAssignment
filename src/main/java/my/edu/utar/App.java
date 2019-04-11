/*package my.edu.utar;

import java.util.List;

public class App {

	public static void main(String[] args) {
		WaitingList waitingList = new WaitingList();
		Room roomAvailable = new Room(1, 1, 3);
		
		
		User user1 = new User("Goh", "VIP", true);
		
		int id = 1;
		Booking booking1 = new Booking(id);
		int room_requested = 1;
		booking1.setBooking(user1, room_requested, roomAvailable, waitingList);
		
		id = 2;
		Booking booking2 = new Booking(id);
		room_requested = 2;
		booking2.setBooking(user1, room_requested, roomAvailable, waitingList);
		
		for(Booking theBooking: user1.getBookings()) {
			System.out.println(user1.getName() + " -> Booking " + theBooking.getID() + " : ");
			Room roomAllocated = theBooking.getRoomAllocated();
			System.out.println(roomAllocated.getVIP() + " VIP Room allocated.");
			System.out.println(roomAllocated.getDeluxe() + " Deluxe Room allocated.");
			System.out.println(roomAllocated.getStandard() + " Standard Room allocated.");
		}
		
		booking1.cancelBooking(user1, id, roomAvailable, waitingList);
		
		for(Booking theBooking: user1.getBookings()) {
			System.out.println(user1.getName() + " -> Booking " + theBooking.getID() + " : ");
			Room roomAllocated = theBooking.getRoomAllocated();
			System.out.println(roomAllocated.getVIP() + " VIP Room allocated.");
			System.out.println(roomAllocated.getDeluxe() + " Deluxe Room allocated.");
			System.out.println(roomAllocated.getStandard() + " Standard Room allocated.");
		}
		
		id = 3;
		Booking booking3 = new Booking(id);
		room_requested = 3;
		booking3.setBooking(user1, room_requested, roomAvailable, waitingList);
		
		List<User> vipWaiting = waitingList.getWaiting("VIP");
		for(User theUser: vipWaiting) {
			System.out.println("Waiting User 1: "+ theUser.getName());
		}
		
		booking3.cancelBooking(user1, id, roomAvailable, waitingList);
		
		for(User theUser: vipWaiting) {
			System.out.println("Waiting User 1: "+ theUser.getName());
		}
		
	}

}
*/