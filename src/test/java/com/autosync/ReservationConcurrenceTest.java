package com.autosync;



import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.autosync.model.constraint.Customer;
import com.autosync.model.constraint.Vehicle;
import com.autosync.repository.constraint.CustomerRepository;
import com.autosync.repository.constraint.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationConcurrencyTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private final HttpClient httpClient = HttpClient.newHttpClient();


    @Test
    void competingReservationsShouldAllowOnlyOneActiveReservation() throws Exception {

        // Create a fresh vehicle for this experiment.
        String suffix = UUID.randomUUID().toString().replace("-", "");

        Vehicle vehicle = new Vehicle();
        vehicle.setVin("TEST" + suffix.substring(0, 13));
        vehicle.setMake("Toyota");
        vehicle.setModel("Concurrency Test");
        vehicle.setYear(2020);

        vehicle = vehicleRepository.save(vehicle);

        // Create 50 unique customers and launch their requests.
        List<CompletableFuture<HttpResponse<String>>> requests =
                new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            Customer customer = customerRepository.save(
                    new Customer(
                            "Concurrency Customer " + i,
                            "autosync-" + suffix + "-" + i + "@example.com"
                    )
            );

            requests.add(sendReservation(customer.getId(), vehicle.getId()));
        }

        // Wait until every request has completed.
        CompletableFuture.allOf(
                requests.toArray(new CompletableFuture<?>[0])
        ).join();

        // Count the HTTP status codes.
        Map<Integer, Long> statusCounts = new TreeMap<>();

        for (CompletableFuture<HttpResponse<String>> request : requests) {
            HttpResponse<String> response = request.join();

            statusCounts.merge(response.statusCode(), 1L, Long::sum);
        }

        System.out.println("Test vehicle ID: " + vehicle.getId());
        System.out.println("HTTP status counts: " + statusCounts);
    }

    private CompletableFuture<HttpResponse<String>> sendReservation(
            Long customerId, Long vehicleId) throws Exception {

        String json = """
            {
                "customerId": %d,
                "vehicleId": %d
            }
            """.formatted(customerId, vehicleId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/reservations"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return httpClient.sendAsync(
                request,
                HttpResponse.BodyHandlers.ofString());
    }

}
