package se.yrgo.spring.integrationtests;


import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/other-tiers.xml", "/datasource-test.xml" })
@Transactional
public class ManagingCustomersIntegrationTest {

    @Autowired
    private CustomerManagementService customerService;

    @Test
    public void testCreatingNewCustomer() {

    String customerId = "123456abc";
        Customer newCustomer = new Customer(customerId, "HoneyPie AB", "new small business");
        customerService.newCustomer(newCustomer);
        Customer retrievedCustomer = null;

        try {
            retrievedCustomer = customerService.findCustomerById(customerId);
            assertEquals(newCustomer, retrievedCustomer, "Not the same!");

        } catch (CustomerNotFoundException | RecordNotFoundException e) {
            System.out.println("Something fucked up......");
        }

    }

    @Test
    public void testFindingNonExistingCustomer() {

        assertThrows(CustomerNotFoundException.class, () -> {
            Customer foundCustomer = customerService.findCustomerById("wrongId156");
        });


    }




}
