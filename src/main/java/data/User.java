package data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yuriily on 10-Jan-17.
 */
public class User {
    private Integer id;
    private String email;
    //read-only - no need to play with it, even if it worked
    @JsonProperty("is_active")
    private boolean isActive;
    private String name;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
