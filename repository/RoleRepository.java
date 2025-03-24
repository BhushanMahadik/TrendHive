package com.TrendHive.TrendHive.repository;

import com.TrendHive.TrendHive.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
    Set<Role> findByNameIn(Set<String> name);
}
