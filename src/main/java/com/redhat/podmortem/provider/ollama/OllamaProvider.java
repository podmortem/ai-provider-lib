package com.redhat.podmortem.provider.ollama;

import com.redhat.podmortem.common.model.analysis.AnalysisResult;
import com.redhat.podmortem.common.model.provider.AIProvider;
import com.redhat.podmortem.common.model.provider.AIProviderConfig;
import com.redhat.podmortem.common.model.provider.AIResponse;
import com.redhat.podmortem.common.model.provider.ValidationResult;
import com.redhat.podmortem.provider.ollama.dto.Request;
import com.redhat.podmortem.provider.ollama.dto.Response;
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

/**
 * Ollama AI provider implementation for generating explanations of pod failure analysis results.
 *
 * <p>Integrates with Ollama's local AI model API. Registered as CDI bean with name "ollama".
 *
 * @see AIProvider
 * @see com.redhat.podmortem.provider.ollama.OllamaClient
 */
@ApplicationScoped
@Named("ollama")
@RegisterForReflection
public class OllamaProvider implements AIProvider {

    @Inject PromptTemplateService promptService;

    /**
     * Generates an AI explanation for the given pod failure analysis result.
     *
     * @param result the analysis result containing pod failure information to explain
     * @param config the AI provider configuration including API URL and model
     * @return a Uni that emits the AI-generated explanation with metadata
     * @throws IllegalArgumentException if required configuration is missing
     */
    @Override
    public Uni<AIResponse> generateExplanation(AnalysisResult result, AIProviderConfig config) {
        String userPrompt = promptService.buildUserPrompt(result);
        String systemPrompt = promptService.getSystemPrompt();

        var requestBody = new Request();
        requestBody.setModel(config.getModelId());
        requestBody.setPrompt(systemPrompt + "\n" + userPrompt);
        requestBody.setStream(false);

        Instant requestStart = Instant.now();

        // create REST client
        OllamaClient client =
                RestClientBuilder.newBuilder()
                        .baseUri(URI.create(config.getApiUrl()))
                        .build(OllamaClient.class);

        return client.getCompletion(requestBody)
                .map(osResponse -> toAIResponse(osResponse, config, requestStart));
    }

    /**
     * Validates the provided AI provider configuration.
     *
     * <p>Currently returns a mocked successful validation.
     *
     * @param config the configuration to validate
     * @return a Uni that emits the validation result
     */
    @Override
    public Uni<ValidationResult> validateConfiguration(AIProviderConfig config) {
        // TODO: add validation, currently just a mock
        ValidationResult result =
                new ValidationResult(
                        true,
                        getProviderId(),
                        config.getModelId(),
                        "Connection successful (mocked).");
        return Uni.createFrom().item(result);
    }

    /**
     * Returns the unique identifier for this AI provider.
     *
     * @return the string "ollama"
     */
    @Override
    public String getProviderId() {
        return "ollama";
    }

    /**
     * Converts an Ollama API response to the internal AIResponse format.
     *
     * @param response the raw response from the Ollama API
     * @param config the configuration used for the request
     * @param requestStart the timestamp when the request was initiated
     * @return a formatted AIResponse with explanation text and metadata
     */
    private AIResponse toAIResponse(
            Response response, AIProviderConfig config, Instant requestStart) {
        Instant responseEnd = Instant.now();
        AIResponse aiResponse = new AIResponse();
        aiResponse.setProviderId(getProviderId());
        aiResponse.setModelId(config.getModelId());
        aiResponse.setGeneratedAt(response.getCreatedAt());
        aiResponse.setExplanation(response.getResponse());
        aiResponse.setTokenCount(response.getEvalCount());
        aiResponse.setProcessingTime(Duration.between(requestStart, responseEnd));
        return aiResponse;
    }
}
