package com.tcsms.securityserver.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Operator {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "specialOperationCertificateNumber")
    private String specialOperationCertificateNumber;
    @Column(name = "name")
    private String name;
    @Column(name = "workerId")
    private String workerId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSpecialOperationCertificateNumber() {
        return specialOperationCertificateNumber;
    }

    public void setSpecialOperationCertificateNumber(String specialOperationCertificateNumber) {
        this.specialOperationCertificateNumber = specialOperationCertificateNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operator operator = (Operator) o;
        return Objects.equals(username, operator.username) &&
                Objects.equals(specialOperationCertificateNumber, operator.specialOperationCertificateNumber) &&
                Objects.equals(name, operator.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, specialOperationCertificateNumber, name);
    }

}
