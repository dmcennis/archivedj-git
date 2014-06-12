/**
 * HyperbolicDistance
 * Created Oct 29, 2010-8:16:59 AM by Daniel McEnnisOct 29, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.similarity;

import java.util.Vector;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public class HyperbolicDistance implements Similarity {

	/*
	 * Removed until I find my notes on hyperbolic spaces
	 */
//	static {
//		SimilarityFactory.register("HyperbolicDistance", new HyperbolicDistance());
//	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.similarity.Similarity#similarity(cern.colt.matrix.DoubleMatrix1D, cern.colt.matrix.DoubleMatrix1D, java.util.Vector)
	 */
	@Override
	public double similarity(DoubleMatrix1D left, DoubleMatrix1D right,
			Vector<DoubleMatrix1D> context) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.similarity.Similarity#similarityMatrix(java.util.Vector)
	 */
	@Override
	public DoubleMatrix2D similarityMatrix(Vector<DoubleMatrix1D> source) {
		// TODO Auto-generated method stub
		return null;
	}

	public HyperbolicDistance clone(){
		return new HyperbolicDistance();
	}
}
