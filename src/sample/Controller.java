package sample;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller
{
    PokemonDAO DAO = new PokemonDAO(); //Creem el DATA ACCESS OBJECT PER LA BASE DE DADES DE POKEMONS.
    public Slider slider;
    //public ScrollPane scrollPane;
    public Button searchBT;
    public TextArea searchText;
    public ListView<String> listView;
    public ImageView image;
    public Text details;
    public Text downloadinginfo;

    public ObservableList<String> items = FXCollections.observableArrayList();

    public void initialize()
    {
        downloadinginfo.setText("\nDATABASE STATUS");
        image.setFitHeight(200);
        image.setFitWidth(200);
        slider.setValue(image.getFitHeight() / 5);
        slider.setVisible(false);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() //ON CLICK LISTENER PER AL LISTVIEW
        {
            public void handle(MouseEvent event)
            {
                slider.setVisible(true);
                listView.setVisible(false);
                image.setVisible(true);
                image.setImage(new Image(DAO.getImageURL(listView.getSelectionModel().getSelectedIndex() + 1)));
                details.setVisible(true);
                details.setText(listView.getSelectionModel().getSelectedItem());
            }
        });

        image.setOnMouseClicked(new EventHandler<MouseEvent>() //ON CLICK LISTENER PER A LA IMATGE
        {
            public void handle(MouseEvent event)
            {
                slider.setVisible(false);
                listView.setVisible(true);
                image.setVisible(false);
                details.setVisible(false);
            }
        });
        slider.valueProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                Double sliderValue = newValue.doubleValue();
                downloadinginfo.setText("IMAGE WIDTH: "+image.getFitWidth()+" dp\nIMAGE HEIGHT: "+image.getFitHeight()+" dp");
                image.setFitHeight(sliderValue * 5);
                image.setFitWidth(sliderValue * 5);
            }
        });
        back();
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
        slider.setVisible(true);
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
            back();
            items.clear();
            items.add("\n THAT POKEMON DOESN'T EXIST");
            listView.setItems(items);
        }
        searchText.setVisible(false);
        searchBT.setVisible(false);
    }
    public void back()
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
    public void close(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void about(ActionEvent actionEvent) {
        back();
        items.clear();
        items.add("\n THIS IS JUST A POKEDEX\n\n NO MORE INFO AVAILABLE");
        listView.setItems(items);
    }

    public void back(ActionEvent actionEvent) {back();}

    public void setStatusText(String status)
    {
        downloadinginfo.setText(status);
    }

    public void refresh(ActionEvent actionEvent)
    {
        downloadinginfo.setText("\nUPDATING DATABASE");
        PokemonDatabaseGenerator downloader = new PokemonDatabaseGenerator(this);
        downloader.start();
    }
    public void loadListView()
    {
        back();
    }
}
