package nz.ac.wgtn.swen301.a3.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;


public class Client {

    public static void createFile(String file, String uri){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        try{
            client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(file)));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            assert args != null && args.length == 2;

            String type = args[0];
            String file = args[1];
            assert type.equalsIgnoreCase("csv") || type.equalsIgnoreCase("excel");
            assert file != null && file.length() > 0;
            assert (file.endsWith(".csv") && type.equalsIgnoreCase("csv")) ||
                    (file.endsWith(".xls") && type.equalsIgnoreCase("excel"));


            if(file.endsWith(".xls") && type.equalsIgnoreCase("excel"))     type = "xls";
            else if (file.endsWith(".csv") && type.equalsIgnoreCase("csv"))  type = "csv";
            else throw new Exception();

            String uri = "http://localhost:8080/resthome4logs/stats" + type;
            createFile(file, uri);
            System.out.println("File has been successfully created!");


        }catch (Exception ignored){
            System.out.println("File cannot be created");
        }

    }

}
