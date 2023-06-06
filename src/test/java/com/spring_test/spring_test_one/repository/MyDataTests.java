package com.spring_test.spring_test_one.repository;

import com.spring_test.spring_test_one.entity.Fire;
import com.spring_test.spring_test_one.entity.Fireman;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class MyDataTests {


    @Autowired
    FireRepository fireRepository;

    @Autowired
    FiremanRepository firemanRepository;

//    private static Fireman firstFireman;
//
//    @BeforeAll
//    public static void setUp(){
//
//    }

    @DisplayName("Test fire creation")
    @Test
    void testCreateFire() { //testCreate
        // given
        int severity = 8;
        Instant date = Instant.now();
        Fire fire = new Fire(severity, date);
        fireRepository.saveAndFlush(fire);

        // when
        Optional<Fire> fireFromDB = fireRepository.findById(fire.getId());

        // then
        assertTrue(fireFromDB.isPresent());
        assertEquals(fire.getId(), fireFromDB.get().getId());
        assertEquals(date, fireFromDB.get().getDate());
        assertEquals(severity, fireFromDB.get().getSeverity());
    }


    @DisplayName("Test fireman creation (with fire)")
    @Test
    void testCreateFireman() {
        // given
        String name = "Jean-Louis";
        Fireman fireman = new Fireman(name); // id null
        Fireman savedFireman = firemanRepository.save(fireman);
        List<Fire> fires = new ArrayList<>();

        System.out.println("@@@@@@@@@ fireman : " + savedFireman.toString());
        for (int i = 0; i < 10; i++) {
            Random r = new Random();
            int severity = r.nextInt(9);
            // System.out.println("@@@@@@@@@@ random : "+random);

            System.out.println("@@@@@@@@@ severity : " + severity);
            Instant date = Instant.now();

            Fire fire = new Fire(severity, date);

            // flush envoie les données instantanément à la base
            Fire savedFire = fireRepository.saveAndFlush(fire);
            fires.add(savedFire);
            System.out.println("@@@@@@@@@ fire : " + savedFire.toString());
        }
        savedFireman.setFires(fires);
        savedFireman = firemanRepository.save(savedFireman);
        firemanRepository.flush();

        // when
        Optional<Fireman> firemanFromDB = firemanRepository.findById(savedFireman.getId());

        // then
        assertTrue(firemanFromDB.isPresent());
        // System.out.println("@@@@@@@@@ fireman : " + firemanFromDB.get().toString());
        assertEquals(savedFireman.getId(), firemanFromDB.get().getId());
        assertEquals(savedFireman.getName(), firemanFromDB.get().getName());
        assertEquals(savedFireman.getFires(), firemanFromDB.get().getFires());
    }

    @Test
    void testSave_negativeSeverity() {
        // given
        String name = "Jean-Louis";
        Fireman fireman = new Fireman(name); // id null
        Fireman savedFireman = firemanRepository.save(fireman);
        System.out.println("@@@@@@@@@ fireman : " + savedFireman.toString());
        Fire fire = new Fire(-3, Instant.now());
        // when - then
        assertThrows(ConstraintViolationException.class, () -> fireRepository.saveAndFlush(fire), "");
    }

    @Test
    void testGetVeteran_nominal() {
        // given
        Fireman firstFireman = buildAndSaveFireman("Eric", 10);
        Fireman secondFireman = buildAndSaveFireman("Momo", 8);
        Fireman thirdFireman = buildAndSaveFireman("Rick", 5);
        // when
        Optional<Fireman> veteranFromDB = firemanRepository.getVeteran();
        // then
        assertTrue(veteranFromDB.isPresent());
        assertEquals(firstFireman.getId(), veteranFromDB.get().getId());
        assertEquals(firstFireman.getFires().size(), veteranFromDB.get().getFires().size());
    }

    @Test
    void testGetVeteran_firemanAlone() {
        // given
        Fireman lonelyFireman = buildAndSaveFireman("Jean-Louis", 10);
        // when
        Optional<Fireman> veteranFromDB = firemanRepository.getVeteran();
        // then
        assertTrue(veteranFromDB.isPresent());
        assertEquals(lonelyFireman.getId(), veteranFromDB.get().getId());
        assertEquals(lonelyFireman.getFires().size(), veteranFromDB.get().getFires().size());
    }

    @Test
    void testGetVeteran_emptyList() {
        // given

        // when
        Optional<Fireman> veteranFromDB = firemanRepository.getVeteran();
        // then
        assertFalse(veteranFromDB.isPresent());
    }


    private Fireman buildAndSaveFireman(String name,Integer nbFires){
        Fireman fireman = firemanRepository.save(new Fireman(name));
        List<Fire> fires = new ArrayList<>();
        for (int i = 0; i < nbFires; i++) {
            Random r = new Random();
            int severity = r.nextInt(9);
            System.out.println("@@@@@@@@@ severity : " + severity);
            Fire newFire = new Fire(severity, Instant.now());
            newFire.setFireman(fireman);
            Fire fire = fireRepository.save(newFire);
            fires.add(fire);
            System.out.println("@@@@@@@@@ fire : " + fire.toString());
        }
        fireman.setFires(fires);
        return firemanRepository.save(fireman);
    }

}