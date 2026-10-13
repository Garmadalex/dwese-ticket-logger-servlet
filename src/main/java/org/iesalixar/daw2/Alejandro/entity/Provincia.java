package org.iesalixar.daw2.Alejandro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase Province que representa una entidad para modelar una provincia
 * asociada a una Comunidad Autónoma (Region) en la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provincia {

    /** Identificador único de la provincia. */
    private int id;

    /** Código único de la provincia. */
    private String code;

    /** Nombre de la provincia. */
    private String name;

    /** Identificador de la región (Comunidad Autónoma) a la que pertenece. */
    private int idRegion;

    /** Objeto Region asociado para mostrar su información detallada. */
    private Region region;

    /**
     * Constructor personalizado sin ID, útil para inserciones previas a la generación de la clave primaria.
     *
     * @param code     Código de la provincia.
     * @param name     Nombre de la provincia.
     * @param idRegion ID de la región asociada.
     */
    public Provincia(String code, String name, int idRegion) {
        this.code = code;
        this.name = name;
        this.idRegion = idRegion;
    }
}