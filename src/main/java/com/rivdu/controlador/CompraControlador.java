/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rivdu.controlador;

import com.rivdu.dao.GenericoDao;
import com.rivdu.dto.ExpedientesDTO;
import com.rivdu.dto.SaveCompraDTO;
import com.rivdu.entidades.Compra;
import com.rivdu.entidades.Personacompra;
import com.rivdu.excepcion.GeneralException;
import com.rivdu.servicio.CompraServicio;
import com.rivdu.util.BusquedaPaginada;
import com.rivdu.util.Mensaje;
import com.rivdu.util.Respuesta;
import com.rivdu.util.RivduUtil;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author javie
 */
@RestController
@RequestMapping("/compra")
public class CompraControlador {

    private final Logger loggerControlador = LoggerFactory.getLogger(getClass());
    @Autowired
    private CompraServicio compraservicio;
    
    @Autowired
    private GenericoDao<Compra,Long> compraDao;
    @Autowired
    private GenericoDao<Personacompra,Long> personacompraDao;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity crear(HttpServletRequest request, @RequestBody SaveCompraDTO entidad) throws GeneralException {
        Respuesta resp = new Respuesta();
        if (entidad != null) {
            try {
                long idCompra = compraservicio.insertar(entidad);
                if (idCompra > 0) {
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(idCompra);
                } else {
                    throw new GeneralException(Mensaje.ERROR_CRUD_GUARDAR, "Guardar retorno nulo", loggerControlador);
                }
            } catch (Exception e) {
                throw e;
            }
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @RequestMapping(value = "guardar", method = RequestMethod.POST)
    public ResponseEntity guardar(HttpServletRequest request, @RequestBody SaveCompraDTO entidad) throws GeneralException {
        Respuesta resp = new Respuesta();
        if (entidad != null) {
            try {
                Long id = compraservicio.insertar(entidad);
                if (id > 0) {
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(id);
                }
            } catch (Exception e) {
                throw e;
            }
        } else {
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.ERROR.getValor());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    };
    
    @RequestMapping(value = "actualizar", method = RequestMethod.PUT)
    public ResponseEntity actualizar(HttpServletRequest request, @RequestBody SaveCompraDTO entidad) throws GeneralException {
        Respuesta resp = new Respuesta();
        if (entidad != null) {
            try {
                Compra guardado = compraservicio.actualizar(entidad);
                if (guardado != null) {
                    compraservicio.guardarPredioServicio(entidad.getServicios(), entidad.getPredio());
                    resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                    resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                    resp.setExtraInfo(guardado);
                } else {
                    throw new GeneralException(Mensaje.ERROR_CRUD_GUARDAR, "Guardar retorno nulo", loggerControlador);
                }
            } catch (Exception e) {
                throw e;
            }
        } else {
            resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.ERROR.getValor());
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
    @RequestMapping(value = "eliminar", method = RequestMethod.POST)
    public ResponseEntity eliminar(HttpServletRequest request, @RequestBody Map<String, Object> parametros) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            Long id = RivduUtil.obtenerFiltroComoLong(parametros, "id");
            Compra compra = compraDao.obtener(Compra.class, id);
            if(compra.isEstado()){
            compra.setEstado(Boolean.FALSE);
            }
            else{
            compra.setEstado(Boolean.TRUE);
            }
            compra = compraDao.actualizar(compra);
            if (compra.getId() != null) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(compra.getId());
            } else {
                throw new GeneralException(Mensaje.ERROR_CRUD_LISTAR, "No hay datos", loggerControlador);
            }
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }//fin del metodo eliminar

    @RequestMapping(value = "quitarallegado/{id}", method = RequestMethod.GET)
    public ResponseEntity quitarallegado(HttpServletRequest request, @PathVariable("id") Long id) throws GeneralException {
        Respuesta resp = new Respuesta();
        try {
            Personacompra pc = personacompraDao.obtener(Personacompra.class, id);
            personacompraDao.eliminar(pc);
            if (pc.getId() != null) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(pc.getId());
            } else {
                throw new GeneralException(Mensaje.ERROR_CRUD_LISTAR, "No hay datos", loggerControlador);
            }
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }//fin del metodo eliminar

    
    @RequestMapping(value = "pagina/{pagina}/cantidadPorPagina/{cantidadPorPagina}", method = RequestMethod.POST)
    public ResponseEntity<BusquedaPaginada> busquedaPaginada(HttpServletRequest request, @PathVariable("pagina") Long pagina, 
                                                             @PathVariable("cantidadPorPagina") Long cantidadPorPagina, 
                                                             @RequestBody Map<String, Object> parametros) throws GeneralException{
        try {
            String clientenombre,clientedoc,correlativo;
            BusquedaPaginada busquedaPaginada = new BusquedaPaginada();
            busquedaPaginada.setBuscar(parametros);
            Compra entidadBuscar = new Compra();
            clientenombre = busquedaPaginada.obtenerFiltroComoString("nombre");
            clientedoc = busquedaPaginada.obtenerFiltroComoString("dni");
            correlativo = busquedaPaginada.obtenerFiltroComoString("correlativo");
            busquedaPaginada.setPaginaActual(pagina);
            busquedaPaginada.setCantidadPorPagina(cantidadPorPagina);
            busquedaPaginada = compraservicio.busquedaPaginada(entidadBuscar, busquedaPaginada, clientenombre,clientedoc,correlativo);
            return new ResponseEntity<>(busquedaPaginada, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }

    @RequestMapping(value = "obtener", method = RequestMethod.POST)
    public ResponseEntity Obtener(HttpServletRequest request,@RequestBody Map<String, Object> parametros) throws GeneralException {
      Respuesta resp = new Respuesta();
        try {
            Long id = RivduUtil.obtenerFiltroComoLong(parametros, "id");
            SaveCompraDTO compra = compraservicio.obtener(id);
            if (compra!=null) {
                resp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
                resp.setExtraInfo(compra);
            }else{
                throw new GeneralException(Mensaje.ERROR_CRUD_LISTAR, "No hay datos", loggerControlador);
            }
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }
    
    @RequestMapping(value = "expedienteslist/{id}", method = RequestMethod.GET)
    public ResponseEntity listarExpedientes(HttpServletRequest request, @PathVariable("id") Long id) throws GeneralException {
        Respuesta rsp = new Respuesta();
        List<ExpedientesDTO> lista;
        try {
            lista = compraservicio.listarExpedientes(id);
            if (lista != null) {
                rsp.setEstadoOperacion(Respuesta.EstadoOperacionEnum.EXITO.getValor());
                rsp.setOperacionMensaje("");
                rsp.setExtraInfo(lista);
            } else {
                throw new GeneralException("Lista no disponible", "No hay datos", loggerControlador);
            }
            return new ResponseEntity<>(rsp, HttpStatus.OK);
        } catch (Exception e) {
            loggerControlador.error(e.getMessage());
            throw e;
        }
    }
    
    
}
