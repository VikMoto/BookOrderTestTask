package org.example;

import java.io.*;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        ClassLoader classLoader = Main.class.getClassLoader();
        String inputFileName = "input.txt"; // Adjust the file name as needed
        String outputFileName = "output.txt";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(inputFileName))));
             FileWriter writer = new FileWriter(outputFileName)) {

            OrderBook orderBook = new OrderBook();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                switch (tokens[0]) {
                    case "u":
                        orderBook.updateOrder(tokens);
                        break;
                    case "q":
                        if (tokens[1].equals("best_bid")) {
                            String bestBid = orderBook.queryBestBid();
                            if (!bestBid.isEmpty()) {
                                writer.write(bestBid + "\n");
                            }
                        } else if (tokens[1].equals("size")) {
                            int price = Integer.parseInt(tokens[2]);
                            String sizeInfo = orderBook.querySizeAtPrice(price);
                            writer.write(sizeInfo + "\n");
                        }
                        break;
                    case "o":
                        String orderType = tokens[1];
                        int orderSize = Integer.parseInt(tokens[2]);

                        if (orderType.equals("buy")) {
                            orderBook.processBuyOrder(orderSize);
                        } else if (orderType.equals("sell")) {
                            orderBook.processSellOrder(orderSize);
                        }
                        break;
                    default:
                        // Handle unknown command
                        break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}