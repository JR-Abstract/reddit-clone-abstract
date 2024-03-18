package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

public class PostNotFoundException extends SimpleApplicationException {
    public PostNotFoundException() {
        super("Post not found");
    }
}
