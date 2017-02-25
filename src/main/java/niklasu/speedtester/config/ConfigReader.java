package niklasu.speedtester.config;

import com.beust.jcommander.JCommander;
import niklasu.speedtester.exceptions.BadFileException;
import niklasu.speedtester.exceptions.TooSmallFileException;

import java.net.MalformedURLException;

/**
 * Created by enzo on 25.02.2017.
 */
public class ConfigReader {

    ConfigStore configStore;

    public ConfigReader(String []args, ConfigStore configStore) throws BadFileException, TooSmallFileException, MalformedURLException {
        this.configStore = configStore;
        new JCommander(configStore, args);
        ParamValidator.validateParams(configStore.getSize(), configStore.getInterval(), configStore.getUrl());
    }
}
