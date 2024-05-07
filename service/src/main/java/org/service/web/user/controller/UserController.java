package org.service.web.user.controller;

import lombok.RequiredArgsConstructor;
import org.service.web.user.controller.dto.OperatorRequst;
import org.service.web.user.controller.dto.SuccessResponse;
import org.service.web.user.controller.dto.UsersContaining;
import org.service.web.user.controller.dto.UsersResponse;
import org.service.web.user.entity.Role;
import org.service.web.user.entity.User;
import org.service.web.user.entity.repository.RoleRepository;
import org.service.web.user.entity.repository.UserRepo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController{
    private final UserRepo userRepo;
    private final RoleRepository roleRepo;

    @PreAuthorize("hasRole('Администратор')")
    @Transactional
    @PostMapping("/operator/")
    public SuccessResponse operator(@Valid @RequestBody OperatorRequst request) {
        Set<Role> roles = new HashSet<>();

        User user = userRepo.findUserByUsername(request.getUsername());
        roles = user.getRoles();

        Role role = roleRepo.findByName("Оператор");
        roles.add(role);

        user.setRoles(roles);

        return new SuccessResponse("Роль успешно обновленна");
    }

    @PreAuthorize("hasRole('Администратор')")
    @GetMapping("/users/")
    public UsersResponse users() {
        List<User> users = userRepo.findAll();

        return new UsersResponse(users);
    }

    @PreAuthorize("hasRole('Администратор')")
    @GetMapping("/users/containing")
    public UsersResponse usersContaining(@Valid @RequestBody UsersContaining request) {
        List<User> users = userRepo.findUsersByUsernameContainingIgnoreCase(request.getName());

        return new UsersResponse(users);
    }
}
