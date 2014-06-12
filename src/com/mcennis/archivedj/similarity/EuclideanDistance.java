/**
 * EuclideanDistance
 * Created Oct 29, 2010-8:05:49 AM by Daniel McEnnisOct 29, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.similarity;

import java.util.Vector;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;

/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public class EuclideanDistance implements Similarity {

	static {
		SimilarityFactory.register("EuclideanDistance", new EuclideanDistance());
	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.similarity.Similarity#similarity(cern.colt.matrix.DoubleMatrix1D, cern.colt.matrix.DoubleMatrix1D)
	 */
	@Override
	public double similarity(DoubleMatrix1D left, DoubleMatrix1D right, Vector<DoubleMatrix1D> context) {
		return Math.sqrt(left.aggregate(right, Functions.plus, new DoubleDoubleFunction(){
			@Override
			public double apply(double x, double y) {
				return Math.abs(x*y);
			}
		}));
	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.similarity.Similarity#similarityMatrix(java.util.Vector)
	 */
	@Override
	public DoubleMatrix2D similarityMatrix(Vector<DoubleMatrix1D> source) {
		DoubleMatrix2D result = new DenseDoubleMatrix2D(source.size(),source.size());
		for(int i=0;i<source.size();++i){
			for(int j=0;j<source.get(i).size();++j){
				result.set(i, j, this.similarity(source.get(i), source.get(j),source));
			}
		}
		return result;
	}
	
	public EuclideanDistance clone(){
		return new EuclideanDistance();
	}
}
