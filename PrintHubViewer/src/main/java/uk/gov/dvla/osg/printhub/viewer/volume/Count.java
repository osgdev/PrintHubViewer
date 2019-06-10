package uk.gov.dvla.osg.printhub.viewer.volume;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Count {

    public String jobType;
    public int volume;

    public Count(String jobType, int volume) {
        this.jobType = jobType;
        this.volume = volume;
    }

    public String getJobType() {
        return jobType;
    }

    public Integer getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("jobType", jobType).append("count", volume).toString();
    }
}
