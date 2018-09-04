package fish.eyebrow.blackandorangeservices.requestservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("menu")
public class MenuRequestHandler {
	@GET
	@Produces("application/json")
	public Response generateMenu() {
		final StringBuilder jsonResponse = new StringBuilder()
				.append("{")
				.append("}");

		return Response.status(Response.Status.OK).entity(jsonResponse.toString()).build();
	}
}
