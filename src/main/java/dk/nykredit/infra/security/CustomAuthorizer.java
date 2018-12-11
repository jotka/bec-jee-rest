package dk.nykredit.infra.security;

import dk.nykredit.infra.exception.CustomException;
import org.apache.deltaspike.security.api.authorization.Secures;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SessionScoped
@Named("authorizer")
public class CustomAuthorizer implements Serializable {

    private Map<String, String> currentUser = new ConcurrentHashMap<>();


    @Secures
    @Admin
    public boolean doAdminCheck(InvocationContext invocationContext, BeanManager manager) throws Exception {
        if (!currentUser.containsKey("user") && currentUser.get("user").equals("admin")) {
            throw new CustomException("Access denied");
        }
        return true;
    }

    @Secures
    @Guest
    public boolean doGuestCheck(InvocationContext invocationContext, BeanManager manager) throws Exception {
        if (!currentUser.containsKey("user") && currentUser.get("user").equals("guest") || doAdminCheck(null, null)) {
            throw new CustomException("Access denied");
        }
        return true;
    }

    public void login(String username) {
        currentUser.put("user", username);
        if (FacesContext.getCurrentInstance() != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Logged in sucessfully as <b>" + username + "</b>"));
        }
    }

    public Map<String, String> getCurrentUser() {
        return currentUser;
    }
}
