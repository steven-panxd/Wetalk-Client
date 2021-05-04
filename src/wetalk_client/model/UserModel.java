package wetalk_client.model;

import java.util.Objects;

/**
 * Data model for users
 * Includes current signed up user and their friends
 */
public class UserModel {
    private final int id;
    private final String username;
    private int newMessageNum = 0;

    /**
     * Constructor of UserModel
     * @param id user's id
     * @param username user's username
     */
    public UserModel(int id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * returns user's id
     * @return user's id
     */
    public int getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return id == userModel.id && username.equals(userModel.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    /**
     * returns user's username
     * @return user's username
     */
    public String getUsername() {
        return username;
    }

    public int getNewMessageNum() {
        return newMessageNum;
    }

    public void setNewMessageNum(int newMessageNum) {
        this.newMessageNum = newMessageNum;
    }
}
