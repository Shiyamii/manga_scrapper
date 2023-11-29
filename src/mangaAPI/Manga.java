package mangaAPI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Manga {
    String titre;
    String titreOriginal;
    public ArrayList<String> titreAux = new ArrayList<>();
    String type;
    String auteur;
    String dessinateur;
    String desc;
    int seuil;
    public ArrayList<String> genres;
    public String nomRepo;
    public ArrayList<Tome> tomes;

    public Manga(String titre, String titreOriginal, String type, String auteur, String desc, ArrayList<String> genres){
        Random r = new Random();
        this.seuil = r.nextInt(10)+5;
        this.tomes = new ArrayList<>();
        this.titre = titre;
        String[] titreTab = titreOriginal.split(" / ");
        this.titreOriginal = titreTab[titreTab.length-1];
        if(titreTab.length>1){
            for(int i = titreTab.length-2; i >=0;i--){
                titreAux.add(titreTab[i]);
            }
        }
        if(titre.equals("Jujutsu Kaisen")){
            titreAux.add("JJK");
        }
        this.type = type;
        this.auteur = auteur;
        this.dessinateur = auteur;
        this.desc = desc;
        this.genres = genres;
        if(this.titre.contains("'")){
            this.nomRepo = titreOriginal.split(" / ")[0].replace(" ","").replace(":","");

        }
        else{
            this.nomRepo = titre.replace(" ","").replace(":","");
        }
    }

    public Manga(String titre, String titreOriginal, String type, String auteur, String dessinateur, String desc, ArrayList<String> genres){
        this.tomes = new ArrayList<>();
        this.titre = titre;
        String[] titreTab = titreOriginal.split(" / ");
        this.titreOriginal = titreTab[titreTab.length-1];
        if(titreTab.length>1){
            titreAux.add(titreTab[0]);
        }
        this.type = type;
        this.auteur = auteur;
        this.dessinateur = dessinateur;
        this.desc = desc;
        this.genres = genres;
        if(this.titre.contains("'")){
            this.nomRepo = titreOriginal.split(" / ")[0].replace(" ","");

        }
        else{
            this.nomRepo = titre.replace(" ","");
        }
    }

    @Override
    public String toString() {
        return titre + ", type:" + type + ", auteur: " + auteur + ", \ndesc='" + desc + '\'' +
                ';';
    }

    public void addTome(Tome tome) {
        this.tomes.add(tome);
    }

    public Tome getTome(int index) {
        return tomes.get(index);
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("titre",this.titre);
        json.put("titreOriginal",this.titreOriginal);

        JSONArray titreJson = new JSONArray();
        titreJson.addAll(titreAux);
        json.put("titres",titreJson);

        json.put("type",this.type);
        json.put("auteur",this.auteur);
        json.put("dessinateur",this.dessinateur);

        JSONArray genreJson = new JSONArray();
        genreJson.addAll(genres);
        json.put("genre",genreJson);

        json.put("desc",this.desc);
        JSONArray tomesJson = new JSONArray();
        for(Tome t:this.tomes){
            tomesJson.add(t.getJSON());
        }
        json.put("tomes",tomesJson);
        return json;
    }


}
