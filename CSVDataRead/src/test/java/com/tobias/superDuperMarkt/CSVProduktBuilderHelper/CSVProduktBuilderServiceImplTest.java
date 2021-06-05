package com.tobias.superDuperMarkt.CSVProduktBuilderHelper;

import com.tobias.superDuperMarkt.CSVException.UndefinedProduktTypeException;
import com.tobias.superDuperMarkt.CSVProduktBuilderAPI.CSVProduktBuilderServiceImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CSVProduktBuilderServiceImplTest {

    @Test
    void extractDataSuccessfull() throws IOException, UndefinedProduktTypeException {

        CSVProduktBuilderServiceImpl csvProduktBuilderService = new CSVProduktBuilderServiceImpl();

        String file = System.getProperty("user.dir") + "\\src/test/java/com/tobias/superDuperMarkt/CSVProduktBuilderHelper/TestdataCorrect.csv";
        Reader in = new FileReader(file);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(in);


        csvProduktBuilderService.iterate(records);

        assertDoesNotThrow(() -> csvProduktBuilderService.iterate(records));
    }

    @Test
    void extractDataNotSuccessful() throws IOException {

        String file = System.getProperty("user.dir") + "\\src/test/java/com/tobias/superDuperMarkt/CSVProduktBuilderHelper/TestdataNotCorrect.csv";
        Reader in = null;
        try {
            in = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert in != null;
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(in);

        CSVProduktBuilderServiceImpl csvProduktBuilderService = new CSVProduktBuilderServiceImpl();

        assertThatThrownBy(() -> csvProduktBuilderService.iterate(records)).
                isInstanceOf(UndefinedProduktTypeException.class)
                .withFailMessage("There is soemthing wrong with the ProduktType");
    }
}