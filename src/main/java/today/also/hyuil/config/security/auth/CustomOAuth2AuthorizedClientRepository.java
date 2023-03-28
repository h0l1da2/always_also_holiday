package today.also.hyuil.config.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomOAuth2AuthorizedClientRepository implements OAuth2AuthorizedClientRepository {

    private final Map<String, OAuth2AuthorizedClient> authorizedClients = new ConcurrentHashMap<>();

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request) {

//        authorizedClients.get()
        return null;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, Authentication principal, HttpServletRequest request, HttpServletResponse response) {

    }
}
