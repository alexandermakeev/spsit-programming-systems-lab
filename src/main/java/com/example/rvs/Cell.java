package com.example.rvs;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(schema = "public", name = "cells")
public class Cell implements Comparable<Cell> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Integer row;
    @NotNull
    private Integer col;
    @Column(name = "value")
    @NotNull
    private Double val;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matrix_id")
    private Matrix matrix;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Double getVal() {
        return val;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    @JsonIgnore
    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public int compareTo(Cell o) {
        int compareRow = o.getRow().compareTo(getRow());
        return compareRow == 0 ?
                o.getCol().compareTo(getCol()) : compareRow;
    }
}
