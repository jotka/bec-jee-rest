package dk.nykredit.infra.rest;

import dk.nykredit.infra.exception.CustomException;
import dk.nykredit.infra.security.CustomAuthorizer;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@RestSecured
@Interceptor
public class RestSecuredImpl implements Serializable {

    @Inject
    CustomAuthorizer authorizer;

    @Inject
    Instance<HttpServletRequest> request;


    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        String currentUser = request.get().getHeader("user");
        if (currentUser != null) {
            authorizer.login(currentUser);
        } else {
            throw new CustomException("Access forbidden");
        }
        return context.proceed();
    }
}
