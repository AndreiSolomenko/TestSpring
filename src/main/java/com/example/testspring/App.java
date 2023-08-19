package com.example.testspring;

import javax.persistence.*;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {

    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main( String[] args ) {
        Scanner sc = new Scanner(System.in);

        try {
            emf = Persistence.createEntityManagerFactory("BankDataBase");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add client");
                    System.out.println("2: add random clients");
                    System.out.println("3: view client");
                    System.out.println("4: account replenishment");
                    System.out.println("5: transfer from account to account");
                    System.out.println("6: currencies converted inside client accounts");
                    System.out.println("7: total amount on client accounts in UAH");
                    System.out.println("8: view all transactions");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addClient(sc);
                            break;
                        case "2":
                            insertRandomClients(sc);
                            break;
                        case "3":
                            viewClients();
                            break;
                        case "4":
                            accountReplenishment(sc);
                            break;
                        case "5":
                            transferFromAccountToAccount(sc);
                            break;
                        case "6":
                            currenciesConvertedInsideClientAccounts(sc);
                            break;
                        case "7":
                            totalAmountOnClientAccountsInUah(sc);
                            break;
                        case "8":
                            viewAllTransactions();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void addClient(Scanner sc) {
        System.out.print("Enter client name: ");
        String name = sc.nextLine();
        System.out.print("Enter client age: ");
        String sAge = sc.nextLine();
        int age = Integer.parseInt(sAge);

        em.getTransaction().begin();
        try {
            Client c = new Client(name, age);
            Accounts a = new Accounts(0.0, 0.0, 0.0);
            c.setAccounts(a);
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void insertRandomClients(Scanner sc) {
        System.out.print("Enter quantity of clients: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);

        em.getTransaction().begin();
        try {
            for (int i = 0; i < count; i++) {
                Client c = new Client(randomName(), RND.nextInt(100));
                Accounts a = new Accounts(0.0, 0.0, 0.0);
                c.setAccounts(a);
                em.persist(c);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    static final String[] MEALS_NAMES = {"George", "John", "Thomas", "James", "Andrew"};
    static final Random RND = new Random();

    static String randomName() {
        return MEALS_NAMES[RND.nextInt(MEALS_NAMES.length)];
    }


    private static void viewClients() {
        Query query = em.createQuery("SELECT c FROM Client c", Client.class);
        List<Client> list = (List<Client>) query.getResultList();

        for (Client c : list)
            System.out.println(c);
    }

    private static void accountReplenishment(Scanner sc) {
        System.out.print("Enter the ID of the client whose account will be replenished: ");
        String sId = sc.nextLine();
        Long id = Long.parseLong(sId);
        System.out.print("Enter currency for replenish account (UAH, EUR, USD): ");
        String currency = sc.nextLine().toLowerCase();
        System.out.print("Enter the replenishment amount: ");
        String sSum = sc.nextLine();
        Double sum = Double.parseDouble(sSum);

        Client c = em.getReference(Client.class, id);

        em.getTransaction().begin();

        if (currency.equals("uah")) {
            c.getAccounts().setUah(c.getAccounts().getUah() + sum);
        } else if (currency.equals("eur")) {
            c.getAccounts().setEur(c.getAccounts().getEur() + sum);
        } else if (currency.equals("usd")) {
            c.getAccounts().setUsd(c.getAccounts().getUsd() + sum);
        }

        Transactions transaction = new Transactions(id, currency, "+" + sSum);
        em.persist(transaction);

        em.getTransaction().commit();
    }

    private static void transferFromAccountToAccount(Scanner sc) {
        System.out.print("Enter the ID of the client whose account will be debited: ");
        String sIdFrom = sc.nextLine();
        Long idFrom = Long.parseLong(sIdFrom);
        System.out.print("Enter the ID of the client whose account will be replenished: ");
        String sId = sc.nextLine();
        Long id = Long.parseLong(sId);
        System.out.print("Enter currency for replenish account (UAH, EUR, USD): ");
        String currency = sc.nextLine().toLowerCase();
        System.out.print("Enter the replenishment amount: ");
        String sSum = sc.nextLine();
        Double sum = Double.parseDouble(sSum);

        Client cFrom = em.getReference(Client.class, idFrom);
        Client cTo = em.getReference(Client.class, id);

        em.getTransaction().begin();

        if (currency.equals("uah")) {
            cFrom.getAccounts().setUah(cFrom.getAccounts().getUah() - sum);
            cTo.getAccounts().setUah(cTo.getAccounts().getUah() + sum);
        } else if (currency.equals("eur")) {
            cFrom.getAccounts().setEur(cFrom.getAccounts().getEur() - sum);
            cTo.getAccounts().setEur(cTo.getAccounts().getEur() + sum);
        } else if (currency.equals("usd")) {
            cFrom.getAccounts().setUsd(cFrom.getAccounts().getUsd() - sum);
            cTo.getAccounts().setUsd(cTo.getAccounts().getUsd() + sum);
        }

        Transactions transactionFrom = new Transactions(idFrom, currency, "-" + sSum);
        em.persist(transactionFrom);
        Transactions transactionTo = new Transactions(id, currency, "+" + sSum);
        em.persist(transactionTo);

        em.getTransaction().commit();
    }

    private static void currenciesConvertedInsideClientAccounts(Scanner sc) {
        System.out.print("Enter the ID of the client whose accounts currencies will be converted: ");
        String sId = sc.nextLine();
        Long id = Long.parseLong(sId);
        System.out.print("Enter currency account which will be debited (UAH, EUR, USD): ");
        String currencyFrom = sc.nextLine().toLowerCase();
        System.out.print("Enter currency account which will be replenished (UAH, EUR, USD): ");
        String currencyTo = sc.nextLine().toLowerCase();
        System.out.print("Enter the replenishment amount: ");
        String sSum = sc.nextLine();
        Double sum = Double.parseDouble(sSum);

        Client c = em.getReference(Client.class, id);

        em.getTransaction().begin();

        ExchangeRates er = new ExchangeRates();
        Transactions transactionTo = null;

        if (currencyFrom.equals("uah")) {
            c.getAccounts().setUah(c.getAccounts().getUah() - sum);

            if (currencyTo.equals("uah")) {
                c.getAccounts().setUah(c.getAccounts().getUah() + sum);
                transactionTo = new Transactions(id, currencyTo, "+" + sum);
            } else if (currencyTo.equals("eur")) {
                c.getAccounts().setEur(c.getAccounts().getEur() + (sum / er.getEur()));
                transactionTo = new Transactions(id, currencyTo, "+" + (sum / er.getEur()));
            } else if (currencyTo.equals("usd")) {
                c.getAccounts().setUsd(c.getAccounts().getUsd() + (sum / er.getUsd()));
                transactionTo = new Transactions(id, currencyTo, "+" + (sum / er.getUsd()));
            }

        } else if (currencyFrom.equals("eur")) {
            c.getAccounts().setEur(c.getAccounts().getEur() - sum);

            if (currencyTo.equals("uah")) {
                c.getAccounts().setUah(c.getAccounts().getUah() + (sum * er.getEur()));
                transactionTo = new Transactions(id, currencyTo, "+" + (sum * er.getEur()));
            } else if (currencyTo.equals("eur")) {
                c.getAccounts().setEur(c.getAccounts().getEur() + sum);
                transactionTo = new Transactions(id, currencyTo, "+" + sum);
            } else if (currencyTo.equals("usd")) {
                c.getAccounts().setUsd(c.getAccounts().getUsd() + (sum * (er.getEur() / er.getUsd())));
                transactionTo = new Transactions(id, currencyTo, "+" + (er.getEur() / er.getUsd()));
            }

        } else if (currencyFrom.equals("usd")) {
            c.getAccounts().setUsd(c.getAccounts().getUsd() - sum);

            if (currencyTo.equals("uah")) {
                c.getAccounts().setUah(c.getAccounts().getUah() + (sum * er.getUsd()));
                transactionTo = new Transactions(id, currencyTo, "+" + (sum * er.getUsd()));
            } else if (currencyTo.equals("eur")) {
                c.getAccounts().setEur(c.getAccounts().getEur() + (sum * (er.getUsd() / er.getEur())));
                transactionTo = new Transactions(id, currencyTo, "+" + (er.getUsd() / er.getEur()));
            } else if (currencyTo.equals("usd")) {
                c.getAccounts().setUsd(c.getAccounts().getUsd() + sum);
                transactionTo = new Transactions(id, currencyTo, "+" + sum);
            }
        }

        Transactions transactionFrom = new Transactions(id, currencyFrom, "-" + sSum);
        em.persist(transactionFrom);
        em.persist(transactionTo);

        em.getTransaction().commit();
    }

    private static void totalAmountOnClientAccountsInUah(Scanner sc) {
        System.out.print("Enter the ID of the client for view total amount on accounts in UAH: ");
        String sId = sc.nextLine();
        Long id = Long.parseLong(sId);

        Client c = em.getReference(Client.class, id);

        Double res;

        em.getTransaction().begin();

        ExchangeRates er = new ExchangeRates();

        res = (c.getAccounts().getUah() * er.getUah()) +
                (c.getAccounts().getEur() * er.getEur()) +
                (c.getAccounts().getUsd() * er.getUsd());


        System.out.println("Total amount on client accounts in UAH: " + res);

        em.getTransaction().commit();
    }

    private static void viewAllTransactions() {
        Query query = em.createQuery("SELECT c FROM Transactions c", Transactions.class);
        List<Transactions> list = (List<Transactions>) query.getResultList();

        for (Transactions c : list)
            System.out.println(c);

    }

}
