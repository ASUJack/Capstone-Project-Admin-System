package dev.capstone.asu.Capstone.Project.Admin.System.Repository;

import dev.capstone.asu.Capstone.Project.Admin.System.Entity.Admin;
import dev.capstone.asu.Capstone.Project.Admin.System.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin, Long> {

    default Optional<Admin> findByEmail(String asuEmail)
    {
        for (Admin admin: this.findAll())
        {
            if(admin.getAsuEmail().equals(asuEmail))
            {
                return Optional.of(admin);
            }
        }

        return Optional.empty();
    }

}
