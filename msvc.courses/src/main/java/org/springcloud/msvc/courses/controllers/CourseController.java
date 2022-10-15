package org.springcloud.msvc.courses.controllers;

import feign.FeignException;
import org.springcloud.msvc.courses.models.User;
import org.springcloud.msvc.courses.models.entity.Course;
import org.springcloud.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> findAllCourses(){
        return ResponseEntity.ok(courseService.findAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> findCourseById(@PathVariable Long id){
        Optional<Course> courseOptional = courseService.getCourseByIdWithUserDetails(id);//courseService.findById(id);
        if(courseOptional.isPresent()){
            return ResponseEntity.ok(courseOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course, BindingResult result){
        if(result.hasErrors()){
            return validations(result);
        }
        Course courseDB = courseService.save(course);
        return new ResponseEntity<>(courseDB, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@Valid @RequestBody Course course,BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return validations(result);
        }
        Optional<Course> courseOptional = courseService.findById(id);
        if (courseOptional.isPresent()){
            Course courseDb = courseOptional.get();
            courseDb.setName(course.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id){
        Optional<Course> courseOptional = courseService.findById(id);
        if(courseOptional.isPresent()){
            courseService.delete(courseOptional.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //--------------------- http feign

    @PutMapping("/assign-user/{courseId}")
    public ResponseEntity<?> assignUserToCourse(@RequestBody User user, @PathVariable Long courseId){
        Optional<User> userOptional;
        try{
            userOptional = courseService.assignUserToCourse(user, courseId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Mensaje","Usuario no encontrado o error en la comunicacion: " + e.getMessage()));
        }
        if (userOptional.isPresent()){
            return new ResponseEntity<>(userOptional.get(), HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-user/{courseId}")
    public ResponseEntity<?> createUserToCourse(@RequestBody User user, @PathVariable Long courseId){
        Optional<User> userOptional;
        try{
            userOptional = courseService.createUserToCourse(user, courseId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Mensaje","Error al crear usuario: " + e.getMessage()));
        }
        if (userOptional.isPresent()){
            return new ResponseEntity<>(userOptional.get(), HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-user/{courseId}")
    public ResponseEntity<?> deleteUserToCourse(@RequestBody User user, @PathVariable Long courseId){
        Optional<User> userOptional;
        try{
            userOptional = courseService.deleteUserToCourse(user, courseId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Mensaje","Usuario no encontrado o error en la comunicacion: " + e.getMessage()));
        }
        if (userOptional.isPresent()){
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    //desvincular usuario del curso
    @DeleteMapping("/delete-course-user/{id}")
    public ResponseEntity<?> deleteCourseUserById(@PathVariable Long id){
        courseService.deleteCourseUserById(id);
        return ResponseEntity.noContent().build();
    }

    //metodo de validaciones
    private static ResponseEntity<Map<String, String>> validations(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo "+ err.getField()+ " "+ err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
