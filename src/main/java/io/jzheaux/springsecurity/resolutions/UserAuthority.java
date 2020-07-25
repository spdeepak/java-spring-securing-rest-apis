package io.jzheaux.springsecurity.resolutions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity(name = "authorities")
public class UserAuthority {

    @Id
    private UUID id;

    @Column
    private String authority;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    public UserAuthority() {
    }

    public UserAuthority(String authority, User user) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.authority = authority;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
