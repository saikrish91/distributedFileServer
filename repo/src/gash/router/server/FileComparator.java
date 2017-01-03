package gash.router.server;

import java.util.Comparator;

import routing.Pipe.File;

public class FileComparator implements Comparator<File> {

	@Override
	public int compare(File arg0, File arg1) {
		// TODO Auto-generated method stub
		if(arg0 == null )
			return -1;
		else if(arg1 == null)
			return 1;
		return arg0.getChunkId() - arg1.getChunkId();
	}

}
