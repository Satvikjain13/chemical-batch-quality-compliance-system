package com.chemicalmanagement.service;

import com.chemicalmanagement.domain.*;
import com.chemicalmanagement.repository.InMemoryInventoryRepository;
import com.chemicalmanagement.repository.InMemoryReservationRepository;
import com.chemicalmanagement.repository.InventoryRepository;
import com.chemicalmanagement.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class OrderServiceTest {
    private  Chemical chemical;
    private  Customer customer;
    private  CustomerOrder order;
    private InventoryRepository inventoryRepository;
    private ReservationRepository reservationRepository;
    private  InventoryService inventoryService;
    private  OrderService orderService;

    @BeforeEach
    void setUp(){
        chemical = new Chemical("CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365);

        customer = new Customer("CUST-001",
                "Satvik Chemicals",
                "9039779122",
                "sats@gmail.com",
                "Indore");

        order = new CustomerOrder("ORD-001",
                customer,
                chemical,
                new BigDecimal("1500"));
        inventoryRepository = new InMemoryInventoryRepository();
        reservationRepository = new InMemoryReservationRepository();

        inventoryService = new InventoryService(inventoryRepository,reservationRepository);
        orderService = new OrderService(inventoryService);

    }

    @Test
    void shouldValidateOrderWhenCustomerIsActive(){
        orderService.validateOrder(order);
        Assertions.assertEquals(OrderStatus.VALIDATED,order.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenValidatingNullOrder() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> orderService.validateOrder(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsInactive() {
        customer.setCustomerStatus(CustomerStatus.INACTIVE);

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> orderService.validateOrder(order)
        );

        Assertions.assertEquals(OrderStatus.CREATED, order.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenReservingNullOrder() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> orderService.reserveOrder(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenReservingUnvalidatedOrder() {
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> orderService.reserveOrder(order)
        );
    }

    @Test
    void shouldRejectOrderWhenInventoryIsInsufficient() {
        orderService.validateOrder(order);

        boolean reserved = orderService.reserveOrder(order);

        Assertions.assertFalse(reserved);
        Assertions.assertEquals(OrderStatus.REJECTED, order.getOrderStatus());
    }

    @Test
    void shouldCancelCreatedOrder() {
        orderService.cancelOrder(order);

        Assertions.assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenCancellingRejectedOrder() {
        order.markAsRejected();

        Assertions.assertThrows(
                IllegalStateException.class,
                () -> orderService.cancelOrder(order)
        );

        Assertions.assertEquals(OrderStatus.REJECTED, order.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenDispatchingUnreservedOrder() {
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> orderService.dispatchOrder(order)
        );

        Assertions.assertEquals(OrderStatus.CREATED, order.getOrderStatus());
    }

}
