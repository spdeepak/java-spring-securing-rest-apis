package io.jzheaux.springsecurity.resolutions;

import lombok.Data;

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

@Data
@Entity(name = "users")
public class User implements Serializable {

    @Id
    UUID id;
    @Column
    String username;
    @Column
    String password;
    @Column
    boolean enabled = true;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Collection<UserAuthority> userAuthorities = new ArrayList<>();

    public User(String username, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
    }

    public Collection<UserAuthority> getUserAuthorities() {
        return Collections.unmodifiableCollection(userAuthorities);
    }

    public void grantAuthority(String authority) {
        UserAuthority userAuthority = new UserAuthority(authority, this);
        this.userAuthorities.add(userAuthority);
    }
}
