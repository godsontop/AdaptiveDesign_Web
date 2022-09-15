package com.ats.adaptive.service;

import com.ats.evo.AdaptiveDesign;
import com.webdifftool.server.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
//@PropertySource(value = {"classpath:application.properties"})
public class ArchOutputService {

    @Value("${kg.dir}")
    private String workspace;
    @Value("${kg.baseKG}")
    private String KGBaseName;
    @Value("${output.architecture.dir}")
    private String outputPath;


    public String designArchitecture(String generation,String requirement) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        AdaptiveDesign ad = new AdaptiveDesign();
        List<String> names= new ArrayList<>();
        String req=requirement.replaceAll("\\[","").replaceAll("\\]","").replaceAll(" ","");
        ad.parseKGs(workspace,KGBaseName,generation,req);
        List<OWLManager> managers = ad.planA(ad.getJointKG(), ad.getRequirementKGs());
        for (OWLManager manager : managers) {
            String name = manager.designOntology(outputPath,req,generation);
            names.add(name);
        }
        return names.get(0);
    }
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public void setKGBaseName(String KGBaseName) {
        this.KGBaseName = KGBaseName;
    }

}
