package io.london.ttn2es;

import org.json.JSONObject;
import org.thethingsnetwork.java.app.lib.Message;

import AgroduAnnotatedData.ValueType;

/**
 * @author ctranoris
 *
 */
public class SimpleAnnotatedData implements IAnnotatedData {


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
	public SimpleAnnotatedData(final Message data, final ValueType vt, final String value) {
		annotatedData = new JSONObject(data.toString());
		annotatedData.put("ValueType", vt.getDisplayName());
		annotatedData.put("Value", value);
		this.valueType = vt;

	}

	public static SimpleAnnotatedData[] transformToSimpleAnnotatedData(Message data, String payload) {
		SimpleAnnotatedData[] ads = new SimpleAnnotatedData[1];
		ads[0] = new SimpleAnnotatedData(data, ValueType.SIMPLE, payload);
		return ads;
	}
	

	public ValueType getValueType() {
		return valueType;
	}


	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

}
