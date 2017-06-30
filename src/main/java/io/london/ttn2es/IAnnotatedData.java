package io.london.ttn2es;

import org.json.JSONObject;

import AgroduAnnotatedData.ValueType;

/**
 * @author ctranoris
 *
 */
public interface IAnnotatedData {

	
	/**
	 * @return a JSONObject
	 */
	JSONObject getJSONObject();

	/**
	 * 
	 * @return the ValueType
	 */
	ValueType getValueType();
}
