package pimpmykerberos.beans.docker;

import java.util.HashMap;
import java.util.Map;

public class DockerCompose {
	String version;
	Map<String, Service> services = new HashMap<String, Service>();
	String dockerComposeFile="";

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Service> getServices() {
		return services;
	}

	public void setServices(Map<String, Service> services) {
		this.services = services;
	}

	public String getDockerComposeFile() {
		return dockerComposeFile;
	}

	public void setDockerComposeFile(String dockerComposeFile) {
		this.dockerComposeFile = dockerComposeFile;
	}

}
