package sample.PokemonDatabase;

import java.util.ArrayList;

/**
 * Created by 48089748z on 09/12/15.
 */
public class Pokedex{
    private String created;
    private String lastModified;
    private String name;
    private ArrayList<Pokemon> pokemons;

    public Pokedex(String created, String lastModified, String name, ArrayList<Pokemon> pokemons)
    {
        this.created=created;
        this.lastModified=lastModified;
        this.name=name;
        this.pokemons=pokemons;
    }
    public Pokedex(){}
    public String getCreated() {return created;}
    public void setCreated(String created) {this.created = created;}
    public String getLastModified() {return lastModified;}
    public void setLastModified(String lastModified) {this.lastModified = lastModified;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public ArrayList<Pokemon> getPokemons() {return pokemons;}
    public void setPokemons(ArrayList<Pokemon> pokemons) {this.pokemons = pokemons;}
    public String toString()
    {
        String allPokemons = "{";
        for(int x=0; x<pokemons.size(); x++)
        {
            allPokemons=allPokemons+pokemons.get(x).toString();
        }
        allPokemons=allPokemons+"}";
        return "\nPOKEDEX DETAILS\nCreated: "+created+"\nLast Modified: "+lastModified+"\nName: "+name+"\nPokemons: "+allPokemons;
    }
}

