package com.example.rvs;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/matrix")
public class MatrixController {

    private final MatrixService matrixService;

    public MatrixController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @GetMapping
    public Collection<LogsView> getAll() {
        return matrixService.getAll();
    }

    @GetMapping("{id}")
    public Log getById(@PathVariable Long id) {
        return matrixService.getById(id);
    }

    @PostMapping
    public Log calc(@RequestBody @Valid MatrixRequest matrixRequest, HttpServletRequest request) {
        return matrixService.calc(matrixRequest.getA(), matrixRequest.getB(), request);
    }

    private static class MatrixRequest {
        @NotNull
        private List<List<Double>> a = new ArrayList<>();
        @NotNull
        private List<List<Double>> b = new ArrayList<>();

        List<List<Double>> getA() {
            return a;
        }

        public void setA(List<List<Double>> a) {
            this.a = a;
        }

        List<List<Double>> getB() {
            return b;
        }

        public void setB(List<List<Double>> b) {
            this.b = b;
        }
    }

}
