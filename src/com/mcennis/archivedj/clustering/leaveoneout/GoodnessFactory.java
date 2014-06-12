/**
 * ClusteringFactory
 * Created Nov 1, 2010-7:34:23 PM by Daniel McEnnisNov 1, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.clustering.leaveoneout;

import java.util.HashMap;

/**
 * <p>Static singleton-factory class for generating Goodness metrics without explicit references.
 * Add the following code to include a goodness metric automatically:</p>
 * <code>static{
 * 			GoodnessFactory.register("my algorithm name",new MyAlgorithm());
 * }/<code>
 *
 * @author Daniel McEnnis
 *
 */
public class GoodnessFactory {
	static private HashMap<String,GoodnessMetric> map = null;
		
	/**
	 * Registers the goodness metric for retrieval by its key using the prototype method.
	 * It will override the existing key if it is already present.
	 * 
	 * @param name key to be used.
	 * @param metric prototype to create future instances from
	 */
	public static void register(String name,GoodnessMetric metric){
		if(map == null){
			map = new HashMap<String,GoodnessMetric>();
		}
	}
	
	/**
	 * Create the instance from the prototype associated with the given key.
	 * 
	 * @param name key to be used.
	 * @return new instance of the given type or the default if the key is not present.
	 */
	public static GoodnessMetric create(String name){
		if(map == null){
			map = new HashMap<String,GoodnessMetric>();
		}
		if(map.containsKey(name)){ 
			return map.get(name).clone();
		}
		return new AverageGoodnessMetric();
	}
}
