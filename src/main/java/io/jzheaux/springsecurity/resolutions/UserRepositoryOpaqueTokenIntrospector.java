package io.jzheaux.springsecurity.resolutions;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class UserRepositoryOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
	private final UserRepository users;
	private final OpaqueTokenIntrospector delegate;

	public UserRepositoryOpaqueTokenIntrospector(UserRepository users, OpaqueTokenIntrospector delegate) {
		this.users = users;
		this.delegate = delegate;
	}

	@Override
	public OAuth2AuthenticatedPrincipal introspect(String token) {
		OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
		User user = this.users.findByUsername(principal.getName())
				.orElseThrow(() -> new UsernameNotFoundException("no user"));
		Collection<GrantedAuthority> authorities = principal.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthority().substring(6)))
				.collect(Collectors.toList());
		Collection<GrantedAuthority> userAuthorities = user.getUserAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
				.collect(Collectors.toList());
		authorities.retainAll(userAuthorities);
		if ("premium".equals(user.getSubscription()) &&
				authorities.contains(new SimpleGrantedAuthority("resolution:write"))) {
			authorities.add(new SimpleGrantedAuthority("resolution:share"));
		}
		return new UserOAuth2AuthenticatedPrincipal(user, principal.getAttributes(), authorities);
	}

	private static class UserOAuth2AuthenticatedPrincipal extends User implements OAuth2AuthenticatedPrincipal {
		private Map<String, Object> attributes;
		private Collection<GrantedAuthority> authorities;

		public UserOAuth2AuthenticatedPrincipal(
				User user, Map<String, Object> attributes, Collection<GrantedAuthority> authorities) {
			super(user);
			this.attributes = attributes;
			this.authorities = authorities;
		}

		@Override
		public Map<String, Object> getAttributes() {
			return this.attributes;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return this.authorities;
		}

		@Override
		public String getName() {
			return this.username;
		}
	}
}
