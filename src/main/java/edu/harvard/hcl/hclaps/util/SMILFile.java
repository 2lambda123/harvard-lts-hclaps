//
//  SMILFile.java
//
//  Created by Robert La Ferla on 8/11/06.
//  Copyright 2006 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

import java.io.*;

/**
 * A simple but not very flexible wrapper for SMIL (Synchronized Multimedia Integrated Language) files.
 *
 * @author Robert La Ferla
 */

public class SMILFile {
	String contents;
	
	public SMILFile() {
		/*contents = "<?xml version=\"1.0\"?>\n<!DOCTYPE smil PUBLIC \"-//W3C//DTD SMIL 1.0//EN\" \"http://www.w3.org/TR/REC-smil/SMIL10.dtd\">\n";
		*/
		contents = "<?xml version=\"1.0\"?>\n<!DOCTYPE smil PUBLIC \"-//W3C//DTD SMIL 1.0//EN\" \"http://hul.harvard.edu/ois/xml/xsd/drs/SMIL10.dtd\">\n";
	}

	public void addLine(String str) {
		contents = contents + str + '\n';
	}

	public void addAudioObject(String path, String title, String begin, String end, String clipBegin, String clipEnd) {
		contents = contents + "<audio src=\"" + path + "\"" + " begin=\"" + begin + "\" end=\"" + end + "\" clip-begin=\"" + clipBegin + "\" clip-end=\"" + clipEnd + "\"";
		if (title != null) {
			contents = contents + "title=\"" + title + "\"";
		}
		contents = contents + "/>\n";
	}
	
	public void saveTo(String path) {
		try {
			File file;
			FileWriter fw; 
			BufferedWriter bw;
			
			file = new File(path);
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(contents, 0, contents.length());
			bw.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void dump() {
		System.out.println(contents);
	}
	
}
