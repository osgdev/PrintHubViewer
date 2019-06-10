package uk.gov.dvla.osg.printhub.viewer.volume;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Custom deserializer to retrieve the volume for a jobType from the JSON response received from PrintHub.
 * <p>The JSON response from PrintHub contains pending volumes for all available job types. As only one
 * job type is processed at a time, its volume is extracted from the received JSON.
 * <pre>{
    "jobType": "TRAILER REGISTRATION CERTIFICATE",
    "counts": [
        {
          "jobStatus": "PENDING",
          "count": 300
        }
    ]
 }</pre>
 */
public class VolumeDeserializer extends JsonDeserializer<VolumeCounts> {

    static final Logger LOG = LogManager.getLogger();
    
    @Override
    public VolumeCounts deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        
        // Get the whole tree from the parser
        JsonNode tree = jp.getCodec().readTree(jp);
        
        //Root node is an un-named array, so we grab all parent nodes as an iterable list
        Iterator<JsonNode> elements = tree.elements();
        // Create a list for all the Pending job types
        List<Count> counts = new ArrayList<>();
        
        // Look at each parent node and only process the one with a matching job type
        while (elements.hasNext()) {
            JsonNode currentNode = elements.next();
            String jobType = currentNode.get("jobType").asText();
            int volume = currentNode.get("counts").findValue("count").asInt();
            counts.add(new Count(jobType, volume));
        }
        
        return new VolumeCounts(counts);
    }
}
