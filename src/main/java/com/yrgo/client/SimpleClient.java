package com.yrgo.client;

import java.util.*;

import com.yrgo.dataaccess.RecordNotFoundException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import com.yrgo.services.calls.CallHandlingService;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;

public class SimpleClient {

    public static void main(String[] args) throws RecordNotFoundException, CustomerNotFoundException {
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");

        try {
            CustomerManagementService customerService = container.getBean(CustomerManagementService.class);
            CallHandlingService callService = container.getBean(CallHandlingService.class);
            DiaryManagementService diaryService = container.getBean(DiaryManagementService.class);

            customerService.newCustomer(new Customer("1", "Acme", "Good Customer"));

            Call newCall = new Call("Larry Wall called from Acme Corp");
            Action action1 = new Action("Call back Larry to ask how things are going", new GregorianCalendar(2016, 0, 0), "rac");
            Action action2 = new Action("Check our sales dept to make sure Larry is being tracked", new GregorianCalendar(2016, 0, 0), "rac");

            Call newCall2 = new Call("Hanna Wall called from Acme Corp");
            Action action3 = new Action("Get back to Hanna", new GregorianCalendar(2016, Calendar.FEBRUARY, 1), "rac");

            List<Action> actions = new ArrayList<Action>();
            actions.add(action1);
            actions.add(action2);
            actions.add(action3);

            try {
                callService.recordCall("1", newCall, actions);
                callService.recordCall("1", newCall2, actions);
            } catch (CustomerNotFoundException | RecordNotFoundException e) {
                System.out.println("That customer doesn't exist");
            }

            System.out.println("Here are the outstanding actions:");
            Collection<Action> incompleteActions = diaryService.getAllIncompleteActions("rac");
            for (Action next : incompleteActions) {
                System.out.println(next);
            }

            try {
                Customer customer = customerService.getFullCustomerDetail("1");
                System.out.println("Full details for Customer 1: ");
                System.out.println(customer);

            } catch (CustomerNotFoundException | RecordNotFoundException e) {
                System.out.println("That customer doesn't exist");
            }
        } finally {
            container.close();
        }
    }
}