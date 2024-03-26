package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Local;
import util.exception.UnknownPersistenceException;

@Local
public interface EmployeeSessionBeanLocal {
    public long createEmployee(Employee employee) throws UnknownPersistenceException;
}
