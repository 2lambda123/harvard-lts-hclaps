//
//  WAVEDataChunkWriter.java
//  bwav
//
//  Created by Robert La Ferla on 2/22/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav;

/**
 * The WAVEDataChunkWriter interface must be implemented in a delegate of your application to write WAVE files.
 *
 * @author Robert La Ferla
 */

public interface WAVEDataChunkWriter {
	/**
	 * Writes the data chunk for the specified WAVEFile.
	 *
	 * @param aWAVEFile The WAVEFile to write the data chunk for.
	 */
	public long writeDataChunkForFile(WAVEFile aWAVEFile);
}
