package com.sajib_4414.expense.tracker.user;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    @Autowired
    public void createUser(UserDetails user) {
        User newUser = new User();
        newUser.setEmail(user.getUsername());
        newUser.setPassword(user.getPassword()); // Hash the password before saving



        // Set roles
        List<Role> roles = user.getAuthorities()
                .stream()
                .map(grantedAuthority -> {
                    String roleName = grantedAuthority.getAuthority();
                    return roleRepository.findByName(roleName);
                })
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        newUser.setRoleList(roles);
        userRepository.save(newUser);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
