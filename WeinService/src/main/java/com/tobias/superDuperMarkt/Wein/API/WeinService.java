package com.tobias.superDuperMarkt.Wein.API;

import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadQualityException;
import com.tobias.superDuperMarkt.Wein.Service.Wein;

public interface WeinService {
    void checkQuality(Wein wein) throws BadQualityException;

    void manageInternalQualityCounter(Wein wein);

    boolean increaseQuality(Wein wein);
}
