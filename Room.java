package net.hb.work.hotel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Room {

    private String roomNumber; //복수의 방 예약시 체크
    private String userid;
    private boolean reserved;
    private int price;
    private int reservedDay;
    private List<String> reservedDates; // List to store reserved dates

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
        this.reserved = false;
        this.reservedDates = new ArrayList<>();
    }

    //getter and setter auto generate by annotation

    public static Room findRoomByNumber(Room[][] rooms, String roomNumber) {
        for (Room[] room : rooms) {
            for (int j = 0; j < room.length; j++) {
                if (room[j].getRoomNumber().equals(roomNumber)) {
                    return room[j];
                }
            }
        }
        return null; // Room not found
    }
    public void addReservedDate(String date) {
        reservedDates.add(date);
    }

    public void removeReservedDate(String date) {
        reservedDates.remove(date);
    }


    public boolean isReserved(){
        return reserved;
    }




}
