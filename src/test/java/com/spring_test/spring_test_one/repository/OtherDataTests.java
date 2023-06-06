package com.spring_test.spring_test_one.repository;

import com.spring_test.spring_test_one.entity.Fire;
import com.spring_test.spring_test_one.entity.Fireman;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@DataJpaTest
public class OtherDataTests {

    @Autowired
    FireRepository fireRepository;

    @Autowired
    FiremanRepository firemanRepository;

    @BeforeEach
    public void setup() {
        fireRepository.deleteAll();
        firemanRepository.deleteAll();
    }

    @Test
    public void testGetVeteranWithMultipleFiremen() {
        Fireman fireman1 = createFiremanWithFires("John Doe", 5);
        Fireman fireman2 = createFiremanWithFires("Jane Smith", 3);
        Fireman fireman3 = createFiremanWithFires("Mike Johnson", 8);
        firemanRepository.saveAll(Arrays.asList(fireman1, fireman2, fireman3));

        Optional<Fireman> veteran = firemanRepository.getVeteran();

        Assertions.assertTrue(veteran.isPresent());
        Assertions.assertEquals("Mike Johnson", veteran.get().getName());
    }

    @Test
    public void testGetVeteranWithSingleFireman() {
        // Create a fireman with fires
        Fireman fireman = createFiremanWithFires("John Doe", 10);

        // Save fireman in bdd
        firemanRepository.save(fireman);

        // get veteran
        Optional<Fireman> veteran = firemanRepository.getVeteran();

        // check resultat
        Assertions.assertTrue(veteran.isPresent());
        Assertions.assertEquals("John Doe", veteran.get().getName());
    }

    @Test
    public void testGetVeteranWithNoFireman() {
        // Retrouver le veteran
        Optional<Fireman> veteran = firemanRepository.getVeteran();

        // Verify the result
        Assertions.assertTrue(veteran.isEmpty());
    }

    private Fireman createFiremanWithFires(String name, int numFires) {
        Fireman fireman = new Fireman();
        fireman.setName(name);

        for (int i = 0; i < numFires; i++) {
            Fire fire = new Fire(8, Instant.now());
            fireman.getFires().add(fire);
            fireRepository.save(fire);
        }

        return fireman;
    }
}
