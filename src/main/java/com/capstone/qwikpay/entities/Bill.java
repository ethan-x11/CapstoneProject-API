package com.capstone.qwikpay.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "bill_details")
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from serialization
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer billId;

    private Integer amount;

    private String billType;

    @Column(name = "bill_status", length = 50)
    private String billStatus;

    @Column(length = 255)
    private String description;

    @Column(name = "due_date")
    @JsonProperty("due_date") // Rename for JSON output
    private LocalDateTime dueDate;

    @Column(name = "created_at", updatable = false)
    @JsonProperty(value = "created_at", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonProperty(value = "updated_at", access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL)
    @JsonIgnore // Prevent circular reference during serialization
    private Payment payment;

    // Custom constructor for deserialization
    public Bill(Integer billId) {
        this.billId = billId;
    }
}
