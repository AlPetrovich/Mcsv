package org.springcloud.msvc.users.services;

import org.springcloud.msvc.users.models.entity.User;

import java.util.List;
import java.util.Optional;


public interface IUserService {

    List<User> findAllUsers();
    Optional<User> findById(Long id);
    User saveUser(User user);
    void deleteUser(Long id);

    Optional<User> findByEmail(String email);
    boolean existsUserByEmail(String email);

    List<User> findAllById(Iterable<Long> ids);


}
