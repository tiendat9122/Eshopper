package com.ecommerce.api.eshopper.controller.admin.student_course;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ecommerce.api.eshopper.entity.Course;
import com.ecommerce.api.eshopper.repository.CourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.api.eshopper.dto.StudentDto;
import com.ecommerce.api.eshopper.entity.Student;
import com.ecommerce.api.eshopper.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentApi {

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;
    
    @GetMapping("/get")
    private ResponseEntity<?> getStudent() {
        List<Student> students = studentRepository.findAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping("/insert")
    private ResponseEntity<?> insertStudent(@RequestBody StudentDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.getName());

        List<Long> courseIds = studentDto.getCourseIds();
        Set<Course> courses = new HashSet<>();
        if(courseIds == null) {
            courses = new HashSet<>();
        } else {
            for(Long courseId : courseIds) {
                Course course = courseRepository.findById(courseId).orElseThrow();
                courses.add(course);
            }
        }
        student.setCourses(courses);

        Student studentInserted = studentRepository.save(student);
        return new ResponseEntity<>(studentInserted, HttpStatus.OK);

    }

    @PutMapping("/update")
    public ResponseEntity<?> updateStudent(@RequestParam(name = "id") Long id, @RequestBody StudentDto studentDto) {
        Student student = studentRepository.findById(id).orElseThrow();
        student.setName(studentDto.getName());

        List<Long> courseIds = studentDto.getCourseIds();
        Set<Course> courses = new HashSet<>();
        if(courseIds == null) {
            courses = new HashSet<>();
        } else {
            for(Long courseId : courseIds) {
                Course course = courseRepository.findById(courseId).orElseThrow();
                courses.add(course);
            }
        }
        student.setCourses(courses);

        Student studentUpdated = studentRepository.save(student);

        return new ResponseEntity<>(studentUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    private ResponseEntity<?> removeStudent(@RequestParam(name = "id") Long id) {
        Student student = studentRepository.findById(id).orElseThrow();
        studentRepository.delete(student);
        return new ResponseEntity<>("Student deleted", HttpStatus.OK);
    }
}
