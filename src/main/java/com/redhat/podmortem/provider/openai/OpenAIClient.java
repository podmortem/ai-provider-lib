package com.redhat.podmortem.provider.openai;

import com.redhat.podmortem.provider.openai.dto.Request;
import com.redhat.podmortem.provider.openai.dto.Response;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://api.openai.com/v1") // overridden at runtime
public interface OpenAIClient {

    @POST
    @Path("/completions")
    Uni<Response> getTextCompletion(
            @HeaderParam("Authorization") String bearerToken, Request requestBody);
}
