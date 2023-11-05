package dev.capstone.asu.Capstone.Project.Admin.System.Controller;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.*;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.AdminRepo;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.ProjectRepo;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class AdminService {

    private final ProjectRepo projectRepo;

    private final StudentRepo studentRepo;

    private final AdminRepo adminRepo;

    @Autowired
    public AdminService(ProjectRepo projectRepo, StudentRepo studentRepo, AdminRepo adminRepo)
    {
        this.projectRepo = projectRepo;
        this.studentRepo = studentRepo;
        this.adminRepo = adminRepo;
    }


    // =====================================================
    //  STUDENT METHODS
    // =====================================================

    public Student addStudent(Student student)
    {
        return studentRepo.save(student);
    }

    public List<Student> findAllStudents()
    {
        return studentRepo.findAll();
    }

    public Student findStudentById(Long id)
    {
        Optional<Student> foundStudent = studentRepo.findById(id);
        if (foundStudent.isEmpty()) throw new EntityNotFoundException("Student not found with id = " + id.toString());
        return foundStudent.get();
    }

    public Student updateStudent(Long id, Student student)
    {
        Student foundStudent = this.findStudentById(id);
        if (!foundStudent.getId().equals(student.getId()))
            throw new InputMismatchException("Path variable id = '"
                    + id.toString()
                    + "' does not match student object id = '"
                    + student.getId().toString()
                    + "'");
        return studentRepo.save(student);
    }

    public void deleteStudent(Long id)
    {
        Optional<Student> toDelete = studentRepo.findById(id);
        Long assignedProjectID;
        if(toDelete.isPresent()) {
            Student student = toDelete.get();
            assignedProjectID = student.getAssignedProject();

            if(assignedProjectID != 0) {
                Optional<Project> toUpdate = projectRepo.findById(assignedProjectID);

                if (toUpdate.isPresent()) {
                    Project project = toUpdate.get();
                    List<Long> assignedStudents = project.getAssignedStudents();
                    assignedStudents.remove(id);
                    project.setAssignedStudents(assignedStudents);
                }
            }
        }
        studentRepo.deleteById(id);
    }

    public void deleteAllStudents()
    {
        studentRepo.deleteAll();
    }

    public Long studentLogin(List<String> userCredentials)
    {
        Optional<Student> student;
        student = studentRepo.findByASURite(userCredentials.get(0));

        if(student.isPresent())
        {
            Student realStudent = student.get();

            if(realStudent.getStudentID().toString().equals(userCredentials.get(1)))
            {
                return realStudent.getId();
            }
            else
            {
                throw new EntityNotFoundException("Student not found with credentials = " + userCredentials.toString());
            }
        }
        else
        {
            throw new EntityNotFoundException("Student not found with credentials = " + userCredentials.toString());
        }
    }



    // =====================================================
    //  PROJECT METHODS
    // =====================================================

    public Project addProject(Project project)
    {
        return projectRepo.save(project);
    }

    public Project updateProject(Long id, Project project)
    {
        Project foundProject = this.findProjectById(id);
        if (!foundProject.getId().equals(project.getId()))
            throw new InputMismatchException("Path variable id = '"
                    + id.toString()
                    + "' does not match project object id = '"
                    + project.getId().toString()
                    + "'");
        return projectRepo.save(project);
    }

    public List<Project> findAllProjects()
    {
        return projectRepo.findAll();
    }

    public Project findProjectById(Long id)
    {
        Optional<Project> foundProject = projectRepo.findById(id);
        if (foundProject.isEmpty()) throw new EntityNotFoundException("Project not found with id = " + id.toString());
        return foundProject.get();
    }

    public List<Project> findProjectsByYear(String year)
    {
        return projectRepo.findAllByYear(year);
    }

    public void deleteProject(Long id)
    {
        Optional<Project> toDelete = projectRepo.findById(id);
        if(toDelete.isPresent())
        {
            Project project = toDelete.get();
            List<Long> AssignedStudents = project.getAssignedStudents();

            for(Long studentID : AssignedStudents)
            {
                Optional<Student> toUpdate = studentRepo.findById(studentID);
                if(toUpdate.isPresent())
                {
                    Student student = toUpdate.get();
                    student.setAssignedProject(0L);
                }
            }
        }
        projectRepo.deleteById(id);
    }

    public void deleteAllProjects()
    {
        projectRepo.deleteAll();
    }



    // =====================================================
    //  ADMIN METHODS
    // =====================================================

    public Admin addAdmin(Admin admin)
    {
        return adminRepo.save(admin);
    }

    public List<Admin> findAllAdmin()
    {
        return adminRepo.findAll();
    }

    public Admin findAdminById(Long id)
    {
        Optional<Admin> foundAdmin = adminRepo.findById(id);
        if (foundAdmin.isEmpty()) throw new EntityNotFoundException("Admin not found with id = " + id.toString());
        return foundAdmin.get();
    }

    public Admin updateAdmin(Long id, Admin admin)
    {
        Admin foundAdmin = this.findAdminById(id);
        if (!foundAdmin.getId().equals(admin.getId()))
            throw new InputMismatchException("Path variable id = '"
                    + id.toString()
                    + "' does not match admin object id = '"
                    + admin.getId().toString()
                    + "'");
        return adminRepo.save(admin);
    }

    public void deleteAdmin(Long id)
    {
        adminRepo.deleteById(id);
    }

    public Long adminLogin(List<String> adminCredentials)
    {
        Optional<Admin> admin;
        admin = adminRepo.findByEmail(adminCredentials.get(0));

        if(admin.isPresent())
        {
            Admin realAdmin = admin.get();

            if(realAdmin.getPassword().equals(adminCredentials.get(1)))
            {
                return realAdmin.getId();
            }
            else
            {
                throw new EntityNotFoundException("Admin not found with credentials = " + adminCredentials.toString());
            }
        }
        else
        {
            throw new EntityNotFoundException("Admin not found with credentials = " + adminCredentials.toString());
        }
    }


    // =====================================================
    //  SCRIPT METHODS
    // =====================================================


    public List<String> getEmailsByProject(Long id)
    {
        Project foundProject = this.findProjectById(id);

        List<String> emails = new ArrayList<>();

        emails.add(foundProject.getProposerEmail());
        emails.add(foundProject.getProjectContactEmail());
        emails.add(foundProject.getCoordinatorEmail());

        List<Long> assignedStudents = foundProject.getAssignedStudents();
        Student tempStudent;
        for(Long assignedStudent : assignedStudents)
        {
            tempStudent = null;
            try
            {
                tempStudent = this.findStudentById(assignedStudent);
            }
            catch (EntityNotFoundException ex)
            {
                System.out.println(ex.getMessage());
            }
            if (!Objects.isNull(tempStudent))
            {
                emails.add(tempStudent.getEmail());
            }
        }

        return emails;
    }

    public Student projectSignup(Long id, List<Long> projectIds)
    {
        Student foundStudent = this.findStudentById(id);
        foundStudent.setProjectPreferences(projectIds);
        foundStudent.setSignupTimestamp(Instant.now());
        return studentRepo.save(foundStudent);
    }

    public void assignProjects()
    {
        // Fetch and sort all student objects, fetch project objects
        ArrayList<Student> allStudents = new ArrayList<>(this.findAllStudents());
        allStudents.sort(new StudentTimestampComparator());
        ArrayList<Project> allProjects = new ArrayList<>(this.findAllProjects());

        // Temporary storage
        List<Student> unassignedStudents = new ArrayList<>();
        List<Student> assignedStudents = new ArrayList<>();
        List<Project> fullProjects = new ArrayList<>();

        // Temporary variables
        Project tempProject;
        Student tempStudent;

        // Print all sorted student objects to standard output
        for (Student student : allStudents)
        {
            System.out.println("Student: " + student.toString());
        }

        // Assign all students with a signup timestamp
        while (!allStudents.isEmpty() && !allProjects.isEmpty())
        {
            // If no signup timestamp, move to unassigned array
            tempStudent = allStudents.get(0);
            if (Objects.isNull(tempStudent.getSignupTimestamp()))
            {
                unassignedStudents.add(tempStudent);
                allStudents.remove(tempStudent);
                continue;
            }

            // Search project preferences for first available project
            for (Long projectId : tempStudent.getProjectPreferences())
            {
                tempProject = null;
                for (Project p : allProjects)
                {
                    if (projectId.equals(p.getId()))
                    {
                        tempProject = p;
                        break;
                    }
                }
                if (Objects.isNull(tempProject)) continue;
                if (!tempProject.atCapacity())
                {
                    tempProject.addAssignedStudent(tempStudent.getId());
                    tempStudent.setAssignedProject(projectId);
                    assignedStudents.add(tempStudent);
                    allStudents.remove(tempStudent);
                    if (tempProject.atCapacity())
                    {
                        fullProjects.add(tempProject);
                        allProjects.remove(tempProject);
                    }
                    break;
                }
            }

            // If no preferred project available, move to unassigned array
            if (allStudents.contains(tempStudent))
            {
                unassignedStudents.add(tempStudent);
                allStudents.remove(tempStudent);
            }
        }

        // If there are no more available projects, move remaining students to unassigned array
        unassignedStudents.addAll(allStudents);
        System.out.println("After assigning students with timestamps:");
        for (Student s : unassignedStudents)
        {
            System.out.println("Unassigned Student: " + s.toString());
        }
        allStudents.clear();

        // Assign all remaining students with no timestamp
        //  or with no available preferred projects
        while (!unassignedStudents.isEmpty() && !allProjects.isEmpty())
        {
            tempStudent = unassignedStudents.get(0);
            tempProject = allProjects.get(0);

            // Find first available project
            if (tempProject.atCapacity())
            {
                fullProjects.add(tempProject);
                allProjects.remove(tempProject);
                continue;
            }

            tempProject.addAssignedStudent(tempStudent.getId());
            tempStudent.setAssignedProject(tempProject.getId());
            assignedStudents.add(tempStudent);
            unassignedStudents.remove(tempStudent);
        }

        // Merge all temporary storage back into original arrays
        allStudents.addAll(assignedStudents);
        allStudents.addAll(unassignedStudents);
        allProjects.addAll(fullProjects);

        // Write changes to database
        studentRepo.saveAll(allStudents);
        projectRepo.saveAll(allProjects);
    }

    public void clearAssignedProjects()
    {
        List<Student> students = studentRepo.findAll();
        List<Project> projects = projectRepo.findAll();

        for (Student student : students)
        {
            student.setAssignedProject(0L);
        }

        for (Project project : projects)
        {
            project.setAssignedStudents(new ArrayList<Long>());
        }

        studentRepo.saveAll(students);
        projectRepo.saveAll(projects);
    }

    public boolean hasCsvFormat(MultipartFile file) {
        return "text/csv".equals(file.getContentType());
    }

    public ResponseMessage processStudentsCsv(MultipartFile file) throws IOException {
        List<Student> students = null;
        try
        {
            students = this.csvToStudents(file.getInputStream());
        }
        catch (UnsupportedEncodingException ex)
        {
            return ResponseMessage.build("Students .csv file is not coded using UTF-8");
        }
        catch (IOException ex)
        {
            return ResponseMessage.build("Failed to retrieve input stream from students .csv file");
        }
        if (Objects.isNull(students))
            return ResponseMessage.build("Failed to parse students .csv file");
        return ResponseMessage.build("Student information added to database", studentRepo.saveAll(students));
    }

    public ResponseMessage processProjectsCsv(MultipartFile file) {
        List<Project> projects = null;
        try
        {
            projects = this.csvToProjects(file.getInputStream());
        }
        catch (UnsupportedEncodingException ex)
        {
            return ResponseMessage.build("Projects .csv file is not coded using UTF-8");
        }
        catch (IOException ex)
        {
            return ResponseMessage.build("Failed to retrieve input stream from projects .csv file");
        }

        if (Objects.isNull(projects))
            return ResponseMessage.build("Failed to parse projects .csv file");

        return ResponseMessage.build("Project information added to database", projectRepo.saveAll(projects));
    }

    private List<CSVRecord> inputStreamToCsvRecords(InputStream stream, Charset charset, CSVFormat format)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
        CSVParser parser = null;
        try
        {
            parser = new CSVParser(reader, format);
        }
        catch (IOException ex)
        {
            System.out.println("Error creating parser for .csv:");
            System.out.println(ex.getMessage());
            System.out.println(ex.getLocalizedMessage());
        }

        if (Objects.isNull(parser)) return null;
        return parser.getRecords();
    }

    private List<Project> csvToProjects(InputStream stream) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setDelimiter(',')
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
        List<CSVRecord> records = this.inputStreamToCsvRecords(stream, StandardCharsets.UTF_8, format);
        if (Objects.isNull(records)) return null;
        List<Project> projects = new ArrayList<>();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("M/d/yyyy H:mm:ss", Locale.US)
                        .withZone(ZoneId.from(ZoneOffset.UTC));
        for (CSVRecord record : records)
        {
            Project project = new Project();

            ZonedDateTime zdt = ZonedDateTime.parse(record.get(0), formatter);
            Instant timestamp = zdt.toInstant();
            project.setTimestamp(timestamp.toString());
            project.setCohort(record.get(1));
            project.setOrganizationClassification(record.get(2));
            project.setIntellectualPropertyConcerns(record.get(3));
            project.setProjectResourcesProvided(record.get(4));
            project.setDedicatedContact(record.get(5));
            project.setProposerOrganization(record.get(6));
            project.setProposerName(record.get(7));
            project.setProposerEmail(record.get(8));
            project.setProjectContactName(record.get(9));
            project.setProjectContactEmail(record.get(10));
            project.setTitle(record.get(11));
            project.setDescription(record.get(12));
            project.setStudentLearningExperience(record.get(13));
            project.setExpectedDeliverables(record.get(14));
            project.setDesiredBackground(record.get(15));
            project.setProjectFocus(record.get(16));
            project.setMaxTeamSize(Integer.parseInt(record.get(17)));
            project.setRequiredAgreements(record.get(18));
            project.setProjectLinks(record.get(19));
            project.setCoordinatorName("");
            project.setCoordinatorEmail("");
            project.setAssignedStudents(new ArrayList<Long>());

            projects.add(project);
        }
        return projects;
    }

    private List<Student> csvToStudents(InputStream inputStream) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setDelimiter(',')
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();
        List<CSVRecord> records = this.inputStreamToCsvRecords(inputStream, StandardCharsets.UTF_8, format);

        if (Objects.isNull(records)) return null;

        List<Student> students = new ArrayList<>();
        for (CSVRecord record : records)
        {
            System.out.println(record.toList().toString());

            Student student = new Student();
            student.setStudentID(Long.parseLong(record.get(0)));
            student.setFirstName(record.get(1));
            student.setLastName(record.get(2));
            student.setAsuriteID(record.get(3));
            student.setEmail(record.get(4));
            student.setSignupTimestamp(null);
            student.setProjectPreferences(new ArrayList<Long>());
            student.setAssignedProject(0L);
            students.add(student);

        }
        return students;
    }

    public List<Student> randomProjectSignup()
    {
        List<Project> projects = projectRepo.findAll();
        List<Student> students = studentRepo.findAll();

        if (projects.isEmpty() || students.isEmpty()) return null;

        int pSize = projects.size();
        int cap = 80;
        int rand;
        int proj;
        Project temp;

        for (Student student : students)
        {
            rand = (int)(Math.random() * 100);
            if (rand > cap) continue;
            while (student.getProjectPreferences().size() < 10)
            {
                proj = (int)(Math.random() * pSize);
                temp = projects.get(proj);
                student.addProjectPreference(temp.getId());
            }
            student.setSignupTimestamp(Instant.now());
        }

        return studentRepo.saveAll(students);
    }

    public List<Student> clearProjectPreferences()
    {
        List<Student> students = studentRepo.findAll();

        for (Student student : students)
        {
            student.setProjectPreferences(new ArrayList<Long>());
            student.setSignupTimestamp(null);
        }

        return studentRepo.saveAll(students);
    }

}
