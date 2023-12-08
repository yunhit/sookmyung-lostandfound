package pack.mp_team5project;

public class CommentModel {
    private String author;
    private String commentContent;
    private String timeStamp;

    private String targetPostKey;

    public CommentModel() {
        // 기본 생성자
    }


    public CommentModel(String author, String commentContent, String timeStamp, String targetPostKey){
        this.author = author;
        this.commentContent = commentContent;
        this.timeStamp = timeStamp;
        this.targetPostKey = targetPostKey;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor() {
        this.author = author;
    }
    public String getCommentContent() {
        return commentContent;
    }
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTargetPostKey() {
        return targetPostKey;
    }

    public void setTargetPostKey(String targetPostKey) {
        this.targetPostKey = targetPostKey;
    }
}
