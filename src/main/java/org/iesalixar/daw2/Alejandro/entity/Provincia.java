package org.iesalixar.daw2.Alejandro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provincia {

    private int id;

    private String code;

    private String name;

    private int idRegion;

    private Region region;

    /**
     *  Este es un constructor personalizado que no incluye el campo `id`.
     *              * Se utiliza para crear instancias de `Provincia` cuando no es necesario o no
     *              se conoce el `id` de la provincia
     *              * (por ejemplo, antes de insertar la región en la base de datos, donde el
     *              `id` es autogenerado).
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