package org.manhattan;


import java.sql.*;
import java.util.Scanner;

public class App
{

    public static final String URLTOCONNECT="jdbc:mysql://localhost:3306/banking";
    public static final String USERNAME="root";
    public static final String PASSWORD="";
    static Connection dbConnection;
    String qry,qryTemp;
    static Statement theStatement;
    ResultSet resultSet;

    public Time time;



    public static void main( String[] args )
    {
        Scanner input=new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection dbConnection= DriverManager.getConnection(URLTOCONNECT,USERNAME,PASSWORD);
            theStatement=dbConnection.createStatement();


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        while (true) {
            System.out.println("-----Welcome to Manhattan Banking------");
            System.out.println("1. Login Account");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");

            System.out.println("Enter your choice: ");
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter account number: ");
                    int number = input.nextInt();
                    System.out.println("Enter password: ");
                    String password = input.next();
                    new App().loginAccount(number, password);
                    boolean forward=true;
                    while (forward) {
                        System.out.println("----Banking Operations----");
                        System.out.println("1. Check Balance");
                        System.out.println("2. Deposit Money");
                        System.out.println("3. Withdraw Money");
                        System.out.println("4. Transfer Money");
                        System.out.println("5. Log Out");

                        System.out.println("Enter your choice: ");
                        int choice2 = input.nextInt();
                        switch (choice2) {
                            case 1:
                                new App().checkBalance(number);
                                break;

                            case 2:
                                System.out.println("Enter Money to deposit: ");
                                int deposit = input.nextInt();
                                new App().depositMoney(deposit, number);
                                break;

                            case 3:
                                System.out.println("Enter Money to withdraw: ");
                                int withdraw = input.nextInt();
                                new App().withdrawMoney(withdraw, number);
                                break;

                            case 4:
                                System.out.println("Enter account number of person you wanna transfer money: ");
                                int receiverId = input.nextInt();
                                System.out.println("Enter amount you want to send ");
                                int transferAmount = input.nextInt();
                                new App().transferMoney(transferAmount, receiverId, number);
                                break;

                            case 5:
                                System.out.println("You have been logged out..");
                                forward=false;
                                break;



                            default:
                                System.out.println("Invalid Entry");
                        }
                    }


                    break;

                case 2:
                    System.out.println("Enter your name: ");
                    String name = input.next();
                    System.out.println("Set a password: ");
                    String passWord = input.next();
                    System.out.println("Enter initial amount you want to deposit (Min balance =1000): ");
                    int balance = input.nextInt();
                    new App().createAccount(name, passWord, balance);
                    break;

                case 3:
                    System.out.println("App Shutting down...");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid Entry");
                    break;

            }

        }



    }

    void createAccount(String name,String passWord,int balance)
    {
        qry="INSERT INTO customer(account_name,account_balance,account_password) " +
                "VALUES('" + name + "', + '" + balance + "', '" + passWord + "')";
        try {
            if(theStatement.executeUpdate(qry)>0)
            {
                System.out.println("New account created.. ");
                System.out.println("Your account number alloted is: "+new App().getAccountNumber(name));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    int getAccountNumber(String name)
    {
        qry="SELECT *FROM customer WHERE account_name='"+name+"'";
        int accountNumber=0;
        try {
            resultSet=theStatement.executeQuery(qry);
            while ((resultSet.next()))
            {
                if(resultSet.getString("account_name").equals(name))
                    accountNumber=resultSet.getInt("account_number");
            }
            return accountNumber;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    void loginAccount(int number,String password)
    {
        qry="SELECT account_password FROM customer WHERE account_number="+number;
        try {
            resultSet=theStatement.executeQuery(qry);
            while ((resultSet.next()))
            {
                if(resultSet.getString("account_password").equals(password))
                    System.out.println("Login Successful...");
                else
                    System.out.println("Invalid Credentials...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void checkBalance(int number)
    {
        qry="SELECT account_balance FROM customer WHERE account_number="+number;
        try {
            resultSet=theStatement.executeQuery(qry);
            while ((resultSet.next()))
            {
                System.out.println("Current Balance: "+resultSet.getString("account_balance"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void depositMoney(int deposit,int number)
    {
        qry="UPDATE customer SET account_balance=(account_balance+"+deposit+") WHERE account_number="+number;
        try {
            theStatement.execute(qry);
            System.out.println("Money has been deposited...");
            new App().checkBalance(number);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    void withdrawMoney(int withdraw,int number)
    {
        qry="UPDATE customer SET account_balance=(account_balance-"+withdraw+") WHERE account_number="+number;
        try {
            if((new App().returnBalance(number)-withdraw)<1000)
            {
                System.out.println("Minimum balance should be at least 1000 ");
                System.out.println("Withdrawal unsuccessful");}
            else{
            theStatement.execute(qry);
            System.out.println("Money has been withdrawn...");
            new App().checkBalance(number);}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    int returnBalance(int number)
    {
        qry="SELECT account_balance FROM customer WHERE account_number="+number;
        int balance=0;
        try {
            resultSet=theStatement.executeQuery(qry);
            while ((resultSet.next()))
            {
                balance=resultSet.getInt("account_balance");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return balance;
    }

    void transferMoney(int transferAmount,int receiverId,int number)
    {
        qry="UPDATE customer SET account_balance=(account_balance-"+transferAmount+") WHERE account_number="+number;
        qryTemp="UPDATE customer SET account_balance=(account_balance+"+transferAmount+") WHERE account_number="+receiverId;
        try {
            if((new App().returnBalance(number)-transferAmount)<1000)
            {
                System.out.println("Minimum balance should be at least 1000 ");
                System.out.println("Transfer unsuccessful");}
            else{
                theStatement.execute(qry);
                theStatement.execute(qryTemp);
                System.out.println("Money has been transferred...");
                new App().checkBalance(number);}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
