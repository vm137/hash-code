package com.hashcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        List<String> dataSets = Arrays.asList("a_example.txt"
//                "c_incunabula.txt", "b_read_on.txt",
//                "d_tough_choices.txt", "e_so_many_books.txt", "f_libraries_of_the_world.txt"
        );

        List<InData> inData = new ArrayList<>();

        dataSets.forEach(filename -> {
            inData.add(getData(filename));
            
            System.out.println(filename);
        });

        System.out.println(inData);
    }

    static void solution(InData inData) {
        List<Integer> power = new ArrayList<>();

        inData.libraries.forEach(library -> {

        });


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

                String descrLine = reader.readLine().trim();
                int[] descrNumbers = Arrays.stream(descrLine.split("\\s")).mapToInt(Integer::parseInt).toArray();
                library.n = descrNumbers[0];
                library.t = descrNumbers[1];
                library.m = descrNumbers[2];

                String booksLine = reader.readLine().trim();
                library.booksIds = Arrays.stream(booksLine.split("\\s")).mapToInt(Integer::parseInt).toArray();

                library.books = new ArrayList<>();
                for (int j = 0; j < library.n; j++) {
                    Book newBook = new Book(library.booksIds[j], inData.scores[library.booksIds[j]]);
                    library.books.add(newBook);
                }

                // shell sort books[] desc by highest score
                for (int ii = 0; ii < library.books.size() - 1; ii++) {

                    int maxScore = library.books.get(ii).score;
                    for (int jj = library.books.size() - 2; jj >= ii; jj--) {

                        if (library.books.get(jj).score > library.books.get(jj + 1).score) {
                            maxScore = library.books.get(jj).score;

                            // swap
                            int tempId = library.books.get(jj).id;
                            int tempScore = library.books.get(jj).score;
                            library.books.get(jj).id = library.books.get(jj + 1).id;
                            library.books.get(jj).score = library.books.get(jj + 1).score;
                            library.books.get(jj + 1).id = tempId;
                            library.books.get(jj + 1).score = tempScore;
                        }
                    }
                }

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

        int[] booksIds;
        List<Book> books;
    }

    static class Book {
        int id;
        int score;

        Book(int id, int score) {
            this.id = id;
            this.score = score;
        }
    }

    static class OutData {
        // output format:
        // number of libraries
        // next goes blocks of libraries:
        // libId, number of books for scanning
        // list of books

        List<Library> libraries;

        void printResults() {
            System.out.println(libraries.size());

            libraries.forEach(library -> {
                System.out.println(library.id + " " + library.booksIds.length);
                System.out.println(Stream.of(library.booksIds).map( String::valueOf ).collect( Collectors.joining( " " ) ));
            });
        }
    }
}
