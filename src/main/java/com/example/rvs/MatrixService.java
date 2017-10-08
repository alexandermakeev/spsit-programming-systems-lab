package com.example.rvs;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class MatrixService {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public Collection<LogsView> getAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<LogsView> query = cb.createQuery(LogsView.class);
        query.from(LogsView.class);
        return em.createQuery(query).getResultList();
    }

    @Transactional(readOnly = true)
    public Log getById(Long id) {
        Log log = em.find(Log.class, id);
        if (log == null) throw new ClientErrorException("Log not found");
        return log;
    }

    @Transactional(rollbackFor = Exception.class)
    public Log calc(List<List<Double>> aCells,
                       List<List<Double>> bCells,
                       HttpServletRequest request) {

        Log log = new Log();
        log.setAddress(request.getRemoteAddr());

        Matrix a = new Matrix();
        a.setCells(aCells);
        Matrix b = new Matrix();
        b.setCells(bCells);


        List<List<Double>> resCells = toList(multiplyMatrices(toArray(aCells), toArray(bCells)));
        Matrix res = new Matrix();
        res.setCells(resCells);

        em.persist(a);
        em.persist(b);
        em.persist(res);

        log.setA(a);
        log.setB(b);
        log.setRes(res);
        log.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        em.persist(log);
        return log;
    }

    private List<List<Double>> toList(double[][] arr) {
        return Arrays.stream(arr)
                .map(a -> DoubleStream.of(a).boxed().collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private double[][] toArray(List<List<Double>> list) {
        return list.stream()
                .map(l -> l.stream().mapToDouble(v -> v).toArray())
                .toArray(double[][]::new);
    }

    private double[][] multiplyMatrices(double[][] a, double[][] b) {

        int aRows = a.length;
        if (aRows < 1)
            throw new IllegalArgumentException("Matrix A is empty");
        int bRows = b.length;
        if (bRows < 1)
            throw new IllegalArgumentException("Matrix B is empty");
        int aCols = a[0].length;
        int bCols = b[0].length;

        if (aRows != bCols)
            throw new ClientErrorException(String.format("A rows: %d didn't match B cols: %d", aRows, bCols));

        //init
        double[][] res = new double[aRows][bCols];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                res[i][j] = 0;
            }
        }

        //multiply each A row concurrently
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < aRows; i++) {
            final int index = i;
            executor.submit(() -> multiply(a, b, res, bRows, aCols, index));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException("Cannot execute request", e);
        }

        return res;
    }

    private void multiply(double[][] a, double[][] b, double[][] res, int bRows, int aCols, int index) {
        for (int j = 0; j < bRows; j++) {
            for (int k = 0; k < aCols; k++) {
                res[index][j] += a[index][k] * b[k][j];
            }
        }
    }
}
