/**
 * LogSerendipityDecorator
 * Created Oct 29, 2010-8:13:32 AM by Daniel McEnnisOct 29, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.similarity;

import java.util.Vector;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public class LogSerendipityDecorator implements Similarity {

	Similarity sim;
	
	static {
		SimilarityFactory.register("LogSerendepity-Euclidean", new LogSerendipityDecorator(new EuclideanDistance()));
	}
	
	public LogSerendipityDecorator(Similarity sim){
		this.sim = sim;
	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.similarity.Similarity#similarity(cern.colt.matrix.DoubleMatrix1D, cern.colt.matrix.DoubleMatrix1D, java.util.Vector)
	 */
	@Override
	public double similarity(DoubleMatrix1D left, DoubleMatrix1D right,
			Vector<DoubleMatrix1D> context) {
		double adjustment = Double.NEGATIVE_INFINITY;
		for(int i=0;i<context.size();++i){
			double value = sim.similarity(left, context.get(i), context);
			if(value > adjustment){
				adjustment = value;
			}
		}
		if(adjustment <= 0){
			adjustment = 0.00000001;
		}
		return (1/Math.log(adjustment))*similarity(left, right, context);
	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.similarity.Similarity#similarityMatrix(java.util.Vector)
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

	public LogSerendipityDecorator clone(){
		return new LogSerendipityDecorator(sim.clone());
	}
}
