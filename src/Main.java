import mangaAPI.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        boolean createDB = true;



        String[] mangas = {
                "Tokyo Ghoul",
                "Tokyo Ghoul-re",
                "one piece",
                "DR. Stone",
                "My hero academia",
                "Bleach",
                "Black clover",
                "Shingeki no Kyojin",
                "Kimetsu no Yaiba",
                "Kaguya-sama - Love is War",
                "Naruto","Fire Force",
                "Code Geass - Lelouch of the Rebellion",
                "Tokyo Revengers","Dragon Ball",
                "Jujutsu Kaisen","Noragami",
                "Hunter x hunter",
                "Death Note",
                "GTO - Great Teacher Onizuka",
                "The Promised Neverland",
                "Spice - Wolf",
                "Oshi no ko",
                "Made in Abyss",
                "20th Century Boys",
                "Berserk",
                "Seven Deadly Sins",
                "One-Punch Man",
                "GTO - Great Teacher Onizuka"};

        MangaAPI m = new MangaAPI(mangas);

        m.makesMangas();


        DataBaseUse db = new DataBaseUse();
        SQLGen s = new SQLGen(db);

        if(createDB){
            db.executeInsertSQL(s.dropTables());
            db.executeInsertSQL(s.initTables());
            db.executeInsertSQL(s.initTablesCompl());

            SQLFileWriter.writeFile("dropTables",s.dropTables());
            ArrayList<String> sqlTables = s.initTables();
            sqlTables.addAll(s.initTablesCompl());
            SQLFileWriter.writeFile("creationTables",sqlTables);

        }
        else{
            db.executeInsertSQL(s.dropAllData());

            SQLFileWriter.writeFile("deleteAllData",s.dropAllData());
        }

        ArrayList<String> sqlInserts = s.initTables();
        for(Manga ma : m.mangas){
            db.executeInsertSQL(s.addManga(ma));
            sqlInserts.addAll(s.addManga(ma));
            for(Tome t: ma.tomes){
                db.executeInsertSQL(s.addTome(ma,t));
                sqlInserts.addAll(s.addTome(ma,t));
            }
        }
        SQLFileWriter.writeFile("insertValues",sqlInserts);

        ArrayList<String> sqlOtherData =new ArrayList<>();

        sqlOtherData.addAll(s.addQuotes(m.importQuote()));
        sqlOtherData.addAll(s.genStock());
        sqlOtherData.addAll(s.addAdmin());
        sqlOtherData.addAll(s.addAccount());
        db.executeInsertSQL(sqlOtherData);

        SQLFileWriter.writeFile("insertOtherData",sqlOtherData);

        ArrayList<String> sqlOtherData2 =new ArrayList<>();
        sqlOtherData2.addAll(s.addFactures());

        db.executeInsertSQL(sqlOtherData2);
        SQLFileWriter.writeFile("insertOtherData2",sqlOtherData2);




        db.close();
    }
}
