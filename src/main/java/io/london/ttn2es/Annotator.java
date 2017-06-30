package io.london.ttn2es;

import org.thethingsnetwork.java.app.lib.Message;

/**
 * The Annotator class.
 * 
 * @author ctranoris
 *
 */
public class Annotator {

	/** The only instance of the class. */
	private static Annotator instance = null;

	/**
	 * Get the only instance of the class. In threadsafe manner
	 * 
	 * @return a Annotator instance
	 */
	public static synchronized Annotator getInstance() {
		if (instance == null) {
			instance = new Annotator();
		}
		return instance;
	}

	/**
	 * Get the data and transform the according to payload and the semantics of
	 * the device. We return an array of IAnnotatedData since in a single
	 * oayload there is the possibility to be encapsulated multiple values (as
	 * in case of temp,humidity soil sensors from a single device) we split the
	 * message in 3 distinct AnnotatedData
	 * 
	 * @param data
	 * @param payload
	 * @return an Array of AnnotatedData
	 */
	public final IAnnotatedData[] getAnnotatedData(final Message data, final String payload) {

		String deviceID = data.getString("dev_id");
		if (deviceID.contains("agrodu")) {
			return getAgroduAnnotatedData(data, payload);
		}
		if (deviceID.contains("_gps")) {
			return getGPSAnnotatedData(data, payload);
		} else {
			return getSimpleAnnotatedData(data, payload);
		}

	}

	/**
	 * The most generic type, just Annotate  the payload.
	 * @param data to annotate
	 * @param payload a payload
	 * @return an Array of AnnotatedData
	 */
	private IAnnotatedData[] getSimpleAnnotatedData(final Message data, final String payload) {


		return SimpleAnnotatedData.transformToSimpleAnnotatedData(data, payload);
	}
	
	
	/**
	 *  Annotate  the payload of a GPS position data sensor.
	 * @param data to annotate
	 * @param payload a payload
	 * @return an Array of AnnotatedData
	 */
	private IAnnotatedData[] getGPSAnnotatedData(final Message data, final String payload) {

		return GPSAnnotatedData.transformToGPSAnnotatedData(data, payload);
	}
	/**
	 *  Annotate  the payload of an agrodu device.
	 * @param data to annotate
	 * @param payload a payload
	 * @return an Array of AnnotatedData
	 */
	private IAnnotatedData[] getAgroduAnnotatedData(final Message data, final String payload) {

		return AgroduAnnotatedData.transformToAgroduAnnotatedData(data, payload);
	}

}
