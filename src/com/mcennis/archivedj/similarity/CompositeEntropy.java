/**
 * CompositeEntropy
 * Created Nov 1, 2010-9:23:51 PM by Daniel McEnnisNov 1, 2010
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
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;

/**
 * TODO add class description
 * NOT A REENTRANT CLASS
 *
 * @author Daniel McEnnis
 *
 */
public class CompositeEntropy implements Similarity {

	static {
		SimilarityFactory.register("CompositeEntropy", new CompositeEntropy());
	}
	
	DoubleDoubleFunction composite = Functions.mult;
	
	DoubleMatrix1D center = null;
	
	DoubleMatrix1D stddev = null;
	
	
	/* (non-Javadoc)
	 * @see com.mcennis.archivedj.similarity.Similarity#similarity(cern.colt.matrix.DoubleMatrix1D, cern.colt.matrix.DoubleMatrix1D, java.util.Vector)
	 */
	@Override
	public double similarity(DoubleMatrix1D left, DoubleMatrix1D right,
			Vector<DoubleMatrix1D> context) {
		getDistribution(context);
		double leftEntropy = getEntropy(left);
		double rightEntropy = getEntropy(right);
		
		return composite.apply(leftEntropy, rightEntropy);
	}

	/* (non-Javadoc)
	 * @see com.mcennis.archivedj.similarity.Similarity#similarityMatrix(java.util.Vector)
	 */
	@Override
	public DoubleMatrix2D similarityMatrix(Vector<DoubleMatrix1D> source) {
		DoubleMatrix2D ret = new DenseDoubleMatrix2D(source.size(),source.size());
		for(int i=0;i<source.size();++i){
			for(int j=0;j<source.size();++j){
				ret.set(i, j, similarity(source.get(i),source.get(j),source));
			}
		}
		return ret;
	}
	
	protected void getDistribution(Vector<DoubleMatrix1D> left){
		center = new DenseDoubleMatrix1D(left.size());
		stddev = new DenseDoubleMatrix1D(left.size());
	}
	
	
	/**
	 * Gets distribution probability of an index relative to the given value
	 * @param center
	 * @param stdev
	 * @param value
	 * @return
	 */
	protected double norm(int index, double value){
		return Double.NaN;
	}
	
	protected double getEntropy(DoubleMatrix1D left){
		double ret = 0.0;
		for(int i=0;i<left.size();++i){
			ret += Math.log(1.0/(norm(i,left.get(i))));
		}
		return ret;
	}
	
	public CompositeEntropy clone(){
		return new CompositeEntropy();
	}

}
