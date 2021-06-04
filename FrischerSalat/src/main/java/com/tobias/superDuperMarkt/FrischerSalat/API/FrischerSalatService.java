package com.tobias.superDuperMarkt.FrischerSalat.API;

import com.tobias.superDuperMarkt.FrischerSalat.Model.FrischerSalat;
import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadDateException;

import java.time.LocalDate;

public interface FrischerSalatService {

    boolean checkFrischerSalatBadDate(FrischerSalat frischerSalat, LocalDate localDate) throws BadDateException;

    boolean checkQuality(FrischerSalat frischerSalat);

    void reduceQuality(FrischerSalat frischerSalat);

    void needToBeRemoved(FrischerSalat frischerSalat);

    void setDailyPriceOfFrischerSalat(FrischerSalat frischerSalat, LocalDate simulatedDay);
}
