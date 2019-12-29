package best.server;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ServerApplication {

    public static void main(String[] args) {
        beforeApplicationSetup(args);
        SpringApplication.run(ServerApplication.class, args);
    }

    private static void beforeApplicationSetup(String[] args) {
        checkActiveProfile();
    }

    static void checkActiveProfile() {
        final String defaultProfile = "local";
        String profileValues = System.getProperty("spring.profiles.active");

        if (!StringUtils.hasText(profileValues)) {
            logger.warn("Setting default profile because empty active profile. default : [{}]", defaultProfile);
            System.setProperty("spring.profiles.active", defaultProfile);
            return;
        }

        Set<String> requiredProfileSet = new HashSet<>();
        requiredProfileSet.add("local");
        requiredProfileSet.add("dev");
        requiredProfileSet.add("prod");

        String[] profiles = profileValues.split(",");
        for (String profile : profiles) {
            if (requiredProfileSet.contains(profile)) {
                return;
            }
        }

        profileValues = profileValues + "," + defaultProfile;
        System.setProperty("spring.profiles.active", profileValues);
    }
}
