package com.tobias.superDuperMarkt.kaese.API;

import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadDateException;
import com.tobias.superDuperMarkt.kaese.Model.Käse;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.AmountFormatParams;

import javax.money.*;
import javax.money.format.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

import static javax.money.Monetary.*;

public class KäseServiceImpl implements IKäseService {

    @Override
    public boolean checkKäseBadDate(Käse käse, LocalDate simulatedDay) throws BadDateException {
        LocalDate actualDate = simulatedDay;
        LocalDate lastPossibleDate = actualDate.plusDays(51); // count +1 to get the last day
        LocalDate requiredDate = actualDate.plusDays(29); // substract with 1 to get also 30 days.

        if (!(käse.getBadDate().isBefore(lastPossibleDate) && käse.getBadDate().isAfter(requiredDate))) {
            throw new BadDateException("badDate is wrong!");
        }
        return true;
    }

    @Override
    public boolean checkQuality(Käse käse) {
        return käseProductQuality(käse.getQuality());
    }

    @Override
    public void reduceQuality(Käse käse) {
        int reducingQuality = käse.getQuality() - 1;
        käse.setQuality(reducingQuality);
    }

    @Override
    public void setDailyPriceOfKäse(Käse käse) throws ParseException {
        reduceQuality(käse);

        double quality = 0.1 * käse.getQuality();

        //Respecting Cent Values and Euros
        Money actualDailyPrice = käse.getPrice()
                .add(Money.of(quality, "EUR"))
                .with(getDefaultRounding());

        käse.setDailyPrice(actualDailyPrice);

    }
    @Override
    public void needToBeRemoved(Käse käse) {
        if (checkQuality(käse)) {
            käse.setRemoveInformation(true);
        }
    }

    private boolean käseProductQuality(int quality) {
        return quality < 30;
    }
}
