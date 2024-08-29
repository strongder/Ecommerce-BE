package com.example.shop.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "comment")
        private String comment;

        @Column(name = "rating")
        private int rating;

        @Column(name = "create_at")
        private LocalDateTime createAt;

        @ManyToOne
        private User user;

        @ManyToOne
        private Product product;
}
