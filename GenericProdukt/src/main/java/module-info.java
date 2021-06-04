import com.tobias.superDuperMarkt.GenericProdukt.API.ProduktServiceImpl;

module GenericProdukt {
    requires moneta;
    requires money.api;
    exports com.tobias.superDuperMarkt.GenericProdukt.Service;
    exports com.tobias.superDuperMarkt.GenericProdukt.API;
    exports com.tobias.superDuperMarkt.GenericProdukt.Utils;
    uses ProduktServiceImpl;

}