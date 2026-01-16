/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author Toky
 */
public class Serveur {
    private String ipPrincipal;
    private String nom;
    private String protocole;
    private int port;
    private List<ServeurEnfant> enfants;

    public Serveur(String ipPrincipal, String nom, String protocole, int port, List<ServeurEnfant> enfants) {
        this.ipPrincipal = ipPrincipal;
        this.nom = nom;
        this.protocole = protocole;
        this.port = port;
        this.enfants = enfants;
    }

    public String getIpPrincipal() {
        return ipPrincipal;
    }

    public void setIpPrincipal(String ipPrincipal) {
        this.ipPrincipal = ipPrincipal;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getProtocole() {
        return protocole;
    }

    public void setProtocole(String protocole) {
        this.protocole = protocole;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<ServeurEnfant> getEnfants() {
        return enfants;
    }

    public void setEnfants(List<ServeurEnfant> enfants) {
        this.enfants = enfants;
    }
    
    
}
