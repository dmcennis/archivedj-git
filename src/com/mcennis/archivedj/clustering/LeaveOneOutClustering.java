/**
 * LeaveOneOutClustering
 * Created Oct 30, 2010-1:19:06 PM by Daniel McEnnisOct 30, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.clustering;

import java.util.Vector;

import cern.colt.matrix.DoubleMatrix2D;

import com.mcennis.archivedj.Playlist;
import com.mcennis.archivedj.clustering.leaveoneout.AverageGoodnessMetric;
import com.mcennis.archivedj.clustering.leaveoneout.GoodnessMetric;
import com.mcennis.archivedj.similarity.Similarity;
import com.mcennis.archivedj.similarity.SimilarityFactory;

/**
 * Performs the Leave-One-Out Clustering as described in:
 * 
 * McEnnis, D. 2009.  Natural Language Processing Algorithms. University of Waikato Technical Report 2009.
 * 
 * @author Daniel McEnnis
 *
 */
public class LeaveOneOutClustering implements LayeredClustering {

	Similarity similarity = (new SimilarityFactory()).create("Euclidean");
	
	GoodnessMetric metric = new AverageGoodnessMetric();

	private double threshold = 0.4;

	private int minSize = 10;
	
	/**
	 * Returns the level of goodness a playlist must reach to be included.
	 * 
	 * @return threshold goodness for playlist inclusion
	 */
	public double getSimilarityThreshold() {
		return threshold;
	}

	/**
	 * Sets the level of goodness a playlist must reach to be inlcuded.
	 * 
	 * @param threshold goodness for playlist inclusion.
	 */
	public void setSimilarityThreshold(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * Get the minimum number of songs that must be included for a playlist t be included.
	 * @return minimum number of songs per playlist
	 */
	public int getMinimumPlaylistSize() {
		return minSize;
	}

	/**
	 * Sets the minimum number of songs that must be included for a playlist to be included.
	 * @param minSize minimum number of songs per playlist
	 */
	public void setMinimumPlaylistSize(int minSize) {
		this.minSize = minSize;
	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.clustering.LayeredClustering#cluster(com.mcennis.playlistgenerator.Playlist)
	 */
	@Override
	public Vector<Playlist> cluster(Playlist dataSet) {
		DoubleMatrix2D similarityMatrix = similarity.similarityMatrix(dataSet.getData());
		Vector<Playlist> results = new Vector<Playlist>();
		generatePlaylists(dataSet,results);
		return results;
	}

	/**
	 * @param results
	 */
	private double generatePlaylists(Playlist candidate, Vector<Playlist> results) {
		DoubleMatrix2D sim = similarity.similarityMatrix(candidate.getData());
		double ret = metric.getGoodness(sim);
		boolean accept = true;
		sim=null;
		for(int i=0;i<candidate.getData().size();++i){
			Playlist child = candidate.clone();
			child.remove(candidate.getNames().get(i));
			DoubleMatrix2D childSim = similarity.similarityMatrix(child.getData());
			double goodness = metric.getGoodness(childSim);
			if(goodness > ret){
				accept=false;
			}
		}
		if(accept && (ret > threshold) && (candidate.getNames().size()>minSize)){
			results.add(candidate);
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.clustering.LayeredClustering#setSimilairty(com.mcennis.playlistgenerator.similarity.Similarity)
	 */
	@Override
	public void setSimilairty(Similarity similarity) {
		this.similarity = similarity;
	}

	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.clustering.LayeredClustering#getSimilarity()
	 */
	@Override
	public Similarity getSimilarity() {
		return similarity;
	}
	
	@Override
	public LeaveOneOutClustering clone(){
		return new LeaveOneOutClustering();
	}

}
