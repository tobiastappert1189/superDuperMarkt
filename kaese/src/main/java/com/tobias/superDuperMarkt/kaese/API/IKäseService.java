package com.tobias.superDuperMarkt.kaese.API;

import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadDateException;
import com.tobias.superDuperMarkt.kaese.Model.Käse;

import java.text.ParseException;
import java.time.LocalDate;

public interface IKäseService {

    boolean checkKäseBadDate(Käse käse, LocalDate localDate) throws BadDateException;

    boolean checkQuality(Käse käse);

    void reduceQuality(Käse käse);

    void setDailyPriceOfKäse(Käse käse) throws ParseException;

    void needToBeRemoved(Käse käse);
}
