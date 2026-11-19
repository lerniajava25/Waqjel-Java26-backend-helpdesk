package com.example.backendUppgift2.repository;

import com.example.backendUppgift2.entity.Ticket;
import com.example.backendUppgift2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedByOrderByCreatedAtDesc(User user);
    List<Ticket> findAllByOrderByCreatedAtDesc();
}
