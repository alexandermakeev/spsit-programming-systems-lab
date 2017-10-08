package com.example.rvs;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
@Table(schema = "public", name = "matrices")
public class Matrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "matrix", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Cell> cells = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<List<Double>> getCells() {
        return cells.stream()
                .sorted(Comparator.comparing(Cell::getRow))
                .collect(Collectors.groupingBy(Cell::getRow))
                .entrySet().stream()
                .map(
                        row ->
                                row.getValue().stream()
                                        .sorted(Comparator.comparing(Cell::getCol))
                                        .map(Cell::getVal)
                                        .collect(Collectors.toList())
                ).collect(Collectors.toList());
    }

    public void setCells(List<List<Double>> cells) {
        IntStream.range(0, cells.size()).forEach(i -> {
            List<Double> row = cells.get(i);
            IntStream.range(0, row.size())
                    .forEach(j -> {
                        Cell col = new Cell();
                        col.setRow(i);
                        col.setCol(j);
                        col.setVal(row.get(j));
                        col.setMatrix(this);
                        this.cells.add(col);
                    });
        });
    }


}
