package com.airlock.iam.scim.jpa.repository;

import com.airlock.iam.scim.jpa.entity.MedusaUser;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedusaUserRepository extends JpaRepository<MedusaUser, Long> {
    @Nullable
    MedusaUser getMedusaUserByUserName(String userName);

    boolean existsMedusaUserByUserName(String userName);

    void deleteMedusaUserByUserName(String id);
}
