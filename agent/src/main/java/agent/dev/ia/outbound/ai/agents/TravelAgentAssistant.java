package agent.dev.ia.outbound.ai.agents;

import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface TravelAgentAssistant {

    String chat(String userMessage);

}
