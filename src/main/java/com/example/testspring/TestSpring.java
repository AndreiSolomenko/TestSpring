package com.example.testspring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@RestController
public class TestSpring {
    static EntityManagerFactory emf;
    static EntityManager em;

    @GetMapping("/test")
    public String showMessage() {
        String message = "View all transactions by endpoint: /transactions. " +
                         "View total balance all clients by endpoint: /total-balance.";
        return message;
    }

    @GetMapping("/transactions")
    public List<Transactions> viewAllTransactions() {
        emf = Persistence.createEntityManagerFactory("BankDataBase");
        em = emf.createEntityManager();
        List<Transactions> transactions = em.createQuery("SELECT t FROM Transactions t", Transactions.class).getResultList();
        em.close();
        return transactions;
    }

    @GetMapping("/total-balance")
    public Double viewTotalBalanceAllClients() {

        emf = Persistence.createEntityManagerFactory("BankDataBase");
        em = emf.createEntityManager();

        double totalBalance = 0.0;

        List<Client> clients = em.createQuery("SELECT a FROM Client a", Client.class).getResultList();

        ExchangeRates er = new ExchangeRates();
        for (Client client : clients) {
            totalBalance += client.getAccounts().getUah();
            totalBalance += client.getAccounts().getEur() * er.getEur();
            totalBalance += client.getAccounts().getUsd() * er.getUsd();
        }
        em.close();

        return totalBalance;
    }
}

