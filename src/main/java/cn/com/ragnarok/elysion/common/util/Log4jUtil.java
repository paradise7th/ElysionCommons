package cn.com.ragnarok.elysion.common.util;


import java.io.IOException;
import java.util.Enumeration;

import org.apache.log4j.*;

public class Log4jUtil {

    public static final String APPENDER_ELYSION_COMMON_LOG="F";
    public static final String APPENDER_ELYSION_COMMON_ERR="ERR";
    public static final String LAYOUT_ELYSION_COMMON_FILE="%-5p [%d{yyyy-MM-dd HH\\:mm\\:ss}] %c{1}:%L - %m%n";
    public static final String LAYOUT_ELYSION_COMMON_CONSOLE="%d{ABSOLUTE} %5p %c{1}:%L - %m%n";


    public static void changeElysionLogFile(String filename,int maxBackups,String filesize){
        changeLog4jFile(APPENDER_ELYSION_COMMON_LOG,filename,maxBackups,filesize);
    }

    public static void changeElysionErrorFile(String filename,int maxBackups,String filesize){
        changeLog4jFile(APPENDER_ELYSION_COMMON_ERR,filename,maxBackups,filesize);
    }

    
    public static void changeLog4jFile(String appenderName,String filename) {
        changeLog4jFile(appenderName,filename,-1,null);
    }

    public static void changeLog4jFile(String appenderName,String filename,int maxBackups,String filesize) {
        RollingFileAppender file=getLog4jRollingFileAppender(appenderName);
        if(file!=null) {
            file.setFile(filename);
        }
        if(maxBackups>=0) {
            file.setMaxBackupIndex(maxBackups);
        }

        if(filesize!=null) {
            file.setMaxFileSize(filesize);
        }

        file.activateOptions();
    }


    public static RollingFileAppender getLog4jRollingFileAppender(String appenderName) {
        Logger rootLogger = Logger.getRootLogger();
        Enumeration en = rootLogger.getAllAppenders();
        while (en.hasMoreElements()){
            Object obj = en.nextElement();
            if(obj instanceof RollingFileAppender){
                RollingFileAppender file = (RollingFileAppender)obj;
                if(file.getName().equals(appenderName)) {
                    return file;
                }

            }
        }
        return null;

    }


    public static RollingFileAppender addSimpleRollingFileAppender (Logger log,String filename,int maxBackups,String filesize) throws IOException {
        PatternLayout layout=new PatternLayout();
        layout.setConversionPattern(LAYOUT_ELYSION_COMMON_FILE);
        RollingFileAppender appender=new RollingFileAppender(layout,filename);
        appender.setMaxBackupIndex(maxBackups);
        appender.setMaxFileSize(filesize);
        appender.setThreshold(Level.INFO);
        appender.activateOptions();
        log.addAppender(appender);
        return appender;
    }

    public static ConsoleAppender addSimpleConsoleAppender (Logger log,String filename,int maxBackups,String filesize) throws IOException {
        PatternLayout layout=new PatternLayout();
        layout.setConversionPattern(LAYOUT_ELYSION_COMMON_CONSOLE);
        ConsoleAppender appender=new ConsoleAppender(new SimpleLayout(),filename);
        appender.setThreshold(Level.INFO);
        appender.activateOptions();
        log.addAppender(appender);
        return appender;
    }

    public static void changeClassLoggerLevel(Class cls,Level level) {
        Logger log=LogManager.getLogger(cls);
        log.setLevel(level);
    }

    public static void hideClassLoggerAdditivity(Class cls) {
        Logger log=LogManager.getLogger(cls);
        log.setAdditivity(false);
    }


}
