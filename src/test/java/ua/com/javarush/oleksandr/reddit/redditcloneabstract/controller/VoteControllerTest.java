
package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.VoteDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.VoteService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class VoteControllerTest {

    @InjectMocks
    private VoteController voteController;
    @Mock
    private VoteService voteService;

    @Test
    public void testSaveVote() {

        VoteDto voteDto = VoteDto.with().build();

        ResponseEntity<Void> response = voteController.saveVote(voteDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(voteService).vote(voteDto);
    }
}



