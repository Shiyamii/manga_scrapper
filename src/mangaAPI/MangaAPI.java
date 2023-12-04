package mangaAPI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;

public class MangaAPI {
    private String[] animeNames;
    private boolean downloadImages;
    public ArrayList<Manga> mangas = new ArrayList<>();

    public MangaAPI(String[] animeNames, boolean downloadImages){
        this.downloadImages = downloadImages;
        this.animeNames = animeNames;
    }

    public Manga getManga(String nom){
        Logs.addLog(Level.CONFIG,"Téléchargement des données sur "+nom);
        Manga manga = null;
        try {
            Document doc = getHtmlManga(nom);
            manga = getMangaInfo(doc);
            File json = new File("./data/"+manga.nomRepo+"/info.json");
            if(json.exists()){
                Logs.addLog(Level.CONFIG,"Création des tomes pour "+nom+" appartir de \"./data/"+manga.nomRepo+"/info.json\"");
                addTomesExist(json,manga);
            }
            else{
                Logs.addLog(Level.CONFIG,"Téléchargement des données sur les tomes de "+nom);
                addTomes(doc,manga);


                File f = new File("./data");
                if (!f.exists()) {
                    f.mkdir();
                }
                File file = new File("./data/"+manga.nomRepo);
                if (!file.exists()) {
                    file.mkdir();
                }

                try {
                    Files.write(Paths.get("./data/"+manga.nomRepo+"/info.json"), manga.toJSON().toJSONString().getBytes());
                    Logs.addLog(Level.CONFIG,"Ecriture des donnéess de "+nom+" dans \"./data/"+manga.nomRepo+"/info.json\"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return manga;
    }


    public Manga getMangaInfo(Document doc){
        String auteur = null;
        String dessinateur = null;
        String titreOrg = null;
        String titreAlt = null;
        String type = null;
        String desc;
        ArrayList<String> genres = new ArrayList<>();
        Elements nameSpan = doc.select("h1.h1titre>span");
        String titre =nameSpan.text();
        Elements ulInfo = doc.select("div.liste_infos>ul.mb10");
        for (Element e: ulInfo.get(0).children()){
            if(e.html().contains("Auteur : ")){
                auteur = (e.text().replace("Auteur : ",""));
            }
            if(e.html().contains("Scénariste : ")){
                auteur = (e.text().replace("Scénariste : ",""));
            }
            if(e.html().contains("Scénaristes : ")){
                auteur = (e.text().replace("Scénaristes : ",""));
            }
            if(e.html().contains("Genres : ")){
                String[] genresTab = e.text().replace("Genres : ","").split(" - ");
                genres = new ArrayList<>(List.of(genresTab));
            }
            if(e.html().contains("Dessinateur : ")){
                dessinateur = (e.text().replace("Dessinateur : ","")).replace(" (mangaka)","");
            }
            if(e.html().contains("Type : ")){
                type = (e.text().replace("Type : ",""));
            }
            if(e.html().contains("Titre original : ")){
                titreOrg = (e.text().replace("Titre original : ",""));
            }
            if(e.html().contains("Titre alternatif : ")){
                titreAlt = (e.text().replace("Titre alternatif : ",""));
            }
        }
        if(titreAlt != null){
            titreOrg = titreAlt+" / "+titreOrg;
        }
        Elements descInfo = doc.select("div.description");
        desc = descInfo.get(0).text();
        if(dessinateur==null){
            return new Manga(titre,titreOrg,type,auteur,desc,genres);
        }
        else{
            return new Manga(titre,titreOrg,type,auteur,dessinateur,desc,genres);
        }
    }

    public void addTomes(Document doc, Manga manga){
        Elements tomesInfo = doc.select("div#edition_0>div#edition_0-1");
        String price = null;
        for(Element t : tomesInfo.get(0).children()){
            if(t.text().contains("Vol. ")){
                Logs.addLog(Level.FINE,"Téléchargement des données sur le tome "+ t.text().replace("Vol. ","")+ " de "+manga.titre );
                Tome tome = createTome(t.select("a").get(0).absUrl("href"),t.text().replace("Vol. ",""),manga.nomRepo, price);
                if(tome != null){
                    price = tome.prixAchat;
                    manga.addTome(tome);
                }
            }
        }
    }

    public void addTomesExist(File file, Manga manga){
        try {
            JSONParser jsonP = new JSONParser();
            JSONObject jsonO = (JSONObject) jsonP.parse(new FileReader(file));
            JSONArray jsonArray = (JSONArray) jsonO.get("tomes");
            for(int i = 0; i< jsonArray.size();i++){
                JSONObject o = (JSONObject) jsonArray.get(i);
                ArrayList<String> images = new ArrayList<>();
                String nomTome = o.get("nom").toString();
                String numTome = o.get("num").toString();
                String dateParution = o.get("dateParution").toString();
                String prixAchat = o.get("prixAchat").toString();
                String prixPublic = o.get("prixPublic").toString();
                String editeur = o.get("editeur").toString();
                String desc = o.get("desc").toString();
                String editeurImage = null;
                if(downloadImages){
                    Object editeurImageObj = o.get("editeurImage");
                    if(editeurImageObj != null){
                        editeurImage = editeurImageObj.toString();
                    }
                    JSONArray a = (JSONArray) o.get("images");
                    for(int j = 0;j<a.size();j++){
                        images.add(a.get(j).toString());
                    }
                }
                manga.addTome(new Tome(nomTome,numTome,dateParution,prixPublic,prixAchat,editeur,editeurImage,desc,images));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    public Tome createTome(String tomeLink, String numTome, String nomRepo, String price){
        try {
            Thread.sleep(2000);
            Document tome = getHtmlLink(tomeLink);
            //tomeLink.select("img").get(0).absUrl("src")
            String nomTome =tome.select("h1.h1titre>span").text();
            String dateParution = null;
            String prix = null;
            Elements infosTome =  tome.select("div.liste_infos>ul.mb10>li");
            for(Element info : infosTome){
                if(info.html().contains("Date de parution VF : ")){
                    String dateParutionGet = (info.text().replace("Date de parution VF : ",""));
                    String[] date = dateParutionGet.split("/");
                    if(date.length>2){
                        dateParution = date[2]+"-"+date[1]+"-"+date[0];
                    }
                }
                if(info.html().contains("Prix : ")){
                    prix = (info.text().replace("Prix : ",""));
                    prix = prix.split(" ")[0];
                    prix = prix.substring(0, prix.length() - 1);
                }

            }
            if(dateParution == null || Integer.parseInt(dateParution.split("-")[0])>2022){
                return null;
            }
            String editeur = tome.select("div.liste_infos>ul.mb10>li.first>span>a>span").text();
            String desc = tome.select("div.description").text();
            String imageEditeur = null;
            ArrayList<String> lienImage = new ArrayList<>();
            if(downloadImages) {
                imageEditeur = tome.select("div.liste_infos>ul.mb10>li.first> span:nth-of-type(2)>a").get(0).absUrl("href");
                imageEditeur = downloadPublisherImage(imageEditeur);
                Elements imageLinks = tome.select("div.image_fiche");
                int n = 1;
                for (Element e : imageLinks.get(0).children()) {
                    if (!e.absUrl("href").isEmpty() && !e.className().equals("tbn preview")) {
                        String link = e.absUrl("href");
                        if (link.contains("www.nautiljon.com")) {
                            link = link.replace("\\", "");
                            int i = link.indexOf('?');
                            if (i > -1) {
                                link = link.substring(0, i);
                            }
                            String lien = downloadImage(link, n, numTome, nomRepo);
                            n++;
                            if (lien != null) {
                                lienImage.add(lien);
                            }
                        }
                    }
                }
            }

            if(price == null){
                Random r = new Random();
                double prixDb = Double.parseDouble(prix);
                double percent = 1 + (r.nextDouble() * (10 - 1));
                percent /= 100;
                prixDb -= prixDb * percent;
                DecimalFormat df = new DecimalFormat("###.##");
                price = df.format(prixDb).replace(",",".");
            }

            return new Tome(nomTome,numTome,dateParution,prix,price,editeur,imageEditeur,desc,lienImage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Document getHtmlManga(String nomAnime) throws IOException {
        nomAnime = nomAnime.replace(" ","+").toLowerCase();
        Connection c = Jsoup.connect("https://www.nautiljon.com/mangas/"+nomAnime+".html");
        return c.userAgent("Chrome").get();
    }

    public Document getHtmlLink(String link) throws IOException {
        Connection c = Jsoup.connect(link);
        return c.userAgent("Chrome").get();
    }

    public String getJSON(){
        JSONObject o = new JSONObject();
        JSONArray a = new JSONArray();
        for(int i = 0;i<this.animeNames.length;i++){
            Manga m = getManga(animeNames[i]);
            a.add(m.toJSON());
        }
        o.put("mangas",a);
        return o.toJSONString();
    }

    public void makesMangas(){
        for(int i = 0;i<this.animeNames.length;i++){
            mangas.add(getManga(animeNames[i]));
        }
    }


    public String downloadImage(String urlSt,int i,String ntome,String nomRepo){
        File image = new File("./img/"+nomRepo+"/"+ntome+"."+i+".webp");
        if(!image.exists()){
            URL url = null;
            try {
                url = new URL(urlSt);
                byte[] response = getImage(url);
                File file = new File("./img/"+nomRepo);
                if (!file.exists()) {
                    file.mkdir();
                }
                FileOutputStream fos = new FileOutputStream("./img/"+nomRepo+"/"+ntome+"."+i+".webp");
                fos.write(response);
                fos.close();
                return "./img/"+nomRepo+"/"+ntome+"."+i+".webp";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else{
            return "./img/"+nomRepo+"/"+ntome+"."+i+".webp";
        }

    }

    public String downloadPublisherImage(String lien){
        String name = lien.split("/")[4].split(",")[0];
        File img = new File("./img/publisher/" + name + ".webp");
        if(img.exists()){
            return "./img/publisher/" + name + ".webp";
        }
        else {
            try {
                Document doc = getHtmlLink(lien);
                Elements imageLinks = doc.select("div.image_fiche>a.cboxImage");
                if(!imageLinks.isEmpty()){
                    String imageLink = imageLinks.get(0).absUrl("href");
                    URL url = new URL(imageLink);
                    byte[] response = getImage(url);
                    File file = new File("./img/publisher");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    FileOutputStream fos = new FileOutputStream("./img/publisher/" + name + ".webp");
                    fos.write(response);
                    fos.close();
                    return "./img/publisher/" + name + ".webp";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private byte[] getImage(URL url) throws IOException {
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        File f = new File("./img");
        if (!f.exists()) {
            f.mkdir();
        }
        return out.toByteArray();
    }

    public ArrayList<Quote> importQuote(){
        BufferedReader reader;
        ArrayList<Quote> quotes = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader("data/quote/data.csv"));
            String line = reader.readLine();

            while (line != null) {
                String[] lines = line.split(";");
                quotes.add(new Quote(lines[0],lines[1]));
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quotes;
    }

}
