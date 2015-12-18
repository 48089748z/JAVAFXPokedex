package sample;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PokemonDatabaseGenerator extends Thread
{
    //FULL POKEDEX URL = http://pokeapi.co/api/v1/pokedex/1/
    //FULL POKEMON 1 URL = http://pokeapi.co/api/v1/pokemon/1/
    //FULL POKEMON 1 IMAGE URL = http://pokeapi.co/media/img/1.png
    private Scanner in = new Scanner(System.in);
    private String BASE_URL = "http://pokeapi.co/";
    private String POKEDEX_URL = BASE_URL + "api/v1/pokedex/1/";
    private String RESOURCE_URI = "api/v1/pokemon/1/";
    private String POKEMON_URL = BASE_URL + RESOURCE_URI;
    private Pokedex pokedex;
    private Controller controller;

    public PokemonDatabaseGenerator(Controller controller)
    {
        this.controller = controller;
    }
    public void run()
    {
        deleteDatabase();
        generateDatabase();
        generateObjects();
        //printInfo();
        doInserts();
        controller.setStatusText("DATABASE CREATED SUCCESFULLY");
        controller.loadListView();
    }
    public void doInserts()
    {
        System.out.println();
        for (int x=0; x<pokedex.getPokemons().size(); x++)
        {
            try
            {
                Class.forName("org.sqlite.JDBC");
                Connection connection = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
                connection.setAutoCommit(false);
                String tableSQL = "INSERT INTO POKEMONS"+
                        "(NAME, RESOURCE_URI, WEIGHT, LIFEPOINTS, ID, IMAGE) VALUES"+
                        "(?,?,?,?,?,?)";
                PreparedStatement prepStat = connection.prepareStatement(tableSQL);
                prepStat.setString(1, pokedex.getPokemons().get(x).getName());
                prepStat.setString(2, pokedex.getPokemons().get(x).getResource_uri());
                prepStat.setString(3, pokedex.getPokemons().get(x).getWeight());
                prepStat.setString(4, pokedex.getPokemons().get(x).getLifepoints());
                prepStat.setString(5, pokedex.getPokemons().get(x).getId());
                prepStat.setString(6, pokedex.getPokemons().get(x).getImage());
                prepStat.executeUpdate();
                prepStat.close();
                connection.commit();
                connection.close();
                System.out.println("INSERTED " + pokedex.getPokemons().get(x).getName().toUpperCase() + " INTO DATABASE SUCCESSFULLY!");
                controller.setStatusText("\nINSERTED " + pokedex.getPokemons().get(x).getName().toUpperCase()+" INTO DATABASE SUCCESSFULLY!");
            }
            catch (Exception one) {
                System.out.println("INSERT "+x+" FAILED! ("+pokedex.getPokemons().get(x).getName()+")");
            }
        }
    }
    private void generateDatabase() //Metode que nom�s sha d'executar un cop al principi per a crear la base de dades de Pelicules.
    {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
            Statement statement = connection.createStatement();
            String query =   "CREATE TABLE POKEMONS"+
                    "(NAME        TEXT ,"+
                    "RESOURCE_URI TEXT ,"+
                    "IMAGE        TEXT ,"+
                    "WEIGHT       TEXT ,"+
                    "LIFEPOINTS   TEXT ,"+
                    "ID           TEXT)";
            statement.executeUpdate(query);
            statement.close();
            connection.close();
            controller.setStatusText("\nDATABASE GENERATED");
        } catch (Exception one) {System.out.println("FAILED TO GENERATE DATABASE");}
        System.out.println("\n---------------------------------------- DATABASE GENERATED SUCCESSFULLY! ----------------------------------------");
    }
    public void deleteDatabase()
    {
        File database = new File("/home/48089748z/IdeaProjects/JAVAFXPokemons/Pokemons.db");
        if(database.delete()) {System.out.println("\n---------------------------------------- DATABASE DELETED SUCCESSFULLY! ----------------------------------------");
            controller.setStatusText("Database Deleted");}
    }
    public void printInfo()
    {
        System.out.println("\n---------------------------------------- PRINTING ALL INFO FROM OBJECTS ----------------------------------------");
        System.out.println(pokedex.toString());
        for(int x=0; x<pokedex.getPokemons().size(); x++)
        {
            System.out.println("\n"+pokedex.getPokemons().get(x).toStringDetailed());
        }
    }
    public void generateObjects()
    {
        System.out.println("\n---------------------------------------- GENERATING OBJECTS... (This might take a while) ----------------------------------------");
        pokedex = new Pokedex();
        JSONObject pokedexJO = (JSONObject) JSONValue.parse(getJSON(POKEDEX_URL));
        pokedex.setName(pokedexJO.get("name").toString());
        pokedex.setCreated(pokedexJO.get("created").toString());
        pokedex.setLastModified(pokedexJO.get("modified").toString());

        ArrayList<Pokemon> pokemons = new ArrayList<>();
        JSONArray pokemonsJA = (JSONArray) JSONValue.parse(pokedexJO.get("pokemon").toString());
        for(int x=0; x<pokemonsJA.size(); x++) //Agafarem els Pokemons del 0 al 5 per testejar
        {
            JSONObject pokemonJO = (JSONObject) JSONValue.parse(pokemonsJA.get(x).toString());
            Pokemon pokemon = new Pokemon();
            pokemon.setName(pokemonJO.get("name").toString());
            RESOURCE_URI = pokemonJO.get("resource_uri").toString();
            POKEMON_URL = BASE_URL + RESOURCE_URI;
            pokemon.setResource_uri(POKEMON_URL);

            JSONObject pokemonDetailsJO = (JSONObject) JSONValue.parse(getJSON(POKEMON_URL));
            pokemon.setWeight(pokemonDetailsJO.get("weight").toString());
            pokemon.setLifepoints(pokemonDetailsJO.get("hp").toString());
            pokemon.setId(pokemonDetailsJO.get("pkdx_id").toString());
            pokemon.setImage(BASE_URL + "media/img/" + pokemon.getId() + ".png");
            pokemon.toStringDetailed();

            System.out.println("\n" + pokemon.getName().toUpperCase() + " OBJECT GENERATED... (" + x + "/" + pokemonsJA.size() + ")");
            pokemons.add(pokemon);
            controller.setStatusText("\n"+pokemon.getName().toUpperCase()+ " OBJECT GENERATED... ("+x+"/"+pokemonsJA.size()+")");
        }
        System.out.println("\n---------------------------------------- POKEMON OBJECTS GENERATED SUCCESFULLY ----------------------------------------");
        pokedex.setPokemons(pokemons);
        System.out.println("\n---------------------------------------- POKEDEX OBJECT GENERATED SUCCESFULLY ----------------------------------------");
    }
    public String getJSON(String URLtoRead) //Metode per agafar el String que cont� el JSON desde internet
    {
        try
        {
            StringBuilder stringJSON = new StringBuilder();
            URL url = new URL(URLtoRead);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
            {
                stringJSON.append(line);
            }
            reader.close();
            return stringJSON.toString();
        }
        catch (Exception one)
        {
            return "getJSON() didn't work, you are in the Catch block!";
        }
    }
}
