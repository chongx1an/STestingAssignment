package my.edu.utar;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.Parameters;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class WaitingListTest {
	
	@Test
	@Parameters(method = "paramsForTestAddWaiting")
	public void testAddWaiting(String name, String member_type) {
		User user = new User(name, member_type, true);
		
		WaitingList waitingList = new WaitingList();
		
		waitingList.addWaiting(user);
		
		List<User> actualUserList = waitingList.getWaiting(member_type);
		
		assertTrue(actualUserList.contains(user));
	}
	
	private Object[] paramsForTestAddWaiting() {
		return new Object [] {
				new Object[] {
						"Goh", "VIP",
				},
				new Object[] {
						"Goh", "Member",
				},
				new Object[] {
						"Goh", "Normal",
				}
		};
	}
	
	
	
	@Test
	@Parameters(method = "paramsForTestRemoveWaiting")
	public void testRemoveWaiting(WaitingList waitingList, String name, String member_type) {
		User user = new User(name, member_type, true);
		
		waitingList.removeWaiting(user);
		
		List<User> actualUserList = waitingList.getWaiting(member_type);
		
		assertFalse(actualUserList.contains(user));
	}
	
	private Object[] paramsForTestRemoveWaiting() {
		return new Object [] {
				
				new Object[] {
						new WaitingList() {{ 
							addWaiting(new User("Goh", "VIP", true)); 
							addWaiting(new User("Chong", "VIP", true)); 
							}},
						"Chong", "VIP",
						
						},
				new Object[] {
						new WaitingList() {{ 
							addWaiting(new User("Goh", "Member", true)); 
							addWaiting(new User("Chong", "Member", true)); 
							}},
						"Chong", "Member",
						
						},
				new Object[] {
						new WaitingList() {{ 
							addWaiting(new User("Goh", "Normal", true)); 
							addWaiting(new User("Chong", "Normal", true)); 
							addWaiting(new User("Xian", "Normal", true));
							}},
						"Chong", "Normal",
						
						},
				};
				
	}
}
	

