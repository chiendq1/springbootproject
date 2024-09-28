package org.example.springbootproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

@Entity
@Table(name = "contracts")
@Setter
@Getter
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
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