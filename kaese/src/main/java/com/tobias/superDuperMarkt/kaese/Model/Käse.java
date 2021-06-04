package com.tobias.superDuperMarkt.kaese.Model;

import org.javamoney.moneta.Money;
import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;

import java.time.LocalDate;

public class Käse extends Produkt {

    private Money dailyPrice;
    private final LocalDate badDate;

    public Käse(Money actualPrice, int id, String productName, int quality, LocalDate date) {
        super(actualPrice, id, productName, quality, date);
        this.badDate = date;
        this.dailyPrice = actualPrice;

    }

    @Override
    public String toString() {
        return "Produkt{" +
                "price=" + this.getDailyPrice() +
                ", dailyPrice=" + dailyPrice +
                ", productName='" + getBezeichnung() + '\'' +
                ", quality=" + getQuality() +
                ", id=" + getId() +
                ", badDate=" + badDate +
                '}';
    }

}
