package io.london.ttn2es;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.ws.rs.core.Response;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import org.thethingsnetwork.java.app.lib.Client;
import org.thethingsnetwork.java.app.lib.Message;
import org.apache.log4j.Logger;
import org.apache.cxf.jaxrs.client.WebClient;

/**
 * @author ctranoris
 *
 */
public class TTN2ES {

	/** */
	final private static Logger log = Logger.getLogger(TTN2ES.class);
	/** */
	private static final String REGION = "eu.thethings.network";
	/** */
	private static final String APPID = "YOUR_APPID";
	/** */
	private static final String ACCESSKEY = "YOUR_API_KEY";
	/** */
	private static final String ESENDPOINTURL = "http://150.140.184.249:9200";

	/**
	 * 
	 */
	public TTN2ES() {

		try {
			this.initializeClinet();
		} catch (URISyntaxException e) {
			log.error("error: " + e.getMessage());
			e.printStackTrace();
		} catch (MqttException e) {
			log.error("error: " + e.getMessage());
		}
	}

	/**
	 * @throws URISyntaxException
	 *             e
	 * @throws MqttException
	 *             e
	 */
	private void initializeClinet() throws URISyntaxException, MqttException {

		String region = REGION;
		String appId = APPID;
		String accessKey = ACCESSKEY;

		final Client client = new Client(region, appId, accessKey);

		client.onMessage(new BiConsumer<String, Object>() {
			public void accept(final String devId, final Object data) {
				log.debug("Device: " + devId + ", JSON: " + data);

				byte[] rawpayload = ((org.thethingsnetwork.java.app.lib.Message) data).getBinary("payload_raw");

				String payload = new String(rawpayload);
				((org.thethingsnetwork.java.app.lib.Message) data).put("Payload", payload);
				log.debug(data);
				annotateAndPostData((org.thethingsnetwork.java.app.lib.Message) data, payload);

			}

		});

		client.onActivation(new BiConsumer<String, JSONObject>() {

			public void accept(final String devId, final JSONObject data) {
				log.info("Activation: " + devId + ", data: " + data);
			}
		});

		client.onError(new Consumer<Throwable>() {
			public void accept(final Throwable error) {
				log.error("error: " + error.getMessage());
			}
		});

		client.onConnected(new Consumer<MqttClient>() {
			public void accept(final MqttClient client) {
				log.info("Client connected!");
			}
		});

		client.start();
	}

	/**
	 * @param data
	 *            data
	 * @param payload
	 *            a decoded payload
	 */
	protected final void annotateAndPostData(final Message data, final String payload) {
		log.debug("annotateAndPostData: " + data);
		IAnnotatedData[] annotatedData = Annotator.getInstance()
				.getAnnotatedData((org.thethingsnetwork.java.app.lib.Message) data, payload);

		for (IAnnotatedData ad : annotatedData) {
			postToES(ad);
		}

	}

	/**
	 * post to ES
	 * 
	 * @param ad
	 */
	protected void postToES(final IAnnotatedData ad) {
		log.info("IAnnotatedData: " + ad.getJSONObject().toString());

		String urlAPIIndex = "";
		// will post each type to a different dataset
		switch (ad.getValueType()) {
		case TEMPERATURE:
			urlAPIIndex = "temperature";
			break;
		case HUMIDITY:
			urlAPIIndex = "humidity";
			break;

		case SOILHYGROMETER:
			urlAPIIndex = "soilhygrometer";
			break;
		case GPSLOCATION:
			urlAPIIndex = "gpslocation";
			break;
		case SIMPLE:
			urlAPIIndex = "simplevalues";
			break;

		default:
			break;
		}

		List<Object> providers = new ArrayList<Object>();
		providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
		String url = ESENDPOINTURL + "/"+urlAPIIndex + "_index/" + urlAPIIndex;
		WebClient client = WebClient.create(url, providers);
		Response r = client.accept("application/json").type("application/json").post(ad.getJSONObject().toString());
		log.debug("POSTed to: " + url + ". Response " + r.getStatus() );

	}

}
