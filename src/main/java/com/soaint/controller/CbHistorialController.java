package com.soaint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.DTO.CbHistorialDto;
import com.soaint.entity.CbHistorial;
import com.soaint.repository.CbHistorialRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients/historial")
@Api(tags = "CLIENTS")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class CbHistorialController {
    @Autowired
    private CbHistorialRepository cbHistorialRepository;



    //Get para Search(Busqueda) time_question_client y time_response_soniat estilo Json
    @GetMapping("/find-all-search")
    @ApiOperation(value = "Busqueda Registo Historial Ascendente y Descendente por Time del Question_Client y Response_Soniat", notes = "Busqueda Time Question y Response del Historial")
    public List<CbHistorial> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "orderBy", required = false, defaultValue = "ASC") String orderBy,
            @RequestParam(value = "columnNames", required = false) List<String> columnNames
    ) {


        // En el front columnNames deben pasar datos asi si se va a ordenar más de una
        // columna. ejemplo: columnName=id,firstName,lastName. En el front sería ["id","firstName","lastName"]
        // si es un solo elemento sería ["id"]

        Sort order;
        List<Sort.Order> sorts = new ArrayList<>();
        switch (orderBy.toUpperCase()) {

            case "DESC":
                for (String s : columnNames
                ) {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, s));
                }
                order = new Sort(sorts);
                break;
            default:
                for (String s : columnNames
                ) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, s));
                }
                order = new Sort(sorts);
                break;
        }


        ObjectMapper mapper = new ObjectMapper();

        List<CbHistorial> list = cbHistorialRepository.findAll((Specification<CbHistorial>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(search)) {
                try {
                    CbHistorialDto dao = mapper.readValue(search, CbHistorialDto.class);

                    if (!StringUtils.isEmpty(dao.getId_client())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id_client"), dao.getId_client())));
                    }

                    if (!StringUtils.isEmpty(dao.getId_client_private())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id_client_private"), dao.getId_client_private())));
                    }

                    if (!StringUtils.isEmpty(dao.getDay_time_question_client())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                criteriaBuilder.function("DAY", Integer.class, root.get("time_question_client")), dao.getDay_time_question_client()
                        )));
                    }

                    if (!StringUtils.isEmpty(dao.getMonth_time_question_client())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                criteriaBuilder.function("MONTH", Integer.class, root.get("time_question_client")), dao.getMonth_time_question_client()
                        )));
                    }

                    if (!StringUtils.isEmpty(dao.getYear_time_question_client())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                criteriaBuilder.function("YEAR", Integer.class, root.get("time_question_client")), dao.getYear_time_question_client()
                        )));
                    }

                    ///
                    if (!StringUtils.isEmpty(dao.getDay_time_response_soniat())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                criteriaBuilder.function("DAY", Integer.class, root.get("time_response_soniat")), dao.getDay_time_response_soniat()
                        )));
                    }

                    if (!StringUtils.isEmpty(dao.getMonth_time_response_soniat())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                criteriaBuilder.function("MONTH", Integer.class, root.get("time_response_soniat")), dao.getMonth_time_response_soniat()
                        )));
                    }

                    if (!StringUtils.isEmpty(dao.getYear_time_response_soniat())) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.equal(
                                criteriaBuilder.function("YEAR", Integer.class, root.get("time_response_soniat")), dao.getYear_time_response_soniat()
                        )));
                    }


                } catch (IOException io) {
                    System.out.println(io.getMessage());
                    try {
                        throw new Exception("Error");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, order);

        return list;

    }
}
