/**
 * GoodnessMetric
 * Created Oct 30, 2010-2:53:58 PM by Daniel McEnnisOct 30, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.clustering.leaveoneout;

import cern.colt.matrix.DoubleMatrix2D;

/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public interface GoodnessMetric {

	/**
	 * 
	 * @param data
	 * @return
	 */
	public double getGoodness(DoubleMatrix2D data);
	
	
	public GoodnessMetric clone();
}
