package com.tobias.superDuperMarkt.FrischerSalat.API;

import com.tobias.superDuperMarkt.FrischerSalat.Model.FrischerSalat;
import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadDateException;
import org.javamoney.moneta.Money;

import java.time.LocalDate;

import static javax.money.Monetary.getDefaultRounding;

public class FrischerSalatServiceImpl implements FrischerSalatService {
    @Override
    public boolean checkFrischerSalatBadDate(FrischerSalat frischerSalat, LocalDate simulatedDay) throws BadDateException {
        LocalDate actualDate = simulatedDay;
        LocalDate lastPossibleDate = actualDate.plusDays(4); // count +1 to get the last day
        LocalDate requiredDate = actualDate.plusDays(1); // substract with 1 to get also 30 days.

        if (!(frischerSalat.getBadDate().isBefore(lastPossibleDate) && frischerSalat.getBadDate().isAfter(requiredDate))) {
            throw new BadDateException("badDate is wrong!");
        }
        return true;
    }

    @Override
    public boolean checkQuality(FrischerSalat frischerSalat) {
        return frischerSalatProductQualityCheck(frischerSalat.getQuality());
    }

    @Override
    public void reduceQuality(FrischerSalat frischerSalat) {
        int reducingQuality = frischerSalat.getQuality() - 1;
        frischerSalat.setQuality(reducingQuality);

    }

    //set removeInformation to true if Quality is under a defined value
    @Override
    public void needToBeRemoved(FrischerSalat frischerSalat) {
        if (checkQuality(frischerSalat)) {
            frischerSalat.setRemoveInformation(true);
        }
    }

    @Override
    public void setDailyPriceOfFrischerSalat(FrischerSalat frischerSalat, LocalDate simulatedDate) {

        //If Product has last Possible badDate, the price get divided by 2, as a special Price
        if (checkIfLastDayForFrischerSalat(frischerSalat, simulatedDate)) {
            Money dailyPrice = calcDailyPrice(frischerSalat)
                    .divide(2)
                    .with(getDefaultRounding());

            frischerSalat.setDailyPrice(dailyPrice);
        } else frischerSalat.setDailyPrice(calcDailyPrice(frischerSalat));
    }

    public boolean checkIfLastDayForFrischerSalat(FrischerSalat frischerSalat, LocalDate simulatedDate) {
        return frischerSalat.getBadDate().isEqual(simulatedDate);
    }

    private boolean frischerSalatProductQualityCheck(int quality) {
        return quality < 20;
    }

    private Money calcDailyPrice(FrischerSalat frischerSalat) {
        reduceQuality(frischerSalat);

        double quality = 0.1 * frischerSalat.getQuality();

        //Respecting Cent Values and Euros
        return frischerSalat.getPrice()
                .add(Money.of(quality, "EUR")).with(getDefaultRounding());
    }
}

