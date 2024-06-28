package com.example.shopapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
