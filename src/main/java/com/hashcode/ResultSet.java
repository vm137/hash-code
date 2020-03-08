package com.hashcode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Solution
     */
    static ResultSet findSolution(DataSet dataSet) {
        ResultSet resultSet = new ResultSet();
        int daysLeft = dataSet.d;

        while (dataSet.libraries.size() > 0 && daysLeft > 0) {

            int bestLibId = findBestLib(dataSet.libraries, daysLeft);

            OutLibrary outLibrary = new OutLibrary();

            Library lib = dataSet.libraries.get(bestLibId);
            outLibrary.id = lib.id;

            // add books
            for (int dayBook = 0; dayBook < lib.books.size() && dayBook <= daysLeft * lib.t; dayBook++) {
                Book book = lib.books.get(dayBook);
                outLibrary.bookIds.add(book.id);
            }

            resultSet.libraries.add(outLibrary);

            daysLeft -= lib.t;
            dataSet.libraries.remove(bestLibId);
        }

        return resultSet;
    }

    static int findBestLib(List<Library> libraries, int daysLeft) {
        Long[] libPower = new Long[libraries.size()];

        for (int i = 0; i < libraries.size(); i++) {
            Library lib = libraries.get(i);
            int daysToScan = daysLeft - lib.t;

            long power = 0;
            for (int dayBook = 0; dayBook < daysToScan * lib.m && dayBook < lib.n; dayBook++) {
                power += lib.books.get(dayBook).score;
            }

            libPower[i] = power;
        }

        // get lib with max power
        List<Long> powerList = Arrays.asList(libPower);
        int goodLibId = powerList.indexOf(Collections.max(powerList));

        return goodLibId;
    }

    void printToConsole() {
        System.out.println(libraries.size());

        libraries.forEach(library -> {
            System.out.println(library.id + " " + library.bookIds.size());
            System.out.println(library.bookIds.stream().map(String::valueOf).collect(Collectors.joining(" ")));
        });
    }

    void printToFile(String filename) {
        boolean out = new File("out").mkdir();
        String outFilename = "out/" + filename.replaceFirst("[.][^.]+$", "") + ".out";

        new File(outFilename).delete();

        try (
                FileWriter fileWriter = new FileWriter(outFilename, true);
                BufferedWriter writer = new BufferedWriter(fileWriter);
        ) {
            writer.write(String.valueOf(libraries.size()));
            writer.append("\n");

            libraries.forEach(library -> {
                try {
                    writer.append(String.valueOf(library.id)).append(" ").append(String.valueOf(library.bookIds.size()));
                    writer.append("\n");
                    writer.append(library.bookIds.stream().map(String::valueOf).collect(Collectors.joining(" ")));
                    writer.append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
