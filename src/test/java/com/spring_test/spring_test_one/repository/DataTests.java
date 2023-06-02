package com.spring_test.spring_test_one.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.spring_test.spring_test_one.entity.Fire;
import com.spring_test.spring_test_one.entity.Fireman;

// dit à Spring Data que c'est un test de data et qu'il faut initialiser la fausse base de données
@DataJpaTest
public class DataTests {

    // on peut ensuite injecter un repository et l'utiliser normalement
    @Autowired
    FireRepository fireRepository;

    @Autowired
    FiremanRepository firemanRepository;

    @Test
    public void testCreateFire() {
        int severity = 8;
        Instant date = Instant.now();
        Fire fire = new Fire(severity, date);

        // flush envoie les données instantanément à la base
        fireRepository.saveAndFlush(fire);

        // ❔pourquoi optional ❔
        Optional<Fire> fireFromDB = fireRepository.findById(fire.getId());

        assertTrue(fireFromDB.isPresent());
        assertEquals(fire.getId(), fireFromDB.get().getId());
        assertEquals(date, fireFromDB.get().getDate());
        assertEquals(severity, fireFromDB.get().getSeverity());
    }

    @Test
    public void testCreateFireman() {
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

        Optional<Fireman> firemanFromDB = firemanRepository.findById(savedFireman.getId());

        assertTrue(firemanFromDB.isPresent());
        // System.out.println("@@@@@@@@@ fireman : " + firemanFromDB.get().toString());
        assertEquals(savedFireman.getId(), firemanFromDB.get().getId());
        assertEquals(savedFireman.getName(), firemanFromDB.get().getName());
        assertEquals(savedFireman.getFires(), firemanFromDB.get().getFires());
    }

    @Test
    public void testNegativeSeverity() {
        String name = "Jean-Louis";
        Fireman fireman = new Fireman(name); // id null
        Fireman savedFireman = firemanRepository.save(fireman);
        System.out.println("@@@@@@@@@ fireman : " + savedFireman.toString());
        int severity = -3;
        Instant date = Instant.now();
        List<Fire> fires = new ArrayList<>();

        assertThrows(ConstraintViolationException.class, () -> {
            Fire fire = new Fire(severity, date);
            Fire savedFire = fireRepository.saveAndFlush(fire);
            fires.add(savedFire);
        }, "");
    }

    @Test
    public void emptyFireman() {
        Optional<Fireman> veteran = firemanRepository.getVeteran();

        assertTrue(veteran.isEmpty());
    }

    public void firemanVeteran() {
        String[] names = { "Eric", "Momo", "Rick" };
        Fireman veteran = null;
        int fireCount = names.length - 1;
        int maxCount = fireCount;

        for (String name : names) {
            Fireman fireman = new Fireman(name); // id null
            Fireman savedFireman = firemanRepository.save(fireman);
            List<Fire> fires = new ArrayList<>();

            // pour la comparaison avec le veteranFromDB
            if (fireCount == maxCount) {
                System.out.println("@@@@@@@@@ firecount = " + fireCount + ", maxCount = " + maxCount);
                veteran = savedFireman;
                System.out.println("@@@@@@@@@ veteran : " + savedFireman.toString());
            }

            // attribution des feux
            for (int i = 0; i < fireCount + 1; i++) {
                Random r = new Random();
                int severity = r.nextInt(9);
                System.out.println("@@@@@ severity : " + severity);
                Instant date = Instant.now();
                Fire fire = new Fire(severity, date);
                // flush envoie les données instantanément à la base
                Fire savedFire = fireRepository.saveAndFlush(fire);
                fires.add(savedFire);
                System.out.println("@@@@@ fire : " + savedFire.toString());
            }

            savedFireman.setFires(fires);
            savedFireman = firemanRepository.save(savedFireman);
            firemanRepository.flush();
            System.out.println("@@@@@@@@@@ fireman : " + savedFireman.toString());
            fireCount--;
        }
        // System.out.println("@@@@@@@@@@@@@@@@@@@@@ veteran = "+veteran.toString());
        Optional<Fireman> veteranFromDB = firemanRepository.getVeteran();
        System.out.println("@@@@@@@@@@@@@@ veteran : " + veteranFromDB.toString());
        assertTrue(veteranFromDB.isPresent());
        assertEquals(veteran.getId(), veteranFromDB.get().getId());
        assertEquals(veteran.getFires().size(), veteranFromDB.get().getFires().size());
    }

    @Test
    public void firemanAlone() {
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
        // savedFireman.setFires
        firemanRepository.flush();

        Optional<Fireman> veteranFromDB = firemanRepository.getVeteran();
        assertTrue(veteranFromDB.isPresent());
        assertEquals(savedFireman.getId(), veteranFromDB.get().getId());
        assertEquals(savedFireman.getFires().size(), veteranFromDB.get().getFires().size());

    }

}