package org.iesalixar.daw2.Alejandro.dao;


import org.iesalixar.daw2.Alejandro.entity.Provincia;
import org.iesalixar.daw2.Alejandro.entity.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


 // Implementación de la interfaz ProvinciaDAO para gestionar las operaciones SQL de provincias.

public class ProvinciaDAOImpl implements ProvinciaDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProvinciaDAOImpl.class); //Al igual que con el resto de clases donde nos interese saber el estado actual de nuestra aplicacion recogemos info con Logger
// Alejandro Madrigal
    @Override
    public List<Provincia> listAllProvinces() throws SQLException {
        logger.info("Entrando en el método listAllProvinces");
        //Aqui creamos una lista de las provincias pasandole como objeto la Clase Provincia, creamos la query donde relacionamos cada caolumna con su valor
        List<Provincia> provinces = new ArrayList<>();
        String query = "SELECT p.id, p.code, p.name, p.id_region, r.code AS r_code, r.name AS r_name FROM provinces p JOIN regions r ON p.id_region = r.id";
        //Iniciamos el proceso de conexión con la base de datos
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Provincia province = new Provincia();
                province.setId(resultSet.getInt("id"));
                province.setCode(resultSet.getString("code"));
                province.setName(resultSet.getString("name"));
                province.setIdRegion(resultSet.getInt("id_region"));

                Region region = new Region();
                region.setId(resultSet.getInt("id_region"));
                region.setCode(resultSet.getString("r_code"));
                region.setName(resultSet.getString("r_name"));
                province.setRegion(region);

                provinces.add(province);
            }
        } catch (SQLException e) { //Se ocurre cualquier clase de error lo capturamos en este catch y sino retornamos provinces (La lista de provincias)
            logger.error("Error al listar las provincias: {}", e.getMessage(), e);
            throw e;
        }

        logger.info("Saliendo de listAllProvinces. Provincias encontradas: {}", provinces.size());
        return provinces;
    }

    @Override
    public void insertProvincia(Provincia province) throws SQLException {
        logger.info("Entrando en insertProvince con código: {}", province.getCode());
        String query = "INSERT INTO provinces (code, name, id_region) VALUES (?, ?, ?)"; //Creamos la query con los datos ha ingresar (que seran para ingresar una nueva provincia en este caso)
        //Y al igual que ante creamos un objeto Connection donde le pasaremos los datos a actualizar
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, province.getCode());
            ps.setString(2, province.getName());
            ps.setInt(3, province.getIdRegion());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al insertar la provincia: {}", e.getMessage(), e); //Si hay algún error saltará el catch y sino saltara el logger indicando que se ha insertado con exito
            throw e;
        }
        logger.info("Provincia insertada exitosamente.");
    }

    @Override
    public void updateProvincia(Provincia province) throws SQLException {
        logger.info("Entrando en updateProvince con ID: {}", province.getId());
        String query = "UPDATE provinces SET code = ?, name = ?, id_region = ? WHERE id = ?"; //Query con los datos para actualizar una provincia

        try (Connection connection = DatabaseConnectionManager.getConnection(); //Al igual que antes creamos el objeto Connection pero aádiendo province.getId ya que es necesario para que no exista duplicidad de ids al actualizar una provincia
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, province.getCode());
            ps.setString(2, province.getName());
            ps.setInt(3, province.getIdRegion());
            ps.setInt(4, province.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al actualizar la provincia con ID {}: {}", province.getId(), e.getMessage(), e);
            throw e;
        }
        logger.info("Provincia actualizada exitosamente.");
    }

    @Override
    public void deleteProvincia(int id) throws SQLException {
        logger.info("Entrando en deleteProvince con ID: {}", id);
        String query = "DELETE FROM provinces WHERE id = ?"; // Query en este caso para borrar una provincia

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) { //En este caso solo necesitamos saber el id de la provincia para poder eliminarla
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al eliminar la provincia con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
        logger.info("Provincia eliminada exitosamente.");
    }

    @Override
    public Provincia getProvinciaById(int id) throws SQLException {
        logger.info("Entrando en getProvinceById con ID: {}", id);
        //Creamos la Query en este caso para obtener una provincia en cuestion
        String query = "SELECT p.id, p.code, p.name, p.id_region, r.code AS r_code, r.name AS r_name FROM provinces p JOIN regions r ON p.id_region = r.id WHERE p.id = ?";
        Provincia province = null;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    province = new Provincia();
                    province.setId(rs.getInt("id"));
                    province.setCode(rs.getString("code"));
                    province.setName(rs.getString("name"));
                    province.setIdRegion(rs.getInt("id_region"));

                    Region region = new Region();
                    region.setId(rs.getInt("id_region"));
                    region.setCode(rs.getString("r_code"));
                    region.setName(rs.getString("r_name"));
                    province.setRegion(region);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener provincia por ID {}: {}", id, e.getMessage(), e);
            throw e;
        }

        logger.info("Saliendo de getProvinceById.");
        return province;
    }

    @Override
    public boolean existsProvinciaByCode(String code) throws SQLException {
        logger.info("Verificando existencia de provincia por código: {}", code);
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ?"; //Verificamos si existe una provincia en cuestion unicamente por su paremetro "codigo"
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean existsProvinciaByCodeAndNotId(String code, int id) throws SQLException {
        logger.info("Verificando existencia de provincia por código {} excluyendo ID {}", code, id);
        String sql = "SELECT COUNT(*) FROM provinces WHERE UPPER(code) = ? AND id != ?"; // Similar al anterior pero en este caso verificamos si existe por su código ignorando si parametro "id"
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code.toUpperCase());
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }
}
