package com.example.shopapp.service;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.entity.OrderDetail;
import com.example.shopapp.exception.DataNotFoundException;

import java.util.List;

public interface OrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateDetail( Long id,OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    void deleteOrderDetail(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
}
