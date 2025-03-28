package hhh.scheduler;


import hhh.upvote.service.UpVoteService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CleanUpScheduler {

    private final UpVoteService upVoteService;


    @Autowired
    public CleanUpScheduler(UpVoteService upVoteService) {
        this.upVoteService = upVoteService;
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanUp() {
        upVoteService.cleanUp();
    }
}
