package com.tobias.superDuperMarkt.GenericProdukt.Service;

import com.tobias.superDuperMarkt.GenericProdukt.API.ProduktServiceImpl;
import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadDateException;
import com.tobias.superDuperMarkt.GenericProdukt.Utils.ListHelper;
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

class ProduktServiceImplTest {

    private final ProduktServiceImpl produktService = new ProduktServiceImpl();

    @Test
    void checkGenericProductBadDateNegativeExample() throws Exception {
        LocalDate simulatedDay = LocalDate.of(2021, Month.JUNE, 1);
        Produkt badDate = new Produkt(Money.of(3, "EUR"), 3, "GenericProdukt", 40, LocalDate.of(1999, Month.JULY, 30));
        assertThatThrownBy(() -> {
            produktService.checkBadDate(badDate, simulatedDay);
        }).isInstanceOf(BadDateException.class).hasMessage("badDate is wrong!");

    }

    @Test
    void checkGenericProductBadDatePositiveExample() throws Exception {
        LocalDate simulatedDay = LocalDate.of(2021, Month.JUNE, 1);
        Produkt badDate = new Produkt(Money.of(3, "EUR"), 3, "GenericProdukt", 40, LocalDate.of(2021, Month.JULY, 2));
        assertDoesNotThrow(() -> {
            produktService.checkBadDate(badDate, simulatedDay);
        });
    }

    @Test
    void checkQualityCorrect() throws Exception {
        Produkt qualityCheck = new Produkt(Money.of(3, "EUR"), 3, "GenericProdukt", 40, LocalDate.of(2021, Month.JULY, 2));
        produktService.needToBeRemoved(qualityCheck);
        assertThat(qualityCheck).isNotNull()
                .hasFieldOrPropertyWithValue("removeInformation", false);
    }

    @Test
    void checkQualityNotCorrect() {
        Produkt qualityCheck = new Produkt(Money.of(3, "EUR"), 3, "GenericProdukt", -3, LocalDate.of(2021, Month.JULY, 2));

        assertThat(qualityCheck)
                .isNotNull()
                .isInstanceOf(Produkt.class)
                .hasFieldOrPropertyWithValue("removeInformation", false);

        produktService.needToBeRemoved(qualityCheck);

        assertThat(qualityCheck).isNotNull()
                .hasFieldOrPropertyWithValue("removeInformation", true);
    }

    @Test
    void simulateDays() {
        List<Produkt> testList = initiateTestList();

        assertThat(testList).
                isNotNull()
                .hasSize(4);

        for (int i = 1; i < 200; i++) {
            for (int j = 0; j < testList.size(); j++) {

                processProdukt(testList.get(j));

                ListHelper.deleteAllProductswithBadQuality(testList);

                // use some Day and test Price mechanism
                if (i == 5 && testList.get(j).getId() == 3) {
                    Produkt produkt = testList.get(j);

                    double quality = 0.1 * produkt.getQuality();

                    //Respecting Cent Values and Euros
                    //calculate Price
                    Money actualDailyPrice = produkt.getPrice()
                            .add(Money.of(quality, "EUR"))
                            .with(getDefaultRounding());

                    assertThat(produkt.getDailyPrice()).isEqualTo(Money.of(actualDailyPrice.getNumber(), "EUR"));
                }

                //check if Product get Removed after condition is meet.Produkt gets removed because Quality is under 0.
                if (i == 13) {
                    Optional<Produkt> noGenericSpecificProdukt = assertSpecificIsProduktIsMissing(testList, 3);
                    assertThat(noGenericSpecificProdukt).isEmpty();
                }
            }
        }
    }

    private void processProdukt(Produkt produkt) {
        produktService.checkQuality(produkt);
        produktService.setDailyPrice(produkt);
        produktService.needToBeRemoved(produkt);
    }

    private Optional<Produkt> assertSpecificIsProduktIsMissing(List<Produkt> ListWithoutCertainElement, int id) {
        return ListWithoutCertainElement
                .stream()
                .filter(p -> p.getId() == id)
                .findAny();
    }

    private List<Produkt> initiateTestList() {

        ArrayList<Produkt> testList = new ArrayList<>();

        testList.add(new Produkt(Money.of(3, "EUR"), 1, "GenericProdukt", 50, LocalDate.of(2021, Month.JULY, 4)));
        testList.add(new Produkt(Money.of(3.30, "EUR"), 2, "proteinBrot", 70, LocalDate.of(2021, Month.JULY, 8)));
        testList.add(new Produkt(Money.of(2.99, "EUR"), 3, "Süsses", 12, LocalDate.of(2021, Month.JULY, 14)));
        testList.add(new Produkt(Money.of(5.50, "EUR"), 4, "Frischer Salat", 70, LocalDate.of(2021, Month.JULY, 14)));
        return testList;
    }
}