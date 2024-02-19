package com.main.clouddefenceai.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.ObjectUtils;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.main.clouddefenceai.datamodels.request.GetDataRequest;

@Service
@SuppressWarnings("deprecation")
public class GitHubRepoDependencyFinder {

    // I have used kohsuke github library to fetch data from github
    // Also added a method to add all the dependencies if it is a gradle project 
    
    private GitHub authenticateWithGitHub(String accessToken) throws Exception {
	return GitHub.connectUsingOAuth(accessToken);
    }

    public Map<String, List<String>> getAllDependencies(GetDataRequest getDataRequest) throws Exception {
	Map<String, List<String>> dependenciesMap = new HashMap<>();
	GitHub github = authenticateWithGitHub(getDataRequest.getAccess_token());
	if (ObjectUtils.isNotEmpty(getDataRequest.getRepoName())) {
	    GHRepository curRepository = github
		    .getRepository(getDataRequest.getUsername() + "/" + getDataRequest.getRepoName());
	    return getDependencies(github, curRepository, getDataRequest, dependenciesMap);
	}
	Map<String, GHRepository> repositories = github.getUser(getDataRequest.getUsername()).getRepositories();
	for (Map.Entry<String, GHRepository> entry : repositories.entrySet()) {
	    dependenciesMap.put(entry.getKey(), new ArrayList<>());
	    getDependencies(github, entry.getValue(), getDataRequest, dependenciesMap);
	}
	return dependenciesMap;
    }

    public Map<String, List<String>> getDependencies(GitHub gitHub, GHRepository ghRepository,
	    GetDataRequest getDataRequest, Map<String, List<String>> dependenciesMap) throws Exception {
	if (gitHub.isOffline()) {
	    gitHub = authenticateWithGitHub(getDataRequest.getAccess_token());
	}
	List<String> pomFilesPaths = findPomFiles(ghRepository);
	for (String path : pomFilesPaths) {
	    String pomContent = ghRepository.getFileContent(path).getContent();
	    List<String> pomDependencies = getPomDependencies(pomContent);
	    dependenciesMap.merge(ghRepository.getName(), new ArrayList<>(pomDependencies), (existingVal, newVal) -> {
		existingVal.addAll(newVal);
		return existingVal;
	    });
	}
	List<String> gradleFilesPaths = findGradleFiles(ghRepository);
	for (String path : gradleFilesPaths) {
	    String gradleContent = ghRepository.getFileContent(path).getContent();
	    List<String> gradleDependencies = getGradleDependencies(gradleContent);
	    dependenciesMap.merge(ghRepository.getName(), new ArrayList<>(gradleDependencies),
		    (existingVal, newVal) -> {
			existingVal.addAll(newVal);
			return existingVal;
		    });
	}
	return dependenciesMap;
    }

    private List<String> findGradleFiles(GHRepository repository) throws Exception {
	List<String> gradleFilesPath = findFilesInRoot(repository, "build.gradle");
	return gradleFilesPath;
    }

    private List<String> findPomFiles(GHRepository repository) throws Exception {
	List<String> pomFilesPath = findFilesInRoot(repository, "pom.xml");
	return pomFilesPath;
    }

    private static List<String> findFilesInRoot(GHRepository repository, String fileName) throws IOException {
	List<GHContent> contents = repository.getDirectoryContent("");
	return contents.stream().filter(content -> content.getName().equals(fileName)).map(GHContent::getPath)
		.collect(Collectors.toList());
    }

    public List<String> getGradleDependencies(String gradleContent) {
	List<String> resList = new ArrayList<>();
	String[] lines = gradleContent.split("\n");
	for (String line : lines) {
	    line = line.trim();
	    if (line.startsWith("implementation") || line.startsWith("compileOnly")
		    || line.startsWith("annotationProcessor") || line.startsWith("compile")) {
		String dependency = line.substring(line.indexOf(' ') + 1).replace("'", "").replace("\"", "");
		if (!dependency.contains("group:")) {
		    resList.add(dependency);
		} else {
		    String group = extractValue(dependency, "group");
		    String name = extractValue(dependency, "name");
		    String version = extractValue(dependency, "version");
		    resList.add(group + ":" + name + ":" + version);
		}
	    }
	}
	return resList;
    }

    private static String extractValue(String dependency, String key) {
	String pattern = key + ": '";
	int startIndex = dependency.indexOf(pattern) + pattern.length();
	int endIndex = dependency.indexOf("'", startIndex);
	if (startIndex >= pattern.length() && endIndex > startIndex) {
	    return dependency.substring(startIndex, endIndex);
	} else {
	    return "";
	}
    }

    private List<String> getPomDependencies(String pomContent) throws Exception {
	List<String> resList = new ArrayList<>();
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	factory.setNamespaceAware(false);
	DocumentBuilder builder = factory.newDocumentBuilder();
	InputStream inputStream = new ByteArrayInputStream(pomContent.getBytes(StandardCharsets.UTF_8));
	Document doc = builder.parse(inputStream);
	doc.getDocumentElement().normalize();
	NodeList dependencyList = doc.getElementsByTagName("dependency");
	for (int i = 0; i < dependencyList.getLength(); i++) {
	    Node node = dependencyList.item(i);
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
		Element element = (Element) node;
		String groupId = element.getElementsByTagName("groupId").item(0).getTextContent();
		String artifactId = element.getElementsByTagName("artifactId").item(0).getTextContent();
		String version = element.getElementsByTagName("version").item(0).getTextContent();
		resList.add(groupId + ":" + artifactId + ": Version " + version);
	    }
	}
	return resList;
    }
}
