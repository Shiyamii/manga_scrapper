package mangaAPI;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

public class DataBaseUse {

    private Connection conn;

    public DataBaseUse(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://ip:3306/db", "p3", "FicsiT22!");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeInsertSQL(ArrayList<String> sqlRequests){
        try {
            Statement stmt = conn.createStatement();
            for(String sql: sqlRequests){
                try {
                    stmt.executeUpdate(sql);
                    Logs.addLog(Level.FINEST,"SQL executé: "+sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean editeurExist(String editeur){
        boolean b = false;
        try {
            Statement stmt = conn.createStatement();
            editeur= "'"+editeur+"'";
            ResultSet rs = stmt.executeQuery("SELECT e.nom FROM EDITEUR e WHERE e.nom="+editeur+"");
            b = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }



    public boolean genreExist(String genre){
        boolean b = false;
        try {
            Statement stmt = conn.createStatement();
            genre = "'"+genre+"'";
            ResultSet rs = stmt.executeQuery("SELECT g.nom FROM GENRE g WHERE g.nom="+genre+"");
            b = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public boolean typeExist(String type){
        boolean b = false;
        try {
            Statement stmt = conn.createStatement();
            type= "'"+type+"'";
            ResultSet rs = stmt.executeQuery("SELECT e.nom FROM TYPE e WHERE e.nom="+type+"");
            b = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    public int getNbPrduit(){
        try {
            Statement stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT COUNT(p.idProduit) FROM PRODUIT p");
            if (rs.next())
                return rs.getInt("COUNT(p.idProduit)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<Integer> getClients(){
        ArrayList<Integer> ids =new ArrayList<>();
        boolean b = false;
        try {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT c.idClient FROM CLIENT c");
            while(rs.next()){
                ids.add(rs.getInt("c.idClient"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }



}
