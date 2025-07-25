package ch.makery.address.view
import ch.makery.address.model.Person
import ch.makery.address.MainApp
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import scalafx.Includes.*
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scala.util.{Failure, Success}

@FXML
class PersonOverviewController():
  @FXML
  private var personTable: TableView[Person] = null
  @FXML
  private var firstNameColumn: TableColumn[Person, String] = null
  @FXML
  private var lastNameColumn: TableColumn[Person, String] = null
  @FXML
  private var firstNameLabel: Label = null
  @FXML
  private var lastNameLabel: Label = null
  @FXML
  private var streetLabel: Label = null
  @FXML
  private var postalCodeLabel: Label = null
  @FXML
  private var cityLabel: Label = null
  @FXML
  private var birthdayLabel: Label = null
  // initialize Table View display contents model
  def initialize() =
    personTable.items = MainApp.personData
    // initialize columns's cell values
    firstNameColumn.cellValueFactory = _.value.firstName
    lastNameColumn.cellValueFactory  = _.value.lastName

  private def showPersonDetails(person: Option[Person]): Unit =
    person match
      case Some(person) =>
        // Fill the labels with info from the person object.
        firstNameLabel.text <== person.firstName
        lastNameLabel.text <== person.lastName
        streetLabel.text <== person.street
        cityLabel.text <== person.city;
        postalCodeLabel.text = person.postalCode.value.toString

      case None =>
        // Person is null, remove all the text.
        firstNameLabel.text = ""
        lastNameLabel.text = ""
        streetLabel.text = ""
        postalCodeLabel.text = ""
        cityLabel.text = ""
        birthdayLabel.text = ""

  /**
   * Called when the user clicks on the delete button.
   */
  def handleDeletePerson(action : ActionEvent) =
    val selectedIndex = personTable.selectionModel().selectedIndex.value
    val selectedPerson = personTable.selectionModel().selectedItem.value
    if (selectedIndex >= 0) then
      selectedPerson.save() match
        case Success(x) =>
          personTable.items().remove(selectedIndex);
        case Failure(e) =>
          val alert = new Alert(Alert.AlertType.Warning):
            initOwner(MainApp.stage)
            title = "Failed to Save"
            headerText = "Database Error"
            contentText = "Database problem filed to save changes"
          .showAndWait()
    else
        // Nothing selected.
        val alert = new Alert(AlertType.Warning):
          initOwner(MainApp.stage)
          title       = "No Selection"
          headerText  = "No Person Selected"
          contentText = "Please select a person in the table."
        .showAndWait()


  def handleNewPerson(action: ActionEvent) =
    val person = new Person("", "")
    val okClicked = MainApp.showPersonEditDialog(person)
    if (okClicked) then {
      MainApp.personData += person
      person.save()
    }


  def handleEditPerson(action: ActionEvent) =
    val selectedPerson = personTable.selectionModel().selectedItem.value
    if (selectedPerson != null) then
      val okClicked = MainApp.showPersonEditDialog(selectedPerson)

      if (okClicked) then{
         showPersonDetails(Some(selectedPerson))
         selectedPerson.save()
      }

    else
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning):
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Person Selected"
        contentText = "Please select a person in the table."
      .showAndWait()

