package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {
    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    
    @Override
    public long createPartner(Partner partner) throws UnknownPersistenceException {
        System.out.println("PartnerSessionBean.createEmployee(Partner employee)");
        em.persist(partner);
        em.flush();
        System.out.println(String.format("Partner (ID: %d) successfully created", partner.getPartnerId()));
        System.out.println("EmployeeSessionBean.createEmployee(Employee employee) done");
        return partner.getPartnerId();
    }
    
    @Override
    public boolean usernameExist(String username) {
        System.out.println("EmployeeSessionBean.usernameExist(String username)");
        Query query = em.createQuery("SELECT COUNT(u) FROM Partner u WHERE u.username = :username");
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
    
    @Override
    public Partner getPartner(String username, String password) throws InvalidLoginCredentialsException {
        Query query = em.createQuery("SELECT e FROM Partner e WHERE e.username = :username");
        query.setParameter("username", username);
        
        Partner partner = null;
        try {
            partner = (Partner) query.getSingleResult();
            if (!partner.getPassword().equals(password)) {
                throw new InvalidLoginCredentialsException();
            }
        } catch (NoResultException e) {
            throw new InvalidLoginCredentialsException();
        }
        
        em.detach(partner);
        
        return partner;
    }
}
