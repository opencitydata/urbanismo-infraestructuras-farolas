/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.upm.oeg.entity.extractor.extractor;


import es.upm.oeg.entity.extractor.extractor.gate.TweetPropertyExtractorGateApplication;
import es.upm.oeg.entity.extractor.extractor.gate.TwitterCorpus;
import gate.Gate;
import gate.creole.ResourceInstantiationException;
import gate.gui.MainFrame;
import gate.util.GateException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Properties;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 *
 * @author pcalleja
 */
public class EntityExtractor {
   
    final static Logger logger = Logger.getLogger(EntityExtractor.class);
    
    public static Properties properties;
    
    public static String BaseDir;
    
    /**
     * The main method init the entity extractor project initalizing the properties and the corpus to be used. 
     *
     * @param args
     * @throws InterruptedException
     * @throws java.io.IOException
     * @throws gate.util.GateException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static void main(String[] args) throws InterruptedException, IOException, GateException, InvocationTargetException {

        // PROPERTIES
        properties = new Properties();
        InputStream input;

        //load a properties file from class path, inside static method
        String filename = "extractor.properties";
        java.net.URL url = EntityExtractor.class.getClassLoader().getResource(filename);
        properties.load(url.openStream());
        if (properties == null) {
            logger.error("Sorry, unable to find " + filename);
            return;
        }

        // create the base dir project 
        BaseDir = new File(System.getProperty("user.dir")).getParent();
        if (BaseDir == null) {
            logger.error("Unable to find the Base directory");
            return;
        }

        // Corpus creator
        logger.info("Initialising GATE...");

        Gate.setGateHome(new File(BaseDir + File.separator + properties.getProperty("EntityExtractionResourcesDir")));
        Gate.setPluginsHome(new File(BaseDir + File.separator + properties.getProperty("EntityExtractionResourcesDir")));
        Gate.setSiteConfigFile(new File(BaseDir + File.separator + properties.getProperty("EntityExtractionResourcesDir") + File.separator + "gate.xml"));
        Gate.init();
        logger.info("...GATE initialised");

        logger.info("Creating corpus of tweets");
        TwitterCorpus tweetCorpus = new TwitterCorpus();
        tweetCorpus.Authenticate();
        tweetCorpus.setQuery("oddfarolas");

        tweetCorpus.createCorpus();

        processTweetCorpus(tweetCorpus);

    }
   
    /**
     * Main method for the normal Named Entity Recognition process. The
     * execution iterates over the corpus documents to not overheap the memory
     *
     * @param tweetCorpus
     * @throws java.lang.InterruptedException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static void processTweetCorpus(TwitterCorpus tweetCorpus) throws InterruptedException, InvocationTargetException {

        try {

            

            /*
             SwingUtilities.invokeAndWait (new Runnable() {
             public void run() {
             MainFrame.getInstance().setVisible(true) ;
             }
             }) ;*/
            TweetPropertyExtractorGateApplication Application = new TweetPropertyExtractorGateApplication();

            Application.ANNIEDir = BaseDir + File.separator + properties.getProperty("ANNIEdir");
            Application.EntityExtractorPluginsDir = BaseDir + File.separator + properties.getProperty("EntityExtractionPluginsDir");
            Application.EntityExtractorResourcesDir = BaseDir + File.separator + properties.getProperty("EntityExtractionResourcesDir");

            
            Application.initApplicationModules();
            Application.initRepository(tweetCorpus.getRepository());

            Application.createApplication();

            Application.setCorpus(tweetCorpus.getCorpus());
            Application.execute();

        } catch (ResourceInstantiationException ex) {
            logger.error(ex);
        } catch (MalformedURLException ex) {
            logger.error(ex);
        } catch (GateException | IOException ex) {
            logger.error(ex);
        }

    }


    
    
}
