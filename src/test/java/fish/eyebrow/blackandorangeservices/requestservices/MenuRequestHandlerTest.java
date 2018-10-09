package fish.eyebrow.blackandorangeservices.requestservices;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static fish.eyebrow.blackandorangeservices.requestservices.MenuRequestHandler.generateMenu;
import static org.junit.jupiter.api.Assertions.*;

class MenuRequestHandlerTest {

	@Test
	void checkIfGenerateMenuIsNotNull() {
		assertNotNull(generateMenu());
	}

	@Test
	void generateMenuStatusOK() {
		assertEquals(generateMenu().getStatus(), Response.Status.OK.getStatusCode());
	}
}