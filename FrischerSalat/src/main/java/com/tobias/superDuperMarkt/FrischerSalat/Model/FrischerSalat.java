package com.tobias.superDuperMarkt.FrischerSalat.Model;

import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;
import org.javamoney.moneta.Money;

import java.time.LocalDate;

public class FrischerSalat extends Produkt {
    public FrischerSalat(Money price, int id, String productName, int quality, LocalDate badDate) {
        super(price, id, productName, quality, badDate);
    }
}
