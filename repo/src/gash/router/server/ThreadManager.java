package gash.router.server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gash.router.container.GlobalRoutingConf;
import gash.router.container.RoutingConf;
import gash.router.server.MessageServer.StartCommandCommunication;
import gash.router.server.MessageServer.StartGlobalCommunication;
import gash.router.server.MessageServer.StartWorkCommunication;
import gash.router.server.edges.EdgeMonitor;
import gash.router.server.paralleltasks.HandleFileReadRequestsThread;

public class ThreadManager {
	static List<Thread> threads = new ArrayList<Thread>();
	
	public static void addThreadToList(Thread t){
		threads.add(t);
	}
	public List<Thread> getThreadList(){
		return threads;
	}
	protected static Logger logger = LoggerFactory.getLogger("ThreadManager");
	public static void startEdgeMonitorThread(ServerState state) {
		EdgeMonitor emon = new EdgeMonitor(state);
		logger.info("EdgeMonitor starting... ");
		Thread t = new Thread(emon);
		t.start();
		addThreadToList(t);
	}

	public static void startWorkCommunicationThread(RoutingConf conf) {
		StartWorkCommunication comm = new StartWorkCommunication(conf);
		logger.info("Work starting... ");
		Thread cthread = new Thread(comm);
		cthread.start();
		addThreadToList(cthread);
	}

	public static void startHandleFileReadRequestThread() {
		HandleFileReadRequestsThread r =  new HandleFileReadRequestsThread();
		logger.info("HandleFileReadRequestsThread starting... ");
		Thread readThread = new Thread(r);
		readThread.start();
		addThreadToList(readThread);		
	}

	public static void startWorkStealingThread() {
		WorkStealingThread wst = new WorkStealingThread();
		logger.info("WorkStealingThread starting... ");
		Thread wstThread = new Thread(wst);
		wstThread.start();
		addThreadToList(wstThread);
	}

	public static void startGlobalCommunicationThread(GlobalRoutingConf globalConf) {
		StartGlobalCommunication s = new StartGlobalCommunication(globalConf);
		logger.info("GlobalCommunication starting... ");
		Thread globalCommunicationThread = new Thread(s);
		globalCommunicationThread.start();
		addThreadToList(globalCommunicationThread);
	}

	public static void startCommandCommunicationThread(RoutingConf conf, boolean background) {
		StartCommandCommunication comm2 = new StartCommandCommunication(conf);
		logger.info("Command starting... ");
		ServerState.state=ServerLeaderElectionState.LEADER;
		if (background) {
			Thread cthread2 = new Thread(comm2);
			cthread2.start();
			addThreadToList(cthread2);
		} else
			comm2.run();		
	}	
	
}
