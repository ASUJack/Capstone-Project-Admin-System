package dev.capstone.asu.Capstone.Project.Admin.System.Students;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Arrays;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @Column(length = 64)
    private String signupTimestamp;

    private long asuriteID;

    private long[] projectPreferences;

    private long assignedProject;


    public Long getId() {
        return id;
    }

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

    public long getAsuriteID() {
        return asuriteID;
    }

    public long[] getProjectPreferences() {
        return projectPreferences;
    }

    public long getAssignedProject() {
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

    public void setAsuriteID(long asuriteID) {
        this.asuriteID = asuriteID;
    }

    public void setProjectPreferences(long[] projectPreferences) {
        this.projectPreferences = projectPreferences;
    }

    public void setAssignedProject(long assignedProject) {
        this.assignedProject = assignedProject;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", signupTimestamp='" + signupTimestamp + '\'' +
                ", asuriteID=" + asuriteID +
                ", projectPreferences=" + Arrays.toString(projectPreferences) +
                ", assignedProject=" + assignedProject +
                '}';
    }
}
