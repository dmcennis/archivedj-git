/**
 * PairwiseMostDissimilar
 * Created Oct 29, 2010-8:05:28 AM by Daniel McEnnisOct 29, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.ordering;

import jAudioFeatureExtractor.DataTypes.RecordingInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

import com.mcennis.archivedj.Playlist;
import com.mcennis.archivedj.ordering.PairwiseLeastDissimilar.Link;
import com.mcennis.archivedj.similarity.EuclideanDistance;
import com.mcennis.archivedj.similarity.Similarity;


/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public class PairwiseMostDissimilar implements Ordering {

	static {
		OrderingFactory.register("PairwiseMostDissimilar", new PairwiseMostDissimilar());
	}

	Similarity sim = new EuclideanDistance();
	
	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.ordering.Ordering#order(java.util.Vector)
	 */
	@Override
	public void order(Playlist source) {
		Vector<DoubleMatrix1D> data = source.getData();
		Vector<Link> linkList = new Vector<Link>();
		HashSet<Integer> fromList = new HashSet<Integer>();
		HashSet<Integer> toList = new HashSet<Integer>();
		DoubleMatrix2D simMatrix = sim.similarityMatrix(data);
		Vector<RecordingInfo> names = source.getNames();
		Vector<RecordingInfo> newNames = new Vector<RecordingInfo>();
		Vector<DoubleMatrix1D> newData = new Vector<DoubleMatrix1D>();
		Link[] link = new Link[simMatrix.rows()*simMatrix.columns()];
		int index=0;
		for(int i=0;i<simMatrix.rows();++i){
			for(int j=0;j<simMatrix.columns();++j){
				link[index++] = new Link(i,j,simMatrix.get(i, j));
			}
		}
		Arrays.sort(link);
		int i=0;
		while((i<link.length)&&(fromList.size()<data.size())){
			if((!fromList.contains(link[i].x))&&(!toList.contains(link[i].y))){
				linkList.add(link[i]);
				fromList.add(link[i].x);
				toList.add(link[i].y);
			}
			++i;
		}
		for(i=0;i<linkList.size();++i){
			newData.add(data.get(link[i].x));
			newNames.add(names.get(link[i].x));
		}
		source.set(newNames,newData);
	}

	public class Link implements Comparable<Link>{
		public int x;
		public int y;
		public double ret;
		
		public Link(int x, int y, double ret){
			this.x=x;
			this.y=y;
			this.ret = ret;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Link o) {
			if(this.ret > o.ret){
				return -1;
			}else if(this.ret < o.ret){
				return 1;
			}else{
				return 0;
			}
		}
	}

	public PairwiseMostDissimilar clone(){
		return new PairwiseMostDissimilar();
	}
}
