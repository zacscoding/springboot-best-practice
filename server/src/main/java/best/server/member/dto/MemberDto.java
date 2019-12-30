package best.server.member.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import best.server.common.domain.Address;
import best.server.member.domain.MemberRole;
import lombok.Builder;
import lombok.Data;

/**
 * Member DTO
 */
@Data
@Builder
public class MemberDto {

    private Long id;

    @Email
    private String email;

    @NotEmpty
    private String password;

    private String firstName;

    private String lastName;

    private Set<MemberRole> roles;

    private Address address;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("id", id)
                          .add("email", email)
                          .add("password", "[PROTECTED]")
                          .add("firstName", firstName)
                          .add("lastName", lastName)
                          .add("roles", roles)
                          .add("address", address)
                          .toString();
    }
}
