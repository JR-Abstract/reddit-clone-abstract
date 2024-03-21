package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

public class SubredditNotFoundException extends SimpleApplicationException {
    public SubredditNotFoundException() {
        super("Subreddit not found");
    }
}
