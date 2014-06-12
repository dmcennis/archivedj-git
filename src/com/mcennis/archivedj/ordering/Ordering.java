/**
 * Ordering
 * Created Oct 28, 2010-9:10:42 PM by Daniel McEnnisOct 28, 2010
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj.ordering;

import com.mcennis.archivedj.Playlist;


/**
 * TODO add class description
 *
 * @author Daniel McEnnis
 *
 */
public interface Ordering {
	/**
	 * 
	 * @param source
	 * @return
	 */
	public void order(Playlist source);
	
	public Ordering clone();
}
