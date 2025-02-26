package com.example.library.repository;

import com.example.library.entity.Request;
import com.example.library.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findByUser(User user, Pageable pageable);
}
