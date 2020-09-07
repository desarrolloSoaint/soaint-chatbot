package com.soaint.repository;

import com.soaint.entity.CbHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CbHistorialRepository extends JpaRepository<CbHistorial,Long>, JpaSpecificationExecutor<CbHistorial> {

    Optional<CbHistorial> findById(Integer id);

    @Query(value = "select clas.email as email, count(email) as num from cb_historial art \n" +
            "join ac_clients clas on clas.id = art.id_client\n" +
            "group by email having count(email) > 0 Order by num desc", nativeQuery = true)
    ArrayList UsersFrec();

    @Query(value = "select clas.email as email, count(email) as num from cb_historial art \n" +
            "join ac_clients_private clas on clas.id = art.id_client_private\n" +
            "group by email having count(email) > 0 Order by num desc", nativeQuery = true)
    ArrayList UsersFrecPrivate();


    @Query(value = "select * from cb_historial order by id ASC", nativeQuery = true)
    List<CbHistorial> OrderASC();


    @Query(value = "select response_soniat, count(response_soniat) as num from cb_historial group by response_soniat having count(response_soniat) > 0 order by num desc", nativeQuery = true)
    ArrayList FrecResponse();

    @Query(value = "select question_client, count(question_client) as num from cb_historial group by question_client having count(question_client) > 0 order by num desc", nativeQuery = true)
    ArrayList FrecQuestion();

    @Query(value="select *from cb_historial where time_question_client >=current_date -7", nativeQuery = true)
    List<CbHistorial> LastWeek();

    @Query(value="Select COUNT (question_client) FROM  cb_historial \n" +
            "WHERE time_question_client>=current_date-7", nativeQuery = true)
    Long CountLastWeek();

    @Query(value="Select COUNT(question_client) FROM  cb_historial \n" +
            "WHERE time_question_client>=current_date-0", nativeQuery = true)
    Long CountToday();


}
