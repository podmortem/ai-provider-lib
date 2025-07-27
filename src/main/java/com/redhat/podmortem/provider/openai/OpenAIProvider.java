package com.redhat.podmortem.provider.openai;

import com.redhat.podmortem.common.model.analysis.AnalysisResult;
import com.redhat.podmortem.common.model.provider.AIProvider;
import com.redhat.podmortem.common.model.provider.AIProviderConfig;
import com.redhat.podmortem.common.model.provider.AIResponse;
import com.redhat.podmortem.common.model.provider.ValidationResult;
import com.redhat.podmortem.provider.openai.dto.Request;
import com.redhat.podmortem.provider.openai.dto.Response;
import com.redhat.podmortem.provider.service.PromptTemplateService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

@ApplicationScoped
@Named("openai")
@RegisterForReflection
public class OpenAIProvider implements AIProvider {

    @Inject PromptTemplateService promptService;

    @Override
    public Uni<AIResponse> generateExplanation(AnalysisResult result, AIProviderConfig config) {
        String userPrompt = promptService.buildUserPrompt(result);
        String systemPrompt = promptService.getSystemPrompt();

        // Combine system and user prompts into a single completion prompt
        String combinedPrompt = systemPrompt + "\n\n" + userPrompt;

        var requestBody = new Request();
        requestBody.setModel(config.getModelId());
        requestBody.setStream(false);
        requestBody.setMaxTokens(config.getMaxTokens());
        requestBody.setPrompt(combinedPrompt);
        requestBody.setTemperature(config.getTemperature());

        Instant requestStart = Instant.now();

        // create REST client
        OpenAIClient client =
                RestClientBuilder.newBuilder()
                        .baseUri(URI.create(config.getApiUrl()))
                        .build(OpenAIClient.class);

        return client.getTextCompletion("Bearer " + config.getAuthToken(), requestBody)
                .map(osResponse -> toAIResponse(osResponse, config, requestStart));
    }

    @Override
    public Uni<ValidationResult> validateConfiguration(AIProviderConfig config) {
        ValidationResult result =
                new ValidationResult(
                        true,
                        getProviderId(),
                        config.getModelId(),
                        "Connection successful (mocked).");
        return Uni.createFrom().item(result);
    }

    @Override
    public String getProviderId() {
        return "openai";
    }

    private AIResponse toAIResponse(
            Response response, AIProviderConfig config, Instant requestStart) {
        Instant responseEnd = Instant.now();
        AIResponse aiResponse = new AIResponse();
        aiResponse.setProviderId(getProviderId());
        aiResponse.setModelId(config.getModelId());
        aiResponse.setGeneratedAt(response.getCreatedAsInstant());

        if (response.getChoices() != null && !response.getChoices().isEmpty()) {
            var choice = response.getChoices().get(0);
            if (choice != null && choice.getText() != null) {
                aiResponse.setExplanation(choice.getText().trim());
            } else {
                aiResponse.setExplanation("No explanation could be generated.");
            }
        } else {
            aiResponse.setExplanation("No explanation could be generated.");
        }

        if (response.getUsage() != null) {
            aiResponse.setTokenCount(response.getUsage().getTotalTokens());
        }

        aiResponse.setProcessingTime(Duration.between(requestStart, responseEnd));
        return aiResponse;
    }
}
