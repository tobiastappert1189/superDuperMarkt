package com.tobias.superDuperMarkt.GenericProdukt.API;

import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;
import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadDateException;
import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadQualityException;

import java.time.LocalDate;

public interface IProduktService {
    void setDailyPrice(Produkt produkt);

    boolean checkQuality(Produkt produkt);

    boolean checkBadDate(Produkt produkt, LocalDate simulatedDay) throws BadDateException;

    void reduceQuality(Produkt produkt) throws BadQualityException;

    void needToBeRemoved(Produkt produkt);
}

