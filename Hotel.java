package net.hb.work.hotel;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


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
        String roomNumber;

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
    }//function generateHotelRoom end

    public void makeReservation(){
    }

}

class HotelWork {
    public static void main(String[] args) {
        HotelWork hotelWork = new HotelWork();
        hotelWork.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> users = User.loadUserData();

        // Display login or signup options
        System.out.println("**************호텔 예약 시스템*************");

        boolean running = true;
        while (running) {
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("3. 종료");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> {
                    User user = Login.loginUser();
                    if (user != null) {
                        performActions(user);
                    }
                }
                case 2 -> {
                    users = User.loadUserData();
                    String newUserId = Integer.toString(users.size()); //size() 는  index +1 때문에 그대로 받으면됨

                    User newUser = SignUp.makeNewAccount(newUserId);
                    if (newUser != null) {
                        performActions(newUser);
                    }
                }
                case 3 -> {
                    System.out.println("프로그램을 종료합니다.");
                    running = false;
                }
                default -> System.out.println("잘못된 선택입니다. 다시 선택하세요.");
            }
        }
    }

    public void performActions(User user) {
        // Perform actions for the logged-in user
        // Example: Access the user's reservations, make new reservations, etc.
        System.out.println("사용자 " + user.getUserName() + "로 로그인되었습니다.");
        // Implement your desired actions for the user
    }
}

