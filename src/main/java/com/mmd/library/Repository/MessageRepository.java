package com.mmd.library.Repository;

import com.mmd.library.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByUserEmail(@Param("userEmail") String userEmail, Pageable pageable);

    Page<Message> findByClosed(@Param("closed") boolean closed, Pageable pageable);
}
