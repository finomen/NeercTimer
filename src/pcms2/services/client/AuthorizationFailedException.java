package pcms2.services.client;

public class AuthorizationFailedException extends Exception
{

    public AuthorizationFailedException()
    {
    }

    public AuthorizationFailedException(String login, String password)
    {
        super((new StringBuilder()).append(login).append(":").append(password).toString());
    }
}