/**
 * JAudioSettings
 * Created Oct 28, 2010-11:54:55 PM by Daniel McEnnisOct 28, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Momento class for a DataModel object except it allows alteration of the contents.
 *
 * @author Daniel McEnnis
 *
 */
public class JAudioSettings {
	int windowLength;
	int windowOverlap;
	double sampleRate;
	boolean normalise;
	boolean savePerWindow;
	boolean saveOverall;
	int outputType;
	HashMap<String,Boolean> checkedMap_;
	HashMap<String,String[]> attributeMap_;
	String[] aggregatorNames;
	String[][] aggregatorFeatures;
	String[][] aggregatorParameters;
	
	/**
	 * 
	 * Constructor for JAudioSettings.
	 * Re-establishes ArchiveDJ default settings.
	 */
	public JAudioSettings(){
		windowLength=1024;
		windowOverlap=0;
		sampleRate=44100.0;
		normalise=true;
		savePerWindow=false;
		saveOverall = true;
		outputType=0;
		checkedMap_ = new HashMap<String,Boolean>();
		checkedMap_.put("ConstantQ", false);
		checkedMap_.put("Log of ConstantQ", false);
		checkedMap_.put("2D Polynomial Approximation of Log of ConstantQ", true);
		attributeMap_ = new HashMap<String,String[]>();
		attributeMap_.put("ConstantQ",new String[]{"0.25"});
		attributeMap_.put("Log of ConstantQ",new String[]{});
		attributeMap_.put("2D Polynomial Approximation of Log of ConstantQ",new String[]{"22","21","15","15"});
		aggregatorNames = new String[]{"2D Polynomial Approximation of a signal"};
		aggregatorFeatures = new String[][]{new String[]{"2D Polynomial Approximation of Log of ConstantQ"}};
		aggregatorParameters = new String[][]{new String[]{"100","100"}};
	}
	
	/**
	 * Set this settings object to this object's settings.
	 * 
	 * @param batch
	 */
	public void setBatch(Batch batch){
		batch.setSettings(windowLength, windowOverlap, sampleRate, normalise, savePerWindow, saveOverall, outputType);
		batch.setAggregators(aggregatorNames, aggregatorFeatures, aggregatorParameters);
		batch.setFeatures(checkedMap_, attributeMap_);
	}
	
	/**
	 * Load this object from the given file.
	 * 
	 * @param file the XML-based jAudio Settings to load into this settings object
	 * @throws Exception error in parsing or reading the XML file.
	 */
	public void loadSettings(File file) throws Exception{
		Object object = jAudioFeatureExtractor.ACE.XMLParsers.XMLDocumentParser.parseXMLDocument(file.getAbsolutePath(), "save_settings");
		if((object != null)&&(object instanceof Object[])&&(((Object[])object).length == 12)){
			windowLength = ((Integer)((Object[])object)[0]).intValue();
			windowOverlap = ((Integer)((Object[])object)[1]).intValue();
			sampleRate = ((Double)((Object[])object)[2]).doubleValue();
			normalise = (Boolean)((Object[])object)[3];
			savePerWindow = (Boolean)((Object[])object)[4];
			saveOverall = (Boolean)((Object[])object)[5];
			outputType = (Integer)((Object[])object)[6];
			checkedMap_ = (HashMap<String,Boolean>)((Object[])object)[7];
			attributeMap_ = (HashMap<String,String[]>)((Object[])object)[8];
			aggregatorNames = (String[])((Object[])object)[9];
			aggregatorFeatures = (String[][])((Object[]) object)[10];
			aggregatorParameters = (String[][])((Object[]) object)[11];
		}else if(object==null){
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Settings object is null.");
		}else if(!(object instanceof Object[])){			
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Settings object is expected to be java.lang.Object[], but is '"+object.getClass().getCanonicalName()+"'");
		}else{
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Settings has "+((Object[])object).length+" objects, but should have 12");
		}
	}
	
}
