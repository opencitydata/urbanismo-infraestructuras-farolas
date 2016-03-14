/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.oeg.entityoutputgateplugin;

import java.util.HashMap;
import twitter4j.GeoLocation;

/**
 *
 * @author pcalleja
 */
public class Farola {
    
    
    
    private String Id;
    private String User;
    private String Text;
    private GeoLocation Geo;
    
    
    private String Tipo;
    private String Bombilla;
    private String Cubierta;
    private String Alcance;
    private String Inclinación;
    private String Dispersión;
    
    
    
    public Farola(String id, String user,String text, GeoLocation geo){
    
        this.Id=id;
        this.User=user;
        this.Text=text;
        this.Geo=geo;
        
    
    }
    public void instanciateProperties(HashMap <String,String> propertiesMap){
    
    
        Tipo= propertiesMap.get("tipo");
        Bombilla= propertiesMap.get("Bombilla");
        Cubierta= propertiesMap.get("cubierta");
        Alcance= propertiesMap.get("alcance");
        Inclinación= propertiesMap.get("inclinacion");
        Dispersión= propertiesMap.get("dispersion");
        
    
    }
    
    
    public String getProperties(){
    
        return "ID="+this.Id+";User="+this.User+";Geo="+this.Geo+";Tipo="+this.Tipo+";Bombilla="+this.Bombilla+";Cubierta="+this.Cubierta+";Alcance="+this.Alcance+";Inclinación="+this.Inclinación+";Dispersión="+this.Dispersión;
    
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String User) {
        this.User = User;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }

    public GeoLocation getGeo() {
        return Geo;
    }

    public void setGeo(GeoLocation Geo) {
        this.Geo = Geo;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getBombilla() {
        return Bombilla;
    }

    public void setBombilla(String Bombilla) {
        this.Bombilla = Bombilla;
    }

    public String getCubierta() {
        return Cubierta;
    }

    public void setCubierta(String Cubierta) {
        this.Cubierta = Cubierta;
    }

    public String getAlcance() {
        return Alcance;
    }

    public void setAlcance(String Alcance) {
        this.Alcance = Alcance;
    }

    public String getInclinación() {
        return Inclinación;
    }

    public void setInclinación(String Inclinación) {
        this.Inclinación = Inclinación;
    }

    public String getDispersión() {
        return Dispersión;
    }

    public void setDispersión(String Dispersión) {
        this.Dispersión = Dispersión;
    }
   
    
    
    
    
    
}
