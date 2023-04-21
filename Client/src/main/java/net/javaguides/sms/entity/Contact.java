package net.javaguides.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name",nullable=false)
    public String firstName;

    //	if we do not state column line, then it will automatically take the column name as variable name
    @Column(name="last_name")
    private String lastName;

    @Column(name="phone")
    private String phone;

    @Column(name = "upi_id")
    private String upiId;
}
