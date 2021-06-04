package com.tobias.superDuperMarkt.main.Utils;

import com.tobias.superDuperMarkt.CSVException.UndefinedProduktTypeException;
import com.tobias.superDuperMarkt.CSVProduktBuilderAPI.CSVProduktBuilderServiceImpl;
import com.tobias.superDuperMarkt.FrischerSalat.API.FrischerSalatServiceImpl;
import com.tobias.superDuperMarkt.FrischerSalat.Model.FrischerSalat;
import com.tobias.superDuperMarkt.GenericProdukt.API.ProduktServiceImpl;
import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;
import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadDateException;
import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadQualityException;
import com.tobias.superDuperMarkt.Wein.Service.Wein;
import com.tobias.superDuperMarkt.Wein.Service.WeinServiceImpl;
import com.tobias.superDuperMarkt.kaese.API.KäseServiceImpl;
import com.tobias.superDuperMarkt.kaese.Model.Käse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ListHelper {

    private static final Logger log = LoggerFactory.getLogger(ListHelper.class);

    static KäseServiceImpl käseService = new KäseServiceImpl();
    static WeinServiceImpl weinService = new WeinServiceImpl();
    static ProduktServiceImpl produktService = new ProduktServiceImpl();
    static FrischerSalatServiceImpl frischerSalatService = new FrischerSalatServiceImpl();

    static CSVProduktBuilderServiceImpl csvProduktBuilderService = new CSVProduktBuilderServiceImpl();

    static void addToActiveList(ArrayList<Produkt> beginningList, LocalDate simulatedDay) throws BadDateException, BadQualityException {
        for (Produkt product : beginningList) {
            if (product instanceof Käse) {
                käseService.checkKäseBadDate((Käse) product, simulatedDay);
            } else if (product instanceof Wein) {
                weinService.checkQuality((Wein) product);
            } else if (product instanceof FrischerSalat) {
                frischerSalatService.checkFrischerSalatBadDate((FrischerSalat) product, simulatedDay);
            } else produktService.checkBadDate(product, simulatedDay);
        }
    }

    static void deleteAllProductsWithBadQuality(List<Produkt> list) {

        //Use If-Condition explicitly, for Logging reasons
        list.removeIf(produkt -> {
            boolean returnValue = produkt.getRemoveInformation();
            if (returnValue) {
                log.debug("Product deleted: " + produkt.toString());
                return true;
            }
            return false;
        });

    }

    public void simulation() throws BadDateException, BadQualityException, ParseException, IOException, UndefinedProduktTypeException {
        LocalDate simulatedDay = LocalDate.of(2021, Month.JUNE, 5);
        ArrayList<Produkt> listOfProdukts = ListHelper.initate(simulatedDay);
        showStartValuesOfProducts(listOfProdukts);
        simulateDays(listOfProdukts, simulatedDay);
    }


    void showStartValuesOfProducts(List<Produkt> list) {
        for (Produkt produkt : list) {
            System.out.println((produkt.toString()));
        }
    }

    void simulateDays(List<Produkt> list, LocalDate simulatedDay) throws ParseException {
        for (int i = 1; i < 200; i++) {
            System.out.println(("Day:" + i));
            simulateDay(list, simulatedDay);
            ListHelper.deleteAllProductsWithBadQuality(list);
        }
    }

    static ArrayList initate(LocalDate simulatedDay) throws BadDateException, BadQualityException, IOException, UndefinedProduktTypeException {
        ArrayList<Produkt> list = csvProduktBuilderService.initiateList();
        addToActiveList(list, simulatedDay);
        return list;
    }

    void simulateDay(List<Produkt> list, LocalDate simulatedDay) throws ParseException {
        for (Produkt produkt : list) {
            System.out.println(produkt.toString());
            if (produkt instanceof Käse) {
                processKäse((Käse) produkt);
            } else if (produkt instanceof Wein) {
                weinService.manageInternalQualityCounter((Wein) produkt);
            } else if (produkt instanceof FrischerSalat) {
                processFrischerSalat((FrischerSalat) produkt, simulatedDay);
            } else processProduct(produkt);
        }
    }

    private static void processFrischerSalat(FrischerSalat frischerSalat, LocalDate simulatedDay) {
        frischerSalatService.checkQuality(frischerSalat);
        frischerSalatService.setDailyPriceOfFrischerSalat(frischerSalat, simulatedDay);
        frischerSalatService.needToBeRemoved(frischerSalat);
    }


    private static void processKäse(Käse käse) throws ParseException {
        käseService.checkQuality(käse);
        käseService.setDailyPriceOfKäse(käse);
        käseService.needToBeRemoved(käse);
    }

    private static void processProduct(Produkt produkt) {
        produktService.checkQuality(produkt);
        produktService.setDailyPrice(produkt);
        produktService.needToBeRemoved(produkt);
    }
}
