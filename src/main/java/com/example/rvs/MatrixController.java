package com.example.rvs;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

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
    private final RestThreadPool threadPool;

    public MatrixController(MatrixService matrixService, RestThreadPool threadPool) {
        this.matrixService = matrixService;
        this.threadPool = threadPool;
    }

    @ApiOperation(value = "Список расчетов", response = LogsView.class)
    @GetMapping
    public DeferredResult<Collection<LogsView>> getAll() {
        return threadPool.execute(matrixService::getAll);
    }

    @ApiOperation(value = "Результат расчета", response = Log.class)
    @GetMapping("{id}")
    public DeferredResult<Log> getById(@PathVariable Long id) {
        return threadPool.execute(() -> matrixService.getById(id));
    }

    @ApiOperation(value = "Выполнить расчет", response = Log.class)
    @PostMapping
    public DeferredResult<Log> calc(@RequestBody @Valid MatrixRequest matrixRequest, HttpServletRequest request) {
        return threadPool.execute(() -> matrixService.calc(matrixRequest.getA(), matrixRequest.getB(), request));
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
