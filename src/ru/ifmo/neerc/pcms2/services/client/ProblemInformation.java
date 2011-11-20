package ru.ifmo.neerc.pcms2.services.client;

import java.io.Serializable;

public class ProblemInformation implements Serializable {

	public ProblemInformation(String alias, String id, String name,
			int submitMethod, LanguageInformation languages[], String outputs[]) {
		this.alias = alias;
		this.id = id;
		this.name = name;
		this.submitMethod = submitMethod;
		this.languages = languages;
		this.outputs = outputs;
	}

	public final String alias;
	public final String id;
	public final String name;
	public final int submitMethod;
	public final LanguageInformation languages[];
	public final String outputs[];
}
