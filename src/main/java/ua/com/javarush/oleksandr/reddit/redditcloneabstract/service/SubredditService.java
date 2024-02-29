package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.SubredditRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SubredditService {

    private final SubredditRepository subredditRepository;

    @Autowired
    public SubredditService(SubredditRepository subredditRepository) {
        this.subredditRepository = subredditRepository;
    }

    public List<Subreddit> findAll() {
        return subredditRepository.findAll();
    }

    public Optional<Subreddit> findById(Long id) {
        return subredditRepository.findById(id);
    }

    @Transactional
    public void save(Subreddit subreddit) {
        subredditRepository.save(subreddit);
    }
}