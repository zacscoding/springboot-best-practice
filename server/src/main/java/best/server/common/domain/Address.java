package best.server.common.domain;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Address's value object
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipCode;

    /**
     * Returns a {@link Address} given args
     */
    public static Address createAddress(String city, String street, String zipCode) {
        return new Address(city, street, zipCode);
    }
}
