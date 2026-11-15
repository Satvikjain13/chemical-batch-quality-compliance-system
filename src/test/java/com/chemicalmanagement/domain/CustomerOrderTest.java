package com.chemicalmanagement.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CustomerOrderTest {
    private Chemical sulphuricAcid;
    private Customer customer;
    CustomerOrder order1;
    @BeforeEach
    void setUp(){
        sulphuricAcid = new Chemical("CHEM-001",
                "Sulphuric Acid",
                "Litres",
                365);
        customer = new Customer("CUST-001",
                "ABC Chemicals",
                "9039779122",
                "satvik@gmail.com",
                "Indore");
        order1 = new CustomerOrder("ORD-001",
                customer,
                sulphuricAcid,
                new BigDecimal("1500")
        );
    }

    @Test
    void shouldCreateCustomerOrderSuccessfully(){
        Assertions.assertEquals("ORD-001",order1.getOrderId());
        Assertions.assertSame(customer,order1.getCustomer());
        Assertions.assertSame(sulphuricAcid,order1.getChemical());
        Assertions.assertEquals(0,new BigDecimal("1500").compareTo(order1.getOrderQuantity()));
        Assertions.assertEquals(OrderStatus.CREATED,order1.getOrderStatus());
        Assertions.assertNotNull(order1.getOrderDate());
        Assertions.assertEquals("ABC Chemicals",order1.getCustomerNameAtOrder());
        Assertions.assertEquals("9039779122",order1.getCustomerContactAtOrder());
        Assertions.assertEquals("satvik@gmail.com",order1.getCustomerEmailAtOrder());
        Assertions.assertEquals("Indore",order1.getCustomerAddressAtOrder());
    }


    @Test
    void shouldThrowExceptionWhenOrderIdIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CustomerOrder(
                        null,
                        customer,
                        sulphuricAcid,
                        new BigDecimal("1500")
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CustomerOrder(
                        "",
                        customer,
                        sulphuricAcid,
                        new BigDecimal("1500")
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderIdIsBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CustomerOrder(
                        "   ",
                        customer,
                        sulphuricAcid,
                        new BigDecimal("1500")
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CustomerOrder(
                        "ORD-002",
                        null,
                        sulphuricAcid,
                        new BigDecimal("1500")
                )
        );
    }
    @Test
    void shouldThrowExceptionWhenCreatingOrderForInactiveCustomer() {

        customer.setCustomerStatus(CustomerStatus.INACTIVE);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            new CustomerOrder(
                    "ORD-002",
                    customer,
                    sulphuricAcid,
                    new BigDecimal("1500")
            );
        });
    }

    @Test
    void shouldAllowOrderCreationForActiveCustomer() {

        customer.setCustomerStatus(CustomerStatus.ACTIVE);
        CustomerOrder newOrder = new CustomerOrder(
                "ORD-003",
                customer,
                sulphuricAcid,
                new BigDecimal("1500")
        );

        Assertions.assertNotNull(newOrder);
        Assertions.assertEquals(
                OrderStatus.CREATED,
                newOrder.getOrderStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenChemicalIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CustomerOrder(
                        "ORD-002",
                        customer,
                        null,
                        new BigDecimal("1500")
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderQuantityIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CustomerOrder(
                        "ORD-002",
                        customer,
                        sulphuricAcid,
                        null
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderQuantityIsZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CustomerOrder(
                        "ORD-002",
                        customer,
                        sulphuricAcid,
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenOrderQuantityIsNegative() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new CustomerOrder(
                        "ORD-002",
                        customer,
                        sulphuricAcid,
                        new BigDecimal("-100")
                )
        );
    }

    @Test
    void shouldPreserveCustomerDetailsAtOrderCreation() {

        customer.setCustomerName("XYZ Chemicals");
        customer.setContactNumber("9876543210");
        customer.setEmail("xyz@example.com");
        customer.setAddress("Bhopal");

        // Customer details have changed
        Assertions.assertEquals(
                "XYZ Chemicals",
                customer.getCustomerName()
        );

        // Order must retain the original details
        Assertions.assertEquals(
                "ABC Chemicals",
                order1.getCustomerNameAtOrder()
        );

        Assertions.assertEquals(
                "9039779122",
                order1.getCustomerContactAtOrder()
        );

        Assertions.assertEquals(
                "satvik@gmail.com",
                order1.getCustomerEmailAtOrder()
        );

        Assertions.assertEquals(
                "Indore",
                order1.getCustomerAddressAtOrder()
        );
    }

    @Test
    void shouldMarkCreatedOrderAsValidated(){
        order1.markAsValidated();
        Assertions.assertEquals(OrderStatus.VALIDATED,order1.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenValidatingOrderThatIsNotCreated(){
        order1.markAsValidated();

        Assertions.assertThrows(IllegalStateException.class,() -> {
            order1.markAsValidated();
        });

        Assertions.assertEquals(OrderStatus.VALIDATED,order1.getOrderStatus());
    }

    @Test
    void shouldMarkValidatedOrderAsReserved(){
        order1.markAsValidated();
        order1.markAsReserved();
        Assertions.assertEquals(OrderStatus.RESERVED,order1.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenReservingOrderThatIsNotValidated(){
        Assertions.assertThrows(IllegalStateException.class,() -> {
            order1.markAsReserved();
        });
        Assertions.assertEquals(OrderStatus.CREATED,order1.getOrderStatus());
    }

    @Test
    void shouldMarkCreatedOrderAsRejected(){
        order1.markAsRejected();
        Assertions.assertEquals(OrderStatus.REJECTED,order1.getOrderStatus());
    }

    @Test
    void shouldMarkValidatedOrderAsRejected(){
        order1.markAsValidated();
        order1.markAsRejected();
        Assertions.assertEquals(OrderStatus.REJECTED,order1.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenRejectingReservedOrder(){
        order1.markAsValidated();
        order1.markAsReserved();

        Assertions.assertThrows(IllegalStateException.class,() -> {
            order1.markAsRejected();
        });
        Assertions.assertEquals(OrderStatus.RESERVED,order1.getOrderStatus());
    }

    @Test
    void shouldMarkReservedOrderAsDispatched(){
        order1.markAsValidated();
        order1.markAsReserved();
        order1.markAsDispatched();
        Assertions.assertEquals(OrderStatus.DISPATCHED,order1.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenDispatchingOrderThatIsNotReserved(){
        Assertions.assertThrows(IllegalStateException.class,() -> {
            order1.markAsDispatched();
        });
        Assertions.assertEquals(OrderStatus.CREATED,order1.getOrderStatus());
    }

    @Test
    void shouldMarkCreatedOrderAsCancelled(){
        order1.markAsCancelled();
        Assertions.assertEquals(OrderStatus.CANCELLED,order1.getOrderStatus());
    }

    @Test
    void shouldMarkValidatedOrderAsCancelled(){
        order1.markAsValidated();
        order1.markAsCancelled();
        Assertions.assertEquals(OrderStatus.CANCELLED,order1.getOrderStatus());
    }

    @Test
    void shouldMarkReservedOrderAsCancelled(){
        order1.markAsValidated();
        order1.markAsReserved();
        order1.markAsCancelled();
        Assertions.assertEquals(OrderStatus.CANCELLED,order1.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenCancellingDispatchedOrder(){
        order1.markAsValidated();
        order1.markAsReserved();
        order1.markAsDispatched();
        Assertions.assertThrows(IllegalStateException.class,() -> {
            order1.markAsCancelled();
        });
    }

    @Test
    void shouldThrowExceptionWhenCancellingRejectedOrder(){
        order1.markAsRejected();
        Assertions.assertThrows(IllegalStateException.class,() -> {
            order1.markAsCancelled();
        });
        Assertions.assertEquals(OrderStatus.REJECTED,order1.getOrderStatus());
    }

    @Test
    void shouldThrowExceptionWhenCancellingAlreadyCancelledOrder(){
        order1.markAsCancelled();
        Assertions.assertThrows(IllegalStateException.class,() -> {
            order1.markAsCancelled();
        });
        Assertions.assertEquals(OrderStatus.CANCELLED,order1.getOrderStatus());
    }

}
