package wetalk_client.model;

import java.util.Objects;

public class UserModel {
    private final int id;
    private final String username;

    public UserModel(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getID() {
        return id;
    }

    public String getUsername() {
        return username;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
