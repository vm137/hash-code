package com.hashcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<String> dataSets = Arrays.asList("a_example.txt", "c_incunabula.txt", "b_read_on.txt",
                "d_tough_choices.txt", "e_so_many_books.txt", "f_libraries_of_the_world.txt");

        List<InData> inData = new ArrayList<>();

        dataSets.forEach(filename -> {
            inData.add(getData(filename));
            
            System.out.println(filename);
        });

        System.out.println(inData);
    }

    static InData getData(String filename) {
        InData inData = new InData();

        Path resourceDirectory = Paths.get("src","main", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath + "/" + filename))) {

            String line1 = reader.readLine().trim();
            int[] numbers1 = Arrays.stream(line1.split("\\s")).mapToInt(Integer::parseInt).toArray();
            inData.b = numbers1[0];
            inData.l = numbers1[1];
            inData.d = numbers1[2];

            String line2 = reader.readLine().trim();
            inData.scores = Arrays.stream(line2.split("\\s")).mapToInt(Integer::parseInt).toArray();

            inData.libraries = new ArrayList<>();

            // iterate through libraries
            for (int i = 0; i < inData.l; i++) {
                Library library = new Library();
                library.id = i;

                String lineDescr = reader.readLine().trim();
                int[] nDescr = Arrays.stream(lineDescr.split("\\s")).mapToInt(Integer::parseInt).toArray();
                library.n = nDescr[0];
                library.t = nDescr[1];
                library.m = nDescr[2];

                String lineBooks = reader.readLine().trim();
                library.books = Arrays.stream(lineBooks.split("\\s")).mapToInt(Integer::parseInt).toArray();

                inData.libraries.add(library);
            }

            return inData;

        } catch (Exception e) {
            System.out.println("Data file parse error.");
            e.printStackTrace();
        }

        return null;
    }

    static class InData {
        int b; // number of different books
        int l; // number of libraries
        int d; // number of days

        int[] scores; // scores of individual books

        List<Library> libraries;
    }

    static class Library {
        int id; // library number
        int n; // number of books
        int t; // signup days
        int m; // number of books that can be scanned per day

        int[] books;
    }
}
