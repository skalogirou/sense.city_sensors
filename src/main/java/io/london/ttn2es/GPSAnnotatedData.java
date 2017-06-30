package io.london.ttn2es;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.thethingsnetwork.java.app.lib.Message;

import AgroduAnnotatedData.ValueType;

/**
 * @author ctranoris
 *
 */
public class GPSAnnotatedData implements IAnnotatedData {

	/** */
	final private static Logger log = Logger.getLogger(GPSAnnotatedData.class);

	/** */
	private ValueType valueType;
	
	/**
	 * 
	 */
	private JSONObject annotatedData;
	/* (non-Javadoc)
	 * @see io.london.ttn2es.IAnnotatedData#getJSONObject()
	 */	
	public final JSONObject getJSONObject() {
		// TODO Auto-generated method stub
		return annotatedData;
	}
	
	/**
	 * @param data
	 * @param vt
	 * @param string
	 */
	public GPSAnnotatedData(final Message data, final ValueType vt, final double lat, final double lon, final double alt) {
		annotatedData = new JSONObject(data.toString());
		annotatedData.put("ValueType", vt.getDisplayName());
		JSONObject latlon = new JSONObject();
		latlon.put("lat", lat);
		latlon.put("lon", lon);
		annotatedData.put("ValueLocation", latlon);
		annotatedData.put("ValueAltitude", alt);
		this.valueType = vt;

	}

	/**
	 * @param data
	 * @param payload
	 * @return
	 */
	public static GPSAnnotatedData[] transformToGPSAnnotatedData(Message data, String payload ) {
		if ( payload.contains("0 0")){
			log.debug("Will not transform message, Not correct payload");
			return new GPSAnnotatedData[0];
		}
		GPSAnnotatedData[] ads = new GPSAnnotatedData[1];
		String vals[] = payload.split(" ");
		 //38236052 21750556 63 168
		double lat = Double.parseDouble(vals[0])/1000000;
		double lon = Double.parseDouble(vals[1])/1000000;
		double alt = Double.parseDouble(vals[2]);
		ads[0] = new GPSAnnotatedData(data, ValueType.GPSLOCATION, lat, lon, alt);
		return ads;
	}
	

	public ValueType getValueType() {
		return valueType;
	}


	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

}
