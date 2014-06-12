/**
 * ArchiveReader
 * Created Oct 28, 2010-10:07:32 PM by Daniel McEnnisOct 28, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj;

import jAudioFeatureExtractor.DataTypes.RecordingInfo;

import java.io.File;
import java.util.Vector;

import cern.colt.matrix.DoubleMatrix1D;

import com.mcennis.archivedj.clustering.LayeredClustering;
import com.mcennis.archivedj.clustering.LeaveOneOutClustering;
import com.mcennis.archivedj.ordering.Ordering;
import com.mcennis.archivedj.ordering.PairwiseLeastDissimilar;
import com.mcennis.archivedj.similarity.KLDistance;
import com.mcennis.archivedj.similarity.NegativeLogSerendepityDecorator;
import com.mcennis.archivedj.similarity.Similarity;

/**
 * BasicArchive
 * 
 * BasicArchive performs the most basic archive analysis in ArchiveDJ.  Only audio data is used - no metadata from files 
 * are included.
 *
 * @author Daniel McEnnis
 *
 */
public class BaseArchive implements Archive {
	
	transient Vector<RecordingInfo> audioData = new Vector<RecordingInfo>();
	
	transient Vector<DoubleMatrix1D> songVector = new Vector<DoubleMatrix1D>();
	
	Vector<Playlist> playlists = new Vector<Playlist>();
	
	JAudioWrapper wrapper;
	
	Similarity similarity = new NegativeLogSerendepityDecorator(new KLDistance());
	
	Ordering ordering= new PairwiseLeastDissimilar();
	
	LeaveOneOutClustering cluster = new LeaveOneOutClustering();
	
	/**
	 * 
	 * Constructor for Archive.
	 *
	 * @param archive master directory of the audio archive.
	 * @throws Exception if there is an error crawling the directory
	 */
	public BaseArchive(File archive) throws Exception{
		
		Vector<File> fileList = new Vector<File>();
		getFileList(archive,fileList);
		
		// construct the audio analysis portion
		JAudioSettings settings = new JAudioSettings();
		JAudioWrapper wrapper = new JAudioWrapper(fileList.toArray(new File[]{}));
		wrapper.setSettings(settings);
	}
	
	/**
	 * synonym for analyze
	 * 
	 * @throws Exception
	 */
	public void createPlaylists() throws Exception {
		analyze();
	}
	
	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.ArchiveInterface#setSimilarityMetric(com.mcennis.playlistgenerator.similarity.Similarity)
	 */
	@Override
	public void setSimilarityMetric(Similarity sim){
		this.similarity = sim;
	}
	
/*	public void orderPlaylists(){
		
	}*/
	
	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.ArchiveInterface#setOrderingAlgorithm(com.mcennis.playlistgenerator.ordering.Ordering)
	 */
	@Override
	public void setOrderingAlgorithm(Ordering order){
		this.ordering = order;
	}
	
	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.ArchiveInterface#getPlaylists()
	 */
	@Override
	public Vector<Playlist> getPlaylists(){
		return playlists;
	}
	
	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.ArchiveInterface#analyze()
	 */
	@Override
	public void analyze() throws Exception{
		wrapper.execute();
		RecordingInfo[] files = wrapper.getFiles();
		for(int i=0;i<files.length;++i){
			audioData.add(files[i]);
		}
		songVector = wrapper.getResults();
		Playlist master = new Playlist(audioData,songVector);
		playlists = cluster.cluster(master);
		for(int i=0;i<playlists.size();++i){
			playlists.get(i).orderPlaylist(ordering);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.ArchiveInterface#setSettings(com.mcennis.playlistgenerator.JAudioSettings)
	 */
	@Override
	public void setSettings(JAudioSettings settings){
		wrapper.setSettings(settings);
	}
	
	/* (non-Javadoc)
	 * @see com.mcennis.playlistgenerator.ArchiveInterface#getFiles()
	 */
	@Override
	public RecordingInfo[] getFiles(){
		return wrapper.getFiles();
	}
	
	/**
	 * Recursive descent analysis of the master directory.
	 * 
	 * @param current
	 * @param list
	 */
	protected void getFileList(File current, Vector<File> list){
		if(current.isDirectory()){
			File[] children = current.listFiles();
			for(int i=0;i<children.length;++i){
				getFileList(children[i],list);
			}
		}else{
			String name = current.getName();
			if(name.toLowerCase().endsWith(".wav") ||
					name.toLowerCase().endsWith(".wave") ||
					name.toLowerCase().endsWith(".aif") ||
					name.toLowerCase().endsWith(".aiff") ||
					name.toLowerCase().endsWith(".aifc") ||
					name.toLowerCase().endsWith(".au") ||
					name.toLowerCase().endsWith(".snd") ||
					name.toLowerCase().endsWith(".mp3") ||
					name.toLowerCase().endsWith(".ogg") )
			{
				list.add(current);
			}
		}
	}
	
	@Override
	public void add(File file) throws Exception{
		Vector<File> additionalFiles = new Vector<File>();
		getFileList(file, additionalFiles);
		wrapper.setRecordings(additionalFiles.toArray(new File[]{}));
	}

	/**
	 * Sets the minimum coherence a playlist must have before it is included in the list of
	 * returned playlists.
	 * 
	 * @param threshold double value that must be exceeded for a playlist to be included in the result set
	 */
	public void setSimilarityThreshold(double threshold) {
		cluster.setSimilarityThreshold(threshold);
	}

	/**
	 * Sets the minimum number of songs for a playlist to be included in the result set.
	 * 
	 * @param minSize
	 */
	public void setMinimumPlaylistSize(int minSize) {
		cluster.setMinimumPlaylistSize(minSize);
	}
	
}
