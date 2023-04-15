package com.OOAD.NPCI.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bankserver")
public class BankServerEntity {
    @Id
    @Column(name = "bankname")
    private String bankName;
    @Column(name = "bankurl")
    private String bankURL;
    @Column(name = "bankapikey")
    private String bankAPIKey;
}
