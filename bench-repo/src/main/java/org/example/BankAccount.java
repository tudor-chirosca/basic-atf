package org.example;

public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance){
        this.balance = initialBalance;
    }

    public void deposit (double amount){
        if (amount > 0){
            balance += amount;
            System.out.println(amount + " deposited successfully.");
        } else {
            System.out.println("Invalid amount to deposit.");
        }
    }

    public void withdraw (double amount){
        if (amount > 0 && amount <= balance){
            balance -= amount;
            System.out.println(amount + " withdrawn successfully.");
        }else {
            System.out.println("Insufficient funds or invalid amount to withdraw.");
        }
    }

    public double getBalance(){
        return balance;
    }
}
