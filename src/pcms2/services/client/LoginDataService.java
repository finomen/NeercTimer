package pcms2.services.client;

import pcms2.framework.Service;

public interface LoginDataService
    extends Service
{

    public abstract LoginData getLoginData(String s, String s1)
        throws AuthorizationFailedException;
}

