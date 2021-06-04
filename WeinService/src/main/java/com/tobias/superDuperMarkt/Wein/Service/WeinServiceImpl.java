package com.tobias.superDuperMarkt.Wein.Service;

import com.tobias.superDuperMarkt.GenericProdukt.Utils.BadQualityException;
import com.tobias.superDuperMarkt.Wein.API.WeinService;

public class WeinServiceImpl implements WeinService {

    @Override
    public void checkQuality(Wein wein) throws BadQualityException {
        if (wein.getQuality() < 0) {
            throw new BadQualityException("Quality is to small");
        }
    }

    public void manageInternalQualityCounter(Wein wein) {
        if (!increaseQuality(wein)) {
            int internalCounter = wein.getInternalQualityCounter() + 1;
            wein.setInternalQualityCounter(internalCounter);
        }
    }

    @Override
    public boolean increaseQuality(Wein wein) {
        if (wein.getInternalQualityCounter() == 10 && wein.getQuality() < 50) {
            int increasingQuality = wein.getQuality() + 1;
            wein.setQuality(increasingQuality);
            wein.setInternalQualityCounter(0);
            return true;
        }
        return false;
    }
}
