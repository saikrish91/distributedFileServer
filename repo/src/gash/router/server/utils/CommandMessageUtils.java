package gash.router.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gash.router.server.CommandHandler;
import gash.router.server.ServerState;
import gash.router.server.messagestates.command.HasDeleteRequestState;
import gash.router.server.messagestates.command.HasPingState;
import gash.router.server.messagestates.command.HasReadRequestState;
import gash.router.server.messagestates.command.HasUpdateRequestState;
import gash.router.server.messagestates.command.HasWriteRequestState;
import io.netty.channel.ChannelHandlerContext;
import pipe.common.Common.Header;
import pipe.work.Work.FileUploadedAck;
import pipe.work.Work.WorkState;
import routing.Pipe.CommandMessage;


public class CommandMessageUtils {
	private static Logger logger = LoggerFactory.getLogger("CommandMessageUtils");
 public static void setMessageState(CommandMessage msg, ChannelHandlerContext ctx, CommandHandler commandHandler)
 {
	 try{
		 if(msg.hasPing()){
			 commandHandler.setMsgState(new HasPingState(msg, ctx, commandHandler));
		 }else if(msg.getRequest().getRequestType().equals(routing.Pipe.RequestType.WRITE)){
			 commandHandler.setMsgState(new HasWriteRequestState(msg, ctx, commandHandler));
		 }else if(msg.getRequest().getRequestType().equals(routing.Pipe.RequestType.READ)){
			 commandHandler.setMsgState(new HasReadRequestState(msg, ctx, commandHandler));
		 }else if(msg.getRequest().getRequestType().equals(routing.Pipe.RequestType.DELETE)){
			 commandHandler.setMsgState(new HasDeleteRequestState(msg, ctx, commandHandler));
		 }else if(msg.getRequest().getRequestType().equals(routing.Pipe.RequestType.UPDATE)){
			 commandHandler.setMsgState(new HasUpdateRequestState(msg, ctx, commandHandler));
		 }
	 }
		catch(Exception e)
		{
			logger.error("Exception Occurred : " + e.getMessage() );
		}
 }
 	public static CommandMessage createFileTransferAck(String filename,ServerState state) {
		// TODO Auto-generated method stub
		try{
		logger.info("Creating file transfer ack for client");
		routing.Pipe.CommandMessage.Builder rb = routing.Pipe.CommandMessage.newBuilder();
		routing.Pipe.GlobalHeader.Builder hb = routing.Pipe.GlobalHeader.newBuilder();
		hb.setClusterId(999);
	 	hb.setTime(System.currentTimeMillis());
	 	routing.Pipe.FileUploadedAck.Builder file=routing.Pipe.FileUploadedAck.newBuilder();
		file.setFileName(filename);
		
		rb.setGlobalHeader(hb.build());
		rb.setFileUploadedAck(file.build());
		return rb.build();
		}
		catch(Exception e)
		{
			logger.error("Exception Occurred : " + e.getMessage() );
			return null;
		}
	}
}
