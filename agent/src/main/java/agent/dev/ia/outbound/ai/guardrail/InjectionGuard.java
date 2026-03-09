package agent.dev.ia.outbound.ai.guardrail;

import agent.dev.ia.outbound.ai.agents.PackageSecurityExpert;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class InjectionGuard implements InputGuardrail {

    @Inject
    PackageSecurityExpert packageSecurityExpert;

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        if (packageSecurityExpert.isAttack(userMessage.singleText())) {
            return failure("Sua mensagem foi bloqueada por conter instruções não permitidas.");
        }
        return InputGuardrailResult.success();
    }

}
