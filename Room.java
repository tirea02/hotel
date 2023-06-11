package net.hb.work.hotel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Room {

    private String roomNumber; //복수의 방 예약시 체크
    private String userid;
    private boolean reserved;
    private int price;
    private int reservedDay;
    private List<Date> reservedDates; // List to store reserved dates

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
        this.reserved = false;
        this.reservedDates = new ArrayList<>();
    }

    //getter and setter auto generate by annotation

    public static Room findRoomByNumber(Room[][] rooms, String roomNumber) {
        for (Room[] room : rooms) {
            for (Room value : room) {
                if (value.getRoomNumber().equals(roomNumber)) {
                    return value;
                }
            }
        }
        return null; // Room not found
    }//function findRoomByNumber end

    public void addReservedDateRange(Room room, Date startDate, Date endDate) {
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();

        for (long millis = startMillis; millis <= endMillis; millis += 24 * 60 * 60 * 1000) {
            Date date = new Date(millis);
            room.addReservedDate(date);
        }
    }

    public void addReservedDate(Date date) {
        reservedDates.add(date);
    }

    public void removeReservedDate(Date date) {
        reservedDates.remove(date);
    }


    public boolean isReserved(){
        return reserved;
    }


}//Room class END
