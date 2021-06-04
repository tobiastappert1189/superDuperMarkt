package com.tobias.superDuperMarkt.CSVProduktBuilderAPI;

import com.tobias.superDuperMarkt.CSVException.UndefinedProduktTypeException;
import com.tobias.superDuperMarkt.FrischerSalat.Model.FrischerSalat;
import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;
import com.tobias.superDuperMarkt.Wein.Service.Wein;
import com.tobias.superDuperMarkt.kaese.Model.Käse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.javamoney.moneta.Money;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;

public class CSVProduktBuilderServiceImpl implements CSVProduktBuilderService {

    @Override
    public ArrayList<Produkt> initiateList() throws IOException, UndefinedProduktTypeException {
        //combine users dir path with relative path
        String file = System.getProperty("user.dir") + "\\CSVDataRead\\src/main/java/com/tobias/superDuperMarkt/Utils/Testdatei.csv";
        Reader in = new FileReader(file);

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(in);
        return iterate(records);
    }

    @Override
    public ArrayList<Produkt> iterate(Iterable<CSVRecord> records) throws UndefinedProduktTypeException {
        ArrayList<Produkt> list = new ArrayList<>();

        for (CSVRecord record : records) {
            switch (record.get(0)) {
                case "GenericProdukt":
                    Produkt genericProdukt = new Produkt(Money.from((Money.parse("EUR " + record.get(1)))), (Integer.parseInt(record.get(2))), record.get(3), (Integer.parseInt(record.get(4))), LocalDate.parse(record.get(5)));
                    genericProdukt.setType(record.get(0));
                    list.add(genericProdukt);
                    break;
                case "Wein":
                    Produkt wein = new Wein(Money.from((Money.parse("EUR " + record.get(1)))), (Integer.parseInt(record.get(2))), record.get(3), Integer.parseInt(record.get(4)));
                    wein.setType(record.get(0));
                    list.add(wein);
                    break;
                case "Käse": {
                    Produkt käse = new Käse(Money.from((Money.parse("EUR " + record.get(1)))), (Integer.parseInt(record.get(2))), record.get(3), (Integer.parseInt(record.get(4))), LocalDate.parse(record.get(5)));
                    käse.setType(record.get(0));
                    list.add(käse);
                    break;
                }
                case "FrischerSalat": {
                    Produkt käse = new FrischerSalat(Money.from((Money.parse("EUR " + record.get(1)))), (Integer.parseInt(record.get(2))), record.get(3), (Integer.parseInt(record.get(4))), LocalDate.parse(record.get(5)));
                    käse.setType(record.get(0));
                    list.add(käse);
                    break;
                }
                default:
                    throw new UndefinedProduktTypeException("There is something wrong with the ProduktType!");
            }
        }
        return list;
    }
}

