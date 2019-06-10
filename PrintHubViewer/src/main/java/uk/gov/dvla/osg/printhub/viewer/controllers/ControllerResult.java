package uk.gov.dvla.osg.printhub.viewer.controllers;

import java.util.List;

import uk.gov.dvla.osg.printhub.viewer.volume.Count;

public class ControllerResult {

    private List<Count> volumeCounts;
    private boolean success;
    private String message;


    public static ControllerResult success() {
        return new ControllerResult();
    }

    public static ControllerResult success(List<Count> volumeCounts) {
        return new ControllerResult(volumeCounts);
    }
    
    public static ControllerResult error(String message) {
        return new ControllerResult(message);
    }
    
    private ControllerResult(List<Count> volumeCounts) {
        this.volumeCounts = volumeCounts;
        this.success = true;
    }

    private ControllerResult(String message) {
        this.success = false;
        this.message = message;
    }

    public ControllerResult() {
        this.success = true;
    }

    public boolean isError() {
        return !success;
    }

    public String getMessage() {
        return message;
    }

    public List<Count> getVolume() {
        return volumeCounts;
    }



}
