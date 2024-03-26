package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Remote;
import util.exception.UnknownPersistenceException;

@Remote
public interface PartnerSessionBeanRemote {
    public long createPartner(Partner partner) throws UnknownPersistenceException;
    public boolean usernameExist(String username);
    public long test();
}
