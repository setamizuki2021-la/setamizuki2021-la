package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
	List<ToDo> findByUserCodeOrderByCodeAsc(Integer userCode);

}
