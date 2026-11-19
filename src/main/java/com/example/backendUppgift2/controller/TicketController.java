package com.example.backendUppgift2.controller;

import com.example.backendUppgift2.entity.Comment;
import com.example.backendUppgift2.entity.Role;
import com.example.backendUppgift2.entity.Ticket;
import com.example.backendUppgift2.entity.TicketStatus;
import com.example.backendUppgift2.entity.User;
import com.example.backendUppgift2.repository.CommentRepository;
import com.example.backendUppgift2.repository.TicketRepository;
import com.example.backendUppgift2.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final CommentRepository commentRepo;

    public TicketController(TicketRepository ticketRepo, UserRepository userRepo, CommentRepository commentRepo) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
    }

    @GetMapping
    public String listTickets(Model model, Principal principal) {
        User currentUser = userRepo.findByUsername(principal.getName()).orElseThrow();
        if (currentUser.getRole() == Role.USER) {
            model.addAttribute("tickets", ticketRepo.findByCreatedByOrderByCreatedAtDesc(currentUser));
        } else {
            model.addAttribute("tickets", ticketRepo.findAllByOrderByCreatedAtDesc());
        }
        return "tickets/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "tickets/new";
    }

    @PostMapping
    public String createTicket(@ModelAttribute Ticket ticket, Principal principal) {
        User currentUser = userRepo.findByUsername(principal.getName()).orElseThrow();
        ticket.setCreatedBy(currentUser);
        ticket.setStatus(TicketStatus.NEW);
        ticketRepo.save(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model, Principal principal) {
        Ticket ticket = ticketRepo.findById(id).orElseThrow();
        User currentUser = userRepo.findByUsername(principal.getName()).orElseThrow();

        if (currentUser.getRole() == Role.USER && !ticket.getCreatedBy().getId().equals(currentUser.getId())) {
            return "redirect:/tickets?error=unauthorized";
        }

        model.addAttribute("ticket", ticket);
        model.addAttribute("newComment", new Comment());
        model.addAttribute("statuses", TicketStatus.values());
        model.addAttribute("agents", userRepo.findByRoleInAndActiveTrue(List.of(Role.AGENT, Role.ADMIN)));
        return "tickets/detail";
    }

    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable Long id, @ModelAttribute Comment newComment, Principal principal) {
        Ticket ticket = ticketRepo.findById(id).orElseThrow();
        User currentUser = userRepo.findByUsername(principal.getName()).orElseThrow();

        if (currentUser.getRole() == Role.USER && !ticket.getCreatedBy().getId().equals(currentUser.getId())) {
            return "redirect:/tickets?error=unauthorized";
        }

        newComment.setId(null);
        newComment.setTicket(ticket);
        newComment.setAuthor(currentUser);
        commentRepo.save(newComment);
        return "redirect:/tickets/" + id;
    }

    @PostMapping("/{id}/assign")
    public String assignToSelf(@PathVariable Long id, Principal principal) {
        User currentUser = userRepo.findByUsername(principal.getName()).orElseThrow();
        if (currentUser.getRole() == Role.USER) return "redirect:/tickets";

        Ticket ticket = ticketRepo.findById(id).orElseThrow();
        ticket.setAssignedTo(currentUser);
        if (ticket.getStatus() == TicketStatus.NEW) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }
        ticketRepo.save(ticket);
        return "redirect:/tickets/" + id;
    }

    @PostMapping("/{id}/update")
    public String updateTicket(@PathVariable Long id,
                               @RequestParam TicketStatus status,
                               @RequestParam(required = false) Long userId,
                               Principal principal) {
        User currentUser = userRepo.findByUsername(principal.getName()).orElseThrow();
        if (currentUser.getRole() == Role.USER) return "redirect:/tickets";

        Ticket ticket = ticketRepo.findById(id).orElseThrow();
        ticket.setStatus(status);

        if (userId != null) {
            User assignee = userRepo.findById(userId).orElseThrow();
            ticket.setAssignedTo(assignee);
        } else {
            ticket.setAssignedTo(null);
        }

        ticketRepo.save(ticket);
        return "redirect:/tickets/" + id;
    }
}
