package tech.mcprison.prison.wip.tasks;

import static org.junit.Assert.*;

import org.junit.Test;

import tech.mcprison.prison.tasks.PrisonCommandTaskData;

public class PrisonCommandTaskDataTest 
	extends PrisonCommandTaskData {

	public PrisonCommandTaskDataTest() {
		super( "junit-test", "junit tests" );
	}
	@Test
	public void test() {

		{
			String orig = "{range: 1 1}";
			String result = taskInsertRange( orig );
			
			assertEquals( "1", result);
			System.out.println( "Test 1:  orig: " + orig + "  result: " + result);
		}
		{
			String orig = "{range: 1 3}";
			String result = taskInsertRange( orig );
			
			boolean success = "1".equals(result) ||
								"2".equals(result) ||
								"3".equals(result);
			assertTrue( success );
			System.out.println( "Test 2:  orig: " + orig + "  result: " + result);
		}
		{
			String orig = "{range: 6 7}";
			String result = taskInsertRange( orig );
			
			boolean success = "6".equals(result) ||
					"7".equals(result);
			assertTrue( success );
			System.out.println( "Test 3:  orig: " + orig + "  result: " + result);
		}
		{
			String orig = "{range: 0 2}";
			String result = taskInsertRange( orig );
			
			boolean success = "0".equals(result) ||
					"1".equals(result) |
					"2".equals(result);
			assertTrue( success );
			System.out.println( "Test 4:  orig: " + orig + "  result: " + result);
		}
		{
			String orig = "{range: -1 2}";
			String result = taskInsertRange( orig );
			
			boolean success = "0".equals(result) ||
					"-1".equals(result) |
					"1".equals(result) |
					"2".equals(result);
			assertTrue( success );
			System.out.println( "Test 5:  orig: " + orig + "  result: " + result);
		}
		
		
//		fail("Not yet implemented");
	}

}
