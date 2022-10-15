package org.springcloud.msvc.courses.models.entity;

import org.springcloud.msvc.courses.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    //ids de usuarios del curso
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id")
    private List<CourseUser> courseUsers;

    //Atributo no está mapeado a la persistencia, solo obtener los datos completos del usuario
    @Transient
    private List<User> users;


    public Course() {
        courseUsers = new ArrayList<>();
        users = new ArrayList<>();
    }

    //Agregar usuarios al curso
    public void addCourseUser(CourseUser courseUser){
        courseUsers.add(courseUser);
    }

    //cada vez que removemos un usuario, lo busca y compara por id
    //sobreescribir equals en courseUser para validar por id
    public void removeCourseUser(CourseUser courseUser){
        courseUsers.remove(courseUser);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<CourseUser> getCourseUsers() {
        return courseUsers;
    }

    public void setCourseUsers(List<CourseUser> courseUsers) {
        this.courseUsers = courseUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
