package com.example.testspring;

import javax.persistence.*;

@Entity
@Table(name = "exchangeRates")
public class ExchangeRates {
    @Id
    @GeneratedValue
    private Long id;

    private Double uah = 1.0;

    private Double eur = 41.0;

    private Double usd = 37.0;

    public ExchangeRates() {
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
}

