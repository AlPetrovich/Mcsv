package org.springcloud.msvc.courses.models.entity;

import javax.persistence.*;

@Entity
@Table(name = "courses_users")
public class CourseUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true)
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        //si la instancia es igual al objeto enviado
        if (this == obj){
            return true;
        }
        //valido si paso otro tipo de instancia
        if (!(obj instanceof CourseUser)){
            return false;
        }
        CourseUser o = (CourseUser) obj;
        return this.userId != null && this.userId.equals(o.getUserId());
    }
}
