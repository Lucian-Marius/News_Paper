package it.aulab.news_paper.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.news_paper.Models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
