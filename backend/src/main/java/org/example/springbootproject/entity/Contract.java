package org.example.springbootproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contracts")
@Setter
@Getter
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "contract_name", nullable = false, unique = true)
    private String contractName;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "deposit", nullable = false)
    private float deposit;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "status", nullable = false)
    private int status;

    @OneToMany(mappedBy = "contract",  cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ContractDetails> contractDetails;

    @Column(name = "created_at", nullable = false)
    private Date createAt;

    public String getTypeText() {
        switch (type) {
            case 0:
                return "Monthly";
                case 1:
                    return "Quater";
                    case 2:
                        return "Half Year";
                        case 3:
                            return "Yearly";
                            default:
                                return "";
        }
    }
}