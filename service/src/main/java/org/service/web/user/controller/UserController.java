package org.service.web.user.controller;

import lombok.RequiredArgsConstructor;
import org.service.web.user.controller.dto.*;
import org.service.web.user.entity.Role;
import org.service.web.user.entity.User;
import org.service.web.user.entity.repository.RoleRepository;
import org.service.web.user.entity.repository.UserRepo;
import org.service.web.user.exception.UserRegisterException;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController implements IUserController{
    private final UserRepo userRepo;
    private final RoleRepository roleRepo;

    @Override
    public SuccessResponse registration(RegistrationRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            throw new UserRegisterException("Пользователь существет");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPasswrod());

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepo.findByName("Пользователь"));
        user.setRoles(roles);

        userRepo.save(user);

        return new SuccessResponse("Пользователь успешно зарегестрирован");
    }

    @Override
    public SuccessResponse operator(OperatorRequst request) {
        Set<Role> roles = new HashSet<>();

        User user = userRepo.findUserByUsername(request.getUsername());
        roles = user.getRoles();

        Role role = roleRepo.findByName("Оператор");
        roles.add(role);

        user.setRoles(roles);

        return new SuccessResponse("Роль успешно обновленна");
    }

    @Override
    public UsersResponse users() {
        List<User> users = userRepo.findAll();

        return new UsersResponse(users);
    }

    @Override
    public UsersResponse usersContaining(UsersContaining request) {
        List<User> users = userRepo.findUsersByUsernameContainingIgnoreCase(request.getName());

        return new UsersResponse(users);
    }
}
