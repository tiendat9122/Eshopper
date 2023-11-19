package com.ecommerce.api.eshopper.controller.admin.student_course;

import com.ecommerce.api.eshopper.dto.CourseDto;
import com.ecommerce.api.eshopper.entity.Course;
import com.ecommerce.api.eshopper.entity.Student;
import com.ecommerce.api.eshopper.repository.CourseRepository;
import com.ecommerce.api.eshopper.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseApi {
    
    private final CourseRepository courseRepository;

    private final StudentRepository studentRepository;

    @GetMapping("/get")
    private ResponseEntity<?> getCourse() {
        List<Course> courses = courseRepository.findAll();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping("/insert")
    private ResponseEntity<?> insertCourse(@RequestBody CourseDto courseDto) {
        Course course = new Course();
        course.setName(courseDto.getName());

        List<Long> studentIds = courseDto.getStudentIds();
        Set<Student> students = new HashSet<>();
        if(students == null) {
            students = new HashSet<>();
        } else {
            for(Long studentId : studentIds) {
                Student student = studentRepository.findById(studentId).orElseThrow();
                students.add(student);
            }
        }

        course.setStudents(students);

        Course courseInserted = courseRepository.save(course);

        return new ResponseEntity<>(courseInserted, HttpStatus.OK);
    }

    @PutMapping("/update")
    private ResponseEntity<?> updateCourse(@RequestParam(name = "id") Long id, @RequestBody CourseDto courseDto) {
        Course course = courseRepository.findById(id).orElseThrow();
        course.setName(courseDto.getName());

        List<Long> studentIds = courseDto.getStudentIds();
        Set<Student> students = new HashSet<>();
        if(students == null) {
            students = new HashSet<>();
        } else {
            for(Long studentId : studentIds) {
                Student student = studentRepository.findById(studentId).orElseThrow();
                students.add(student);
            }
        }

        course.setStudents(students);

        Course courseUpdated = courseRepository.save(course);

        return new ResponseEntity<>(courseUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCourse(@RequestParam(name = "id") Long id) {
        Course course = courseRepository.findById(id).orElseThrow();
        courseRepository.delete(course);
        return new ResponseEntity<>("Course deleted", HttpStatus.OK);
    }

}
