package org.iesalixar.daw2.Alejandro.dao;


import org.iesalixar.daw2.Alejandro.entity.Provincia;
import java.sql.SQLException;
import java.util.List;


 //La interfaz ProvinciaDAO define los métodos de acceso a datos para la entidad Provincia.

public interface ProvinciaDAO {

    List<Provincia> listAllProvinces() throws SQLException;

    void insertProvince(Provincia province) throws SQLException;

    void updateProvince(Provincia province) throws SQLException;

    void deleteProvince(int id) throws SQLException;

    Provincia getProvinceById(int id) throws SQLException;

    boolean existsProvinceByCode(String code) throws SQLException;

    boolean existsProvinceByCodeAndNotId(String code, int id) throws SQLException;
}