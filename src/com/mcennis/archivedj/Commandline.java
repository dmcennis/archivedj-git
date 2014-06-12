/**
 * Commandline
 * Created Oct 28, 2010-10:16:52 PM by Daniel McEnnisOct 28, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj;

import java.io.File;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcennis.archivedj.ordering.Ordering;
import com.mcennis.archivedj.ordering.OrderingFactory;
import com.mcennis.archivedj.similarity.Similarity;
import com.mcennis.archivedj.similarity.SimilarityFactory;

/**
 * Command line program for extracting playlists to the given location from the given master directory of music.
 * 
 * @author Daniel McEnnis
 * 
 */
public class Commandline {

	/**
	 * Main class for running ArchiveDJ from the terminal
	 * 
	 * @param args mandatory 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < args.length; ++i) {
			if (args[i].contentEquals("-h") || args[i].contentEquals("--help")
					|| args[i].contentEquals("-?")||args.length<2) {
				getHelp();
				System.exit(0);
			}
		}
		Vector<String> pieces = new Vector<String>();
		for (int i = 0; i < args.length; ++i) {
			pieces.add(args[i]);
		}
		JAudioSettings settings = getSettings(pieces);
		Similarity similarity = getSimilarity(pieces);
		Ordering ordering = getOrdering(pieces);
		double threshold = getThreshold(pieces);
		int minSize = getMinSize(pieces);
		if(pieces.size()<2){
			getHelp();
			System.exit(0);
		}
		BaseArchive archive = new BaseArchive(new File(pieces.get(0)));

		if (settings != null) {
			archive.setSettings(settings);
		}

		if (similarity != null) {
			archive.setSimilarityMetric(similarity);
		}

		if (ordering != null) {
			archive.setOrderingAlgorithm(ordering);
		}
		
		if(!Double.isNaN(threshold)){
			archive.setSimilarityThreshold(threshold);
		}
		
		if(minSize >0){
			archive.setMinimumPlaylistSize(minSize);
		}

		archive.analyze();
		Vector<Playlist> result = archive.getPlaylists();
		for (int i = 0; i < result.size(); ++i) {
			result.get(i).outputM3U(
					new File(pieces.get(1) + File.separator + i + ".mcu"));
		}
	}

	public static JAudioSettings getSettings(Vector<String> args)
			throws Exception {
		JAudioSettings settings = null;
		Vector<String> ret = new Vector<String>();
		int i = 0;
		while (i < args.size()) {
			if (args.get(i).contentEquals("--settings")) {
				settings = new JAudioSettings();
				settings.loadSettings(new File(args.get(i + 1)));
				i += 2;
			} else {
				ret.add(args.get(i++));
			}
		}
		args.clear();
		args.addAll(ret);
		return settings;
	}

	public static Similarity getSimilarity(Vector<String> args) {
		Similarity settings = null;
		Vector<String> ret = new Vector<String>();
		int i = 0;
		while (i < args.size()) {
			if (args.get(i).contentEquals("--similarity")) {
				settings = SimilarityFactory.create(args.get(i + 1));
				i += 2;
			} else {
				ret.add(args.get(i++));
			}
		}
		args.clear();
		args.addAll(ret);
		return settings;
	}

	public static Ordering getOrdering(Vector<String> args) {
		Ordering settings = null;
		Vector<String> ret = new Vector<String>();
		int i = 0;
		while (i < args.size()) {
			if (args.get(i).contentEquals("--ordering")) {
				settings = OrderingFactory.create(args.get(i + 1));
				i += 2;
			} else {
				ret.add(args.get(i++));
			}
		}
		args.clear();
		args.addAll(ret);
		return settings;
	}

	public static int getMinSize(Vector<String> args) {
		int settings = Integer.MIN_VALUE;
		Vector<String> ret = new Vector<String>();
		int i = 0;
		while (i < args.size()) {
			if (args.get(i).contentEquals("--minSize")) {
				try {
					settings = Integer.parseInt(args.get(i + 1));
					if (settings < 1) {
						settings = Integer.MIN_VALUE;
						Logger.getLogger(Commandline.class.getName())
								.log(Level.WARNING,
										"minimum playlist size is not a positive number, using defaults.");
					}
					i += 2;
				} catch (NumberFormatException e) {
					Logger.getLogger(Commandline.class.getName())
							.log(Level.WARNING,
									"minimum playlist size is not an integer, using defaults.");
				}
			} else {
				ret.add(args.get(i++));
			}
		}
		args.clear();
		args.addAll(ret);
		return settings;
	}

	public static double getThreshold(Vector<String> args) {
		double settings = Double.NaN;
		Vector<String> ret = new Vector<String>();
		int i = 0;
		while (i < args.size()) {
			if (args.get(i).contentEquals("--minSize")) {
				try {
					settings = Double.parseDouble(args.get(i + 1));
					if (Double.isInfinite(settings)||Double.isNaN(settings)) {
						settings = Double.NaN;
						Logger.getLogger(Commandline.class.getName())
								.log(Level.WARNING,
										"minimum goodness is infinity or NaN, using defaults.");
					}
					i += 2;
				} catch (NumberFormatException e) {
					Logger.getLogger(Commandline.class.getName())
							.log(Level.WARNING,
									"minimum goodness threshold is not a float or double, using defaults.");
				}
			} else {
				ret.add(args.get(i++));
			}
		}
		args.clear();
		args.addAll(ret);
		return settings;
	}
	
	protected static void getHelp(){
		System.out
		.println("Usage: archivedj [options] archive_root_directory destination_directory");
System.out.println("Options:");
System.out
		.println("  '--settings <jAudio settings file>' audio processing instructions file");
System.out
		.println("  '--similarity <factory String ID>' similarity metric to use");
System.out
		.println("  '--ordering <factory string ID>' algorithm for ordering a playlist");
System.out
		.println("  '--minSize <integer>' fewest number of songs permitted in a playlist");
System.out
		.println("  '--threshold <double>' least internal 'goodness' in a playlist (usually 0.0-1.0)");
	}

}
