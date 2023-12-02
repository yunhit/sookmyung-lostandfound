package pack.mp_team5project;

public class PostModel {
    private String title;
    private String date;
    private String description;
    private String imageUrl;



    public PostModel() {
        // 기본 생성자 필요 (Firebase에서 데이터를 가져올 때 필요)
    }

    public PostModel(String title, String date, String description, String imageUrl) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
