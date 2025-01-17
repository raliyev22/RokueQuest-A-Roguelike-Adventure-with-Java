package test;

import main.view.LoadMenuView;
import main.controller.PlayModeController;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadMenuViewTest {

    private LoadMenuView loadMenuView;

    @Before
    public void setUp() throws IOException {
        loadMenuView = new LoadMenuView();

        // Prepare the mock save file list for testing
        String filePath = "src/saveFiles/allSaveFiles.txt";
        File saveFileList = new File(filePath);
        if (!saveFileList.exists()) {
            saveFileList.createNewFile();
        }

        // Simulate adding some save files for testing
        try (FileWriter writer = new FileWriter(saveFileList)) {
            writer.write("save1.txt\n");
            writer.write("save2.txt\n");
            writer.write("save3.txt\n");
        }
    }

   
    @Test
    public void testUpdateSaveFileList() {
        // Test the method responsible for updating the save file list after a file has been deleted
        
        String filePath = "src/saveFiles/allSaveFiles.txt";
        File saveFile = new File("src/saveFiles/save4.txt");

        // Simulate adding a new save file
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("save4.txt\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check if the file was added
        ArrayList<String> savedFiles = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                savedFiles.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure the new save file is in the list
        assertTrue("New save file should be in the list", savedFiles.contains("save4.txt"));

        // Call the updateSaveFileList method to remove it
        loadMenuView.updateSaveFileList(savedFiles, "save4.txt", filePath);

        // Verify the save file is removed
        savedFiles.clear();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                savedFiles.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure "save4.txt" has been removed from the list
        assertFalse("Deleted save file should not exist in the list", savedFiles.contains("save4.txt"));
    }
    @Test
    public void testUpdateSaveFileList_EmptyList() {
    // Test the method when the save file list is empty
    
        String filePath = "src/saveFiles/allSaveFiles.txt";
        File saveFile = new File("src/saveFiles/save5.txt");

        // Simulate an empty save file list by clearing the file
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("");  // Writing an empty list
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check the list is empty
        ArrayList<String> savedFiles = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                savedFiles.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure the list is empty
        assertTrue("Save file list should be empty", savedFiles.isEmpty());

        // Simulate adding a new save file (save5.txt)
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("save5.txt\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Verify the new save file is in the list
        savedFiles.clear();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                savedFiles.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure "save5.txt" is now in the list
        assertTrue("New save file should be added to the list", savedFiles.contains("save5.txt"));
    }
    @Test
    public void testUpdateSaveFileList_FileNotExist() {
        // Test when trying to remove a file that doesn't exist in the save file list
        
        String filePath = "src/saveFiles/allSaveFiles.txt";
        File saveFileToRemove = new File("src/saveFiles/save6.txt");

        // Simulate a save file list that doesn't contain "save6.txt"
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("save1.txt\n");
            writer.write("save2.txt\n");
            writer.write("save3.txt\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Check the save file list before removal
        ArrayList<String> savedFiles = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                savedFiles.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure "save6.txt" is not in the list
        assertFalse("Save file should not exist in the list", savedFiles.contains("save6.txt"));

        // Call the updateSaveFileList method to remove "save6.txt" (it doesn't exist)
        loadMenuView.updateSaveFileList(savedFiles, "save6.txt", filePath);

        // Verify the save file list is unchanged
        savedFiles.clear();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                savedFiles.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ensure the list still contains the existing files
        assertTrue("Existing files should remain in the list", savedFiles.contains("save1.txt"));
        assertTrue("Existing files should remain in the list", savedFiles.contains("save2.txt"));
        assertTrue("Existing files should remain in the list", savedFiles.contains("save3.txt"));
    }
}
