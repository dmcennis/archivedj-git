/**
 * Similarity
 * Created Oct 28, 2010-9:06:22 PM by Daniel McEnnisOct 28, 2010
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
public interface Similarity {
	public double similarity(DoubleMatrix1D left,DoubleMatrix1D right,Vector<DoubleMatrix1D> context);
	
	public DoubleMatrix2D similarityMatrix(Vector<DoubleMatrix1D> source);
	
	public Similarity clone();
}
