package dev.capstone.asu.Capstone.Project.Admin.System.Controller;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.*;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService)
    {
        this.adminService = adminService;
    }

    @GetMapping("/allStudents")
    public ResponseEntity<List<Student>> findAllStudents()
    {
        List<Student> students = adminService.findAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/findStudent/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long id)
    {
        Optional<Student> student = adminService.findStudentById(id);
        if (student.isEmpty()) throw new EntityNotFoundException("Student not found with id = " + id.toString());
        return new ResponseEntity<>(student.get(), HttpStatus.OK);
    }

    @PostMapping("/addStudent")
    public ResponseEntity<Student> addStudent(@RequestBody Student student)
    {
        Student newStudent = adminService.addStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long id, @RequestBody Student student)
    {
        Optional<Student> foundStudent = adminService.updateStudent(id, student);
        if (foundStudent.isEmpty()) throw new EntityNotFoundException("Student not found with id = " + id.toString());
        return new ResponseEntity<>(foundStudent.get(), HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id)
    {
        adminService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addProject")
    public ResponseEntity<Project> addProject(@RequestBody Project project)
    {
        Project newProject = adminService.addProject(project);
        return new ResponseEntity<>(newProject, HttpStatus.CREATED);
    }

    @GetMapping("/allProjects")
    public ResponseEntity<List<Project>> findAllProjects()
    {
        List<Project> projects = adminService.findAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/findProject/{id}")
    public ResponseEntity<Project> findProjectById(@PathVariable("id") Long id)
    {
        Optional<Project> foundProject = adminService.findProjectById(id);
        if (foundProject.isEmpty()) throw new EntityNotFoundException("Project not found with id = " + id.toString());
        return new ResponseEntity<>(foundProject.get(), HttpStatus.OK);
    }

    @PutMapping("/updateProject/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Long id, @RequestBody Project project)
    {
        Optional<Project> foundProject = adminService.updateProject(id, project);
        if (foundProject.isEmpty()) throw new EntityNotFoundException("Project not found with id = " + id.toString());
        return new ResponseEntity<>(foundProject.get(), HttpStatus.OK);
    }

    @GetMapping("/projectsByYear/{year}")
    public ResponseEntity<List<Project>> findProjectsByYear(@PathVariable("year") String year)
    {
        List<Project> projects = adminService.findProjectsByYear(year);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @DeleteMapping("/deleteProject/{id}")
    public void deleteProject(@PathVariable("id") Long id)
    {
        adminService.deleteProject(id);
    }


    //Custom Scripts Below//
    @GetMapping("/getEmails/{id}")
    public ResponseEntity<List<String>> getEmails(@PathVariable("id") Long id)
    {
        List<String> emails = new ArrayList<>();

        Optional<Project> foundProject = adminService.findProjectById(id);
        if (foundProject.isEmpty()) throw new EntityNotFoundException("Project not found with id = " + id.toString());
        else
        {
            Project existingProject = foundProject.get();
            emails.add(existingProject.getProposerEmail());
            emails.add(existingProject.getCoordinatorEmail());
            emails.add(existingProject.getProjectContactEmail());
            List<Long> assignedStudents = existingProject.getAssignedStudents();

            for (Long assignedStudent : assignedStudents) {
                Optional<Student> foundStudent = adminService.findStudentById(assignedStudent);
                if (foundStudent.isEmpty() && assignedStudent != 0)
                    throw new EntityNotFoundException("Student not found with id = " + id.toString());
                else if(assignedStudent != 0)
                {
                    Student existingStudent = foundStudent.get();
                    emails.add(existingStudent.getEmail());
                }
            }
        }
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

}
