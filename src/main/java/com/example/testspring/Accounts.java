package com.example.testspring;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Accounts {
    @Id
    @GeneratedValue
    private Long id;

    private Double uah;

    private Double eur;

    private Double usd;

    @OneToOne(mappedBy = "accounts")
    private Client client;

    public Accounts() {
    }

    public Accounts(Double uah, Double eur, Double usd) {
        this.uah = uah;
        this.eur = eur;
        this.usd = usd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getUah() {
        return uah;
    }

    public void setUah(Double uah) {
        this.uah = uah;
    }

    public Double getEur() {
        return eur;
    }

    public void setEur(Double eur) {
        this.eur = eur;
    }

    public Double getUsd() {
        return usd;
    }

    public void setUsd(Double usd) {
        this.usd = usd;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}