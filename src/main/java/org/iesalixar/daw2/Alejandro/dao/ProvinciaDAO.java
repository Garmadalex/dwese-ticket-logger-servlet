package org.iesalixar.daw2.Alejandro.dao;


import org.iesalixar.daw2.Alejandro.entity.Provincia;
import java.sql.SQLException;
import java.util.List;


 //La interfaz ProvinciaDAO define los métodos de acceso a datos para la entidad Provincia.

public interface ProvinciaDAO {

    List<Provincia> listAllProvinces() throws SQLException; //Metodo para mostrar todas las provincias de la base de datos

    void insertProvincia(Provincia province) throws SQLException; //Metodo para agregar una nueva provincia

    void updateProvincia(Provincia province) throws SQLException; //Metodo para editar/actualizar una nueva provincia

    void deleteProvincia(int id) throws SQLException; //Metodo para borrar/eliminar una provincia

    Provincia getProvinciaById(int id) throws SQLException; //Metodo para obtener una provincia

    boolean existsProvinciaByCode(String code) throws SQLException; //Metodo para comprobar si existe una provincia por su codigo

    boolean existsProvinciaByCodeAndNotId(String code, int id) throws SQLException; //Metodo para comprobar si existe una provincia únicamente por su código
}