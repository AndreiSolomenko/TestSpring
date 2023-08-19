package com.example.testspring;

import javax.persistence.*;

@Entity
@Table(name="client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int age;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "accounts_id")
    private Accounts accounts;

    public Client() {}

    public Client(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Accounts getAccounts() { return accounts; }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", Accounts{" +
                "UAH=" + accounts.getUah() +
                ", EUR=" + accounts.getEur() +
                ", USD=" + accounts.getUsd() + '}' +
                '}';
    }
}
