package uk.gov.dvla.osg.printhub.viewer.controllers;

import uk.gov.dvla.osg.printhub.viewer.volume.VolumeQuery;
import uk.gov.dvla.osg.printhub.viewer.volume.VolumeCounts;

public class VolumeController {

    private VolumeQuery query;

    public static VolumeController getInstance(VolumeQuery getVolumeQuery) {
        return new VolumeController(getVolumeQuery);
    }

    private VolumeController(VolumeQuery getVolumeQuery) {
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
