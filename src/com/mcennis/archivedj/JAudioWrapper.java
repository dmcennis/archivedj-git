/**
 * JAudioWrapper
 * Created Oct 30, 2010-10:27:55 AM by Daniel McEnnisOct 30, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;

import java.io.File;
import java.util.Vector;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

/**
 * Wrapper making embedded jAudio a little less painful.
 *
 * @author Daniel McEnnis
 *
 */
public class JAudioWrapper {
	
	Batch batch;
	
	Vector<DoubleMatrix1D> results = new Vector<DoubleMatrix1D>();
	
	/**
	 * 
	 * Constructor for JAudioWrapper.
	 * Files to be transformed into feature vectors.
	 *
	 * @param source Set of files to be analyzed
	 * @throws Exception files can not be read
	 */
	public JAudioWrapper(File[] source) throws Exception{
		batch = new Batch();
		batch.setRecordings(source);
	}
		
	/**
	 * Files to be transformed into feature vectors.  Overrides previous entries.
	 * 
	 * @param files set of files to be analyzed
	 * @throws Exception files can not be read
	 */
	public void setRecordings(File[] files) throws Exception {
		batch.setRecordings(files);
	}

	/**
	 * Momento of the parameters for analysis.
	 * 
	 * @param settings jAudio settings momento
	 */
	public void setSettings(JAudioSettings settings){
		settings.setBatch(batch);
	}
	
	/**
	 * Load settings from the given jAudio XML settings file.
	 * @param settingsFile location of the settings file
	 * @throws Exception error reading the file
	 */
	public void setSettings(File settingsFile) throws Exception{
		JAudioSettings settings = new JAudioSettings();
		settings.loadSettings(settingsFile);
		settings.setBatch(batch);
	}
	
	/**
	 * start the transformation process
	 * 
	 * @throws Exception error occurs during the analysis process
	 */
	public void execute() throws Exception{
		batch.execute();
		batch.getDataModel().collectResults=true;
		double[][][] rawResults = batch.getResults();
		
		// Assume 1D result vector - TODO matrix feature vectors
		int vectorLength = 0;
		for(int i=0;i<rawResults[0].length;++i){
			vectorLength += rawResults[0][i].length;
		}
		
		for(int i=0;i<rawResults.length;++i){
			DenseDoubleMatrix1D vector = new DenseDoubleMatrix1D(vectorLength);
			int index=0;
			for(int j=0;j<rawResults[0].length;++j){
				for(int k=0;k<rawResults[0][j].length;++k){
					vector.set(index, rawResults[i][j][k]);
					index++;
				}
			}
		}
	}
	
	/**
	 * return a jAudio metadata array describing the files scheduled for analysis
	 * @return metadata of files to be analyzed
	 */
	public RecordingInfo[] getFiles(){
		return batch.getDataModel().recordingInfo;
	}
	
	/**
	 * returns a vector describing all audio in feature vector format.
	 * @return set of Colt vectors derived from all files in the archive
	 */
	public Vector<DoubleMatrix1D> getResults(){
		return results;
	}
}
