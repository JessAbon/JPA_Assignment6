package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomerDaoJPAImpl implements CustomerDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Customer customer) {
        System.out.println("using jpa");
        em.persist(customer);
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        try{
            return(Customer)em.createQuery("select customer from Customer customer where customer.customerId = :customerId").setParameter("customerId", customerId).getSingleResult();
        }catch (javax.persistence.NoResultException e){
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getByName(String name) {
        return em.createQuery("select customer from Customer customer where customer.name = :name", Customer.class)
                .setParameter("name", name).getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        Customer existingCustomer = em.find(Customer.class, customerToUpdate.getCustomerId());

        existingCustomer.setCompanyName(customerToUpdate.getCompanyName());
        existingCustomer.setEmail(customerToUpdate.getEmail());
        existingCustomer.setTelephone(customerToUpdate.getTelephone());
        existingCustomer.setNotes(customerToUpdate.getNotes());

        //synka in till databasen
        em.merge(existingCustomer);
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        Customer customer = em.find(Customer.class, oldCustomer.getCustomerId());
        em.remove(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return em.createQuery("select customer from Customer as customer" , Customer.class).getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        return (Customer) em.createQuery("Select customer from Customer customer LEFT JOIN FETCH customer.calls WHERE customer.customerId = :customerId")
                .setParameter("customerId", customerId)
                .getSingleResult();
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        customer.addCall(newCall);
    }
}
