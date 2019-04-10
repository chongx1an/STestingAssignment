package my.edu.utar;

import java.util.Iterator;
import java.util.List;

public class Booking {
	
	private int id;
	private Room roomAllocated;
	private boolean allocatedStatus;
	private Printer printer;
	
	public void setPrinter (Printer printer) {
		this.printer = printer;
	}
	
	public void setRoomAllocated(Room roomAllocated) {
		this.roomAllocated = roomAllocated;
	}
	public Room getRoomAllocated() {
		return roomAllocated;
	}
	public int getID() {
		return id;
	}
	
	public boolean getAllocatedStatus() {
		return allocatedStatus;
	}
	
	public void setAllocatedStatus(boolean allocatedStatus) {
		this.allocatedStatus = allocatedStatus;
	}
	
	public Booking(int id) {
		this.id = id;
		allocatedStatus = false;
		printer = new Printer();
	}
	
	public void setBooking(User user, int room_requested, Room roomAvailable, WaitingList waitingList) {
		int vip_room = 0;
		int deluxe_room = 0;
		int standard_room = 0;
		
		if (room_requested <= 0)
			throw new IllegalArgumentException();
		
		switch(user.getMember_type()) {
			case "VIP":
				if(room_requested > 3)
					throw new IllegalArgumentException();
				break;
			case "Member":
				if(room_requested > 2)
					throw new IllegalArgumentException();
				break;
			case "Normal":
				if(room_requested > 1)
					throw new IllegalArgumentException();
				break;
		}
		
		for(int i = 0; i<room_requested; i++) {
			switch (user.getMember_type()) {
			case "VIP":
				if(roomAvailable.checkRoom("VIP Room")) {
					vip_room++;
					
					allocatedStatus = true;
					
				}else if(roomAvailable.checkRoom("Deluxe Room")){
					deluxe_room++;
					roomAvailable.setDeluxe(roomAvailable.getDeluxe()-1);
					
					allocatedStatus = true;
					
				}else if (roomAvailable.checkRoom("Standard Room")){
					standard_room++;
					roomAvailable.setStandard(roomAvailable.getStandard()-1);
					
					allocatedStatus = true;
					
				}else {
					allocatedStatus = false;
					
				}
				break;
				
			case "Member":
				if(roomAvailable.checkRoom("VIP Room") && user.getExcl_reward()) {
					vip_room++;
					roomAvailable.setVIP(roomAvailable.getVIP()-1);
					user.removeExcl_Reward();
					
					allocatedStatus = true;
					
				}else if(roomAvailable.checkRoom("Deluxe Room")){
					deluxe_room++;
					
					allocatedStatus = true;
					
				}else if (roomAvailable.checkRoom("Standard Room")){
					standard_room++;
					roomAvailable.setStandard(roomAvailable.getStandard()-1);
					
					allocatedStatus = true;
					
				}
				else {
					allocatedStatus = false;

				}
				break;
				
			case "Normal":
				if (roomAvailable.checkRoom("Standard Room")){
					standard_room++;

					
					allocatedStatus = true;
					
				}
				else {
					allocatedStatus = false;

				}
				break;
				
			}
		}
		if(allocatedStatus == false) {
			waitingList.addWaiting(user);
			roomAllocated = new Room(vip_room, deluxe_room, standard_room);
			user.addBooking(this);
		}else {
			
			roomAvailable.setVIP(roomAvailable.getVIP()- vip_room);
			roomAvailable.setDeluxe(roomAvailable.getDeluxe() - deluxe_room);
			roomAvailable.setStandard(roomAvailable.getStandard()- standard_room);
			
			roomAllocated = new Room(vip_room, deluxe_room, standard_room);
			user.addBooking(this);
			
			for(int i = 0; i < vip_room; i++) {
				printer.printInfo(user.getName(), user.getMember_type(), "VIP Room");
			}
				
			for(int i = 0; i < deluxe_room; i++) {
				printer.printInfo(user.getName(), user.getMember_type(), "Deluxe Room");
			}
			
			for(int i = 0; i < standard_room; i++) {
				printer.printInfo(user.getName(), user.getMember_type(), "Standard Room");
			}
		
		}
		
		
	}
	
	public void cancelBooking(User user, int bookingID, Room roomAvailable, WaitingList waitingList) {
		

		Booking bookingToRemove = user.getBooking(bookingID);
		
		if(bookingToRemove != null) {
			if (bookingToRemove.getAllocatedStatus()) {
				user.removeBooking(bookingToRemove);
				Room roomToCancel = bookingToRemove.getRoomAllocated();
				roomAvailable.setVIP(roomAvailable.getVIP()+roomToCancel.getVIP());
				roomAvailable.setDeluxe(roomAvailable.getDeluxe()+roomToCancel.getDeluxe());
				roomAvailable.setStandard(roomAvailable.getStandard()+roomToCancel.getStandard());
	
			}else {
				waitingList.removeWaiting(user);
	
			}
		}
		
	}
}

