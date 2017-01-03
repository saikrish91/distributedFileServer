package gash.router.server.messagestates.work;


import gash.router.server.ServerLeaderElectionState;
import gash.router.server.ServerState;
import gash.router.server.WorkHandler;
import gash.router.server.edges.LeaderDetails;
import gash.router.server.edges.singleton.SingletonEgde;
import gash.router.server.utils.ElectionUtil;
import io.netty.channel.ChannelHandlerContext;
import pipe.work.Work.Heartbeat;
import pipe.work.Work.WorkMessage;

public class HasBeatState extends WorkMessageState{
	public HasBeatState(WorkMessage msg, ChannelHandlerContext ctx, WorkHandler workHandler) {
		this.msg = msg;
		this.ctx = ctx;
		this.workHandler = workHandler;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		try{
		if(!ServerState.state.equals("Leader")){
			ServerState.state=ServerLeaderElectionState.FOLLOWER;
			ElectionUtil.isHappening=false;
			Heartbeat hb = msg.getBeat();
			//logger.info("heartbeat from " + msg.getHeader().getNodeId());
			//(hb.getTerm()>=LeaderDetails.getTerm()){
			LeaderDetails.setTerm(hb.getTerm());
			LeaderDetails.setId(hb.getId());
			LeaderDetails.receivedHBFromLeader=true;	
			LeaderDetails.setEdge(SingletonEgde.getInstance().getAllEdgesmap().get(hb.getId()));					
			SingletonEgde.getInstance().updateActiveEdges();
			//logger.info("received HB from leader");
		}	
		else if(ServerState.state.equals("Leader")) {
			if(msg.getBeat().getServerState().equals("Leader")){
				if(msg.getBeat().getTerm()>=LeaderDetails.getTerm()){
					LeaderDetails.setTerm(msg.getBeat().getTerm());
					LeaderDetails.setId(msg.getBeat().getId());
					LeaderDetails.receivedHBFromLeader=true;	
					ServerState.state=ServerLeaderElectionState.FOLLOWER;							
					SingletonEgde.getInstance().updateActiveEdges();
				}
			}
			ElectionUtil.isHappening=false;
			WorkHandler.logger.info("heartbeat from follower");				
		}
		}
		catch(Exception e)
		{
			logger.error("Exception Occurred : " + e.getMessage() );
		}
	}
}
