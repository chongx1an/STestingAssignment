package my.edu.utar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.Parameters;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class UserTest {
	
	@Test
	public void testRemoveExcl_Reward() {
		User user = new User("Goh", "VIP", true);
		
		user.removeExcl_Reward();
		
		boolean actual = user.getExcl_reward();
		
		assertFalse(actual);
	}
	
	@Test
	@Parameters(method = "paramsForTestRemoveBooking")
	public void testRemoveBooking(List<Booking> bookings, Booking booking) {
		User user = new User("Goh", "VIP", true);
		
		user.setBookings(bookings);
		
		user.removeBooking(booking);
		
		List<Booking> actualBookings = user.getBookings();
		
		assertFalse(actualBookings.contains(booking));
		
	}
	
	private Object[] paramsForTestRemoveBooking() {
		return new Object [] {
				new Object[] {
					new ArrayList<Booking>() {{ add(new Booking(1)); add(new Booking(2)); }},
					new Booking(1),
		
				}
		};
	}
	
}
