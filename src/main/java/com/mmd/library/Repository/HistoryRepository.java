package com.mmd.library.Repository;

import com.mmd.library.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface HistoryRepository extends JpaRepository<History, Long> {
	Page<History> findBooksByUserEmail(@Param("email") String userEmail, Pageable pageable);
}
