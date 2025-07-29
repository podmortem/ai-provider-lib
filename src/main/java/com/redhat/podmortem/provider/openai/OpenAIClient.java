package com.redhat.podmortem.provider.openai;

import com.redhat.podmortem.provider.openai.dto.Request;
import com.redhat.podmortem.provider.openai.dto.Response;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client interface for communicating with the OpenAI API.
 *
 * @see com.redhat.podmortem.provider.openai.OpenAIProvider
 */
@RegisterRestClient(baseUri = "https://api.openai.com/v1") // overridden at runtime
public interface OpenAIClient {

    /**
     * Requests a text completion from the OpenAI API.
     *
     * @param bearerToken the Bearer authentication token for API access
     * @param requestBody the completion request containing model, prompt, and parameters
     * @return a Uni that emits the completion response asynchronously
     * @throws jakarta.ws.rs.WebApplicationException if the API request fails
     */
    @POST
    @Path("/completions")
    Uni<Response> getTextCompletion(
            @HeaderParam("Authorization") String bearerToken, Request requestBody);
}
