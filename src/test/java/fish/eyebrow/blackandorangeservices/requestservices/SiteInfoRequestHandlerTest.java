package fish.eyebrow.blackandorangeservices.requestservices;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static fish.eyebrow.blackandorangeservices.requestservices.SiteInfoRequestHandler.generateAbout;
import static fish.eyebrow.blackandorangeservices.requestservices.SiteInfoRequestHandler.generateContact;
import static fish.eyebrow.blackandorangeservices.requestservices.SiteInfoRequestHandler.generateMenu;
import static org.junit.jupiter.api.Assertions.*;

class SiteInfoRequestHandlerTest {

	@Test
	void aboutGenerationStatusOK() {
		assertEquals(generateAbout().getStatus(), Response.Status.OK.getStatusCode());
	}

	@Test
	void checkIfGenerateMenuIsNotNull() {
		assertNotNull(generateMenu());
	}

	@Test
	void generateMenuStatusOK() {
		assertEquals(generateMenu().getStatus(), Response.Status.OK.getStatusCode());
	}

	@Test
	void contactGenerationStatusOK() {
		assertEquals(generateContact().getStatus(), Response.Status.OK.getStatusCode());
	}
}