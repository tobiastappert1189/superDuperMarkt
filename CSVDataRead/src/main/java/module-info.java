import com.tobias.superDuperMarkt.CSVProduktBuilderAPI.CSVProduktBuilderServiceImpl;

module CSVDataRead {
    requires commons.csv;
    requires GenericProdukt;
    requires WeinService;
    requires moneta;
    requires kaese;
    requires FrischerSalat;
    exports com.tobias.superDuperMarkt.CSVException;
    exports com.tobias.superDuperMarkt.CSVProduktBuilderAPI;

    uses CSVProduktBuilderServiceImpl;
}