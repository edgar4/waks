package ke.co.edgar.waks.fragments.pages;

/**
 * Created by Lincoln on 15/01/16.
 */
public class Job {
    private String title, description, post_time, location, salaryAmount,type;
    private int id;

    public Job() {
    }

    public Job(int id, String title, String description, String post_time, String location, String salaryAmount ,String type) {
        this.title = title;
        this.description = description;
        this.post_time = post_time;

        this.location = location;
        this.salaryAmount = salaryAmount;
        this.type = type;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getPostTime() {
        return post_time;
    }

    public void setPostTime(String post_time) {
        this.post_time = post_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(String salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type= type;
    }
}
