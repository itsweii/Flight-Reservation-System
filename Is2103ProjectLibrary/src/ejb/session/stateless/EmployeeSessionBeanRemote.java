package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Remote;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

@Remote
public interface EmployeeSessionBeanRemote {

    public Employee getEmployee(String username, String password) throws InvalidLoginCredentialsException, UnknownPersistenceException;
//    public Employee getEmployee(String username) throws UnknownPersistenceException;
    public long createEmployee(Employee employee) throws UnknownPersistenceException;
    public boolean usernameExist(String username);
    public long test();
    
}
