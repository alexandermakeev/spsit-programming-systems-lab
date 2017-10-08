package com.example.rvs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static com.example.rvs.Util.toList;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class MatrixServiceTest {

    @Autowired
    private MatrixService matrixService;

    @Test
    public void getById() throws Exception {
        matrixService.getAll()
                .forEach(logsView -> assertNotNull(matrixService.getById(logsView.getId())));
    }

    @Test
    public void calc() throws Exception {
        int M = 100;
        int N = 200;
        Random random = new Random(12345L);
        double[][] a = new double[M][N];
        double[][] b = new double[N][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                a[i][j] = random.nextDouble();
                b[j][i] = random.nextDouble();
            }
        }

        matrixService.calc(toList(a), toList(b), new MockHttpServletRequest());
    }

}