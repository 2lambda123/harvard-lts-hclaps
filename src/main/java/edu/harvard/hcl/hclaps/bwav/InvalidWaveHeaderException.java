//
//  InvalidWaveHeaderException
//  BWAVParser
//
//  Created by Robert La Ferla on 1/24/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.bwav;

/**
 * @author Robert La Ferla
 */

public class InvalidWaveHeaderException extends Exception {
    public InvalidWaveHeaderException(String str) {
        super(str);
    }
}
