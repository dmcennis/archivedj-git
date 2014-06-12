/**
 * NGramMostSimilar
 * Created Oct 29, 2010-8:17:45 AM by Daniel McEnnisOct 29, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.ordering;

import java.util.Vector;

import com.mcennis.archivedj.Playlist;


import cern.colt.matrix.DoubleMatrix1D;


/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public class NGramMostSimilar implements Ordering {

// postponed until 0.2 (maybe 0.3)
//	static {
//		OrderingFactory.register("NGramMostSimilar", new NGramMostSimilar());
//	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.ordering.Ordering#order(java.util.Vector)
	 */
	@Override
	public void order(Playlist source) {
		// TODO Auto-generated method stub
	}

	public NGramMostSimilar clone(){
		return new NGramMostSimilar();
	}
}
