//package com.ats.adaptive.service;
//
//import com.ats.adaptive.io.VowlConvertor;
//import com.ats.evo.AdaptiveDesign;
//import com.webdifftool.server.OWLManager;
//import org.semanticweb.owlapi.model.OWLOntologyCreationException;
//import org.semanticweb.owlapi.model.OWLOntologyStorageException;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
////@Service
//public class ArchOutputService {
////    private AdaptiveDesign ad;
////    private VowlConvertor vowlConvertor;
////    @Autowired
////    public ArchOutputService(AdaptiveDesign ad,VowlConvertor vowlConvertor){
////        this.ad = ad;
////        this.vowlConvertor =vowlConvertor;
////
////
////    }
//    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
//        ArchOutputService service = new ArchOutputService();
//        service.designArchitecture("C3zw","Execute_DDT,Monitor");
//    }
//
//    public void designArchitecture(String generation,String requirement) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
//        AdaptiveDesign ad = new AdaptiveDesign();
//        Set<String> names= new HashSet<>();
//        VowlConvertor vowlConvertor = new VowlConvertor();
//        ad.parseKGs(generation,requirement);
//        List<OWLManager> managers = ad.planA(ad.getJointKG(), ad.getRequirementKGs());
//        for (OWLManager manager : managers) {
//            String name = manager.designOntology(requirement,generation);
//            names.add(name);
//            vowlConvertor.convertToVowl(name+".owl",name);
//        }
//    }
//
//}
