package uk.gov.dvla.osg.printhub.viewer.volume;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import uk.gov.dvla.osg.printhub.core.services.VolumeService;

public class GetVolumeQuery {
    
    static final Logger LOGGER = LogManager.getLogger();

    private VolumeService service;
    private final VolumeDeserializer volumeDeserializer;
    
    public GetVolumeQuery (VolumeService service, VolumeDeserializer volumeDeserializer) {
        this.volumeDeserializer = volumeDeserializer;
        this.service = service;
    }
    
    public VolumeCounts execute() throws RuntimeException {
        String data = service.getVolume();

        LOGGER.debug("Volume response from server: {}", data);
        
        ObjectMapper mapper = new ObjectMapper();
        
        SimpleModule module = new SimpleModule();
        module.addDeserializer(VolumeCounts.class, volumeDeserializer);
        mapper.registerModule(module);
        
        try {
            return mapper.readValue(data, VolumeCounts.class);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("VolumeService is unable to parse JSON response:\n%s\n\nPrintHub response was:\n%s", ex.getMessage(), data));
        }
    }
}
