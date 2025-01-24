package com.faneva.fanadinana.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.faneva.fanadinana.dto.StudentSearchRequest;
import com.faneva.fanadinana.entity.Student;
import com.faneva.fanadinana.service.StudentService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @GetMapping(path = "{studentId}")
    public Optional<Student> getStudentsById(@PathVariable("studentId") Long studentId) {

        return studentService.getStudentsById(studentId);
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestBody Student student) {

        studentService.updateStudent(studentId, student.getName(), student.getMail(), student.getDob());
    }

    @PostMapping("/search")
    public List<Student> searchStudents(
            @RequestBody StudentSearchRequest searchStudent) {
        return studentService.searchStudents(searchStudent.getId(), searchStudent.getName(), searchStudent.getMail(),
                searchStudent.getDob(), searchStudent.getAge());
    }

}
