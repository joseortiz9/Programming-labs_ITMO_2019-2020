package ru.students.lab.commands.collectionhandlers;

import ru.students.lab.client.IHandlerInput;
import ru.students.lab.commands.ICommand;
import ru.students.lab.managers.CollectionManager;
 /** 
 * Класс для выполнения и получения информации о функции удаления элемента из коллекции по его ключу
 * @autor Хосе Ортис
 * @version 1.0
*/


public class RemoveKeyCommand implements ICommand {

    public static final String DESCRIPTION = "удалить элемент из коллекции по его ключу.\nSyntax: remove_key key";
    private CollectionManager collectionManager;
    /** 
     * Конструктор - создает объект класса clearCommand и экземпляр класса RemoveKeyCommand для последущей работе с коллекцией
     * @see RemoveKeyCommand#RemoveKeyCommand(CollectionManager)
     */
    public RemoveKeyCommand(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(IHandlerInput userInputHandler, String[] args) throws NumberFormatException {

        if (this.collectionManager.removeKey(Integer.valueOf(args[0])) != null)
            userInputHandler.printLn(0,"k:" + args[0] + " Successfully removed!");
        else
            userInputHandler.printLn(1,"The key '" + args[0] + "' doesn't exist");
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
