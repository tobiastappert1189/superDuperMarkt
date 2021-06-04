package com.tobias.superDuperMarkt.main;

import com.tobias.superDuperMarkt.main.Utils.ListHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class main {
    static ListHelper listHelper = new ListHelper();
    private static final Logger log = LoggerFactory.getLogger(main.class);

    public static void main(String[] args) {
        log.debug("SuperDuperMarktApp");

        try {
            listHelper.simulation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
