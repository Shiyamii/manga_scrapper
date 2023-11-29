package mangaAPI;

import java.io.File;
import java.util.Date;
import java.util.logging.*;

public class Logs {
    private static Logger loggerKB = null;
    private static Logger logger = null;

    static {
        loggerKB = Logger.getLogger(Logs.class.getName());

        try {
            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.FINEST);
            consoleHandler.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new Date(lr.getMillis()),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage()
                    );
                }
            });
            logger = Logger.getAnonymousLogger();
            logger.setLevel(Level.FINEST);
            logger.addHandler(consoleHandler);



            File directory = new File("./log");
            directory.mkdir();
            FileHandler fichier = new FileHandler("./log/logs.txt");
            fichier.setLevel(Level.FINEST);
            fichier.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new Date(lr.getMillis()),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage()
                    );
                }
            });
            loggerKB.addHandler(fichier);
            loggerKB.setLevel(Level.FINEST);

        } catch( Exception exception ) {
            loggerKB.log(Level.SEVERE, "Cannot read logger file", exception);
        }

    }

    static public void addLog(Level level, String text) {
        logger.log(level, text);
    	Logs.loggerKB.log(level, text);
    }

}