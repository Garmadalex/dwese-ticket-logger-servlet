package org.iesalixar.daw2.Alejandro.dao;


import org.iesalixar.daw2.Alejandro.entity.Provincia;
import java.sql.SQLException;
import java.util.List;


 //La interfaz ProvinciaDAO define los métodos de acceso a datos para la entidad Provincia.

public interface ProvinciaDAO {

    List<Provincia> listAllProvinces() throws SQLException;

    void insertProvincia(Provincia province) throws SQLException;

    void updateProvincia(Provincia province) throws SQLException;

    void deleteProvincia(int id) throws SQLException;

    Provincia getProvinciaById(int id) throws SQLException;

    boolean existsProvinciaByCode(String code) throws SQLException;

    boolean existsProvinciaByCodeAndNotId(String code, int id) throws SQLException;
}