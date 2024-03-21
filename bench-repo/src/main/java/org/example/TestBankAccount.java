package org.example;

public class TestBankAccount {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(0);
        account.deposit(1000.75);
        account.deposit(2500.35);
        account.withdraw(500);
        account.deposit(0);
        account.withdraw(5000);
//        account.balance = 15000;

        System.out.println(account.getBalance());
    }
}
