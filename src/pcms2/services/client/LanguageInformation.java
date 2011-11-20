package pcms2.services.client;

import java.io.Serializable;

public final class LanguageInformation implements Serializable {

	public LanguageInformation(String id, String name, String sourcesMask) {
		this.id = id;
		this.name = name;
		this.sourcesMask = sourcesMask;
	}

	public final String id;
	public final String name;
	public final String sourcesMask;
}