package io.jzheaux.springsecurity.resolutions;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserRepositoryUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserRepositoryUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(BridgeUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user"));
    }

    private static class BridgeUser extends User implements UserDetails {

        public BridgeUser(User user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return this.getUserAuthorities().stream()
                    .map(UserAuthority::getAuthority)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
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
