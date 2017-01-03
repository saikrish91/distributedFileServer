package gash.router.server.edges.singleton;

import java.util.concurrent.ConcurrentHashMap;

import gash.router.server.edges.EdgeInfo;
/*
 * Singleton Class to generate single instance of each edge map
 * 
 * 
 * 
 * 
 */
public class SingletonEgde {
	private volatile ConcurrentHashMap<Integer, EdgeInfo> allEdgesMap = null;

	private volatile ConcurrentHashMap<Integer, Boolean>  edgeStatus = null;
	
	private volatile ConcurrentHashMap<Integer, EdgeInfo> activeEdges =null;
	private static volatile SingletonEgde instance = null;
	private SingletonEgde(){
		allEdgesMap = new ConcurrentHashMap<>();
		edgeStatus = new ConcurrentHashMap<>();
		activeEdges = new ConcurrentHashMap<>();
	}
	/*
	 * Use this method to create instance of Singleton
	 *  Double checked lock to avoid different threads to avoid creating its own instance of SingletonEdge
	 */
	public static SingletonEgde getInstance()
	{
		if(instance == null)
		{
			synchronized (SingletonEgde.class)
			{
				if(instance == null)
				{
					
					instance = new SingletonEgde();
				}
			}
		}
		return instance;	
	}
	
	public ConcurrentHashMap<Integer, EdgeInfo> getAllEdgesmap()
	{
		return allEdgesMap;
	}
	
	public ConcurrentHashMap<Integer, Boolean> getEdgeStatus()
	{
		return edgeStatus;
	}
	
	public ConcurrentHashMap<Integer, EdgeInfo> getActiveEdges()
	{
		return activeEdges;
	}
	
	public void updateActiveEdges()
	{
		activeEdges=new ConcurrentHashMap<>(SingletonEgde.getInstance().getAllEdgesmap());
	}
}
