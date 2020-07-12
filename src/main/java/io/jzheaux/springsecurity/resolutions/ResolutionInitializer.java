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
        User user = new User("user", "{bcrypt}$2a$10$MywQEqdZFNIYnx.Ro/VQ0ulanQAl34B5xVjK2I/SDZNVGS5tHQ08W");
        this.userRepository.save(user);
    }
}
