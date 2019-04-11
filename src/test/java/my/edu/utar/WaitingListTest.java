package my.edu.utar;

import static org.junit.Assert.assertEquals;

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
	public void testAddWaiting(String name, String member_type, List<User> expectedUserList) {
		User user = new User(name, member_type, true);
		
		WaitingList waitingList = new WaitingList();
		
		waitingList.addWaiting(user);
		
		List<User> actualUserList = waitingList.getWaiting(member_type);
		
		assertEquals(true, checkEquals(expectedUserList, actualUserList));
	}
	
	private Object[] paramsForTestAddWaiting() {
		return new Object [] {
				new Object[] {
						"Goh", "VIP",
						new ArrayList<User>() {{ 
							add(new User("Goh", "VIP", true));
							}}
						},
				new Object[] {
						"Goh", "Member",
						new ArrayList<User>() {{ 
							add(new User("Goh", "Member", true));
							}}
						},
				new Object[] {
						"Goh", "Normal",
						new ArrayList<User>() {{ 
							add(new User("Goh", "Normal", true));
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
	
	@Test
	@Parameters(method = "paramsForTestRemoveWaiting")
	public void testRemoveWaiting(WaitingList waitingList, String name, String member_type, List<User> expectedUserList) {
		User user = new User(name, member_type, true);
		
		waitingList.removeWaiting(user);
		
		List<User> actualUserList = waitingList.getWaiting(member_type);
		
		assertEquals(true, checkEquals(expectedUserList, actualUserList));
	}
	
	private Object[] paramsForTestRemoveWaiting() {
		return new Object [] {
				new Object[] {
						new WaitingList() {{ 
							addWaiting(new User("Goh", "VIP", true)); 
							addWaiting(new User("Chong", "VIP", true)); 
							}},
						"Chong", "VIP",
						new ArrayList<User>() {{ 
							add(new User("Goh", "VIP", true));
							}}
						},
				new Object[] {
						new WaitingList() {{ 
							addWaiting(new User("Goh", "Member", true)); 
							addWaiting(new User("Chong", "Member", true)); 
							}},
						"Chong", "Member",
						new ArrayList<User>() {{ 
							add(new User("Goh", "Member", true));
							}}
						},
				new Object[] {
						new WaitingList() {{ 
							addWaiting(new User("Goh", "Normal", true)); 
							addWaiting(new User("Chong", "Normal", true)); 
							addWaiting(new User("Xian", "Normal", true));
							}},
						"Chong", "Normal",
						new ArrayList<User>() {{ 
							add(new User("Goh", "Normal", true));
							add(new User("Xian", "Normal", true));
							}}
						},
				};
				
	}
}
	

