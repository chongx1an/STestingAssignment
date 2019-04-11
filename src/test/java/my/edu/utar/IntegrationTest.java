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
						new User("Goh", "VIP", false), //vip2
						3,
						new Boolean[] {true, true, true},
						new Boolean[] {},
						new Boolean[] {},
						3, 0, 0
				},
				new Object[] {
						new User("Goh","VIP", false), //vip3
						3,
						new Boolean[] {false},
						new Boolean[] {true, true, true},
						new Boolean[] {},
						0, 3, 0
				},
				new Object[] {
						new User("Goh","VIP", false), //vip4
						3,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true, true, true},
						0, 0, 3
				},
				new Object[] {
						new User("Goh","VIP", false), //vip5
						3,
						new Boolean[] {true, true, false},
						new Boolean[] {true},
						new Boolean[] {},
						2, 1, 0
				},
				new Object[] {
						new User("Goh","VIP", false), //vip6
						3,
						new Boolean[] {true, true, false},
						new Boolean[] {false},
						new Boolean[] {true},
						2, 0, 1
				},
				
				new Object[] {
						new User("Goh","VIP", false), //vip7
						3,
						new Boolean[] {true, false},
						new Boolean[] {true, true},
						new Boolean[] {},
						1, 2, 0
				},
				new Object[] {
						new User("Goh","VIP", false), //vip8
						3,
						new Boolean[] {false},
						new Boolean[] {true, true, false},
						new Boolean[] {true},
						0, 2, 1
				},
				new Object[] {
						new User("Goh","VIP", false), //vip9
						3,
						new Boolean[] {true, false},
						new Boolean[] {false},
						new Boolean[] {true, true},
						1, 0, 2
				},
				new Object[] {
						new User("Goh","VIP", false), //vip10
						3,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {true, true},
						0, 1, 2
				},
				new Object[] {
						new User("Goh","VIP", false), //vip11
						3,
						new Boolean[] {true, false},
						new Boolean[] {true, false},
						new Boolean[] {true},
						1, 1, 1
				},
				new Object[] {
						new User("Goh","VIP", false), //vip31
						1,
						new Boolean[] {true},
						new Boolean[] {},
						new Boolean[] {},
						1, 0, 0
				},
				new Object[] {
						new User("Goh","VIP", false), //vip32
						1,
						new Boolean[] {false},
						new Boolean[] {true},
						new Boolean[] {},
						0, 1, 0
				},
				new Object[] {
						new User("Goh","VIP", false), //vip33
						1,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true},
						0, 0, 1
				},
				
				new Object[] {
						new User("Goh", "Member", true), //member2
						2,
						new Boolean[] {},
						new Boolean[] {true, true},
						new Boolean[] {},
						0, 2, 0
				},
				new Object[] {
						new User("Goh", "Member", true), //member3
						2,
						new Boolean[] {true},
						new Boolean[] {true, false},
						new Boolean[] {},
						1, 1, 0
				},
				new Object[] {
						new User("Goh","Member", true), //member4
						2,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {true},
						0, 1, 1
				},
				new Object[] {
						new User("Goh","Member", true), //member5
						2,
						new Boolean[] {true},
						new Boolean[] {false},
						new Boolean[] {true},
						1, 0, 1
				},
				new Object[] {
						new User("Goh","Member", true), //member6
						2,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true, true},
						0, 0, 2
				},
				new Object[] {
						new User("Goh","Member", false), //member7
						2,
						new Boolean[] {},
						new Boolean[] {true, true},
						new Boolean[] {},
						0, 2, 0
				},
				new Object[] {
						new User("Goh","Member", false), //member8
						2,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {true, true},
						0, 0, 2
				},
				new Object[] {
						new User("Goh","Member", false), //member8
						2,
						new Boolean[] {},
						new Boolean[] {true, false},
						new Boolean[] {true},
						0, 1, 1
				},
				new Object[] {
						new User("Goh","Member", true), //member17
						1,
						new Boolean[] {true},
						new Boolean[] {false},
						new Boolean[] {},
						1, 0, 0
				},
				new Object[] {
						new User("Goh","Member", true), //member18
						1,
						new Boolean[] {},
						new Boolean[] {true},
						new Boolean[] {},
						0, 1, 0
				},
				new Object[] {
						new User("Goh","Member", true), //member19
						1,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true},
						0, 0, 1
				},
				new Object[] {
						new User("Goh","Member", false), //member20
						1,
						new Boolean[] {},
						new Boolean[] {true},
						new Boolean[] {},
						0, 1, 0
				},
				new Object[] {
						new User("Goh","Member", false), //member21
						1,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {true},
						0, 0, 1
				},
				new Object[] {
						new User("Goh","Normal", false), //normal12
						1,
						new Boolean[] {},
						new Boolean[] {},
						new Boolean[] {true},
						0, 0, 1
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
						new User("Goh", "VIP", false), //vip12
						3,
						new Boolean[] {true, true, false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "VIP", false), //vip13
						3,
						new Boolean[] {true, false},
						new Boolean[] {true, false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "VIP", false), //vip14
						3,
						new Boolean[] {true, false},
						new Boolean[] {false},
						new Boolean[] {true, false},
				},
				new Object[] {
						new User("Goh", "VIP", false), //vip15
						3,
						new Boolean[] {true, false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "VIP", false), //vip16
						3,
						new Boolean[] {false},
						new Boolean[] {true, true, false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "VIP", false), //vip17
						3,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {true, false},
				},
				new Object[] {
						new User("Goh", "VIP", false), //vip18
						3,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "VIP", false), //vip19
						3,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true, false},
				},
				new Object[] {
						new User("Goh", "VIP", false), //vip20
						3,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "VIP", false), //vip34
						1,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "Member", true), //member10
						2,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "Member", true), //member11
						2,
						new Boolean[] {true},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "Member", true), //member12
						2,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true, false},
				},
				new Object[] {
						new User("Goh", "Member", true), //member13
						2,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "Member", false), //member14
						2,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "Member", false), //member15
						2,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {true, false},
				},
				new Object[] {
						new User("Goh", "Member", false), //member16
						2,
						new Boolean[] {},
						new Boolean[] {true, false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "Member", true), //member22
						1,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "Member", false), //member23
						1,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {
						new User("Goh", "Normal", false), //normal3
						1,
						new Boolean[] {},
						new Boolean[] {},
						new Boolean[] {false},
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
