package ru.ifmo.neerc.pcms2.services.client;

import ru.ifmo.neerc.pcms2.framework.Service;

public interface LoginDataService
    extends Service
{

    public abstract LoginData getLoginData(String s, String s1)
        throws AuthorizationFailedException;
}
~                  
