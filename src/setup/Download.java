package setup;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Download {
    public static void main(String[] args) throws IOException, InterruptedException {
        String sessionToken = Files.readString(Paths.get("data/session.token")).trim();
        String year = Files.readString(Paths.get("data/year.txt")).trim();


        System.out.print("Download input for day: ");
        Scanner scanner = new Scanner(System.in);
        int day = scanner.nextInt();
        URI uri = URI.create(String.format("https://adventofcode.com/%s/day/%d/input", year, day));
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder(uri).GET().header("cookie", String.format("session=%s", sessionToken)).build();

        Path file = Paths.get(String.format("data/day%d/input.txt", day));
        file.toFile().getParentFile().mkdirs();
        httpClient.send(req, HttpResponse.BodyHandlers.ofFile(file));

        System.out.print("Link to puzzle: ");
        System.out.println(String.format("https://adventofcode.com/%s/day/%d", year, day));
    }
}
