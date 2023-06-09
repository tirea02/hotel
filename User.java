package net.hb.work.hotel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

@Getter
@Setter
@ToString

public class User {
    private String id;
    private String userId;
    private String pwd;
    private String userName;
    private Room[] rooms;

    static String fileName = "C:\\Mtest\\workJava\\day0601\\src\\net\\hb\\work\\hotel\\data/userData.json";

    public User(String id, String userId, String pwd, String userName) {
        this.id = id;
        this.userId = userId;
        this.pwd = pwd;
        this.userName = userName;
    }

    public User(String id, String pwd, String userName, Room[] rooms) {
        this.id = id;
        this.pwd = pwd;
        this.userName = userName;
        this.rooms = rooms;
    }
    public User(String id, String pwd, String userName) {
        this.id = id;
        this.pwd = pwd;
        this.userName = userName;
        this.rooms = null;
    }

    public User(String number, String number1) {
    }


    public static void changeUserData(ArrayList<User> users, String id, Consumer<User> function) {
        //change userdata with function
        User foundUser = null;
        for (User user : users) {
            if (user.getId().equals(id)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser != null) {
            function.accept(foundUser); // Invoke the function on the user object
        } else {
            System.out.println("User not found");
        }
    }// function changeUserData end


    public static void saveUserData(ArrayList<User> users) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(users);

        try {
            FileWriter file = new FileWriter(fileName);
            file.write(json);
            file.flush();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }// function saveUserData end

    public static ArrayList<User> loadUserData() {
        ArrayList<User> users = null;
        try {
            FileReader reader = new FileReader(fileName);
            Gson gson = new Gson();
            users = gson.fromJson(reader, new TypeToken<ArrayList<User>>() {
            }.getType());
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }// function loadUserData end

}//class END