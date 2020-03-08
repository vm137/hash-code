package com.hashcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * output format:
 * number of libraries
 *
 * next goes blocks of libraries:
 * libId, number of books for scanning
 * list of books
 */
class ResultSet {

    List<OutLibrary> libraries;

    ResultSet() {
        libraries = new ArrayList<>();
    }

    static class OutLibrary {
        int id;
        List<Integer> bookIds;

        OutLibrary() {
            bookIds = new ArrayList<>();
        }
    }

    static ResultSet findSolution(DataSet dataSet) {
        ResultSet resultSet = new ResultSet();

        int daysLeft = dataSet.d;
        int libsLeft = dataSet.libraries.size();

        while (daysLeft > 0 && libsLeft > 0) {

            Integer[] powerOfLibrary = new Integer[libsLeft];
            for (int i = 0; i < libsLeft; i++) {
                Library lib = dataSet.libraries.get(i);
                int daysLeftForScanning = daysLeft - lib.t;

                int power = 0;
                for (int day = 0; day < daysLeftForScanning; day++) {
                    for (int k = 0; k < lib.m; k++) {
                        int index = k * lib.m + k;
                        if (index < lib.n) {
                            power += lib.books.get(index).score;
                        }
                    }
                }
                powerOfLibrary[i] = power;
            }

            // get lib with max power
            List<Integer> powerList = Arrays.asList(powerOfLibrary);
            int goodLibId = powerList.indexOf(Collections.max(powerList));
            Library goodLib = dataSet.libraries.get(goodLibId);

            // make out data
            OutLibrary newOutLib = new OutLibrary();

            newOutLib.id = goodLib.id;

            int daysForScanning = daysLeft - goodLib.t;
            for (int i = 0; i < daysForScanning; i++) {
                for (int j = 0; j < goodLib.m; j++) {
                    int index = i * goodLib.m + j;
                    if (index < goodLib.n) {
                        newOutLib.bookIds.add(goodLib.books.get(index).id);
                    }
                }
            }

            resultSet.libraries.add(newOutLib);

            daysLeft -= goodLib.t;
            dataSet.libraries.remove(goodLibId);
            libsLeft--;
        }

        return resultSet;
    }

    void printResults() {
        System.out.println(libraries.size());

        libraries.forEach(library -> {
            System.out.println(library.id + " " + library.bookIds.size());
            System.out.println(Stream.of(library.bookIds).map( String::valueOf ).collect( Collectors.joining( " " ) ));
        });
    }
}
