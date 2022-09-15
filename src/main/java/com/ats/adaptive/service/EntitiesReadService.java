package com.ats.adaptive.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class EntitiesReadService {
    @Value("${kg.dir}")
    private String workspace;
    @Value("${kg.baseKG}")
    private String KGBaseName;

    public ArrayList<String> getFiles(String dic) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(workspace+"/"+dic);
        File[] tempList = file.listFiles();

        if (tempList != null) {
            for (File value : tempList) {
                if (value.isFile()) {
                    String fileName = value.getName();
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if(suffix.equals("owl")) {
                        if(!fileName.equals(KGBaseName+".owl")) {
                            files.add(value.getName().substring(0, fileName.lastIndexOf(".")));
                        }
                    }
                }
            }
        }
        return files;
    }

    public ArrayList<String> getDictionaries() {
        ArrayList<String> dics = new ArrayList<String>();
        File file = new File(workspace);
        File[] tempList = file.listFiles();

        if (tempList != null) {
            for (File value : tempList) {
                if (value.isDirectory()) {
                    dics.add(value.toString());
                }
            }
        }
        return dics;
    }

    public ArrayList<String> getAbsoluteDictionaries() {
        ArrayList<String> dics = new ArrayList<String>();
        File file = new File(workspace);
        File[] tempList = file.listFiles();


        if (tempList != null) {
            for (File value : tempList) {
                if (value.isDirectory()) {
                    dics.add(value.getName());
                }
            }
        }
        return dics;
    }


    public Map<String,ArrayList<String>> getEntitiesMap(){
        Map<String,ArrayList<String>> res = new HashMap<>();
        ArrayList<String> dics = getAbsoluteDictionaries();
        for(String dic : dics){
            res.put(dic,getFiles(dic));
        }
        return res;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getKGBaseName() {
        return KGBaseName;
    }

    public void setKGBaseName(String KGBaseName) {
        this.KGBaseName = KGBaseName;
    }
}
