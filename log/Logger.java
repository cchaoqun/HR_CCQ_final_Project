package com.CIT594.project594.log;

import com.CIT594.project594.util.ParserUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Chaoqun Cheng
 * @date 2021-07-2021/7/31-21:11
 */

public class Logger {
    private static FileWriter fw;
    // private constructor
    private Logger(String logfile){
        createPW(logfile);
    }

    // private static instance singleton
    private static Logger instance = null;

    public static Logger getInstance(String logfile){
        if(instance==null){
            instance = new Logger(logfile);
        }
        return instance;
    }

    // write log contents to specified
    public void log(Object contents){
        try {
            fw.write(ParserUtils.getCurrentTime()+" "+contents.toString()+"\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // create the filewriter
    public void createPW(String logfile){
        try{
            File file = new File(logfile);
            fw = new FileWriter(file, true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}