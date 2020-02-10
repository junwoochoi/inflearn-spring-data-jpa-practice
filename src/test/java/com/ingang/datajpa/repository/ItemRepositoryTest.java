package com.ingang.datajpa.repository;

import com.ingang.datajpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void save() {
        final Item a = new Item("A");
        final Item save = itemRepository.save(a);

        assertThat(save.getId()).isNotNull();
    }
}