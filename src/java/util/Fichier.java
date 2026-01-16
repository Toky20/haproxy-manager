/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Toky
 */
public class Fichier {
    
    public static String readTemplate(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }
    
    public static void sauvegarderFichier(String contenu, String nomFichier, String emplacement) throws IOException {
        // Création du chemin complet du fichier
        String cheminComplet = emplacement + "/" + nomFichier;

        // Création d'un objet FileWriter pour écrire dans le fichier
        try (FileWriter writer = new FileWriter(cheminComplet)) {
            writer.write(contenu);
        }
    }
}
