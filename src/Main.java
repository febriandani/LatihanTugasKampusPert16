import java.sql.*;
import java.lang.String;
import java.util.Scanner;

public class Main {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String url = "jdbc:mysql://localhost:3306/databuah";
    static final String user = "root";
    static final String password = "";

    static Connection conn;
    static Statement stat;
    static ResultSet rs;

    static boolean lanjutkan = true;

    //method menampilkan menu awal
    static void TampilMenu() throws SQLException {
        Scanner input = new Scanner(System.in);

        while (lanjutkan) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. STOK BUAH TERSEDIA");
            System.out.println("2. EDIT STOK BUAH");
            System.out.println("3. CARI DATA");
            System.out.println("4. TAMBAH STOK BUAH");
            System.out.println("5. DELETE STOK BUAH");
            System.out.println("6.EXIT");
            System.out.println("Pilih : ");
            int pilih = input.nextInt();

            switch (pilih) {
                case 1:
                    tampilData();
                    System.out.println("Diatas adalah daftar buah yang tersedia");
                    System.out.print("\nApakah anda ingin beli (y/n)?");
                    String pilih1 = input.next();
                    if (pilih1.equalsIgnoreCase("y")) {
                        beliBuah();
                    } else {
                        TampilMenu();
                    }

                    break;

                case 2:
                    Edit();
                    break;

                case 3:
                    cariData();
                    break;

                case 4:
                    TambahStokBuah();
                    break;

                case 5:
                    DeleteStokBuah();
                    break;

                case 6:
                    System.exit(0);

                default:
                    System.out.println("Kode menu salah.");
            }
            lanjutkan = ulang("Apakah anda ingin kembali ke awal?");
        }
    }

    //method tampilkan data dari database
    static void tampilData() {

        Scanner input = new Scanner(System.in);
        String sql = "SELECT * FROM stokbuah";

        try {
            rs = stat.executeQuery(sql);
            System.out.println("\n|\t\tKODE\t\t|\t\tNAMA BUAH\t|\t\tHARGA\t\t|");
            System.out.println("==================================================================");

            while (rs.next()) {
                String kodebuah = rs.getString("kodebuah");
                String namabuah = rs.getString("namabuah");
                String hargabuah = rs.getString("hargabuah");

                System.out.println(String.format("|%s\t\t\t\t|%s\t\t\t\t|%s\t\t\t\t|",kodebuah,namabuah,hargabuah));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method transaksi
    static void beliBuah() throws SQLException {
        tampilData();
        Scanner input = new Scanner(System.in);
        String kdbuah = "";
        String namabuah = "";
        String namacustomer = "";
        int hargabuah = 0;
        int jmlbeli;
        int bayar;
        int diskon = 0;
        int totalbayar;


        System.out.println("Masukan nama terlebih dahulu : ");
        namacustomer = input.nextLine();
        while (lanjutkan) {
            System.out.print("Masukan kode buah yang ingin anda beli : ");
            kdbuah = input.next();
            try {

                String sql2 = "SELECT * FROM stokbuah "+"WHERE kodebuah LIKE ('%" + kdbuah + "%')";
                rs = stat.executeQuery(sql2);


                while (rs.next()) {
                    System.out.print("\n"+rs.getString("namabuah"));
                    namabuah = rs.getString("namabuah");
                    System.out.print("\n"+rs.getInt("hargabuah"));
                    hargabuah = rs.getInt("hargabuah");
                }
                System.out.println("\nNama buah " + namabuah);
                System.out.println("\nHarga buah " + hargabuah);
                System.out.println("\nJml beli = ");
                jmlbeli = input.nextInt();
                bayar = jmlbeli * hargabuah;
                System.out.println("bayar = " + bayar);
                if (bayar >= 300000) {
                    diskon = bayar * 10 / 100;
                }
                System.out.println("Diskon = " + diskon);
                totalbayar = bayar - diskon;
                System.out.println("Total bayar = " + totalbayar);

                stat.executeUpdate("INSERT INTO belibuah (kodebuah, namabuah, hargabuah, jmlbeli, bayar, diskon, totalbayar) VALUES('" + kdbuah + "','" + namabuah + "','" + hargabuah + "','" + jmlbeli + "','" + bayar + "','" + diskon + "','" + totalbayar + "')");
                stat.executeUpdate("INSERT INTO backupbeli (namacustomer, kodebuah, namabuah, hargabuah, jmlbeli, bayar, diskon, totalbayar) VALUES('"+namacustomer+"','" + kdbuah + "','" + namabuah + "','" + hargabuah + "','" + jmlbeli + "','" + bayar + "','" + diskon + "','" + totalbayar + "')");

            } catch(SQLException e){
                e.printStackTrace();
            }
            lanjutkan = Tambahdt("Apakah anda ingin tambah data lagi?");
        }
    }

    //method untuk menampilkan seluruh buah yang di beli
    static void TampilBeli(){

        Scanner input = new Scanner(System.in);
        String sql = "SELECT * FROM belibuah";

        try {
            rs = stat.executeQuery(sql);

            System.out.println("\n|\t\tKODE\t\t|\t\tNAMA BUAH\t\t|\t\tHARGA\t\t|\t\tJMLBELI\t\t|\t\tBAYAR\t\t|\t\tDISKON\t\t|\t\tTOTAL BAYAR|");
            System.out.println("=================================================================================================================================================================================");


            while (rs.next()) {
                String kdbuah = rs.getString("kodebuah");
                String namabuah = rs.getString("namabuah");
                int hargabuah = rs.getInt("hargabuah");
                int jmlbeli = rs.getInt("jmlbeli");
                int bayar = rs.getInt("bayar");
                int diskon = rs.getInt("diskon");
                int totalbayar = rs.getInt("totalbayar");


                System.out.println(String.format("|\t\t%s\t\t|\t\t%s\t\t|\t\t%d\t\t|\t\t%d\t\t|\t\t%d\t\t|\t\t%d\t\t|\t\t%d|", kdbuah, namabuah, hargabuah, jmlbeli, bayar, diskon,totalbayar));
            }
            System.out.println("\n");
            TotalBayar();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method untung tambah data lagi / tidak
    static boolean Tambahdt(String message) throws SQLException {
        Scanner input = new Scanner(System.in);
        System.out.print("\n " +message+"(y/n)? ");
        String tmbh = input.next();
        if (tmbh.equalsIgnoreCase("n")){
            TampilBeli();
            //delete untuk kosongkan column di sql agar transaksi selanjutnya dapat di lakukan
            // dan data yang sudah di lakukan transaksi akan di backup di table backupdt
            stat.executeUpdate("DELETE FROM belibuah WHERE belibuah.kodebuah = 'a001' OR belibuah.kodebuah = 'j002' OR belibuah.kodebuah = 'm003'");

        }

        while (!tmbh.equalsIgnoreCase("y") && !tmbh.equalsIgnoreCase("n")) {
            System.err.println("pilihan anda bukan y / n");
            System.out.print("\n"+message+" (y/n)? ");
            tmbh = input.next();

        }
        return tmbh.equalsIgnoreCase("y");
    }

    //method searching backup data
    static void cariData(){
        Scanner input = new Scanner(System.in);

        TampilDataCustomer();
        String key = "";

        System.out.println("Masukan nama customer : ");
        key = input.nextLine();
        try{
            String sql = ("SELECT * FROM backupbeli " + "WHERE namacustomer LIKE ('%"+ key +"%')");

            rs = stat.executeQuery(sql);

            System.out.println("\n|NAMA\t\t\t|KODE\t\t\t|NAMABUAH\t\t\t|HARGA\t\t\t|JMLBELI\t\t\t|BAYAR\t\t\t|DISKON\t\t\t|TOTALBAYAR");
            System.out.println("===========================================================================================");
            while (rs.next()){
                System.out.print("\n" +rs.getString("namacustomer") + "\t\t\t");
                System.out.print(rs.getString("kodebuah") + "\t\t\t");
                System.out.print(rs.getString("namabuah") + "\t\t\t");
                System.out.print(rs.getInt("hargabuah") + "\t\t\t");
                System.out.print(rs.getInt("jmlbeli") + "\t\t\t");
                System.out.print(rs.getInt("bayar") + "\t\t\t");
                System.out.print(rs.getInt("diskon")+"\t\t\t");
                System.out.print(rs.getInt("totalbayar"));

            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    //method untuk tampilkan data/nama customer yang sudah transaksi
    static void TampilDataCustomer(){
        String sql = "SELECT * FROM backupbeli";
        try{
            rs = stat.executeQuery(sql);
            System.out.println("\n|\t\tCUSTOMER\t\t|\t\tKODE\t\t|\t\tNAMA BUAH\t\t|\t\tHARGA\t\t|\t\tJMLBELI\t\t|\t\tBAYAR\t\t|\t\tDISKON\t\t|\t\tTOTAL BAYAR|");
            System.out.println("=================================================================================================================================================================================");


            while (rs.next()){
                String customer = rs.getString("namacustomer");
                String kdbuah = rs.getString("kodebuah");
                String namabuah = rs.getString("namabuah");
                int hargabuah = rs.getInt("hargabuah");
                int jmlbeli = rs.getInt("jmlbeli");
                int bayar = rs.getInt("bayar");
                int diskon = rs.getInt("diskon");
                int totalbayar = rs.getInt("totalbayar");

                System.out.println(String.format("|\t\t%s\t\t|\t\t%s\t\t|\t\t%s\t\t|\t\t%d\t\t|\t\t%d\t\t|\t\t%d\t\t|\t\t%d\t\t|\t\t%d|",customer, kdbuah, namabuah, hargabuah, jmlbeli, bayar, diskon,totalbayar));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method untuk edit data buah dan harga buah
    static void Edit() throws SQLException {
        Scanner input = new Scanner(System.in);
        tampilData();
        System.out.println("Bagian mana yang ingin di edit?");
        System.out.println("1. Nama buah");
        System.out.println("2. Harga buah");
        System.out.println("3. Exit");
        System.out.println("pilih : ");
        int pilih = input.nextInt();

        switch (pilih){
            case 1:
                updateNamaBuah();
                break;

            case 2:
                updateHargaBuah();
                break;

            case 3:
                TampilMenu();
                break;
        }

    }

    //method berisi source updateNamaBuah ketika di edit
    static void updateNamaBuah(){
        Scanner input = new Scanner(System.in);

        try{
            tampilData();

            System.out.println("UBAH / PERBARUI NAMA BUAH");
            System.out.println("INPUT KODE YANG AKAN DI PERBARUI NAMA-NYA.");

            System.out.print("\nKODE : ");
            String kode_buah = input.nextLine();
            System.out.print("\nInput NAMA BUAH baru : ");
            String nama_buah = input.nextLine();

            String.format(" ");
            stat.executeUpdate("UPDATE stokbuah SET stokbuah.namabuah = '"+nama_buah+"' WHERE stokbuah.kodebuah = '"+kode_buah+"'");
           // stat.executeUpdate( "UPDATE informasimahasiswa SET nama = UPPER (nama)");


            System.out.println("");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method berisi source updateNamaBuah ketika di edit
    static void updateHargaBuah(){
        Scanner input = new Scanner(System.in);


        try{
            tampilData();

            System.out.println("UPDATE NAMA BUAH");
            System.out.println("Input kode buah yang akan di perbarui namanya");

            System.out.print("\nKODE : ");
            String kode_buah = input.nextLine();
            System.out.print("\nInput HARGA BUAH baru : ");
            int harga_buah_baru = input.nextInt();

            String.format(" ");
            stat.executeUpdate("UPDATE stokbuah SET stokbuah.hargabuah = '"+harga_buah_baru+"' WHERE stokbuah.kodebuah = '"+kode_buah+"'");

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    //method untuk tambahkan stok buah baru
    static void TambahStokBuah(){

        Scanner input = new Scanner(System.in);

        try {
            System.out.print("\nKODE BUAH : ");
            String kode_buah = input.next();
            String nama_buah = input.nextLine();
            System.out.print("\nNAMA BUAH : ");
            nama_buah = input.nextLine();
            System.out.print("\nHARAGA : ");
            int harga_buah = input.nextInt();


            stat.executeUpdate("INSERT INTO `stokbuah` (`kodebuah`, `namabuah`, `hargabuah`) VALUES ('" + kode_buah + "', '" + nama_buah + "', '" + harga_buah + "')");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method untuk hapus buah dari stok buah
    static  void DeleteStokBuah(){

        Scanner input = new Scanner(System.in);
        tampilData();
        try {

            System.out.println("Masukan KODE BUAH yang akan di HAPUS");
            System.out.print("\nKODE BUAH : ");
            String kode_buah = input.nextLine();


            stat.executeUpdate( "DELETE FROM `stokbuah` WHERE `stokbuah`.`kodebuah` = '"+kode_buah+"' ");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //method untuk total seluruh bayar dari transaksi
    static void TotalBayar(){
        String sql = "SELECT SUM(totalbayar) as totalbayar FROM belibuah";
        try{
            rs = stat.executeQuery(sql);

            while (rs.next()){
                int totalbayar = rs.getInt("totalbayar");

                //tampilkan
                System.out.println(String.format("Jumlah yang harus di bayar : %s",totalbayar));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //method ulang digunakan untuk ulang ke menu awal
    static boolean ulang(String message){
        Scanner input = new Scanner(System.in);
        System.out.print("\n"+message+" [y/n]?");
        String pilih = input.next();

        while (!pilih.equalsIgnoreCase("y") && !pilih.equalsIgnoreCase("n")) {

            System.err.println("Pilihan anda bukan y atau n");
            System.out.printf("\n"+message+" [y/n]?");
            pilih = input.next();
        }
        return pilih.equalsIgnoreCase("y");
    }


    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,password);
            stat = conn.createStatement();

            while (!conn.isClosed()){
                TampilMenu();
            }
            stat.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
