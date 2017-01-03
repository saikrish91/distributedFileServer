package gash.router.server.paralleltasks;

import gash.router.server.FileRequestContainer;
import gash.router.server.FileRequestObject;
import gash.router.server.dbhandler.MongoDBHandler;

public class HandleFileReadRequestsThread implements Runnable {
	 public HandleFileReadRequestsThread() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				//System.out.println("inside handlefileread "+FileRequestContainer.readRequestQueue.isEmpty());
				if(!FileRequestContainer.readRequestQueue.isEmpty())
				{
					FileRequestObject fro = FileRequestContainer.readRequestQueue.take();		
					System.out.println("readrequestqueue request Id"+fro.getRequestId());
					DBReadRequestThread r  = new DBReadRequestThread(fro.getCtx(),fro.getFileName(), fro.getMessageType(),fro.getRequestId(), new MongoDBHandler(),fro.getDesitinationId());
					Thread readThread = new Thread(r);
					readThread.start();
				}
				
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				break;
			} catch (Exception e) {
				break;
			}
		}
	}
	


	
}
