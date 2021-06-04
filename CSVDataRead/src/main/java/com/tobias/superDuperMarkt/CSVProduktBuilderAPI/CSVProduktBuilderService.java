package com.tobias.superDuperMarkt.CSVProduktBuilderAPI;

import com.tobias.superDuperMarkt.CSVException.UndefinedProduktTypeException;
import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.ArrayList;

public interface CSVProduktBuilderService {

    ArrayList<Produkt> initiateList() throws IOException, UndefinedProduktTypeException;

    ArrayList<Produkt> iterate(Iterable<CSVRecord> records) throws UndefinedProduktTypeException;
}
