package dev.capstone.asu.Capstone.Project.Admin.System.Controller;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.*;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.AdminRepo;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.ProjectRepo;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.StudentRepo;
import dev.capstone.asu.Capstone.Project.Admin.System.Repository.ProjectAssignmentsToSendRepo;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
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

    private final ProjectAssignmentsToSendRepo projectAssignmentsToSendRepo;

    @Autowired
    public AdminService(ProjectRepo projectRepo, StudentRepo studentRepo, AdminRepo adminRepo, ProjectAssignmentsToSendRepo projectAssignmentsToSendRepo)
    {
        this.projectRepo = projectRepo;
        this.studentRepo = studentRepo;
        this.adminRepo = adminRepo;
        this.projectAssignmentsToSendRepo = projectAssignmentsToSendRepo;
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

    public List<String> studentLogin(List<String> userCredentials)
    {
        List<String> info = new ArrayList<>();
        Optional<Student> student;
        student = studentRepo.findByASURite(userCredentials.get(0));

        if(student.isPresent())
        {
            Student realStudent = student.get();

            if(realStudent.getStudentID().toString().equals(userCredentials.get(1)))
            {
                info.add(realStudent.getId().toString());
                info.add(realStudent.getFirstName());
                return info;
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

    public List<Student> unassignedStudents()
    {
        return (List<Student>) studentRepo.findByUnassigned();
    }



    // =====================================================
    //  PROJECT METHODS
    // =====================================================

    public Project addProject(Project project)
    {
        DateTimeFormatter formatter =
                DateTimeFormatter
                        .ofPattern("yyyy-MM-dd hh:mm:ss")
                        .withZone(ZoneId.from(ZoneOffset.UTC));
        project.setTimestamp(formatter.format(Instant.now()));
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

    public List<String> adminLogin(List<String> adminCredentials)
    {
        Optional<Admin> admin;
        List<String> info = new ArrayList<>();
        admin = adminRepo.findByEmail(adminCredentials.get(0));

        if(admin.isPresent())
        {
            Admin realAdmin = admin.get();

            if(realAdmin.getPassword().equals(adminCredentials.get(1)))
            {
                info.add(realAdmin.getId().toString());
                info.add(realAdmin.getFirstName());
                return info;
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
        ArrayList<Student> unassignedStudents = new ArrayList<>(studentRepo.findByUnassigned());
        unassignedStudents.sort(new StudentTimestampComparator());
        ArrayList<Project> assignableProjects = new ArrayList<>(projectRepo.findByAssignable());

        // Temporary storage
        List<Student> noPreferenceStudents = new ArrayList<>();
        List<Integer> hasProposedTeam = new ArrayList<>();

        // Find projects with proposed teams
        ArrayList<ArrayList<Student>> studentAssignments = new ArrayList<>(assignableProjects.size());
        for (int i = 0; i < assignableProjects.size(); ++i)
        {
            studentAssignments.add(new ArrayList<>());
            List<String> asuriteIds = assignableProjects.get(i).getProposedTeamAsuriteIds();
            if (!Objects.isNull(asuriteIds) && !asuriteIds.isEmpty())
            {
                hasProposedTeam.add(i);
            }
        }

        // Assign proposed teams
        for (Integer index : hasProposedTeam)
        {
            Project p = assignableProjects.get(index);
            for (String asurite : p.getProposedTeamAsuriteIds())
            {
                Student s = unassignedStudents.stream()
                        .filter(student -> asurite.equals(student.getAsuriteID()))
                        .findFirst()
                        .orElse(null);
                if (!Objects.isNull(s))
                {
                    studentAssignments.get(index).add(s);
                    unassignedStudents.remove(s);
                }
            }
        }

        // Temporary Variables
        Student tempStudent;
        Project tempProject;

        // Assign all students with a signup timestamp
        while (!unassignedStudents.isEmpty() && !assignableProjects.isEmpty())
        {
            // If no signup timestamp, move to noPreference array
            tempStudent = unassignedStudents.get(0);
            if (Objects.isNull(tempStudent.getSignupTimestamp()))
            {
                noPreferenceStudents.add(tempStudent);
                unassignedStudents.remove(tempStudent);
                continue;
            }

            // Search project preferences for first available project
            for (Long projectId : tempStudent.getProjectPreferences())
            {
                tempProject = assignableProjects.stream()
                        .filter(project -> projectId.equals(project.getId()))
                        .findFirst()
                        .orElse(null);
                if (Objects.isNull(tempProject)) continue;
                int index = assignableProjects.indexOf(tempProject);
                if (studentAssignments.get(index).size() < tempProject.getMaxTeamSize())
                {
                    studentAssignments.get(index).add(tempStudent);
                    unassignedStudents.remove(tempStudent);
                    break;
                }
            }

            if (unassignedStudents.contains(tempStudent))
            {
                noPreferenceStudents.add(tempStudent);
                unassignedStudents.remove(tempStudent);
            }
        }

        // If there are no more available projects, move remaining students to noPreference array
        noPreferenceStudents.addAll(unassignedStudents);
        unassignedStudents.clear();

        // Assign remaining students to projects with only one assigned student
        boolean madeChanges = true;
        while (!noPreferenceStudents.isEmpty() && madeChanges)
        {
            madeChanges = false;
            for (int i = 0; i < assignableProjects.size(); ++i)
            {
                if (studentAssignments.get(i).size() == 1)
                {
                    tempStudent = noPreferenceStudents.get(0);
                    studentAssignments.get(i).add(tempStudent);
                    noPreferenceStudents.remove(tempStudent);
                    madeChanges = true;
                    if (noPreferenceStudents.isEmpty()) break;
                }
            }
        }

        madeChanges = true;
        while (!noPreferenceStudents.isEmpty() && madeChanges)
        {
            madeChanges = false;
            for (int i = 0; i < assignableProjects.size(); ++i)
            {
                while (!studentAssignments.get(i).isEmpty() &&
                        studentAssignments.get(i).size() < assignableProjects.get(i).getMaxTeamSize() &&
                        !noPreferenceStudents.isEmpty())
                {
                    tempStudent = noPreferenceStudents.get(0);
                    studentAssignments.get(i).add(tempStudent);
                    noPreferenceStudents.remove(tempStudent);
                    madeChanges = true;
                }
            }
        }

        // Collect all projects with only one student assigned
        List<Integer> onlyOne = new ArrayList<>();
        for (int i = 0; i < assignableProjects.size(); ++i)
        {
            if (studentAssignments.get(i).size() == 1)
                onlyOne.add(i);
        }

        // Consolidate assignments with only one student
        if (onlyOne.size() > 1)
        {
            for (int i = 0; i < onlyOne.size(); i += 2)
            {
                tempStudent = studentAssignments.get(onlyOne.get(i + 1)).get(0);
                studentAssignments.get(onlyOne.get(i + 1)).remove(tempStudent);
                studentAssignments.get(onlyOne.get(i)).add(tempStudent);
            }
        }
        if (onlyOne.size() == 1)
        {
            ArrayList<Student> list = null;
            for (int i = 0; i < assignableProjects.size(); ++i)
            {
                if (studentAssignments.get(i).size() > 1 && studentAssignments.get(i).size() < assignableProjects.get(i).getMaxTeamSize())
                {
                    list = studentAssignments.get(i);
                    break;
                }
            }
            if (Objects.isNull(list))
            {
                tempStudent = studentAssignments.get(onlyOne.get(0)).get(0);
                studentAssignments.get(onlyOne.get(0)).remove(tempStudent);
                unassignedStudents.add(tempStudent);
            }
            else
            {
                tempStudent = studentAssignments.get(onlyOne.get(0)).get(0);
                studentAssignments.get(onlyOne.get(0)).remove(tempStudent);
                list.add(tempStudent);
            }
        }


        //Make sure we have a valid ProjectAssignmentsToSendRepo entity to save IDs.
        List<ProjectAssignmentsToSend> assignmentsToSendList = projectAssignmentsToSendRepo.findAll();
        ProjectAssignmentsToSend assignmentList;

        if(assignmentsToSendList.isEmpty())
        {
            ProjectAssignmentsToSend assignmentsToSend = new ProjectAssignmentsToSend();
            List<Long> listOfAssignments = new ArrayList<Long>();
            assignmentsToSend.setNewlyAssignedProjects(listOfAssignments);
            projectAssignmentsToSendRepo.save(assignmentsToSend);
        }

        assignmentsToSendList = projectAssignmentsToSendRepo.findAll();
        assignmentList = assignmentsToSendList.get(0);


        // Update Project & Student objects with assignments
        for (int i = 0; i < assignableProjects.size(); ++i)
        {
            tempProject = assignableProjects.get(i);
            ArrayList<Student> list = studentAssignments.get(i);

            //Add Project ID to the projectAssignmentsToSend Object.
            assignmentList.getNewlyAssignedProjects().add(tempProject.getId());

            for (Student student : list)
            {
                tempProject.addAssignedStudent(student.getId());
                student.setAssignedProject(tempProject.getId());
                unassignedStudents.add(student);
            }
        }

        // Merge temporary storage back into original array
        unassignedStudents.addAll(noPreferenceStudents);

        // Write changes to database
        studentRepo.saveAll(unassignedStudents);
        projectRepo.saveAll(assignableProjects);
        projectAssignmentsToSendRepo.save(assignmentList);
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


    public void sendEmail(List<String> emailParts) throws EmailException      //emailParts: [senderEmail, senderPassword, EmailSubject, EmailBody, receiverEmail1, ... ]
    {
        Email email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(emailParts.get(0), emailParts.get(1)));
        email.setSSLOnConnect(true);
        email.setFrom(emailParts.get(0));
        email.setSubject(emailParts.get(2));
        email.setMsg(emailParts.get(3));

        for(int i = 4; i < emailParts.size(); i++)
        {
            email.addTo(emailParts.get(i));
        }

        email.send();

    }

    public void sendProjectAssignments(List<String> accountCredentials) throws EmailException       //accountCredentials: [senderEmail, senderPassword]
    {
        List<ProjectAssignmentsToSend> assignmentsToSendList = projectAssignmentsToSendRepo.findAll();

        if (!assignmentsToSendList.isEmpty()) {
            ProjectAssignmentsToSend assignmentList = assignmentsToSendList.get(0);
            String body = "";
            String subject = "[Ignore] [Back-end Testing] Capstone Project Team Assignments";

            for (Long id : assignmentList.getNewlyAssignedProjects()) {
                Optional<Project> check = projectRepo.findById(id);
                Project project;
                List<String> associatedEmails = new ArrayList<String>();

                if (check.isPresent()) {
                    project = check.get();
                    List<String> teamEmails = new ArrayList<String>();
                    List<String> teamNames = new ArrayList<String>();
                    associatedEmails = this.getEmailsByProject(project.getId());

                    for (Long studentID : project.getAssignedStudents()) {
                        Optional<Student> checkStudent = studentRepo.findById(studentID);
                        if (checkStudent.isPresent()) {
                            String studentName = checkStudent.get().getFirstName() + " " + checkStudent.get().getLastName();
                            String studentEmail = checkStudent.get().getEmail();
                            teamNames.add(studentName);
                            teamEmails.add(studentEmail);
                        }
                    }

                    body = "I am pleased to inform you that your project '" + project.getTitle() + "' was selected as a capstone project this semester!";
                    body += "\nSponsor Email: " + project.getSponsorEmail();
                    body += "\nTeam Member Names: \n" + teamNames.toString();
                    body += "\n\nTeam Member Emails: \n" + teamEmails.toString();
                    body += "\n\n Students and sponsors should review the Capstone kickoff information here: https://sites.google.com/asu.edu/cidse-capstone/kickoff ";
                    body += "\n\n Students teams are expected to send an initial contact request to the sponsor.  Only one student from each team should initially contact the sponsor, so please coordinate within your team.";
                    body += "\n\nPlease let me know if you have any questions!\n\nThank you, \n-Ryan Meuth\n\n Associate Teaching Professor, Capstone Project Coordinator\nArizona State University.\n";

                    List<String> emailParts = new ArrayList<String>();

                    emailParts.add(accountCredentials.get(0));
                    emailParts.add(accountCredentials.get(1));
                    emailParts.add(subject);
                    emailParts.add(body);

                    for(String email : associatedEmails)
                    {
                        if(!email.isEmpty())
                        {
                            emailParts.add(email);
                        }
                    }

                    this.sendEmail(emailParts);

                }
            }

            List<Long> empty = new ArrayList<Long>();
            assignmentList.setNewlyAssignedProjects(empty);
        }

        return;
    }
}
