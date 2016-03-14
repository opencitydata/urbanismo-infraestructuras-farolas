/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.oeg.entity.extractor.extractor.gate;

import es.upm.oeg.entityoutputgateplugin.EntityOutput;
import es.upm.oeg.entityoutputgateplugin.FarolasRepo;
import gate.Corpus;
import gate.Factory;
import gate.Gate;
import gate.ProcessingResource;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.creole.Transducer;
import gate.creole.gazetteer.DefaultGazetteer;
import gate.creole.splitter.SentenceSplitter;
import gate.creole.tokeniser.SimpleTokeniser;
import gate.util.GateException;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
/**
 *
 * @author pcalleja
 */
public class TweetPropertyExtractorGateApplication {

    final static Logger logger = Logger.getLogger(TweetPropertyExtractorGateApplication.class);

    /**
     * The Corpus Pipeline application 
     */
    private SerialAnalyserController Application;
    
    // Path of the resources
    public String ANNIEDir;
    public String EntityExtractorPluginsDir;
    public String EntityExtractorResourcesDir;
    
    
    // MAIN MODULES USED
    private SimpleTokeniser Tokeniser;
    private SentenceSplitter Splitter;
    private DefaultGazetteer Gazetteer;
    private Transducer Patterns ;
    private EntityOutput EntityOutputModule;

    /**
     * Initialise the proccesing resources that are used in the project. 
     * @throws gate.util.GateException
     * @throws java.io.IOException
     */
    public void initApplicationModules() throws GateException, IOException {

        logger.info("Initialising Resources...");

        // Register Plugins
        Gate.getCreoleRegister().registerDirectories(
               new File(this.ANNIEDir).toURI().toURL());

        Gate.getCreoleRegister().registerDirectories(
                new File(this.EntityExtractorPluginsDir).toURI().toURL());

        // TOKENISER
        Tokeniser = (SimpleTokeniser) Factory.createResource("gate.creole.tokeniser.SimpleTokeniser");
        Tokeniser.setEncoding("UTF-8");
        Tokeniser.setRulesURL(new File(this.ANNIEDir + File.separator + "resources" 
                + File.separator + "tokeniser" + File.separator+ "DefaultTokeniser.rules").toURI().toURL());
       

        // SPLITTER -PR
        Splitter = (SentenceSplitter) Factory.createResource("gate.creole.splitter.SentenceSplitter");
        Splitter.setEncoding("UTF-8");
        Splitter.setGazetteerListsURL(
                 new File(this.ANNIEDir + File.separator + "resources" + File.separator + "sentenceSplitter"
                        + File.separator + "gazetteer" + File.separator + "lists.def").toURI().toURL());
        Splitter.setTransducerURL(
                 new File(this.ANNIEDir + File.separator + "resources" + File.separator + "sentenceSplitter"
                        + File.separator + "grammar" + File.separator + "main.jape").toURI().toURL());
        
        // GAZETTEER -PR
        Gazetteer = (DefaultGazetteer) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer");
        Gazetteer.setCaseSensitive(false);
        Gazetteer.setEncoding("UTF-8");
        Gazetteer.setAnnotationSetName(null);
        Gazetteer.setGazetteerFeatureSeparator(":");
        Gazetteer.setListsURL(new File(this.EntityExtractorResourcesDir 
                + File.separator + "gazetteer" + File.separator + "list.def").toURI().toURL());
        Gazetteer.init();
         
         
         // JAPE RULES - PR
        Patterns = (Transducer) Factory.createResource("gate.creole.Transducer", gate.Utils
         .featureMap("grammarURL", new File(this.EntityExtractorResourcesDir
                 +File.separator+"jape"+File.separator+"main.jape").toURI().toURL(),
                "encoding", "UTF-8"));
        
        
        // The Entity output module PR
        EntityOutputModule = (EntityOutput) Factory.createResource("es.upm.oeg.entityoutputgateplugin.EntityOutput");
     
        logger.info("...Resources loaded");
    }
    
    
    public void initRepository(FarolasRepo repo){

        EntityOutputModule.setRepo(repo);
    }
    
    /**
     * The method creates a "corpus pipeline" application that can be used to run sets of documents.
     * The method creates the application and puts the different processes in the specific order.
     * 
     */
    public void createApplication() {
  
        try {
            // the processing queue
            Application = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController");
            // reset
            Application.add((gate.ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR"));
            // tokeniser
            Application.add(Tokeniser);
            // sppliter
            Application.add(Splitter);

            // Gazetteers
            Application.add(Gazetteer);
            // JAPE patterns
            Application.add(Patterns);
            
            // Entity Output Module
            Application.add(EntityOutputModule);
           
            
        } catch (ResourceInstantiationException e) {
            logger.error("Error initializing ANNIE Application", e);
        }
        
    }

    /**
     * Executes the application with the corpus that is established.
     */
    public void execute() {
        
        logger.info("Processing ..........");
        try {
            Application.execute();
            logger.info("...Processing complete");
        } catch (GateException e) {
            // no se ha podido ejecutar
            logger.error("Error running  ANNIE Application", e);
        }

    }

    /**
     * Tell application controller about the corpus you want to run on
     *
     * @param corpus
     */
    public void setCorpus(Corpus corpus) {
        Application.setCorpus(corpus);
    }

    /** 
     * Cleans the application and the resources in it
     * @throws ResourceInstantiationException 
     */
    public void clean() throws ResourceInstantiationException{

        this.Application.cleanup();
        Factory.deleteResource(Application);
        Application = null;
    }

}
