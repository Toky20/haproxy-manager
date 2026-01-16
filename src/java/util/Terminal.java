/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Serveur;

/**
 *
 * @author Toky
 */
public class Terminal {
    
    public static void supprimerTousLesPortProxy() throws IOException, InterruptedException {
        // Exécuter la commande pour afficher toutes les règles
        Process process = Runtime.getRuntime().exec("netsh interface portproxy show all");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // Pattern pour parser les lignes de sortie
        Pattern pattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+(\\d+)");

        List<String> commandesSuppression = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String listenAddress = matcher.group(1);
                String listenPort = matcher.group(2);

                // Construire la commande de suppression
                String commandeSuppression = String.format("netsh interface portproxy delete v4tov4 listenport=%s listenaddress=%s ",
                        listenPort , listenAddress);
                commandesSuppression.add(commandeSuppression);
                System.out.println(commandeSuppression);
                //executerCommande(commandeSuppression);
            }
        }

        // Exécuter les commandes de suppression
        for (String commande : commandesSuppression) {
            executerCommande(commande);
        }
    }
    
    public static void configurerPortProxy(List<Serveur> serveurs) throws IOException, InterruptedException {
        for (Serveur serveur : serveurs) {
            String commande = String
                    .format("netsh interface portproxy add v4tov4 listenaddress=0.0.0.0 listenport=%d connectaddress=%s connectport=%d",
                    serveur.getPort(), serveur.getIpPrincipal(), serveur.getPort());
            executerCommande(commande);
        }
    }
    
    public static void executerCommande(String commande) throws IOException, InterruptedException {
        // Obtenir une référence au processus
        Process process = Runtime.getRuntime().exec(commande);

        // Récupérer la sortie standard
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String ligne;
        while ((ligne = reader.readLine()) != null) {
            System.out.println(ligne);
        }

        // Attendre la fin du processus
        int exitValue = process.waitFor();
        System.out.println("Processus terminé avec le code : " + exitValue);
    }
    
    public static void restartHAProxy() throws IOException, InterruptedException{
        //executerCommande("cmd /c C:\\Users\\itu\\Documents\\NetBeansProjects\\HAProxyManager\\script\\restartHAProxy.bat");
        //executerCommande(" systemctl stop haproxy");
        executerCommande(" systemctl restart haproxy");
    }
}
