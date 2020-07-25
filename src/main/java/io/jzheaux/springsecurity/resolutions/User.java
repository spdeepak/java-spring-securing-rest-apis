package io.jzheaux.springsecurity.resolutions;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity(name = "users")
public class User implements Serializable {

    @Id
    private UUID id;
    @Column(name = "username", unique = true)
    private String username;
    @Column
    private String password;
    @Column
    private boolean enabled = true;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<UserAuthority> userAuthorities = new ArrayList<>();

    public User() {
    }

    public User(String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
    }

    public User(User user) {
        this.id = user.id;
        this.username = user.username;
        this.password = user.password;
        this.enabled = user.enabled;
        this.userAuthorities = user.userAuthorities;
    }

    public Collection<UserAuthority> getUserAuthorities() {
        return Collections.unmodifiableCollection(userAuthorities);
    }

    public void setUserAuthorities(Collection<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    public void grantAuthority(String authority) {
        UserAuthority userAuthority = new UserAuthority(authority, this);
        this.userAuthorities.add(userAuthority);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
