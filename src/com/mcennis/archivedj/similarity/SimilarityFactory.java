/**
 * SimilarityFactory
 * Created Oct 29, 2010-8:04:02 AM by Daniel McEnnisOct 29, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.similarity;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public class SimilarityFactory {

	static private HashMap<String,Similarity> knownTypes = null;
	
	static public void register(String name, Similarity sim){
		if(knownTypes == null){
			knownTypes = new HashMap<String,Similarity>();
		}
		knownTypes.put(name, sim);
	}
	
	static public Similarity create(String arg){
		if(knownTypes == null){
			knownTypes = new HashMap<String,Similarity>();
		}
		if(knownTypes.containsKey(arg)){
			return knownTypes.get(arg).clone();
		}else{
			Logger.getLogger(SimilarityFactory.class.getName()).log(Level.WARNING, "Similarity algorithm '"+arg+"' is not registered with the SimilarityFactory.");
			return new EuclideanDistance();
		}
	}
	
}
