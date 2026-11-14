
package com.chemicalmanagement.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(
                "CUST-001",
                "Satvik Jain",
                "9876543210",
                "satvik@example.com",
                "Indore"
        );
    }

    // ---------------- CONSTRUCTOR TESTS ----------------

    @Test
    void shouldCreateCustomerSuccessfully() {
        assertEquals("CUST-001", customer.getCustomerId());
        assertEquals("Satvik Jain", customer.getCustomerName());
        assertEquals("9876543210", customer.getContactNumber());
        assertEquals("satvik@example.com", customer.getEmail());
        assertEquals("Indore", customer.getAddress());
        assertEquals(CustomerStatus.ACTIVE, customer.getCustomerStatus());
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer(null, "Customer", "9876543210", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("", "Customer", "9876543210", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("   ", "Customer", "9876543210", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerNameIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", null, "9876543210", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "", "9876543210", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "   ", "9876543210", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenContactNumberIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "Customer", null, null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenContactNumberIsEmpty() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "Customer", "", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenContactNumberIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "Customer", "   ", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenContactNumberHasInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "Customer", "987654321", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenContactNumberContainsLetters() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "Customer", "98765ABCDE", null, null)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "Customer", "9876543210", "   ", null)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailHasInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "Customer", "9876543210", "invalid-email", null)
        );
    }

    @Test
    void shouldThrowExceptionWhenAddressIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Customer("CUST-001", "Customer", "9876543210", null, "   ")
        );
    }

    @Test
    void shouldAllowNullEmailAndAddress() {
        Customer result = new Customer(
                "CUST-002",
                "Customer",
                "9876543210",
                null,
                null
        );

        assertNull(result.getEmail());
        assertNull(result.getAddress());
    }

    // ---------------- CUSTOMER NAME SETTER TESTS ----------------

    @Test
    void shouldUpdateCustomerNameSuccessfully() {
        customer.setCustomerName("Rahul Sharma");

        assertEquals("Rahul Sharma", customer.getCustomerName());
    }

    @Test
    void shouldThrowExceptionWhenSettingNullCustomerName() {
        assertThrows(IllegalArgumentException.class, () ->
                customer.setCustomerName(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenSettingBlankCustomerName() {
        assertThrows(IllegalArgumentException.class, () ->
                customer.setCustomerName("   ")
        );
    }

    // ---------------- CONTACT NUMBER SETTER TESTS ----------------

    @Test
    void shouldUpdateContactNumberSuccessfully() {
        customer.setContactNumber("9123456780");

        assertEquals("9123456780", customer.getContactNumber());
    }

    @Test
    void shouldThrowExceptionWhenSettingNullContactNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                customer.setContactNumber(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenSettingInvalidContactNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                customer.setContactNumber("12345")
        );
    }

    // ---------------- EMAIL SETTER TESTS ----------------

    @Test
    void shouldUpdateEmailSuccessfully() {
        customer.setEmail("new@example.com");

        assertEquals("new@example.com", customer.getEmail());
    }

    @Test
    void shouldAllowNullEmailInSetter() {
        customer.setEmail(null);

        assertNull(customer.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenSettingBlankEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                customer.setEmail("   ")
        );
    }

    @Test
    void shouldThrowExceptionWhenSettingInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                customer.setEmail("invalid-email")
        );
    }

    // ---------------- ADDRESS SETTER TESTS ----------------

    @Test
    void shouldUpdateAddressSuccessfully() {
        customer.setAddress("Bhopal");

        assertEquals("Bhopal", customer.getAddress());
    }

    @Test
    void shouldAllowNullAddressInSetter() {
        customer.setAddress(null);

        assertNull(customer.getAddress());
    }

    @Test
    void shouldThrowExceptionWhenSettingBlankAddress() {
        assertThrows(IllegalArgumentException.class, () ->
                customer.setAddress("   ")
        );
    }

    // ---------------- CUSTOMER STATUS TESTS ----------------

    @Test
    void shouldThrowExceptionWhenSettingNullCustomerStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                customer.setCustomerStatus(null)
        );
    }

    @Test
    void shouldUpdateCustomerStatusSuccessfully() {
        customer.setCustomerStatus(CustomerStatus.ACTIVE);

        assertEquals(CustomerStatus.ACTIVE, customer.getCustomerStatus());
    }
}
