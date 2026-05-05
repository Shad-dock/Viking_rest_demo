package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingAnalysisService;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class VikingAnalysisController {
    private final VikingAnalysisService service;

    public VikingAnalysisController(VikingAnalysisService service) {
        this.service = service;
    }

    @GetMapping("/count/age-greater/{age}")
    @Operation(summary = "Количество викингов старше указанного возраста")
    public long countAgeGreater(@PathVariable int age) {
        return service.countByAgeGreaterThan(age);
    }

    @GetMapping("/count/age-less/{age}")
    @Operation(summary = "Количество викингов младше указанного возраста")
    public long countAgeLess(@PathVariable int age) {
        return service.countByAgeLessThan(age);
    }

    @GetMapping("/count/age-between")
    @Operation(summary = "Количество викингов в диапазоне возрастов")
    public long countAgeBetween(@RequestParam int min, @RequestParam int max) {
        return service.countByAgeBetween(min, max);
    }

    @GetMapping("/count/age-outside")
    @Operation(summary = "Количество викингов вне диапазона возрастов")
    public long countAgeOutside(@RequestParam int min, @RequestParam int max) {
        return service.countByAgeOutside(min, max);
    }


    @GetMapping("/count/beard-hair")
    @Operation(summary = "Количество викингов с определённой бородой и цветом волос")
    public long countBeardHair(@RequestParam BeardStyle beard, @RequestParam HairColor hair) {
        return service.countByBeardAndHairColor(beard, hair);
    }

    @GetMapping("/count/one-axe")
    @Operation(summary = "Количество викингов с одним топором")
    public long countOneAxe() {
        return service.countWithOneAxe();
    }

    @GetMapping("/count/two-axes")
    @Operation(summary = "Количество викингов с двумя топорами")
    public long countTwoAxes() {
        return service.countWithTwoAxes();
    }

    @GetMapping("/random-tall")
    @Operation(summary = "Случайный викинг ростом выше 180 см")
    public Viking randomTall() {
        return service.getRandomVikingTallerThan180();
    }

    @GetMapping("/legendary")
    @Operation(summary = "Все викинги с легендарным снаряжением")
    public List<Viking> legendary() {
        return service.getVikingsWithLegendaryEquipment();
    }

    @GetMapping("/red-beard-sorted")
    @Operation(summary = "Рыжебородые викинги, отсортированные по возрасту")
    public List<Viking> redBeardSorted() {
        return service.getRedBeardedSortedByAge();
    }

    @PostMapping("/max-id")
    @Operation(summary = "Найти максимальный ID в массиве")
    public Integer maxId(@RequestBody List<Integer> ids) {
        return service.findMaxId(ids);
    }

    @PostMapping("/even-ids")
    @Operation(summary = "Получить все чётные ID из массива")
    public List<Integer> evenIds(@RequestBody List<Integer> ids) {
        return service.getEvenIds(ids);
    }
}
