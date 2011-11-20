package pcms2.services.client;

import pcms2.services.site.Clock;

public interface LoginData {
	public abstract String getSite();

	public abstract String getLogin();

	public abstract String getSessionId();

	public abstract String getPartyName();

	public abstract String getDefaultContestId();

	public abstract String getDefaultContestScoringModel();

	public abstract String getContestName();

	public abstract Clock getClock();

	public abstract ProblemInformation[] getProblems();
}
