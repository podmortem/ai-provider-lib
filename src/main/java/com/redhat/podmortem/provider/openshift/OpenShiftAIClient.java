package com.redhat.podmortem.provider.openshift;

import com.redhat.podmortem.provider.openshift.dto.Request;
import com.redhat.podmortem.provider.openshift.dto.Response;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import java.net.URI;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://localhost") // overridden at runtime
public interface OpenShiftAIClient {

    /**
     * Method for making a call to the OpenShift AI completion endpoint.
     *
     * @param baseUri The full URI of the AI provider's endpoint.
     * @param bearerToken The Authorization token.
     * @param requestBody The request payload.
     * @return A Uni that will resolve to the provider's response.
     */
    @POST
    Uni<Response> getCompletion(
            URI baseUri, @HeaderParam("Authorization") String bearerToken, Request requestBody);
}
