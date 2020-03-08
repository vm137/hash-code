package com.hashcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DataSet {
    int b; // number of different books
    int l; // number of libraries
    int d; // number of days

    int[] scores; // scores of individual books
    List<Library> libraries;


    static DataSet readData(String filename) {
        DataSet dataSet = new DataSet();

        Path resourceDirectory = Paths.get("src","main", "resources");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath + "/" + filename))) {

            String line1 = reader.readLine().trim();
            int[] numbers1 = Arrays.stream(line1.split("\\s")).mapToInt(Integer::parseInt).toArray();
            dataSet.b = numbers1[0];
            dataSet.l = numbers1[1];
            dataSet.d = numbers1[2];

            String line2 = reader.readLine().trim();
            dataSet.scores = Arrays.stream(line2.split("\\s")).mapToInt(Integer::parseInt).toArray();

            dataSet.libraries = new ArrayList<>();

            // iterate through libraries
            for (int i = 0; i < dataSet.l; i++) {
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
                    Book newBook = new Book(library.booksIds[j], dataSet.scores[library.booksIds[j]]);
                    library.books.add(newBook);
                }

                // shell sort List<Book> library.books desc by score
                int num = library.books.size();

                for (int ii = 0; ii < ( num - 1 ); ii++) {
                    for (int jj = 0; jj < num - ii - 1; jj++) {
                        if (library.books.get(jj).score < library.books.get(jj + 1).score) {
                            int tempId = library.books.get(jj).id;
                            int tempScore = library.books.get(jj).score;
                            library.books.get(jj).id = library.books.get(jj + 1).id;
                            library.books.get(jj).score = library.books.get(jj + 1).score;
                            library.books.get(jj + 1).id = tempId;
                            library.books.get(jj + 1).score = tempScore;
                        }
                    }
                }
                dataSet.libraries.add(library);
            }

            return dataSet;

        } catch (Exception e) {
            System.out.println("Data file parse error.");
            e.printStackTrace();
        }

        return null;
    }
}
