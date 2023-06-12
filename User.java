package net.hb.work.hotel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

@Getter
@Setter
@ToString

public class User {
    private String id;
    private String userId;
    private String pwd;
    private String userName;
    private List<Room> userReservedRooms;

    static String userFileName = "src/net/hb/work/hotel/data/userData.json";

    public User(String id, String userId, String pwd, String userName) {
        this.id = id;
        this.userId = userId;
        this.pwd = pwd;
        this.userName = userName;
        this.userReservedRooms = new ArrayList<Room>();
    }

    public User(String id, String pwd, String userName, ArrayList<Room> userReservedRooms) {
        this.id = id;
        this.pwd = pwd;
        this.userName = userName;
        this.userReservedRooms = userReservedRooms;
    }
    public User(String id, String pwd, String userName) {
        this.id = id;
        this.pwd = pwd;
        this.userName = userName;
        this.userReservedRooms = new ArrayList<Room>();
    }

    public User(String id, String pwd) {
        this.id = id;
        this.pwd = pwd;
    }

    public void addReservation(Room room, Date startDate, Date endDate) {
        room.addReservedDateRange(room, startDate, endDate);
        room.setReserved(true);
        userReservedRooms.add(room);
    }


    public void removeReservation(Room room) {
        if (userReservedRooms.contains(room)) {
            userReservedRooms.remove(room);
            room.setReserved(false);
            room.setUserid(null);
            room.setReservedDates(null);
        }
    }

    public static User findUserWithId(ArrayList<User> users, String userId){
        User foundUser = null;
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                foundUser = user;
                break;
            }
        }
        return foundUser;
    }

    public void updateUsers(ArrayList<User> users, User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUserId().equals(updatedUser.getUserId())) {
                // Update the user in the list
                users.set(i, updatedUser);
                return;
            }
        }
    }

    public static void changeUserData(ArrayList<User> users, String userId, Consumer<User> function) {
        //change userdata with function
        User foundUser = findUserWithId(users, userId);

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
            FileWriter file = new FileWriter(userFileName);
            file.write(json);
            file.flush();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }// function saveUserData end

    public static ArrayList<User> loadUserData() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(userFileName);
        if (!file.exists()) {
            // File does not exist, return an empty list
            return users;
        }
        try {
            FileReader reader = new FileReader(file);
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
