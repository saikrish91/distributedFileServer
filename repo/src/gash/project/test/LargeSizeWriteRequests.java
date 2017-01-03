package gash.project.test;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import gash.project.client.Client;
import gash.project.client.ClientConnectListener;
import gash.project.client.ClientFunc;
import gash.project.client.UploadThread;
import gash.router.client.CommListener;

public class LargeSizeWriteRequests {

	@Test
	public void test() {
		try{
			ExecutorService serv = Executors.newCachedThreadPool();
			
			long startTime = System.currentTimeMillis();
					
				 serv.submit(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						/* file size greater than 50 MB less than 100 MB */ 
						String fname = "test3.mp4";
						String path = "/home/bala/bitbucket/";		
						ClientFunc con = new ClientFunc(Client.leaderHost, Client.leaderPort);// give the cmdport here
						CommListener listener = new ClientConnectListener("My First Client");
						con.addListener(listener);
						Client cl = new Client();
						UploadThread ut = new UploadThread(cl,con, fname, path);
						Thread uThread = new Thread(ut);
						uThread.start();
					}
				});
				 serv.shutdown();
				 serv.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				 long endTime = System.currentTimeMillis();
				 System.out.println("Time taken  : " + (endTime- startTime));
				 assertTrue(true);
		}
		catch(Exception e)
		{
			System.out.println("Exception occurred : "+ e.getMessage());
			fail("Test case Failed");
		}
	}
}
