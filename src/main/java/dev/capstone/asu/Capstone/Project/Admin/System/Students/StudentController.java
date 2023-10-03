package dev.capstone.asu.Capstone.Project.Admin.System.Students;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents()
    {
        List<Student> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long id)
    {
        Student student = studentService.findById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<Student> addStudent(@RequestBody Student student)
    {
        Student newStudent = studentService.addStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long id, @RequestBody Student student)
    {
        Student updatedStudent = studentService.findById(id);
        updatedStudent.setFirstName(student.getFirstName());
        updatedStudent.setLastName(student.getLastName());
        updatedStudent.setEmail(student.getEmail());
        updatedStudent.setAsuriteID(student.getAsuriteID());
        updatedStudent.setProjectPreferences(student.getProjectPreferences());
        updatedStudent.setAssignedProject(student.getAssignedProject());

        studentService.addStudent(updatedStudent);
        return new ResponseEntity<>(updatedStudent, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id)
    {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
