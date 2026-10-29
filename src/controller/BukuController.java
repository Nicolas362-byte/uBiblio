
package controller;

import dao.BukuDAO;
import model.Buku;
import java.sql.SQLException;
import java.util.List;

public class BukuController {

    private final BukuDAO dao = new BukuDAO();

    public List<Buku> cari(String kata)
            throws SQLException {
        return dao.getAll(kata);
    }

    public boolean tambah(Buku buku)
            throws SQLException {
        return dao.tambah(buku);
    }

    public boolean edit(String isbnLama, Buku buku)
            throws SQLException {
        return dao.edit(isbnLama, buku);
    }

    public boolean hapus(String isbn)
            throws SQLException {
        return dao.hapus(isbn);
    }
}
