package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

@Local
public interface PartnerSessionBeanLocal {
    public long createPartner(Partner partner) throws UnknownPersistenceException;

    public Partner getPartner(String username, String password) throws InvalidLoginCredentialsException;
}
