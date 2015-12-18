package sample;

/**
 * Created by 48089748z on 09/12/15.
 */
public class Pokemon
{
    private String name;
    private String resource_uri;
    private String image;  //http://pokeapi.co/media/img/1383571573.78.png
    private String weight;
    private String lifepoints;
    private String id;
    public Pokemon(String name, String resource_uri, String image, String weight, String lifepoints, String id)
    {
        this.name=name;
        this.resource_uri=resource_uri;
        this.image=image;
        this.weight=weight;
        this.lifepoints=lifepoints;
        this.id=id;
    }
    public Pokemon(){}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getResource_uri() {return resource_uri;}
    public void setResource_uri(String resource_uri) {this.resource_uri = resource_uri;}
    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}
    public String getWeight() {return weight;}
    public void setWeight(String weight) {this.weight = weight;}
    public String getLifepoints() {return lifepoints;}
    public void setLifepoints(String lifepoints) {this.lifepoints = lifepoints;}
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String toString()
    {
        return " [Name:: "+name+" - Lifepoints: "+lifepoints+"] ";
    }
    public String toStringDetailed()
    {
        return "\nPOKEMON "+id+" DETAILS\nName: "+name+"\nLifepoints: "+lifepoints+"\nWeight: "+weight+"\nImage: "+image+"\nResource uri: "+resource_uri;
    }
}
