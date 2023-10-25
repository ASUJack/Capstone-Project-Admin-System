package dev.capstone.asu.Capstone.Project.Admin.System.Controller;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.*;
import dev.capstone.asu.Capstone.Project.Admin.System.ExceptionHandler.AdminApiError;
import dev.capstone.asu.Capstone.Project.Admin.System.ExceptionHandler.AdminApiRequestError;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
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


    // =====================================================
    //  STUDENT METHODS
    // =====================================================

    @GetMapping("/allStudents")
    public ResponseEntity<List<Student>> findAllStudents()
    {
        List<Student> students = adminService.findAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/findStudent/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long id)
    {
        Student student = adminService.findStudentById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PostMapping("/addStudent")
    public ResponseEntity<Student> addStudent(@RequestBody Student student)
    {
        Student newStudent = adminService.addStudent(student);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long id, @RequestBody Student student) {
        Student foundStudent = adminService.updateStudent(id, student);
        return new ResponseEntity<>(foundStudent, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id)
    {
        adminService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // =====================================================
    //  PROJECT METHODS
    // =====================================================

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
        Project foundProject = adminService.findProjectById(id);
        return new ResponseEntity<>(foundProject, HttpStatus.OK);
    }

    @PutMapping("/updateProject/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Long id, @RequestBody Project project)
    {
        Project foundProject = adminService.updateProject(id, project);
        return new ResponseEntity<>(foundProject, HttpStatus.OK);
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


    // =====================================================
    //  SCRIPT METHODS
    // =====================================================

    @GetMapping("/getEmails/{id}")
    public ResponseEntity<List<String>> getEmails(@PathVariable("id") Long id)
    {
        List<String> emails = adminService.getEmailsByProject(id);
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @PutMapping("/projectSignup/{id}")
    public ResponseEntity<Student> projectSignup(@PathVariable("id") Long id, @RequestBody List<Long> projectIds)
    {
        Student foundStudent = adminService.projectSignup(id, projectIds);
        return new ResponseEntity<>(foundStudent, HttpStatus.OK);
    }

    @PutMapping("/assignProjects")
    public ResponseEntity<?> assignProjects()
    {
        adminService.assignProjects();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/clearAssignedProjects")
    public ResponseEntity<?> clearAssignedProjects()
    {
        adminService.clearAssignedProjects();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@RequestMapping(value = "/studentsCsv", method = RequestMethod.POST, consumes = "multipart/form-data")
    @PostMapping("/studentsCsv")
    public ResponseEntity<?> studentsCsv(@RequestParam("file") MultipartFile file) throws IOException
    {
        if (!adminService.hasCsvFormat(file))
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);

        adminService.processStudentsCsv(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
