package pack.mp_team5project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostModel {
    private String title;
    private String date;
    private String description;
    private String imageUrl;
    private String campus;
    private String arc;
    private String dtPlace;
    private String ctg;
    private String etc;
    private String userEmailId;
    private String postKey;
    private boolean isDeleteButtonVisible;



    public PostModel() {
        // 기본 생성자 필요 (Firebase에서 데이터를 가져올 때 필요)
    }

    public PostModel(String title, String date, String description, String imageUrl,
                     String campus, String arc, String dtPlace, String ctg, String etc,
                     String userEmailId, String postKey, boolean isDeleteButtonVisible) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageUrl = imageUrl;
        this.campus = campus;
        this.arc = arc;
        this.dtPlace = dtPlace;
        this.ctg = ctg;
        this.etc = etc;
        this.userEmailId = userEmailId;
        this.postKey = postKey;
        this.isDeleteButtonVisible = isDeleteButtonVisible;
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

    public String getCampus() {
        return campus;
    }
    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getArc() {
        return arc;
    }
    public void setArc(String arc) {
        this.arc = arc;
    }

    public String getDtPlace() {
        return dtPlace;
    }
    public void setDtPlace(String dtPlace) {
        this.dtPlace = dtPlace;
    }

    public String getCtg() {
        return ctg;
    }
    public void setCtg(String ctg) {
        this.ctg = ctg;
    }

    public String getEtc() {
        return etc;
    }
    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getUserEmailId() {
        return userEmailId;
    }
    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getPostKey() {
        return postKey;
    }
    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
    public boolean isDeleteButtonVisible() {
        return isDeleteButtonVisible;
    }

}
