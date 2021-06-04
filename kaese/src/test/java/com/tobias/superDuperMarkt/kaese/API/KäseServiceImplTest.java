package com.tobias.superDuperMarkt.kaese.API;

import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;
import com.tobias.superDuperMarkt.Wein.Service.Wein;
import com.tobias.superDuperMarkt.kaese.Model.Käse;
import com.tobias.superDuperMarkt.kaese.Utils.ListHelper;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.money.Monetary.getDefaultRounding;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KäseServiceImplTest {

    KäseServiceImpl käseService = new KäseServiceImpl();
    private static final Logger log = LoggerFactory.getLogger(KäseServiceImplTest.class);

    @Test
    void checkKäseBadDateThrowError() throws Exception {
        LocalDate simulatedDay = LocalDate.of(2021, Month.JUNE, 1);
        Käse badDate = new Käse(Money.of(2.40, "EUR"), 1, "Käse", 40, LocalDate.of(2021, Month.JULY, 30));
        assertThatThrownBy(() -> {
            käseService.checkKäseBadDate(badDate, simulatedDay);
        }).hasMessage("badDate is wrong!");

    }

    @Test
    void checkKäseBadDateCorrect() {
        LocalDate simulatedDay = LocalDate.of(2021, Month.JUNE, 1);
        Käse badDate = new Käse(Money.of(3.40, "EUR"), 1, "Käse", 40, LocalDate.of(2021, Month.JULY, 2));
        assertDoesNotThrow(() -> {
            käseService.checkKäseBadDate(badDate, simulatedDay);
        });
    }

    @Test
    void checkQualityCorrect() throws Exception {
        Käse qualityCheck = new Käse(Money.of(5.90, "EUR"), 1, "Käse", 40, LocalDate.of(2021, Month.JULY, 10));
        käseService.needToBeRemoved(qualityCheck);
        assertThat(qualityCheck).isNotNull()
                .hasFieldOrPropertyWithValue("removeInformation", false);
    }

    @Test
    void checkQualityIncorrect() {
        Käse qualityCheck = new Käse(Money.of(2.35, "EUR"), 1, "Käse", 20, LocalDate.of(2021, Month.JULY, 2));


        assertThat(qualityCheck)
                .isNotNull()
                .isInstanceOf(Käse.class)
                .hasFieldOrPropertyWithValue("removeInformation", false);

        käseService.needToBeRemoved(qualityCheck);

        assertThat(qualityCheck).isNotNull()
                .hasFieldOrPropertyWithValue("removeInformation", true);
    }

    @Test
    void checkWithLoop() throws Exception {
        List<Produkt> testList = initiateTestList();

        assertThat(testList).
                isNotNull()
                .hasSize(6);

        for (int i = 1; i < 200; i++)
            for (int j = 0; j < testList.size(); j++) {
                if (testList.get(j) instanceof Käse) {
                    processKäse((Käse) testList.get(j));
                }

                //Käse has Quality under 30, after 10 Days, so it needs to removed
                ListHelper.deleteAllProductsWithBadQuality(testList);


                //use some Day and test Price mechanism
                if (i == 5 && testList.get(j).getId() == 3) {
                    Käse käse = (Käse) testList.get(j);

                    double quality = 0.1 * käse.getQuality();

                    //Respecting Cent Values and Euros
                    Money actualDailyPrice = käse.getPrice()
                            .add(Money.of(quality, "EUR"))
                            .with(getDefaultRounding());

                    assertThat(käse.getDailyPrice()).isEqualTo(Money.of(actualDailyPrice.getNumber(), "EUR"));
                }

                if (i == 12) {
                    Optional<Produkt> noKäse = assertNotKäse(testList, 3);
                    assertThat(noKäse).isEmpty();
                }
            }
    }

    private void processKäse(Käse käse) throws ParseException {
        käseService.checkQuality(käse);
        käseService.setDailyPriceOfKäse(käse);
        käseService.needToBeRemoved(käse);
    }

    public Optional<Produkt> assertNotKäse(List<Produkt> ListWithoutCertainElement, int id) {
        return ListWithoutCertainElement
                .stream()
                .filter(p -> p.getId() == id)
                .findAny();
    }

    public List<Produkt> initiateTestList() throws Exception {

        ArrayList<Produkt> beispielListe = new ArrayList<>();

        beispielListe.add(new Produkt(Money.of(2.40, "EUR"), 1, "Wurst", 50, LocalDate.of(2021, Month.JULY, 3)));
        beispielListe.add(new Wein(Money.of(5.33, "EUR"), 2, "Wein", 47));
        beispielListe.add(new Käse(Money.of(2.99, "EUR"), 3, "SchweizerKäse", 40, LocalDate.of(2021, Month.JULY, 14)));
        beispielListe.add(new Produkt(Money.of(1.50, "EUR"), 4, "Wurst", 70, LocalDate.of(2021, Month.JULY, 3)));
        beispielListe.add(new Produkt(Money.of(0.90, "EUR"), 5, "Süsses", 47, LocalDate.of(2021, Month.JULY, 14)));
        beispielListe.add(new Produkt(Money.of(2.80, "EUR"), 6, "Frischer Salat", 70, LocalDate.of(2021, Month.JULY, 14)));
        return beispielListe;
    }
}