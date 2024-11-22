package com.example.Atiko.clr;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.Atiko.entities.ERole;
import com.example.Atiko.entities.Profile;
import com.example.Atiko.entities.Role;
import com.example.Atiko.entities.Structure;
import com.example.Atiko.entities.User;
import com.example.Atiko.repositories.ProfileRepository;
import com.example.Atiko.repositories.RoleRepository;
import com.example.Atiko.repositories.StructureRepository;
import com.example.Atiko.repositories.UserRepository;

import java.util.Optional;
import java.util.Set;

@Component
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final StructureRepository structureRepository;

    public UserInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ProfileRepository profileRepository, StructureRepository structureRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
        this.structureRepository = structureRepository;
    }

    @Override
public void run(String... args) throws Exception {
    for (ERole roleEnum : ERole.values()) {
        // Vérifier si le rôle existe, sinon le créer
        Optional<Role> roleOptional = roleRepository.findByName(roleEnum);
        Role role;
        if (roleOptional.isEmpty()) { // Utiliser isEmpty() pour vérifier l'absence
            role = new Role();
            role.setName(roleEnum);
            roleRepository.save(role);
        } else {
            role = roleOptional.get();
        }

        // Vérifier si un utilisateur pour ce rôle existe, sinon le créer
        String username = roleEnum.name().toLowerCase().replace("role_", "");
        if (userRepository.findByUsername(username).isEmpty()) { // Assurez-vous que findByUsername renvoie un Optional
            User user = new User();
            user.setUsername(username);
            user.setEmail(username + "@bhc.com");
            user.setPassword(passwordEncoder.encode("password"));
            user.setStatus(true);
            user.setRoles(Set.of(role));
            userRepository.save(user);
            Profile profile = new Profile();
                profile.setUser(user); // L'utilisateur nouvellement créé
                // Vous pouvez définir d'autres attributs ici si nécessaire

                // Sauvegarder le profil
                profileRepository.save(profile); 
            System.out.println("Utilisateur '" + username + "' avec le rôle '" + roleEnum + "' créé.");
        }
        
    }
    structureRepository.save(new Structure()); 
}

}
