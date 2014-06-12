/**
 * AverageGoodnessMetric
 * Created Oct 30, 2010-2:55:52 PM by Daniel McEnnisOct 30, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.clustering.leaveoneout;

import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;

/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public class AverageGoodnessMetric implements GoodnessMetric {

	static {
		GoodnessFactory.register("AverageGoodnessMetric", new AverageGoodnessMetric());
	}
	
	/* (non-Javadoc)
	 * @see com.mcennis.archivedj.clustering.leaveoneout.GoodnessMetric#getGoodness(double[])
	 */
	@Override
	public double getGoodness(DoubleMatrix2D data) {
		double ret = data.aggregate(Functions.plus, Functions.abs);
		ret /= data.rows();
		return ret;
	}

	@Override
	public AverageGoodnessMetric clone(){
		return new AverageGoodnessMetric();
	}
}
