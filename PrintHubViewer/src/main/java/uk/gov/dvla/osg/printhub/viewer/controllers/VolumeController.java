package uk.gov.dvla.osg.printhub.viewer.controllers;

import uk.gov.dvla.osg.printhub.viewer.volume.GetVolumeQuery;
import uk.gov.dvla.osg.printhub.viewer.volume.VolumeCounts;

public class VolumeController {

    private GetVolumeQuery query;

    public static VolumeController getInstance(GetVolumeQuery getVolumeQuery) {
        return new VolumeController(getVolumeQuery);
    }

    private VolumeController(GetVolumeQuery getVolumeQuery) {
        this.query = getVolumeQuery;
    }

    public ControllerResult execute() {
        try {
            VolumeCounts volumeCounts = query.execute();
            return ControllerResult.success(volumeCounts.getAll());
        } catch (RuntimeException ex) {
            return ControllerResult.error(ex.getMessage());
        }
    }

}
