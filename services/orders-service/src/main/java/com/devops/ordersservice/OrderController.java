package com.devops.ordersservice;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    public record Order(String id, String item, int quantity) {}
    public record NewOrder(String item, int quantity) {}

    private final Map<String, Order> store = new ConcurrentHashMap<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestBody NewOrder in) {
        Order o = new Order(UUID.randomUUID().toString(), in.item(), in.quantity());
        store.put(o.id(), o);
        return o;
    }

    @GetMapping
    public List<Order> list() { return List.copyOf(store.values()); }

    @GetMapping("/{id}")
    public Order get(@PathVariable String id) {
        Order o = store.get(id);
        if (o == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return o;
    }
}
