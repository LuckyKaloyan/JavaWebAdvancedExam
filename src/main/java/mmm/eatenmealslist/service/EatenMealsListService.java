package mmm.eatenmealslist.service;

import mmm.eatenmealslist.model.EatenMealsList;
import mmm.eatenmealslist.repository.EatenMealsListRepository;
import mmm.exception.NotFound;
import mmm.web.dto.EatenMealsRequest;
import mmm.web.mapper.Mapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class EatenMealsListService {

    private final EatenMealsListRepository eatenMealListRepository;
    private final Mapper mapper;

    @Autowired
    public EatenMealsListService(EatenMealsListRepository eatenMealListRepository, Mapper mapper) {
        if (eatenMealListRepository == null) {
            throw new IllegalArgumentException("UserMealListRepository cannot be null");
        }
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper cannot be null");
        }
        this.eatenMealListRepository = eatenMealListRepository;
        this.mapper = mapper;
    }

    public void addMealToUser(UUID userId, UUID mealId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (mealId == null) {
            throw new IllegalArgumentException("Meal ID cannot be null");
        }

        EatenMealsList userMealList = eatenMealListRepository.findByUserId(userId)
                .orElseGet(() -> {
                    EatenMealsList list = new EatenMealsList();
                    list.setUserId(userId);
                    list.setMealsIds(new ArrayList<>());
                    return list;
                });

        userMealList.getMealsIds().add(mealId);
        eatenMealListRepository.save(userMealList);
    }

    public EatenMealsRequest getUserMealList(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        EatenMealsList userMealList = eatenMealListRepository.findByUserId(userId)
                .orElseGet(() -> {
                    EatenMealsList list = new EatenMealsList();
                    list.setUserId(userId);
                    list.setMealsIds(new ArrayList<>());
                    return list;
                });

        return mapper.toRequest(userMealList);
    }

    public void deleteMealFromUser(UUID userId, int mealIndex) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (mealIndex < 0) {
            throw new IllegalArgumentException("Meal index cannot be negative");
        }

        EatenMealsList eatenMealsList = eatenMealListRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("User with ID " + userId + " not found"));

        if (mealIndex >= eatenMealsList.getMealsIds().size()) {
            throw new IndexOutOfBoundsException("Meal index " + mealIndex + " is out of bounds");
        }

        eatenMealsList.getMealsIds().remove(mealIndex);
        eatenMealListRepository.save(eatenMealsList);
    }

    @Transactional
    public void deleteAllMealsWithId(UUID mealId) {
        if (mealId == null) {
            throw new IllegalArgumentException("Meal ID cannot be null");
        }

        eatenMealListRepository.findAll().forEach(userMealList -> {
            if (userMealList.getMealsIds() != null) {
                userMealList.getMealsIds().removeIf(id -> mealId.equals(id));
                eatenMealListRepository.save(userMealList);
            }
        });
    }

    public void deleteAll() {
        eatenMealListRepository.deleteAll();
    }
}
