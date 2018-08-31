package fish.eyebrow.blackandorangeservices.requestservices;

import fish.eyebrow.blackandorangeservices.emailservices.SimpleMailHandler;
import fish.eyebrow.blackandorangeservices.emailservices.exceptions.CredentialNotSetException;
import fish.eyebrow.blackandorangeservices.emailservices.exceptions.MissingPropertyException;
import org.json.JSONObject;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Path("enquiry")
public class EnquiryRequestHandler {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response enquiryRequest(InputStream enquiryData) {
		final StringBuilder enquiryDataBuilder = new StringBuilder();

		try {
			final BufferedReader enquiryDataReader = new BufferedReader(new InputStreamReader(enquiryData));

			String line;
			while ((line = enquiryDataReader.readLine()) != null) {
				enquiryDataBuilder.append(line);
			}

			final JSONObject enquiryJSON = new JSONObject(enquiryDataBuilder.toString());

			final String emailAddress = enquiryJSON.getString("emailAddress");
			final String fullName = enquiryJSON.getString("fullName");
			final String enquiryDetails = enquiryJSON.getString("enquiryDetails");

			final SimpleMailHandler simpleMailHandler = new SimpleMailHandler();
			simpleMailHandler.generateCredentials("webapps/requestservices/WEB-INF/classes/email-login.properties");

			final String[] recipients = {emailAddress};
			final String subject = "Enquiry from ".concat(fullName);
			final String content = "Hello, ".concat(fullName).concat("\n") +
					"You have sent an enquiry with this information: ".concat(enquiryDetails).concat("\n") +
					"We will get back to you as soon as possible";

			simpleMailHandler.sendEmail(recipients, subject, content);
		} catch (IOException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (MissingPropertyException | MessagingException | CredentialNotSetException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		return Response.status(Response.Status.OK).build();
	}
}