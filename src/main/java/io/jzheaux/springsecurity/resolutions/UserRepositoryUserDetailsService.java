package io.jzheaux.springsecurity.resolutions;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;

public class UserRepositoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserRepositoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::map)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user"));
    }

    private BridgeUser map(User user) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        user.getUserAuthorities().forEach(userAuthority -> {
            if (userAuthority.getAuthority().equals("ROLE_ADMIN")) {
                authorities.add(new SimpleGrantedAuthority("resolution:read"));
                authorities.add(new SimpleGrantedAuthority("resolution:write"));
            }
            authorities.add(new SimpleGrantedAuthority(userAuthority.getAuthority()));
        });
        return new BridgeUser(user, authorities);
    }

    private static class BridgeUser extends User implements UserDetails {

        private Collection<GrantedAuthority> authorities;

        public BridgeUser(User user, Collection<GrantedAuthority> authorities) {
            super(user);
            this.authorities = authorities;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public boolean isAccountNonExpired() {
            return this.isEnabled();
        }

        @Override
        public boolean isAccountNonLocked() {
            return this.isEnabled();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return this.isEnabled();
        }
    }
}
