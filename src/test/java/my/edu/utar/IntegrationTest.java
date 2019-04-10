package my.edu.utar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
public class IntegrationTest {
	
	@Test
	@Parameters(method = "paramsForTestIntegrationValid")
	public void testSuccessBookRoom(
			User user, 
			int room_requested,
			Boolean[] availabilityVIP, 
			Boolean[] availabilityDeluxe, 
			Boolean[] availabilityStandard,
			Room expectedRoomAllocated
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

		Room actualRoomAllocated = booking.getRoomAllocated();

		assertEquals(expectedRoomAllocated.getVIP(), actualRoomAllocated.getVIP());
		assertEquals(expectedRoomAllocated.getDeluxe(), actualRoomAllocated.getDeluxe());
		assertEquals(expectedRoomAllocated.getStandard(), actualRoomAllocated.getStandard());
		
		verify(printerMock, times(expectedRoomAllocated.getVIP())).printInfo(user.getName(), user.getMember_type(), "VIP Room");
		verify(printerMock, times(expectedRoomAllocated.getDeluxe())).printInfo(user.getName(), user.getMember_type(), "Deluxe Room");
		verify(printerMock, times(expectedRoomAllocated.getStandard())).printInfo(user.getName(), user.getMember_type(), "Standard Room");
	}
	
	private Object[] paramsForTestIntegrationValid() {
		return new Object [] {
				new Object[] {
						new User("Goh", "VIP", true),
						3,
						new Boolean[] {true, true, true},
						new Boolean[] {},
						new Boolean[] {},
						new Room(3, 0, 0),
						
				},
				new Object[] {
						new User("Goh", "VIP", true),
						2,
						new Boolean[] {true, true},
						new Boolean[] {},
						new Boolean[] {},
						new Room(2, 0, 0),
						
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
			Boolean[] availabilityStandard,
			List<User> expectedUserList
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
		
		assertTrue(checkEquals(expectedUserList, actualUserList));
		
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
						new ArrayList<User>() {{ 
							add(new User("Goh", "VIP", true));
							}}
				},
		};
	}
	
	private boolean checkEquals(List<User> expected, List<User> actual) {
		boolean status = false;
		int i = 0;
		if(expected.size() == actual.size()) {
			while(i < expected.size()) {
				User expectedUser = expected.get(i);
				User actualUser = actual.get(i);
				
				if(expectedUser.getName().equals(actualUser.getName())&&
						expectedUser.getMember_type().equals(actualUser.getMember_type())) {
					status = true;
				}else {
					status = false;
				}
				i++;
			}
		}
		
		return status;
	}
	
	@Test(expected = IllegalArgumentException.class)
	@Parameters(method = "paramsForTestIntegrationInvalid")
	public void testBookRoomInvalid(
			User user, 
			int room_requested
			) {
		
		WaitingList waitingList = new WaitingList();
		Booking booking = new Booking(1);
		
		Room roomAvailMock = mock(Room.class);
		
		booking.setBooking(user, room_requested, roomAvailMock, waitingList);
		
		Room actualRoomAllocated = booking.getRoomAllocated();
		
	}
	
	private Object[] paramsForTestIntegrationInvalid() {
		return new Object [] {
				new Object[] {
						new User("Goh", "VIP", true),
						4,
						
				},
		};
	}
}
