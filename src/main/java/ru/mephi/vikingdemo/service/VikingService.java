
package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {
    // каждый раз при изменении создаётся новая копия списка
    private final CopyOnWriteArrayList<Viking> vikings = new CopyOnWriteArrayList<>();
    private final VikingFactory vikingFactory;
    @Autowired
    public VikingService(VikingFactory vikingFactory) {
        this.vikingFactory = vikingFactory;
    }

    public List<Viking> findAll() {
        return List.copyOf(vikings);
    }

    public Viking createRandomViking() {


        Viking viking = vikingFactory.createRandomViking();

        vikings.add(viking);
        return viking;
    }
    public Viking addViking(Viking viking) {
        vikings.add(viking);
        return viking;
    }

    public boolean deleteVikingByName(String name) {
        return vikings.removeIf(viking -> viking.name().equals(name));
    }

    public boolean updateViking(String name, Viking updatedViking) {
        for (int i = 0; i < vikings.size(); i++) {
            if (vikings.get(i).name().equals(name)) {
                vikings.set(i, updatedViking);
                return true;
            }
        }
        return false;
    }

    public Optional<Viking> findVikingByName(String name) {
        return vikings.stream()
                .filter(viking -> viking.name().equals(name))
                .findFirst();
    }

}

package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mephi.vikingdemo.repository.VikingStorage;

@Service
public class VikingService {
    // каждый раз при изменении создаётся новая копия списка 

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;
    
    
    @Autowired
    public VikingService(
            VikingFactory vikingFactory,
            VikingStorage vikingStorage
    ) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
    }
    
    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        return vikingStorage.save(viking);
    }
    public void deleteById(int id) {
        vikingStorage.deleteById(id);
    }
}

