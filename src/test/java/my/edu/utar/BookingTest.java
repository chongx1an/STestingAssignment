package my.edu.utar;

import static org.junit.Assert.assertEquals;
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
public class BookingTest {
	
	/*
	 * Test the condition that
	 * user books a valid number of rooms and
	 * the bookings is succeed.
	 * 
	 */
	@Test
	@Parameters(method = "paramsForTestSetSuccessBookingValidValue")
	public void testSetSuccessBookingValidValue(
			String member_type, 
			boolean excl_reward, 
			int room_requested, 
			Boolean[] availabilityVIP, 
			Boolean[] availabilityDeluxe, 
			Boolean[] availabilityStandard,
			int expectedVipRoomAllocated,
			int expectedDeluxeRoomAllocated,
			int expectedStandardRoomAllocated
		) {
		
		User userMock = mock(User.class);
		when(userMock.getMember_type()).thenReturn(member_type);
		when(userMock.getExcl_reward()).thenReturn(excl_reward, false);
		
		Room roomAvailMock = mock(Room.class);
		when(roomAvailMock.checkRoom("VIP Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityVIP)));
		when(roomAvailMock.checkRoom("Deluxe Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityDeluxe)));
		when(roomAvailMock.checkRoom("Standard Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityStandard)));
		
		WaitingList waitingListMock = mock(WaitingList.class);
		
		Booking booking = new Booking(1);
		booking.setBooking(userMock, room_requested, roomAvailMock, waitingListMock);
		
		int actualVipRoomAllocated = booking.getVip_roomAllocated();
		int actualDeluxeRoomAllocated = booking.getDeluxe_roomAllocated();
		int actualStandardRoomAllocated = booking.getStandard_roomAllocated();
		
		/*
		 * Check the 
		 * - amount of VIP room allocated to user
		 * - amount of Deluxe room allocated to user
		 * - amount of Standard room allocated to user
		 * 
		 */
		assertEquals(expectedVipRoomAllocated, actualVipRoomAllocated);
		assertEquals(expectedDeluxeRoomAllocated, actualDeluxeRoomAllocated);
		assertEquals(expectedStandardRoomAllocated, actualStandardRoomAllocated);
	}
	
	/*
	 * Boundary Value Analysis used
	 */
	private Object[] paramsForTestSetSuccessBookingValidValue() {
		return new Object [] {
				new Object[] {"VIP", //vip2
						false,
						3,
						new Boolean[] {true, true, true},
						new Boolean[] {},
						new Boolean[] {},
						3, 0, 0
				},
				new Object[] {"VIP", //vip3
						false,
						3,
						new Boolean[] {false},
						new Boolean[] {true, true, true},
						new Boolean[] {},
						0, 3, 0
				},
				new Object[] {"VIP", //vip4
						false,
						3,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true, true, true},
						0, 0, 3
				},
				new Object[] {"VIP", //vip5
						false,
						3,
						new Boolean[] {true, true, false},
						new Boolean[] {true},
						new Boolean[] {},
						2, 1, 0
				},
				new Object[] {"VIP", //vip6
						false,
						3,
						new Boolean[] {true, true, false},
						new Boolean[] {false},
						new Boolean[] {true},
						2, 0, 1
				},
				
				new Object[] {"VIP", //vip7
						false,
						3,
						new Boolean[] {true, false},
						new Boolean[] {true, true},
						new Boolean[] {},
						1, 2, 0
				},
				new Object[] {"VIP", //vip8
						false,
						3,
						new Boolean[] {false},
						new Boolean[] {true, true, false},
						new Boolean[] {true},
						0, 2, 1
				},
				new Object[] {"VIP", //vip9
						false,
						3,
						new Boolean[] {true, false},
						new Boolean[] {false},
						new Boolean[] {true, true},
						1, 0, 2
				},
				new Object[] {"VIP", //vip10
						false,
						3,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {true, true},
						0, 1, 2
				},
				new Object[] {"VIP", //vip11
						false,
						3,
						new Boolean[] {true, false},
						new Boolean[] {true, false},
						new Boolean[] {true},
						1, 1, 1
				},
				new Object[] {"VIP", //vip31
						false,
						1,
						new Boolean[] {true},
						new Boolean[] {},
						new Boolean[] {},
						1, 0, 0
				},
				new Object[] {"VIP", //vip32
						false,
						1,
						new Boolean[] {false},
						new Boolean[] {true},
						new Boolean[] {},
						0, 1, 0
				},
				new Object[] {"VIP", //vip33
						false,
						1,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true},
						0, 0, 1
				},
				
				new Object[] {"Member", //member2
						true,
						2,
						new Boolean[] {},
						new Boolean[] {true, true},
						new Boolean[] {},
						0, 2, 0
				},
				new Object[] {"Member", //member3
						true,
						2,
						new Boolean[] {true},
						new Boolean[] {true, false},
						new Boolean[] {},
						1, 1, 0
				},
				new Object[] {"Member", //member4
						true,
						2,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {true},
						0, 1, 1
				},
				new Object[] {"Member", //member5
						true,
						2,
						new Boolean[] {true},
						new Boolean[] {false},
						new Boolean[] {true},
						1, 0, 1
				},
				new Object[] {"Member", //member6
						true,
						2,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true, true},
						0, 0, 2
				},
				new Object[] {"Member", //member7
						false,
						2,
						new Boolean[] {},
						new Boolean[] {true, true},
						new Boolean[] {},
						0, 2, 0
				},
				new Object[] {"Member", //member8
						false,
						2,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {true, true},
						0, 0, 2
				},
				new Object[] {"Member", //member9
						false,
						2,
						new Boolean[] {},
						new Boolean[] {true, false},
						new Boolean[] {true},
						0, 1, 1
				},
				new Object[] {"Member", //member17
						true,
						1,
						new Boolean[] {true},
						new Boolean[] {false},
						new Boolean[] {},
						1, 0, 0
				},
				new Object[] {"Member", //member18
						true,
						1,
						new Boolean[] {},
						new Boolean[] {true},
						new Boolean[] {},
						0, 1, 0
				},
				new Object[] {"Member", //member19
						true,
						1,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true},
						0, 0, 1
				},
				new Object[] {"Member", //member20
						false,
						1,
						new Boolean[] {},
						new Boolean[] {true},
						new Boolean[] {},
						0, 1, 0
				},
				new Object[] {"Member", //member21
						false,
						1,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {true},
						0, 0, 1
				},
				new Object[] {"Normal", //normal2
						false,
						1,
						new Boolean[] {},
						new Boolean[] {},
						new Boolean[] {true},
						0, 0, 1
				},

		};
	}
	
	/*
	 * Test the condition that
	 * user book a valid number of rooms but
	 * the room is not enough to allocate and
	 * user is add into respectively waiting list based on user's member.
	 * 
	 */
	@Test
	@Parameters(method = "paramsForTestSetWaitingBookingValidValue")
	public void testSetWaitingBookingValidValue(
			String member_type, 
			boolean excl_reward, 
			int room_requested, 
			Boolean[] availabilityVIP, 
			Boolean[] availabilityDeluxe, 
			Boolean[] availabilityStandard
		) {
		
		User userMock = mock(User.class);
		when(userMock.getMember_type()).thenReturn(member_type);
		when(userMock.getExcl_reward()).thenReturn(excl_reward, false);
		
		Room roomAvailMock = mock(Room.class);
		when(roomAvailMock.checkRoom("VIP Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityVIP)));
		when(roomAvailMock.checkRoom("Deluxe Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityDeluxe)));
		when(roomAvailMock.checkRoom("Standard Room")).thenAnswer(AdditionalAnswers.returnsElementsOf(Arrays.asList(availabilityStandard)));
		

		WaitingList waitingListMock = mock(WaitingList.class);
		
		Booking booking = new Booking(1);
		booking.setBooking(userMock, room_requested, roomAvailMock, waitingListMock);

		verify(waitingListMock, times(1)).addWaiting(userMock);
	}
	
	private Object[] paramsForTestSetWaitingBookingValidValue() {
		return new Object [] {
				new Object[] {"VIP", //vip12
						false,
						3,
						new Boolean[] {true, true, false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {"VIP", //vip13
						false,
						3,
						new Boolean[] {true, false},
						new Boolean[] {true, false},
						new Boolean[] {false},
				},
				new Object[] {"VIP", //vip14
						false,
						3,
						new Boolean[] {true, false},
						new Boolean[] {false},
						new Boolean[] {true, false},
				},
				new Object[] {"VIP", //vip15
						false,
						3,
						new Boolean[] {true, false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {"VIP", //vip16
						false,
						3,
						new Boolean[] {false},
						new Boolean[] {true, true, false},
						new Boolean[] {false},
				},
				new Object[] {"VIP", //vip17
						false,
						3,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {true, false},
				},
				new Object[] {"VIP", //vip18
						false,
						3,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {false},
				},
				new Object[] {"VIP", //vip19
						false,
						3,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true, false},
				},
				new Object[] {"VIP", //vip20
						false,
						3,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {"VIP", //vip34
						false,
						1,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {"Member", //member10						
						true,
						2,
						new Boolean[] {false},
						new Boolean[] {true, false},
						new Boolean[] {false},
				},
				new Object[] {"Member", //member11					
						true,
						2,
						new Boolean[] {true},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {"Member", //member12						
						true,
						2,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true, false},
				},
				new Object[] {"Member", //member13						
						true,
						2,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {"Member", //member14						
						false,
						2,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {"Member", //member15						
						false,
						2,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {true, false},
				},
				new Object[] {"Member", //member16						
						false,
						2,
						new Boolean[] {},
						new Boolean[] {true, false},
						new Boolean[] {false},
				},
				new Object[] {"Member", //member22					
						true,
						1,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {"Member", //member23					
						false,
						1,
						new Boolean[] {},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				new Object[] {"Normal", //normal3					
						false,
						1,
						new Boolean[] {},
						new Boolean[] {},
						new Boolean[] {false},
				},
				
		};
	}
	
	/*
	 * Test the condition that
	 * user books a invalid number of rooms and
	 * the bookings session is aborted.
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	@Parameters(method = "paramsForTestSetBookingInvalidValue")
	public void testSetBookingInvalidValue(
			String member_type, 
			int room_requested
		) {
		

		User userMock = mock(User.class);
		when(userMock.getMember_type()).thenReturn(member_type);
		
		Room roomAvailMock = mock(Room.class);
		
		WaitingList waitingListMock = mock(WaitingList.class);
		
		Booking booking = new Booking(1);
		booking.setBooking(userMock, room_requested, roomAvailMock, waitingListMock);
		
		
	}
	
	private Object[] paramsForTestSetBookingInvalidValue() {
			
		List<Object> returnedObj = new ArrayList<Object>();


        String line = null;
        
        try {

            FileReader fileReader = new FileReader("paramsForTestSetBookingInvalidValue.txt");


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
	
	/*
	 * Test the condition that
	 * user cancels a successfully booking.
	 * (Remove the specific booking from user booking list)
	 * 
	 */
	@Test
	public void testCancelBookingRemoveFromBookings() {
		
		int bookingIdToRemove = 1;
		Booking booking = new Booking(bookingIdToRemove);
		
		booking.setAllocatedStatus(true);
		booking.setVip_roomAllocated(3);
		booking.setDeluxe_roomAllocated(0);
		booking.setStandard_roomAllocated(0);
		
		User userMock = mock(User.class);
		when(userMock.getBooking(anyInt())).thenReturn(booking);
		
		Room roomAvailMock = mock(Room.class);
		
		WaitingList waitingListMock = mock(WaitingList.class);
		
		
		booking.cancelBooking(userMock, bookingIdToRemove, roomAvailMock, waitingListMock);
		
		verify(userMock, times(1)).removeBooking(booking);
		verify(roomAvailMock, times(1)).setVIP(anyInt());
		verify(roomAvailMock, times(1)).setDeluxe(anyInt());
		verify(roomAvailMock, times(1)).setStandard(anyInt());
		
	}
	
	/*
	 * Test the condition that
	 * user cancels a unsuccessfully booking that
	 * put user inside the waiting list.
	 * (Remove user from waiting list)
	 * 
	 */
	@Test
	public void testCancelBookingRemoveFromWaitingList() {
		
		int bookingIdToRemove = 1;
		Booking booking = new Booking(bookingIdToRemove);
		booking.setAllocatedStatus(false);
		
		User userMock = mock(User.class);
		when(userMock.getBooking(anyInt())).thenReturn(null);
		
		Room roomAvailMock = mock(Room.class);
		
		WaitingList waitingListMock = mock(WaitingList.class);
		
		
		booking.cancelBooking(userMock, bookingIdToRemove, roomAvailMock, waitingListMock);
		
		verify(waitingListMock, times(1)).removeWaiting(userMock);
		
	}
	
}
