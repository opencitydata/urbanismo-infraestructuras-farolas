/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.oeg.entityoutputgateplugin;

import java.util.ArrayList;
import java.util.List;
import twitter4j.GeoLocation;

/**
 *
 * @author pcalleja
 */
public class FarolasRepo {
    
    
    
    private List <Farola> FarolasList;
    
    
    public FarolasRepo(){
        
        FarolasList= new ArrayList();
    
    }

    public List<Farola> getFarolasList() {
        return FarolasList;
    }

    public void setFarolasList(List<Farola> FarolasList) {
        this.FarolasList = FarolasList;
    }
    
    public void instanciateNew(String id, String user, String text, GeoLocation geo){
    
        this.FarolasList.add(new Farola(id,user,text,geo));
    
    }
    
    
    
    
    public Farola getFarola(String id){
    
    
        for(Farola f: this.FarolasList){
        
            if(f.getId().equals(id)){
                return f;
            }
        }
        return null;
    
    }
}
