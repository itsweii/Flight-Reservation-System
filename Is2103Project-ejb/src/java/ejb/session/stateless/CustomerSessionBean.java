/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerAccountFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author mw
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {
    
    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    
    @Override
    public Customer createCustomerAccount(Customer cus){
        em.persist(cus);
        em.flush();
        return cus;
    }
    
    @Override
    public Customer doLogin(String username, String password) throws InvalidLoginCredentialsException, UnknownPersistenceException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.userName = :userName");
        query.setParameter("userName", username);
        
        Customer customer = null;
        try {
            customer = (Customer) query.getSingleResult();
            if (!customer.getPassword().equals(password)) {
                throw new InvalidLoginCredentialsException();
            }
        } catch (NoResultException e) {
            throw new InvalidLoginCredentialsException();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException();
        }
        return customer;
    }
    
    @Override
    public Customer findUsername(String username) throws CustomerAccountFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.userName = :userName");
        query.setParameter("userName", username);
        
        try {
            Customer cus = (Customer) query.getSingleResult();
            throw new CustomerAccountFoundException("Username has been registered!");
        } catch (NoResultException e) {
            return null;
        }
    }
   

}
