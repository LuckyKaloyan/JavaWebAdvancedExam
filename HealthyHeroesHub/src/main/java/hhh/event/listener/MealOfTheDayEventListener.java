package hhh.event.listener;

import hhh.event.MealOfTheDayEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MealOfTheDayEventListener {

    @EventListener
    public void handleMealOfTheDayEvent(MealOfTheDayEvent event) {
        log.info("Received Meal of the Day Event: {}", event);
    }
}