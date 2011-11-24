package ru.ifmo.neerc.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import org.ho.yaml.Yaml;

public class Settings {
	static Settings instance;
	public String host;
	public String login;
	public String password;
	public Map<String, String> colorScheme;
	public int syncInterval;
	
	public static Settings instance() {
		if (instance == null) {
			try {
				instance = Yaml.loadType(new File("config.yaml"), Settings.class);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			};
		}
		
		return instance;	
	}
}
