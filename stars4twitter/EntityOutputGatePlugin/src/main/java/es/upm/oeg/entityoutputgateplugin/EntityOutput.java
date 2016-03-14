/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.oeg.entityoutputgateplugin;

/**
 *
 * @author pcalleja
 */

import gate.Annotation;
import gate.AnnotationSet;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleResource;
import gate.util.OffsetComparator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
/**
 *
 * @author pcalleja
 */
@CreoleResource(name = "EntityOutput", comment = "EntityOutput")
public class EntityOutput extends AbstractLanguageAnalyser {

    
	private static final long serialVersionUID = -2831327540486345380L;
	final static Logger logger = Logger.getLogger(EntityOutput.class);
  
        private FarolasRepo Repo;
   
       
	/* 
	 * this method should provide the actual functionality of the PR 
	 * (from where the main execution begins). This method 
	 * gets called when user click on the "RUN" button in the 
	 * GATE Developer GUI’s application window. 
	 */
       public void execute() throws ExecutionException {

        logger.info("Document  : " + this.document.getName());

        try {

            // GET THE GENERAL ANNOTATION SET
            String inputASName = "";
            AnnotationSet annotSetGen = (inputASName == null || inputASName.trim().equals("")) ? document
                    .getAnnotations() : document.getAnnotations(inputASName);

            // to retreive the entities
            AnnotationSet annotEntitySet;
            List<Annotation> EntityAnnotationsList;

            annotEntitySet = annotSetGen.get("property");
            EntityAnnotationsList = new ArrayList<>(annotEntitySet);
            Collections.sort(EntityAnnotationsList, new OffsetComparator());

            HashMap <String,String> PropertiesMap=new HashMap();
            for (Annotation annot : EntityAnnotationsList) {
                String rule = annot.getFeatures().get("kind").toString();
                String string = annot.getFeatures().get("string").toString();

                logger.debug(" property " + rule + " : " + string);
                PropertiesMap.put(rule, string);
            }
        
            
            Farola f= this.Repo.getFarola(document.getName());
            f.instanciateProperties(PropertiesMap);

            
            // write in csv
            String CSVfile= "TweetsFarolas.csv";
            try {
                 FileOutputStream fileStream = new FileOutputStream(new File(CSVfile),true);
                 OutputStreamWriter writer = new OutputStreamWriter(fileStream, "UTF-8");
                 writer.append(f.getProperties()+"\n");
                 writer.close();
                fileStream.close();

            } catch (IOException ex) {
                logger.error(ex);
            }
            
            
        } catch (Exception e) {
            logger.error(super.getDocument().getName() + " The process has stopped. ", e);
        }

    }

    public void setRepo(FarolasRepo Repo) {
        this.Repo = Repo;
    }

       
       
       
        
	/* 
	 * this method gets called whenever an object of this 
	 * class is created either from GATE Developer GUI or if 
	 * initiated using Factory.createResource() method. 
	 */
	public Resource init() throws ResourceInstantiationException {
            
            logger.debug(" Loaded");
            
            
           
            
            return this;
	}

	/* this method is called to reinitialize the resource */
	public void reInit() throws ResourceInstantiationException {
		// reinitialization code 
		
	}
        
  
}
