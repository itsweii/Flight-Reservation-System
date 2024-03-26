package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    
    @Override
    public Employee getEmployee(String username, String password) throws InvalidLoginCredentialsException, UnknownPersistenceException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        query.setParameter("username", username);
        
        Employee employee = null;
        try {
            employee = (Employee) query.getSingleResult();
            if (!employee.getPassword().equals(password)) {
                throw new InvalidLoginCredentialsException();
            }
        } catch (NoResultException e) {
            throw new InvalidLoginCredentialsException();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
        return employee;
    }
    
//    @Override
//    public Employee getEmployee(String username) throws UnknownPersistenceException {
//        System.out.println("geting employee using username");
//        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
//        query.setParameter("username", username);
//        Employee employee = null;
//        try {
//            employee = (Employee) query.getSingleResult();
//        } catch (PersistenceException ex) {
//            if (ex.getMessage().equals("getSingleResult() did not retrieve any entities.")) {
//                
//            } else {
//                throw new UnknownPersistenceException(ex.getMessage());
//            }
//        }
//        System.out.println(employee);
//        System.out.println("geting employee using username done");
//        return employee;
//    }
    
    @Override
    public long createEmployee(Employee employee) throws UnknownPersistenceException {
        System.out.println("EmployeeSessionBean.createEmployee(Employee employee)");
        em.persist(employee);
        em.flush();
        System.out.println(String.format("Employee (ID: %d) successfully created", employee.getEmployteeId()));
        System.out.println("EmployeeSessionBean.createEmployee(Employee employee) done");
        return employee.getEmployteeId();
    }
    
    @Override
    public boolean usernameExist(String username) {
        System.out.println("EmployeeSessionBean.usernameExist(String username)");
        Query query = em.createQuery("SELECT COUNT(u) FROM Employee u WHERE u.username = :username");
        query.setParameter("username", username);

        Long count = (Long) query.getSingleResult();
        System.out.println("EmployeeSessionBean.usernameExist(String username) done");
        if (count > 0) {
            return true;
        }
        return false;
    }
    
    @Override
    public long test() {
        System.out.println("EmployeeSessionBean.test()");
        return 1;
    }
}
