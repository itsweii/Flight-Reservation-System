/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Local;
import util.exception.CustomerAccountFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author mw
 */
@Local
public interface CustomerSessionBeanLocal {
    public Customer doLogin(String username, String password) throws InvalidLoginCredentialsException, UnknownPersistenceException;
    public Customer createCustomerAccount(Customer cus);
    public Customer findUsername(String username) throws CustomerAccountFoundException ;
}
