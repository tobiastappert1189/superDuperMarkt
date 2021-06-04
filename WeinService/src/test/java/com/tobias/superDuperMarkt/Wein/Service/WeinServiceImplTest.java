package com.tobias.superDuperMarkt.Wein.Service;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WeinServiceImplTest {

    private WeinServiceImpl weinService = new WeinServiceImpl();

    @org.junit.jupiter.api.Test
    void checkQualityAtStartNegativeExample() throws Exception {
        Produkt badWine = new Wein(Money.of(22.33, "EUR"), 3, "Wein", -3);
        assertThatThrownBy(() -> {
            weinService.checkQuality(((Wein) badWine));
        }).hasMessage("Quality is to small");
    }

    @org.junit.jupiter.api.Test
    void checkQualityAtStartPositiveExample() throws Exception {
        Produkt badWine = new Wein(Money.of(22.33, "EUR"), 3, "Wein", 40);
        assertDoesNotThrow(() -> {
            weinService.checkQuality(((Wein) badWine));
        });
    }


    @Test
    void simulateDays() throws Exception {
        List<Produkt> testList = initiateTestList();

        assertThat(testList).
                isNotNull()
                .hasSize(5);

        for (int i = 1; i < 200; i++) {
            for (int j = 0; j < testList.size(); j++) {
                if (testList.get(j) instanceof Wein) {
                    Wein wein = (Wein) testList.get(j);
                    weinService.manageInternalQualityCounter(wein);
                }

                //check that Quality is increasing from 40 to 41 after exact 10 Days
                if (i == 11 && testList.get(j) instanceof Wein) {
                    Wein wein = (Wein) testList.get(j);
                    assertThat(wein.getQuality()).isEqualTo(41);
                }

                //check if Wine gets Deleted by Time
                //check if Wine Quality is not Increasing after reaching 50
                if (i == 180 && testList.get(j).getId() == 2) {
                    Optional<Produkt> correctWeinInList = assertWein(testList, 2);
                    assertThat(correctWeinInList).isNotEmpty();
                    assertThat(testList.get(j).getQuality()).isEqualTo(50);
                }
            }
        }
    }

    public Optional<Produkt> assertWein(List<Produkt> ListWithoutCertainElement, int id) {
        return ListWithoutCertainElement
                .stream()
                .filter(p -> p.getId() == id)
                .findAny();
    }

    public List<Produkt> initiateTestList() throws Exception {
        ArrayList<Produkt> testList = new ArrayList<>();

        testList.add(new Produkt(Money.of(2.40, "EUR"), 1, "Wurst", 50, LocalDate.of(2021, Month.JULY, 3)));
        testList.add(new Wein(Money.of(3.80, "EUR"), 2, "Wein", 40));
        testList.add(new Produkt(Money.of(2.45, "EUR"), 3, "Wurst", 70, LocalDate.of(2021, Month.JULY, 3)));
        testList.add(new Produkt(Money.of(2.50, "EUR"), 4, "Wurst", 70, LocalDate.of(2021, Month.JULY, 3)));
        testList.add(new Produkt(Money.of(10.20, "EUR"), 6, "Frischer Salat", 70, LocalDate.of(2021, Month.JULY, 14)));
        return testList;
    }
}