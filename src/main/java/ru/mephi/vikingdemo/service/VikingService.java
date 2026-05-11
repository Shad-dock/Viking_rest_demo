package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import ru.mephi.vikingdemo.model.VikingEntity;
import ru.mephi.vikingdemo.repository.VikingRepository;
import ru.mephi.vikingdemo.repository.VikingStorage;

@Service
public class VikingService {
    // каждый раз при изменении создаётся новая копия списка

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;
    private final VikingRepository vikingRepository;


    @Autowired
    public VikingService(
            VikingFactory vikingFactory,
            VikingStorage vikingStorage,
            VikingRepository vikingRepository
    ) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
        this.vikingRepository = vikingRepository;
    }

    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        return vikingStorage.save(viking);
    }

    public boolean deleteVikingByName(String name) {
        List<VikingEntity> entities = vikingRepository.findAll();
        for (VikingEntity entity : entities) {
            if (entity.name().equals(name)) {
                deleteById(entity.id());
                return true;
            }
        }
        return false;
    }

    public Viking addViking(Viking viking) {
        return vikingStorage.save(viking);
    }
    public void deleteById(int id) {
        vikingStorage.deleteById(id);
    }

    public List<Viking> generateVikings(int count) {
        if (count <= 0 || count > 20) {
            throw new IllegalArgumentException("Количество должно быть от 1 до 20");
        }

        List<Viking> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Viking viking = vikingFactory.createRandomViking();
            vikingStorage.save(viking);
            result.add(viking);
        }
        return result;
    }
}