package best.server.common.config;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import best.server.common.config.properties.AppProperties;
import best.server.common.config.properties.AppProperties.User;
import best.server.common.domain.Address;
import best.server.member.domain.Member;
import best.server.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
@Slf4j
public class AppConfiguration {

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            private AppProperties properties;
            @Autowired
            private MemberService memberService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                initMembers();
            }

            private void initMembers() throws Exception {
                for (User user : properties.getUsers()) {
                    final Address address = Address.createAddress(user.getAddress().getCity(),
                                                                  user.getAddress().getCity(),
                                                                  user.getAddress().getZipCode());

                    final Member member = Member.createMember(user.getEmail(), user.getPassword(),
                                                              new HashSet<>(user.getRoles()), address);

                    try {
                        Long id = memberService.joinMember(member);
                        logger.info("Success to save member {} > {}", member, id);
                    } catch (Exception e) {
                        logger.warn("Exception occur while saving member", e);
                    }
                }
            }
        };
    }
}
