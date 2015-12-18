package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by 48089748z on 10/12/15.
 */
public class PokemonDAO
{
    public String search(String pokemonName)
    {
        pokemonName.toLowerCase();
        try
        {
            String toReturn;

            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM POKEMONS WHERE NAME = '"+pokemonName+"'");
            if (result.next())
            {
                toReturn = "Pokemon ID:                       " + result.getString("ID")
                        +"\nPokemon Name:                 " + result.getString("NAME").toUpperCase()
                        + "\nPokemon LifePoints:           " + result.getString("LIFEPOINTS")
                        +"\nPokemon Weight:               " + result.getString("WEIGHT")+" kg"
                        +"\nPokemon Image URL:          " + result.getString("IMAGE")
                        +"\nPokemon Resource URL:     " + result.getString("RESOURCE_URI")+"\n\n";
                result.close();
                statement.close();
                connection.close();
                return toReturn;
            }
        }
        catch (Exception one){}
        return " ";
    }
    public String getImageURL(int pokemonId)
    {
        try
        {
            String imageURL;

            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM POKEMONS WHERE ID ="+pokemonId);
            if (result.next())
            {
                imageURL = result.getString("IMAGE");
                result.close();
                statement.close();
                connection.close();
                return imageURL;
            }
        }
        catch (Exception one){}
        return " ";
    }
    public String getPokemonInfo(int pokemonId)
    {
        try
        {
            String toReturn;

            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM POKEMONS WHERE ID ="+pokemonId);
            if (result.next())
            {
                toReturn = "Pokemon ID:                       " + result.getString("ID")
                        +"\nPokemon Name:                 " + result.getString("NAME").toUpperCase()
                        + "\nPokemon LifePoints:           " + result.getString("LIFEPOINTS")
                        +"\nPokemon Weight:               " + result.getString("WEIGHT")+" kg"
                        +"\nPokemon Image URL:          " + result.getString("IMAGE")
                        +"\nPokemon Resource URL:     " + result.getString("RESOURCE_URI")+"\n\n";
                result.close();
                statement.close();
                connection.close();
                return toReturn;
            }
        }
        catch (Exception one){}
        return " ";
    }

    public String getImageURL(String pokemonName)
    {
        try
        {
            String imageURL;

            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Pokemons.db");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet result;
            result = statement.executeQuery("SELECT * FROM POKEMONS WHERE NAME = '"+pokemonName+"'");
            if (result.next())
            {
                imageURL = result.getString("IMAGE");
                result.close();
                statement.close();
                connection.close();
                return imageURL;
            }
        }
        catch (Exception one){}
        return " ";
    }
}
