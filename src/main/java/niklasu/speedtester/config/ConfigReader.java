package niklasu.speedtester.config;

import com.beust.jcommander.JCommander;
import niklasu.speedtester.exceptions.BadFileException;
import niklasu.speedtester.exceptions.TooSmallFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
public class ConfigReader {
    @Autowired
    private ConfigStore configStore;

    public void parseArgs(String []args)throws BadFileException, TooSmallFileException, MalformedURLException{
        new JCommander(configStore, args);
        ParamValidator.validateParams(configStore.getSize(), configStore.getInterval(), configStore.getUrl());
    }
}
