package uk.gov.dvla.osg.printhub.viewer.volume;

import java.util.List;

public class VolumeCounts {

    public final List<Count> counts;

    public VolumeCounts(List<Count> counts) {
        this.counts = counts;
    }
    
    public List<Count> getAll() {
        return this.counts;
    }

    @Override
    public String toString() {
        return String.format("VolumeCounts [counts=%s]", counts);
    }


}
