package dev.capstone.asu.Capstone.Project.Admin.System.Students;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepo studentRepo;

    @Autowired
    public StudentService(StudentRepo studentRepo)
    {
        this.studentRepo = studentRepo;
    }

    public Student addStudent(Student student)
    {
        return studentRepo.save(student);
    }

    public List<Student> getAllStudents()
    {
        return studentRepo.findAll();
    }

    public Student findById(Long id)
    {
        return studentRepo.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " not found!"));
    }

    public void deleteStudent(Long id)
    {
        studentRepo.deleteById(id);
    }
}
