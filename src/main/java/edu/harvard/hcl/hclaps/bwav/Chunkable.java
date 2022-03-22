//
//  Chunkable.java
//  hclaps
//
//  Created by Robert on 10/16/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav;

import edu.harvard.hcl.hclaps.bwav.chunks.Chunk;

/**
 * The Chunkable interface defines whether an object can add or remove chunks.
 *
 * @author Robert La Ferla
 */

public interface Chunkable {
	/**
	 * Add a Chunk instance.
	 *
	 * @param aChunk Chunk instance.
	 */
	public void addChunk(Chunk aChunk);

	/**
	 * Remove a Chunk instance.
	 *
	 * @param aChunk Chunk instance.
	 */
	public void removeChunk(Chunk aChunk);
}
