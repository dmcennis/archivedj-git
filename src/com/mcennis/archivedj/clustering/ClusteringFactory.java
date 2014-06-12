/**
 * ClusteringFactory
 * Created Nov 1, 2010-7:34:23 PM by Daniel McEnnisNov 1, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.clustering;

import java.util.HashMap;

/**
 * Static object implementing a variant of the Singleton-Factory pattern for the 
 * Clustering interface.
 *
 * @author Daniel McEnnis
 *
 */
public class ClusteringFactory {

	static private HashMap<String,LayeredClustering> map = null;
	
	/**
	 * <p>Registers a new Clustering algorithm with the system along with its key.
	 * Adding a known key replaces the old key.  This should be called in a 
	 * static block in the algorithm:</p>
	 * <code>static {
	 * 			ClusteringFactory.register("my algorithm name",new MyAlgorithm());
	 * 	}</code>
	 * 
	 * @param name string key to choose this algorithm implementation
	 * @param cluster prototype to clone future versions from.
	 */
	static public void register(String name, LayeredClustering cluster){
		if(map == null){
			map = new HashMap<String,LayeredClustering>();
		}
		map.put(name, cluster);
	}
	
	/**
	 * Create an clustering algorithm object.
	 * 
	 * @param name ket associated with the algorithm.
	 * @return newly constructed algorithm 
	 */
	static public LayeredClustering create(String name){
		if(map == null){
			map = new HashMap<String,LayeredClustering>();
		}
		if(map.containsKey(name)){
			return map.get(name).clone();
		}
		return new LeaveOneOutClustering();
	}
}
