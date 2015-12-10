package sample.PokemonDatabase;

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

/**
 * Created by 48089748z on 09/12/15.
 */
public class PokemonDatabaseGenerator
{
    //FULL POKEDEX URL = http://pokeapi.co/api/v1/pokedex/1/
    //FULL POKEMON 1 URL = http://pokeapi.co/api/v1/pokemon/1/
    //FULL POKEMON 1 IMAGE URL = http://pokeapi.co/media/img/1.png
    private static Scanner in = new Scanner(System.in);
    private static String BASE_URL = "http://pokeapi.co/";
    private static String POKEDEX_URL = BASE_URL + "api/v1/pokedex/1/";
    private static String RESOURCE_URI = "api/v1/pokemon/1/";
    private static String POKEMON_URL = BASE_URL + RESOURCE_URI;
    private static Pokedex pokedex;

    public static void main(String[] args)
    {
        //deleteDatabase();
        //generateDatabase();
        //generateObjects();
        //printInfo();
        //doInserts();
        //doSelects();
    }
    public static void doSelects()
    {
        System.out.println("\nSearch Pokemon by name");
        String name = in.nextLine().toLowerCase();
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM POKEMONS WHERE NAME = '" + name + "'");
            if (result.next()) //UNA BUSQUEDA POR NOMBRE SOLO PUEDEN DARNOS UN CURSOR CON UN RESULTADO, POR ESO PONEMOS IF EN VEZ DE WHILE
            {
                System.out.println("\nPokemon Name: " + result.getString("NAME"));
                System.out.println("Pokemon LifePoints: " + result.getString("LIFEPOINTS"));
                System.out.println("Pokemon Id: " + result.getString("ID"));
                System.out.println("Pokemon Weight: " + result.getString("WEIGHT"));
                System.out.println("Pokemon Image URL: " + result.getString("IMAGE"));
                System.out.println("Pokemon Resource URL: " + result.getString("RESOURCE_URI"));
            }
            else{System.out.println("NO RESULTS FOR '"+name+"'");}
            result.close();
            statement.close();
            connection.close();
        } catch (Exception one) {}
    }
    public static void doInserts()
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
                System.out.println("INSERTED "+pokedex.getPokemons().get(x).getName().toUpperCase()+" INTO DATABASE SUCCESSFULLY!");
            }
            catch (Exception one) {
                System.out.println("INSERT "+x+" FAILED! ("+pokedex.getPokemons().get(x).getName()+")");
            }
        }
    }
    private static void generateDatabase() //Metode que nom�s sha d'executar un cop al principi per a crear la base de dades de Pelicules.
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
        } catch (Exception one) {System.out.println("FAILED TO GENERATE DATABASE");}
        System.out.println("\n---------------------------------------- DATABASE GENERATED SUCCESSFULLY! ----------------------------------------");
    }
    public static void deleteDatabase()
    {
        File database = new File("/home/48089748z/IdeaProjects/JAVAFXPokemons/Pokemons.db");
        if(database.delete()) {System.out.println("\n---------------------------------------- DATABASE DELETED SUCCESSFULLY! ----------------------------------------");}
    }
    public static void printInfo()
    {
        System.out.println("\n---------------------------------------- PRINTING ALL INFO FROM OBJECTS ----------------------------------------");
        System.out.println(pokedex.toString());
        for(int x=0; x<pokedex.getPokemons().size(); x++)
        {
            System.out.println("\n"+pokedex.getPokemons().get(x).toStringDetailed());
        }
    }
    public static void generateObjects()
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
            pokemon.setImage(BASE_URL+"media/img/"+pokemon.getId()+".png");
            pokemon.toStringDetailed();

            System.out.println("\n"+pokemon.getName().toUpperCase()+ " OBJECT GENERATED... ("+x+"/"+pokemonsJA.size()+")");
            pokemons.add(pokemon);
        }
        System.out.println("\n---------------------------------------- POKEMON OBJECTS GENERATED SUCCESFULLY ----------------------------------------");
        pokedex.setPokemons(pokemons);
        System.out.println("\n---------------------------------------- POKEDEX OBJECT GENERATED SUCCESFULLY ----------------------------------------");
    }
    public static String getJSON(String URLtoRead) //Metode per agafar el String que cont� el JSON desde internet
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
