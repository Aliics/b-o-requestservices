package fish.eyebrow.blackandorangeservices.requestservices

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/adminlogin")
class AdminLoginRequestHandler {
    @GET
    @Produces("application/json")
    fun attemptLogin() {

    }
}