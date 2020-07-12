package io.jzheaux.springsecurity.resolutions;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@Entity(name = "authorities")
public class UserAuthority {

    @Id
    private UUID id;

    @Column
    private String authority;

    @ManyToOne
    @JoinColumn(name="username", referencedColumnName="username")
    private User user;

    public UserAuthority(String authority,User user) {
        this.id=UUID.randomUUID();
        this.user=user;
        this.authority = authority;
    }

}
