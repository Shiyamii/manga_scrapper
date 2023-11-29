package mangaAPI;

import hash.BCrypt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import static mangaAPI.SQLFileWriter.writeFilePW;

public class SQLGen {
    private DataBaseUse db;

    public SQLGen(DataBaseUse db){
        this.db = db;
    }

    public ArrayList<String> initTables(){
        ArrayList<String> sqlRequests = new ArrayList<>();
        //Table Genre
        sqlRequests.add("CREATE TABLE GENRE " +
                "(idGenre INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " nom VARCHAR(35))");

        //Table Type
        sqlRequests.add("CREATE TABLE TYPE " +
                "(idType INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " nom VARCHAR(35), " +
                " description TEXT)");

        //Table Manga
        sqlRequests.add("CREATE TABLE MANGA " +
                "(idManga INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " idType INTEGER, " +
                " titreManga VARCHAR(50), " +
                " auteur VARCHAR(50), " +
                " dessinateur VARCHAR(50), " +
                " description TEXT, " +
                " FOREIGN KEY (idType) REFERENCES TYPE(idType))");

        //Table Titre_Manga
        sqlRequests.add("CREATE TABLE TITRE_MANGA " +
                "(idTitre INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " idManga INTEGER,"+
                " titre VARCHAR(70),"+
                " isOriginal BOOLEAN,"+
                " FOREIGN KEY (idManga) REFERENCES MANGA(idManga))");

        //Table Editeur
        sqlRequests.add("CREATE TABLE EDITEUR " +
                "(idEditeur INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " nom VARCHAR(70)," +
                " lienImage VARCHAR(100))");

        //Table Produit
        sqlRequests.add("CREATE TABLE PRODUIT " +
                "(idProduit INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " idManga INTEGER, " +
                " idEditeur INTEGER, " +
                " titreTome VARCHAR(50), " +
                " numTome VARCHAR(20), " +
                " prixPublic DOUBLE(5,2), " +
                " prixAchat DOUBLE(5,2), " +
                " description TEXT, " +
                " dateParution DATE, " +
                " FOREIGN KEY (idManga) REFERENCES MANGA(idManga), " +
                " FOREIGN KEY (idEditeur) REFERENCES EDITEUR(idEditeur))");

        //Table Image
        sqlRequests.add("CREATE TABLE IMAGE " +
                "(idImage INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " idProduit INTEGER, " +
                " lienImage VARCHAR(100), " +
                " FOREIGN KEY (idProduit) REFERENCES PRODUIT(idProduit))");

        //Table Genre_Manga
        sqlRequests.add("CREATE TABLE GENRE_MANGA " +
                "(idGenre INTEGER , " +
                " idManga INTEGER, " +
                " PRIMARY KEY(idManga,idGenre), " +
                " FOREIGN KEY (idGenre) REFERENCES GENRE(idGenre), " +
                " FOREIGN KEY (idManga) REFERENCES MANGA(idManga))");

        return sqlRequests;
    }

    public ArrayList<String> initTablesCompl(){
        ArrayList<String> sqlRequests = new ArrayList<>();
        //Table Compte
        sqlRequests.add("CREATE TABLE COMPTE " +
                "(idCompte INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " identifiant VARCHAR(80), " +
                " motDePasse VARCHAR(200))");

        //Table Web_Master
        sqlRequests.add("CREATE TABLE WEB_MASTER " +
                "(idWebMaster INTEGER PRIMARY KEY REFERENCES COMPTE(idCompte))");

        //Table Adresse
        sqlRequests.add("CREATE TABLE ADRESSE " +
                "(idAdresse INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " codePostal VARCHAR(5), " +
                " ville VARCHAR(20), " +
                " rue VARCHAR(80), " +
                " numero VARCHAR(5))");

        //Table Client
        sqlRequests.add("CREATE TABLE CLIENT " +
                "(idClient INTEGER PRIMARY KEY REFERENCES COMPTE(idCompte), " +
                " idAdresse INTEGER, " +
                " adresseMail VARCHAR(80), " +
                " nom VARCHAR(40), " +
                " prenom VARCHAR(40), " +
                " FOREIGN KEY (idAdresse) REFERENCES ADRESSE(idAdresse))");

        //Table Facturation
        sqlRequests.add("CREATE TABLE FACTURATION " +
                "(idFacturation INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " idClient INTEGER, " +
                " date DATE, " +
                " fini BOOLEAN, " +
                " FOREIGN KEY (idClient) REFERENCES CLIENT(idClient))");


        //Table Produit_Facturation
        sqlRequests.add("CREATE TABLE PRODUIT_FACTURATION " +
                "(idFacturation INTEGER , " +
                " idProduit INTEGER, " +
                " nombreProduit INTEGER, " +
                " PRIMARY KEY(idProduit,idFacturation), " +
                " FOREIGN KEY (idFacturation) REFERENCES FACTURATION(idFacturation), " +
                " FOREIGN KEY (idProduit) REFERENCES PRODUIT(idProduit))");

        //Table Gestion_Stock
        sqlRequests.add("CREATE TABLE GESTION_STOCK " +
                "(idStock INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " idProduit INTEGER, " +
                " seuilLimite INTEGER, " +
                " FOREIGN KEY (idProduit) REFERENCES PRODUIT(idProduit))");

        //Table Reappro_Stock
        sqlRequests.add("CREATE TABLE REAPPRO_STOCK " +
                "(idReappro INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " idProduit INTEGER, " +
                " qte INTEGER, " +
                " date DATE, " +
                " FOREIGN KEY (idProduit) REFERENCES PRODUIT(idProduit))");

        //Table Citation
        sqlRequests.add("CREATE TABLE CITATION " +
                "(idCitation INTEGER PRIMARY KEY not NULL AUTO_INCREMENT, " +
                " citation VARCHAR(500), " +
                " personnage VARCHAR(50))");
        return sqlRequests;
    }

    public ArrayList<String> dropTables(){
        ArrayList<String> sqlRequests = new ArrayList<>();
        sqlRequests.add("DROP TABLE IF EXISTS CITATION");
        sqlRequests.add("DROP TABLE IF EXISTS REAPPRO_STOCK");
        sqlRequests.add("DROP TABLE IF EXISTS GESTION_STOCK");
        sqlRequests.add("DROP TABLE IF EXISTS PRODUIT_FACTURATION");
        sqlRequests.add("DROP TABLE IF EXISTS FACTURATION");
        sqlRequests.add("DROP TABLE IF EXISTS CLIENT");
        sqlRequests.add("DROP TABLE IF EXISTS ADRESSE");
        sqlRequests.add("DROP TABLE IF EXISTS WEB_MASTER");
        sqlRequests.add("DROP TABLE IF EXISTS COMPTE");
        sqlRequests.add("DROP TABLE IF EXISTS GENRE_MANGA");
        sqlRequests.add("DROP TABLE IF EXISTS IMAGE");
        sqlRequests.add("DROP TABLE IF EXISTS PRODUIT");
        sqlRequests.add("DROP TABLE IF EXISTS EDITEUR");
        sqlRequests.add("DROP TABLE IF EXISTS TITRE_MANGA");
        sqlRequests.add("DROP TABLE IF EXISTS MANGA");
        sqlRequests.add("DROP TABLE IF EXISTS GENRE");
        sqlRequests.add("DROP TABLE IF EXISTS TYPE");
        return sqlRequests;
    }

    public ArrayList<String> dropAllData(){
        ArrayList<String> sqlRequests = new ArrayList<>();
        sqlRequests.add("DELETE FROM CITATION");
        sqlRequests.add("DELETE FROM GESTION_STOCK");
        sqlRequests.add("DELETE FROM PRODUIT_FACTURATION");
        sqlRequests.add("DELETE FROM FACTURATION");
        sqlRequests.add("DELETE FROM CLIENT");
        sqlRequests.add("DELETE FROM ADRESSE");
        sqlRequests.add("DELETE FROM WEB_MASTER");
        sqlRequests.add("DELETE FROM COMPTE");
        sqlRequests.add("DELETE FROM GENRE_MANGA");
        sqlRequests.add("DELETE FROM IMAGE");
        sqlRequests.add("DELETE FROM PRODUIT");
        sqlRequests.add("DELETE FROM EDITEUR");
        sqlRequests.add("DELETE FROM TITRE_MANGA");
        sqlRequests.add("DELETE FROM MANGA");
        sqlRequests.add("DELETE FROM GENRE");
        sqlRequests.add("DELETE FROM TYPE");
        return sqlRequests;
    }



    public ArrayList<String> addManga(Manga m){
        ArrayList<String> sqlRequests = new ArrayList<>();
        if(!db.typeExist(m.type)){
            sqlRequests.add("INSERT INTO TYPE(nom,description) VALUES('"+m.type+"','')");
        }
        for(String s: m.genres){
            if(!db.genreExist(s)){
                sqlRequests.add("INSERT INTO GENRE(nom) VALUES('"+s+"')");
            }
        }
        if(m.dessinateur == null){
            sqlRequests.add("INSERT INTO MANGA(idType,auteur,titreManga,description) " +
                    "VALUES((SELECT t.idType FROM TYPE t WHERE t.nom = '"+m.type+"')," +
                    "'"+m.auteur+"','"+m.titre.replace("'", "''")+"','"+m.desc.replace("'", "''")+"')");
        }
        else{
            sqlRequests.add("INSERT INTO MANGA(idType,auteur,dessinateur,titreManga,description) " +
                    "VALUES((SELECT t.idType FROM TYPE t WHERE t.nom = '"+m.type+"')," +
                    "'"+m.auteur+"','"+m.dessinateur+"','"+m.titre.replace("'", "''")+"','"+m.desc.replace("'", "''")+"')");
        }
        sqlRequests.add("INSERT INTO TITRE_MANGA(idManga,titre,isOriginal) VALUES("+
                "(SELECT m.idManga FROM MANGA m WHERE m.titreManga = '"+m.titre.replace("'", "''")+"' ),"+
                "'"+m.titreOriginal.replace("'", "''")+"',TRUE)");
        for(String titre:m.titreAux){
            sqlRequests.add("INSERT INTO TITRE_MANGA(idManga,titre,isOriginal) VALUES("+
                    "(SELECT m.idManga FROM MANGA m WHERE m.titreManga = '"+m.titre.replace("'", "''")+"' ),"+
                    "'"+titre.replace("'", "''")+"',FALSE)");
        }
        for(String s: m.genres){
            sqlRequests.add("INSERT INTO GENRE_MANGA(idGenre,idManga) VALUES("+
                    "(SELECT g.idGenre FROM GENRE g WHERE g.nom = '"+s+"'),"+
                    "(SELECT m.idManga FROM MANGA m WHERE m.titreManga = '"+m.titre.replace("'", "''")+"' ))");
        }
        return sqlRequests;
    }


    public ArrayList<String> addTome(Manga m,Tome t){
        ArrayList<String> sqlRequests = new ArrayList<>();
        if(!db.editeurExist(t.editeur)){
            if(t.editeurImage == null || t.editeurImage.equals("")){
                sqlRequests.add("INSERT INTO EDITEUR(nom,lienImage) VALUES('"+t.editeur+"',NULL)");
            }
            else{
                sqlRequests.add("INSERT INTO EDITEUR(nom,lienImage) VALUES('"+t.editeur+"','"+t.editeurImage+"')");
            }
        }
        sqlRequests.add("INSERT INTO PRODUIT(idManga,idEditeur,titreTome,numTome,prixPublic,prixAchat,description,dateParution) VALUES("+
                "(SELECT m.idManga FROM MANGA m WHERE m.titreManga = '"+m.titre.replace("'", "''")+"')," +
                "(SELECT e.idEditeur FROM EDITEUR e WHERE e.nom = '"+t.editeur+"')," +
                "'"+t.nomTome.replace("'", "''")+"','"+t.numTome.replace("'", "''")+"',"+t.prixPublic +","+t.prixAchat +",'"+t.desc.replace("'", "''")+"','"+t.dateParution+"')");
        sqlRequests.add("INSERT INTO GESTION_STOCK(idProduit,seuilLimite) VALUES("+
                "(SELECT p.idProduit FROM PRODUIT p WHERE p.titreTome = '"+t.nomTome.replace("'", "''")+"')," +
                m.seuil+")");
        for(String img : t.lienImage){
            sqlRequests.add("INSERT INTO IMAGE(idProduit,lienImage) VALUES("+
                    "(SELECT p.idProduit FROM PRODUIT p WHERE p.titreTome = '"+t.nomTome.replace("'", "''")+"')," +
                    "'"+img+"')");
        }
        return sqlRequests;
    }

    public ArrayList<String> addStock(int id, String date, int quantity){
        ArrayList<String> sqlRequests = new ArrayList<>();
        sqlRequests.add("INSERT INTO REAPPRO_STOCK(idProduit,date,qte) VALUES("+id+",'"+date+"',"+quantity+")");
        return sqlRequests;
    }

    public String genDate(){
        Random r = new Random();
        int year = r.nextInt(4)+2019;
        int month = r.nextInt(12)+1;
        int day;
        if(month == 2){
            day = r.nextInt(28)+1;

        }else if(month == 4||month == 6||month == 9||month == 11){
            day = r.nextInt(30)+1;
        }else{
            day = r.nextInt(31)+1;
        }
        String date = year+"-"+month+"-"+day;
        return date;
    }

    public ArrayList<String> genStock(){
        Random r = new Random();
        ArrayList<String> sqlRequests = new ArrayList<>();
        int nbProduit = this.db.getNbPrduit();
        for(int i = 1; i<=nbProduit;i++){
            int stocks = r.nextInt(7)+4;
            sqlRequests.addAll(addStock(i,"2019-01-01",10));
            for(int j = 0; j<stocks;j++){
                String date = genDate();
                int qte = r.nextInt(40)+5;
                sqlRequests.addAll(addStock(i,date,qte));
            }
        }
        return sqlRequests;
    }



    public ArrayList<String> addAdmin(){
        String pw = "admin331;$2y$10$gh.mimzI2ZUcuJI5iovK7OA13qtPy1yNzLx9BONjXJFE4IU/CU5nu\n" +
                "admin763;$2y$10$MJaIIhvZl8cnlfjbgllkTeUsMTSBbC2Hjs3X28wljx2.JgKk2v2ly\n" +
                "admin850;$2y$10$s3LL5ybuZ0z1wbWV1ZtZhOBYMK1GGllUey3pR7DIuBMjV4.3qGf6W\n" +
                "admin447;$2y$10$tP0CqICHspJj3RYEZCHFeOFQlmovZ19XJgqBB7xl4JUL6bo7/RqeG\n" +
                "admin325;$2y$10$KCo4VFJ2x.ICdte/AduxXeWrAM89wjLBTgsUMLDr.A8sWkZmdxma.\n" +
                "admin263;$2y$10$uR5RgU3Ps6SGcBG1E2cZeubH81/zgZsysgtEmoDc2tiQWnXqxOp2O\n" +
                "admin576;$2y$10$nhe5IE5QqnJhhz1LyFudpeTaVGyLIQAmYvyT0E7ME1xGRoHgIL4HS\n" +
                "admin733;$2y$10$hQpyUE8duPQBI4LZRpsrkeOuL4lmYXlHSEhu6MApN9Z7cX6UjTFMq\n" +
                "admin304;$2y$10$RsqHMishLD2upFaWtS6pae3LUr2WBvmo8Hv3MetiuPxufr.CaUlVG\n" +
                "admin121;$2y$10$hUenfQZHQcP3vmKpdWX/beKmesrBUFWIerfklvo88Eqho5pGKWYP2";
        String[] pwTab = pw.split("\n");
        ArrayList<String> sqlRequests = new ArrayList<>();
        ArrayList<String> accounts = new ArrayList<>();
        for(int i = 1 ; i<=10;i++){
            String nom = "admin"+i;
            sqlRequests.add("INSERT INTO COMPTE(identifiant,motDePasse) VALUES('"+nom+"','"+pwTab[i-1].split(";")[1]+"')");
            accounts.add(nom+";"+pwTab[i-1].split(";")[0]);
            sqlRequests.add("INSERT INTO WEB_MASTER VALUES((SELECT c.idCompte FROM COMPTE c WHERE c.identifiant = '"+nom+"'))");
        }
        writeFilePW("adminAccount",accounts);
        return sqlRequests;
    }

    public String genPassword(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public ArrayList<String> addAccount(){
        String[] noms = {"MARTIN","BERNARD","THOMAS","PETIT","ROBERT","RICHARD","DURAND","DUBOIS","MOREAU","LAURENT","SIMON","MICHEL","LEFEBVRE","LEROY","ROUX","DAVID","BERTRAND","MOREL","FOURNIER","GIRARD","BONNET","DUPONT","LAMBERT","FONTAINE","ROUSSEAU","VINCENT","MULLER","LEFEVRE","FAURE","ANDRE","MERCIER","BLANC","GUERIN","BOYER","GARNIER","CHEVALIER","FRANCOIS","LEGRAND",
                "GAUTHIER","GARCIA","PERRIN","ROBIN","CLEMENT","MORIN","NICOLAS","HENRY","ROUSSEL","MATHIEU","GAUTIER","MASSON","MARCHAND","DUVAL","DENIS","DUMONT","MARIE","LEMAIRE","NOEL","MEYER","DUFOUR","MEUNIER","BRUN","BLANCHARD","GIRAUD","JOLY","RIVIERE","LUCAS","BRUNET"};
        String[] prenoms = {"Henri","Maximilien","Mona","Colette","Kristian","Baptiste","Youssef","Sabrina","Teodora","Katy","Simon","Guillaume","Elias","Reich","Corentin","Jean","Leopold","Florent","Tom","Ivan","Olivier","William","Titouan"};
        String[] mail = {"gmail.com","hotmail.com","yahoo.com","outlook.fr"};
        Random r = new Random();
        ArrayList<String> sqlRequests = new ArrayList<>();
        ArrayList<String> accounts = new ArrayList<>();
        for(int i = 1 ; i<=150;i++){
            String nom =  noms[r.nextInt(noms.length)];
            String prenom = prenoms[r.nextInt(prenoms.length)];
            String email =nom +"."+prenom+r.nextInt(100)+"@"+mail[r.nextInt(mail.length)];
            String pw = genPassword();
            sqlRequests.add("INSERT INTO COMPTE(identifiant,motDePasse) VALUES('"+email+"','"+BCrypt.hashpw(pw,BCrypt.gensalt())+"')");
            accounts.add(email+";"+pw );
            sqlRequests.add("INSERT INTO CLIENT(idClient,adresseMail,nom,prenom) VALUES((SELECT c.idCompte FROM COMPTE c WHERE identifiant = '"+email+"'),'"+email+"','"+nom+"','"+prenom+"')");
        }
        writeFilePW("account",accounts);
        return sqlRequests;
    }

    public ArrayList<String> addQuotes(ArrayList<Quote> quotes){
        ArrayList<String> sqlRequests = new ArrayList<>();
        for(Quote q:quotes){
            sqlRequests.add("INSERT INTO CITATION(citation,personnage) VALUES('"+q.quote.replace("'","''")+"','"+q.caracther+"')");
        }
        return sqlRequests;
    }

    public ArrayList<String> addFactures(){
        Random r = new Random();
        int nbProduit = this.db.getNbPrduit();
        ArrayList<String> sqlRequests = new ArrayList<>();
        ArrayList<Integer> ids = db.getClients();
        for(Integer i:ids){
            for(int j = 0 ; j< r.nextInt(15);j++){
                String date = genDate();
                sqlRequests.add("INSERT INTO FACTURATION(idClient,date,fini) VALUES("+i+",'"+date+"',1)");
                for(int k = 0 ; k< r.nextInt(30)+2;k++) {
                    sqlRequests.add("INSERT INTO PRODUIT_FACTURATION(idFacturation,idProduit,nombreProduit) VALUES((SELECT f.idFacturation FROM FACTURATION f WHERE f.idClient = " + i + " ORDER BY f.idFacturation DESC LIMIT 1)," + (r.nextInt(nbProduit - 1) + 1) + "," + (r.nextInt(4) + 1) + ")");

                }
            }
        }
        return sqlRequests;
    }

}
