package best.server.member.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import best.server.common.domain.Address;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Embedded
    private Address address;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>();

    /**
     * Returns a {@link Member} given params
     */
    public static Member createMember(String email, String password, Set<MemberRole> roles, Address address) {
        return new Member(email, password, roles, address);
    }

    private Member(String email, String password, Set<MemberRole> roles, Address address) {
        this.email = email;
        this.password = password;
        this.address = address;
        this.roles = roles;
    }

    public void updatePassword(String password) {
        this.password = checkNotNull(password, "password");
    }
}
