package org.springcloud.msvc.courses.clients;

import org.springcloud.msvc.courses.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-users", url = "${spring.users.url}")
public interface UserFeignClientRest {

    @GetMapping("/{id}")
    User findUserById(@PathVariable Long id);

    @PostMapping("/")
    User createUser(@RequestBody User user);

    @GetMapping("/users-per-course")
    List<User> getAllUsersById(@RequestParam Iterable<Long> ids);
}
