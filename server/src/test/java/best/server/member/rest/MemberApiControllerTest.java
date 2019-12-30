package best.server.member.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import best.server.common.AbstractControllerTest;
import best.server.common.RestDocsUtil;
import best.server.common.domain.Address;
import best.server.common.response.ApiErrorCode;
import best.server.member.MemberDetails;
import best.server.member.domain.Member;
import best.server.member.domain.MemberRole;
import best.server.member.service.MemberService;

public class MemberApiControllerTest extends AbstractControllerTest {

    private Member admin;
    private Member user1;

    private String adminAccessToken;
    private String userAccessToken;

    @MockBean
    MemberService accountService;

    @BeforeEach
    public void setUp() {
        admin = createMember(1L, "admin@gmail.com", passwordEncoder.encode("admin"),
                             "zac", "kim",
                             Address.createAddress("seoul", "st1", "1"),
                             MemberRole.ADMIN, MemberRole.USER);

        user1 = createMember(2L, "user1@gmail.com", passwordEncoder.encode("user1"),
                             "user1", "kim",
                             Address.createAddress("seoul", "st2", "1234"),
                             MemberRole.USER);

        when(accountService.loadUserByUsername(admin.getEmail())).thenReturn(new MemberDetails(admin));
        when(accountService.loadUserByUsername(user1.getEmail())).thenReturn(new MemberDetails(user1));
    }

    @Test
    @DisplayName("get self member's info")
    public void testGetSelfMember() throws Exception {
        // given
        ensureAdminAccessToken();

        // when then
        mockMvc.perform(get("/account/me")
                                .header(HttpHeaders.AUTHORIZATION, getBearerToken(adminAccessToken))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaTypes.HAL_JSON_VALUE))
               .andDo(print())
               // http status
               .andExpect(status().isOk())
               .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
               // data
               .andExpect(jsonPath("email").value(admin.getEmail()))
               .andExpect(jsonPath("password").doesNotHaveJsonPath())
               .andExpect(jsonPath("firstName").value(admin.getFirstName()))
               .andExpect(jsonPath("lastName").value(admin.getLastName()))
               .andExpect(jsonPath("roles").isNotEmpty())
               .andExpect(jsonPath("roles").isArray())
               .andExpect(jsonPath("address").hasJsonPath())
               .andExpect(jsonPath("address.city").value(admin.getAddress().getCity()))
               .andExpect(jsonPath("address.street").value(admin.getAddress().getStreet()))
               .andExpect(jsonPath("address.zipCode").value(admin.getAddress().getZipCode()))
               .andExpect(jsonPath("address.zipCode").value(admin.getAddress().getZipCode()))
               // links
               .andExpect(jsonPath("_links").hasJsonPath())
               .andExpect(jsonPath("_links.self").hasJsonPath())
               // docs
               .andDo(
                       document("get-account-me",
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE)
                                                .description("content type header")
                                ),
                                // response
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                                ),
                                links(
                                        linkWithRel("self").description("link to self")
                                ),
                                relaxedResponseFields(
                                        RestDocsUtil.getMemberFieldDescriptors()
                                )
                       )
               );
    }

    @Test
    @DisplayName("get self member fail if no access token")
    public void testGetSelfMemberThrowAccessDeniedIfNoAccessToken() throws Exception {
        // when then
        mockMvc.perform(get("/account/me")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaTypes.HAL_JSON_VALUE))
               .andDo(print())
               // http status
               .andExpect(status().isUnauthorized())
               // api response
               .andExpect(jsonPath("status").value(ApiErrorCode.UNAUTHORIZED.getStatus()))
               .andExpect(jsonPath("code").value(ApiErrorCode.UNAUTHORIZED.getCode()))
               .andExpect(jsonPath("message").value(ApiErrorCode.UNAUTHORIZED.getMessage()))
               .andExpect(jsonPath("errors").isArray());
    }

    private void ensureAdminAccessToken() throws Exception {
        final String adminUsername = "admin@gmail.com";
        final String adminPassword = "admin";
        final Map<String, Object> tokenMap = getOAuth2Token(adminUsername, adminPassword);
        adminAccessToken = (String) tokenMap.get("access_token");
        assertThat(adminAccessToken).isNotNull();
    }

    private void ensureUserAccessToken() throws Exception {
        final String adminUsername = "user1@gmail.com";
        final String adminPassword = "user1";
        final Map<String, Object> tokenMap = getOAuth2Token(adminUsername, adminPassword);
        userAccessToken = (String) tokenMap.get("access_token");
        assertThat(userAccessToken).isNotNull();
    }

    private Member createMember(Long id, String email, String pass, String firstName, String lastName,
                                Address address, MemberRole... roles) {

        final Member member = Member.createMember(email, pass, firstName, lastName,
                                                  new HashSet<>(Arrays.asList(roles)), address);

        try {
            Field field = member.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(member, id);
            assertThat(member.getId()).isEqualTo(id);
            return member;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
