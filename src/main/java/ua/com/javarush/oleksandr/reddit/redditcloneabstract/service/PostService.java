package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.PostRepository;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Post with id %d not found", id)));
    }

    public Collection<Post> findAll() {
        return postRepository.findAll();
    }

    public Collection<Post> findPostsByUsername(String username) {
        return postRepository.findByUser_Username(username);
    }

    public Integer countAllBySubreddit_Id(Long id) {
        return postRepository.countAllBySubreddit_Id(id);
    }

    public Optional<Post> findByUrl(String url) {
        return postRepository.findFirstByUrl(url);
    }
}
