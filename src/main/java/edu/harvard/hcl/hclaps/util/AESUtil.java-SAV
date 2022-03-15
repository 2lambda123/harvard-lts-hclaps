//
//  AESUtil.java
//  hclaps
//
//  Created by Robert La Ferla on 3/5/07.
//  Copyright 2007 Harvard University. All rights reserved.
//

package edu.harvard.hcl.hclaps.util;

import java.io.*;
import java.util.*;
import java.nio.charset.Charset;
import java.math.BigDecimal;
import edu.harvard.hcl.hclaps.bwav.*;
import org.exolab.castor.xml.*;
import org.exolab.castor.util.LocalConfiguration;
import org.aes.tcf.*;
import org.aes.processhistory.*;
import org.aes.processhistory.types.*;
import com.therockquarry.aes31.adl.*;
import java.net.URI;
import org.aes.audioObject.AudioObjectType;
import org.aes.audioObject.FaceRegionType;
import org.aes.audioObject.FaceType;
import org.aes.audioObject.ChannelAssignmentType;

/**
 * @author Robert La Ferla
 */

public class AESUtil implements AttributeListener {

	HashMap oldIDtoNewIDMap = new HashMap();
	boolean finishedParsingIDMap = false;

	public static OperatorType getOperatorType() {
		OperatorType ot = null;
		String hpPath;
		
		hpPath = System.getProperty("user.home") + "/hclaps.prop";
		try {
			FileInputStream fis;
			Properties prop;
			
			fis = new FileInputStream(hpPath);
			prop = new Properties();
			prop.load(fis);
			ot = new OperatorType();
			ot.setFirstName(prop.getProperty("operator.firstname"));
			ot.setLastName(prop.getProperty("operator.lastname"));
			ot.setOrganization(prop.getProperty("operator.organization"));
			ot.setStreet1(prop.getProperty("operator.street1"));
			ot.setStreet2(prop.getProperty("operator.street2"));
			ot.setCity(prop.getProperty("operator.city"));
			ot.setRegion(prop.getProperty("operator.region"));
			ot.setCountryCode(prop.getProperty("operator.countrycode"));
			ot.setPostalCode(prop.getProperty("operator.postalcode"));
		}
		catch (FileNotFoundException ex) {
			System.err.println("Could not open " + hpPath);
			System.exit(-1);
		}
		catch (IOException ex) {
			System.err.println("Could not load " + hpPath);
			System.exit(-1);
		}
		return ot;
	}
	
	public static void saveObject(ObjectType obj, String rootElementName, String fileName, boolean validate) {
		try {
			Properties prop;
			FileOutputStream fos;
			OutputStreamWriter osw;
			Marshaller marsh;
			
			prop = LocalConfiguration.getInstance().getProperties();
			prop.setProperty("org.exolab.castor.indent", "true");
			prop.setProperty("org.exolab.castor.marshalling.validation", "true");		
			
			fos = new FileOutputStream(fileName);
			osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
			
			marsh = new Marshaller(osw);
			marsh.setSchemaLocation("http://www.aes.org/processhistory http://hul.harvard.edu/ois/xml/xsd/drs/processhistory.xsd");
			marsh.setNamespaceMapping("tcf", "http://www.aes.org/tcf");
			marsh.setNamespaceMapping("xlink", "http://www.w3.org/1999/xlink");
			marsh.setNamespaceMapping("xsd", "http://www.w3.org/2001/XMLSchema");
			marsh.setRootElement(rootElementName);
			marsh.setValidation(validate);
			marsh.marshal(obj);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	public static ArrayList<StreamType> getStreams(MediaType mt, ADLSection adls) {
		ArrayList<StreamType> streams;
		EventListSection els;
		TracklistSection tls;
		ArrayList eventEntries;
		Iterator it;
		TcfToken beginTimeline = null;
		TcfToken endTimeline = null;
		TimeCodeCharacterFormatType firstTimeCode, lastTimeCode, durationTimeCode;
		BigDecimal duration;
		TimeRangeType trt;
		TreeSet dcSet;
		
		dcSet = new TreeSet();
		
		els = adls.getEventListSection();
		eventEntries = els.getEventEntries(BaseEditEntry.EntryType.CUT);
		it = eventEntries.iterator();
		while (it.hasNext()) {
			CutEditEntry cee;
			TcfToken din, dout;
			Range dcRange;
			
			cee = (CutEditEntry)it.next();
			
			// Determine destination channels
			dcRange = cee.getDestChannels();
			for (int i = dcRange.getBegin(); i <= dcRange.getEnd(); i++) {
				dcSet.add(new Integer(i));
			}
			
			din = cee.getDestIn();
			dout = cee.getDestOut();
			
			// Determine beginning of timeline
			if (beginTimeline == null) {
				beginTimeline = din;
			}
			else {
				if (din.valueOf() < beginTimeline.valueOf()) {
					beginTimeline = din;
				}
			}
			
			// Determine end of timeline
			if (endTimeline == null) {
				endTimeline = dout;
			}
			else {
				if (dout.valueOf() > endTimeline.valueOf()) {
					endTimeline = dout;
				}
			}
		}
		//System.out.println("# dest channels = " + dcSet.size());
		
		tls = adls.getTracklistSection();
		
		streams = new ArrayList<StreamType>();
		it = dcSet.iterator();
		while (it.hasNext()) {
			StreamType st;
			Integer num;
			
			num = (Integer)it.next();
			st = new StreamType();
			st.setStreamNum(num.intValue());
			if (tls != null) {
				TrackType tt;
				
				tt = tls.getTrackAtIndex(num.intValue());
				st.setLabel(tt.getTrackName());
			}
			else {
				st.setLabel("Track #" + num);
			}
			streams.add(st);
		}
		
		// Convert TcfToken to TimeCodeCharacterFormatType
		firstTimeCode = AES31Time.parseString(beginTimeline.toString());
		lastTimeCode = AES31Time.parseString(endTimeline.toString());
		
		trt = new TimeRangeType();
		trt.setStartTime(firstTimeCode);
		// Need to calculate the duration
		
		duration = AES31Time.getNumberOfSecondsForTimeCode(lastTimeCode).subtract(AES31Time.getNumberOfSecondsForTimeCode(firstTimeCode));
		durationTimeCode = AES31Time.convertFromBigDecimal(duration, lastTimeCode.getSamples().getSampleRate(), AES31Time.getTimeBaseForType(lastTimeCode.getTimeBase()), AES31Time.getFrameCountForType(lastTimeCode.getFrameCount()), lastTimeCode.getCountingMode());
		durationTimeCode.setFrameCount(lastTimeCode.getFrameCount());
		durationTimeCode.setTimeBase(lastTimeCode.getTimeBase());
		durationTimeCode.setVideoField(lastTimeCode.getVideoField());
		durationTimeCode.setFilmFraming(lastTimeCode.getFilmFraming());
		durationTimeCode.setCountingMode(lastTimeCode.getCountingMode());
		trt.setDuration(durationTimeCode);
		mt.setTimeRange(trt);
		mt.setStream(streams);
		return streams;
	}
	
	public static MediaType mediaFromADLFile(String filename) {
	    // System.out.println("mediaFromADLFile(" + filename + ")");
		MediaType mt = null;
		
		try {
			ADL adl;
			ADLSection adls = null;
			LocStringType lst;
			Iterator it;
			
			adl = new ADL(filename);
			adl.open("r");
			adls = adl.getADLSection();
			if (adls != null) {
				File f;
				URI uri;
				ProjectSection ps;
				
				lst = new LocStringType();
				lst.setShow(LocStringTypeShowType.NONE);
				lst.setActuate(LocStringTypeActuateType.NONE);
				
				f = new File(filename);
				uri = f.toURI();
				lst.setHref(uri.toString());
				
				ps = adls.getProjectSection();
				lst.setTitle(ps.getProjTitle());
				
				mt = new MediaType();
				mt.setMediaLocation(lst);
				mt.setKind(MediaKindType.AES_31_3_ADL_METADATA);
				mt.setTemporary(false);
				getStreams(mt, adls);
				it = mt.getStreamAsReference().iterator();
				while (it.hasNext()) {
					StreamType st;
					
					st = (StreamType)it.next();
					st.setMediaRef(mt);
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return mt;
	}
	
	public static MediaType mediaForFile(String path) {
		MediaType mt = null;
		LocStringType lst;
		File file;
		
		// System.out.println("mediaForFile(" + path + ")");
		file = new File(path);
		path = file.getAbsolutePath();
		
		mt = new MediaType();
		lst = new LocStringType();
		try {
			URI uri;
			
			uri = file.toURI();
			lst.setHref(uri.toString());
			if (path.endsWith(".wav")) {
				WAVEFile bwav;
				TimeRangeType trt;
				int numChannels;
				
				bwav = new WAVEFile(path);
				bwav.load();
				numChannels = bwav.formatChunk().getNumberOfChannels();
				for (int i = 0; i < numChannels; i++) {
					StreamType st;
					
					st = new StreamType();
					st.setStreamNum(i);
					st.setMediaRef(mt);
					mt.addStream(st);
				}
				//System.out.println("start time = " + bwav.getStartTime());
				//System.out.println("duration = " + bwav.getDuration());
				
				trt = new TimeRangeType();
				trt.setStartTime(AES31Time.parseString(bwav.getStartTime().toString()));
				trt.setDuration(AES31Time.parseString(bwav.getDuration().toString()));
				//System.out.println("AES31Time.toString(trt.getStartTime()) = " + AES31Time.toString(trt.getStartTime()));
				//System.out.println("AES31Time.toString(trt.getDuration()) = " + AES31Time.toString(trt.getDuration()));
				mt.setTimeRange(trt);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		mt.setMediaLocation(lst);
		return mt;
	}
	
	public static ArrayList<MediaType> mediaForPathsInPool(ArrayList<String> paths, MediaPoolType mpt) {
		ArrayList<String> mediaLocations;
		ArrayList al;
		Iterator it;
		
		al = new ArrayList();
		
		mediaLocations = new ArrayList<String>();
		it = paths.iterator();
		while (it.hasNext()) {
			File file;
			String path;
			
			path = (String)it.next();
			file = new File(path);
			path = file.getAbsolutePath();
			try {
				URI uri;
				
				uri = file.toURI();
				mediaLocations.add(uri.toString());
				// System.out.println("Trying path " + uri.toString());
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}			
		}
		
		it = mpt.getMediaAsReference().iterator();
		while (it.hasNext()) {
			MediaType mt;
			String href;
			
			mt = (MediaType)it.next();
			href = mt.getMediaLocation().getHref();
			if (AESUtil.isStringInArrayList(href, mediaLocations)) {
				al.add(mt);
				// System.out.println("Added " + href);
			}
		}
		
		return al;
	}
	
	public static boolean isStringInArrayList(String str, ArrayList<String> array) {
		Iterator<String> it;
		boolean retval = false;
		
		it = array.iterator();
		while (it.hasNext()) {
			String s;
			
			s = it.next();
			if (s.equals(str)) {
				retval = true;
				break;
			}
		}
		return retval;
	}

	public MediaType importCoreAudioFile(String fileName) {
		MediaType mt = null;
		AudioObjectType aot = null;
		
		//System.out.println("importCoreAudioFile(" + fileName + ")");
		
		aot = (AudioObjectType)importObjectFromFile(fileName, AudioObjectType.class, true);
		//System.out.println("aot = " + aot);
		if (aot != null) {
			Iterator streamIt;
			LocStringType lst;
			FaceRegionType frt = null;  // CHANGE THIS
			FaceType ft;
			
			mt = new MediaType();
			mt.setKind(MediaKindType.CORE_AUDIO_METADATA);
			mt.setTemporary(false);
			lst = new LocStringType();
			
			try {
				File f;
				URI uri;
				
				f = new File(fileName);
				uri = f.toURI();
				lst.setHref(uri.toString());
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			
			lst.setShow(LocStringTypeShowType.NONE);
			lst.setActuate(LocStringTypeActuateType.NONE);
			ft = aot.getFace(0);
			frt = ft.getRegion(0);
			lst.setTitle(ft.getLabel() + " - " + frt.getLabel());
			//System.out.println("lst.getTitle() = " + lst.getTitle());
			mt.setMediaLocation(lst);
			mt.setTimeRange(frt.getTimeRange());
			//System.out.println("frt.getTimeRange() = " + frt.getTimeRange());
			
			streamIt = frt.getStreamAsReference().iterator();
			while (streamIt.hasNext()) {
				org.aes.audioObject.StreamType aca_st;
				org.aes.processhistory.StreamType ph_st;
				ChannelAssignmentType cat;
				
				aca_st = (org.aes.audioObject.StreamType)streamIt.next();
				cat = aca_st.getChannelAssignment();
				ph_st = new org.aes.processhistory.StreamType();
				ph_st.setStreamNum(cat.getChannelNum());
				ph_st.setLabel(cat.getMapLocation());
				mt.addStream(ph_st);
				ph_st.setMediaRef(mt);
			}
		}
		
		return mt;
	}
	
	public Object importObjectFromFile(String fileName, Class aClass, boolean validate) {
		Object obj = null;
		FileInputStream fis;
		InputStreamReader isr;
		Unmarshaller unm;
		
		//System.out.println("importObjectFromFile(" + fileName + ", " + aClass + ")");
		try {
			BufferedReader br;
			String str;
			String line;
			StringBuffer sb;
			StringReader sr;
			Iterator it;
			
			fis = new FileInputStream(fileName);
			isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			
			unm = new Unmarshaller(aClass);
			//unm.setAttributeListener(this);
			// Validate the first time before we replace anything.
			unm.setValidation(validate);
			// Unmarshal the model object to get the map of IDs
			unm.unmarshal(isr);
			finishedParsingIDMap = true;
			fis.close();
			isr.close();
			
			fis = new FileInputStream(fileName);
			isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			br = new BufferedReader(isr);
			
			sb = new StringBuffer();
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			
			str = sb.toString();
			
			it = oldIDtoNewIDMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry;
				String key;
				String value;
				String newStr;
				
				entry = (Map.Entry)it.next();
				key = (String)entry.getKey();
				value = (String)entry.getValue();
				
				newStr = str.replaceAll(key, value);
				str = newStr;
			}
			
			// System.out.println(str);
			sr = new StringReader(str);
			
			// Unmarshal a second time to substitute the IDs
			// Do not validate the second time because we are changing references during unmarshalling.
			unm.setValidation(false);
			obj = unm.unmarshal(sr);
			finishedParsingIDMap = false;
			fis.close();
			isr.close();
		}
		catch (FileNotFoundException ex) {
			System.err.println("Can't open file: " + fileName);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return obj;
	}
	
	public void processAttribute(String attName, String attValue) {
		if (attName.equals("ID") == true) {
			String newValue;
			
			newValue = org.aes.processhistory.ObjectType.newObjectID();
			// key = old ID, value = new ID
			oldIDtoNewIDMap.put(attValue, newValue);
			// System.out.println("added (" + attValue + "," + newValue + ") to hash map\n\n");
		}		
	}
	
	
}
