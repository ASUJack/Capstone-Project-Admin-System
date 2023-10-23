package dev.capstone.asu.Capstone.Project.Admin.System.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 16, unique = true)
    private String asuriteID;

    private String firstName;

    private String lastName;

    private String email;

    @Column(length = 64)
    private String signupTimestamp;

    private List<Long> projectPreferences;

    @OneToOne(targetEntity = Project.class)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project assignedProject;

    public Long getId() { return id; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getSignupTimestamp() { return signupTimestamp; }

    public String getAsuriteID() {
        return asuriteID;
    }

    public List<Long> getProjectPreferences() {
        return projectPreferences;
    }

    public Project getAssignedProject() {
        return assignedProject;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSignupTimestamp(String signupTimestamp) { this.signupTimestamp = signupTimestamp; }

    public void setAsuriteID(String asuriteID) {
        this.asuriteID = asuriteID;
    }

    public void setProjectPreferences(List<Long> projectPreferences) {
        this.projectPreferences = projectPreferences;
    }

    public void setAssignedProject(Project assignedProject) {
        this.assignedProject = assignedProject;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", asuriteID='" + asuriteID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", signupTimestamp='" + signupTimestamp + '\'' +
                ", projectPreferences=" + projectPreferences +
                ", assignedProject=" + assignedProject +
                '}';
    }
}
