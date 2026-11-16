
package com.chemicalmanagement.repository;

import com.chemicalmanagement.domain.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class InMemoryCustomerRepositoryTest {

    private InMemoryCustomerRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryCustomerRepository();
    }

    private Customer createCustomer(String id, String name) {
        return new Customer(
                id,
                name,
                "9876543210",
                "customer@example.com",
                "Indore"
        );
    }

    @Test
    void shouldSaveValidCustomer() {
        Customer customer = createCustomer("CUST-001", "Rahul");

        repository.save(customer);

        Assertions.assertTrue(repository.existsById("CUST-001"));
    }

    @Test
    void shouldThrowExceptionWhenSavingNullCustomer() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenSavingDuplicateCustomer() {
        repository.save(createCustomer("CUST-001", "Rahul"));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(createCustomer("CUST-001", "Amit"))
        );
    }

    @Test
    void shouldReturnCustomerWhenIdExists() {
        repository.save(createCustomer("CUST-001", "Rahul"));

        Optional<Customer> result = repository.findById("CUST-001");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Rahul", result.get().getCustomerName());
    }

    @Test
    void shouldReturnEmptyWhenCustomerDoesNotExist() {
        Assertions.assertTrue(repository.findById("CUST-999").isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenFindingCustomerWithNullId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingCustomerWithBlankId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById("  ")
        );
    }

    @Test
    void shouldThrowExceptionWhenFindingCustomerWithEmptyId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById("")
        );
    }

    @Test
    void shouldReturnAllSavedCustomers() {
        Customer customer1 = createCustomer("CUST-001", "Rahul");
        Customer customer2 = createCustomer("CUST-002", "Amit");

        repository.save(customer1);
        repository.save(customer2);

        List<Customer> result = repository.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(customer1));
        Assertions.assertTrue(result.contains(customer2));
    }

    @Test
    void shouldReturnEmptyListWhenNoCustomersAreSaved() {
        Assertions.assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldReturnUnmodifiableList() {
        repository.save(createCustomer("CUST-001", "Rahul"));

        List<Customer> result = repository.findAll();

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> result.add(createCustomer("CUST-002", "Amit"))
        );
    }

    @Test
    void shouldReturnTrueWhenCustomerExists() {
        repository.save(createCustomer("CUST-001", "Rahul"));

        Assertions.assertTrue(repository.existsById("CUST-001"));
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExist() {
        Assertions.assertFalse(repository.existsById("CUST-999"));
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithNullId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithEmptyId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById("")
        );
    }

    @Test
    void shouldThrowExceptionWhenCheckingExistenceWithBlankId() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsById("  ")
        );
    }

    @Test
    void shouldNotModifyRepositoryWhenReturnedListIsModified() {
        repository.save(createCustomer("CUST-001", "Rahul"));

        List<Customer> result = repository.findAll();

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> result.add(createCustomer("CUST-002", "Amit"))
        );

        Assertions.assertEquals(1, repository.findAll().size());
        Assertions.assertTrue(repository.existsById("CUST-001"));
    }
}
