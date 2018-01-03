package tech.mcprison.prison.mines.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.mines.MineException;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

public class MineManager {
  /*
   * Fields & Constants
   */

  private Collection collection;
  private List<Mine> loadedMines;

  /*
   * Constructor
   */

  /**
   * Instantiate this {@link MineManager}.
   */
  public MineManager(Collection collection) {
    this.collection = collection;
    this.loadedMines = new ArrayList<>();
  }

  /*
   * Methods & Getters & Setters
   */

  /**
   * Loads a Mine from a file into the loaded Mines list.
   * After this method is called, the Mine will be ready for use in the server.
   *
   * @param MineFile The key that this Mine is stored as. Case sensitive.
   * @throws IOException If the file could not be read or does not exist.
   */
  public void loadMine(String MineFile) throws IOException {
    Document document = collection.get(MineFile).orElseThrow(IOException::new);
    loadedMines.add(new Mine(document));
  }

  /**
   * Loads every file within a directory with the extension ".Mine.json".
   * If one file could not be loaded, it will simply be skipped.
   *
   * @throws Exception If the world in which the mine is located cannot be found.
   */
  public void loadMines() throws MineException {
    List<Document> Mines = collection.getAll();
    Mines.forEach(document -> loadedMines.add(new Mine(document)));
  }

  /**
   * Saves a Mine to its save file.
   *
   * @param Mine     The {@link Mine} to save.
   * @param saveFile The key to write the Mine as. Case sensitive.
   * @throws IOException If the Mine could not be serialized, or if the Mine could not be saved to the file.
   */
  public void saveMine(Mine Mine, String saveFile) throws IOException {
    collection.insert(saveFile, Mine.toDocument());
  }

  /**
   * Saves a Mine to its save file.
   *
   * @param Mine The {@link Mine} to save.
   * @throws IOException If the Mine could not be serialized, or if the Mine could not be saved to the file.
   */
  public void saveMine(Mine Mine) throws IOException {
    this.saveMine(Mine, "Mine_" + Mine.name);
  }

  /**
   * Saves all the loaded Mines to their own files within a directory.
   *
   * @throws IOException If the MineFolder does not exist, or if one of the Mines could not be saved.
   */
  public void saveMines() throws IOException {
    for (Mine Mine : loadedMines) {
      saveMine(Mine);
    }
  }

  /**
   * Creates a new Mine with the specified parameters.
   * This new Mine will be loaded, but will not be written to disk until {@link #saveMine(Mine, String)} is called.
   *
   * @param name The name of this Mine, for use with the user (i.e. this will be shown to the user).
   * @param tag  The tag of this Mine, which is used for prefixes/suffixes.
   * @param cost The cost of this Mine, in whichever units the player chose (i.e. money or experience).
   * @return An optional containing either the {@link Mine} if it could be created, or empty
   * if the Mine's creation failed.
   */
  public Optional<Mine> createMine(String name, String tag, double cost) {
    // Set the default values...
    Mine newMine = new Mine();
    newMine.id = getNextAvailableId();
    newMine.name = name;
    newMine.tag = tag;
    newMine.cost = cost;
    newMine.MineUpCommands = new ArrayList<>();

    // ... add it to the list...
    loadedMines.add(newMine);

    // ...and return it.
    return Optional.of(newMine);
  }

  /**
   * Returns the next available ID for a new Mine.
   * This works by adding one to the highest current Mine ID.
   *
   * @return The next available Mine's ID.
   */
  private int getNextAvailableId() {
    // Set the highest to -1 for now, since we'll add one at the end
    int highest = -1;

    // If anything's higher, it's now the highest...
    for (Mine Mine : loadedMines) {
      if (highest < Mine.id) {
        highest = Mine.id;
      }
    }

    return highest + 1;
  }

  /**
   * Removes the provided Mine. This will go through the process of removing the Mine from the loaded
   * Mines list, removing the Mine's save files, adjusting the ladder positions that this Mine is a part of,
   * and finally, moving the players back to the previous Mine of their ladder. This is a potentially destructive
   * operation; be sure that you are using it in the correct manner.
   *
   * @param Mine The {@link Mine} to be removed.
   * @return true if the Mine was removed successfully, false otherwise.
   */
  public boolean removeMine(Mine Mine) {

    // ... remove it from each ladder it was in...
    final boolean[] success = {true};
    PrisonMines.getInstance().getMineManager().getLaddersWithMine(Mine.id)
        .forEach(MineLadder -> {
          MineLadder.removeMine(MineLadder.getPositionOfMine(Mine));
          try {
            PrisonMines.getInstance().getLadderManager().saveLadder(MineLadder);
          } catch (IOException e) {
            success[0] = false;
            Output.get().logError("Could not save ladder.", e);
          }
        });
    if(!success[0]) {
      return false;
    }

    // Remove it from the list...
    loadedMines.remove(Mine);

    // ... and remove the Mine's save files.
    collection.remove("Mine_" + Mine.id);
    return true;
  }

  /**
   * Returns the Mine with the specified ID.
   *
   * @param name The mine's name.
   * @return An optional containing either the {@link Mine} if it could be found, or empty if it does not exist by the specified id.
   */
  public Optional<Mine> getMine(String name) {
    return loadedMines.stream().filter(Mine -> Mine.getName().equals(name)).findFirst();
  }

  /**
   * Returns a list of all the loaded Mines on the server.
   *
   * @return A {@link List}. This will never return null, because if there are no loaded Mines, the list will just be empty.
   */
  public List<Mine> getMines() {
    return loadedMines;
  }

}
