package com.hashcode;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<String> fileNames = Arrays.asList(
                "a_example.txt",
                "b_read_on.txt",
                "c_incunabula.txt",
                "d_tough_choices.txt",
                "e_so_many_books.txt",
                "f_libraries_of_the_world.txt"
        );

        fileNames.forEach(filename -> {
            DataSet dataSet = DataSet.readData(filename);

            ResultSet resultSet = ResultSet.findSolution(dataSet);

//            resultSet.printToConsole();
            resultSet.printToFile(filename);
        });
    }
}
