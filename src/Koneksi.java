import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Koneksi {
    public static Connection conn;
    public static Statement stat;

    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/databuah";
            String user = "root";
            String password = "";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,password);
            stat = conn.createStatement();
            System.out.println("Koneksi berhasil");
        } catch (Exception e){
            System.out.println("Koneksi gagal "+e.getMessage());
        }
    }
}
