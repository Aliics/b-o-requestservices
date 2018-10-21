package fish.eyebrow.blackandorangeservices.requestservices;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CrossOriginFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) {
		final String requestOrigin = request.getHeaders().getFirst("Origin");

		response.getHeaders().add("Access-Control-Allow-Origin", requestOrigin);
	}
}