/**
 * Copyright 2016 Gash.
 *
 * This file and intellectual content is protected under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package gash.router.server.edges;

import gash.router.server.edges.singleton.SingletonEgde;

public class EdgeList {

	public EdgeList() {
	}

	public EdgeInfo createIfNew(int ref, String host, int port) {
		if (hasNode(ref))
			return getNode(ref);
		else
			return addNode(ref, host, port);
	}

	public EdgeInfo addNode(int ref, String host, int port) {
		if (!verify(ref, host, port)) {
			// TODO log error
			throw new RuntimeException("Invalid node info");
		}

		if (!hasNode(ref)) {
			EdgeInfo ei = new EdgeInfo(ref, host, port);
			SingletonEgde.getInstance().getAllEdgesmap().put(ref, ei);
			SingletonEgde.getInstance().getActiveEdges().put(ref, ei);
			SingletonEgde.getInstance().getEdgeStatus().put(ref,true);
			return ei;
		} else
			return null;
	}

	private boolean verify(int ref, String host, int port) {
		if (ref < 0 || host == null || port < 1024)
			return false;
		else
			return true;
	}

	public boolean hasNode(int ref) {
		return SingletonEgde.getInstance().getAllEdgesmap().containsKey(ref);

	}

	public EdgeInfo getNode(int ref) {
		return SingletonEgde.getInstance().getAllEdgesmap().get(ref);
	}

	public void removeNode(int ref) {
		SingletonEgde.getInstance().getAllEdgesmap().remove(ref);
	}

	public void clear() {
		SingletonEgde.getInstance().getAllEdgesmap().clear();
	}
}
