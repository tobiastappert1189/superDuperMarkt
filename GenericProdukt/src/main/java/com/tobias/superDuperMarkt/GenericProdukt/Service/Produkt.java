package com.tobias.superDuperMarkt.GenericProdukt.Service;

import org.javamoney.moneta.Money;

import java.time.LocalDate;

public class Produkt extends RemovableProducts {
    private Money dailyPrice;
    private int quality;
    private int id;
    private LocalDate badDate;
    private String type;
    private final Money price;
    private final String bezeichnung;


    public Produkt(Money price, int id, String bezeichnung, int quality, LocalDate badDate) {
        super(price, id, bezeichnung, quality);
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.quality = quality;
        this.price = price;
        this.badDate = badDate;
        this.dailyPrice = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public Money getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBadDate(LocalDate badDate) {
        this.badDate = badDate;
    }

    public LocalDate getBadDate() {
        return badDate;
    }

    public Money getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(Money dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    @Override
    public String toString() {
        return "Produkt{" +
                "price=" + price +
                ", dailyPrice=" + dailyPrice +
                ", productName='" + bezeichnung + '\'' +
                ", quality=" + quality +
                ", id=" + id +
                ", badDate=" + badDate +
                '}';
    }
}
