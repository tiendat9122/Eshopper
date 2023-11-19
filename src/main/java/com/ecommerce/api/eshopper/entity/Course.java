package com.ecommerce.api.eshopper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "courses", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("courses")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Student> students;

    @PreRemove
    public void preRemove() {
        for(Student student : students) {
            student.getCourses().remove(this);
        }
    }

}
