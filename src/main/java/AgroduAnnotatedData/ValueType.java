package AgroduAnnotatedData;

/**
 * @author ctranoris
 *
 */
public enum ValueType {

	  /** */
	  SIMPLE("Simple value"),
	  /** */
	  GPSLOCATION("GPS location"),
	  /** */
	  TEMPERATURE("Temperature"),
	  /** */
	  HUMIDITY("Humidity"),
	  /** */
	  SOILHYGROMETER("Soil Hygrometer");

	  /** */
	  private final String displayName;

	  /**
	   * @param adisplayName String
	   * @param adisplayNamePlural String
	   */
	  ValueType(String adisplayName)
	  {
	    this.displayName = adisplayName;
	  }

	  /**
	   * @return String
	   */
	  public String getDisplayName()
	  {
	    return displayName;
	  }

	}
