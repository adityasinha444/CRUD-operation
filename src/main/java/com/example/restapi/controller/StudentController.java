package com.example.restapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.restapi.entity.Student;
import com.example.restapi.repository.StudentRepository;

@RestController
public class StudentController {

    @Autowired
    private StudentRepository repo;

    // Get all the students
    // localhost:8080/students
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return repo.findAll();
    }

    // Get a student by ID (Now we manually filter using findAll())
    // localhost:8080/students/{id}
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable int id) {
        
        Optional<Student> student = repo.findAll().stream()
                                        .filter(s -> s.getId() == id)
                                        .findFirst();
        
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Add a new student
    @PostMapping("/student/add")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student newStudent = repo.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStudent);
    }

    // Update an existing student (Now we manually filter using findAll())
    @PutMapping("/student/update/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student updatedStudent) {
        // Manually filter by ID
        Optional<Student> existingStudentOpt = repo.findAll().stream()
                                                   .filter(s -> s.getId().equals(id))
                                                   .findFirst();
        
        return existingStudentOpt.map(existingStudent -> {
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setPercentage(updatedStudent.getPercentage());
            existingStudent.setBranch(updatedStudent.getBranch());
            return ResponseEntity.ok(repo.save(existingStudent));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete a student (Now we manually filter using findAll())
    @DeleteMapping("/student/delete/{id}")
    public ResponseEntity<Object> removeStudent(@PathVariable Integer id) {
        // Manually filter by ID
        Optional<Student> studentOpt = repo.findAll().stream()
                                           .filter(s -> s.getId().equals(id))
                                           .findFirst();
        
        return studentOpt.map(student -> {
            repo.delete(student);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
