
package dao;

import config.Database;
import model.Buku;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {

    public List<Buku> getAll(String kata)
            throws SQLException {
        List<Buku> daftar = new ArrayList<>();

        String sql = "SELECT * FROM buku "
                + "WHERE isbn LIKE ? OR judul LIKE ? "
                + "OR penulis LIKE ? OR genre LIKE ? "
                + "ORDER BY judul";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            String cari = "%" + kata + "%";

            for (int i = 1; i <= 4; i++) {
                p.setString(i, cari);
            }

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    daftar.add(new Buku(
                        r.getString("isbn"),
                        r.getString("judul"),
                        r.getString("penulis"),
                        r.getString("genre")
                    ));
                }
            }
        }

        return daftar;
    }

    public boolean tambah(Buku b)
            throws SQLException {
        String sql = "INSERT INTO buku VALUES (?, ?, ?, ?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, b.getIsbn());
            p.setString(2, b.getJudul());
            p.setString(3, b.getPenulis());
            p.setString(4, b.getGenre());

            return p.executeUpdate() > 0;
        }
    }

    public boolean edit(String isbnLama, Buku b)
            throws SQLException {
        String sql = "UPDATE buku SET isbn=?, judul=?, "
                + "penulis=?, genre=? WHERE isbn=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, b.getIsbn());
            p.setString(2, b.getJudul());
            p.setString(3, b.getPenulis());
            p.setString(4, b.getGenre());
            p.setString(5, isbnLama);

            return p.executeUpdate() > 0;
        }
    }

    public boolean hapus(String isbn)
            throws SQLException {
        String sql = "DELETE FROM buku WHERE isbn=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, isbn);
            return p.executeUpdate() > 0;
        }
    }
}

