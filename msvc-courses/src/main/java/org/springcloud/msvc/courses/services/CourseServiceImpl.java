package org.springcloud.msvc.courses.services;
import org.springcloud.msvc.courses.clients.UserFeignClientRest;
import org.springcloud.msvc.courses.models.User;
import org.springcloud.msvc.courses.models.entity.Course;
import org.springcloud.msvc.courses.models.entity.CourseUser;
import org.springcloud.msvc.courses.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserFeignClientRest userFeignClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAllCourses() {
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCourseUserById(Long id) {
        //eliminar un usuario del curso
        courseRepository.deleteCourseUserById(id);
    }

    //-----------------------http feign client  --------------

    @Override
    @Transactional
    public Optional<User> assignUserToCourse(User user, Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if(courseOptional.isPresent()){
            User userClient = userFeignClientRest.findUserById(user.getId());

            Course course = courseOptional.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userClient.getId());

            //asigno usuario al curso en especifico
            course.addCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(userClient);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> createUserToCourse(User user, Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if(courseOptional.isPresent()){
            User userCreateClient = userFeignClientRest.createUser(user);

            Course course = courseOptional.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userCreateClient.getId());

            //asigno usuario creado
            course.addCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(userCreateClient);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> deleteUserToCourse(User user, Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if(courseOptional.isPresent()){
            User userClient = userFeignClientRest.findUserById(user.getId());

            Course course = courseOptional.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userClient.getId());

            //remuevo usuario al curso en especifico
            course.removeCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(userClient);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> getCourseByIdWithUserDetails(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if(courseOptional.isPresent()){
            Course course = courseOptional.get();
            if(!course.getCourseUsers().isEmpty()){
                //stream para obtener lista de ids
                List<Long> idsUsers= course.getCourseUsers()
                        .stream().map(cu -> cu.getUserId()).toList();
                //obtengo lista de usuarios, usando lista de ids
                List<User> usersClient = userFeignClientRest.getAllUsersById(idsUsers);
                //asignamos detalles de usuarios al curso
                course.setUsers(usersClient);
            }
            return Optional.of(course);
        }
        return Optional.empty();
    }
}
