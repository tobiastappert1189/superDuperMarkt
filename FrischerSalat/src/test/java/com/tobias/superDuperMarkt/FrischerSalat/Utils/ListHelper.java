package com.tobias.superDuperMarkt.FrischerSalat.Utils;

import com.tobias.superDuperMarkt.GenericProdukt.Service.Produkt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ListHelper {
    private static final Logger log = LoggerFactory.getLogger(ListHelper.class);

    public static void deleteAllProductswithBadQuality(List<Produkt> liste) {
        liste.removeIf(produkt -> {
            boolean returnValue = produkt.getRemoveInformation();
            if (returnValue) {
                log.debug("Product deleted: " + produkt.toString());
                return true;
            }
            return false;
        });
    }
}
