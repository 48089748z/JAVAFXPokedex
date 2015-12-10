package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


public class Controller //ABANS D'EXECUTAR LA APLICACIÓ HAURÁS D'EXECUTAR EL PokemonDatabaseGenerator per a que crei la BASE DE DADES.
{
    PokemonDAO DAO = new PokemonDAO(); //Creem el DATA ACCESS OBJECT PER LA BASE DE DADES DE POKEMONS.

    public Button searchBT;
    public TextArea searchText;
    public ListView<String> listView;
    public ImageView image;
    public Text details;
    public ObservableList<String> items = FXCollections.observableArrayList();

    public void initialize()
    {
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() //ON CLICK LISTENER PER AL LISTVIEW
        {
            public void handle(MouseEvent event)
            {
                listView.setVisible(false);
                image.setVisible(true);
                image.setImage(new Image(DAO.getImageURL(listView.getSelectionModel().getSelectedIndex()+1)));
                details.setVisible(true);
                details.setText(listView.getSelectionModel().getSelectedItem());
            }
        });

        image.setOnMouseClicked(new EventHandler<MouseEvent>() //ON CLICK LISTENER PER A LA IMATGE
        {
            public void handle(MouseEvent event)
            {
                listView.setVisible(true);
                image.setVisible(false);
                details.setVisible(false);
            }
        });
        refresh();
    }

    public void letSearch(ActionEvent actionEvent)
    {
        searchText.setVisible(true);
        searchBT.setVisible(true);
        searchText.setText("");
        searchText.setPromptText("Search by Pokemon name");
    }
    public void search(ActionEvent actionEvent)
    {
        listView.setVisible(false);
        image.setVisible(true);
        details.setVisible(true);

        try
        {
            details.setText(DAO.search(searchText.getText()));
            image.setImage(new Image(DAO.getImageURL(searchText.getText())));
        }
        catch (Exception one)
        {
            refresh();
            items.clear();
            items.add("\n THAT POKEMON DOESN'T EXIST");
            listView.setItems(items);
        }
        searchText.setVisible(false);
        searchBT.setVisible(false);
    }
    public void refresh()
    {
        searchText.setVisible(false);
        searchBT.setVisible(false);
        listView.setVisible(true);
        image.setVisible(false);
        details.setVisible(false);
        items.clear();
        for (int x=1; x<=718; x++)
        {
            items.add(DAO.getPokemonInfo(x));
        }
        listView.setItems(items);
    }
    public void close(ActionEvent actionEvent)
    {
        Platform.exit();
    }
    public void about(ActionEvent actionEvent)
    {
        refresh();
        items.clear();
        items.add("\n THIS IS JUST A POKEDEX\n\n NO MORE INFO AVAILABLE");
        listView.setItems(items);
    }
    public void refresh(ActionEvent actionEvent)
    {
        refresh();
    }
}
