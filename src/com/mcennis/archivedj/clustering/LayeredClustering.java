/**
 * LayeredClustering
 * Created Oct 30, 2010-1:11:07 PM by Daniel McEnnis
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.clustering;

import java.util.Vector;


import com.mcennis.archivedj.Playlist;
import com.mcennis.archivedj.similarity.Similarity;

/**
 * Interface describing a clustering algorithm.  It must operate over a playlist object
 * containing file metadata and audio feature vectors, creating a list of playlists.
 *
 * @author Daniel McEnnis
 *
 */
public interface LayeredClustering {
	
	/**
	 * Create playlists from the given set of songs.
	 * 
	 * @param dataSet playlist that contains the data about the set of songs.
	 * @return vector of playlists
	 */
	public Vector<Playlist> cluster(Playlist dataSet);
	
	/**
	 * set the similarity algorithm used internally.  This can be a null-op if the algorithm does
	 * not use a similarity measure in the calculation process.
	 * 
	 * @param similarity similarity algorithm object for comparisons
	 */
	public void setSimilairty(Similarity similarity);
	
	/**
	 * Return sthis objects current algorithm.  Can be null if no similarity algorithm is used.
	 * 
	 * @return similarity algorithm for the object
	 */
	public Similarity getSimilarity();
	
	/**
	 * Produce a new copy of this algorithm by the prototype pattern.
	 * @return new instance of this clustering algorithm.
	 */
	public LayeredClustering clone();
}
