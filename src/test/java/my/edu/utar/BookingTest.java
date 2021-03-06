package my.edu.utar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;

import junitparams.Parameters;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class BookingTest {
	
	@Test
	@Parameters(method = "paramsForTestSetSuccessBookingValidValue")
	public void testSetSuccessBookingValidValue(
			String member_type, 
			boolean excl_reward, 
			int room_requested, 
			Boolean[] availabilityVIP, 
			Boolean[] availabilityDeluxe, 
			Boolean[] availabilityStandard,
			Room expectedRoomAllocated
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
		
		Room actualRoomAllocated = booking.getRoomAllocated();
		
		assertEquals(expectedRoomAllocated.getVIP(), actualRoomAllocated.getVIP());
		assertEquals(expectedRoomAllocated.getDeluxe(), actualRoomAllocated.getDeluxe());
		assertEquals(expectedRoomAllocated.getStandard(), actualRoomAllocated.getStandard());
	}
	
	private Object[] paramsForTestSetSuccessBookingValidValue() {
		return new Object [] {
				new Object[] {"VIP", 
						true,
						3,
						new Boolean[] {true, true, true},
						new Boolean[] {},
						new Boolean[] {},
						new Room(3, 0, 0)
				},
				new Object[] {"VIP", 
						true,
						3,
						new Boolean[] {false},
						new Boolean[] {true, true, true},
						new Boolean[] {},
						new Room(0, 3, 0)
				},
				new Object[] {"VIP", 
						true,
						3,
						new Boolean[] {false},
						new Boolean[] {false},
						new Boolean[] {true, true, true},
						new Room(0, 0, 3)
				},
				new Object[] {"VIP", 
						true,
						3,
						new Boolean[] {true, true, false},
						new Boolean[] {true},
						new Boolean[] {},
						new Room(2, 1, 0)
				},

		};
	}
	
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
				new Object[] {"VIP", 
						true,
						3,
						new Boolean[] {true, true, false},
						new Boolean[] {false},
						new Boolean[] {false},
				},
				
		};
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Parameters(method = "paramsForTestSetBookingInvalidValue")
	public void testSetBookingInvalidValue(
			String member_type, 
			boolean excl_reward, 
			int room_requested
		) {
		

		User userMock = mock(User.class);
		when(userMock.getMember_type()).thenReturn(member_type);
		when(userMock.getExcl_reward()).thenReturn(excl_reward, false);
		
		Room roomAvailMock = mock(Room.class);
		
		WaitingList waitingListMock = mock(WaitingList.class);
		
		Booking booking = new Booking(1);
		booking.setBooking(userMock, room_requested, roomAvailMock, waitingListMock);
		
		
	}
	
	private Object[] paramsForTestSetBookingInvalidValue() {
		return new Object [] {
				new Object[] {"VIP", 
						true,
						4
				},
				new Object[] {"Member", 
						true,
						3
				},
				
		};
	}
	
	@Test
	public void testCancelBookingRemoveFromBookings() {
		
		int bookingIdToRemove = 1;
		Booking booking = new Booking(bookingIdToRemove);
		booking.setAllocatedStatus(true);
		booking.setRoomAllocated(new Room(3, 0, 0));
		
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
	
}
