package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingAnalysisService;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private VikingListener vikingListener;
    private VikingAnalysisService analysisService;

    public VikingController(VikingService vikingService, VikingListener vikingListener, VikingAnalysisService analysisService) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
        this.analysisService = analysisService;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов",
            operationId = "getAllVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов",
            operationId = "getTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    @Operation(summary = "Создать викинга со случайными параметрами",
            operationId = "post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно создан")
    })
    public void addViking(){
        System.out.println("POST api/vikings/post called");
        vikingListener.testAdd();
    }

    @PostMapping
    @Operation(summary = "Добавить викинга с конкретными параметрами")
    public ResponseEntity<Viking> addViking(@RequestBody Viking viking) {
        if (viking == null || viking.name() == null || viking.name().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Viking savedViking = vikingService.addViking(viking);

        if (vikingListener != null && vikingListener.getGui() != null) {
            vikingListener.getGui().addNewViking(savedViking);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedViking);
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Удалить викинга по имени")
    public ResponseEntity<Void> deleteViking(@PathVariable String name) {
        System.out.println("DELETE /api/vikings/" + name + " called");

        boolean deleted = vikingService.deleteVikingByName(name);

        if (deleted) {
            if (vikingListener != null && vikingListener.getGui() != null) {
                vikingListener.getGui().removeVikingByName(name);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}