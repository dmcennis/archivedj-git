/**
 * Playlist
 * Created Oct 28, 2010-10:28:05 PM by Daniel McEnnisOct 28, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj;

import jAudioFeatureExtractor.DataTypes.RecordingInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import cern.colt.matrix.DoubleMatrix1D;

import com.mcennis.archivedj.ordering.Ordering;

/**
 * Class for describing an ordered list of songs for playback in a media player.  Also
 * used internally to archive and other objects for an abstraction of a grouping of songs
 * during analysis.
 *
 * @author Daniel McEnnis
 *
 */
public class Playlist {
	Vector<RecordingInfo> names;
	Vector<DoubleMatrix1D> data;
	
	HashMap<RecordingInfo,DoubleMatrix1D> nameHash = new HashMap<RecordingInfo,DoubleMatrix1D>();
	HashMap<DoubleMatrix1D,RecordingInfo> dataHash = new HashMap<DoubleMatrix1D,RecordingInfo>();
	
	/**
	 * 
	 * Constructor for Playlist.
	 * Set up a playlist with both audio data and file metadata
	 *
	 * @param audioData metadata about the feature vector of the same index
	 * @param songData jAudio derived feature vector
	 */
	public Playlist(Vector<RecordingInfo> audioData, Vector<DoubleMatrix1D> songData){
		names = audioData;
		data = songData;
		for(int i=0;i<names.size();++i){
			nameHash.put(names.get(i),data.get(i));
			dataHash.put(data.get(i), names.get(i));
		}
	}
	
	/**
	 * Create a M3U format playlist for the given list of songs.
	 * 
	 * @param folder file to store the playlist in.
	 * @throws IOException error during writing the file
	 */
	public void outputM3U(File folder) throws IOException{
		FileWriter output = new FileWriter(folder);
		output.write("#EXTM3U\r\n");
		for(int i=0;i<names.size();++i){
			output.write("#EXTINF:"+i+","+names.get(i).file_path.substring(names.get(i).file_path.lastIndexOf(File.separator))+"\r\n");
			output.write(names.get(i).file_path+"\r\n");
		}
		output.close();
	}
	
	/**
	 * Remove the given song from the current list of songs by metadata.
	 * 
	 * @param target song to be removed
	 */
	public void remove(RecordingInfo target){
		names.remove(target);
		DoubleMatrix1D value = nameHash.get(target);
		data.remove(value);
		nameHash.remove(target);
		dataHash.remove(value);
	}
	
	/**
	 * Remove the given song from the current list of songs by feature vector
	 * 
	 * @param target song to be removed
	 */
	public void remove(DoubleMatrix1D target){
		RecordingInfo info = dataHash.get(target);
		data.remove(target);
		names.remove(info);
		nameHash.remove(info);
		dataHash.remove(target);
	}
	
	/**
	 * Return a deep copy of this object.  THis includes copying internal index and maps 
	 * but does NOT include the underlying feature vectors or audio metadata.
	 */
	public Playlist clone(){
		Vector<RecordingInfo> namesCopy = new Vector<RecordingInfo>(names);
		Vector<DoubleMatrix1D> dataCopy = new Vector<DoubleMatrix1D>(data);
		Playlist clone = new Playlist(namesCopy,dataCopy);
		return clone;
	}
	
	/**
	 * Use the provided ordering algorithm to order the songs in this playlist
	 * 
	 * @param ordering algorithm to be applied
	 */
	public void orderPlaylist(Ordering ordering){
		ordering.order(this);
	}

	/**
	 * Get file metadata for the songs in this playlist in order.
	 * 
	 * @return vector of jAudio file metadata
	 */
	public Vector<RecordingInfo> getNames() {
		return names;
	}

	/**
	 * Set Content of the playlist.
	 * 
	 * @param names metadata half of the data.
 	 * @param data audio feature vector half of the data.
	 */
	public void set(Vector<RecordingInfo> names, Vector<DoubleMatrix1D> data) {
		this.names = names;
		this.data=data;
		for(int i=0;i<names.size();++i){
			nameHash.put(names.get(i),data.get(i));
			dataHash.put(data.get(i), names.get(i));
		}
	}

	/**
	 * Return the audio feature vector data of the playlist.
	 * 
	 * @return audio feature vector
	 */
	public Vector<DoubleMatrix1D> getData() {
		return data;
	}
	
}
