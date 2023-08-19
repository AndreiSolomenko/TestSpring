package com.example.testspring;

import javax.persistence.*;

@Entity
@Table(name="transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;

    private String currency;

    private String sum;

    public Transactions() {
    }

    public Transactions(Long clientId, String currency, String sum) {
        this.clientId = clientId;
        this.currency = currency;
        this.sum = sum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClient() {
        return clientId;
    }

    public void setClient(Long client) {
        this.clientId = clientId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + id +
                ", client='" + clientId + '\'' +
                ", currency='" + currency + '\'' +
                ", sum='" + sum + '\'' +
                '}';
    }
}
