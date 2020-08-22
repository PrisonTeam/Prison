package tech.mcprison.prison.mines.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

public class MineSchedulerTest
		extends MineScheduler
{

	/**
	 * <p>In this first jUnit test we need to setup a workflow with three entries.
	 * There should be two messages and one reset.  The total time for all workflow
	 * items must match the resetTime.  The first item that is popped off the stack
	 * should NOT be the reset job, since that should be last.
	 * </p>
	 */
	@Test
	public void testInitializeJobWorkflow01()
	{
		int resetTime = 301;
		boolean includeMessages = true;
		ArrayList<Integer> rwTimes = new ArrayList<>();
		rwTimes.add( 45 );
		rwTimes.add( 120 );
		
		int totalTime = 0;
		
		List<MineJob> jWorkflow = initializeJobWorkflow( resetTime, includeMessages, rwTimes );
		
		assertNotNull( jWorkflow );
		assertEquals( 3, jWorkflow.size() );
		
		Stack<MineJob> jobStack = new Stack<>();
		jobStack.addAll( jWorkflow );
		
		assertEquals( 3, jobStack.size() );
		
		{
			MineJob job1 = jobStack.pop();
			
			assertEquals( 2, jobStack.size() );
			
			// Tests that the first job that is popped off the stack is a message and not the reset:
			assertTrue( job1.getAction() == MineJobAction.MESSAGE );
			assertEquals( 181.0d, job1.getDelayActionSec(), 0.1d );
			assertEquals( 120.0d, job1.getResetInSec(), 0.1d );
			totalTime += job1.getDelayActionSec();
		}
		
		{
			MineJob job2 = jobStack.pop();
			
			assertEquals( 1, jobStack.size() );
			
			assertTrue( job2.getAction() == MineJobAction.MESSAGE );
			assertEquals( 75.0d, job2.getDelayActionSec(), 0.1d );
			assertEquals( 45.0d, job2.getResetInSec(), 0.1d );
			totalTime += job2.getDelayActionSec();
		}
		
		{
			MineJob job3 = jobStack.pop();
			
			assertEquals( 0, jobStack.size() );
			
			assertTrue( job3.getAction() == MineJobAction.RESET_SYNC );
			assertEquals( 45.0d, job3.getDelayActionSec(), 0.1d );
			assertEquals( 0.0d, job3.getResetInSec(), 0.1d );
			totalTime += job3.getDelayActionSec();
		}
		
		
		assertEquals( resetTime, totalTime );

	}
	
	
	/**
	 * <p>This unit test should be similar as the first, but with no message generation
	 * so there should only be one item in the workflow and that should be the RESET
	 * for the total time of 301 seconds.
	 * </p>
	 * 
	 */
	@Test
	public void testInitializeJobWorkflow02()
	{
		int resetTime = 301;
		boolean includeMessages = false;
		ArrayList<Integer> rwTimes = new ArrayList<>();
		rwTimes.add( 45 );
		rwTimes.add( 120 );
		
		List<MineJob> jWorkflow = initializeJobWorkflow( resetTime, includeMessages, rwTimes );
		
		assertEquals( 1L, jWorkflow.size() );
		
		Stack<MineJob> jobStack = new Stack<>();
		jobStack.addAll( jWorkflow );
		
		assertEquals( 1L, jobStack.size() );
		
		MineJob job = jobStack.pop();
		
		assertEquals( 0L, jobStack.size() );
		
		// Tests that the first job that is popped off the stack is a message and not the reset:
		assertTrue( job.getAction() == MineJobAction.RESET_SYNC );
		assertEquals( resetTime, job.getDelayActionSec(), 0.1d );
		
	}

}
