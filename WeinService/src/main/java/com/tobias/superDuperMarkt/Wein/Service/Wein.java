package com.tobias.superDuperMarkt.Wein.Service;

import org.javamoney.moneta.Money;
import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;

public class Wein extends Produkt {
    private int internalQualityCounter;

    public Wein(Money price, int id, String productName, int quality) {
        super(price, id, productName, quality, null);
        setInternalQualityCounter(0);
    }
    public int getInternalQualityCounter() {
        return internalQualityCounter;
    }

    public void setInternalQualityCounter(int internalQualityCounter) {
        this.internalQualityCounter = internalQualityCounter;
    }
}
