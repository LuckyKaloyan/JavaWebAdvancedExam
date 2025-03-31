package hhh.winner.service;

import hhh.exception.BadInputException;
import hhh.meal.model.Meal;
import hhh.user.model.User;
import hhh.winner.model.Winner;
import hhh.winner.repository.WinnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WinnerService {

    WinnerRepository winnerRepository;

    @Autowired
    public WinnerService(WinnerRepository winnerRepository) {
        this.winnerRepository = winnerRepository;
    }

    public void newWinner(User user, Meal meal){
        if(user == null){
            throw new BadInputException("User ID cannot be null");
        }
        if(meal == null){
            throw new BadInputException("Meal cannot be null");
        }
        Winner winner = Winner.builder()
                .meal(meal)
                .user(user)
                .build();
        winnerRepository.deleteAll();
        winnerRepository.save(winner);
    }

    public Winner getTheWinner(){
        if(winnerRepository.findAll().isEmpty()){
            return null;
        }else return  winnerRepository.findFirstBy().get();
    }
}