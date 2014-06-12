/**
 * NGramLeastSimilar
 * Created Oct 29, 2010-8:18:04 AM by Daniel McEnnisOct 29, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.ordering;

import com.mcennis.archivedj.Playlist;
import com.mcennis.archivedj.similarity.CityBlockDistance;
import com.mcennis.archivedj.similarity.SimilarityFactory;



/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public class NGramLeastSimilar implements Ordering {

	// postponed until 0.2 (maybe 0.3)
//	static {
//		OrderingFactory.register("NGramLeastSimilar", new NGramLeastSimilar());
//	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.ordering.Ordering#order(java.util.Vector)
	 */
	@Override
	public void order(Playlist source) {
		// TODO Auto-generated method stub
	}

	public NGramLeastSimilar clone(){
		return new NGramLeastSimilar();
	}
}
