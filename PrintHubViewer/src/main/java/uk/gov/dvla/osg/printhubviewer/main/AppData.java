package uk.gov.dvla.osg.printhubviewer.main;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppData {

    @JsonProperty("acronymLookupFile") 
    private String acronymLookupFile;

    @JsonProperty("networkConfigFile") 
    private String networkConfigFile;

    @JsonProperty("outputConfigFile") 
    private String outputConfigFile;

    @JsonProperty("emailCredentialsFile") 
    private String emailCredentialsFile;

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
