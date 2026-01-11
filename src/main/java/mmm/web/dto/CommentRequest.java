package mmm.web.dto;

import jakarta.validation.constraints.Size;

public class CommentRequest {

    @Size(min = 2, max = 500, message = "Come clean! Use between 2 and 500 letters.")
    private String text;

    // No-args constructor
    public CommentRequest() {
    }

    // All-args constructor
    public CommentRequest(String text) {
        this.text = text;
    }

    // Getter and setter

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
