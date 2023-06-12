package net.hb.work.hotel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;


import static net.hb.work.hotel.Room.findRoomByNumber;
import static net.hb.work.hotel.User.*;


@Getter
@Setter

public class Hotel {
    private Room[][] rooms;
    private Scanner sc;
    private Date today;

    static String hotelFileName = "src/net/hb/work/hotel/data/hotelData.json";

    public Hotel() {
        rooms = null;
        sc = new Scanner(System.in);
        today = new Date();
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



    public void printListOfRooms(Room[][] rooms){
        System.out.println("전체 예약 상황 확인");

        if (rooms == null) {
            System.out.println("방을 먼저 생성해야 합니다.");
            return;
        }

        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                String roomNumber = String.format("%d%02d", i + 1, j + 1);
                System.out.print(roomNumber + "\t\t\t\t\t");
            }
            System.out.println();

            for (int j = 0; j < rooms[i].length; j++) {
                if (rooms[i][j].isReserved()) {
                    //load users
                    ArrayList<User> loadedUsersData = loadUserData();
                    String userId = rooms[i][j].getUserId();
                    System.out.print(findUserWithId(loadedUsersData,userId).getUserName());
                }
                System.out.print("\t\t\t\t\t");
            }
            System.out.println();
        }

    }//function printListOfRooms end

    public void printSelectedUserRoomList(User user){
        System.out.println(user.getUserName() + "님 예약상황 확인");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Room room : user.getUserReservedRooms()) {
            String roomNumber = room.getRoomNumber();
            Date startDate = room.getReservedDates().get(0);
            Date endDate = room.getReservedDates().get(room.getReservedDates().size() - 1);
            String startDateStr = dateFormat.format(startDate);
            String endDateStr = dateFormat.format(endDate);
            System.out.println(roomNumber + " : " + startDateStr + " ~ " + endDateStr);
        }
    }//function printSelectedUserRoomList end

    public void updateRooms(){

    }
    public void makeReservation(User user, ArrayList<User> users) {
        Scanner scanner = new Scanner(System.in);

        // Display available rooms
        this.printListOfRooms(this.getRooms());

        // Get user input for room selection
        System.out.print("예약할 방 번호를 입력하세요: ");
        String roomNumber = scanner.nextLine();

        // Find the selected room in the hotel
        Room selectedRoom = findRoomByNumber(this.getRooms(), roomNumber);

        if (selectedRoom == null) {
            System.out.println("해당 방 번호가 유효하지 않습니다.");
            return;
        }

        if (selectedRoom.isReserved()) {
            System.out.println("이미 예약된 방입니다.");
            return;
        }

        // Update room reservation status and user information
        selectedRoom.setReserved(true);
        selectedRoom.setUserId(user.getUserId());

        // Get user input for start and end dates
        Date today = this.getToday(); // Get current date from the hotel
        Date startDate = null;
        Date endDate = null;
        boolean validDates = false;

        while (!validDates) {
            System.out.print("예약 시작 날짜를 입력하세요 (YYYY-MM-DD): ");
            String startDateString = scanner.nextLine();
            System.out.print("예약 종료 날짜를 입력하세요 (YYYY-MM-DD): ");
            String endDateString = scanner.nextLine();

            // Convert start and end date strings to Date objects
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startDate = dateFormat.parse(startDateString);
                endDate = dateFormat.parse(endDateString);

                // Compare start date with today's date
                if (startDate.before(today)) {
                    System.out.println("시작 날짜가 오늘보다 이전입니다. 다시 입력하세요.");
                    continue;
                }

                validDates = true; // Dates are valid, exit the loop
            } catch (ParseException e) {
                System.out.println("잘못된 날짜 형식입니다. 다시 입력하세요.");
            }
        }

        // Update user's reservation information
        user.addReservation(selectedRoom, startDate, endDate);

        //update users
        updateUsers(users, user);

        // Save updated hotel and user data
        this.saveHotelData(this);
        saveUserData(users);

        System.out.println("예약이 완료되었습니다.");
    }//function makeReservation end

    public void cancelReservation(User user, ArrayList<User> users){
        Scanner scanner = new Scanner(System.in);
        System.out.println(user.getUserName()+"님 취소하실 방을 골라주세요");

        //show user reserved room
        printSelectedUserRoomList(user);

        boolean isInputValid = false;
        String selectedRoomNumber ="";

        while(!isInputValid) {
            selectedRoomNumber = scanner.nextLine();
            for (Room room : user.getUserReservedRooms()) {
                if (room.getRoomNumber().equals(selectedRoomNumber)) {
                    isInputValid = true;
                    break;
                }
            }
            if(!isInputValid) {
                System.out.println("예약한 방번호가 아닙니다.");
                System.out.println("예약취소를 취소하시려면 9를 입력하세요, 계속하시려면 취소하려는 방번호를 입력해주세요.");
                if (scanner.nextLine().equals("9")) {
                    return;
                }
            }
        }

        // Find the selected room in the hotel
        Room selectedRoom = Room.findRoomByNumber(this.getRooms(), selectedRoomNumber);

        // remove room from Room[][]
        assert selectedRoom != null;
        selectedRoom.setReserved(false);
        selectedRoom.setUserId(null);
        selectedRoom.setReservedDates(null);


        // Update user's reservation information
        user.removeReservation(selectedRoom);

        //update users
        updateUsers(users, user);

        // Save updated hotel and user data
        this.saveHotelData(this);
        saveUserData(users);

        System.out.println("예약이 취소되었습니다.");

    }//function cancelReservation end



    public void saveHotelData(Hotel hotel) {
        try (FileWriter writer = new FileWriter(hotelFileName)) {
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Scanner.class, new ScannerTypeAdapter())
                    .setPrettyPrinting()
                    .create();
            gson.toJson(hotel, writer);
            System.out.println("호텔 데이터가 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("호텔 데이터를 저장할 수 없습니다.");
        }
    }

    public static Hotel loadHotelData() {
        File file = new File(hotelFileName);
        if (!file.exists()) {
            System.out.println("호텔 데이터 파일이 존재하지 않습니다. 새로운 파일을 생성합니다.");
            return new Hotel();
        }
        try (FileReader reader = new FileReader(file)) {
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Scanner.class, new ScannerTypeAdapter())
                    .setPrettyPrinting()
                    .create();
            return gson.fromJson(reader, Hotel.class);
        } catch (IOException e) {
            System.out.println("호텔 데이터를 로드할 수 없습니다.");
        }
        return null;
    }


}

class HotelWork {
    String headMessage="";
    Boolean isLogin = false;
    public static void main(String[] args) {
        //see your working dir
//        String workingDir = System.getProperty("user.dir");
//        System.out.println("Working Directory: " + workingDir);


        //generate new hotelWork
        HotelWork hotelWork = new HotelWork();
        //check if hotel present and load data
        Hotel loadedHotel = Hotel.loadHotelData();
        if (loadedHotel != null) {
            hotelWork.run(loadedHotel);
        } else {
            // Handle the case where the hotel data couldn't be loaded
            System.out.println("Failed to load hotel data. Creating a new hotel.");
            Hotel newHotel = new Hotel();
            hotelWork.run(newHotel);
        }

    }

    public void run(Hotel hotel) {
        Scanner sc = new Scanner(System.in);
        // check if room is not loaded
        if (hotel.getRooms() == null) {
            hotel.generateHotelRoom();
        }
        ArrayList<User> users = User.loadUserData();
        int choice = 0;


        // Display login or signup options
        System.out.println("**************호텔 예약 시스템*************");

        boolean running = true;
        while (running) {
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("3. 예약상황 확인");
            System.out.println("9. 종료");
            System.out.print("메뉴 선택: ");

            boolean isInputChoiceValid = false;
            while(!isInputChoiceValid) {
                try {
                    choice = sc.nextInt();
                    isInputChoiceValid = true;
                } catch (InputMismatchException e) {
                    System.out.println("메뉴중에 골라주세요 : ");
                    sc.nextLine();
                }
            }
            sc.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> {
                    User user = Login.loginUser();
                    if (user != null) {
                        performActions(hotel, user);
                    }
                }
                case 2 -> {
                    users = User.loadUserData();
                    String newUserId = Integer.toString(users.size()); //size() 는  index +1 때문에 그대로 받으면됨
                    User newUser = SignUp.makeNewAccount(newUserId);
                    performActions(hotel, newUser);
                }
                case 3->{
                    hotel.printListOfRooms(hotel.getRooms());
                }
                case 9 -> {
                    System.out.println("프로그램을 종료합니다.");
                    running = false;
                }
                default -> System.out.println("잘못된 선택입니다. 다시 선택하세요.");
            }
        }
    }

    public void performActions(Hotel hotel, User user) {
        Scanner scanner = new Scanner(System.in);
        isLogin = true;
        // Perform actions for the logged-in user
        System.out.println("사용자 " + user.getUserName() + "로 로그인되었습니다.");
        headMessage = user.getUserName() + "님 환영합니다";

        while (isLogin) {
            System.out.println(headMessage);
            System.out.println("**************메뉴 선택*************");
            System.out.println("1. 로그아웃");
            System.out.println("2. 예약하기");
            System.out.println("3. 예약 취소하기");
            System.out.println("4. 예약 내역 보기");
            System.out.println("5. 개인 정보 수정");
            System.out.println("6. 비밀번호 변경");
            System.out.println("7. 회원 탈퇴");


            System.out.print("메뉴 선택: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.println("로그아웃 되었습니다.");
                    isLogin = false;
                }
                case 2 -> {
                    ArrayList < User > users = loadUserData();
                    hotel.makeReservation(user, users);
                }
                case 3 -> {
                    // 예약 취소하기 로직
                    ArrayList < User > users = loadUserData();
                    hotel.cancelReservation(user, users);
                }
                case 4 -> {
                    hotel.printSelectedUserRoomList(user);
                }
                case 5-> {
                    // 개인 정보 수정 로직
                    ArrayList < User > users = loadUserData();
                    user.changeUserData(users, user.getUserId(), UserUtils::changeName);
                }
                case 6 -> {
                    // 비밀번호 변경 로직
                    ArrayList < User > users = loadUserData();
                    user.changeUserData(users, user.getUserId(), UserUtils::changePassword);
                }
                case 7 -> {
                    // 회원 탈퇴 로직
                    ArrayList < User > users = loadUserData();
                    System.out.println("탈퇴하시겠습니까? 이 결정은 번복할 수 없습니다.");
                    System.out.print("정말 탈퇴하시려면 1번을 취소하시려면 아무키나 입력하세요 : ");
                    int tempInput = Integer.parseInt(scanner.nextLine());
                    if(tempInput==1){
                        System.out.println("정상 탈퇴 처리 되었습니다.");
                        users.remove(user);
                        saveUserData(users);
                        isLogin = false;
                    }
                }
                default -> {
                    System.out.println("잘못된 선택입니다. 다시 선택하세요.");
                }
            }
        }

    }// function performAction end
}//HotelWork class END

