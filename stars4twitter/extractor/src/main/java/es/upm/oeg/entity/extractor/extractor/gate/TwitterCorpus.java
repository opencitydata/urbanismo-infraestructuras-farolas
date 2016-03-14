/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.oeg.entity.extractor.extractor.gate;



import es.upm.oeg.entityoutputgateplugin.FarolasRepo;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.creole.ResourceInstantiationException;
import java.util.ArrayList;
import java.util.List;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author PabloPortatil
 */
public class TwitterCorpus {

    private Corpus corpus;
    private FarolasRepo repository;

    final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(TwitterCorpus.class);

    private ConfigurationBuilder cb;
    private String queryString;
    
    
    
    /**
     *  TODO: PUT HERE YOUR OWN TWITTER AUTHENTICATION PASS
     */
    public void Authenticate(){
         cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("");
    
    }
    public void setQuery(String q){
        queryString=q;
    
    }

    public void createCorpus() {

       
        repository =new FarolasRepo();

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try {
            corpus = Factory.newCorpus("tweetcorpus");
            Query query = new Query(queryString); //"oddfarolas"
            QueryResult result;
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                Document doc = Factory.newDocument(tweet.getText());
                doc.setName(String.valueOf(tweet.getId()));
                corpus.add(doc);

              
                logger.info(tweet.getId() + "  @" + tweet.getUser().getScreenName() + " - " + tweet.getText() + " -" + tweet.getGeoLocation());
                repository.instanciateNew(String.valueOf(tweet.getId()), tweet.getUser().getScreenName(), tweet.getText(), tweet.getGeoLocation());
                
                
            }

        } catch (TwitterException te) {
            logger.error(te);
            logger.error("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        } catch (ResourceInstantiationException ex) {
            logger.error(ex);
        }
        logger.info("corpus size" + corpus.size());

    }

    public Corpus getCorpus() {
        return corpus;
    }

    public FarolasRepo getRepository() {
        return repository;
    }

    
}
