package org.springcloud.msvc.users.services;

import org.springcloud.msvc.users.client.CourseFeignClientRest;
import org.springcloud.msvc.users.models.entity.User;
import org.springcloud.msvc.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseFeignClientRest courseFeignClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional //de escritura porque modifica la tabla
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    //Comunicacion Feign
    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        //tambien se va del curso
        courseFeignClientRest.deleteCourseUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //listar usuarios por ids
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllById(Iterable<Long> ids) {
        return (List<User>) userRepository.findAllById(ids);
    }

}
