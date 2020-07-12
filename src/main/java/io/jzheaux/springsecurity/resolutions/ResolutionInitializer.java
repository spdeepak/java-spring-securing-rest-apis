package io.jzheaux.springsecurity.resolutions;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResolutionInitializer implements SmartInitializingSingleton {

    private final ResolutionRepository resolutions;
    private final UserRepository userRepository;

    @Override
    public void afterSingletonsInstantiated() {
        this.resolutions.save(new Resolution("Read War and Peace", "user"));
        this.resolutions.save(new Resolution("Free Solo the Eiffel Tower", "user"));
        this.resolutions.save(new Resolution("Hang Christmas Lights", "user"));

        User user = new User("user", "{bcrypt}$2y$12$igU7VilJIQu8DUY.XgCJPuisjRCE9maNNnO5CnhrdrQvNv3UNqm.m");
        this.userRepository.save(user);

        User hasread = new User("hasread", "{bcrypt}$2y$12$igU7VilJIQu8DUY.XgCJPuisjRCE9maNNnO5CnhrdrQvNv3UNqm.m");
        hasread.grantAuthority("resolution:read");
        this.userRepository.save(hasread);

        User haswrite = new User("haswrite", "{bcrypt}$2y$12$igU7VilJIQu8DUY.XgCJPuisjRCE9maNNnO5CnhrdrQvNv3UNqm.m");
        haswrite.grantAuthority("resolution:write");
        this.userRepository.save(haswrite);
    }
}
