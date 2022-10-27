package de.fhkiel.advancedjava.testObjectGenerators;

import de.fhkiel.advancedjava.enums.MeanOfTransport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeansOfTransportGenerator {

    public final static List<MeanOfTransport> ALL_TYPES = Arrays.asList(MeanOfTransport.values());

    public static List<MeanOfTransport> generateOnlyBus() {
        List<MeanOfTransport> meansOfTransportList = new ArrayList<>();
        meansOfTransportList.add(MeanOfTransport.BUS);
        return meansOfTransportList;
    }

    public static List<MeanOfTransport> generateOnlySuburbanTrain() {
        List<MeanOfTransport> meansOfTransportList = new ArrayList<>();
        meansOfTransportList.add(MeanOfTransport.SUBURBAN_TRAIN);
        return meansOfTransportList;
    }

    public static List<MeanOfTransport> generateOnlySubway() {
        List<MeanOfTransport> meansOfTransportList = new ArrayList<>();
        meansOfTransportList.add(MeanOfTransport.SUBWAY);
        return meansOfTransportList;
    }


}
