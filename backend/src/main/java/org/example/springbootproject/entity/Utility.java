package org.example.springbootproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "utilities")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Utility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "en_name", nullable = false)
    private String enName;

    @Column(name = "ja_name", nullable = false)
    private String jaName;

    @Column(name = "unit_price")
    private float unitPrice;
}
