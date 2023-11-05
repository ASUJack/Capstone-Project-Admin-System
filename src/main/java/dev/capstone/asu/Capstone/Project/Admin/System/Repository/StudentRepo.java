package dev.capstone.asu.Capstone.Project.Admin.System.Repository;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long>
{
    @Query(value = "SELECT * FROM Student s WHERE s.asuriteid = :asurite", nativeQuery = true)
    Optional<Student> findByASURite(@Param("asurite") String asurite);

    @Query(value = "SELECT * FROM Student s WHERE s.assigned_project = 0", nativeQuery = true)
    Collection<Student> findByUnassigned();

    @Query(value = "SELECT * FROM Student s WHERE s.assigned_project = :projectid", nativeQuery = true)
    Collection<Student> findByAssigned(@Param("projectid") Long projectid);
}
