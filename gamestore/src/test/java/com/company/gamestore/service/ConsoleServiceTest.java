package com.company.gamestore.service;

import com.company.gamestore.model.Console;
import com.company.gamestore.repository.ConsoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ConsoleServiceTest {
    private ConsoleService consoleService;
    private ConsoleRepository consoleRepository;

    @BeforeEach
    public void setUp() throws Exception {
        consoleRepository = mock(ConsoleRepository.class);
        consoleService = new ConsoleService(consoleRepository);

        Console console = new Console();
        console.setConsoleId(1);
        console.setModel("Test Model");
        console.setManufacturer("Test Manufacturer");
        console.setMemoryAmount("Test Memory");
        console.setProcessor("Test Processor");
        console.setPrice(1.00);
        console.setQuantity(1);

        Console console2 = new Console();
        console2.setConsoleId(2);
        console2.setModel("Test Model 2");
        console2.setManufacturer("Test Manufacturer 2");
        console2.setMemoryAmount("Test Memory 2");
        console2.setProcessor("Test Processor 2");
        console2.setPrice(2.00);
        console2.setQuantity(2);

        List<Console> consoleList = new ArrayList<>();
        consoleList.add(console);
        consoleList.add(console2);

        doReturn(console).when(consoleRepository).save(console);
        doReturn(console2).when(consoleRepository).save(console2);

        doReturn(Optional.of(console)).when(consoleRepository).findById(1);
        doReturn(Optional.of(console2)).when(consoleRepository).findById(2);

        doReturn(true).when(consoleRepository).existsById(1);
        doReturn(true).when(consoleRepository).existsById(2);

        doReturn(consoleList).when(consoleRepository).findAll();

        doReturn(consoleList).when(consoleRepository).findByManufacturer(anyString());
    }

    @Test
    public void testAddConsole() {
        Console consoleToAdd = new Console();
        consoleToAdd.setModel("Test Model");
        consoleToAdd.setManufacturer("Test Manufacturer");
        consoleToAdd.setMemoryAmount("Test Memory");
        consoleToAdd.setProcessor("Test Processor");
        consoleToAdd.setPrice(1.00);
        consoleToAdd.setQuantity(1);

        Console mockSavedConsole = new Console();
        mockSavedConsole.setConsoleId(1);
        mockSavedConsole.setModel("Test Model");
        mockSavedConsole.setManufacturer("Test Manufacturer");
        mockSavedConsole.setMemoryAmount("Test Memory");
        mockSavedConsole.setProcessor("Test Processor");
        mockSavedConsole.setPrice(1.00);
        mockSavedConsole.setQuantity(1);

        when(consoleRepository.save(consoleToAdd)).thenReturn(mockSavedConsole);

        Console savedConsole = consoleService.addConsole(consoleToAdd);
        assertEquals("Test Model", savedConsole.getModel());
        assertEquals(1, savedConsole.getConsoleId());
    }

    @Test
    public void testGetAllConsoles() {
        List<Console> consoles = consoleService.getAllConsoles();
        assertEquals(2, consoles.size());
    }

    @Test
    public void testGetConsoleById() {
        Optional<Console> console = consoleService.getConsoleById(1);
        assertEquals("Test Model", console.get().getModel());
    }

    @Test
    public void testFindByManufacturer() {
        List<Console> manufacturerConsoles = new ArrayList<>();
        manufacturerConsoles.add(new Console());
        doReturn(manufacturerConsoles).when(consoleRepository).findByManufacturer("Test Manufacturer");

        List<Console> consolesByManufacturer = consoleService.findByManufacturer("Test Manufacturer");
        assertEquals(1, consolesByManufacturer.size());
    }

    @Test
    public void testUpdateConsole() {
        Console consoleToUpdate = new Console();
        consoleToUpdate.setConsoleId(1);
        consoleToUpdate.setModel("Updated Model");
        consoleToUpdate.setManufacturer("Test Manufacturer");
        consoleToUpdate.setMemoryAmount("Test Memory");
        consoleToUpdate.setProcessor("Test Processor");
        consoleToUpdate.setPrice(1.00);
        consoleToUpdate.setQuantity(1);

        when(consoleRepository.save(consoleToUpdate)).thenReturn(consoleToUpdate);

        Console updatedConsole = consoleService.updateConsole(consoleToUpdate);
        assertEquals("Updated Model", updatedConsole.getModel());
    }

    @Test
    public void testDeleteConsole() {
        doNothing().when(consoleRepository).deleteById(1);

        consoleService.deleteConsole(1);
        verify(consoleRepository).deleteById(1);
    }
}