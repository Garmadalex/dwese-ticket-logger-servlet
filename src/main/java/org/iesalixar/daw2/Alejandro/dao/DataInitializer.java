package org.iesalixar.daw2.Alejandro.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import org.iesalixar.daw2.Alejandro.dao.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class); //Creamos un objeto Logger con el que iremos visualizando por consola todo lo vayasmos ejecutando de la aplicacion
    public static void loadDataFromSQL(InputStream sqlFileStream) throws SQLException, IOException {
        logger.info("Entrando en el método loadDataFromSQL");
        if (sqlFileStream == null) {
            logger.error("El archivo SQL no se ha proporcionado o es nulo");
            throw new IOException("El archivo SQL es nulo o no se ha encontrado");
        }
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            logger.info("Conexión a la base de datos establecida");
            // Leer el archivo y dividir las sentencias por ';'
            String sql;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(sqlFileStream))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                logger.error("Error al leer el archivo SQL: {}", e.getMessage(), e);
                throw new IOException("Error al leer el archivo SQL", e);
            }
            String[] statements = sql.split(";"); // Con esto lo que haremos es divir cada sentencia sql cada punto y coma = ;
            // Manejaremo transacciones
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                // Con el siguiente for le pasaremos la variable statements para manejar cada sentencia por separado
                for (String sqlStatement : statements) {
                    if (!sqlStatement.trim().isEmpty()) {
                        logger.info("Ejecutando la sentencia SQL: {}", sqlStatement);
                        statement.execute(sqlStatement.trim());
                    }
                }
                connection.commit(); // Una vez se haya ejecutado la sentencia guardamos los cambios si los hay
                logger.info("Datos cargados exitosamente desde el archivo SQL");
            } catch (SQLException e) {
                connection.rollback(); // Si algo falla revertimos la sentencia para evitar cargarnos los datos
                logger.error("Error al ejecutar el archivo SQL, se ha revertido la transacción: {}", e.getMessage(), e);
                throw new SQLException("Error al ejecutar el archivo SQL, transacción revertida", e);
            }
        } catch (SQLException e) {
            logger.error("Error durante la conexión a la base de datos o la ejecución SQL: {}", e.getMessage(), e);
            throw e;
        }
        logger.info("Saliendo del método loadDataFromSQL");
    }
}
