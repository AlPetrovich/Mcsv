package org.springcloud.msvc.courses.services;

import org.springcloud.msvc.courses.models.User;
import org.springcloud.msvc.courses.models.entity.Course;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CourseService {

    List<Course> findAllCourses();
    Optional<Course> findById(Long id);
    Course save(Course course);
    void delete(Long id);
    void deleteCourseUserById(Long id);

    Optional<User> assignUserToCourse(User user, Long courseId);
    Optional<User> createUserToCourse(User user, Long courseId);
    Optional<User> deleteUserToCourse(User user, Long courseId);

    Optional<Course> getCourseByIdWithUserDetails(Long courseId);

}
