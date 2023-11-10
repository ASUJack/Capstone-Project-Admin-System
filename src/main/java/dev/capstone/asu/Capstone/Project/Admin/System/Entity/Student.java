package dev.capstone.asu.Capstone.Project.Admin.System.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentID;

    private String firstName;

    private String lastName;

    @Column(length = 16, unique = true)
    private String asuriteID;

    private String email;

    private Instant signupTimestamp;

    private List<Long> projectPreferences;

    private Long assignedProject;

    public Long getId() {
        return id;
    }

    public Long getStudentID() {
        return studentID;
    }

    public void setStudentID(Long studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAsuriteID() {
        return asuriteID;
    }

    public void setAsuriteID(String asuriteID) {
        this.asuriteID = asuriteID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getSignupTimestamp() {
        return signupTimestamp;
    }

    public void setSignupTimestamp(Instant signupTimestamp) {
        this.signupTimestamp = signupTimestamp;
    }

    public List<Long> getProjectPreferences() {
        return projectPreferences;
    }

    public void setProjectPreferences(List<Long> projectPreferences) {
        this.projectPreferences = projectPreferences;
    }

    public Long getAssignedProject() {
        return assignedProject;
    }

    public void setAssignedProject(Long assignedProject) {
        this.assignedProject = assignedProject;
    }

    public boolean addProjectPreference(Long projectId)
    {
        if (this.projectPreferences.contains(projectId) || this.projectPreferences.size() >= 10)
        {
            return false;
        }
        this.projectPreferences.add(projectId);
        return true;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentID=" + studentID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", asuriteID='" + asuriteID + '\'' +
                ", email='" + email + '\'' +
                ", signupTimestamp=" + signupTimestamp +
                ", projectPreferences=" + projectPreferences +
                ", assignedProject=" + assignedProject +
                '}';
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Student s)) return false;
        return this.getId().equals(s.getId());
    }
}
