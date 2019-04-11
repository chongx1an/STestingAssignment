package my.edu.utar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;

import junitparams.Parameters;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class IntegrationTest {
	
	@Test
	@Parameters(method = "paramsForTestIntegrationValid")
	public void testSuccessBookRoom(
			User user, 
			int room_requested,
			Boolean[] availabilityVIP, 
			Boolean[] availabilityDeluxe, 
			Boolean[] availabilityStandard,
			int expectedVipRoomAllocated,
			int expectedDeluxeRoomAllocated,
			int expectedStandardRoomAllocated
			) {
		
		WaitingList waitingList = new WaitingList();
		
		Booking booking = new Booking(1);
		
		Printer printerMock = mock(Printer.class);
		booking.setPrinter(printerMock);
		
		Room roomAvailMock = mock(Room.class);
		when(roomAvailMock.checkRoom("VIP Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityVIP)));
		when(roomAvailMock.checkRoom("Deluxe Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityDeluxe)));
		when(roomAvailMock.checkRoom("Standard Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityStandard)));
		
		booking.setBooking(user, room_requested, roomAvailMock, waitingList);

		int actualVipRoomAllocated = booking.getVip_roomAllocated();
		int actualDeluxeRoomAllocated = booking.getDeluxe_roomAllocated();
		int actualStandardRoomAllocated = booking.getStandard_roomAllocated();

		assertEquals(expectedVipRoomAllocated, actualVipRoomAllocated);
		assertEquals(expectedDeluxeRoomAllocated, actualDeluxeRoomAllocated);
		assertEquals(expectedStandardRoomAllocated, actualStandardRoomAllocated);
		
		verify(printerMock, times(expectedVipRoomAllocated)).printInfo(user.getName(), user.getMember_type(), "VIP Room");
		verify(printerMock, times(expectedDeluxeRoomAllocated)).printInfo(user.getName(), user.getMember_type(), "Deluxe Room");
		verify(printerMock, times(expectedStandardRoomAllocated)).printInfo(user.getName(), user.getMember_type(), "Standard Room");
	}
	
	private Object[] paramsForTestIntegrationValid() {
		return new Object [] {
				new Object[] {
						new User("Goh", "VIP", true),
						3,
						new Boolean[] {true, true, true},
						new Boolean[] {},
						new Boolean[] {},
						3, 0, 0,
						
				},
				new Object[] {
						new User("Goh", "VIP", true),
						2,
						new Boolean[] {true, true},
						new Boolean[] {},
						new Boolean[] {},
						2, 0, 0,
						
				},
		};
	}
	
	@Test
	@Parameters(method = "paramsForTestBookRoomButWait")
	public void testBookRoomButWait(
			User user, 
			int room_requested,
			Boolean[] availabilityVIP, 
			Boolean[] availabilityDeluxe, 
			Boolean[] availabilityStandard
			
			) {
		
		WaitingList waitingList = new WaitingList();
		
		Booking booking = new Booking(1);
		
		Printer printerMock = mock(Printer.class);
		booking.setPrinter(printerMock);
		
		Room roomAvailMock = mock(Room.class);
		when(roomAvailMock.checkRoom("VIP Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityVIP)));
		when(roomAvailMock.checkRoom("Deluxe Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityDeluxe)));
		when(roomAvailMock.checkRoom("Standard Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityStandard)));
		
		booking.setBooking(user, room_requested, roomAvailMock, waitingList);
		
		List<User> actualUserList = waitingList.getWaiting(user.getMember_type());
		
		assertTrue(actualUserList.contains(user));
		
		verifyZeroInteractions(printerMock);
	}
	
	private Object[] paramsForTestBookRoomButWait() {
		return new Object [] {
				new Object[] {
						new User("Goh", "VIP", true),
						3,
						new Boolean[] {true, true, false},
						new Boolean[] {},
						new Boolean[] {},
						
				},
		};
	}
		
	@Test(expected = IllegalArgumentException.class)
	@Parameters(method = "paramsForTestIntegrationInvalidValue")
	public void testBookRoomInvalid(
			String member_type, 
			int room_requested
			) {
		
		User user = new User("Goh", member_type, true); 
		
		WaitingList waitingList = new WaitingList();
		Booking booking = new Booking(1);
		
		Room roomAvailMock = mock(Room.class);
		
		booking.setBooking(user, room_requested, roomAvailMock, waitingList);
		
		
	}
	
	private Object[] paramsForTestIntegrationInvalidValue() {
		
		List<Object> returnedObj = new ArrayList<Object>();


        String line = null;
        
        try {

            FileReader fileReader = new FileReader("paramsForTestIntegrationInvalidValue.txt");


            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                StringTokenizer parameters = new StringTokenizer(line); 
                String member_type = parameters.nextToken(",");
                int number_of_rooms= Integer.parseInt(parameters.nextToken(",")) ;
                returnedObj.add(new Object[] {member_type,number_of_rooms});
            }   

            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        
        Object[] returnedObjArray = new Object[returnedObj.size()];
        returnedObjArray = returnedObj.toArray(returnedObjArray);
		/*
		return new Object [] {
				new Object[] {"VIP", 4},
				new Object[] {"Member", 3},
				new Object[] {"Normal", 2 },
				new Object[] {"VIP", 0},
				new Object[] {"Member", 0},
				new Object[] {"Normal", 0 },
				
		};
		*/
        return  returnedObjArray;
	}
	
	@Test
	public void testCancelBookingRemoveFromBookings() {
		
		int bookingIdToRemove = 1;
		Booking booking = new Booking(bookingIdToRemove);
		booking.setAllocatedStatus(true);
		//booking.setRoomAllocated(new Room(3, 0, 0));
		booking.setVip_roomAllocated(3);
		booking.setDeluxe_roomAllocated(0);
		booking.setStandard_roomAllocated(0);
		
		User user = new User("Goh", "VIP", true);
		
		Room roomAvailMock = mock(Room.class);
		
		WaitingList waitingList = new WaitingList();
		
		user.addBooking(booking);
		booking.cancelBooking(user, bookingIdToRemove, roomAvailMock, waitingList);
		
		List<Booking> actualBookings = user.getBookings();
		
		assertFalse(actualBookings.contains(booking));
		verify(roomAvailMock, times(1)).setVIP(anyInt());
		verify(roomAvailMock, times(1)).setDeluxe(anyInt());
		verify(roomAvailMock, times(1)).setStandard(anyInt());
		
	}
	
	@Test
	public void testCancelBookingRemoveFromWaitingList() {
		
		int bookingIdToRemove = 1;
		Booking booking = new Booking(bookingIdToRemove);
		booking.setAllocatedStatus(false);
		
		User user = new User("Goh", "VIP", true);
		
		Room roomAvailMock = mock(Room.class);
		when(roomAvailMock.checkRoom(anyString())).thenReturn(false);
		
		WaitingList waitingList = new WaitingList();
		booking.setBooking(user, 3, roomAvailMock, waitingList);
		
		booking.cancelBooking(user, bookingIdToRemove, roomAvailMock, waitingList);
		List<User> actualUserList = waitingList.getWaiting(user.getMember_type());
		
		assertFalse(actualUserList.contains(user));
		
	}
}
