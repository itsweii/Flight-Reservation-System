/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Remote;
import util.exception.CustomerAccountFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author mw
 */
@Remote
public interface CustomerSessionBeanRemote {
    public Customer doLogin(String username, String password) throws InvalidLoginCredentialsException, UnknownPersistenceException;
    public Customer findUsername(String username) throws CustomerAccountFoundException ;
    public Customer createCustomerAccount(Customer cus);
}
