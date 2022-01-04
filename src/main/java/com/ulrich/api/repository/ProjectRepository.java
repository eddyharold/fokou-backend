package com.ulrich.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ulrich.api.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{

}
