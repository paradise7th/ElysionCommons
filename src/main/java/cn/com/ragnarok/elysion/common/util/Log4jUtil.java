package cn.com.ragnarok.elysion.common.util;


import java.io.IOException;
import java.util.Enumeration;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.SimpleLayout;

public class Log4jUtil {

    public static final String APPENDER_ELYSION_COMMON_LOG="F";
    public static final String APPENDER_ELYSION_COMMON_ERR="ERR";


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
        RollingFileAppender appender=new RollingFileAppender(new SimpleLayout(),filename);
        appender.setMaxBackupIndex(maxBackups);
        appender.setMaxFileSize(filesize);
        appender.setThreshold(Level.INFO);
        appender.activateOptions();
        log.addAppender(appender);
        return appender;
    }

    public static ConsoleAppender addSimpleConsoleAppender (Logger log,String filename,int maxBackups,String filesize) throws IOException {
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
