package com.pvt.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pvt.crud.entity.Tutorial;

public interface TutorialRespository extends JpaRepository<Tutorial, Long> {
	  List<Tutorial> findByPublished(boolean published);
	  List<Tutorial> findByTitleContaining(String title);

}
