import jdk.jshell.spi.ExecutionControl;

import java.sql.*;
import java.util.Scanner;

public class Main2 {
    static Scanner sc = new Scanner(System.in);
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String password = "mysql.jdbc";
    private static final String name = "root";


private static void make_reservation(Connection con, Scanner sc, Statement st){
    System.out.println("Enter name");
    String name = sc.nextLine();
    System.out.println("Enter room number");
    int room_no = sc.nextInt();
    System.out.println("Enter Contact number");
    String phone = sc.nextLine();

    String query = "insert into reservation (guest_name,room_number,contact_number) values ('"+name+"','"+room_no+"','"+phone+"') ";


    try{
        int rows_affected = st.executeUpdate(query);
        if(rows_affected>0){
            System.out.println("Reservation succesful");
        }else{
            System.out.println("Reservation Failed");
        }

    }catch (SQLException e){
        System.out.println(e.getMessage());
    }
}

private static void display(Connection con, Scanner sc, Statement st) {
    String query = "select reservation_id,guest_name,room_number,contact_number,reservation_date from reservation";

    try {

        System.out.println("Current Reservations");
        ResultSet rt = st.executeQuery(query);
        while (rt.next()) {
            int reservationID = rt.getInt("reservation_id");
            String guestName = rt.getString("guest_name");
            int room_number = rt.getInt("room_number");
            String contactNumber = rt.getString("contact_number");
            String reservatinDate = rt.getTimestamp("reservation_date").toString();

            System.out.println("Reservation id : " + reservationID);
            System.out.println("Guest Name : " + guestName);
            System.out.println("The Room Number is : " + room_number);
            System.out.println("Contact Number : " + contactNumber);
            System.out.println("The reservation date : " + reservatinDate);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

private static void updateReservation(Connection con, Scanner sc, Statement st){
    try{
        System.out.println("Enter reservation ID : ");
        int ID = sc.nextInt();

        if(!reservationExists(con,ID, st)){
            System.out.println("Reservation Not found");
            return;
        }

        System.out.println("Enter new Name : ");
        String name = sc.nextLine();
        System.out.println("Allocate a room : ");
        int room = sc.nextInt();
        System.out.println("Enter new Contact Number : ");
        String number = sc.nextLine();
        String query = "update reservation set guest_name = '"+ name + "', room_number = '"+room+"', contact_number = '"+number+", where reservation_ID ="+ID;
        int affected_rows = st.executeUpdate(query);
        if(affected_rows>0){
            System.out.println("Updates successfully ");

        }else{
            System.out.println("Updation failed ");
        }
    }catch (SQLException e ){

    }
}


private static void deleteReservation(Connection con, Scanner sc, Statement st){
    try{
        System.out.println("Enter reservation ID to delete : ");
        int id = sc.nextInt();
        if(!reservationExists(con,id,st)){
            System.out.println("Reservation not found for thr ID");
            return;
        }
        String query = "delete from reservation where reservation_id = "+id;
        int af_rows = st.executeUpdate(query);
        if(af_rows>0){
            System.out.println("Deletion successful ");
        }else{
            System.out.println("Failed deletion ");
        }
    }catch (SQLException e){
        System.out.println(e.getMessage());
    }
}




    private static boolean reservationExists(Connection con, int reservationID, Statement st){
    try{
        String query = "select reservation_id from reservations where reservation_id = "+reservationID;
        ResultSet rt = st.executeQuery(query);
        return rt.next();

    }catch (SQLException e){
        System.out.println(e.getMessage());
        return false;
    }
}


public static void exit() throws InterruptedException{
    System.out.println("Exiting system ");
    int i = 5;
    while(i!=0){
        System.out.println(".");
        Thread.sleep(450);
        i--;
    }
    System.out.println();
    System.out.println("Thank you for using Hotel Reservation System ");
}

private static void getRoomNumber(Connection con, Scanner sc, Statement st){
        try{
            System.out.println("Enter reservation ID : ");
            int reservationId = sc.nextInt();
            System.out.println("Enter guest name : ");
            String name = sc.nextLine();

            String query = "select room_number from reservation where reservation_id = " + reservationId + "and guest_name = '"+ name+ "'";
            ResultSet rt = st.executeQuery(query);
            if(rt.next()){
                int room_number = rt.getInt("room_number");
                System.out.println("Room number for Reservation ID "+ reservationId + "and Guest "+name+"is : "+room_number);


            }else{
                System.out.println("Reservation not found ");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }





    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection con = DriverManager.getConnection(url,name,password);
            Statement st = con.createStatement();
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Make a Reservation");
                System.out.println("2. Show Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println();
                int choice = sc.nextInt();
                switch (choice){
                    case 1:

                        make_reservation(con,sc,st);
                        break;
                    case 2:

                        display(con,sc,st);
                        break;
                    case 3:

                        getRoomNumber(con,sc,st);
                        break;
                    case 4:

                        updateReservation(con,sc,st);
                        break;
                    case 5:

                        deleteReservation(con,sc,st);
                        break;
                    case 0:

                        exit();
                        sc.close();
                       return;
                    default:
                        System.out.println("Invalid choice , try again....");

                }


            }

        }catch (SQLException e){

            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
