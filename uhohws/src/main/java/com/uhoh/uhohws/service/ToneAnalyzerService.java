package com.uhoh.uhohws.service;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.uhoh.uhohws.dto.ToneElement;
import com.uhoh.uhohws.dto.ToneNode;
import com.uhoh.uhohws.dto.ToneObject;
import com.uhoh.uhohws.dto.ToneText;

@SuppressWarnings("deprecation")
public class ToneAnalyzerService {

	public static class Analysis {
		public ArrayList<ToneNode> tones = null;
		public String sentiment;
	}

	private ClientConfig clientConfig = null;
	Client client = null;
	String toneURL = "";
	WebTarget post = null;

	public ToneAnalyzerService() {
		clientConfig = new ClientConfig();// .register( LoggingFilter.class );
		// values are in milliseconds
		//clientConfig.property(ClientProperties.READ_TIMEOUT, 2000);
		//clientConfig.property(ClientProperties.CONNECT_TIMEOUT, 500);
		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager();

		connectionManager.setMaxTotal(1);
		clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER,
				connectionManager);
		clientConfig.connectorProvider(new ApacheConnectorProvider());

		client = ClientBuilder.newClient(clientConfig);

		// GET request to findBook resource with a query parameter
		toneURL = "https://gateway.watsonplatform.net/tone-analyzer-beta/api/v3/tone?version=2016-02-11&sentences=false";
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(
				"c148fe39-e464-4edf-9699-49a944d74c60", "J5TVrsZa5Poq");
		post = client.target(toneURL).register(feature);
	}

	public ToneObject analyzeSentence(String toneText) {

		ToneText x = new ToneText(toneText);
		Invocation.Builder invocationBuilder = post
				.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(x,
				MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			return null;
		} else {
			ToneObject tone = response.readEntity(ToneObject.class);
			return tone;
		}
	}

	public Analysis isStrong(String toneText) {
		ToneObject obj = analyzeSentence(toneText);
		ArrayList<ToneNode> ret = new ArrayList<ToneNode>();
		Analysis anl = new Analysis();
		ToneElement tones = obj.getDocument_tone().getTone_categories().get(0);
		ToneElement emotional = obj.getDocument_tone().getTone_categories().get(2);
		if (tones.getTones().get(0).getScore() > 0.5
				&& emotional.getTones().get(4).getScore() > 0.8) {
			System.out.println("anger + emotional range > threshold: " + toneText);
			ret.add(tones.getTones().get(0));
			ret.add(tones.getTones().get(1));
			anl.sentiment = "NEG";
		} else {
			ret.add(tones.getTones().get(3));
			ret.add(tones.getTones().get(4));
			anl.sentiment = "POS";
		}

		anl.tones = ret;
		return anl;
	}

	public static void printResults(ArrayList<ToneNode> nodes) {

		for (ToneNode node : nodes) {
			System.out.println(node.toString());
		}
	}

	static class MyThread implements Runnable {
		ToneAnalyzerService svc;
		String txt;

		public MyThread(ToneAnalyzerService svc, String txt) {
			// store parameter for later user
			this.svc = svc;
			this.txt = txt;
		}

		public void run() {
			ToneAnalyzerService.printResults(svc.isStrong(txt).tones);
		}
	}

	public static void main(String[] args) throws Exception {
		// Create Jersey client

		Long now = System.currentTimeMillis();
		ToneAnalyzerService svc = new ToneAnalyzerService();

		ArrayList<Thread> threads = new ArrayList<Thread>();
		for( int i=0; i< 10; i++){
			String txt = "trying to explain what's going on to foreigners is hard, but they are looking at us as if we've lost our minds";
			//Runnable r = new MyThread(svc, txt);
			Thread t1 = new Thread(new MyThread(svc, txt));
			threads.add(t1);
			t1.start();
		}
		
		for(Thread thread : threads){
			((Thread) thread).join();
		}
		/*
		toneText = "trying to explain what's going on to foreigners is hard, but they are looking at us as if we've lost our minds";
		printResults(svc.isStrong(toneText).tones);
		toneText = "trying to explain what's going on to foreigners is hard, but they are looking at us as if we've lost our minds";
		printResults(svc.isStrong(toneText).tones);
		toneText = "trying to explain what's going on to foreigners is hard, but they are looking at us as if we've lost our minds";
		printResults(svc.isStrong(toneText).tones);
		toneText = "trying to explain what's going on to foreigners is hard, but they are looking at us as if we've lost our minds";
		printResults(svc.isStrong(toneText).tones);
		toneText = "trying to explain what's going on to foreigners is hard, but they are looking at us as if we've lost our minds";
		printResults(svc.isStrong(toneText).tones);
		toneText = "trying to explain what's going on to foreigners is hard, but they are looking at us as if we've lost our minds";
		printResults(svc.isStrong(toneText).tones);
		toneText = "trying to explain what's going on to foreigners is hard, but they are looking at us as if we've lost our minds";
		printResults(svc.isStrong(toneText).tones);
*/
		System.out.println("Time taken: " + (System.currentTimeMillis() - now)); // 5619
	}

}
