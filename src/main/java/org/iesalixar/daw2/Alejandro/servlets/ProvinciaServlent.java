package org.iesalixar.daw2.Alejandro.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.Alejandro.dao.*;
import org.iesalixar.daw2.Alejandro.entity.Provincia;
import org.iesalixar.daw2.Alejandro.entity.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet que gestiona las operaciones CRUD para la entidad Province.
 */
@WebServlet("/provinces")
public class ProvinciaServlent extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProvinciaServlent.class);
    private ProvinciaDAO provinceDAO;
    private RegionDAO regionDAO;

    @Override
    public void init() throws ServletException {
        logger.info("Inicializando ProvinceServlet y DAOs...");
        try {
            provinceDAO = new ProvinciaDAOImpl();
            regionDAO = new RegionDAOImpl();
        } catch (Exception e) {
            logger.error("Error al inicializar ProvinceDAO o RegionDAO: {}", e.getMessage(), e);
            throw new ServletException("Error al inicializar DAOs", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entrando en doGet de ProvinceServlet");
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            if (action == null) {
                action = "list";
            }
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listProvinces(request, response);
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error SQL en doGet: {}", ex.getMessage(), ex);
            throw new ServletException(ex);
        }
        logger.info("Saliendo de doGet de ProvinceServlet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entrando en doPost de ProvinceServlet");
        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            if (action == null) {
                action = "list";
            }
            switch (action) {
                case "insert":
                    insertProvince(request, response);
                    break;
                case "update":
                    updateProvince(request, response);
                    break;
                case "delete":
                    deleteProvince(request, response);
                    break;
                default:
                    listProvinces(request, response);
                    break;
            }
        } catch (SQLException ex) {
            logger.error("Error SQL en doPost: {}", ex.getMessage(), ex);
            throw new ServletException(ex);
        }
        logger.info("Saliendo de doPost de ProvinceServlet");
    }

    private void listProvinces(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        logger.info("Listando todas las provincias");
        List<Provincia> listProvinces = provinceDAO.listAllProvinces();
        request.setAttribute("listProvinces", listProvinces);
        request.getRequestDispatcher("province.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        logger.info("Mostrando formulario para nueva provincia");
        List<Region> listRegions = regionDAO.listAllRegions();
        request.setAttribute("listRegions", listRegions);
        request.getRequestDispatcher("province-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        logger.info("Mostrando formulario de edición para provincia");
        int id = Integer.parseInt(request.getParameter("id"));
        Provincia existingProvince = provinceDAO.getProvinciaById(id);
        List<Region> listRegions = regionDAO.listAllRegions();
        request.setAttribute("province", existingProvince);
        request.setAttribute("listRegions", listRegions);
        request.getRequestDispatcher("province-form.jsp").forward(request, response);
    }

    private void insertProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        logger.info("Insertando nueva provincia");
        String code = request.getParameter("code") != null ? request.getParameter("code").trim().toUpperCase() : "";
        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String idRegionStr = request.getParameter("id_region");

        if (code.isEmpty() || name.isEmpty() || idRegionStr == null || idRegionStr.isEmpty()) {
            request.setAttribute("errorMessage", "Todos los campos son obligatorios.");
            request.setAttribute("listRegions", regionDAO.listAllRegions());
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        int idRegion = Integer.parseInt(idRegionStr);

        if (provinceDAO.existsProvinciaByCode(code)) {
            request.setAttribute("errorMessage", "El código de la provincia ya existe.");
            request.setAttribute("listRegions", regionDAO.listAllRegions());
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        Provincia newProvince = new Provincia(code, name, idRegion);
        try {
            provinceDAO.insertProvincia(newProvince);
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                request.setAttribute("errorMessage", "El código de la provincia debe ser único.");
                request.setAttribute("listRegions", regionDAO.listAllRegions());
                request.getRequestDispatcher("province-form.jsp").forward(request, response);
                return;
            }
            throw e;
        }
        response.sendRedirect("provinces");
    }

    private void updateProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        logger.info("Actualizando provincia");
        int id = Integer.parseInt(request.getParameter("id"));
        String code = request.getParameter("code") != null ? request.getParameter("code").trim().toUpperCase() : "";
        String name = request.getParameter("name") != null ? request.getParameter("name").trim() : "";
        String idRegionStr = request.getParameter("id_region");

        if (code.isEmpty() || name.isEmpty() || idRegionStr == null || idRegionStr.isEmpty()) {
            request.setAttribute("errorMessage", "Todos los campos son obligatorios.");
            request.setAttribute("province", provinceDAO.getProvinciaById(id));
            request.setAttribute("listRegions", regionDAO.listAllRegions());
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        int idRegion = Integer.parseInt(idRegionStr);

        if (provinceDAO.existsProvinciaByCodeAndNotId(code, id)) {
            request.setAttribute("errorMessage", "El código de la provincia ya existe para otra provincia.");
            request.setAttribute("province", provinceDAO.getProvinciaById(id));
            request.setAttribute("listRegions", regionDAO.listAllRegions());
            request.getRequestDispatcher("province-form.jsp").forward(request, response);
            return;
        }

        Provincia updatedProvince = new Provincia(id, code, name, idRegion, null);
        try {
            provinceDAO.updateProvincia(updatedProvince);
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                request.setAttribute("errorMessage", "El código de la provincia debe ser único.");
                request.setAttribute("province", provinceDAO.getProvinciaById(id));
                request.setAttribute("listRegions", regionDAO.listAllRegions());
                request.getRequestDispatcher("province-form.jsp").forward(request, response);
                return;
            }
            throw e;
        }
        response.sendRedirect("provinces");
    }

    private void deleteProvince(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        logger.info("Eliminando provincia con ID: {}", request.getParameter("id"));
        int id = Integer.parseInt(request.getParameter("id"));
        provinceDAO.deleteProvincia(id);
        response.sendRedirect("provinces");
    }
}