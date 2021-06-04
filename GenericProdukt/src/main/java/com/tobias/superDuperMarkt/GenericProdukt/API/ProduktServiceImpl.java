package com.tobias.superDuperMarkt.GenericProdukt.API;

import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadDateException;
import org.javamoney.moneta.Money;
import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;

import javax.money.Monetary;
import java.time.LocalDate;

public class ProduktServiceImpl implements IProduktService {

    @Override
    public boolean checkBadDate(Produkt produkt, LocalDate simulatedDay) throws BadDateException {
        LocalDate lastPossibleDate = simulatedDay.plusDays(365); // count +1 to get the last day
        LocalDate requiredDate = simulatedDay.plusDays(1); // substract with 1 to get also 30 days.

        if (!(produkt.getBadDate().isBefore(lastPossibleDate) && produkt.getBadDate().isAfter(requiredDate))) {
            throw new BadDateException("badDate is wrong!");
        }
        return true;
    }

    @Override
    public boolean checkQuality(Produkt produkt) {
        return productQuality(produkt.getQuality());
    }

    public void reduceQuality(Produkt produkt) {
        int reducingQuality = produkt.getQuality() - 1;
        produkt.setQuality(reducingQuality);
    }

    @Override
    public void setDailyPrice(Produkt produkt) {
        reduceQuality(produkt);

        double quality = 0.1 * produkt.getQuality();

        //Respecting Cent Values and Euros
        Money actualDailyPrice = produkt.getPrice()
                .add(Money.of(quality, "EUR"))
                .with(Monetary.getDefaultRounding());

        produkt.setDailyPrice(actualDailyPrice);
    }

    public void needToBeRemoved(Produkt produkt) {
        if (checkQuality(produkt)) {
            produkt.setRemoveInformation(true);
        }
    }

    private boolean productQuality(int quality) {
        return quality <= 0;
    }
}


