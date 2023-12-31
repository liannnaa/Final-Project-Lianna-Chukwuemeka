package com.company.gamestore.service;

import com.company.gamestore.exception.InvalidException;
import com.company.gamestore.exception.NotFoundException;
import com.company.gamestore.model.Game;
import com.company.gamestore.model.Tshirt;
import com.company.gamestore.repository.TshirtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
public class TshirtServiceTest {
    private TshirtService tshirtService;
    private TshirtRepository tshirtRepository;

    @BeforeEach
    public void setUp() throws Exception {
        tshirtRepository = mock(TshirtRepository.class);
        tshirtService = new TshirtService(tshirtRepository);

        Tshirt tshirt = new Tshirt();
        tshirt.setTshirtId(1);
        tshirt.setSize("Test Size");
        tshirt.setColor("Test Color");
        tshirt.setDescription("Test Description");
        tshirt.setPrice(1.00);
        tshirt.setQuantity(1);

        Tshirt tshirt2 = new Tshirt();
        tshirt2.setTshirtId(2);
        tshirt2.setSize("Test Size 2");
        tshirt2.setColor("Test Color 2");
        tshirt2.setDescription("Test Description 2");
        tshirt2.setPrice(2.00);
        tshirt2.setQuantity(2);

        List<Tshirt> consoleList = new ArrayList<>();
        consoleList.add(tshirt);
        consoleList.add(tshirt2);

        doReturn(tshirt).when(tshirtRepository).save(tshirt);
        doReturn(tshirt2).when(tshirtRepository).save(tshirt2);

        doReturn(Optional.of(tshirt)).when(tshirtRepository).findById(1);
        doReturn(Optional.of(tshirt2)).when(tshirtRepository).findById(2);

        doReturn(true).when(tshirtRepository).existsById(1);
        doReturn(true).when(tshirtRepository).existsById(2);

        doReturn(consoleList).when(tshirtRepository).findAll();

        doReturn(consoleList).when(tshirtRepository).findByColor(anyString());

        doReturn(consoleList).when(tshirtRepository).findBySize(anyString());
    }

    @Test
    public void testAddTshirt() {
        Tshirt tshirtToAdd = new Tshirt();
        tshirtToAdd.setTshirtId(1);
        tshirtToAdd.setSize("Test Size");
        tshirtToAdd.setColor("Test Color");
        tshirtToAdd.setDescription("Test Description");
        tshirtToAdd.setPrice(1.00);
        tshirtToAdd.setQuantity(1);

        Tshirt mockSavedTshirt = new Tshirt();
        mockSavedTshirt.setTshirtId(1);
        mockSavedTshirt.setSize("Test Size");
        mockSavedTshirt.setColor("Test Color");
        mockSavedTshirt.setDescription("Test Description");
        mockSavedTshirt.setPrice(1.00);
        mockSavedTshirt.setQuantity(1);

        when(tshirtRepository.save(tshirtToAdd)).thenReturn(mockSavedTshirt);

        Tshirt savedTshirt = tshirtService.addTshirt(tshirtToAdd);
        assertEquals("Test Size", savedTshirt.getSize());
        assertEquals(1, savedTshirt.getTshirtId());
    }

    @Test
    public void testGetAllTshirts() {
        List<Tshirt> tshirts = tshirtService.getAllTshirts();
        assertEquals(2, tshirts.size());
    }

    @Test
    public void testGetTshirtById() {
        Optional<Tshirt> tshirt = tshirtService.getTshirtById(1);
        assertEquals("Test Size", tshirt.get().getSize());
    }

    @Test
    public void testFindByColor() {
        List<Tshirt> colorTshirts = new ArrayList<>();
        colorTshirts.add(new Tshirt());
        doReturn(colorTshirts).when(tshirtRepository).findByColor("Test Color");

        List<Tshirt> tshirtsByColor = tshirtService.findByColor("Test Color");
        assertEquals(1, tshirtsByColor.size());
    }

    @Test
    public void testFindBySize() {
        List<Tshirt> sizeTshirts = new ArrayList<>();
        sizeTshirts.add(new Tshirt());
        doReturn(sizeTshirts).when(tshirtRepository).findBySize("Test Size");

        List<Tshirt> tshirtsBySize = tshirtService.findBySize("Test Size");
        assertEquals(1, tshirtsBySize.size());
    }

    @Test
    public void testUpdateTshirt() {
        Tshirt tshirtToUpdate = new Tshirt();
        tshirtToUpdate.setTshirtId(1);
        tshirtToUpdate.setSize("Updated Size");
        tshirtToUpdate.setColor("Test Color");
        tshirtToUpdate.setDescription("Test Description");
        tshirtToUpdate.setPrice(1.00);
        tshirtToUpdate.setQuantity(1);

        when(tshirtRepository.save(tshirtToUpdate)).thenReturn(tshirtToUpdate);

        Tshirt updatedTshirt = tshirtService.updateTshirt(tshirtToUpdate);
        assertEquals("Updated Size", updatedTshirt.getSize());
    }

    @Test
    public void testDeleteTshirt() {
        doNothing().when(tshirtRepository).deleteById(1);

        tshirtService.deleteTshirt(1);
        verify(tshirtRepository).deleteById(1);
    }

    @Test
    public void testAddTshirtMissingFields() {
        Tshirt incompleteTshirt = new Tshirt();
        incompleteTshirt.setSize("Incomplete Tshirt");

        Exception exception = assertThrows(InvalidException.class, () -> {
            tshirtService.addTshirt(incompleteTshirt);
        });

        assertEquals("All tshirt fields must be filled.", exception.getMessage());
    }

    @Test
    public void testGetTshirtByInvalidId() {
        when(tshirtRepository.existsById(99)).thenReturn(false);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            tshirtService.getTshirtById(99);
        });

        assertEquals("Tshirt not found with id: 99", exception.getMessage());
    }

    @Test
    public void testUpdateNonExistingTshirt() {
        Tshirt tshirt = new Tshirt();
        tshirt.setTshirtId(99);
        tshirt.setSize("Non Existing Tshirt");
        tshirt.setColor("Test Color");
        tshirt.setDescription("Test Description");
        tshirt.setPrice(1.00);
        tshirt.setQuantity(1);

        when(tshirtRepository.existsById(99)).thenReturn(false);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            tshirtService.updateTshirt(tshirt);
        });

        assertEquals("Tshirt not found with id: 99", exception.getMessage());
    }

    @Test
    public void testDeleteNonExistingTshirt() {
        when(tshirtRepository.existsById(99)).thenReturn(false);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            tshirtService.deleteTshirt(99);
        });

        assertEquals("Tshirt not found with id: 99", exception.getMessage());
    }

    @Test
    public void testAddTshirtWithNegativePrice() {
        Tshirt tshirtWithNegativePrice = new Tshirt();
        tshirtWithNegativePrice.setSize("Test Size");
        tshirtWithNegativePrice.setColor("Test Color");
        tshirtWithNegativePrice.setDescription("Test Description");
        tshirtWithNegativePrice.setPrice(-1.00);
        tshirtWithNegativePrice.setQuantity(1);

        InvalidException thrown = assertThrows(
                InvalidException.class,
                () -> tshirtService.addTshirt(tshirtWithNegativePrice),
                "Expected addTshirt to throw an exception, but it didn't"
        );

        assertEquals("Tshirt price must be greater than 0.", thrown.getMessage());
    }

    @Test
    public void testUpdateTshirtWithNegativePrice() {
        Tshirt tshirtToUpdateWithNegativePrice = new Tshirt();
        tshirtToUpdateWithNegativePrice.setTshirtId(1);
        tshirtToUpdateWithNegativePrice.setSize("Test Size");
        tshirtToUpdateWithNegativePrice.setColor("Test Color");
        tshirtToUpdateWithNegativePrice.setDescription("Test Description");
        tshirtToUpdateWithNegativePrice.setPrice(-1.00);
        tshirtToUpdateWithNegativePrice.setQuantity(1);

        when(tshirtRepository.existsById(tshirtToUpdateWithNegativePrice.getTshirtId())).thenReturn(true);

        InvalidException thrown = assertThrows(
                InvalidException.class,
                () -> tshirtService.updateTshirt(tshirtToUpdateWithNegativePrice),
                "Expected updateTshirt to throw an exception, but it didn't"
        );

        assertEquals("Tshirt price must be greater than 0.", thrown.getMessage());
    }
}
