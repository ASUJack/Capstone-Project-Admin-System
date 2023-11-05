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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @DeleteMapping("/deleteAllStudents")
    public ResponseEntity<ResponseMessage> deleteAllStudents()
    {
        adminService.deleteAllStudents();
        ResponseMessage msg = ResponseMessage.build("All student data deleted from database");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/studentLogin")
    public ResponseEntity<Long> studentLogin(@RequestBody List<String> userCredentials)
    {
        Long id = adminService.studentLogin(userCredentials);
        return new ResponseEntity<>(id, HttpStatus.OK);
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

    @DeleteMapping("/deleteAllProjects")
    public ResponseEntity<ResponseMessage> deleteAllProjects()
    {
        adminService.deleteAllProjects();
        ResponseMessage msg = ResponseMessage.build("All project information deleted from database");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }


    // =====================================================
    //  ADMIN METHODS
    // =====================================================

    @GetMapping("/allAdmins")
    public ResponseEntity<List<Admin>> findAllAdmins()
    {
        List<Admin> admins = adminService.findAllAdmin();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    @GetMapping("/findAdmin/{id}")
    public ResponseEntity<Admin> getAdmin(@PathVariable("id") Long id)
    {
        Admin admin = adminService.findAdminById(id);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<Admin> addAdmin(@RequestBody Admin admin)
    {
        Admin newAdmin = adminService.addAdmin(admin);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    @PutMapping("/updateAdmin/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable("id") Long id, @RequestBody Admin admin) {
        Admin foundAdmin = adminService.updateAdmin(id, admin);
        return new ResponseEntity<>(foundAdmin, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteAdmin/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") Long id)
    {
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/adminLogin")
    public ResponseEntity<Long> adminLogin(@RequestBody List<String> adminCredentials)
    {
        Long id = adminService.adminLogin(adminCredentials);
        return new ResponseEntity<>(id, HttpStatus.OK);
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

    @PostMapping("/studentsCsv")
    public ResponseEntity<ResponseMessage> studentsCsv(@RequestParam("file") MultipartFile file) throws IOException
    {
        if (!adminService.hasCsvFormat(file))
        {
            ResponseMessage msg = ResponseMessage.build("Uploaded file is not .csv");
            return new ResponseEntity<>(msg, HttpStatus.EXPECTATION_FAILED);
        }

        ResponseMessage msg = adminService.processStudentsCsv(file);
        if (Objects.isNull(msg.getEntity()))
        {
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/projectsCsv")
    public ResponseEntity<ResponseMessage> projectsCsv(@RequestParam("file") MultipartFile file)
    {
        if (!adminService.hasCsvFormat(file))
        {
            ResponseMessage msg = ResponseMessage.build("Uploaded file is not .csv");
            return new ResponseEntity<>(msg, HttpStatus.EXPECTATION_FAILED);
        }

        ResponseMessage msg = adminService.processProjectsCsv(file);
        if (Objects.isNull(msg.getEntity()))
        {
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/randomProjectSignup")
    public ResponseEntity<ResponseMessage> randomProjectSignup()
    {
        List<Student> students = adminService.randomProjectSignup();
        if (Objects.isNull(students))
        {
            ResponseMessage msg = ResponseMessage.build("No projects assigned");
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseMessage msg = ResponseMessage.build("Projects assigned", students);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/clearProjectPreferences")
    public ResponseEntity<ResponseMessage> clearProjectPreferences()
    {
        List<Student> students = adminService.clearProjectPreferences();
        ResponseMessage msg = ResponseMessage.build("Project preferences cleared", students);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
