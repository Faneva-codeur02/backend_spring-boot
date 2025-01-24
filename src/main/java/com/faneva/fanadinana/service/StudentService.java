package com.faneva.fanadinana.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.faneva.fanadinana.entity.Student;
import com.faneva.fanadinana.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getMail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("student with id" + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String mail, LocalDate dob) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException(
                        "student with id " + studentId + "does not existing"));

        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if (mail != null &&
                mail.length() > 0 &&
                !Objects.equals(student.getName(), mail)) {
            Optional<Student> studentOptional = studentRepository
                    .findStudentByEmail(mail);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            student.setMail(mail);
        }

        if (dob != null && !Objects.equals(student.getDob(), dob)) {
            student.setDob(dob);
        }
    }

    public Optional<Student> getStudentsById(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("student with id" + studentId + " does not exists");
        }
        return studentRepository.findById(studentId);
    }

    @Transactional
    public List<Student> searchStudents(Long id, String name, String mail, String dob, Integer age) {
        final LocalDate[] parsedDob = { null };
        if (dob != null) {
            try {
                parsedDob[0] = LocalDate.parse(dob);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use 'yyyy-MM-dd'.");
            }
        }

        return studentRepository.findAll().stream()
                .filter(student -> (id == null || student.getId().equals(id)))
                .filter(student -> (name == null || student.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(student -> (mail == null || student.getMail().toLowerCase().contains(mail.toLowerCase())))
                .filter(student -> (parsedDob[0] == null || student.getDob().isEqual(parsedDob[0])))
                .filter(student -> (age == null || student.getAge().equals(age)))
                .collect(Collectors.toList());
    }

}
