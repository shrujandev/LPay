package com.bank.bankServer.Entity;

import javax.annotation.processing.Generated;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String accountNo;
    private int Balance;
}