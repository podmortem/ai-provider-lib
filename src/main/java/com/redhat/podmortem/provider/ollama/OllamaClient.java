package com.redhat.podmortem.provider.ollama;

import com.redhat.podmortem.provider.ollama.dto.Request;
import com.redhat.podmortem.provider.ollama.dto.Response;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client interface for communicating with the Ollama API.
 *
 * @see com.redhat.podmortem.provider.ollama.OllamaProvider
 */
@RegisterRestClient(baseUri = "http://localhost:11434") // overridden at runtime
public interface OllamaClient {

    /**
     * Requests a text completion from the Ollama API.
     *
     * @param requestBody the completion request containing model, prompt, and parameters
     * @return a Uni that emits the completion response asynchronously
     * @throws jakarta.ws.rs.WebApplicationException if the API request fails
     */
    @POST
    @Path("/api/generate")
    Uni<Response> getCompletion(Request requestBody);
}
