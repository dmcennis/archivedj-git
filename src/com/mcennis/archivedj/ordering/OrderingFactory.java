/**
 * OrderingFactory
 * Created Oct 29, 2010-8:04:20 AM by Daniel McEnnisOct 29, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.ordering;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcennis.archivedj.similarity.EuclideanDistance;
import com.mcennis.archivedj.similarity.Similarity;
import com.mcennis.archivedj.similarity.SimilarityFactory;

/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public class OrderingFactory {

	static private HashMap<String,Ordering> knownTypes = null;
		
	static public void register(String name, Ordering sim){
		if(knownTypes == null){
			knownTypes = new HashMap<String,Ordering>();
		}
		knownTypes.put(name, sim);
	}
	
	static public Ordering create(String arg){
		if(knownTypes == null){
			knownTypes = new HashMap<String,Ordering>();
		}
		if(knownTypes.containsKey(arg)){
			return knownTypes.get(arg).clone();
		}else{
			Logger.getLogger(OrderingFactory.class.getName()).log(Level.WARNING, "Ordering algorithm '"+arg+"' is not registered with the OrderingFactory.");
			return new PairwiseLeastDissimilar();
		}
	}
}
