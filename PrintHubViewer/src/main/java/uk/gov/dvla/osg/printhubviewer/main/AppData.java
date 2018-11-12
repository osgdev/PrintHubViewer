package uk.gov.dvla.osg.printhubviewer.main;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppData {

    @JsonProperty("networkConfigFile") 
    private String networkConfigFile;

    public File getNetworkConfigFile() {
        return new File(networkConfigFile);
    }

    public ImmutablePair<Boolean, String> checkValidity() {
        return Arrays.asList(getNetworkConfigFile())
                     .stream().filter(file -> !file.exists())
                     .map(f -> f.getName())
                     .reduce((x, y) -> x + " , " + y)
                     .map(s -> new ImmutablePair<>(false, s))
                     .orElse(new ImmutablePair<>(true, ""));
    }
}
