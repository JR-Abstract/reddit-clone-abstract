package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void saveVote_ReturnsOK() throws Exception {
        String voteDtoJson = "{\"userId\":1,\"postId\":1,\"voteType\":\"UPVOTE\"}";

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(voteDtoJson))
                .andExpect(ok);
    }

    @Test
    public void saveVote_ReturnsBadRequest_WhenInvalidJson() throws Exception {
        String invalidJson = "{\"userId\":\"invalid\",\"postId\":1,\"voteType\":\"UPVOTE\"}";

        ResultMatcher badRequest = MockMvcResultMatchers.status().isBadRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(badRequest);
    }
}
