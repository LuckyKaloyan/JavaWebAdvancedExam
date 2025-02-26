package hhh.upvote.service;
import hhh.upvote.repository.UpVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpVoteService {

    private final UpVoteRepository upVoteRepository;

    @Autowired
    public UpVoteService(UpVoteRepository upVoteRepository) {
        this.upVoteRepository = upVoteRepository;
    }

}
