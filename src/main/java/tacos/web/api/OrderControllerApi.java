package tacos.web.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tacos.TacoOrder;
import tacos.data.OrderRepository;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/orders", produces = "application/json")
@CrossOrigin(origins = "http://tacocloud:8080")
public class OrderControllerApi {
    private OrderRepository orderRepo;

    public OrderControllerApi(OrderRepository orderRepository) {
        orderRepo = orderRepository;
    }

    @GetMapping(params = "recent")
    public Iterable<TacoOrder> recentTacoOrder() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return orderRepo.findAll(page).getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TacoOrder> tacoOrderById(@PathVariable("id") Long id) {
        Optional<TacoOrder> optOrder = orderRepo.findById(id);
        return optOrder.map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public TacoOrder postTacoOrder(@RequestBody TacoOrder tacoOrder) {
        return orderRepo.save(tacoOrder);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public TacoOrder putTacoOrder(@Param("id") Long id, @RequestBody TacoOrder tacoOrder) {
        tacoOrder.setId(id);
        return orderRepo.save(tacoOrder);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public TacoOrder patchTacoOrder(@Param("id") Long id, @RequestBody TacoOrder tacoOrder) {
        TacoOrder order = orderRepo.findById(id).get();
        if (tacoOrder.getDeliveryName() != null) {
            order.setDeliveryName(tacoOrder.getDeliveryName());
        }
        if (tacoOrder.getDeliveryStreet() != null) {
            order.setDeliveryStreet(tacoOrder.getDeliveryStreet());
        }
        if (tacoOrder.getDeliveryCity() != null) {
            order.setDeliveryCity(tacoOrder.getDeliveryCity());
        }
        if (tacoOrder.getDeliveryState() != null) {
            order.setDeliveryState(tacoOrder.getDeliveryState());
        }
        if (tacoOrder.getDeliveryZip() != null) {
            order.setDeliveryZip(tacoOrder.getDeliveryZip());
        }
        if (tacoOrder.getCcNumber() != null) {
            order.setCcNumber(tacoOrder.getCcNumber());
        }
        if (tacoOrder.getCcExpiration() != null) {
            order.setCcExpiration(tacoOrder.getCcExpiration());
        }
        if (tacoOrder.getCcCVV() != null) {
            order.setCcCVV(tacoOrder.getCcCVV());
        }
        if (tacoOrder.getTacos() != null) {
            order.setTacos(tacoOrder.getTacos());
        }
        return orderRepo.save(tacoOrder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@Param("id") Long id) {
        orderRepo.deleteById(id);
    }
}
