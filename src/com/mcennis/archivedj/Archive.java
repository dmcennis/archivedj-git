/**
 * ArchiveInterface
 * Created Oct 30, 2010-2:24:16 PM by Daniel McEnnisOct 30, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj;

import jAudioFeatureExtractor.DataTypes.RecordingInfo;

import java.io.File;
import java.util.Vector;

import com.mcennis.archivedj.ordering.Ordering;
import com.mcennis.archivedj.similarity.Similarity;

/**
 * Archive
 * 
 * <p>Archive represents an entire music collection. It includes all operations that operate
 * over it.</p>  
 * <p>The input to an Archive object is a master directory file.  All music files inside are then added 
 * to the object.  A set of defaults are provided for clustering, similarity metrics, and clustering goodness metrics.
 * If these defaults are not enough, replacements can be inserted.</p>
 *
 * @author Daniel McEnnis
 *
 */
public interface Archive {

	/**
	 *  Sets the similarity metric used to construct the similarity matrici used by the clustering algorithm.
	 *  Each algorithm is a little unusual in that full context (all entries) are provided for exotic similarities.
	 *  
	 * @param sim similarity metric to use
	 */
	public abstract void setSimilarityMetric(Similarity sim);

	/**
	 * Sets the ordering metric to use after clustering has created the set of songs to be included in a playlist.
	 * Each ordering uses an internal similarity metric.  A different similarity metric implies a different ordering metric.
	 * 
	 * @param order Algorithm for determining the order of songs in a playlist.
	 */
	public abstract void setOrderingAlgorithm(Ordering order);

	/**
	 * Return a set of playlists extracted from the given music archive.  This is an empty vector if
	 * no files have been inserted yet.
	 * 
	 * @return vector of playlists extracted from the given set of music.
	 */
	public abstract Vector<Playlist> getPlaylists();

	/**
	 * Perform all analysis over the given set of songs, generating a set of playlists.
	 * 
	 * @throws Exception 
	 * 
	 */
	public abstract void analyze() throws Exception;

	/**
	 * The settings for producing a set of audio feature vectors from a set of files.
	 * 
	 * @param settings set of settings for audio analysis
	 */
	public abstract void setSettings(JAudioSettings settings);

	/**
	 * jAudio metadata on each file that will be processed when the archive is analyzed.
	 * 
	 * @return array of jAudio file metadata.
	 */
	public abstract RecordingInfo[] getFiles();
	
	/**
	 * Replace the original master directory file with the one provided, triggering 
	 * a recursive descent search for music files. Known bug: a cyclic file structure can cause an infinite
	 * loop...
	 * 
	 * @throws Exception if problems are discovered while searching directories.
	 * 
	 */
	public abstract void add(File file) throws Exception;
}