package org.example.springbootproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contract_details")
@Setter
@Getter
public class ContractDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    @JsonBackReference
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "utility_id", nullable = false)
    @JsonBackReference
    private Utility utility;

    @Column(name = "unit_price")
    private float unitPrice;
}
