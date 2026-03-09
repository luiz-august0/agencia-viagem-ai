package agent.dev.ia.inbound.http;

import agent.dev.ia.outbound.ai.agents.PackageExpert;
import agent.dev.ia.outbound.ai.agents.TravelAgentAssistant;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/travel")
public class TravelAgentController {

    @Inject
    TravelAgentAssistant travelAgentAssistant;

    @Inject
    PackageExpert packageExpert;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String ask(String question) {
        return travelAgentAssistant.chat(question);
    }

    @POST
    @Path("/package")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String ask(String question, @HeaderParam("X-User-Name") String userName) {
        if (userName != null && !userName.isEmpty()) {
            return packageExpert.chat(userName, question, userName);
        } else {
            return "Usuário precisa estar autenticado!";
        }
    }

}
