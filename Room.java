package net.hb.work.hotel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {

    private String roomNumber; //복수의 방 예약시 체크
    private String userid;
    private boolean reserved;
    private int price;
    private int reservedDay;

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
        this.reserved = false;
    }

    public boolean isReserved(){
        return reserved;
    }




}
