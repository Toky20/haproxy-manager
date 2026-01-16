/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.Fichier;
import util.Terminal;


public class ConfigFile {
    
    static String configFilePath="/home/itu/Bureau/HAProxyManager/template/haproxy.cfg";
    static String destinationPath="/etc/haproxy";
    
    public static void generateAndSaveConfigFile(List<Serveur> serveurs) throws IOException {
        String config=generateConfigFile(serveurs);
        Fichier.sauvegarderFichier(config, "haproxy.cfg", destinationPath);
    }
    
    public static String generateConfigFile(List<Serveur> serveurs) throws IOException {
        String template = Fichier.readTemplate(configFilePath);

        String result = template;
        result=result.replace("{konfigy}", generateConfigContent(serveurs));
        return result;
    }

    private static String generateConfigContent(List<Serveur> serveurs) {
        StringBuilder configBuilder = new StringBuilder();

        // Loop through each server and build configuration entries
        for (Serveur serveur : serveurs) {
            configBuilder.append(buildServerConfig(serveur));
            configBuilder.append("\n"); // Add newline after each server entry
        }

        return configBuilder.toString();
    }
    
    private static String buildServerConfig(Serveur serveur) {
        StringBuilder serverConfig = new StringBuilder();

        if ("http".equalsIgnoreCase(serveur.getProtocole())) {
            // Configuration HTTP
            serverConfig.append("frontend ").append(serveur.getNom()).append("_front\n");
            serverConfig.append("\tbind *:").append(serveur.getPort()).append("\n");
            serverConfig.append("\tdefault_backend ").append(serveur.getNom()).append("_back\n\n");

            serverConfig.append("backend ").append(serveur.getNom()).append("_back\n");
            serverConfig.append("\tbalance roundrobin\n");
            serverConfig.append("\tcookie SERVERID insert indirect nocache\n\n");

            for (int i = 0; i < serveur.getEnfants().size(); i++) {
                ServeurEnfant enfant = serveur.getEnfants().get(i);
                serverConfig.append("\tserver server")
                        .append(i + 1) // Index du serveur enfant + 1
                        .append(" ")
                        .append(enfant.getIp())
                        .append(":")
                        .append(enfant.getPort())
                        .append(" check cookie srv")
                        .append(i + 1) // Index du serveur enfant + 1
                        .append("\n");
            }
        } else if ("tcp".equalsIgnoreCase(serveur.getProtocole())) {
            // Configuration TCP
            serverConfig.append("frontend ").append(serveur.getNom()).append("_front\n");
            serverConfig.append("\tbind *:").append(serveur.getPort()).append("\n");
            serverConfig.append("\tmode tcp\n");
            serverConfig.append("\tdefault_backend ").append(serveur.getNom()).append("_backends\n\n");

            serverConfig.append("backend ").append(serveur.getNom()).append("_backends\n");
            serverConfig.append("\tmode tcp\n");
            serverConfig.append("\tbalance roundrobin\n");

            for (int i = 0; i < serveur.getEnfants().size(); i++) {
                ServeurEnfant enfant = serveur.getEnfants().get(i);
                serverConfig.append("\tserver ")
                        .append(serveur.getNom())
                        .append("_")
                        .append(i + 1) // Index du serveur enfant + 1
                        .append(" ")
                        .append(enfant.getIp())
                        .append(":")
                        .append(enfant.getPort())
                        .append(" check")
                        .append("\n");
            }
        } else {
            // Gestion des protocoles non supportés
            System.err.println("Protocole non supporté : " + serveur.getProtocole());
        }

        return serverConfig.toString();
    }

}
