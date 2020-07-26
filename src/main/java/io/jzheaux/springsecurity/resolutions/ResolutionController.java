package io.jzheaux.springsecurity.resolutions;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RestController
public class ResolutionController {
    private final ResolutionRepository resolutions;
    private final UserRepository userRepository;

    public ResolutionController(ResolutionRepository resolutions, UserRepository userRepository) {
        this.resolutions = resolutions;
        this.userRepository = userRepository;
    }

    @CrossOrigin(allowCredentials = "true")//(maxAge = 0) if locally verifying
    @GetMapping("/resolutions")
    @PreAuthorize("hasAuthority('resolution:read')")
    @PostFilter("@post.filter(#root)")
    public Iterable<Resolution> read() {
        Iterable<Resolution> resolutions = this.resolutions.findAll();
        resolutions.forEach(resolution -> {
            String fullName = userRepository.findByUsername(resolution.getOwner())
                    .map(User::getFullName).orElse("Anonymous");
            resolution.setText(resolution.getText().concat(", by").concat(fullName));
        });
        return resolutions;
    }

    @GetMapping("/resolution/{id}")
    @PreAuthorize("hasAuthority('resolution:read')")
    @PostAuthorize("@post.authorize(#root)")
    public Optional<Resolution> read(@PathVariable("id") UUID id) {
        return this.resolutions.findById(id);
    }

    @PostMapping("/resolution")
    @PreAuthorize("hasAuthority('resolution:write')")
    public Resolution make(@CurrentUsername String username, @RequestBody String text) {
        Resolution resolution = new Resolution(text, username);
        return this.resolutions.save(resolution);
    }

    @Transactional
    @PutMapping(path = "/resolution/{id}/revise")
    @PreAuthorize("hasAuthority('resolution:write')")
    @PostAuthorize("@post.authorize(#root)")
    public Optional<Resolution> revise(@PathVariable("id") UUID id, @RequestBody String text) {
        this.resolutions.revise(id, text);
        return read(id);
    }

    @Transactional
    @PutMapping("/resolution/{id}/complete")
    @PreAuthorize("hasAuthority('resolution:write')")
    @PostAuthorize("@post.authorize(#root)")
    public Optional<Resolution> complete(@PathVariable("id") UUID id) {
        this.resolutions.complete(id);
        return read(id);
    }
}
