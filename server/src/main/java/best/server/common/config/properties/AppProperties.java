package best.server.common.config.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import best.server.member.domain.MemberRole;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {

    private String clientId;
    private String clientSecret;
    private List<User> users;

    @Getter
    @Setter
    public static class User {
        private String email;
        private String password;
        private String firstName;
        private String secondName;
        private List<MemberRole> roles;
        private Addr address;
    }

    @Getter
    @Setter
    public static class Addr {
        private String city;
        private String street;
        private String zipCode;
    }
}
