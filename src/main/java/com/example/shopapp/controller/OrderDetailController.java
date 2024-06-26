package com.example.shopapp.controller;

import com.example.shopapp.dto.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order-details")
public class OrderDetailController {

    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        return ResponseEntity.ok("create order detail successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable Long id) {
        return ResponseEntity.ok("get order detail with id: " + id);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getAllOrderDetails(
            @Valid @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok("get all order details with order id: " + orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable Long id,
            @RequestBody OrderDetailDTO newOrderDetailData) {
        return ResponseEntity.ok("update order detail with id: " + id
                + ", newOrderDetail: " + newOrderDetailData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(
            @Valid @PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
