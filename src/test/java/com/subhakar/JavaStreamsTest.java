package com.subhakar;

import com.subhakar.streams.models.Customer;
import com.subhakar.streams.models.Order;
import com.subhakar.streams.models.Product;
import com.subhakar.streams.repository.CustomerRepository;
import com.subhakar.streams.repository.OrderRepository;
import com.subhakar.streams.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@DataJpaTest
@Slf4j
class JavaStreamsTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    @DisplayName("Obtain a list of product with category = \"Books\" and price > 100")
    public void exercise1a() {
        long startTime = System.currentTimeMillis();
        List<Product> result = productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Books"))
                .filter(p -> p.getPrice() > 100)
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 1a - execution time: %1$d ms", (endTime - startTime)));
        result.forEach(p -> log.info(p.toString()));
    }

    @Test
    @DisplayName("Obtain a list of product with category = \"Books\" and price > 100 (using Predicate)")
    public void exercise1b() {
        long startTime = System.currentTimeMillis();
        Predicate<Product> categoryPredicate = product -> product.getCategory().equalsIgnoreCase("Books");
        Predicate<Product> pricePredicate = product -> product.getPrice() > 100;
        List<Product> result = productRepository.findAll()
                .stream()
                .filter(categoryPredicate)
                .filter(pricePredicate)
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 1b - execution time: %1$d ms", (endTime - startTime)));
        result.forEach(product -> log.info(product.toString()));
    }

    @Test
    @DisplayName("Obtain a list of product with category = \"Books\" and price > 100 (using Predicate chaining for filter)")
    public void exercise1c() {
        long startTime = System.currentTimeMillis();
        Predicate<Product> categoryPredicate = product -> product.getCategory().equalsIgnoreCase("Books");
        Predicate<Product> pricePredicate = product -> product.getPrice() > 100;
        List<Product> result = productRepository.findAll()
                .stream()
                .filter(categoryPredicate.and(pricePredicate))
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 1c - execution time: %1$d ms", (endTime - startTime)));
        result.forEach(product -> log.info(product.toString()));
    }

    @Test
    @DisplayName("Obtain a list of product with category = \"Books\" and price > 100 (using BiPredicate for filter)")
    public void exercise1d() {
        long startTime = System.currentTimeMillis();
        BiPredicate<Product, String> categoryFilter = (product, category) -> product.getCategory().equalsIgnoreCase(category);
        List<Product> result = productRepository.findAll()
                .stream()
                .filter(p -> categoryFilter.test(p, "Books"))
                .filter(p -> p.getPrice() > 100)
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 1d - execution time: %1$d ms", (endTime - startTime)));
        result.forEach(product -> log.info(product.toString()));
    }

    @Test
    @DisplayName("Obtain a list of order with product category = \"Baby\"")
    public void exercise2() {
        long startTime = System.currentTimeMillis();
        List<Order> result = orderRepository.findAll()
                .stream()
                .filter(
                        o -> o.getProducts().stream().anyMatch(p -> p.getCategory().equalsIgnoreCase("Baby")
                        )
                )
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 2 - execution time: %1$d ms", (endTime - startTime)));
        result.forEach(o -> log.info(o.toString()));
    }

    @Test
    @DisplayName("Obtain a list of product with category = “Toys” and then apply 10% discount\"")
    public void exercise3() {
        long startTime = System.currentTimeMillis();
        List<Product> result = productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Toys"))
                .map(p -> p.withPrice(p.getPrice() * 0.9))
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 3 - execution time: %1$d ms", (endTime - startTime)));
        result.forEach(o -> log.info(o.toString()));
    }

    @Test
    @DisplayName("Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021")
    public void exercise4() {
        long startTime = System.currentTimeMillis();
        List<Product> result = orderRepository.findAll()
                .stream()
                .filter(o -> o.getCustomer().getTier() == 2)
                .filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
                .filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 4, 1)) <= 0)
                .flatMap(o -> o.getProducts().stream())
                .distinct()
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 4 - execution time: %1$d ms", (endTime - startTime)));
        result.forEach(o -> log.info(o.toString()));
    }

    @Test
    @DisplayName("Get the 3 cheapest products of \"Books\" category")
    public void exercise5() {
        long startTime = System.currentTimeMillis();
        Optional<Product> result = productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Books"))
                .min(Comparator.comparing(Product::getPrice));

        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 5 - execution time: %1$d ms", (endTime - startTime)));
        log.info(result.get().toString());
    }

    @Test
    @DisplayName("Get the 3 most recent placed order")
    public void exercise6() {
        long startTime = System.currentTimeMillis();
        List<Order> result = orderRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 6 - execution time: %1$d ms", (endTime - startTime)));
        result.forEach(o -> log.info(o.toString()));
    }

    @Test
    @DisplayName("Get a list of products which was ordered on 15-Mar-2021")
    public void exercise7() {
        long startTime = System.currentTimeMillis();
        List<Product> result = orderRepository.findAll()
                .stream()
                .filter(o -> o.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
                .peek(o -> System.out.println(o.toString()))
                .flatMap(o -> o.getProducts().stream())
                .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 7 - execution time: %1$d ms", (endTime - startTime)));
        result.forEach(o -> log.info(o.toString()));
    }

    @Test
    @DisplayName("Calculate the total lump of all orders placed in Feb 2021")
    public void exercise8() {
        long startTime = System.currentTimeMillis();
        double result = orderRepository.findAll()
                .stream()
                .filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
                .filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 3, 1)) < 0)
                .flatMap(o -> o.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .sum();
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 8 - execution time: %1$d ms", (endTime - startTime)));
        log.info("Total lump sum = " + result);
    }

    @Test
    @DisplayName("Calculate the average price of all orders placed on 15-Mar-2021")
    public void exercise9() {
        long startTime = System.currentTimeMillis();
        double result = orderRepository.findAll()
                .stream()
                .filter(o -> o.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
                .flatMap(o -> o.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .average()
                .getAsDouble();
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 9 - execution time: %1$d ms", (endTime - startTime)));
        log.info("Total lump sum = " + result);
    }

    @Test
    @DisplayName("Obtain statistics summary of all products belong to \"Books\" category")
    public void exercise10() {
        long startTime = System.currentTimeMillis();
        DoubleSummaryStatistics statistics = productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase("Books"))
                .mapToDouble(Product::getPrice)
                .summaryStatistics();
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 10 - execution time: %1$d ms", (endTime - startTime)));
        log.info(String.format("count = %1$d, average = %2$f, max = %3$f, min = %4$f, sum = %5$f",
                statistics.getCount(), statistics.getAverage(), statistics.getMax(), statistics.getMin(), statistics.getSum()));
    }

    @Test
    @DisplayName("Obtain a mapping of order id and the order's product count")
    public void exercise11() {
        long startTime = System.currentTimeMillis();
        Map<Long, Integer> result = orderRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Order::getId,
                        o -> o.getProducts().size()
                ));
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 10 - execution time: %1$d ms", (endTime - startTime)));
        log.info(result.toString());
    }

    @Test
    @DisplayName("Obtain a data map of customer and list of orders")
    public void exercise12() {
        long startTime = System.currentTimeMillis();
        HashMap<Customer, List<Order>> result = orderRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer(),
                        HashMap::new,
                        Collectors.mapping(order -> order, Collectors.toList())));

        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 12- execution time: %1$d ms", (endTime - startTime)));
        log.info(result.toString());
    }

    @Test
    @DisplayName("Obtain a data map of customer_id and list of order_id(s)")
    public void exercise12a() {
        long startTime = System.currentTimeMillis();
        HashMap<Long, List<Long>> result = orderRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getId(),
                        HashMap::new,
                        Collectors.mapping(Order::getId, Collectors.toList())));
        long endTime = System.currentTimeMillis();
        log.info(String.format("exercise 12a- execution time: %1$d ms", (endTime - startTime)));
        log.info(result.toString());
    }
}
