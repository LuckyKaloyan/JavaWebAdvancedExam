package hhh.winner.service;

import hhh.exception.BadInputException;
import hhh.meal.model.Meal;
import hhh.user.model.User;
import hhh.user.service.UserService;
import hhh.winner.model.Winner;
import hhh.winner.repository.WinnerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WinnerService {

    private final WinnerRepository winnerRepository;
    private final UserService userService;

    @Autowired
    public WinnerService(WinnerRepository winnerRepository, UserService userService) {
        this.winnerRepository = winnerRepository;
        this.userService = userService;
    }

    @Transactional
    public void newWinner(User user, Meal meal){
        if(user == null){
            throw new BadInputException("User ID cannot be null");
        }
        if(meal == null) {
            throw new BadInputException("Meal cannot be null");

        }

        Winner winner = Winner.builder()
                .totalCatalogs(user.getMealCatalogs().size())
                .totalMeals(userService.getAllMeals(user).size())
                .totalUpVotes(userService.getAllUpVotesToUsersMeals(user).size())
                .totalComments(meal.getComments().size())
                .totalFavourites(meal.getFavouriteMeals().size())
                .crowningDate(LocalDate.now())
                .meal(meal)
                .user(user).build();
        if(winner.getTotalUpVotes()>0){
            winnerRepository.deleteAll();winnerRepository.save(winner);
        }
    }

    public Winner getTheWinner(){
        if(winnerRepository.findAll().isEmpty()){
            return null;
        }else
            return  winnerRepository.findFirstBy().get();
    }
    public void deleteMeal(){
        getTheWinner().setMeal(null);
    }
}