/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.singleton;

import javax.ejb.Remote;

/**
 *
 * @author hao
 */
@Remote
public interface TestcaseRemote {
    public void build();
}
