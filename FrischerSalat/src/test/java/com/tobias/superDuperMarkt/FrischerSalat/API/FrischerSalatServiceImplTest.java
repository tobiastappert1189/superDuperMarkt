package com.tobias.superDuperMarkt.FrischerSalat.API;

import com.tobias.superDuperMarkt.FrischerSalat.Model.FrischerSalat;
import com.tobias.superDuperMarkt.FrischerSalat.Utils.ListHelper;
import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;
import com.tobias.superDuperMarkt.Wein.Service.Wein;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.money.Monetary.getDefaultRounding;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FrischerSalatServiceImplTest {

    FrischerSalatServiceImpl frischerSalatService = new FrischerSalatServiceImpl();

    @Test
    void checkFrischerSalatBadDate() {
        LocalDate simulatedDay = LocalDate.of(2021, Month.JUNE, 1);
        FrischerSalat badDate = new FrischerSalat(Money.of(2.30, "EUR"), 3, "FrischerSalat", 40, LocalDate.of(2021, Month.JULY, 30));
        assertThatThrownBy(() -> {
            frischerSalatService.checkFrischerSalatBadDate(badDate, simulatedDay);
        }).hasMessage("badDate is wrong!");
    }

    @Test
    void checkKäseBadDateCorrect() {
        LocalDate simulatedDay = LocalDate.of(2021, Month.JUNE, 1);
        FrischerSalat badDate = new FrischerSalat(Money.of(2.30, "EUR"), 3, "Käse", 40, LocalDate.of(2021, Month.JUNE, 3));
        assertDoesNotThrow(() -> {
            frischerSalatService.checkFrischerSalatBadDate(badDate, simulatedDay);
        });
    }

    @Test
    void simulateDays() throws Exception {
        List<Produkt> testList = initiateTestList();
        LocalDate simulatedDay = LocalDate.of(2021, Month.JUNE, 1);

        assertThat(testList).
                isNotNull()
                .hasSize(2);

        int i = 1;
        while (i < 200) {
            for (int j = 0; j < testList.size(); j++) {

                //Frischersalat has Quality under 30, after 10 Days, so it needs to removed
                if (testList.get(j) instanceof FrischerSalat) {
                    processFrischerSalat((FrischerSalat) testList.get(j), simulatedDay);
                }

                //after looping check if it needs to be removed
                ListHelper.deleteAllProductswithBadQuality(testList);

                // use some Day and test Price mechanism
                if (i == 5 && testList.get(j).getId() == 3) {
                    FrischerSalat frischerSalat = (FrischerSalat) testList.get(j);

                    double quality = 0.1 * frischerSalat.getQuality();

                    //Respecting Cent Values and Euros
                    Money actualDailyPrice = frischerSalat.getPrice()
                            .add(Money.of(quality, "EUR"))
                            .with(getDefaultRounding());

                    assertThat(frischerSalat.getDailyPrice()).isEqualTo(Money.of(actualDailyPrice.getNumber(), "EUR"));
                }

                if (i == 12) {
                    Optional<Produkt> noFrischerSalatInList = assertNoFrischerSalat(testList);
                    assertThat(noFrischerSalatInList).isEmpty();
                }

            }
            i++;
        }
    }

    @Test
    void checkQualityCorrect() {
        FrischerSalat qualityCheck = new FrischerSalat(Money.of(2.30, "EUR"), 3, "FrischerSalat", 30, LocalDate.of(2021, Month.JULY, 10));
        frischerSalatService.needToBeRemoved(qualityCheck);
        assertThat(qualityCheck).isNotNull()
                .hasFieldOrPropertyWithValue("removeInformation", false);
    }

    @Test
    void checkQualityIncorrect() throws Exception {
        FrischerSalat qualityCheck = new FrischerSalat(Money.of(2.30, "EUR"), 3, "FrischerSalat", 10, LocalDate.of(2021, Month.JULY, 2));

        assertThat(qualityCheck)
                .isNotNull()
                .isInstanceOf(FrischerSalat.class)
                .hasFieldOrPropertyWithValue("removeInformation", false);

        frischerSalatService.needToBeRemoved(qualityCheck);

        assertThat(qualityCheck).isNotNull()
                .hasFieldOrPropertyWithValue("removeInformation", true);
    }

    @Test
    void checkLastDaySpecialPrice() {
        FrischerSalat lastDaySpecialPrice = new FrischerSalat(Money.of(2.30, "EUR"), 3, "FrischerSalat", 10, LocalDate.of(2021, Month.JUNE, 5));
        LocalDate simulatedDay = LocalDate.of(2021, Month.JUNE, 5);

        //need to reduce because during the process it reduces the quality to.
        frischerSalatService.reduceQuality(lastDaySpecialPrice);


        double quality = 0.1 * lastDaySpecialPrice.getQuality();

        Money actualDailyPrice = lastDaySpecialPrice.getPrice()
                .add(Money.of(quality, "EUR"))
                .divide(2)
                .with(getDefaultRounding());

        lastDaySpecialPrice.setQuality(10);

        frischerSalatService.setDailyPriceOfFrischerSalat((FrischerSalat) lastDaySpecialPrice, simulatedDay);

        //assert that quality is reduced by the last day .
        assertThat(lastDaySpecialPrice.getDailyPrice()).isEqualTo(Money.of(actualDailyPrice.getNumber(), "EUR"));
    }

    private void processFrischerSalat(FrischerSalat frischerSalat, LocalDate simulatedDay) {
        frischerSalatService.checkQuality(frischerSalat);
        frischerSalatService.setDailyPriceOfFrischerSalat(frischerSalat, simulatedDay);
        frischerSalatService.needToBeRemoved(frischerSalat);
    }

    private Optional<Produkt> assertNoFrischerSalat(List<Produkt> ListWithoutCertainElement) {
        return ListWithoutCertainElement
                .stream()
                .filter(p -> p.getId() == 1)
                .findAny();
    }

    public List<Produkt> initiateTestList() throws Exception {
        ArrayList<Produkt> exampleList = new ArrayList<>();
        exampleList.add(new FrischerSalat(Money.of(2.30, "EUR"), 1, "Frischer Salat", 30, LocalDate.of(2021, Month.JUNE, 7)));
        exampleList.add(new Wein(Money.of(10, "EUR"), 2, "Wein", 47));
        return exampleList;
    }
}