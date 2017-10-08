package com.example.rvs;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(schema = "public", name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "remote_address")
    private String address;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "matrix_a_id")
    private Matrix a;

    @ManyToOne
    @JoinColumn(name = "matrix_b_id")
    private Matrix b;

    @ManyToOne
    @JoinColumn(name = "matrix_result_id")
    private Matrix res;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Matrix getA() {
        return a;
    }

    public void setA(Matrix a) {
        this.a = a;
    }

    public Matrix getB() {
        return b;
    }

    public void setB(Matrix b) {
        this.b = b;
    }

    public Matrix getRes() {
        return res;
    }

    public void setRes(Matrix res) {
        this.res = res;
    }
}
