package org.springcloud.msvc.users.repositories;
import org.springcloud.msvc.users.models.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email=?1")
    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);
}

