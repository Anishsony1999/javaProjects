package com.example.emailapp.repository;

import com.example.emailapp.model.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    Optional<Email> findByMessageId(String messageId);

    List<Email> findBySenderContainingIgnoreCase(String sender);

    List<Email> findBySubjectContainingIgnoreCase(String subject);

    List<Email> findByIsRead(Boolean isRead);

    List<Email> findByFolder(String folder);

    @Query("SELECT e FROM Email e WHERE e.sentDate BETWEEN :startDate AND :endDate")
    List<Email> findEmailsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT e FROM Email e WHERE e.sender LIKE %:keyword% OR e.subject LIKE %:keyword% OR e.content LIKE %:keyword%")
    Page<Email> searchEmails(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(e) FROM Email e WHERE e.isRead = false")
    long countUnreadEmails();

    List<Email> findTop10BySenderOrderBySentDateDesc(String sender);
}