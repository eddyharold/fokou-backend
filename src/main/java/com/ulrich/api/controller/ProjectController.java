package com.ulrich.api.controller;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ulrich.api.model.Project;
import com.ulrich.api.repository.ProjectRepository;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

	private Log logger = LogFactory.getLog(ProjectController.class);

    @Autowired
    ProjectRepository projectRepository;
	
    
	//build create project rest api
	@PostMapping
	public ResponseEntity<?> save(@RequestBody Project project){
		Project _project = null;
        try {
        	_project = projectRepository.save(project);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(_project, HttpStatus.CREATED);
	}
	
	
	//build get all projects REST api
	@GetMapping
	public ResponseEntity<?> getAll(){
		return new ResponseEntity<>(projectRepository.findAll(), HttpStatus.OK);
	}
	
	
	//build get project by id
	@GetMapping("{id}")
	public ResponseEntity<?> get(@PathVariable("id") long projectId){
		Optional<Project> projectOptional =  Optional.empty();

        try {
        	projectOptional = projectRepository.findById(projectId);
            if (!projectOptional.isPresent()) {
                logger.info(String.format("Project: " + projectId + " not found in the database."));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<>(projectOptional.get(), HttpStatus.OK);
	}
	
	
	//build update project rest api
	@PutMapping("{id}")
	public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Project project){
		Project _project = null;
        try {
            Optional<Project> projetData = projectRepository.findById(id);

            if (projetData.isPresent()) {
            	_project = projetData.get();
            	_project.setName(project.getName());
            	_project.setDomain(project.getDomain());
            	_project.setDuration(project.getDuration());
            	_project.setFunding(project.getFunding());
            	_project.setDescription(project.getDescription());
            	_project = projectRepository.save(_project);
            } else {
                logger.info(String.format("Project: " + project.getName() + " not found in the database."));
                return new ResponseEntity<>(String.format("Project: " + project.getName() + " not found."), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(_project, HttpStatus.OK);
	}
	
	
	//build delete project rest api
	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id){
		try {
			projectRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(String.format("Project: " + id + " not found."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
}
