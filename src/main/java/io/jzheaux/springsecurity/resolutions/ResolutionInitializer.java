package io.jzheaux.springsecurity.resolutions;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

@Component
public class ResolutionInitializer implements SmartInitializingSingleton {

    private final ResolutionRepository resolutions;
    private final UserRepository userRepository;

    public ResolutionInitializer(ResolutionRepository resolutions, UserRepository userRepository) {
        this.resolutions = resolutions;
        this.userRepository = userRepository;
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.resolutions.save(new Resolution("Read War and Peace", "user"));
        this.resolutions.save(new Resolution("Free Solo the Eiffel Tower", "user"));
        this.resolutions.save(new Resolution("Hang Christmas Lights", "user"));
        this.resolutions.save(new Resolution("Hang Christmas Lights", "hasread"));
        this.resolutions.save(new Resolution("Hang Christmas Lights", "haswrite"));

        User user = new User("user", "{bcrypt}$2y$12$igU7VilJIQu8DUY.XgCJPuisjRCE9maNNnO5CnhrdrQvNv3UNqm.m");
        user.grantAuthority("ROLE_ADMIN");
        user.grantAuthority("resolution:read");
        user.grantAuthority("resolution:write");
        this.userRepository.save(user);

        User admin = new User("admin","{bcrypt}$2a$10$bTu5ilpT4YILX8dOWM/05efJnoSlX4ElNnjhNopL9aPoRyUgvXAYa");
        admin.grantAuthority("ROLE_ADMIN");
        this.userRepository.save(admin);

        User hasread = new User("hasread", "{bcrypt}$2y$12$igU7VilJIQu8DUY.XgCJPuisjRCE9maNNnO5CnhrdrQvNv3UNqm.m");
        hasread.grantAuthority("resolution:read");
        this.userRepository.save(hasread);

        User haswrite = new User("haswrite", "{bcrypt}$2y$12$igU7VilJIQu8DUY.XgCJPuisjRCE9maNNnO5CnhrdrQvNv3UNqm.m");
        haswrite.grantAuthority("resolution:write");
        this.userRepository.save(haswrite);
    }
}
