package sample;

public class title {
    private String name;

    public title(String name) {
        this.name = name;
    }
    public title() {
    }

    public String getTitle() {
        return name;
    }


    public void setTitle(String title) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
