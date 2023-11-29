package mangaAPI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Tome {
    String nomTome;
    String numTome;
    String dateParution;
    String prixPublic;
    String prixAchat;
    String editeur;
    String editeurImage;
    String desc;
    ArrayList<String> lienImage;

    public Tome(String nomTome, String numTome, String dateParution, String prixPublic, String prixAchat, String editeur, String editeurImage, String desc, ArrayList<String> lienImage){
        this.nomTome = nomTome;
        this.numTome = numTome;
        this.dateParution = dateParution;
        this.prixPublic = prixPublic;
        this.prixAchat = prixAchat;
        this.editeur = editeur;
        this.editeurImage = editeurImage;
        this.desc = desc;
        this.lienImage = lienImage;
    }


    @Override
    public String toString() {
        return "Tome{" +
                "nomTome='" + nomTome + '\'' +
                ", numTome='" + numTome + '\'' +
                ", dateParution='" + dateParution + '\'' +
                ", prix='" + prixPublic + '\'' +
                ", editeur='" + editeur + '\'' +
                ", desc='" + desc + '\'' +
                ", lienImage=" + lienImage +
                '}';
    }

    public JSONObject getJSON() {
        JSONObject tome = new JSONObject();
        tome.put("nom",nomTome);
        tome.put("num",numTome);
        tome.put("dateParution",dateParution);
        tome.put("prixPublic", prixPublic);
        tome.put("prixAchat", prixAchat);
        tome.put("editeur",editeur);
        tome.put("editeurImage",editeurImage);
        tome.put("desc",desc);
        JSONArray images = new JSONArray();
        for (String i : lienImage){
            images.add(i);
        }
        tome.put("images",images);
        return tome;
    }
}
