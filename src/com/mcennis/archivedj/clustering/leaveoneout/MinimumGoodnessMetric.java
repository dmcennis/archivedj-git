/**
 * MinimumGoodnessMetric
 * Created Nov 2, 2010-3:47:06 PM by Daniel McEnnisNov 2, 2010
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
public class MinimumGoodnessMetric implements GoodnessMetric {

	static{
		GoodnessFactory.register("MinimumGoodnessMetric", new MinimumGoodnessMetric());
	}
	/* (non-Javadoc)
	 * @see com.mcennis.archivedj.clustering.leaveoneout.GoodnessMetric#getGoodness(cern.colt.matrix.DoubleMatrix2D)
	 */
	@Override
	public double getGoodness(DoubleMatrix2D data) {
		double ret = Double.POSITIVE_INFINITY;
		for(int i=0;i<data.rows();++i){
			for(int j=0;j<data.columns();++j){
				if(data.get(i,j)<ret){
					ret = data.get(i,j);
				}
			}
		}
		return ret;
	}
	
	public MinimumGoodnessMetric clone(){
		return new MinimumGoodnessMetric();
	}

}
