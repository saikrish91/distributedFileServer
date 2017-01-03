package gash.router.server.paralleltasks;

import java.util.Map;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gash.router.server.ServerLeaderElectionState;
import gash.router.server.ServerState;
import gash.router.server.edges.EdgeInfo;
import gash.router.server.edges.singleton.SingletonEgde;

public 	class ActiveNodesCheck extends TimerTask{
	private static Logger logger = LoggerFactory.getLogger("ActiveNodesCheck");
	@Override
	public void run() {
		try{
		if(ServerState.state== ServerLeaderElectionState.LEADER){
			for (Map.Entry<Integer, Boolean> entry :SingletonEgde.getInstance().getEdgeStatus().entrySet()) {
				if(entry.getValue()==false){
					logger.info("getting false for"+entry.getKey());
					EdgeInfo ei = SingletonEgde.getInstance().getAllEdgesmap().get(entry.getKey());
					SingletonEgde.getInstance().getAllEdgesmap().get(ei.getRef()).setChannel(null);
					SingletonEgde.getInstance().getAllEdgesmap().get(ei.getRef()).setActive(false);;
					SingletonEgde.getInstance().getActiveEdges().remove(entry.getKey());
			/*	if(EdgeList.activeEdges.size()==0){
						ServerState.state="Follower";
					}*/
				}
				SingletonEgde.getInstance().getEdgeStatus().put(entry.getKey(), false);
			}		
		}
		}
		catch(Exception e)
		{
			logger.error("Exception Occurred : " + e.getMessage() );
		}
	}

}