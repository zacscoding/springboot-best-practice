package best.server.common;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Map;

import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.google.gson.Gson;

import best.server.config.RestDocsConfig;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(RestDocsConfig.class)
@AutoConfigureRestDocs(uriPort = 3000)
@ActiveProfiles("test")
public class AbstractControllerTest {

    protected final String clientId = "application";
    protected final String clientSecret = "pass";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected Gson gson = new Gson();

    @Autowired
    protected ModelMapper modelMapper;

    /**
     * Getting access token
     * {
     *  "access_token" : "0607cb87-306f-40b6-9423-b0f85731b9e0",
     *  "token_type":"bearer",
     *  "refresh_token":"b1a0a5ac-d2c0-4230-bae4-eed44a984ed3",
     *  "expires_in":59999,
     *  "scope":"read write"
     * }
     */
    protected Map<String, Object> getOAuth2Token(String username, String password) throws Exception {

        ResultActions perform = mockMvc.perform(post("/oauth/token")
                                                        .with(httpBasic(clientId, clientSecret))
                                                        .param("username", username)
                                                        .param("password", password)
                                                        .param("grant_type", "password"));

        final String response = perform.andReturn().getResponse().getContentAsString();
        System.out.println("## response " + response);
        return new Jackson2JsonParser().parseMap(response);
    }

    /**
     * Getting access token with refresh_token
     */
    protected Map<String, Object> refreshOAuth2Token(String refreshToken) throws Exception {

        ResultActions perform = mockMvc.perform(post("/oauth/token")
                                                        .with(httpBasic(clientId, clientSecret))
                                                        .param("grant_type", "refresh_token")
                                                        .param("refresh_token", refreshToken));

        final String response = perform.andReturn().getResponse().getContentAsString();
        return new Jackson2JsonParser().parseMap(response);
    }

    protected String getBearerToken(String accessToken) {
        return "Bearer " + accessToken;
    }
}
