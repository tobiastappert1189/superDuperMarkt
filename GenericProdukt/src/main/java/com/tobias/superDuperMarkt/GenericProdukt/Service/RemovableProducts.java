package com.tobias.superDuperMarkt.GenericProdukt.Service;

import org.javamoney.moneta.Money;

public abstract class RemovableProducts {

    private boolean removeInformation;

    protected RemovableProducts(Money actualPrice, int id, String productName, int quality) {
        removeInformation = false;
    }

    public boolean getRemoveInformation() {
        return removeInformation;
    }

    @Override
    public String toString() {
        return "removableProducts{" +
                "removeInformation=" + removeInformation +
                '}';
    }

    public void setRemoveInformation(boolean removeInformation) {
        this.removeInformation = removeInformation;
    }
}
