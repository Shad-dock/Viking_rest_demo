package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@Service
public class VikingAnalysisService {
    private VikingService vikingService;
    public VikingAnalysisService(VikingService vikingService){
        this.vikingService = vikingService;
    }

    public long countByAgeGreaterThan(int age){
        return countByFilter(viking -> viking.age() > age);
    }
    public long countByAgeLessThan(int age) {
        return countByFilter(v -> v.age() < age);
    }

    public long countByAgeBetween(int minAge, int maxAge) {
        return countByFilter(v -> v.age() >= minAge && v.age() <= maxAge);
    }

    public long countByAgeOutside(int minAge, int maxAge) {
        return countByFilter(v -> v.age() < minAge || v.age() > maxAge);
    }
    public long countByBeardAndHairColor(BeardStyle beardStyle, HairColor hairColor) {
        return countByFilter(v -> v.beardStyle() == beardStyle && v.hairColor() == hairColor);
    }

    public long countWithOneAxe() {
        return countByFilter(v -> v.equipment().stream()
                .filter(item -> item.name().equalsIgnoreCase("Axe")).count() == 1);
    }

    public long countWithTwoAxes() {
        return countByFilter(v -> v.equipment().stream()
                .filter(item -> item.name().equalsIgnoreCase("Axe")).count() == 2);
    }
    private long countByFilter(Predicate<Viking> filter){
        return vikingService.findAll().stream().filter(filter).count();
    }


    public Viking getRandomVikingTallerThan180() {
        List<Viking> list = vikingService.findAll().stream()
                .filter(v -> v.heightCm() > 180).toList();
        return list.isEmpty() ? null : list.get(new Random().nextInt(list.size()));
    }
    public List<Viking> getVikingsWithLegendaryEquipment() {
        return vikingService.findAll().stream()
                .filter(v -> v.equipment().stream()
                        .anyMatch(item -> item.quality().equalsIgnoreCase("Legendary")))
                .toList();
    }
    public List<Viking> getRedBeardedSortedByAge() {
        return vikingService.findAll().stream()
                .filter(v -> v.hairColor() == HairColor.Red &&
                        (v.beardStyle() == BeardStyle.LONG || v.beardStyle() == BeardStyle.BRAIDED || v.beardStyle() == BeardStyle.FORKED ||
                                v.beardStyle() == BeardStyle.SHORT))
                .sorted(Comparator.comparingInt(Viking::age))
                .toList();
    }


    public Integer findMaxId(List<Integer> ids) {
        return ids.stream().max(Integer::compareTo).orElse(null);
    }

    public List<Integer> getEvenIds(List<Integer> ids) {
        return ids.stream().filter(id -> id % 2 == 0).toList();
    }
}
