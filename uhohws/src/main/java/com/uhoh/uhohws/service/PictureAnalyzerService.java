package com.uhoh.uhohws.service;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import com.uhoh.uhohws.dto.ImageClassNode;
import com.uhoh.uhohws.dto.ImageNode;
import com.uhoh.uhohws.dto.ImagesAnalysis;

public class PictureAnalyzerService {

	private ClientConfig clientConfig = null;

	// Client client = null;
	// String toneURL = "";
	// WebTarget post = null;

	public PictureAnalyzerService() {

		// values are in milliseconds
		// clientConfig.property(ClientProperties.READ_TIMEOUT, 2000);
		// clientConfig.property(ClientProperties.CONNECT_TIMEOUT, 5000);
		// PoolingClientConnectionManager connectionManager = new
		// PoolingClientConnectionManager();
		//
		// connectionManager.setMaxTotal(1);
		// clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER,
		// connectionManager);
		// clientConfig.connectorProvider(new ApacheConnectorProvider());

	}

	public ImagesAnalysis analyzeImageBundle(String path) {

		ClientConfig clientConfig = new ClientConfig();// .register(
														// LoggingFilter.class
														// );
		clientConfig.register(MultiPartFeature.class);
		Client client = ClientBuilder.newClient(clientConfig);

		// GET request to findBook resource with a query parameter
		String toneURL = "https://gateway.watsonplatform.net/visual-recognition-beta/api/v2/classify?version=2015-12-02";
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(
				"9466f616-47d0-4948-a43c-e9788794b44d", "qRK8hHYy8dVZ");
		WebTarget post = client.target(toneURL).register(feature);

		final FileDataBodyPart filePart = new FileDataBodyPart("image_file",
				new File(path));
		FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart
				.bodyPart(filePart);
		multipart
				.field("classifier_ids",
						"{\"classifiers\":[ {\"classifier_id\":\"FratModel2_1186732285\"}]}");
		Response response = post.request().post(
				Entity.entity(multipart, multipart.getMediaType()));

		if (response.getStatus() != 200) {
			return null;
		} else {
			ImagesAnalysis anl = response.readEntity(ImagesAnalysis.class);
			return anl;
		}
	}

	public static void main(String[] args) {
		PictureAnalyzerService svc = new PictureAnalyzerService();

		Long now = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			ImagesAnalysis anl = svc.analyzeImageBundle("/tmp/picture1.jpg");
			System.out.println("done");

			if (anl != null) {
				ImageNode node = anl.getImages().get(0);
				if (node != null && node.getScores().get(0) != null) {
					System.out.println("Score: "
							+ node.getScores().get(0).getScore());
				}
			}
		}
		
		System.out.println("time taken: " + (System.currentTimeMillis()-now)/1000 );
		// for (ImageNode x : anl.getImages())
		// for (ImageClassNode n : x.getScores())
		// System.out.println(x.getImage() + ";" + n.getName() + ";" +
		// n.getScore());

	}

}
