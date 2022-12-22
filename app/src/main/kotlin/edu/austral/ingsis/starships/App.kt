package edu.austral.ingsis.starships

import edu.austral.ingsis.starships.ui.*
import edu.austral.ingsis.starships.ui.Collision
import edu.austral.ingsis.starships.ui.ElementColliderType.*
import javafx.application.Application
import javafx.application.Application.launch
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import starships.GameConfiguration
import starships.Juego
import starships.collidable.*
import starships.factories.GameFactory
import kotlin.system.exitProcess

fun main() {
    launch(Starships::class.java)
}

class Starships : Application() {
    private val imageResolver = CachedImageResolver(DefaultImageResolver())
    private val facade = ElementsViewFacade(imageResolver)
    private val keyTracker = KeyTracker()

    companion object {
        val STARSHIP = ImageRef("starship", 70.0, 70.0)
        val BULLET = ImageRef("bullet2", 70.0, 70.0)
        val ASTEROID = ImageRef("asteroid", 70.0, 70.0)
        private var gameFactory = GameFactory()
        var juego: Juego = gameFactory.generate(false,  GameConfiguration())
    }

    override fun start(primaryStage: Stage) {
        val pane = mainGameScene()
        val menu = menuScene(primaryStage, pane)


        val container = GameContainer(juego)
        facade.timeListenable.addEventListener(TimeListener(facade.elements, container, facade, this))
        facade.collisionsListenable.addEventListener(CollisionListener(container))
        keyTracker.keyPressedListenable.addEventListener(KeyPressedListener(container,  this, primaryStage, pane, menu))

        keyTracker.scene = menu
        primaryStage.scene = menu
        primaryStage.height = 800.0
        primaryStage.width = 800.0

        facade.start()
        keyTracker.start()
        primaryStage.show()
    }

    override fun stop() {
        facade.stop()
        keyTracker.stop()
        exitProcess(0)
    }

    fun adaptShape(shape : CollidableShape) : ElementColliderType{
        return when(shape){
            CollidableShape.RECTANGULAR -> Rectangular
            CollidableShape.ELLIPTICAL -> Elliptical
            CollidableShape.TRIANGULAR -> Triangular
        }
    }

    fun getImage(element: Collidable) : ImageRef {
        if (element.collidableType == CollidableType.SHIP){
            return STARSHIP
        }
        return if (element.collidableType == CollidableType.BULLET){
            BULLET

        } else ASTEROID
    }

    private fun mainGameScene(): StackPane {
        val pane = StackPane()
        val root = facade.view
        pane.children.addAll(root)
        root.id = "pane"
        return pane
    }

    private fun addElements(){
        val elements = juego.elements
        for (element in elements){
            facade.elements[element.id] = ElementModel(
                element.id,
                element.position.x.toDouble(),
                element.position.y.toDouble(),
                element.height,
                element.width,
                element.rotationInDegrees,
                adaptShape(element.collidableShape),
                getImage(element)
            )
        }
    }

    private fun menuScene(primaryStage: Stage, pane: StackPane): Scene {
        val title = Label("STARSHIPS")
        title.textFill = Color.BLANCHEDALMOND
        title.style = "-fx-font-family: VT323; -fx-font-size: 90;"

        val newGame = Label("Juego Nuevo")
        newGame.textFill = Color.BLANCHEDALMOND
        newGame.style = "-fx-font-family: VT323; -fx-font-size: 45"
        newGame.setOnMouseClicked {
            primaryStage.scene.root = pane
            addElements()
        }

        newGame.setOnMouseEntered {
            newGame.textFill = Color.MEDIUMPURPLE
            newGame.cursor = Cursor.HAND
        }

        newGame.setOnMouseExited {
            newGame.textFill = Color.BLANCHEDALMOND
        }

        val loadGame = Label("Juego Guardado")
        loadGame.textFill = Color.BLANCHEDALMOND
        loadGame.style = "-fx-font-family: VT323; -fx-font-size: 45;"
        loadGame.setOnMouseClicked {
            primaryStage.scene.root = pane
            //game.start(true)
            //juego.start(true)
            addElements()
        }
        loadGame.setOnMouseEntered {
            loadGame.textFill = Color.MEDIUMPURPLE
            loadGame.cursor = Cursor.HAND
        }

        loadGame.setOnMouseExited {
            loadGame.textFill = Color.BLANCHEDALMOND
        }

        val newAndLoadGame = HBox(70.0)
        newAndLoadGame.alignment = Pos.CENTER
        newAndLoadGame.children.addAll(newGame, loadGame)

        val verticalLayout = VBox(50.0)
        verticalLayout.id = "menu"
        verticalLayout.alignment = Pos.CENTER
        verticalLayout.children.addAll(title, newAndLoadGame)

        val menu = Scene(verticalLayout)
        menu.stylesheets.add(this::class.java.classLoader.getResource("style.css")?.toString())
        return menu
    }

    fun pauseScene(primaryStage: Stage, pane: StackPane, menu: Scene): Scene {
        val resume = Label("Reanudar")
        resume.textFill = Color.BLANCHEDALMOND
        resume.style = "-fx-font-family: VT323; -fx-font-size: 45"
        resume.setOnMouseClicked {
            primaryStage.scene = menu
            primaryStage.scene.root = pane
            juego.pause()
        }

        resume.setOnMouseEntered {
            resume.textFill = Color.MEDIUMPURPLE
            resume.cursor = Cursor.HAND
        }

        resume.setOnMouseExited {
            resume.textFill = Color.BLANCHEDALMOND
        }
        var saved = false
        val saveGame = Label("Guardar")
        saveGame.textFill = Color.BLANCHEDALMOND
        saveGame.style = "-fx-font-family: VT323; -fx-font-size: 45;"
        saveGame.setOnMouseClicked {
            saveGame.textFill = Color.PURPLE
            juego.saveGame()
            saved = true
        }
        saveGame.setOnMouseEntered {
            if (!saved){
                saveGame.textFill = Color.MEDIUMPURPLE
                saveGame.cursor = Cursor.HAND
            }
        }

        saveGame.setOnMouseExited {
            if (saved){
                saveGame.textFill = Color.PURPLE
            }
            else{
                saveGame.textFill = Color.BLANCHEDALMOND
            }
        }

        val exitGame = Label("Salir del Juego")
        exitGame.textFill = Color.BLANCHEDALMOND
        exitGame.style = "-fx-font-family: VT323; -fx-font-size: 45;"
        exitGame.setOnMouseClicked {
            juego.printLeaderBoard()
            stop()
        }
        exitGame.setOnMouseEntered {
            exitGame.textFill = Color.MEDIUMPURPLE
            exitGame.cursor = Cursor.HAND
        }

        exitGame.setOnMouseExited {
            exitGame.textFill = Color.BLANCHEDALMOND
        }

        val verticalLayout = VBox(50.0)
        verticalLayout.id = "pause"
        verticalLayout.alignment = Pos.CENTER
        verticalLayout.children.addAll(
            resume,
            saveGame,
            exitGame
        )
        val pause = Scene(verticalLayout)
        pause.stylesheets.add(this::class.java.classLoader.getResource("style.css")?.toString())
        return pause
    }
}

class TimeListener(private val elements: Map<String, ElementModel>, private val container: GameContainer, private val facade: ElementsViewFacade, private val starships: Starships) : EventListener<TimePassed> {
    override fun handle(event: TimePassed) {
        var juego = container.getGame()
        if (juego.hasFinished()){
            juego.printLeaderBoard()
            starships.stop()
        }
        juego = juego.updateView()
        container.setGame(juego)

        val elementsInScreen = juego.state.elements ?: return
        for (element in elementsInScreen){
            val elem = elements[element.id]

            if (elem != null){
                elem.x.set(element.position.x.toDouble())
                elem.y.set(element.position.y.toDouble())
                elem.rotationInDegrees.set(element.rotationInDegrees)
                elem.height.set(element.height)
                elem.width.set(element.width)
            }else facade.elements[element.id] = ElementModel(element.id, element.position.x.toDouble(), element.position.y.toDouble(), element.height, element.width, element.rotationInDegrees, starships.adaptShape(element.collidableShape), starships.getImage(element))
        }
        val deads = juego.deadElements
        for (dead in deads){
            if (elements.containsKey(dead)){
                facade.elements[dead] = null
            }
        }
    }
}

class CollisionListener(private val container: GameContainer) : EventListener<Collision> {
    override fun handle(event: Collision) {
        container.setGame(container.getGame().handleCollision(event.element1Id, event.element2Id))
    }
}

class GameContainer(private var game: Juego) {
    fun getGame(): Juego = game
    fun setGame(game: Juego) {
        this.game = game
    }
}

class KeyPressedListener( private val container: GameContainer, private val starships: Starships, private val primaryStage: Stage, private val pane: StackPane, private val menu: Scene): EventListener<KeyPressed> {
    override fun handle(event: KeyPressed) {
        val juego = container.getGame()
        val map = juego.keyboardConfig()
        if (event.key == KeyCode.S && juego.isPaused) {
            juego.saveGame()
        }
        when(event.key) {
//            map["accelerate-1"] -> game = game.moveShip("starship-1")
//            map["stop-1"] -> game =game.moveShip("starship-1")
//            map["rotate-left-1"] -> game =game.rotate("starship-1", -5)
//            map["rotate-right-1"] -> game =game.rotate("starship-1", 5)
//            map["shoot-1"] -> game =game.shoot("starship-1")
//            map["accelerate-2"] -> game =game.moveShip("starship-2")
//            map["stop-2"] -> game =game.moveShip("starship-2")
//            map["rotate-left-2"] -> game =game.rotate("starship-2", -5)
//            map["rotate-right-2"] -> game =game.rotate("starship-2", 5)
//            map["shoot-2"] -> game =game.shoot("starship-2")

            map["accelerate-1"] -> {
                val next = juego.move("starship-1", Vector(0, -1 ))
                container.setGame(next)
            }
            map["stop-1"] -> {
                val next = juego.move("starship-1", Vector(0, 1))
                container.setGame(next)
            }
            map["left-1"] -> {
                val next = juego.move("starship-1", Vector(-1, 1))
                container.setGame(next)
            }
            map["right-1"] -> {
                val next = juego.move("starship-1", Vector(1, 1))
                container.setGame(next)
            }
            map["rotate-left-1"] -> {
                val next = juego.rotate("starship-1", -5.0)
                container.setGame(next)
            }
            map["rotate-right-1"] -> {
                val next = juego.rotate("starship-1", 5.0)
                container.setGame(next)
            }
            map["shoot-1"] -> {
                val next = juego.shoot("starship-1")
                container.setGame(next)
            }
//            map["rotate-left-1"] -> game = game.rotate("starship-1", -5)
//            map["rotate-right-1"] -> game = game.rotate("starship-1", 5)
//            map["shoot-1"] -> game = game.shoot("starship-1")
//

            map["accelerate-2"] -> {
                val next = juego.move("starship-2", Vector(0, -1))
                container.setGame(next)
            }
            map["stop-2"] -> {
                val next = juego.move("starship-2", Vector(0, 1))
                container.setGame(next)
            }
            map["left-2"] -> {
                val next = juego.move("starship-2", Vector(-1, 1))
                container.setGame(next)
            }
            map["right-2"] -> {
                val next = juego.move("starship-2", Vector(1, 1))
                container.setGame(next)
            }
            map["rotate-left-2"] -> {
                val next = juego.rotate("starship-2", -5.0)
                container.setGame(next)
            }
            map["rotate-right-2"] -> {
                val next = juego.rotate("starship-2", 5.0)
                container.setGame(next)
            }
            map["shoot-2"] -> {
                val next = juego.shoot("starship-2")
                container.setGame(next)
            }



            KeyCode.P -> {
                juego.pause()
                if (juego.isPaused){
                    primaryStage.scene = starships.pauseScene(primaryStage, pane, menu)
                }
            }
            else -> {}
        }
    }

}