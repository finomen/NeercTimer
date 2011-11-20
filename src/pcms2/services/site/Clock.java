package pcms2.services.site;

public interface Clock
{

    public abstract int getStatus();

    public abstract long getStartTime();

    public abstract long getTime();

    public abstract long getLength();

    public static final int UNKNOWN = 0;
    public static final int BEFORE = 1;
    public static final int RUNNING = 2;
    public static final int PAUSED = 3;
    public static final int OVER = 4;
}