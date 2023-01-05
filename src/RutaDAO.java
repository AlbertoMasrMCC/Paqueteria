import java.sql.*;
import java.util.ArrayList;

public class RutaDAO {

    Connection conexion = null;

    public RutaDAO() {

        // Cargar el driver de PostgreSQL
        try {

            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {

            e.printStackTrace();

        }

    }

    public void setConexion() {

        // Establecer la conexión con la base de datos
        String url = "jdbc:postgresql://localhost:5432/TecnicasProgramacion";
        String usuario = "postgres";
        String contraseña = "root";

        try {

            if (conexion == null) {

                conexion = DriverManager.getConnection(url, usuario, contraseña);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public String[] obtenerEsquinaCercana(String latitud, String longitud) {

        String[] esquina = new String[3];

        setConexion();

        try {

            // Recuperar la esquina más cercana a la posición inicial
            String sql = "SELECT * FROM obtener_esquina_cercana(?, ?)";
            PreparedStatement st = conexion.prepareStatement(sql);
            st.setString(1, latitud);
            st.setString(2, longitud);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                esquina[0] = rs.getString("out_calle");
                esquina[1] = rs.getString("out_latitud");
                esquina[2] = rs.getString("out_longitud");

            }

        }
        catch (SQLException e) {

            e.printStackTrace();

        }

        return esquina;

    }

    public ArrayList<String[]> obtenerVecinos(String latitud, String longitud) {

        ArrayList<String[]> vecinos = new ArrayList<String[]>();

        setConexion();

        try {

            // Recuperar la esquina más cercana a la posición inicial
            String sql = "SELECT * FROM obtener_vecinos(?, ?)";
            PreparedStatement st = conexion.prepareStatement(sql);
            st.setString(1, latitud);
            st.setString(2, longitud);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                String[] informacionVecino = new String[3];

                informacionVecino[0] = rs.getString("out_calle");
                informacionVecino[1] = rs.getString("out_latitud");
                informacionVecino[2] = rs.getString("out_longitud");

                vecinos.add(informacionVecino);

            }

        }
        catch (SQLException e) {

            e.printStackTrace();

        }

        return vecinos;

    }

}
