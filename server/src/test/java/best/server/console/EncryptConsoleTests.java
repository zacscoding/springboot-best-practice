package best.server.console;

import java.util.function.Function;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EncryptConsoleTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    StringEncryptor stringEncryptor;

    @Test
    public void runPasswordEncoderTests() {
        String[] messages = {
                "pass"
        };

        runTests(messages, message -> passwordEncoder.encode(message), "", "");
    }

    @Test
    public void runPropertyEncryptTests() throws Exception {
        String[] messages = {
                "tester"
        };

        runTests(messages, message -> stringEncryptor.encrypt(message), "ENC(", ")");
    }

    private void runTests(String[] messages, Function<String, String> encrypt,
                          String prefix, String suffx) {
        for (String message : messages) {
            System.out.println("Message : " + message);
            System.out.println(prefix + encrypt.apply(message) + suffx);
            System.out.println("----------------------------");
        }
    }
}
