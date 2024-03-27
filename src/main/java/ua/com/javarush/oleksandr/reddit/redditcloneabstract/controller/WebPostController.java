package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.PostMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.BindingResultService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.validator.PostRequestValidator;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class WebPostController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final PostRequestValidator postRequestValidator;
    private final BindingResultService bindingResultService;

    @GetMapping
    public String showFeed(Model model) {
        Collection<Post> posts = postService.findAll();

        model.addAttribute("posts", posts);
        return "post/feed"; // View name
    }


}
