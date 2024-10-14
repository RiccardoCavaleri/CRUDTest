package com.example.demo_test.controllers;

import com.example.demo_test.services.StudentService;
import com.example.demo_test.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity<Student> create(@RequestBody Student student){
        Student createdStudent = studentService.create(student);
        return ResponseEntity.ok().body(createdStudent);
    }

    @GetMapping("/readAll")
    public ResponseEntity<List<Student>> readAll(){
        List<Student> allStudents = studentService.readAll();
        return  ResponseEntity.ok().body(allStudents);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Student> readById(@PathVariable Long id){
        Optional<Student> studentOptional = studentService.readById(id);
        if (studentOptional.isPresent()){
            return ResponseEntity.ok().body(studentOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/change/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        Optional<Student> studentOptional = studentService.update(id, student);
        if (studentOptional.isPresent()){
            return ResponseEntity.ok().body(studentOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/setWorking/{id}")
    public ResponseEntity<Student> updateWorking(@PathVariable Long id, @RequestParam(name = "working") boolean isWorking) {
        Optional<Student> studentOptional = studentService.updateWorking(id, isWorking);
        if (studentOptional.isPresent()){
            return ResponseEntity.ok().body(studentOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        boolean wasThere = studentService.delete(id);
        if(wasThere){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}