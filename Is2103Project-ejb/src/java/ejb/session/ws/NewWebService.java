package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Partner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialsException;

@WebService(serviceName = "NewWebService")
@Stateless()
public class NewWebService {

    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;

    @WebMethod(operationName = "getPartner")
    public Partner getPartner(String username, String password) {
        try {
            Partner partner = partnerSessionBean.getPartner(username, password);
            return partner;
        } catch (InvalidLoginCredentialsException e) {
            return null;
        }
    }
}
