package nl.wtrlmn.skm;

import nl.wtrlmn.skm.models.User;
import nl.wtrlmn.skm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SkmApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkmApplication.class, args);
    }


    @Bean
    CommandLineRunner initDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String email = "admin@example.com";
            if (userRepository.findByEmail(email).isEmpty()) {
                User admin = new User();
                admin.setName("Super Admin");
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setAdmin(true);
                admin.setCoach(false);
                admin.setImgProfile(null);
                admin.setPosition("Administrator");
                admin.setTeam(null);
                userRepository.save(admin);
                System.out.println("Admin: " + email + " / admin123");
            } else {
                System.out.println("Account with this email already exist: " + email);
            }
        };
    }



}
