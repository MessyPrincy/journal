package dev.messyprincy.messyJournal.logging;

import dev.messyprincy.messyJournal.MessyJournal;

import java.util.logging.Logger;

public class LoggerManager {
    private static Logger logger;

    public LoggerManager() {
        logger = MessyJournal.instance.getLogger();
    }

    public void successLog(String text) {
        logger.info("[Meßy's Journal]" + text);
    }

    public void errorLog(String text) {
        logger.warning("[Meßy's Journal]" + text);
    }
}
