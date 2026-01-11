package mmm.winner.service;

import mmm.exception.BadInputException;
import mmm.meal.model.Meal;
import mmm.user.model.User;
import mmm.user.service.UserService;
import mmm.winner.model.Winner;
import mmm.winner.repository.WinnerRepository;
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
    public void newWinner(User user, Meal meal) {
        if (user == null) {
            throw new BadInputException("User ID cannot be null");
        }
        if (meal == null) {
            throw new BadInputException("Meal cannot be null");
        }

        Winner winner = new Winner();
        winner.setTotalCatalogs(user.getMealCatalogs().size());
        winner.setTotalMeals(userService.getAllMeals(user).size());
        winner.setTotalUpVotes(userService.getAllUpVotesToUsersMeals(user).size());
        winner.setTotalComments(meal.getComments().size());
        winner.setTotalFavourites(meal.getFavouriteMeals().size());
        winner.setCrowningDate(LocalDate.now());
        winner.setMeal(meal);
        winner.setUser(user);

        if (winner.getTotalUpVotes() > 0) {
            winnerRepository.deleteAll();
            winnerRepository.save(winner);
        }
    }

    public Winner getTheWinner() {
        if (winnerRepository.findAll().isEmpty()) {
            return null;
        } else {
            return winnerRepository.findFirstBy().get();
        }
    }

    public void deleteMeal() {
        getTheWinner().setMeal(null);
    }

    public void saveIt(Winner winner) {
        winnerRepository.save(winner);
    }
}
