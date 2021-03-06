package ru.students.lab.database;

import java.io.Serializable;
import java.util.Objects;

public class Credentials implements Serializable {

    private static final long serialVersionUID = 171771050017957012L;

    public final int id;
    public final String username;
    public final String password;

    public Credentials(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
