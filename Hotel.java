package net.hb.work.hotel;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static net.hb.work.hotel.SignUp.makeNewAccount;
import static net.hb.work.hotel.TestCode.printRooms;
import static net.hb.work.hotel.User.*;


public class Hotel {
    private Room[][] rooms;
    private Scanner sc;



    public Hotel() {
        rooms = null;
        sc = new Scanner(System.in);
    }

    public void generateHotelRoom(){
        int totalFloors = 0;
        boolean validInput = false;
        String roomNumber ="";

        while (!validInput) {
            System.out.print("몇 층의 방을 만들까요? : ");
            try {
                totalFloors = sc.nextInt();
                validInput = true; // Set validInput to true if an integer is entered
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 정수를 입력해주세요.");
                sc.nextLine(); // Consume the invalid input
            }
        }
        sc.nextLine(); // Consume leftover \n from nextInt()

        rooms = new Room[totalFloors][];
        for (int i = 0; i < totalFloors; i++) {
            System.out.print(i + 1 + "층에 몇 개의 방을 만들까요? : ");
            int totalRooms;
            try {
                totalRooms = sc.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 정수를 입력해주세요.");
                sc.nextLine(); // Consume leftover \n from nextInt()
                generateHotelRoom();
                return;
            }
            sc.nextLine(); // delete leftover \n from nextInt()

            rooms[i] = new Room[totalRooms];
            for (int j = 0; j < totalRooms; j++) {
                roomNumber = (i+1)+"0"+(j+1);
                rooms[i][j] = new Room(roomNumber);
            }
        }

    }





}

class HotelWork {
    public static void main(String[] args) {

//        Hotel hotel = new Hotel();
//        hotel.generateHotelRoom();
        makeNewAccount("3");



    }
}

