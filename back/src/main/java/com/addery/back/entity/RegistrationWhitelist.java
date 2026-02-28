package com.addery.back.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "registration_whitelist", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"phone", "id_card"})
})
public class RegistrationWhitelist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 11)
    private String phone;

    @Column(name = "id_card", nullable = false, length = 18)
    private String idCard;

    @Column(name = "remark")
    private String remark;
}