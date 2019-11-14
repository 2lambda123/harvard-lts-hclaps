//
//  IFFFile.java
//  hclaps
//
//  Created by Robert La Ferla on 10/13/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav;

import java.util.*;
import edu.harvard.hcl.hclaps.bwav.chunks.*;

/**
 * An IFF file is a file in the Interchange File Format that contains data stored in tagged chunks.  It is a big-endian format.
 *
 * @author Robert La Ferla
 */

public abstract class IFFFile implements Chunkable {
	public ArrayList chunks = new ArrayList();

	/**
	 * Add a Chunk instance.
	 *
	 * @param aChunk Chunk instance.
	 */
	public void addChunk(Chunk aChunk) {
		chunks.add(aChunk);
		aChunk.setParentFile(this);
	}
	
	/**
	 * Remove a Chunk instance.
	 *
	 * @param aChunk Chunk instance.
	 */
	public void removeChunk(Chunk aChunk) {
		aChunk.setParentFile(null);
		chunks.remove(aChunk);
	}
}
