package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
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
    public void addViking(){
        vikingListener.testAdd();
    }

    @PostMapping
    @Operation(summary = "Добавить конкретного викинга",
            operationId = "addViking")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Викинг успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные викинга")
    })

    public ResponseEntity<Viking> addViking(@RequestBody Viking viking) {
        System.out.println("POST /api/vikings called with: " + viking);

        if (viking == null || viking.name() == null || viking.name().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Viking addedViking = vikingService.addViking(viking);

        if (vikingListener != null && vikingListener.getGui() != null) {
            vikingListener.getGui().addNewViking(addedViking);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(addedViking);
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Удалить викинга по имени",
            operationId = "deleteVikingByName")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно удален"),
            @ApiResponse(responseCode = "404", description = "Викинг не найден")
    })

    public ResponseEntity<Void> deleteViking(@PathVariable String name) {
        boolean deleted = vikingService.deleteVikingByName(name);

        if (deleted) {
            if (vikingListener.getGui() != null) {
                vikingListener.getGui().removeVikingByName(name);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{name}")
    @Operation(summary = "Обновить параметры существующего викинга",
            operationId = "updateViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Викинг не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<Viking> updateViking(@PathVariable String name,
                                               @RequestBody Viking updatedViking) {
        boolean updated = vikingService.updateViking(name, updatedViking);

        if (updated) {
            if (vikingListener.getGui() != null) {
                vikingListener.getGui().updateViking(name, updatedViking);
            }
            return ResponseEntity.ok(updatedViking);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{name}")
    @Operation(summary = "Получить викинга по имени",
            operationId = "getVikingByName")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг найден"),
            @ApiResponse(responseCode = "404", description = "Викинг не найден")
    })
    public ResponseEntity<Viking> getVikingByName(
            @Parameter(description = "Имя викинга")
            @PathVariable String name) {
        System.out.println("GET /api/vikings/" + name + " called");

        Optional<Viking> viking = vikingService.findVikingByName(name);

        return viking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}



