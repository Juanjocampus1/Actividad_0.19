package com.empresa.actividad_019;

import com.mongodb.client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Comparator;

public class HelloController {
    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField edadTextField;
    @FXML
    private TextField emailTextField;

    @FXML
    private TableView<Persona> personasTable;
    @FXML
    private TableColumn<Persona, String> nombreColumn;
    @FXML
    private TableColumn<Persona, Integer> edadColumn;
    @FXML
    private TableColumn<Persona, String> emailColumn;

    @FXML
    private ComboBox<String> ordenarComboBox;
    @FXML
    private ComboBox<String> ordenarEdadComboBox;
    @FXML
    private ComboBox<String> ordenarEmailLongitudComboBox;
    @FXML
    private TextField buscadorTextField;

    String uri = "mongodb+srv://admin:admin@cluster0.uay2677.mongodb.net/";

    @FXML
    protected void agregarPersona() {
        String nombre = nombreTextField.getText();
        int edad = Integer.parseInt(edadTextField.getText());
        String email = emailTextField.getText();

        Persona persona = new Persona(nombre, edad, email);

        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("actividad");
        MongoCollection<Document> collection = database.getCollection("personas");

        Document doc = new Document("nombre", persona.getNombre())
                .append("edad", persona.getEdad())
                .append("email", persona.getEmail());

        collection.insertOne(doc);
        mongoClient.close();

        nombreTextField.clear();
        edadTextField.clear();
        emailTextField.clear();
    }

    @FXML
    protected void mostrarPersonas() {
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("actividad");
        MongoCollection<Document> collection = database.getCollection("personas");

        FindIterable<Document> docs = collection.find();
        ObservableList<Persona> personas = FXCollections.observableArrayList();
        for (Document doc : docs) {
            String nombre = doc.getString("nombre");
            int edad = doc.getInteger("edad");
            String email = doc.getString("email");
            personas.add(new Persona(nombre, edad, email));
        }

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        personasTable.setItems(personas);

        mongoClient.close();
    }

    @FXML
    public void initialize() {
        ordenarComboBox.getItems().addAll("A-Z", "Z-A");
        ordenarComboBox.getSelectionModel().selectFirst();
        ordenarEdadComboBox.getItems().addAll("Mayor a menor", "Menor a mayor");
        ordenarEdadComboBox.getSelectionModel().selectFirst();
        ordenarEmailLongitudComboBox.getItems().addAll("Mayor a menor", "Menor a mayor");
        ordenarEmailLongitudComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    protected void ordenarPorNombre() {
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("actividad");
        MongoCollection<Document> collection = database.getCollection("personas");

        FindIterable<Document> docs = collection.find();
        ObservableList<Persona> personas = FXCollections.observableArrayList();
        for (Document doc : docs) {
            String nombre = doc.getString("nombre");
            int edad = doc.getInteger("edad");
            String email = doc.getString("email");
            personas.add(new Persona(nombre, edad, email));
        }

        if (ordenarComboBox.getValue().equals("A-Z")) {
            personas.sort(Comparator.comparing(Persona::getNombre));
        }
        else {
            personas.sort(Comparator.comparing(Persona::getNombre).reversed());
        }

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        personasTable.setItems(personas);

        mongoClient.close();
    }

    @FXML
    protected void ordenarPorEdad() {
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("actividad");
        MongoCollection<Document> collection = database.getCollection("personas");

        FindIterable<Document> docs = collection.find();
        ObservableList<Persona> personas = FXCollections.observableArrayList();
        for (Document doc : docs) {
            String nombre = doc.getString("nombre");
            int edad = doc.getInteger("edad");
            String email = doc.getString("email");
            personas.add(new Persona(nombre, edad, email));
        }

        // Convertir ObservableList a Array
        Persona[] personasArray = new Persona[personas.size()];
        personas.toArray(personasArray);

        // Implementación del algoritmo de ordenamiento Shell
        int n = personasArray.length;
        for (int gap = n/2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i += 1) {
                Persona temp = personasArray[i];
                int j;
                for (j = i; j >= gap && (ordenarEdadComboBox.getValue().equals("Mayor a menor") ?
                        personasArray[j - gap].getEdad() < temp.getEdad() : personasArray[j - gap].getEdad() > temp.getEdad()); j -= gap) {
                    personasArray[j] = personasArray[j - gap];
                }
                personasArray[j] = temp;
            }
        }

        // Convertir Array a ObservableList
        ObservableList<Persona> personasOrdenadas = FXCollections.observableArrayList(personasArray);

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        personasTable.setItems(personasOrdenadas);

        mongoClient.close();
    }

    @FXML
    protected void ordenarPorLongitudEmail() {
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("actividad");
        MongoCollection<Document> collection = database.getCollection("personas");

        FindIterable<Document> docs = collection.find();
        ObservableList<Persona> personas = FXCollections.observableArrayList();
        for (Document doc : docs) {
            String nombre = doc.getString("nombre");
            int edad = doc.getInteger("edad");
            String email = doc.getString("email");
            personas.add(new Persona(nombre, edad, email));
        }

        // Convertir ObservableList a Array
        Persona[] personasArray = new Persona[personas.size()];
        personas.toArray(personasArray);

        // Implementación del algoritmo de ordenamiento por inserción
        int n = personasArray.length;
        for (int i = 1; i < n; ++i) {
            Persona key = personasArray[i];
            int j = i - 1;

            while (j >= 0 && (ordenarEmailLongitudComboBox.getValue().equals("Mayor a menor") ?
                    personasArray[j].getEmail().length() < key.getEmail().length() : personasArray[j].getEmail().length() > key.getEmail().length())) {
                personasArray[j + 1] = personasArray[j];
                j = j - 1;
            }
            personasArray[j + 1] = key;
        }

        // Convertir Array a ObservableList
        ObservableList<Persona> personasOrdenadas = FXCollections.observableArrayList(personasArray);

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        personasTable.setItems(personasOrdenadas);

        mongoClient.close();
    }

    @FXML
    protected void buscar() {
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("actividad");
        MongoCollection<Document> collection = database.getCollection("personas");

        FindIterable<Document> docs = collection.find();
        ObservableList<Persona> personas = FXCollections.observableArrayList();
        for (Document doc : docs) {
            String nombre = doc.getString("nombre");
            int edad = doc.getInteger("edad");
            String email = doc.getString("email");
            personas.add(new Persona(nombre, edad, email));
        }

        String textoBuscado = buscadorTextField.getText().toLowerCase();

        ObservableList<Persona> coincidencias = FXCollections.observableArrayList();

        for (Persona persona : personas) {
            if (persona.getNombre().toLowerCase().contains(textoBuscado) ||
                    String.valueOf(persona.getEdad()).contains(textoBuscado) ||
                    persona.getEmail().toLowerCase().contains(textoBuscado)) {
                coincidencias.add(persona);
            }
        }

        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        personasTable.setItems(coincidencias);

        mongoClient.close();
    }
}